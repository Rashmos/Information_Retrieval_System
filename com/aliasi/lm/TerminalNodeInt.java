/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class TerminalNodeInt extends TerminalNode
/*      */ {
/*      */   final int mCount;
/*      */ 
/*      */   public TerminalNodeInt(long count)
/*      */   {
/* 1130 */     this.mCount = ((int)count);
/*      */   }
/*      */   public long count() {
/* 1133 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TerminalNodeInt
 * JD-Core Version:    0.6.2
 */