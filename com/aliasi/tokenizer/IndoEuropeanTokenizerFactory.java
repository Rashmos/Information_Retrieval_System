/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class IndoEuropeanTokenizerFactory
/*     */   implements TokenizerFactory, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -5608280781322140944L;
/* 100 */   public static final IndoEuropeanTokenizerFactory INSTANCE = new IndoEuropeanTokenizerFactory();
/*     */ 
/* 103 */   static final IndoEuropeanTokenizerFactory FACTORY = INSTANCE;
/*     */ 
/*     */   public Tokenizer tokenizer(char[] ch, int start, int length)
/*     */   {
/* 129 */     return new IndoEuropeanTokenizer(ch, start, length);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 133 */     return new Externalizer();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 142 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   private static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 3826670589236636230L;
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut)
/*     */     {
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */     {
/* 156 */       return IndoEuropeanTokenizerFactory.INSTANCE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.IndoEuropeanTokenizerFactory
 * JD-Core Version:    0.6.2
 */