/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class TwoDtrNodeShort extends TwoDtrNode
/*      */ {
/*      */   final short mCount;
/*      */ 
/*      */   public TwoDtrNodeShort(char c1, Node dtr1, char c2, Node dtr2, long count)
/*      */   {
/* 1027 */     super(c1, dtr1, c2, dtr2);
/* 1028 */     this.mCount = ((short)(int)count);
/*      */   }
/*      */   public long count() {
/* 1031 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TwoDtrNodeShort
 * JD-Core Version:    0.6.2
 */