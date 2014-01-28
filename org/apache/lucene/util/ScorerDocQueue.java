/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.search.Scorer;
/*     */ 
/*     */ public class ScorerDocQueue
/*     */ {
/*     */   private final HeapedScorerDoc[] heap;
/*     */   private final int maxSize;
/*     */   private int size;
/*     */   private HeapedScorerDoc topHSD;
/*     */ 
/*     */   public ScorerDocQueue(int maxSize)
/*     */   {
/*  55 */     this.size = 0;
/*  56 */     int heapSize = maxSize + 1;
/*  57 */     this.heap = new HeapedScorerDoc[heapSize];
/*  58 */     this.maxSize = maxSize;
/*  59 */     this.topHSD = this.heap[1];
/*     */   }
/*     */ 
/*     */   public final void put(Scorer scorer)
/*     */   {
/*  68 */     this.size += 1;
/*  69 */     this.heap[this.size] = new HeapedScorerDoc(scorer);
/*  70 */     upHeap();
/*     */   }
/*     */ 
/*     */   public boolean insert(Scorer scorer)
/*     */   {
/*  80 */     if (this.size < this.maxSize) {
/*  81 */       put(scorer);
/*  82 */       return true;
/*     */     }
/*  84 */     int docNr = scorer.docID();
/*  85 */     if ((this.size > 0) && (docNr >= this.topHSD.doc)) {
/*  86 */       this.heap[1] = new HeapedScorerDoc(scorer, docNr);
/*  87 */       downHeap();
/*  88 */       return true;
/*     */     }
/*  90 */     return false;
/*     */   }
/*     */ 
/*     */   public final Scorer top()
/*     */   {
/* 100 */     return this.topHSD.scorer;
/*     */   }
/*     */ 
/*     */   public final int topDoc()
/*     */   {
/* 109 */     return this.topHSD.doc;
/*     */   }
/*     */ 
/*     */   public final float topScore() throws IOException
/*     */   {
/* 114 */     return this.topHSD.scorer.score();
/*     */   }
/*     */ 
/*     */   public final boolean topNextAndAdjustElsePop() throws IOException {
/* 118 */     return checkAdjustElsePop(this.topHSD.scorer.nextDoc() != 2147483647);
/*     */   }
/*     */ 
/*     */   public final boolean topSkipToAndAdjustElsePop(int target) throws IOException {
/* 122 */     return checkAdjustElsePop(this.topHSD.scorer.advance(target) != 2147483647);
/*     */   }
/*     */ 
/*     */   private boolean checkAdjustElsePop(boolean cond) {
/* 126 */     if (cond) {
/* 127 */       this.topHSD.doc = this.topHSD.scorer.docID();
/*     */     } else {
/* 129 */       this.heap[1] = this.heap[this.size];
/* 130 */       this.heap[this.size] = null;
/* 131 */       this.size -= 1;
/*     */     }
/* 133 */     downHeap();
/* 134 */     return cond;
/*     */   }
/*     */ 
/*     */   public final Scorer pop()
/*     */   {
/* 143 */     Scorer result = this.topHSD.scorer;
/* 144 */     popNoResult();
/* 145 */     return result;
/*     */   }
/*     */ 
/*     */   private final void popNoResult()
/*     */   {
/* 152 */     this.heap[1] = this.heap[this.size];
/* 153 */     this.heap[this.size] = null;
/* 154 */     this.size -= 1;
/* 155 */     downHeap();
/*     */   }
/*     */ 
/*     */   public final void adjustTop()
/*     */   {
/* 167 */     this.topHSD.adjust();
/* 168 */     downHeap();
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/* 173 */     return this.size;
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 178 */     for (int i = 0; i <= this.size; i++) {
/* 179 */       this.heap[i] = null;
/*     */     }
/* 181 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   private final void upHeap() {
/* 185 */     int i = this.size;
/* 186 */     HeapedScorerDoc node = this.heap[i];
/* 187 */     int j = i >>> 1;
/* 188 */     while ((j > 0) && (node.doc < this.heap[j].doc)) {
/* 189 */       this.heap[i] = this.heap[j];
/* 190 */       i = j;
/* 191 */       j >>>= 1;
/*     */     }
/* 193 */     this.heap[i] = node;
/* 194 */     this.topHSD = this.heap[1];
/*     */   }
/*     */ 
/*     */   private final void downHeap() {
/* 198 */     int i = 1;
/* 199 */     HeapedScorerDoc node = this.heap[i];
/* 200 */     int j = i << 1;
/* 201 */     int k = j + 1;
/* 202 */     if ((k <= this.size) && (this.heap[k].doc < this.heap[j].doc)) {
/* 203 */       j = k;
/*     */     }
/* 205 */     while ((j <= this.size) && (this.heap[j].doc < node.doc)) {
/* 206 */       this.heap[i] = this.heap[j];
/* 207 */       i = j;
/* 208 */       j = i << 1;
/* 209 */       k = j + 1;
/* 210 */       if ((k <= this.size) && (this.heap[k].doc < this.heap[j].doc)) {
/* 211 */         j = k;
/*     */       }
/*     */     }
/* 214 */     this.heap[i] = node;
/* 215 */     this.topHSD = this.heap[1];
/*     */   }
/*     */ 
/*     */   private class HeapedScorerDoc
/*     */   {
/*     */     Scorer scorer;
/*     */     int doc;
/*     */ 
/*     */     HeapedScorerDoc(Scorer s)
/*     */     {
/*  40 */       this(s, s.docID());
/*     */     }
/*     */     HeapedScorerDoc(Scorer scorer, int doc) {
/*  43 */       this.scorer = scorer;
/*  44 */       this.doc = doc;
/*     */     }
/*     */     void adjust() {
/*  47 */       this.doc = this.scorer.docID();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.ScorerDocQueue
 * JD-Core Version:    0.6.2
 */