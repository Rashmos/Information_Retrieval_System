/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.search.ComplexExplanation;
/*     */ import org.apache.lucene.search.Explanation;
/*     */ import org.apache.lucene.search.Query;
/*     */ import org.apache.lucene.search.Scorer;
/*     */ import org.apache.lucene.search.Searcher;
/*     */ import org.apache.lucene.search.Similarity;
/*     */ import org.apache.lucene.search.Weight;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class CustomScoreQuery extends Query
/*     */ {
/*     */   private Query subQuery;
/*     */   private ValueSourceQuery[] valSrcQueries;
/*  54 */   private boolean strict = false;
/*     */ 
/*     */   public CustomScoreQuery(Query subQuery)
/*     */   {
/*  61 */     this(subQuery, new ValueSourceQuery[0]);
/*     */   }
/*     */ 
/*     */   public CustomScoreQuery(Query subQuery, ValueSourceQuery valSrcQuery)
/*     */   {
/*  73 */     this(subQuery, valSrcQuery != null ? new ValueSourceQuery[] { valSrcQuery } : new ValueSourceQuery[0]);
/*     */   }
/*     */ 
/*     */   public CustomScoreQuery(Query subQuery, ValueSourceQuery[] valSrcQueries)
/*     */   {
/*  86 */     this.subQuery = subQuery;
/*  87 */     this.valSrcQueries = (valSrcQueries != null ? valSrcQueries : new ValueSourceQuery[0]);
/*     */ 
/*  89 */     if (subQuery == null) throw new IllegalArgumentException("<subquery> must not be null!");
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  95 */     CustomScoreQuery clone = null;
/*     */ 
/*  97 */     Query sq = this.subQuery.rewrite(reader);
/*  98 */     if (sq != this.subQuery) {
/*  99 */       clone = (CustomScoreQuery)clone();
/* 100 */       clone.subQuery = sq;
/*     */     }
/*     */ 
/* 103 */     for (int i = 0; i < this.valSrcQueries.length; i++) {
/* 104 */       ValueSourceQuery v = (ValueSourceQuery)this.valSrcQueries[i].rewrite(reader);
/* 105 */       if (v != this.valSrcQueries[i]) {
/* 106 */         if (clone == null) clone = (CustomScoreQuery)clone();
/* 107 */         clone.valSrcQueries[i] = v;
/*     */       }
/*     */     }
/*     */ 
/* 111 */     return clone == null ? this : clone;
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms)
/*     */   {
/* 117 */     this.subQuery.extractTerms(terms);
/* 118 */     for (int i = 0; i < this.valSrcQueries.length; i++)
/* 119 */       this.valSrcQueries[i].extractTerms(terms);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 126 */     CustomScoreQuery clone = (CustomScoreQuery)super.clone();
/* 127 */     clone.subQuery = ((Query)this.subQuery.clone());
/* 128 */     clone.valSrcQueries = new ValueSourceQuery[this.valSrcQueries.length];
/* 129 */     for (int i = 0; i < this.valSrcQueries.length; i++) {
/* 130 */       clone.valSrcQueries[i] = ((ValueSourceQuery)this.valSrcQueries[i].clone());
/*     */     }
/* 132 */     return clone;
/*     */   }
/*     */ 
/*     */   public String toString(String field)
/*     */   {
/* 138 */     StringBuilder sb = new StringBuilder(name()).append("(");
/* 139 */     sb.append(this.subQuery.toString(field));
/* 140 */     for (int i = 0; i < this.valSrcQueries.length; i++) {
/* 141 */       sb.append(", ").append(this.valSrcQueries[i].toString(field));
/*     */     }
/* 143 */     sb.append(")");
/* 144 */     sb.append(this.strict ? " STRICT" : "");
/* 145 */     return sb.toString() + ToStringUtils.boost(getBoost());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 151 */     if (getClass() != o.getClass()) {
/* 152 */       return false;
/*     */     }
/* 154 */     CustomScoreQuery other = (CustomScoreQuery)o;
/* 155 */     if ((getBoost() != other.getBoost()) || (!this.subQuery.equals(other.subQuery)) || (this.strict != other.strict) || (this.valSrcQueries.length != other.valSrcQueries.length))
/*     */     {
/* 159 */       return false;
/*     */     }
/* 161 */     return Arrays.equals(this.valSrcQueries, other.valSrcQueries);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 167 */     return getClass().hashCode() + this.subQuery.hashCode() + Arrays.hashCode(this.valSrcQueries) ^ Float.floatToIntBits(getBoost()) ^ (this.strict ? 1234 : 4321);
/*     */   }
/*     */ 
/*     */   protected CustomScoreProvider getCustomScoreProvider(IndexReader reader)
/*     */     throws IOException
/*     */   {
/* 179 */     return new CustomScoreProvider(reader)
/*     */     {
/*     */       public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException
/*     */       {
/* 183 */         return CustomScoreQuery.this.customScore(doc, subQueryScore, valSrcScores);
/*     */       }
/*     */ 
/*     */       public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException
/*     */       {
/* 188 */         return CustomScoreQuery.this.customScore(doc, subQueryScore, valSrcScore);
/*     */       }
/*     */ 
/*     */       public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation[] valSrcExpls) throws IOException
/*     */       {
/* 193 */         return CustomScoreQuery.this.customExplain(doc, subQueryExpl, valSrcExpls);
/*     */       }
/*     */ 
/*     */       public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation valSrcExpl) throws IOException
/*     */       {
/* 198 */         return CustomScoreQuery.this.customExplain(doc, subQueryExpl, valSrcExpl);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public float customScore(int doc, float subQueryScore, float[] valSrcScores)
/*     */   {
/* 216 */     if (valSrcScores.length == 1) {
/* 217 */       return customScore(doc, subQueryScore, valSrcScores[0]);
/*     */     }
/* 219 */     if (valSrcScores.length == 0) {
/* 220 */       return customScore(doc, subQueryScore, 1.0F);
/*     */     }
/* 222 */     float score = subQueryScore;
/* 223 */     for (int i = 0; i < valSrcScores.length; i++) {
/* 224 */       score *= valSrcScores[i];
/*     */     }
/* 226 */     return score;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public float customScore(int doc, float subQueryScore, float valSrcScore)
/*     */   {
/* 240 */     return subQueryScore * valSrcScore;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation[] valSrcExpls)
/*     */   {
/* 254 */     if (valSrcExpls.length == 1) {
/* 255 */       return customExplain(doc, subQueryExpl, valSrcExpls[0]);
/*     */     }
/* 257 */     if (valSrcExpls.length == 0) {
/* 258 */       return subQueryExpl;
/*     */     }
/* 260 */     float valSrcScore = 1.0F;
/* 261 */     for (int i = 0; i < valSrcExpls.length; i++) {
/* 262 */       valSrcScore *= valSrcExpls[i].getValue();
/*     */     }
/* 264 */     Explanation exp = new Explanation(valSrcScore * subQueryExpl.getValue(), "custom score: product of:");
/* 265 */     exp.addDetail(subQueryExpl);
/* 266 */     for (int i = 0; i < valSrcExpls.length; i++) {
/* 267 */       exp.addDetail(valSrcExpls[i]);
/*     */     }
/* 269 */     return exp;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation valSrcExpl)
/*     */   {
/* 283 */     float valSrcScore = 1.0F;
/* 284 */     if (valSrcExpl != null) {
/* 285 */       valSrcScore *= valSrcExpl.getValue();
/*     */     }
/* 287 */     Explanation exp = new Explanation(valSrcScore * subQueryExpl.getValue(), "custom score: product of:");
/* 288 */     exp.addDetail(subQueryExpl);
/* 289 */     exp.addDetail(valSrcExpl);
/* 290 */     return exp;
/*     */   }
/*     */ 
/*     */   public Weight createWeight(Searcher searcher)
/*     */     throws IOException
/*     */   {
/* 467 */     return new CustomWeight(searcher);
/*     */   }
/*     */ 
/*     */   public boolean isStrict()
/*     */   {
/* 480 */     return this.strict;
/*     */   }
/*     */ 
/*     */   public void setStrict(boolean strict)
/*     */   {
/* 489 */     this.strict = strict;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 496 */     return "custom";
/*     */   }
/*     */ 
/*     */   private class CustomScorer extends Scorer
/*     */   {
/*     */     private final float qWeight;
/*     */     private Scorer subQueryScorer;
/*     */     private Scorer[] valSrcScorers;
/*     */     private IndexReader reader;
/*     */     private final CustomScoreProvider provider;
/*     */     private float[] vScores;
/*     */ 
/*     */     private CustomScorer(Similarity similarity, IndexReader reader, CustomScoreQuery.CustomWeight w, Scorer subQueryScorer, Scorer[] valSrcScorers)
/*     */       throws IOException
/*     */     {
/* 419 */       super();
/* 420 */       this.qWeight = w.getValue();
/* 421 */       this.subQueryScorer = subQueryScorer;
/* 422 */       this.valSrcScorers = valSrcScorers;
/* 423 */       this.reader = reader;
/* 424 */       this.vScores = new float[valSrcScorers.length];
/* 425 */       this.provider = CustomScoreQuery.this.getCustomScoreProvider(reader);
/*     */     }
/*     */ 
/*     */     public int nextDoc() throws IOException
/*     */     {
/* 430 */       int doc = this.subQueryScorer.nextDoc();
/* 431 */       if (doc != 2147483647) {
/* 432 */         for (int i = 0; i < this.valSrcScorers.length; i++) {
/* 433 */           this.valSrcScorers[i].advance(doc);
/*     */         }
/*     */       }
/* 436 */       return doc;
/*     */     }
/*     */ 
/*     */     public int docID()
/*     */     {
/* 441 */       return this.subQueryScorer.docID();
/*     */     }
/*     */ 
/*     */     public float score()
/*     */       throws IOException
/*     */     {
/* 447 */       for (int i = 0; i < this.valSrcScorers.length; i++) {
/* 448 */         this.vScores[i] = this.valSrcScorers[i].score();
/*     */       }
/* 450 */       return this.qWeight * this.provider.customScore(this.subQueryScorer.docID(), this.subQueryScorer.score(), this.vScores);
/*     */     }
/*     */ 
/*     */     public int advance(int target) throws IOException
/*     */     {
/* 455 */       int doc = this.subQueryScorer.advance(target);
/* 456 */       if (doc != 2147483647) {
/* 457 */         for (int i = 0; i < this.valSrcScorers.length; i++) {
/* 458 */           this.valSrcScorers[i].advance(doc);
/*     */         }
/*     */       }
/* 461 */       return doc;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class CustomWeight extends Weight
/*     */   {
/*     */     Similarity similarity;
/*     */     Weight subQueryWeight;
/*     */     Weight[] valSrcWeights;
/*     */     boolean qStrict;
/*     */ 
/*     */     public CustomWeight(Searcher searcher)
/*     */       throws IOException
/*     */     {
/* 302 */       this.similarity = CustomScoreQuery.this.getSimilarity(searcher);
/* 303 */       this.subQueryWeight = CustomScoreQuery.this.subQuery.weight(searcher);
/* 304 */       this.valSrcWeights = new Weight[CustomScoreQuery.this.valSrcQueries.length];
/* 305 */       for (int i = 0; i < CustomScoreQuery.this.valSrcQueries.length; i++) {
/* 306 */         this.valSrcWeights[i] = CustomScoreQuery.this.valSrcQueries[i].createWeight(searcher);
/*     */       }
/* 308 */       this.qStrict = CustomScoreQuery.this.strict;
/*     */     }
/*     */ 
/*     */     public Query getQuery()
/*     */     {
/* 314 */       return CustomScoreQuery.this;
/*     */     }
/*     */ 
/*     */     public float getValue()
/*     */     {
/* 320 */       return CustomScoreQuery.this.getBoost();
/*     */     }
/*     */ 
/*     */     public float sumOfSquaredWeights()
/*     */       throws IOException
/*     */     {
/* 326 */       float sum = this.subQueryWeight.sumOfSquaredWeights();
/* 327 */       for (int i = 0; i < this.valSrcWeights.length; i++) {
/* 328 */         if (this.qStrict)
/* 329 */           this.valSrcWeights[i].sumOfSquaredWeights();
/*     */         else {
/* 331 */           sum += this.valSrcWeights[i].sumOfSquaredWeights();
/*     */         }
/*     */       }
/* 334 */       sum *= CustomScoreQuery.this.getBoost() * CustomScoreQuery.this.getBoost();
/* 335 */       return sum;
/*     */     }
/*     */ 
/*     */     public void normalize(float norm)
/*     */     {
/* 341 */       norm *= CustomScoreQuery.this.getBoost();
/* 342 */       this.subQueryWeight.normalize(norm);
/* 343 */       for (int i = 0; i < this.valSrcWeights.length; i++)
/* 344 */         if (this.qStrict)
/* 345 */           this.valSrcWeights[i].normalize(1.0F);
/*     */         else
/* 347 */           this.valSrcWeights[i].normalize(norm);
/*     */     }
/*     */ 
/*     */     public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer)
/*     */       throws IOException
/*     */     {
/* 359 */       Scorer subQueryScorer = this.subQueryWeight.scorer(reader, true, false);
/* 360 */       if (subQueryScorer == null) {
/* 361 */         return null;
/*     */       }
/* 363 */       Scorer[] valSrcScorers = new Scorer[this.valSrcWeights.length];
/* 364 */       for (int i = 0; i < valSrcScorers.length; i++) {
/* 365 */         valSrcScorers[i] = this.valSrcWeights[i].scorer(reader, true, topScorer);
/*     */       }
/* 367 */       return new CustomScoreQuery.CustomScorer(CustomScoreQuery.this, this.similarity, reader, this, subQueryScorer, valSrcScorers, null);
/*     */     }
/*     */ 
/*     */     public Explanation explain(IndexReader reader, int doc) throws IOException
/*     */     {
/* 372 */       Explanation explain = doExplain(reader, doc);
/* 373 */       return explain == null ? new Explanation(0.0F, "no matching docs") : explain;
/*     */     }
/*     */ 
/*     */     private Explanation doExplain(IndexReader reader, int doc) throws IOException {
/* 377 */       Explanation subQueryExpl = this.subQueryWeight.explain(reader, doc);
/* 378 */       if (!subQueryExpl.isMatch()) {
/* 379 */         return subQueryExpl;
/*     */       }
/*     */ 
/* 382 */       Explanation[] valSrcExpls = new Explanation[this.valSrcWeights.length];
/* 383 */       for (int i = 0; i < this.valSrcWeights.length; i++) {
/* 384 */         valSrcExpls[i] = this.valSrcWeights[i].explain(reader, doc);
/*     */       }
/* 386 */       Explanation customExp = CustomScoreQuery.this.getCustomScoreProvider(reader).customExplain(doc, subQueryExpl, valSrcExpls);
/* 387 */       float sc = getValue() * customExp.getValue();
/* 388 */       Explanation res = new ComplexExplanation(true, sc, CustomScoreQuery.this.toString() + ", product of:");
/*     */ 
/* 390 */       res.addDetail(customExp);
/* 391 */       res.addDetail(new Explanation(getValue(), "queryBoost"));
/* 392 */       return res;
/*     */     }
/*     */ 
/*     */     public boolean scoresDocsOutOfOrder()
/*     */     {
/* 397 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.CustomScoreQuery
 * JD-Core Version:    0.6.2
 */