/*     */ package com.aliasi.stats;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public class BernoulliEstimator extends BernoulliDistribution
/*     */   implements Compilable
/*     */ {
/*     */   long mTotalCount;
/*     */   long mSuccessCount;
/*     */ 
/*     */   public void train(boolean success, int numSamples)
/*     */   {
/*  61 */     this.mTotalCount += numSamples;
/*  62 */     if (success) this.mSuccessCount += numSamples;
/*     */   }
/*     */ 
/*     */   public void train(boolean success)
/*     */   {
/*  73 */     train(success, 1);
/*     */   }
/*     */ 
/*     */   public double successProbability()
/*     */   {
/*  85 */     return this.mSuccessCount / this.mTotalCount;
/*     */   }
/*     */ 
/*     */   public long numTrainingSamples()
/*     */   {
/*  96 */     return this.mTotalCount;
/*     */   }
/*     */ 
/*     */   public long numTrainingSamples(boolean success)
/*     */   {
/* 108 */     return success ? this.mSuccessCount : this.mTotalCount - this.mSuccessCount;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 121 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable { private static final long serialVersionUID = -3979523774865702910L;
/*     */     final BernoulliEstimator mDistro;
/*     */ 
/* 127 */     public Externalizer() { this.mDistro = null; } 
/*     */     public Externalizer(BernoulliEstimator distro) {
/* 129 */       this.mDistro = distro;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 133 */       out.writeDouble(this.mDistro.successProbability());
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 137 */       double successProb = in.readDouble();
/* 138 */       return new BernoulliConstant(successProb);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.BernoulliEstimator
 * JD-Core Version:    0.6.2
 */