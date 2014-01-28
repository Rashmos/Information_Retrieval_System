/*    */ package org.apache.lucene.analysis.br;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Set;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ 
/*    */ public final class BrazilianStemFilter extends TokenFilter
/*    */ {
/* 44 */   private BrazilianStemmer stemmer = new BrazilianStemmer();
/* 45 */   private Set<?> exclusions = null;
/* 46 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 47 */   private final KeywordAttribute keywordAttr = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public BrazilianStemFilter(TokenStream in)
/*    */   {
/* 55 */     super(in);
/*    */   }
/*    */ 
/*    */   @Deprecated
/*    */   public BrazilianStemFilter(TokenStream in, Set<?> exclusiontable)
/*    */   {
/* 67 */     this(in);
/* 68 */     this.exclusions = exclusiontable;
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 73 */     if (this.input.incrementToken()) {
/* 74 */       String term = this.termAtt.toString();
/*    */ 
/* 76 */       if ((!this.keywordAttr.isKeyword()) && ((this.exclusions == null) || (!this.exclusions.contains(term)))) {
/* 77 */         String s = this.stemmer.stem(term);
/*    */ 
/* 79 */         if ((s != null) && (!s.equals(term)))
/* 80 */           this.termAtt.setEmpty().append(s);
/*    */       }
/* 82 */       return true;
/*    */     }
/* 84 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.br.BrazilianStemFilter
 * JD-Core Version:    0.6.2
 */