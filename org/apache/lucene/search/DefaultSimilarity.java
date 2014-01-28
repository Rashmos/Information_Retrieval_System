/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ import org.apache.lucene.index.FieldInvertState;
/*    */ 
/*    */ public class DefaultSimilarity extends Similarity
/*    */ {
/*    */   protected boolean discountOverlaps;
/*    */ 
/*    */   public float computeNorm(String field, FieldInvertState state)
/*    */   {
/*    */     int numTerms;
/*    */     int numTerms;
/* 37 */     if (this.discountOverlaps)
/* 38 */       numTerms = state.getLength() - state.getNumOverlap();
/*    */     else
/* 40 */       numTerms = state.getLength();
/* 41 */     return state.getBoost() * lengthNorm(field, numTerms);
/*    */   }
/*    */ 
/*    */   public float lengthNorm(String fieldName, int numTerms)
/*    */   {
/* 47 */     return (float)(1.0D / Math.sqrt(numTerms));
/*    */   }
/*    */ 
/*    */   public float queryNorm(float sumOfSquaredWeights)
/*    */   {
/* 53 */     return (float)(1.0D / Math.sqrt(sumOfSquaredWeights));
/*    */   }
/*    */ 
/*    */   public float tf(float freq)
/*    */   {
/* 59 */     return (float)Math.sqrt(freq);
/*    */   }
/*    */ 
/*    */   public float sloppyFreq(int distance)
/*    */   {
/* 65 */     return 1.0F / (distance + 1);
/*    */   }
/*    */ 
/*    */   public float idf(int docFreq, int numDocs)
/*    */   {
/* 71 */     return (float)(Math.log(numDocs / (docFreq + 1)) + 1.0D);
/*    */   }
/*    */ 
/*    */   public float coord(int overlap, int maxOverlap)
/*    */   {
/* 77 */     return overlap / maxOverlap;
/*    */   }
/*    */ 
/*    */   public void setDiscountOverlaps(boolean v)
/*    */   {
/* 94 */     this.discountOverlaps = v;
/*    */   }
/*    */ 
/*    */   public boolean getDiscountOverlaps()
/*    */   {
/* 99 */     return this.discountOverlaps;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.DefaultSimilarity
 * JD-Core Version:    0.6.2
 */