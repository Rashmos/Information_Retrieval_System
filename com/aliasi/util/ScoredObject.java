/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public class ScoredObject<E>
/*     */   implements Scored
/*     */ {
/*     */   private final E mObj;
/*     */   private final double mScore;
/* 138 */   static final Comparator<Scored> REVERSE_SCORE_COMPARATOR = new ReverseScoredComparator();
/*     */ 
/* 141 */   static final Comparator<Scored> SCORE_COMPARATOR = new ScoredComparator();
/*     */ 
/*     */   public ScoredObject(E obj, double score)
/*     */   {
/*  47 */     this.mObj = obj;
/*  48 */     this.mScore = score;
/*     */   }
/*     */ 
/*     */   public E getObject()
/*     */   {
/*  57 */     return this.mObj;
/*     */   }
/*     */ 
/*     */   public double score()
/*     */   {
/*  66 */     return this.mScore;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  78 */     return this.mScore + ":" + getObject();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/*  93 */     if (!(that instanceof ScoredObject))
/*  94 */       return false;
/*  95 */     ScoredObject thatSo = (ScoredObject)that;
/*  96 */     return (this.mObj.equals(thatSo.mObj)) && (this.mScore == thatSo.mScore);
/*     */   }
/*     */ 
/*     */   public static <E extends Scored> Comparator<E> comparator()
/*     */   {
/* 119 */     Comparator result = SCORE_COMPARATOR;
/* 120 */     return result;
/*     */   }
/*     */ 
/*     */   public static <E extends Scored> Comparator<E> reverseComparator()
/*     */   {
/* 133 */     Comparator result = REVERSE_SCORE_COMPARATOR;
/* 134 */     return result;
/*     */   }
/*     */ 
/*     */   static class ReverseScoredComparator
/*     */     implements Comparator<Scored>
/*     */   {
/*     */     public int compare(Scored obj1, Scored obj2)
/*     */     {
/* 160 */       return obj1.score() < obj2.score() ? 1 : obj1.score() > obj2.score() ? -1 : 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ScoredComparator
/*     */     implements Comparator<Scored>
/*     */   {
/*     */     public int compare(Scored obj1, Scored obj2)
/*     */     {
/* 148 */       return obj1.score() < obj2.score() ? -1 : obj1.score() > obj2.score() ? 1 : 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.ScoredObject
 * JD-Core Version:    0.6.2
 */