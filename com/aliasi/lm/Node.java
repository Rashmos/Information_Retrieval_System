package com.aliasi.lm;

import com.aliasi.util.ObjectToCounterMap;
import java.util.LinkedList;
import java.util.List;

abstract interface Node
{
  public abstract long count(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract long count();

  public abstract long contextCount(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract int numOutcomes(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract char[] outcomes(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract long size();

  public abstract Node increment(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract Node increment(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3);

  public abstract Node decrement();

  public abstract Node decrement(int paramInt);

  public abstract Node decrement(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract Node decrement(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3);

  public abstract Node prune(long paramLong);

  public abstract void toString(StringBuilder paramStringBuilder, int paramInt);

  public abstract void addCounts(List<Long> paramList, int paramInt);

  public abstract void topNGrams(NBestCounter paramNBestCounter, char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract void addNGramCounts(long[][] paramArrayOfLong, int paramInt);

  public abstract long uniqueNGramCount(int paramInt);

  public abstract long totalNGramCount(int paramInt);

  public abstract void countNodeTypes(ObjectToCounterMap<String> paramObjectToCounterMap);

  public abstract void addDaughters(LinkedList<Node> paramLinkedList);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.Node
 * JD-Core Version:    0.6.2
 */