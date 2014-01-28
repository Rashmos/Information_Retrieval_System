/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ class Similarity$2 extends Explanation.IDFExplanation
/*     */ {
/*     */   Similarity$2(Similarity paramSimilarity, float paramFloat, StringBuilder paramStringBuilder)
/*     */   {
/*     */   }
/*     */ 
/*     */   public float getIdf()
/*     */   {
/* 780 */     return this.val$fIdf;
/*     */   }
/*     */ 
/*     */   public String explain() {
/* 784 */     return this.val$exp.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Similarity.2
 * JD-Core Version:    0.6.2
 */