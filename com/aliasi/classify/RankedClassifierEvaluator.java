/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public class RankedClassifierEvaluator<E> extends BaseClassifierEvaluator<E>
/*     */ {
/*  35 */   boolean mDefectiveRanking = false;
/*     */   private final int[][] mRankCounts;
/*     */ 
/*     */   public RankedClassifierEvaluator(RankedClassifier<E> classifier, String[] categories, boolean storeInputs)
/*     */   {
/*  50 */     super(classifier, categories, storeInputs);
/*  51 */     int len = categories.length;
/*  52 */     this.mRankCounts = new int[len][len];
/*     */   }
/*     */ 
/*     */   public void setClassifier(RankedClassifier<E> classifier)
/*     */   {
/*  65 */     if (!getClass().equals(RankedClassifierEvaluator.class)) {
/*  66 */       String msg = "Require appropriate classifier type. Evaluator class=" + getClass() + " Found classifier.class=" + classifier.getClass();
/*     */ 
/*  69 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  71 */     this.mClassifier = classifier;
/*     */   }
/*     */ 
/*     */   public RankedClassifier<E> classifier()
/*     */   {
/*  82 */     RankedClassifier result = (RankedClassifier)super.classifier();
/*     */ 
/*  84 */     return result;
/*     */   }
/*     */ 
/*     */   public void handle(Classified<E> classified)
/*     */   {
/*  90 */     Object input = classified.getObject();
/*  91 */     Classification refClassification = classified.getClassification();
/*  92 */     String refCategory = refClassification.bestCategory();
/*  93 */     validateCategory(refCategory);
/*  94 */     RankedClassification classification = classifier().classify(input);
/*  95 */     addClassification(refCategory, classification, input);
/*     */ 
/*  98 */     addRanking(refCategory, classification);
/*     */   }
/*     */ 
/*     */   void addRanking(String refCategory, RankedClassification ranking) {
/* 102 */     int refCategoryIndex = categoryToIndex(refCategory);
/* 103 */     if (ranking.size() < numCategories())
/* 104 */       this.mDefectiveRanking = true;
/* 105 */     for (int rank = 0; (rank < numCategories()) && (rank < ranking.size()); rank++) {
/* 106 */       String category = ranking.category(rank);
/* 107 */       if (category.equals(refCategory)) {
/* 108 */         this.mRankCounts[refCategoryIndex][rank] += 1;
/* 109 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 113 */     this.mRankCounts[refCategoryIndex][(this.mCategories.length - 1)] += 1;
/*     */   }
/*     */ 
/*     */   public int rankCount(String referenceCategory, int rank)
/*     */   {
/* 150 */     validateCategory(referenceCategory);
/* 151 */     int i = categoryToIndex(referenceCategory);
/* 152 */     return this.mRankCounts[i][rank];
/*     */   }
/*     */ 
/*     */   public double averageRankReference()
/*     */   {
/* 171 */     double sum = 0.0D;
/* 172 */     int count = 0;
/* 173 */     for (int i = 0; i < numCategories(); i++)
/* 174 */       for (int rank = 0; rank < numCategories(); rank++) {
/* 175 */         int rankCount = this.mRankCounts[i][rank];
/* 176 */         if (rankCount != 0) {
/* 177 */           count += rankCount;
/* 178 */           sum += rank * rankCount;
/*     */         }
/*     */       }
/* 181 */     return sum / count;
/*     */   }
/*     */ 
/*     */   public double meanReciprocalRank()
/*     */   {
/* 205 */     double sum = 0.0D;
/* 206 */     int numCases = 0;
/* 207 */     for (int i = 0; i < numCategories(); i++)
/* 208 */       for (int rank = 0; rank < numCategories(); rank++) {
/* 209 */         int rankCount = this.mRankCounts[i][rank];
/* 210 */         if (rankCount != 0) {
/* 211 */           numCases += rankCount;
/* 212 */           sum += rankCount / (1.0D + rank);
/*     */         }
/*     */       }
/* 215 */     return sum / numCases;
/*     */   }
/*     */ 
/*     */   public double averageRank(String refCategory, String responseCategory)
/*     */   {
/* 290 */     validateCategory(refCategory);
/* 291 */     validateCategory(responseCategory);
/* 292 */     double sum = 0.0D;
/* 293 */     int count = 0;
/*     */ 
/* 295 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 296 */       if (((String)this.mReferenceCategories.get(i)).equals(refCategory)) {
/* 297 */         RankedClassification rankedClassification = (RankedClassification)this.mClassifications.get(i);
/*     */ 
/* 299 */         int rank = getRank(rankedClassification, responseCategory);
/* 300 */         sum += rank;
/* 301 */         count++;
/*     */       }
/*     */     }
/* 304 */     return sum / count;
/*     */   }
/*     */ 
/*     */   int categoryToIndex(String category) {
/* 308 */     int result = confusionMatrix().getIndex(category);
/* 309 */     if (result < 0) {
/* 310 */       String msg = "Unknown category=" + category;
/* 311 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 313 */     return result;
/*     */   }
/*     */ 
/*     */   int getRank(RankedClassification classification, String responseCategory) {
/* 317 */     for (int rank = 0; rank < classification.size(); rank++) {
/* 318 */       if (classification.category(rank).equals(responseCategory))
/* 319 */         return rank;
/*     */     }
/* 321 */     return this.mCategories.length - 1;
/*     */   }
/*     */ 
/*     */   void baseToString(StringBuilder sb)
/*     */   {
/* 326 */     super.baseToString(sb);
/* 327 */     sb.append("Average Reference Rank=" + averageRankReference() + "\n");
/*     */   }
/*     */ 
/*     */   void oneVsAllToString(StringBuilder sb, String category, int i)
/*     */   {
/* 333 */     super.oneVsAllToString(sb, category, i);
/* 334 */     sb.append("Rank Histogram=\n");
/* 335 */     appendCategoryLine(sb);
/* 336 */     for (int rank = 0; rank < numCategories(); rank++) {
/* 337 */       if (rank > 0) sb.append(',');
/* 338 */       sb.append(this.mRankCounts[i][rank]);
/*     */     }
/* 340 */     sb.append("\n");
/*     */ 
/* 342 */     sb.append("Average Rank Histogram=\n");
/* 343 */     appendCategoryLine(sb);
/* 344 */     for (int j = 0; j < numCategories(); j++) {
/* 345 */       if (j > 0) sb.append(',');
/* 346 */       sb.append(averageRank(category, categories()[j]));
/*     */     }
/* 348 */     sb.append("\n");
/*     */   }
/*     */ 
/*     */   void appendCategoryLine(StringBuilder sb) {
/* 352 */     sb.append("  ");
/* 353 */     for (int i = 0; i < numCategories(); i++) {
/* 354 */       if (i > 0) sb.append(',');
/* 355 */       sb.append(categories()[i]);
/*     */     }
/* 357 */     sb.append("\n  ");
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.RankedClassifierEvaluator
 * JD-Core Version:    0.6.2
 */