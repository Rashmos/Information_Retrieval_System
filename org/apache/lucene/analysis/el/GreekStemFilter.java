/*    */ package org.apache.lucene.analysis.el;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class GreekStemFilter extends TokenFilter
/*    */ {
/* 43 */   private final GreekStemmer stemmer = new GreekStemmer();
/* 44 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 45 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public GreekStemFilter(TokenStream input) {
/* 48 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 53 */     if (this.input.incrementToken()) {
/* 54 */       if (!this.keywordAttr.isKeyword()) {
/* 55 */         int newlen = this.stemmer.stem(this.termAtt.buffer(), this.termAtt.length());
/* 56 */         this.termAtt.setLength(newlen);
/*    */       }
/* 58 */       return true;
/*    */     }
/* 60 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.el.GreekStemFilter
 * JD-Core Version:    0.6.2
 */