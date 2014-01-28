/*     */ package org.apache.lucene.analysis.miscellaneous;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.lucene.analysis.Analyzer;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.analysis.StopAnalyzer;
/*     */ import org.apache.lucene.analysis.StopFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class PatternAnalyzer extends Analyzer
/*     */ {
/*  70 */   public static final Pattern NON_WORD_PATTERN = Pattern.compile("\\W+");
/*     */ 
/*  73 */   public static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
/*     */ 
/*  75 */   private static final CharArraySet EXTENDED_ENGLISH_STOP_WORDS = CharArraySet.unmodifiableSet(new CharArraySet(Version.LUCENE_CURRENT, Arrays.asList(new String[] { "a", "about", "above", "across", "adj", "after", "afterwards", "again", "against", "albeit", "all", "almost", "alone", "along", "already", "also", "although", "always", "among", "amongst", "an", "and", "another", "any", "anyhow", "anyone", "anything", "anywhere", "are", "around", "as", "at", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "both", "but", "by", "can", "cannot", "co", "could", "down", "during", "each", "eg", "either", "else", "elsewhere", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "first", "for", "former", "formerly", "from", "further", "had", "has", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "i", "ie", "if", "in", "inc", "indeed", "into", "is", "it", "its", "itself", "last", "latter", "latterly", "least", "less", "ltd", "many", "may", "me", "meanwhile", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "namely", "neither", "never", "nevertheless", "next", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", "per", "perhaps", "rather", "s", "same", "seem", "seemed", "seeming", "seems", "several", "she", "should", "since", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "t", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefor", "therein", "thereupon", "these", "they", "this", "those", "though", "through", "throughout", "thru", "thus", "to", "together", "too", "toward", "towards", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "whatsoever", "when", "whence", "whenever", "whensoever", "where", "whereafter", "whereas", "whereat", "whereby", "wherefrom", "wherein", "whereinto", "whereof", "whereon", "whereto", "whereunto", "whereupon", "wherever", "wherewith", "whether", "which", "whichever", "whichsoever", "while", "whilst", "whither", "who", "whoever", "whole", "whom", "whomever", "whomsoever", "whose", "whosoever", "why", "will", "with", "within", "without", "would", "xsubj", "xcal", "xauthor", "xother ", "xnote", "yet", "you", "your", "yours", "yourself", "yourselves" }), true));
/*     */ 
/* 125 */   public static final PatternAnalyzer DEFAULT_ANALYZER = new PatternAnalyzer(Version.LUCENE_CURRENT, NON_WORD_PATTERN, true, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
/*     */ 
/* 135 */   public static final PatternAnalyzer EXTENDED_ANALYZER = new PatternAnalyzer(Version.LUCENE_CURRENT, NON_WORD_PATTERN, true, EXTENDED_ENGLISH_STOP_WORDS);
/*     */   private final Pattern pattern;
/*     */   private final boolean toLowerCase;
/*     */   private final Set<?> stopWords;
/*     */   private final Version matchVersion;
/*     */ 
/*     */   public PatternAnalyzer(Version matchVersion, Pattern pattern, boolean toLowerCase, Set<?> stopWords)
/*     */   {
/* 164 */     if (pattern == null) {
/* 165 */       throw new IllegalArgumentException("pattern must not be null");
/*     */     }
/* 167 */     if (eqPattern(NON_WORD_PATTERN, pattern)) pattern = NON_WORD_PATTERN;
/* 168 */     else if (eqPattern(WHITESPACE_PATTERN, pattern)) pattern = WHITESPACE_PATTERN;
/*     */ 
/* 170 */     if ((stopWords != null) && (stopWords.size() == 0)) stopWords = null;
/*     */ 
/* 172 */     this.pattern = pattern;
/* 173 */     this.toLowerCase = toLowerCase;
/* 174 */     this.stopWords = stopWords;
/* 175 */     this.matchVersion = matchVersion;
/*     */   }
/*     */ 
/*     */   public TokenStream tokenStream(String fieldName, String text)
/*     */   {
/* 191 */     if (text == null)
/* 192 */       throw new IllegalArgumentException("text must not be null");
/*     */     TokenStream stream;
/*     */     TokenStream stream;
/* 195 */     if (this.pattern == NON_WORD_PATTERN) {
/* 196 */       stream = new FastStringTokenizer(text, true, this.toLowerCase, this.stopWords);
/*     */     }
/*     */     else
/*     */     {
/*     */       TokenStream stream;
/* 198 */       if (this.pattern == WHITESPACE_PATTERN) {
/* 199 */         stream = new FastStringTokenizer(text, false, this.toLowerCase, this.stopWords);
/*     */       }
/*     */       else {
/* 202 */         stream = new PatternTokenizer(text, this.pattern, this.toLowerCase);
/* 203 */         if (this.stopWords != null) stream = new StopFilter(this.matchVersion, stream, this.stopWords);
/*     */       }
/*     */     }
/* 206 */     return stream;
/*     */   }
/*     */ 
/*     */   public TokenStream tokenStream(String fieldName, Reader reader)
/*     */   {
/* 222 */     if ((reader instanceof FastStringReader)) {
/* 223 */       return tokenStream(fieldName, ((FastStringReader)reader).getString());
/*     */     }
/*     */     try
/*     */     {
/* 227 */       String text = toString(reader);
/* 228 */       return tokenStream(fieldName, text);
/*     */     } catch (IOException e) {
/* 230 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 243 */     if (this == other) return true;
/* 244 */     if ((this == DEFAULT_ANALYZER) && (other == EXTENDED_ANALYZER)) return false;
/* 245 */     if ((other == DEFAULT_ANALYZER) && (this == EXTENDED_ANALYZER)) return false;
/*     */ 
/* 247 */     if ((other instanceof PatternAnalyzer)) {
/* 248 */       PatternAnalyzer p2 = (PatternAnalyzer)other;
/* 249 */       return (this.toLowerCase == p2.toLowerCase) && (eqPattern(this.pattern, p2.pattern)) && (eq(this.stopWords, p2.stopWords));
/*     */     }
/*     */ 
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 264 */     if (this == DEFAULT_ANALYZER) return -1218418418;
/* 265 */     if (this == EXTENDED_ANALYZER) return 1303507063;
/*     */ 
/* 267 */     int h = 1;
/* 268 */     h = 31 * h + this.pattern.pattern().hashCode();
/* 269 */     h = 31 * h + this.pattern.flags();
/* 270 */     h = 31 * h + (this.toLowerCase ? 1231 : 1237);
/* 271 */     h = 31 * h + (this.stopWords != null ? this.stopWords.hashCode() : 0);
/* 272 */     return h;
/*     */   }
/*     */ 
/*     */   private static boolean eq(Object o1, Object o2)
/*     */   {
/* 277 */     return (o1 == o2) || ((o1 != null) && (o1.equals(o2)));
/*     */   }
/*     */ 
/*     */   private static boolean eqPattern(Pattern p1, Pattern p2)
/*     */   {
/* 282 */     return (p1 == p2) || ((p1.flags() == p2.flags()) && (p1.pattern().equals(p2.pattern())));
/*     */   }
/*     */ 
/*     */   private static String toString(Reader input)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 293 */       int len = 256;
/* 294 */       char[] buffer = new char[len];
/* 295 */       char[] output = new char[len];
/*     */ 
/* 297 */       len = 0;
/*     */       int n;
/*     */       char[] tmp;
/* 299 */       while ((n = input.read(buffer)) >= 0) {
/* 300 */         if (len + n > output.length) {
/* 301 */           tmp = new char[Math.max(output.length << 1, len + n)];
/* 302 */           System.arraycopy(output, 0, tmp, 0, len);
/* 303 */           System.arraycopy(buffer, 0, tmp, len, n);
/* 304 */           buffer = output;
/* 305 */           output = tmp;
/*     */         } else {
/* 307 */           System.arraycopy(buffer, 0, output, len, n);
/*     */         }
/* 309 */         len += n;
/*     */       }
/*     */ 
/* 312 */       return new String(output, 0, len);
/*     */     } finally {
/* 314 */       input.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class FastStringReader extends StringReader
/*     */   {
/*     */     private final String s;
/*     */ 
/*     */     FastStringReader(String s)
/*     */     {
/* 482 */       super();
/* 483 */       this.s = s;
/*     */     }
/*     */ 
/*     */     String getString() {
/* 487 */       return this.s;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class FastStringTokenizer extends TokenStream
/*     */   {
/*     */     private final String str;
/*     */     private int pos;
/*     */     private final boolean isLetter;
/*     */     private final boolean toLowerCase;
/*     */     private final Set<?> stopWords;
/* 392 */     private static final Locale locale = Locale.getDefault();
/* 393 */     private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 394 */     private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */ 
/*     */     public FastStringTokenizer(String str, boolean isLetter, boolean toLowerCase, Set<?> stopWords) {
/* 397 */       this.str = str;
/* 398 */       this.isLetter = isLetter;
/* 399 */       this.toLowerCase = toLowerCase;
/* 400 */       this.stopWords = stopWords; } 
/* 405 */     public boolean incrementToken() { clearAttributes();
/*     */ 
/* 407 */       String s = this.str;
/* 408 */       int len = s.length();
/* 409 */       int i = this.pos;
/* 410 */       boolean letter = this.isLetter;
/*     */ 
/* 412 */       int start = 0;
/*     */       String text;
/*     */       do { text = null;
/* 417 */         while ((i < len) && (!isTokenChar(s.charAt(i), letter))) {
/* 418 */           i++;
/*     */         }
/*     */ 
/* 421 */         if (i < len) {
/* 422 */           start = i;
/* 423 */           while ((i < len) && (isTokenChar(s.charAt(i), letter))) {
/* 424 */             i++;
/*     */           }
/*     */ 
/* 427 */           text = s.substring(start, i);
/* 428 */           if (this.toLowerCase) text = text.toLowerCase(locale);
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 440 */       while ((text != null) && (isStopWord(text)));
/*     */ 
/* 442 */       this.pos = i;
/* 443 */       if (text == null)
/*     */       {
/* 445 */         return false;
/*     */       }
/* 447 */       this.termAtt.setEmpty().append(text);
/* 448 */       this.offsetAtt.setOffset(start, i);
/* 449 */       return true;
/*     */     }
/*     */ 
/*     */     public final void end()
/*     */     {
/* 455 */       int finalOffset = this.str.length();
/* 456 */       this.offsetAtt.setOffset(finalOffset, finalOffset);
/*     */     }
/*     */ 
/*     */     private boolean isTokenChar(char c, boolean isLetter) {
/* 460 */       return !Character.isWhitespace(c) ? true : isLetter ? Character.isLetter(c) : false;
/*     */     }
/*     */ 
/*     */     private boolean isStopWord(String text) {
/* 464 */       return (this.stopWords != null) && (this.stopWords.contains(text));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class PatternTokenizer extends TokenStream
/*     */   {
/*     */     private final String str;
/*     */     private final boolean toLowerCase;
/*     */     private Matcher matcher;
/* 331 */     private int pos = 0;
/* 332 */     private static final Locale locale = Locale.getDefault();
/* 333 */     private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 334 */     private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */ 
/*     */     public PatternTokenizer(String str, Pattern pattern, boolean toLowerCase) {
/* 337 */       this.str = str;
/* 338 */       this.matcher = pattern.matcher(str);
/* 339 */       this.toLowerCase = toLowerCase;
/*     */     }
/*     */ 
/*     */     public final boolean incrementToken()
/*     */     {
/* 344 */       if (this.matcher == null) return false;
/* 345 */       clearAttributes();
/*     */       while (true) {
/* 347 */         int start = this.pos;
/*     */ 
/* 349 */         boolean isMatch = this.matcher.find();
/*     */         int end;
/* 350 */         if (isMatch) {
/* 351 */           int end = this.matcher.start();
/* 352 */           this.pos = this.matcher.end();
/*     */         } else {
/* 354 */           end = this.str.length();
/* 355 */           this.matcher = null;
/*     */         }
/*     */ 
/* 358 */         if (start != end) {
/* 359 */           String text = this.str.substring(start, end);
/* 360 */           if (this.toLowerCase) text = text.toLowerCase(locale);
/* 361 */           this.termAtt.setEmpty().append(text);
/* 362 */           this.offsetAtt.setOffset(start, end);
/* 363 */           return true;
/*     */         }
/* 365 */         if (!isMatch) return false;
/*     */       }
/*     */     }
/*     */ 
/*     */     public final void end()
/*     */     {
/* 372 */       int finalOffset = this.str.length();
/* 373 */       this.offsetAtt.setOffset(finalOffset, finalOffset);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.miscellaneous.PatternAnalyzer
 * JD-Core Version:    0.6.2
 */