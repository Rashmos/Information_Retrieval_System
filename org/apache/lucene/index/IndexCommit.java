/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.apache.lucene.store.Directory;
/*     */ 
/*     */ public abstract class IndexCommit
/*     */ {
/*     */   public abstract String getSegmentsFileName();
/*     */ 
/*     */   public abstract Collection<String> getFileNames()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract Directory getDirectory();
/*     */ 
/*     */   public abstract void delete();
/*     */ 
/*     */   public abstract boolean isDeleted();
/*     */ 
/*     */   public abstract boolean isOptimized();
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/*  84 */     if ((other instanceof IndexCommit)) {
/*  85 */       IndexCommit otherCommit = (IndexCommit)other;
/*  86 */       return (otherCommit.getDirectory().equals(getDirectory())) && (otherCommit.getVersion() == getVersion());
/*     */     }
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  93 */     return (int)(getDirectory().hashCode() + getVersion());
/*     */   }
/*     */ 
/*     */   public abstract long getVersion();
/*     */ 
/*     */   public abstract long getGeneration();
/*     */ 
/*     */   public long getTimestamp()
/*     */     throws IOException
/*     */   {
/* 110 */     return getDirectory().fileModified(getSegmentsFileName());
/*     */   }
/*     */ 
/*     */   public abstract Map<String, String> getUserData()
/*     */     throws IOException;
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexCommit
 * JD-Core Version:    0.6.2
 */