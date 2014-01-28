/*    */ package com.aliasi.matrix;
/*    */ 
/*    */ abstract class MatrixBackedVector extends AbstractVector
/*    */ {
/*    */   protected final Matrix mMatrix;
/*    */   protected final int mIndex;
/*    */ 
/*    */   MatrixBackedVector(Matrix m, int index)
/*    */   {
/*  9 */     this.mMatrix = m;
/* 10 */     this.mIndex = index;
/*    */   }
/*    */ 
/*    */   static class Column extends MatrixBackedVector
/*    */     implements Vector
/*    */   {
/*    */     Column(Matrix m, int index)
/*    */     {
/* 37 */       super(index);
/*    */     }
/*    */ 
/*    */     public int numDimensions() {
/* 41 */       return this.mMatrix.numRows();
/*    */     }
/*    */ 
/*    */     public void setValue(int row, double value) {
/* 45 */       this.mMatrix.setValue(row, this.mIndex, value);
/*    */     }
/*    */ 
/*    */     public double value(int row) {
/* 49 */       return this.mMatrix.value(row, this.mIndex);
/*    */     }
/*    */ 
/*    */     public Vector add(Vector v) {
/* 53 */       return Matrices.add(this, v);
/*    */     }
/*    */   }
/*    */ 
/*    */   static class Row extends MatrixBackedVector
/*    */     implements Vector
/*    */   {
/*    */     Row(Matrix m, int index)
/*    */     {
/* 15 */       super(index);
/*    */     }
/*    */ 
/*    */     public int numDimensions() {
/* 19 */       return this.mMatrix.numColumns();
/*    */     }
/*    */ 
/*    */     public void setValue(int column, double value) {
/* 23 */       this.mMatrix.setValue(this.mIndex, column, value);
/*    */     }
/*    */ 
/*    */     public double value(int column) {
/* 27 */       return this.mMatrix.value(this.mIndex, column);
/*    */     }
/*    */ 
/*    */     public Vector add(Vector v) {
/* 31 */       return Matrices.add(this, v);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.MatrixBackedVector
 * JD-Core Version:    0.6.2
 */