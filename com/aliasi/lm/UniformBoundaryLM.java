/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Exceptions;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public class UniformBoundaryLM
/*     */   implements LanguageModel.Dynamic, LanguageModel.Sequence
/*     */ {
/*     */   private final double mLog2EstimatePerChar;
/*     */   private final int mNumOutcomes;
/* 198 */   public static final UniformBoundaryLM ZERO_LM = new UniformBoundaryLM(0);
/*     */ 
/*     */   public UniformBoundaryLM()
/*     */   {
/*  46 */     this(65534);
/*     */   }
/*     */ 
/*     */   public UniformBoundaryLM(int numOutcomes)
/*     */   {
/*  58 */     UniformProcessLM.validateNumOutcomes(numOutcomes + 1);
/*  59 */     this.mNumOutcomes = numOutcomes;
/*  60 */     this.mLog2EstimatePerChar = (-com.aliasi.util.Math.log2(1.0D + numOutcomes));
/*     */   }
/*     */ 
/*     */   public UniformBoundaryLM(double crossEntropyRate)
/*     */   {
/*  92 */     Exceptions.finiteNonNegative("Cross-entropy rate", crossEntropyRate);
/*     */ 
/*  94 */     this.mLog2EstimatePerChar = (-crossEntropyRate);
/*  95 */     this.mNumOutcomes = java.lang.Math.max(0, (int)(java.lang.Math.pow(2.0D, crossEntropyRate) - 1.0D));
/*     */   }
/*     */ 
/*     */   private UniformBoundaryLM(int numOutcomes, double log2EstimatePerChar)
/*     */   {
/* 101 */     this.mNumOutcomes = numOutcomes;
/* 102 */     this.mLog2EstimatePerChar = log2EstimatePerChar;
/*     */   }
/*     */ 
/*     */   public int numOutcomes()
/*     */   {
/* 111 */     return this.mNumOutcomes;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 123 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public void train(char[] cs, int start, int end)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void train(char[] cs, int start, int end, int count)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void train(CharSequence cSeq)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void train(CharSequence cSeq, int count)
/*     */   {
/*     */   }
/*     */ 
/*     */   public double log2Estimate(char[] cs, int start, int end)
/*     */   {
/* 171 */     Strings.checkArgsStartEnd(cs, start, end);
/* 172 */     return log2Estimate(end - start);
/*     */   }
/*     */ 
/*     */   public double log2Estimate(CharSequence cSeq) {
/* 176 */     return log2Estimate(cSeq.length());
/*     */   }
/*     */ 
/*     */   private double log2Estimate(int length) {
/* 180 */     return this.mLog2EstimatePerChar * (1.0D + length);
/*     */   }
/*     */ 
/*     */   private static UniformBoundaryLM createUniformBoundaryLM(int numOutcomes, double log2EstimatePerChar)
/*     */   {
/* 186 */     return new UniformBoundaryLM(numOutcomes, log2EstimatePerChar);
/*     */   }
/*     */ 
/*     */   private static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -5389627995529538230L;
/*     */     private final UniformBoundaryLM mLM;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 205 */       this.mLM = null;
/*     */     }
/*     */     public Externalizer(UniformBoundaryLM lm) {
/* 208 */       this.mLM = lm;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws IOException {
/* 212 */       int numOutcomes = objIn.readInt();
/* 213 */       double log2EstimatePerChar = objIn.readDouble();
/* 214 */       return UniformBoundaryLM.createUniformBoundaryLM(numOutcomes, log2EstimatePerChar);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 218 */       objOut.writeInt(this.mLM.numOutcomes());
/* 219 */       objOut.writeDouble(this.mLM.mLog2EstimatePerChar);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.UniformBoundaryLM
 * JD-Core Version:    0.6.2
 */