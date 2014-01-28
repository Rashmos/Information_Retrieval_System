/*      */ package com.aliasi.lm;
/*      */ 
/*      */ final class TerminalNodeShort extends TerminalNode
/*      */ {
/*      */   final short mCount;
/*      */ 
/*      */   public TerminalNodeShort(long count)
/*      */   {
/* 1006 */     this.mCount = ((short)(int)count);
/*      */   }
/*      */   public long count() {
/* 1009 */     return this.mCount;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TerminalNodeShort
 * JD-Core Version:    0.6.2
 */