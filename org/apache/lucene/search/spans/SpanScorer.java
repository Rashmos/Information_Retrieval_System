/*     */ package org.apache.lucene.search.spans;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.search.Explanation;
/*     */ import org.apache.lucene.search.Scorer;
/*     */ import org.apache.lucene.search.Similarity;
/*     */ import org.apache.lucene.search.Weight;
/*     */ 
/*     */ public class SpanScorer extends Scorer
/*     */ {
/*     */   protected Spans spans;
/*     */   protected Weight weight;
/*     */   protected byte[] norms;
/*     */   protected float value;
/*  36 */   protected boolean more = true;
/*     */   protected int doc;
/*     */   protected float freq;
/*     */ 
/*     */   protected SpanScorer(Spans spans, Weight weight, Similarity similarity, byte[] norms)
/*     */     throws IOException
/*     */   {
/*  43 */     super(similarity);
/*  44 */     this.spans = spans;
/*  45 */     this.norms = norms;
/*  46 */     this.weight = weight;
/*  47 */     this.value = weight.getValue();
/*  48 */     if (this.spans.next()) {
/*  49 */       this.doc = -1;
/*     */     } else {
/*  51 */       this.doc = 2147483647;
/*  52 */       this.more = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int nextDoc() throws IOException
/*     */   {
/*  58 */     if (!setFreqCurrentDoc()) {
/*  59 */       this.doc = 2147483647;
/*     */     }
/*  61 */     return this.doc;
/*     */   }
/*     */ 
/*     */   public int advance(int target) throws IOException
/*     */   {
/*  66 */     if (!this.more) {
/*  67 */       return this.doc = 2147483647;
/*     */     }
/*  69 */     if (this.spans.doc() < target) {
/*  70 */       this.more = this.spans.skipTo(target);
/*     */     }
/*  72 */     if (!setFreqCurrentDoc()) {
/*  73 */       this.doc = 2147483647;
/*     */     }
/*  75 */     return this.doc;
/*     */   }
/*     */ 
/*     */   protected boolean setFreqCurrentDoc() throws IOException {
/*  79 */     if (!this.more) {
/*  80 */       return false;
/*     */     }
/*  82 */     this.doc = this.spans.doc();
/*  83 */     this.freq = 0.0F;
/*     */     do {
/*  85 */       int matchLength = this.spans.end() - this.spans.start();
/*  86 */       this.freq += getSimilarity().sloppyFreq(matchLength);
/*  87 */       this.more = this.spans.next();
/*  88 */     }while ((this.more) && (this.doc == this.spans.doc()));
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   public int docID() {
/*  93 */     return this.doc;
/*     */   }
/*     */ 
/*     */   public float score() throws IOException {
/*  97 */     float raw = getSimilarity().tf(this.freq) * this.value;
/*  98 */     return this.norms == null ? raw : raw * Similarity.decodeNorm(this.norms[this.doc]);
/*     */   }
/*     */ 
/*     */   protected Explanation explain(int doc)
/*     */     throws IOException
/*     */   {
/* 104 */     Explanation tfExplanation = new Explanation();
/*     */ 
/* 106 */     int expDoc = advance(doc);
/*     */ 
/* 108 */     float phraseFreq = expDoc == doc ? this.freq : 0.0F;
/* 109 */     tfExplanation.setValue(getSimilarity().tf(phraseFreq));
/* 110 */     tfExplanation.setDescription("tf(phraseFreq=" + phraseFreq + ")");
/*     */ 
/* 112 */     return tfExplanation;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.spans.SpanScorer
 * JD-Core Version:    0.6.2
 */