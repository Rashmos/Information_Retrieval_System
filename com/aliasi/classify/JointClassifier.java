package com.aliasi.classify;

public abstract interface JointClassifier<E> extends ConditionalClassifier<E>
{
  public abstract JointClassification classify(E paramE);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.JointClassifier
 * JD-Core Version:    0.6.2
 */