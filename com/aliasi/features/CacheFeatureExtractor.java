/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CacheFeatureExtractor<E> extends FeatureExtractorFilter<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -3991123544605867490L;
/*     */   final Map<E, Map<String, ? extends Number>> mCache;
/*     */ 
/*     */   public CacheFeatureExtractor(FeatureExtractor<? super E> extractor, Map<E, Map<String, ? extends Number>> cache)
/*     */   {
/*  79 */     super(extractor);
/*  80 */     this.mCache = cache;
/*     */   }
/*     */ 
/*     */   public Map<E, Map<String, ? extends Number>> cache()
/*     */   {
/*  91 */     return this.mCache;
/*     */   }
/*     */ 
/*     */   public Map<String, ? extends Number> features(E in)
/*     */   {
/*  96 */     Map features = (Map)this.mCache.get(in);
/*  97 */     if (features == null) {
/*  98 */       features = baseExtractor().features(in);
/*  99 */       this.mCache.put(in, features);
/*     */     }
/* 101 */     return features;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 105 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 4597111698841666424L;
/*     */     final CacheFeatureExtractor<F> mExtractor;
/*     */ 
/* 112 */     public Serializer(CacheFeatureExtractor<F> extractor) { this.mExtractor = extractor; }
/*     */ 
/*     */     public Serializer() {
/* 115 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 119 */       out.writeObject(this.mExtractor.mExtractor);
/* 120 */       out.writeObject(this.mExtractor.mCache);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 126 */       FeatureExtractor extractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 129 */       Map cache = (Map)in.readObject();
/*     */ 
/* 131 */       return new CacheFeatureExtractor(extractor, cache);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.CacheFeatureExtractor
 * JD-Core Version:    0.6.2
 */