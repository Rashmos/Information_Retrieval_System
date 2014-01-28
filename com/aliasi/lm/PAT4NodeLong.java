/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT4NodeLong extends PAT4Node
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public PAT4NodeLong(char c1, char c2, char c3, char c4, long count)
/*      */   {
/* 1216 */     super(c1, c2, c3, c4);
/* 1217 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1220 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT4NodeLong
 * JD-Core Version:    0.6.2
 */