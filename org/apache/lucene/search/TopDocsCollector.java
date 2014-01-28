/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import org.apache.lucene.util.PriorityQueue;
/*     */ 
/*     */ public abstract class TopDocsCollector<T extends ScoreDoc> extends Collector
/*     */ {
/*  35 */   protected static final TopDocs EMPTY_TOPDOCS = new TopDocs(0, new ScoreDoc[0], (0.0F / 0.0F));
/*     */   protected PriorityQueue<T> pq;
/*     */   protected int totalHits;
/*     */ 
/*     */   protected TopDocsCollector(PriorityQueue<T> pq)
/*     */   {
/*  49 */     this.pq = pq;
/*     */   }
/*     */ 
/*     */   protected void populateResults(ScoreDoc[] results, int howMany)
/*     */   {
/*  57 */     for (int i = howMany - 1; i >= 0; i--)
/*  58 */       results[i] = ((ScoreDoc)this.pq.pop());
/*     */   }
/*     */ 
/*     */   protected TopDocs newTopDocs(ScoreDoc[] results, int start)
/*     */   {
/*  69 */     return results == null ? EMPTY_TOPDOCS : new TopDocs(this.totalHits, results);
/*     */   }
/*     */ 
/*     */   public int getTotalHits()
/*     */   {
/*  74 */     return this.totalHits;
/*     */   }
/*     */ 
/*     */   public final TopDocs topDocs()
/*     */   {
/*  82 */     return topDocs(0, this.totalHits < this.pq.size() ? this.totalHits : this.pq.size());
/*     */   }
/*     */ 
/*     */   public final TopDocs topDocs(int start)
/*     */   {
/* 101 */     return topDocs(start, this.totalHits < this.pq.size() ? this.totalHits : this.pq.size());
/*     */   }
/*     */ 
/*     */   public final TopDocs topDocs(int start, int howMany)
/*     */   {
/* 123 */     int size = this.totalHits < this.pq.size() ? this.totalHits : this.pq.size();
/*     */ 
/* 127 */     if ((start < 0) || (start >= size) || (howMany <= 0)) {
/* 128 */       return newTopDocs(null, start);
/*     */     }
/*     */ 
/* 132 */     howMany = Math.min(size - start, howMany);
/* 133 */     ScoreDoc[] results = new ScoreDoc[howMany];
/*     */ 
/* 140 */     for (int i = this.pq.size() - start - howMany; i > 0; i--) this.pq.pop();
/*     */ 
/* 143 */     populateResults(results, howMany);
/*     */ 
/* 145 */     return newTopDocs(results, start);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TopDocsCollector
 * JD-Core Version:    0.6.2
 */