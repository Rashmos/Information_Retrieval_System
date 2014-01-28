/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT4NodeByte extends PAT4Node
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public PAT4NodeByte(char c1, char c2, char c3, char c4, long count)
/*     */   {
/* 880 */     super(c1, c2, c3, c4);
/* 881 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 884 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT4NodeByte
 * JD-Core Version:    0.6.2
 */