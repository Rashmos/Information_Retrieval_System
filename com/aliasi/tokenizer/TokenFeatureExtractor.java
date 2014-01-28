/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Counter;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TokenFeatureExtractor
/*     */   implements FeatureExtractor<CharSequence>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -1946484959983081450L;
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */ 
/*     */   public TokenFeatureExtractor(TokenizerFactory factory)
/*     */   {
/*  66 */     this.mTokenizerFactory = factory;
/*     */   }
/*     */ 
/*     */   public Map<String, Counter> features(CharSequence in)
/*     */   {
/*  79 */     ObjectToCounterMap map = new ObjectToCounterMap();
/*  80 */     char[] cs = Strings.toCharArray(in);
/*  81 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*  82 */     for (String token : tokenizer)
/*  83 */       map.increment(token);
/*  84 */     return map;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  96 */     return "com.aliasi.tokenizer.TokenFeatureExtractor(" + this.mTokenizerFactory + ")";
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 102 */     return new Externalizer(this);
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 4716086241839692672L;
/*     */     private final TokenFeatureExtractor mExtractor;
/*     */ 
/* 109 */     public Externalizer() { this(null); }
/*     */ 
/*     */     public Externalizer(TokenFeatureExtractor extractor) {
/* 112 */       this.mExtractor = extractor;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 116 */       AbstractExternalizable.compileOrSerialize(this.mExtractor.mTokenizerFactory, out);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 123 */       TokenizerFactory factory = (TokenizerFactory)in.readObject();
/*     */ 
/* 125 */       return new TokenFeatureExtractor(factory);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.TokenFeatureExtractor
 * JD-Core Version:    0.6.2
 */