/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PATArrayNodeByte extends PATArrayNode
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public PATArrayNodeByte(char[] cs, long count)
/*     */   {
/* 890 */     super(cs);
/* 891 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 894 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PATArrayNodeByte
 * JD-Core Version:    0.6.2
 */