/*     */ package org.apache.lucene.analysis.br;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Collections;
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
/*     */ import org.apache.lucene.analysis.standard.StandardFilter;
/*     */ import org.apache.lucene.analysis.standard.StandardTokenizer;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class BrazilianAnalyzer extends StopwordAnalyzerBase
/*     */ {
/*     */ 
/*     */   @Deprecated
/*  61 */   public static final String[] BRAZILIAN_STOP_WORDS = { "a", "ainda", "alem", "ambas", "ambos", "antes", "ao", "aonde", "aos", "apos", "aquele", "aqueles", "as", "assim", "com", "como", "contra", "contudo", "cuja", "cujas", "cujo", "cujos", "da", "das", "de", "dela", "dele", "deles", "demais", "depois", "desde", "desta", "deste", "dispoe", "dispoem", "diversa", "diversas", "diversos", "do", "dos", "durante", "e", "ela", "elas", "ele", "eles", "em", "entao", "entre", "essa", "essas", "esse", "esses", "esta", "estas", "este", "estes", "ha", "isso", "isto", "logo", "mais", "mas", "mediante", "menos", "mesma", "mesmas", "mesmo", "mesmos", "na", "nas", "nao", "nas", "nem", "nesse", "neste", "nos", "o", "os", "ou", "outra", "outras", "outro", "outros", "pelas", "pelas", "pelo", "pelos", "perante", "pois", "por", "porque", "portanto", "proprio", "propios", "quais", "qual", "qualquer", "quando", "quanto", "que", "quem", "quer", "se", "seja", "sem", "sendo", "seu", "seus", "sob", "sobre", "sua", "suas", "tal", "tambem", "teu", "teus", "toda", "todas", "todo", "todos", "tua", "tuas", "tudo", "um", "uma", "umas", "uns" };
/*     */   public static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";
/* 113 */   private Set<?> excltable = Collections.emptySet();
/*     */ 
/*     */   public static Set<?> getDefaultStopSet()
/*     */   {
/*  90 */     return DefaultSetHolder.DEFAULT_STOP_SET;
/*     */   }
/*     */ 
/*     */   public BrazilianAnalyzer(Version matchVersion)
/*     */   {
/* 119 */     this(matchVersion, DefaultSetHolder.DEFAULT_STOP_SET);
/*     */   }
/*     */ 
/*     */   public BrazilianAnalyzer(Version matchVersion, Set<?> stopwords)
/*     */   {
/* 131 */     super(matchVersion, stopwords);
/*     */   }
/*     */ 
/*     */   public BrazilianAnalyzer(Version matchVersion, Set<?> stopwords, Set<?> stemExclusionSet)
/*     */   {
/* 144 */     this(matchVersion, stopwords);
/* 145 */     this.excltable = CharArraySet.unmodifiableSet(CharArraySet.copy(matchVersion, stemExclusionSet));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BrazilianAnalyzer(Version matchVersion, String[] stopwords)
/*     */   {
/* 155 */     this(matchVersion, StopFilter.makeStopSet(matchVersion, stopwords));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BrazilianAnalyzer(Version matchVersion, Map<?, ?> stopwords)
/*     */   {
/* 164 */     this(matchVersion, stopwords.keySet());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BrazilianAnalyzer(Version matchVersion, File stopwords)
/*     */     throws IOException
/*     */   {
/* 174 */     this(matchVersion, WordlistLoader.getWordSet(IOUtils.getDecodingReader(stopwords, IOUtils.CHARSET_UTF_8), matchVersion));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(String[] exclusionlist)
/*     */   {
/* 184 */     this.excltable = StopFilter.makeStopSet(this.matchVersion, exclusionlist);
/* 185 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(Map<?, ?> exclusionlist)
/*     */   {
/* 193 */     this.excltable = new HashSet(exclusionlist.keySet());
/* 194 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setStemExclusionTable(File exclusionlist)
/*     */     throws IOException
/*     */   {
/* 202 */     this.excltable = WordlistLoader.getWordSet(IOUtils.getDecodingReader(exclusionlist, IOUtils.CHARSET_UTF_8), this.matchVersion);
/*     */ 
/* 204 */     setPreviousTokenStream(null);
/*     */   }
/*     */ 
/*     */   protected ReusableAnalyzerBase.TokenStreamComponents createComponents(String fieldName, Reader reader)
/*     */   {
/* 220 */     Tokenizer source = new StandardTokenizer(this.matchVersion, reader);
/* 221 */     TokenStream result = new LowerCaseFilter(this.matchVersion, source);
/* 222 */     result = new StandardFilter(this.matchVersion, result);
/* 223 */     result = new StopFilter(this.matchVersion, result, this.stopwords);
/* 224 */     if ((this.excltable != null) && (!this.excltable.isEmpty()))
/* 225 */       result = new KeywordMarkerFilter(result, this.excltable);
/* 226 */     return new ReusableAnalyzerBase.TokenStreamComponents(source, new BrazilianStemFilter(result));
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
/*  98 */         DEFAULT_STOP_SET = WordlistLoader.getWordSet(IOUtils.getDecodingReader(BrazilianAnalyzer.class, "stopwords.txt", IOUtils.CHARSET_UTF_8), "#", Version.LUCENE_CURRENT);
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 103 */         throw new RuntimeException("Unable to load default stopword set");
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.br.BrazilianAnalyzer
 * JD-Core Version:    0.6.2
 */