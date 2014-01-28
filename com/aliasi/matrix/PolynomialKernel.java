/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class PolynomialKernel
/*     */   implements KernelFunction, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 2807317510032521328L;
/*     */   private final int mDegree;
/*     */ 
/*     */   public PolynomialKernel(int degree)
/*     */   {
/*  72 */     this.mDegree = degree;
/*     */   }
/*     */ 
/*     */   public double proximity(Vector v1, Vector v2)
/*     */   {
/*  86 */     return power(1.0D + v1.dotProduct(v2));
/*     */   }
/*     */ 
/*     */   double power(double base) {
/*  90 */     switch (this.mDegree) { case 0:
/*  91 */       return 1.0D;
/*     */     case 1:
/*  92 */       return base;
/*     */     case 2:
/*  93 */       return base * base;
/*     */     case 3:
/*  94 */       return base * base * base;
/*     */     case 4:
/*  95 */       return base * base * base * base; }
/*  96 */     return Math.pow(base, this.mDegree);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return "PolynomialKernel(" + this.mDegree + ")";
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 112 */     return new Externalizer(this.mDegree);
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 4795059467534365487L;
/*     */     final int mDegree;
/*     */ 
/* 119 */     public Externalizer() { this(-1); }
/*     */ 
/*     */     public Externalizer(int degree) {
/* 122 */       this.mDegree = degree;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 126 */       out.writeInt(this.mDegree);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 130 */       int degree = in.readInt();
/* 131 */       return new PolynomialKernel(degree);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.PolynomialKernel
 * JD-Core Version:    0.6.2
 */