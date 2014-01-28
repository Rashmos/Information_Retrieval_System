/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class ArrayDtrNodeShort extends ArrayDtrNodeCacheExtCount
/*      */ {
/*      */   final short mCount;
/*      */ 
/*      */   public ArrayDtrNodeShort(char[] cs, Node[] dtrs, long count)
/*      */   {
/* 1067 */     super(cs, dtrs);
/* 1068 */     this.mCount = ((short)(int)count);
/*      */   }
/*      */   public long count() {
/* 1071 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ArrayDtrNodeShort
 * JD-Core Version:    0.6.2
 */