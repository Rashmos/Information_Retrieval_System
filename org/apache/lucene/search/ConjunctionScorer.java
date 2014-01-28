/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ class ConjunctionScorer extends Scorer
/*     */ {
/*     */   private final Scorer[] scorers;
/*     */   private final float coord;
/*  30 */   private int lastDoc = -1;
/*     */ 
/*     */   public ConjunctionScorer(Similarity similarity, Collection<Scorer> scorers) throws IOException {
/*  33 */     this(similarity, (Scorer[])scorers.toArray(new Scorer[scorers.size()]));
/*     */   }
/*     */ 
/*     */   public ConjunctionScorer(Similarity similarity, Scorer[] scorers) throws IOException {
/*  37 */     super(similarity);
/*  38 */     this.scorers = scorers;
/*  39 */     this.coord = similarity.coord(scorers.length, scorers.length);
/*     */ 
/*  41 */     for (int i = 0; i < scorers.length; i++) {
/*  42 */       if (scorers[i].nextDoc() == 2147483647)
/*     */       {
/*  45 */         this.lastDoc = 2147483647;
/*  46 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  55 */     Arrays.sort(scorers, new Comparator() {
/*     */       public int compare(Scorer o1, Scorer o2) {
/*  57 */         return o1.docID() - o2.docID();
/*     */       }
/*     */     });
/*  70 */     if (doNext() == 2147483647)
/*     */     {
/*  72 */       this.lastDoc = 2147483647;
/*  73 */       return;
/*     */     }
/*     */ 
/*  82 */     int end = scorers.length - 1;
/*  83 */     int max = end >> 1;
/*  84 */     for (int i = 0; i < max; i++) {
/*  85 */       Scorer tmp = scorers[i];
/*  86 */       int idx = end - i - 1;
/*  87 */       scorers[i] = scorers[idx];
/*  88 */       scorers[idx] = tmp;
/*     */     }
/*     */   }
/*     */ 
/*     */   private int doNext() throws IOException {
/*  93 */     int first = 0;
/*  94 */     int doc = this.scorers[(this.scorers.length - 1)].docID();
/*     */     Scorer firstScorer;
/*  96 */     while ((firstScorer = this.scorers[first]).docID() < doc) {
/*  97 */       doc = firstScorer.advance(doc);
/*  98 */       first = first == this.scorers.length - 1 ? 0 : first + 1;
/*     */     }
/* 100 */     return doc;
/*     */   }
/*     */ 
/*     */   public int advance(int target) throws IOException
/*     */   {
/* 105 */     if (this.lastDoc == 2147483647)
/* 106 */       return this.lastDoc;
/* 107 */     if (this.scorers[(this.scorers.length - 1)].docID() < target) {
/* 108 */       this.scorers[(this.scorers.length - 1)].advance(target);
/*     */     }
/* 110 */     return this.lastDoc = doNext();
/*     */   }
/*     */ 
/*     */   public int docID()
/*     */   {
/* 115 */     return this.lastDoc;
/*     */   }
/*     */ 
/*     */   public int nextDoc() throws IOException
/*     */   {
/* 120 */     if (this.lastDoc == 2147483647)
/* 121 */       return this.lastDoc;
/* 122 */     if (this.lastDoc == -1) {
/* 123 */       return this.lastDoc = this.scorers[(this.scorers.length - 1)].docID();
/*     */     }
/* 125 */     this.scorers[(this.scorers.length - 1)].nextDoc();
/* 126 */     return this.lastDoc = doNext();
/*     */   }
/*     */ 
/*     */   public float score() throws IOException
/*     */   {
/* 131 */     float sum = 0.0F;
/* 132 */     for (int i = 0; i < this.scorers.length; i++) {
/* 133 */       sum += this.scorers[i].score();
/*     */     }
/* 135 */     return sum * this.coord;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.ConjunctionScorer
 * JD-Core Version:    0.6.2
 */