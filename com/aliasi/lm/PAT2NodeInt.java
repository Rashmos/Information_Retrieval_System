/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT2NodeInt extends PAT2Node
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public PAT2NodeInt(char c1, char c2, long count)
/*      */   {
/* 1089 */     super(c1, c2);
/* 1090 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1093 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT2NodeInt
 * JD-Core Version:    0.6.2
 */