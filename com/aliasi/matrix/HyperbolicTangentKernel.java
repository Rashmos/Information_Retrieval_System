/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class HyperbolicTangentKernel
/*     */   implements KernelFunction, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -4623910478151947840L;
/*     */   private final double mK0;
/*     */   private final double mK1;
/*     */ 
/*     */   public HyperbolicTangentKernel(double k0, double k1)
/*     */   {
/*  71 */     if ((Double.isInfinite(k0)) || (Double.isNaN(k0))) {
/*  72 */       String msg = "k0 must be a finite number. Found k0=" + k0;
/*     */ 
/*  74 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  76 */     if ((Double.isInfinite(k1)) || (Double.isNaN(k1)) || (k1 == 0.0D)) {
/*  77 */       String msg = "k1 must be a finite, non-zero number. Found k1=" + k1;
/*     */ 
/*  79 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  81 */     this.mK0 = k0;
/*  82 */     this.mK1 = k1;
/*     */   }
/*     */ 
/*     */   public double proximity(Vector v1, Vector v2)
/*     */   {
/*  96 */     return Math.tanh(this.mK1 * v1.dotProduct(v2) + this.mK0);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 107 */     return "HyperbolicTangentKernel(" + this.mK0 + ", " + this.mK1 + ")";
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 113 */     return new Externalizer(this.mK0, this.mK1);
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 5756879441704225246L;
/*     */     final double mK0;
/*     */     final double mK1;
/*     */ 
/*     */     public Externalizer() {
/* 123 */       this(0.0D, 0.0D);
/*     */     }
/*     */     public Externalizer(double k0, double k1) {
/* 126 */       this.mK0 = k0;
/* 127 */       this.mK1 = k1;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 131 */       out.writeDouble(this.mK0);
/* 132 */       out.writeDouble(this.mK1);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 136 */       double k0 = in.readDouble();
/* 137 */       double k1 = in.readDouble();
/* 138 */       return new HyperbolicTangentKernel(k0, k1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.HyperbolicTangentKernel
 * JD-Core Version:    0.6.2
 */