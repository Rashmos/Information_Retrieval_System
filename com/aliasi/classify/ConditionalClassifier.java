package com.aliasi.classify;

public abstract interface ConditionalClassifier<E> extends ScoredClassifier<E>
{
  public abstract ConditionalClassification classify(E paramE);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ConditionalClassifier
 * JD-Core Version:    0.6.2
 */