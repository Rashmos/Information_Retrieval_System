/*    */ package org.apache.lucene.analysis.cz;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class CzechStemFilter extends TokenFilter
/*    */ {
/* 40 */   private final CzechStemmer stemmer = new CzechStemmer();
/* 41 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 42 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public CzechStemFilter(TokenStream input) {
/* 45 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 50 */     if (this.input.incrementToken()) {
/* 51 */       if (!this.keywordAttr.isKeyword()) {
/* 52 */         int newlen = this.stemmer.stem(this.termAtt.buffer(), this.termAtt.length());
/* 53 */         this.termAtt.setLength(newlen);
/*    */       }
/* 55 */       return true;
/*    */     }
/* 57 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cz.CzechStemFilter
 * JD-Core Version:    0.6.2
 */