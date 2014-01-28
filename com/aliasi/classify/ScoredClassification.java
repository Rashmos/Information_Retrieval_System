/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ScoredClassification extends RankedClassification
/*     */ {
/*     */   private final double[] mScores;
/*     */ 
/*     */   public ScoredClassification(String[] categories, double[] scores)
/*     */   {
/*  58 */     super(categories);
/*  59 */     if (categories.length != scores.length) {
/*  60 */       String msg = "Categories and scores must be of same length. Categories length=" + categories.length + " Scores length=" + scores.length;
/*     */ 
/*  63 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  65 */     for (int i = 1; i < scores.length; i++)
/*  66 */       if (scores[(i - 1)] < scores[i]) {
/*  67 */         String msg = "Array of scores must be in order. scores[" + (i - 1) + "]=" + scores[(i - 1)] + " < scores[" + i + "]=" + scores[i];
/*     */ 
/*  70 */         throw new IllegalArgumentException(msg);
/*     */       }
/*  72 */     this.mScores = scores;
/*     */   }
/*     */ 
/*     */   public static ScoredClassification create(ScoredObject<String>[] categoryScores)
/*     */   {
/*  85 */     Arrays.sort(categoryScores, ScoredObject.reverseComparator());
/*  86 */     String[] categories = new String[categoryScores.length];
/*  87 */     double[] scores = new double[categoryScores.length];
/*  88 */     for (int i = 0; i < categoryScores.length; i++) {
/*  89 */       categories[i] = ((String)categoryScores[i].getObject());
/*  90 */       scores[i] = categoryScores[i].score();
/*     */     }
/*  92 */     return new ScoredClassification(categories, scores);
/*     */   }
/*     */ 
/*     */   public double score(int rank)
/*     */   {
/* 111 */     checkRange(rank);
/* 112 */     return this.mScores[rank];
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 123 */     StringBuilder sb = new StringBuilder();
/* 124 */     sb.append("Rank  Category  Score\n");
/* 125 */     for (int i = 0; i < size(); i++)
/* 126 */       sb.append(i + "=" + category(i) + " " + score(i) + '\n');
/* 127 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ScoredClassification
 * JD-Core Version:    0.6.2
 */