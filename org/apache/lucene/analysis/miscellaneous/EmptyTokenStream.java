/*    */ package org.apache.lucene.analysis.miscellaneous;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ 
/*    */ public final class EmptyTokenStream extends TokenStream
/*    */ {
/*    */   public final boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 31 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.miscellaneous.EmptyTokenStream
 * JD-Core Version:    0.6.2
 */