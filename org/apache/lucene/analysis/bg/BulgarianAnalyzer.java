/*     */ package org.apache.lucene.analysis.bg;
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
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class BulgarianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */ 
/*     */   @Deprecated
/*     */   public static final String STOPWORDS_COMMENT = "#";
/*     */   private final Set<?> stemExclusionSet;
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  72 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public BulgarianAnalyzer(Version matchVersion)
/*     */   {
/* 100 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public BulgarianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 107 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public BulgarianAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 116 */     super(matchVersion, stopwords);
/* 117 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   public ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 134 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 135 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 136 */     result = new LowerCaseFilter(this.matchVersion, result);
/* 137 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 138 */     if (!this.stemExclusionSet.isEmpty())
/* 139 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/* 140 */     result = new BulgarianStemFilter(result);
/* 141 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
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
/*  84 */         DEFAULT_STOP_SET = BulgarianAnalyzer.loadStopwordSet(false, BulgarianAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  88 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.bg.BulgarianAnalyzer
 * JD-Core Version:    0.6.2
 */