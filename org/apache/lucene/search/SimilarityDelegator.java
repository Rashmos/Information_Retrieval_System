/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ import org.apache.lucene.index.FieldInvertState;
/*    */ 
/*    */ public class SimilarityDelegator extends Similarity
/*    */ {
/*    */   private Similarity delegee;
/*    */ 
/*    */   public SimilarityDelegator(Similarity delegee)
/*    */   {
/* 34 */     this.delegee = delegee;
/*    */   }
/*    */ 
/*    */   public float computeNorm(String fieldName, FieldInvertState state)
/*    */   {
/* 39 */     return this.delegee.computeNorm(fieldName, state);
/*    */   }
/*    */ 
/*    */   public float lengthNorm(String fieldName, int numTerms)
/*    */   {
/* 44 */     return this.delegee.lengthNorm(fieldName, numTerms);
/*    */   }
/*    */ 
/*    */   public float queryNorm(float sumOfSquaredWeights)
/*    */   {
/* 49 */     return this.delegee.queryNorm(sumOfSquaredWeights);
/*    */   }
/*    */ 
/*    */   public float tf(float freq)
/*    */   {
/* 54 */     return this.delegee.tf(freq);
/*    */   }
/*    */ 
/*    */   public float sloppyFreq(int distance)
/*    */   {
/* 59 */     return this.delegee.sloppyFreq(distance);
/*    */   }
/*    */ 
/*    */   public float idf(int docFreq, int numDocs)
/*    */   {
/* 64 */     return this.delegee.idf(docFreq, numDocs);
/*    */   }
/*    */ 
/*    */   public float coord(int overlap, int maxOverlap)
/*    */   {
/* 69 */     return this.delegee.coord(overlap, maxOverlap);
/*    */   }
/*    */ 
/*    */   public float scorePayload(int docId, String fieldName, int start, int end, byte[] payload, int offset, int length)
/*    */   {
/* 74 */     return this.delegee.scorePayload(docId, fieldName, start, end, payload, offset, length);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.SimilarityDelegator
 * JD-Core Version:    0.6.2
 */