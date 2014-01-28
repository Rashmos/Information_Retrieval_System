/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.search.FieldCache.CacheEntry;
/*     */ import org.apache.lucene.search.FieldCache.CreationPlaceholder;
/*     */ 
/*     */ public final class FieldCacheSanityChecker
/*     */ {
/*  58 */   private RamUsageEstimator ramCalc = null;
/*     */ 
/*     */   public void setRamUsageEstimator(RamUsageEstimator r)
/*     */   {
/*  67 */     this.ramCalc = r;
/*     */   }
/*     */ 
/*     */   public static Insanity[] checkSanity(FieldCache cache)
/*     */   {
/*  76 */     return checkSanity(cache.getCacheEntries());
/*     */   }
/*     */ 
/*     */   public static Insanity[] checkSanity(FieldCache.CacheEntry[] cacheEntries)
/*     */   {
/*  85 */     FieldCacheSanityChecker sanityChecker = new FieldCacheSanityChecker();
/*     */ 
/*  87 */     sanityChecker.setRamUsageEstimator(new RamUsageEstimator(false));
/*  88 */     return sanityChecker.check(cacheEntries);
/*     */   }
/*     */ 
/*     */   public Insanity[] check(FieldCache.CacheEntry[] cacheEntries)
/*     */   {
/* 100 */     if ((null == cacheEntries) || (0 == cacheEntries.length)) {
/* 101 */       return new Insanity[0];
/*     */     }
/* 103 */     if (null != this.ramCalc) {
/* 104 */       for (int i = 0; i < cacheEntries.length; i++) {
/* 105 */         cacheEntries[i].estimateSize(this.ramCalc);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 113 */     MapOfSets valIdToItems = new MapOfSets(new HashMap(17));
/*     */ 
/* 115 */     MapOfSets readerFieldToValIds = new MapOfSets(new HashMap(17));
/*     */ 
/* 119 */     Set valMismatchKeys = new HashSet();
/*     */ 
/* 122 */     for (int i = 0; i < cacheEntries.length; i++) {
/* 123 */       FieldCache.CacheEntry item = cacheEntries[i];
/* 124 */       Object val = item.getValue();
/*     */ 
/* 126 */       if (!(val instanceof FieldCache.CreationPlaceholder))
/*     */       {
/* 129 */         ReaderField rf = new ReaderField(item.getReaderKey(), item.getFieldName());
/*     */ 
/* 132 */         Integer valId = Integer.valueOf(System.identityHashCode(val));
/*     */ 
/* 135 */         valIdToItems.put(valId, item);
/* 136 */         if (1 < readerFieldToValIds.put(rf, valId)) {
/* 137 */           valMismatchKeys.add(rf);
/*     */         }
/*     */       }
/*     */     }
/* 141 */     List insanity = new ArrayList(valMismatchKeys.size() * 3);
/*     */ 
/* 143 */     insanity.addAll(checkValueMismatch(valIdToItems, readerFieldToValIds, valMismatchKeys));
/*     */ 
/* 146 */     insanity.addAll(checkSubreaders(valIdToItems, readerFieldToValIds));
/*     */ 
/* 149 */     return (Insanity[])insanity.toArray(new Insanity[insanity.size()]);
/*     */   }
/*     */ 
/*     */   private Collection<Insanity> checkValueMismatch(MapOfSets<Integer, FieldCache.CacheEntry> valIdToItems, MapOfSets<ReaderField, Integer> readerFieldToValIds, Set<ReaderField> valMismatchKeys)
/*     */   {
/* 163 */     List insanity = new ArrayList(valMismatchKeys.size() * 3);
/*     */     Map rfMap;
/*     */     Map valMap;
/* 165 */     if (!valMismatchKeys.isEmpty())
/*     */     {
/* 168 */       rfMap = readerFieldToValIds.getMap();
/* 169 */       valMap = valIdToItems.getMap();
/* 170 */       for (ReaderField rf : valMismatchKeys) {
/* 171 */         List badEntries = new ArrayList(valMismatchKeys.size() * 2);
/* 172 */         for (Integer value : (Set)rfMap.get(rf)) {
/* 173 */           for (FieldCache.CacheEntry cacheEntry : (Set)valMap.get(value)) {
/* 174 */             badEntries.add(cacheEntry);
/*     */           }
/*     */         }
/*     */ 
/* 178 */         FieldCache.CacheEntry[] badness = new FieldCache.CacheEntry[badEntries.size()];
/* 179 */         badness = (FieldCache.CacheEntry[])badEntries.toArray(badness);
/*     */ 
/* 181 */         insanity.add(new Insanity(InsanityType.VALUEMISMATCH, "Multiple distinct value objects for " + rf.toString(), badness));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 186 */     return insanity;
/*     */   }
/*     */ 
/*     */   private Collection<Insanity> checkSubreaders(MapOfSets<Integer, FieldCache.CacheEntry> valIdToItems, MapOfSets<ReaderField, Integer> readerFieldToValIds)
/*     */   {
/* 200 */     List insanity = new ArrayList(23);
/*     */ 
/* 202 */     Map badChildren = new HashMap(17);
/* 203 */     MapOfSets badKids = new MapOfSets(badChildren);
/*     */ 
/* 205 */     Map viToItemSets = valIdToItems.getMap();
/* 206 */     Map rfToValIdSets = readerFieldToValIds.getMap();
/*     */ 
/* 208 */     Set seen = new HashSet(17);
/*     */ 
/* 210 */     Set readerFields = rfToValIdSets.keySet();
/* 211 */     for (ReaderField rf : readerFields)
/*     */     {
/* 213 */       if (!seen.contains(rf))
/*     */       {
/* 215 */         List kids = getAllDecendentReaderKeys(rf.readerKey);
/* 216 */         for (Iterator i$ = kids.iterator(); i$.hasNext(); ) { Object kidKey = i$.next();
/* 217 */           ReaderField kid = new ReaderField(kidKey, rf.fieldName);
/*     */ 
/* 219 */           if (badChildren.containsKey(kid))
/*     */           {
/* 222 */             badKids.put(rf, kid);
/* 223 */             badKids.putAll(rf, (Collection)badChildren.get(kid));
/* 224 */             badChildren.remove(kid);
/*     */           }
/* 226 */           else if (rfToValIdSets.containsKey(kid))
/*     */           {
/* 228 */             badKids.put(rf, kid);
/*     */           }
/* 230 */           seen.add(kid);
/*     */         }
/* 232 */         seen.add(rf);
/*     */       }
/*     */     }
/*     */ 
/* 236 */     for (ReaderField parent : badChildren.keySet()) {
/* 237 */       Set kids = (Set)badChildren.get(parent);
/*     */ 
/* 239 */       List badEntries = new ArrayList(kids.size() * 2);
/*     */ 
/* 243 */       for (Integer value : (Set)rfToValIdSets.get(parent)) {
/* 244 */         badEntries.addAll((Collection)viToItemSets.get(value));
/*     */       }
/*     */ 
/* 249 */       for (ReaderField kid : kids) {
/* 250 */         for (Integer value : (Set)rfToValIdSets.get(kid)) {
/* 251 */           badEntries.addAll((Collection)viToItemSets.get(value));
/*     */         }
/*     */       }
/*     */ 
/* 255 */       FieldCache.CacheEntry[] badness = new FieldCache.CacheEntry[badEntries.size()];
/* 256 */       badness = (FieldCache.CacheEntry[])badEntries.toArray(badness);
/*     */ 
/* 258 */       insanity.add(new Insanity(InsanityType.SUBREADER, "Found caches for decendents of " + parent.toString(), badness));
/*     */     }
/*     */ 
/* 264 */     return insanity;
/*     */   }
/*     */ 
/*     */   private List getAllDecendentReaderKeys(Object seed)
/*     */   {
/* 274 */     List all = new ArrayList(17);
/* 275 */     all.add(seed);
/* 276 */     for (int i = 0; i < all.size(); i++) {
/* 277 */       Object obj = all.get(i);
/* 278 */       if ((obj instanceof IndexReader)) {
/* 279 */         IndexReader[] subs = ((IndexReader)obj).getSequentialSubReaders();
/* 280 */         for (int j = 0; (null != subs) && (j < subs.length); j++) {
/* 281 */           all.add(subs[j].getFieldCacheKey());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 287 */     return all.subList(1, all.size());
/*     */   }
/*     */ 
/*     */   public static final class InsanityType
/*     */   {
/*     */     private final String label;
/* 397 */     public static final InsanityType SUBREADER = new InsanityType("SUBREADER");
/*     */ 
/* 415 */     public static final InsanityType VALUEMISMATCH = new InsanityType("VALUEMISMATCH");
/*     */ 
/* 423 */     public static final InsanityType EXPECTED = new InsanityType("EXPECTED");
/*     */ 
/*     */     private InsanityType(String label)
/*     */     {
/* 388 */       this.label = label;
/*     */     }
/*     */     public String toString() {
/* 391 */       return this.label;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Insanity
/*     */   {
/*     */     private final FieldCacheSanityChecker.InsanityType type;
/*     */     private final String msg;
/*     */     private final FieldCache.CacheEntry[] entries;
/*     */ 
/*     */     public Insanity(FieldCacheSanityChecker.InsanityType type, String msg, FieldCache.CacheEntry[] entries)
/*     */     {
/* 328 */       if (null == type) {
/* 329 */         throw new IllegalArgumentException("Insanity requires non-null InsanityType");
/*     */       }
/*     */ 
/* 332 */       if ((null == entries) || (0 == entries.length)) {
/* 333 */         throw new IllegalArgumentException("Insanity requires non-null/non-empty CacheEntry[]");
/*     */       }
/*     */ 
/* 336 */       this.type = type;
/* 337 */       this.msg = msg;
/* 338 */       this.entries = entries;
/*     */     }
/*     */ 
/*     */     public FieldCacheSanityChecker.InsanityType getType()
/*     */     {
/* 344 */       return this.type;
/*     */     }
/*     */ 
/*     */     public String getMsg() {
/* 348 */       return this.msg;
/*     */     }
/*     */ 
/*     */     public FieldCache.CacheEntry[] getCacheEntries() {
/* 352 */       return this.entries;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 360 */       StringBuilder buf = new StringBuilder();
/* 361 */       buf.append(getType()).append(": ");
/*     */ 
/* 363 */       String m = getMsg();
/* 364 */       if (null != m) buf.append(m);
/*     */ 
/* 366 */       buf.append('\n');
/*     */ 
/* 368 */       FieldCache.CacheEntry[] ce = getCacheEntries();
/* 369 */       for (int i = 0; i < ce.length; i++) {
/* 370 */         buf.append('\t').append(ce[i].toString()).append('\n');
/*     */       }
/*     */ 
/* 373 */       return buf.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ReaderField
/*     */   {
/*     */     public final Object readerKey;
/*     */     public final String fieldName;
/*     */ 
/*     */     public ReaderField(Object readerKey, String fieldName)
/*     */     {
/* 297 */       this.readerKey = readerKey;
/* 298 */       this.fieldName = fieldName;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 302 */       return System.identityHashCode(this.readerKey) * this.fieldName.hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object that) {
/* 306 */       if (!(that instanceof ReaderField)) return false;
/*     */ 
/* 308 */       ReaderField other = (ReaderField)that;
/* 309 */       return (this.readerKey == other.readerKey) && (this.fieldName.equals(other.fieldName));
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 314 */       return this.readerKey.toString() + "+" + this.fieldName;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.FieldCacheSanityChecker
 * JD-Core Version:    0.6.2
 */