/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.util.PriorityQueue;
/*     */ 
/*     */ public abstract class TopScoreDocCollector extends TopDocsCollector<ScoreDoc>
/*     */ {
/*     */   ScoreDoc pqTop;
/* 121 */   int docBase = 0;
/*     */   Scorer scorer;
/*     */ 
/*     */   public static TopScoreDocCollector create(int numHits, boolean docsScoredInOrder)
/*     */   {
/* 112 */     if (docsScoredInOrder) {
/* 113 */       return new InOrderTopScoreDocCollector(numHits, null);
/*     */     }
/* 115 */     return new OutOfOrderTopScoreDocCollector(numHits, null);
/*     */   }
/*     */ 
/*     */   private TopScoreDocCollector(int numHits)
/*     */   {
/* 126 */     super(new HitQueue(numHits, true));
/*     */ 
/* 129 */     this.pqTop = ((ScoreDoc)this.pq.top());
/*     */   }
/*     */ 
/*     */   protected TopDocs newTopDocs(ScoreDoc[] results, int start)
/*     */   {
/* 134 */     if (results == null) {
/* 135 */       return EMPTY_TOPDOCS;
/*     */     }
/*     */ 
/* 142 */     float maxScore = (0.0F / 0.0F);
/* 143 */     if (start == 0) {
/* 144 */       maxScore = results[0].score;
/*     */     } else {
/* 146 */       for (int i = this.pq.size(); i > 1; i--) this.pq.pop();
/* 147 */       maxScore = ((ScoreDoc)this.pq.pop()).score;
/*     */     }
/*     */ 
/* 150 */     return new TopDocs(this.totalHits, results, maxScore);
/*     */   }
/*     */ 
/*     */   public void setNextReader(IndexReader reader, int base)
/*     */   {
/* 155 */     this.docBase = base;
/*     */   }
/*     */ 
/*     */   public void setScorer(Scorer scorer) throws IOException
/*     */   {
/* 160 */     this.scorer = scorer;
/*     */   }
/*     */ 
/*     */   private static class OutOfOrderTopScoreDocCollector extends TopScoreDocCollector
/*     */   {
/*     */     private OutOfOrderTopScoreDocCollector(int numHits)
/*     */     {
/*  74 */       super(null);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/*  79 */       float score = this.scorer.score();
/*     */ 
/*  82 */       assert (!Float.isNaN(score));
/*     */ 
/*  84 */       this.totalHits += 1;
/*  85 */       doc += this.docBase;
/*  86 */       if ((score < this.pqTop.score) || ((score == this.pqTop.score) && (doc > this.pqTop.doc))) {
/*  87 */         return;
/*     */       }
/*  89 */       this.pqTop.doc = doc;
/*  90 */       this.pqTop.score = score;
/*  91 */       this.pqTop = ((ScoreDoc)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/*  96 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class InOrderTopScoreDocCollector extends TopScoreDocCollector
/*     */   {
/*     */     private InOrderTopScoreDocCollector(int numHits)
/*     */     {
/*  42 */       super(null);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/*  47 */       float score = this.scorer.score();
/*     */ 
/*  50 */       assert (score != (1.0F / -1.0F));
/*  51 */       assert (!Float.isNaN(score));
/*     */ 
/*  53 */       this.totalHits += 1;
/*  54 */       if (score <= this.pqTop.score)
/*     */       {
/*  58 */         return;
/*     */       }
/*  60 */       this.pqTop.doc = (doc + this.docBase);
/*  61 */       this.pqTop.score = score;
/*  62 */       this.pqTop = ((ScoreDoc)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/*  67 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TopScoreDocCollector
 * JD-Core Version:    0.6.2
 */