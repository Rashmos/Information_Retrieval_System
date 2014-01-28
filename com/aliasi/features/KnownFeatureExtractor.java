/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class KnownFeatureExtractor<E> extends ModifiedFeatureExtractor<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 973305985402711781L;
/*     */   private final Set<String> mKnownFeatureSet;
/*     */ 
/*     */   public KnownFeatureExtractor(FeatureExtractor<? super E> baseExtractor, Set<String> knownFeatureSet)
/*     */   {
/*  63 */     super(baseExtractor);
/*  64 */     this.mKnownFeatureSet = knownFeatureSet;
/*     */   }
/*     */ 
/*     */   public Number filter(String feature, Number value)
/*     */   {
/*  76 */     return this.mKnownFeatureSet.contains(feature) ? value : null;
/*     */   }
/*     */ 
/*     */   public Set<String> knownFeatureSet()
/*     */   {
/*  88 */     return Collections.unmodifiableSet(this.mKnownFeatureSet);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/*  92 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -8477676811264111403L;
/*     */     final KnownFeatureExtractor<F> mExtractor;
/*     */ 
/*  99 */     public Serializer(KnownFeatureExtractor<F> extractor) { this.mExtractor = extractor; }
/*     */ 
/*     */     public Serializer() {
/* 102 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 106 */       out.writeInt(this.mExtractor.mKnownFeatureSet.size());
/* 107 */       for (String s : this.mExtractor.mKnownFeatureSet)
/* 108 */         out.writeUTF(s);
/* 109 */       out.writeObject(this.mExtractor.baseExtractor());
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 113 */       int size = in.readInt();
/* 114 */       Set knownFeatureSet = new HashSet(3 * size / 2);
/* 115 */       for (int i = 0; i < size; i++) {
/* 116 */         knownFeatureSet.add(in.readUTF());
/*     */       }
/* 118 */       FeatureExtractor baseExtractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 120 */       return new KnownFeatureExtractor(baseExtractor, knownFeatureSet);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.KnownFeatureExtractor
 * JD-Core Version:    0.6.2
 */