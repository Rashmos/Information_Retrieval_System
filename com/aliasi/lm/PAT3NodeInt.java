/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT3NodeInt extends PAT3Node
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public PAT3NodeInt(char c1, char c2, char c3, long count)
/*      */   {
/* 1099 */     super(c1, c2, c3);
/* 1100 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1103 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT3NodeInt
 * JD-Core Version:    0.6.2
 */