/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class BrokenInputStream extends InputStream
/*     */ {
/*     */   private final IOException exception;
/*     */ 
/*     */   public BrokenInputStream(IOException exception)
/*     */   {
/*  44 */     this.exception = exception;
/*     */   }
/*     */ 
/*     */   public BrokenInputStream()
/*     */   {
/*  51 */     this(new IOException("Broken input stream"));
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  62 */     throw this.exception;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/*  73 */     throw this.exception;
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/*  85 */     throw this.exception;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/*  95 */     throw this.exception;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 105 */     throw this.exception;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.input.BrokenInputStream
 * JD-Core Version:    0.6.2
 */