/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class NGramTokenizerFactory
/*     */   implements TokenizerFactory, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -3208689473309929010L;
/*     */   private final int mMinNGram;
/*     */   private final int mMaxNGram;
/*     */ 
/*     */   public NGramTokenizerFactory(int minNGram, int maxNGram)
/*     */   {
/*  80 */     if (maxNGram < 1) {
/*  81 */       String msg = "Require max >= 1. Found maxNGram=" + maxNGram;
/*     */ 
/*  83 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  85 */     if (minNGram > maxNGram) {
/*  86 */       String msg = "Require min <= max. Found min=" + minNGram + " max=" + maxNGram;
/*     */ 
/*  89 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  91 */     this.mMinNGram = minNGram;
/*  92 */     this.mMaxNGram = maxNGram;
/*     */   }
/*     */ 
/*     */   public int minNGram()
/*     */   {
/* 103 */     return this.mMinNGram;
/*     */   }
/*     */ 
/*     */   public int maxNGram()
/*     */   {
/* 113 */     return this.mMaxNGram;
/*     */   }
/*     */ 
/*     */   public Tokenizer tokenizer(char[] cs, int start, int length)
/*     */   {
/* 127 */     return new NGramTokenizer(cs, start, length, this.mMinNGram, this.mMaxNGram);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 137 */     return "com.aliasi.tokenizer.NGramTokenizerFactory( min=" + this.mMinNGram + ", max=" + this.mMaxNGram + ")";
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 144 */     return new Externalizer(this);
/*     */   }
/*     */   private static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 7342984199917470310L;
/*     */     final NGramTokenizerFactory mFactory;
/*     */ 
/* 151 */     public Externalizer() { this(null); }
/*     */ 
/*     */     public Externalizer(NGramTokenizerFactory factory) {
/* 154 */       this.mFactory = factory;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 158 */       objOut.writeInt(this.mFactory.mMinNGram);
/* 159 */       objOut.writeInt(this.mFactory.mMaxNGram);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws IOException {
/* 163 */       int minNGram = objIn.readInt();
/* 164 */       int maxNGram = objIn.readInt();
/* 165 */       return new NGramTokenizerFactory(minNGram, maxNGram);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.NGramTokenizerFactory
 * JD-Core Version:    0.6.2
 */