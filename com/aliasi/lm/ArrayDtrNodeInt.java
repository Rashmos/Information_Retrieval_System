/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class ArrayDtrNodeInt extends ArrayDtrNodeCacheExtCount
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public ArrayDtrNodeInt(char[] cs, Node[] dtrs, long count)
/*      */   {
/* 1174 */     super(cs, dtrs);
/* 1175 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1178 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ArrayDtrNodeInt
 * JD-Core Version:    0.6.2
 */