/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ 
/*     */ public class ConstantScoreQuery extends Query
/*     */ {
/*     */   protected final Filter filter;
/*     */ 
/*     */   public ConstantScoreQuery(Filter filter)
/*     */   {
/*  34 */     this.filter = filter;
/*     */   }
/*     */ 
/*     */   public Filter getFilter()
/*     */   {
/*  39 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader) throws IOException
/*     */   {
/*  44 */     return this;
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Weight createWeight(Searcher searcher)
/*     */   {
/* 158 */     return new ConstantWeight(searcher);
/*     */   }
/*     */ 
/*     */   public String toString(String field)
/*     */   {
/* 164 */     return "ConstantScore(" + this.filter.toString() + (getBoost() == 1.0D ? ")" : new StringBuilder().append("^").append(getBoost()).toString());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 171 */     if (this == o) return true;
/* 172 */     if (!(o instanceof ConstantScoreQuery)) return false;
/* 173 */     ConstantScoreQuery other = (ConstantScoreQuery)o;
/* 174 */     return (getBoost() == other.getBoost()) && (this.filter.equals(other.filter));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 181 */     return this.filter.hashCode() + Float.floatToIntBits(getBoost());
/*     */   }
/*     */ 
/*     */   protected class ConstantScorer extends Scorer
/*     */   {
/*     */     final DocIdSetIterator docIdSetIterator;
/*     */     final float theScore;
/* 117 */     int doc = -1;
/*     */ 
/*     */     public ConstantScorer(Similarity similarity, IndexReader reader, Weight w) throws IOException {
/* 120 */       super();
/* 121 */       this.theScore = w.getValue();
/* 122 */       DocIdSet docIdSet = ConstantScoreQuery.this.filter.getDocIdSet(reader);
/* 123 */       if (docIdSet == null) {
/* 124 */         this.docIdSetIterator = DocIdSet.EMPTY_DOCIDSET.iterator();
/*     */       } else {
/* 126 */         DocIdSetIterator iter = docIdSet.iterator();
/* 127 */         if (iter == null)
/* 128 */           this.docIdSetIterator = DocIdSet.EMPTY_DOCIDSET.iterator();
/*     */         else
/* 130 */           this.docIdSetIterator = iter;
/*     */       }
/*     */     }
/*     */ 
/*     */     public int nextDoc()
/*     */       throws IOException
/*     */     {
/* 137 */       return this.docIdSetIterator.nextDoc();
/*     */     }
/*     */ 
/*     */     public int docID()
/*     */     {
/* 142 */       return this.docIdSetIterator.docID();
/*     */     }
/*     */ 
/*     */     public float score() throws IOException
/*     */     {
/* 147 */       return this.theScore;
/*     */     }
/*     */ 
/*     */     public int advance(int target) throws IOException
/*     */     {
/* 152 */       return this.docIdSetIterator.advance(target);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class ConstantWeight extends Weight
/*     */   {
/*     */     private Similarity similarity;
/*     */     private float queryNorm;
/*     */     private float queryWeight;
/*     */ 
/*     */     public ConstantWeight(Searcher searcher)
/*     */     {
/*  59 */       this.similarity = ConstantScoreQuery.this.getSimilarity(searcher);
/*     */     }
/*     */ 
/*     */     public Query getQuery()
/*     */     {
/*  64 */       return ConstantScoreQuery.this;
/*     */     }
/*     */ 
/*     */     public float getValue()
/*     */     {
/*  69 */       return this.queryWeight;
/*     */     }
/*     */ 
/*     */     public float sumOfSquaredWeights() throws IOException
/*     */     {
/*  74 */       this.queryWeight = ConstantScoreQuery.this.getBoost();
/*  75 */       return this.queryWeight * this.queryWeight;
/*     */     }
/*     */ 
/*     */     public void normalize(float norm)
/*     */     {
/*  80 */       this.queryNorm = norm;
/*  81 */       this.queryWeight *= this.queryNorm;
/*     */     }
/*     */ 
/*     */     public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer) throws IOException
/*     */     {
/*  86 */       return new ConstantScoreQuery.ConstantScorer(ConstantScoreQuery.this, this.similarity, reader, this);
/*     */     }
/*     */ 
/*     */     public Explanation explain(IndexReader reader, int doc)
/*     */       throws IOException
/*     */     {
/*  92 */       ConstantScoreQuery.ConstantScorer cs = new ConstantScoreQuery.ConstantScorer(ConstantScoreQuery.this, this.similarity, reader, this);
/*  93 */       boolean exists = cs.docIdSetIterator.advance(doc) == doc;
/*     */ 
/*  95 */       ComplexExplanation result = new ComplexExplanation();
/*     */ 
/*  97 */       if (exists) {
/*  98 */         result.setDescription("ConstantScoreQuery(" + ConstantScoreQuery.this.filter + "), product of:");
/*     */ 
/* 100 */         result.setValue(this.queryWeight);
/* 101 */         result.setMatch(Boolean.TRUE);
/* 102 */         result.addDetail(new Explanation(ConstantScoreQuery.this.getBoost(), "boost"));
/* 103 */         result.addDetail(new Explanation(this.queryNorm, "queryNorm"));
/*     */       } else {
/* 105 */         result.setDescription("ConstantScoreQuery(" + ConstantScoreQuery.this.filter + ") doesn't match id " + doc);
/*     */ 
/* 107 */         result.setValue(0.0F);
/* 108 */         result.setMatch(Boolean.FALSE);
/*     */       }
/* 110 */       return result;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.ConstantScoreQuery
 * JD-Core Version:    0.6.2
 */