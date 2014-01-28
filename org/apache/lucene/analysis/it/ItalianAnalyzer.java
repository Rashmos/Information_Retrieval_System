/*     */ package org.apache.lucene.analysis.it;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
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
/*     */ import org.apache.lucene.analysis.fr.ElisionFilter;
/*     */ import org.apache.lucene.analysis.snowball.SnowballFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ import org.tartarus.snowball.ext.ItalianStemmer;
/*     */ 
/*     */ public final class ItalianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */   private final Set<?> stemExclusionSet;
/*     */   public static final String DEFAULT_STOPWORD_FILE = "italian_stop.txt";
/*  59 */   private static final CharArraySet DEFAULT_ARTICLES = CharArraySet.unmodifiableSet(new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(new String[] { "c", "l", "all", "dall", "dell", "nell", "sull", "coll", "pell", "gl", "agl", "dagl", "degl", "negl", "sugl", "un", "m", "t", "s", "v", "d" }), true));
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  71 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public ItalianAnalyzer(Version matchVersion)
/*     */   {
/*  97 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public ItalianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 107 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public ItalianAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 120 */     super(matchVersion, stopwords);
/* 121 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 140 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 141 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 142 */     if (this.matchVersion.onOrAfter(Version.LUCENE_32)) {
/* 143 */       result = new ElisionFilter(this.matchVersion, result, DEFAULT_ARTICLES);
/*     */     }
/* 145 */     result = new LowerCaseFilter(this.matchVersion, result);
/* 146 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 147 */     if (!this.stemExclusionSet.isEmpty())
/* 148 */       result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/* 149 */     result = new SnowballFilter(result, new ItalianStemmer());
/* 150 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
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
/*  83 */         DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, "italian_stop.txt", IOUtils.CHARSET_UTF_8), Version.LUCENE_CURRENT);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  88 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.it.ItalianAnalyzer
 * JD-Core Version:    0.6.2
 */