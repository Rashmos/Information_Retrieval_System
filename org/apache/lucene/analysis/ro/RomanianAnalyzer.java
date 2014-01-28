/*     */ package org.apache.lucene.analysis.ro;
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
/*     */ import org.apache.lucene.analysis.snowball.SnowballFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.Version;
/*     */ import org.tartarus.snowball.ext.RomanianStemmer;
/*     */ 
/*     */ public final class RomanianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   private final Set<?> stemExclusionSet;
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */   private static final String STOPWORDS_COMMENT = "#";
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  57 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public RomanianAnalyzer(Version matchVersion)
/*     */   {
/*  83 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public RomanianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/*  93 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public RomanianAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 106 */     super(matchVersion, stopwords);
/* 107 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 126 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 127 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 128 */     result = new LowerCaseFilter(this.matchVersion, result);
/* 129 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 130 */     if (!this.stemExclusionSet.isEmpty())
/* 131 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/* 132 */     result = new SnowballFilter(result, new RomanianStemmer());
/* 133 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
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
/*  69 */         DEFAULT_STOP_SET = RomanianAnalyzer.loadStopwordSet(false, RomanianAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  74 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ro.RomanianAnalyzer
 * JD-Core Version:    0.6.2
 */