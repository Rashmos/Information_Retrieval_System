package org.apache.lucene.index;

public abstract interface TermPositionVector extends TermFreqVector
{
  public abstract int[] getTermPositions(int paramInt);

  public abstract TermVectorOffsetInfo[] getOffsets(int paramInt);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermPositionVector
 * JD-Core Version:    0.6.2
 */