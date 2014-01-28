/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class ThreeDtrNodeInt extends ThreeDtrNode
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public ThreeDtrNodeInt(char c1, Node dtr1, char c2, Node dtr2, char c3, Node dtr3, long count)
/*      */   {
/* 1164 */     super(c1, dtr1, c2, dtr2, c3, dtr3);
/* 1165 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1168 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ThreeDtrNodeInt
 * JD-Core Version:    0.6.2
 */