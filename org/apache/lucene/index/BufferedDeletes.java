/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.lucene.search.Query;
/*     */ 
/*     */ class BufferedDeletes
/*     */ {
/*     */   int numTerms;
/*     */   Map<Term, Num> terms;
/*  39 */   Map<Query, Integer> queries = new HashMap();
/*  40 */   List<Integer> docIDs = new ArrayList();
/*     */   long bytesUsed;
/*     */   private final boolean doTermSort;
/*     */ 
/*     */   public BufferedDeletes(boolean doTermSort)
/*     */   {
/*  45 */     this.doTermSort = doTermSort;
/*  46 */     if (doTermSort)
/*  47 */       this.terms = new TreeMap();
/*     */     else
/*  49 */       this.terms = new HashMap();
/*     */   }
/*     */ 
/*     */   int size()
/*     */   {
/*  82 */     return this.numTerms + this.queries.size() + this.docIDs.size();
/*     */   }
/*     */ 
/*     */   void update(BufferedDeletes in) {
/*  86 */     this.numTerms += in.numTerms;
/*  87 */     this.bytesUsed += in.bytesUsed;
/*  88 */     this.terms.putAll(in.terms);
/*  89 */     this.queries.putAll(in.queries);
/*  90 */     this.docIDs.addAll(in.docIDs);
/*  91 */     in.clear();
/*     */   }
/*     */ 
/*     */   void clear() {
/*  95 */     this.terms.clear();
/*  96 */     this.queries.clear();
/*  97 */     this.docIDs.clear();
/*  98 */     this.numTerms = 0;
/*  99 */     this.bytesUsed = 0L;
/*     */   }
/*     */ 
/*     */   void addBytesUsed(long b) {
/* 103 */     this.bytesUsed += b;
/*     */   }
/*     */ 
/*     */   boolean any() {
/* 107 */     return (this.terms.size() > 0) || (this.docIDs.size() > 0) || (this.queries.size() > 0);
/*     */   }
/*     */ 
/*     */   synchronized void remap(MergeDocIDRemapper mapper, SegmentInfos infos, int[][] docMaps, int[] delCounts, MergePolicy.OneMerge merge, int mergeDocCount)
/*     */   {
/*     */     Map newDeleteTerms;
/*     */     Map newDeleteTerms;
/* 122 */     if (this.terms.size() > 0)
/*     */     {
/*     */       Map newDeleteTerms;
/* 123 */       if (this.doTermSort)
/* 124 */         newDeleteTerms = new TreeMap();
/*     */       else {
/* 126 */         newDeleteTerms = new HashMap();
/*     */       }
/* 128 */       for (Map.Entry entry : this.terms.entrySet()) {
/* 129 */         Num num = (Num)entry.getValue();
/* 130 */         newDeleteTerms.put(entry.getKey(), new Num(mapper.remap(num.getNum())));
/*     */       }
/*     */     }
/*     */     else {
/* 134 */       newDeleteTerms = null;
/*     */     }
/*     */     List newDeleteDocIDs;
/*     */     List newDeleteDocIDs;
/* 140 */     if (this.docIDs.size() > 0) {
/* 141 */       newDeleteDocIDs = new ArrayList(this.docIDs.size());
/* 142 */       for (Integer num : this.docIDs)
/* 143 */         newDeleteDocIDs.add(Integer.valueOf(mapper.remap(num.intValue())));
/*     */     }
/*     */     else {
/* 146 */       newDeleteDocIDs = null;
/*     */     }
/*     */     HashMap newDeleteQueries;
/*     */     HashMap newDeleteQueries;
/* 152 */     if (this.queries.size() > 0) {
/* 153 */       newDeleteQueries = new HashMap(this.queries.size());
/* 154 */       for (Map.Entry entry : this.queries.entrySet()) {
/* 155 */         Integer num = (Integer)entry.getValue();
/* 156 */         newDeleteQueries.put(entry.getKey(), Integer.valueOf(mapper.remap(num.intValue())));
/*     */       }
/*     */     }
/*     */     else {
/* 160 */       newDeleteQueries = null;
/*     */     }
/* 162 */     if (newDeleteTerms != null)
/* 163 */       this.terms = newDeleteTerms;
/* 164 */     if (newDeleteDocIDs != null)
/* 165 */       this.docIDs = newDeleteDocIDs;
/* 166 */     if (newDeleteQueries != null)
/* 167 */       this.queries = newDeleteQueries;
/*     */   }
/*     */ 
/*     */   static final class Num
/*     */   {
/*     */     private int num;
/*     */ 
/*     */     Num(int num)
/*     */     {
/*  58 */       this.num = num;
/*     */     }
/*     */ 
/*     */     int getNum() {
/*  62 */       return this.num;
/*     */     }
/*     */ 
/*     */     void setNum(int num)
/*     */     {
/*  72 */       if (num > this.num)
/*  73 */         this.num = num;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.BufferedDeletes
 * JD-Core Version:    0.6.2
 */