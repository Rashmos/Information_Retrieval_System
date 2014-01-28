/*     */ package org.apache.lucene.analysis;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ public class WordlistLoader
/*     */ {
/*     */   public static HashSet<String> getWordSet(File wordfile)
/*     */     throws IOException
/*     */   {
/*  43 */     HashSet result = new HashSet();
/*  44 */     FileReader reader = null;
/*     */     try {
/*  46 */       reader = new FileReader(wordfile);
/*  47 */       result = getWordSet(reader);
/*     */     }
/*     */     finally {
/*  50 */       if (reader != null)
/*  51 */         reader.close();
/*     */     }
/*  53 */     return result;
/*     */   }
/*     */ 
/*     */   public static HashSet<String> getWordSet(File wordfile, String comment)
/*     */     throws IOException
/*     */   {
/*  67 */     HashSet result = new HashSet();
/*  68 */     FileReader reader = null;
/*     */     try {
/*  70 */       reader = new FileReader(wordfile);
/*  71 */       result = getWordSet(reader, comment);
/*     */     }
/*     */     finally {
/*  74 */       if (reader != null)
/*  75 */         reader.close();
/*     */     }
/*  77 */     return result;
/*     */   }
/*     */ 
/*     */   public static HashSet<String> getWordSet(Reader reader)
/*     */     throws IOException
/*     */   {
/*  91 */     HashSet result = new HashSet();
/*  92 */     BufferedReader br = null;
/*     */     try {
/*  94 */       if ((reader instanceof BufferedReader))
/*  95 */         br = (BufferedReader)reader;
/*     */       else {
/*  97 */         br = new BufferedReader(reader);
/*     */       }
/*  99 */       String word = null;
/* 100 */       while ((word = br.readLine()) != null)
/* 101 */         result.add(word.trim());
/*     */     }
/*     */     finally
/*     */     {
/* 105 */       if (br != null)
/* 106 */         br.close();
/*     */     }
/* 108 */     return result;
/*     */   }
/*     */ 
/*     */   public static HashSet<String> getWordSet(Reader reader, String comment)
/*     */     throws IOException
/*     */   {
/* 122 */     HashSet result = new HashSet();
/* 123 */     BufferedReader br = null;
/*     */     try {
/* 125 */       if ((reader instanceof BufferedReader))
/* 126 */         br = (BufferedReader)reader;
/*     */       else {
/* 128 */         br = new BufferedReader(reader);
/*     */       }
/* 130 */       String word = null;
/* 131 */       while ((word = br.readLine()) != null) {
/* 132 */         if (!word.startsWith(comment))
/* 133 */           result.add(word.trim());
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 138 */       if (br != null)
/* 139 */         br.close();
/*     */     }
/* 141 */     return result;
/*     */   }
/*     */ 
/*     */   public static HashMap<String, String> getStemDict(File wordstemfile)
/*     */     throws IOException
/*     */   {
/* 155 */     if (wordstemfile == null)
/* 156 */       throw new NullPointerException("wordstemfile may not be null");
/* 157 */     HashMap result = new HashMap();
/* 158 */     BufferedReader br = null;
/* 159 */     FileReader fr = null;
/*     */     try {
/* 161 */       fr = new FileReader(wordstemfile);
/* 162 */       br = new BufferedReader(fr);
/*     */       String line;
/* 164 */       while ((line = br.readLine()) != null) {
/* 165 */         String[] wordstem = line.split("\t", 2);
/* 166 */         result.put(wordstem[0], wordstem[1]);
/*     */       }
/*     */     } finally {
/* 169 */       if (fr != null)
/* 170 */         fr.close();
/* 171 */       if (br != null)
/* 172 */         br.close();
/*     */     }
/* 174 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.WordlistLoader
 * JD-Core Version:    0.6.2
 */