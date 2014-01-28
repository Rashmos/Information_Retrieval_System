/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class TwoDtrNodeByte extends TwoDtrNode
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public TwoDtrNodeByte(char c1, Node dtr1, char c2, Node dtr2, long count)
/*     */   {
/* 921 */     super(c1, dtr1, c2, dtr2);
/* 922 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 925 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TwoDtrNodeByte
 * JD-Core Version:    0.6.2
 */