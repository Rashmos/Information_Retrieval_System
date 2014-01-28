/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class OneDtrNode extends AbstractDtrNode
/*     */ {
/*     */   char mC;
/*     */   Node mDaughter;
/*     */ 
/*     */   public OneDtrNode(char c, Node daughter)
/*     */   {
/* 326 */     this.mC = c;
/* 327 */     this.mDaughter = daughter;
/*     */   }
/*     */ 
/*     */   public long contextCount() {
/* 331 */     return this.mDaughter.count();
/*     */   }
/*     */ 
/*     */   public Node getDtr(char c) {
/* 335 */     return c == this.mC ? this.mDaughter : null;
/*     */   }
/*     */ 
/*     */   char[] chars() {
/* 339 */     return new char[] { this.mC };
/*     */   }
/*     */ 
/*     */   Node[] dtrs() {
/* 343 */     return new Node[] { this.mDaughter };
/*     */   }
/*     */   public int numDtrs() {
/* 346 */     return 1;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.OneDtrNode
 * JD-Core Version:    0.6.2
 */