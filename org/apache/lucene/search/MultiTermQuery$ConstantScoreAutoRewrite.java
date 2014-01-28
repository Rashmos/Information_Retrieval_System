/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ 
/*     */ public class MultiTermQuery$ConstantScoreAutoRewrite extends MultiTermQuery.RewriteMethod
/*     */   implements Serializable
/*     */ {
/* 184 */   public static int DEFAULT_TERM_COUNT_CUTOFF = 350;
/*     */ 
/* 188 */   public static double DEFAULT_DOC_COUNT_PERCENT = 0.1D;
/*     */ 
/* 190 */   private int termCountCutoff = DEFAULT_TERM_COUNT_CUTOFF;
/* 191 */   private double docCountPercent = DEFAULT_DOC_COUNT_PERCENT;
/*     */ 
/*     */   public void setTermCountCutoff(int count)
/*     */   {
/* 197 */     this.termCountCutoff = count;
/*     */   }
/*     */ 
/*     */   public int getTermCountCutoff()
/*     */   {
/* 202 */     return this.termCountCutoff;
/*     */   }
/*     */ 
/*     */   public void setDocCountPercent(double percent)
/*     */   {
/* 211 */     this.docCountPercent = percent;
/*     */   }
/*     */ 
/*     */   public double getDocCountPercent()
/*     */   {
/* 216 */     return this.docCountPercent;
/*     */   }
/*     */ 
/*     */   public Query rewrite(IndexReader reader, MultiTermQuery query)
/*     */     throws IOException
/*     */   {
/* 225 */     Collection pendingTerms = new ArrayList();
/* 226 */     int docCountCutoff = (int)(this.docCountPercent / 100.0D * reader.maxDoc());
/* 227 */     int termCountLimit = Math.min(BooleanQuery.getMaxClauseCount(), this.termCountCutoff);
/* 228 */     int docVisitCount = 0;
/*     */ 
/* 230 */     FilteredTermEnum enumerator = query.getEnum(reader);
/*     */     try
/*     */     {
/* 233 */       Term t = enumerator.term();
/* 234 */       if (t != null) {
/* 235 */         pendingTerms.add(t);
/*     */ 
/* 240 */         docVisitCount += reader.docFreq(t);
/*     */       }
/*     */ 
/* 243 */       if ((pendingTerms.size() >= termCountLimit) || (docVisitCount >= docCountCutoff))
/*     */       {
/* 245 */         Query result = new ConstantScoreQuery(new MultiTermQueryWrapperFilter(query));
/* 246 */         result.setBoost(query.getBoost());
/* 247 */         return result;
/* 248 */       }if (!enumerator.next())
/*     */       {
/* 252 */         BooleanQuery bq = new BooleanQuery(true);
/* 253 */         for (Iterator i$ = pendingTerms.iterator(); i$.hasNext(); ) { term = (Term)i$.next();
/* 254 */           TermQuery tq = new TermQuery(term);
/* 255 */           bq.add(tq, BooleanClause.Occur.SHOULD);
/*     */         }
/*     */         Term term;
/* 258 */         Object result = new ConstantScoreQuery(new QueryWrapperFilter(bq));
/* 259 */         ((Query)result).setBoost(query.getBoost());
/* 260 */         query.incTotalNumberOfTerms(pendingTerms.size());
/* 261 */         return (Term)result;
/*     */       }
/*     */     }
/*     */     finally {
/* 265 */       enumerator.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 271 */     int prime = 1279;
/* 272 */     return (int)(1279 * this.termCountCutoff + Double.doubleToLongBits(this.docCountPercent));
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 277 */     if (this == obj)
/* 278 */       return true;
/* 279 */     if (obj == null)
/* 280 */       return false;
/* 281 */     if (getClass() != obj.getClass()) {
/* 282 */       return false;
/*     */     }
/* 284 */     ConstantScoreAutoRewrite other = (ConstantScoreAutoRewrite)obj;
/* 285 */     if (other.termCountCutoff != this.termCountCutoff) {
/* 286 */       return false;
/*     */     }
/*     */ 
/* 289 */     if (Double.doubleToLongBits(other.docCountPercent) != Double.doubleToLongBits(this.docCountPercent)) {
/* 290 */       return false;
/*     */     }
/*     */ 
/* 293 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiTermQuery.ConstantScoreAutoRewrite
 * JD-Core Version:    0.6.2
 */