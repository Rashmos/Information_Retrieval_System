/*     */ package com.aliasi.lm;
/*     */ 
/*     */ final class ArrayDtrNodeByte extends ArrayDtrNode
/*     */ {
/*     */   final byte mCount;
/*     */ 
/*     */   public ArrayDtrNodeByte(char[] cs, Node[] dtrs, long count)
/*     */   {
/* 944 */     super(cs, dtrs);
/* 945 */     this.mCount = ((byte)(int)count);
/*     */   }
/*     */   public long count() {
/* 948 */     return this.mCount;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ArrayDtrNodeByte
 * JD-Core Version:    0.6.2
 */