/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.corpus.Corpus;
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.features.Features;
/*     */ import com.aliasi.io.Reporter;
/*     */ import com.aliasi.io.Reporters;
/*     */ import com.aliasi.matrix.DenseVector;
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.stats.AnnealingSchedule;
/*     */ import com.aliasi.stats.LogisticRegression;
/*     */ import com.aliasi.stats.RegressionPrior;
/*     */ import com.aliasi.symbol.MapSymbolTable;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.io.CharArrayWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class LogisticRegressionClassifier<E>
/*     */   implements ConditionalClassifier<E>, Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -400005337034204553L;
/*     */   private final LogisticRegression mModel;
/*     */   private final FeatureExtractor<? super E> mFeatureExtractor;
/*     */   private final boolean mAddInterceptFeature;
/*     */   private final SymbolTable mFeatureSymbolTable;
/*     */   private final String[] mCategorySymbols;
/*     */   public static final String INTERCEPT_FEATURE_NAME = "*&^INTERCEPT%$^&**";
/* 737 */   static final Vector[] EMPTY_VECTOR_ARRAY = new Vector[0];
/*     */ 
/*     */   LogisticRegressionClassifier(LogisticRegression model, FeatureExtractor<? super E> featureExtractor, boolean addInterceptFeature, SymbolTable featureSymbolTable, String[] categorySymbols)
/*     */   {
/* 147 */     if (model.numOutcomes() != categorySymbols.length) {
/* 148 */       String msg = "Number of model outcomes must match category symbols length. Found model.numOutcomes()=" + model.numOutcomes() + " categorySymbols.length=" + categorySymbols.length;
/*     */ 
/* 151 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 153 */     Set categorySymbolSet = new HashSet();
/* 154 */     for (int i = 0; i < categorySymbols.length; i++) {
/* 155 */       if (!categorySymbolSet.add(categorySymbols[i])) {
/* 156 */         String msg = "Categories must be unique. Found duplicate category categorySymbols[" + i + "]=" + categorySymbols[i];
/*     */ 
/* 158 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 161 */     this.mModel = model;
/* 162 */     this.mFeatureExtractor = featureExtractor;
/* 163 */     this.mAddInterceptFeature = addInterceptFeature;
/* 164 */     this.mFeatureSymbolTable = featureSymbolTable;
/* 165 */     this.mCategorySymbols = categorySymbols;
/*     */   }
/*     */ 
/*     */   public SymbolTable featureSymbolTable()
/*     */   {
/* 175 */     return MapSymbolTable.unmodifiableView(this.mFeatureSymbolTable);
/*     */   }
/*     */ 
/*     */   public List<String> categorySymbols()
/*     */   {
/* 187 */     return Arrays.asList(this.mCategorySymbols);
/*     */   }
/*     */ 
/*     */   public LogisticRegression model()
/*     */   {
/* 197 */     return this.mModel;
/*     */   }
/*     */ 
/*     */   public boolean addInterceptFeature()
/*     */   {
/* 208 */     return this.mAddInterceptFeature;
/*     */   }
/*     */ 
/*     */   public FeatureExtractor<E> featureExtractor()
/*     */   {
/* 224 */     return new FeatureExtractor() {
/*     */       public Map<String, ? extends Number> features(E in) {
/* 226 */         return LogisticRegressionClassifier.this.mFeatureExtractor.features(in);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public ConditionalClassification classifyVector(Vector v)
/*     */   {
/* 241 */     double[] conditionalProbs = this.mModel.classify(v);
/*     */ 
/* 243 */     ScoredObject[] sos = (ScoredObject[])new ScoredObject[conditionalProbs.length];
/*     */ 
/* 245 */     for (int i = 0; i < conditionalProbs.length; i++) {
/* 246 */       sos[i] = new ScoredObject(this.mCategorySymbols[i], conditionalProbs[i]);
/*     */     }
/* 248 */     Arrays.sort(sos, ScoredObject.reverseComparator());
/* 249 */     String[] categories = new String[conditionalProbs.length];
/* 250 */     for (int i = 0; i < conditionalProbs.length; i++) {
/* 251 */       categories[i] = ((String)sos[i].getObject()).toString();
/* 252 */       conditionalProbs[i] = sos[i].score();
/*     */     }
/* 254 */     return new ConditionalClassification(categories, conditionalProbs);
/*     */   }
/*     */ 
/*     */   public ConditionalClassification classifyFeatures(Map<String, ? extends Number> featureMap)
/*     */   {
/* 269 */     Vector v = Features.toVector(featureMap, this.mFeatureSymbolTable, this.mFeatureSymbolTable.numSymbols(), this.mAddInterceptFeature);
/*     */ 
/* 273 */     return classifyVector(v);
/*     */   }
/*     */ 
/*     */   public ConditionalClassification classify(E in)
/*     */   {
/* 285 */     return classifyFeatures(this.mFeatureExtractor.features(in));
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 303 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   private int categoryToId(String category) {
/* 307 */     for (int i = 0; i < this.mCategorySymbols.length; i++)
/* 308 */       if (this.mCategorySymbols[i].equals(category))
/* 309 */         return i;
/* 310 */     return -1;
/*     */   }
/*     */ 
/*     */   public ObjectToDoubleMap<String> featureValues(String category)
/*     */   {
/* 325 */     int categoryId = categoryToId(category);
/* 326 */     if (categoryId < 0) {
/* 327 */       String msg = "Unknown category=" + category;
/* 328 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 330 */     ObjectToDoubleMap result = new ObjectToDoubleMap();
/* 331 */     if (categoryId == this.mCategorySymbols.length - 1)
/* 332 */       return result;
/* 333 */     int numSymbols = this.mFeatureSymbolTable.numSymbols();
/* 334 */     Vector[] weightVectors = this.mModel.weightVectors();
/* 335 */     Vector weightVector = weightVectors[categoryId];
/* 336 */     for (int i = 0; i < numSymbols; i++) {
/* 337 */       String symbol = this.mFeatureSymbolTable.idToSymbol(i);
/* 338 */       result.set(symbol, weightVector.value(i));
/*     */     }
/* 340 */     return result;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 351 */     CharArrayWriter writer = new CharArrayWriter();
/* 352 */     PrintWriter printWriter = new PrintWriter(writer);
/* 353 */     List categorySymbols = categorySymbols();
/* 354 */     printWriter.println("NUMBER OF CATEGORIES=" + categorySymbols.size());
/* 355 */     printWriter.println("NUMBER OF FEATURES=" + this.mFeatureSymbolTable.numSymbols());
/*     */     ObjectToDoubleMap parameterVector;
/* 356 */     for (int i = 0; i < categorySymbols.size() - 1; i++) {
/* 357 */       String category = (String)categorySymbols.get(i);
/* 358 */       printWriter.println("\n  CATEGORY=" + category);
/* 359 */       parameterVector = featureValues(category);
/* 360 */       for (String feature : parameterVector.keysOrderedByValueList())
/* 361 */         printWriter.printf("%20s %15.6f\n", new Object[] { feature, parameterVector.get(feature) });
/*     */     }
/* 363 */     printWriter.write(10);
/* 364 */     return writer.toString();
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 369 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public static <F> LogisticRegressionClassifier<F> train(Corpus<ObjectHandler<Classified<F>>> corpus, FeatureExtractor<? super F> featureExtractor, int minFeatureCount, boolean addInterceptFeature, RegressionPrior prior, AnnealingSchedule annealingSchedule, double minImprovement, int minEpochs, int maxEpochs, Reporter reporter)
/*     */     throws IOException
/*     */   {
/* 423 */     int priorBlockSize = -1;
/* 424 */     return train(corpus, featureExtractor, minFeatureCount, addInterceptFeature, prior, priorBlockSize, null, annealingSchedule, minImprovement, 5, minEpochs, maxEpochs, null, reporter);
/*     */   }
/*     */ 
/*     */   public static <F> LogisticRegressionClassifier<F> train(Corpus<ObjectHandler<Classified<F>>> corpus, FeatureExtractor<? super F> featureExtractor, int minFeatureCount, boolean addInterceptFeature, RegressionPrior prior, int priorBlockSize, LogisticRegressionClassifier<F> hotStart, AnnealingSchedule annealingSchedule, double minImprovement, int rollingAverageSize, int minEpochs, int maxEpochs, ObjectHandler<LogisticRegressionClassifier<F>> classifierHandler, Reporter reporter)
/*     */     throws IOException
/*     */   {
/* 494 */     MapSymbolTable featureSymbolTable = new MapSymbolTable();
/* 495 */     MapSymbolTable categorySymbolTable = new MapSymbolTable();
/*     */ 
/* 497 */     if (reporter == null) {
/* 498 */       reporter = Reporters.silent();
/*     */     }
/* 500 */     if (addInterceptFeature) {
/* 501 */       featureSymbolTable.getOrAddSymbol("*&^INTERCEPT%$^&**");
/*     */     }
/* 503 */     reporter.info("Feature Extractor class=" + featureExtractor.getClass());
/* 504 */     reporter.info("min feature count=" + minFeatureCount);
/*     */ 
/* 506 */     reporter.info("Extracting Training Data");
/* 507 */     reporter.debug("  Counting features");
/* 508 */     ObjectToCounterMap featureCounter = new ObjectToCounterMap();
/* 509 */     corpus.visitTrain(new FeatureCounter(featureExtractor, featureCounter));
/*     */ 
/* 511 */     reporter.debug("  Pruning features");
/* 512 */     featureCounter.prune(minFeatureCount);
/* 513 */     for (String feature : featureCounter.keySet()) {
/* 514 */       featureSymbolTable.getOrAddSymbol(feature);
/*     */     }
/* 516 */     reporter.debug("  Extracting vectors");
/* 517 */     DataExtractor dataExtractor = new DataExtractor(featureExtractor, featureSymbolTable, categorySymbolTable, addInterceptFeature, featureSymbolTable.numSymbols());
/*     */ 
/* 523 */     corpus.visitTrain(dataExtractor);
/* 524 */     Vector[] inputs = dataExtractor.inputs();
/* 525 */     int[] categories = dataExtractor.categories();
/*     */ 
/* 527 */     int numInputDimensions = inputs[0].numDimensions();
/*     */ 
/* 529 */     String[] categorySymbols = new String[categorySymbolTable.numSymbols()];
/* 530 */     for (int i = 0; i < categorySymbols.length; i++) {
/* 531 */       categorySymbols[i] = categorySymbolTable.idToSymbol(i);
/*     */     }
/*     */ 
/* 534 */     LogisticRegression lrHotStart = null;
/* 535 */     if (hotStart != null) {
/* 536 */       reporter.debug("hot starting");
/*     */ 
/* 539 */       Set hotStartCategorySet = new HashSet(hotStart.categorySymbols());
/*     */ 
/* 541 */       Vector[] weightVectors = new Vector[categorySymbols.length - 1];
/* 542 */       for (int k = 0; k < weightVectors.length; k++) {
/* 543 */         weightVectors[k] = new DenseVector(numInputDimensions);
/*     */       }
/* 545 */       for (int k = 0; k < weightVectors.length - 1; k++) {
/* 546 */         String category = categorySymbols[k];
/* 547 */         if (hotStartCategorySet.contains(category))
/*     */         {
/* 549 */           ObjectToDoubleMap featureVector = hotStart.featureValues(category);
/*     */ 
/* 551 */           for (int i = 0; i < numInputDimensions; i++) {
/* 552 */             String feature = featureSymbolTable.idToSymbol(i);
/* 553 */             double value = featureVector.getValue(feature);
/* 554 */             weightVectors[k].setValue(i, value);
/*     */           }
/*     */         }
/*     */       }
/* 557 */       lrHotStart = new LogisticRegression(weightVectors);
/*     */     }
/* 559 */     reporter.info(hotStart != null ? "Hot start" : "Cold start");
/*     */ 
/* 561 */     ObjectHandler regressionHandler = classifierHandler == null ? null : new RegressionHandlerAdapter(classifierHandler, featureExtractor, addInterceptFeature, featureSymbolTable, categorySymbols);
/*     */ 
/* 569 */     reporter.info("Regression callback handler=" + null);
/*     */ 
/* 574 */     if (priorBlockSize == -1) {
/* 575 */       priorBlockSize = Math.max(1, categories.length / 50);
/*     */     }
/* 577 */     LogisticRegression model = LogisticRegression.estimate(inputs, categories, prior, priorBlockSize, lrHotStart, annealingSchedule, minImprovement, rollingAverageSize, minEpochs, maxEpochs, regressionHandler, reporter);
/*     */ 
/* 591 */     return new LogisticRegressionClassifier(model, featureExtractor, addInterceptFeature, featureSymbolTable, categorySymbols);
/*     */   }
/*     */ 
/*     */   static class DataExtractor<F>
/*     */     implements ObjectHandler<Classified<F>>
/*     */   {
/*     */     final FeatureExtractor<? super F> mFeatureExtractor;
/*     */     final SymbolTable mFeatureSymbolTable;
/*     */     final SymbolTable mCategorySymbolTable;
/*     */     final boolean mAddInterceptFeature;
/*     */     final int mNumSymbols;
/* 694 */     final List<Vector> mInputVectorList = new ArrayList();
/* 695 */     final List<Integer> mOutputCategoryList = new ArrayList();
/*     */ 
/*     */     DataExtractor(FeatureExtractor<? super F> featureExtractor, SymbolTable featureSymbolTable, SymbolTable categorySymbolTable, boolean addInterceptFeature, int numSymbols)
/*     */     {
/* 703 */       this.mFeatureExtractor = featureExtractor;
/* 704 */       this.mFeatureSymbolTable = featureSymbolTable;
/* 705 */       this.mCategorySymbolTable = categorySymbolTable;
/* 706 */       this.mAddInterceptFeature = addInterceptFeature;
/* 707 */       this.mNumSymbols = numSymbols;
/*     */     }
/*     */     public void handle(Classified<F> classified) {
/* 710 */       Object input = classified.getObject();
/* 711 */       Classification output = classified.getClassification();
/* 712 */       String outputCategoryName = output.bestCategory();
/* 713 */       Integer outputCategoryId = Integer.valueOf(this.mCategorySymbolTable.getOrAddSymbol(outputCategoryName));
/* 714 */       Map featureMap = this.mFeatureExtractor.features(input);
/* 715 */       Vector vector = Features.toVector(featureMap, this.mFeatureSymbolTable, this.mNumSymbols, this.mAddInterceptFeature);
/*     */ 
/* 721 */       this.mInputVectorList.add(vector);
/* 722 */       this.mOutputCategoryList.add(outputCategoryId);
/*     */     }
/*     */     int[] categories() {
/* 725 */       int[] inputs = new int[this.mOutputCategoryList.size()];
/* 726 */       for (int i = 0; i < inputs.length; i++)
/* 727 */         inputs[i] = ((Integer)this.mOutputCategoryList.get(i)).intValue();
/* 728 */       return inputs;
/*     */     }
/*     */     Vector[] inputs() {
/* 731 */       return (Vector[])this.mInputVectorList.toArray(LogisticRegressionClassifier.EMPTY_VECTOR_ARRAY);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Externalizer<G> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -2003123148721825458L;
/*     */     final LogisticRegressionClassifier<G> mClassifier;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 648 */       this(null);
/*     */     }
/*     */     public Externalizer(LogisticRegressionClassifier<G> classifier) {
/* 651 */       this.mClassifier = classifier;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 655 */       objOut.writeObject(this.mClassifier.mModel);
/* 656 */       objOut.writeObject(this.mClassifier.mFeatureExtractor);
/* 657 */       objOut.writeBoolean(this.mClassifier.mAddInterceptFeature);
/* 658 */       objOut.writeObject(this.mClassifier.mFeatureSymbolTable);
/* 659 */       objOut.writeInt(this.mClassifier.mCategorySymbols.length);
/* 660 */       for (int i = 0; i < this.mClassifier.mCategorySymbols.length; i++)
/* 661 */         objOut.writeUTF(this.mClassifier.mCategorySymbols[i]);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws IOException, ClassNotFoundException
/*     */     {
/* 666 */       LogisticRegression model = (LogisticRegression)objIn.readObject();
/*     */ 
/* 670 */       FeatureExtractor featureExtractor = (FeatureExtractor)objIn.readObject();
/*     */ 
/* 672 */       boolean addInterceptFeature = objIn.readBoolean();
/* 673 */       SymbolTable featureSymbolTable = (SymbolTable)objIn.readObject();
/* 674 */       int numSymbols = objIn.readInt();
/* 675 */       String[] categorySymbols = new String[numSymbols];
/* 676 */       for (int i = 0; i < categorySymbols.length; i++)
/* 677 */         categorySymbols[i] = objIn.readUTF();
/* 678 */       return new LogisticRegressionClassifier(model, featureExtractor, addInterceptFeature, featureSymbolTable, categorySymbols);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class FeatureCounter<H>
/*     */     implements ObjectHandler<Classified<H>>
/*     */   {
/*     */     private final FeatureExtractor<? super H> mFeatureExtractor;
/*     */     private final ObjectToCounterMap<String> mFeatureCounter;
/*     */ 
/*     */     FeatureCounter(FeatureExtractor<? super H> featureExtractor, ObjectToCounterMap<String> featureCounter)
/*     */     {
/* 632 */       this.mFeatureExtractor = featureExtractor;
/* 633 */       this.mFeatureCounter = featureCounter;
/*     */     }
/*     */     public void handle(Classified<H> classified) {
/* 636 */       Object h = classified.getObject();
/* 637 */       Map featureMap = this.mFeatureExtractor.features(h);
/* 638 */       for (String feature : featureMap.keySet())
/* 639 */         this.mFeatureCounter.increment(feature);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class RegressionHandlerAdapter<F>
/*     */     implements ObjectHandler<LogisticRegression>
/*     */   {
/*     */     private final ObjectHandler<LogisticRegressionClassifier<F>> mClassifierHandler;
/*     */     private final FeatureExtractor<? super F> mFeatureExtractor;
/*     */     private final boolean mAddInterceptFeature;
/*     */     private final SymbolTable mFeatureSymbolTable;
/*     */     private final String[] mCategorySymbols;
/*     */ 
/*     */     public RegressionHandlerAdapter(ObjectHandler<LogisticRegressionClassifier<F>> handler, FeatureExtractor<? super F> featureExtractor, boolean addInterceptFeature, SymbolTable featureSymbolTable, String[] categorySymbols)
/*     */     {
/* 611 */       this.mClassifierHandler = handler;
/* 612 */       this.mFeatureExtractor = featureExtractor;
/* 613 */       this.mAddInterceptFeature = addInterceptFeature;
/* 614 */       this.mFeatureSymbolTable = featureSymbolTable;
/* 615 */       this.mCategorySymbols = categorySymbols;
/*     */     }
/*     */     public void handle(LogisticRegression regressionModel) {
/* 618 */       this.mClassifierHandler.handle(new LogisticRegressionClassifier(regressionModel, this.mFeatureExtractor, this.mAddInterceptFeature, this.mFeatureSymbolTable, this.mCategorySymbols));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.LogisticRegressionClassifier
 * JD-Core Version:    0.6.2
 */