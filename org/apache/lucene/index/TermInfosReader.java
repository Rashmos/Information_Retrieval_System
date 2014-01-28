package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.store.Directory;

final class TermInfosReader
{
  private Directory directory;
  private String segment;
  private FieldInfos fieldInfos;
  private SegmentTermEnum jdField_enum;
  private int size;
  Term[] indexTerms = null;
  TermInfo[] indexInfos;
  long[] indexPointers;

  TermInfosReader(Directory paramDirectory, String paramString, FieldInfos paramFieldInfos)
    throws IOException
  {
    this.directory = paramDirectory;
    this.segment = paramString;
    this.fieldInfos = paramFieldInfos;
    this.jdField_enum = new SegmentTermEnum(this.directory.openFile(this.segment + ".tis"), this.fieldInfos, false);
    this.size = this.jdField_enum.size;
    readIndex();
  }

  final void close()
    throws IOException
  {
    if (this.jdField_enum != null)
      this.jdField_enum.close();
  }

  final int size()
  {
    return this.size;
  }

  private final void readIndex()
    throws IOException
  {
    SegmentTermEnum localSegmentTermEnum = new SegmentTermEnum(this.directory.openFile(this.segment + ".tii"), this.fieldInfos, true);
    try
    {
      int i = localSegmentTermEnum.size;
      this.indexTerms = new Term[i];
      this.indexInfos = new TermInfo[i];
      this.indexPointers = new long[i];
      for (int j = 0; localSegmentTermEnum.next(); j++)
      {
        this.indexTerms[j] = localSegmentTermEnum.term();
        this.indexInfos[j] = localSegmentTermEnum.termInfo();
        this.indexPointers[j] = localSegmentTermEnum.indexPointer;
      }
    }
    finally
    {
      localSegmentTermEnum.close();
    }
  }

  private final int getIndexOffset(Term paramTerm)
    throws IOException
  {
    int i = 0;
    int j = this.indexTerms.length - 1;
    while (j >= i)
    {
      int k = i + j >> 1;
      int m = paramTerm.compareTo(this.indexTerms[k]);
      if (m < 0)
        j = k - 1;
      else if (m > 0)
        i = k + 1;
      else
        return k;
    }
    return j;
  }

  private final void seekEnum(int paramInt)
    throws IOException
  {
    this.jdField_enum.seek(this.indexPointers[paramInt], paramInt * 128 - 1, this.indexTerms[paramInt], this.indexInfos[paramInt]);
  }

  final synchronized TermInfo get(Term paramTerm)
    throws IOException
  {
    if (this.size == 0)
      return null;
    if ((this.jdField_enum.term() != null) && (((this.jdField_enum.prev != null) && (paramTerm.compareTo(this.jdField_enum.prev) > 0)) || (paramTerm.compareTo(this.jdField_enum.term()) >= 0)))
    {
      int i = this.jdField_enum.position / 128 + 1;
      if ((this.indexTerms.length == i) || (paramTerm.compareTo(this.indexTerms[i]) < 0))
        return scanEnum(paramTerm);
    }
    seekEnum(getIndexOffset(paramTerm));
    return scanEnum(paramTerm);
  }

  private final TermInfo scanEnum(Term paramTerm)
    throws IOException
  {
    while ((paramTerm.compareTo(this.jdField_enum.term()) > 0) && (this.jdField_enum.next()));
    if ((this.jdField_enum.term() != null) && (paramTerm.compareTo(this.jdField_enum.term()) == 0))
      return this.jdField_enum.termInfo();
    return null;
  }

  final synchronized Term get(int paramInt)
    throws IOException
  {
    if (this.size == 0)
      return null;
    if ((this.jdField_enum != null) && (this.jdField_enum.term() != null) && (paramInt >= this.jdField_enum.position) && (paramInt < this.jdField_enum.position + 128))
      return scanEnum(paramInt);
    seekEnum(paramInt / 128);
    return scanEnum(paramInt);
  }

  private final Term scanEnum(int paramInt)
    throws IOException
  {
    while (this.jdField_enum.position < paramInt)
      if (!this.jdField_enum.next())
        return null;
    return this.jdField_enum.term();
  }

  final synchronized int getPosition(Term paramTerm)
    throws IOException
  {
    if (this.size == 0)
      return -1;
    int i = getIndexOffset(paramTerm);
    seekEnum(i);
    while ((paramTerm.compareTo(this.jdField_enum.term()) > 0) && (this.jdField_enum.next()));
    if (paramTerm.compareTo(this.jdField_enum.term()) == 0)
      return this.jdField_enum.position;
    return -1;
  }

  final synchronized SegmentTermEnum terms()
    throws IOException
  {
    if (this.jdField_enum.position != -1)
      seekEnum(0);
    return (SegmentTermEnum)this.jdField_enum.clone();
  }

  final synchronized SegmentTermEnum terms(Term paramTerm)
    throws IOException
  {
    get(paramTerm);
    return (SegmentTermEnum)this.jdField_enum.clone();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermInfosReader
 * JD-Core Version:    0.6.2
 */