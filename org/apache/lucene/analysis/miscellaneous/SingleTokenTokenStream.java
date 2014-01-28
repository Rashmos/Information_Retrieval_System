/*    */ package org.apache.lucene.analysis.miscellaneous;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.Token;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.util.AttributeImpl;
/*    */ 
/*    */ public final class SingleTokenTokenStream extends TokenStream
/*    */ {
/* 32 */   private boolean exhausted = false;
/*    */   private Token singleToken;
/*    */   private final AttributeImpl tokenAtt;
/*    */ 
/*    */   public SingleTokenTokenStream(Token token)
/*    */   {
/* 39 */     super(Token.TOKEN_ATTRIBUTE_FACTORY);
/*    */ 
/* 41 */     assert (token != null);
/* 42 */     this.singleToken = ((Token)token.clone());
/*    */ 
/* 44 */     this.tokenAtt = ((AttributeImpl)addAttribute(CharTermAttribute.class));
/* 45 */     assert ((this.tokenAtt instanceof Token));
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken() throws IOException
/*    */   {
/* 50 */     if (this.exhausted) {
/* 51 */       return false;
/*    */     }
/* 53 */     clearAttributes();
/* 54 */     this.singleToken.copyTo(this.tokenAtt);
/* 55 */     this.exhausted = true;
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */     throws IOException
/*    */   {
/* 62 */     this.exhausted = false;
/*    */   }
/*    */ 
/*    */   public Token getToken() {
/* 66 */     return (Token)this.singleToken.clone();
/*    */   }
/*    */ 
/*    */   public void setToken(Token token) {
/* 70 */     this.singleToken = ((Token)token.clone());
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.miscellaneous.SingleTokenTokenStream
 * JD-Core Version:    0.6.2
 */