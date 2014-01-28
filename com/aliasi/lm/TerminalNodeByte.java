/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class TerminalNodeByte extends TerminalNode
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public TerminalNodeByte(long count)
/*     */   {
/* 900 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 903 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TerminalNodeByte
 * JD-Core Version:    0.6.2
 */