/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class ShortPriorityQueue<E> extends AbstractSet<E>
/*     */   implements Queue<E>, SortedSet<E>
/*     */ {
/*     */   private final Comparator<? super E> mComparator;
/*     */   private final E[] mElts;
/*  67 */   private int mSize = 0;
/*     */ 
/*     */   public ShortPriorityQueue(Comparator<? super E> comparator, int maxSize)
/*     */   {
/*  82 */     Object[] elts = (Object[])new Object[maxSize];
/*  83 */     this.mElts = elts;
/*  84 */     this.mComparator = comparator;
/*     */   }
/*     */ 
/*     */   public int maxSize()
/*     */   {
/*  93 */     return this.mElts.length;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 103 */     return this.mSize;
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/* 119 */     return Iterators.arraySlice(this.mElts, 0, this.mSize);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 130 */     for (int i = 0; i < this.mSize; i++)
/* 131 */       this.mElts[i] = null;
/* 132 */     this.mSize = 0;
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 147 */     return isEmpty() ? null : this.mElts[0];
/*     */   }
/*     */ 
/*     */   public E element()
/*     */   {
/* 163 */     Object result = peek();
/* 164 */     if (result == null)
/* 165 */       throw new NoSuchElementException("");
/* 166 */     return result;
/*     */   }
/*     */ 
/*     */   public E peekLast()
/*     */   {
/* 176 */     return isEmpty() ? null : this.mElts[(this.mSize - 1)];
/*     */   }
/*     */ 
/*     */   public E first()
/*     */   {
/* 187 */     if (isEmpty()) throw new NoSuchElementException();
/* 188 */     return this.mElts[0];
/*     */   }
/*     */ 
/*     */   public E last()
/*     */   {
/* 200 */     if (isEmpty()) throw new NoSuchElementException();
/* 201 */     return this.mElts[(this.mSize - 1)];
/*     */   }
/*     */ 
/*     */   public SortedSet<E> headSet(E toElement)
/*     */   {
/* 219 */     SortedSet result = new TreeSet();
/* 220 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 221 */       if (this.mComparator.compare(e, toElement) >= 0) break;
/* 222 */       result.add(e);
/*     */     }
/*     */ 
/* 226 */     return result;
/*     */   }
/*     */ 
/*     */   public SortedSet<E> tailSet(E fromElement)
/*     */   {
/* 245 */     SortedSet result = new TreeSet();
/* 246 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 247 */       if (this.mComparator.compare(e, fromElement) >= 0)
/* 248 */         result.add(e);
/*     */     }
/* 250 */     return result;
/*     */   }
/*     */ 
/*     */   public Comparator<? super E> comparator()
/*     */   {
/* 259 */     return this.mComparator;
/*     */   }
/*     */ 
/*     */   public SortedSet<E> subSet(E fromElement, E toElement)
/*     */   {
/* 281 */     int c = this.mComparator.compare(fromElement, toElement);
/* 282 */     if (c >= 0) {
/* 283 */       String msg = "Lower bound must not be greater than the upper bound. Found fromElement=" + fromElement + " toElement=" + toElement;
/*     */ 
/* 286 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 288 */     SortedSet result = new TreeSet();
/* 289 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 290 */       if (this.mComparator.compare(e, fromElement) >= 0) {
/* 291 */         if (this.mComparator.compare(e, toElement) >= 0) break;
/* 292 */         result.add(e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 297 */     return result;
/*     */   }
/*     */ 
/*     */   public E poll()
/*     */   {
/* 313 */     if (isEmpty()) return null;
/* 314 */     Object result = this.mElts[0];
/* 315 */     for (int i = 1; i < this.mSize; i++)
/* 316 */       this.mElts[(i - 1)] = this.mElts[i];
/* 317 */     this.mSize -= 1;
/* 318 */     return result;
/*     */   }
/*     */ 
/*     */   public E remove()
/*     */   {
/* 333 */     Object result = poll();
/* 334 */     if (result == null)
/* 335 */       throw new NoSuchElementException();
/* 336 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 346 */     return this.mSize == 0;
/*     */   }
/*     */ 
/*     */   public boolean offer(E elt)
/*     */   {
/* 360 */     if (this.mSize == this.mElts.length) {
/* 361 */       int c = this.mComparator.compare(this.mElts[(this.mElts.length - 1)], elt);
/* 362 */       if (c >= 0)
/* 363 */         return false;
/* 364 */       this.mElts[(this.mElts.length - 1)] = elt;
/*     */     }
/* 366 */     if (this.mSize < this.mElts.length) {
/* 367 */       this.mElts[this.mSize] = elt;
/* 368 */       this.mSize += 1;
/*     */     }
/* 370 */     int i = this.mSize - 1;
/*     */     while (true) { i--; if ((i < 0) || (this.mComparator.compare(this.mElts[i], this.mElts[(i + 1)]) >= 0)) break;
/* 371 */       Object temp = this.mElts[i];
/* 372 */       this.mElts[i] = this.mElts[(i + 1)];
/* 373 */       this.mElts[(i + 1)] = temp;
/*     */     }
/* 375 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean remove(Object obj)
/*     */   {
/* 391 */     for (int i = 0; i < this.mSize; i++) {
/* 392 */       if (obj.equals(this.mElts[i])) {
/* 393 */         for (i++; i < this.mSize; i++)
/* 394 */           this.mElts[(i - 1)] = this.mElts[i];
/* 395 */         this.mSize -= 1;
/* 396 */         return true;
/*     */       }
/*     */     }
/* 399 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 409 */     StringBuilder sb = new StringBuilder();
/* 410 */     sb.append("ShortPriorityQueue(comparator=" + this.mComparator.getClass());
/* 411 */     sb.append(" maxLength=" + this.mElts.length + ")\n");
/* 412 */     for (int i = 0; i < this.mSize; i++)
/* 413 */       sb.append("  [" + i + "]=" + this.mElts[i] + "\n");
/* 414 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.ShortPriorityQueue
 * JD-Core Version:    0.6.2
 */