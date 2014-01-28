/*    */ package org.apache.lucene.util.cache;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ 
/*    */ public abstract class Cache<K, V>
/*    */   implements Closeable
/*    */ {
/*    */   public static <K, V> Cache<K, V> synchronizedCache(Cache<K, V> cache)
/*    */   {
/* 77 */     return cache.getSynchronizedCache();
/*    */   }
/*    */ 
/*    */   Cache<K, V> getSynchronizedCache()
/*    */   {
/* 88 */     return new SynchronizedCache(this);
/*    */   }
/*    */ 
/*    */   public abstract void put(K paramK, V paramV);
/*    */ 
/*    */   public abstract V get(Object paramObject);
/*    */ 
/*    */   public abstract boolean containsKey(Object paramObject);
/*    */ 
/*    */   public abstract void close();
/*    */ 
/*    */   static class SynchronizedCache<K, V> extends Cache<K, V>
/*    */   {
/*    */     private Object mutex;
/*    */     private Cache<K, V> cache;
/*    */ 
/*    */     SynchronizedCache(Cache<K, V> cache)
/*    */     {
/* 36 */       this.cache = cache;
/* 37 */       this.mutex = this;
/*    */     }
/*    */ 
/*    */     SynchronizedCache(Cache<K, V> cache, Object mutex) {
/* 41 */       this.cache = cache;
/* 42 */       this.mutex = mutex;
/*    */     }
/*    */ 
/*    */     public void put(K key, V value)
/*    */     {
/* 47 */       synchronized (this.mutex) { this.cache.put(key, value); }
/*    */     }
/*    */ 
/*    */     public V get(Object key)
/*    */     {
/* 52 */       synchronized (this.mutex) { return this.cache.get(key); }
/*    */     }
/*    */ 
/*    */     public boolean containsKey(Object key)
/*    */     {
/* 57 */       synchronized (this.mutex) { return this.cache.containsKey(key); }
/*    */     }
/*    */ 
/*    */     public void close()
/*    */     {
/* 62 */       synchronized (this.mutex) { this.cache.close(); }
/*    */     }
/*    */ 
/*    */     Cache<K, V> getSynchronizedCache()
/*    */     {
/* 67 */       return this;
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.cache.Cache
 * JD-Core Version:    0.6.2
 */