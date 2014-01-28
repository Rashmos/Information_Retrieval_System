/*     */ package org.apache.lucene.analysis.hunspell;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.util.AttributeSource.State;
/*     */ 
/*     */ public final class HunspellStemFilter extends TokenFilter
/*     */ {
/*  35 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  36 */   private final PositionIncrementAttribute posIncAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/*     */   private final HunspellStemmer stemmer;
/*     */   private List<HunspellStemmer.Stem> buffer;
/*     */   private AttributeSource.State savedState;
/*     */   private final boolean dedup;
/*     */ 
/*     */   public HunspellStemFilter(TokenStream input, HunspellDictionary dictionary)
/*     */   {
/*  52 */     this(input, dictionary, true);
/*     */   }
/*     */ 
/*     */   public HunspellStemFilter(TokenStream input, HunspellDictionary dictionary, boolean dedup)
/*     */   {
/*  64 */     super(input);
/*  65 */     this.dedup = dedup;
/*  66 */     this.stemmer = new HunspellStemmer(dictionary);
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*  74 */     if ((this.buffer != null) && (!this.buffer.isEmpty())) {
/*  75 */       HunspellStemmer.Stem nextStem = (HunspellStemmer.Stem)this.buffer.remove(0);
/*  76 */       restoreState(this.savedState);
/*  77 */       this.posIncAtt.setPositionIncrement(0);
/*  78 */       this.termAtt.copyBuffer(nextStem.getStem(), 0, nextStem.getStemLength());
/*  79 */       this.termAtt.setLength(nextStem.getStemLength());
/*  80 */       return true;
/*     */     }
/*     */ 
/*  83 */     if (!this.input.incrementToken()) {
/*  84 */       return false;
/*     */     }
/*     */ 
/*  87 */     this.buffer = (this.dedup ? this.stemmer.uniqueStems(this.termAtt.buffer(), this.termAtt.length()) : this.stemmer.stem(this.termAtt.buffer(), this.termAtt.length()));
/*     */ 
/*  89 */     if (this.buffer.isEmpty()) {
/*  90 */       return true;
/*     */     }
/*     */ 
/*  93 */     HunspellStemmer.Stem stem = (HunspellStemmer.Stem)this.buffer.remove(0);
/*  94 */     this.termAtt.copyBuffer(stem.getStem(), 0, stem.getStemLength());
/*  95 */     this.termAtt.setLength(stem.getStemLength());
/*     */ 
/*  97 */     if (!this.buffer.isEmpty()) {
/*  98 */       this.savedState = captureState();
/*     */     }
/*     */ 
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 109 */     super.reset();
/* 110 */     this.buffer = null;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hunspell.HunspellStemFilter
 * JD-Core Version:    0.6.2
 */