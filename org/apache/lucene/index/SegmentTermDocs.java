package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.util.BitVector;

class SegmentTermDocs
  implements TermDocs
{
  protected SegmentReader parent;
  private InputStream freqStream;
  private int freqCount;
  private BitVector deletedDocs;
  int doc = 0;
  int freq;

  SegmentTermDocs(SegmentReader paramSegmentReader)
    throws IOException
  {
    this.parent = paramSegmentReader;
    this.freqStream = ((InputStream)paramSegmentReader.freqStream.clone());
    this.deletedDocs = paramSegmentReader.deletedDocs;
  }

  public void seek(Term paramTerm)
    throws IOException
  {
    TermInfo localTermInfo = this.parent.tis.get(paramTerm);
    seek(localTermInfo);
  }

  void seek(TermInfo paramTermInfo)
    throws IOException
  {
    if (paramTermInfo == null)
    {
      this.freqCount = 0;
    }
    else
    {
      this.freqCount = paramTermInfo.docFreq;
      this.doc = 0;
      this.freqStream.seek(paramTermInfo.freqPointer);
    }
  }

  public void close()
    throws IOException
  {
    this.freqStream.close();
  }

  public final int doc()
  {
    return this.doc;
  }

  public final int freq()
  {
    return this.freq;
  }

  protected void skippingDoc()
    throws IOException
  {
  }

  public boolean next()
    throws IOException
  {
    while (true)
    {
      if (this.freqCount == 0)
        return false;
      int i = this.freqStream.readVInt();
      this.doc += (i >>> 1);
      if ((i & 0x1) != 0)
        this.freq = 1;
      else
        this.freq = this.freqStream.readVInt();
      this.freqCount -= 1;
      if ((this.deletedDocs == null) || (!this.deletedDocs.get(this.doc)))
        break;
      skippingDoc();
    }
    return true;
  }

  public int read(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws IOException
  {
    int i = paramArrayOfInt1.length;
    label107: for (int j = 0; (j < i) && (this.freqCount > 0); j++)
    {
      int k = this.freqStream.readVInt();
      this.doc += (k >>> 1);
      if ((k & 0x1) != 0)
        this.freq = 1;
      else
        this.freq = this.freqStream.readVInt();
      this.freqCount -= 1;
      if ((this.deletedDocs != null) && (this.deletedDocs.get(this.doc)))
        break label107;
      paramArrayOfInt1[j] = this.doc;
      paramArrayOfInt2[j] = this.freq;
    }
    return j;
  }

  public boolean skipTo(int paramInt)
    throws IOException
  {
    do
      if (!next())
        return false;
    while (paramInt > this.doc);
    return true;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentTermDocs
 * JD-Core Version:    0.6.2
 */