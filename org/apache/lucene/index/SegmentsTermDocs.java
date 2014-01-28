package org.apache.lucene.index;

import java.io.IOException;

class SegmentsTermDocs
  implements TermDocs
{
  protected SegmentReader[] readers;
  protected int[] starts;
  protected Term term;
  protected int base = 0;
  protected int pointer = 0;
  private SegmentTermDocs[] segTermDocs;
  protected SegmentTermDocs current;

  SegmentsTermDocs(SegmentReader[] paramArrayOfSegmentReader, int[] paramArrayOfInt)
  {
    this.readers = paramArrayOfSegmentReader;
    this.starts = paramArrayOfInt;
    this.segTermDocs = new SegmentTermDocs[paramArrayOfSegmentReader.length];
  }

  public final int doc()
  {
    return this.base + this.current.doc;
  }

  public final int freq()
  {
    return this.current.freq;
  }

  public final void seek(Term paramTerm)
  {
    this.term = paramTerm;
    this.base = 0;
    this.pointer = 0;
    this.current = null;
  }

  public final boolean next()
    throws IOException
  {
    if ((this.current != null) && (this.current.next()))
      return true;
    if (this.pointer < this.readers.length)
    {
      this.base = this.starts[this.pointer];
      this.current = termDocs(this.pointer++);
      return next();
    }
    return false;
  }

  public final int read(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws IOException
  {
    int i;
    while (true)
    {
      if (this.pointer < this.readers.length)
      {
        this.base = this.starts[this.pointer];
        this.current = termDocs(this.pointer++);
      }
      else
      {
        return 0;
      }
      while (this.current != null)
      {
        i = this.current.read(paramArrayOfInt1, paramArrayOfInt2);
        if (i != 0)
          break label84;
        this.current = null;
      }
    }
    label84: int j = this.base;
    for (int k = 0; k < i; k++)
      paramArrayOfInt1[k] += j;
    return i;
  }

  public boolean skipTo(int paramInt)
    throws IOException
  {
    do
      if (!next())
        return false;
    while (paramInt > doc());
    return true;
  }

  private SegmentTermDocs termDocs(int paramInt)
    throws IOException
  {
    if (this.term == null)
      return null;
    SegmentTermDocs localSegmentTermDocs = this.segTermDocs[paramInt];
    if (localSegmentTermDocs == null)
      localSegmentTermDocs = this.segTermDocs[paramInt] =  = termDocs(this.readers[paramInt]);
    localSegmentTermDocs.seek(this.term);
    return localSegmentTermDocs;
  }

  protected SegmentTermDocs termDocs(SegmentReader paramSegmentReader)
    throws IOException
  {
    return (SegmentTermDocs)paramSegmentReader.termDocs();
  }

  public final void close()
    throws IOException
  {
    for (int i = 0; i < this.segTermDocs.length; i++)
      if (this.segTermDocs[i] != null)
        this.segTermDocs[i].close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentsTermDocs
 * JD-Core Version:    0.6.2
 */