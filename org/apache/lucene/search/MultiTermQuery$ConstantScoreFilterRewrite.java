/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.lucene.index.IndexReader;
/*    */ 
/*    */ final class MultiTermQuery$ConstantScoreFilterRewrite extends MultiTermQuery.RewriteMethod
/*    */   implements Serializable
/*    */ {
/*    */   public Query rewrite(IndexReader reader, MultiTermQuery query)
/*    */   {
/* 72 */     Query result = new ConstantScoreQuery(new MultiTermQueryWrapperFilter(query));
/* 73 */     result.setBoost(query.getBoost());
/* 74 */     return result;
/*    */   }
/*    */ 
/*    */   protected Object readResolve()
/*    */   {
/* 79 */     return MultiTermQuery.CONSTANT_SCORE_FILTER_REWRITE;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiTermQuery.ConstantScoreFilterRewrite
 * JD-Core Version:    0.6.2
 */