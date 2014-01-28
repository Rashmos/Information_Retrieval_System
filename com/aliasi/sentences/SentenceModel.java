package com.aliasi.sentences;

import java.util.Collection;

public abstract interface SentenceModel
{
  public abstract int[] boundaryIndices(String[] paramArrayOfString1, String[] paramArrayOfString2);

  public abstract void boundaryIndices(String[] paramArrayOfString1, String[] paramArrayOfString2, int paramInt1, int paramInt2, Collection<Integer> paramCollection);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.sentences.SentenceModel
 * JD-Core Version:    0.6.2
 */