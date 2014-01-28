/*     */ package org.apache.lucene.analysis.cjk;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.StopwordAnalyzerBase;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class CJKAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  46 */   public static final String[] STOP_WORDS = { "a", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "s", "such", "t", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with", "", "www" };
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  64 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public CJKAnalyzer(Version matchVersion)
/*     */   {
/*  79 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public CJKAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/*  91 */     super(matchVersion, stopwords);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public CJKAnalyzer(Version matchVersion, String[] stopWords)
/*     */   {
/* 102 */     super(matchVersion, StopFilter.makeStopSet(matchVersion, stopWords));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 110 */     Tokenizer source = new CJKTokenizer(reader);
/* 111 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new StopFilter(this.matchVersion, source, this.stopwords));
/*     */   }
/*     */ 
/*     */   private static class DefaultSetHolder
/*     */   {
/*  68 */     static final Set<?> DEFAULT_STOP_SET = CharArraySet.unmodifiableSet(new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(CJKAnalyzer.STOP_WORDS), false));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cjk.CJKAnalyzer
 * JD-Core Version:    0.6.2
 */