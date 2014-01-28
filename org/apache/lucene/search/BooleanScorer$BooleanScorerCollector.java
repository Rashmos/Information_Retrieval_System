/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ 
/*     */ final class BooleanScorer$BooleanScorerCollector extends Collector
/*     */ {
/*     */   private BooleanScorer.BucketTable bucketTable;
/*     */   private int mask;
/*     */   private Scorer scorer;
/*     */ 
/*     */   public BooleanScorer$BooleanScorerCollector(int mask, BooleanScorer.BucketTable bucketTable)
/*     */   {
/*  66 */     this.mask = mask;
/*  67 */     this.bucketTable = bucketTable;
/*     */   }
/*     */ 
/*     */   public final void collect(int doc) throws IOException
/*     */   {
/*  72 */     BooleanScorer.BucketTable table = this.bucketTable;
/*  73 */     int i = doc & 0x7FF;
/*  74 */     BooleanScorer.Bucket bucket = table.buckets[i];
/*  75 */     if (bucket == null)
/*     */     {
/*     */       void tmp36_33 = new BooleanScorer.Bucket(); bucket = tmp36_33; table.buckets[i] = tmp36_33;
/*     */     }
/*  78 */     if (bucket.doc != doc) {
/*  79 */       bucket.doc = doc;
/*  80 */       bucket.score = this.scorer.score();
/*  81 */       bucket.bits = this.mask;
/*  82 */       bucket.coord = 1;
/*     */ 
/*  84 */       bucket.next = table.first;
/*  85 */       table.first = bucket;
/*     */     } else {
/*  87 */       bucket.score += this.scorer.score();
/*  88 */       bucket.bits |= this.mask;
/*  89 */       bucket.coord += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setNextReader(IndexReader reader, int docBase)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setScorer(Scorer scorer)
/*     */     throws IOException
/*     */   {
/* 100 */     this.scorer = scorer;
/*     */   }
/*     */ 
/*     */   public boolean acceptsDocsOutOfOrder()
/*     */   {
/* 105 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanScorer.BooleanScorerCollector
 * JD-Core Version:    0.6.2
 */