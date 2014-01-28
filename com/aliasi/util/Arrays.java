/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class Arrays
/*     */ {
/* 356 */   public static final int[] EMPTY_INT_ARRAY = new int[0];
/*     */ 
/*     */   public static <E> E[] reallocate(E[] xs, int newSize)
/*     */   {
/*  56 */     Object[] ys = (Object[])Array.newInstance(xs.getClass().getComponentType(), newSize);
/*     */ 
/*  60 */     int end = Math.min(xs.length, newSize);
/*  61 */     for (int i = 0; i < end; i++)
/*  62 */       ys[i] = xs[i];
/*  63 */     return ys;
/*     */   }
/*     */ 
/*     */   public static int[] reallocate(int[] xs, int newSize)
/*     */   {
/*  78 */     int[] ys = new int[newSize];
/*  79 */     int end = Math.min(xs.length, newSize);
/*  80 */     for (int i = 0; i < end; i++)
/*  81 */       ys[i] = xs[i];
/*  82 */     return ys;
/*     */   }
/*     */ 
/*     */   public static int[] reallocate(int[] xs)
/*     */   {
/*  96 */     int len = xs.length * 3 / 2;
/*  97 */     return reallocate(xs, len == xs.length ? xs.length + 1 : len);
/*     */   }
/*     */ 
/*     */   public static char[] add(char c, char[] cs)
/*     */   {
/* 115 */     if (java.util.Arrays.binarySearch(cs, c) >= 0)
/* 116 */       return cs;
/* 117 */     char[] result = new char[cs.length + 1];
/* 118 */     int i = 0;
/* 119 */     while ((i < cs.length) && (c > cs[i])) {
/* 120 */       result[i] = cs[i];
/* 121 */       i++;
/*     */     }
/* 123 */     result[i] = c;
/* 124 */     i++;
/* 125 */     while (i < result.length) {
/* 126 */       result[i] = cs[(i - 1)];
/* 127 */       i++;
/*     */     }
/* 129 */     return result;
/*     */   }
/*     */ 
/*     */   public static char[] copy(char[] cs)
/*     */   {
/* 142 */     if (cs == null) return null;
/* 143 */     char[] cs2 = new char[cs.length];
/* 144 */     for (int i = 0; i < cs.length; i++)
/* 145 */       cs2[i] = cs[i];
/* 146 */     return cs2;
/*     */   }
/*     */ 
/*     */   public static char[] toArray(CharSequence cSeq)
/*     */   {
/* 159 */     char[] cs = new char[cSeq.length()];
/* 160 */     for (int i = 0; i < cs.length; i++)
/* 161 */       cs[i] = cSeq.charAt(i);
/* 162 */     return cs;
/*     */   }
/*     */ 
/*     */   public static boolean member(Object x, Object[] xs)
/*     */   {
/* 177 */     if (xs == null) return false;
/* 178 */     int i = xs.length;
/*     */     do { i--; if (i < 0) break; }
/* 179 */     while ((xs[i] == null) || 
/* 180 */       (!xs[i].equals(x))); return true;
/*     */ 
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean member(char c, char[] cs)
/*     */   {
/* 196 */     if (cs == null) return false;
/* 197 */     for (int i = 0; i < cs.length; i++) {
/* 198 */       if (cs[i] == c) return true;
/*     */     }
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */   public static String arrayToString(Object[] xs)
/*     */   {
/* 212 */     StringBuilder sb = new StringBuilder();
/* 213 */     arrayToStringBuilder(sb, xs);
/* 214 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static void arrayToStringBuilder(StringBuilder sb, Object[] xs)
/*     */   {
/* 228 */     sb.append('[');
/* 229 */     for (int i = 0; i < xs.length; i++) {
/* 230 */       if (i > 0) sb.append(',');
/* 231 */       sb.append(xs[i]);
/*     */     }
/* 233 */     sb.append(']');
/*     */   }
/*     */ 
/*     */   public static char[] concatenate(char[] cs, char c)
/*     */   {
/* 248 */     char[] result = new char[cs.length + 1];
/* 249 */     for (int i = 0; i < cs.length; i++)
/* 250 */       result[i] = cs[i];
/* 251 */     result[(result.length - 1)] = c;
/* 252 */     return result;
/*     */   }
/*     */ 
/*     */   public static String[] concatenate(String[] xs, String[] ys)
/*     */   {
/* 268 */     String[] result = new String[xs.length + ys.length];
/* 269 */     System.arraycopy(xs, 0, result, 0, xs.length);
/* 270 */     System.arraycopy(ys, 0, result, xs.length, ys.length);
/* 271 */     return result;
/*     */   }
/*     */ 
/*     */   public static boolean equals(Object[] xs, Object[] ys)
/*     */   {
/* 286 */     if (xs.length != ys.length) return false;
/* 287 */     for (int i = 0; i < xs.length; i++)
/* 288 */       if (!xs[i].equals(ys[i])) return false;
/* 289 */     return true;
/*     */   }
/*     */ 
/*     */   public static <E> void permute(E[] xs)
/*     */   {
/* 302 */     permute(xs, new Random());
/*     */   }
/*     */ 
/*     */   public static <E> void permute(E[] xs, Random random)
/*     */   {
/* 315 */     int i = xs.length;
/*     */     while (true) { i--; if (i <= 0) break;
/* 316 */       int pos = random.nextInt(i);
/* 317 */       Object temp = xs[pos];
/* 318 */       xs[pos] = xs[i];
/* 319 */       xs[i] = temp;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void permute(int[] xs)
/*     */   {
/* 333 */     permute(xs, new Random());
/*     */   }
/*     */ 
/*     */   public static void permute(int[] xs, Random random)
/*     */   {
/* 344 */     int i = xs.length;
/*     */     while (true) { i--; if (i <= 0) break;
/* 345 */       int pos = random.nextInt(i);
/* 346 */       int temp = xs[pos];
/* 347 */       xs[pos] = xs[i];
/* 348 */       xs[i] = temp;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Arrays
 * JD-Core Version:    0.6.2
 */