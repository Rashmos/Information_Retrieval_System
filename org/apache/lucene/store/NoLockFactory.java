/*    */ package org.apache.lucene.store;
/*    */ 
/*    */ public class NoLockFactory extends LockFactory
/*    */ {
/* 33 */   private static NoLock singletonLock = new NoLock();
/* 34 */   private static NoLockFactory singleton = new NoLockFactory();
/*    */ 
/*    */   public static NoLockFactory getNoLockFactory() {
/* 37 */     return singleton;
/*    */   }
/*    */ 
/*    */   public Lock makeLock(String lockName)
/*    */   {
/* 42 */     return singletonLock;
/*    */   }
/*    */ 
/*    */   public void clearLock(String lockName)
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.NoLockFactory
 * JD-Core Version:    0.6.2
 */