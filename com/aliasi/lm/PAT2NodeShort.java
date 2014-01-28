/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT2NodeShort extends PAT2Node
/*     */ {
/*     */   final short mCount;
/*     */ 
/*     */   public PAT2NodeShort(char c1, char c2, long count)
/*     */   {
/* 965 */     super(c1, c2);
/* 966 */     this.mCount = ((short)(int)count);
/*     */   }
/*     */   public long count() {
/* 969 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT2NodeShort
 * JD-Core Version:    0.6.2
 */