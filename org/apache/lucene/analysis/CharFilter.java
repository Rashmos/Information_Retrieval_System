/*    */ package org.apache.lucene.analysis;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class CharFilter extends CharStream
/*    */ {
/*    */   protected CharStream input;
/*    */ 
/*    */   protected CharFilter(CharStream in)
/*    */   {
/* 36 */     this.input = in;
/*    */   }
/*    */ 
/*    */   protected int correct(int currentOff)
/*    */   {
/* 46 */     return currentOff;
/*    */   }
/*    */ 
/*    */   public final int correctOffset(int currentOff)
/*    */   {
/* 55 */     return this.input.correctOffset(correct(currentOff));
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/* 60 */     this.input.close();
/*    */   }
/*    */ 
/*    */   public int read(char[] cbuf, int off, int len) throws IOException
/*    */   {
/* 65 */     return this.input.read(cbuf, off, len);
/*    */   }
/*    */ 
/*    */   public boolean markSupported()
/*    */   {
/* 70 */     return this.input.markSupported();
/*    */   }
/*    */ 
/*    */   public void mark(int readAheadLimit) throws IOException
/*    */   {
/* 75 */     this.input.mark(readAheadLimit);
/*    */   }
/*    */ 
/*    */   public void reset() throws IOException
/*    */   {
/* 80 */     this.input.reset();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.CharFilter
 * JD-Core Version:    0.6.2
 */