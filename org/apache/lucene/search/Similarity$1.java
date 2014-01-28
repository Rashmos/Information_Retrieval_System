/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ class Similarity$1 extends Explanation.IDFExplanation
/*     */ {
/*     */   Similarity$1(Similarity paramSimilarity, int paramInt1, int paramInt2, float paramFloat)
/*     */   {
/*     */   }
/*     */ 
/*     */   public String explain()
/*     */   {
/* 741 */     return "idf(docFreq=" + this.val$df + ", maxDocs=" + this.val$max + ")";
/*     */   }
/*     */ 
/*     */   public float getIdf()
/*     */   {
/* 746 */     return this.val$idf;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Similarity.1
 * JD-Core Version:    0.6.2
 */