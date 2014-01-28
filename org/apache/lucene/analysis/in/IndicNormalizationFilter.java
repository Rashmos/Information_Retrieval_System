/*    */ package org.apache.lucene.analysis.in;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ 
/*    */ public final class IndicNormalizationFilter extends TokenFilter
/*    */ {
/* 31 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 32 */   private final IndicNormalizer normalizer = new IndicNormalizer();
/*    */ 
/*    */   public IndicNormalizationFilter(TokenStream input) {
/* 35 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 40 */     if (this.input.incrementToken()) {
/* 41 */       this.termAtt.setLength(this.normalizer.normalize(this.termAtt.buffer(), this.termAtt.length()));
/* 42 */       return true;
/*    */     }
/* 44 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.in.IndicNormalizationFilter
 * JD-Core Version:    0.6.2
 */