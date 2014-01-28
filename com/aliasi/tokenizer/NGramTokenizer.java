/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ class NGramTokenizer extends Tokenizer
/*     */ {
/*     */   private final char[] mChars;
/*     */   private final int mOffset;
/*     */   private final int mLength;
/*     */   private final int mMaxNGram;
/*     */   private int mCurrentSize;
/*     */   private int mNextStart;
/*  61 */   private int mLastTokenStartPosition = -1;
/*  62 */   private int mLastTokenEndPosition = -1;
/*     */ 
/*     */   public NGramTokenizer(char[] ch, int offset, int length, int minNGram, int maxNGram)
/*     */   {
/*  79 */     if (minNGram > maxNGram) {
/*  80 */       String msg = "Require min n-gram to be less than max n-gramfound min=" + minNGram + "found max=" + maxNGram;
/*     */ 
/*  83 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  85 */     this.mChars = ch;
/*  86 */     this.mOffset = offset;
/*  87 */     this.mLength = length;
/*     */ 
/*  89 */     this.mMaxNGram = maxNGram;
/*     */ 
/*  91 */     this.mCurrentSize = minNGram;
/*  92 */     this.mNextStart = this.mOffset;
/*     */   }
/*     */ 
/*     */   public int lastTokenStartPosition()
/*     */   {
/* 105 */     return this.mLastTokenStartPosition;
/*     */   }
/*     */ 
/*     */   public int lastTokenEndPosition()
/*     */   {
/* 110 */     return this.mLastTokenEndPosition;
/*     */   }
/*     */ 
/*     */   public String nextToken()
/*     */   {
/* 122 */     while ((this.mCurrentSize <= this.mMaxNGram) && (this.mNextStart + this.mCurrentSize > this.mOffset + this.mLength)) {
/* 123 */       this.mCurrentSize += 1;
/* 124 */       this.mNextStart = this.mOffset;
/*     */     }
/* 126 */     if (this.mCurrentSize > this.mMaxNGram) return null;
/* 127 */     this.mLastTokenStartPosition = (this.mNextStart - this.mOffset);
/* 128 */     this.mLastTokenEndPosition = (this.mLastTokenStartPosition + this.mCurrentSize);
/* 129 */     return new String(this.mChars, this.mNextStart++, this.mCurrentSize);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.NGramTokenizer
 * JD-Core Version:    0.6.2
 */