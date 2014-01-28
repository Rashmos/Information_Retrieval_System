/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.stats.MultivariateEstimator;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Counter;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.Math;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BernoulliClassifier<E>
/*     */   implements JointClassifier<E>, ObjectHandler<Classified<E>>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -7761909693358968780L;
/*     */   private final MultivariateEstimator mCategoryDistribution;
/*     */   private final FeatureExtractor<E> mFeatureExtractor;
/*     */   private final double mActivationThreshold;
/*     */   private final Set<String> mFeatureSet;
/*     */   private final Map<String, ObjectToCounterMap<String>> mFeatureDistributionMap;
/*     */ 
/*     */   public BernoulliClassifier(FeatureExtractor<E> featureExtractor)
/*     */   {
/* 129 */     this(featureExtractor, 0.0D);
/*     */   }
/*     */ 
/*     */   public BernoulliClassifier(FeatureExtractor<E> featureExtractor, double featureActivationThreshold)
/*     */   {
/* 142 */     this(new MultivariateEstimator(), featureExtractor, featureActivationThreshold, new HashSet(), new HashMap());
/*     */   }
/*     */ 
/*     */   BernoulliClassifier(MultivariateEstimator catDistro, FeatureExtractor<E> featureExtractor, double activationThreshold, Set<String> featureSet, Map<String, ObjectToCounterMap<String>> featureDistributionMap)
/*     */   {
/* 154 */     this.mCategoryDistribution = catDistro;
/* 155 */     this.mFeatureExtractor = featureExtractor;
/* 156 */     this.mActivationThreshold = activationThreshold;
/* 157 */     this.mFeatureSet = featureSet;
/* 158 */     this.mFeatureDistributionMap = featureDistributionMap;
/*     */   }
/*     */ 
/*     */   public double featureActivationThreshold()
/*     */   {
/* 167 */     return this.mActivationThreshold;
/*     */   }
/*     */ 
/*     */   public FeatureExtractor<E> featureExtractor()
/*     */   {
/* 176 */     return this.mFeatureExtractor;
/*     */   }
/*     */ 
/*     */   public String[] categories()
/*     */   {
/* 185 */     String[] categories = new String[this.mCategoryDistribution.numDimensions()];
/* 186 */     for (int i = 0; i < this.mCategoryDistribution.numDimensions(); i++)
/* 187 */       categories[i] = this.mCategoryDistribution.label(i);
/* 188 */     return categories;
/*     */   }
/*     */ 
/*     */   public void handle(Classified<E> classified)
/*     */   {
/* 198 */     handle(classified.getObject(), classified.getClassification());
/*     */   }
/*     */ 
/*     */   void handle(E input, Classification classification)
/*     */   {
/* 212 */     String category = classification.bestCategory();
/* 213 */     this.mCategoryDistribution.train(category, 1L);
/* 214 */     ObjectToCounterMap categoryCounter = (ObjectToCounterMap)this.mFeatureDistributionMap.get(category);
/*     */ 
/* 216 */     if (categoryCounter == null) {
/* 217 */       categoryCounter = new ObjectToCounterMap();
/* 218 */       this.mFeatureDistributionMap.put(category, categoryCounter);
/*     */     }
/*     */ 
/* 221 */     for (String feature : activeFeatureSet(input)) {
/* 222 */       categoryCounter.increment(feature);
/* 223 */       this.mFeatureSet.add(feature);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JointClassification classify(E input)
/*     */   {
/* 235 */     Set activeFeatureSet = activeFeatureSet(input);
/* 236 */     Set inactiveFeatureSet = new HashSet(this.mFeatureSet);
/* 237 */     inactiveFeatureSet.removeAll(activeFeatureSet);
/*     */ 
/* 239 */     String[] activeFeatures = (String[])activeFeatureSet.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 241 */     String[] inactiveFeatures = (String[])inactiveFeatureSet.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 244 */     ObjectToDoubleMap categoryToLog2P = new ObjectToDoubleMap();
/*     */ 
/* 246 */     int numCategories = this.mCategoryDistribution.numDimensions();
/* 247 */     for (long i = 0L; i < numCategories; i += 1L) {
/* 248 */       String category = this.mCategoryDistribution.label(i);
/* 249 */       double log2P = Math.log2(this.mCategoryDistribution.probability(i));
/*     */ 
/* 251 */       double categoryCount = this.mCategoryDistribution.getCount(i);
/*     */ 
/* 254 */       ObjectToCounterMap categoryFeatureCounts = (ObjectToCounterMap)this.mFeatureDistributionMap.get(category);
/*     */ 
/* 257 */       for (String activeFeature : activeFeatures) {
/* 258 */         double featureCount = categoryFeatureCounts.getCount(activeFeature);
/* 259 */         if (featureCount != 0.0D) {
/* 260 */           log2P += Math.log2((featureCount + 1.0D) / (categoryCount + 2.0D));
/*     */         }
/*     */       }
/* 263 */       for (String inactiveFeature : inactiveFeatures) {
/* 264 */         double notFeatureCount = categoryCount - categoryFeatureCounts.getCount(inactiveFeature);
/*     */ 
/* 267 */         log2P += Math.log2((notFeatureCount + 1.0D) / (categoryCount + 2.0D));
/*     */       }
/* 269 */       categoryToLog2P.set(category, log2P);
/*     */     }
/* 271 */     String[] categories = new String[numCategories];
/* 272 */     double[] log2Ps = new double[numCategories];
/* 273 */     List scoredObjectList = categoryToLog2P.scoredObjectsOrderedByValueList();
/*     */ 
/* 275 */     for (int i = 0; i < numCategories; i++) {
/* 276 */       ScoredObject so = (ScoredObject)scoredObjectList.get(i);
/* 277 */       categories[i] = ((String)so.getObject());
/* 278 */       log2Ps[i] = so.score();
/*     */     }
/* 280 */     return new JointClassification(categories, log2Ps);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 284 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   private Set<String> activeFeatureSet(E input) {
/* 288 */     Set activeFeatureSet = new HashSet();
/* 289 */     Map featureMap = this.mFeatureExtractor.features(input);
/*     */ 
/* 291 */     for (Map.Entry entry : featureMap.entrySet()) {
/* 292 */       String feature = (String)entry.getKey();
/* 293 */       Number val = (Number)entry.getValue();
/* 294 */       if (val.doubleValue() > this.mActivationThreshold)
/* 295 */         activeFeatureSet.add(feature);
/*     */     }
/* 297 */     return activeFeatureSet;
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 4803666611627400222L;
/*     */     final BernoulliClassifier<F> mClassifier;
/*     */ 
/* 304 */     public Serializer(BernoulliClassifier<F> classifier) { this.mClassifier = classifier; }
/*     */ 
/*     */     public Serializer() {
/* 307 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 311 */       objOut.writeObject(this.mClassifier.mCategoryDistribution);
/* 312 */       objOut.writeObject(this.mClassifier.mFeatureExtractor);
/* 313 */       objOut.writeDouble(this.mClassifier.mActivationThreshold);
/* 314 */       objOut.writeInt(this.mClassifier.mFeatureSet.size());
/* 315 */       for (String feature : this.mClassifier.mFeatureSet)
/* 316 */         objOut.writeUTF(feature);
/* 317 */       objOut.writeInt(this.mClassifier.mFeatureDistributionMap.size());
/* 318 */       for (Map.Entry entry : this.mClassifier.mFeatureDistributionMap.entrySet()) {
/* 319 */         objOut.writeUTF((String)entry.getKey());
/* 320 */         ObjectToCounterMap map = (ObjectToCounterMap)entry.getValue();
/* 321 */         objOut.writeInt(map.size());
/* 322 */         for (Map.Entry entry2 : map.entrySet()) {
/* 323 */           objOut.writeUTF((String)entry2.getKey());
/* 324 */           objOut.writeInt(((Counter)entry2.getValue()).intValue());
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 332 */       MultivariateEstimator estimator = (MultivariateEstimator)objIn.readObject();
/*     */ 
/* 335 */       FeatureExtractor featureExtractor = (FeatureExtractor)objIn.readObject();
/*     */ 
/* 337 */       double activationThreshold = objIn.readDouble();
/* 338 */       int featureSetSize = objIn.readInt();
/* 339 */       Set featureSet = new HashSet(2 * featureSetSize);
/* 340 */       for (int i = 0; i < featureSetSize; i++)
/* 341 */         featureSet.add(objIn.readUTF());
/* 342 */       int featureDistributionMapSize = objIn.readInt();
/* 343 */       Map featureDistributionMap = new HashMap(2 * featureDistributionMapSize);
/*     */ 
/* 345 */       for (int i = 0; i < featureDistributionMapSize; i++) {
/* 346 */         String key = objIn.readUTF();
/* 347 */         int mapSize = objIn.readInt();
/* 348 */         ObjectToCounterMap otc = new ObjectToCounterMap();
/* 349 */         featureDistributionMap.put(key, otc);
/* 350 */         for (int j = 0; j < mapSize; j++) {
/* 351 */           String key2 = objIn.readUTF();
/* 352 */           int count = objIn.readInt();
/* 353 */           otc.set(key2, count);
/*     */         }
/*     */       }
/* 356 */       return new BernoulliClassifier(estimator, featureExtractor, activationThreshold, featureSet, featureDistributionMap);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.BernoulliClassifier
 * JD-Core Version:    0.6.2
 */