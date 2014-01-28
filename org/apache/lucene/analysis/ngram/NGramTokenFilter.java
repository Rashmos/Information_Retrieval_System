/*     */ package org.apache.lucene.analysis.ngram;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ 
/*     */ public final class NGramTokenFilter extends TokenFilter
/*     */ {
/*     */   public static final int DEFAULT_MIN_NGRAM_SIZE = 1;
/*     */   public static final int DEFAULT_MAX_NGRAM_SIZE = 2;
/*     */   private int minGram;
/*     */   private int maxGram;
/*     */   private char[] curTermBuffer;
/*     */   private int curTermLength;
/*     */   private int curGramSize;
/*     */   private int curPos;
/*     */   private int tokStart;
/*  42 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  43 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */ 
/*     */   public NGramTokenFilter(TokenStream input, int minGram, int maxGram)
/*     */   {
/*  52 */     super(input);
/*  53 */     if (minGram < 1) {
/*  54 */       throw new IllegalArgumentException("minGram must be greater than zero");
/*     */     }
/*  56 */     if (minGram > maxGram) {
/*  57 */       throw new IllegalArgumentException("minGram must not be greater than maxGram");
/*     */     }
/*  59 */     this.minGram = minGram;
/*  60 */     this.maxGram = maxGram;
/*     */   }
/*     */ 
/*     */   public NGramTokenFilter(TokenStream input)
/*     */   {
/*  68 */     this(input, 1, 2);
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken() throws IOException
/*     */   {
/*     */     while (true)
/*     */     {
/*  75 */       if (this.curTermBuffer == null) {
/*  76 */         if (!this.input.incrementToken()) {
/*  77 */           return false;
/*     */         }
/*  79 */         this.curTermBuffer = ((char[])this.termAtt.buffer().clone());
/*  80 */         this.curTermLength = this.termAtt.length();
/*  81 */         this.curGramSize = this.minGram;
/*  82 */         this.curPos = 0;
/*  83 */         this.tokStart = this.offsetAtt.startOffset();
/*     */       }
/*     */ 
/*  86 */       while (this.curGramSize <= this.maxGram) {
/*  87 */         if (this.curPos + this.curGramSize <= this.curTermLength) {
/*  88 */           clearAttributes();
/*  89 */           this.termAtt.copyBuffer(this.curTermBuffer, this.curPos, this.curGramSize);
/*  90 */           this.offsetAtt.setOffset(this.tokStart + this.curPos, this.tokStart + this.curPos + this.curGramSize);
/*  91 */           this.curPos += 1;
/*  92 */           return true;
/*     */         }
/*  94 */         this.curGramSize += 1;
/*  95 */         this.curPos = 0;
/*     */       }
/*  97 */       this.curTermBuffer = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 103 */     super.reset();
/* 104 */     this.curTermBuffer = null;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ngram.NGramTokenFilter
 * JD-Core Version:    0.6.2
 */