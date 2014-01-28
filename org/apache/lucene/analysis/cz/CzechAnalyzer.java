/*     */ package org.apache.lucene.analysis.cz;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.KeywordMarkerFilter;
/*     */ import org.apache.lucene.analysis.LowerCaseFilter;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase;
/*     */ import org.apache.lucene.analysis.ReusableAnalyzerBase.TokenStreamComponents;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.WordlistLoader;
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class CzechAnalyzer extends ReusableAnalyzerBase
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  66 */   public static final String[] CZECH_STOP_WORDS = { "a", "s", "k", "o", "i", "u", "v", "z", "dnes", "cz", "tímto", "budeš", "budem", "byli", "jseš", "můj", "svým", "ta", "tomto", "tohle", "tuto", "tyto", "jej", "zda", "proč", "máte", "tato", "kam", "tohoto", "kdo", "kteří", "mi", "nám", "tom", "tomuto", "mít", "nic", "proto", "kterou", "byla", "toho", "protože", "asi", "ho", "naši", "napište", "re", "což", "tím", "takže", "svých", "její", "svými", "jste", "aj", "tu", "tedy", "teto", "bylo", "kde", "ke", "pravé", "ji", "nad", "nejsou", "či", "pod", "téma", "mezi", "přes", "ty", "pak", "vám", "ani", "když", "však", "neg", "jsem", "tento", "článku", "články", "aby", "jsme", "před", "pta", "jejich", "byl", "ještě", "až", "bez", "také", "pouze", "první", "vaše", "která", "nás", "nový", "tipy", "pokud", "může", "strana", "jeho", "své", "jiné", "zprávy", "nové", "není", "vás", "jen", "podle", "zde", "už", "být", "více", "bude", "již", "než", "který", "by", "které", "co", "nebo", "ten", "tak", "má", "při", "od", "po", "jsou", "jak", "další", "ale", "si", "se", "ve", "to", "jako", "za", "zpět", "ze", "do", "pro", "je", "na", "atd", "atp", "jakmile", "přičemž", "já", "on", "ona", "ono", "oni", "ony", "my", "vy", "jí", "ji", "mě", "mne", "jemu", "tomu", "těm", "těmu", "němu", "němuž", "jehož", "jíž", "jelikož", "jež", "jakož", "načež" };
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/*     */   private Set<?> stoptable;
/*     */   private final Version matchVersion;
/*     */   private final Set<?> stemExclusionTable;
/*     */ 
/*     */   public static final Set<?> getDefaultStopSet()
/*     */   {
/*  96 */     return DefaultSetHolder.DEFAULT_SET;
/*     */   }
/*     */ 
/*     */   public CzechAnalyzer(Version matchVersion)
/*     */   {
/* 130 */     this(matchVersion, DefaultSetHolder.DEFAULT_SET);
/*     */   }
/*     */ 
/*     */   public CzechAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 141 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public CzechAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionTable)
/*     */   {
/* 154 */     this.matchVersion = matchVersion;
/* 155 */     this.stoptable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stopwords));
/* 156 */     this.stemExclusionTable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionTable));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public CzechAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 170 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public CzechAnalyzer(Version matchVersion, HashSet<?> stopwords)
/*     */   {
/* 183 */     this(matchVersion, stopwords);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public CzechAnalyzer(Version matchVersion, File stopwords)
/*     */     throws IOException
/*     */   {
/* 196 */     this(matchVersion, WordlistLoader.getWordSet(IOUtils.getDecodingReader(stopwords, IOUtils.CHARSET_UTF_8), matchVersion));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void loadStopWords(InputStream wordfile, String encoding)
/*     */   {
/* 210 */     setPreviousTokenStream(null);
/* 211 */     if (wordfile == null) {
/* 212 */       this.stoptable = CharArraySet.EMPTY_SET;
/* 213 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 217 */       this.stoptable = CharArraySet.EMPTY_SET;
/* 218 */       this.stoptable = WordlistLoader.getWordSet(IOUtils.getDecodingReader(wordfile, encoding == null ? IOUtils.CHARSET_UTF_8 : Charset.forName(encoding)), this.matchVersion);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 223 */       this.stoptable = Collections.emptySet();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 244 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 245 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 246 */     result = new LowerCaseFilter(this.matchVersion, result);
/* 247 */     result = new StopFilter(this.matchVersion, result, this.stoptable);
/* 248 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31)) {
/* 249 */       if (!this.stemExclusionTable.isEmpty())
/* 250 */         result = new KeywordMarkerFilter(result, this.stemExclusionTable);
/* 251 */       result = new CzechStemFilter(result);
/*     */     }
/* 253 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
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
/* 104 */         DEFAULT_SET = WordlistLoader.getWordSet(IOUtils.getDecodingReader(CzechAnalyzer.class, "stopwords.txt", IOUtils.CHARSET_UTF_8), "#", Version.LUCENE_CURRENT);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 109 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cz.CzechAnalyzer
 * JD-Core Version:    0.6.2
 */