/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ObjectToDoubleMap<E> extends HashMap<E, Double>
/*     */ {
/*     */   static final long serialVersionUID = 2891073039706316972L;
/*     */ 
/*     */   public ObjectToDoubleMap()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ObjectToDoubleMap(int initialCapacity)
/*     */   {
/*  59 */     super(initialCapacity);
/*     */   }
/*     */ 
/*     */   public void increment(E key, double increment)
/*     */   {
/*  72 */     set(key, increment + getValue(key));
/*     */   }
/*     */ 
/*     */   public void set(E key, double val)
/*     */   {
/*  82 */     if (val == 0.0D)
/*  83 */       remove(key);
/*     */     else
/*  85 */       put(key, Double.valueOf(val));
/*     */   }
/*     */ 
/*     */   public double getValue(E key)
/*     */   {
/*  96 */     Double d = (Double)get(key);
/*  97 */     return d == null ? 0.0D : d.doubleValue();
/*     */   }
/*     */ 
/*     */   public List<E> keysOrderedByValueList()
/*     */   {
/* 107 */     List keys = new ArrayList(keySet().size());
/* 108 */     keys.addAll(keySet());
/* 109 */     Collections.sort(keys, valueComparator());
/* 110 */     return keys;
/*     */   }
/*     */ 
/*     */   public List<ScoredObject<E>> scoredObjectsOrderedByValueList()
/*     */   {
/* 120 */     Set entrySet = entrySet();
/* 121 */     List sos = new ArrayList(entrySet.size());
/*     */ 
/* 123 */     for (Map.Entry entry : entrySet) {
/* 124 */       Object key = entry.getKey();
/* 125 */       double val = ((Double)entry.getValue()).doubleValue();
/* 126 */       sos.add(new ScoredObject(key, val));
/*     */     }
/* 128 */     Collections.sort(sos, ScoredObject.reverseComparator());
/* 129 */     return sos;
/*     */   }
/*     */ 
/*     */   public Comparator<E> valueComparator()
/*     */   {
/* 144 */     return new Comparator() {
/*     */       public int compare(E o1, E o2) {
/* 146 */         double d1 = ObjectToDoubleMap.this.getValue(o1);
/* 147 */         double d2 = ObjectToDoubleMap.this.getValue(o2);
/* 148 */         if (d1 > d2) {
/* 149 */           return -1;
/*     */         }
/* 151 */         if (d1 < d2) {
/* 152 */           return 1;
/*     */         }
/* 154 */         if ((Double.isNaN(d1)) && (!Double.isNaN(d2))) {
/* 155 */           return 1;
/*     */         }
/* 157 */         if (Double.isNaN(d2)) {
/* 158 */           return -1;
/*     */         }
/* 160 */         if ((!(o1 instanceof Comparable)) || (!(o2 instanceof Comparable)))
/*     */         {
/* 162 */           return 0;
/*     */         }
/*     */ 
/* 166 */         Comparable c1 = (Comparable)o1;
/* 167 */         return c1.compareTo(o2);
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.ObjectToDoubleMap
 * JD-Core Version:    0.6.2
 */