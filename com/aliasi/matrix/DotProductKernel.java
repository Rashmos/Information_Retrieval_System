/*    */ package com.aliasi.matrix;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class DotProductKernel
/*    */   implements KernelFunction, Serializable
/*    */ {
/*    */   static final long serialVersionUID = -3943009270761485840L;
/*    */ 
/*    */   public String toString()
/*    */   {
/* 52 */     return "DotProductKernel()";
/*    */   }
/*    */ 
/*    */   public double proximity(Vector v1, Vector v2)
/*    */   {
/* 66 */     return v1.dotProduct(v2);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.DotProductKernel
 * JD-Core Version:    0.6.2
 */