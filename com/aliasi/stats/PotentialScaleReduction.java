/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public class PotentialScaleReduction
/*     */ {
/*     */   private final OnlineNormalEstimator mGlobalEstimator;
/*     */   private final OnlineNormalEstimator[] mChainEstimators;
/*     */ 
/*     */   public PotentialScaleReduction(int numChains)
/*     */   {
/* 135 */     if (numChains < 2) {
/* 136 */       String msg = "Need at least two chains. Found numChains=" + numChains;
/*     */ 
/* 138 */       throw new IllegalStateException(msg);
/*     */     }
/* 140 */     this.mChainEstimators = new OnlineNormalEstimator[numChains];
/* 141 */     for (int m = 0; m < numChains; m++)
/* 142 */       this.mChainEstimators[m] = new OnlineNormalEstimator();
/* 143 */     this.mGlobalEstimator = new OnlineNormalEstimator();
/*     */   }
/*     */ 
/*     */   public PotentialScaleReduction(double[][] yss)
/*     */   {
/* 157 */     this(yss.length);
/* 158 */     for (int m = 0; m < yss.length; m++)
/* 159 */       for (int n = 0; n < yss[m].length; n++)
/* 160 */         update(m, yss[m][n]);
/*     */   }
/*     */ 
/*     */   public int numChains()
/*     */   {
/* 169 */     return this.mChainEstimators.length;
/*     */   }
/*     */ 
/*     */   public OnlineNormalEstimator estimator(int chain)
/*     */   {
/* 179 */     return this.mChainEstimators[chain];
/*     */   }
/*     */ 
/*     */   public OnlineNormalEstimator globalEstimator()
/*     */   {
/* 190 */     return this.mGlobalEstimator;
/*     */   }
/*     */ 
/*     */   public void update(int chain, double y)
/*     */   {
/* 202 */     this.mChainEstimators[chain].handle(y);
/* 203 */     this.mGlobalEstimator.handle(y);
/*     */   }
/*     */ 
/*     */   public double rHat()
/*     */   {
/* 213 */     long minSamples = 9223372036854775807L;
/* 214 */     for (OnlineNormalEstimator estimator : this.mChainEstimators) {
/* 215 */       if (minSamples > estimator.numSamples())
/* 216 */         minSamples = estimator.numSamples();
/*     */     }
/* 218 */     double w = 0.0D;
/* 219 */     for (OnlineNormalEstimator estimator : this.mChainEstimators)
/* 220 */       w += estimator.varianceUnbiased();
/* 221 */     w /= numChains();
/*     */ 
/* 223 */     double crossChainMean = 0.0D;
/* 224 */     for (OnlineNormalEstimator estimator : this.mChainEstimators)
/* 225 */       crossChainMean += estimator.mean();
/* 226 */     crossChainMean /= numChains();
/*     */ 
/* 228 */     double b = 0.0D;
/* 229 */     for (OnlineNormalEstimator estimator : this.mChainEstimators) {
/* 230 */       b += square(estimator.mean() - crossChainMean);
/*     */     }
/* 232 */     b /= (numChains() - 1.0D);
/*     */ 
/* 234 */     double varPlus = (minSamples - 1L) * w / minSamples + b;
/*     */ 
/* 236 */     return Math.sqrt(varPlus / w);
/*     */   }
/*     */ 
/*     */   static double square(double x) {
/* 240 */     return x * x;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.PotentialScaleReduction
 * JD-Core Version:    0.6.2
 */