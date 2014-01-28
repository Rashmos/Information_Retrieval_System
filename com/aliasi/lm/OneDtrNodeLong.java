/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class OneDtrNodeLong extends OneDtrNode
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public OneDtrNodeLong(char c, Node dtr, long count)
/*      */   {
/* 1245 */     super(c, dtr);
/* 1246 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1249 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.OneDtrNodeLong
 * JD-Core Version:    0.6.2
 */