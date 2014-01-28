/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT2NodeLong extends PAT2Node
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public PAT2NodeLong(char c1, char c2, long count)
/*      */   {
/* 1195 */     super(c1, c2);
/* 1196 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1199 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT2NodeLong
 * JD-Core Version:    0.6.2
 */