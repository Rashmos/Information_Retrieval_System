/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ public enum Version
/*    */ {
/* 33 */   LUCENE_20, 
/*    */ 
/* 36 */   LUCENE_21, 
/*    */ 
/* 39 */   LUCENE_22, 
/*    */ 
/* 42 */   LUCENE_23, 
/*    */ 
/* 45 */   LUCENE_24, 
/*    */ 
/* 48 */   LUCENE_29, 
/*    */ 
/* 55 */   LUCENE_30, 
/*    */ 
/* 75 */   LUCENE_CURRENT;
/*    */ 
/*    */   /** @deprecated */
/*    */   public boolean onOrAfter(Version other) {
/* 79 */     return compareTo(other) >= 0;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.Version
 * JD-Core Version:    0.6.2
 */