/*     */ package org.apache.lucene.analysis.ngram;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ 
/*     */ public final class NGramTokenizer extends Tokenizer
/*     */ {
/*     */   public static final int DEFAULT_MIN_NGRAM_SIZE = 1;
/*     */   public static final int DEFAULT_MAX_NGRAM_SIZE = 2;
/*     */   private int minGram;
/*     */   private int maxGram;
/*     */   private int gramSize;
/*  37 */   private int pos = 0;
/*     */   private int inLen;
/*     */   private String inStr;
/*  40 */   private boolean started = false;
/*     */ 
/*  42 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  43 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */ 
/*     */   public NGramTokenizer(Reader input, int minGram, int maxGram)
/*     */   {
/*  52 */     super(input);
/*  53 */     init(minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public NGramTokenizer(AttributeSource source, Reader input, int minGram, int maxGram)
/*     */   {
/*  64 */     super(source, input);
/*  65 */     init(minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public NGramTokenizer(AttributeSource.AttributeFactory factory, Reader input, int minGram, int maxGram)
/*     */   {
/*  76 */     super(factory, input);
/*  77 */     init(minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public NGramTokenizer(Reader input)
/*     */   {
/*  85 */     this(input, 1, 2);
/*     */   }
/*     */ 
/*     */   private void init(int minGram, int maxGram) {
/*  89 */     if (minGram < 1) {
/*  90 */       throw new IllegalArgumentException("minGram must be greater than zero");
/*     */     }
/*  92 */     if (minGram > maxGram) {
/*  93 */       throw new IllegalArgumentException("minGram must not be greater than maxGram");
/*     */     }
/*  95 */     this.minGram = minGram;
/*  96 */     this.maxGram = maxGram;
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken()
/*     */     throws IOException
/*     */   {
/* 102 */     clearAttributes();
/* 103 */     if (!this.started) {
/* 104 */       this.started = true;
/* 105 */       this.gramSize = this.minGram;
/* 106 */       char[] chars = new char[1024];
/* 107 */       this.input.read(chars);
/* 108 */       this.inStr = new String(chars).trim();
/* 109 */       this.inLen = this.inStr.length();
/*     */     }
/*     */ 
/* 112 */     if (this.pos + this.gramSize > this.inLen) {
/* 113 */       this.pos = 0;
/* 114 */       this.gramSize += 1;
/* 115 */       if (this.gramSize > this.maxGram)
/* 116 */         return false;
/* 117 */       if (this.pos + this.gramSize > this.inLen) {
/* 118 */         return false;
/*     */       }
/*     */     }
/* 121 */     int oldPos = this.pos;
/* 122 */     this.pos += 1;
/* 123 */     this.termAtt.setEmpty().append(this.inStr, oldPos, oldPos + this.gramSize);
/* 124 */     this.offsetAtt.setOffset(correctOffset(oldPos), correctOffset(oldPos + this.gramSize));
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   public final void end()
/*     */   {
/* 131 */     int finalOffset = this.inLen;
/* 132 */     this.offsetAtt.setOffset(finalOffset, finalOffset);
/*     */   }
/*     */ 
/*     */   public void reset(Reader input) throws IOException
/*     */   {
/* 137 */     super.reset(input);
/* 138 */     reset();
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 143 */     super.reset();
/* 144 */     this.started = false;
/* 145 */     this.pos = 0;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ngram.NGramTokenizer
 * JD-Core Version:    0.6.2
 */