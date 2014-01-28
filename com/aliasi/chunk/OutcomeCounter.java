/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTableCompiler;
/*     */ 
/*     */ class OutcomeCounter
/*     */ {
/*     */   private final String mSymbol;
/*     */   private final SymbolTableCompiler mSymbolTable;
/*     */   private int mCount;
/*     */   private float mEstimate;
/*     */ 
/*     */   public OutcomeCounter(String symbol, SymbolTableCompiler symbolTable, int count)
/*     */   {
/*  54 */     this.mSymbol = symbol;
/*  55 */     this.mSymbolTable = symbolTable;
/*  56 */     this.mCount = count;
/*     */   }
/*     */ 
/*     */   public void increment()
/*     */   {
/*  64 */     this.mCount += 1;
/*     */   }
/*     */ 
/*     */   public int count()
/*     */   {
/*  73 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public void addSymbolToTable()
/*     */   {
/*  81 */     if (this.mSymbol != null)
/*  82 */       this.mSymbolTable.addSymbol(this.mSymbol);
/*     */   }
/*     */ 
/*     */   public int getSymbolID()
/*     */   {
/*  93 */     return this.mSymbolTable.symbolToID(this.mSymbol);
/*     */   }
/*     */ 
/*     */   public float estimate()
/*     */   {
/* 102 */     return this.mEstimate;
/*     */   }
/*     */ 
/*     */   public void setEstimate(float estimate)
/*     */   {
/* 112 */     this.mEstimate = estimate;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.OutcomeCounter
 * JD-Core Version:    0.6.2
 */