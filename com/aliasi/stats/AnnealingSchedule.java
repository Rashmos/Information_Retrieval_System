/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public abstract class AnnealingSchedule
/*     */ {
/*     */   public abstract double learningRate(int paramInt);
/*     */ 
/*     */   public boolean receivedError(int epoch, double rate, double error)
/*     */   {
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */   public static AnnealingSchedule inverse(double initialLearningRate, double annealingRate)
/*     */   {
/* 149 */     return new InverseAnnealingSchedule(initialLearningRate, annealingRate);
/*     */   }
/*     */ 
/*     */   public static AnnealingSchedule exponential(double initialLearningRate, double base)
/*     */   {
/* 168 */     return new ExponentialAnnealingSchedule(initialLearningRate, base);
/*     */   }
/*     */ 
/*     */   public static AnnealingSchedule constant(double learningRate)
/*     */   {
/* 183 */     return new ConstantAnnealingSchedule(learningRate);
/*     */   }
/*     */ 
/*     */   static void verifyFinitePositive(String varName, double val)
/*     */   {
/* 189 */     if ((Double.isNaN(val)) || (Double.isInfinite(val)) || (val <= 0.0D))
/*     */     {
/* 192 */       String msg = varName + " must be finite and positive." + " Found " + varName + "=" + val;
/*     */ 
/* 194 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ExponentialAnnealingSchedule extends AnnealingSchedule.AnnealingScheduleImpl
/*     */   {
/*     */     private final double mExponentBase;
/*     */ 
/*     */     ExponentialAnnealingSchedule(double initialLearningRate, double base)
/*     */     {
/* 246 */       super();
/* 247 */       if ((Double.isNaN(base)) || (Double.isInfinite(base)) || (base <= 0.0D) || (base > 1.0D))
/*     */       {
/* 251 */         String msg = "Base must be between 0.0 (exclusive) and 1.0 (inclusive) Found base=" + base;
/*     */ 
/* 253 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 255 */       this.mExponentBase = base;
/*     */     }
/*     */ 
/*     */     public double learningRate(int epoch) {
/* 259 */       return this.mInitialLearningRate * Math.pow(this.mExponentBase, epoch);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 263 */       return "Exponential(initialLearningRate=" + this.mInitialLearningRate + ", base=" + this.mExponentBase + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   static class InverseAnnealingSchedule extends AnnealingSchedule.AnnealingScheduleImpl
/*     */   {
/*     */     private final double mAnnealingRate;
/*     */ 
/*     */     InverseAnnealingSchedule(double initialLearningRate, double annealingRate)
/*     */     {
/* 226 */       super();
/* 227 */       verifyFinitePositive("annealing rate", annealingRate);
/* 228 */       this.mAnnealingRate = annealingRate;
/*     */     }
/*     */ 
/*     */     public double learningRate(int epoch) {
/* 232 */       return this.mInitialLearningRate / (1.0D + epoch / this.mAnnealingRate);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 236 */       return "Inverse(initialLearningRate=" + this.mInitialLearningRate + ", annealingRate=" + this.mAnnealingRate + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ConstantAnnealingSchedule extends AnnealingSchedule.AnnealingScheduleImpl
/*     */   {
/*     */     ConstantAnnealingSchedule(double learningRate)
/*     */     {
/* 209 */       super();
/*     */     }
/*     */ 
/*     */     public double learningRate(int epoch) {
/* 213 */       return this.mInitialLearningRate;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 217 */       return "AnnealingSchedule.constant(" + this.mInitialLearningRate + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class AnnealingScheduleImpl extends AnnealingSchedule
/*     */   {
/*     */     final double mInitialLearningRate;
/*     */ 
/*     */     AnnealingScheduleImpl(double initialLearningRate)
/*     */     {
/* 201 */       verifyFinitePositive("initial learning rate", initialLearningRate);
/* 202 */       this.mInitialLearningRate = initialLearningRate;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.AnnealingSchedule
 * JD-Core Version:    0.6.2
 */