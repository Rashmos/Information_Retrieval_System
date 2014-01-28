/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ class BooleanQuery$1 extends SimilarityDelegator
/*     */ {
/*     */   BooleanQuery$1(BooleanQuery paramBooleanQuery, Similarity x0)
/*     */   {
/*  99 */     super(x0); } 
/* 100 */   public float coord(int overlap, int maxOverlap) { return 1.0F; }
/*     */ 
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanQuery.1
 * JD-Core Version:    0.6.2
 */