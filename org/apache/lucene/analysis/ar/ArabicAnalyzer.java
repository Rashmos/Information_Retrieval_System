/*     */ package org.apache.lucene.analysis.ar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.KeywordMarkerFilter;
/*     */ import org.apache.lucene.analysis.LowerCaseFilter;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.StopwordAnalyzerBase;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.WordlistLoader;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class ArabicAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */ 
/*     */   @Deprecated
/*     */   public static final String STOPWORDS_COMMENT = "#";
/*     */   private final Set<?> stemExclusionSet;
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  79 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public ArabicAnalyzer(Version matchVersion)
/*     */   {
/* 106 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public ArabicAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 118 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public ArabicAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 134 */     super(matchVersion, stopwords);
/* 135 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ArabicAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 145 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ArabicAnalyzer(Version matchVersion, Hashtable<?, ?> stopwords)
/*     */   {
/* 154 */     this(matchVersion, stopwords.keySet());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ArabicAnalyzer(Version matchVersion, File stopwords)
/*     */     throws IOException
/*     */   {
/* 163 */     this(matchVersion, WordlistLoader.getWordSet(IOUtils.getDecodingReader(stopwords, IOUtils.CHARSET_UTF_8), "#", matchVersion));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 181 */     Tokenizer source = this.matchVersion.onOrAfter(Version.LUCENE_31) ? new StandardTokenizer(this.matchVersion, reader) : new ArabicLetterTokenizer(this.matchVersion, reader);
/*     */ 
/* 183 */     TokenStream result = new LowerCaseFilter(this.matchVersion, source);
/*     */ 
/* 185 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/*     */ 
/* 187 */     result = new ArabicNormalizationFilter(result);
/* 188 */     if (!this.stemExclusionSet.isEmpty()) {
/* 189 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/*     */     }
/* 191 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new ArabicStemFilter(result));
/*     */   }
/*     */ 
/*     */   private static class DefaultSetHolder
/*     */   {
/*     */     static final Set<?> DEFAULT_STOP_SET;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/*  91 */         DEFAULT_STOP_SET = ArabicAnalyzer.loadStopwordSet(false, ArabicAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  95 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ar.ArabicAnalyzer
 * JD-Core Version:    0.6.2
 */