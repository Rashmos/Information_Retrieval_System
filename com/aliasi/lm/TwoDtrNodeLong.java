/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class TwoDtrNodeLong extends TwoDtrNode
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public TwoDtrNodeLong(char c1, Node dtr1, char c2, Node dtr2, long count)
/*      */   {
/* 1257 */     super(c1, dtr1, c2, dtr2);
/* 1258 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1261 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TwoDtrNodeLong
 * JD-Core Version:    0.6.2
 */