/*     */ package org.apache.lucene.analysis.fr;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
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
/*     */ import org.tartarus.snowball.ext.FrenchStemmer;
/*     */ 
/*     */ public final class FrenchAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  76 */   public static final String[] FRENCH_STOP_WORDS = { "a", "afin", "ai", "ainsi", "après", "attendu", "au", "aujourd", "auquel", "aussi", "autre", "autres", "aux", "auxquelles", "auxquels", "avait", "avant", "avec", "avoir", "c", "car", "ce", "ceci", "cela", "celle", "celles", "celui", "cependant", "certain", "certaine", "certaines", "certains", "ces", "cet", "cette", "ceux", "chez", "ci", "combien", "comme", "comment", "concernant", "contre", "d", "dans", "de", "debout", "dedans", "dehors", "delà", "depuis", "derrière", "des", "désormais", "desquelles", "desquels", "dessous", "dessus", "devant", "devers", "devra", "divers", "diverse", "diverses", "doit", "donc", "dont", "du", "duquel", "durant", "dès", "elle", "elles", "en", "entre", "environ", "est", "et", "etc", "etre", "eu", "eux", "excepté", "hormis", "hors", "hélas", "hui", "il", "ils", "j", "je", "jusqu", "jusque", "l", "la", "laquelle", "le", "lequel", "les", "lesquelles", "lesquels", "leur", "leurs", "lorsque", "lui", "là", "ma", "mais", "malgré", "me", "merci", "mes", "mien", "mienne", "miennes", "miens", "moi", "moins", "mon", "moyennant", "même", "mêmes", "n", "ne", "ni", "non", "nos", "notre", "nous", "néanmoins", "nôtre", "nôtres", "on", "ont", "ou", "outre", "où", "par", "parmi", "partant", "pas", "passé", "pendant", "plein", "plus", "plusieurs", "pour", "pourquoi", "proche", "près", "puisque", "qu", "quand", "que", "quel", "quelle", "quelles", "quels", "qui", "quoi", "quoique", "revoici", "revoilà", "s", "sa", "sans", "sauf", "se", "selon", "seront", "ses", "si", "sien", "sienne", "siennes", "siens", "sinon", "soi", "soit", "son", "sont", "sous", "suivant", "sur", "ta", "te", "tes", "tien", "tienne", "tiennes", "tiens", "toi", "ton", "tous", "tout", "toute", "toutes", "tu", "un", "une", "va", "vers", "voici", "voilà", "vos", "votre", "vous", "vu", "vôtre", "vôtres", "y", "à", "ça", "ès", "été", "être", "ô" };
/*     */   public static final String DEFAULT_STOPWORD_FILE = "french_stop.txt";
/* 107 */   private Set<?> excltable = CharArraySet.EMPTY_SET;
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/* 114 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public FrenchAnalyzer(Version matchVersion)
/*     */   {
/* 140 */     this(matchVersion, matchVersion.onOrAfter(Version.LUCENE_31) ? DefaultSetHolder.DEFAULT_STOP_SET : DefaultSetHolder.DEFAULT_STOP_SET_30);
/*     */   }
/*     */ 
/*     */   public FrenchAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 154 */     this(matchVersion, stopwords, CharArraySet.EMPTY_SET);
/*     */   }
/*     */ 
/*     */   public FrenchAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclutionSet)
/*     */   {
/* 169 */     super(matchVersion, stopwords);
/* 170 */     this.excltable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclutionSet));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public FrenchAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 181 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public FrenchAnalyzer(Version matchVersion, File stopwords)
/*     */     throws IOException
/*     */   {
/* 191 */     this(matchVersion, WordlistLoader.getWordSet(IOUtils.getDecodingReader(stopwords, IOUtils.CHARSET_UTF_8), matchVersion));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(String[] exclusionlist)
/*     */   {
/* 201 */     this.excltable = StopFilter.makeStopSet(this.matchVersion, exclusionlist);
/* 202 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(Map<?, ?> exclusionlist)
/*     */   {
/* 211 */     this.excltable = new HashSet(exclusionlist.keySet());
/* 212 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(File exclusionlist)
/*     */     throws IOException
/*     */   {
/* 222 */     this.excltable = WordlistLoader.getWordSet(IOUtils.getDecodingReader(exclusionlist, IOUtils.CHARSET_UTF_8), this.matchVersion);
/*     */ 
/* 224 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 242 */     if (this.matchVersion.onOrAfter(Version.LUCENE_31)) {
/* 243 */       Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 244 */       TokenStream result = new StandardFilter(this.matchVersion, source);
/* 245 */       result = new ElisionFilter(this.matchVersion, result);
/* 246 */       result = new LowerCaseFilter(this.matchVersion, result);
/* 247 */       result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 248 */       if (!this.excltable.isEmpty())
/* 249 */         result = new KeywordMarkerFilter(result, this.excltable);
/* 250 */       result = new SnowballFilter(result, new FrenchStemmer());
/* 251 */       return new ReusableAnalyzerBase.TokenStreamComponents(source, result);
/*     */     }
/* 253 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 254 */     TokenStream result = new StandardFilter(this.matchVersion, source);
/* 255 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 256 */     if (!this.excltable.isEmpty())
/* 257 */       result = new KeywordMarkerFilter(result, this.excltable);
/* 258 */     result = new FrenchStemFilter(result);
/*     */ 
/* 260 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new LowerCaseFilter(this.matchVersion, result));
/*     */   }
/*     */ 
/*     */   private static class DefaultSetHolder
/*     */   {
/*     */ 
/*     */     @Deprecated
/* 120 */     static final Set<?> DEFAULT_STOP_SET_30 = CharArraySet.unmodifiableSet(new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(FrenchAnalyzer.FRENCH_STOP_WORDS), false));
/*     */     static final Set<?> DEFAULT_STOP_SET;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/* 126 */         DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, "french_stop.txt", IOUtils.CHARSET_UTF_8), Version.LUCENE_CURRENT);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 131 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fr.FrenchAnalyzer
 * JD-Core Version:    0.6.2
 */