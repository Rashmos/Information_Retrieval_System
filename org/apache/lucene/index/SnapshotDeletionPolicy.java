/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.lucene.store.Directory;
/*     */ 
/*     */ public class SnapshotDeletionPolicy
/*     */   implements IndexDeletionPolicy
/*     */ {
/*     */   private IndexCommit lastCommit;
/*     */   private IndexDeletionPolicy primary;
/*     */   private String snapshot;
/*     */ 
/*     */   public SnapshotDeletionPolicy(IndexDeletionPolicy primary)
/*     */   {
/*  52 */     this.primary = primary;
/*     */   }
/*     */ 
/*     */   public synchronized void onInit(List<? extends IndexCommit> commits) throws IOException {
/*  56 */     this.primary.onInit(wrapCommits(commits));
/*  57 */     this.lastCommit = ((IndexCommit)commits.get(commits.size() - 1));
/*     */   }
/*     */ 
/*     */   public synchronized void onCommit(List<? extends IndexCommit> commits) throws IOException {
/*  61 */     this.primary.onCommit(wrapCommits(commits));
/*  62 */     this.lastCommit = ((IndexCommit)commits.get(commits.size() - 1));
/*     */   }
/*     */ 
/*     */   public synchronized IndexCommit snapshot()
/*     */   {
/*  75 */     if (this.lastCommit == null) {
/*  76 */       throw new IllegalStateException("no index commits to snapshot !");
/*     */     }
/*     */ 
/*  79 */     if (this.snapshot == null)
/*  80 */       this.snapshot = this.lastCommit.getSegmentsFileName();
/*     */     else
/*  82 */       throw new IllegalStateException("snapshot is already set; please call release() first");
/*  83 */     return this.lastCommit;
/*     */   }
/*     */ 
/*     */   public synchronized void release()
/*     */   {
/*  88 */     if (this.snapshot != null)
/*  89 */       this.snapshot = null;
/*     */     else
/*  91 */       throw new IllegalStateException("snapshot was not set; please call snapshot() first");
/*     */   }
/*     */ 
/*     */   private List<IndexCommit> wrapCommits(List<? extends IndexCommit> commits)
/*     */   {
/* 143 */     int count = commits.size();
/* 144 */     List myCommits = new ArrayList(count);
/* 145 */     for (int i = 0; i < count; i++)
/* 146 */       myCommits.add(new MyCommitPoint((IndexCommit)commits.get(i)));
/* 147 */     return myCommits;
/*     */   }
/*     */ 
/*     */   private class MyCommitPoint extends IndexCommit
/*     */   {
/*     */     IndexCommit cp;
/*     */ 
/*     */     MyCommitPoint(IndexCommit cp)
/*     */     {
/*  97 */       this.cp = cp;
/*     */     }
/*     */ 
/*     */     public String getSegmentsFileName() {
/* 101 */       return this.cp.getSegmentsFileName();
/*     */     }
/*     */ 
/*     */     public Collection<String> getFileNames() throws IOException {
/* 105 */       return this.cp.getFileNames();
/*     */     }
/*     */ 
/*     */     public Directory getDirectory() {
/* 109 */       return this.cp.getDirectory();
/*     */     }
/*     */ 
/*     */     public void delete() {
/* 113 */       synchronized (SnapshotDeletionPolicy.this)
/*     */       {
/* 116 */         if ((SnapshotDeletionPolicy.this.snapshot == null) || (!SnapshotDeletionPolicy.this.snapshot.equals(getSegmentsFileName())))
/* 117 */           this.cp.delete();
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isDeleted() {
/* 122 */       return this.cp.isDeleted();
/*     */     }
/*     */ 
/*     */     public long getVersion() {
/* 126 */       return this.cp.getVersion();
/*     */     }
/*     */ 
/*     */     public long getGeneration() {
/* 130 */       return this.cp.getGeneration();
/*     */     }
/*     */ 
/*     */     public Map<String, String> getUserData() throws IOException {
/* 134 */       return this.cp.getUserData();
/*     */     }
/*     */ 
/*     */     public boolean isOptimized() {
/* 138 */       return this.cp.isOptimized();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.SnapshotDeletionPolicy
 * JD-Core Version:    0.6.2
 */