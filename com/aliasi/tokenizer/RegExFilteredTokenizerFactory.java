/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class RegExFilteredTokenizerFactory extends ModifyTokenTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -288230238413152431L;
/*     */   private final Pattern mPattern;
/*     */ 
/*     */   public RegExFilteredTokenizerFactory(TokenizerFactory factory, Pattern pattern)
/*     */   {
/*  69 */     super(factory);
/*  70 */     this.mPattern = pattern;
/*     */   }
/*     */ 
/*     */   public String modifyToken(String token)
/*     */   {
/*  83 */     return this.mPattern.matcher(token).matches() ? token : null;
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/*  89 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<RegExFilteredTokenizerFactory> {
/*     */     static final long serialVersionUID = -9179825153562519026L;
/*     */ 
/*     */     public Serializer() {
/*  96 */       this(null);
/*     */     }
/*     */     public Serializer(RegExFilteredTokenizerFactory factory) {
/*  99 */       super();
/*     */     }
/*     */ 
/*     */     public void writeExternalRest(ObjectOutput out) throws IOException {
/* 103 */       out.writeObject(((RegExFilteredTokenizerFactory)factory()).mPattern);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in, TokenizerFactory baseFactory) throws IOException, ClassNotFoundException {
/* 107 */       Pattern pattern = (Pattern)in.readObject();
/* 108 */       return new RegExFilteredTokenizerFactory(baseFactory, pattern);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.RegExFilteredTokenizerFactory
 * JD-Core Version:    0.6.2
 */