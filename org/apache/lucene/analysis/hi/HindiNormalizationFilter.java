/*    */ package org.apache.lucene.analysis.hi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class HindiNormalizationFilter extends TokenFilter
/*    */ {
/* 41 */   private final HindiNormalizer normalizer = new HindiNormalizer();
/* 42 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 43 */   private final KeywordAttribute keywordAtt = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public HindiNormalizationFilter(TokenStream input) {
/* 46 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 51 */     if (this.input.incrementToken()) {
/* 52 */       if (!this.keywordAtt.isKeyword()) {
/* 53 */         this.termAtt.setLength(this.normalizer.normalize(this.termAtt.buffer(), this.termAtt.length()));
/*    */       }
/* 55 */       return true;
/*    */     }
/* 57 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hi.HindiNormalizationFilter
 * JD-Core Version:    0.6.2
 */