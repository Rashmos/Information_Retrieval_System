/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ObjectToSet<K, M> extends HashMap<K, Set<M>>
/*     */ {
/*     */   static final long serialVersionUID = -5758024598554958671L;
/*     */ 
/*     */   public void addMember(K key, M member)
/*     */   {
/*  57 */     if (containsKey(key)) {
/*  58 */       ((Set)get(key)).add(member);
/*     */     } else {
/*  60 */       HashSet val = new HashSet();
/*  61 */       val.add(member);
/*  62 */       put(key, val);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean removeMember(K key, M member)
/*     */   {
/*  78 */     if (!containsKey(key)) return false;
/*  79 */     boolean result = ((Set)get(key)).remove(member);
/*  80 */     if (((Set)get(key)).size() == 0)
/*  81 */       remove(key);
/*  82 */     return result;
/*     */   }
/*     */ 
/*     */   public void addMembers(K key, Set<? extends M> values)
/*     */   {
/*  93 */     Set memberSet = (Set)get(key);
/*  94 */     if (memberSet == null)
/*  95 */       put(key, new HashSet(values));
/*     */     else
/*  97 */       memberSet.addAll(values);
/*     */   }
/*     */ 
/*     */   public Set<M> memberValues()
/*     */   {
/* 108 */     Set set = new HashSet();
/* 109 */     for (Set members : values())
/* 110 */       set.addAll(members);
/* 111 */     return set;
/*     */   }
/*     */ 
/*     */   public Set<M> getSet(K key)
/*     */   {
/* 121 */     Set result = (Set)get(key);
/* 122 */     return result != null ? result : new HashSet(0);
/*     */   }
/*     */ 
/*     */   public Iterator<M> memberIterator()
/*     */   {
/* 138 */     return new MemberIterator(this);
/*     */   }
/*     */ 
/*     */   static class MemberIterator<N> extends Iterators.Buffered<N>
/*     */   {
/*     */     final Iterator<? extends Set<N>> mTopIterator;
/* 143 */     Iterator<N> mMemberSetIterator = null;
/*     */ 
/* 145 */     MemberIterator(ObjectToSet<?, N> ots) { this.mTopIterator = ots.values().iterator(); }
/*     */ 
/*     */     protected N bufferNext()
/*     */     {
/* 149 */       while ((this.mMemberSetIterator == null) || (!this.mMemberSetIterator.hasNext())) {
/* 150 */         if (!this.mTopIterator.hasNext())
/* 151 */           return null;
/* 152 */         this.mMemberSetIterator = ((Set)this.mTopIterator.next()).iterator();
/*     */       }
/* 154 */       return this.mMemberSetIterator.next();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.ObjectToSet
 * JD-Core Version:    0.6.2
 */