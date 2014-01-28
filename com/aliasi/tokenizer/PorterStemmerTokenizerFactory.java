/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import java.io.ObjectInput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class PorterStemmerTokenizerFactory extends ModifyTokenTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 1257970981781551262L;
/*     */ 
/*     */   public PorterStemmerTokenizerFactory(TokenizerFactory factory)
/*     */   {
/*  86 */     super(factory);
/*     */   }
/*     */ 
/*     */   public String modifyToken(String token)
/*     */   {
/*  97 */     return stem(token);
/*     */   }
/*     */ 
/*     */   public static String stem(String in)
/*     */   {
/* 108 */     String result = PorterStemmer.stem(in);
/* 109 */     return result;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 113 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<PorterStemmerTokenizerFactory>
/*     */   {
/*     */     static final long serialVersionUID = -4758505014396491716L;
/*     */ 
/*     */     public Serializer() {
/* 121 */       this(null);
/*     */     }
/*     */     public Serializer(PorterStemmerTokenizerFactory factory) {
/* 124 */       super();
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in, TokenizerFactory baseFactory) {
/* 128 */       return new PorterStemmerTokenizerFactory(baseFactory);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.PorterStemmerTokenizerFactory
 * JD-Core Version:    0.6.2
 */