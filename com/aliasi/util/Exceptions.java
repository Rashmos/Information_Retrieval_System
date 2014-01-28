/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class Exceptions
/*     */ {
/*     */   public static IOException toIO(String msg, Throwable t)
/*     */   {
/*  48 */     IOException e = new IOException(message(msg, t));
/*  49 */     copyStack(t, e);
/*  50 */     return e;
/*     */   }
/*     */ 
/*     */   public static IllegalArgumentException toIllegalArgument(String msg, Throwable t)
/*     */   {
/*  67 */     IllegalArgumentException e = new IllegalArgumentException(message(msg, t));
/*     */ 
/*  69 */     copyStack(t, e);
/*  70 */     return e;
/*     */   }
/*     */ 
/*     */   public static void finiteNonNegative(String name, double value)
/*     */   {
/*  85 */     if ((Double.isNaN(value)) || (Double.isInfinite(value)) || (value < 0.0D))
/*     */     {
/*  88 */       String msg = name + " must be finite and non-negative." + " Found " + name + "=" + value;
/*     */ 
/*  90 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void finite(String name, double value)
/*     */   {
/* 104 */     if ((Double.isNaN(value)) || (Double.isInfinite(value)))
/*     */     {
/* 106 */       String msg = name + " must be finite." + " Found " + name + "=" + value;
/*     */ 
/* 108 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String message(String msg, Throwable t) {
/* 113 */     return msg + " Rethrown." + " Original throwable class=" + t.getClass().toString() + " Original message=" + t.getMessage();
/*     */   }
/*     */ 
/*     */   private static void copyStack(Throwable from, Throwable to)
/*     */   {
/* 120 */     to.setStackTrace(from.getStackTrace());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Exceptions
 * JD-Core Version:    0.6.2
 */