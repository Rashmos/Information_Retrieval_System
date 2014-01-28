/*     */ package org.apache.lucene.analysis.en;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.KeywordMarkerFilter;
/*     */ import org.apache.lucene.analysis.LowerCaseFilter;
/*     */ import org.apache.lucene.analysis.PorterStemFilter;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.StopwordAnalyzerBase;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.standard.StandardAnalyzer;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class EnglishAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   private final Set<?> stemExclusionSet;
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  48 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public EnglishAnalyzer(Version matchVersion)
/*     */   {
/*  63 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public EnglishAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/*  73 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public EnglishAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/*  86 */     super(matchVersion, stopwords);
/*  87 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 106 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 107 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/*     */ 
/* 109 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31))
/* 110 */       result = new EnglishPossessiveFilter(result);
/* 111 */     result = new LowerCaseFilter(this.matchVersion, result);
/* 112 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 113 */     if (!this.stemExclusionSet.isEmpty())
/* 114 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/* 115 */     result = new PorterStemFilter(result);
/* 116 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
/*     */   }
/*     */ 
/*     */   private static class DefaultSetHolder
/*     */   {
/*  56 */     static final Set<?> DEFAULT_STOP_SET = StandardAnalyzer.STOP_WORDS_SET;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.en.EnglishAnalyzer
 * JD-Core Version:    0.6.2
 */