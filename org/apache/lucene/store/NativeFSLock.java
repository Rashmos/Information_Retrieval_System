/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ class NativeFSLock extends Lock
/*     */ {
/*     */   private RandomAccessFile f;
/*     */   private FileChannel channel;
/*     */   private FileLock lock;
/*     */   private File path;
/*     */   private File lockDir;
/* 184 */   private static HashSet<String> LOCK_HELD = new HashSet();
/*     */ 
/*     */   public NativeFSLock(File lockDir, String lockFileName) {
/* 187 */     this.lockDir = lockDir;
/* 188 */     this.path = new File(lockDir, lockFileName);
/*     */   }
/*     */ 
/*     */   private synchronized boolean lockExists() {
/* 192 */     return this.lock != null;
/*     */   }
/*     */ 
/*     */   public synchronized boolean obtain()
/*     */     throws IOException
/*     */   {
/* 198 */     if (lockExists())
/*     */     {
/* 200 */       return false;
/*     */     }
/*     */ 
/* 204 */     if (!this.lockDir.exists()) {
/* 205 */       if (!this.lockDir.mkdirs())
/* 206 */         throw new IOException("Cannot create directory: " + this.lockDir.getAbsolutePath());
/*     */     }
/* 208 */     else if (!this.lockDir.isDirectory()) {
/* 209 */       throw new IOException("Found regular file where directory expected: " + this.lockDir.getAbsolutePath());
/*     */     }
/*     */ 
/* 213 */     String canonicalPath = this.path.getCanonicalPath();
/*     */ 
/* 215 */     boolean markedHeld = false;
/*     */     try
/*     */     {
/* 222 */       synchronized (LOCK_HELD) {
/* 223 */         if (LOCK_HELD.contains(canonicalPath))
/*     */         {
/* 225 */           boolean bool1 = false; jsr 224; return bool1;
/*     */         }
/*     */ 
/* 231 */         LOCK_HELD.add(canonicalPath);
/* 232 */         markedHeld = true;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 249 */         this.channel = this.f.getChannel();
/*     */         try {
/* 251 */           this.lock = this.channel.tryLock();
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/* 262 */           this.failureReason = e;
/*     */         }
/*     */         finally
/*     */         {
/* 264 */           if (this.lock != null);
/*     */         }
/* 268 */         ret;
/*     */       }
/*     */       finally
/*     */       {
/* 273 */         if (this.channel != null);
/*     */       }
/*     */ 
/* 277 */       ret;
/*     */     }
/*     */     finally
/*     */     {
/* 284 */       if ((markedHeld) && (!lockExists())) {
/* 285 */         synchronized (LOCK_HELD) {
/* 286 */           if (LOCK_HELD.contains(canonicalPath)) {
/* 287 */             LOCK_HELD.remove(canonicalPath);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 292 */     return lockExists();
/*     */   }
/*     */ 
/*     */   public synchronized void release() throws IOException
/*     */   {
/* 297 */     if (lockExists()) {
/*     */       try {
/* 299 */         this.lock.release();
/*     */       } finally {
/* 301 */         this.lock = null;
/*     */       }
/*     */ 
/* 312 */       ret; ret;
/*     */ 
/* 319 */       this.path.delete();
/*     */     }
/*     */     else
/*     */     {
/* 326 */       boolean obtained = false;
/*     */       try {
/* 328 */         if (!(obtained = obtain())) {
/* 329 */           throw new LockReleaseFailedException("Cannot forcefully unlock a NativeFSLock which is held by another indexer component: " + this.path);
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 334 */         if (obtained)
/* 335 */           release();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean isLocked()
/*     */   {
/* 346 */     if (lockExists()) return true;
/*     */ 
/* 349 */     if (!this.path.exists()) return false;
/*     */ 
/*     */     try
/*     */     {
/* 353 */       boolean obtained = obtain();
/* 354 */       if (obtained) release();
/* 355 */       return !obtained; } catch (IOException ioe) {
/*     */     }
/* 357 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 363 */     return "NativeFSLock@" + this.path;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.NativeFSLock
 * JD-Core Version:    0.6.2
 */