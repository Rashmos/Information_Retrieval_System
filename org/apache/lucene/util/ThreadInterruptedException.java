/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ public final class ThreadInterruptedException extends RuntimeException
/*    */ {
/*    */   public ThreadInterruptedException(InterruptedException ie)
/*    */   {
/* 28 */     super(ie);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.ThreadInterruptedException
 * JD-Core Version:    0.6.2
 */