/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.corpus.Corpus;
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.features.Features;
/*     */ import com.aliasi.matrix.KernelFunction;
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.symbol.MapSymbolTable;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Arrays;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class PerceptronClassifier<E>
/*     */   implements ScoredClassifier<E>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 8752291174601085455L;
/*     */   final FeatureExtractor<? super E> mFeatureExtractor;
/*     */   final MapSymbolTable mSymbolTable;
/*     */   final KernelFunction mKernelFunction;
/*     */   final Vector[] mBasisVectors;
/*     */   final int[] mBasisWeights;
/*     */   final String mAcceptCategory;
/*     */   final String mRejectCategory;
/* 529 */   static final Vector[] EMPTY_SPARSE_FLOAT_VECTOR_ARRAY = new Vector[0];
/*     */   static final int INITIAL_BASIS_SIZE = 32768;
/*     */ 
/*     */   PerceptronClassifier(FeatureExtractor<? super E> featureExtractor, KernelFunction kernelFunction, MapSymbolTable symbolTable, Vector[] basisVectors, int[] basisWeights, String acceptCategory, String rejectCategory)
/*     */   {
/* 288 */     this.mFeatureExtractor = featureExtractor;
/*     */ 
/* 290 */     this.mKernelFunction = kernelFunction;
/* 291 */     this.mBasisVectors = basisVectors;
/* 292 */     this.mBasisWeights = basisWeights;
/*     */ 
/* 294 */     this.mAcceptCategory = acceptCategory;
/* 295 */     this.mRejectCategory = rejectCategory;
/*     */ 
/* 297 */     this.mSymbolTable = symbolTable;
/*     */   }
/*     */ 
/*     */   public PerceptronClassifier(Corpus<ObjectHandler<Classified<E>>> corpus, FeatureExtractor<? super E> featureExtractor, KernelFunction kernelFunction, String corpusAcceptCategory, int numIterations, String outputAcceptCategory, String outputRejectCategory)
/*     */     throws IOException
/*     */   {
/* 322 */     this.mFeatureExtractor = featureExtractor;
/* 323 */     this.mKernelFunction = kernelFunction;
/* 324 */     this.mAcceptCategory = outputAcceptCategory;
/* 325 */     this.mRejectCategory = outputRejectCategory;
/*     */ 
/* 327 */     this.mSymbolTable = new MapSymbolTable();
/*     */ 
/* 330 */     CorpusCollector collector = new CorpusCollector();
/* 331 */     corpus.visitCorpus(collector);
/* 332 */     Vector[] featureVectors = collector.featureVectors();
/* 333 */     boolean[] polarities = collector.polarities();
/* 334 */     corpus = null;
/*     */ 
/* 337 */     int currentPerceptronIndex = -1;
/* 338 */     int[] weights = new int[32768];
/* 339 */     int[] basisIndexes = new int[32768];
/*     */ 
/* 341 */     for (int iteration = 0; iteration < numIterations; iteration++)
/*     */     {
/* 343 */       for (int i = 0; i < featureVectors.length; i++) {
/* 344 */         double yHat = prediction(featureVectors[i], featureVectors, polarities, weights, basisIndexes, currentPerceptronIndex);
/*     */ 
/* 350 */         boolean accept = yHat > 0.0D;
/*     */ 
/* 354 */         if (accept == polarities[i])
/*     */         {
/* 356 */           if (currentPerceptronIndex >= 0)
/* 357 */             weights[currentPerceptronIndex] += 1;
/*     */         }
/*     */         else {
/* 360 */           currentPerceptronIndex++;
/* 361 */           if (currentPerceptronIndex >= weights.length) {
/* 362 */             weights = Arrays.reallocate(weights);
/* 363 */             basisIndexes = Arrays.reallocate(basisIndexes);
/*     */           }
/* 365 */           basisIndexes[currentPerceptronIndex] = i;
/* 366 */           weights[currentPerceptronIndex] = 1;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 372 */     Map renumbering = new HashMap();
/* 373 */     int next = 0;
/* 374 */     for (int i = 0; i <= currentPerceptronIndex; i++) {
/* 375 */       if (!renumbering.containsKey(Integer.valueOf(basisIndexes[i]))) {
/* 376 */         renumbering.put(Integer.valueOf(basisIndexes[i]), Integer.valueOf(next++));
/*     */       }
/*     */     }
/* 379 */     this.mBasisVectors = new Vector[renumbering.size()];
/* 380 */     this.mBasisWeights = new int[renumbering.size()];
/* 381 */     int weightSum = 0;
/* 382 */     int i = currentPerceptronIndex + 1;
/*     */     while (true) { i--; if (i < 0) break;
/* 383 */       int oldIndex = basisIndexes[i];
/* 384 */       int newIndex = ((Integer)renumbering.get(Integer.valueOf(oldIndex))).intValue();
/* 385 */       this.mBasisVectors[newIndex] = featureVectors[oldIndex];
/* 386 */       weightSum += weights[i];
/* 387 */       if (polarities[i] != 0)
/* 388 */         this.mBasisWeights[newIndex] += weightSum;
/*     */       else
/* 390 */         this.mBasisWeights[newIndex] -= weightSum;
/*     */     }
/*     */   }
/*     */ 
/*     */   public KernelFunction kernelFunction()
/*     */   {
/* 400 */     return this.mKernelFunction;
/*     */   }
/*     */ 
/*     */   public FeatureExtractor<? super E> featureExtractor()
/*     */   {
/* 410 */     return this.mFeatureExtractor;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 421 */     StringBuilder sb = new StringBuilder();
/* 422 */     sb.append("Averaged Perceptron");
/* 423 */     sb.append("  Kernel Function=" + this.mKernelFunction + "\n");
/* 424 */     for (int i = 0; i < this.mBasisVectors.length; i++) {
/* 425 */       sb.append("  idx=" + i + " " + "vec=" + this.mBasisVectors[i] + " wgt=" + this.mBasisWeights[i] + "\n");
/*     */     }
/*     */ 
/* 429 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public ScoredClassification classify(E in)
/*     */   {
/* 445 */     Map featureVector = this.mFeatureExtractor.features(in);
/* 446 */     Vector inputVector = Features.toVector(featureVector, this.mSymbolTable, 2147483647, false);
/* 447 */     double sum = 0.0D;
/* 448 */     int i = this.mBasisVectors.length;
/*     */     while (true) { i--; if (i < 0) break;
/* 449 */       sum += this.mBasisWeights[i] * this.mKernelFunction.proximity(this.mBasisVectors[i], inputVector);
/*     */     }
/* 451 */     return sum > 0.0D ? new ScoredClassification(new String[] { this.mAcceptCategory, this.mRejectCategory }, new double[] { sum, -sum }) : new ScoredClassification(new String[] { this.mRejectCategory, this.mAcceptCategory }, new double[] { -sum, sum });
/*     */   }
/*     */ 
/*     */   double prediction(Vector inputVector, Vector[] featureVectors, boolean[] polarities, int[] ignoreMyWeights, int[] basisIndexes, int currentPerceptronIndex)
/*     */   {
/* 466 */     double sum = 0.0D;
/*     */ 
/* 468 */     int weightSum = 1;
/* 469 */     for (int i = currentPerceptronIndex; i >= 0; i--)
/*     */     {
/* 471 */       int index = basisIndexes[i];
/* 472 */       double kernel = this.mKernelFunction.proximity(inputVector, featureVectors[index]);
/* 473 */       double total = (polarities[i] != 0 ? weightSum : -weightSum) * kernel;
/* 474 */       sum += total;
/*     */     }
/* 476 */     return sum;
/*     */   }
/*     */ 
/*     */   static double power(double base, int exponent) {
/* 480 */     switch (exponent) {
/*     */     case 0:
/* 482 */       return 1.0D;
/*     */     case 1:
/* 484 */       return base;
/*     */     case 2:
/* 486 */       return base * base;
/*     */     case 3:
/* 488 */       return base * base * base;
/*     */     case 4:
/* 490 */       return base * base * base * base;
/*     */     }
/* 492 */     return Math.pow(base, exponent);
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 497 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   static class Externalizer<F> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -1901362811305741506L;
/*     */     final PerceptronClassifier<F> mClassifier;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 537 */       this(null);
/*     */     }
/*     */     public Externalizer(PerceptronClassifier<F> classifier) {
/* 540 */       this.mClassifier = classifier;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 548 */       FeatureExtractor featureExtractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 551 */       KernelFunction kernelFunction = (KernelFunction)in.readObject();
/*     */ 
/* 554 */       MapSymbolTable symbolTable = (MapSymbolTable)in.readObject();
/*     */ 
/* 556 */       int basisLen = in.readInt();
/* 557 */       Vector[] basisVectors = new Vector[basisLen];
/* 558 */       for (int i = 0; i < basisLen; i++) {
/* 559 */         basisVectors[i] = ((Vector)in.readObject());
/*     */       }
/* 561 */       int[] basisWeights = new int[basisLen];
/* 562 */       for (int i = 0; i < basisLen; i++) {
/* 563 */         basisWeights[i] = in.readInt();
/*     */       }
/* 565 */       String acceptCategory = in.readUTF();
/* 566 */       String rejectCategory = in.readUTF();
/*     */ 
/* 568 */       return new PerceptronClassifier(featureExtractor, kernelFunction, symbolTable, basisVectors, basisWeights, acceptCategory, rejectCategory);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out)
/*     */       throws IOException
/*     */     {
/* 579 */       AbstractExternalizable.compileOrSerialize(this.mClassifier.mFeatureExtractor, out);
/* 580 */       AbstractExternalizable.compileOrSerialize(this.mClassifier.mKernelFunction, out);
/*     */ 
/* 583 */       out.writeObject(this.mClassifier.mSymbolTable);
/*     */ 
/* 587 */       out.writeInt(this.mClassifier.mBasisVectors.length);
/*     */ 
/* 590 */       for (int i = 0; i < this.mClassifier.mBasisVectors.length; i++) {
/* 591 */         out.writeObject(this.mClassifier.mBasisVectors[i]);
/*     */       }
/*     */ 
/* 594 */       for (int i = 0; i < this.mClassifier.mBasisWeights.length; i++) {
/* 595 */         out.writeInt(this.mClassifier.mBasisWeights[i]);
/*     */       }
/*     */ 
/* 598 */       out.writeUTF(this.mClassifier.mAcceptCategory);
/* 599 */       out.writeUTF(this.mClassifier.mRejectCategory);
/*     */     }
/*     */   }
/*     */ 
/*     */   class CorpusCollector
/*     */     implements ObjectHandler<Classified<E>>
/*     */   {
/* 504 */     final List<Vector> mInputFeatureVectorList = new ArrayList();
/*     */ 
/* 506 */     final List<Boolean> mInputAcceptList = new ArrayList();
/*     */ 
/*     */     CorpusCollector() {
/*     */     }
/* 510 */     public void handle(Classified<E> classified) { Object object = classified.getObject();
/* 511 */       Classification c = classified.getClassification();
/* 512 */       Map featureMap = PerceptronClassifier.this.mFeatureExtractor.features(object);
/* 513 */       this.mInputFeatureVectorList.add(Features.toVectorAddSymbols(featureMap, PerceptronClassifier.this.mSymbolTable, 2147483647, false));
/* 514 */       this.mInputAcceptList.add(PerceptronClassifier.this.mAcceptCategory.equals(c.bestCategory()) ? Boolean.TRUE : Boolean.FALSE);
/*     */     }
/*     */ 
/*     */     Vector[] featureVectors()
/*     */     {
/* 519 */       return (Vector[])this.mInputFeatureVectorList.toArray(PerceptronClassifier.EMPTY_SPARSE_FLOAT_VECTOR_ARRAY);
/*     */     }
/*     */     boolean[] polarities() {
/* 522 */       boolean[] categories = new boolean[this.mInputAcceptList.size()];
/* 523 */       for (int i = 0; i < categories.length; i++)
/* 524 */         categories[i] = ((Boolean)this.mInputAcceptList.get(i)).booleanValue();
/* 525 */       return categories;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.PerceptronClassifier
 * JD-Core Version:    0.6.2
 */