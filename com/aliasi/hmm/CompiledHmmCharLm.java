/*     */ package com.aliasi.hmm;
/*     */ 
/*     */ import com.aliasi.lm.LanguageModel;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ 
/*     */ class CompiledHmmCharLm extends AbstractHmm
/*     */ {
/*     */   private final double[][] mTransitionProbs;
/*     */   private final double[][] mTransitionLog2Probs;
/*     */   private final double[] mStartProbs;
/*     */   private final double[] mStartLog2Probs;
/*     */   private final double[] mEndProbs;
/*     */   private final double[] mEndLog2Probs;
/*     */   private final LanguageModel[] mEmissionLms;
/*     */ 
/*     */   public CompiledHmmCharLm(ObjectInput in)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/*  24 */     super((SymbolTable)in.readObject());
/*  25 */     int numStates = stateSymbolTable().numSymbols();
/*  26 */     this.mTransitionProbs = new double[numStates][numStates];
/*  27 */     this.mTransitionLog2Probs = new double[numStates][numStates];
/*  28 */     for (int i = 0; i < numStates; i++) {
/*  29 */       for (int j = 0; j < numStates; j++)
/*     */       {
/*     */         double tmp81_76 = in.readDouble(); this.mTransitionProbs[i][j] = tmp81_76; this.mTransitionLog2Probs[i][j] = com.aliasi.util.Math.log2(tmp81_76);
/*     */       }
/*     */     }
/*  33 */     this.mEmissionLms = new LanguageModel[numStates];
/*  34 */     for (int i = 0; i < numStates; i++) {
/*  35 */       this.mEmissionLms[i] = ((LanguageModel)in.readObject());
/*     */     }
/*  37 */     this.mStartProbs = new double[numStates];
/*  38 */     this.mStartLog2Probs = new double[numStates];
/*  39 */     this.mEndProbs = new double[numStates];
/*  40 */     this.mEndLog2Probs = new double[numStates];
/*  41 */     for (int i = 0; i < numStates; i++)
/*     */     {
/*     */       double tmp186_181 = in.readDouble(); this.mStartProbs[i] = tmp186_181; this.mStartLog2Probs[i] = com.aliasi.util.Math.log2(tmp186_181);
/*     */     }
/*  44 */     for (int i = 0; i < numStates; i++)
/*     */     {
/*     */       double tmp221_216 = in.readDouble(); this.mEndProbs[i] = tmp221_216; this.mEndLog2Probs[i] = com.aliasi.util.Math.log2(tmp221_216);
/*     */     }
/*     */   }
/*     */ 
/*     */   public double startProb(String state)
/*     */   {
/*  53 */     int id = stateSymbolTable().symbolToID(state);
/*  54 */     return id < 0 ? 0.0D : startProb(id);
/*     */   }
/*     */ 
/*     */   public double startProb(int stateId)
/*     */   {
/*  59 */     return this.mStartProbs[stateId];
/*     */   }
/*     */ 
/*     */   public double startLog2Prob(int stateId)
/*     */   {
/*  64 */     return this.mStartLog2Probs[stateId];
/*     */   }
/*     */ 
/*     */   public double endProb(String state)
/*     */   {
/*  71 */     int id = stateSymbolTable().symbolToID(state);
/*  72 */     return id < 0 ? 0.0D : endProb(id);
/*     */   }
/*     */ 
/*     */   public double endProb(int stateId)
/*     */   {
/*  77 */     return this.mEndProbs[stateId];
/*     */   }
/*     */ 
/*     */   public double endLog2Prob(int stateId)
/*     */   {
/*  82 */     return this.mEndLog2Probs[stateId];
/*     */   }
/*     */ 
/*     */   public double transitProb(String source, String target)
/*     */   {
/*  89 */     int idSrc = stateSymbolTable().symbolToID(source);
/*  90 */     if (idSrc < 0) return 0.0D;
/*  91 */     int idTarget = stateSymbolTable().symbolToID(target);
/*  92 */     return idTarget < 0 ? 0.0D : transitProb(idSrc, idTarget);
/*     */   }
/*     */ 
/*     */   public double transitProb(int sourceId, int targetId)
/*     */   {
/*  97 */     return this.mTransitionProbs[sourceId][targetId];
/*     */   }
/*     */ 
/*     */   public double transitLog2Prob(int sourceId, int targetId)
/*     */   {
/* 102 */     return this.mTransitionLog2Probs[sourceId][targetId];
/*     */   }
/*     */ 
/*     */   public double emitProb(String state, CharSequence emission)
/*     */   {
/* 107 */     int id = stateSymbolTable().symbolToID(state);
/* 108 */     return id < 0 ? 0.0D : emitProb(id, emission);
/*     */   }
/*     */ 
/*     */   public double emitProb(int stateId, CharSequence emission)
/*     */   {
/* 113 */     return java.lang.Math.pow(2.0D, emitLog2Prob(stateId, emission));
/*     */   }
/*     */ 
/*     */   public double emitLog2Prob(int stateId, CharSequence emission)
/*     */   {
/* 118 */     return this.mEmissionLms[stateId].log2Estimate(emission);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.hmm.CompiledHmmCharLm
 * JD-Core Version:    0.6.2
 */