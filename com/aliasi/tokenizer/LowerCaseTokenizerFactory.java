/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class LowerCaseTokenizerFactory extends ModifyTokenTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 4616272325206021322L;
/*     */   private final Locale mLocale;
/*     */ 
/*     */   public LowerCaseTokenizerFactory(TokenizerFactory factory)
/*     */   {
/*  63 */     this(factory, Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */   public LowerCaseTokenizerFactory(TokenizerFactory factory, Locale locale)
/*     */   {
/*  75 */     super(factory);
/*  76 */     this.mLocale = locale;
/*     */   }
/*     */ 
/*     */   public Locale locale()
/*     */   {
/*  85 */     return this.mLocale;
/*     */   }
/*     */ 
/*     */   public String modifyToken(String token)
/*     */   {
/*  97 */     return token.toLowerCase(this.mLocale);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 101 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<LowerCaseTokenizerFactory>
/*     */   {
/*     */     static final long serialVersionUID = 994442977212325798L;
/*     */ 
/*     */     public Serializer() {
/* 109 */       this(null);
/*     */     }
/*     */     public Serializer(LowerCaseTokenizerFactory factory) {
/* 112 */       super();
/*     */     }
/*     */     public void writeExternalRest(ObjectOutput out) throws IOException {
/* 115 */       out.writeObject(((LowerCaseTokenizerFactory)factory()).locale());
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in, TokenizerFactory baseFactory)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 121 */       Locale locale = (Locale)in.readObject();
/* 122 */       return new LowerCaseTokenizerFactory(baseFactory, locale);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.LowerCaseTokenizerFactory
 * JD-Core Version:    0.6.2
 */