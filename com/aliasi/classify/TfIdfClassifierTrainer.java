/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.symbol.MapSymbolTable;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TfIdfClassifierTrainer<E>
/*     */   implements ObjectHandler<Classified<E>>, Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -2793388723202924633L;
/*     */   final FeatureExtractor<? super E> mFeatureExtractor;
/*     */   final Map<Integer, ObjectToDoubleMap<Integer>> mFeatureToCategoryCount;
/*     */   final MapSymbolTable mFeatureSymbolTable;
/*     */   final MapSymbolTable mCategorySymbolTable;
/*     */ 
/*     */   public TfIdfClassifierTrainer(FeatureExtractor<? super E> featureExtractor)
/*     */   {
/* 208 */     this(featureExtractor, new HashMap(), new MapSymbolTable(), new MapSymbolTable());
/*     */   }
/*     */ 
/*     */   TfIdfClassifierTrainer(FeatureExtractor<? super E> featureExtractor, Map<Integer, ObjectToDoubleMap<Integer>> featureToCategoryCount, MapSymbolTable featureSymbolTable, MapSymbolTable categorySymbolTable)
/*     */   {
/* 219 */     this.mFeatureExtractor = featureExtractor;
/* 220 */     this.mFeatureToCategoryCount = featureToCategoryCount;
/* 221 */     this.mFeatureSymbolTable = featureSymbolTable;
/* 222 */     this.mCategorySymbolTable = categorySymbolTable;
/*     */   }
/*     */ 
/*     */   public FeatureExtractor<? super E> featureExtractor()
/*     */   {
/* 231 */     return this.mFeatureExtractor;
/*     */   }
/*     */ 
/*     */   public Set<String> categories()
/*     */   {
/* 241 */     return this.mCategorySymbolTable.symbolSet();
/*     */   }
/*     */ 
/*     */   void handle(E input, Classification classification)
/*     */   {
/* 252 */     String category = classification.bestCategory();
/* 253 */     int categoryId = this.mCategorySymbolTable.getOrAddSymbol(category);
/*     */ 
/* 255 */     Map featureVector = this.mFeatureExtractor.features(input);
/*     */ 
/* 258 */     for (Map.Entry entry : featureVector.entrySet()) {
/* 259 */       String feature = (String)entry.getKey();
/* 260 */       double value = ((Number)entry.getValue()).doubleValue();
/* 261 */       int featureId = this.mFeatureSymbolTable.getOrAddSymbol(feature);
/* 262 */       ObjectToDoubleMap categoryCounts = (ObjectToDoubleMap)this.mFeatureToCategoryCount.get(Integer.valueOf(featureId));
/*     */ 
/* 264 */       if (categoryCounts == null) {
/* 265 */         categoryCounts = new ObjectToDoubleMap();
/* 266 */         this.mFeatureToCategoryCount.put(Integer.valueOf(featureId), categoryCounts);
/*     */       }
/* 268 */       categoryCounts.increment(Integer.valueOf(categoryId), value);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handle(Classified<E> classified)
/*     */   {
/* 279 */     handle(classified.getObject(), classified.getClassification());
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 290 */     out.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 295 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static double idf(double docFrequency, double numDocs) {
/* 299 */     return Math.log(numDocs / docFrequency);
/*     */   }
/*     */ 
/*     */   static double tf(double count) {
/* 303 */     return Math.sqrt(count);
/*     */   }
/*     */ 
/*     */   static class Serializer<F> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -4757808688956812832L;
/*     */     final TfIdfClassifierTrainer<F> mTrainer;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 528 */       this(null);
/*     */     }
/*     */     public Serializer(TfIdfClassifierTrainer<F> trainer) {
/* 531 */       this.mTrainer = trainer;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 535 */       AbstractExternalizable.serializeOrCompile(this.mTrainer.mFeatureExtractor, out);
/*     */ 
/* 537 */       out.writeObject(this.mTrainer.mFeatureToCategoryCount);
/* 538 */       out.writeObject(this.mTrainer.mFeatureSymbolTable);
/* 539 */       out.writeObject(this.mTrainer.mCategorySymbolTable);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 546 */       FeatureExtractor featureExtractor = (FeatureExtractor)objIn.readObject();
/*     */ 
/* 551 */       Map featureToCategoryCount = (Map)objIn.readObject();
/*     */ 
/* 555 */       MapSymbolTable featureSymbolTable = (MapSymbolTable)objIn.readObject();
/*     */ 
/* 558 */       MapSymbolTable categorySymbolTable = (MapSymbolTable)objIn.readObject();
/*     */ 
/* 561 */       return new TfIdfClassifierTrainer(featureExtractor, featureToCategoryCount, featureSymbolTable, categorySymbolTable);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TfIdfClassifier<G>
/*     */     implements ScoredClassifier<G>
/*     */   {
/*     */     final FeatureExtractor<? super G> mFeatureExtractor;
/*     */     final MapSymbolTable mFeatureSymbolTable;
/*     */     final String[] mCategories;
/*     */     final float[] mFeatureIdfs;
/*     */     final int[] mFeatureOffsets;
/*     */     final int[] mCategoryIds;
/*     */     final float[] mTfIdfs;
/*     */ 
/*     */     TfIdfClassifier(FeatureExtractor<? super G> featureExtractor, MapSymbolTable featureSymbolTable, String[] categories, float[] featureIdfs, int[] featureOffsets, int[] categoryIds, float[] tfIdfs)
/*     */     {
/* 449 */       this.mFeatureExtractor = featureExtractor;
/* 450 */       this.mFeatureSymbolTable = featureSymbolTable;
/* 451 */       this.mCategories = categories;
/* 452 */       this.mFeatureIdfs = featureIdfs;
/* 453 */       this.mFeatureOffsets = featureOffsets;
/* 454 */       this.mCategoryIds = categoryIds;
/* 455 */       this.mTfIdfs = tfIdfs;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 461 */       StringBuilder sb = new StringBuilder();
/* 462 */       sb.append("TfIdfClassifierTrainer.TfIdfClassifier\n");
/* 463 */       sb.append("Feature Symbol Table\n  ");
/* 464 */       sb.append(this.mFeatureSymbolTable.toString());
/* 465 */       sb.append("\n");
/* 466 */       sb.append("Categories\n");
/* 467 */       for (int i = 0; i < this.mCategories.length; i++)
/* 468 */         sb.append("  " + i + "=" + this.mCategories[i] + "\n");
/* 469 */       sb.append("Index  Feature IDF  offset\n");
/* 470 */       for (int i = 0; i < this.mFeatureIdfs.length; i++) {
/* 471 */         sb.append("  " + i + "  " + this.mFeatureSymbolTable.idToSymbol(i) + "   " + this.mFeatureIdfs[i] + "   " + this.mFeatureOffsets[i] + "\n");
/*     */       }
/*     */ 
/* 477 */       sb.append("Index  CategoryID  TF-IDF\n");
/* 478 */       for (int i = 0; i < this.mCategoryIds.length; i++) {
/* 479 */         sb.append("  " + i + "   " + this.mCategoryIds[i] + "    " + this.mTfIdfs[i] + "\n");
/*     */       }
/* 481 */       return sb.toString();
/*     */     }
/*     */ 
/*     */     public ScoredClassification classify(G in) {
/* 485 */       Map featureVector = this.mFeatureExtractor.features(in);
/*     */ 
/* 488 */       double[] scores = new double[this.mCategories.length];
/* 489 */       double inputLengthSquared = 0.0D;
/*     */ 
/* 491 */       for (Map.Entry featureValue : featureVector.entrySet()) {
/* 492 */         String feature = (String)featureValue.getKey();
/* 493 */         int featureId = this.mFeatureSymbolTable.symbolToID(feature);
/* 494 */         if (featureId != -1) {
/* 495 */           double inputTf = TfIdfClassifierTrainer.tf(((Number)featureValue.getValue()).doubleValue());
/* 496 */           double inputIdf = this.mFeatureIdfs[featureId];
/* 497 */           double inputTfIdf = inputTf * inputIdf;
/* 498 */           inputLengthSquared += inputTfIdf * inputTfIdf;
/* 499 */           for (int offset = this.mFeatureOffsets[featureId]; 
/* 500 */             offset < this.mFeatureOffsets[(featureId + 1)]; 
/* 501 */             offset++) {
/* 502 */             int categoryId = this.mCategoryIds[offset];
/* 503 */             double docNormedTfIdf = this.mTfIdfs[offset];
/* 504 */             scores[categoryId] += docNormedTfIdf * inputTfIdf;
/*     */           }
/*     */         }
/*     */       }
/* 508 */       double inputLength = Math.sqrt(inputLengthSquared);
/*     */ 
/* 512 */       ScoredObject[] categoryScores = (ScoredObject[])new ScoredObject[this.mCategories.length];
/*     */ 
/* 514 */       for (int i = 0; i < categoryScores.length; i++) {
/* 515 */         double score = scores[i] / inputLength;
/* 516 */         categoryScores[i] = new ScoredObject(this.mCategories[i], score);
/*     */       }
/* 518 */       return ScoredClassification.create(categoryScores);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Externalizer<F> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 5578122239615646843L;
/*     */     final TfIdfClassifierTrainer<F> mTrainer;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 311 */       this(null);
/*     */     }
/*     */     public Externalizer(TfIdfClassifierTrainer<F> trainer) {
/* 314 */       this.mTrainer = trainer;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException
/*     */     {
/* 319 */       AbstractExternalizable.compileOrSerialize(this.mTrainer.mFeatureExtractor, out);
/*     */ 
/* 323 */       int numFeatures = this.mTrainer.mFeatureSymbolTable.numSymbols();
/* 324 */       out.writeObject(this.mTrainer.mFeatureSymbolTable);
/*     */ 
/* 326 */       int numCats = this.mTrainer.mCategorySymbolTable.numSymbols();
/* 327 */       double numCatsD = numCats;
/*     */ 
/* 329 */       out.writeInt(numCats);
/*     */ 
/* 331 */       for (int i = 0; i < numCats; i++) {
/* 332 */         out.writeUTF(this.mTrainer.mCategorySymbolTable.idToSymbol(i));
/*     */       }
/*     */ 
/* 335 */       for (int i = 0; i < this.mTrainer.mFeatureSymbolTable.numSymbols(); i++) {
/* 336 */         int docFrequency = ((ObjectToDoubleMap)this.mTrainer.mFeatureToCategoryCount.get(Integer.valueOf(i))).size();
/* 337 */         float idf = (float)TfIdfClassifierTrainer.idf(docFrequency, numCatsD);
/* 338 */         out.writeFloat(idf);
/*     */       }
/*     */ 
/* 342 */       int nextFeatureOffset = 0;
/* 343 */       for (int i = 0; i < numFeatures; i++) {
/* 344 */         out.writeInt(nextFeatureOffset);
/* 345 */         int featureSize = ((ObjectToDoubleMap)this.mTrainer.mFeatureToCategoryCount.get(Integer.valueOf(i))).size();
/* 346 */         nextFeatureOffset += featureSize;
/*     */       }
/*     */ 
/* 350 */       out.writeInt(nextFeatureOffset);
/*     */ 
/* 352 */       double[] catLengths = new double[numCats];
/*     */ 
/* 354 */       for (Map.Entry entry : this.mTrainer.mFeatureToCategoryCount.entrySet()) {
/* 355 */         ObjectToDoubleMap categoryCounts = (ObjectToDoubleMap)entry.getValue();
/* 356 */         idf = TfIdfClassifierTrainer.idf(categoryCounts.size(), numCatsD);
/* 357 */         for (Map.Entry categoryCount : categoryCounts.entrySet()) {
/* 358 */           int catId = ((Integer)categoryCount.getKey()).intValue();
/* 359 */           double count = ((Double)categoryCount.getValue()).doubleValue();
/* 360 */           double tfIdf = TfIdfClassifierTrainer.tf(count) * idf;
/* 361 */           catLengths[catId] += tfIdf * tfIdf;
/*     */         }
/*     */       }
/*     */       double idf;
/* 364 */       for (int i = 0; i < catLengths.length; i++)
/* 365 */         catLengths[i] = Math.sqrt(catLengths[i]);
/*     */       double idf;
/* 368 */       for (int featureId = 0; featureId < numFeatures; featureId++) {
/* 369 */         ObjectToDoubleMap categoryCounts = (ObjectToDoubleMap)this.mTrainer.mFeatureToCategoryCount.get(Integer.valueOf(featureId));
/*     */ 
/* 371 */         idf = TfIdfClassifierTrainer.idf(categoryCounts.size(), numCatsD);
/* 372 */         for (Map.Entry categoryCount : categoryCounts.entrySet()) {
/* 373 */           int catId = ((Integer)categoryCount.getKey()).intValue();
/* 374 */           double count = ((Double)categoryCount.getValue()).doubleValue();
/* 375 */           float tfIdf = (float)(TfIdfClassifierTrainer.tf(count) * idf / catLengths[catId]);
/* 376 */           out.writeInt(catId);
/* 377 */           out.writeFloat(tfIdf);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 388 */       FeatureExtractor featureExtractor = (FeatureExtractor)objIn.readObject();
/*     */ 
/* 391 */       MapSymbolTable featureSymbolTable = (MapSymbolTable)objIn.readObject();
/*     */ 
/* 393 */       int numFeatures = featureSymbolTable.numSymbols();
/*     */ 
/* 395 */       int numCategories = objIn.readInt();
/* 396 */       String[] categories = new String[numCategories];
/* 397 */       for (int i = 0; i < numCategories; i++) {
/* 398 */         categories[i] = objIn.readUTF();
/*     */       }
/* 400 */       float[] featureIdfs = new float[featureSymbolTable.numSymbols()];
/* 401 */       for (int i = 0; i < featureIdfs.length; i++) {
/* 402 */         featureIdfs[i] = objIn.readFloat();
/*     */       }
/* 404 */       int[] featureOffsets = new int[numFeatures + 1];
/* 405 */       for (int i = 0; i < numFeatures; i++) {
/* 406 */         featureOffsets[i] = objIn.readInt();
/*     */       }
/* 408 */       int catIdTfIdfArraySize = objIn.readInt();
/* 409 */       featureOffsets[(featureOffsets.length - 1)] = catIdTfIdfArraySize;
/* 410 */       int[] catIds = new int[catIdTfIdfArraySize];
/* 411 */       float[] normedTfIdfs = new float[catIdTfIdfArraySize];
/* 412 */       for (int i = 0; i < catIdTfIdfArraySize; i++) {
/* 413 */         catIds[i] = objIn.readInt();
/* 414 */         normedTfIdfs[i] = objIn.readFloat();
/*     */       }
/*     */ 
/* 417 */       return new TfIdfClassifierTrainer.TfIdfClassifier(featureExtractor, featureSymbolTable, categories, featureIdfs, featureOffsets, catIds, normedTfIdfs);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.TfIdfClassifierTrainer
 * JD-Core Version:    0.6.2
 */