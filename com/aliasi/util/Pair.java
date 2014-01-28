/*     */ package com.aliasi.util;
/*     */ 
/*     */ public class Pair<A, B>
/*     */ {
/*     */   private final A mA;
/*     */   private final B mB;
/*     */ 
/*     */   public Pair(A a, B b)
/*     */   {
/*  45 */     this.mA = a;
/*  46 */     this.mB = b;
/*     */   }
/*     */ 
/*     */   public A a()
/*     */   {
/*  55 */     return this.mA;
/*     */   }
/*     */ 
/*     */   public B b()
/*     */   {
/*  64 */     return this.mB;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  76 */     return "(" + a() + "," + b() + ")";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/*  94 */     if (!(that instanceof Pair))
/*  95 */       return false;
/*  96 */     Pair thatPair = (Pair)that;
/*  97 */     return (this.mA.equals(thatPair.mA)) && (this.mB.equals(thatPair.mB));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 110 */     return 31 * this.mA.hashCode() + this.mB.hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Pair
 * JD-Core Version:    0.6.2
 */