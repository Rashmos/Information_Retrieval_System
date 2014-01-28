/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.util.ScorerDocQueue;
/*     */ 
/*     */ class DisjunctionSumScorer extends Scorer
/*     */ {
/*     */   private final int nrScorers;
/*     */   protected final List<Scorer> subScorers;
/*     */   private final int minimumNrMatchers;
/*     */   private ScorerDocQueue scorerDocQueue;
/*  53 */   private int currentDoc = -1;
/*     */ 
/*  56 */   protected int nrMatchers = -1;
/*     */ 
/*  58 */   private float currentScore = (0.0F / 0.0F);
/*     */ 
/*     */   public DisjunctionSumScorer(List<Scorer> subScorers, int minimumNrMatchers)
/*     */     throws IOException
/*     */   {
/*  71 */     super(null);
/*     */ 
/*  73 */     this.nrScorers = subScorers.size();
/*     */ 
/*  75 */     if (minimumNrMatchers <= 0) {
/*  76 */       throw new IllegalArgumentException("Minimum nr of matchers must be positive");
/*     */     }
/*  78 */     if (this.nrScorers <= 1) {
/*  79 */       throw new IllegalArgumentException("There must be at least 2 subScorers");
/*     */     }
/*     */ 
/*  82 */     this.minimumNrMatchers = minimumNrMatchers;
/*  83 */     this.subScorers = subScorers;
/*     */ 
/*  85 */     initScorerDocQueue();
/*     */   }
/*     */ 
/*     */   public DisjunctionSumScorer(List<Scorer> subScorers)
/*     */     throws IOException
/*     */   {
/*  92 */     this(subScorers, 1);
/*     */   }
/*     */ 
/*     */   private void initScorerDocQueue()
/*     */     throws IOException
/*     */   {
/*  99 */     this.scorerDocQueue = new ScorerDocQueue(this.nrScorers);
/* 100 */     for (Scorer se : this.subScorers)
/* 101 */       if (se.nextDoc() != 2147483647)
/* 102 */         this.scorerDocQueue.insert(se);
/*     */   }
/*     */ 
/*     */   public void score(Collector collector)
/*     */     throws IOException
/*     */   {
/* 112 */     collector.setScorer(this);
/* 113 */     while (nextDoc() != 2147483647)
/* 114 */       collector.collect(this.currentDoc);
/*     */   }
/*     */ 
/*     */   protected boolean score(Collector collector, int max, int firstDocID)
/*     */     throws IOException
/*     */   {
/* 128 */     collector.setScorer(this);
/* 129 */     while (this.currentDoc < max) {
/* 130 */       collector.collect(this.currentDoc);
/* 131 */       if (nextDoc() == 2147483647) {
/* 132 */         return false;
/*     */       }
/*     */     }
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   public int nextDoc() throws IOException
/*     */   {
/* 140 */     if ((this.scorerDocQueue.size() < this.minimumNrMatchers) || (!advanceAfterCurrent())) {
/* 141 */       this.currentDoc = 2147483647;
/*     */     }
/* 143 */     return this.currentDoc;
/*     */   }
/*     */ 
/*     */   protected boolean advanceAfterCurrent()
/*     */     throws IOException
/*     */   {
/*     */     do
/*     */     {
/* 166 */       this.currentDoc = this.scorerDocQueue.topDoc();
/* 167 */       this.currentScore = this.scorerDocQueue.topScore();
/* 168 */       this.nrMatchers = 1;
/*     */ 
/* 170 */       while ((this.scorerDocQueue.topNextAndAdjustElsePop()) || 
/* 171 */         (this.scorerDocQueue.size() != 0))
/*     */       {
/* 175 */         if (this.scorerDocQueue.topDoc() != this.currentDoc) {
/*     */           break;
/*     */         }
/* 178 */         this.currentScore += this.scorerDocQueue.topScore();
/* 179 */         this.nrMatchers += 1;
/*     */       }
/*     */ 
/* 182 */       if (this.nrMatchers >= this.minimumNrMatchers)
/* 183 */         return true; 
/*     */     }
/* 184 */     while (this.scorerDocQueue.size() >= this.minimumNrMatchers);
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */   public float score()
/*     */     throws IOException
/*     */   {
/* 194 */     return this.currentScore;
/*     */   }
/*     */ 
/*     */   public int docID() {
/* 198 */     return this.currentDoc;
/*     */   }
/*     */ 
/*     */   public int nrMatchers()
/*     */   {
/* 205 */     return this.nrMatchers;
/*     */   }
/*     */ 
/*     */   public int advance(int target)
/*     */     throws IOException
/*     */   {
/* 220 */     if (this.scorerDocQueue.size() < this.minimumNrMatchers) {
/* 221 */       return this.currentDoc = 2147483647;
/*     */     }
/* 223 */     if (target <= this.currentDoc) {
/* 224 */       return this.currentDoc;
/*     */     }
/*     */     do
/* 227 */       if (this.scorerDocQueue.topDoc() >= target)
/* 228 */         return this.currentDoc = 2147483647;
/* 229 */     while ((this.scorerDocQueue.topSkipToAndAdjustElsePop(target)) || 
/* 230 */       (this.scorerDocQueue.size() >= this.minimumNrMatchers));
/* 231 */     return this.currentDoc = 2147483647;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.DisjunctionSumScorer
 * JD-Core Version:    0.6.2
 */