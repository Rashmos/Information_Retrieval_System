/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.lm.LanguageModel;
/*     */ import com.aliasi.lm.LanguageModel.Dynamic;
/*     */ import com.aliasi.lm.LanguageModel.Sequence;
/*     */ import com.aliasi.lm.UniformBoundaryLM;
/*     */ import com.aliasi.lm.UniformProcessLM;
/*     */ import com.aliasi.stats.MultivariateEstimator;
/*     */ 
/*     */ public class BinaryLMClassifier extends DynamicLMClassifier<LanguageModel.Dynamic>
/*     */ {
/*     */   private final String mAcceptCategory;
/*     */   private final String mRejectCategory;
/* 242 */   public static final String DEFAULT_ACCEPT_CATEGORY = Boolean.TRUE.toString();
/*     */ 
/* 249 */   public static final String DEFAULT_REJECT_CATEGORY = Boolean.FALSE.toString();
/*     */ 
/*     */   public BinaryLMClassifier(LanguageModel.Dynamic acceptingLM, double crossEntropyThreshold)
/*     */   {
/*  97 */     this(acceptingLM, crossEntropyThreshold, DEFAULT_ACCEPT_CATEGORY, DEFAULT_REJECT_CATEGORY);
/*     */   }
/*     */ 
/*     */   public BinaryLMClassifier(LanguageModel.Dynamic acceptingLM, double crossEntropyThreshold, String acceptCategory, String rejectCategory)
/*     */   {
/* 122 */     super(new String[] { rejectCategory, acceptCategory }, new LanguageModel.Dynamic[] { createRejectLM(crossEntropyThreshold, acceptingLM), acceptingLM });
/*     */ 
/* 128 */     this.mAcceptCategory = acceptCategory;
/* 129 */     this.mRejectCategory = rejectCategory;
/*     */ 
/* 131 */     ((MultivariateEstimator)categoryDistribution()).train(acceptCategory, 1L);
/* 132 */     ((MultivariateEstimator)categoryDistribution()).train(rejectCategory, 1L);
/*     */   }
/*     */ 
/*     */   public String acceptCategory()
/*     */   {
/* 141 */     return this.mAcceptCategory;
/*     */   }
/*     */ 
/*     */   public String rejectCategory()
/*     */   {
/* 150 */     return this.mRejectCategory;
/*     */   }
/*     */ 
/*     */   void train(String category, char[] cs, int start, int end)
/*     */   {
/* 163 */     super.train(category, cs, start, end);
/*     */   }
/*     */ 
/*     */   void train(String category, CharSequence cSeq)
/*     */   {
/* 179 */     ((LanguageModel.Dynamic)languageModel(this.mAcceptCategory)).train(cSeq);
/*     */   }
/*     */ 
/*     */   public void handle(Classified<CharSequence> classified)
/*     */   {
/* 197 */     CharSequence cSeq = (CharSequence)classified.getObject();
/* 198 */     Classification classification = classified.getClassification();
/* 199 */     String bestCategory = classification.bestCategory();
/* 200 */     if (this.mRejectCategory.equals(bestCategory))
/* 201 */       return;
/* 202 */     if (!this.mAcceptCategory.equals(bestCategory)) {
/* 203 */       String msg = "Require accept or reject category. Accept category=" + this.mAcceptCategory + " Reject category=" + this.mRejectCategory + " Found classified best category=" + bestCategory;
/*     */ 
/* 207 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 209 */     ((LanguageModel.Dynamic)languageModel(this.mAcceptCategory)).train(cSeq);
/*     */   }
/*     */ 
/*     */   public void resetCategory(String category, LanguageModel.Dynamic lm, int newCount)
/*     */   {
/* 224 */     String msg = "Resets not allowed for Binary LM classifier.";
/* 225 */     throw new UnsupportedOperationException(msg);
/*     */   }
/*     */ 
/*     */   static LanguageModel.Dynamic createRejectLM(double crossEntropyThreshold, LanguageModel acceptingLM)
/*     */   {
/* 232 */     if ((acceptingLM instanceof LanguageModel.Sequence)) {
/* 233 */       return new UniformBoundaryLM(crossEntropyThreshold);
/*     */     }
/* 235 */     return new UniformProcessLM(crossEntropyThreshold);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.BinaryLMClassifier
 * JD-Core Version:    0.6.2
 */