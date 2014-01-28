/*     */ package org.apache.lucene.analysis.tr;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.KeywordMarkerFilter;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.StopwordAnalyzerBase;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.snowball.SnowballFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.Version;
/*     */ import org.tartarus.snowball.ext.TurkishStemmer;
/*     */ 
/*     */ public final class TurkishAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   private final Set<?> stemExclusionSet;
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */   private static final String STOPWORDS_COMMENT = "#";
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  56 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public TurkishAnalyzer(Version matchVersion)
/*     */   {
/*  82 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public TurkishAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/*  92 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public TurkishAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 105 */     super(matchVersion, stopwords);
/* 106 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 125 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 126 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 127 */     result = new TurkishLowerCaseFilter(result);
/* 128 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 129 */     if (!this.stemExclusionSet.isEmpty())
/* 130 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/* 131 */     result = new SnowballFilter(result, new TurkishStemmer());
/* 132 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
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
/*  68 */         DEFAULT_STOP_SET = TurkishAnalyzer.loadStopwordSet(false, TurkishAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  73 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tr.TurkishAnalyzer
 * JD-Core Version:    0.6.2
 */