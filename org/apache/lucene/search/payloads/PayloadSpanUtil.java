/*     */ package org.apache.lucene.search.payloads;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.search.BooleanClause;
/*     */ import org.apache.lucene.search.BooleanQuery;
/*     */ import org.apache.lucene.search.DisjunctionMaxQuery;
/*     */ import org.apache.lucene.search.FilteredQuery;
/*     */ import org.apache.lucene.search.MultiPhraseQuery;
/*     */ import org.apache.lucene.search.PhraseQuery;
/*     */ import org.apache.lucene.search.Query;
/*     */ import org.apache.lucene.search.TermQuery;
/*     */ import org.apache.lucene.search.spans.SpanNearQuery;
/*     */ import org.apache.lucene.search.spans.SpanOrQuery;
/*     */ import org.apache.lucene.search.spans.SpanQuery;
/*     */ import org.apache.lucene.search.spans.SpanTermQuery;
/*     */ import org.apache.lucene.search.spans.Spans;
/*     */ 
/*     */ public class PayloadSpanUtil
/*     */ {
/*     */   private IndexReader reader;
/*     */ 
/*     */   public PayloadSpanUtil(IndexReader reader)
/*     */   {
/*  62 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */   public Collection<byte[]> getPayloadsForQuery(Query query)
/*     */     throws IOException
/*     */   {
/*  73 */     Collection payloads = new ArrayList();
/*  74 */     queryToSpanQuery(query, payloads);
/*  75 */     return payloads;
/*     */   }
/*     */ 
/*     */   private void queryToSpanQuery(Query query, Collection<byte[]> payloads) throws IOException
/*     */   {
/*  80 */     if ((query instanceof BooleanQuery)) {
/*  81 */       BooleanClause[] queryClauses = ((BooleanQuery)query).getClauses();
/*     */ 
/*  83 */       for (int i = 0; i < queryClauses.length; i++) {
/*  84 */         if (!queryClauses[i].isProhibited()) {
/*  85 */           queryToSpanQuery(queryClauses[i].getQuery(), payloads);
/*     */         }
/*     */       }
/*     */     }
/*  89 */     else if ((query instanceof PhraseQuery)) {
/*  90 */       Term[] phraseQueryTerms = ((PhraseQuery)query).getTerms();
/*  91 */       SpanQuery[] clauses = new SpanQuery[phraseQueryTerms.length];
/*  92 */       for (int i = 0; i < phraseQueryTerms.length; i++) {
/*  93 */         clauses[i] = new SpanTermQuery(phraseQueryTerms[i]);
/*     */       }
/*     */ 
/*  96 */       int slop = ((PhraseQuery)query).getSlop();
/*  97 */       boolean inorder = false;
/*     */ 
/*  99 */       if (slop == 0) {
/* 100 */         inorder = true;
/*     */       }
/*     */ 
/* 103 */       SpanNearQuery sp = new SpanNearQuery(clauses, slop, inorder);
/* 104 */       sp.setBoost(query.getBoost());
/* 105 */       getPayloads(payloads, sp);
/* 106 */     } else if ((query instanceof TermQuery)) {
/* 107 */       SpanTermQuery stq = new SpanTermQuery(((TermQuery)query).getTerm());
/* 108 */       stq.setBoost(query.getBoost());
/* 109 */       getPayloads(payloads, stq);
/* 110 */     } else if ((query instanceof SpanQuery)) {
/* 111 */       getPayloads(payloads, (SpanQuery)query);
/* 112 */     } else if ((query instanceof FilteredQuery)) {
/* 113 */       queryToSpanQuery(((FilteredQuery)query).getQuery(), payloads);
/* 114 */     } else if ((query instanceof DisjunctionMaxQuery))
/*     */     {
/* 116 */       Iterator iterator = ((DisjunctionMaxQuery)query).iterator();
/* 117 */       while (iterator.hasNext()) {
/* 118 */         queryToSpanQuery((Query)iterator.next(), payloads);
/*     */       }
/*     */     }
/* 121 */     else if ((query instanceof MultiPhraseQuery)) {
/* 122 */       MultiPhraseQuery mpq = (MultiPhraseQuery)query;
/* 123 */       List termArrays = mpq.getTermArrays();
/* 124 */       int[] positions = mpq.getPositions();
/* 125 */       if (positions.length > 0)
/*     */       {
/* 127 */         int maxPosition = positions[(positions.length - 1)];
/* 128 */         for (int i = 0; i < positions.length - 1; i++) {
/* 129 */           if (positions[i] > maxPosition) {
/* 130 */             maxPosition = positions[i];
/*     */           }
/*     */         }
/*     */ 
/* 134 */         List[] disjunctLists = new List[maxPosition + 1];
/* 135 */         int distinctPositions = 0;
/*     */ 
/* 137 */         for (int i = 0; i < termArrays.size(); i++) {
/* 138 */           Term[] termArray = (Term[])termArrays.get(i);
/* 139 */           List disjuncts = disjunctLists[positions[i]];
/* 140 */           if (disjuncts == null) {
/* 141 */             disjuncts = disjunctLists[positions[i]] =  = new ArrayList(termArray.length);
/*     */ 
/* 143 */             distinctPositions++;
/*     */           }
/* 145 */           for (Term term : termArray) {
/* 146 */             disjuncts.add(new SpanTermQuery(term));
/*     */           }
/*     */         }
/*     */ 
/* 150 */         int positionGaps = 0;
/* 151 */         int position = 0;
/* 152 */         SpanQuery[] clauses = new SpanQuery[distinctPositions];
/* 153 */         for (int i = 0; i < disjunctLists.length; i++) {
/* 154 */           List disjuncts = disjunctLists[i];
/* 155 */           if (disjuncts != null) {
/* 156 */             clauses[(position++)] = new SpanOrQuery((SpanQuery[])disjuncts.toArray(new SpanQuery[disjuncts.size()]));
/*     */           }
/*     */           else {
/* 159 */             positionGaps++;
/*     */           }
/*     */         }
/*     */ 
/* 163 */         int slop = mpq.getSlop();
/* 164 */         boolean inorder = slop == 0;
/*     */ 
/* 166 */         SpanNearQuery sp = new SpanNearQuery(clauses, slop + positionGaps, inorder);
/*     */ 
/* 168 */         sp.setBoost(query.getBoost());
/* 169 */         getPayloads(payloads, sp);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void getPayloads(Collection<byte[]> payloads, SpanQuery query) throws IOException
/*     */   {
/* 176 */     Spans spans = query.getSpans(this.reader);
/*     */ 
/* 178 */     while (spans.next() == true)
/* 179 */       if (spans.isPayloadAvailable()) {
/* 180 */         Collection payload = spans.getPayload();
/* 181 */         for (byte[] bytes : payload)
/* 182 */           payloads.add(bytes);
/*     */       }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.payloads.PayloadSpanUtil
 * JD-Core Version:    0.6.2
 */