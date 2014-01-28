/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.index.TermDocs;
/*     */ import org.apache.lucene.search.ComplexExplanation;
/*     */ import org.apache.lucene.search.Explanation;
/*     */ import org.apache.lucene.search.Query;
/*     */ import org.apache.lucene.search.Scorer;
/*     */ import org.apache.lucene.search.Searcher;
/*     */ import org.apache.lucene.search.Similarity;
/*     */ import org.apache.lucene.search.Weight;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class ValueSourceQuery extends Query
/*     */ {
/*     */   ValueSource valSrc;
/*     */ 
/*     */   public ValueSourceQuery(ValueSource valSrc)
/*     */   {
/*  53 */     this.valSrc = valSrc;
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  59 */     return this;
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Weight createWeight(Searcher searcher)
/*     */   {
/* 171 */     return new ValueSourceWeight(searcher);
/*     */   }
/*     */ 
/*     */   public String toString(String field)
/*     */   {
/* 176 */     return this.valSrc.toString() + ToStringUtils.boost(getBoost());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 182 */     if (getClass() != o.getClass()) {
/* 183 */       return false;
/*     */     }
/* 185 */     ValueSourceQuery other = (ValueSourceQuery)o;
/* 186 */     return (getBoost() == other.getBoost()) && (this.valSrc.equals(other.valSrc));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 193 */     return getClass().hashCode() + this.valSrc.hashCode() ^ Float.floatToIntBits(getBoost());
/*     */   }
/*     */ 
/*     */   private class ValueSourceScorer extends Scorer
/*     */   {
/*     */     private final ValueSourceQuery.ValueSourceWeight weight;
/*     */     private final float qWeight;
/*     */     private final DocValues vals;
/*     */     private final TermDocs termDocs;
/* 135 */     private int doc = -1;
/*     */ 
/*     */     private ValueSourceScorer(Similarity similarity, IndexReader reader, ValueSourceQuery.ValueSourceWeight w) throws IOException
/*     */     {
/* 139 */       super();
/* 140 */       this.weight = w;
/* 141 */       this.qWeight = w.getValue();
/*     */ 
/* 143 */       this.vals = ValueSourceQuery.this.valSrc.getValues(reader);
/* 144 */       this.termDocs = reader.termDocs(null);
/*     */     }
/*     */ 
/*     */     public int nextDoc() throws IOException
/*     */     {
/* 149 */       return this.doc = this.termDocs.next() ? this.termDocs.doc() : 2147483647;
/*     */     }
/*     */ 
/*     */     public int docID()
/*     */     {
/* 154 */       return this.doc;
/*     */     }
/*     */ 
/*     */     public int advance(int target) throws IOException
/*     */     {
/* 159 */       return this.doc = this.termDocs.skipTo(target) ? this.termDocs.doc() : 2147483647;
/*     */     }
/*     */ 
/*     */     public float score()
/*     */       throws IOException
/*     */     {
/* 165 */       return this.qWeight * this.vals.floatVal(this.termDocs.doc());
/*     */     }
/*     */   }
/*     */ 
/*     */   class ValueSourceWeight extends Weight
/*     */   {
/*     */     Similarity similarity;
/*     */     float queryNorm;
/*     */     float queryWeight;
/*     */ 
/*     */     public ValueSourceWeight(Searcher searcher)
/*     */     {
/*  74 */       this.similarity = ValueSourceQuery.this.getSimilarity(searcher);
/*     */     }
/*     */ 
/*     */     public Query getQuery()
/*     */     {
/*  80 */       return ValueSourceQuery.this;
/*     */     }
/*     */ 
/*     */     public float getValue()
/*     */     {
/*  86 */       return this.queryWeight;
/*     */     }
/*     */ 
/*     */     public float sumOfSquaredWeights()
/*     */       throws IOException
/*     */     {
/*  92 */       this.queryWeight = ValueSourceQuery.this.getBoost();
/*  93 */       return this.queryWeight * this.queryWeight;
/*     */     }
/*     */ 
/*     */     public void normalize(float norm)
/*     */     {
/*  99 */       this.queryNorm = norm;
/* 100 */       this.queryWeight *= this.queryNorm;
/*     */     }
/*     */ 
/*     */     public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer) throws IOException
/*     */     {
/* 105 */       return new ValueSourceQuery.ValueSourceScorer(ValueSourceQuery.this, this.similarity, reader, this, null);
/*     */     }
/*     */ 
/*     */     public Explanation explain(IndexReader reader, int doc)
/*     */       throws IOException
/*     */     {
/* 111 */       DocValues vals = ValueSourceQuery.this.valSrc.getValues(reader);
/* 112 */       float sc = this.queryWeight * vals.floatVal(doc);
/*     */ 
/* 114 */       Explanation result = new ComplexExplanation(true, sc, ValueSourceQuery.this.toString() + ", product of:");
/*     */ 
/* 117 */       result.addDetail(vals.explain(doc));
/* 118 */       result.addDetail(new Explanation(ValueSourceQuery.this.getBoost(), "boost"));
/* 119 */       result.addDetail(new Explanation(this.queryNorm, "queryNorm"));
/* 120 */       return result;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.ValueSourceQuery
 * JD-Core Version:    0.6.2
 */