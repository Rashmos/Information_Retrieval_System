/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ public abstract class AbstractMatrix
/*     */   implements Matrix
/*     */ {
/*     */   public abstract int numRows();
/*     */ 
/*     */   public abstract int numColumns();
/*     */ 
/*     */   public abstract double value(int paramInt1, int paramInt2);
/*     */ 
/*     */   public void setValue(int row, int column, double value)
/*     */   {
/*  57 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Vector rowVector(int row)
/*     */   {
/*  76 */     return new MatrixBackedVector.Row(this, row);
/*     */   }
/*     */ 
/*     */   public Vector columnVector(int column)
/*     */   {
/*  96 */     return new MatrixBackedVector.Column(this, column);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 112 */     if (!(that instanceof Matrix)) return false;
/* 113 */     Matrix thatMatrix = (Matrix)that;
/* 114 */     if (numRows() != thatMatrix.numRows()) return false;
/* 115 */     if (numColumns() != thatMatrix.numColumns()) return false;
/* 116 */     int i = numRows();
/*     */     do { i--; if (i < 0) break; }
/* 117 */     while (rowVector(i).equals(thatMatrix.rowVector(i)));
/* 118 */     return false;
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 130 */     int code = 1;
/* 131 */     int numRows = numRows();
/* 132 */     int numColumns = numColumns();
/* 133 */     for (int i = 0; i < numRows; i++)
/* 134 */       for (int j = 0; j < numColumns; j++) {
/* 135 */         long v = Double.doubleToLongBits(value(i, j));
/* 136 */         int valHash = (int)(v ^ v >>> 32);
/* 137 */         code = 31 * code + valHash;
/*     */       }
/* 139 */     return code;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.AbstractMatrix
 * JD-Core Version:    0.6.2
 */