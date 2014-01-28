/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public abstract class MultivariateDistribution extends AbstractDiscreteDistribution
/*     */ {
/*     */   public long minOutcome()
/*     */   {
/*  69 */     return 0L;
/*     */   }
/*     */ 
/*     */   public long maxOutcome()
/*     */   {
/*  82 */     return numDimensions() - 1;
/*     */   }
/*     */ 
/*     */   public long outcome(String label)
/*     */   {
/*     */     try
/*     */     {
/*  96 */       long outcome = Long.valueOf(label).longValue();
/*  97 */       if (outcomeOutOfRange(outcome))
/*  98 */         return -1L;
/*  99 */       return outcome; } catch (NumberFormatException e) {
/*     */     }
/* 101 */     return -1L;
/*     */   }
/*     */ 
/*     */   public String label(long outcome)
/*     */   {
/* 115 */     checkOutcome(outcome);
/* 116 */     return Long.toString(outcome);
/*     */   }
/*     */ 
/*     */   public double probability(String label)
/*     */   {
/* 128 */     return probability(outcome(label));
/*     */   }
/*     */ 
/*     */   public double log2Probability(String label)
/*     */   {
/* 141 */     return log2Probability(outcome(label));
/*     */   }
/*     */ 
/*     */   public abstract int numDimensions();
/*     */ 
/*     */   public abstract double probability(long paramLong);
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.MultivariateDistribution
 * JD-Core Version:    0.6.2
 */