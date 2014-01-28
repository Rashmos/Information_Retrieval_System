/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.index.TermPositions;
/*     */ 
/*     */ class PhraseQuery$PhraseWeight extends Weight
/*     */ {
/*     */   private Similarity similarity;
/*     */   private float value;
/*     */   private float idf;
/*     */   private float queryNorm;
/*     */   private float queryWeight;
/*     */   private Explanation.IDFExplanation idfExp;
/*     */ 
/*     */   public PhraseQuery$PhraseWeight(PhraseQuery paramPhraseQuery, Searcher searcher)
/*     */     throws IOException
/*     */   {
/* 120 */     this.similarity = paramPhraseQuery.getSimilarity(searcher);
/*     */ 
/* 122 */     this.idfExp = this.similarity.idfExplain(PhraseQuery.access$000(paramPhraseQuery), searcher);
/* 123 */     this.idf = this.idfExp.getIdf();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 127 */     return "weight(" + this.this$0 + ")";
/*     */   }
/*     */   public Query getQuery() {
/* 130 */     return this.this$0;
/*     */   }
/*     */   public float getValue() {
/* 133 */     return this.value;
/*     */   }
/*     */ 
/*     */   public float sumOfSquaredWeights() {
/* 137 */     this.queryWeight = (this.idf * this.this$0.getBoost());
/* 138 */     return this.queryWeight * this.queryWeight;
/*     */   }
/*     */ 
/*     */   public void normalize(float queryNorm)
/*     */   {
/* 143 */     this.queryNorm = queryNorm;
/* 144 */     this.queryWeight *= queryNorm;
/* 145 */     this.value = (this.queryWeight * this.idf);
/*     */   }
/*     */ 
/*     */   public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer) throws IOException
/*     */   {
/* 150 */     if (PhraseQuery.access$000(this.this$0).size() == 0) {
/* 151 */       return null;
/*     */     }
/* 153 */     TermPositions[] tps = new TermPositions[PhraseQuery.access$000(this.this$0).size()];
/* 154 */     for (int i = 0; i < PhraseQuery.access$000(this.this$0).size(); i++) {
/* 155 */       TermPositions p = reader.termPositions((Term)PhraseQuery.access$000(this.this$0).get(i));
/* 156 */       if (p == null)
/* 157 */         return null;
/* 158 */       tps[i] = p;
/*     */     }
/*     */ 
/* 161 */     if (PhraseQuery.access$100(this.this$0) == 0) {
/* 162 */       return new ExactPhraseScorer(this, tps, this.this$0.getPositions(), this.similarity, reader.norms(PhraseQuery.access$200(this.this$0)));
/*     */     }
/*     */ 
/* 165 */     return new SloppyPhraseScorer(this, tps, this.this$0.getPositions(), this.similarity, PhraseQuery.access$100(this.this$0), reader.norms(PhraseQuery.access$200(this.this$0)));
/*     */   }
/*     */ 
/*     */   public Explanation explain(IndexReader reader, int doc)
/*     */     throws IOException
/*     */   {
/* 175 */     Explanation result = new Explanation();
/* 176 */     result.setDescription("weight(" + getQuery() + " in " + doc + "), product of:");
/*     */ 
/* 178 */     StringBuilder docFreqs = new StringBuilder();
/* 179 */     StringBuilder query = new StringBuilder();
/* 180 */     query.append('"');
/* 181 */     docFreqs.append(this.idfExp.explain());
/* 182 */     for (int i = 0; i < PhraseQuery.access$000(this.this$0).size(); i++) {
/* 183 */       if (i != 0) {
/* 184 */         query.append(" ");
/*     */       }
/*     */ 
/* 187 */       Term term = (Term)PhraseQuery.access$000(this.this$0).get(i);
/*     */ 
/* 189 */       query.append(term.text());
/*     */     }
/* 191 */     query.append('"');
/*     */ 
/* 193 */     Explanation idfExpl = new Explanation(this.idf, "idf(" + PhraseQuery.access$200(this.this$0) + ":" + docFreqs + ")");
/*     */ 
/* 197 */     Explanation queryExpl = new Explanation();
/* 198 */     queryExpl.setDescription("queryWeight(" + getQuery() + "), product of:");
/*     */ 
/* 200 */     Explanation boostExpl = new Explanation(this.this$0.getBoost(), "boost");
/* 201 */     if (this.this$0.getBoost() != 1.0F)
/* 202 */       queryExpl.addDetail(boostExpl);
/* 203 */     queryExpl.addDetail(idfExpl);
/*     */ 
/* 205 */     Explanation queryNormExpl = new Explanation(this.queryNorm, "queryNorm");
/* 206 */     queryExpl.addDetail(queryNormExpl);
/*     */ 
/* 208 */     queryExpl.setValue(boostExpl.getValue() * idfExpl.getValue() * queryNormExpl.getValue());
/*     */ 
/* 212 */     result.addDetail(queryExpl);
/*     */ 
/* 215 */     Explanation fieldExpl = new Explanation();
/* 216 */     fieldExpl.setDescription("fieldWeight(" + PhraseQuery.access$200(this.this$0) + ":" + query + " in " + doc + "), product of:");
/*     */ 
/* 219 */     PhraseScorer scorer = (PhraseScorer)scorer(reader, true, false);
/* 220 */     if (scorer == null) {
/* 221 */       return new Explanation(0.0F, "no matching docs");
/*     */     }
/* 223 */     Explanation tfExplanation = new Explanation();
/* 224 */     int d = scorer.advance(doc);
/* 225 */     float phraseFreq = d == doc ? scorer.currentFreq() : 0.0F;
/* 226 */     tfExplanation.setValue(this.similarity.tf(phraseFreq));
/* 227 */     tfExplanation.setDescription("tf(phraseFreq=" + phraseFreq + ")");
/*     */ 
/* 229 */     fieldExpl.addDetail(tfExplanation);
/* 230 */     fieldExpl.addDetail(idfExpl);
/*     */ 
/* 232 */     Explanation fieldNormExpl = new Explanation();
/* 233 */     byte[] fieldNorms = reader.norms(PhraseQuery.access$200(this.this$0));
/* 234 */     float fieldNorm = fieldNorms != null ? Similarity.decodeNorm(fieldNorms[doc]) : 1.0F;
/*     */ 
/* 236 */     fieldNormExpl.setValue(fieldNorm);
/* 237 */     fieldNormExpl.setDescription("fieldNorm(field=" + PhraseQuery.access$200(this.this$0) + ", doc=" + doc + ")");
/* 238 */     fieldExpl.addDetail(fieldNormExpl);
/*     */ 
/* 240 */     fieldExpl.setValue(tfExplanation.getValue() * idfExpl.getValue() * fieldNormExpl.getValue());
/*     */ 
/* 244 */     result.addDetail(fieldExpl);
/*     */ 
/* 247 */     result.setValue(queryExpl.getValue() * fieldExpl.getValue());
/*     */ 
/* 249 */     if (queryExpl.getValue() == 1.0F) {
/* 250 */       return fieldExpl;
/*     */     }
/* 252 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.PhraseQuery.PhraseWeight
 * JD-Core Version:    0.6.2
 */