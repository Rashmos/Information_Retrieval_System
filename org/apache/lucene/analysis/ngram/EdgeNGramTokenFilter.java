/*     */ package org.apache.lucene.analysis.ngram;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ 
/*     */ public final class EdgeNGramTokenFilter extends TokenFilter
/*     */ {
/*  34 */   public static final Side DEFAULT_SIDE = Side.FRONT;
/*     */   public static final int DEFAULT_MAX_GRAM_SIZE = 1;
/*     */   public static final int DEFAULT_MIN_GRAM_SIZE = 1;
/*     */   private final int minGram;
/*     */   private final int maxGram;
/*     */   private Side side;
/*     */   private char[] curTermBuffer;
/*     */   private int curTermLength;
/*     */   private int curGramSize;
/*     */   private int tokStart;
/*  75 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  76 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */ 
/*     */   public EdgeNGramTokenFilter(TokenStream input, Side side, int minGram, int maxGram)
/*     */   {
/*  87 */     super(input);
/*     */ 
/*  89 */     if (side == null) {
/*  90 */       throw new IllegalArgumentException("sideLabel must be either front or back");
/*     */     }
/*     */ 
/*  93 */     if (minGram < 1) {
/*  94 */       throw new IllegalArgumentException("minGram must be greater than zero");
/*     */     }
/*     */ 
/*  97 */     if (minGram > maxGram) {
/*  98 */       throw new IllegalArgumentException("minGram must not be greater than maxGram");
/*     */     }
/*     */ 
/* 101 */     this.minGram = minGram;
/* 102 */     this.maxGram = maxGram;
/* 103 */     this.side = side;
/*     */   }
/*     */ 
/*     */   public EdgeNGramTokenFilter(TokenStream input, String sideLabel, int minGram, int maxGram)
/*     */   {
/* 115 */     this(input, Side.getSide(sideLabel), minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken() throws IOException
/*     */   {
/*     */     while (true) {
/* 121 */       if (this.curTermBuffer == null) {
/* 122 */         if (!this.input.incrementToken()) {
/* 123 */           return false;
/*     */         }
/* 125 */         this.curTermBuffer = ((char[])this.termAtt.buffer().clone());
/* 126 */         this.curTermLength = this.termAtt.length();
/* 127 */         this.curGramSize = this.minGram;
/* 128 */         this.tokStart = this.offsetAtt.startOffset();
/*     */       }
/*     */ 
/* 131 */       if ((this.curGramSize <= this.maxGram) && 
/* 132 */         (this.curGramSize <= this.curTermLength) && (this.curGramSize <= this.maxGram))
/*     */       {
/* 135 */         int start = this.side == Side.FRONT ? 0 : this.curTermLength - this.curGramSize;
/* 136 */         int end = start + this.curGramSize;
/* 137 */         clearAttributes();
/* 138 */         this.offsetAtt.setOffset(this.tokStart + start, this.tokStart + end);
/* 139 */         this.termAtt.copyBuffer(this.curTermBuffer, start, this.curGramSize);
/* 140 */         this.curGramSize += 1;
/* 141 */         return true;
/*     */       }
/*     */ 
/* 144 */       this.curTermBuffer = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 150 */     super.reset();
/* 151 */     this.curTermBuffer = null;
/*     */   }
/*     */ 
/*     */   public static abstract enum Side
/*     */   {
/*  42 */     FRONT, 
/*     */ 
/*  48 */     BACK;
/*     */ 
/*     */     public abstract String getLabel();
/*     */ 
/*     */     public static Side getSide(String sideName)
/*     */     {
/*  57 */       if (FRONT.getLabel().equals(sideName)) {
/*  58 */         return FRONT;
/*     */       }
/*  60 */       if (BACK.getLabel().equals(sideName)) {
/*  61 */         return BACK;
/*     */       }
/*  63 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter
 * JD-Core Version:    0.6.2
 */