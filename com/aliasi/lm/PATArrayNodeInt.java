/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PATArrayNodeInt extends PATArrayNode
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public PATArrayNodeInt(char[] cs, long count)
/*      */   {
/* 1120 */     super(cs);
/* 1121 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1124 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PATArrayNodeInt
 * JD-Core Version:    0.6.2
 */