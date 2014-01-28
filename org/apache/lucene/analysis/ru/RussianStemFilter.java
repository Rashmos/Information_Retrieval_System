/*    */ package org.apache.lucene.analysis.ru;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ @Deprecated
/*    */ public final class RussianStemFilter extends TokenFilter
/*    */ {
/* 54 */   private RussianStemmer stemmer = new RussianStemmer();
/*    */ 
/* 56 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 57 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public RussianStemFilter(TokenStream in)
/*    */   {
/* 61 */     super(in);
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 69 */     if (this.input.incrementToken()) {
/* 70 */       if (!this.keywordAttr.isKeyword()) {
/* 71 */         String term = this.termAtt.toString();
/* 72 */         String s = this.stemmer.stem(term);
/* 73 */         if ((s != null) && (!s.equals(term)))
/* 74 */           this.termAtt.setEmpty().append(s);
/*    */       }
/* 76 */       return true;
/*    */     }
/* 78 */     return false;
/*    */   }
/*    */ 
/*    */   public void setStemmer(RussianStemmer stemmer)
/*    */   {
/* 88 */     if (stemmer != null)
/*    */     {
/* 90 */       this.stemmer = stemmer;
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ru.RussianStemFilter
 * JD-Core Version:    0.6.2
 */