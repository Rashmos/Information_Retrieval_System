/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class TwoDtrNodeInt extends TwoDtrNode
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public TwoDtrNodeInt(char c1, Node dtr1, char c2, Node dtr2, long count)
/*      */   {
/* 1151 */     super(c1, dtr1, c2, dtr2);
/* 1152 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1155 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TwoDtrNodeInt
 * JD-Core Version:    0.6.2
 */