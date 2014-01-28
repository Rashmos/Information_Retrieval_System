/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class StopTokenizerFactory extends ModifyTokenTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -69680626775848410L;
/*     */   private final Set<String> mStopSet;
/*     */ 
/*     */   public StopTokenizerFactory(TokenizerFactory factory, Set<String> stopSet)
/*     */   {
/*  68 */     super(factory);
/*  69 */     this.mStopSet = new HashSet(stopSet);
/*     */   }
/*     */ 
/*     */   public Set<String> stopSet()
/*     */   {
/*  79 */     return Collections.unmodifiableSet(this.mStopSet);
/*     */   }
/*     */ 
/*     */   public String modifyToken(String token)
/*     */   {
/*  84 */     return this.mStopSet.contains(token) ? null : token;
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/*  90 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<StopTokenizerFactory> {
/*     */     static final long serialVersionUID = 1555788949118743254L;
/*     */ 
/*     */     public Serializer() {
/*  97 */       this(null);
/*     */     }
/*     */     public Serializer(StopTokenizerFactory factory) {
/* 100 */       super();
/*     */     }
/*     */ 
/*     */     public void writeExternalRest(ObjectOutput out) throws IOException {
/* 104 */       out.writeInt(((StopTokenizerFactory)factory()).mStopSet.size());
/* 105 */       for (String stop : ((StopTokenizerFactory)factory()).mStopSet)
/* 106 */         out.writeUTF(stop);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in, TokenizerFactory baseFactory)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 112 */       int size = in.readInt();
/* 113 */       Set stopSet = new HashSet();
/* 114 */       for (int i = 0; i < size; i++)
/* 115 */         stopSet.add(in.readUTF());
/* 116 */       return new StopTokenizerFactory(baseFactory, stopSet);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.StopTokenizerFactory
 * JD-Core Version:    0.6.2
 */