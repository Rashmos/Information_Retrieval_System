/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.util.ThreadInterruptedException;
/*     */ 
/*     */ public class TimeLimitingCollector extends Collector
/*     */ {
/*     */   public static final int DEFAULT_RESOLUTION = 20;
/*  43 */   public boolean DEFAULT_GREEDY = false;
/*     */ 
/*  45 */   private static long resolution = 20L;
/*     */ 
/*  47 */   private boolean greedy = this.DEFAULT_GREEDY;
/*     */ 
/* 123 */   private static final TimerThread TIMER_THREAD = new TimerThread(null);
/*     */   private final long t0;
/*     */   private final long timeout;
/*     */   private final Collector collector;
/*     */ 
/*     */   public TimeLimitingCollector(Collector collector, long timeAllowed)
/*     */   {
/* 139 */     this.collector = collector;
/* 140 */     this.t0 = TIMER_THREAD.getMilliseconds();
/* 141 */     this.timeout = (this.t0 + timeAllowed);
/*     */   }
/*     */ 
/*     */   public static long getResolution()
/*     */   {
/* 149 */     return resolution;
/*     */   }
/*     */ 
/*     */   public static void setResolution(long newResolution)
/*     */   {
/* 167 */     resolution = Math.max(newResolution, 5L);
/*     */   }
/*     */ 
/*     */   public boolean isGreedy()
/*     */   {
/* 179 */     return this.greedy;
/*     */   }
/*     */ 
/*     */   public void setGreedy(boolean greedy)
/*     */   {
/* 188 */     this.greedy = greedy;
/*     */   }
/*     */ 
/*     */   public void collect(int doc)
/*     */     throws IOException
/*     */   {
/* 200 */     long time = TIMER_THREAD.getMilliseconds();
/* 201 */     if (this.timeout < time) {
/* 202 */       if (this.greedy)
/*     */       {
/* 204 */         this.collector.collect(doc);
/*     */       }
/*     */ 
/* 207 */       throw new TimeExceededException(this.timeout - this.t0, time - this.t0, doc, null);
/*     */     }
/*     */ 
/* 210 */     this.collector.collect(doc);
/*     */   }
/*     */ 
/*     */   public void setNextReader(IndexReader reader, int base) throws IOException
/*     */   {
/* 215 */     this.collector.setNextReader(reader, base);
/*     */   }
/*     */ 
/*     */   public void setScorer(Scorer scorer) throws IOException
/*     */   {
/* 220 */     this.collector.setScorer(scorer);
/*     */   }
/*     */ 
/*     */   public boolean acceptsDocsOutOfOrder()
/*     */   {
/* 225 */     return this.collector.acceptsDocsOutOfOrder();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 126 */     TIMER_THREAD.start();
/*     */   }
/*     */ 
/*     */   public static class TimeExceededException extends RuntimeException
/*     */   {
/*     */     private long timeAllowed;
/*     */     private long timeElapsed;
/*     */     private int lastDocCollected;
/*     */ 
/*     */     private TimeExceededException(long timeAllowed, long timeElapsed, int lastDocCollected)
/*     */     {
/* 101 */       super();
/* 102 */       this.timeAllowed = timeAllowed;
/* 103 */       this.timeElapsed = timeElapsed;
/* 104 */       this.lastDocCollected = lastDocCollected;
/*     */     }
/*     */ 
/*     */     public long getTimeAllowed() {
/* 108 */       return this.timeAllowed;
/*     */     }
/*     */ 
/*     */     public long getTimeElapsed() {
/* 112 */       return this.timeElapsed;
/*     */     }
/*     */ 
/*     */     public int getLastDocCollected() {
/* 116 */       return this.lastDocCollected;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class TimerThread extends Thread
/*     */   {
/*  61 */     private volatile long time = 0L;
/*     */ 
/*     */     private TimerThread()
/*     */     {
/*  70 */       super();
/*  71 */       setDaemon(true);
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       while (true)
/*     */       {
/*  78 */         this.time += TimeLimitingCollector.resolution;
/*     */         try {
/*  80 */           Thread.sleep(TimeLimitingCollector.resolution);
/*     */         } catch (InterruptedException ie) {
/*  82 */           throw new ThreadInterruptedException(ie);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public long getMilliseconds()
/*     */     {
/*  91 */       return this.time;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TimeLimitingCollector
 * JD-Core Version:    0.6.2
 */