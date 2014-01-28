/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.lucene.util.ThreadInterruptedException;
/*     */ 
/*     */ public class FilterManager
/*     */ {
/*     */   protected static FilterManager manager;
/*     */   protected static final int DEFAULT_CACHE_CLEAN_SIZE = 100;
/*     */   protected static final long DEFAULT_CACHE_SLEEP_TIME = 600000L;
/*     */   protected Map<Integer, FilterItem> cache;
/*     */   protected int cacheCleanSize;
/*     */   protected long cleanSleepTime;
/*     */   protected FilterCleaner filterCleaner;
/*     */ 
/*     */   public static synchronized FilterManager getInstance()
/*     */   {
/*  60 */     if (manager == null) {
/*  61 */       manager = new FilterManager();
/*     */     }
/*  63 */     return manager;
/*     */   }
/*     */ 
/*     */   protected FilterManager()
/*     */   {
/*  70 */     this.cache = new HashMap();
/*  71 */     this.cacheCleanSize = 100;
/*  72 */     this.cleanSleepTime = 600000L;
/*     */ 
/*  74 */     this.filterCleaner = new FilterCleaner();
/*  75 */     Thread fcThread = new Thread(this.filterCleaner);
/*     */ 
/*  77 */     fcThread.setDaemon(true);
/*  78 */     fcThread.start();
/*     */   }
/*     */ 
/*     */   public void setCacheSize(int cacheCleanSize)
/*     */   {
/*  86 */     this.cacheCleanSize = cacheCleanSize;
/*     */   }
/*     */ 
/*     */   public void setCleanThreadSleepTime(long cleanSleepTime)
/*     */   {
/*  94 */     this.cleanSleepTime = cleanSleepTime;
/*     */   }
/*     */ 
/*     */   public Filter getFilter(Filter filter)
/*     */   {
/* 106 */     synchronized (this.cache) {
/* 107 */       FilterItem fi = null;
/* 108 */       fi = (FilterItem)this.cache.get(Integer.valueOf(filter.hashCode()));
/* 109 */       if (fi != null) {
/* 110 */         fi.timestamp = new Date().getTime();
/* 111 */         return fi.filter;
/*     */       }
/* 113 */       this.cache.put(Integer.valueOf(filter.hashCode()), new FilterItem(filter));
/* 114 */       return filter;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class FilterCleaner
/*     */     implements Runnable
/*     */   {
/* 150 */     private boolean running = true;
/*     */     private TreeSet<Map.Entry<Integer, FilterManager.FilterItem>> sortedFilterItems;
/*     */ 
/*     */     public FilterCleaner()
/*     */     {
/* 154 */       this.sortedFilterItems = new TreeSet(new Comparator() {
/*     */         public int compare(Map.Entry<Integer, FilterManager.FilterItem> a, Map.Entry<Integer, FilterManager.FilterItem> b) {
/* 156 */           FilterManager.FilterItem fia = (FilterManager.FilterItem)a.getValue();
/* 157 */           FilterManager.FilterItem fib = (FilterManager.FilterItem)b.getValue();
/* 158 */           if (fia.timestamp == fib.timestamp) {
/* 159 */             return 0;
/*     */           }
/*     */ 
/* 162 */           if (fia.timestamp < fib.timestamp) {
/* 163 */             return -1;
/*     */           }
/*     */ 
/* 166 */           return 1;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 173 */       while (this.running)
/*     */       {
/* 177 */         if (FilterManager.this.cache.size() > FilterManager.this.cacheCleanSize)
/*     */         {
/* 179 */           this.sortedFilterItems.clear();
/* 180 */           synchronized (FilterManager.this.cache) {
/* 181 */             this.sortedFilterItems.addAll(FilterManager.this.cache.entrySet());
/* 182 */             Iterator it = this.sortedFilterItems.iterator();
/* 183 */             int numToDelete = (int)((FilterManager.this.cache.size() - FilterManager.this.cacheCleanSize) * 1.5D);
/* 184 */             int counter = 0;
/*     */ 
/* 186 */             while ((it.hasNext()) && (counter++ < numToDelete)) {
/* 187 */               Map.Entry entry = (Map.Entry)it.next();
/* 188 */               FilterManager.this.cache.remove(entry.getKey());
/*     */             }
/*     */           }
/*     */ 
/* 192 */           this.sortedFilterItems.clear();
/*     */         }
/*     */         try
/*     */         {
/* 196 */           Thread.sleep(FilterManager.this.cleanSleepTime);
/*     */         } catch (InterruptedException ie) {
/* 198 */           throw new ThreadInterruptedException(ie);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class FilterItem
/*     */   {
/*     */     public Filter filter;
/*     */     public long timestamp;
/*     */ 
/*     */     public FilterItem(Filter filter)
/*     */     {
/* 128 */       this.filter = filter;
/* 129 */       this.timestamp = new Date().getTime();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FilterManager
 * JD-Core Version:    0.6.2
 */