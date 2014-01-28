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
/*     */ public final class EdgeNGramTokenizer extends Tokenizer
/*     */ {
/*  36 */   public static final Side DEFAULT_SIDE = Side.FRONT;
/*     */   public static final int DEFAULT_MAX_GRAM_SIZE = 1;
/*     */   public static final int DEFAULT_MIN_GRAM_SIZE = 1;
/*  40 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  41 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */   private int minGram;
/*     */   private int maxGram;
/*     */   private int gramSize;
/*     */   private Side side;
/*  76 */   private boolean started = false;
/*     */   private int inLen;
/*     */   private String inStr;
/*     */ 
/*     */   public EdgeNGramTokenizer(Reader input, Side side, int minGram, int maxGram)
/*     */   {
/*  90 */     super(input);
/*  91 */     init(side, minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public EdgeNGramTokenizer(AttributeSource source, Reader input, Side side, int minGram, int maxGram)
/*     */   {
/* 104 */     super(source, input);
/* 105 */     init(side, minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public EdgeNGramTokenizer(AttributeSource.AttributeFactory factory, Reader input, Side side, int minGram, int maxGram)
/*     */   {
/* 118 */     super(factory, input);
/* 119 */     init(side, minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public EdgeNGramTokenizer(Reader input, String sideLabel, int minGram, int maxGram)
/*     */   {
/* 131 */     this(input, Side.getSide(sideLabel), minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public EdgeNGramTokenizer(AttributeSource source, Reader input, String sideLabel, int minGram, int maxGram)
/*     */   {
/* 144 */     this(source, input, Side.getSide(sideLabel), minGram, maxGram);
/*     */   }
/*     */ 
/*     */   public EdgeNGramTokenizer(AttributeSource.AttributeFactory factory, Reader input, String sideLabel, int minGram, int maxGram)
/*     */   {
/* 157 */     this(factory, input, Side.getSide(sideLabel), minGram, maxGram);
/*     */   }
/*     */ 
/*     */   private void init(Side side, int minGram, int maxGram) {
/* 161 */     if (side == null) {
/* 162 */       throw new IllegalArgumentException("sideLabel must be either front or back");
/*     */     }
/*     */ 
/* 165 */     if (minGram < 1) {
/* 166 */       throw new IllegalArgumentException("minGram must be greater than zero");
/*     */     }
/*     */ 
/* 169 */     if (minGram > maxGram) {
/* 170 */       throw new IllegalArgumentException("minGram must not be greater than maxGram");
/*     */     }
/*     */ 
/* 173 */     this.minGram = minGram;
/* 174 */     this.maxGram = maxGram;
/* 175 */     this.side = side;
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken()
/*     */     throws IOException
/*     */   {
/* 181 */     clearAttributes();
/*     */ 
/* 183 */     if (!this.started) {
/* 184 */       this.started = true;
/* 185 */       char[] chars = new char[1024];
/* 186 */       int charsRead = this.input.read(chars);
/* 187 */       this.inStr = new String(chars, 0, charsRead).trim();
/* 188 */       this.inLen = this.inStr.length();
/* 189 */       this.gramSize = this.minGram;
/*     */     }
/*     */ 
/* 193 */     if (this.gramSize > this.inLen) {
/* 194 */       return false;
/*     */     }
/*     */ 
/* 198 */     if (this.gramSize > this.maxGram) {
/* 199 */       return false;
/*     */     }
/*     */ 
/* 203 */     int start = this.side == Side.FRONT ? 0 : this.inLen - this.gramSize;
/* 204 */     int end = start + this.gramSize;
/* 205 */     this.termAtt.setEmpty().append(this.inStr, start, end);
/* 206 */     this.offsetAtt.setOffset(correctOffset(start), correctOffset(end));
/* 207 */     this.gramSize += 1;
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */   public final void end()
/*     */   {
/* 214 */     int finalOffset = this.inLen;
/* 215 */     this.offsetAtt.setOffset(finalOffset, finalOffset);
/*     */   }
/*     */ 
/*     */   public void reset(Reader input) throws IOException
/*     */   {
/* 220 */     super.reset(input);
/* 221 */     reset();
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 226 */     super.reset();
/* 227 */     this.started = false;
/*     */   }
/*     */ 
/*     */   public static abstract enum Side
/*     */   {
/*  47 */     FRONT, 
/*     */ 
/*  53 */     BACK;
/*     */ 
/*     */     public abstract String getLabel();
/*     */ 
/*     */     public static Side getSide(String sideName)
/*     */     {
/*  62 */       if (FRONT.getLabel().equals(sideName)) {
/*  63 */         return FRONT;
/*     */       }
/*  65 */       if (BACK.getLabel().equals(sideName)) {
/*  66 */         return BACK;
/*     */       }
/*  68 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ngram.EdgeNGramTokenizer
 * JD-Core Version:    0.6.2
 */