/*     */ package org.apache.lucene.analysis.fr;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class ElisionFilter extends TokenFilter
/*     */ {
/*  37 */   private CharArraySet articles = CharArraySet.EMPTY_SET;
/*  38 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  39 */   private static final CharArraySet DEFAULT_ARTICLES = CharArraySet.unmodifiableSet(new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(new String[] { "l", "m", "t", "qu", "n", "s", "j" }), true));
/*     */ 
/*  43 */   private static char[] apostrophes = { '\'', '’' };
/*     */ 
/*     */   @Deprecated
/*     */   public void setArticles(Version matchVersion, Set<?> articles)
/*     */   {
/*  53 */     this.articles = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, articles));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setArticles(Set<?> articles)
/*     */   {
/*  64 */     setArticles(Version.LUCENE_CURRENT, articles);
/*     */   }
/*     */ 
/*     */   public ElisionFilter(Version matchVersion, TokenStream input)
/*     */   {
/*  70 */     this(matchVersion, input, DEFAULT_ARTICLES);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ElisionFilter(TokenStream input)
/*     */   {
/*  79 */     this(Version.LUCENE_30, input);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ElisionFilter(TokenStream input, Set<?> articles)
/*     */   {
/*  88 */     this(Version.LUCENE_30, input, articles);
/*     */   }
/*     */ 
/*     */   public ElisionFilter(Version matchVersion, TokenStream input, Set<?> articles)
/*     */   {
/*  98 */     super(input);
/*  99 */     this.articles = CharArraySet.unmodifiableSet(new CharArraySet(matchVersion, articles, true));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ElisionFilter(TokenStream input, String[] articles)
/*     */   {
/* 109 */     this(Version.LUCENE_CURRENT, input, new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(articles), true));
/*     */   }
/*     */ 
/*     */   public final boolean incrementToken()
/*     */     throws IOException
/*     */   {
/* 119 */     if (this.input.incrementToken()) {
/* 120 */       char[] termBuffer = this.termAtt.buffer();
/* 121 */       int termLength = this.termAtt.length();
/*     */ 
/* 123 */       int minPoz = 2147483647;
/* 124 */       for (int i = 0; i < apostrophes.length; i++) {
/* 125 */         char apos = apostrophes[i];
/*     */ 
/* 127 */         for (int poz = 0; poz < termLength; poz++) {
/* 128 */           if (termBuffer[poz] == apos) {
/* 129 */             minPoz = Math.min(poz, minPoz);
/* 130 */             break;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 136 */       if ((minPoz != 2147483647) && (this.articles.contains(this.termAtt.buffer(), 0, minPoz)))
/*     */       {
/* 138 */         this.termAtt.copyBuffer(this.termAtt.buffer(), minPoz + 1, this.termAtt.length() - (minPoz + 1));
/*     */       }
/*     */ 
/* 141 */       return true;
/*     */     }
/* 143 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fr.ElisionFilter
 * JD-Core Version:    0.6.2
 */