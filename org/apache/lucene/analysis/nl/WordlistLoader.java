/*     */ package org.apache.lucene.analysis.nl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class WordlistLoader
/*     */ {
/*     */   /** @deprecated */
/*     */   public static HashMap getWordtable(String path, String wordfile)
/*     */   {
/*  41 */     if ((path == null) || (wordfile == null)) {
/*  42 */       return new HashMap();
/*     */     }
/*  44 */     return getWordtable(new File(path, wordfile));
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static HashMap getWordtable(String wordfile)
/*     */   {
/*  52 */     if (wordfile == null) {
/*  53 */       return new HashMap();
/*     */     }
/*  55 */     return getWordtable(new File(wordfile));
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static HashMap getStemDict(File wordstemfile)
/*     */   {
/*  67 */     if (wordstemfile == null) {
/*  68 */       return new HashMap();
/*     */     }
/*  70 */     HashMap result = new HashMap();
/*     */     try {
/*  72 */       LineNumberReader lnr = new LineNumberReader(new FileReader(wordstemfile));
/*     */       String line;
/*  75 */       while ((line = lnr.readLine()) != null) {
/*  76 */         String[] wordstem = line.split("\t", 2);
/*  77 */         result.put(wordstem[0], wordstem[1]);
/*     */       }
/*     */     } catch (IOException e) {
/*     */     }
/*  81 */     return result;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static HashMap getWordtable(File wordfile)
/*     */   {
/*  89 */     if (wordfile == null) {
/*  90 */       return new HashMap();
/*     */     }
/*  92 */     HashMap result = null;
/*     */     try {
/*  94 */       LineNumberReader lnr = new LineNumberReader(new FileReader(wordfile));
/*  95 */       String word = null;
/*  96 */       String[] stopwords = new String[100];
/*  97 */       int wordcount = 0;
/*  98 */       while ((word = lnr.readLine()) != null) {
/*  99 */         wordcount++;
/* 100 */         if (wordcount == stopwords.length) {
/* 101 */           String[] tmp = new String[stopwords.length + 50];
/* 102 */           System.arraycopy(stopwords, 0, tmp, 0, wordcount);
/* 103 */           stopwords = tmp;
/*     */         }
/* 105 */         stopwords[(wordcount - 1)] = word;
/*     */       }
/* 107 */       result = makeWordTable(stopwords, wordcount);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 111 */       result = new HashMap();
/*     */     }
/* 113 */     return result;
/*     */   }
/*     */ 
/*     */   private static HashMap makeWordTable(String[] words, int length)
/*     */   {
/* 123 */     HashMap table = new HashMap(length);
/* 124 */     for (int i = 0; i < length; i++) {
/* 125 */       table.put(words[i], words[i]);
/*     */     }
/* 127 */     return table;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.nl.WordlistLoader
 * JD-Core Version:    0.6.2
 */