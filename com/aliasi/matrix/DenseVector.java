/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class DenseVector extends AbstractVector
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -4587660322610782962L;
/*     */   static final boolean IGNORE = true;
/*     */   final double[] mValues;
/*     */ 
/*     */   public DenseVector(int numDimensions)
/*     */   {
/*  64 */     this(zeroValues(numDimensions), true);
/*  65 */     if (numDimensions < 1) {
/*  66 */       String msg = "Require positive number of dimensions. Found numDimensions=" + numDimensions;
/*     */ 
/*  68 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DenseVector(double[] values)
/*     */   {
/*  84 */     this(copyValues(values), true);
/*  85 */     if (values.length < 1) {
/*  86 */       String msg = "Vectors must have positive length. Found length=" + values.length;
/*     */ 
/*  88 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DenseVector(DenseVector v)
/*     */   {
/*  98 */     this(v.mValues);
/*     */   }
/*     */ 
/*     */   public DenseVector(Vector v)
/*     */   {
/* 107 */     this.mValues = new double[v.numDimensions()];
/* 108 */     for (int d = 0; d < this.mValues.length; d++)
/* 109 */       this.mValues[d] = v.value(d);
/*     */   }
/*     */ 
/*     */   DenseVector(double[] values, boolean ignore) {
/* 113 */     this.mValues = values;
/*     */   }
/*     */ 
/*     */   public double dotProduct(Vector v)
/*     */   {
/* 119 */     if ((v instanceof SparseFloatVector)) {
/* 120 */       return v.dotProduct(this);
/*     */     }
/* 122 */     return super.dotProduct(v);
/*     */   }
/*     */ 
/*     */   public void setValue(int dimension, double value)
/*     */   {
/* 137 */     this.mValues[dimension] = value;
/*     */   }
/*     */ 
/*     */   public int numDimensions()
/*     */   {
/* 148 */     return this.mValues.length;
/*     */   }
/*     */ 
/*     */   public void increment(double scale, Vector v)
/*     */   {
/* 168 */     if (v.numDimensions() != numDimensions()) {
/* 169 */       String msg = "Require dimensionality match. Found this.numDimensions()=" + numDimensions() + " v.numDimensions()=" + v.numDimensions();
/*     */ 
/* 172 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 174 */     if ((v instanceof SparseFloatVector)) {
/* 175 */       int[] nonZeroDims = v.nonZeroDimensions();
/*     */ 
/* 177 */       float[] vals = ((SparseFloatVector)v).mValues;
/* 178 */       for (int i = 0; i < nonZeroDims.length; i++) {
/* 179 */         int dim = nonZeroDims[i];
/* 180 */         this.mValues[dim] += scale * vals[i];
/*     */       }
/*     */     } else {
/* 183 */       for (int i = 0; i < this.mValues.length; i++)
/* 184 */         this.mValues[i] += scale * v.value(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Vector add(Vector v)
/*     */   {
/* 191 */     return Matrices.add(this, v);
/*     */   }
/*     */ 
/*     */   public double value(int dimension)
/*     */   {
/* 206 */     return this.mValues[dimension];
/*     */   }
/*     */ 
/*     */   private static double[] zeroValues(int n) {
/* 210 */     double[] xs = new double[n];
/* 211 */     Arrays.fill(xs, 0.0D);
/* 212 */     return xs;
/*     */   }
/*     */ 
/*     */   private static double[] copyValues(double[] values) {
/* 216 */     double[] xs = new double[values.length];
/* 217 */     System.arraycopy(values, 0, xs, 0, xs.length);
/* 218 */     return xs;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 229 */     StringBuilder sb = new StringBuilder();
/* 230 */     for (int i = 0; i < this.mValues.length; i++) {
/* 231 */       sb.append(" " + i + "=" + this.mValues[i]);
/*     */     }
/* 233 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 237 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 3997370009258027321L;
/*     */     private final DenseVector mVector;
/*     */ 
/* 244 */     public Serializer(DenseVector vector) { this.mVector = vector; }
/*     */ 
/*     */     public Serializer() {
/* 247 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 251 */       out.writeInt(this.mVector.numDimensions());
/* 252 */       for (int i = 0; i < this.mVector.numDimensions(); i++)
/* 253 */         out.writeDouble(this.mVector.value(i));
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws ClassNotFoundException, IOException {
/* 257 */       int numDimensions = in.readInt();
/* 258 */       double[] vals = new double[numDimensions];
/* 259 */       for (int i = 0; i < numDimensions; i++) {
/* 260 */         vals[i] = in.readDouble();
/*     */       }
/* 262 */       return new DenseVector(vals, true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.DenseVector
 * JD-Core Version:    0.6.2
 */