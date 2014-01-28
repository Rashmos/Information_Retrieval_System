/*     */ package org.apache.lucene.analysis.th;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.BreakIterator;
/*     */ import java.util.Locale;
/*     */ import org.apache.lucene.analysis.LowerCaseFilter;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.analysis.util.CharArrayIterator;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class ThaiWordFilter extends TokenFilter
/*     */ {
/*  56 */   public static final boolean DBBI_AVAILABLE = proto.isBoundary(4);
/*     */ 
/*  52 */   private static final BreakIterator proto = BreakIterator.getWordInstance(new Locale("th"));
/*     */ 
/*  58 */   private final BreakIterator breaker = (BreakIterator)proto.clone();
/*  59 */   private final CharArrayIterator charIterator = CharArrayIterator.newWordInstance();
/*     */   private final boolean handlePosIncr;
/*  63 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  64 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*  65 */   private final PositionIncrementAttribute posAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/*     */ 
/*  67 */   private AttributeSource clonedToken = null;
/*  68 */   private CharTermAttribute clonedTermAtt = null;
/*  69 */   private OffsetAttribute clonedOffsetAtt = null;
/*  70 */   private boolean hasMoreTokensInClone = false;
/*     */ 
/*     */   @Deprecated
/*     */   public ThaiWordFilter(TokenStream input)
/*     */   {
/*  77 */     this(Version.LUCENE_30, input);
/*     */   }
/*     */ 
/*     */   public ThaiWordFilter(Version matchVersion, TokenStream input)
/*     */   {
/*  82 */     super(matchVersion.onOrAfter(Version.LUCENE_31) ? input : new LowerCaseFilter(matchVersion, input));
/*     */ 
/*  84 */     if (!DBBI_AVAILABLE)
/*  85 */       throw new UnsupportedOperationException("This JRE does not have support for Thai segmentation");
/*  86 */     this.handlePosIncr = matchVersion.onOrAfter(Version.LUCENE_31);
/*     */   }
/*     */ 
/*     */   public boolean incrementToken() throws IOException
/*     */   {
/*  91 */     if (this.hasMoreTokensInClone) {
/*  92 */       int start = this.breaker.current();
/*  93 */       int end = this.breaker.next();
/*  94 */       if (end != -1) {
/*  95 */         this.clonedToken.copyTo(this);
/*  96 */         this.termAtt.copyBuffer(this.clonedTermAtt.buffer(), start, end - start);
/*  97 */         this.offsetAtt.setOffset(this.clonedOffsetAtt.startOffset() + start, this.clonedOffsetAtt.startOffset() + end);
/*  98 */         if (this.handlePosIncr) this.posAtt.setPositionIncrement(1);
/*  99 */         return true;
/*     */       }
/* 101 */       this.hasMoreTokensInClone = false;
/*     */     }
/*     */ 
/* 104 */     if (!this.input.incrementToken()) {
/* 105 */       return false;
/*     */     }
/*     */ 
/* 108 */     if ((this.termAtt.length() == 0) || (Character.UnicodeBlock.of(this.termAtt.charAt(0)) != Character.UnicodeBlock.THAI)) {
/* 109 */       return true;
/*     */     }
/*     */ 
/* 112 */     this.hasMoreTokensInClone = true;
/*     */ 
/* 115 */     if (this.clonedToken == null) {
/* 116 */       this.clonedToken = cloneAttributes();
/* 117 */       this.clonedTermAtt = ((CharTermAttribute)this.clonedToken.getAttribute(CharTermAttribute.class));
/* 118 */       this.clonedOffsetAtt = ((OffsetAttribute)this.clonedToken.getAttribute(OffsetAttribute.class));
/*     */     } else {
/* 120 */       copyTo(this.clonedToken);
/*     */     }
/*     */ 
/* 124 */     this.charIterator.setText(this.clonedTermAtt.buffer(), 0, this.clonedTermAtt.length());
/* 125 */     this.breaker.setText(this.charIterator);
/* 126 */     int end = this.breaker.next();
/* 127 */     if (end != -1) {
/* 128 */       this.termAtt.setLength(end);
/* 129 */       this.offsetAtt.setOffset(this.clonedOffsetAtt.startOffset(), this.clonedOffsetAtt.startOffset() + end);
/*     */ 
/* 131 */       return true;
/*     */     }
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 138 */     super.reset();
/* 139 */     this.hasMoreTokensInClone = false;
/* 140 */     this.clonedToken = null;
/* 141 */     this.clonedTermAtt = null;
/* 142 */     this.clonedOffsetAtt = null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  55 */     proto.setText("ภาษาไทย");
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.th.ThaiWordFilter
 * JD-Core Version:    0.6.2
 */