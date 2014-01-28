/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class ArrayDtrNodeLong extends ArrayDtrNodeCacheExtCount
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public ArrayDtrNodeLong(char[] cs, Node[] dtrs, long count)
/*      */   {
/* 1280 */     super(cs, dtrs);
/* 1281 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1284 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ArrayDtrNodeLong
 * JD-Core Version:    0.6.2
 */