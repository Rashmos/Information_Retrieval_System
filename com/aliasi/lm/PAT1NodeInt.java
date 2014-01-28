/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class PAT1NodeInt extends PAT1Node
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public PAT1NodeInt(char c, long count)
/*      */   {
/* 1079 */     super(c);
/* 1080 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1083 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PAT1NodeInt
 * JD-Core Version:    0.6.2
 */