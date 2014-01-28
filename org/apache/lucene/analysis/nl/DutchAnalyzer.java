/*     */ package org.apache.lucene.analysis.nl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArrayMap;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.KeywordMarkerFilter;
/*     */ import org.apache.lucene.analysis.LowerCaseFilter;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.WordlistLoader;
/*     */ import org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilter;
/*     */ import org.apache.lucene.analysis.snowball.SnowballFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ import org.tartarus.snowball.ext.DutchStemmer;
/*     */ 
/*     */ public final class DutchAnalyzer extends ReusableAnalyzerBase
/*     */ {
/*     */ 
/*     */   @Deprecated
/*     */   public static final String[] DUTCH_STOP_WORDS;
/*     */   public static final String DEFAULT_STOPWORD_FILE = "dutch_stop.txt";
/*     */   private final Set<?> stoptable;
/* 125 */   private Set<?> excltable = Collections.emptySet();
/*     */ 
/* 127 */   private Map<Object, String> stemdict = CharArrayMap.emptyMap();
/*     */   private final Version matchVersion;
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  97 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public DutchAnalyzer(Version matchVersion)
/*     */   {
/* 136 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/* 137 */     this.stemdict = new CharArrayMap(matchVersion, 16, false);
/* 138 */     this.stemdict.put("fiets", "fiets");
/* 139 */     this.stemdict.put("bromfiets", "bromfiets");
/* 140 */     this.stemdict.put("ei", "eier");
/* 141 */     this.stemdict.put("kind", "kinder");
/*     */   }
/*     */ 
/*     */   public DutchAnalyzer(Version matchVersion, Set<?> stopwords) {
/* 145 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public DutchAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionTable) {
/* 149 */     this.stoptable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stopwords));
/* 150 */     this.excltable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionTable));
/* 151 */     this.matchVersion = matchVersion;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DutchAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 163 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DutchAnalyzer(Version matchVersion, HashSet<?> stopwords)
/*     */   {
/* 174 */     this(matchVersion, stopwords);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public DutchAnalyzer(Version matchVersion, File stopwords)
/*     */   {
/*     */     try
/*     */     {
/* 187 */       this.stoptable = WordlistLoader.getWordSet(IOUtils.getDecodingReader(stopwords, IOUtils.CHARSET_UTF_8), matchVersion);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 191 */       throw new RuntimeException(e);
/*     */     }
/* 193 */     this.matchVersion = matchVersion;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(String[] exclusionlist)
/*     */   {
/* 204 */     this.excltable = StopFilter.makeStopSet(this.matchVersion, exclusionlist);
/* 205 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(HashSet<?> exclusionlist)
/*     */   {
/* 214 */     this.excltable = exclusionlist;
/* 215 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(File exclusionlist)
/*     */   {
/*     */     try
/*     */     {
/* 226 */       this.excltable = WordlistLoader.getWordSet(IOUtils.getDecodingReader(exclusionlist, IOUtils.CHARSET_UTF_8), this.matchVersion);
/*     */ 
/* 228 */       setPreviousTokenStream(null);
/*     */     }
/*     */     catch (IOException e) {
/* 231 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemDictionary(File stemdictFile)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       this.stemdict = WordlistLoader.getStemDict(IOUtils.getDecodingReader(stemdictFile, IOUtils.CHARSET_UTF_8), new CharArrayMap(this.matchVersion, 16, false));
/*     */ 
/* 247 */       setPreviousTokenStream(null);
/*     */     }
/*     */     catch (IOException e) {
/* 250 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader aReader)
/*     */   {
/* 267 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31)) {
/* 268 */       Tokenizer source = new StandardTokenizer(this.matchVersion, aReader);
/* 269 */       TokenStream result = new StandardFilter(this.matchVersion, source);
/* 270 */       result = new LowerCaseFilter(this.matchVersion, result);
/* 271 */       result = new StopFilter(this.matchVersion, result, this.stoptable);
/* 272 */       if (!this.excltable.isEmpty())
/* 273 */         result = new KeywordMarkerFilter(result, this.excltable);
/* 274 */       if (!this.stemdict.isEmpty())
/* 275 */         result = new StemmerOverrideFilter(this.matchVersion, result, this.stemdict);
/* 276 */       result = new SnowballFilter(result, new DutchStemmer());
/* 277 */       return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
/*     */     }
/* 279 */     Tokenizer source = new StandardTokenizer(this.matchVersion, aReader);
/* 280 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 281 */     result = new StopFilter(this.matchVersion, result, this.stoptable);
/* 282 */     if (!this.excltable.isEmpty())
/* 283 */       result = new KeywordMarkerFilter(result, this.excltable);
/* 284 */     result = new DutchStemFilter(result, this.stemdict);
/* 285 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  84 */     Set defaultStopSet = getDefaultStopSet();
/*  85 */     DUTCH_STOP_WORDS = new String[defaultStopSet.size()];
/*  86 */     int i = 0;
/*  87 */     for (Iterator i$ = defaultStopSet.iterator(); i$.hasNext(); ) { Object object = i$.next();
/*  88 */       DUTCH_STOP_WORDS[(i++)] = new String((char[])(char[])object);
/*     */     }
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
/* 105 */         DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, "dutch_stop.txt", IOUtils.CHARSET_UTF_8), Version.LUCENE_CURRENT);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 110 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.nl.DutchAnalyzer
 * JD-Core Version:    0.6.2
 */