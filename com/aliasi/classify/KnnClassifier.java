/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.features.Features;
/*     */ import com.aliasi.matrix.EuclideanDistance;
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.symbol.MapSymbolTable;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.Proximity;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class KnnClassifier<E>
/*     */   implements ScoredClassifier<E>, ObjectHandler<Classified<E>>, Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 5692985587478284405L;
/*     */   final FeatureExtractor<? super E> mFeatureExtractor;
/*     */   final int mK;
/*     */   final Proximity<Vector> mProximity;
/*     */   final boolean mWeightByProximity;
/*     */   final List<Integer> mTrainingCategories;
/*     */   final List<Vector> mTrainingVectors;
/*     */   final MapSymbolTable mFeatureSymbolTable;
/*     */   final MapSymbolTable mCategorySymbolTable;
/*     */ 
/*     */   public KnnClassifier(FeatureExtractor<? super E> featureExtractor, int k)
/*     */   {
/* 197 */     this(featureExtractor, k, EuclideanDistance.DISTANCE);
/*     */   }
/*     */ 
/*     */   public KnnClassifier(FeatureExtractor<? super E> featureExtractor, int k, Distance<Vector> distance)
/*     */   {
/* 217 */     this(featureExtractor, k, new ProximityWrapper(distance), false);
/*     */   }
/*     */ 
/*     */   public KnnClassifier(FeatureExtractor<? super E> extractor, int k, Proximity<Vector> proximity, boolean weightByProximity)
/*     */   {
/* 240 */     this(extractor, k, proximity, weightByProximity, new ArrayList(), new ArrayList(), new MapSymbolTable(), new MapSymbolTable());
/*     */   }
/*     */ 
/*     */   KnnClassifier(FeatureExtractor<? super E> featureExtractor, int k, Proximity<Vector> proximity, boolean weightByProximity, List<Integer> trainingCategories, List<Vector> trainingVectors, MapSymbolTable featureSymbolTable, MapSymbolTable categorySymbolTable)
/*     */   {
/* 253 */     this.mFeatureExtractor = featureExtractor;
/* 254 */     this.mK = k;
/* 255 */     this.mProximity = proximity;
/* 256 */     this.mWeightByProximity = weightByProximity;
/* 257 */     this.mTrainingCategories = trainingCategories;
/* 258 */     this.mTrainingVectors = trainingVectors;
/* 259 */     this.mFeatureSymbolTable = featureSymbolTable;
/* 260 */     this.mCategorySymbolTable = categorySymbolTable;
/*     */   }
/*     */ 
/*     */   public FeatureExtractor<? super E> featureExtractor()
/*     */   {
/* 269 */     return this.mFeatureExtractor;
/*     */   }
/*     */ 
/*     */   public Proximity<Vector> proximity()
/*     */   {
/* 278 */     return this.mProximity;
/*     */   }
/*     */ 
/*     */   public List<String> categories()
/*     */   {
/* 288 */     List catList = new ArrayList();
/* 289 */     for (Integer i : this.mTrainingCategories)
/* 290 */       catList.add(this.mCategorySymbolTable.idToSymbol(i));
/* 291 */     return catList;
/*     */   }
/*     */ 
/*     */   public boolean weightByProximity()
/*     */   {
/* 301 */     return this.mWeightByProximity;
/*     */   }
/*     */ 
/*     */   public int k()
/*     */   {
/* 311 */     return this.mK;
/*     */   }
/*     */ 
/*     */   void handle(E trainingInstance, Classification classification)
/*     */   {
/* 324 */     String category = classification.bestCategory();
/* 325 */     Map featureMap = this.mFeatureExtractor.features(trainingInstance);
/*     */ 
/* 327 */     Vector vector = Features.toVectorAddSymbols(featureMap, this.mFeatureSymbolTable, 2147483646, false);
/*     */ 
/* 334 */     this.mTrainingCategories.add(this.mCategorySymbolTable.getOrAddSymbolInteger(category));
/* 335 */     this.mTrainingVectors.add(vector);
/*     */   }
/*     */ 
/*     */   public void handle(Classified<E> classifiedObject)
/*     */   {
/* 347 */     handle(classifiedObject.getObject(), classifiedObject.getClassification());
/*     */   }
/*     */ 
/*     */   public ScoredClassification classify(E in)
/*     */   {
/* 372 */     Map featureMap = this.mFeatureExtractor.features(in);
/*     */ 
/* 374 */     Vector inputVector = Features.toVector(featureMap, this.mFeatureSymbolTable, 2147483646, false);
/*     */ 
/* 381 */     BoundedPriorityQueue queue = new BoundedPriorityQueue(ScoredObject.comparator(), this.mK);
/*     */ 
/* 384 */     for (int i = 0; i < this.mTrainingCategories.size(); i++) {
/* 385 */       Integer catId = (Integer)this.mTrainingCategories.get(i);
/* 386 */       Vector trainingVector = (Vector)this.mTrainingVectors.get(i);
/* 387 */       double score = this.mProximity.proximity(inputVector, trainingVector);
/* 388 */       queue.offer(new ScoredObject(catId, score));
/*     */     }
/*     */ 
/* 391 */     int numCats = this.mCategorySymbolTable.numSymbols();
/* 392 */     double[] scores = new double[numCats];
/*     */ 
/* 394 */     for (ScoredObject catScore : queue) {
/* 395 */       int key = ((Integer)catScore.getObject()).intValue();
/* 396 */       double score = catScore.score();
/* 397 */       scores[key] += (this.mWeightByProximity ? score : 1.0D);
/*     */     }
/*     */ 
/* 402 */     ScoredObject[] categoryScores = (ScoredObject[])new ScoredObject[numCats];
/*     */ 
/* 405 */     for (int i = 0; i < numCats; i++) {
/* 406 */       categoryScores[i] = new ScoredObject(this.mCategorySymbolTable.idToSymbol(i), scores[i]);
/*     */     }
/*     */ 
/* 409 */     return ScoredClassification.create(categoryScores);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 413 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 428 */     out.writeObject(writeReplace());
/*     */   }
/*     */ 
/*     */   static class TrainingInstance
/*     */   {
/*     */     final String mCategory;
/*     */     final Vector mVector;
/*     */ 
/*     */     TrainingInstance(String category, Vector vector)
/*     */     {
/* 520 */       this.mCategory = category;
/* 521 */       this.mVector = vector;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ProximityWrapper
/*     */     implements Proximity<Vector>, Serializable
/*     */   {
/*     */     static final long serialVersionUID = -1410999733708772109L;
/*     */     Distance<Vector> mDistance;
/*     */ 
/*     */     public ProximityWrapper()
/*     */     {
/*     */     }
/*     */ 
/*     */     public ProximityWrapper(Distance<Vector> distance)
/*     */     {
/* 508 */       this.mDistance = distance;
/*     */     }
/*     */     public double proximity(Vector v1, Vector v2) {
/* 511 */       double d = this.mDistance.distance(v1, v2);
/* 512 */       return d < 0.0D ? 1.7976931348623157E+308D : 1.0D / (1.0D + d);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Serializer<F> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 4951969636521202268L;
/*     */     final KnnClassifier<F> mClassifier;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 435 */       this(null);
/*     */     }
/*     */     public Serializer(KnnClassifier<F> classifier) {
/* 438 */       this.mClassifier = classifier;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 442 */       AbstractExternalizable.serializeOrCompile(this.mClassifier.mFeatureExtractor, out);
/* 443 */       out.writeInt(this.mClassifier.mK);
/* 444 */       AbstractExternalizable.serializeOrCompile(this.mClassifier.mProximity, out);
/* 445 */       out.writeBoolean(this.mClassifier.mWeightByProximity);
/* 446 */       int numInstances = this.mClassifier.mTrainingCategories.size();
/* 447 */       out.writeInt(numInstances);
/*     */ 
/* 449 */       for (int i = 0; i < numInstances; i++) {
/* 450 */         out.writeInt(((Integer)this.mClassifier.mTrainingCategories.get(i)).intValue());
/*     */       }
/* 452 */       for (int i = 0; i < numInstances; i++) {
/* 453 */         AbstractExternalizable.serializeOrCompile(this.mClassifier.mTrainingVectors.get(i), out);
/*     */       }
/* 455 */       AbstractExternalizable.serializeOrCompile(this.mClassifier.mFeatureSymbolTable, out);
/* 456 */       AbstractExternalizable.serializeOrCompile(this.mClassifier.mCategorySymbolTable, out);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 464 */       FeatureExtractor featureExtractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 466 */       int k = in.readInt();
/*     */ 
/* 470 */       Proximity proximity = (Proximity)in.readObject();
/*     */ 
/* 473 */       boolean weightByProximity = in.readBoolean();
/* 474 */       int numInstances = in.readInt();
/* 475 */       List categoryList = new ArrayList(numInstances);
/* 476 */       for (int i = 0; i < numInstances; i++)
/* 477 */         categoryList.add(Integer.valueOf(in.readInt()));
/* 478 */       List vectorList = new ArrayList(numInstances);
/*     */ 
/* 480 */       for (int i = 0; i < numInstances; i++)
/* 481 */         vectorList.add((Vector)in.readObject());
/* 482 */       MapSymbolTable featureSymbolTable = (MapSymbolTable)in.readObject();
/*     */ 
/* 484 */       MapSymbolTable categorySymbolTable = (MapSymbolTable)in.readObject();
/*     */ 
/* 487 */       return new KnnClassifier(featureExtractor, k, proximity, weightByProximity, categoryList, vectorList, featureSymbolTable, categorySymbolTable);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.KnnClassifier
 * JD-Core Version:    0.6.2
 */