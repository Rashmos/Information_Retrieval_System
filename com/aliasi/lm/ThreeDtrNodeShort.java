/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class ThreeDtrNodeShort extends ThreeDtrNode
/*      */ {
/*      */   final short mCount;
/*      */ 
/*      */   public ThreeDtrNodeShort(char c1, Node dtr1, char c2, Node dtr2, char c3, Node dtr3, long count)
/*      */   {
/* 1040 */     super(c1, dtr1, c2, dtr2, c3, dtr3);
/* 1041 */     this.mCount = ((short)(int)count);
/*      */   }
/*      */   public long count() {
/* 1044 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ThreeDtrNodeShort
 * JD-Core Version:    0.6.2
 */