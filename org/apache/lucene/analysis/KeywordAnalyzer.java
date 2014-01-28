/*    */ package org.apache.lucene.analysis;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ 
/*    */ public class KeywordAnalyzer extends Analyzer
/*    */ {
/*    */   public KeywordAnalyzer()
/*    */   {
/* 29 */     setOverridesTokenStreamMethod(KeywordAnalyzer.class);
/*    */   }
/*    */ 
/*    */   public TokenStream tokenStream(String fieldName, Reader reader)
/*    */   {
/* 34 */     return new KeywordTokenizer(reader);
/*    */   }
/*    */ 
/*    */   public TokenStream reusableTokenStream(String fieldName, Reader reader) throws IOException
/*    */   {
/* 39 */     if (this.overridesTokenStreamMethod)
/*    */     {
/* 43 */       return tokenStream(fieldName, reader);
/*    */     }
/* 45 */     Tokenizer tokenizer = (Tokenizer)getPreviousTokenStream();
/* 46 */     if (tokenizer == null) {
/* 47 */       tokenizer = new KeywordTokenizer(reader);
/* 48 */       setPreviousTokenStream(tokenizer);
/*    */     } else {
/* 50 */       tokenizer.reset(reader);
/* 51 */     }return tokenizer;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.KeywordAnalyzer
 * JD-Core Version:    0.6.2
 */