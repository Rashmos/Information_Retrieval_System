/*    */ package org.apache.lucene.search.payloads;
/*    */ 
/*    */ public class MinPayloadFunction extends PayloadFunction
/*    */ {
/*    */   public float currentScore(int docId, String field, int start, int end, int numPayloadsSeen, float currentScore, float currentPayloadScore)
/*    */   {
/* 28 */     if (numPayloadsSeen == 0) {
/* 29 */       return currentPayloadScore;
/*    */     }
/* 31 */     return Math.min(currentPayloadScore, currentScore);
/*    */   }
/*    */ 
/*    */   public float docScore(int docId, String field, int numPayloadsSeen, float payloadScore)
/*    */   {
/* 37 */     return numPayloadsSeen > 0 ? payloadScore : 1.0F;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 42 */     int prime = 31;
/* 43 */     int result = 1;
/* 44 */     result = 31 * result + getClass().hashCode();
/* 45 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 50 */     if (this == obj)
/* 51 */       return true;
/* 52 */     if (obj == null)
/* 53 */       return false;
/* 54 */     if (getClass() != obj.getClass())
/* 55 */       return false;
/* 56 */     return true;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.payloads.MinPayloadFunction
 * JD-Core Version:    0.6.2
 */