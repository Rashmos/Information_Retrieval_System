/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class GaussianRadialBasisKernel
/*     */   implements KernelFunction, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -1670587197184485884L;
/*     */   private final double mNegativeRadius;
/*     */ 
/*     */   public GaussianRadialBasisKernel(double radius)
/*     */   {
/*  73 */     if ((radius <= 0.0D) || (Double.isInfinite(radius)) || (Double.isNaN(radius)))
/*     */     {
/*  77 */       String msg = "Radius must be positive and finite. Found radius=" + radius;
/*     */ 
/*  79 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  81 */     this.mNegativeRadius = (-radius);
/*     */   }
/*     */ 
/*     */   GaussianRadialBasisKernel(double negativeRadius, boolean ignore) {
/*  85 */     this.mNegativeRadius = negativeRadius;
/*     */   }
/*     */ 
/*     */   public double proximity(Vector v1, Vector v2)
/*     */   {
/* 100 */     double dist = EuclideanDistance.DISTANCE.distance(v1, v2);
/* 101 */     return Math.exp(this.mNegativeRadius * (dist * dist));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 112 */     return "GaussianRadialBasedKernel(" + -this.mNegativeRadius + ")";
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 116 */     return new Externalizer(this.mNegativeRadius);
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -5223595743791099605L;
/*     */     final double mNegativeRadius;
/*     */ 
/* 123 */     public Externalizer() { this(1.0D); }
/*     */ 
/*     */     public Externalizer(double negativeRadius) {
/* 126 */       this.mNegativeRadius = negativeRadius;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 130 */       out.writeDouble(this.mNegativeRadius);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 134 */       double negativeRadius = in.readDouble();
/* 135 */       return new GaussianRadialBasisKernel(negativeRadius, true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.GaussianRadialBasisKernel
 * JD-Core Version:    0.6.2
 */