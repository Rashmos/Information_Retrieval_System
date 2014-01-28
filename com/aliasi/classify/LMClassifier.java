/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.lm.LanguageModel;
/*     */ import com.aliasi.stats.MultivariateDistribution;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class LMClassifier<L extends LanguageModel, M extends MultivariateDistribution>
/*     */   implements JointClassifier<CharSequence>
/*     */ {
/*     */   final L[] mLanguageModels;
/*     */   final M mCategoryDistribution;
/*     */   final HashMap<String, L> mCategoryToModel;
/*     */   final String[] mCategories;
/*     */ 
/*     */   public LMClassifier(String[] categories, L[] languageModels, M categoryDistribution)
/*     */   {
/* 158 */     if (categories.length < 2) {
/* 159 */       String msg = "Require at least two categories. Found categories.length=" + categories.length;
/*     */ 
/* 161 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 163 */     if (categories.length != categoryDistribution.numDimensions()) {
/* 164 */       String msg = "Require same number of categories as dimensions. Found categories.length=" + categories.length + " Found categoryDistribution.numDimensions()=" + categoryDistribution.numDimensions();
/*     */ 
/* 169 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 172 */     this.mCategories = categories;
/* 173 */     if (categories.length != languageModels.length) {
/* 174 */       String msg = "Categories and language models must be same length. Found categories length=" + categories.length + " Found language models length=" + languageModels.length;
/*     */ 
/* 177 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 179 */     this.mLanguageModels = languageModels;
/* 180 */     this.mCategoryDistribution = categoryDistribution;
/* 181 */     this.mCategoryToModel = new HashMap();
/* 182 */     for (int i = 0; i < categories.length; i++)
/* 183 */       this.mCategoryToModel.put(categories[i], languageModels[i]);
/*     */   }
/*     */ 
/*     */   public String[] categories()
/*     */   {
/* 195 */     return (String[])this.mCategories.clone();
/*     */   }
/*     */ 
/*     */   public L languageModel(String category)
/*     */   {
/* 213 */     for (int i = 0; i < this.mCategories.length; i++)
/* 214 */       if (category.equals(this.mCategories[i]))
/* 215 */         return this.mLanguageModels[i];
/* 216 */     String msg = "Category not known.  Category=" + category;
/* 217 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   public M categoryDistribution()
/*     */   {
/* 232 */     return this.mCategoryDistribution;
/*     */   }
/*     */ 
/*     */   public JointClassification classify(CharSequence cSeq)
/*     */   {
/* 245 */     if (!(cSeq instanceof CharSequence)) {
/* 246 */       String msg = "LM Classification requires CharSequence input. Found class=" + (cSeq == null ? null : cSeq.getClass());
/*     */ 
/* 248 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 250 */     return classifyJoint(Strings.toCharArray(cSeq), 0, cSeq.length());
/*     */   }
/*     */ 
/*     */   public JointClassification classifyJoint(char[] cs, int start, int end)
/*     */   {
/* 266 */     Strings.checkArgsStartEnd(cs, start, end);
/*     */ 
/* 270 */     ScoredObject[] estimates = new ScoredObject[categories().length];
/*     */ 
/* 273 */     for (int i = 0; i < categories().length; i++) {
/* 274 */       String category = categories()[i];
/* 275 */       LanguageModel model = this.mLanguageModels[i];
/* 276 */       double charsGivenCatLogProb = model.log2Estimate(new String(cs, start, end - start));
/*     */ 
/* 278 */       double catLogProb = this.mCategoryDistribution.log2Probability(category);
/*     */ 
/* 280 */       double charsCatJointLogProb = charsGivenCatLogProb + catLogProb;
/* 281 */       estimates[i] = new ScoredObject(category, charsCatJointLogProb);
/*     */     }
/*     */ 
/* 285 */     return toJointClassification(estimates, end - start + 2);
/*     */   }
/*     */ 
/*     */   static JointClassification toJointClassification(ScoredObject<String>[] estimates, double length)
/*     */   {
/* 291 */     Arrays.sort(estimates, ScoredObject.reverseComparator());
/* 292 */     String[] categories = new String[estimates.length];
/* 293 */     double[] jointEstimates = new double[estimates.length];
/* 294 */     double[] scores = new double[estimates.length];
/* 295 */     for (int i = 0; i < estimates.length; i++) {
/* 296 */       categories[i] = ((String)estimates[i].getObject());
/* 297 */       jointEstimates[i] = estimates[i].score();
/* 298 */       jointEstimates[i] /= length;
/*     */     }
/* 300 */     return new JointClassification(categories, scores, jointEstimates);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.LMClassifier
 * JD-Core Version:    0.6.2
 */