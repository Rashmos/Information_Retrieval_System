/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PATArrayNodeLong extends PATArrayNode
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public PATArrayNodeLong(char[] cs, long count)
/*      */   {
/* 1226 */     super(cs);
/* 1227 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1230 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PATArrayNodeLong
 * JD-Core Version:    0.6.2
 */