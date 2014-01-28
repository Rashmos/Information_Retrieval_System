/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public final class KeepOnlyLastCommitDeletionPolicy
/*    */   implements IndexDeletionPolicy
/*    */ {
/*    */   public void onInit(List<? extends IndexCommit> commits)
/*    */   {
/* 36 */     onCommit(commits);
/*    */   }
/*    */ 
/*    */   public void onCommit(List<? extends IndexCommit> commits)
/*    */   {
/* 45 */     int size = commits.size();
/* 46 */     for (int i = 0; i < size - 1; i++)
/* 47 */       ((IndexCommit)commits.get(i)).delete();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy
 * JD-Core Version:    0.6.2
 */