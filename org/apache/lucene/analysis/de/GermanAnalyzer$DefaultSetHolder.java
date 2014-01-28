/*     */ package org.apache.lucene.analysis.de;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.WordlistLoader;
/*     */ import org.apache.lucene.analysis.snowball.SnowballFilter;
/*     */ import org.apache.lucene.util.IOUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ class GermanAnalyzer$DefaultSetHolder
/*     */ {
/*     */ 
/*     */   @Deprecated
/* 105 */   private static final Set<?> DEFAULT_SET_30 = CharArraySet.unmodifiableSet(new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(GermanAnalyzer.GERMAN_STOP_WORDS), false));
/*     */   private static final Set<?> DEFAULT_SET;
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 110 */       DEFAULT_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, "german_stop.txt", IOUtils.CHARSET_UTF_8), Version.LUCENE_CURRENT);
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 115 */       throw new RuntimeException("Unable to load default stopword set");
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.de.GermanAnalyzer.DefaultSetHolder
 * JD-Core Version:    0.6.2
 */