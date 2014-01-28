/*     */ package org.apache.lucene.analysis.el;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.StopwordAnalyzerBase;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class GreekAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */ 
/*     */   public static final Set<?> getDefaultStopSet()
/*     */   {
/*  63 */     return DefaultSetHolder.DEFAULT_SET;
/*     */   }
/*     */ 
/*     */   public GreekAnalyzer(Version matchVersion)
/*     */   {
/*  86 */     this(matchVersion, DefaultSetHolder.DEFAULT_SET);
/*     */   }
/*     */ 
/*     */   public GreekAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 100 */     super(matchVersion, stopwords);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public GreekAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 110 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public GreekAnalyzer(Version matchVersion, Map<?, ?> stopwords)
/*     */   {
/* 119 */     this(matchVersion, stopwords.keySet());
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 135 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 136 */     TokenStream result = new GreekLowerCaseFilter(this.matchVersion, source);
/* 137 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31))
/* 138 */       result = new StandardFilter(this.matchVersion, result);
/* 139 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 140 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31))
/* 141 */       result = new GreekStemFilter(result);
/* 142 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
/*     */   }
/*     */ 
/*     */   private static class DefaultSetHolder
/*     */   {
/*     */     private static final Set<?> DEFAULT_SET;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/*  71 */         DEFAULT_SET = GreekAnalyzer.loadStopwordSet(false, GreekAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  75 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.el.GreekAnalyzer
 * JD-Core Version:    0.6.2
 */