/*     */ package com.aliasi.stats;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public class PoissonEstimator extends PoissonDistribution
/*     */ {
/*  47 */   private double mSum = 0.0D;
/*  48 */   private double mNumSamples = 0.0D;
/*     */ 
/*     */   public PoissonEstimator()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PoissonEstimator(double priorNumSamples, double priorMean)
/*     */   {
/*  71 */     if ((priorMean <= 0.0D) || (Double.isNaN(priorMean)) || (Double.isInfinite(priorMean)))
/*     */     {
/*  74 */       String msg = "Prior mean must be finite and positive. Found priorMean=" + priorMean;
/*     */ 
/*  76 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  78 */     if ((priorNumSamples <= 0.0D) || (Double.isNaN(priorNumSamples)) || (Double.isInfinite(priorNumSamples)))
/*     */     {
/*  81 */       String msg = "Prior number of samples must be finite and positive. Found priorNumSamples=" + priorNumSamples;
/*     */ 
/*  83 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  85 */     this.mSum = (priorMean * priorNumSamples);
/*  86 */     this.mNumSamples = priorNumSamples;
/*     */   }
/*     */ 
/*     */   public void train(long sample)
/*     */   {
/* 103 */     train(sample, 1.0D);
/*     */   }
/*     */ 
/*     */   public void train(long sample, double weight)
/*     */   {
/* 123 */     if (sample < 0L) {
/* 124 */       String msg = "Poisson distributions only have positive outcomes. Found training sample=" + sample;
/*     */ 
/* 126 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 128 */     if ((weight < 0.0D) || (Double.isNaN(weight)) || (Double.isInfinite(weight))) {
/* 129 */       String msg = "Training weights must be finite and positive. Found weight=" + weight;
/*     */ 
/* 131 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 133 */     if (9.223372036854776E+18D - this.mSum < sample) {
/* 134 */       String msg = "Adding last sample overflows the event sum. Sum so far=" + this.mSum + " Number of training samples=" + this.mNumSamples;
/*     */ 
/* 137 */       throw new IllegalStateException(msg);
/*     */     }
/* 139 */     this.mSum += sample * weight;
/* 140 */     this.mNumSamples += weight;
/*     */   }
/*     */ 
/*     */   public double mean()
/*     */   {
/* 154 */     if (this.mSum <= 0.0D) {
/* 155 */       String msg = this.mNumSamples == 0.0D ? "No samples provided." : "Only zero samples provided.";
/*     */ 
/* 158 */       throw new IllegalStateException(msg);
/*     */     }
/* 160 */     return this.mSum / this.mNumSamples;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 174 */     PoissonConstant dist = new PoissonConstant(mean());
/* 175 */     dist.compileTo(objOut);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.PoissonEstimator
 * JD-Core Version:    0.6.2
 */