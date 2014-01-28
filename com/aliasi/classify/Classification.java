/*    */ package com.aliasi.classify;
/*    */ 
/*    */ public class Classification
/*    */ {
/*    */   private final String mBestCategory;
/*    */ 
/*    */   public Classification(String bestCategory)
/*    */   {
/* 40 */     if (bestCategory == null) {
/* 41 */       String msg = "Category cannot be null for classifiers.";
/* 42 */       throw new IllegalArgumentException(msg);
/*    */     }
/* 44 */     this.mBestCategory = bestCategory;
/*    */   }
/*    */ 
/*    */   public String bestCategory()
/*    */   {
/* 53 */     return this.mBestCategory;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 64 */     return "Rank    Category\n1=" + bestCategory();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.Classification
 * JD-Core Version:    0.6.2
 */