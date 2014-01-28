/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ 
/*     */ class MultiTermQuery$ConstantScoreBooleanQueryRewrite extends MultiTermQuery.ScoringBooleanQueryRewrite
/*     */   implements Serializable
/*     */ {
/*     */   private MultiTermQuery$ConstantScoreBooleanQueryRewrite()
/*     */   {
/* 142 */     super(null);
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader, MultiTermQuery query) throws IOException {
/* 146 */     Query result = new ConstantScoreQuery(new QueryWrapperFilter(super.rewrite(reader, query)));
/* 147 */     result.setBoost(query.getBoost());
/* 148 */     return result;
/*     */   }
/*     */ 
/*     */   protected Object readResolve()
/*     */   {
/* 154 */     return MultiTermQuery.CONSTANT_SCORE_BOOLEAN_QUERY_REWRITE;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiTermQuery.ConstantScoreBooleanQueryRewrite
 * JD-Core Version:    0.6.2
 */