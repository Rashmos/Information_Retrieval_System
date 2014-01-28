/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class EuclideanDistance
/*     */   implements Distance<Vector>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -7331942504500606550L;
/*  68 */   public static final EuclideanDistance DISTANCE = new EuclideanDistance();
/*     */ 
/*     */   public double distance(Vector v1, Vector v2)
/*     */   {
/*  88 */     if (v1.numDimensions() != v2.numDimensions()) {
/*  89 */       String msg = "Vectors must have same dimensions. v1.numDimensions()=" + v1.numDimensions() + " v2.numDimensions()=" + v2.numDimensions();
/*     */ 
/*  92 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  94 */     if (((v1 instanceof SparseFloatVector)) && ((v2 instanceof SparseFloatVector))) {
/*  95 */       return sparseDistance((SparseFloatVector)v1, (SparseFloatVector)v2);
/*     */     }
/*  97 */     double sum = 0.0D;
/*  98 */     int i = v1.numDimensions();
/*     */     while (true) { i--; if (i < 0) break;
/*  99 */       double diff = v1.value(i) - v2.value(i);
/* 100 */       sum += diff * diff;
/*     */     }
/* 102 */     return Math.sqrt(sum);
/*     */   }
/*     */ 
/*     */   static double sparseDistance(SparseFloatVector v1, SparseFloatVector v2)
/*     */   {
/* 107 */     double sum = 0.0D;
/* 108 */     int index1 = 0;
/* 109 */     int index2 = 0;
/* 110 */     int[] keys1 = v1.mKeys;
/* 111 */     int[] keys2 = v2.mKeys;
/* 112 */     float[] vals1 = v1.mValues;
/* 113 */     float[] vals2 = v2.mValues;
/* 114 */     while ((index1 < keys1.length) && (index2 < keys2.length)) {
/* 115 */       int comp = keys1[index1] - keys2[index2];
/* 116 */       double diff = comp == 0 ? vals1[(index1++)] - vals2[(index2++)] : comp < 0 ? vals1[(index1++)] : vals2[(index2++)];
/*     */ 
/* 122 */       sum += diff * diff;
/*     */     }
/* 124 */     for (; index1 < keys1.length; index1++)
/* 125 */       sum += vals1[index1] * vals1[index1];
/* 126 */     for (; index2 < keys2.length; index2++)
/* 127 */       sum += vals2[index2] * vals2[index2];
/* 128 */     return Math.sqrt(sum);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.EuclideanDistance
 * JD-Core Version:    0.6.2
 */