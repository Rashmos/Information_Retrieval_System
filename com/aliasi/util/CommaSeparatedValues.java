/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FilterReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class CommaSeparatedValues
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 8098086161027647465L;
/*     */   final String[][] mArray;
/*     */   static final int COMMA = 0;
/*     */   static final int NEWLINE = 1;
/*     */   static final int EOF = 2;
/*     */ 
/*     */   public CommaSeparatedValues(File file, String charset)
/*     */     throws IOException
/*     */   {
/* 103 */     FileInputStream in = null;
/* 104 */     InputStreamReader reader = null;
/* 105 */     BufferedReader bufReader = null;
/*     */     try {
/* 107 */       in = new FileInputStream(file);
/* 108 */       reader = new InputStreamReader(in, charset);
/* 109 */       bufReader = new BufferedReader(reader);
/* 110 */       this.mArray = read(bufReader);
/*     */     } finally {
/* 112 */       Streams.closeReader(bufReader);
/* 113 */       Streams.closeReader(reader);
/* 114 */       Streams.closeInputStream(in);
/*     */     }
/*     */   }
/*     */ 
/*     */   public CommaSeparatedValues(InputStream in, String charset)
/*     */     throws IOException
/*     */   {
/* 134 */     InputStreamReader reader = null;
/* 135 */     BufferedReader bufReader = null;
/*     */     try {
/* 137 */       reader = new InputStreamReader(in, charset);
/* 138 */       bufReader = new BufferedReader(reader);
/* 139 */       this.mArray = read(bufReader);
/*     */     } finally {
/* 141 */       Streams.closeReader(bufReader);
/* 142 */       Streams.closeReader(reader);
/* 143 */       Streams.closeInputStream(in);
/*     */     }
/*     */   }
/*     */ 
/*     */   public CommaSeparatedValues(Reader reader)
/*     */     throws IOException
/*     */   {
/* 159 */     this.mArray = read(reader);
/*     */   }
/*     */ 
/*     */   public String[][] getArray()
/*     */   {
/* 171 */     return this.mArray;
/*     */   }
/*     */ 
/*     */   public void toFile(File file, String charset)
/*     */     throws IOException
/*     */   {
/* 185 */     FileOutputStream out = null;
/*     */     try {
/* 187 */       out = new FileOutputStream(file);
/* 188 */       toStream(out, charset);
/*     */     } finally {
/* 190 */       Streams.closeOutputStream(out);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void toStream(OutputStream out, String charset)
/*     */     throws IOException
/*     */   {
/* 207 */     OutputStreamWriter writer = null;
/* 208 */     BufferedWriter bufWriter = null;
/*     */     try {
/* 210 */       writer = new OutputStreamWriter(out, charset);
/* 211 */       bufWriter = new BufferedWriter(writer);
/* 212 */       toWriter(writer);
/*     */     } finally {
/* 214 */       Streams.closeWriter(bufWriter);
/* 215 */       Streams.closeWriter(writer);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void toWriter(Writer writer)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 228 */       for (int i = 0; i < this.mArray.length; i++) {
/* 229 */         if (i > 0) writer.write(10);
/* 230 */         String[] row = this.mArray[i];
/* 231 */         for (int j = 0; j < row.length; j++) {
/* 232 */           if (j > 0) writer.write(44);
/* 233 */           escape(writer, row[j]);
/*     */         }
/*     */       }
/*     */     } finally {
/* 237 */       Streams.closeWriter(writer);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 250 */     CharArrayWriter writer = new CharArrayWriter();
/*     */     try {
/* 252 */       toWriter(writer);
/*     */     }
/*     */     catch (IOException e) {
/*     */     }
/* 256 */     return writer.toString();
/*     */   }
/*     */ 
/*     */   static String[][] read(Reader reader)
/*     */     throws IOException
/*     */   {
/* 265 */     ReportingReader reportingReader = new ReportingReader(reader);
/*     */     try {
/* 267 */       List rowList = new ArrayList();
/* 268 */       read(reportingReader, rowList);
/* 269 */       String[][] rows = new String[rowList.size()][];
/* 270 */       rowList.toArray(rows);
/* 271 */       return rows;
/*     */     } finally {
/* 273 */       Streams.closeReader(reader);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void read(ReportingReader reader, List<String[]> rowList)
/*     */     throws IOException
/*     */   {
/* 280 */     List eltList = new ArrayList();
/* 281 */     int firstChar = firstChar(reader);
/* 282 */     if (firstChar == -1) return; while (true)
/*     */     {
/* 284 */       StringBuilder sb = new StringBuilder();
/* 285 */       switch (readElement(firstChar, sb, reader)) {
/*     */       case 0:
/* 287 */         eltList.add(trim(sb));
/* 288 */         break;
/*     */       case 1:
/* 290 */         eltList.add(trim(sb));
/* 291 */         String[] elts = (String[])eltList.toArray(Strings.EMPTY_STRING_ARRAY);
/* 292 */         rowList.add(elts);
/* 293 */         eltList = new ArrayList();
/* 294 */         break;
/*     */       case 2:
/* 296 */         eltList.add(trim(sb));
/* 297 */         String[] elts2 = (String[])eltList.toArray(Strings.EMPTY_STRING_ARRAY);
/* 298 */         rowList.add(elts2);
/* 299 */         eltList = new ArrayList();
/* 300 */         return;
/*     */       }
/*     */ 
/* 303 */       firstChar = firstChar(reader);
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isSpace(int c)
/*     */   {
/* 310 */     return (c == 32) || (c == 9);
/*     */   }
/*     */ 
/*     */   static String trim(StringBuilder sb)
/*     */   {
/* 315 */     int end = sb.length() - 1;
/* 316 */     while ((end >= 0) && (isSpace(sb.charAt(end))))
/* 317 */       end--;
/* 318 */     return sb.substring(0, end + 1);
/*     */   }
/*     */ 
/*     */   static int firstChar(ReportingReader reader) throws IOException {
/*     */     while (true) {
/* 323 */       int c = reader.read();
/* 324 */       if (c == -1) return -1;
/* 325 */       if (!isSpace(c)) return c; 
/*     */     }
/*     */   }
/*     */ 
/*     */   static int readElement(int firstChar, StringBuilder sb, ReportingReader reader)
/*     */     throws IOException
/*     */   {
/* 331 */     if (firstChar == 34)
/* 332 */       return readQuotedElement(sb, reader);
/* 333 */     if (firstChar == 10)
/* 334 */       return 1;
/* 335 */     if (firstChar == 44)
/* 336 */       return 0;
/* 337 */     if (firstChar == -1)
/* 338 */       return 2;
/* 339 */     sb.append((char)firstChar);
/* 340 */     return readElement(sb, reader);
/*     */   }
/*     */ 
/*     */   static int readQuotedElement(StringBuilder sb, ReportingReader reader)
/*     */     throws IOException
/*     */   {
/*     */     int c;
/* 347 */     while ((c = reader.read()) != -1) {
/* 348 */       if (c == 34) {
/* 349 */         c = reader.read();
/* 350 */         if (c == 34) {
/* 351 */           sb.append('"');
/*     */         }
/*     */         else {
/* 354 */           while (isSpace(c))
/* 355 */             c = reader.read();
/* 356 */           if (c == -1)
/* 357 */             return 2;
/* 358 */           if (c == 10)
/* 359 */             return 1;
/* 360 */           if (c == 44)
/* 361 */             return 0;
/* 362 */           throw reader.illegalArg("Unexpected chars after close quote.");
/*     */         }
/*     */       } else { sb.append((char)c); }
/*     */ 
/*     */     }
/* 367 */     throw reader.illegalArg("EOF in quoted element.");
/*     */   }
/*     */ 
/*     */   static int readElement(StringBuilder sb, ReportingReader reader)
/*     */     throws IOException
/*     */   {
/*     */     int c;
/* 374 */     while ((c = reader.read()) != -1) {
/* 375 */       if (c == 34)
/* 376 */         throw reader.illegalArg("Unexpected quote symbol.");
/* 377 */       if (c == 44)
/* 378 */         return 0;
/* 379 */       if (c == 10)
/* 380 */         return 1;
/* 381 */       sb.append((char)c);
/*     */     }
/* 383 */     return 2;
/*     */   }
/*     */ 
/*     */   static void escape(Writer writer, String elt)
/*     */     throws IOException
/*     */   {
/* 389 */     for (int i = 0; i < elt.length(); i++) {
/* 390 */       char c = elt.charAt(i);
/* 391 */       if ((c == '\n') || (c == ',') || (c == '"')) {
/* 392 */         quote(writer, elt);
/* 393 */         return;
/*     */       }
/*     */     }
/* 396 */     writer.write(elt);
/*     */   }
/*     */ 
/*     */   static void quote(Writer writer, String elt)
/*     */     throws IOException
/*     */   {
/* 402 */     writer.write(34);
/* 403 */     for (int i = 0; i < elt.length(); i++) {
/* 404 */       char c = elt.charAt(i);
/* 405 */       if (c == '"')
/* 406 */         writer.write(34);
/* 407 */       writer.write(c);
/*     */     }
/* 409 */     writer.write(34);
/*     */   }
/*     */ 
/*     */   static class ReportingReader extends FilterReader {
/* 413 */     int mLineNumber = 0;
/* 414 */     int mColumnNumber = 0;
/*     */ 
/* 416 */     ReportingReader(Reader in) { super(); }
/*     */ 
/*     */     public int read() throws IOException
/*     */     {
/* 420 */       int c = super.read();
/* 421 */       if (c == 10) {
/* 422 */         this.mLineNumber += 1;
/* 423 */         this.mColumnNumber = 0;
/*     */       } else {
/* 425 */         this.mColumnNumber += 1;
/*     */       }
/* 427 */       return c;
/*     */     }
/*     */     IllegalArgumentException illegalArg(String msg) {
/* 430 */       String report = "Line=" + this.mLineNumber + " Column=" + this.mColumnNumber + "\n" + msg;
/*     */ 
/* 435 */       return new IllegalArgumentException(report);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.CommaSeparatedValues
 * JD-Core Version:    0.6.2
 */