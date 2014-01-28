/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class LineIterator
/*     */   implements Iterator<String>
/*     */ {
/*     */   private final BufferedReader bufferedReader;
/*     */   private String cachedLine;
/*  59 */   private boolean finished = false;
/*     */ 
/*     */   public LineIterator(Reader reader)
/*     */     throws IllegalArgumentException
/*     */   {
/*  68 */     if (reader == null) {
/*  69 */       throw new IllegalArgumentException("Reader must not be null");
/*     */     }
/*  71 */     if ((reader instanceof BufferedReader))
/*  72 */       this.bufferedReader = ((BufferedReader)reader);
/*     */     else
/*  74 */       this.bufferedReader = new BufferedReader(reader);
/*     */   }
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*  88 */     if (this.cachedLine != null)
/*  89 */       return true;
/*  90 */     if (this.finished)
/*  91 */       return false;
/*     */     try
/*     */     {
/*     */       while (true) {
/*  95 */         String line = this.bufferedReader.readLine();
/*  96 */         if (line == null) {
/*  97 */           this.finished = true;
/*  98 */           return false;
/*  99 */         }if (isValidLine(line)) {
/* 100 */           this.cachedLine = line;
/* 101 */           return true;
/*     */         }
/*     */       }
/*     */     } catch (IOException ioe) {
/* 105 */       close();
/* 106 */       throw new IllegalStateException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isValidLine(String line)
/*     */   {
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   public String next()
/*     */   {
/* 128 */     return nextLine();
/*     */   }
/*     */ 
/*     */   public String nextLine()
/*     */   {
/* 138 */     if (!hasNext()) {
/* 139 */       throw new NoSuchElementException("No more lines");
/*     */     }
/* 141 */     String currentLine = this.cachedLine;
/* 142 */     this.cachedLine = null;
/* 143 */     return currentLine;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 154 */     this.finished = true;
/* 155 */     IOUtils.closeQuietly(this.bufferedReader);
/* 156 */     this.cachedLine = null;
/*     */   }
/*     */ 
/*     */   public void remove()
/*     */   {
/* 165 */     throw new UnsupportedOperationException("Remove unsupported on LineIterator");
/*     */   }
/*     */ 
/*     */   public static void closeQuietly(LineIterator iterator)
/*     */   {
/* 175 */     if (iterator != null)
/* 176 */       iterator.close();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.LineIterator
 * JD-Core Version:    0.6.2
 */