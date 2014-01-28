/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ 
/*     */ class MultiTermQuery$ScoringBooleanQueryRewrite extends MultiTermQuery.RewriteMethod
/*     */   implements Serializable
/*     */ {
/*     */   public Query rewrite(IndexReader reader, MultiTermQuery query)
/*     */     throws IOException
/*     */   {
/* 101 */     FilteredTermEnum enumerator = query.getEnum(reader);
/* 102 */     BooleanQuery result = new BooleanQuery(true);
/* 103 */     int count = 0;
/*     */     try {
/*     */       do {
/* 106 */         Term t = enumerator.term();
/* 107 */         if (t != null) {
/* 108 */           TermQuery tq = new TermQuery(t);
/* 109 */           tq.setBoost(query.getBoost() * enumerator.difference());
/* 110 */           result.add(tq, BooleanClause.Occur.SHOULD);
/* 111 */           count++;
/*     */         }
/*     */       }
/* 113 */       while (enumerator.next());
/*     */     } finally {
/* 115 */       enumerator.close();
/*     */     }
/* 117 */     query.incTotalNumberOfTerms(count);
/* 118 */     return result;
/*     */   }
/*     */ 
/*     */   protected Object readResolve()
/*     */   {
/* 123 */     return MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiTermQuery.ScoringBooleanQueryRewrite
 * JD-Core Version:    0.6.2
 */