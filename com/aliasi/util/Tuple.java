/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ 
/*     */ public abstract class Tuple<E> extends AbstractList<E>
/*     */ {
/*  37 */   private int mHashCode = 0;
/*     */ 
/*     */   public abstract int size();
/*     */ 
/*     */   public abstract E get(int paramInt);
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  72 */     if (this.mHashCode == 0)
/*  73 */       this.mHashCode = super.hashCode();
/*  74 */     return this.mHashCode;
/*     */   }
/*     */ 
/*     */   public static <E> Tuple<E> create()
/*     */   {
/*  84 */     return new EmptyTuple(null);
/*     */   }
/*     */ 
/*     */   public static <E> Tuple<E> create(E obj)
/*     */   {
/*  96 */     return new SingletonTuple(obj, null);
/*     */   }
/*     */ 
/*     */   public static <E> Tuple<E> create(E[] objs)
/*     */   {
/* 109 */     switch (objs.length) { case 0:
/* 110 */       return new EmptyTuple(null);
/*     */     case 1:
/* 111 */       return new SingletonTuple(objs[0], null);
/*     */     case 2:
/* 112 */       return new PairTuple(objs[0], objs[1], null); }
/* 113 */     return new ArrayTuple(objs, null);
/*     */   }
/*     */ 
/*     */   public static <E> Tuple<E> create(E o1, E o2)
/*     */   {
/* 126 */     return new PairTuple(o1, o2, null);
/*     */   }
/*     */ 
/*     */   private static class ArrayTuple<E> extends Tuple<E>
/*     */   {
/*     */     private final E[] mObjs;
/*     */ 
/*     */     private ArrayTuple(E[] objs)
/*     */     {
/* 304 */       super();
/* 305 */       this.mObjs = objs;
/*     */     }
/*     */ 
/*     */     public E get(int index)
/*     */     {
/* 317 */       return this.mObjs[index];
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 327 */       return this.mObjs.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class PairTuple<E> extends Tuple<E>
/*     */   {
/*     */     private final E mObject0;
/*     */     private final E mObject1;
/*     */ 
/*     */     private PairTuple(E object0, E object1)
/*     */     {
/* 252 */       super();
/* 253 */       this.mObject0 = object0;
/* 254 */       this.mObject1 = object1;
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 263 */       return 2;
/*     */     }
/*     */ 
/*     */     public E get(int index)
/*     */     {
/* 275 */       if (index < 0) {
/* 276 */         throw new IndexOutOfBoundsException("Index must be > 0, was=" + index);
/*     */       }
/* 278 */       if (index > 1) {
/* 279 */         throw new IndexOutOfBoundsException("Index must be < 2, was=" + index);
/*     */       }
/* 281 */       return index == 0 ? this.mObject0 : this.mObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SingletonTuple<E> extends Tuple<E>
/*     */   {
/*     */     private final E mObject;
/*     */ 
/*     */     private SingletonTuple(E object)
/*     */     {
/* 198 */       super();
/* 199 */       this.mObject = object;
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 208 */       return 1;
/*     */     }
/*     */ 
/*     */     public E get(int index)
/*     */     {
/* 220 */       if (index < 0) {
/* 221 */         throw new IndexOutOfBoundsException("Index must be > 0, was=" + index);
/*     */       }
/* 223 */       if (index > 0) {
/* 224 */         throw new IndexOutOfBoundsException("Index must be < 1, was=" + index);
/*     */       }
/* 226 */       return this.mObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class EmptyTuple<E> extends Tuple<E>
/*     */   {
/*     */     private EmptyTuple()
/*     */     {
/* 140 */       super();
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 150 */       return 0;
/*     */     }
/*     */ 
/*     */     public E get(int index)
/*     */     {
/* 161 */       String msg = "No elements in empty tuple.";
/* 162 */       throw new IndexOutOfBoundsException(msg);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 174 */       return 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Tuple
 * JD-Core Version:    0.6.2
 */