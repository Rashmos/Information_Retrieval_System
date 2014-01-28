/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class AddFeatureExtractor<E>
/*     */   implements FeatureExtractor<E>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -79320527848334652L;
/*     */   private final List<FeatureExtractor<? super E>> mExtractors;
/*     */ 
/*     */   public AddFeatureExtractor(Collection<? extends FeatureExtractor<? super E>> extractors)
/*     */   {
/*  64 */     this.mExtractors = new ArrayList(extractors);
/*     */   }
/*     */ 
/*     */   public AddFeatureExtractor(FeatureExtractor<? super E> extractor1, FeatureExtractor<? super E> extractor2)
/*     */   {
/*  76 */     this.mExtractors = new ArrayList(2);
/*  77 */     this.mExtractors.add(extractor1);
/*  78 */     this.mExtractors.add(extractor2);
/*     */   }
/*     */ 
/*     */   public AddFeatureExtractor(FeatureExtractor<? super E>[] extractors)
/*     */   {
/*  88 */     this(Arrays.asList(extractors));
/*     */   }
/*     */ 
/*     */   public Map<String, ? extends Number> features(E in)
/*     */   {
/*  93 */     ObjectToDoubleMap result = new ObjectToDoubleMap();
/*  94 */     for (FeatureExtractor extractor : this.mExtractors)
/*  95 */       for (Map.Entry featMap : extractor.features(in).entrySet())
/*  96 */         result.increment(featMap.getKey(), ((Number)featMap.getValue()).doubleValue());
/*  97 */     return result;
/*     */   }
/*     */ 
/*     */   public List<FeatureExtractor<? super E>> baseFeatureExtractors()
/*     */   {
/* 107 */     return Collections.unmodifiableList(this.mExtractors);
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 112 */     return new Externalizer(this);
/*     */   }
/*     */   static class Externalizer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -3717190107802122376L;
/*     */     final AddFeatureExtractor<F> mAddFeatureExtractor;
/*     */ 
/*     */     public Externalizer(AddFeatureExtractor<F> addFeatureExtractor) {
/* 120 */       this.mAddFeatureExtractor = addFeatureExtractor;
/*     */     }
/*     */ 
/*     */     public Externalizer() {
/* 124 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException
/*     */     {
/* 129 */       out.writeInt(this.mAddFeatureExtractor.mExtractors.size());
/* 130 */       for (FeatureExtractor extractor : this.mAddFeatureExtractor.mExtractors)
/* 131 */         out.writeObject(extractor);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 138 */       int size = in.readInt();
/* 139 */       List extractors = new ArrayList();
/* 140 */       for (int i = 0; i < size; i++)
/*     */       {
/* 143 */         FeatureExtractor extractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 145 */         extractors.add(extractor);
/*     */       }
/* 147 */       return new AddFeatureExtractor(extractors);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.AddFeatureExtractor
 * JD-Core Version:    0.6.2
 */