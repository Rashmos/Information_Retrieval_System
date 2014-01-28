/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Exceptions;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public class UniformProcessLM
/*     */   implements LanguageModel.Dynamic, LanguageModel.Process
/*     */ {
/*     */   private final int mNumOutcomes;
/*     */   private final double mLog2EstimatePerChar;
/*     */ 
/*     */   public UniformProcessLM()
/*     */   {
/*  61 */     this(65535);
/*     */   }
/*     */ 
/*     */   public UniformProcessLM(int numOutcomes)
/*     */   {
/*  73 */     validateNumOutcomes(numOutcomes);
/*  74 */     this.mNumOutcomes = numOutcomes;
/*  75 */     this.mLog2EstimatePerChar = (-com.aliasi.util.Math.log2(numOutcomes));
/*     */   }
/*     */ 
/*     */   public UniformProcessLM(double crossEntropyRate)
/*     */   {
/*  99 */     Exceptions.finiteNonNegative("Cross-entropy rate", crossEntropyRate);
/*     */ 
/* 101 */     this.mLog2EstimatePerChar = (-crossEntropyRate);
/* 102 */     this.mNumOutcomes = ((int)java.lang.Math.pow(2.0D, crossEntropyRate));
/*     */   }
/*     */ 
/*     */   private UniformProcessLM(int numOutcomes, double log2EstimatePerChar) {
/* 106 */     this.mNumOutcomes = numOutcomes;
/* 107 */     this.mLog2EstimatePerChar = log2EstimatePerChar;
/*     */   }
/*     */ 
/*     */   public int numOutcomes()
/*     */   {
/* 116 */     return this.mNumOutcomes;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 128 */     objOut.writeObject(new Externalizer(this));
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
/* 176 */     Strings.checkArgsStartEnd(cs, start, end);
/* 177 */     return log2Estimate(end - start);
/*     */   }
/*     */ 
/*     */   public double log2Estimate(CharSequence cSeq) {
/* 181 */     return log2Estimate(cSeq.length());
/*     */   }
/*     */ 
/*     */   private double log2Estimate(int length) {
/* 185 */     return this.mLog2EstimatePerChar * length;
/*     */   }
/*     */ 
/*     */   static void validateNumOutcomes(int numOutcomes) {
/* 189 */     if (numOutcomes <= 0) {
/* 190 */       String msg = "Number of outcomes must be > 0. Found=" + numOutcomes;
/*     */ 
/* 192 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 194 */     if (numOutcomes > 65535) {
/* 195 */       String msg = "Num outcomes must be <=65535 Found value=" + numOutcomes;
/*     */ 
/* 198 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static UniformProcessLM createUniformProcessLM(int numOutcomes, double log2EstimatePerChar)
/*     */   {
/* 205 */     return new UniformProcessLM(numOutcomes, log2EstimatePerChar);
/*     */   }
/*     */   private static class Externalizer extends AbstractExternalizable {
/*     */     private static final long serialVersionUID = 8496069837136242338L;
/*     */     private final UniformProcessLM mLM;
/*     */ 
/* 212 */     public Externalizer() { this.mLM = null; }
/*     */ 
/*     */     public Externalizer(UniformProcessLM lm) {
/* 215 */       this.mLM = lm;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws IOException {
/* 219 */       int numOutcomes = objIn.readInt();
/* 220 */       double log2EstimatePerChar = objIn.readDouble();
/* 221 */       return UniformProcessLM.createUniformProcessLM(numOutcomes, log2EstimatePerChar);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 225 */       objOut.writeInt(this.mLM.numOutcomes());
/* 226 */       objOut.writeDouble(this.mLM.mLog2EstimatePerChar);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.UniformProcessLM
 * JD-Core Version:    0.6.2
 */