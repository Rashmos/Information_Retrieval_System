package org.apache.lucene.index;

import java.io.IOException;
import java.util.Hashtable;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;

final class SegmentsReader extends IndexReader
{
  protected SegmentReader[] readers;
  protected int[] starts;
  private Hashtable normsCache = new Hashtable();
  private int maxDoc = 0;
  private int numDocs = -1;

  SegmentsReader(Directory paramDirectory, SegmentReader[] paramArrayOfSegmentReader)
    throws IOException
  {
    super(paramDirectory);
    this.readers = paramArrayOfSegmentReader;
    this.starts = new int[this.readers.length + 1];
    for (int i = 0; i < this.readers.length; i++)
    {
      this.starts[i] = this.maxDoc;
      this.maxDoc += this.readers[i].maxDoc();
    }
    this.starts[this.readers.length] = this.maxDoc;
  }

  public final synchronized int numDocs()
  {
    if (this.numDocs == -1)
    {
      int i = 0;
      for (int j = 0; j < this.readers.length; j++)
        i += this.readers[j].numDocs();
      this.numDocs = i;
    }
    return this.numDocs;
  }

  public final int maxDoc()
  {
    return this.maxDoc;
  }

  public final Document document(int paramInt)
    throws IOException
  {
    int i = readerIndex(paramInt);
    return this.readers[i].document(paramInt - this.starts[i]);
  }

  public final boolean isDeleted(int paramInt)
  {
    int i = readerIndex(paramInt);
    return this.readers[i].isDeleted(paramInt - this.starts[i]);
  }

  final synchronized void doDelete(int paramInt)
    throws IOException
  {
    this.numDocs = -1;
    int i = readerIndex(paramInt);
    this.readers[i].doDelete(paramInt - this.starts[i]);
  }

  private final int readerIndex(int paramInt)
  {
    int i = 0;
    int j = this.readers.length - 1;
    while (j >= i)
    {
      int k = i + j >> 1;
      int m = this.starts[k];
      if (paramInt < m)
        j = k - 1;
      else if (paramInt > m)
        i = k + 1;
      else
        return k;
    }
    return j;
  }

  public final synchronized byte[] norms(String paramString)
    throws IOException
  {
    byte[] arrayOfByte = (byte[])this.normsCache.get(paramString);
    if (arrayOfByte != null)
      return arrayOfByte;
    arrayOfByte = new byte[maxDoc()];
    for (int i = 0; i < this.readers.length; i++)
      this.readers[i].norms(paramString, arrayOfByte, this.starts[i]);
    this.normsCache.put(paramString, arrayOfByte);
    return arrayOfByte;
  }

  public final TermEnum terms()
    throws IOException
  {
    return new SegmentsTermEnum(this.readers, this.starts, null);
  }

  public final TermEnum terms(Term paramTerm)
    throws IOException
  {
    return new SegmentsTermEnum(this.readers, this.starts, paramTerm);
  }

  public final int docFreq(Term paramTerm)
    throws IOException
  {
    int i = 0;
    for (int j = 0; j < this.readers.length; j++)
      i += this.readers[j].docFreq(paramTerm);
    return i;
  }

  public final TermDocs termDocs()
    throws IOException
  {
    return new SegmentsTermDocs(this.readers, this.starts);
  }

  public final TermPositions termPositions()
    throws IOException
  {
    return new SegmentsTermPositions(this.readers, this.starts);
  }

  final synchronized void doClose()
    throws IOException
  {
    for (int i = 0; i < this.readers.length; i++)
      this.readers[i].close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentsReader
 * JD-Core Version:    0.6.2
 */