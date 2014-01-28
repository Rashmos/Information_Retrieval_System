/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public class ZipfDistribution extends AbstractDiscreteDistribution
/*     */ {
/*     */   private final double[] mEstimates;
/*     */ 
/*     */   public ZipfDistribution(int numOutcomes)
/*     */   {
/*  78 */     if (numOutcomes <= 0) {
/*  79 */       String msg = "Number of outcomes must be postive. Found numOutcomes=" + numOutcomes;
/*     */ 
/*  81 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  83 */     this.mEstimates = zipfDistribution(numOutcomes);
/*     */   }
/*     */ 
/*     */   public long minOutcome()
/*     */   {
/*  93 */     return 1L;
/*     */   }
/*     */ 
/*     */   public long maxOutcome()
/*     */   {
/* 104 */     return this.mEstimates.length;
/*     */   }
/*     */ 
/*     */   public int numOutcomes()
/*     */   {
/* 114 */     return this.mEstimates.length;
/*     */   }
/*     */ 
/*     */   public double probability(long rank)
/*     */   {
/* 127 */     if ((rank <= 0L) || (rank > this.mEstimates.length))
/* 128 */       return 0.0D;
/* 129 */     return this.mEstimates[((int)rank - 1)];
/*     */   }
/*     */ 
/*     */   public static double[] zipfDistribution(int numOutcomes)
/*     */   {
/* 147 */     double[] ratio = new double[numOutcomes];
/* 148 */     for (int i = 1; i <= numOutcomes; i++) {
/* 149 */       ratio[(i - 1)] = (1.0D / i);
/*     */     }
/* 151 */     double sum = 0.0D;
/* 152 */     for (int i = 0; i < ratio.length; i++) {
/* 153 */       sum += ratio[i];
/*     */     }
/* 155 */     for (int i = 0; i < ratio.length; i++)
/* 156 */       ratio[i] /= sum;
/* 157 */     return ratio;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.ZipfDistribution
 * JD-Core Version:    0.6.2
 */