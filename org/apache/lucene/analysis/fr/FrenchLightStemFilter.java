/*    */ package org.apache.lucene.analysis.fr;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class FrenchLightStemFilter extends TokenFilter
/*    */ {
/* 38 */   private final FrenchLightStemmer stemmer = new FrenchLightStemmer();
/* 39 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 40 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public FrenchLightStemFilter(TokenStream input) {
/* 43 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 48 */     if (this.input.incrementToken()) {
/* 49 */       if (!this.keywordAttr.isKeyword()) {
/* 50 */         int newlen = this.stemmer.stem(this.termAtt.buffer(), this.termAtt.length());
/* 51 */         this.termAtt.setLength(newlen);
/*    */       }
/* 53 */       return true;
/*    */     }
/* 55 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fr.FrenchLightStemFilter
 * JD-Core Version:    0.6.2
 */