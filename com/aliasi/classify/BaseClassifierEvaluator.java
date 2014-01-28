/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BaseClassifierEvaluator<E>
/*     */   implements ObjectHandler<Classified<E>>
/*     */ {
/*     */   private final ConfusionMatrix mConfusionMatrix;
/*  71 */   private int mNumCases = 0;
/*     */   final String[] mCategories;
/*     */   final Set<String> mCategorySet;
/*     */   final boolean mStoreInputs;
/*     */   BaseClassifier<E> mClassifier;
/*  76 */   final List<Classification> mClassifications = new ArrayList();
/*  77 */   final List<E> mCases = new ArrayList();
/*  78 */   final List<String> mReferenceCategories = new ArrayList();
/*     */ 
/*     */   public BaseClassifierEvaluator(BaseClassifier<E> classifier, String[] categories, boolean storeInputs)
/*     */   {
/*  99 */     this.mClassifier = classifier;
/* 100 */     this.mStoreInputs = storeInputs;
/* 101 */     this.mCategories = categories;
/* 102 */     this.mCategorySet = new HashSet();
/* 103 */     Collections.addAll(this.mCategorySet, categories);
/* 104 */     this.mConfusionMatrix = new ConfusionMatrix(categories);
/*     */   }
/*     */ 
/*     */   public int numCategories()
/*     */   {
/* 114 */     return this.mCategories.length;
/*     */   }
/*     */ 
/*     */   public String[] categories()
/*     */   {
/* 125 */     return (String[])this.mCategories.clone();
/*     */   }
/*     */ 
/*     */   public BaseClassifier<E> classifier()
/*     */   {
/* 134 */     return this.mClassifier;
/*     */   }
/*     */ 
/*     */   public void setClassifier(BaseClassifier<E> classifier)
/*     */   {
/* 152 */     setClassifier(classifier, BaseClassifierEvaluator.class);
/*     */   }
/*     */ 
/*     */   public List<Classified<E>> truePositives(String category)
/*     */   {
/* 175 */     return caseTypes(category, true, true);
/*     */   }
/*     */ 
/*     */   public List<Classified<E>> falsePositives(String category)
/*     */   {
/* 193 */     return caseTypes(category, false, true);
/*     */   }
/*     */ 
/*     */   public List<Classified<E>> falseNegatives(String category)
/*     */   {
/* 211 */     return caseTypes(category, true, false);
/*     */   }
/*     */ 
/*     */   public List<Classified<E>> trueNegatives(String category)
/*     */   {
/* 229 */     return caseTypes(category, false, false);
/*     */   }
/*     */ 
/*     */   public void handle(Classified<E> classified)
/*     */   {
/* 239 */     Object input = classified.getObject();
/* 240 */     Classification refClassification = classified.getClassification();
/* 241 */     String refCategory = refClassification.bestCategory();
/* 242 */     validateCategory(refCategory);
/* 243 */     Classification classification = this.mClassifier.classify(input);
/* 244 */     addClassification(refCategory, classification, input);
/*     */   }
/*     */ 
/*     */   public void addClassification(String referenceCategory, Classification classification, E input)
/*     */   {
/* 259 */     addClassificationOld(referenceCategory, classification);
/* 260 */     if (this.mStoreInputs)
/* 261 */       this.mCases.add(input);
/*     */   }
/*     */ 
/*     */   public int numCases()
/*     */   {
/* 272 */     return this.mNumCases;
/*     */   }
/*     */ 
/*     */   public ConfusionMatrix confusionMatrix()
/*     */   {
/* 284 */     return this.mConfusionMatrix;
/*     */   }
/*     */ 
/*     */   public PrecisionRecallEvaluation oneVersusAll(String refCategory)
/*     */   {
/* 300 */     validateCategory(refCategory);
/* 301 */     PrecisionRecallEvaluation prEval = new PrecisionRecallEvaluation();
/* 302 */     int numCases = this.mReferenceCategories.size();
/* 303 */     for (int i = 0; i < numCases; i++) {
/* 304 */       Object caseRefCategory = this.mReferenceCategories.get(i);
/* 305 */       Classification response = (Classification)this.mClassifications.get(i);
/* 306 */       Object caseResponseCategory = response.bestCategory();
/* 307 */       boolean inRef = caseRefCategory.equals(refCategory);
/* 308 */       boolean inResp = caseResponseCategory.equals(refCategory);
/* 309 */       prEval.addCase(inRef, inResp);
/*     */     }
/* 311 */     return prEval;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 323 */     StringBuilder sb = new StringBuilder();
/* 324 */     baseToString(sb);
/*     */ 
/* 326 */     sb.append("\nONE VERSUS ALL EVALUATIONS BY CATEGORY\n");
/* 327 */     String[] cats = categories();
/* 328 */     for (int i = 0; i < cats.length; i++) {
/* 329 */       sb.append("\n\nCATEGORY[" + i + "]=" + cats[i] + " VERSUS ALL\n");
/* 330 */       oneVsAllToString(sb, cats[i], i);
/*     */     }
/* 332 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   void baseToString(StringBuilder sb) {
/* 336 */     sb.append("BASE CLASSIFIER EVALUATION\n");
/* 337 */     this.mConfusionMatrix.toStringGlobal(sb);
/*     */   }
/*     */ 
/*     */   void oneVsAllToString(StringBuilder sb, String category, int i) {
/* 341 */     sb.append("\nFirst-Best Precision/Recall Evaluation\n");
/* 342 */     sb.append(oneVersusAll(category));
/* 343 */     sb.append('\n');
/*     */   }
/*     */ 
/*     */   void setClassifier(BaseClassifier<E> classifier, Class<?> clazz)
/*     */   {
/* 348 */     if (!getClass().equals(clazz)) {
/* 349 */       String msg = "Require appropriate classifier type. Evaluator class=" + getClass() + " Found classifier.class=" + classifier.getClass();
/*     */ 
/* 352 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 354 */     this.mClassifier = classifier;
/*     */   }
/*     */ 
/*     */   private List<Classified<E>> caseTypes(String category, boolean refMatch, boolean respMatch) {
/* 358 */     if (!this.mStoreInputs) {
/* 359 */       String msg = "Class must store items to return true positives. Use appropriate constructor flag to store.";
/*     */ 
/* 361 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 363 */     List result = new ArrayList();
/* 364 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 365 */       String refCat = (String)this.mReferenceCategories.get(i);
/* 366 */       Classification c = (Classification)this.mClassifications.get(i);
/* 367 */       String respCat = c.bestCategory();
/* 368 */       if ((category.equals(refCat) == refMatch) && 
/* 369 */         (category.equals(respCat) == respMatch)) {
/* 370 */         Classified classified = new Classified(this.mCases.get(i), c);
/* 371 */         result.add(classified);
/*     */       }
/*     */     }
/* 373 */     return result;
/*     */   }
/*     */ 
/*     */   private void addClassificationOld(String referenceCategory, Classification classification)
/*     */   {
/* 379 */     this.mConfusionMatrix.increment(referenceCategory, classification.bestCategory());
/*     */ 
/* 381 */     this.mReferenceCategories.add(referenceCategory);
/* 382 */     this.mClassifications.add(classification);
/* 383 */     this.mNumCases += 1;
/*     */   }
/*     */ 
/*     */   void validateCategory(String category) {
/* 387 */     if (this.mCategorySet.contains(category))
/* 388 */       return;
/* 389 */     String msg = "Unknown category=" + category;
/* 390 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.BaseClassifierEvaluator
 * JD-Core Version:    0.6.2
 */