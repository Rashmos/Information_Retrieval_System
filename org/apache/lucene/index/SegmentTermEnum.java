package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.store.InputStream;

final class SegmentTermEnum extends TermEnum
  implements Cloneable
{
  private InputStream input;
  private FieldInfos fieldInfos;
  int size;
  int position = -1;
  private Term term = new Term("", "");
  private TermInfo termInfo = new TermInfo();
  boolean isIndex = false;
  long indexPointer = 0L;
  Term prev;
  private char[] buffer = new char[0];

  SegmentTermEnum(InputStream paramInputStream, FieldInfos paramFieldInfos, boolean paramBoolean)
    throws IOException
  {
    this.input = paramInputStream;
    this.fieldInfos = paramFieldInfos;
    this.size = this.input.readInt();
    this.isIndex = paramBoolean;
  }

  protected Object clone()
  {
    SegmentTermEnum localSegmentTermEnum = null;
    try
    {
      localSegmentTermEnum = (SegmentTermEnum)super.clone();
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
    }
    localSegmentTermEnum.input = ((InputStream)this.input.clone());
    localSegmentTermEnum.termInfo = new TermInfo(this.termInfo);
    if (this.term != null)
      localSegmentTermEnum.growBuffer(this.term.text.length());
    return localSegmentTermEnum;
  }

  final void seek(long paramLong, int paramInt, Term paramTerm, TermInfo paramTermInfo)
    throws IOException
  {
    this.input.seek(paramLong);
    this.position = paramInt;
    this.term = paramTerm;
    this.prev = null;
    this.termInfo.set(paramTermInfo);
    growBuffer(this.term.text.length());
  }

  public final boolean next()
    throws IOException
  {
    if (this.position++ >= this.size - 1)
    {
      this.term = null;
      return false;
    }
    this.prev = this.term;
    this.term = readTerm();
    this.termInfo.docFreq = this.input.readVInt();
    this.termInfo.freqPointer += this.input.readVLong();
    this.termInfo.proxPointer += this.input.readVLong();
    if (this.isIndex)
      this.indexPointer += this.input.readVLong();
    return true;
  }

  private final Term readTerm()
    throws IOException
  {
    int i = this.input.readVInt();
    int j = this.input.readVInt();
    int k = i + j;
    if (this.buffer.length < k)
      growBuffer(k);
    this.input.readChars(this.buffer, i, j);
    return new Term(this.fieldInfos.fieldName(this.input.readVInt()), new String(this.buffer, 0, k), false);
  }

  private final void growBuffer(int paramInt)
  {
    this.buffer = new char[paramInt];
    for (int i = 0; i < this.term.text.length(); i++)
      this.buffer[i] = this.term.text.charAt(i);
  }

  public final Term term()
  {
    return this.term;
  }

  final TermInfo termInfo()
  {
    return new TermInfo(this.termInfo);
  }

  final void termInfo(TermInfo paramTermInfo)
  {
    paramTermInfo.set(this.termInfo);
  }

  public final int docFreq()
  {
    return this.termInfo.docFreq;
  }

  final long freqPointer()
  {
    return this.termInfo.freqPointer;
  }

  final long proxPointer()
  {
    return this.termInfo.proxPointer;
  }

  public final void close()
    throws IOException
  {
    this.input.close();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SegmentTermEnum
 * JD-Core Version:    0.6.2
 */