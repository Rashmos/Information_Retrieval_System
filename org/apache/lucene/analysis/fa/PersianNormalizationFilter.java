/*    */ package org.apache.lucene.analysis.fa;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ 
/*    */ public final class PersianNormalizationFilter extends TokenFilter
/*    */ {
/* 33 */   private final PersianNormalizer normalizer = new PersianNormalizer();
/* 34 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*    */ 
/*    */   public PersianNormalizationFilter(TokenStream input) {
/* 37 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 42 */     if (this.input.incrementToken()) {
/* 43 */       int newlen = this.normalizer.normalize(this.termAtt.buffer(), this.termAtt.length());
/*    */ 
/* 45 */       this.termAtt.setLength(newlen);
/* 46 */       return true;
/*    */     }
/* 48 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fa.PersianNormalizationFilter
 * JD-Core Version:    0.6.2
 */