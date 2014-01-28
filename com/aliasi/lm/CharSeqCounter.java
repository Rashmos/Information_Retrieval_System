package com.aliasi.lm;

public abstract interface CharSeqCounter
{
  public abstract long count(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract long extensionCount(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract int numCharactersFollowing(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract char[] charactersFollowing(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract char[] observedCharacters();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.CharSeqCounter
 * JD-Core Version:    0.6.2
 */