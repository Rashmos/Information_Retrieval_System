/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ class CharacterTokenizer extends Tokenizer
/*     */ {
/*     */   private final char[] mChars;
/*     */   private final int mLastPosition;
/*     */   private int mPosition;
/*     */   private final int mStartPosition;
/*  34 */   private int mLastTokenStartPosition = -1;
/*  35 */   private int mLastTokenEndPosition = -1;
/*     */ 
/*     */   public CharacterTokenizer(char[] ch, int offset, int length)
/*     */   {
/*  49 */     if (offset < 0) {
/*  50 */       String msg = "Offset must be greater than 0. Found offset=" + offset;
/*     */ 
/*  52 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  54 */     if (length < 0) {
/*  55 */       String msg = "Length must be greater than 0. Found length=" + length;
/*     */ 
/*  57 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  59 */     if (offset + length > ch.length) {
/*  60 */       String msg = "Offset Plus length must be less than or equal array length. Found ch.length=" + ch.length + " offset=" + offset + " length=" + length + " (offset+length)=" + (offset + length);
/*     */ 
/*  65 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  67 */     this.mChars = ch;
/*  68 */     this.mPosition = offset;
/*  69 */     this.mStartPosition = offset;
/*  70 */     this.mLastPosition = (offset + length);
/*     */   }
/*     */ 
/*     */   public int lastTokenStartPosition() {
/*  74 */     return this.mLastTokenStartPosition;
/*     */   }
/*     */ 
/*     */   public int lastTokenEndPosition() {
/*  78 */     return this.mLastTokenEndPosition;
/*     */   }
/*     */ 
/*     */   public String nextWhitespace()
/*     */   {
/*  83 */     StringBuilder sb = new StringBuilder();
/*     */ 
/*  85 */     while ((hasMoreCharacters()) && (Character.isWhitespace(currentChar()))) {
/*  86 */       sb.append(currentChar());
/*  87 */       this.mPosition += 1;
/*     */     }
/*  89 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String nextToken() {
/*  93 */     skipWhitespace();
/*  94 */     if (!hasMoreCharacters()) return null;
/*  95 */     this.mLastTokenStartPosition = (this.mPosition - this.mStartPosition);
/*  96 */     this.mLastTokenEndPosition = (this.mLastTokenStartPosition + 1);
/*  97 */     return new String(new char[] { this.mChars[(this.mPosition++)] });
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 108 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   private void skipWhitespace()
/*     */   {
/* 114 */     while ((hasMoreCharacters()) && (Character.isWhitespace(currentChar())))
/* 115 */       this.mPosition += 1;
/*     */   }
/*     */ 
/*     */   private boolean hasMoreCharacters() {
/* 119 */     return this.mPosition < this.mLastPosition;
/*     */   }
/*     */ 
/*     */   private char currentChar() {
/* 123 */     return this.mChars[this.mPosition];
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.CharacterTokenizer
 * JD-Core Version:    0.6.2
 */