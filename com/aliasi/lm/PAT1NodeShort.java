/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class PAT1NodeShort extends PAT1Node
/*     */ {
/*     */   final short mCount;
/*     */ 
/*     */   public PAT1NodeShort(char c, long count)
/*     */   {
/* 955 */     super(c);
/* 956 */     this.mCount = ((short)(int)count);
/*     */   }
/*     */   public long count() {
/* 959 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT1NodeShort
 * JD-Core Version:    0.6.2
 */