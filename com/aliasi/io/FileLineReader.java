/*     */ package com.aliasi.io;
/*     */ 
/*     */ import com.aliasi.util.Iterators.Buffered;
/*     */ import com.aliasi.util.Streams;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ 
/*     */ public class FileLineReader extends LineNumberReader
/*     */   implements Iterable<String>
/*     */ {
/*     */   public FileLineReader(File file, String encoding)
/*     */     throws IOException
/*     */   {
/* 106 */     this(file, encoding, false);
/*     */   }
/*     */ 
/*     */   public FileLineReader(File file, String encoding, boolean gzipped)
/*     */     throws IOException
/*     */   {
/* 132 */     super(buildReader(file, encoding, gzipped));
/*     */   }
/*     */ 
/*     */   public Iterator<String> iterator()
/*     */   {
/* 147 */     return new Iterators.Buffered()
/*     */     {
/*     */       public String bufferNext() {
/*     */         try {
/* 151 */           return FileLineReader.this.readLine();
/*     */         } catch (IOException e) {
/* 153 */           throw new IllegalStateException("I/O error reading", e);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public List<String> readLines()
/*     */     throws IOException
/*     */   {
/* 170 */     List lineList = new ArrayList();
/*     */     try {
/* 172 */       for (String line : this)
/* 173 */         lineList.add(line);
/*     */     } finally {
/* 175 */       close();
/*     */     }
/* 177 */     return lineList;
/*     */   }
/*     */ 
/*     */   public static List<String> readLines(File in, String encoding)
/*     */     throws IOException, UnsupportedEncodingException
/*     */   {
/* 194 */     FileLineReader reader = new FileLineReader(in, encoding);
/* 195 */     return reader.readLines();
/*     */   }
/*     */ 
/*     */   public static String[] readLineArray(File in, String encoding)
/*     */     throws IOException, UnsupportedEncodingException
/*     */   {
/* 213 */     List lineList = readLines(in, encoding);
/* 214 */     return (String[])lineList.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */   }
/*     */ 
/*     */   static Reader buildReader(File file, String encoding, boolean gzipped)
/*     */     throws IOException
/*     */   {
/* 221 */     InputStream in = null;
/* 222 */     InputStream zipIn = null;
/* 223 */     InputStreamReader reader = null;
/*     */     try {
/* 225 */       in = new FileInputStream(file);
/* 226 */       zipIn = gzipped ? new GZIPInputStream(in) : in;
/* 227 */       return new InputStreamReader(zipIn, encoding);
/*     */     }
/*     */     catch (IOException e) {
/* 230 */       Streams.closeReader(reader);
/* 231 */       Streams.closeInputStream(zipIn);
/* 232 */       Streams.closeInputStream(in);
/* 233 */       throw e;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.io.FileLineReader
 * JD-Core Version:    0.6.2
 */