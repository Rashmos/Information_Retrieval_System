/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ public class ProximityMatrix extends AbstractMatrix
/*     */ {
/*     */   private final double[][] mValues;
/*     */   private final int mSize;
/*     */ 
/*     */   ProximityMatrix(double[][] values, boolean ignoreMe)
/*     */   {
/*  38 */     this.mValues = values;
/*  39 */     this.mSize = values.length;
/*     */   }
/*     */ 
/*     */   public ProximityMatrix(int numDimensions)
/*     */   {
/*  49 */     this(zeroValues(numDimensions), true);
/*     */   }
/*     */ 
/*     */   public int numColumns()
/*     */   {
/*  60 */     return this.mSize;
/*     */   }
/*     */ 
/*     */   public int numRows()
/*     */   {
/*  71 */     return this.mSize;
/*     */   }
/*     */ 
/*     */   public void setValue(int row, int column, double value)
/*     */   {
/*  93 */     if (row == column) {
/*  94 */       if (value == 0.0D) return;
/*  95 */       String msg = "Cannot set non-zero diagonal on a proximity matrix. Found row=" + row + " column=" + column + " value=" + value;
/*     */ 
/*  99 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 101 */     if ((value < 0.0D) || (Double.isNaN(value))) {
/* 102 */       String msg = "Proximity matrix values must be >= 0.0 Found=" + value;
/*     */ 
/* 104 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 106 */     if (row < column)
/* 107 */       this.mValues[(column - 1)][row] = value;
/*     */     else
/* 109 */       this.mValues[(row - 1)][column] = value;
/*     */   }
/*     */ 
/*     */   public double value(int row, int column)
/*     */   {
/* 126 */     if (row == column) {
/* 127 */       if (row > this.mSize) {
/* 128 */         String msg = "Index out of bounds row=column=" + row;
/* 129 */         throw new IndexOutOfBoundsException(msg);
/*     */       }
/* 131 */       return 0.0D;
/*     */     }
/* 133 */     return row < column ? this.mValues[(column - 1)][row] : this.mValues[(row - 1)][column];
/*     */   }
/*     */ 
/*     */   private static double[][] zeroValues(int numDimensions)
/*     */   {
/* 140 */     if (numDimensions < 1) {
/* 141 */       String msg = "Require positive number of dimensions. Found numDimensions=" + numDimensions;
/*     */ 
/* 143 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 145 */     double[][] values = new double[numDimensions - 1][];
/* 146 */     for (int i = 0; i < values.length; i++) {
/* 147 */       values[i] = new double[i + 1];
/* 148 */       for (int j = 0; j < values[i].length; j++)
/* 149 */         values[i][j] = 0.0D;
/*     */     }
/* 151 */     return values;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.ProximityMatrix
 * JD-Core Version:    0.6.2
 */