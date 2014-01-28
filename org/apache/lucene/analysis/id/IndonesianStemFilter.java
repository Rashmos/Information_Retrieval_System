/*    */ package org.apache.lucene.analysis.id;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class IndonesianStemFilter extends TokenFilter
/*    */ {
/* 31 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 32 */   private final KeywordAttribute keywordAtt = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/* 33 */   private final IndonesianStemmer stemmer = new IndonesianStemmer();
/*    */   private final boolean stemDerivational;
/*    */ 
/*    */   public IndonesianStemFilter(TokenStream input)
/*    */   {
/* 40 */     this(input, true);
/*    */   }
/*    */ 
/*    */   public IndonesianStemFilter(TokenStream input, boolean stemDerivational)
/*    */   {
/* 50 */     super(input);
/* 51 */     this.stemDerivational = stemDerivational;
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 56 */     if (this.input.incrementToken()) {
/* 57 */       if (!this.keywordAtt.isKeyword()) {
/* 58 */         int newlen = this.stemmer.stem(this.termAtt.buffer(), this.termAtt.length(), this.stemDerivational);
/*    */ 
/* 60 */         this.termAtt.setLength(newlen);
/*    */       }
/* 62 */       return true;
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.id.IndonesianStemFilter
 * JD-Core Version:    0.6.2
 */