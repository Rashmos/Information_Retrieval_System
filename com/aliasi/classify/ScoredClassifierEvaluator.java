/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.util.Scored;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ScoredClassifierEvaluator<E> extends RankedClassifierEvaluator<E>
/*     */ {
/*     */   private final List<ScoreOutcome>[] mScoreOutcomeLists;
/*  41 */   boolean mDefectiveScoring = false;
/*     */ 
/*     */   public ScoredClassifierEvaluator(ScoredClassifier<E> classifier, String[] categories, boolean storeInputs)
/*     */   {
/*  56 */     super(classifier, categories, storeInputs);
/*     */ 
/*  58 */     List[] scoreOutcomeLists = new ArrayList[numCategories()];
/*  59 */     this.mScoreOutcomeLists = scoreOutcomeLists;
/*  60 */     for (int i = 0; i < this.mScoreOutcomeLists.length; i++)
/*  61 */       this.mScoreOutcomeLists[i] = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void setClassifier(ScoredClassifier<E> classifier)
/*     */   {
/*  74 */     setClassifier(classifier, ScoredClassifierEvaluator.class);
/*     */   }
/*     */ 
/*     */   public ScoredClassifier<E> classifier()
/*     */   {
/*  85 */     ScoredClassifier result = (ScoredClassifier)super.classifier();
/*     */ 
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */   public void handle(Classified<E> classified)
/*     */   {
/*  93 */     Object input = classified.getObject();
/*  94 */     Classification refClassification = classified.getClassification();
/*  95 */     String refCategory = refClassification.bestCategory();
/*  96 */     validateCategory(refCategory);
/*  97 */     ScoredClassification classification = classifier().classify(input);
/*  98 */     addClassification(refCategory, classification, input);
/*  99 */     addRanking(refCategory, classification);
/*     */ 
/* 102 */     addScoring(refCategory, classification);
/*     */   }
/*     */ 
/*     */   public ScoredPrecisionRecallEvaluation scoredOneVersusAll(String refCategory)
/*     */   {
/* 118 */     validateCategory(refCategory);
/* 119 */     return scoredOneVersusAll(this.mScoreOutcomeLists, categoryToIndex(refCategory));
/*     */   }
/*     */ 
/*     */   public double averageScore(String refCategory, String responseCategory)
/*     */   {
/* 142 */     validateCategory(refCategory);
/* 143 */     validateCategory(responseCategory);
/* 144 */     double sum = 0.0D;
/* 145 */     int count = 0;
/* 146 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 147 */       if (((String)this.mReferenceCategories.get(i)).equals(refCategory)) {
/* 148 */         ScoredClassification c = (ScoredClassification)this.mClassifications.get(i);
/*     */ 
/* 150 */         for (int rank = 0; rank < c.size(); rank++) {
/* 151 */           if (c.category(rank).equals(responseCategory)) {
/* 152 */             sum += c.score(rank);
/* 153 */             count++;
/* 154 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 159 */     return sum / count;
/*     */   }
/*     */ 
/*     */   public double averageScoreReference()
/*     */   {
/* 175 */     double sum = 0.0D;
/* 176 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 177 */       String refCategory = ((String)this.mReferenceCategories.get(i)).toString();
/* 178 */       ScoredClassification c = (ScoredClassification)this.mClassifications.get(i);
/*     */ 
/* 180 */       for (int rank = 0; rank < c.size(); rank++) {
/* 181 */         if (c.category(rank).equals(refCategory)) {
/* 182 */           sum += c.score(rank);
/* 183 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 187 */     return sum / this.mReferenceCategories.size();
/*     */   }
/*     */ 
/*     */   ScoredPrecisionRecallEvaluation scoredOneVersusAll(List<ScoreOutcome>[] outcomeLists, int categoryIndex) {
/* 191 */     ScoredPrecisionRecallEvaluation eval = new ScoredPrecisionRecallEvaluation();
/*     */ 
/* 193 */     for (ScoreOutcome outcome : outcomeLists[categoryIndex])
/* 194 */       eval.addCase(outcome.mOutcome, outcome.mScore);
/* 195 */     return eval;
/*     */   }
/*     */ 
/*     */   void addScoring(String refCategory, ScoredClassification scoring)
/*     */   {
/* 200 */     if (scoring.size() < numCategories())
/* 201 */       this.mDefectiveScoring = true;
/* 202 */     for (int rank = 0; (rank < numCategories()) && (rank < scoring.size()); rank++) {
/* 203 */       double score = scoring.score(rank);
/* 204 */       String category = scoring.category(rank);
/* 205 */       int categoryIndex = categoryToIndex(category);
/* 206 */       boolean match = category.equals(refCategory);
/* 207 */       ScoreOutcome outcome = new ScoreOutcome(score, match, rank == 0);
/* 208 */       this.mScoreOutcomeLists[categoryIndex].add(outcome);
/*     */     }
/*     */   }
/*     */ 
/*     */   void baseToString(StringBuilder sb)
/*     */   {
/* 214 */     super.baseToString(sb);
/* 215 */     sb.append("Average Score Reference=" + averageScoreReference() + "\n");
/*     */   }
/*     */ 
/*     */   void oneVsAllToString(StringBuilder sb, String category, int i)
/*     */   {
/* 221 */     super.oneVsAllToString(sb, category, i);
/* 222 */     sb.append("Scored One Versus All\n");
/* 223 */     sb.append(scoredOneVersusAll(category) + "\n");
/* 224 */     sb.append("Average Score Histogram=\n");
/* 225 */     appendCategoryLine(sb);
/* 226 */     for (int j = 0; j < numCategories(); j++) {
/* 227 */       if (j > 0) sb.append(',');
/* 228 */       sb.append(averageScore(category, categories()[j]));
/*     */     }
/* 230 */     sb.append("\n");
/*     */   }
/*     */   static class ScoreOutcome implements Scored { private final double mScore;
/*     */     private final boolean mOutcome;
/*     */     private final boolean mFirstBest;
/*     */ 
/* 238 */     public ScoreOutcome(double score, boolean outcome, boolean firstBest) { this.mOutcome = outcome;
/* 239 */       this.mScore = score;
/* 240 */       this.mFirstBest = firstBest; }
/*     */ 
/*     */     public double score() {
/* 243 */       return this.mScore;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 247 */       return "(" + this.mScore + ": " + this.mOutcome + "firstBest=" + this.mFirstBest + ")";
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ScoredClassifierEvaluator
 * JD-Core Version:    0.6.2
 */