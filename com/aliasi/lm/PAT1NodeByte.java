/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT1NodeByte extends PAT1Node
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public PAT1NodeByte(char c, long count)
/*     */   {
/* 849 */     super(c);
/* 850 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 853 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT1NodeByte
 * JD-Core Version:    0.6.2
 */