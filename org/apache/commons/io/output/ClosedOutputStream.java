/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class ClosedOutputStream extends OutputStream
/*    */ {
/* 38 */   public static final ClosedOutputStream CLOSED_OUTPUT_STREAM = new ClosedOutputStream();
/*    */ 
/*    */   public void write(int b)
/*    */     throws IOException
/*    */   {
/* 48 */     throw new IOException("write(" + b + ") failed: stream is closed");
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.output.ClosedOutputStream
 * JD-Core Version:    0.6.2
 */