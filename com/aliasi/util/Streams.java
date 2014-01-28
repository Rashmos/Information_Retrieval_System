/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.net.URL;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class Streams
/*     */ {
/*     */   private static final int BYTE_COPY_BUFFER_SIZE = 8192;
/*     */   private static final int CHAR_COPY_BUFFER_SIZE = 4096;
/*     */ 
/*     */   public static String getDefaultJavaCharset()
/*     */   {
/*  65 */     byte[] bytes = new byte[0];
/*  66 */     ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytes);
/*  67 */     InputStreamReader defaultReader = new InputStreamReader(bytesIn);
/*  68 */     return defaultReader.getEncoding();
/*     */   }
/*     */ 
/*     */   public static byte[] toByteArray(InputStream in)
/*     */     throws IOException
/*     */   {
/*  84 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*  85 */     copy(in, out);
/*  86 */     return out.toByteArray();
/*     */   }
/*     */ 
/*     */   public static char[] toCharArray(Reader reader)
/*     */     throws IOException
/*     */   {
/*  99 */     CharArrayWriter writer = new CharArrayWriter();
/* 100 */     copy(reader, writer);
/* 101 */     return writer.toCharArray();
/*     */   }
/*     */ 
/*     */   public static char[] toCharArray(InputStream in, String charset)
/*     */     throws IOException
/*     */   {
/* 116 */     CharArrayWriter writer = new CharArrayWriter();
/* 117 */     InputStreamReader reader = new InputStreamReader(in, charset);
/* 118 */     copy(reader, writer);
/* 119 */     return writer.toCharArray();
/*     */   }
/*     */ 
/*     */   public static char[] toCharArray(InputSource in)
/*     */     throws IOException
/*     */   {
/* 147 */     Reader reader = null;
/* 148 */     InputStream inStr = null;
/* 149 */     reader = in.getCharacterStream();
/* 150 */     if (reader == null) {
/* 151 */       inStr = in.getByteStream();
/* 152 */       if (inStr == null)
/* 153 */         inStr = new URL(in.getSystemId()).openStream();
/* 154 */       String charset = in.getEncoding();
/* 155 */       if (charset == null)
/* 156 */         reader = new InputStreamReader(inStr);
/*     */       else
/* 158 */         reader = new InputStreamReader(inStr, charset);
/*     */     }
/* 160 */     return toCharArray(reader);
/*     */   }
/*     */ 
/*     */   public static void copy(Reader reader, Writer writer)
/*     */     throws IOException
/*     */   {
/* 175 */     char[] buffer = new char[4096];
/*     */     int numChars;
/* 177 */     while ((numChars = reader.read(buffer)) > 0)
/* 178 */       writer.write(buffer, 0, numChars);
/*     */   }
/*     */ 
/*     */   public static void copy(InputStream in, OutputStream out)
/*     */     throws IOException
/*     */   {
/* 194 */     byte[] buffer = new byte[8192];
/*     */     int numBytes;
/* 196 */     while ((numBytes = in.read(buffer)) > 0)
/* 197 */       out.write(buffer, 0, numBytes);
/*     */   }
/*     */ 
/*     */   public static void closeInputSource(InputSource in)
/*     */   {
/* 209 */     if (in == null) return; try
/*     */     {
/* 211 */       closeInputStream(in.getByteStream());
/*     */     }
/*     */     finally {
/* 214 */       closeReader(in.getCharacterStream());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void closeInputStream(InputStream in)
/*     */   {
/* 227 */     if (in == null) return; try
/*     */     {
/* 229 */       in.close();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void closeOutputStream(OutputStream out)
/*     */   {
/* 244 */     if (out == null) return; try
/*     */     {
/* 246 */       out.close();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void closeReader(Reader reader)
/*     */   {
/* 260 */     if (reader == null) return; try
/*     */     {
/* 262 */       reader.close();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void closeWriter(Writer writer)
/*     */   {
/* 276 */     if (writer == null) return; try
/*     */     {
/* 278 */       writer.close();
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Streams
 * JD-Core Version:    0.6.2
 */