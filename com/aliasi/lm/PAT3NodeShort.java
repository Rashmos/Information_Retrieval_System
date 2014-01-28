/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT3NodeShort extends PAT3Node
/*     */ {
/*     */   final short mCount;
/*     */ 
/*     */   public PAT3NodeShort(char c1, char c2, char c3, long count)
/*     */   {
/* 975 */     super(c1, c2, c3);
/* 976 */     this.mCount = ((short)(int)count);
/*     */   }
/*     */   public long count() {
/* 979 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT3NodeShort
 * JD-Core Version:    0.6.2
 */