/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT3NodeByte extends PAT3Node
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public PAT3NodeByte(char c1, char c2, char c3, long count)
/*     */   {
/* 869 */     super(c1, c2, c3);
/* 870 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 873 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT3NodeByte
 * JD-Core Version:    0.6.2
 */