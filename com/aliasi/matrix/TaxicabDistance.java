/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TaxicabDistance
/*     */   implements Distance<Vector>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -8456511197031445244L;
/*  65 */   public static final TaxicabDistance DISTANCE = new TaxicabDistance();
/*     */ 
/*     */   public double distance(Vector v1, Vector v2)
/*     */   {
/*  85 */     if (v1.numDimensions() != v2.numDimensions()) {
/*  86 */       String msg = "Vectors must have same dimensions. v1.numDimensions()=" + v1.numDimensions() + " v2.numDimensions()=" + v2.numDimensions();
/*     */ 
/*  89 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  91 */     if (((v1 instanceof SparseFloatVector)) && ((v2 instanceof SparseFloatVector))) {
/*  92 */       return sparseDistance((SparseFloatVector)v1, (SparseFloatVector)v2);
/*     */     }
/*  94 */     double sum = 0.0D;
/*  95 */     int i = v1.numDimensions();
/*     */     while (true) { i--; if (i < 0) break;
/*  96 */       double diff = Math.abs(v1.value(i) - v2.value(i));
/*  97 */       sum += diff;
/*     */     }
/*  99 */     return sum;
/*     */   }
/*     */ 
/*     */   static double sparseDistance(SparseFloatVector v1, SparseFloatVector v2)
/*     */   {
/* 104 */     double sum = 0.0D;
/* 105 */     int index1 = 0;
/* 106 */     int index2 = 0;
/* 107 */     int[] keys1 = v1.mKeys;
/* 108 */     int[] keys2 = v2.mKeys;
/* 109 */     float[] vals1 = v1.mValues;
/* 110 */     float[] vals2 = v2.mValues;
/* 111 */     while ((index1 < keys1.length) && (index2 < keys2.length)) {
/* 112 */       int comp = keys1[index1] - keys2[index2];
/* 113 */       double diff = comp == 0 ? vals1[(index1++)] - vals2[(index2++)] : comp < 0 ? vals1[(index1++)] : vals2[(index2++)];
/*     */ 
/* 119 */       sum += Math.abs(diff);
/*     */     }
/* 121 */     for (; index1 < keys1.length; index1++)
/* 122 */       sum += Math.abs(vals1[index1]);
/* 123 */     for (; index2 < keys2.length; index2++)
/* 124 */       sum += Math.abs(vals2[index2]);
/* 125 */     return sum;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.TaxicabDistance
 * JD-Core Version:    0.6.2
 */