/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class HardFastCache<K, V> extends AbstractMap<K, V>
/*     */ {
/*     */   private static final double DEFAULT_LOAD_FACTOR = 0.5D;
/*     */   private final Record<K, V>[] mBuckets;
/*  75 */   private volatile int mNumEntries = 0;
/*     */   private int mMaxEntries;
/*     */ 
/*     */   public HardFastCache(int size)
/*     */   {
/*  87 */     this(size, 0.5D);
/*     */   }
/*     */ 
/*     */   public HardFastCache(int size, double loadFactor)
/*     */   {
/* 102 */     this.mMaxEntries = ((int)(loadFactor * size));
/* 103 */     if (this.mMaxEntries < 1) {
/* 104 */       String msg = "size * loadFactor must be > 0. Found size=" + size + " loadFactor=" + loadFactor;
/*     */ 
/* 107 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 111 */     Record[] bucketsTemp = (Record[])new Record[size];
/* 112 */     this.mBuckets = bucketsTemp;
/*     */   }
/*     */ 
/*     */   public V get(Object key)
/*     */   {
/* 130 */     int bucketId = bucketId(key);
/* 131 */     for (Record record = this.mBuckets[bucketId]; 
/* 132 */       record != null; 
/* 133 */       record = record.mNextRecord)
/*     */     {
/* 135 */       if (record.mKey.equals(key)) {
/* 136 */         record.mCount += 1;
/* 137 */         return record.mValue;
/*     */       }
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   int bucketId(Object key) {
/* 144 */     return Math.abs(key.hashCode() % this.mBuckets.length);
/*     */   }
/*     */ 
/*     */   public V put(K key, V value)
/*     */   {
/* 164 */     int bucketId = bucketId(key);
/* 165 */     Record firstRecord = this.mBuckets[bucketId];
/* 166 */     for (Record record = firstRecord; 
/* 167 */       record != null; 
/* 168 */       record = record.mNextRecord) {
/* 169 */       if (record.mKey.equals(key)) {
/* 170 */         record.mCount += 1;
/* 171 */         return null;
/*     */       }
/*     */     }
/* 174 */     prune();
/* 175 */     Record record = new Record(key, value, firstRecord);
/* 176 */     this.mBuckets[bucketId] = record;
/* 177 */     this.mNumEntries += 1;
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */   public void prune()
/*     */   {
/* 192 */     synchronized (this) {
/* 193 */       if (this.mNumEntries < this.mMaxEntries) return;
/* 194 */       int count = 0;
/* 195 */       for (int i = 0; i < this.mBuckets.length; i++) {
/* 196 */         Record record = this.mBuckets[i];
/* 197 */         Record prunedRecord = prune(record);
/* 198 */         this.mBuckets[i] = prunedRecord;
/* 199 */         for (Record r = prunedRecord; 
/* 200 */           r != null; 
/* 201 */           r = r.mNextRecord)
/* 202 */           count++;
/*     */       }
/* 204 */       this.mNumEntries = count;
/*     */     }
/*     */   }
/*     */ 
/*     */   final Record<K, V> prune(Record<K, V> inRecord) {
/* 209 */     Record record = inRecord;
/* 210 */     while ((record != null) && (record.mCount >>>= 1 == 0))
/* 211 */       record = record.mNextRecord;
/* 212 */     if (record == null) return null;
/* 213 */     record.mNextRecord = prune(record.mNextRecord);
/* 214 */     return record;
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<K, V>> entrySet()
/*     */   {
/* 226 */     HashSet entrySet = new HashSet();
/* 227 */     for (int i = 0; i < this.mBuckets.length; i++)
/* 228 */       for (Record record = this.mBuckets[i]; 
/* 229 */         record != null; 
/* 230 */         record = record.mNextRecord)
/* 231 */         entrySet.add(record);
/* 232 */     return entrySet; } 
/*     */   static final class Record<K, V> implements Map.Entry<K, V> { final K mKey;
/*     */     final V mValue;
/*     */     Record<K, V> mNextRecord;
/*     */     int mCount;
/*     */ 
/* 241 */     Record(K key, V value) { this(key, value, null); }
/*     */ 
/*     */     Record(K key, V value, Record<K, V> nextRecord) {
/* 244 */       this(key, value, nextRecord, 1);
/*     */     }
/*     */     Record(K key, V value, Record<K, V> nextRecord, int count) {
/* 247 */       this.mKey = key;
/* 248 */       this.mValue = value;
/* 249 */       this.mNextRecord = nextRecord;
/* 250 */       this.mCount = count;
/*     */     }
/*     */     public K getKey() {
/* 253 */       return this.mKey;
/*     */     }
/*     */     public V getValue() {
/* 256 */       return this.mValue;
/*     */     }
/*     */     public V setValue(V value) {
/* 259 */       String msg = "Cache records may not be set.";
/* 260 */       throw new UnsupportedOperationException(msg);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 265 */       return (this.mKey == null ? 0 : this.mKey.hashCode()) ^ (this.mValue == null ? 0 : this.mValue.hashCode());
/*     */     }
/*     */ 
/*     */     public boolean equals(Object o)
/*     */     {
/* 271 */       if (!(o instanceof Map.Entry)) return false;
/* 272 */       Map.Entry e2 = (Map.Entry)o;
/* 273 */       return (this.mKey == null ? e2.getKey() == null : this.mKey.equals(e2.getKey())) && (this.mValue == null ? e2.getValue() == null : this.mValue.equals(e2.getValue()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.HardFastCache
 * JD-Core Version:    0.6.2
 */