/*     */ package com.aliasi.stats;
/*     */ 
/*     */ import com.aliasi.util.Math;
/*     */ 
/*     */ public class MultinomialDistribution
/*     */ {
/*     */   private final MultivariateDistribution mBasisDistribution;
/*     */ 
/*     */   public MultinomialDistribution(MultivariateDistribution distribution)
/*     */   {
/*  95 */     this.mBasisDistribution = distribution;
/*     */   }
/*     */ 
/*     */   public double log2Probability(int[] sampleCounts)
/*     */   {
/* 142 */     checkNumSamples(sampleCounts);
/* 143 */     double sum = log2MultinomialCoefficient(sampleCounts);
/* 144 */     for (int i = 0; i < sampleCounts.length; i++) {
/* 145 */       sum += sampleCounts[i] * this.mBasisDistribution.log2Probability(i);
/*     */     }
/* 147 */     return sum;
/*     */   }
/*     */ 
/*     */   public double chiSquared(int[] sampleCounts)
/*     */   {
/* 191 */     checkNumSamples(sampleCounts);
/* 192 */     int totalCount = Math.sum(sampleCounts);
/* 193 */     double sum = 0.0D;
/* 194 */     for (int i = 0; i < sampleCounts.length; i++) {
/* 195 */       sum += normSquareDiff(sampleCounts[i], this.mBasisDistribution.probability(i), totalCount);
/*     */     }
/*     */ 
/* 198 */     return sum;
/*     */   }
/*     */ 
/*     */   public int numDimensions()
/*     */   {
/* 210 */     return this.mBasisDistribution.numDimensions();
/*     */   }
/*     */ 
/*     */   public MultivariateDistribution basisDistribution()
/*     */   {
/* 221 */     return this.mBasisDistribution;
/*     */   }
/*     */ 
/*     */   void checkNumSamples(int[] samples) {
/* 225 */     if (samples.length != numDimensions()) {
/* 226 */       String msg = "Require same number of samples as dimensions. Number of dimensions=" + numDimensions() + "  Found #samples=" + samples.length;
/*     */ 
/* 229 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static double log2MultinomialCoefficient(int[] sampleCounts)
/*     */   {
/* 264 */     checkNonNegative(sampleCounts);
/* 265 */     long totalCount = Math.sum(sampleCounts);
/* 266 */     double coeff = Math.log2Factorial(totalCount);
/* 267 */     for (int i = 0; i < sampleCounts.length; i++)
/* 268 */       coeff -= Math.log2Factorial(sampleCounts[i]);
/* 269 */     return coeff;
/*     */   }
/*     */ 
/*     */   static double normSquareDiff(double count, double probability, double totalCount)
/*     */   {
/* 274 */     double expectedCount = totalCount * probability;
/* 275 */     double diff = count - expectedCount;
/* 276 */     return diff * diff / expectedCount;
/*     */   }
/*     */ 
/*     */   static void checkNonNegative(int[] sampleCounts) {
/* 280 */     for (int i = 0; i < sampleCounts.length; i++)
/* 281 */       if (sampleCounts[i] < 0) {
/* 282 */         String msg = "Sample Counts must be non-negative. Found sampleCounts[" + i + "]=" + sampleCounts[i];
/*     */ 
/* 284 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.MultinomialDistribution
 * JD-Core Version:    0.6.2
 */