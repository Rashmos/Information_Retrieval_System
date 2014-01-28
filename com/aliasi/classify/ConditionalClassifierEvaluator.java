/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ConditionalClassifierEvaluator<E> extends ScoredClassifierEvaluator<E>
/*     */ {
/*     */   private final List<ScoredClassifierEvaluator.ScoreOutcome>[] mConditionalOutcomeLists;
/*  42 */   boolean mDefectiveConditioning = false;
/*     */ 
/*     */   public ConditionalClassifierEvaluator(ConditionalClassifier<E> classifier, String[] categories, boolean storeInputs)
/*     */   {
/*  56 */     super(classifier, categories, storeInputs);
/*     */ 
/*  58 */     List[] conditionalOutcomeLists = new ArrayList[numCategories()];
/*  59 */     this.mConditionalOutcomeLists = conditionalOutcomeLists;
/*  60 */     for (int i = 0; i < this.mConditionalOutcomeLists.length; i++)
/*  61 */       this.mConditionalOutcomeLists[i] = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void setClassifier(ConditionalClassifier<E> classifier)
/*     */   {
/*  74 */     setClassifier(classifier, ConditionalClassifierEvaluator.class);
/*     */   }
/*     */ 
/*     */   public ConditionalClassifier<E> classifier()
/*     */   {
/*  85 */     ConditionalClassifier result = (ConditionalClassifier)super.classifier();
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
/*  97 */     ConditionalClassification classification = classifier().classify(input);
/*  98 */     addClassification(refCategory, classification, input);
/*  99 */     addRanking(refCategory, classification);
/* 100 */     addScoring(refCategory, classification);
/*     */ 
/* 103 */     addConditioning(refCategory, classification);
/*     */   }
/*     */ 
/*     */   public double averageConditionalProbability(String refCategory, String responseCategory)
/*     */   {
/* 129 */     validateCategory(refCategory);
/* 130 */     validateCategory(responseCategory);
/* 131 */     double sum = 0.0D;
/* 132 */     int count = 0;
/* 133 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 134 */       if (((String)this.mReferenceCategories.get(i)).equals(refCategory)) {
/* 135 */         ConditionalClassification c = (ConditionalClassification)this.mClassifications.get(i);
/*     */ 
/* 137 */         for (int rank = 0; rank < c.size(); rank++) {
/* 138 */           if (c.category(rank).equals(responseCategory)) {
/* 139 */             sum += c.conditionalProbability(rank);
/* 140 */             count++;
/* 141 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 146 */     return sum / count;
/*     */   }
/*     */ 
/*     */   public double averageConditionalProbabilityReference()
/*     */   {
/* 162 */     double sum = 0.0D;
/* 163 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 164 */       String refCategory = ((String)this.mReferenceCategories.get(i)).toString();
/* 165 */       ConditionalClassification c = (ConditionalClassification)this.mClassifications.get(i);
/*     */ 
/* 167 */       for (int rank = 0; rank < c.size(); rank++) {
/* 168 */         if (c.category(rank).equals(refCategory)) {
/* 169 */           sum += c.conditionalProbability(rank);
/* 170 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 174 */     return sum / this.mReferenceCategories.size();
/*     */   }
/*     */ 
/*     */   public ScoredPrecisionRecallEvaluation conditionalOneVersusAll(String refCategory)
/*     */   {
/* 192 */     validateCategory(refCategory);
/* 193 */     return scoredOneVersusAll(this.mConditionalOutcomeLists, categoryToIndex(refCategory));
/*     */   }
/*     */ 
/*     */   void addConditioning(String refCategory, ConditionalClassification scoring)
/*     */   {
/* 199 */     if (scoring.size() < numCategories())
/* 200 */       this.mDefectiveConditioning = true;
/* 201 */     for (int rank = 0; (rank < numCategories()) && (rank < scoring.size()); rank++) {
/* 202 */       double score = scoring.conditionalProbability(rank);
/* 203 */       String category = scoring.category(rank);
/* 204 */       int categoryIndex = categoryToIndex(category);
/* 205 */       boolean match = category.equals(refCategory);
/* 206 */       ScoredClassifierEvaluator.ScoreOutcome outcome = new ScoredClassifierEvaluator.ScoreOutcome(score, match, rank == 0);
/* 207 */       this.mConditionalOutcomeLists[categoryIndex].add(outcome);
/*     */     }
/*     */   }
/*     */ 
/*     */   void baseToString(StringBuilder sb)
/*     */   {
/* 213 */     super.baseToString(sb);
/* 214 */     sb.append("Average Conditional Probability Reference=" + averageConditionalProbabilityReference() + "\n");
/*     */   }
/*     */ 
/*     */   void oneVsAllToString(StringBuilder sb, String category, int i)
/*     */   {
/* 220 */     super.oneVsAllToString(sb, category, i);
/* 221 */     sb.append("Conditional One Versus All\n");
/* 222 */     sb.append(conditionalOneVersusAll(category).toString() + "\n");
/* 223 */     sb.append("Average Conditional Probability Histogram=\n");
/* 224 */     appendCategoryLine(sb);
/* 225 */     for (int j = 0; j < numCategories(); j++) {
/* 226 */       if (j > 0) sb.append(',');
/* 227 */       sb.append(averageConditionalProbability(category, categories()[j]));
/*     */     }
/*     */ 
/* 230 */     sb.append("\n");
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ConditionalClassifierEvaluator
 * JD-Core Version:    0.6.2
 */