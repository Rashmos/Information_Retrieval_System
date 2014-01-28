/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class ThreeDtrNodeByte extends ThreeDtrNode
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public ThreeDtrNodeByte(char c1, Node dtr1, char c2, Node dtr2, char c3, Node dtr3, long count)
/*     */   {
/* 934 */     super(c1, dtr1, c2, dtr2, c3, dtr3);
/* 935 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 938 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ThreeDtrNodeByte
 * JD-Core Version:    0.6.2
 */