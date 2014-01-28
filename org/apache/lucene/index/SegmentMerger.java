package org.apache.lucene.index;

import java.io.IOException;
import java.util.Vector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.OutputStream;
import org.apache.lucene.util.BitVector;
import org.apache.lucene.util.PriorityQueue;

final class SegmentMerger
{
  private Directory directory;
  private String segment;
  private Vector readers = new Vector();
  private FieldInfos fieldInfos;
  private OutputStream freqOutput = null;
  private OutputStream proxOutput = null;
  private TermInfosWriter termInfosWriter = null;
  private SegmentMergeQueue queue = null;
  private final TermInfo termInfo = new TermInfo();

  SegmentMerger(Directory paramDirectory, String paramString)
  {
    this.directory = paramDirectory;
    this.segment = paramString;
  }

  final void add(SegmentReader paramSegmentReader)
  {
    this.readers.addElement(paramSegmentReader);
  }

  final SegmentReader segmentReader(int paramInt)
  {
    return (SegmentReader)this.readers.elementAt(paramInt);
  }

  final void merge()
    throws IOException
  {
    try
    {
      mergeFields();
      mergeTerms();
      mergeNorms();
    }
    finally
    {
      for (int i = 0; i < this.readers.size(); i++)
      {
        SegmentReader localSegmentReader = (SegmentReader)this.readers.elementAt(i);
        localSegmentReader.close();
      }
    }
  }

  private final void mergeFields()
    throws IOException
  {
    this.fieldInfos = new FieldInfos();
    for (int i = 0; i < this.readers.size(); i++)
    {
      localObject1 = (SegmentReader)this.readers.elementAt(i);
      this.fieldInfos.add(((SegmentReader)localObject1).fieldInfos);
    }
    this.fieldInfos.write(this.directory, this.segment + ".fnm");
    Object localObject1 = new FieldsWriter(this.directory, this.segment, this.fieldInfos);
    try
    {
      for (int j = 0; j < this.readers.size(); j++)
      {
        SegmentReader localSegmentReader = (SegmentReader)this.readers.elementAt(j);
        BitVector localBitVector = localSegmentReader.deletedDocs;
        int k = localSegmentReader.maxDoc();
        for (int m = 0; m < k; m++)
          if ((localBitVector == null) || (!localBitVector.get(m)))
            ((FieldsWriter)localObject1).addDocument(localSegmentReader.document(m));
      }
    }
    finally
    {
      ((FieldsWriter)localObject1).close();
    }
  }

  private final void mergeTerms()
    throws IOException
  {
    try
    {
      this.freqOutput = this.directory.createFile(this.segment + ".frq");
      this.proxOutput = this.directory.createFile(this.segment + ".prx");
      this.termInfosWriter = new TermInfosWriter(this.directory, this.segment, this.fieldInfos);
      mergeTermInfos();
    }
    finally
    {
      if (this.freqOutput != null)
        this.freqOutput.close();
      if (this.proxOutput != null)
        this.proxOutput.close();
      if (this.termInfosWriter != null)
        this.termInfosWriter.close();
      if (this.queue != null)
        this.queue.close();
    }
  }

  private final void mergeTermInfos()
    throws IOException
  {
    this.queue = new SegmentMergeQueue(this.readers.size());
    int i = 0;
    Object localObject2;
    for (int j = 0; j < this.readers.size(); j++)
    {
      localObject1 = (SegmentReader)this.readers.elementAt(j);
      SegmentTermEnum localSegmentTermEnum = (SegmentTermEnum)((SegmentReader)localObject1).terms();
      localObject2 = new SegmentMergeInfo(i, localSegmentTermEnum, (SegmentReader)localObject1);
      i += ((SegmentReader)localObject1).numDocs();
      if (((SegmentMergeInfo)localObject2).next())
        this.queue.put(localObject2);
      else
        ((SegmentMergeInfo)localObject2).close();
    }
    Object localObject1 = new SegmentMergeInfo[this.readers.size()];
    while (this.queue.size() > 0)
    {
      int k = 0;
      localObject1[(k++)] = ((SegmentMergeInfo)this.queue.pop());
      localObject2 = localObject1[0].term;
      for (SegmentMergeInfo localSegmentMergeInfo = (SegmentMergeInfo)this.queue.top(); (localSegmentMergeInfo != null) && (((Term)localObject2).compareTo(localSegmentMergeInfo.term) == 0); localSegmentMergeInfo = (SegmentMergeInfo)this.queue.top())
        localObject1[(k++)] = ((SegmentMergeInfo)this.queue.pop());
      mergeTermInfo((SegmentMergeInfo[])localObject1, k);
      while (k > 0)
      {
        Object localObject3 = localObject1[(--k)];
        if (localObject3.next())
          this.queue.put(localObject3);
        else
          localObject3.close();
      }
    }
  }

