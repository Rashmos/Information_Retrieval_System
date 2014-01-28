/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ 
/*    */ public final class DummyConcurrentLock
/*    */   implements Lock
/*    */ {
/* 31 */   public static final DummyConcurrentLock INSTANCE = new DummyConcurrentLock();
/*    */ 
/*    */   public void lock() {
/*    */   }
/*    */   public void lockInterruptibly() {
/*    */   }
/*    */   public boolean tryLock() {
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean tryLock(long time, TimeUnit unit) {
/* 42 */     return true;
/*    */   }
/*    */   public void unlock() {
/*    */   }
/*    */ 
/*    */   public Condition newCondition() {
/* 48 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.DummyConcurrentLock
 * JD-Core Version:    0.6.2
 */