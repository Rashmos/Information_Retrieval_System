/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class TwoDtrNode extends AbstractDtrNode
/*     */ {
/*     */   char mC1;
/*     */   Node mDaughter1;
/*     */   char mC2;
/*     */   Node mDaughter2;
/*     */ 
/*     */   public TwoDtrNode(char c1, Node daughter1, char c2, Node daughter2)
/*     */   {
/* 356 */     this.mC1 = c1;
/* 357 */     this.mDaughter1 = daughter1;
/* 358 */     this.mC2 = c2;
/* 359 */     this.mDaughter2 = daughter2;
/*     */   }
/*     */ 
/*     */   public long contextCount() {
/* 363 */     return this.mDaughter1.count() + this.mDaughter2.count();
/*     */   }
/*     */ 
/*     */   public Node getDtr(char c)
/*     */   {
/* 368 */     return c == this.mC2 ? this.mDaughter2 : c == this.mC1 ? this.mDaughter1 : null;
/*     */   }
/*     */ 
/*     */   char[] chars()
/*     */   {
/* 376 */     return new char[] { this.mC1, this.mC2 };
/*     */   }
/*     */ 
/*     */   Node[] dtrs() {
/* 380 */     return new Node[] { this.mDaughter1, this.mDaughter2 };
/*     */   }
/*     */   public int numDtrs() {
/* 383 */     return 2;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TwoDtrNode
 * JD-Core Version:    0.6.2
 */