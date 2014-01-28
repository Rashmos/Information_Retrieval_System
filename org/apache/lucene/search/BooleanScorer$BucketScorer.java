/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ final class BooleanScorer$BucketScorer extends Scorer
/*     */ {
/*     */   float score;
/* 117 */   int doc = 2147483647;
/*     */ 
/* 119 */   public BooleanScorer$BucketScorer() { super(null); }
/*     */ 
/*     */   public int advance(int target) throws IOException {
/* 122 */     return 2147483647;
/*     */   }
/*     */   public int docID() {
/* 125 */     return this.doc;
/*     */   }
/*     */   public int nextDoc() throws IOException {
/* 128 */     return 2147483647;
/*     */   }
/*     */   public float score() throws IOException {
/* 131 */     return this.score;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanScorer.BucketScorer
 * JD-Core Version:    0.6.2
 */