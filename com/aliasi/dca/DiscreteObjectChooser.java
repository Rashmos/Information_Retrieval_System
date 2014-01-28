/*     */ package com.aliasi.dca;
/*     */ 
/*     */ import com.aliasi.features.Features;
/*     */ import com.aliasi.io.Reporter;
/*     */ import com.aliasi.io.Reporters;
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.stats.AnnealingSchedule;
/*     */ import com.aliasi.stats.RegressionPrior;
/*     */ import com.aliasi.symbol.MapSymbolTable;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class DiscreteObjectChooser<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3178131664571660923L;
/*     */   private final FeatureExtractor<E> mFeatureExtractor;
/*     */   private final SymbolTable mFeatureSymbolTable;
/*     */   private final DiscreteChooser mChooser;
/*     */   static final boolean ADD_INTERCEPT_FALSE = false;
/*     */ 
/*     */   public DiscreteObjectChooser(FeatureExtractor<E> featureExtractor, SymbolTable featureSymbolTable, DiscreteChooser chooser)
/*     */   {
/*  89 */     this.mFeatureExtractor = featureExtractor;
/*  90 */     this.mFeatureSymbolTable = featureSymbolTable;
/*  91 */     this.mChooser = chooser;
/*     */   }
/*     */ 
/*     */   public FeatureExtractor<E> featureExtractor()
/*     */   {
/* 100 */     return this.mFeatureExtractor;
/*     */   }
/*     */ 
/*     */   public SymbolTable featureSymbolTable()
/*     */   {
/* 110 */     return MapSymbolTable.unmodifiableView(this.mFeatureSymbolTable);
/*     */   }
/*     */ 
/*     */   public DiscreteChooser chooser()
/*     */   {
/* 118 */     return this.mChooser;
/*     */   }
/*     */ 
/*     */   public static <F> DiscreteObjectChooser<F> estimate(FeatureExtractor<F> featureExtractor, List<List<F>> alternativeObjectss, int[] choices, int minFeatureCount, RegressionPrior prior, int priorBlockSize, AnnealingSchedule annealingSchedule, double minImprovement, int minEpochs, int maxEpochs, Reporter reporter)
/*     */   {
/* 140 */     if (reporter == null)
/* 141 */       reporter = Reporters.silent();
/* 142 */     ObjectToCounterMap featureCounter = new ObjectToCounterMap();
/*     */ 
/* 144 */     for (List alternativeObjects : alternativeObjectss)
/* 145 */       for (i$ = alternativeObjects.iterator(); i$.hasNext(); ) { Object alternativeObject = i$.next();
/* 146 */         Map featureMap = featureExtractor.features(alternativeObject);
/*     */ 
/* 148 */         for (String feature : featureMap.keySet())
/* 149 */           featureCounter.increment(feature);
/*     */       }
/*     */     Iterator i$;
/* 152 */     featureCounter.prune(minFeatureCount);
/* 153 */     MapSymbolTable featureSymbolTable = new MapSymbolTable();
/* 154 */     for (String feature : featureCounter.keySet())
/* 155 */       featureSymbolTable.getOrAddSymbol(feature);
/* 156 */     int numDimensions = featureSymbolTable.numSymbols();
/*     */ 
/* 158 */     Vector[][] alternativess = new Vector[alternativeObjectss.size()][];
/* 159 */     for (int i = 0; i < alternativess.length; i++) {
/* 160 */       List alternativeObjects = (List)alternativeObjectss.get(i);
/* 161 */       alternativess[i] = new Vector[alternativeObjects.size()];
/* 162 */       for (int k = 0; k < alternativess[i].length; k++) {
/* 163 */         Map featureMap = featureExtractor.features(alternativeObjects.get(k));
/*     */ 
/* 165 */         alternativess[i][k] = Features.toVectorAddSymbols(featureMap, featureSymbolTable, numDimensions, false);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 170 */     DiscreteChooser chooser = DiscreteChooser.estimate(alternativess, choices, prior, priorBlockSize, annealingSchedule, minImprovement, minEpochs, maxEpochs, reporter);
/*     */ 
/* 180 */     return new DiscreteObjectChooser(featureExtractor, featureSymbolTable, chooser);
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 186 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 4420046415835317661L;
/*     */     final DiscreteObjectChooser<F> mObjectChooser;
/*     */ 
/*     */     public Serializer() {
/* 195 */       this(null);
/*     */     }
/*     */     public Serializer(DiscreteObjectChooser<F> objectChooser) {
/* 198 */       this.mObjectChooser = objectChooser;
/*     */     }
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 201 */       out.writeObject(this.mObjectChooser.mFeatureExtractor);
/* 202 */       out.writeObject(this.mObjectChooser.mFeatureSymbolTable);
/* 203 */       out.writeObject(this.mObjectChooser.mChooser);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 207 */       FeatureExtractor featureExtractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 210 */       SymbolTable featureSymbolTable = (SymbolTable)in.readObject();
/*     */ 
/* 213 */       DiscreteChooser chooser = (DiscreteChooser)in.readObject();
/*     */ 
/* 215 */       return new DiscreteObjectChooser(featureExtractor, featureSymbolTable, chooser);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dca.DiscreteObjectChooser
 * JD-Core Version:    0.6.2
 */