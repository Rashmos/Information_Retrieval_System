/*     */ package com.aliasi.util;
/*     */ 
/*     */ public class Counter extends Number
/*     */ {
/*     */   static final long serialVersionUID = -6794167256664036748L;
/*     */   private int mCount;
/*     */ 
/*     */   public Counter()
/*     */   {
/*  46 */     this(0);
/*     */   }
/*     */ 
/*     */   public Counter(int count)
/*     */   {
/*  55 */     this.mCount = count;
/*     */   }
/*     */ 
/*     */   public int value()
/*     */   {
/*  64 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public void increment()
/*     */   {
/*  71 */     this.mCount += 1;
/*     */   }
/*     */ 
/*     */   public void increment(int n)
/*     */   {
/*  80 */     this.mCount += n;
/*     */   }
/*     */ 
/*     */   public void set(int count)
/*     */   {
/*  90 */     this.mCount = count;
/*     */   }
/*     */ 
/*     */   public double doubleValue()
/*     */   {
/* 100 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public float floatValue()
/*     */   {
/* 110 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 120 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public long longValue()
/*     */   {
/* 130 */     return this.mCount;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 140 */     return String.valueOf(value());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Counter
 * JD-Core Version:    0.6.2
 */