/*    */ package org.apache.lucene.search.payloads;
/*    */ 
/*    */ public class AveragePayloadFunction extends PayloadFunction
/*    */ {
/*    */   public float currentScore(int docId, String field, int start, int end, int numPayloadsSeen, float currentScore, float currentPayloadScore)
/*    */   {
/* 31 */     return currentPayloadScore + currentScore;
/*    */   }
/*    */ 
/*    */   public float docScore(int docId, String field, int numPayloadsSeen, float payloadScore)
/*    */   {
/* 36 */     return numPayloadsSeen > 0 ? payloadScore / numPayloadsSeen : 1.0F;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 41 */     int prime = 31;
/* 42 */     int result = 1;
/* 43 */     result = 31 * result + getClass().hashCode();
/* 44 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 49 */     if (this == obj)
/* 50 */       return true;
/* 51 */     if (obj == null)
/* 52 */       return false;
/* 53 */     if (getClass() != obj.getClass())
/* 54 */       return false;
/* 55 */     return true;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.payloads.AveragePayloadFunction
 * JD-Core Version:    0.6.2
 */