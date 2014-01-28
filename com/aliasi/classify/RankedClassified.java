/*    */ package com.aliasi.classify;
/*    */ 
/*    */ public class RankedClassified<E> extends Classified<E>
/*    */ {
/*    */   public RankedClassified(E object, RankedClassification c)
/*    */   {
/* 39 */     super(object, c);
/*    */   }
/*    */ 
/*    */   public RankedClassification getClassification()
/*    */   {
/* 49 */     RankedClassification result = (RankedClassification)super.getClassification();
/* 50 */     return result;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.RankedClassified
 * JD-Core Version:    0.6.2
 */