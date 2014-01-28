/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class MapOfSets<K, V>
/*    */ {
/*    */   private final Map<K, Set<V>> theMap;
/*    */ 
/*    */   public MapOfSets(Map<K, Set<V>> m)
/*    */   {
/* 37 */     this.theMap = m;
/*    */   }
/*    */ 
/*    */   public Map<K, Set<V>> getMap()
/*    */   {
/* 44 */     return this.theMap;
/*    */   }
/*    */ 
/*    */   public int put(K key, V val)
/*    */   {
/*    */     Set theSet;
/*    */     Set theSet;
/* 54 */     if (this.theMap.containsKey(key)) {
/* 55 */       theSet = (Set)this.theMap.get(key);
/*    */     } else {
/* 57 */       theSet = new HashSet(23);
/* 58 */       this.theMap.put(key, theSet);
/*    */     }
/* 60 */     theSet.add(val);
/* 61 */     return theSet.size();
/*    */   }
/*    */ 
/*    */   public int putAll(K key, Collection<? extends V> vals)
/*    */   {
/*    */     Set theSet;
/*    */     Set theSet;
/* 71 */     if (this.theMap.containsKey(key)) {
/* 72 */       theSet = (Set)this.theMap.get(key);
/*    */     } else {
/* 74 */       theSet = new HashSet(23);
/* 75 */       this.theMap.put(key, theSet);
/*    */     }
/* 77 */     theSet.addAll(vals);
/* 78 */     return theSet.size();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.MapOfSets
 * JD-Core Version:    0.6.2
 */