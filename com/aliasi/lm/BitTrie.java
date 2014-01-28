/*    */ package com.aliasi.lm;
/*    */ 
/*    */ import java.util.Stack;
/*    */ 
/*    */ class BitTrie
/*    */ {
/* 27 */   private final Stack<Long> mLastSymbolStack = new Stack();
/*    */ 
/*    */   long popValue()
/*    */   {
/* 35 */     return ((Long)this.mLastSymbolStack.pop()).longValue();
/*    */   }
/*    */ 
/*    */   long pushValue(long n) {
/* 39 */     this.mLastSymbolStack.push(Long.valueOf(n));
/* 40 */     return n;
/*    */   }
/*    */ 
/*    */   static void checkCount(long n) {
/* 44 */     if (n > 0L) return;
/* 45 */     String msg = "All counts must be positive. Found count=" + n;
/*    */ 
/* 47 */     throw new IllegalArgumentException(msg);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.BitTrie
 * JD-Core Version:    0.6.2
 */