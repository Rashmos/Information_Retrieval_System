/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class CompactHashSet<E> extends AbstractSet<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -2524057065260042957L;
/*     */   private E[] mBuckets;
/* 162 */   private int mSize = 0;
/*     */   static final float LOAD_FACTOR = 0.75F;
/*     */   static final float RESIZE_FACTOR = 1.5F;
/*     */ 
/*     */   public CompactHashSet(int initialCapacity)
/*     */   {
/* 173 */     if (initialCapacity < 1) {
/* 174 */       String msg = "Capacity must be positive. Found initialCapacity=" + initialCapacity;
/*     */ 
/* 176 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 178 */     alloc(initialCapacity);
/*     */   }
/*     */ 
/*     */   public CompactHashSet(E[] es)
/*     */   {
/* 188 */     this(1);
/* 189 */     for (Object e : es)
/* 190 */       add(e);
/*     */   }
/*     */ 
/*     */   public boolean add(E e)
/*     */   {
/* 208 */     if (e == null) {
/* 209 */       String msg = "Cannot add null to CompactHashSet";
/* 210 */       throw new NullPointerException(msg);
/*     */     }
/* 212 */     int slot = findSlot(e);
/* 213 */     if (this.mBuckets[slot] != null)
/* 214 */       return false;
/* 215 */     if (this.mSize + 1 >= 0.75F * this.mBuckets.length) {
/* 216 */       realloc();
/* 217 */       slot = findSlot(e);
/* 218 */       if (this.mBuckets[slot] != null)
/* 219 */         throw new IllegalStateException("");
/*     */     }
/* 221 */     this.mBuckets[slot] = e;
/* 222 */     this.mSize += 1;
/* 223 */     return true;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 235 */     alloc(1);
/*     */   }
/*     */ 
/*     */   public boolean contains(Object o)
/*     */   {
/* 249 */     if (o == null) {
/* 250 */       String msg = "Compact hash sets do not support null objects.";
/* 251 */       throw new NullPointerException(msg);
/*     */     }
/* 253 */     Object o2 = this.mBuckets[findSlot(o)];
/* 254 */     return (o2 != null) && (o.equals(o2));
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 275 */     return new BucketIterator();
/*     */   }
/*     */ 
/*     */   public boolean remove(Object o)
/*     */   {
/* 290 */     if (o == null) {
/* 291 */       return false;
/*     */     }
/* 293 */     int slot = findSlot(o);
/* 294 */     if (this.mBuckets[slot] == null)
/* 295 */       return false;
/* 296 */     this.mBuckets[slot] = null;
/* 297 */     tampCollisions(slot);
/* 298 */     this.mSize -= 1;
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   void tampCollisions(int index)
/*     */   {
/* 304 */     for (int i = nextIndex(index); this.mBuckets[i] != null; i = nextIndex(i)) {
/* 305 */       int slot = findSlot(this.mBuckets[i]);
/* 306 */       if (slot != i) {
/* 307 */         this.mBuckets[slot] = this.mBuckets[i];
/* 308 */         this.mBuckets[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> collection)
/*     */   {
/* 333 */     boolean modified = false;
/* 334 */     for (Iterator i$ = collection.iterator(); i$.hasNext(); ) { Object o = i$.next();
/* 335 */       if (remove(o))
/* 336 */         modified = true; }
/* 337 */     return modified;
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> collection)
/*     */   {
/* 363 */     boolean modified = false;
/* 364 */     for (int i = 0; i < this.mBuckets.length; i++) {
/* 365 */       if ((this.mBuckets[i] != null) && (collection.contains(this.mBuckets[i]))) {
/* 366 */         modified = true;
/* 367 */         this.mBuckets[i] = null;
/* 368 */         tampCollisions(i);
/* 369 */         this.mSize -= 1;
/*     */       }
/*     */     }
/* 372 */     return modified;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 382 */     return this.mSize;
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 398 */     Object[] result = new Object[this.mSize];
/* 399 */     int nextIndex = 0;
/* 400 */     for (int i = 0; i < this.mBuckets.length; i++)
/* 401 */       if (this.mBuckets[i] != null)
/* 402 */         result[(nextIndex++)] = this.mBuckets[i];
/* 403 */     return result;
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] array)
/*     */   {
/* 431 */     Object[] result = array.length >= this.mSize ? array : (Object[])Array.newInstance(array.getClass().getComponentType(), this.mSize);
/*     */ 
/* 436 */     int nextIndex = 0;
/* 437 */     for (int i = 0; i < this.mBuckets.length; i++) {
/* 438 */       if (this.mBuckets[i] != null)
/*     */       {
/* 440 */         Object next = this.mBuckets[i];
/* 441 */         result[(nextIndex++)] = next;
/*     */       }
/*     */     }
/* 444 */     if (result.length > this.mSize)
/* 445 */       result[this.mSize] = null;
/* 446 */     return result;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 450 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   int findSlot(Object e) {
/* 454 */     for (int i = firstIndex(e); ; i = nextIndex(i)) {
/* 455 */       if (this.mBuckets[i] == null)
/* 456 */         return i;
/* 457 */       if (this.mBuckets[i].equals(e))
/* 458 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   int firstIndex(Object e) {
/* 463 */     return Math.abs(supplementalHash(e.hashCode())) % this.mBuckets.length;
/*     */   }
/*     */ 
/*     */   int nextIndex(int index) {
/* 467 */     return (index + 1) % this.mBuckets.length;
/*     */   }
/*     */ 
/*     */   void alloc(int capacity) {
/* 471 */     if (capacity < 0) {
/* 472 */       String msg = "Capacity must be non-negative. Found capacity=" + capacity;
/*     */ 
/* 474 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 477 */     Object[] buckets = (Object[])new Object[capacity];
/* 478 */     this.mBuckets = buckets;
/* 479 */     this.mSize = 0;
/*     */   }
/*     */ 
/*     */   void realloc() {
/* 483 */     Object[] oldBuckets = this.mBuckets;
/* 484 */     long capacity = Math.max(()(1.5F * this.mBuckets.length), this.mBuckets.length + 1);
/*     */ 
/* 486 */     if (capacity > 2147483647L) {
/* 487 */       String msg = "Not enough room to resize. Last capacity=" + this.mBuckets.length + " Failed New capacity=" + capacity;
/*     */ 
/* 490 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 492 */     alloc((int)capacity);
/* 493 */     for (int i = 0; i < oldBuckets.length; i++)
/* 494 */       if (oldBuckets[i] != null)
/* 495 */         add(oldBuckets[i]);
/*     */   }
/*     */ 
/*     */   static int supplementalHash(int n)
/*     */   {
/* 530 */     int n2 = n ^ n >>> 20 ^ n >>> 12;
/* 531 */     return n2 ^ n2 >>> 7 ^ n2 >>> 4;
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 7799253382220016205L;
/*     */     final CompactHashSet<F> mSet;
/*     */ 
/* 538 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(CompactHashSet<F> set) {
/* 541 */       this.mSet = set;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 545 */       out.writeInt(this.mSet.mBuckets.length);
/* 546 */       out.writeInt(this.mSet.size());
/* 547 */       for (int i = 0; i < this.mSet.mBuckets.length; i++)
/* 548 */         if (this.mSet.mBuckets[i] != null)
/* 549 */           out.writeObject(this.mSet.mBuckets[i]);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 556 */       int capacity = in.readInt();
/* 557 */       int size = in.readInt();
/* 558 */       CompactHashSet result = new CompactHashSet(capacity);
/* 559 */       for (int i = 0; i < size; i++)
/*     */       {
/* 561 */         Object f = in.readObject();
/* 562 */         result.add(f);
/*     */       }
/* 564 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */   class BucketIterator
/*     */     implements Iterator<E>
/*     */   {
/* 499 */     int mNextBucket = 0;
/* 500 */     int mRemoveIndex = -1;
/*     */ 
/*     */     BucketIterator() {  } 
/* 502 */     public boolean hasNext() { for (; this.mNextBucket < CompactHashSet.this.mBuckets.length; this.mNextBucket += 1)
/* 503 */         if (CompactHashSet.this.mBuckets[this.mNextBucket] != null)
/* 504 */           return true;
/* 505 */       return false; }
/*     */ 
/*     */     public E next() {
/* 508 */       if (!hasNext()) {
/* 509 */         throw new NoSuchElementException();
/*     */       }
/* 511 */       this.mRemoveIndex = (this.mNextBucket++);
/* 512 */       return CompactHashSet.this.mBuckets[this.mRemoveIndex];
/*     */     }
/*     */     public void remove() {
/* 515 */       if (this.mRemoveIndex == -1)
/* 516 */         throw new IllegalStateException();
/* 517 */       CompactHashSet.this.mBuckets[this.mRemoveIndex] = null;
/* 518 */       CompactHashSet.access$106(CompactHashSet.this);
/* 519 */       this.mRemoveIndex = -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.CompactHashSet
 * JD-Core Version:    0.6.2
 */