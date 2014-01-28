/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class OneDtrNodeByte extends OneDtrNode
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public OneDtrNodeByte(char c, Node dtr, long count)
/*     */   {
/* 909 */     super(c, dtr);
/* 910 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 913 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.OneDtrNodeByte
 * JD-Core Version:    0.6.2
 */