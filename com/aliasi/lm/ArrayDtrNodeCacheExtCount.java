/*      */ package com.aliasi.lm;
/*      */ 
/*      */ abstract class ArrayDtrNodeCacheExtCount extends ArrayDtrNode
/*      */ {
/* 1049 */   long mExtCount = -1L;
/*      */ 
/* 1051 */   public ArrayDtrNodeCacheExtCount(char[] cs, Node[] dtrs) { super(cs, dtrs); }
/*      */ 
/*      */ 
/*      */   public long contextCount()
/*      */   {
/* 1056 */     synchronized (this) {
/* 1057 */       if (this.mExtCount == -1L)
/* 1058 */         this.mExtCount = super.contextCount();
/* 1059 */       return this.mExtCount;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ArrayDtrNodeCacheExtCount
 * JD-Core Version:    0.6.2
 */