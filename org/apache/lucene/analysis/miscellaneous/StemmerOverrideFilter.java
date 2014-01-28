/*    */ package org.apache.lucene.analysis.miscellaneous;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Map;
/*    */ import org.apache.lucene.analysis.CharArrayMap;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
/*    */ import org.apache.lucene.util.Version;
/*    */ 
/*    */ public final class StemmerOverrideFilter extends TokenFilter
/*    */ {
/*    */   private final CharArrayMap<String> dictionary;
/* 37 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 38 */   private final KeywordAttribute keywordAtt = (KeywordAttribute)addAttribute(KeywordAttribute.class);
/*    */ 
/*    */   public StemmerOverrideFilter(Version matchVersion, TokenStream input, Map<?, String> dictionary)
/*    */   {
/* 50 */     super(input);
/* 51 */     this.dictionary = ((dictionary instanceof CharArrayMap) ? (CharArrayMap)dictionary : CharArrayMap.copy(matchVersion, dictionary));
/*    */   }
/*    */ 
/*    */   public boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 57 */     if (this.input.incrementToken()) {
/* 58 */       if (!this.keywordAtt.isKeyword()) {
/* 59 */         String stem = (String)this.dictionary.get(this.termAtt.buffer(), 0, this.termAtt.length());
/* 60 */         if (stem != null) {
/* 61 */           this.termAtt.setEmpty().append(stem);
/* 62 */           this.keywordAtt.setKeyword(true);
/*    */         }
/*    */       }
/* 65 */       return true;
/*    */     }
/* 67 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.miscellaneous.StemmerOverrideFilter
 * JD-Core Version:    0.6.2
 */