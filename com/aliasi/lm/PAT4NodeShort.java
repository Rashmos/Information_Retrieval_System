/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT4NodeShort extends PAT4Node
/*     */ {
/*     */   final short mCount;
/*     */ 
/*     */   public PAT4NodeShort(char c1, char c2, char c3, char c4, long count)
/*     */   {
/* 986 */     super(c1, c2, c3, c4);
/* 987 */     this.mCount = ((short)(int)count);
/*     */   }
/*     */   public long count() {
/* 990 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT4NodeShort
 * JD-Core Version:    0.6.2
 */