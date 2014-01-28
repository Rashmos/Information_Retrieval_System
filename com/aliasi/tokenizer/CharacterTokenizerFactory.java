/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class CharacterTokenizerFactory
/*     */   implements Serializable, TokenizerFactory
/*     */ {
/*     */   static final long serialVersionUID = -7533920958722689657L;
/* 104 */   public static final TokenizerFactory INSTANCE = new CharacterTokenizerFactory();
/*     */ 
/*     */   public Tokenizer tokenizer(char[] ch, int start, int length)
/*     */   {
/*  81 */     return new CharacterTokenizer(ch, start, length);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  91 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/*  95 */     return new Externalizer();
/*     */   }
/*     */ 
/*     */   private static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = 1313238312180578595L;
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut)
/*     */     {
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */     {
/* 119 */       return CharacterTokenizerFactory.INSTANCE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.CharacterTokenizerFactory
 * JD-Core Version:    0.6.2
 */