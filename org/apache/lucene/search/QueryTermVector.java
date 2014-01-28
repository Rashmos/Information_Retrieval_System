/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/*     */ import org.apache.lucene.index.TermFreqVector;
/*     */ 
/*     */ public class QueryTermVector
/*     */   implements TermFreqVector
/*     */ {
/*  39 */   private String[] terms = new String[0];
/*  40 */   private int[] termFreqs = new int[0];
/*     */ 
/*  42 */   public String getField() { return null; }
/*     */ 
/*     */ 
/*     */   public QueryTermVector(String[] queryTerms)
/*     */   {
/*  50 */     processTerms(queryTerms);
/*     */   }
/*     */ 
/*     */   public QueryTermVector(String queryString, Analyzer analyzer) {
/*  54 */     if (analyzer != null)
/*     */     {
/*  56 */       TokenStream stream = analyzer.tokenStream("", new StringReader(queryString));
/*  57 */       if (stream != null)
/*     */       {
/*  59 */         List terms = new ArrayList();
/*     */         try {
/*  61 */           boolean hasMoreTokens = false;
/*     */ 
/*  63 */           stream.reset();
/*  64 */           TermAttribute termAtt = (TermAttribute)stream.addAttribute(TermAttribute.class);
/*     */ 
/*  66 */           hasMoreTokens = stream.incrementToken();
/*  67 */           while (hasMoreTokens) {
/*  68 */             terms.add(termAtt.term());
/*  69 */             hasMoreTokens = stream.incrementToken();
/*     */           }
/*  71 */           processTerms((String[])terms.toArray(new String[terms.size()]));
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processTerms(String[] queryTerms)
/*     */   {
/*     */     int i;
/*  79 */     if (queryTerms != null) {
/*  80 */       Arrays.sort(queryTerms);
/*  81 */       Map tmpSet = new HashMap(queryTerms.length);
/*     */ 
/*  83 */       List tmpList = new ArrayList(queryTerms.length);
/*  84 */       List tmpFreqs = new ArrayList(queryTerms.length);
/*  85 */       int j = 0;
/*  86 */       for (int i = 0; i < queryTerms.length; i++) {
/*  87 */         String term = queryTerms[i];
/*  88 */         Integer position = (Integer)tmpSet.get(term);
/*  89 */         if (position == null) {
/*  90 */           tmpSet.put(term, Integer.valueOf(j++));
/*  91 */           tmpList.add(term);
/*  92 */           tmpFreqs.add(Integer.valueOf(1));
/*     */         }
/*     */         else {
/*  95 */           Integer integer = (Integer)tmpFreqs.get(position.intValue());
/*  96 */           tmpFreqs.set(position.intValue(), Integer.valueOf(integer.intValue() + 1));
/*     */         }
/*     */       }
/*  99 */       this.terms = ((String[])tmpList.toArray(this.terms));
/*     */ 
/* 101 */       this.termFreqs = new int[tmpFreqs.size()];
/* 102 */       i = 0;
/* 103 */       for (Integer integer : tmpFreqs)
/* 104 */         this.termFreqs[(i++)] = integer.intValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 111 */     StringBuilder sb = new StringBuilder();
/* 112 */     sb.append('{');
/* 113 */     for (int i = 0; i < this.terms.length; i++) {
/* 114 */       if (i > 0) sb.append(", ");
/* 115 */       sb.append(this.terms[i]).append('/').append(this.termFreqs[i]);
/*     */     }
/* 117 */     sb.append('}');
/* 118 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 123 */     return this.terms.length;
/*     */   }
/*     */ 
/*     */   public String[] getTerms() {
/* 127 */     return this.terms;
/*     */   }
/*     */ 
/*     */   public int[] getTermFrequencies() {
/* 131 */     return this.termFreqs;
/*     */   }
/*     */ 
/*     */   public int indexOf(String term) {
/* 135 */     int res = Arrays.binarySearch(this.terms, term);
/* 136 */     return res >= 0 ? res : -1;
/*     */   }
/*     */ 
/*     */   public int[] indexesOf(String[] terms, int start, int len) {
/* 140 */     int[] res = new int[len];
/*     */ 
/* 142 */     for (int i = 0; i < len; i++) {
/* 143 */       res[i] = indexOf(terms[i]);
/*     */     }
/* 145 */     return res;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.QueryTermVector
 * JD-Core Version:    0.6.2
 */