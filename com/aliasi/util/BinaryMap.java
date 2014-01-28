/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BinaryMap<E> extends AbstractMap<E, Integer>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -6965494782866980478L;
/*     */   private final Set<E> mPositiveSet;
/* 488 */   public static final Integer ONE = Integer.valueOf(1);
/*     */ 
/* 490 */   static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/*     */ 
/*     */   public BinaryMap()
/*     */   {
/*  90 */     this(1);
/*     */   }
/*     */ 
/*     */   public BinaryMap(int initialCapacity)
/*     */   {
/* 109 */     this(new CompactHashSet(initialCapacity));
/*     */   }
/*     */ 
/*     */   public BinaryMap(Set<E> positiveSet)
/*     */   {
/* 120 */     this.mPositiveSet = positiveSet;
/*     */   }
/*     */ 
/*     */   public boolean add(E e)
/*     */   {
/* 136 */     return this.mPositiveSet.add(e);
/*     */   }
/*     */ 
/*     */   public Set<E> keySet()
/*     */   {
/* 162 */     return this.mPositiveSet;
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<E, Integer>> entrySet()
/*     */   {
/* 187 */     return new EntrySet();
/*     */   }
/*     */ 
/*     */   public Integer get(Object key)
/*     */   {
/* 204 */     return this.mPositiveSet.contains(key) ? ONE : null;
/*     */   }
/*     */ 
/*     */   public Integer remove(Object key)
/*     */   {
/* 221 */     return this.mPositiveSet.remove(key) ? ONE : null;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 233 */     return this.mPositiveSet.size();
/*     */   }
/*     */ 
/*     */   public Collection<Integer> values()
/*     */   {
/* 244 */     return new Values();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 258 */     this.mPositiveSet.clear();
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object o)
/*     */   {
/* 276 */     return this.mPositiveSet.contains(o);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object o)
/*     */   {
/* 292 */     return (ONE.equals(o)) && (!isEmpty());
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 302 */     return this.mPositiveSet.isEmpty();
/*     */   }
/*     */ 
/*     */   public Integer put(E e, Integer n)
/*     */   {
/* 317 */     if (!ONE.equals(n))
/* 318 */       throw new IllegalArgumentException();
/* 319 */     return this.mPositiveSet.add(e) ? null : ONE;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 323 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer<F> extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -1922361159364699771L;
/*     */     final BinaryMap<F> mMap;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 497 */       this(null);
/*     */     }
/*     */     public Serializer(BinaryMap<F> map) {
/* 500 */       this.mMap = map;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 504 */       out.writeObject(this.mMap.mPositiveSet);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException
/*     */     {
/* 509 */       Set positiveSet = (Set)in.readObject();
/* 510 */       return new BinaryMap(positiveSet);
/*     */     }
/*     */   }
/*     */ 
/*     */   class EntrySetIterator
/*     */     implements Iterator<Map.Entry<E, Integer>>
/*     */   {
/*     */     private final Iterator<E> mIterator;
/*     */ 
/*     */     EntrySetIterator()
/*     */     {
/* 439 */       this.mIterator = BinaryMap.this.mPositiveSet.iterator();
/*     */     }
/* 441 */     public boolean hasNext() { return this.mIterator.hasNext(); }
/*     */ 
/*     */     public Map.Entry<E, Integer> next() {
/* 444 */       return new PositiveMapEntry();
/*     */     }
/*     */     public void remove() {
/* 447 */       this.mIterator.remove();
/*     */     }
/*     */ 
/*     */     class PositiveMapEntry implements Map.Entry<E, Integer> {
/* 451 */       private final E mE = BinaryMap.EntrySetIterator.this.mIterator.next();
/*     */ 
/*     */       PositiveMapEntry() {  } 
/* 453 */       public E getKey() { return this.mE; }
/*     */ 
/*     */       public Integer getValue() {
/* 456 */         return BinaryMap.ONE;
/*     */       }
/*     */       public Integer setValue(Integer value) {
/* 459 */         throw new UnsupportedOperationException();
/*     */       }
/*     */       public boolean equals(Object that) {
/* 462 */         if (!(that instanceof Map.Entry)) {
/* 463 */           return false;
/*     */         }
/* 465 */         Map.Entry e1 = (Map.Entry)that;
/* 466 */         Map.Entry e2 = this;
/* 467 */         return (e1.getKey() == null ? e2.getKey() == null : e1.getKey().equals(e2.getKey())) && (e1.getValue() == null ? e2.getValue() == null : e1.getValue().equals(e2.getValue()));
/*     */       }
/*     */ 
/*     */       public int hashCode()
/*     */       {
/* 477 */         return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   class EntrySet extends AbstractSet<Map.Entry<E, Integer>>
/*     */   {
/*     */     EntrySet()
/*     */     {
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 404 */       return BinaryMap.this.mPositiveSet.size();
/*     */     }
/*     */ 
/*     */     public Iterator<Map.Entry<E, Integer>> iterator() {
/* 408 */       return new BinaryMap.EntrySetIterator(BinaryMap.this);
/*     */     }
/*     */ 
/*     */     public void clear() {
/* 412 */       BinaryMap.this.mPositiveSet.clear();
/*     */     }
/*     */ 
/*     */     public boolean contains(Object o) {
/* 416 */       if (!(o instanceof Map.Entry)) {
/* 417 */         return false;
/*     */       }
/* 419 */       Map.Entry entry = (Map.Entry)o;
/* 420 */       return (BinaryMap.ONE.equals(entry.getValue())) && (BinaryMap.this.mPositiveSet.contains(entry.getKey()));
/*     */     }
/*     */ 
/*     */     public boolean isEmpty()
/*     */     {
/* 425 */       return BinaryMap.this.mPositiveSet.isEmpty();
/*     */     }
/*     */ 
/*     */     public boolean remove(Object o) {
/* 429 */       if (!(o instanceof Map.Entry)) {
/* 430 */         return false;
/*     */       }
/* 432 */       Map.Entry entry = (Map.Entry)o;
/* 433 */       return (BinaryMap.ONE.equals(entry.getValue())) && (BinaryMap.this.mPositiveSet.remove(entry.getKey()));
/*     */     }
/*     */   }
/*     */ 
/*     */   class ValuesIterator
/*     */     implements Iterator<Integer>
/*     */   {
/* 380 */     boolean finished = BinaryMap.this.mPositiveSet.isEmpty();
/* 381 */     boolean mayRemove = false;
/*     */ 
/*     */     ValuesIterator() {  } 
/* 383 */     public boolean hasNext() { return !this.finished; }
/*     */ 
/*     */     public Integer next() {
/* 386 */       if (!hasNext())
/* 387 */         throw new NoSuchElementException();
/* 388 */       this.finished = true;
/* 389 */       this.mayRemove = true;
/* 390 */       return BinaryMap.ONE;
/*     */     }
/*     */     public void remove() {
/* 393 */       if (!this.mayRemove)
/* 394 */         throw new IllegalStateException();
/* 395 */       this.mayRemove = false;
/* 396 */       BinaryMap.this.mPositiveSet.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   class Values extends AbstractCollection<Integer>
/*     */   {
/*     */     Values()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator<Integer> iterator()
/*     */     {
/* 330 */       return new BinaryMap.ValuesIterator(BinaryMap.this);
/*     */     }
/*     */ 
/*     */     public int size() {
/* 334 */       return isEmpty() ? 0 : 1;
/*     */     }
/*     */ 
/*     */     public void clear() {
/* 338 */       BinaryMap.this.mPositiveSet.clear();
/*     */     }
/*     */ 
/*     */     public boolean contains(Object o) {
/* 342 */       return (BinaryMap.ONE.equals(o)) && (!isEmpty());
/*     */     }
/*     */ 
/*     */     public boolean isEmpty() {
/* 346 */       return BinaryMap.this.mPositiveSet.isEmpty();
/*     */     }
/*     */ 
/*     */     public boolean remove(Object o) {
/* 350 */       if (!BinaryMap.ONE.equals(o))
/* 351 */         return false;
/* 352 */       boolean removedSomething = !isEmpty();
/* 353 */       BinaryMap.this.mPositiveSet.clear();
/* 354 */       return removedSomething;
/*     */     }
/*     */ 
/*     */     public boolean removeAll(Collection<?> c) {
/* 358 */       if (!c.contains(BinaryMap.ONE))
/* 359 */         return false;
/* 360 */       boolean removedSomething = !isEmpty();
/* 361 */       BinaryMap.this.mPositiveSet.clear();
/* 362 */       return removedSomething;
/*     */     }
/*     */ 
/*     */     public boolean retainAll(Collection<?> c) {
/* 366 */       if (isEmpty()) return false;
/* 367 */       if (c.contains(BinaryMap.ONE)) return false;
/* 368 */       BinaryMap.this.mPositiveSet.clear();
/* 369 */       return true;
/*     */     }
/*     */ 
/*     */     public Object[] toArray() {
/* 373 */       return new Object[] { isEmpty() ? BinaryMap.EMPTY_OBJECT_ARRAY : BinaryMap.ONE };
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.BinaryMap
 * JD-Core Version:    0.6.2
 */