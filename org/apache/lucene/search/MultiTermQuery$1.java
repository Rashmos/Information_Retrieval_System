/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ class MultiTermQuery$1 extends MultiTermQuery.ConstantScoreAutoRewrite
/*     */ {
/*     */   public void setTermCountCutoff(int count)
/*     */   {
/* 312 */     throw new UnsupportedOperationException("Please create a private instance");
/*     */   }
/*     */ 
/*     */   public void setDocCountPercent(double percent)
/*     */   {
/* 317 */     throw new UnsupportedOperationException("Please create a private instance");
/*     */   }
/*     */ 
/*     */   protected Object readResolve()
/*     */   {
/* 322 */     return MultiTermQuery.CONSTANT_SCORE_AUTO_REWRITE_DEFAULT;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiTermQuery.1
 * JD-Core Version:    0.6.2
 */