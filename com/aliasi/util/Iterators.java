/*      */ package com.aliasi.util;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ public class Iterators
/*      */ {
/*   41 */   static final Iterator<Object> EMPTY_ITERATOR = new Empty();
/*      */ 
/*      */   public static <E> Iterator<E> empty()
/*      */   {
/*   53 */     Iterator emptyIterator = EMPTY_ITERATOR;
/*   54 */     return emptyIterator;
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> singleton(E e)
/*      */   {
/*   67 */     Iterator singletonIterator = new Singleton(e);
/*   68 */     return singletonIterator;
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> pair(E e1, E e2)
/*      */   {
/*   84 */     return new Pair(e1, e2);
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> list(E[] es)
/*      */   {
/*   99 */     return Arrays.asList(es).iterator();
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> unmodifiable(Iterator<E> it)
/*      */   {
/*  112 */     Iterator mIt = it;
/*  113 */     return new Iterator() {
/*      */       public boolean hasNext() {
/*  115 */         return this.val$mIt.hasNext();
/*      */       }
/*      */       public E next() {
/*  118 */         return this.val$mIt.next();
/*      */       }
/*      */       public void remove() {
/*  121 */         String msg = "Cannot remove from an unmodifiable iterator.";
/*  122 */         throw new UnsupportedOperationException(msg);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> sequence(Iterator<? extends E> it1, Iterator<? extends E> it2)
/*      */   {
/*  140 */     Iterator result = new Sequence(it1, it2);
/*  141 */     return result;
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> sequence(List<? extends Iterator<? extends E>> iterators)
/*      */   {
/*  146 */     Iterator[] elts = (Iterator[])iterators.toArray(new Iterator[0]);
/*      */ 
/*  148 */     Iterator it = new Sequence(elts);
/*  149 */     return it;
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> sequence(Iterator<? extends Iterator<? extends E>> iteratorOfIterators)
/*      */   {
/*  155 */     Iterator it = new Sequence(iteratorOfIterators);
/*  156 */     return it;
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> array(E[] members)
/*      */   {
/*  176 */     return new Array(members);
/*      */   }
/*      */ 
/*      */   public static <E> Iterator<E> arraySlice(E[] members, int start, int length)
/*      */   {
/*  199 */     return new ArraySlice(members, start, length);
/*      */   }
/*      */ 
/*      */   public static PrimitiveInt intRange(int end)
/*      */   {
/* 1030 */     return new IntRange(0, end);
/*      */   }
/*      */ 
/*      */   public static PrimitiveInt intRange(int start, int end)
/*      */   {
/* 1045 */     return new IntRange(start, end);
/*      */   }
/*      */ 
/*      */   public static PrimitiveInt intArray(int[] members)
/*      */   {
/* 1060 */     return new IntArray(members);
/*      */   }
/*      */ 
/*      */   static class IntArray extends Iterators.PrimitiveInt
/*      */   {
/*      */     private final int[] mMembers;
/*      */     private int mPosition;
/*      */ 
/*      */     public IntArray(int[] members)
/*      */     {
/* 1105 */       this.mMembers = members;
/*      */     }
/*      */     public boolean hasNext() {
/* 1108 */       return this.mPosition < this.mMembers.length;
/*      */     }
/*      */     public int nextPrimitive() {
/* 1111 */       if (!hasNext())
/* 1112 */         throw new NoSuchElementException();
/* 1113 */       return this.mMembers[(this.mPosition++)];
/*      */     }
/*      */   }
/*      */ 
/*      */   static class IntRange extends Iterators.PrimitiveInt
/*      */   {
/*      */     private int mCur;
/*      */     private final int mEnd;
/*      */ 
/*      */     public IntRange(int start, int end)
/*      */     {
/* 1075 */       if (end < start) {
/* 1076 */         String msg = "End point must be >= start point. Found start=" + start + " end=" + end;
/*      */ 
/* 1079 */         throw new IllegalArgumentException(msg);
/*      */       }
/* 1081 */       this.mCur = start;
/* 1082 */       this.mEnd = end;
/*      */     }
/*      */     public boolean hasNext() {
/* 1085 */       return this.mCur < this.mEnd;
/*      */     }
/*      */     public int nextPrimitive() {
/* 1088 */       if (!hasNext())
/* 1089 */         throw new NoSuchElementException();
/* 1090 */       return this.mCur++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class PrimitiveInt
/*      */     implements Iterator<Integer>
/*      */   {
/*      */     public abstract int nextPrimitive();
/*      */ 
/*      */     public abstract boolean hasNext();
/*      */ 
/*      */     public void remove()
/*      */     {
/* 1004 */       String msg = "Iterators.PrimitiveInt does not support remove.";
/* 1005 */       throw new UnsupportedOperationException(msg);
/*      */     }
/*      */ 
/*      */     public Integer next()
/*      */     {
/* 1017 */       return Integer.valueOf(nextPrimitive());
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Sequence<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Iterator<? extends Iterator<? extends E>> mIterators;
/*      */     private Iterator<? extends E> mCurrentIterator;
/*      */     private Iterator<? extends E> mLastIteratorNextCalled;
/*      */ 
/*      */     public Sequence(Iterator<? extends E> iterator1, Iterator<? extends E> iterator2)
/*      */     {
/*  858 */       this(toIteratorIterator(iterator1, iterator2));
/*      */     }
/*      */ 
/*      */     public Sequence(Iterator<? extends E>[] iterators)
/*      */     {
/*  869 */       this(new Iterators.Array(iterators));
/*      */     }
/*      */ 
/*      */     public Sequence(Iterator<? extends Iterator<? extends E>> iteratorOfIterators)
/*      */     {
/*  882 */       this.mIterators = iteratorOfIterators;
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  897 */       if (this.mCurrentIterator == null)
/*  898 */         nextIterator();
/*  899 */       for (; this.mCurrentIterator != null; nextIterator())
/*  900 */         if (this.mCurrentIterator.hasNext())
/*  901 */           return true;
/*  902 */       return false;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  915 */       if (!hasNext())
/*  916 */         throw new NoSuchElementException();
/*  917 */       this.mLastIteratorNextCalled = this.mCurrentIterator;
/*  918 */       return this.mCurrentIterator.next();
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  932 */       if (this.mLastIteratorNextCalled == null)
/*  933 */         throw new IllegalStateException("next() not yet called.");
/*  934 */       this.mLastIteratorNextCalled.remove();
/*  935 */       this.mLastIteratorNextCalled = null;
/*      */     }
/*      */ 
/*      */     private void nextIterator()
/*      */     {
/*  940 */       this.mCurrentIterator = (this.mIterators.hasNext() ? (Iterator)this.mIterators.next() : null);
/*      */     }
/*      */ 
/*      */     static <E> Iterator<? extends Iterator<? extends E>> toIteratorIterator(Iterator<? extends E> it1, Iterator<? extends E> it2)
/*      */     {
/*  950 */       ArrayList list = new ArrayList(2);
/*      */ 
/*  952 */       list.add(it1);
/*  953 */       list.add(it2);
/*  954 */       return list.iterator();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Buffered<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private E mNext;
/*      */ 
/*      */     protected abstract E bufferNext();
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  787 */       return (this.mNext != null) || ((this.mNext = bufferNext()) != null);
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  798 */       if (!hasNext())
/*  799 */         throw new NoSuchElementException();
/*  800 */       Object result = this.mNext;
/*  801 */       this.mNext = null;
/*  802 */       return result;
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  812 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class Modifier<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> mIt;
/*      */ 
/*      */     public Modifier(Iterator<? extends E> it)
/*      */     {
/*  699 */       this.mIt = it;
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  707 */       this.mIt.remove();
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  719 */       return this.mIt.hasNext();
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  731 */       return modify(this.mIt.next());
/*      */     }
/*      */ 
/*      */     public abstract E modify(E paramE);
/*      */   }
/*      */ 
/*      */   public static abstract class Filter<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> mIterator;
/*  591 */     private boolean mFoundNext = false;
/*      */     private E mNext;
/*      */ 
/*      */     public Filter(Iterator<? extends E> iterator)
/*      */     {
/*  600 */       this.mIterator = iterator;
/*      */     }
/*      */ 
/*      */     public abstract boolean accept(E paramE);
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  625 */       if (this.mFoundNext) return true;
/*  626 */       while (this.mIterator.hasNext()) {
/*  627 */         Object y = this.mIterator.next();
/*  628 */         if (accept(y)) {
/*  629 */           this.mFoundNext = true;
/*  630 */           this.mNext = y;
/*  631 */           return true;
/*      */         }
/*      */       }
/*  634 */       return false;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  648 */       if (!hasNext())
/*  649 */         throw new NoSuchElementException();
/*  650 */       this.mFoundNext = false;
/*  651 */       Object result = this.mNext;
/*  652 */       this.mNext = null;
/*  653 */       return result;
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  670 */       String msg = "Cannot remove from a filtered iterator.";
/*  671 */       throw new UnsupportedOperationException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Pair<E>
/*      */     implements Iterator<E>
/*      */   {
/*  510 */     private int mMembersReturned = 0;
/*      */     private E mMember1;
/*      */     private E mMember2;
/*      */ 
/*      */     public Pair(E member1, E member2)
/*      */     {
/*  523 */       this.mMember1 = member1;
/*  524 */       this.mMember2 = member2;
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  535 */       return this.mMembersReturned < 2;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  545 */       if (this.mMembersReturned == 0) {
/*  546 */         this.mMembersReturned += 1;
/*  547 */         Object result1 = this.mMember1;
/*  548 */         this.mMember1 = null;
/*  549 */         return result1;
/*      */       }
/*  551 */       if (this.mMembersReturned == 1) {
/*  552 */         this.mMembersReturned += 1;
/*  553 */         Object result2 = this.mMember2;
/*  554 */         this.mMember2 = null;
/*  555 */         return result2;
/*      */       }
/*  557 */       throw new NoSuchElementException();
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  566 */       String msg = "This iterator does not support remove(). class=" + getClass();
/*      */ 
/*  568 */       throw new UnsupportedOperationException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Singleton<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private E mMember;
/*  443 */     private boolean mHasNext = true;
/*      */ 
/*      */     public Singleton(E member)
/*      */     {
/*  452 */       this.mMember = member;
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  463 */       return this.mHasNext;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  475 */       if (!this.mHasNext)
/*  476 */         throw new NoSuchElementException();
/*  477 */       this.mHasNext = false;
/*  478 */       Object result = this.mMember;
/*  479 */       this.mMember = null;
/*  480 */       return result;
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  489 */       String msg = "This iterator does not support remove(). class=" + getClass();
/*      */ 
/*  491 */       throw new UnsupportedOperationException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Empty<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     public boolean hasNext()
/*      */     {
/*  397 */       return false;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  407 */       String msg = "No elements in empty iterator.";
/*  408 */       throw new NoSuchElementException(msg);
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  417 */       String msg = "No elements to remove in empty iterator.";
/*  418 */       throw new IllegalStateException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ArraySlice<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final E[] mObjects;
/*      */     private int mNext;
/*      */     private final int mLast;
/*      */ 
/*      */     public ArraySlice(E[] objects, int start, int length)
/*      */     {
/*  319 */       if (start < 0) {
/*  320 */         String msg = "Require start of slice to be non-negative. Found start=" + start;
/*      */ 
/*  322 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  324 */       if (start + length > objects.length) {
/*  325 */         String msg = "Start plus length must not exceed array length. Found objects.length=" + objects.length + " start=" + start + " length=" + length + " (start+length)=" + (start + length);
/*      */ 
/*  330 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  332 */       this.mObjects = objects;
/*  333 */       this.mNext = start;
/*  334 */       this.mLast = (start + length);
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  345 */       return this.mNext < this.mLast;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  357 */       if (!hasNext()) throw new NoSuchElementException();
/*  358 */       return this.mObjects[(this.mNext++)];
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  367 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Array<E>
/*      */     implements Iterator<E>
/*      */   {
/*      */     private final E[] mMembers;
/*      */     private int mPosition;
/*      */ 
/*      */     public Array(E[] members)
/*      */     {
/*  235 */       this.mMembers = members;
/*      */     }
/*      */ 
/*      */     public boolean hasNext()
/*      */     {
/*  246 */       return this.mPosition < this.mMembers.length;
/*      */     }
/*      */ 
/*      */     public E next()
/*      */     {
/*  257 */       if (!hasNext())
/*  258 */         throw new NoSuchElementException();
/*  259 */       return this.mMembers[(this.mPosition++)];
/*      */     }
/*      */ 
/*      */     public void remove()
/*      */     {
/*  272 */       if (this.mPosition < 1)
/*  273 */         throw new IllegalStateException("Next not yet called.");
/*  274 */       if (this.mMembers[(this.mPosition - 1)] == null)
/*  275 */         throw new IllegalStateException("Remove already called.");
/*  276 */       this.mMembers[(this.mPosition - 1)] = null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Iterators
 * JD-Core Version:    0.6.2
 */