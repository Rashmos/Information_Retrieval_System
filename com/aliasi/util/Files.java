/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ 
/*     */ public class Files
/*     */ {
/* 282 */   private static String FILE_URL_PREFIX = "file:///";
/*     */   private static final String TEMP_DIRECTORY_SYS_PROPERTY = "java.io.tmpdir";
/* 295 */   public static final FileFilter NON_CVS_DIRECTORY_FILE_FILTER = new FileFilter()
/*     */   {
/*     */     public boolean accept(File file) {
/* 298 */       return (file.isDirectory()) && (!file.getName().equalsIgnoreCase("CVS"));
/*     */     }
/* 295 */   };
/*     */ 
/* 307 */   public static final FileFilter FILES_ONLY_FILE_FILTER = new FileFilter()
/*     */   {
/*     */     public boolean accept(File file) {
/* 310 */       return file.isFile();
/*     */     }
/* 307 */   };
/*     */ 
/*     */   public static void writeBytesToFile(byte[] bytes, File file)
/*     */     throws IOException
/*     */   {
/*  65 */     FileOutputStream out = new FileOutputStream(file);
/*  66 */     out.write(bytes);
/*  67 */     Streams.closeOutputStream(out);
/*     */   }
/*     */ 
/*     */   public static byte[] readBytesFromFile(File file)
/*     */     throws IOException
/*     */   {
/*  80 */     long fileLength = file.length();
/*  81 */     if (fileLength > 2147483647L) {
/*  82 */       String msg = "Files must be less than Integer.MAX_VALUE=2147483647 in length. Found file.length()=" + file.length();
/*     */ 
/*  85 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/*  88 */     FileInputStream in = new FileInputStream(file);
/*  89 */     ByteArrayOutputStream bytesOut = new ByteArrayOutputStream((int)fileLength);
/*  90 */     Streams.copy(in, bytesOut);
/*  91 */     Streams.closeInputStream(in);
/*  92 */     return bytesOut.toByteArray();
/*     */   }
/*     */ 
/*     */   public static void writeCharsToFile(char[] chars, File file, String encoding)
/*     */     throws IOException
/*     */   {
/* 108 */     FileOutputStream out = new FileOutputStream(file);
/* 109 */     OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
/* 110 */     writer.write(chars);
/* 111 */     Streams.closeWriter(writer);
/*     */   }
/*     */ 
/*     */   public static void writeStringToFile(String s, File file, String encoding)
/*     */     throws IOException
/*     */   {
/* 127 */     writeCharsToFile(s.toCharArray(), file, encoding);
/*     */   }
/*     */ 
/*     */   public static char[] readCharsFromFile(File file, String encoding)
/*     */     throws IOException
/*     */   {
/* 146 */     long fileLength = file.length();
/* 147 */     if (fileLength > 2147483647L) {
/* 148 */       String msg = "Files must be less than Integer.MAX_VALUE=2147483647 in length. Found file.length()=" + file.length();
/*     */ 
/* 151 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 153 */     CharArrayWriter charWriter = new CharArrayWriter((int)fileLength);
/* 154 */     FileInputStream in = null;
/* 155 */     InputStreamReader inReader = null;
/* 156 */     BufferedReader bufferedReader = null;
/*     */     try {
/* 158 */       in = new FileInputStream(file);
/* 159 */       inReader = new InputStreamReader(in, encoding);
/* 160 */       bufferedReader = new BufferedReader(inReader);
/* 161 */       Streams.copy(bufferedReader, charWriter);
/*     */     } finally {
/* 163 */       Streams.closeReader(bufferedReader);
/* 164 */       Streams.closeReader(inReader);
/* 165 */       Streams.closeInputStream(in);
/*     */     }
/* 167 */     return charWriter.toCharArray();
/*     */   }
/*     */ 
/*     */   public static String readFromFile(File file, String encoding)
/*     */     throws IOException
/*     */   {
/* 184 */     return new String(readCharsFromFile(file, encoding));
/*     */   }
/*     */ 
/*     */   public static String baseName(File file)
/*     */   {
/* 199 */     return prefix(file.getName());
/*     */   }
/*     */ 
/*     */   static String prefix(String name) {
/* 203 */     int lastDotIndex = name.lastIndexOf('.');
/* 204 */     if (lastDotIndex < 0) return name;
/* 205 */     return name.substring(0, lastDotIndex);
/*     */   }
/*     */ 
/*     */   public static String extension(File file)
/*     */   {
/* 217 */     String name = file.getName();
/* 218 */     int lastDotIndex = name.lastIndexOf('.');
/* 219 */     if (lastDotIndex < 0) return null;
/* 220 */     return name.substring(lastDotIndex + 1);
/*     */   }
/*     */ 
/*     */   public static int removeRecursive(File file)
/*     */   {
/* 232 */     if (file == null) return 0;
/* 233 */     int descCount = removeDescendants(file);
/* 234 */     file.delete();
/* 235 */     return descCount + 1;
/*     */   }
/*     */ 
/*     */   public static int removeDescendants(File file)
/*     */   {
/* 246 */     if (!file.isDirectory()) return 0;
/* 247 */     int count = 0;
/* 248 */     File[] files = file.listFiles();
/* 249 */     for (int i = 0; i < files.length; i++)
/* 250 */       count += removeRecursive(files[i]);
/* 251 */     return count;
/*     */   }
/*     */ 
/*     */   public static void copyFile(File from, File to)
/*     */     throws IOException
/*     */   {
/* 264 */     FileInputStream in = null;
/* 265 */     FileOutputStream out = null;
/*     */     try {
/* 267 */       in = new FileInputStream(from);
/* 268 */       out = new FileOutputStream(to);
/* 269 */       byte[] bytes = new byte[4096];
/* 270 */       int len = 0;
/* 271 */       while ((len = in.read(bytes)) >= 0)
/* 272 */         out.write(bytes, 0, len);
/*     */     } finally {
/* 274 */       Streams.closeInputStream(in);
/* 275 */       Streams.closeOutputStream(out);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Files
 * JD-Core Version:    0.6.2
 */