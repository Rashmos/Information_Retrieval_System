/*    */ package com.aliasi.classify;
/*    */ 
/*    */ public class Classified<E>
/*    */ {
/*    */   private final E mObject;
/*    */   private final Classification mClassification;
/*    */ 
/*    */   public Classified(E object, Classification c)
/*    */   {
/* 42 */     this.mObject = object;
/* 43 */     this.mClassification = c;
/*    */   }
/*    */ 
/*    */   public E getObject()
/*    */   {
/* 52 */     return this.mObject;
/*    */   }
/*    */ 
/*    */   public Classification getClassification()
/*    */   {
/* 61 */     return this.mClassification;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return this.mObject + ":" + this.mClassification.bestCategory();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.Classified
 * JD-Core Version:    0.6.2
 */