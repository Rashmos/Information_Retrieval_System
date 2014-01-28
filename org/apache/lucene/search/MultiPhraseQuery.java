/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.MultipleTermPositions;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.index.TermPositions;
/*     */ import org.apache.lucene.util.ToStringUtils;
/*     */ 
/*     */ public class MultiPhraseQuery extends Query
/*     */ {
/*     */   private String field;
/*     */   private ArrayList<Term[]> termArrays;
/*     */   private ArrayList<Integer> positions;
/*     */   private int slop;
/*     */ 
/*     */   public MultiPhraseQuery()
/*     */   {
/*  41 */     this.termArrays = new ArrayList();
/*  42 */     this.positions = new ArrayList();
/*     */ 
/*  44 */     this.slop = 0;
/*     */   }
/*     */ 
/*     */   public void setSlop(int s)
/*     */   {
/*  49 */     this.slop = s;
/*     */   }
/*     */ 
/*     */   public int getSlop()
/*     */   {
/*  54 */     return this.slop;
/*     */   }
/*     */ 
/*     */   public void add(Term term)
/*     */   {
/*  59 */     add(new Term[] { term });
/*     */   }
/*     */ 
/*     */   public void add(Term[] terms)
/*     */   {
/*  67 */     int position = 0;
/*  68 */     if (this.positions.size() > 0) {
/*  69 */       position = ((Integer)this.positions.get(this.positions.size() - 1)).intValue() + 1;
/*     */     }
/*  71 */     add(terms, position);
/*     */   }
/*     */ 
/*     */   public void add(Term[] terms, int position)
/*     */   {
/*  82 */     if (this.termArrays.size() == 0) {
/*  83 */       this.field = terms[0].field();
/*     */     }
/*  85 */     for (int i = 0; i < terms.length; i++) {
/*  86 */       if (terms[i].field() != this.field) {
/*  87 */         throw new IllegalArgumentException("All phrase terms must be in the same field (" + this.field + "): " + terms[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  93 */     this.termArrays.add(terms);
/*  94 */     this.positions.add(Integer.valueOf(position));
/*     */   }
/*     */ 
/*     */   public List<Term[]> getTermArrays()
/*     */   {
/* 102 */     return Collections.unmodifiableList(this.termArrays);
/*     */   }
/*     */ 
/*     */   public int[] getPositions()
/*     */   {
/* 109 */     int[] result = new int[this.positions.size()];
/* 110 */     for (int i = 0; i < this.positions.size(); i++)
/* 111 */       result[i] = ((Integer)this.positions.get(i)).intValue();
/* 112 */     return result;
/*     */   }
/*     */ 
/*     */   public void extractTerms(Set<Term> terms)
/*     */   {
/* 118 */     for (Term[] arr : this.termArrays)
/* 119 */       for (Term term : arr)
/* 120 */         terms.add(term);
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader)
/*     */   {
/* 266 */     if (this.termArrays.size() == 1) {
/* 267 */       Term[] terms = (Term[])this.termArrays.get(0);
/* 268 */       BooleanQuery boq = new BooleanQuery(true);
/* 269 */       for (int i = 0; i < terms.length; i++) {
/* 270 */         boq.add(new TermQuery(terms[i]), BooleanClause.Occur.SHOULD);
/*     */       }
/* 272 */       boq.setBoost(getBoost());
/* 273 */       return boq;
/*     */     }
/* 275 */     return this;
/*     */   }
/*     */ 
/*     */   public Weight createWeight(Searcher searcher)
/*     */     throws IOException
/*     */   {
/* 281 */     return new MultiPhraseWeight(searcher);
/*     */   }
/*     */ 
/*     */   public final String toString(String f)
/*     */   {
/* 287 */     StringBuilder buffer = new StringBuilder();
/* 288 */     if (!this.field.equals(f)) {
/* 289 */       buffer.append(this.field);
/* 290 */       buffer.append(":");
/*     */     }
/*     */ 
/* 293 */     buffer.append("\"");
/* 294 */     Iterator i = this.termArrays.iterator();
/* 295 */     while (i.hasNext()) {
/* 296 */       Term[] terms = (Term[])i.next();
/* 297 */       if (terms.length > 1) {
/* 298 */         buffer.append("(");
/* 299 */         for (int j = 0; j < terms.length; j++) {
/* 300 */           buffer.append(terms[j].text());
/* 301 */           if (j < terms.length - 1)
/* 302 */             buffer.append(" ");
/*     */         }
/* 304 */         buffer.append(")");
/*     */       } else {
/* 306 */         buffer.append(terms[0].text());
/*     */       }
/* 308 */       if (i.hasNext())
/* 309 */         buffer.append(" ");
/*     */     }
/* 311 */     buffer.append("\"");
/*     */ 
/* 313 */     if (this.slop != 0) {
/* 314 */       buffer.append("~");
/* 315 */       buffer.append(this.slop);
/*     */     }
/*     */ 
/* 318 */     buffer.append(ToStringUtils.boost(getBoost()));
/*     */ 
/* 320 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 327 */     if (!(o instanceof MultiPhraseQuery)) return false;
/* 328 */     MultiPhraseQuery other = (MultiPhraseQuery)o;
/* 329 */     return (getBoost() == other.getBoost()) && (this.slop == other.slop) && (termArraysEquals(this.termArrays, other.termArrays)) && (this.positions.equals(other.positions));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 338 */     return Float.floatToIntBits(getBoost()) ^ this.slop ^ termArraysHashCode() ^ this.positions.hashCode() ^ 0x4AC65113;
/*     */   }
/*     */ 
/*     */   private int termArraysHashCode()
/*     */   {
/* 347 */     int hashCode = 1;
/* 348 */     for (Term[] termArray : this.termArrays) {
/* 349 */       hashCode = 31 * hashCode + (termArray == null ? 0 : Arrays.hashCode(termArray));
/*     */     }
/*     */ 
/* 352 */     return hashCode;
/*     */   }
/*     */ 
/*     */   private boolean termArraysEquals(List<Term[]> termArrays1, List<Term[]> termArrays2)
/*     */   {
/* 357 */     if (termArrays1.size() != termArrays2.size()) {
/* 358 */       return false;
/*     */     }
/* 360 */     ListIterator iterator1 = termArrays1.listIterator();
/* 361 */     ListIterator iterator2 = termArrays2.listIterator();
/* 362 */     while (iterator1.hasNext()) {
/* 363 */       Term[] termArray1 = (Term[])iterator1.next();
/* 364 */       Term[] termArray2 = (Term[])iterator2.next();
/* 365 */       if (termArray1 == null ? termArray2 != null : !Arrays.equals(termArray1, termArray2))
/*     */       {
/* 367 */         return false;
/*     */       }
/*     */     }
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */   private class MultiPhraseWeight extends Weight
/*     */   {
/*     */     private Similarity similarity;
/*     */     private float value;
/*     */     private float idf;
/*     */     private float queryNorm;
/*     */     private float queryWeight;
/*     */ 
/*     */     public MultiPhraseWeight(Searcher searcher)
/*     */       throws IOException
/*     */     {
/* 135 */       this.similarity = MultiPhraseQuery.this.getSimilarity(searcher);
/*     */ 
/* 138 */       int maxDoc = searcher.maxDoc();
/* 139 */       for (Term[] terms : MultiPhraseQuery.this.termArrays)
/* 140 */         for (Term term : terms)
/* 141 */           this.idf += this.similarity.idf(searcher.docFreq(term), maxDoc);
/*     */     }
/*     */ 
/*     */     public Query getQuery()
/*     */     {
/* 147 */       return MultiPhraseQuery.this;
/*     */     }
/*     */     public float getValue() {
/* 150 */       return this.value;
/*     */     }
/*     */ 
/*     */     public float sumOfSquaredWeights() {
/* 154 */       this.queryWeight = (this.idf * MultiPhraseQuery.this.getBoost());
/* 155 */       return this.queryWeight * this.queryWeight;
/*     */     }
/*     */ 
/*     */     public void normalize(float queryNorm)
/*     */     {
/* 160 */       this.queryNorm = queryNorm;
/* 161 */       this.queryWeight *= queryNorm;
/* 162 */       this.value = (this.queryWeight * this.idf);
/*     */     }
/*     */ 
/*     */     public Scorer scorer(IndexReader reader, boolean scoreDocsInOrder, boolean topScorer) throws IOException
/*     */     {
/* 167 */       if (MultiPhraseQuery.this.termArrays.size() == 0) {
/* 168 */         return null;
/*     */       }
/* 170 */       TermPositions[] tps = new TermPositions[MultiPhraseQuery.this.termArrays.size()];
/* 171 */       for (int i = 0; i < tps.length; i++) {
/* 172 */         Term[] terms = (Term[])MultiPhraseQuery.this.termArrays.get(i);
/*     */         TermPositions p;
/*     */         TermPositions p;
/* 175 */         if (terms.length > 1)
/* 176 */           p = new MultipleTermPositions(reader, terms);
/*     */         else {
/* 178 */           p = reader.termPositions(terms[0]);
/*     */         }
/* 180 */         if (p == null) {
/* 181 */           return null;
/*     */         }
/* 183 */         tps[i] = p;
/*     */       }
/*     */ 
/* 186 */       if (MultiPhraseQuery.this.slop == 0) {
/* 187 */         return new ExactPhraseScorer(this, tps, MultiPhraseQuery.this.getPositions(), this.similarity, reader.norms(MultiPhraseQuery.this.field));
/*     */       }
/*     */ 
/* 190 */       return new SloppyPhraseScorer(this, tps, MultiPhraseQuery.this.getPositions(), this.similarity, MultiPhraseQuery.this.slop, reader.norms(MultiPhraseQuery.this.field));
/*     */     }
/*     */ 
/*     */     public Explanation explain(IndexReader reader, int doc)
/*     */       throws IOException
/*     */     {
/* 197 */       ComplexExplanation result = new ComplexExplanation();
/* 198 */       result.setDescription("weight(" + getQuery() + " in " + doc + "), product of:");
/*     */ 
/* 200 */       Explanation idfExpl = new Explanation(this.idf, "idf(" + getQuery() + ")");
/*     */ 
/* 203 */       Explanation queryExpl = new Explanation();
/* 204 */       queryExpl.setDescription("queryWeight(" + getQuery() + "), product of:");
/*     */ 
/* 206 */       Explanation boostExpl = new Explanation(MultiPhraseQuery.this.getBoost(), "boost");
/* 207 */       if (MultiPhraseQuery.this.getBoost() != 1.0F) {
/* 208 */         queryExpl.addDetail(boostExpl);
/*     */       }
/* 210 */       queryExpl.addDetail(idfExpl);
/*     */ 
/* 212 */       Explanation queryNormExpl = new Explanation(this.queryNorm, "queryNorm");
/* 213 */       queryExpl.addDetail(queryNormExpl);
/*     */ 
/* 215 */       queryExpl.setValue(boostExpl.getValue() * idfExpl.getValue() * queryNormExpl.getValue());
/*     */ 
/* 219 */       result.addDetail(queryExpl);
/*     */ 
/* 222 */       ComplexExplanation fieldExpl = new ComplexExplanation();
/* 223 */       fieldExpl.setDescription("fieldWeight(" + getQuery() + " in " + doc + "), product of:");
/*     */ 
/* 226 */       PhraseScorer scorer = (PhraseScorer)scorer(reader, true, false);
/* 227 */       if (scorer == null) {
/* 228 */         return new Explanation(0.0F, "no matching docs");
/*     */       }
/* 230 */       Explanation tfExplanation = new Explanation();
/* 231 */       int d = scorer.advance(doc);
/* 232 */       float phraseFreq = d == doc ? scorer.currentFreq() : 0.0F;
/* 233 */       tfExplanation.setValue(this.similarity.tf(phraseFreq));
/* 234 */       tfExplanation.setDescription("tf(phraseFreq=" + phraseFreq + ")");
/* 235 */       fieldExpl.addDetail(tfExplanation);
/* 236 */       fieldExpl.addDetail(idfExpl);
/*     */ 
/* 238 */       Explanation fieldNormExpl = new Explanation();
/* 239 */       byte[] fieldNorms = reader.norms(MultiPhraseQuery.this.field);
/* 240 */       float fieldNorm = fieldNorms != null ? Similarity.decodeNorm(fieldNorms[doc]) : 1.0F;
/*     */ 
/* 242 */       fieldNormExpl.setValue(fieldNorm);
/* 243 */       fieldNormExpl.setDescription("fieldNorm(field=" + MultiPhraseQuery.this.field + ", doc=" + doc + ")");
/* 244 */       fieldExpl.addDetail(fieldNormExpl);
/*     */ 
/* 246 */       fieldExpl.setMatch(Boolean.valueOf(tfExplanation.isMatch()));
/* 247 */       fieldExpl.setValue(tfExplanation.getValue() * idfExpl.getValue() * fieldNormExpl.getValue());
/*     */ 
/* 251 */       result.addDetail(fieldExpl);
/* 252 */       result.setMatch(fieldExpl.getMatch());
/*     */ 
/* 255 */       result.setValue(queryExpl.getValue() * fieldExpl.getValue());
/*     */ 
/* 257 */       if (queryExpl.getValue() == 1.0F) {
/* 258 */         return fieldExpl;
/*     */       }
/* 260 */       return result;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiPhraseQuery
 * JD-Core Version:    0.6.2
 */