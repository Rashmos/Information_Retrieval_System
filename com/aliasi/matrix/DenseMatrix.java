/*     */ package com.aliasi.matrix;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class DenseMatrix extends AbstractMatrix
/*     */ {
/*     */   private final double[][] mValues;
/*     */   private final int mNumRows;
/*     */   private final int mNumColumns;
/*     */   private static final boolean IGNORE = true;
/*     */ 
/*     */   public DenseMatrix(int numRows, int numColumns)
/*     */   {
/*  53 */     this(zeroValues(numRows, numColumns), true);
/*     */   }
/*     */ 
/*     */   public DenseMatrix(double[][] values)
/*     */   {
/*  70 */     this(copyValues(values), true);
/*     */   }
/*     */ 
/*     */   DenseMatrix(double[][] values, boolean ignoreMe)
/*     */   {
/*  76 */     this.mValues = values;
/*  77 */     this.mNumRows = values.length;
/*  78 */     if (this.mNumRows < 1) {
/*  79 */       String msg = "Require positive number of rows. Found number of rows=" + this.mNumRows;
/*     */ 
/*  81 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  83 */     this.mNumColumns = values[0].length;
/*  84 */     if (this.mNumColumns < 1) {
/*  85 */       String msg = "Require positive number of columns. Found number of columns=" + this.mNumColumns;
/*     */ 
/*  87 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int numRows()
/*     */   {
/*  93 */     return this.mNumRows;
/*     */   }
/*     */ 
/*     */   public final int numColumns()
/*     */   {
/*  98 */     return this.mNumColumns;
/*     */   }
/*     */ 
/*     */   public double value(int row, int column)
/*     */   {
/* 103 */     return this.mValues[row][column];
/*     */   }
/*     */ 
/*     */   public void setValue(int row, int column, double value)
/*     */   {
/* 108 */     this.mValues[row][column] = value;
/*     */   }
/*     */ 
/*     */   private static int numColumns(double[][] values)
/*     */   {
/* 113 */     int numColumns = 0;
/* 114 */     for (int i = 0; i < values.length; i++)
/* 115 */       numColumns = Math.max(numColumns, values[i].length);
/* 116 */     return numColumns;
/*     */   }
/*     */ 
/*     */   private static double[][] copyValues(double[][] values) {
/* 120 */     int numRows = values.length;
/* 121 */     int numColumns = numColumns(values);
/* 122 */     double[][] result = new double[numRows][numColumns];
/* 123 */     for (int i = 0; i < numRows; i++) {
/* 124 */       for (int j = 0; j < values[i].length; j++)
/* 125 */         result[i][j] = values[i][j];
/* 126 */       for (int j = values[i].length; j < numColumns; j++)
/* 127 */         result[i][j] = 0.0D;
/*     */     }
/* 129 */     return result;
/*     */   }
/*     */ 
/*     */   private static double[][] zeroValues(int numRows, int numColumns) {
/* 133 */     double[][] result = new double[numRows][numColumns];
/* 134 */     for (int i = 0; i < result.length; i++)
/* 135 */       Arrays.fill(result[i], 0.0D);
/* 136 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.DenseMatrix
 * JD-Core Version:    0.6.2
 */