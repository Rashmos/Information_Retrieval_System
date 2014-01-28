/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import java.util.List;
/*     */ 
/*     */ class DtrMap2
/*     */   implements DtrMap
/*     */ {
/*     */   final int mTok1;
/*     */   final int mTok2;
/*     */   IntNode mDtr1;
/*     */   IntNode mDtr2;
/*     */ 
/*     */   public DtrMap2(int tok1, int tok2, IntNode dtr1, IntNode dtr2)
/*     */   {
/* 384 */     this.mTok1 = tok1;
/* 385 */     this.mDtr1 = dtr1;
/* 386 */     this.mTok2 = tok2;
/* 387 */     this.mDtr2 = dtr2;
/*     */   }
/*     */   public DtrMap prune(int minCount) {
/* 390 */     if (this.mDtr1.count() < minCount) {
/* 391 */       if (this.mDtr2.count() < minCount)
/* 392 */         return DtrMap0.EMPTY_DTR_MAP;
/* 393 */       this.mDtr2.prune(minCount);
/* 394 */       return new DtrMap1(this.mTok2, this.mDtr2);
/*     */     }
/* 396 */     this.mDtr1.prune(minCount);
/* 397 */     if (this.mDtr2.count() < minCount)
/* 398 */       return new DtrMap1(this.mTok1, this.mDtr1);
/* 399 */     this.mDtr2.prune(minCount);
/* 400 */     return this;
/*     */   }
/*     */   public DtrMap rescale(double countMultiplier) {
/* 403 */     this.mDtr1.rescale(countMultiplier);
/* 404 */     this.mDtr2.rescale(countMultiplier);
/* 405 */     if (this.mDtr1.count() == 0) {
/* 406 */       if (this.mDtr2.count() == 0)
/* 407 */         return DtrMap0.EMPTY_DTR_MAP;
/* 408 */       return new DtrMap1(this.mTok2, this.mDtr2);
/*     */     }
/* 410 */     if (this.mDtr2.count() == 0)
/* 411 */       return new DtrMap1(this.mTok1, this.mDtr1);
/* 412 */     return this;
/*     */   }
/*     */   public int numExtensions() {
/* 415 */     return 2;
/*     */   }
/*     */   public void toString(StringBuilder sb, int depth, SymbolTable st) {
/* 418 */     if (st != null)
/* 419 */       sb.append(IntNode.idToSymbol(this.mTok1, st));
/*     */     else
/* 421 */       sb.append(this.mTok1);
/* 422 */     sb.append(": ");
/* 423 */     this.mDtr1.toString(sb, depth + 1, st);
/* 424 */     AbstractNode.indent(sb, depth);
/* 425 */     if (st != null)
/* 426 */       sb.append(IntNode.idToSymbol(this.mTok2, st));
/*     */     else
/* 428 */       sb.append(this.mTok2);
/* 429 */     sb.append(": ");
/* 430 */     this.mDtr2.toString(sb, depth + 1, st);
/*     */   }
/*     */   public void addDtrs(List<IntNode> queue) {
/* 433 */     queue.add(this.mDtr1);
/* 434 */     queue.add(this.mDtr2);
/*     */   }
/*     */   public int dtrsTrieSize() {
/* 437 */     return this.mDtr1.trieSize() + this.mDtr2.trieSize();
/*     */   }
/*     */   public IntNode getDtr(int tok) {
/* 440 */     if (tok == this.mTok1) return this.mDtr1;
/* 441 */     if (tok == this.mTok2) return this.mDtr2;
/* 442 */     return null;
/*     */   }
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end) {
/* 445 */     if (start == end) return this;
/* 446 */     int tok = tokIndices[start];
/* 447 */     if (tok == this.mTok1) {
/* 448 */       this.mDtr1.increment(tokIndices, start + 1, end);
/* 449 */       return this;
/*     */     }
/* 451 */     if (tok == this.mTok2) {
/* 452 */       this.mDtr2.increment(tokIndices, start + 1, end);
/* 453 */       return this;
/*     */     }
/* 455 */     IntNode dtr = new IntNode(tokIndices, start + 1, end);
/* 456 */     return new DtrMapMap(tok, this.mTok1, this.mTok2, dtr, this.mDtr1, this.mDtr2);
/*     */   }
/*     */ 
/*     */   public DtrMap incrementDtrs(int[] tokIndices, int start, int end, int count) {
/* 460 */     if (start == end) return this;
/* 461 */     int tok = tokIndices[start];
/* 462 */     if (tok == this.mTok1) {
/* 463 */       this.mDtr1.increment(tokIndices, start + 1, end, count);
/* 464 */       return this;
/*     */     }
/* 466 */     if (tok == this.mTok2) {
/* 467 */       this.mDtr2.increment(tokIndices, start + 1, end, count);
/* 468 */       return this;
/*     */     }
/* 470 */     IntNode dtr = new IntNode(tokIndices, start + 1, end, count);
/* 471 */     return new DtrMapMap(tok, this.mTok1, this.mTok2, dtr, this.mDtr1, this.mDtr2);
/*     */   }
/*     */ 
/*     */   public DtrMap incrementSequence(int[] tokIndices, int start, int end, int count)
/*     */   {
/* 476 */     if (start == end) return this;
/* 477 */     int tok = tokIndices[start];
/* 478 */     if (tok == this.mTok1) {
/* 479 */       this.mDtr1.incrementSequence(tokIndices, start + 1, end, count);
/* 480 */       return this;
/*     */     }
/* 482 */     if (tok == this.mTok2) {
/* 483 */       this.mDtr2.incrementSequence(tokIndices, start + 1, end, count);
/*     */     }
/* 485 */     IntNode dtr = new IntNode(tokIndices, start + 1, end, count, false);
/* 486 */     return new DtrMapMap(tok, this.mTok1, this.mTok2, dtr, this.mDtr1, this.mDtr2);
/*     */   }
/*     */ 
/*     */   public int[] integersFollowing() {
/* 490 */     return new int[] { this.mTok1, this.mTok2 };
/*     */   }
/*     */   public long extensionCount() {
/* 493 */     return this.mDtr1.count() + this.mDtr2.count();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.DtrMap2
 * JD-Core Version:    0.6.2
 */