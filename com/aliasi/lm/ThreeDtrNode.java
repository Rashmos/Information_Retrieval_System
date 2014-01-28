/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class ThreeDtrNode extends AbstractDtrNode
/*     */ {
/*     */   char mC1;
/*     */   Node mDaughter1;
/*     */   char mC2;
/*     */   Node mDaughter2;
/*     */   char mC3;
/*     */   Node mDaughter3;
/*     */ 
/*     */   public ThreeDtrNode(char c1, Node daughter1, char c2, Node daughter2, char c3, Node daughter3)
/*     */   {
/* 396 */     this.mC1 = c1;
/* 397 */     this.mDaughter1 = daughter1;
/* 398 */     this.mC2 = c2;
/* 399 */     this.mDaughter2 = daughter2;
/* 400 */     this.mC3 = c3;
/* 401 */     this.mDaughter3 = daughter3;
/*     */   }
/*     */ 
/*     */   public long contextCount() {
/* 405 */     return this.mDaughter1.count() + this.mDaughter2.count() + this.mDaughter3.count();
/*     */   }
/*     */ 
/*     */   public Node getDtr(char c)
/*     */   {
/* 411 */     return c == this.mC3 ? this.mDaughter3 : c == this.mC2 ? this.mDaughter2 : c == this.mC1 ? this.mDaughter1 : null;
/*     */   }
/*     */ 
/*     */   char[] chars()
/*     */   {
/* 421 */     return new char[] { this.mC1, this.mC2, this.mC3 };
/*     */   }
/*     */ 
/*     */   Node[] dtrs() {
/* 425 */     return new Node[] { this.mDaughter1, this.mDaughter2, this.mDaughter3 };
/*     */   }
/*     */   public int numDtrs() {
/* 428 */     return 3;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ThreeDtrNode
 * JD-Core Version:    0.6.2
 */