/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.lm.LanguageModel.Sequence;
/*     */ import com.aliasi.lm.NGramBoundaryLM;
/*     */ import com.aliasi.lm.TokenizedLM;
/*     */ import com.aliasi.lm.UniformBoundaryLM;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ 
/*     */ public class NaiveBayesClassifier extends DynamicLMClassifier<TokenizedLM>
/*     */ {
/*     */   public NaiveBayesClassifier(String[] categories, TokenizerFactory tokenizerFactory)
/*     */   {
/* 152 */     this(categories, tokenizerFactory, 0);
/*     */   }
/*     */ 
/*     */   public NaiveBayesClassifier(String[] categories, TokenizerFactory tokenizerFactory, int charSmoothingNGram)
/*     */   {
/* 174 */     this(categories, tokenizerFactory, charSmoothingNGram, 65534);
/*     */   }
/*     */ 
/*     */   public NaiveBayesClassifier(String[] categories, TokenizerFactory tokenizerFactory, int charSmoothingNGram, int maxObservedChars)
/*     */   {
/* 205 */     super(categories, naiveBayesLMs(categories.length, tokenizerFactory, charSmoothingNGram, maxObservedChars));
/*     */   }
/*     */ 
/*     */   private static TokenizedLM[] naiveBayesLMs(int length, TokenizerFactory tokenizerFactory, int charSmoothingNGram, int maxObservedChars)
/*     */   {
/* 217 */     TokenizedLM[] lms = new TokenizedLM[length];
/* 218 */     for (int i = 0; i < lms.length; i++)
/*     */     {
/*     */       LanguageModel.Sequence charLM;
/*     */       LanguageModel.Sequence charLM;
/* 220 */       if (charSmoothingNGram < 1)
/* 221 */         charLM = new UniformBoundaryLM(maxObservedChars);
/*     */       else {
/* 223 */         charLM = new NGramBoundaryLM(charSmoothingNGram, maxObservedChars);
/*     */       }
/* 225 */       lms[i] = new TokenizedLM(tokenizerFactory, 1, charLM, UniformBoundaryLM.ZERO_LM, 1.0D);
/*     */     }
/*     */ 
/* 232 */     return lms;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.NaiveBayesClassifier
 * JD-Core Version:    0.6.2
 */