/*    */ package org.apache.lucene.analysis.en;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class KStemFilter extends TokenFilter
/*    */ {
/* 38 */   private final KStemmer stemmer = new KStemmer();
/* 39 */   private final CharTermAttribute termAttribute = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 40 */   private final KeywordAttribute keywordAtt = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public KStemFilter(TokenStream in) {
/* 43 */     super(in);
/*    */   }
/*    */ 
/*    */   public boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 52 */     if (!this.input.incrementToken()) {
/* 53 */       return false;
/*    */     }
/* 55 */     char[] term = this.termAttribute.buffer();
/* 56 */     int len = this.termAttribute.length();
/* 57 */     if ((!this.keywordAtt.isKeyword()) && (this.stemmer.stem(term, len))) {
/* 58 */       this.termAttribute.setEmpty().append(this.stemmer.asCharSequence());
/*    */     }
/*    */ 
/* 61 */     return true;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.en.KStemFilter
 * JD-Core Version:    0.6.2
 */