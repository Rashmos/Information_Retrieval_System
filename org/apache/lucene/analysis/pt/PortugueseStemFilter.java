/*    */ package org.apache.lucene.analysis.pt;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class PortugueseStemFilter extends TokenFilter
/*    */ {
/* 38 */   private final PortugueseStemmer stemmer = new PortugueseStemmer();
/* 39 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 40 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public PortugueseStemFilter(TokenStream input) {
/* 43 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 48 */     if (this.input.incrementToken()) {
/* 49 */       if (!this.keywordAttr.isKeyword())
/*    */       {
/* 51 */         int len = this.termAtt.length();
/* 52 */         int newlen = this.stemmer.stem(this.termAtt.resizeBuffer(len + 1), len);
/* 53 */         this.termAtt.setLength(newlen);
/*    */       }
/* 55 */       return true;
/*    */     }
/* 57 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.pt.PortugueseStemFilter
 * JD-Core Version:    0.6.2
 */