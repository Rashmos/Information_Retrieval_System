/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class MinkowskiDistance
/*     */   implements Distance<Vector>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -3492306373950488519L;
/*     */   int mOrder;
/*     */ 
/*     */   public MinkowskiDistance(int order)
/*     */   {
/*  75 */     this.mOrder = order;
/*     */   }
/*     */ 
/*     */   public int order()
/*     */   {
/*  84 */     return this.mOrder;
/*     */   }
/*     */ 
/*     */   public double distance(Vector v1, Vector v2)
/*     */   {
/*  98 */     if (v1.numDimensions() != v2.numDimensions()) {
/*  99 */       String msg = "Vectors must have same dimensions. v1.numDimensions()=" + v1.numDimensions() + " v2.numDimensions()=" + v2.numDimensions();
/*     */ 
/* 102 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 104 */     if (((v1 instanceof SparseFloatVector)) && ((v2 instanceof SparseFloatVector))) {
/* 105 */       return sparseDistance((SparseFloatVector)v1, (SparseFloatVector)v2);
/*     */     }
/* 107 */     double sum = 0.0D;
/* 108 */     int i = v1.numDimensions();
/*     */     while (true) { i--; if (i < 0) break;
/* 109 */       double absDiff = Math.abs(v1.value(i) - v2.value(i));
/* 110 */       sum += Math.pow(absDiff, this.mOrder);
/*     */     }
/* 112 */     return Math.pow(sum, 1.0D / this.mOrder);
/*     */   }
/*     */ 
/*     */   double sparseDistance(SparseFloatVector v1, SparseFloatVector v2)
/*     */   {
/* 117 */     double sum = 0.0D;
/* 118 */     int index1 = 0;
/* 119 */     int index2 = 0;
/* 120 */     int[] keys1 = v1.mKeys;
/* 121 */     int[] keys2 = v2.mKeys;
/* 122 */     float[] vals1 = v1.mValues;
/* 123 */     float[] vals2 = v2.mValues;
/* 124 */     while ((index1 < keys1.length) && (index2 < keys2.length)) {
/* 125 */       int comp = keys1[index1] - keys2[index2];
/* 126 */       double diff = Math.abs(comp < 0 ? vals1[(index1++)] : comp == 0 ? vals1[(index1++)] - vals2[(index2++)] : vals2[(index2++)]);
/*     */ 
/* 130 */       sum += Math.pow(diff, this.mOrder);
/*     */     }
/* 132 */     for (; index1 < keys1.length; index1++)
/* 133 */       sum += Math.pow(Math.abs(vals1[index1]), this.mOrder);
/* 134 */     for (; index2 < keys2.length; index2++)
/* 135 */       sum += Math.pow(Math.abs(vals2[index2]), this.mOrder);
/* 136 */     return Math.pow(sum, 1.0D / this.mOrder);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.MinkowskiDistance
 * JD-Core Version:    0.6.2
 */