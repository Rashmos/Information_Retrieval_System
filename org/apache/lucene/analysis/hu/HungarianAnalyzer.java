/*     */ package org.apache.lucene.analysis.hu;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
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
/*     */ import org.apache.lucene.analysis.snowball.SnowballFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ import org.tartarus.snowball.ext.HungarianStemmer;
/*     */ 
/*     */ public final class HungarianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   private final Set<?> stemExclusionSet;
/*     */   public static final String DEFAULT_STOPWORD_FILE = "hungarian_stop.txt";
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  54 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public HungarianAnalyzer(Version matchVersion)
/*     */   {
/*  80 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public HungarianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/*  90 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public HungarianAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 103 */     super(matchVersion, stopwords);
/* 104 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 123 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 124 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 125 */     result = new LowerCaseFilter(this.matchVersion, result);
/* 126 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 127 */     if (!this.stemExclusionSet.isEmpty())
/* 128 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/* 129 */     result = new SnowballFilter(result, new HungarianStemmer());
/* 130 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
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
/*  66 */         DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, "hungarian_stop.txt", IOUtils.CHARSET_UTF_8), Version.LUCENE_CURRENT);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  71 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hu.HungarianAnalyzer
 * JD-Core Version:    0.6.2
 */