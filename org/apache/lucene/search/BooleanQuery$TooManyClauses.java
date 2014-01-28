/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ public class BooleanQuery$TooManyClauses extends RuntimeException
/*    */ {
/*    */   public String getMessage()
/*    */   {
/* 45 */     return "maxClauseCount is set to " + BooleanQuery.access$000();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanQuery.TooManyClauses
 * JD-Core Version:    0.6.2
 */