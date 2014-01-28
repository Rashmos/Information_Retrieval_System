package com.aliasi.lm;

import com.aliasi.symbol.SymbolTable;
import java.util.List;

abstract interface DtrMap
{
  public abstract int numExtensions();

  public abstract long extensionCount();

  public abstract void addDtrs(List<IntNode> paramList);

  public abstract int[] integersFollowing();

  public abstract IntNode getDtr(int paramInt);

  public abstract int dtrsTrieSize();

  public abstract void toString(StringBuilder paramStringBuilder, int paramInt, SymbolTable paramSymbolTable);

  public abstract DtrMap rescale(double paramDouble);

  public abstract DtrMap prune(int paramInt);

  public abstract DtrMap incrementDtrs(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract DtrMap incrementDtrs(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3);

  public abstract DtrMap incrementSequence(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.DtrMap
 * JD-Core Version:    0.6.2
 */