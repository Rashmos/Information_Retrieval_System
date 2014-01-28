/*    */ package org.apache.lucene.analysis.ru;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ 
/*    */ @Deprecated
/*    */ public final class RussianLowerCaseFilter extends TokenFilter
/*    */ {
/* 35 */   private CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*    */ 
/*    */   public RussianLowerCaseFilter(TokenStream in)
/*    */   {
/* 39 */     super(in);
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 45 */     if (this.input.incrementToken()) {
/* 46 */       char[] chArray = this.termAtt.buffer();
/* 47 */       int chLen = this.termAtt.length();
/* 48 */       for (int i = 0; i < chLen; i++)
/*    */       {
/* 50 */         chArray[i] = Character.toLowerCase(chArray[i]);
/*    */       }
/* 52 */       return true;
/*    */     }
/* 54 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ru.RussianLowerCaseFilter
 * JD-Core Version:    0.6.2
 */