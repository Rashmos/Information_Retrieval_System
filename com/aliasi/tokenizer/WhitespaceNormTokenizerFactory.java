/*    */ package com.aliasi.tokenizer;
/*    */ 
/*    */ import java.io.ObjectInput;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class WhitespaceNormTokenizerFactory extends ModifyTokenTokenizerFactory
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = -5560361157129856065L;
/*    */ 
/*    */   public WhitespaceNormTokenizerFactory(TokenizerFactory factory)
/*    */   {
/* 58 */     super(factory);
/*    */   }
/*    */ 
/*    */   public String modifyWhitespace(String whitespace)
/*    */   {
/* 69 */     return whitespace.length() > 0 ? " " : "";
/*    */   }
/*    */ 
/*    */   Object writeReplace() {
/* 73 */     return new Serializer(this);
/*    */   }
/*    */ 
/*    */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<WhitespaceNormTokenizerFactory>
/*    */   {
/*    */     static final long serialVersionUID = -9192398875622789296L;
/*    */ 
/*    */     public Serializer() {
/* 81 */       this(null);
/*    */     }
/*    */     public Serializer(WhitespaceNormTokenizerFactory factory) {
/* 84 */       super();
/*    */     }
/*    */ 
/*    */     public Object read(ObjectInput in, TokenizerFactory baseFactory) {
/* 88 */       return new WhitespaceNormTokenizerFactory(baseFactory);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.WhitespaceNormTokenizerFactory
 * JD-Core Version:    0.6.2
 */