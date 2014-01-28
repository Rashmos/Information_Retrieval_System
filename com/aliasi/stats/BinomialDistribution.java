/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public class BinomialDistribution extends AbstractDiscreteDistribution
/*     */ {
/*     */   private final BernoulliDistribution mBernoulliDistribution;
/*     */   private final int mNumTrials;
/*     */ 
/*     */   public BinomialDistribution(BernoulliDistribution distribution, int numTrials)
/*     */   {
/* 135 */     if (numTrials < 0) {
/* 136 */       String msg = "Number of trials must be non-negative. Found num trials=" + numTrials;
/*     */ 
/* 138 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 140 */     this.mBernoulliDistribution = distribution;
/* 141 */     this.mNumTrials = numTrials;
/*     */   }
/*     */ 
/*     */   public BernoulliDistribution bernoulliDistribution()
/*     */   {
/* 151 */     return this.mBernoulliDistribution;
/*     */   }
/*     */ 
/*     */   public long minOutcome()
/*     */   {
/* 162 */     return 0L;
/*     */   }
/*     */ 
/*     */   public long maxOutcome()
/*     */   {
/* 173 */     return this.mNumTrials;
/*     */   }
/*     */ 
/*     */   public long numTrials()
/*     */   {
/* 183 */     return this.mNumTrials;
/*     */   }
/*     */ 
/*     */   public double probability(long outcome)
/*     */   {
/* 214 */     return java.lang.Math.pow(2.0D, log2Probability(outcome));
/*     */   }
/*     */ 
/*     */   public double log2Probability(long outcome)
/*     */   {
/* 230 */     if ((outcome < 0L) || (outcome > maxOutcome()))
/* 231 */       return (-1.0D / 0.0D);
/* 232 */     return log2BinomialCoefficient(this.mNumTrials, outcome) + outcome * this.mBernoulliDistribution.log2Probability(1L) + (this.mNumTrials - outcome) * this.mBernoulliDistribution.log2Probability(0L);
/*     */   }
/*     */ 
/*     */   public double z(int numSuccesses)
/*     */   {
/* 263 */     return z(this.mBernoulliDistribution.successProbability(), numSuccesses, this.mNumTrials);
/*     */   }
/*     */ 
/*     */   public double variance()
/*     */   {
/* 281 */     double successProb = this.mBernoulliDistribution.successProbability();
/* 282 */     return successProb * (1.0D - successProb) * this.mNumTrials;
/*     */   }
/*     */ 
/*     */   public static double z(double successProbability, int numSuccesses, int numTrials)
/*     */   {
/* 322 */     if ((successProbability < 0.0D) || (successProbability > 1.0D) || (Double.isNaN(successProbability)))
/*     */     {
/* 325 */       String msg = "Require probability between 0 and 1 for success. Found success probability=" + successProbability;
/*     */ 
/* 327 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 329 */     if ((numSuccesses < 0) || (numSuccesses > numTrials)) {
/* 330 */       String msg = "Require 0 <= num successes <= num trials Found num successes= " + numSuccesses + " num successes=" + numTrials;
/*     */ 
/* 333 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 335 */     double numTrialsD = numTrials;
/* 336 */     double numSuccessesD = numSuccesses;
/* 337 */     double expectedSuccesses = successProbability * numTrialsD;
/* 338 */     return (numSuccessesD - expectedSuccesses) / java.lang.Math.sqrt(numTrialsD * successProbability * (1.0D - successProbability));
/*     */   }
/*     */ 
/*     */   public static double log2BinomialCoefficient(long n, long m)
/*     */   {
/* 364 */     if (n < m) {
/* 365 */       String msg = "Require n > m for binomial coefficient. Found n= " + n + " m = " + m;
/*     */ 
/* 368 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 370 */     return com.aliasi.util.Math.log2Factorial(n) - com.aliasi.util.Math.log2Factorial(m) - com.aliasi.util.Math.log2Factorial(n - m);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.BinomialDistribution
 * JD-Core Version:    0.6.2
 */