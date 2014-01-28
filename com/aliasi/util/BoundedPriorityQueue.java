/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class BoundedPriorityQueue<E> extends AbstractSet<E>
/*     */   implements Queue<E>, SortedSet<E>
/*     */ {
/*     */   final SortedSet<Entry<E>> mQueue;
/*     */   private int mMaxSize;
/*     */   private final Comparator<? super E> mComparator;
/*     */ 
/*     */   public BoundedPriorityQueue(Comparator<? super E> comparator, int maxSize)
/*     */   {
/*  95 */     if (maxSize < 1) {
/*  96 */       String msg = "Require maximum size >= 1. Found max size=" + maxSize;
/*     */ 
/*  98 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 100 */     this.mQueue = new TreeSet(new EntryComparator(null));
/* 101 */     this.mComparator = comparator;
/* 102 */     this.mMaxSize = maxSize;
/*     */   }
/*     */ 
/*     */   public E element()
/*     */   {
/* 113 */     Object result = peek();
/* 114 */     if (result == null)
/* 115 */       throw new NoSuchElementException();
/* 116 */     return result;
/*     */   }
/*     */ 
/*     */   public E poll()
/*     */   {
/* 131 */     return pop();
/*     */   }
/*     */ 
/*     */   public boolean offer(E o)
/*     */   {
/* 144 */     if (size() < this.mMaxSize)
/* 145 */       return this.mQueue.add(new Entry(o));
/* 146 */     Entry last = (Entry)this.mQueue.last();
/* 147 */     Object lastObj = last.mObject;
/* 148 */     if (this.mComparator.compare(o, lastObj) <= 0)
/* 149 */       return false;
/* 150 */     if (!this.mQueue.add(new Entry(o)))
/* 151 */       return false;
/* 152 */     this.mQueue.remove(last);
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   public E remove()
/*     */   {
/* 168 */     Object result = poll();
/* 169 */     if (result == null)
/* 170 */       throw new NoSuchElementException();
/* 171 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 183 */     return this.mQueue.isEmpty();
/*     */   }
/*     */ 
/*     */   public E peekLast()
/*     */   {
/* 196 */     if (isEmpty()) return null;
/* 197 */     return ((Entry)this.mQueue.last()).mObject;
/*     */   }
/*     */ 
/*     */   public E last()
/*     */   {
/* 207 */     if (isEmpty()) throw new NoSuchElementException();
/* 208 */     return ((Entry)this.mQueue.last()).mObject;
/*     */   }
/*     */ 
/*     */   public SortedSet<E> headSet(E toElement)
/*     */   {
/* 226 */     SortedSet result = new TreeSet();
/* 227 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 228 */       if (this.mComparator.compare(e, toElement) >= 0) break;
/* 229 */       result.add(e);
/*     */     }
/*     */ 
/* 233 */     return result;
/*     */   }
/*     */ 
/*     */   public SortedSet<E> tailSet(E fromElement)
/*     */   {
/* 252 */     SortedSet result = new TreeSet();
/* 253 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 254 */       if (this.mComparator.compare(e, fromElement) >= 0)
/* 255 */         result.add(e);
/*     */     }
/* 257 */     return result;
/*     */   }
/*     */ 
/*     */   public SortedSet<E> subSet(E fromElement, E toElement)
/*     */   {
/* 279 */     int c = this.mComparator.compare(fromElement, toElement);
/* 280 */     if (c >= 0) {
/* 281 */       String msg = "Lower bound must not be greater than the upper bound. Found fromElement=" + fromElement + " toElement=" + toElement;
/*     */ 
/* 284 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 286 */     SortedSet result = new TreeSet();
/* 287 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 288 */       if (this.mComparator.compare(e, fromElement) >= 0) {
/* 289 */         if (this.mComparator.compare(e, toElement) >= 0) break;
/* 290 */         result.add(e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 295 */     return result;
/*     */   }
/*     */ 
/*     */   public Comparator<? super E> comparator()
/*     */   {
/* 305 */     return this.mComparator;
/*     */   }
/*     */ 
/*     */   public E first()
/*     */   {
/* 315 */     if (isEmpty()) throw new NoSuchElementException();
/* 316 */     return ((Entry)this.mQueue.first()).mObject;
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 330 */     if (isEmpty()) return null;
/* 331 */     return ((Entry)this.mQueue.first()).mObject;
/*     */   }
/*     */ 
/*     */   E pop()
/*     */   {
/* 342 */     if (isEmpty()) return null;
/* 343 */     if (this.mQueue.isEmpty()) return null;
/* 344 */     Entry entry = (Entry)this.mQueue.first();
/* 345 */     this.mQueue.remove(entry);
/* 346 */     return entry.mObject;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object obj)
/*     */   {
/* 364 */     Object eObj = obj;
/* 365 */     Entry entry = new Entry(eObj, -1L);
/* 366 */     boolean result = this.mQueue.remove(entry);
/* 367 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> c)
/*     */   {
/* 372 */     boolean changed = false;
/* 373 */     for (Iterator it = iterator(); it.hasNext(); ) {
/* 374 */       if (c.contains(it.next())) {
/* 375 */         it.remove();
/* 376 */         changed = true;
/*     */       }
/*     */     }
/* 379 */     return changed;
/*     */   }
/*     */ 
/*     */   public void setMaxSize(int maxSize)
/*     */   {
/* 395 */     this.mMaxSize = maxSize;
/* 396 */     while (this.mQueue.size() > maxSize())
/* 397 */       this.mQueue.remove(this.mQueue.last());
/*     */   }
/*     */ 
/*     */   public boolean add(E o)
/*     */   {
/* 409 */     String msg = "Adds not supported by queue because cannot guarantee addition.\nUse offer() instead.";
/* 410 */     throw new UnsupportedOperationException(msg);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 419 */     this.mQueue.clear();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 430 */     return this.mQueue.size();
/*     */   }
/*     */ 
/*     */   public int maxSize()
/*     */   {
/* 439 */     return this.mMaxSize;
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 452 */     return new QueueIterator(this.mQueue.iterator());
/*     */   }
/*     */ 
/*     */   private static class QueueIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final Iterator<BoundedPriorityQueue.Entry<E>> mIterator;
/*     */ 
/*     */     QueueIterator(Iterator<BoundedPriorityQueue.Entry<E>> iterator)
/*     */     {
/* 495 */       this.mIterator = iterator;
/*     */     }
/*     */     public boolean hasNext() {
/* 498 */       return this.mIterator.hasNext();
/*     */     }
/*     */     public E next() {
/* 501 */       return BoundedPriorityQueue.Entry.access$100((BoundedPriorityQueue.Entry)this.mIterator.next());
/*     */     }
/*     */     public void remove() {
/* 504 */       this.mIterator.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Entry<E>
/*     */   {
/*     */     private final long mId;
/*     */     private final E mObject;
/* 485 */     private static long sNextId = 0L;
/*     */ 
/*     */     public Entry(E object)
/*     */     {
/* 476 */       this(object, nextId());
/*     */     }
/*     */     public Entry(E object, long id) {
/* 479 */       this.mObject = object;
/* 480 */       this.mId = id;
/*     */     }
/*     */     private static synchronized long nextId() {
/* 483 */       return sNextId++;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 488 */       return "qEntry(" + this.mObject.toString() + "," + this.mId + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EntryComparator
/*     */     implements Comparator<BoundedPriorityQueue.Entry<E>>
/*     */   {
/*     */     private EntryComparator()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int compare(BoundedPriorityQueue.Entry<E> entry1, BoundedPriorityQueue.Entry<E> entry2)
/*     */     {
/* 458 */       Object eObj1 = entry1.mObject;
/* 459 */       Object eObj2 = entry2.mObject;
/* 460 */       if (eObj1.equals(eObj2)) return 0;
/* 461 */       int comp = BoundedPriorityQueue.this.mComparator.compare(eObj1, eObj2);
/* 462 */       if (comp != 0) return -comp;
/*     */ 
/* 464 */       return entry1.mId < entry2.mId ? 1 : -1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.BoundedPriorityQueue
 * JD-Core Version:    0.6.2
 */