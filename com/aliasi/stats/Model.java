package com.aliasi.stats;

public abstract interface Model<E>
{
  public abstract double prob(E paramE);

  public abstract double log2Prob(E paramE);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.Model
 * JD-Core Version:    0.6.2
 */