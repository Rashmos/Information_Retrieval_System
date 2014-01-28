/*     */ package org.apache.lucene.analysis.hi;
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
/*     */ import org.apache.lucene.analysis.in.IndicNormalizationFilter;
/*     */ import org.apache.lucene.analysis.in.IndicTokenizer;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class HindiAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   private final Set<?> stemExclusionSet;
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */   private static final String STOPWORDS_COMMENT = "#";
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  55 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public HindiAnalyzer(Version version, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/*  84 */     super(version, stopwords);
/*  85 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(this.matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   public HindiAnalyzer(Version version, Set<?> stopwords)
/*     */   {
/*  96 */     this(version, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public HindiAnalyzer(Version version)
/*     */   {
/* 104 */     this(version, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 122 */     Tokenizer source = new IndicTokenizer(this.matchVersion, reader);
/* 123 */     TokenStream result = new LowerCaseFilter(this.matchVersion, source);
/* 124 */     if (!this.stemExclusionSet.isEmpty())
/* 125 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/* 126 */     result = new IndicNormalizationFilter(result);
/* 127 */     result = new HindiNormalizationFilter(result);
/* 128 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 129 */     result = new HindiStemFilter(result);
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
/*  67 */         DEFAULT_STOP_SET = HindiAnalyzer.loadStopwordSet(false, HindiAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  71 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hi.HindiAnalyzer
 * JD-Core Version:    0.6.2
 */