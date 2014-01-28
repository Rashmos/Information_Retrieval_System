/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.lm.LanguageModel;
/*     */ import com.aliasi.lm.LanguageModel.Dynamic;
/*     */ import com.aliasi.lm.NGramBoundaryLM;
/*     */ import com.aliasi.lm.NGramProcessLM;
/*     */ import com.aliasi.lm.TokenizedLM;
/*     */ import com.aliasi.stats.MultivariateDistribution;
/*     */ import com.aliasi.stats.MultivariateEstimator;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class DynamicLMClassifier<L extends LanguageModel.Dynamic> extends LMClassifier<L, MultivariateEstimator>
/*     */   implements ObjectHandler<Classified<CharSequence>>, Compilable
/*     */ {
/*     */   public DynamicLMClassifier(String[] categories, L[] languageModels)
/*     */   {
/*  96 */     super(categories, languageModels, createCategoryEstimator(categories));
/*     */   }
/*     */ 
/*     */   void train(String category, char[] cs, int start, int end)
/*     */   {
/* 128 */     train(category, new String(cs, start, end - start));
/*     */   }
/*     */ 
/*     */   void train(String category, CharSequence sampleCSeq)
/*     */   {
/* 142 */     train(category, sampleCSeq, 1);
/*     */   }
/*     */ 
/*     */   public void train(String category, CharSequence sampleCSeq, int count)
/*     */   {
/* 163 */     if (count < 0) {
/* 164 */       String msg = "Counts must be non-negative. Found count=" + count;
/*     */ 
/* 166 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 168 */     if (count == 0) return;
/* 169 */     ((LanguageModel.Dynamic)languageModel(category)).train(sampleCSeq, count);
/* 170 */     ((MultivariateEstimator)categoryDistribution()).train(category, count);
/*     */   }
/*     */ 
/*     */   void handle(CharSequence charSequence, Classification classification)
/*     */   {
/* 188 */     train(classification.bestCategory(), charSequence);
/*     */   }
/*     */ 
/*     */   public void handle(Classified<CharSequence> classified)
/*     */   {
/* 201 */     handle((CharSequence)classified.getObject(), classified.getClassification());
/*     */   }
/*     */ 
/*     */   MultivariateEstimator categoryEstimator()
/*     */   {
/* 215 */     return (MultivariateEstimator)this.mCategoryDistribution;
/*     */   }
/*     */ 
/*     */   L lmForCategory(String category)
/*     */   {
/* 228 */     LanguageModel.Dynamic result = (LanguageModel.Dynamic)this.mCategoryToModel.get(category);
/* 229 */     if (result == null) {
/* 230 */       String msg = "Unknown category=" + category;
/* 231 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 233 */     return result;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 247 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public void resetCategory(String category, L lm, int newCount)
/*     */   {
/* 263 */     if (newCount < 0) {
/* 264 */       String msg = "Count must be non-negative. Found new count=" + newCount;
/*     */ 
/* 266 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 268 */     ((MultivariateEstimator)categoryDistribution()).resetCount(category);
/* 269 */     ((MultivariateEstimator)categoryDistribution()).train(category, newCount);
/* 270 */     LanguageModel.Dynamic currentLM = (LanguageModel.Dynamic)languageModel(category);
/* 271 */     for (int i = 0; i < ((LanguageModel.Dynamic[])this.mLanguageModels).length; i++) {
/* 272 */       if (currentLM == ((LanguageModel.Dynamic[])this.mLanguageModels)[i]) {
/* 273 */         ((LanguageModel.Dynamic[])this.mLanguageModels)[i] = lm;
/* 274 */         break;
/*     */       }
/*     */     }
/* 277 */     this.mCategoryToModel.put(category, lm);
/*     */   }
/*     */ 
/*     */   public static DynamicLMClassifier<NGramProcessLM> createNGramProcess(String[] categories, int maxCharNGram)
/*     */   {
/* 299 */     NGramProcessLM[] lms = new NGramProcessLM[categories.length];
/* 300 */     for (int i = 0; i < lms.length; i++) {
/* 301 */       lms[i] = new NGramProcessLM(maxCharNGram);
/*     */     }
/* 303 */     return new DynamicLMClassifier(categories, lms);
/*     */   }
/*     */ 
/*     */   public static DynamicLMClassifier<NGramBoundaryLM> createNGramBoundary(String[] categories, int maxCharNGram)
/*     */   {
/* 324 */     NGramBoundaryLM[] lms = new NGramBoundaryLM[categories.length];
/* 325 */     for (int i = 0; i < lms.length; i++) {
/* 326 */       lms[i] = new NGramBoundaryLM(maxCharNGram);
/*     */     }
/* 328 */     return new DynamicLMClassifier(categories, lms);
/*     */   }
/*     */ 
/*     */   public static DynamicLMClassifier<TokenizedLM> createTokenized(String[] categories, TokenizerFactory tokenizerFactory, int maxTokenNGram)
/*     */   {
/* 354 */     TokenizedLM[] lms = new TokenizedLM[categories.length];
/* 355 */     for (int i = 0; i < lms.length; i++)
/* 356 */       lms[i] = new TokenizedLM(tokenizerFactory, maxTokenNGram);
/* 357 */     return new DynamicLMClassifier(categories, lms);
/*     */   }
/*     */ 
/*     */   static MultivariateEstimator createCategoryEstimator(String[] categories)
/*     */   {
/* 362 */     MultivariateEstimator estimator = new MultivariateEstimator();
/* 363 */     for (int i = 0; i < categories.length; i++)
/* 364 */       estimator.train(categories[i], 1L);
/* 365 */     return estimator;
/*     */   }
/*     */ 
/*     */   private static class Externalizer<LL extends LanguageModel.Dynamic> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -5411956637253735953L;
/*     */     final DynamicLMClassifier<LL> mClassifier;
/*     */ 
/*     */     public Externalizer() {
/* 375 */       this.mClassifier = null;
/*     */     }
/*     */     public Externalizer(DynamicLMClassifier<LL> classifier) {
/* 378 */       this.mClassifier = classifier;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 382 */       objOut.writeObject(this.mClassifier.categories());
/* 383 */       ((MultivariateEstimator)this.mClassifier.categoryDistribution()).compileTo(objOut);
/* 384 */       int numCategories = this.mClassifier.mCategories.length;
/* 385 */       for (int i = 0; i < numCategories; i++)
/* 386 */         ((LanguageModel.Dynamic[])this.mClassifier.mLanguageModels)[i].compileTo(objOut);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 392 */       String[] categories = (String[])objIn.readObject();
/*     */ 
/* 394 */       MultivariateDistribution categoryEstimator = (MultivariateDistribution)objIn.readObject();
/*     */ 
/* 396 */       LanguageModel[] models = new LanguageModel[categories.length];
/* 397 */       for (int i = 0; i < models.length; i++)
/* 398 */         models[i] = ((LanguageModel)objIn.readObject());
/* 399 */       return new LMClassifier(categories, models, categoryEstimator);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.DynamicLMClassifier
 * JD-Core Version:    0.6.2
 */