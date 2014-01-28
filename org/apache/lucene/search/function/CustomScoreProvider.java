/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.Explanation;
/*     */ 
/*     */ public class CustomScoreProvider
/*     */ {
/*     */   protected final IndexReader reader;
/*     */ 
/*     */   public CustomScoreProvider(IndexReader reader)
/*     */   {
/*  48 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */   public float customScore(int doc, float subQueryScore, float[] valSrcScores)
/*     */     throws IOException
/*     */   {
/*  75 */     if (valSrcScores.length == 1) {
/*  76 */       return customScore(doc, subQueryScore, valSrcScores[0]);
/*     */     }
/*  78 */     if (valSrcScores.length == 0) {
/*  79 */       return customScore(doc, subQueryScore, 1.0F);
/*     */     }
/*  81 */     float score = subQueryScore;
/*  82 */     for (int i = 0; i < valSrcScores.length; i++) {
/*  83 */       score *= valSrcScores[i];
/*     */     }
/*  85 */     return score;
/*     */   }
/*     */ 
/*     */   public float customScore(int doc, float subQueryScore, float valSrcScore)
/*     */     throws IOException
/*     */   {
/* 109 */     return subQueryScore * valSrcScore;
/*     */   }
/*     */ 
/*     */   public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation[] valSrcExpls)
/*     */     throws IOException
/*     */   {
/* 124 */     if (valSrcExpls.length == 1) {
/* 125 */       return customExplain(doc, subQueryExpl, valSrcExpls[0]);
/*     */     }
/* 127 */     if (valSrcExpls.length == 0) {
/* 128 */       return subQueryExpl;
/*     */     }
/* 130 */     float valSrcScore = 1.0F;
/* 131 */     for (int i = 0; i < valSrcExpls.length; i++) {
/* 132 */       valSrcScore *= valSrcExpls[i].getValue();
/*     */     }
/* 134 */     Explanation exp = new Explanation(valSrcScore * subQueryExpl.getValue(), "custom score: product of:");
/* 135 */     exp.addDetail(subQueryExpl);
/* 136 */     for (int i = 0; i < valSrcExpls.length; i++) {
/* 137 */       exp.addDetail(valSrcExpls[i]);
/*     */     }
/* 139 */     return exp;
/*     */   }
/*     */ 
/*     */   public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation valSrcExpl)
/*     */     throws IOException
/*     */   {
/* 154 */     float valSrcScore = 1.0F;
/* 155 */     if (valSrcExpl != null) {
/* 156 */       valSrcScore *= valSrcExpl.getValue();
/*     */     }
/* 158 */     Explanation exp = new Explanation(valSrcScore * subQueryExpl.getValue(), "custom score: product of:");
/* 159 */     exp.addDetail(subQueryExpl);
/* 160 */     exp.addDetail(valSrcExpl);
/* 161 */     return exp;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.CustomScoreProvider
 * JD-Core Version:    0.6.2
 */