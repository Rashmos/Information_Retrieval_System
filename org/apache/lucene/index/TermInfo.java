package org.apache.lucene.index;

final class TermInfo
{
  int docFreq = 0;
  long freqPointer = 0L;
  long proxPointer = 0L;

  TermInfo()
  {
  }

  TermInfo(int paramInt, long paramLong1, long paramLong2)
  {
    this.docFreq = paramInt;
    this.freqPointer = paramLong1;
    this.proxPointer = paramLong2;
  }

  TermInfo(TermInfo paramTermInfo)
  {
    this.docFreq = paramTermInfo.docFreq;
    this.freqPointer = paramTermInfo.freqPointer;
    this.proxPointer = paramTermInfo.proxPointer;
  }

  final void set(int paramInt, long paramLong1, long paramLong2)
  {
    this.docFreq = paramInt;
    this.freqPointer = paramLong1;
    this.proxPointer = paramLong2;
  }

  final void set(TermInfo paramTermInfo)
  {
    this.docFreq = paramTermInfo.docFreq;
    this.freqPointer = paramTermInfo.freqPointer;
    this.proxPointer = paramTermInfo.proxPointer;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermInfo
 * JD-Core Version:    0.6.2
 */