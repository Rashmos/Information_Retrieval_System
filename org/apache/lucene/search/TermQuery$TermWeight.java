/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.index.TermDocs;
/*     */ 
/*     */ class TermQuery$TermWeight extends Weight
/*     */ {
/*     */   private Similarity similarity;
/*     */   private float value;
/*     */   private float idf;
/*     */   private float queryNorm;
/*     */   private float queryWeight;
/*     */   private Explanation.IDFExplanation idfExp;
/*     */ 
/*     */   public TermQuery$TermWeight(TermQuery paramTermQuery, Searcher searcher)
/*     */     throws IOException
/*     */   {
/*  45 */     this.similarity = paramTermQuery.getSimilarity(searcher);
/*  46 */     this.idfExp = this.similarity.idfExplain(TermQuery.access$000(paramTermQuery), searcher);
/*  47 */     this.idf = this.idfExp.getIdf();
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  51 */     return "weight(" + this.this$0 + ")";
/*     */   }
/*     */   public Query getQuery() {
/*  54 */     return this.this$0;
/*     */   }
/*     */   public float getValue() {
/*  57 */     return this.value;
/*     */   }
/*     */ 
/*     */   public float sumOfSquaredWeights() {
/*  61 */     this.queryWeight = (this.idf * this.this$0.getBoost());
/*  62 */     return this.queryWeight * this.queryWeight;
/*     */   }
/*     */ 
/*     */   public void normalize(float queryNorm)
/*     */   {
/*  67 */     this.queryNorm = queryNorm;
/*  68 */     this.queryWeight *= queryNorm;
/*  69 */     this.value = (this.queryWeight * this.idf);
/*     */   }
/*     */ 
/*     */   public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer) throws IOException
/*     */   {
/*  74 */     TermDocs termDocs = reader.termDocs(TermQuery.access$000(this.this$0));
/*     */ 
/*  76 */     if (termDocs == null) {
/*  77 */       return null;
/*     */     }
/*  79 */     return new TermScorer(this, termDocs, this.similarity, reader.norms(TermQuery.access$000(this.this$0).field()));
/*     */   }
/*     */ 
/*     */   public Explanation explain(IndexReader reader, int doc)
/*     */     throws IOException
/*     */   {
/*  86 */     ComplexExplanation result = new ComplexExplanation();
/*  87 */     result.setDescription("weight(" + getQuery() + " in " + doc + "), product of:");
/*     */ 
/*  89 */     Explanation expl = new Explanation(this.idf, this.idfExp.explain());
/*     */ 
/*  92 */     Explanation queryExpl = new Explanation();
/*  93 */     queryExpl.setDescription("queryWeight(" + getQuery() + "), product of:");
/*     */ 
/*  95 */     Explanation boostExpl = new Explanation(this.this$0.getBoost(), "boost");
/*  96 */     if (this.this$0.getBoost() != 1.0F)
/*  97 */       queryExpl.addDetail(boostExpl);
/*  98 */     queryExpl.addDetail(expl);
/*     */ 
/* 100 */     Explanation queryNormExpl = new Explanation(this.queryNorm, "queryNorm");
/* 101 */     queryExpl.addDetail(queryNormExpl);
/*     */ 
/* 103 */     queryExpl.setValue(boostExpl.getValue() * expl.getValue() * queryNormExpl.getValue());
/*     */ 
/* 107 */     result.addDetail(queryExpl);
/*     */ 
/* 110 */     String field = TermQuery.access$000(this.this$0).field();
/* 111 */     ComplexExplanation fieldExpl = new ComplexExplanation();
/* 112 */     fieldExpl.setDescription("fieldWeight(" + TermQuery.access$000(this.this$0) + " in " + doc + "), product of:");
/*     */ 
/* 115 */     Explanation tfExplanation = new Explanation();
/* 116 */     int tf = 0;
/* 117 */     TermDocs termDocs = reader.termDocs(TermQuery.access$000(this.this$0));
/* 118 */     if (termDocs != null) {
/*     */       try {
/* 120 */         if ((termDocs.skipTo(doc)) && (termDocs.doc() == doc))
/* 121 */           tf = termDocs.freq();
/*     */       }
/*     */       finally {
/* 124 */         termDocs.close();
/*     */       }
/* 126 */       tfExplanation.setValue(this.similarity.tf(tf));
/* 127 */       tfExplanation.setDescription("tf(termFreq(" + TermQuery.access$000(this.this$0) + ")=" + tf + ")");
/*     */     } else {
/* 129 */       tfExplanation.setValue(0.0F);
/* 130 */       tfExplanation.setDescription("no matching term");
/*     */     }
/* 132 */     fieldExpl.addDetail(tfExplanation);
/* 133 */     fieldExpl.addDetail(expl);
/*     */ 
/* 135 */     Explanation fieldNormExpl = new Explanation();
/* 136 */     byte[] fieldNorms = reader.norms(field);
/* 137 */     float fieldNorm = fieldNorms != null ? Similarity.decodeNorm(fieldNorms[doc]) : 1.0F;
/*     */ 
/* 139 */     fieldNormExpl.setValue(fieldNorm);
/* 140 */     fieldNormExpl.setDescription("fieldNorm(field=" + field + ", doc=" + doc + ")");
/* 141 */     fieldExpl.addDetail(fieldNormExpl);
/*     */ 
/* 143 */     fieldExpl.setMatch(Boolean.valueOf(tfExplanation.isMatch()));
/* 144 */     fieldExpl.setValue(tfExplanation.getValue() * expl.getValue() * fieldNormExpl.getValue());
/*     */ 
/* 148 */     result.addDetail(fieldExpl);
/* 149 */     result.setMatch(fieldExpl.getMatch());
/*     */ 
/* 152 */     result.setValue(queryExpl.getValue() * fieldExpl.getValue());
/*     */ 
/* 154 */     if (queryExpl.getValue() == 1.0F) {
/* 155 */       return fieldExpl;
/*     */     }
/* 157 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TermQuery.TermWeight
 * JD-Core Version:    0.6.2
 */