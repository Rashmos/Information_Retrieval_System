/*     */ package com.aliasi.hmm;
/*     */ 
/*     */ import com.aliasi.lm.NGramBoundaryLM;
/*     */ import com.aliasi.symbol.MapSymbolTable;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Exceptions;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Tuple;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class HmmCharLmEstimator extends AbstractHmmEstimator
/*     */ {
/*     */   private final MapSymbolTable mStateMapSymbolTable;
/*  85 */   private final ObjectToCounterMap<String> mStateExtensionCounter = new ObjectToCounterMap();
/*     */ 
/*  88 */   private final ObjectToCounterMap<Tuple<String>> mStatePairCounter = new ObjectToCounterMap();
/*     */ 
/*  91 */   private final Map<String, NGramBoundaryLM> mStateToLm = new HashMap();
/*     */   private final double mCharLmInterpolation;
/*     */   private final int mCharLmMaxNGram;
/*     */   private final int mMaxCharacters;
/*  97 */   private int mNumStarts = 0;
/*  98 */   private final ObjectToCounterMap<String> mStartCounter = new ObjectToCounterMap();
/*     */ 
/* 101 */   private int mNumEnds = 0;
/* 102 */   private final ObjectToCounterMap<String> mEndCounter = new ObjectToCounterMap();
/*     */   private final boolean mSmootheStates;
/* 106 */   final Set<String> mStateSet = new HashSet();
/*     */ 
/*     */   public HmmCharLmEstimator()
/*     */   {
/* 118 */     this(6, 65534, 6.0D);
/*     */   }
/*     */ 
/*     */   public HmmCharLmEstimator(int charLmMaxNGram, int maxCharacters, double charLmInterpolation)
/*     */   {
/* 142 */     this(charLmMaxNGram, maxCharacters, charLmInterpolation, false);
/*     */   }
/*     */ 
/*     */   public HmmCharLmEstimator(int charLmMaxNGram, int maxCharacters, double charLmInterpolation, boolean smootheStates)
/*     */   {
/* 172 */     super(new MapSymbolTable());
/* 173 */     this.mSmootheStates = smootheStates;
/* 174 */     if (charLmMaxNGram < 1) {
/* 175 */       String msg = "Max n-gram must be greater than 0. Found charLmMaxNGram=" + charLmMaxNGram;
/*     */ 
/* 177 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 179 */     if ((maxCharacters < 1) || (maxCharacters > 65534)) {
/* 180 */       String msg = "Require between 1 and 65534 max characters. Found maxCharacters=" + maxCharacters;
/*     */ 
/* 183 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 185 */     if (charLmInterpolation < 0.0D) {
/* 186 */       String msg = "Char interpolation param must be between  0.0 and 1.0 inclusive. Found charLmInterpolation=" + charLmInterpolation;
/*     */ 
/* 189 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 191 */     this.mStateMapSymbolTable = ((MapSymbolTable)stateSymbolTable());
/* 192 */     this.mCharLmInterpolation = charLmInterpolation;
/* 193 */     this.mCharLmMaxNGram = charLmMaxNGram;
/* 194 */     this.mMaxCharacters = maxCharacters;
/*     */   }
/*     */ 
/*     */   void addStateSmoothe(String state) {
/* 198 */     if (!this.mStateSet.add(state)) return;
/* 199 */     this.mStateMapSymbolTable.getOrAddSymbol(state);
/* 200 */     if (!this.mSmootheStates) return;
/* 201 */     trainStart(state);
/* 202 */     trainEnd(state);
/* 203 */     for (String state2 : this.mStateSet) {
/* 204 */       trainTransit(state, state2);
/* 205 */       if (!state.equals(state2))
/* 206 */         trainTransit(state2, state);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void trainStart(String state)
/*     */   {
/* 213 */     if (state == null) return;
/* 214 */     addStateSmoothe(state);
/* 215 */     this.mNumStarts += 1;
/* 216 */     this.mStartCounter.increment(state);
/*     */   }
/*     */ 
/*     */   static void verifyNonNegativeCount(int count) {
/* 220 */     if (count >= 0) return;
/* 221 */     String msg = "Counts must be positve. Found count=" + count;
/*     */ 
/* 223 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   public void trainEnd(String state)
/*     */   {
/* 229 */     if (state == null) return;
/* 230 */     addStateSmoothe(state);
/* 231 */     this.mStateExtensionCounter.increment(state);
/* 232 */     this.mNumEnds += 1;
/* 233 */     this.mEndCounter.increment(state);
/*     */   }
/*     */ 
/*     */   public void trainEmit(String state, CharSequence emission)
/*     */   {
/* 238 */     if (state == null) return;
/* 239 */     if (emission == null) return;
/* 240 */     addStateSmoothe(state);
/* 241 */     emissionLm(state).train(emission);
/*     */   }
/*     */ 
/*     */   public void trainTransit(String sourceState, String targetState)
/*     */   {
/* 246 */     if ((sourceState == null) || (targetState == null)) return;
/* 247 */     addStateSmoothe(sourceState);
/* 248 */     addStateSmoothe(targetState);
/* 249 */     this.mStateExtensionCounter.increment(sourceState);
/* 250 */     this.mStatePairCounter.increment(Tuple.create(sourceState, targetState));
/*     */   }
/*     */ 
/*     */   public double startProb(String state)
/*     */   {
/* 256 */     double count = this.mStartCounter.getCount(state);
/* 257 */     double total = this.mNumStarts;
/* 258 */     return count / total;
/*     */   }
/*     */ 
/*     */   public double endProb(String state)
/*     */   {
/* 263 */     double count = this.mEndCounter.getCount(state);
/* 264 */     double total = this.mNumEnds;
/* 265 */     return count / total;
/*     */   }
/*     */ 
/*     */   public double transitProb(String source, String target)
/*     */   {
/* 285 */     double extCount = this.mStateExtensionCounter.getCount(source);
/* 286 */     double pairCount = this.mStatePairCounter.getCount(Tuple.create(source, target));
/*     */ 
/* 288 */     return pairCount / extCount;
/*     */   }
/*     */ 
/*     */   public double emitProb(String state, CharSequence emission)
/*     */   {
/* 304 */     return Math.pow(2.0D, emitLog2Prob(state, emission));
/*     */   }
/*     */ 
/*     */   public double emitLog2Prob(String state, CharSequence emission)
/*     */   {
/* 309 */     return emissionLm(state).log2Estimate(emission);
/*     */   }
/*     */ 
/*     */   public NGramBoundaryLM emissionLm(String state)
/*     */   {
/* 321 */     NGramBoundaryLM lm = (NGramBoundaryLM)this.mStateToLm.get(state);
/* 322 */     if (lm == null) {
/* 323 */       lm = new NGramBoundaryLM(this.mCharLmMaxNGram, this.mMaxCharacters, this.mCharLmInterpolation, 65535);
/*     */ 
/* 327 */       this.mStateToLm.put(state, lm);
/*     */     }
/* 329 */     return lm;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut) throws IOException
/*     */   {
/* 334 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     private static final long serialVersionUID = 8463739963673120677L;
/*     */     final HmmCharLmEstimator mEstimator;
/*     */ 
/* 341 */     public Externalizer() { this(null); }
/*     */ 
/*     */     public Externalizer(HmmCharLmEstimator handler) {
/* 344 */       this.mEstimator = handler;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/*     */       try {
/* 349 */         return new CompiledHmmCharLm(in);
/*     */       } catch (ClassNotFoundException e) {
/* 351 */         throw Exceptions.toIO("HmmCharLmEstimator.compileTo()", e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException
/*     */     {
/* 357 */       objOut.writeObject(this.mEstimator.mStateMapSymbolTable);
/*     */ 
/* 359 */       int numStates = this.mEstimator.mStateMapSymbolTable.numSymbols();
/*     */ 
/* 362 */       for (int i = 0; i < numStates; i++) {
/* 363 */         for (int j = 0; j < numStates; j++) {
/* 364 */           objOut.writeDouble((float)this.mEstimator.transitProb(i, j));
/*     */         }
/*     */       }
/* 367 */       for (int i = 0; i < numStates; i++) {
/* 368 */         String state = this.mEstimator.mStateMapSymbolTable.idToSymbol(i);
/* 369 */         this.mEstimator.emissionLm(state).compileTo(objOut);
/*     */       }
/*     */ 
/* 373 */       for (int i = 0; i < numStates; i++) {
/* 374 */         objOut.writeDouble(this.mEstimator.startProb(i));
/*     */       }
/*     */ 
/* 377 */       for (int i = 0; i < numStates; i++)
/* 378 */         objOut.writeDouble(this.mEstimator.endProb(i));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.hmm.HmmCharLmEstimator
 * JD-Core Version:    0.6.2
 */