  private final void mergeTermInfo(SegmentMergeInfo[] paramArrayOfSegmentMergeInfo, int paramInt)
    throws IOException
  {
    long l1 = this.freqOutput.getFilePointer();
    long l2 = this.proxOutput.getFilePointer();
    int i = appendPostings(paramArrayOfSegmentMergeInfo, paramInt);
    if (i > 0)
    {
      this.termInfo.set(i, l1, l2);
      this.termInfosWriter.add(paramArrayOfSegmentMergeInfo[0].term, this.termInfo);
    }
  }

  private final int appendPostings(SegmentMergeInfo[] paramArrayOfSegmentMergeInfo, int paramInt)
    throws IOException
  {
    int i = 0;
    int j = 0;
    for (int k = 0; k < paramInt; k++)
    {
      SegmentMergeInfo localSegmentMergeInfo = paramArrayOfSegmentMergeInfo[k];
      SegmentTermPositions localSegmentTermPositions = localSegmentMergeInfo.postings;
      int m = localSegmentMergeInfo.base;
      int[] arrayOfInt = localSegmentMergeInfo.docMap;
      localSegmentMergeInfo.termEnum.termInfo(this.termInfo);
      localSegmentTermPositions.seek(this.termInfo);
      while (localSegmentTermPositions.next())
      {
        int n;
        if (arrayOfInt == null)
          n = m + localSegmentTermPositions.doc;
        else
          n = m + arrayOfInt[localSegmentTermPositions.doc];
        if (n < i)
          throw new IllegalStateException("docs out of order");
        int i1 = n - i << 1;
        i = n;
        int i2 = localSegmentTermPositions.freq;
        if (i2 == 1)
        {
          this.freqOutput.writeVInt(i1 | 0x1);
        }
        else
        {
          this.freqOutput.writeVInt(i1);
          this.freqOutput.writeVInt(i2);
        }
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++)
        {
          int i5 = localSegmentTermPositions.nextPosition();
          this.proxOutput.writeVInt(i5 - i3);
          i3 = i5;
        }
        j++;
      }
    }
    return j;
  }

  private final void mergeNorms()
    throws IOException
  {
    for (int i = 0; i < this.fieldInfos.size(); i++)
    {
      FieldInfo localFieldInfo = this.fieldInfos.fieldInfo(i);
      if (localFieldInfo.isIndexed)
      {
        OutputStream localOutputStream = this.directory.createFile(this.segment + ".f" + i);
        try
        {
          for (int j = 0; j < this.readers.size(); j++)
          {
            SegmentReader localSegmentReader = (SegmentReader)this.readers.elementAt(j);
            BitVector localBitVector = localSegmentReader.deletedDocs;
            InputStream localInputStream = localSegmentReader.normStream(localFieldInfo.name);
            int k = localSegmentReader.maxDoc();
            try
            {
              for (int m = 0; m < k; m++)
              {
                byte b = localInputStream != null ? localInputStream.readByte() : 0;
                if ((localBitVector == null) || (!localBitVector.get(m)))
                  localOutputStream.writeByte(b);
              }
            }
            finally
            {
              if (localInputStream != null)
                localInputStream.close();
            }
          }
        }
        finally
        {
          localOutputStream.close();
        }
      }
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentMerger
 * JD-Core Version:    0.6.2
 */