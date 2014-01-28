/*    */ package org.apache.lucene.search.payloads;
/*    */ 
/*    */ public class MaxPayloadFunction extends PayloadFunction
/*    */ {
/*    */   public float currentScore(int docId, String field, int start, int end, int numPayloadsSeen, float currentScore, float currentPayloadScore)
/*    */   {
/* 30 */     if (numPayloadsSeen == 0) {
/* 31 */       return currentPayloadScore;
/*    */     }
/* 33 */     return Math.max(currentPayloadScore, currentScore);
/*    */   }
/*    */ 
/*    */   public float docScore(int docId, String field, int numPayloadsSeen, float payloadScore)
/*    */   {
/* 39 */     return numPayloadsSeen > 0 ? payloadScore : 1.0F;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 44 */     int prime = 31;
/* 45 */     int result = 1;
/* 46 */     result = 31 * result + getClass().hashCode();
/* 47 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 52 */     if (this == obj)
/* 53 */       return true;
/* 54 */     if (obj == null)
/* 55 */       return false;
/* 56 */     if (getClass() != obj.getClass())
/* 57 */       return false;
/* 58 */     return true;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.payloads.MaxPayloadFunction
 * JD-Core Version:    0.6.2
 */