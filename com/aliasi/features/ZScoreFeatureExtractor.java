/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.classify.Classified;
/*     */ import com.aliasi.corpus.Corpus;
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.stats.OnlineNormalEstimator;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ZScoreFeatureExtractor<E> extends FeatureExtractorFilter<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -5628628145432035433L;
/*     */   final Map<String, MeanDev> mFeatureToMeanDev;
/*     */ 
/*     */   ZScoreFeatureExtractor(FeatureExtractor<? super E> extractor, Map<String, MeanDev> featureToMeanDev)
/*     */   {
/* 105 */     super(extractor);
/* 106 */     this.mFeatureToMeanDev = new LinkedHashMap(featureToMeanDev);
/*     */   }
/*     */ 
/*     */   public ZScoreFeatureExtractor(Corpus<ObjectHandler<Classified<E>>> corpus, FeatureExtractor<? super E> extractor)
/*     */     throws IOException
/*     */   {
/* 122 */     this(extractor, meanDevs(corpus, extractor));
/*     */   }
/*     */ 
/*     */   public Map<String, ? extends Number> features(E in)
/*     */   {
/* 134 */     Map featureMap = super.features(in);
/* 135 */     Map result = new HashMap();
/* 136 */     for (Map.Entry featMeanDev : this.mFeatureToMeanDev.entrySet()) {
/* 137 */       String feature = (String)featMeanDev.getKey();
/* 138 */       MeanDev meanDev = (MeanDev)featMeanDev.getValue();
/* 139 */       Number n = (Number)featureMap.get(feature);
/* 140 */       double val = meanDev.zScore(n == null ? 0.0D : ((Number)featureMap.get(feature)).doubleValue());
/*     */ 
/* 143 */       result.put(feature, Double.valueOf(val));
/*     */     }
/* 145 */     return result;
/*     */   }
/*     */ 
/*     */   public double zScore(String feature, double value)
/*     */   {
/* 157 */     MeanDev meanDev = (MeanDev)this.mFeatureToMeanDev.get(feature);
/* 158 */     return (meanDev == null ? null : Double.valueOf(meanDev.zScore(value))).doubleValue();
/*     */   }
/*     */ 
/*     */   public double mean(String feature)
/*     */   {
/* 172 */     MeanDev meanDev = (MeanDev)this.mFeatureToMeanDev.get(feature);
/* 173 */     return meanDev == null ? (0.0D / 0.0D) : meanDev.mMean;
/*     */   }
/*     */ 
/*     */   public double standardDeviation(String feature)
/*     */   {
/* 186 */     MeanDev meanDev = (MeanDev)this.mFeatureToMeanDev.get(feature);
/* 187 */     return meanDev == null ? (0.0D / 0.0D) : meanDev.mDev;
/*     */   }
/*     */ 
/*     */   public Set<String> knownFeatures()
/*     */   {
/* 199 */     return Collections.unmodifiableSet(this.mFeatureToMeanDev.keySet());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 212 */     StringBuilder sb = new StringBuilder();
/* 213 */     for (Map.Entry entry : this.mFeatureToMeanDev.entrySet()) {
/* 214 */       String feature = (String)entry.getKey();
/* 215 */       MeanDev meanDev = (MeanDev)entry.getValue();
/* 216 */       sb.append("|");
/* 217 */       sb.append(feature);
/* 218 */       sb.append("| ");
/* 219 */       sb.append(meanDev);
/* 220 */       sb.append('\n');
/*     */     }
/* 222 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 226 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static <F> Map<String, MeanDev> meanDevs(Corpus<ObjectHandler<Classified<F>>> corpus, final FeatureExtractor<? super F> extractor)
/*     */     throws IOException
/*     */   {
/* 233 */     Set collectedFeatures = new HashSet();
/* 234 */     corpus.visitTrain(new ObjectHandler() {
/*     */       public void handle(Classified<F> classified) {
/* 236 */         this.val$collectedFeatures.addAll(extractor.features(classified.getObject()).keySet());
/*     */       }
/*     */     });
/* 240 */     final Map featToEstimator = new HashMap();
/*     */ 
/* 243 */     corpus.visitTrain(new ObjectHandler() {
/*     */       public void handle(Classified<F> classified) {
/* 245 */         Object in = classified.getObject();
/*     */ 
/* 247 */         for (String feature : this.val$collectedFeatures) {
/* 248 */           Number value = (Number)extractor.features(in).get(feature);
/* 249 */           double v = value == null ? 0.0D : value.doubleValue();
/*     */ 
/* 251 */           OnlineNormalEstimator estimator = (OnlineNormalEstimator)featToEstimator.get(feature);
/* 252 */           if (estimator == null) {
/* 253 */             estimator = new OnlineNormalEstimator();
/* 254 */             featToEstimator.put(feature, estimator);
/*     */           }
/*     */ 
/* 257 */           estimator.handle(v);
/*     */         }
/*     */       }
/*     */     });
/* 261 */     Map result = new HashMap();
/* 262 */     for (Map.Entry entry : featToEstimator.entrySet()) {
/* 263 */       String feat = (String)entry.getKey();
/* 264 */       OnlineNormalEstimator estimator = (OnlineNormalEstimator)entry.getValue();
/* 265 */       double mean = estimator.mean();
/* 266 */       double dev = estimator.standardDeviation();
/* 267 */       if (dev > 0.0D)
/* 268 */         result.put(feat, new MeanDev(mean, dev));
/*     */     }
/* 270 */     return result;
/*     */   }
/*     */ 
/*     */   static class Serializer<F> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 6365515337527915147L;
/*     */     private final ZScoreFeatureExtractor<F> mFilter;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 292 */       this(null);
/*     */     }
/*     */     public Serializer(ZScoreFeatureExtractor<F> filter) {
/* 295 */       this.mFilter = filter;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 299 */       out.writeObject(this.mFilter.baseExtractor());
/* 300 */       out.writeInt(this.mFilter.mFeatureToMeanDev.size());
/* 301 */       for (Map.Entry entry : this.mFilter.mFeatureToMeanDev.entrySet()) {
/* 302 */         out.writeUTF((String)entry.getKey());
/* 303 */         out.writeDouble(((ZScoreFeatureExtractor.MeanDev)entry.getValue()).mMean);
/* 304 */         out.writeDouble(((ZScoreFeatureExtractor.MeanDev)entry.getValue()).mDev);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 313 */       FeatureExtractor extractor = (FeatureExtractor)in.readObject();
/* 314 */       int numFeats = in.readInt();
/* 315 */       Map featureToMeanDev = new HashMap(3 * numFeats / 2);
/* 316 */       for (int i = 0; i < numFeats; i++) {
/* 317 */         String feature = in.readUTF();
/* 318 */         double mean = in.readDouble();
/* 319 */         double dev = in.readDouble();
/* 320 */         featureToMeanDev.put(feature, new ZScoreFeatureExtractor.MeanDev(mean, dev));
/*     */       }
/* 322 */       return new ZScoreFeatureExtractor(extractor, featureToMeanDev);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class MeanDev
/*     */   {
/*     */     final double mMean;
/*     */     final double mDev;
/*     */ 
/*     */     MeanDev(double mean, double dev)
/*     */     {
/* 277 */       this.mMean = mean;
/* 278 */       this.mDev = dev;
/*     */     }
/*     */     double zScore(double x) {
/* 281 */       return (x - this.mMean) / this.mDev;
/*     */     }
/*     */     public String toString() {
/* 284 */       return "mean=" + this.mMean + " dev=" + this.mDev;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.ZScoreFeatureExtractor
 * JD-Core Version:    0.6.2
 */