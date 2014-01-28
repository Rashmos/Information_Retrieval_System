/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class ModifyTokenTokenizerFactory extends ModifiedTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -5608290781322140944L;
/*     */ 
/*     */   public ModifyTokenTokenizerFactory(TokenizerFactory factory)
/*     */   {
/*  54 */     super(factory);
/*     */   }
/*     */ 
/*     */   public final Tokenizer modify(Tokenizer tokenizer)
/*     */   {
/*  63 */     return new ModifiedTokenizer(tokenizer);
/*     */   }
/*     */ 
/*     */   public String modifyToken(String token)
/*     */   {
/*  77 */     return token;
/*     */   }
/*     */ 
/*     */   public String modifyWhitespace(String whitespace)
/*     */   {
/*  90 */     return whitespace;
/*     */   }
/*     */   class ModifiedTokenizer extends Tokenizer implements Serializable {
/*     */     static final long serialVersionUID = -5608290781382946944L;
/*     */     private final Tokenizer mTokenizer;
/*     */ 
/*     */     ModifiedTokenizer(Tokenizer tokenizer) {
/*  98 */       this.mTokenizer = tokenizer;
/*     */     }
/*     */     public String nextToken() {
/*     */       while (true) {
/* 102 */         String token = this.mTokenizer.nextToken();
/* 103 */         if (token == null)
/* 104 */           return null;
/* 105 */         String modifiedToken = ModifyTokenTokenizerFactory.this.modifyToken(token);
/* 106 */         if (modifiedToken != null)
/* 107 */           return modifiedToken; 
/*     */       }
/*     */     }
/*     */ 
/* 111 */     public String nextWhitespace() { String whitespace = this.mTokenizer.nextWhitespace();
/* 112 */       return ModifyTokenTokenizerFactory.this.modifyWhitespace(whitespace); }
/*     */ 
/*     */     public int lastTokenStartPosition() {
/* 115 */       return this.mTokenizer.lastTokenStartPosition();
/*     */     }
/*     */     public int lastTokenEndPosition() {
/* 118 */       return this.mTokenizer.lastTokenEndPosition();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.ModifyTokenTokenizerFactory
 * JD-Core Version:    0.6.2
 */