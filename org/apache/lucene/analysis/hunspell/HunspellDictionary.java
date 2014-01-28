/*     */ package org.apache.lucene.analysis.hunspell;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.lucene.analysis.CharArrayMap;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public class HunspellDictionary
/*     */ {
/*  34 */   static final HunspellWord NOFLAGS = new HunspellWord();
/*     */   private static final String PREFIX_KEY = "PFX";
/*     */   private static final String SUFFIX_KEY = "SFX";
/*     */   private static final String FLAG_KEY = "FLAG";
/*     */   private static final String NUM_FLAG_TYPE = "num";
/*     */   private static final String UTF8_FLAG_TYPE = "UTF-8";
/*     */   private static final String LONG_FLAG_TYPE = "long";
/*     */   private static final String PREFIX_CONDITION_REGEX_PATTERN = "%s.*";
/*     */   private static final String SUFFIX_CONDITION_REGEX_PATTERN = ".*%s";
/*     */   private static final boolean IGNORE_CASE_DEFAULT = false;
/*     */   private CharArrayMap<List<HunspellWord>> words;
/*     */   private CharArrayMap<List<HunspellAffix>> prefixes;
/*     */   private CharArrayMap<List<HunspellAffix>> suffixes;
/*  53 */   private FlagParsingStrategy flagParsingStrategy = new SimpleFlagParsingStrategy(null);
/*  54 */   private boolean ignoreCase = false;
/*     */   private final Version version;
/*     */ 
/*     */   public HunspellDictionary(InputStream affix, InputStream dictionary, Version version)
/*     */     throws IOException, ParseException
/*     */   {
/*  69 */     this(affix, Arrays.asList(new InputStream[] { dictionary }), version, false);
/*     */   }
/*     */ 
/*     */   public HunspellDictionary(InputStream affix, InputStream dictionary, Version version, boolean ignoreCase)
/*     */     throws IOException, ParseException
/*     */   {
/*  84 */     this(affix, Arrays.asList(new InputStream[] { dictionary }), version, ignoreCase);
/*     */   }
/*     */ 
/*     */   public HunspellDictionary(InputStream affix, List<InputStream> dictionaries, Version version, boolean ignoreCase)
/*     */     throws IOException, ParseException
/*     */   {
/*  99 */     this.version = version;
/* 100 */     this.ignoreCase = ignoreCase;
/* 101 */     String encoding = getDictionaryEncoding(affix);
/* 102 */     CharsetDecoder decoder = getJavaEncoding(encoding);
/* 103 */     readAffixFile(affix, decoder);
/* 104 */     this.words = new CharArrayMap(version, 65535, this.ignoreCase);
/* 105 */     for (InputStream dictionary : dictionaries)
/* 106 */       readDictionaryFile(dictionary, decoder);
/*     */   }
/*     */ 
/*     */   public List<HunspellWord> lookupWord(char[] word, int offset, int length)
/*     */   {
/* 119 */     return (List)this.words.get(word, offset, length);
/*     */   }
/*     */ 
/*     */   public List<HunspellAffix> lookupPrefix(char[] word, int offset, int length)
/*     */   {
/* 131 */     return (List)this.prefixes.get(word, offset, length);
/*     */   }
/*     */ 
/*     */   public List<HunspellAffix> lookupSuffix(char[] word, int offset, int length)
/*     */   {
/* 143 */     return (List)this.suffixes.get(word, offset, length);
/*     */   }
/*     */ 
/*     */   private void readAffixFile(InputStream affixStream, CharsetDecoder decoder)
/*     */     throws IOException
/*     */   {
/* 154 */     this.prefixes = new CharArrayMap(this.version, 8, this.ignoreCase);
/* 155 */     this.suffixes = new CharArrayMap(this.version, 8, this.ignoreCase);
/*     */ 
/* 157 */     BufferedReader reader = new BufferedReader(new InputStreamReader(affixStream, decoder));
/* 158 */     String line = null;
/* 159 */     while ((line = reader.readLine()) != null) {
/* 160 */       if (line.startsWith("PFX"))
/* 161 */         parseAffix(this.prefixes, line, reader, "%s.*");
/* 162 */       else if (line.startsWith("SFX"))
/* 163 */         parseAffix(this.suffixes, line, reader, ".*%s");
/* 164 */       else if (line.startsWith("FLAG"))
/*     */       {
/* 167 */         this.flagParsingStrategy = getFlagParsingStrategy(line);
/*     */       }
/*     */     }
/* 170 */     reader.close();
/*     */   }
/*     */ 
/*     */   private void parseAffix(CharArrayMap<List<HunspellAffix>> affixes, String header, BufferedReader reader, String conditionPattern)
/*     */     throws IOException
/*     */   {
/* 187 */     String[] args = header.split("\\s+");
/*     */ 
/* 189 */     boolean crossProduct = args[2].equals("Y");
/*     */ 
/* 191 */     int numLines = Integer.parseInt(args[3]);
/* 192 */     for (int i = 0; i < numLines; i++) {
/* 193 */       String line = reader.readLine();
/* 194 */       String[] ruleArgs = line.split("\\s+");
/*     */ 
/* 196 */       HunspellAffix affix = new HunspellAffix();
/*     */ 
/* 198 */       affix.setFlag(this.flagParsingStrategy.parseFlag(ruleArgs[1]));
/* 199 */       affix.setStrip(ruleArgs[2].equals("0") ? "" : ruleArgs[2]);
/*     */ 
/* 201 */       String affixArg = ruleArgs[3];
/*     */ 
/* 203 */       int flagSep = affixArg.lastIndexOf('/');
/* 204 */       if (flagSep != -1) {
/* 205 */         char[] appendFlags = this.flagParsingStrategy.parseFlags(affixArg.substring(flagSep + 1));
/* 206 */         Arrays.sort(appendFlags);
/* 207 */         affix.setAppendFlags(appendFlags);
/* 208 */         affix.setAppend(affixArg.substring(0, flagSep));
/*     */       } else {
/* 210 */         affix.setAppend(affixArg);
/*     */       }
/*     */ 
/* 213 */       String condition = ruleArgs[4];
/* 214 */       affix.setCondition(condition, String.format(conditionPattern, new Object[] { condition }));
/* 215 */       affix.setCrossProduct(crossProduct);
/*     */ 
/* 217 */       List list = (List)affixes.get(affix.getAppend());
/* 218 */       if (list == null) {
/* 219 */         list = new ArrayList();
/* 220 */         affixes.put(affix.getAppend(), list);
/*     */       }
/*     */ 
/* 223 */       list.add(affix);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getDictionaryEncoding(InputStream affix)
/*     */     throws IOException, ParseException
/*     */   {
/* 236 */     StringBuilder encoding = new StringBuilder();
/*     */     int ch;
/*     */     do
/*     */     {
/* 238 */       encoding.setLength(0);
/*     */ 
/* 240 */       while (((ch = affix.read()) >= 0) && 
/* 241 */         (ch != 10))
/*     */       {
/* 244 */         if (ch != 13) {
/* 245 */           encoding.append((char)ch);
/*     */         }
/*     */       }
/* 248 */       if ((encoding.length() != 0) && (encoding.charAt(0) != '#') && (encoding.toString().trim().length() != 0))
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 253 */     while (ch >= 0);
/* 254 */     throw new ParseException("Unexpected end of affix file.", 0);
/*     */ 
/* 258 */     if ("SET ".equals(encoding.substring(0, 4)))
/*     */     {
/* 260 */       return encoding.substring(4).trim();
/*     */     }
/* 262 */     throw new ParseException("The first non-comment line in the affix file must be a 'SET charset', was: '" + encoding + "'", 0);
/*     */   }
/*     */ 
/*     */   private CharsetDecoder getJavaEncoding(String encoding)
/*     */   {
/* 275 */     Charset charset = Charset.forName(encoding);
/* 276 */     return charset.newDecoder();
/*     */   }
/*     */ 
/*     */   private FlagParsingStrategy getFlagParsingStrategy(String flagLine)
/*     */   {
/* 286 */     String flagType = flagLine.substring(5);
/*     */ 
/* 288 */     if ("num".equals(flagType))
/* 289 */       return new NumFlagParsingStrategy(null);
/* 290 */     if ("UTF-8".equals(flagType))
/* 291 */       return new SimpleFlagParsingStrategy(null);
/* 292 */     if ("long".equals(flagType)) {
/* 293 */       return new DoubleASCIIFlagParsingStrategy(null);
/*     */     }
/*     */ 
/* 296 */     throw new IllegalArgumentException("Unknown flag type: " + flagType);
/*     */   }
/*     */ 
/*     */   private void readDictionaryFile(InputStream dictionary, CharsetDecoder decoder)
/*     */     throws IOException
/*     */   {
/* 307 */     BufferedReader reader = new BufferedReader(new InputStreamReader(dictionary, decoder));
/*     */ 
/* 309 */     String line = reader.readLine();
/* 310 */     int numEntries = Integer.parseInt(line);
/*     */ 
/* 314 */     while ((line = reader.readLine()) != null)
/*     */     {
/* 318 */       int flagSep = line.lastIndexOf('/');
/*     */       String entry;
/*     */       HunspellWord wordForm;
/*     */       String entry;
/* 319 */       if (flagSep == -1) {
/* 320 */         HunspellWord wordForm = NOFLAGS;
/* 321 */         entry = line;
/*     */       }
/*     */       else
/*     */       {
/* 325 */         int end = line.indexOf('\t', flagSep);
/* 326 */         if (end == -1) {
/* 327 */           end = line.length();
/*     */         }
/*     */ 
/* 330 */         wordForm = new HunspellWord(this.flagParsingStrategy.parseFlags(line.substring(flagSep + 1, end)));
/* 331 */         Arrays.sort(wordForm.getFlags());
/* 332 */         entry = line.substring(0, flagSep);
/* 333 */         if (this.ignoreCase) {
/* 334 */           entry = entry.toLowerCase(Locale.ENGLISH);
/*     */         }
/*     */       }
/*     */ 
/* 338 */       List entries = (List)this.words.get(entry);
/* 339 */       if (entries == null) {
/* 340 */         entries = new ArrayList();
/* 341 */         this.words.put(entry, entries);
/*     */       }
/* 343 */       entries.add(wordForm);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Version getVersion() {
/* 348 */     return this.version;
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreCase()
/*     */   {
/* 438 */     return this.ignoreCase;
/*     */   }
/*     */ 
/*     */   private static class DoubleASCIIFlagParsingStrategy extends HunspellDictionary.FlagParsingStrategy
/*     */   {
/*     */     private DoubleASCIIFlagParsingStrategy()
/*     */     {
/* 415 */       super();
/*     */     }
/*     */ 
/*     */     public char[] parseFlags(String rawFlags)
/*     */     {
/* 421 */       if (rawFlags.length() == 0) {
/* 422 */         return new char[0];
/*     */       }
/*     */ 
/* 425 */       StringBuilder builder = new StringBuilder();
/* 426 */       for (int i = 0; i < rawFlags.length(); i += 2) {
/* 427 */         char cookedFlag = (char)(rawFlags.charAt(i) + rawFlags.charAt(i + 1));
/* 428 */         builder.append(cookedFlag);
/*     */       }
/*     */ 
/* 431 */       char[] flags = new char[builder.length()];
/* 432 */       builder.getChars(0, builder.length(), flags, 0);
/* 433 */       return flags;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class NumFlagParsingStrategy extends HunspellDictionary.FlagParsingStrategy
/*     */   {
/*     */     private NumFlagParsingStrategy()
/*     */     {
/* 392 */       super();
/*     */     }
/*     */ 
/*     */     public char[] parseFlags(String rawFlags)
/*     */     {
/* 397 */       String[] rawFlagParts = rawFlags.trim().split(",");
/* 398 */       char[] flags = new char[rawFlagParts.length];
/*     */ 
/* 400 */       for (int i = 0; i < rawFlagParts.length; i++)
/*     */       {
/* 402 */         flags[i] = ((char)Integer.parseInt(rawFlagParts[i].replaceAll("[^0-9]", "")));
/*     */       }
/*     */ 
/* 405 */       return flags;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SimpleFlagParsingStrategy extends HunspellDictionary.FlagParsingStrategy
/*     */   {
/*     */     private SimpleFlagParsingStrategy()
/*     */     {
/* 379 */       super();
/*     */     }
/*     */ 
/*     */     public char[] parseFlags(String rawFlags)
/*     */     {
/* 384 */       return rawFlags.toCharArray();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class FlagParsingStrategy
/*     */   {
/*     */     char parseFlag(String rawFlag)
/*     */     {
/* 363 */       return parseFlags(rawFlag)[0];
/*     */     }
/*     */ 
/*     */     abstract char[] parseFlags(String paramString);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hunspell.HunspellDictionary
 * JD-Core Version:    0.6.2
 */