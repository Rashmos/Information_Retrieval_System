package com.aliasi.classify;

public abstract interface ScoredClassifier<E> extends RankedClassifier<E>
{
  public abstract ScoredClassification classify(E paramE);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ScoredClassifier
 * JD-Core Version:    0.6.2
 */