/*    */ package org.apache.lucene.analysis.miscellaneous;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.Token;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ 
/*    */ public class PrefixAndSuffixAwareTokenFilter extends TokenStream
/*    */ {
/*    */   private PrefixAwareTokenFilter suffix;
/*    */ 
/*    */   public PrefixAndSuffixAwareTokenFilter(TokenStream prefix, TokenStream input, TokenStream suffix)
/*    */   {
/* 36 */     super(suffix);
/* 37 */     prefix = new PrefixAwareTokenFilter(prefix, input)
/*    */     {
/*    */       public Token updateSuffixToken(Token suffixToken, Token lastInputToken) {
/* 40 */         return PrefixAndSuffixAwareTokenFilter.this.updateInputToken(suffixToken, lastInputToken);
/*    */       }
/*    */     };
/* 43 */     this.suffix = new PrefixAwareTokenFilter(prefix, suffix)
/*    */     {
/*    */       public Token updateSuffixToken(Token suffixToken, Token lastInputToken) {
/* 46 */         return PrefixAndSuffixAwareTokenFilter.this.updateSuffixToken(suffixToken, lastInputToken);
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public Token updateInputToken(Token inputToken, Token lastPrefixToken) {
/* 52 */     inputToken.setStartOffset(lastPrefixToken.endOffset() + inputToken.startOffset());
/* 53 */     inputToken.setEndOffset(lastPrefixToken.endOffset() + inputToken.endOffset());
/* 54 */     return inputToken;
/*    */   }
/*    */ 
/*    */   public Token updateSuffixToken(Token suffixToken, Token lastInputToken) {
/* 58 */     suffixToken.setStartOffset(lastInputToken.endOffset() + suffixToken.startOffset());
/* 59 */     suffixToken.setEndOffset(lastInputToken.endOffset() + suffixToken.endOffset());
/* 60 */     return suffixToken;
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 66 */     return this.suffix.incrementToken();
/*    */   }
/*    */ 
/*    */   public void reset() throws IOException
/*    */   {
/* 71 */     this.suffix.reset();
/*    */   }
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 77 */     this.suffix.close();
/*    */   }
/*    */ 
/*    */   public void end() throws IOException
/*    */   {
/* 82 */     this.suffix.end();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.miscellaneous.PrefixAndSuffixAwareTokenFilter
 * JD-Core Version:    0.6.2
 */