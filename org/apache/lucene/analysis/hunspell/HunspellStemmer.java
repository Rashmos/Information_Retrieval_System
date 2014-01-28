/*     */ package org.apache.lucene.analysis.hunspell;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.text.ParseException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.util.CharacterUtils;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public class HunspellStemmer
/*     */ {
/*     */   private static final int RECURSION_CAP = 2;
/*     */   private final HunspellDictionary dictionary;
/*  43 */   private final StringBuilder segment = new StringBuilder();
/*  44 */   private CharacterUtils charUtils = CharacterUtils.getInstance(Version.LUCENE_34);
/*     */ 
/*     */   public HunspellStemmer(HunspellDictionary dictionary)
/*     */   {
/*  52 */     this.dictionary = dictionary;
/*     */   }
/*     */ 
/*     */   public List<Stem> stem(String word)
/*     */   {
/*  62 */     return stem(word.toCharArray(), word.length());
/*     */   }
/*     */ 
/*     */   public List<Stem> stem(char[] word, int length)
/*     */   {
/*  72 */     List stems = new ArrayList();
/*  73 */     if (this.dictionary.lookupWord(word, 0, length) != null) {
/*  74 */       stems.add(new Stem(word, length));
/*     */     }
/*  76 */     stems.addAll(stem(word, length, null, 0));
/*  77 */     return stems;
/*     */   }
/*     */ 
/*     */   public List<Stem> uniqueStems(char[] word, int length)
/*     */   {
/*  87 */     List stems = new ArrayList();
/*  88 */     CharArraySet terms = new CharArraySet(this.dictionary.getVersion(), 8, this.dictionary.isIgnoreCase());
/*  89 */     if (this.dictionary.lookupWord(word, 0, length) != null) {
/*  90 */       stems.add(new Stem(word, length));
/*  91 */       terms.add(word);
/*     */     }
/*  93 */     List otherStems = stem(word, length, null, 0);
/*  94 */     for (Stem s : otherStems) {
/*  95 */       if (!terms.contains(s.stem)) {
/*  96 */         stems.add(s);
/*  97 */         terms.add(s.stem);
/*     */       }
/*     */     }
/* 100 */     return stems;
/*     */   }
/*     */ 
/*     */   private List<Stem> stem(char[] word, int length, char[] flags, int recursionDepth)
/*     */   {
/* 114 */     List stems = new ArrayList();
/*     */ 
/* 116 */     for (int i = 0; i < length; i++) {
/* 117 */       List suffixes = this.dictionary.lookupSuffix(word, i, length - i);
/* 118 */       if (suffixes != null)
/*     */       {
/* 122 */         for (HunspellAffix suffix : suffixes) {
/* 123 */           if (hasCrossCheckedFlag(suffix.getFlag(), flags)) {
/* 124 */             int deAffixedLength = length - suffix.getAppend().length();
/*     */ 
/* 126 */             String strippedWord = suffix.getStrip();
/*     */ 
/* 128 */             List stemList = applyAffix(strippedWord.toCharArray(), strippedWord.length(), suffix, recursionDepth);
/* 129 */             for (Stem stem : stemList) {
/* 130 */               stem.addSuffix(suffix);
/*     */             }
/*     */ 
/* 133 */             stems.addAll(stemList);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 138 */     for (int i = length - 1; i >= 0; i--) {
/* 139 */       List prefixes = this.dictionary.lookupPrefix(word, 0, i);
/* 140 */       if (prefixes != null)
/*     */       {
/* 144 */         for (HunspellAffix prefix : prefixes) {
/* 145 */           if (hasCrossCheckedFlag(prefix.getFlag(), flags)) {
/* 146 */             int deAffixedStart = prefix.getAppend().length();
/* 147 */             int deAffixedLength = length - deAffixedStart;
/*     */ 
/* 149 */             String strippedWord = new StringBuilder().append(prefix.getStrip()).append(word, deAffixedStart, deAffixedLength).toString();
/*     */ 
/* 153 */             List stemList = applyAffix(strippedWord.toCharArray(), strippedWord.length(), prefix, recursionDepth);
/* 154 */             for (Stem stem : stemList) {
/* 155 */               stem.addPrefix(prefix);
/*     */             }
/*     */ 
/* 158 */             stems.addAll(stemList);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 163 */     return stems;
/*     */   }
/*     */ 
/*     */   public List<Stem> applyAffix(char[] strippedWord, int length, HunspellAffix affix, int recursionDepth)
/*     */   {
/*     */     int i;
/* 176 */     if (this.dictionary.isIgnoreCase()) {
/* 177 */       for (i = 0; i < strippedWord.length; ) {
/* 178 */         i += Character.toChars(Character.toLowerCase(this.charUtils.codePointAt(strippedWord, i)), strippedWord, i);
/*     */       }
/*     */     }
/*     */ 
/* 182 */     this.segment.setLength(0);
/* 183 */     this.segment.append(strippedWord, 0, length);
/* 184 */     if (!affix.checkCondition(this.segment)) {
/* 185 */       return Collections.EMPTY_LIST;
/*     */     }
/*     */ 
/* 188 */     List stems = new ArrayList();
/*     */ 
/* 190 */     List words = this.dictionary.lookupWord(strippedWord, 0, length);
/* 191 */     if (words != null) {
/* 192 */       for (HunspellWord hunspellWord : words) {
/* 193 */         if (hunspellWord.hasFlag(affix.getFlag())) {
/* 194 */           stems.add(new Stem(strippedWord, length));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 199 */     if ((affix.isCrossProduct()) && (recursionDepth < 2)) {
/* 200 */       stems.addAll(stem(strippedWord, length, affix.getAppendFlags(), ++recursionDepth));
/*     */     }
/*     */ 
/* 203 */     return stems;
/*     */   }
/*     */ 
/*     */   private boolean hasCrossCheckedFlag(char flag, char[] flags)
/*     */   {
/* 214 */     return (flags == null) || (Arrays.binarySearch(flags, flag) >= 0);
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws IOException, ParseException
/*     */   {
/* 309 */     boolean ignoreCase = false;
/* 310 */     int offset = 0;
/*     */ 
/* 312 */     if (args.length < 2) {
/* 313 */       System.out.println("usage: HunspellStemmer [-i] <affix location> <dic location>");
/* 314 */       System.exit(1);
/*     */     }
/*     */ 
/* 317 */     if (args[offset].equals("-i")) {
/* 318 */       ignoreCase = true;
/* 319 */       System.out.println("Ignoring case. All stems will be returned lowercased");
/* 320 */       offset++;
/*     */     }
/*     */ 
/* 323 */     InputStream affixInputStream = new FileInputStream(args[(offset++)]);
/* 324 */     InputStream dicInputStream = new FileInputStream(args[(offset++)]);
/*     */ 
/* 326 */     HunspellDictionary dictionary = new HunspellDictionary(affixInputStream, dicInputStream, Version.LUCENE_34, ignoreCase);
/*     */ 
/* 328 */     affixInputStream.close();
/* 329 */     dicInputStream.close();
/*     */ 
/* 331 */     HunspellStemmer stemmer = new HunspellStemmer(dictionary);
/*     */ 
/* 333 */     Scanner scanner = new Scanner(System.in);
/*     */ 
/* 335 */     System.out.print("> ");
/* 336 */     while (scanner.hasNextLine()) {
/* 337 */       String word = scanner.nextLine();
/*     */ 
/* 339 */       if ("exit".equals(word))
/*     */       {
/*     */         break;
/*     */       }
/* 343 */       printStemResults(word, stemmer.stem(word.toCharArray(), word.length()));
/*     */ 
/* 345 */       System.out.print("> ");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void printStemResults(String originalWord, List<Stem> stems)
/*     */   {
/* 356 */     StringBuilder builder = new StringBuilder().append("stem(").append(originalWord).append(")").append("\n");
/*     */ 
/* 358 */     for (Stem stem : stems) {
/* 359 */       builder.append("- ").append(stem.getStem()).append(": ");
/*     */ 
/* 361 */       for (HunspellAffix prefix : stem.getPrefixes()) {
/* 362 */         builder.append(prefix.getAppend()).append("+");
/*     */ 
/* 364 */         if (hasText(prefix.getStrip())) {
/* 365 */           builder.append(prefix.getStrip()).append("-");
/*     */         }
/*     */       }
/*     */ 
/* 369 */       builder.append(stem.getStem());
/*     */ 
/* 371 */       for (HunspellAffix suffix : stem.getSuffixes()) {
/* 372 */         if (hasText(suffix.getStrip())) {
/* 373 */           builder.append("-").append(suffix.getStrip());
/*     */         }
/*     */ 
/* 376 */         builder.append("+").append(suffix.getAppend());
/*     */       }
/* 378 */       builder.append("\n");
/*     */     }
/*     */ 
/* 381 */     System.out.println(builder);
/*     */   }
/*     */ 
/*     */   private static boolean hasText(String str)
/*     */   {
/* 391 */     return (str != null) && (str.length() > 0);
/*     */   }
/*     */ 
/*     */   public static class Stem
/*     */   {
/* 223 */     private final List<HunspellAffix> prefixes = new ArrayList();
/* 224 */     private final List<HunspellAffix> suffixes = new ArrayList();
/*     */     private final char[] stem;
/*     */     private final int stemLength;
/*     */ 
/*     */     public Stem(char[] stem, int stemLength)
/*     */     {
/* 234 */       this.stem = stem;
/* 235 */       this.stemLength = stemLength;
/*     */     }
/*     */ 
/*     */     public void addPrefix(HunspellAffix prefix)
/*     */     {
/* 245 */       this.prefixes.add(0, prefix);
/*     */     }
/*     */ 
/*     */     public void addSuffix(HunspellAffix suffix)
/*     */     {
/* 255 */       this.suffixes.add(suffix);
/*     */     }
/*     */ 
/*     */     public List<HunspellAffix> getPrefixes()
/*     */     {
/* 264 */       return this.prefixes;
/*     */     }
/*     */ 
/*     */     public List<HunspellAffix> getSuffixes()
/*     */     {
/* 273 */       return this.suffixes;
/*     */     }
/*     */ 
/*     */     public char[] getStem()
/*     */     {
/* 282 */       return this.stem;
/*     */     }
/*     */ 
/*     */     public int getStemLength()
/*     */     {
/* 289 */       return this.stemLength;
/*     */     }
/*     */ 
/*     */     public String getStemString() {
/* 293 */       return new String(this.stem, 0, this.stemLength);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hunspell.HunspellStemmer
 * JD-Core Version:    0.6.2
 */