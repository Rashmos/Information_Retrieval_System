/*    */ package org.apache.lucene.store;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ class NoLock extends Lock
/*    */ {
/*    */   public boolean obtain()
/*    */     throws IOException
/*    */   {
/* 52 */     return true;
/*    */   }
/*    */ 
/*    */   public void release()
/*    */   {
/*    */   }
/*    */ 
/*    */   public boolean isLocked()
/*    */   {
/* 61 */     return false;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 66 */     return "NoLock";
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.NoLock
 * JD-Core Version:    0.6.2
 */