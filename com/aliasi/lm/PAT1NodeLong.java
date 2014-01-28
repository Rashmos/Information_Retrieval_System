/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT1NodeLong extends PAT1Node
/*      */ {
/*      */   final long mCount;
/*      */ 
/*      */   public PAT1NodeLong(char c, long count)
/*      */   {
/* 1185 */     super(c);
/* 1186 */     this.mCount = count;
/*      */   }
/*      */   public long count() {
/* 1189 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT1NodeLong
 * JD-Core Version:    0.6.2
 */