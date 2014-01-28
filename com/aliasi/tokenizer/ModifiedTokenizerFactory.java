/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ 
/*     */ public abstract class ModifiedTokenizerFactory
/*     */   implements TokenizerFactory
/*     */ {
/*     */   private final TokenizerFactory mFactory;
/*     */ 
/*     */   public ModifiedTokenizerFactory(TokenizerFactory baseFactory)
/*     */   {
/*  49 */     this.mFactory = baseFactory;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory baseTokenizerFactory()
/*     */   {
/*  58 */     return this.mFactory;
/*     */   }
/*     */ 
/*     */   public Tokenizer tokenizer(char[] cs, int start, int length)
/*     */   {
/*  67 */     Tokenizer tokenizer = this.mFactory.tokenizer(cs, start, length);
/*  68 */     return modify(tokenizer);
/*     */   }
/*     */ 
/*     */   protected abstract Tokenizer modify(Tokenizer paramTokenizer);
/*     */ 
/*     */   static abstract class AbstractSerializer<T extends ModifiedTokenizerFactory> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 55645850738262892L;
/*     */     private final T mFactory;
/*     */ 
/*     */     AbstractSerializer()
/*     */     {
/*  87 */       this(null);
/*     */     }
/*     */     AbstractSerializer(T factory) {
/*  90 */       this.mFactory = factory;
/*     */     }
/*     */     public T factory() {
/*  93 */       return this.mFactory;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/*  97 */       TokenizerFactory factory = (TokenizerFactory)in.readObject();
/*  98 */       return read(in, factory);
/*     */     }
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 101 */       out.writeObject(this.mFactory.baseTokenizerFactory());
/* 102 */       writeExternalRest(out);
/*     */     }
/*     */ 
/*     */     public void writeExternalRest(ObjectOutput out)
/*     */       throws IOException
/*     */     {
/*     */     }
/*     */ 
/*     */     public abstract Object read(ObjectInput paramObjectInput, TokenizerFactory paramTokenizerFactory)
/*     */       throws IOException, ClassNotFoundException;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.ModifiedTokenizerFactory
 * JD-Core Version:    0.6.2
 */