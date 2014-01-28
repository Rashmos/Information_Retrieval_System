/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class LengthNormFeatureExtractor<E> extends FeatureExtractorFilter<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -5628628360712035433L;
/*     */ 
/*     */   public LengthNormFeatureExtractor(FeatureExtractor<? super E> extractor)
/*     */   {
/*  70 */     super(extractor);
/*     */   }
/*     */ 
/*     */   public Map<String, ? extends Number> features(E in)
/*     */   {
/*  81 */     Map baseMap = baseExtractor().features(in);
/*  82 */     double sumOfSquares = 0.0D;
/*  83 */     for (Number n : baseMap.values()) {
/*  84 */       double val = n.doubleValue();
/*  85 */       sumOfSquares += val * val;
/*     */     }
/*  87 */     if (sumOfSquares == 0.0D)
/*  88 */       return baseMap;
/*  89 */     double length = Math.sqrt(sumOfSquares);
/*  90 */     Map resultMap = new HashMap();
/*  91 */     for (Map.Entry entry : baseMap.entrySet()) {
/*  92 */       resultMap.put(entry.getKey(), Double.valueOf(((Number)entry.getValue()).doubleValue() / length));
/*     */     }
/*  94 */     return resultMap;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/*  98 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 6365515337527915147L;
/*     */     private final LengthNormFeatureExtractor<F> mFilter;
/*     */ 
/* 105 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(LengthNormFeatureExtractor<F> filter) {
/* 108 */       this.mFilter = filter;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 112 */       out.writeObject(this.mFilter.mExtractor);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 120 */       FeatureExtractor extractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 122 */       return new LengthNormFeatureExtractor(extractor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.LengthNormFeatureExtractor
 * JD-Core Version:    0.6.2
 */