/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class OneDtrNodeInt extends OneDtrNode
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public OneDtrNodeInt(char c, Node dtr, long count)
/*      */   {
/* 1139 */     super(c, dtr);
/* 1140 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1143 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.OneDtrNodeInt
 * JD-Core Version:    0.6.2
 */