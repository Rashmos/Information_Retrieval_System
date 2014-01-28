/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT3NodeLong extends PAT3Node
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public PAT3NodeLong(char c1, char c2, char c3, long count)
/*      */   {
/* 1205 */     super(c1, c2, c3);
/* 1206 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1209 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT3NodeLong
 * JD-Core Version:    0.6.2
 */