/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.stats.Model;
/*     */ import com.aliasi.util.Exceptions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ 
/*     */ public class CompiledNGramBoundaryLM
/*     */   implements LanguageModel.Sequence, LanguageModel.Conditional, Model<CharSequence>
/*     */ {
/*     */   private final char mBoundaryChar;
/*     */   private final char[] mBoundaryArray;
/*     */   private final CompiledNGramProcessLM mProcessLM;
/*     */ 
/*     */   CompiledNGramBoundaryLM(ObjectInput objIn)
/*     */     throws IOException
/*     */   {
/*  58 */     this.mBoundaryChar = objIn.readChar();
/*  59 */     this.mBoundaryArray = new char[] { this.mBoundaryChar };
/*     */     try {
/*  61 */       this.mProcessLM = ((CompiledNGramProcessLM)objIn.readObject());
/*     */     } catch (ClassNotFoundException e) {
/*  63 */       throw Exceptions.toIO("CompiledNGramBoundarLM(ObjectOutput)", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public char[] observedCharacters()
/*     */   {
/*  76 */     return this.mProcessLM.observedCharacters();
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(CharSequence cs)
/*     */   {
/*  81 */     if (cs.length() < 1) {
/*  82 */       String msg = "Conditional estimates require at least one character.";
/*  83 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  85 */     char[] csBounded = NGramBoundaryLM.addBoundaries(cs, this.mBoundaryChar);
/*  86 */     return this.mProcessLM.log2ConditionalEstimate(csBounded, 0, csBounded.length - 1);
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(char[] cs, int start, int end) {
/*  90 */     if (end <= start) {
/*  91 */       String msg = "Conditional estimates require at least one character.";
/*  92 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  94 */     char[] csBounded = NGramBoundaryLM.addBoundaries(cs, start, end, this.mBoundaryChar);
/*  95 */     return this.mProcessLM.log2ConditionalEstimate(csBounded, 0, csBounded.length - 1);
/*     */   }
/*     */ 
/*     */   public double log2Estimate(CharSequence cs)
/*     */   {
/* 101 */     char[] csBounded = NGramBoundaryLM.addBoundaries(cs, this.mBoundaryChar);
/* 102 */     return this.mProcessLM.log2Estimate(csBounded, 0, csBounded.length) - this.mProcessLM.log2Estimate(this.mBoundaryArray, 0, 1);
/*     */   }
/*     */ 
/*     */   public double log2Estimate(char[] cs, int start, int end)
/*     */   {
/* 107 */     char[] csBounded = NGramBoundaryLM.addBoundaries(cs, start, end, this.mBoundaryChar);
/* 108 */     return this.mProcessLM.log2Estimate(csBounded, 0, csBounded.length) - this.mProcessLM.log2Estimate(this.mBoundaryArray, 0, 1);
/*     */   }
/*     */ 
/*     */   public double log2Prob(CharSequence cSeq)
/*     */   {
/* 122 */     return log2Estimate(cSeq);
/*     */   }
/*     */ 
/*     */   public double prob(CharSequence cSeq)
/*     */   {
/* 134 */     return Math.pow(2.0D, log2Estimate(cSeq));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.CompiledNGramBoundaryLM
 * JD-Core Version:    0.6.2
 */