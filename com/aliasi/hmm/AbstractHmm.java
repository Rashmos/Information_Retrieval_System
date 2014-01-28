/*     */ package com.aliasi.hmm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.util.Math;
/*     */ 
/*     */ public abstract class AbstractHmm
/*     */   implements HiddenMarkovModel
/*     */ {
/*     */   private final SymbolTable mStateSymbolTable;
/*     */ 
/*     */   public AbstractHmm(SymbolTable stateSymbolTable)
/*     */   {
/*  54 */     this.mStateSymbolTable = stateSymbolTable;
/*     */   }
/*     */ 
/*     */   public boolean addState(String state)
/*     */   {
/*  66 */     if (this.mStateSymbolTable.symbolToID(state) >= 0) return false;
/*  67 */     this.mStateSymbolTable.getOrAddSymbol(state);
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   public SymbolTable stateSymbolTable()
/*     */   {
/*  80 */     return this.mStateSymbolTable;
/*     */   }
/*     */ 
/*     */   public abstract double startProb(String paramString);
/*     */ 
/*     */   public double startProb(int stateId)
/*     */   {
/* 102 */     String state = stateSymbolTable().idToSymbol(stateId);
/* 103 */     return startProb(state);
/*     */   }
/*     */ 
/*     */   public double startLog2Prob(String state)
/*     */   {
/* 118 */     return Math.log2(startProb(state));
/*     */   }
/*     */ 
/*     */   public double startLog2Prob(int stateId)
/*     */   {
/* 132 */     return Math.log2(startProb(stateId));
/*     */   }
/*     */ 
/*     */   public abstract double endProb(String paramString);
/*     */ 
/*     */   public double endProb(int stateId)
/*     */   {
/* 154 */     String state = stateSymbolTable().idToSymbol(stateId);
/* 155 */     if (stateId < 0) return 0.0D;
/* 156 */     return endProb(state);
/*     */   }
/*     */ 
/*     */   public double endLog2Prob(String state)
/*     */   {
/* 170 */     return Math.log2(endProb(state));
/*     */   }
/*     */ 
/*     */   public double endLog2Prob(int stateId)
/*     */   {
/* 184 */     return Math.log2(endProb(stateId));
/*     */   }
/*     */ 
/*     */   public abstract double transitProb(String paramString1, String paramString2);
/*     */ 
/*     */   public double transitProb(int sourceId, int targetId)
/*     */   {
/* 213 */     return transitProb(this.mStateSymbolTable.idToSymbol(sourceId), this.mStateSymbolTable.idToSymbol(targetId));
/*     */   }
/*     */ 
/*     */   public double transitLog2Prob(String source, String target)
/*     */   {
/* 231 */     return Math.log2(transitProb(source, target));
/*     */   }
/*     */ 
/*     */   public double transitLog2Prob(int sourceId, int targetId)
/*     */   {
/* 247 */     return Math.log2(transitProb(sourceId, targetId));
/*     */   }
/*     */ 
/*     */   public abstract double emitProb(String paramString, CharSequence paramCharSequence);
/*     */ 
/*     */   public double emitProb(int stateId, CharSequence emission)
/*     */   {
/* 276 */     return emitProb(this.mStateSymbolTable.idToSymbol(stateId), emission);
/*     */   }
/*     */ 
/*     */   public double emitLog2Prob(int stateId, CharSequence emission)
/*     */   {
/* 293 */     return Math.log2(emitProb(stateId, emission));
/*     */   }
/*     */ 
/*     */   public double emitLog2Prob(String state, CharSequence emission)
/*     */   {
/* 310 */     return Math.log2(emitProb(state, emission));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.hmm.AbstractHmm
 * JD-Core Version:    0.6.2
 */