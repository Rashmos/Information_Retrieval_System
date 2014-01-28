/*    */ package org.apache.lucene.analysis.en;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ 
/*    */ public final class EnglishPossessiveFilter extends TokenFilter
/*    */ {
/* 30 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*    */ 
/*    */   public EnglishPossessiveFilter(TokenStream input) {
/* 33 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 38 */     if (!this.input.incrementToken()) {
/* 39 */       return false;
/*    */     }
/*    */ 
/* 42 */     char[] buffer = this.termAtt.buffer();
/* 43 */     int bufferLength = this.termAtt.length();
/*    */ 
/* 45 */     if ((bufferLength >= 2) && (buffer[(bufferLength - 2)] == '\'') && ((buffer[(bufferLength - 1)] == 's') || (buffer[(bufferLength - 1)] == 'S')))
/*    */     {
/* 48 */       this.termAtt.setLength(bufferLength - 2);
/*    */     }
/* 50 */     return true;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.en.EnglishPossessiveFilter
 * JD-Core Version:    0.6.2
 */