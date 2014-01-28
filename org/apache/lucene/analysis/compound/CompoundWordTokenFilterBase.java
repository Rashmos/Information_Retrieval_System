/*     */ package org.apache.lucene.analysis.compound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*     */ import org.apache.lucene.util.AttributeSource.State;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public abstract class CompoundWordTokenFilterBase extends TokenFilter
/*     */ {
/*     */   public static final int DEFAULT_MIN_WORD_SIZE = 5;
/*     */   public static final int DEFAULT_MIN_SUBWORD_SIZE = 2;
/*     */   public static final int DEFAULT_MAX_SUBWORD_SIZE = 15;
/*     */   protected final CharArraySet dictionary;
/*     */   protected final LinkedList<CompoundToken> tokens;
/*     */   protected final int minWordSize;
/*     */   protected final int minSubwordSize;
/*     */   protected final int maxSubwordSize;
/*     */   protected final boolean onlyLongestMatch;
/*  77 */   protected final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  78 */   protected final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*  79 */   private final PositionIncrementAttribute posIncAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/*     */   private AttributeSource.State current;
/*     */ 
/*     */   @Deprecated
/*     */   protected CompoundWordTokenFilterBase(TokenStream input, String[] dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/*  88 */     this(Version.LUCENE_30, input, makeDictionary(Version.LUCENE_30, dictionary), minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected CompoundWordTokenFilterBase(TokenStream input, String[] dictionary, boolean onlyLongestMatch)
/*     */   {
/*  96 */     this(Version.LUCENE_30, input, makeDictionary(Version.LUCENE_30, dictionary), 5, 2, 15, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected CompoundWordTokenFilterBase(TokenStream input, Set<?> dictionary, boolean onlyLongestMatch)
/*     */   {
/* 104 */     this(Version.LUCENE_30, input, dictionary, 5, 2, 15, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected CompoundWordTokenFilterBase(TokenStream input, String[] dictionary)
/*     */   {
/* 112 */     this(Version.LUCENE_30, input, makeDictionary(Version.LUCENE_30, dictionary), 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected CompoundWordTokenFilterBase(TokenStream input, Set<?> dictionary)
/*     */   {
/* 120 */     this(Version.LUCENE_30, input, dictionary, 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected CompoundWordTokenFilterBase(TokenStream input, Set<?> dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch)
/*     */   {
/* 128 */     this(Version.LUCENE_30, input, dictionary, minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   protected CompoundWordTokenFilterBase(Version matchVersion, TokenStream input, String[] dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch) {
/* 132 */     this(matchVersion, input, makeDictionary(matchVersion, dictionary), minWordSize, minSubwordSize, maxSubwordSize, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   protected CompoundWordTokenFilterBase(Version matchVersion, TokenStream input, String[] dictionary, boolean onlyLongestMatch) {
/* 136 */     this(matchVersion, input, makeDictionary(matchVersion, dictionary), 5, 2, 15, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   protected CompoundWordTokenFilterBase(Version matchVersion, TokenStream input, Set<?> dictionary, boolean onlyLongestMatch) {
/* 140 */     this(matchVersion, input, dictionary, 5, 2, 15, onlyLongestMatch);
/*     */   }
/*     */ 
/*     */   protected CompoundWordTokenFilterBase(Version matchVersion, TokenStream input, String[] dictionary) {
/* 144 */     this(matchVersion, input, makeDictionary(matchVersion, dictionary), 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   protected CompoundWordTokenFilterBase(Version matchVersion, TokenStream input, Set<?> dictionary) {
/* 148 */     this(matchVersion, input, dictionary, 5, 2, 15, false);
/*     */   }
/*     */ 
/*     */   protected CompoundWordTokenFilterBase(Version matchVersion, TokenStream input, Set<?> dictionary, int minWordSize, int minSubwordSize, int maxSubwordSize, boolean onlyLongestMatch) {
/* 152 */     super(input);
/*     */ 
/* 154 */     this.tokens = new LinkedList();
/* 155 */     this.minWordSize = minWordSize;
/* 156 */     this.minSubwordSize = minSubwordSize;
/* 157 */     this.maxSubwordSize = maxSubwordSize;
/* 158 */     this.onlyLongestMatch = onlyLongestMatch;
/*     */ 
/* 160 */     if ((dictionary == null) || ((dictionary instanceof CharArraySet)))
/* 161 */       this.dictionary = ((CharArraySet)dictionary);
/*     */     else
/* 163 */       this.dictionary = new CharArraySet(matchVersion, dictionary, true);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static CharArraySet makeDictionary(Version matchVersion, String[] dictionary)
/*     */   {
/* 170 */     if (dictionary == null) {
/* 171 */       return null;
/*     */     }
/* 173 */     return new CharArraySet(matchVersion, Arrays.asList(dictionary), true);
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken() throws IOException
/*     */   {
/* 178 */     if (!this.tokens.isEmpty()) {
/* 179 */       assert (this.current != null);
/* 180 */       CompoundToken token = (CompoundToken)this.tokens.removeFirst();
/* 181 */       restoreState(this.current);
/* 182 */       this.termAtt.setEmpty().append(token.txt);
/* 183 */       this.offsetAtt.setOffset(token.startOffset, token.endOffset);
/* 184 */       this.posIncAtt.setPositionIncrement(0);
/* 185 */       return true;
/*     */     }
/*     */ 
/* 188 */     this.current = null;
/* 189 */     if (this.input.incrementToken())
/*     */     {
/* 191 */       if (this.termAtt.length() >= this.minWordSize) {
/* 192 */         decompose();
/*     */ 
/* 194 */         if (!this.tokens.isEmpty()) {
/* 195 */           this.current = captureState();
/*     */         }
/*     */       }
/*     */ 
/* 199 */       return true;
/*     */     }
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   protected abstract void decompose();
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 212 */     super.reset();
/* 213 */     this.tokens.clear();
/* 214 */     this.current = null;
/*     */   }
/*     */ 
/*     */   protected class CompoundToken
/*     */   {
/*     */     public final CharSequence txt;
/*     */     public final int startOffset;
/*     */     public final int endOffset;
/*     */ 
/*     */     public CompoundToken(int offset, int length) {
/* 226 */       int newStart = CompoundWordTokenFilterBase.this.offsetAtt.startOffset() + offset;
/* 227 */       this.txt = CompoundWordTokenFilterBase.this.termAtt.subSequence(offset, offset + length);
/*     */ 
/* 231 */       this.startOffset = newStart;
/* 232 */       this.endOffset = (newStart + length);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.CompoundWordTokenFilterBase
 * JD-Core Version:    0.6.2
 */