/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ 
/*     */ public abstract class Weight
/*     */   implements Serializable
/*     */ {
/*     */   public abstract Explanation explain(IndexReader paramIndexReader, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Query getQuery();
/*     */ 
/*     */   public abstract float getValue();
/*     */ 
/*     */   public abstract void normalize(float paramFloat);
/*     */ 
/*     */   public abstract Scorer scorer(IndexReader paramIndexReader, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract float sumOfSquaredWeights()
/*     */     throws IOException;
/*     */ 
/*     */   public boolean scoresDocsOutOfOrder()
/*     */   {
/* 116 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Weight
 * JD-Core Version:    0.6.2
 */