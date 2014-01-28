/*     */ package org.apache.lucene.analysis.id;
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
/*     */ public final class IndonesianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */   private final Set<?> stemExclusionSet;
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  47 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public IndonesianAnalyzer(Version matchVersion)
/*     */   {
/*  74 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public IndonesianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/*  86 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public IndonesianAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 102 */     super(matchVersion, stopwords);
/* 103 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 121 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 122 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 123 */     result = new LowerCaseFilter(this.matchVersion, result);
/* 124 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 125 */     if (!this.stemExclusionSet.isEmpty()) {
/* 126 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/*     */     }
/* 128 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new IndonesianStemFilter(result));
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
/*  59 */         DEFAULT_STOP_SET = IndonesianAnalyzer.loadStopwordSet(false, IndonesianAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  63 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.id.IndonesianAnalyzer
 * JD-Core Version:    0.6.2
 */