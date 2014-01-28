/*    */ package org.apache.lucene.store;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class LockFactory
/*    */ {
/* 38 */   protected String lockPrefix = null;
/*    */ 
/*    */   public void setLockPrefix(String lockPrefix)
/*    */   {
/* 51 */     this.lockPrefix = lockPrefix;
/*    */   }
/*    */ 
/*    */   public String getLockPrefix()
/*    */   {
/* 58 */     return this.lockPrefix;
/*    */   }
/*    */ 
/*    */   public abstract Lock makeLock(String paramString);
/*    */ 
/*    */   public abstract void clearLock(String paramString)
/*    */     throws IOException;
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.LockFactory
 * JD-Core Version:    0.6.2
 */