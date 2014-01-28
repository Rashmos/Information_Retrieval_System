/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.util.ThreadInterruptedException;
/*     */ 
/*     */ public class ConcurrentMergeScheduler extends MergeScheduler
/*     */ {
/*  38 */   private int mergeThreadPriority = -1;
/*     */ 
/*  40 */   protected List<MergeThread> mergeThreads = new ArrayList();
/*     */ 
/*  43 */   private int maxThreadCount = 1;
/*     */   protected Directory dir;
/*     */   private boolean closed;
/*     */   protected IndexWriter writer;
/*     */   protected int mergeThreadCount;
/* 350 */   static boolean anyExceptions = false;
/*     */   private boolean suppressExceptions;
/*     */   private static List<ConcurrentMergeScheduler> allInstances;
/*     */ 
/*     */   public ConcurrentMergeScheduler()
/*     */   {
/*  52 */     if (allInstances != null)
/*     */     {
/*  54 */       addMyself();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMaxThreadCount(int count)
/*     */   {
/*  64 */     if (count < 1)
/*  65 */       throw new IllegalArgumentException("count should be at least 1");
/*  66 */     this.maxThreadCount = count;
/*     */   }
/*     */ 
/*     */   public int getMaxThreadCount()
/*     */   {
/*  72 */     return this.maxThreadCount;
/*     */   }
/*     */ 
/*     */   public synchronized int getMergeThreadPriority()
/*     */   {
/*  80 */     initMergeThreadPriority();
/*  81 */     return this.mergeThreadPriority;
/*     */   }
/*     */ 
/*     */   public synchronized void setMergeThreadPriority(int pri)
/*     */   {
/*  86 */     if ((pri > 10) || (pri < 1))
/*  87 */       throw new IllegalArgumentException("priority must be in range 1 .. 10 inclusive");
/*  88 */     this.mergeThreadPriority = pri;
/*     */ 
/*  90 */     int numThreads = mergeThreadCount();
/*  91 */     for (int i = 0; i < numThreads; i++) {
/*  92 */       MergeThread merge = (MergeThread)this.mergeThreads.get(i);
/*  93 */       merge.setThreadPriority(pri);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean verbose() {
/*  98 */     return (this.writer != null) && (this.writer.verbose());
/*     */   }
/*     */ 
/*     */   private void message(String message) {
/* 102 */     if (verbose())
/* 103 */       this.writer.message("CMS: " + message);
/*     */   }
/*     */ 
/*     */   private synchronized void initMergeThreadPriority() {
/* 107 */     if (this.mergeThreadPriority == -1)
/*     */     {
/* 110 */       this.mergeThreadPriority = (1 + Thread.currentThread().getPriority());
/* 111 */       if (this.mergeThreadPriority > 10)
/* 112 */         this.mergeThreadPriority = 10;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 118 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   public synchronized void sync() {
/* 122 */     while (mergeThreadCount() > 0) {
/* 123 */       if (verbose())
/* 124 */         message("now wait for threads; currently " + this.mergeThreads.size() + " still running");
/* 125 */       int count = this.mergeThreads.size();
/* 126 */       if (verbose()) {
/* 127 */         for (int i = 0; i < count; i++)
/* 128 */           message("    " + i + ": " + this.mergeThreads.get(i));
/*     */       }
/*     */       try
/*     */       {
/* 132 */         wait();
/*     */       } catch (InterruptedException ie) {
/* 134 */         throw new ThreadInterruptedException(ie);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized int mergeThreadCount() {
/* 140 */     int count = 0;
/* 141 */     int numThreads = this.mergeThreads.size();
/* 142 */     for (int i = 0; i < numThreads; i++)
/* 143 */       if (((MergeThread)this.mergeThreads.get(i)).isAlive())
/* 144 */         count++;
/* 145 */     return count;
/*     */   }
/*     */ 
/*     */   public void merge(IndexWriter writer)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 152 */     assert (!Thread.holdsLock(writer));
/*     */ 
/* 154 */     this.writer = writer;
/*     */ 
/* 156 */     initMergeThreadPriority();
/*     */ 
/* 158 */     this.dir = writer.getDirectory();
/*     */ 
/* 167 */     if (verbose()) {
/* 168 */       message("now merge");
/* 169 */       message("  index: " + writer.segString());
/*     */     }
/*     */ 
/*     */     while (true)
/*     */     {
/* 180 */       MergePolicy.OneMerge merge = writer.getNextMerge();
/* 181 */       if (merge == null) {
/* 182 */         if (verbose())
/* 183 */           message("  no more merges pending; now return");
/* 184 */         return;
/*     */       }
/*     */ 
/* 189 */       writer.mergeInit(merge);
/*     */ 
/* 191 */       boolean success = false;
/*     */       try {
/* 193 */         synchronized (this)
/*     */         {
/* 195 */           while (mergeThreadCount() >= this.maxThreadCount) {
/* 196 */             if (verbose())
/* 197 */               message("    too many merge threads running; stalling...");
/*     */             try {
/* 199 */               wait();
/*     */             } catch (InterruptedException ie) {
/* 201 */               throw new ThreadInterruptedException(ie);
/*     */             }
/*     */           }
/*     */ 
/* 205 */           if (verbose()) {
/* 206 */             message("  consider merge " + merge.segString(this.dir));
/*     */           }
/* 208 */           assert (mergeThreadCount() < this.maxThreadCount);
/*     */ 
/* 212 */           MergeThread merger = getMergeThread(writer, merge);
/* 213 */           this.mergeThreads.add(merger);
/* 214 */           if (verbose()) {
/* 215 */             message("    launch new thread [" + merger.getName() + "]");
/*     */           }
/* 217 */           merger.start();
/* 218 */           success = true;
/*     */         }
/*     */       } finally {
/* 221 */         if (!success)
/* 222 */           writer.mergeFinish(merge);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void doMerge(MergePolicy.OneMerge merge)
/*     */     throws IOException
/*     */   {
/* 231 */     this.writer.merge(merge);
/*     */   }
/*     */ 
/*     */   protected synchronized MergeThread getMergeThread(IndexWriter writer, MergePolicy.OneMerge merge) throws IOException
/*     */   {
/* 236 */     MergeThread thread = new MergeThread(writer, merge);
/* 237 */     thread.setThreadPriority(this.mergeThreadPriority);
/* 238 */     thread.setDaemon(true);
/* 239 */     thread.setName("Lucene Merge Thread #" + this.mergeThreadCount++);
/* 240 */     return thread;
/*     */   }
/*     */ 
/*     */   protected void handleMergeException(Throwable exc)
/*     */   {
/*     */     try
/*     */     {
/* 343 */       Thread.sleep(250L);
/*     */     } catch (InterruptedException ie) {
/* 345 */       throw new ThreadInterruptedException(ie);
/*     */     }
/* 347 */     throw new MergePolicy.MergeException(exc, this.dir);
/*     */   }
/*     */ 
/*     */   public static boolean anyUnhandledExceptions()
/*     */   {
/* 354 */     if (allInstances == null) {
/* 355 */       throw new RuntimeException("setTestMode() was not called; often this is because your test case's setUp method fails to call super.setUp in LuceneTestCase");
/*     */     }
/* 357 */     synchronized (allInstances) {
/* 358 */       int count = allInstances.size();
/*     */ 
/* 361 */       for (int i = 0; i < count; i++)
/* 362 */         ((ConcurrentMergeScheduler)allInstances.get(i)).sync();
/* 363 */       boolean v = anyExceptions;
/* 364 */       anyExceptions = false;
/* 365 */       return v;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void clearUnhandledExceptions() {
/* 370 */     synchronized (allInstances) {
/* 371 */       anyExceptions = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addMyself()
/*     */   {
/* 377 */     synchronized (allInstances) {
/* 378 */       int size = allInstances.size();
/* 379 */       int upto = 0;
/* 380 */       for (int i = 0; i < size; i++) {
/* 381 */         ConcurrentMergeScheduler other = (ConcurrentMergeScheduler)allInstances.get(i);
/* 382 */         if ((!other.closed) || (0 != other.mergeThreadCount()))
/*     */         {
/* 385 */           allInstances.set(upto++, other);
/*     */         }
/*     */       }
/* 387 */       allInstances.subList(upto, allInstances.size()).clear();
/* 388 */       allInstances.add(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setSuppressExceptions()
/*     */   {
/* 396 */     this.suppressExceptions = true;
/*     */   }
/*     */ 
/*     */   void clearSuppressExceptions()
/*     */   {
/* 401 */     this.suppressExceptions = false;
/*     */   }
/*     */ 
/*     */   public static void setTestMode()
/*     */   {
/* 407 */     allInstances = new ArrayList();
/*     */   }
/*     */ 
/*     */   protected class MergeThread extends Thread
/*     */   {
/*     */     IndexWriter writer;
/*     */     MergePolicy.OneMerge startMerge;
/*     */     MergePolicy.OneMerge runningMerge;
/*     */ 
/*     */     public MergeThread(IndexWriter writer, MergePolicy.OneMerge startMerge)
/*     */       throws IOException
/*     */     {
/* 250 */       this.writer = writer;
/* 251 */       this.startMerge = startMerge;
/*     */     }
/*     */ 
/*     */     public synchronized void setRunningMerge(MergePolicy.OneMerge merge) {
/* 255 */       this.runningMerge = merge;
/*     */     }
/*     */ 
/*     */     public synchronized MergePolicy.OneMerge getRunningMerge() {
/* 259 */       return this.runningMerge;
/*     */     }
/*     */ 
/*     */     public void setThreadPriority(int pri) {
/*     */       try {
/* 264 */         setPriority(pri);
/*     */       }
/*     */       catch (NullPointerException npe)
/*     */       {
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 279 */       MergePolicy.OneMerge merge = this.startMerge;
/*     */       try
/*     */       {
/* 283 */         if (ConcurrentMergeScheduler.this.verbose())
/* 284 */           ConcurrentMergeScheduler.this.message("  merge thread: start");
/*     */         while (true)
/*     */         {
/* 287 */           setRunningMerge(merge);
/* 288 */           ConcurrentMergeScheduler.this.doMerge(merge);
/*     */ 
/* 292 */           merge = this.writer.getNextMerge();
/* 293 */           if (merge == null) break;
/* 294 */           this.writer.mergeInit(merge);
/* 295 */           if (ConcurrentMergeScheduler.this.verbose()) {
/* 296 */             ConcurrentMergeScheduler.this.message("  merge thread: do another merge " + merge.segString(ConcurrentMergeScheduler.this.dir));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 301 */         if (ConcurrentMergeScheduler.this.verbose()) {
/* 302 */           ConcurrentMergeScheduler.this.message("  merge thread: done");
/*     */         }
/*     */       }
/*     */       catch (Throwable exc)
/*     */       {
/* 307 */         if ((!(exc instanceof MergePolicy.MergeAbortedException)) && 
/* 308 */           (!ConcurrentMergeScheduler.this.suppressExceptions))
/*     */         {
/* 311 */           ConcurrentMergeScheduler.anyExceptions = true;
/* 312 */           ConcurrentMergeScheduler.this.handleMergeException(exc);
/*     */         }
/*     */       }
/*     */       finally {
/* 316 */         synchronized (ConcurrentMergeScheduler.this) {
/* 317 */           ConcurrentMergeScheduler.this.notifyAll();
/* 318 */           boolean removed = ConcurrentMergeScheduler.this.mergeThreads.remove(this);
/* 319 */           if ((!$assertionsDisabled) && (!removed)) throw new AssertionError();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 326 */       MergePolicy.OneMerge merge = getRunningMerge();
/* 327 */       if (merge == null)
/* 328 */         merge = this.startMerge;
/* 329 */       return "merge thread: " + merge.segString(ConcurrentMergeScheduler.this.dir);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.ConcurrentMergeScheduler
 * JD-Core Version:    0.6.2
 */