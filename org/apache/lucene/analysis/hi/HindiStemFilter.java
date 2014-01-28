/*    */ package org.apache.lucene.analysis.hi;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class HindiStemFilter extends TokenFilter
/*    */ {
/* 31 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 32 */   private final KeywordAttribute keywordAtt = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/* 33 */   private final HindiStemmer stemmer = new HindiStemmer();
/*    */ 
/*    */   public HindiStemFilter(TokenStream input) {
/* 36 */     super(input);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 41 */     if (this.input.incrementToken()) {
/* 42 */       if (!this.keywordAtt.isKeyword())
/* 43 */         this.termAtt.setLength(this.stemmer.stem(this.termAtt.buffer(), this.termAtt.length()));
/* 44 */       return true;
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.hi.HindiStemFilter
 * JD-Core Version:    0.6.2
 */