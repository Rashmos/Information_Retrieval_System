/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ public abstract class AbstractVector
/*     */   implements Vector
/*     */ {
/*     */   public int[] nonZeroDimensions()
/*     */   {
/*  46 */     int count = 0;
/*  47 */     for (int i = 0; i < numDimensions(); i++)
/*  48 */       if (value(i) != 0.0D)
/*  49 */         count++;
/*  50 */     int[] result = new int[count];
/*  51 */     int pos = 0;
/*  52 */     for (int i = 0; i < numDimensions(); i++)
/*  53 */       if (value(i) != 0.0D)
/*  54 */         result[(pos++)] = i;
/*  55 */     return result;
/*     */   }
/*     */ 
/*     */   public void increment(double scale, Vector v)
/*     */   {
/*  68 */     if (v.numDimensions() != numDimensions()) {
/*  69 */       String msg = "Specified vector not same dimensionality. Found this.numDimensions()=" + numDimensions() + " v.numDimensions()=" + v.numDimensions();
/*     */ 
/*  72 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  74 */     for (int i = 0; i < numDimensions(); i++)
/*  75 */       setValue(i, value(i) + scale * v.value(i));
/*     */   }
/*     */ 
/*     */   public abstract int numDimensions();
/*     */ 
/*     */   public abstract double value(int paramInt);
/*     */ 
/*     */   public void setValue(int dimension, double value)
/*     */   {
/* 108 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public double length()
/*     */   {
/* 120 */     double length = 0.0D;
/* 121 */     int i = numDimensions();
/*     */     while (true) { i--; if (i < 0) break;
/* 122 */       double val = value(i);
/* 123 */       length += val * val;
/*     */     }
/* 125 */     return Math.sqrt(length);
/*     */   }
/*     */ 
/*     */   public Vector add(Vector v)
/*     */   {
/* 142 */     int numDimensions = numDimensions();
/* 143 */     if (v.numDimensions() != numDimensions) {
/* 144 */       String msg = "Arrays must have same dimensions to add. found this.numDimensions()=" + numDimensions + " v.numDimensions()=" + v.numDimensions();
/*     */ 
/* 147 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 149 */     double[] result = new double[numDimensions];
/* 150 */     for (int i = 0; i < numDimensions; i++)
/* 151 */       result[i] = (value(i) + v.value(i));
/* 152 */     return new DenseVector(result);
/*     */   }
/*     */ 
/*     */   public double dotProduct(Vector v)
/*     */   {
/* 170 */     verifyMatchingDimensions(v);
/* 171 */     double product = 0.0D;
/* 172 */     int i = numDimensions();
/*     */     while (true) { i--; if (i < 0) break;
/* 173 */       product += value(i) * v.value(i); }
/* 174 */     return product;
/*     */   }
/*     */ 
/*     */   public double cosine(Vector v)
/*     */   {
/* 191 */     verifyMatchingDimensions(v);
/* 192 */     double product = 0.0D;
/* 193 */     double length1 = 0.0D;
/* 194 */     double length2 = 0.0D;
/* 195 */     int i = numDimensions();
/*     */     while (true) { i--; if (i < 0) break;
/* 196 */       double val1 = value(i);
/* 197 */       double val2 = v.value(i);
/* 198 */       product += val1 * val2;
/* 199 */       length1 += val1 * val1;
/* 200 */       length2 += val2 * val2;
/*     */     }
/* 202 */     double cosine = product / Math.sqrt(length1 * length2);
/* 203 */     return cosine > 1.0D ? 1.0D : cosine < -1.0D ? -1.0D : cosine;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 222 */     if (!(that instanceof Vector)) return false;
/* 223 */     Vector thatVector = (Vector)that;
/* 224 */     if (numDimensions() != thatVector.numDimensions())
/* 225 */       return false;
/* 226 */     int i = numDimensions();
/*     */     do { i--; if (i < 0) break; }
/* 227 */     while (value(i) == thatVector.value(i));
/* 228 */     return false;
/* 229 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 243 */     int code = 1;
/* 244 */     int numDimensions = numDimensions();
/* 245 */     for (int i = 0; i < numDimensions; i++)
/* 246 */       if (value(i) != 0.0D) {
/* 247 */         long v = Double.doubleToLongBits(value(i));
/* 248 */         int valHash = (int)(v ^ v >>> 32);
/* 249 */         code = 31 * code + valHash;
/*     */       }
/* 251 */     return code;
/*     */   }
/*     */ 
/*     */   void verifyMatchingDimensions(Vector v) {
/* 255 */     if (numDimensions() != v.numDimensions()) {
/* 256 */       String msg = "Vectors must be same dimensionality. This vector's dimensionality=" + numDimensions() + " Specified vector's dimensionality=" + v.numDimensions();
/*     */ 
/* 259 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.AbstractVector
 * JD-Core Version:    0.6.2
 */