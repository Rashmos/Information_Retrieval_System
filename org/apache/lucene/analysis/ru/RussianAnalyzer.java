/*     */ package org.apache.lucene.analysis.ru;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
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
/*     */ import org.tartarus.snowball.ext.RussianStemmer;
/*     */ 
/*     */ public final class RussianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  63 */   private static final String[] RUSSIAN_STOP_WORDS_30 = { "а", "без", "более", "бы", "был", "была", "были", "было", "быть", "в", "вам", "вас", "весь", "во", "вот", "все", "всего", "всех", "вы", "где", "да", "даже", "для", "до", "его", "ее", "ей", "ею", "если", "есть", "еще", "же", "за", "здесь", "и", "из", "или", "им", "их", "к", "как", "ко", "когда", "кто", "ли", "либо", "мне", "может", "мы", "на", "надо", "наш", "не", "него", "нее", "нет", "ни", "них", "но", "ну", "о", "об", "однако", "он", "она", "они", "оно", "от", "очень", "по", "под", "при", "с", "со", "так", "также", "такой", "там", "те", "тем", "то", "того", "тоже", "той", "только", "том", "ты", "у", "уже", "хотя", "чего", "чей", "чем", "что", "чтобы", "чье", "чья", "эта", "эти", "это", "я" };
/*     */   public static final String DEFAULT_STOPWORD_FILE = "russian_stop.txt";
/*     */   private final Set<?> stemExclusionSet;
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/* 107 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public RussianAnalyzer(Version matchVersion) {
/* 111 */     this(matchVersion, matchVersion.onOrAfter(Version.LUCENE_31) ? DefaultSetHolder.DEFAULT_STOP_SET : DefaultSetHolder.DEFAULT_STOP_SET_30);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public RussianAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 122 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   public RussianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 134 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public RussianAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 147 */     super(matchVersion, stopwords);
/* 148 */     this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public RussianAnalyzer(Version matchVersion, Map<?, ?> stopwords)
/*     */   {
/* 160 */     this(matchVersion, stopwords.keySet());
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 177 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31)) {
/* 178 */       Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 179 */       TokenStream result = new StandardFilter(this.matchVersion, source);
/* 180 */       result = new LowerCaseFilter(this.matchVersion, result);
/* 181 */       result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 182 */       if (!this.stemExclusionSet.isEmpty()) result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/*     */ 
/* 184 */       result = new SnowballFilter(result, new RussianStemmer());
/* 185 */       return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
/*     */     }
/* 187 */     Tokenizer source = new RussianLetterTokenizer(this.matchVersion, reader);
/* 188 */     TokenStream result = new LowerCaseFilter(this.matchVersion, source);
/* 189 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 190 */     if (!this.stemExclusionSet.isEmpty()) result = new KeywordMarkerFilter(result, this.stemExclusionSet);
/*     */ 
/* 192 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new RussianStemFilter(result));
/*     */   }
/*     */ 
/*     */   private static class DefaultSetHolder
/*     */   {
/*     */ 
/*     */     @Deprecated
/*  82 */     static final Set<?> DEFAULT_STOP_SET_30 = CharArraySet.unmodifiableSet(new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(RussianAnalyzer.RUSSIAN_STOP_WORDS_30), false));
/*     */     static final Set<?> DEFAULT_STOP_SET;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/*  89 */         DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, "russian_stop.txt", IOUtils.CHARSET_UTF_8), Version.LUCENE_CURRENT);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  94 */         throw new RuntimeException("Unable to load default stopword set", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ru.RussianAnalyzer
 * JD-Core Version:    0.6.2
 */