/*    */ package org.apache.lucene.analysis.ar;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ 
/*    */ public final class ArabicNormalizationFilter extends TokenFilter
/*    */ {
/* 32 */   private final ArabicNormalizer normalizer = new ArabicNormalizer();
/* 33 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*    */ 
/*    */   public ArabicNormalizationFilter(TokenStream input) {
/* 36 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 41 */     if (this.input.incrementToken()) {
/* 42 */       int newlen = this.normalizer.normalize(this.termAtt.buffer(), this.termAtt.length());
/* 43 */       this.termAtt.setLength(newlen);
/* 44 */       return true;
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ar.ArabicNormalizationFilter
 * JD-Core Version:    0.6.2
 */