/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT2NodeByte extends PAT2Node
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public PAT2NodeByte(char c1, char c2, long count)
/*     */   {
/* 859 */     super(c1, c2);
/* 860 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 863 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT2NodeByte
 * JD-Core Version:    0.6.2
 */