/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class OneDtrNodeShort extends OneDtrNode
/*      */ {
/*      */   final short mCount;
/*      */ 
/*      */   public OneDtrNodeShort(char c, Node dtr, long count)
/*      */   {
/* 1015 */     super(c, dtr);
/* 1016 */     this.mCount = ((short)(int)count);
/*      */   }
/*      */   public long count() {
/* 1019 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.OneDtrNodeShort
 * JD-Core Version:    0.6.2
 */