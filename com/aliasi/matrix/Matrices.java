/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ public class Matrices
/*     */ {
/*     */   public static Vector unmodifiableVector(Vector v)
/*     */   {
/*  47 */     return new UnmodifiableVector(v);
/*     */   }
/*     */ 
/*     */   public static boolean hasZeroDiagonal(Matrix m)
/*     */   {
/* 118 */     int n = m.numRows();
/* 119 */     if (n != m.numColumns())
/* 120 */       return false;
/* 121 */     for (int i = 0; i < n; i++)
/* 122 */       if (m.value(i, i) != 0.0D)
/* 123 */         return false;
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isSymmetric(Matrix m)
/*     */   {
/* 149 */     int n = m.numRows();
/* 150 */     if (n != m.numColumns()) return false;
/* 151 */     for (int i = 0; i < n; i++)
/* 152 */       for (int j = i + 1; j < n; j++)
/* 153 */         if (m.value(i, j) != m.value(j, i))
/* 154 */           return false;
/* 155 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isNonNegative(Matrix m)
/*     */   {
/* 170 */     for (int i = 0; i < m.numRows(); i++)
/* 171 */       for (int j = 0; j < m.numColumns(); j++)
/* 172 */         if ((m.value(i, j) < 0.0D) || (Double.isNaN(m.value(i, j))))
/* 173 */           return false;
/* 174 */     return true;
/*     */   }
/*     */ 
/*     */   public static double[] toArray(Vector v)
/*     */   {
/* 184 */     double[] xs = new double[v.numDimensions()];
/* 185 */     for (int i = 0; i < xs.length; i++)
/* 186 */       xs[i] = v.value(i);
/* 187 */     return xs;
/*     */   }
/*     */ 
/*     */   static Vector add(Vector v1, Vector v2) {
/* 191 */     int numDimensions = v1.numDimensions();
/* 192 */     if (numDimensions != v2.numDimensions()) {
/* 193 */       String msg = "Can only add vectors of the same dimensionality. Found v1.numDimensions()=" + v1.numDimensions() + " v2.numDimensions()=" + v2.numDimensions();
/*     */ 
/* 196 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 198 */     double[] vals = new double[numDimensions];
/* 199 */     for (int i = 0; i < numDimensions; i++)
/* 200 */       vals[i] = (v1.value(i) + v2.value(i));
/* 201 */     return new DenseVector(vals);
/*     */   }
/*     */ 
/*     */   static class VectorFilter
/*     */     implements Vector
/*     */   {
/*     */     protected final Vector mV;
/*     */ 
/*     */     VectorFilter(Vector v)
/*     */     {
/*  68 */       this.mV = v;
/*     */     }
/*     */     public int[] nonZeroDimensions() {
/*  71 */       return this.mV.nonZeroDimensions();
/*     */     }
/*     */     public void increment(double scale, Vector v) {
/*  74 */       this.mV.increment(scale, v);
/*     */     }
/*     */     public Vector add(Vector v) {
/*  77 */       return this.mV.add(v);
/*     */     }
/*     */     public double cosine(Vector v) {
/*  80 */       return this.mV.cosine(v);
/*     */     }
/*     */     public double dotProduct(Vector v) {
/*  83 */       return this.mV.dotProduct(v);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object that) {
/*  87 */       return this.mV.equals(that);
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/*  91 */       return this.mV.hashCode();
/*     */     }
/*     */     public double length() {
/*  94 */       return this.mV.length();
/*     */     }
/*     */     public int numDimensions() {
/*  97 */       return this.mV.numDimensions();
/*     */     }
/*     */     public void setValue(int dimension, double value) {
/* 100 */       this.mV.setValue(dimension, value);
/*     */     }
/*     */     public double value(int dimension) {
/* 103 */       return this.mV.value(dimension);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class UnmodifiableVector extends Matrices.VectorFilter
/*     */   {
/*     */     UnmodifiableVector(Vector v)
/*     */     {
/*  52 */       super();
/*     */     }
/*     */ 
/*     */     public void setValue(int dimension, double value) {
/*  56 */       String msg = "Cannot modify an unmodifiable vector.";
/*  57 */       throw new UnsupportedOperationException(msg);
/*     */     }
/*     */ 
/*     */     public String toString() {
/*  61 */       return this.mV.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.Matrices
 * JD-Core Version:    0.6.2
 */