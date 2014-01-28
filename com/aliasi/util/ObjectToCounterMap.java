/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ObjectToCounterMap<E> extends HashMap<E, Counter>
/*     */ {
/*     */   static final long serialVersionUID = -4735380145915633564L;
/*     */ 
/*     */   public ObjectToCounterMap()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap(int initialSize)
/*     */   {
/*  59 */     super(initialSize);
/*     */   }
/*     */ 
/*     */   public void increment(E key)
/*     */   {
/*  72 */     increment(key, 1);
/*     */   }
/*     */ 
/*     */   public void increment(E key, int n)
/*     */   {
/*  87 */     if (!containsKey(key)) {
/*  88 */       put(key, new Counter(n));
/*  89 */       return;
/*     */     }
/*  91 */     Counter counter = (Counter)get(key);
/*  92 */     counter.increment(n);
/*  93 */     if (counter.value() == 0) remove(key);
/*     */   }
/*     */ 
/*     */   public void set(E key, int n)
/*     */   {
/* 105 */     if (n == 0) {
/* 106 */       remove(key);
/* 107 */       return;
/*     */     }
/* 109 */     if (!containsKey(key)) {
/* 110 */       put(key, new Counter(n));
/* 111 */       return;
/*     */     }
/* 113 */     Counter counter = (Counter)get(key);
/* 114 */     counter.set(n);
/*     */   }
/*     */ 
/*     */   public int getCount(E key)
/*     */   {
/* 125 */     if (!containsKey(key)) return 0;
/* 126 */     return ((Counter)get(key)).value();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 136 */     StringBuilder sb = new StringBuilder();
/* 137 */     List keyList = keysOrderedByCountList();
/* 138 */     for (Iterator i$ = keyList.iterator(); i$.hasNext(); ) { Object key = i$.next();
/* 139 */       sb.append(key);
/* 140 */       sb.append('=');
/* 141 */       sb.append(getCount(key));
/* 142 */       sb.append('\n');
/*     */     }
/* 144 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public List<E> keysOrderedByCountList()
/*     */   {
/* 158 */     Set keySet = keySet();
/* 159 */     List result = new ArrayList(keySet().size());
/* 160 */     result.addAll(keySet);
/* 161 */     Collections.sort(result, countComparator());
/* 162 */     return result;
/*     */   }
/*     */ 
/*     */   public Object[] keysOrderedByCount()
/*     */   {
/* 173 */     return keysOrderedByCountList().toArray();
/*     */   }
/*     */ 
/*     */   public void prune(int minCount)
/*     */   {
/* 183 */     Iterator it = entrySet().iterator();
/* 184 */     while (it.hasNext())
/* 185 */       if (((Counter)((Map.Entry)it.next()).getValue()).value() < minCount)
/* 186 */         it.remove();
/*     */   }
/*     */ 
/*     */   public Comparator<E> countComparator()
/*     */   {
/* 208 */     return new Comparator() {
/*     */       public int compare(E o1, E o2) {
/* 210 */         int count1 = ObjectToCounterMap.this.getCount(o1);
/* 211 */         int count2 = ObjectToCounterMap.this.getCount(o2);
/* 212 */         if (count1 < count2) return 1;
/* 213 */         if (count1 > count2) return -1;
/* 214 */         if ((!(o1 instanceof Comparable)) || (!(o2 instanceof Comparable)))
/*     */         {
/* 216 */           return 0;
/*     */         }
/* 218 */         Comparable c1 = (Comparable)o1;
/*     */ 
/* 220 */         return c1.compareTo(o2);
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.ObjectToCounterMap
 * JD-Core Version:    0.6.2
 */