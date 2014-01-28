/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import java.util.List;
/*     */ 
/*     */ class DtrMap0
/*     */   implements DtrMap
/*     */ {
/* 285 */   static final DtrMap EMPTY_DTR_MAP = new DtrMap0();
/*     */ 
/*     */   public IntNode getDtr(int tok)
/*     */   {
/* 244 */     return null;
/*     */   }
/*     */   public int numExtensions() {
/* 247 */     return 0;
/*     */   }
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end) {
/* 250 */     if (start == end) return this;
/* 251 */     IntNode dtr = new IntNode(tokIndices, start + 1, end);
/* 252 */     return new DtrMap1(tokIndices[start], dtr);
/*     */   }
/*     */ 
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end, int count) {
/* 256 */     if (start == end) return this;
/* 257 */     IntNode dtr = new IntNode(tokIndices, start + 1, end, count);
/* 258 */     return new DtrMap1(tokIndices[start], dtr);
/*     */   }
/*     */ 
/*     */   public DtrMap incrementSequence(int[] tokIndices, int start, int end, int count) {
/* 262 */     if (start == end) return this;
/* 263 */     IntNode dtr = new IntNode(tokIndices, start + 1, end, count, false);
/* 264 */     return new DtrMap1(tokIndices[start], dtr);
/*     */   }
/*     */   public void toString(StringBuilder sb, int depth, SymbolTable st) {
/*     */   }
/*     */ 
/*     */   public long extensionCount() {
/* 270 */     return 0L;
/*     */   }
/*     */   public int[] integersFollowing() {
/* 273 */     return IntNode.EMPTY_INT_ARRAY;
/*     */   }
/*     */   public DtrMap prune(int minCount) {
/* 276 */     return this;
/*     */   }
/*     */   public DtrMap rescale(double countMultiplier) {
/* 279 */     return this;
/*     */   }
/*     */   public void addDtrs(List<IntNode> queue) {
/*     */   }
/*     */   public int dtrsTrieSize() {
/* 284 */     return 0;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.DtrMap0
 * JD-Core Version:    0.6.2
 */