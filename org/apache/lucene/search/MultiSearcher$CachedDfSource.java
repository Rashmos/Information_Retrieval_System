/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.apache.lucene.document.Document;
/*     */ import org.apache.lucene.document.FieldSelector;
/*     */ import org.apache.lucene.index.Term;
/*     */ 
/*     */ class MultiSearcher$CachedDfSource extends Searcher
/*     */ {
/*     */   private final Map<Term, Integer> dfMap;
/*     */   private final int maxDoc;
/*     */ 
/*     */   public MultiSearcher$CachedDfSource(Map<Term, Integer> dfMap, int maxDoc, Similarity similarity)
/*     */   {
/*  53 */     this.dfMap = dfMap;
/*  54 */     this.maxDoc = maxDoc;
/*  55 */     setSimilarity(similarity);
/*     */   }
/*     */ 
/*     */   public int docFreq(Term term)
/*     */   {
/*     */     int df;
/*     */     try {
/*  62 */       df = ((Integer)this.dfMap.get(term)).intValue();
/*     */     } catch (NullPointerException e) {
/*  64 */       throw new IllegalArgumentException("df for term " + term.text() + " not available");
/*     */     }
/*     */ 
/*  67 */     return df;
/*     */   }
/*     */ 
/*     */   public int[] docFreqs(Term[] terms)
/*     */   {
/*  72 */     int[] result = new int[terms.length];
/*  73 */     for (int i = 0; i < terms.length; i++) {
/*  74 */       result[i] = docFreq(terms[i]);
/*     */     }
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */   public int maxDoc()
/*     */   {
/*  81 */     return this.maxDoc;
/*     */   }
/*     */ 
/*     */   public Query rewrite(Query query)
/*     */   {
/*  90 */     return query;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  95 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Document doc(int i)
/*     */   {
/* 100 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Document doc(int i, FieldSelector fieldSelector)
/*     */   {
/* 105 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Explanation explain(Weight weight, int doc)
/*     */   {
/* 110 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void search(Weight weight, Filter filter, Collector results)
/*     */   {
/* 115 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public TopDocs search(Weight weight, Filter filter, int n)
/*     */   {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public TopFieldDocs search(Weight weight, Filter filter, int n, Sort sort)
/*     */   {
/* 125 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiSearcher.CachedDfSource
 * JD-Core Version:    0.6.2
 */