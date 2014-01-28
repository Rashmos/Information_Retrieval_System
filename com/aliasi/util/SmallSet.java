/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class SmallSet<E> extends AbstractSet<E>
/*     */ {
/*  62 */   static final SmallSet SMALL_EMPTY_SET = new SmallEmptySet();
/*     */ 
/*     */   public abstract SmallSet<E> union(SmallSet<? extends E> paramSmallSet);
/*     */ 
/*     */   public static <F> SmallSet<F> create()
/*     */   {
/*  72 */     SmallSet result = SMALL_EMPTY_SET;
/*  73 */     return result;
/*     */   }
/*     */ 
/*     */   public static <F> SmallSet<F> create(F member)
/*     */   {
/*  84 */     return new SingletonSet(member);
/*     */   }
/*     */ 
/*     */   public static <F> SmallSet<F> create(F member1, F member2)
/*     */   {
/*  98 */     if (member1.equals(member2)) return create(member1);
/*  99 */     return new PairSet(member1, member2);
/*     */   }
/*     */ 
/*     */   public static <F> SmallSet<F> create(F[] members)
/*     */   {
/* 115 */     switch (members.length) {
/*     */     case 0:
/* 117 */       return create();
/*     */     case 1:
/* 119 */       return create(members[0]);
/*     */     case 2:
/* 121 */       return create(members[0], members[1]);
/*     */     }
/* 123 */     HashSet set = new HashSet();
/* 124 */     Collections.addAll(set, members);
/* 125 */     return create(set);
/*     */   }
/*     */ 
/*     */   public static <F> SmallSet<F> create(Set<? extends F> members)
/*     */   {
/* 140 */     switch (members.size()) {
/*     */     case 0:
/* 142 */       return create();
/*     */     case 1:
/* 144 */       return create(members.iterator().next());
/*     */     case 2:
/* 146 */       Iterator it = members.iterator();
/* 147 */       Object obj1 = it.next();
/* 148 */       Object obj2 = it.next();
/* 149 */       return create(obj1, obj2);
/* 150 */     }return new ListSet(members);
/*     */   }
/*     */ 
/*     */   public static <F> SmallSet<F> create(F member, Set<? extends F> set)
/*     */   {
/* 165 */     switch (set.size()) { case 0:
/* 166 */       return create(member);
/*     */     case 1:
/* 167 */       return create(member, set.iterator().next());
/*     */     }
/* 169 */     return set.contains(member) ? create(set) : new ListSet(member, set);
/*     */   }
/*     */ 
/*     */   public static <F> SmallSet<F> create(Set<? extends F> set1, Set<? extends F> set2)
/*     */   {
/* 186 */     HashSet union = new HashSet(set1.size() + set2.size());
/* 187 */     union.addAll(set1);
/* 188 */     union.addAll(set2);
/* 189 */     return create(union);
/*     */   }
/*     */ 
/*     */   private static class ListSet<F> extends SmallSet<F>
/*     */   {
/*     */     private final F[] mMembers;
/*     */ 
/*     */     ListSet(Set<? extends F> set)
/*     */     {
/* 430 */       Object[] tempMembers = (Object[])new Object[set.size()];
/* 431 */       this.mMembers = set.toArray(tempMembers);
/*     */     }
/*     */ 
/*     */     ListSet(F x, Set<? extends F> set)
/*     */     {
/* 436 */       Object[] tempMembers = (Object[])new Object[set.size() + 1];
/* 437 */       this.mMembers = set.toArray(tempMembers);
/* 438 */       this.mMembers[(this.mMembers.length - 1)] = x;
/*     */     }
/*     */ 
/*     */     public SmallSet<F> union(SmallSet<? extends F> that)
/*     */     {
/* 444 */       switch (that.size()) {
/*     */       case 0:
/* 446 */         return this;
/*     */       case 1:
/* 448 */         Object next = that.iterator().next();
/* 449 */         return contains(next) ? this : new ListSet(next, this);
/*     */       }
/*     */ 
/* 453 */       HashSet union = new HashSet(size() + that.size());
/* 454 */       for (Object member : this.mMembers)
/* 455 */         union.add(member);
/* 456 */       union.addAll(that);
/* 457 */       return new ListSet(union);
/*     */     }
/*     */ 
/*     */     public Iterator<F> iterator()
/*     */     {
/* 463 */       return Iterators.array(this.mMembers);
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 468 */       return this.mMembers.length;
/*     */     }
/*     */ 
/*     */     public boolean contains(Object obj)
/*     */     {
/* 473 */       for (int i = 0; i < this.mMembers.length; i++)
/* 474 */         if (obj.equals(this.mMembers[i]))
/* 475 */           return true;
/* 476 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class PairSet<F> extends SmallSet<F>
/*     */   {
/*     */     private final F mMember1;
/*     */     private final F mMember2;
/*     */ 
/*     */     public PairSet(F member1, F member2)
/*     */     {
/* 359 */       this.mMember1 = member1;
/* 360 */       this.mMember2 = member2;
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 370 */       return 2;
/*     */     }
/*     */ 
/*     */     public boolean contains(Object obj)
/*     */     {
/* 383 */       return (obj.equals(this.mMember1)) || (obj.equals(this.mMember2));
/*     */     }
/*     */ 
/*     */     public Iterator<F> iterator()
/*     */     {
/* 394 */       return Iterators.pair(this.mMember1, this.mMember2);
/*     */     }
/*     */ 
/*     */     public SmallSet<F> union(SmallSet<? extends F> that)
/*     */     {
/* 407 */       switch (that.size()) {
/*     */       case 0:
/* 409 */         return this;
/*     */       case 1:
/* 411 */         Object member = that.iterator().next();
/* 412 */         if (contains(member)) return this;
/* 413 */         return SmallSet.create(this, that);
/*     */       }
/* 415 */       return SmallSet.create(this, that);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SingletonSet<F> extends SmallSet<F>
/*     */   {
/*     */     private final F mMember;
/*     */ 
/*     */     public SingletonSet(F member)
/*     */     {
/* 276 */       this.mMember = member;
/*     */     }
/*     */ 
/*     */     public Iterator<F> iterator()
/*     */     {
/* 286 */       return Iterators.singleton(this.mMember);
/*     */     }
/*     */ 
/*     */     public int size()
/*     */     {
/* 296 */       return 1;
/*     */     }
/*     */ 
/*     */     public boolean contains(Object obj)
/*     */     {
/* 309 */       return this.mMember.equals(obj);
/*     */     }
/*     */ 
/*     */     public SmallSet<F> union(SmallSet<? extends F> that)
/*     */     {
/* 322 */       switch (that.size()) { case 0:
/* 323 */         return this;
/*     */       case 1:
/* 324 */         return SmallSet.create(this.mMember, that.iterator().next());
/*     */       case 2:
/* 325 */         return SmallSet.create(this.mMember, that); }
/* 326 */       return SmallSet.create(this, that);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SmallEmptySet<F> extends SmallSet<F>
/*     */   {
/*     */     public int size()
/*     */     {
/* 215 */       return 0;
/*     */     }
/*     */ 
/*     */     public Iterator<F> iterator()
/*     */     {
/* 224 */       return Iterators.empty();
/*     */     }
/*     */ 
/*     */     public boolean contains(Object o)
/*     */     {
/* 237 */       return false;
/*     */     }
/*     */ 
/*     */     public SmallSet<F> union(SmallSet<? extends F> that)
/*     */     {
/* 249 */       return SmallSet.create(that);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.SmallSet
 * JD-Core Version:    0.6.2
 */