/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import java.util.List;
/*     */ 
/*     */ class DtrMap1
/*     */   implements DtrMap
/*     */ {
/*     */   final int mTok;
/* 291 */   IntNode mDtr = new IntNode();
/*     */ 
/* 293 */   public DtrMap1(int tok, IntNode dtr) { this.mTok = tok;
/* 294 */     this.mDtr = dtr; }
/*     */ 
/*     */   public DtrMap prune(int minCount) {
/* 297 */     if (this.mDtr.count() < minCount)
/* 298 */       return DtrMap0.EMPTY_DTR_MAP;
/* 299 */     this.mDtr.prune(minCount);
/* 300 */     return this;
/*     */   }
/*     */   public DtrMap rescale(double countMultiplier) {
/* 303 */     this.mDtr.rescale(countMultiplier);
/* 304 */     if (this.mDtr.count() == 0)
/* 305 */       return DtrMap0.EMPTY_DTR_MAP;
/* 306 */     return this;
/*     */   }
/*     */   public int numExtensions() {
/* 309 */     return 1;
/*     */   }
/*     */   public void toString(StringBuilder sb, int depth, SymbolTable st) {
/* 312 */     if (st != null)
/* 313 */       sb.append(IntNode.idToSymbol(this.mTok, st));
/*     */     else
/* 315 */       sb.append(this.mTok);
/* 316 */     sb.append(": ");
/* 317 */     this.mDtr.toString(sb, depth + 1, st);
/*     */   }
/*     */   public void addDtrs(List<IntNode> queue) {
/* 320 */     queue.add(this.mDtr);
/*     */   }
/* 322 */   public int dtrsTrieSize() { return this.mDtr.trieSize(); } 
/*     */   public IntNode getDtr(int tok) {
/* 324 */     return tok == this.mTok ? this.mDtr : null;
/*     */   }
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end) {
/* 327 */     if (start == end) return this;
/* 328 */     if (tokIndices[start] == this.mTok) {
/* 329 */       this.mDtr.increment(tokIndices, start + 1, end);
/* 330 */       return this;
/*     */     }
/* 332 */     IntNode dtr = new IntNode(tokIndices, start + 1, end);
/* 333 */     if (tokIndices[start] < this.mTok) {
/* 334 */       return new DtrMap2(tokIndices[start], this.mTok, dtr, this.mDtr);
/*     */     }
/* 336 */     return new DtrMap2(this.mTok, tokIndices[start], this.mDtr, dtr);
/*     */   }
/*     */ 
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end, int count)
/*     */   {
/* 341 */     if (start == end) return this;
/* 342 */     if (tokIndices[start] == this.mTok) {
/* 343 */       this.mDtr.increment(tokIndices, start + 1, end, count);
/* 344 */       return this;
/*     */     }
/* 346 */     IntNode dtr = new IntNode(tokIndices, start + 1, end, count);
/* 347 */     if (tokIndices[start] < this.mTok) {
/* 348 */       return new DtrMap2(tokIndices[start], this.mTok, dtr, this.mDtr);
/*     */     }
/* 350 */     return new DtrMap2(this.mTok, tokIndices[start], this.mDtr, dtr);
/*     */   }
/*     */ 
/*     */   public DtrMap incrementSequence(int[] tokIndices, int start, int end, int count)
/*     */   {
/* 355 */     if (start == end) return this;
/* 356 */     if (tokIndices[start] == this.mTok) {
/* 357 */       this.mDtr.incrementSequence(tokIndices, start + 1, end, count);
/* 358 */       return this;
/*     */     }
/* 360 */     IntNode dtr = new IntNode(tokIndices, start + 1, end, count, false);
/* 361 */     if (tokIndices[start] < this.mTok) {
/* 362 */       return new DtrMap2(tokIndices[start], this.mTok, dtr, this.mDtr);
/*     */     }
/* 364 */     return new DtrMap2(this.mTok, tokIndices[start], this.mDtr, dtr);
/*     */   }
/*     */ 
/*     */   public long extensionCount() {
/* 368 */     return this.mDtr.count();
/*     */   }
/*     */   public int[] integersFollowing() {
/* 371 */     return new int[] { this.mTok };
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.DtrMap1
 * JD-Core Version:    0.6.2
 */