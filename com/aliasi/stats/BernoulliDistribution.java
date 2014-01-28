/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public abstract class BernoulliDistribution extends MultivariateDistribution
/*     */ {
/*     */   public static final String FAILURE_LABEL = "failure";
/*     */   public static final String SUCCESS_LABEL = "success";
/*     */ 
/*     */   public long maxOutcome()
/*     */   {
/*  53 */     return 1L;
/*     */   }
/*     */ 
/*     */   public int numDimensions()
/*     */   {
/*  66 */     return 2;
/*     */   }
/*     */ 
/*     */   public double variance()
/*     */   {
/*  82 */     double successProb = successProbability();
/*  83 */     return successProb * (1.0D - successProb);
/*     */   }
/*     */ 
/*     */   public double probability(long outcome)
/*     */   {
/*  94 */     if (outcome == 0L)
/*  95 */       return 1.0D - successProbability();
/*  96 */     if (outcome == 1L)
/*  97 */       return successProbability();
/*  98 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public String label(long outcome)
/*     */   {
/* 114 */     if (outcome == 0L) return "failure";
/* 115 */     if (outcome == 1L) return "success";
/* 116 */     String msg = "Only outcomes 0 and 1 have labels. Found outcome=" + outcome;
/*     */ 
/* 118 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   public abstract double successProbability();
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.BernoulliDistribution
 * JD-Core Version:    0.6.2
 */