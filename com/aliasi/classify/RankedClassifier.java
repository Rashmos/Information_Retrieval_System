package com.aliasi.classify;

public abstract interface RankedClassifier<E> extends BaseClassifier<E>
{
  public abstract RankedClassification classify(E paramE);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.RankedClassifier
 * JD-Core Version:    0.6.2
 */