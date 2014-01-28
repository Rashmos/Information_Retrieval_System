/*     */ package com.aliasi.lm;
/*     */ 
/*     */ abstract class ArrayDtrNode extends AbstractDtrNode
/*     */ {
/*     */   char[] mCs;
/*     */   Node[] mDtrs;
/*     */ 
/*     */   public ArrayDtrNode(char[] cs, Node[] daughters)
/*     */   {
/* 435 */     this.mCs = cs;
/* 436 */     this.mDtrs = daughters;
/*     */   }
/*     */ 
/*     */   char[] chars() {
/* 440 */     return this.mCs;
/*     */   }
/*     */ 
/*     */   Node[] dtrs() {
/* 444 */     return this.mDtrs;
/*     */   }
/*     */   public int numDtrs() {
/* 447 */     return this.mDtrs.length;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ArrayDtrNode
 * JD-Core Version:    0.6.2
 */