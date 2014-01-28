/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT4NodeInt extends PAT4Node
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public PAT4NodeInt(char c1, char c2, char c3, char c4, long count)
/*      */   {
/* 1110 */     super(c1, c2, c3, c4);
/* 1111 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1114 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT4NodeInt
 * JD-Core Version:    0.6.2
 */