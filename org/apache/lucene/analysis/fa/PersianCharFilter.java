/*    */ package org.apache.lucene.analysis.fa;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.CharFilter;
/*    */ import org.apache.lucene.analysis.CharStream;
/*    */ 
/*    */ public class PersianCharFilter extends CharFilter
/*    */ {
/*    */   public PersianCharFilter(CharStream in)
/*    */   {
/* 32 */     super(in);
/*    */   }
/*    */ 
/*    */   public int read(char[] cbuf, int off, int len) throws IOException
/*    */   {
/* 37 */     int charsRead = super.read(cbuf, off, len);
/* 38 */     if (charsRead > 0) {
/* 39 */       int end = off + charsRead;
/* 40 */       while (off < end) {
/* 41 */         if (cbuf[off] == '‌')
/* 42 */           cbuf[off] = ' ';
/* 43 */         off++;
/*    */       }
/*    */     }
/* 46 */     return charsRead;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fa.PersianCharFilter
 * JD-Core Version:    0.6.2
 */