/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public abstract class PoissonDistribution extends AbstractDiscreteDistribution
/*     */ {
/*     */   public abstract double mean();
/*     */ 
/*     */   public double variance()
/*     */   {
/* 111 */     return mean();
/*     */   }
/*     */ 
/*     */   public long minOutcome()
/*     */   {
/* 122 */     return 0L;
/*     */   }
/*     */ 
/*     */   public final double log2Probability(long outcome)
/*     */   {
/* 140 */     return log2Poisson(mean(), outcome);
/*     */   }
/*     */ 
/*     */   public final double probability(long outcome)
/*     */   {
/* 156 */     return java.lang.Math.pow(2.0D, log2Probability(outcome));
/*     */   }
/*     */ 
/*     */   private static double log2Poisson(double lambda, long k) {
/* 160 */     if ((lambda <= 0.0D) || (Double.isInfinite(lambda))) {
/* 161 */       String msg = "Mean must be a positive non-infiite value. Found mean=" + lambda;
/*     */ 
/* 163 */       throw new IllegalStateException(msg);
/*     */     }
/* 165 */     if (k < 0L) return (-1.0D / 0.0D);
/* 166 */     return -lambda * com.aliasi.util.Math.LOG2_E + k * com.aliasi.util.Math.log2(lambda) - com.aliasi.util.Math.log2Factorial(k);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.PoissonDistribution
 * JD-Core Version:    0.6.2
 */