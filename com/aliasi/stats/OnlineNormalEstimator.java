/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public class OnlineNormalEstimator
/*     */ {
/* 133 */   private long mN = 0L;
/* 134 */   private double mM = 0.0D;
/* 135 */   private double mS = 0.0D;
/*     */ 
/*     */   public void handle(double x)
/*     */   {
/* 152 */     this.mN += 1L;
/* 153 */     double nextM = this.mM + (x - this.mM) / this.mN;
/* 154 */     this.mS += (x - this.mM) * (x - nextM);
/* 155 */     this.mM = nextM;
/*     */   }
/*     */ 
/*     */   public void unHandle(double x)
/*     */   {
/* 167 */     if (this.mN == 0L) {
/* 168 */       String msg = "Cannot unhandle after 0 samples.";
/* 169 */       throw new IllegalStateException(msg);
/*     */     }
/* 171 */     if (this.mN == 1L) {
/* 172 */       this.mN = 0L;
/* 173 */       this.mM = 0.0D;
/* 174 */       this.mS = 0.0D;
/* 175 */       return;
/*     */     }
/* 177 */     double mOld = (this.mN * this.mM - x) / (this.mN - 1L);
/* 178 */     this.mS -= (x - this.mM) * (x - mOld);
/* 179 */     this.mM = mOld;
/* 180 */     this.mN -= 1L;
/*     */   }
/*     */ 
/*     */   public long numSamples()
/*     */   {
/* 189 */     return this.mN;
/*     */   }
/*     */ 
/*     */   public double mean()
/*     */   {
/* 198 */     return this.mM;
/*     */   }
/*     */ 
/*     */   public double variance()
/*     */   {
/* 208 */     return this.mN > 1L ? this.mS / this.mN : 0.0D;
/*     */   }
/*     */ 
/*     */   public double varianceUnbiased()
/*     */   {
/* 217 */     return this.mN > 1L ? this.mS / (this.mN - 1L) : 0.0D;
/*     */   }
/*     */ 
/*     */   public double standardDeviation()
/*     */   {
/* 227 */     return Math.sqrt(variance());
/*     */   }
/*     */ 
/*     */   public double standardDeviationUnbiased()
/*     */   {
/* 236 */     return Math.sqrt(varianceUnbiased());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 246 */     return "Norm(mean=" + mean() + ", stdDev=" + standardDeviation() + ")[numSamples=" + numSamples() + "]";
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.OnlineNormalEstimator
 * JD-Core Version:    0.6.2
 */