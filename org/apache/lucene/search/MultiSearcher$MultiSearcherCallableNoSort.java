/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ 
/*     */ class MultiSearcher$MultiSearcherCallableNoSort
/*     */   implements Callable<TopDocs>
/*     */ {
/*     */   private final Lock lock;
/*     */   private final Searchable searchable;
/*     */   private final Weight weight;
/*     */   private final Filter filter;
/*     */   private final int nDocs;
/*     */   private final int i;
/*     */   private final HitQueue hq;
/*     */   private final int[] starts;
/*     */ 
/*     */   public MultiSearcher$MultiSearcherCallableNoSort(Lock lock, Searchable searchable, Weight weight, Filter filter, int nDocs, HitQueue hq, int i, int[] starts)
/*     */   {
/* 352 */     this.lock = lock;
/* 353 */     this.searchable = searchable;
/* 354 */     this.weight = weight;
/* 355 */     this.filter = filter;
/* 356 */     this.nDocs = nDocs;
/* 357 */     this.hq = hq;
/* 358 */     this.i = i;
/* 359 */     this.starts = starts;
/*     */   }
/*     */ 
/*     */   public TopDocs call() throws IOException {
/* 363 */     TopDocs docs = this.searchable.search(this.weight, this.filter, this.nDocs);
/* 364 */     ScoreDoc[] scoreDocs = docs.scoreDocs;
/* 365 */     for (int j = 0; j < scoreDocs.length; j++) {
/* 366 */       ScoreDoc scoreDoc = scoreDocs[j];
/* 367 */       scoreDoc.doc += this.starts[this.i];
/*     */ 
/* 369 */       this.lock.lock();
/*     */       try {
/* 371 */         if (scoreDoc == this.hq.insertWithOverflow(scoreDoc))
/* 372 */           break;
/*     */       } finally {
/* 374 */         this.lock.unlock();
/*     */       }
/*     */     }
/* 377 */     return docs;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiSearcher.MultiSearcherCallableNoSort
 * JD-Core Version:    0.6.2
 */