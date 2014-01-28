/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class NativeFSLockFactory extends FSLockFactory
/*     */ {
/*  61 */   private volatile boolean tested = false;
/*     */ 
/*     */   private synchronized void acquireTestLock()
/*     */   {
/*  69 */     if (this.tested) return;
/*  70 */     this.tested = true;
/*     */ 
/*  73 */     if (!this.lockDir.exists()) {
/*  74 */       if (!this.lockDir.mkdirs())
/*  75 */         throw new RuntimeException("Cannot create directory: " + this.lockDir.getAbsolutePath());
/*     */     }
/*  77 */     else if (!this.lockDir.isDirectory()) {
/*  78 */       throw new RuntimeException("Found regular file where directory expected: " + this.lockDir.getAbsolutePath());
/*     */     }
/*     */ 
/*  87 */     String randomLockName = "lucene-" + ManagementFactory.getRuntimeMXBean().getName().replaceAll("[^a..zA..Z0..9]+", "") + "-" + Long.toString(new Random().nextInt(), 36) + "-test.lock";
/*     */ 
/*  92 */     Lock l = makeLock(randomLockName);
/*     */     try {
/*  94 */       l.obtain();
/*  95 */       l.release();
/*     */ 
/*  98 */       File lockFile = new File(this.lockDir, randomLockName);
/*  99 */       if (lockFile.exists())
/* 100 */         lockFile.deleteOnExit();
/*     */     }
/*     */     catch (IOException e) {
/* 103 */       RuntimeException e2 = new RuntimeException("Failed to acquire random test lock; please verify filesystem for lock directory '" + this.lockDir + "' supports locking");
/* 104 */       e2.initCause(e);
/* 105 */       throw e2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public NativeFSLockFactory()
/*     */     throws IOException
/*     */   {
/* 117 */     this((File)null);
/*     */   }
/*     */ 
/*     */   public NativeFSLockFactory(String lockDirName)
/*     */     throws IOException
/*     */   {
/* 127 */     this(new File(lockDirName));
/*     */   }
/*     */ 
/*     */   public NativeFSLockFactory(File lockDir)
/*     */     throws IOException
/*     */   {
/* 137 */     setLockDir(lockDir);
/*     */   }
/*     */ 
/*     */   public synchronized Lock makeLock(String lockName)
/*     */   {
/* 142 */     acquireTestLock();
/* 143 */     if (this.lockPrefix != null)
/* 144 */       lockName = this.lockPrefix + "-" + lockName;
/* 145 */     return new NativeFSLock(this.lockDir, lockName);
/*     */   }
/*     */ 
/*     */   public void clearLock(String lockName)
/*     */     throws IOException
/*     */   {
/* 154 */     if (this.lockDir.exists()) {
/* 155 */       if (this.lockPrefix != null) {
/* 156 */         lockName = this.lockPrefix + "-" + lockName;
/*     */       }
/* 158 */       File lockFile = new File(this.lockDir, lockName);
/* 159 */       if ((lockFile.exists()) && (!lockFile.delete()))
/* 160 */         throw new IOException("Cannot delete " + lockFile);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.NativeFSLockFactory
 * JD-Core Version:    0.6.2
 */