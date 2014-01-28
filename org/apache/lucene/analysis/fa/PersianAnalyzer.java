/*     */ package org.apache.lucene.analysis.fa;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharReader;
/*     */ import org.apache.lucene.analysis.LowerCaseFilter;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.StopwordAnalyzerBase;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.WordlistLoader;
/*     */ import org.apache.lucene.analysis.ar.ArabicLetterTokenizer;
/*     */ import org.apache.lucene.analysis.ar.ArabicNormalizationFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class PersianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */   public static final String STOPWORDS_COMMENT = "#";
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  72 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public PersianAnalyzer(Version matchVersion)
/*     */   {
/*  98 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public PersianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 110 */     super(matchVersion, stopwords);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public PersianAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 119 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public PersianAnalyzer(Version matchVersion, Hashtable<?, ?> stopwords)
/*     */   {
/* 128 */     this(matchVersion, stopwords.keySet());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public PersianAnalyzer(Version matchVersion, File stopwords)
/*     */     throws IOException
/*     */   {
/* 138 */     this(matchVersion, WordlistLoader.getWordSet(IOUtils.getDecodingReader(stopwords, IOUtils.CHARSET_UTF_8), "#", matchVersion));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/*     */     Tokenizer source;
/*     */     Tokenizer source;
/* 157 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31))
/* 158 */       source = new StandardTokenizer(this.matchVersion, reader);
/*     */     else {
/* 160 */       source = new ArabicLetterTokenizer(this.matchVersion, reader);
/*     */     }
/* 162 */     TokenStream result = new LowerCaseFilter(this.matchVersion, source);
/* 163 */     result = new ArabicNormalizationFilter(result);
/*     */ 
/* 165 */     result = new PersianNormalizationFilter(result);
/*     */ 
/* 170 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new StopFilter(this.matchVersion, result, this.stopwords));
/*     */   }
/*     */ 
/*     */   protected Reader initReader(Reader reader)
/*     */   {
/* 178 */     return this.matchVersion.onOrAfter(Version.LUCENE_31) ? new PersianCharFilter(CharReader.get(reader)) : reader;
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
/*  84 */         DEFAULT_STOP_SET = PersianAnalyzer.loadStopwordSet(false, PersianAnalyzer.class, "stopwords.txt", "#");
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  88 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fa.PersianAnalyzer
 * JD-Core Version:    0.6.2
 */