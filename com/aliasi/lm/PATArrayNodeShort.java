/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PATArrayNodeShort extends PATArrayNode
/*      */ {
/*      */   final short mCount;
/*      */ 
/*      */   public PATArrayNodeShort(char[] cs, long count)
/*      */   {
/*  996 */     super(cs);
/*  997 */     this.mCount = ((short)(int)count);
/*      */   }
/*      */   public long count() {
/* 1000 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PATArrayNodeShort
 * JD-Core Version:    0.6.2
 */