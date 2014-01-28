/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ 
/*     */ class MultiSearcher$MultiSearcherCallableWithSort
/*     */   implements Callable<TopFieldDocs>
/*     */ {
/*     */   private final Lock lock;
/*     */   private final Searchable searchable;
/*     */   private final Weight weight;
/*     */   private final Filter filter;
/*     */   private final int nDocs;
/*     */   private final int i;
/*     */   private final FieldDocSortedHitQueue hq;
/*     */   private final int[] starts;
/*     */   private final Sort sort;
/*     */ 
/*     */   public MultiSearcher$MultiSearcherCallableWithSort(Lock lock, Searchable searchable, Weight weight, Filter filter, int nDocs, FieldDocSortedHitQueue hq, Sort sort, int i, int[] starts)
/*     */   {
/* 398 */     this.lock = lock;
/* 399 */     this.searchable = searchable;
/* 400 */     this.weight = weight;
/* 401 */     this.filter = filter;
/* 402 */     this.nDocs = nDocs;
/* 403 */     this.hq = hq;
/* 404 */     this.i = i;
/* 405 */     this.starts = starts;
/* 406 */     this.sort = sort;
/*     */   }
/*     */ 
/*     */   public TopFieldDocs call() throws IOException {
/* 410 */     TopFieldDocs docs = this.searchable.search(this.weight, this.filter, this.nDocs, this.sort);
/*     */ 
/* 414 */     for (int j = 0; j < docs.fields.length; j++) {
/* 415 */       if (docs.fields[j].getType() == 1)
/*     */       {
/* 417 */         for (int j2 = 0; j2 < docs.scoreDocs.length; j2++) {
/* 418 */           FieldDoc fd = (FieldDoc)docs.scoreDocs[j2];
/* 419 */           fd.fields[j] = Integer.valueOf(((Integer)fd.fields[j]).intValue() + this.starts[this.i]);
/*     */         }
/* 421 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 425 */     this.lock.lock();
/*     */     try {
/* 427 */       this.hq.setFields(docs.fields);
/*     */     } finally {
/* 429 */       this.lock.unlock();
/*     */     }
/*     */ 
/* 432 */     ScoreDoc[] scoreDocs = docs.scoreDocs;
/* 433 */     for (int j = 0; j < scoreDocs.length; j++) {
/* 434 */       FieldDoc fieldDoc = (FieldDoc)scoreDocs[j];
/* 435 */       fieldDoc.doc += this.starts[this.i];
/*     */ 
/* 437 */       this.lock.lock();
/*     */       try {
/* 439 */         if (fieldDoc == this.hq.insertWithOverflow(fieldDoc))
/* 440 */           break;
/*     */       } finally {
/* 442 */         this.lock.unlock();
/*     */       }
/*     */     }
/* 445 */     return docs;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiSearcher.MultiSearcherCallableWithSort
 * JD-Core Version:    0.6.2
 */