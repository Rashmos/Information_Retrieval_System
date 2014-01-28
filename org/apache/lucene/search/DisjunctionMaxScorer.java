/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ class DisjunctionMaxScorer extends Scorer
/*     */ {
/*     */   private final Scorer[] subScorers;
/*     */   private int numScorers;
/*     */   private final float tieBreakerMultiplier;
/*  34 */   private int doc = -1;
/*     */ 
/*     */   public DisjunctionMaxScorer(float tieBreakerMultiplier, Similarity similarity, Scorer[] subScorers, int numScorers)
/*     */     throws IOException
/*     */   {
/*  53 */     super(similarity);
/*     */ 
/*  55 */     this.tieBreakerMultiplier = tieBreakerMultiplier;
/*     */ 
/*  59 */     this.subScorers = subScorers;
/*  60 */     this.numScorers = numScorers;
/*     */ 
/*  62 */     heapify();
/*     */   }
/*     */ 
/*     */   public int nextDoc() throws IOException
/*     */   {
/*  67 */     if (this.numScorers == 0) return this.doc = 2147483647;
/*  68 */     while (this.subScorers[0].docID() == this.doc) {
/*  69 */       if (this.subScorers[0].nextDoc() != 2147483647) {
/*  70 */         heapAdjust(0);
/*     */       } else {
/*  72 */         heapRemoveRoot();
/*  73 */         if (this.numScorers == 0) {
/*  74 */           return this.doc = 2147483647;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  79 */     return this.doc = this.subScorers[0].docID();
/*     */   }
/*     */ 
/*     */   public int docID()
/*     */   {
/*  84 */     return this.doc;
/*     */   }
/*     */ 
/*     */   public float score()
/*     */     throws IOException
/*     */   {
/*  92 */     int doc = this.subScorers[0].docID();
/*  93 */     float[] sum = { this.subScorers[0].score() }; float[] max = { sum[0] };
/*  94 */     int size = this.numScorers;
/*  95 */     scoreAll(1, size, doc, sum, max);
/*  96 */     scoreAll(2, size, doc, sum, max);
/*  97 */     return max[0] + (sum[0] - max[0]) * this.tieBreakerMultiplier;
/*     */   }
/*     */ 
/*     */   private void scoreAll(int root, int size, int doc, float[] sum, float[] max) throws IOException
/*     */   {
/* 102 */     if ((root < size) && (this.subScorers[root].docID() == doc)) {
/* 103 */       float sub = this.subScorers[root].score();
/* 104 */       sum[0] += sub;
/* 105 */       max[0] = Math.max(max[0], sub);
/* 106 */       scoreAll((root << 1) + 1, size, doc, sum, max);
/* 107 */       scoreAll((root << 1) + 2, size, doc, sum, max);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int advance(int target) throws IOException
/*     */   {
/* 113 */     if (this.numScorers == 0) return this.doc = 2147483647;
/* 114 */     while (this.subScorers[0].docID() < target) {
/* 115 */       if (this.subScorers[0].advance(target) != 2147483647) {
/* 116 */         heapAdjust(0);
/*     */       } else {
/* 118 */         heapRemoveRoot();
/* 119 */         if (this.numScorers == 0) {
/* 120 */           return this.doc = 2147483647;
/*     */         }
/*     */       }
/*     */     }
/* 124 */     return this.doc = this.subScorers[0].docID();
/*     */   }
/*     */ 
/*     */   private void heapify()
/*     */   {
/* 129 */     for (int i = (this.numScorers >> 1) - 1; i >= 0; i--)
/* 130 */       heapAdjust(i);
/*     */   }
/*     */ 
/*     */   private void heapAdjust(int root)
/*     */   {
/* 138 */     Scorer scorer = this.subScorers[root];
/* 139 */     int doc = scorer.docID();
/* 140 */     int i = root;
/* 141 */     while (i <= (this.numScorers >> 1) - 1) {
/* 142 */       int lchild = (i << 1) + 1;
/* 143 */       Scorer lscorer = this.subScorers[lchild];
/* 144 */       int ldoc = lscorer.docID();
/* 145 */       int rdoc = 2147483647; int rchild = (i << 1) + 2;
/* 146 */       Scorer rscorer = null;
/* 147 */       if (rchild < this.numScorers) {
/* 148 */         rscorer = this.subScorers[rchild];
/* 149 */         rdoc = rscorer.docID();
/*     */       }
/* 151 */       if (ldoc < doc) {
/* 152 */         if (rdoc < ldoc) {
/* 153 */           this.subScorers[i] = rscorer;
/* 154 */           this.subScorers[rchild] = scorer;
/* 155 */           i = rchild;
/*     */         } else {
/* 157 */           this.subScorers[i] = lscorer;
/* 158 */           this.subScorers[lchild] = scorer;
/* 159 */           i = lchild;
/*     */         }
/* 161 */       } else if (rdoc < doc) {
/* 162 */         this.subScorers[i] = rscorer;
/* 163 */         this.subScorers[rchild] = scorer;
/* 164 */         i = rchild;
/*     */       } else {
/* 166 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void heapRemoveRoot()
/*     */   {
/* 173 */     if (this.numScorers == 1) {
/* 174 */       this.subScorers[0] = null;
/* 175 */       this.numScorers = 0;
/*     */     } else {
/* 177 */       this.subScorers[0] = this.subScorers[(this.numScorers - 1)];
/* 178 */       this.subScorers[(this.numScorers - 1)] = null;
/* 179 */       this.numScorers -= 1;
/* 180 */       heapAdjust(0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.DisjunctionMaxScorer
 * JD-Core Version:    0.6.2
 */