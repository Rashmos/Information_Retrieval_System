/*       */ package org.apache.lucene.util.cache;
/*       */ 
/*       */ import java.util.HashMap;
/*       */ import java.util.Map;
/*       */ import java.util.Set;
/*       */ 
/*       */ public class SimpleMapCache<K, V> extends Cache<K, V>
/*       */ {
/*       */   protected Map<K, V> map;
/*       */ 
/*       */   public SimpleMapCache()
/*       */   {
/*    33 */     this(new HashMap());
/*       */   }
/*       */ 
/*       */   public SimpleMapCache(Map<K, V> map) {
/*    37 */     this.map = map;
/*       */   }
/*       */ 
/*       */   public V get(Object key)
/*       */   {
/*    42 */     return this.map.get(key);
/*       */   }
/*       */ 
/*       */   public void put(K key, V value)
/*       */   {
/*    47 */     this.map.put(key, value);
/*       */   }
/*       */ 
/*       */   public void close()
/*       */   {
/*       */   }
/*       */ 
/*       */   public boolean containsKey(Object key)
/*       */   {
/*    57 */     return this.map.containsKey(key);
/*       */   }
/*       */ 
/*       */   public Set<K> keySet()
/*       */   {
/*    64 */     return this.map.keySet();
/*       */   }
/*       */ 
/*       */   Cache<K, V> getSynchronizedCache()
/*       */   {
/*    69 */     return new SynchronizedSimpleMapCache(this);
/*       */   }
/*       */   private static class SynchronizedSimpleMapCache<K, V> extends SimpleMapCache<K, V> {
/*       */     private Object mutex;
/*       */     private SimpleMapCache<K, V> cache;
/*       */ 
/*       */     SynchronizedSimpleMapCache(SimpleMapCache<K, V> cache) {
/*    77 */       this.cache = cache;
/*    78 */       this.mutex = this;
/*       */     }
/*       */ 
/*       */     public void put(K key, V value)
/*       */     {
/*    83 */       synchronized (this.mutex) { this.cache.put(key, value); }
/*       */     }
/*       */ 
/*       */     public V get(Object key)
/*       */     {
/*    88 */       synchronized (this.mutex) { return this.cache.get(key); }
/*       */     }
/*       */ 
/*       */     public boolean containsKey(Object key)
/*       */     {
/*    93 */       synchronized (this.mutex) { return this.cache.containsKey(key); }
/*       */     }
/*       */ 
/*       */     public void close()
/*       */     {
/*    98 */       synchronized (this.mutex) { this.cache.close(); }
/*       */     }
/*       */ 
/*       */     public Set<K> keySet()
/*       */     {
/*   103 */       synchronized (this.mutex) { return this.cache.keySet(); }
/*       */     }
/*       */ 
/*       */     Cache<K, V> getSynchronizedCache()
/*       */     {
/*   108 */       return this;
/*       */     }
/*       */   }
/*       */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.cache.SimpleMapCache
 * JD-Core Version:    0.6.2
 */