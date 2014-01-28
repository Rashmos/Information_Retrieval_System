/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ 
/*     */ public class FileCleaningTracker
/*     */ {
/*     */   ReferenceQueue<Object> q;
/*     */   final Collection<Tracker> trackers;
/*     */   final List<String> deleteFailures;
/*     */   volatile boolean exitWhenFinished;
/*     */   Thread reaper;
/*     */ 
/*     */   public FileCleaningTracker()
/*     */   {
/*  48 */     this.q = new ReferenceQueue();
/*     */ 
/*  52 */     this.trackers = Collections.synchronizedSet(new HashSet());
/*     */ 
/*  56 */     this.deleteFailures = Collections.synchronizedList(new ArrayList());
/*     */ 
/*  60 */     this.exitWhenFinished = false;
/*     */   }
/*     */ 
/*     */   public void track(File file, Object marker)
/*     */   {
/*  77 */     track(file, marker, (FileDeleteStrategy)null);
/*     */   }
/*     */ 
/*     */   public void track(File file, Object marker, FileDeleteStrategy deleteStrategy)
/*     */   {
/*  91 */     if (file == null) {
/*  92 */       throw new NullPointerException("The file must not be null");
/*     */     }
/*  94 */     addTracker(file.getPath(), marker, deleteStrategy);
/*     */   }
/*     */ 
/*     */   public void track(String path, Object marker)
/*     */   {
/* 107 */     track(path, marker, (FileDeleteStrategy)null);
/*     */   }
/*     */ 
/*     */   public void track(String path, Object marker, FileDeleteStrategy deleteStrategy)
/*     */   {
/* 121 */     if (path == null) {
/* 122 */       throw new NullPointerException("The path must not be null");
/*     */     }
/* 124 */     addTracker(path, marker, deleteStrategy);
/*     */   }
/*     */ 
/*     */   private synchronized void addTracker(String path, Object marker, FileDeleteStrategy deleteStrategy)
/*     */   {
/* 136 */     if (this.exitWhenFinished) {
/* 137 */       throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
/*     */     }
/* 139 */     if (this.reaper == null) {
/* 140 */       this.reaper = new Reaper();
/* 141 */       this.reaper.start();
/*     */     }
/* 143 */     this.trackers.add(new Tracker(path, deleteStrategy, marker, this.q));
/*     */   }
/*     */ 
/*     */   public int getTrackCount()
/*     */   {
/* 154 */     return this.trackers.size();
/*     */   }
/*     */ 
/*     */   public List<String> getDeleteFailures()
/*     */   {
/* 164 */     return this.deleteFailures;
/*     */   }
/*     */ 
/*     */   public synchronized void exitWhenFinished()
/*     */   {
/* 190 */     this.exitWhenFinished = true;
/* 191 */     if (this.reaper != null)
/* 192 */       synchronized (this.reaper) {
/* 193 */         this.reaper.interrupt();
/*     */       }
/*     */   }
/*     */ 
/*     */   private static final class Tracker extends PhantomReference<Object>
/*     */   {
/*     */     private final String path;
/*     */     private final FileDeleteStrategy deleteStrategy;
/*     */ 
/*     */     Tracker(String path, FileDeleteStrategy deleteStrategy, Object marker, ReferenceQueue<? super Object> queue)
/*     */     {
/* 257 */       super(queue);
/* 258 */       this.path = path;
/* 259 */       this.deleteStrategy = (deleteStrategy == null ? FileDeleteStrategy.NORMAL : deleteStrategy);
/*     */     }
/*     */ 
/*     */     public String getPath()
/*     */     {
/* 268 */       return this.path;
/*     */     }
/*     */ 
/*     */     public boolean delete()
/*     */     {
/* 278 */       return this.deleteStrategy.deleteQuietly(new File(this.path));
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class Reaper extends Thread
/*     */   {
/*     */     Reaper()
/*     */     {
/* 205 */       super();
/* 206 */       setPriority(10);
/* 207 */       setDaemon(true);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 217 */       while ((!FileCleaningTracker.this.exitWhenFinished) || (FileCleaningTracker.this.trackers.size() > 0))
/*     */         try
/*     */         {
/* 220 */           FileCleaningTracker.Tracker tracker = (FileCleaningTracker.Tracker)FileCleaningTracker.this.q.remove();
/* 221 */           FileCleaningTracker.this.trackers.remove(tracker);
/* 222 */           if (!tracker.delete()) {
/* 223 */             FileCleaningTracker.this.deleteFailures.add(tracker.getPath());
/*     */           }
/* 225 */           tracker.clear();
/*     */         }
/*     */         catch (InterruptedException e)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.FileCleaningTracker
 * JD-Core Version:    0.6.2
 */