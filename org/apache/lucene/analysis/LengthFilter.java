/*    */ package org.apache.lucene.analysis;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/*    */ 
/*    */ public final class LengthFilter extends TokenFilter
/*    */ {
/*    */   final int min;
/*    */   final int max;
/*    */   private TermAttribute termAtt;
/*    */ 
/*    */   public LengthFilter(TokenStream in, int min, int max)
/*    */   {
/* 40 */     super(in);
/* 41 */     this.min = min;
/* 42 */     this.max = max;
/* 43 */     this.termAtt = ((TermAttribute)addAttribute(TermAttribute.class));
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 52 */     while (this.input.incrementToken()) {
/* 53 */       int len = this.termAtt.termLength();
/* 54 */       if ((len >= this.min) && (len <= this.max)) {
/* 55 */         return true;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 60 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.LengthFilter
 * JD-Core Version:    0.6.2
 */