/*    */ package com.aliasi.classify;
/*    */ 
/*    */ public class RankedClassification extends Classification
/*    */ {
/*    */   private final String[] mCategories;
/*    */ 
/*    */   public RankedClassification(String[] categories)
/*    */   {
/* 46 */     super(categories.length == 0 ? null : categories[0]);
/*    */ 
/* 49 */     this.mCategories = categories;
/*    */   }
/*    */ 
/*    */   public int size()
/*    */   {
/* 58 */     return this.mCategories.length;
/*    */   }
/*    */ 
/*    */   public String category(int rank)
/*    */   {
/* 72 */     checkRange(rank);
/* 73 */     return this.mCategories[rank];
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 84 */     StringBuilder sb = new StringBuilder();
/* 85 */     sb.append("Rank  Category\n");
/* 86 */     for (int i = 0; i < size(); i++)
/* 87 */       sb.append("rank " + i + "=" + category(i) + '\n');
/* 88 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   void checkRange(int rank)
/*    */   {
/* 93 */     if ((rank < 0) || (rank >= size())) {
/* 94 */       String msg = "Rank out of bounds. Rank=" + rank + " size()=" + size();
/*    */ 
/* 97 */       throw new IllegalArgumentException(msg);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.RankedClassification
 * JD-Core Version:    0.6.2
 */