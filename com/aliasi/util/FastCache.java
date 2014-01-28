/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class FastCache<K, V> extends AbstractMap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3003326726041067827L;
/*     */   private static final double DEFAULT_LOAD_FACTOR = 0.5D;
/*     */   private final SoftReference<Record<K, V>>[] mBuckets;
/* 103 */   private volatile int mNumEntries = 0;
/*     */   private int mMaxEntries;
/*     */ 
/*     */   public FastCache(int size)
/*     */   {
/* 116 */     this(size, 0.5D);
/*     */   }
/*     */ 
/*     */   FastCache(int maxEntries, int numBuckets, boolean ignoreMe)
/*     */   {
/* 121 */     this.mMaxEntries = maxEntries;
/*     */ 
/* 124 */     SoftReference[] bucketsTemp = (SoftReference[])new SoftReference[numBuckets];
/*     */ 
/* 126 */     this.mBuckets = bucketsTemp;
/*     */   }
/*     */ 
/*     */   public FastCache(int size, double loadFactor)
/*     */   {
/* 142 */     if (size < 1) {
/* 143 */       String msg = "Cache size must be at least 1. Found cache size=" + size;
/*     */ 
/* 145 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 147 */     if ((loadFactor < 0.0D) || (Double.isNaN(loadFactor)) || (Double.isInfinite(loadFactor))) {
/* 148 */       String msg = "Load factor must be finite and positive. found loadFactor=" + loadFactor;
/*     */ 
/* 150 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 152 */     this.mMaxEntries = ((int)(loadFactor * size));
/* 153 */     if (this.mMaxEntries < 1) {
/* 154 */       String msg = "size * loadFactor must be > 0. Found size=" + size + " loadFactor=" + loadFactor;
/*     */ 
/* 157 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 161 */     SoftReference[] bucketsTemp = (SoftReference[])new SoftReference[size];
/*     */ 
/* 163 */     this.mBuckets = bucketsTemp;
/*     */   }
/*     */ 
/*     */   Record<K, V> getFirstRecord(int bucketId)
/*     */   {
/* 168 */     SoftReference ref = this.mBuckets[bucketId];
/* 169 */     return ref == null ? null : (Record)ref.get();
/*     */   }
/*     */ 
/*     */   void setFirstRecord(int bucketId, Record<K, V> record) {
/* 173 */     SoftReference ref = new SoftReference(record);
/* 174 */     this.mBuckets[bucketId] = ref;
/*     */   }
/*     */ 
/*     */   public V get(Object key)
/*     */   {
/* 192 */     int bucketId = bucketId(key);
/* 193 */     for (Record record = getFirstRecord(bucketId); 
/* 194 */       record != null; 
/* 195 */       record = record.mNextRecord)
/*     */     {
/* 197 */       if (record.mKey.equals(key)) {
/* 198 */         record.mCount += 1;
/* 199 */         return record.mValue;
/*     */       }
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */   int bucketId(Object key) {
/* 206 */     return Math.abs(key.hashCode() % this.mBuckets.length);
/*     */   }
/*     */ 
/*     */   public V put(K key, V value)
/*     */   {
/* 226 */     int bucketId = bucketId(key);
/* 227 */     Record firstRecord = getFirstRecord(bucketId);
/* 228 */     for (Record record = firstRecord; 
/* 229 */       record != null; 
/* 230 */       record = record.mNextRecord) {
/* 231 */       if (record.mKey.equals(key)) {
/* 232 */         record.mCount += 1;
/* 233 */         return null;
/*     */       }
/*     */     }
/* 236 */     prune();
/* 237 */     firstRecord = getFirstRecord(bucketId);
/* 238 */     Record record = new Record(key, value, firstRecord);
/* 239 */     setFirstRecord(bucketId, record);
/* 240 */     this.mNumEntries += 1;
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 248 */     synchronized (this) {
/* 249 */       for (SoftReference ref : this.mBuckets)
/* 250 */         if (ref != null)
/* 251 */           ref.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void prune()
/*     */   {
/* 265 */     synchronized (this) {
/* 266 */       if (this.mNumEntries < this.mMaxEntries) return;
/* 267 */       int count = 0;
/* 268 */       for (int i = 0; i < this.mBuckets.length; i++) {
/* 269 */         Record record = getFirstRecord(i);
/* 270 */         Record prunedRecord = prune(record);
/* 271 */         setFirstRecord(i, prunedRecord);
/* 272 */         for (Record r = prunedRecord; 
/* 273 */           r != null; 
/* 274 */           r = r.mNextRecord)
/* 275 */           count++;
/*     */       }
/* 277 */       this.mNumEntries = count;
/*     */     }
/*     */   }
/*     */ 
/*     */   final Record<K, V> prune(Record<K, V> inRecord) {
/* 282 */     Record record = inRecord;
/* 283 */     while ((record != null) && (record.mCount >>>= 1 == 0))
/* 284 */       record = record.mNextRecord;
/* 285 */     if (record == null) return null;
/* 286 */     record.mNextRecord = prune(record.mNextRecord);
/* 287 */     return record;
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 292 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<K, V>> entrySet()
/*     */   {
/* 305 */     HashSet entrySet = new HashSet();
/* 306 */     for (int i = 0; i < this.mBuckets.length; i++)
/* 307 */       for (Record record = getFirstRecord(i); 
/* 308 */         record != null; 
/* 309 */         record = record.mNextRecord)
/* 310 */         entrySet.add(record);
/* 311 */     return entrySet;
/*     */   }
/*     */ 
/*     */   static class Serializer<L, W> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 318542520894659209L;
/*     */     final FastCache<L, W> mCache;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 364 */       this(null);
/*     */     }
/*     */     public Serializer(FastCache<L, W> cache) {
/* 367 */       this.mCache = cache;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 371 */       int numBuckets = in.readInt();
/* 372 */       int maxEntries = in.readInt();
/* 373 */       int numEntries = in.readInt();
/* 374 */       FastCache cache = new FastCache(maxEntries, numBuckets, true);
/* 375 */       for (int i = 0; i < numEntries; i++)
/*     */       {
/* 377 */         Object l = in.readObject();
/*     */ 
/* 379 */         Object w = in.readObject();
/* 380 */         cache.put(l, w);
/*     */       }
/* 382 */       return cache;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 386 */       out.writeInt(this.mCache.mBuckets.length);
/* 387 */       out.writeInt(this.mCache.mMaxEntries);
/* 388 */       out.writeInt(this.mCache.size());
/* 389 */       for (Map.Entry entry : this.mCache.entrySet()) {
/* 390 */         out.writeObject(entry.getKey());
/* 391 */         out.writeObject(entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Record<K, V>
/*     */     implements Map.Entry<K, V>
/*     */   {
/*     */     final K mKey;
/*     */     final V mValue;
/*     */     volatile Record<K, V> mNextRecord;
/*     */     volatile int mCount;
/*     */ 
/*     */     Record(K key, V value)
/*     */     {
/* 320 */       this(key, value, null);
/*     */     }
/*     */     Record(K key, V value, Record<K, V> nextRecord) {
/* 323 */       this(key, value, nextRecord, 1);
/*     */     }
/*     */     Record(K key, V value, Record<K, V> nextRecord, int count) {
/* 326 */       this.mKey = key;
/* 327 */       this.mValue = value;
/* 328 */       this.mNextRecord = nextRecord;
/* 329 */       this.mCount = count;
/*     */     }
/*     */     public K getKey() {
/* 332 */       return this.mKey;
/*     */     }
/*     */     public V getValue() {
/* 335 */       return this.mValue;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 339 */       return (this.mKey == null ? 0 : this.mKey.hashCode()) ^ (this.mValue == null ? 0 : this.mValue.hashCode());
/*     */     }
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/* 345 */       if (!(o instanceof Map.Entry)) return false;
/* 346 */       Map.Entry e2 = (Map.Entry)o;
/* 347 */       return (this.mKey == null ? e2.getKey() == null : this.mKey.equals(e2.getKey())) && (this.mValue == null ? e2.getValue() == null : this.mValue.equals(e2.getValue()));
/*     */     }
/*     */ 
/*     */     public V setValue(V value)
/*     */     {
/* 355 */       String msg = "Cache records may not be set.";
/* 356 */       throw new UnsupportedOperationException(msg);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.FastCache
 * JD-Core Version:    0.6.2
 */