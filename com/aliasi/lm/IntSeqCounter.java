package com.aliasi.lm;

public abstract interface IntSeqCounter
{
  public abstract int count(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract long extensionCount(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract int numExtensions(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract int[] integersFollowing(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract int[] observedIntegers();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.IntSeqCounter
 * JD-Core Version:    0.6.2
 */