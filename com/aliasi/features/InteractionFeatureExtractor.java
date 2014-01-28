/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class InteractionFeatureExtractor<E>
/*     */   implements FeatureExtractor<E>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -8221138094563655817L;
/*     */   private final String mPrefix;
/*     */   private final String mSeparator;
/*     */   private final FeatureExtractor<E> mExtractor1;
/*     */   private final FeatureExtractor<E> mExtractor2;
/*     */ 
/*     */   public InteractionFeatureExtractor(String prefix, String separator, FeatureExtractor<E> extractor1, FeatureExtractor<E> extractor2)
/*     */   {
/* 111 */     this.mPrefix = prefix;
/* 112 */     this.mSeparator = separator;
/* 113 */     this.mExtractor1 = extractor1;
/* 114 */     this.mExtractor2 = extractor2;
/*     */   }
/*     */ 
/*     */   public InteractionFeatureExtractor(String prefix, String separator, FeatureExtractor<E> extractor)
/*     */   {
/* 129 */     this(prefix, separator, extractor, extractor);
/*     */   }
/*     */ 
/*     */   public Map<String, Double> features(E in)
/*     */   {
/* 139 */     return this.mExtractor1 == this.mExtractor2 ? features1(in) : features2(in);
/*     */   }
/*     */ 
/*     */   Map<String, Double> features1(E in)
/*     */   {
/* 145 */     Map featureMap = this.mExtractor1.features(in);
/*     */ 
/* 147 */     String[] features = (String[])featureMap.keySet().toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 149 */     Arrays.sort(features);
/* 150 */     double[] values = new double[features.length];
/* 151 */     for (int i = 0; i < values.length; i++)
/* 152 */       values[i] = ((Number)featureMap.get(features[i])).doubleValue();
/* 153 */     ObjectToDoubleMap featureMapResult = new ObjectToDoubleMap();
/*     */ 
/* 155 */     for (int i = 0; i < features.length; i++) {
/* 156 */       String initial = this.mPrefix + features[i] + this.mSeparator;
/* 157 */       for (int j = i; j < features.length; j++) {
/* 158 */         String feature = initial + features[j];
/* 159 */         double value = values[i] * values[j];
/* 160 */         featureMapResult.set(feature, value);
/*     */       }
/*     */     }
/* 163 */     return featureMapResult;
/*     */   }
/*     */ 
/*     */   Map<String, Double> features2(E in) {
/* 167 */     Map features1 = this.mExtractor1.features(in);
/*     */ 
/* 169 */     Map features2 = this.mExtractor2.features(in);
/*     */ 
/* 171 */     ObjectToDoubleMap features = new ObjectToDoubleMap();
/*     */ 
/* 173 */     for (Map.Entry entry1 : features1.entrySet()) {
/* 174 */       initial = this.mPrefix + (String)entry1.getKey() + this.mSeparator;
/* 175 */       val1 = ((Number)entry1.getValue()).doubleValue();
/* 176 */       for (Map.Entry entry2 : features2.entrySet()) {
/* 177 */         String feature = initial + (String)entry2.getKey();
/* 178 */         double value = val1 * ((Number)entry2.getValue()).doubleValue();
/* 179 */         features.set(feature, value);
/*     */       }
/*     */     }
/*     */     String initial;
/*     */     double val1;
/* 182 */     return features;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 186 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -2678228697747811841L;
/*     */     final InteractionFeatureExtractor<F> mExtractor;
/*     */ 
/* 193 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(InteractionFeatureExtractor<F> extractor) {
/* 196 */       this.mExtractor = extractor;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out)
/*     */       throws IOException
/*     */     {
/* 202 */       out.writeUTF(this.mExtractor.mPrefix);
/* 203 */       out.writeUTF(this.mExtractor.mSeparator);
/* 204 */       boolean same = this.mExtractor.mExtractor1 == this.mExtractor.mExtractor2;
/* 205 */       out.writeBoolean(same);
/* 206 */       out.writeObject(this.mExtractor.mExtractor1);
/* 207 */       if (!same)
/* 208 */         out.writeObject(this.mExtractor.mExtractor2);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 214 */       String prefix = in.readUTF();
/* 215 */       String separator = in.readUTF();
/* 216 */       boolean same = in.readBoolean();
/*     */ 
/* 218 */       FeatureExtractor featureExtractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 221 */       if (same) {
/* 222 */         return new InteractionFeatureExtractor(prefix, separator, featureExtractor);
/*     */       }
/*     */ 
/* 225 */       FeatureExtractor featureExtractor2 = (FeatureExtractor)in.readObject();
/*     */ 
/* 228 */       return new InteractionFeatureExtractor(prefix, separator, featureExtractor, featureExtractor2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.InteractionFeatureExtractor
 * JD-Core Version:    0.6.2
 */