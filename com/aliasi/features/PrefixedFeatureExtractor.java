/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class PrefixedFeatureExtractor<E>
/*     */   implements FeatureExtractor<E>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -4693775065158617229L;
/*     */   private final FeatureExtractor<E> mBaseExtractor;
/*     */   private final String mPrefix;
/*     */ 
/*     */   public PrefixedFeatureExtractor(String prefix, FeatureExtractor<E> extractor)
/*     */   {
/*  71 */     this.mPrefix = prefix;
/*  72 */     this.mBaseExtractor = extractor;
/*     */   }
/*     */ 
/*     */   public Map<String, ? extends Number> features(E in)
/*     */   {
/*  84 */     ObjectToDoubleMap prefixedFeatureMap = new ObjectToDoubleMap();
/*     */ 
/*  86 */     Map featureMap = this.mBaseExtractor.features(in);
/*  87 */     for (Map.Entry entry : featureMap.entrySet()) {
/*  88 */       prefixedFeatureMap.set(this.mPrefix + (String)entry.getKey(), ((Number)entry.getValue()).doubleValue());
/*     */     }
/*  90 */     return prefixedFeatureMap;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/*  94 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 6332957246411784407L;
/*     */     final PrefixedFeatureExtractor<F> mPrefixedFeatureExtractor;
/*     */ 
/* 101 */     public Serializer(PrefixedFeatureExtractor<F> prefixedFeatureExtractor) { this.mPrefixedFeatureExtractor = prefixedFeatureExtractor; }
/*     */ 
/*     */     public Serializer() {
/* 104 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 108 */       out.writeUTF(this.mPrefixedFeatureExtractor.mPrefix);
/* 109 */       out.writeObject(this.mPrefixedFeatureExtractor.mBaseExtractor);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 115 */       String prefix = in.readUTF();
/*     */ 
/* 117 */       FeatureExtractor extractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 119 */       return new PrefixedFeatureExtractor(prefix, extractor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.PrefixedFeatureExtractor
 * JD-Core Version:    0.6.2
 */