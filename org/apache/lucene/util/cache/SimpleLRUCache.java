/*    */ package org.apache.lucene.util.cache;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class SimpleLRUCache<K, V> extends SimpleMapCache<K, V>
/*    */ {
/*    */   private static final float LOADFACTOR = 0.75F;
/*    */ 
/*    */   public SimpleLRUCache(final int cacheSize)
/*    */   {
/* 36 */     super(new LinkedHashMap(0.75F, true, cacheSize)
/*    */     {
/*    */       protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
/* 39 */         return size() > cacheSize;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.cache.SimpleLRUCache
 * JD-Core Version:    0.6.2
 */