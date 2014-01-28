package com.aliasi.stats;

public abstract interface DiscreteDistribution
{
  public abstract double probability(long paramLong);

  public abstract double log2Probability(long paramLong);

  public abstract double cumulativeProbabilityLess(long paramLong);

  public abstract double cumulativeProbabilityGreater(long paramLong);

  public abstract double cumulativeProbability(long paramLong1, long paramLong2);

  public abstract long minOutcome();

  public abstract long maxOutcome();

  public abstract double mean();

  public abstract double variance();

  public abstract double entropy();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.DiscreteDistribution
 * JD-Core Version:    0.6.2
 */