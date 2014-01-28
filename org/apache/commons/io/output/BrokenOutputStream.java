/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class BrokenOutputStream extends OutputStream
/*    */ {
/*    */   private final IOException exception;
/*    */ 
/*    */   public BrokenOutputStream(IOException exception)
/*    */   {
/* 44 */     this.exception = exception;
/*    */   }
/*    */ 
/*    */   public BrokenOutputStream()
/*    */   {
/* 51 */     this(new IOException("Broken output stream"));
/*    */   }
/*    */ 
/*    */   public void write(int b)
/*    */     throws IOException
/*    */   {
/* 62 */     throw this.exception;
/*    */   }
/*    */ 
/*    */   public void flush()
/*    */     throws IOException
/*    */   {
/* 72 */     throw this.exception;
/*    */   }
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 82 */     throw this.exception;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.output.BrokenOutputStream
 * JD-Core Version:    0.6.2
 */