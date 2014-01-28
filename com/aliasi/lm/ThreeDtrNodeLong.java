/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class ThreeDtrNodeLong extends ThreeDtrNode
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public ThreeDtrNodeLong(char c1, Node dtr1, char c2, Node dtr2, char c3, Node dtr3, long count)
/*      */   {
/* 1270 */     super(c1, dtr1, c2, dtr2, c3, dtr3);
/* 1271 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1274 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ThreeDtrNodeLong
 * JD-Core Version:    0.6.2
 */