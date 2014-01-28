/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.util.ArrayUtil;
/*    */ 
/*    */ abstract class FormatPostingsTermsConsumer
/*    */ {
/*    */   char[] termBuffer;
/*    */ 
/*    */   abstract FormatPostingsDocsConsumer addTerm(char[] paramArrayOfChar, int paramInt)
/*    */     throws IOException;
/*    */ 
/*    */   FormatPostingsDocsConsumer addTerm(String text)
/*    */     throws IOException
/*    */   {
/* 36 */     int len = text.length();
/* 37 */     if ((this.termBuffer == null) || (this.termBuffer.length < 1 + len))
/* 38 */       this.termBuffer = new char[ArrayUtil.getNextSize(1 + len)];
/* 39 */     text.getChars(0, len, this.termBuffer, 0);
/* 40 */     this.termBuffer[len] = 65535;
/* 41 */     return addTerm(this.termBuffer, 0);
/*    */   }
/*    */ 
/*    */   abstract void finish()
/*    */     throws IOException;
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FormatPostingsTermsConsumer
 * JD-Core Version:    0.6.2
 */