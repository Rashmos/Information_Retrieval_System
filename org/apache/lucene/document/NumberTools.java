/*     */ package org.apache.lucene.document;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class NumberTools
/*     */ {
/*     */   private static final int RADIX = 36;
/*     */   private static final char NEGATIVE_PREFIX = '-';
/*     */   private static final char POSITIVE_PREFIX = '0';
/*     */   public static final String MIN_STRING_VALUE = "-0000000000000";
/*     */   public static final String MAX_STRING_VALUE = "01y2p0ij32e8e7";
/*  72 */   public static final int STR_SIZE = "-0000000000000".length();
/*     */ 
/*     */   public static String longToString(long l)
/*     */   {
/*  79 */     if (l == -9223372036854775808L)
/*     */     {
/*  81 */       return "-0000000000000";
/*     */     }
/*     */ 
/*  84 */     StringBuilder buf = new StringBuilder(STR_SIZE);
/*     */ 
/*  86 */     if (l < 0L) {
/*  87 */       buf.append('-');
/*  88 */       l = 9223372036854775807L + l + 1L;
/*     */     } else {
/*  90 */       buf.append('0');
/*     */     }
/*  92 */     String num = Long.toString(l, 36);
/*     */ 
/*  94 */     int padLen = STR_SIZE - num.length() - buf.length();
/*  95 */     while (padLen-- > 0) {
/*  96 */       buf.append('0');
/*     */     }
/*  98 */     buf.append(num);
/*     */ 
/* 100 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static long stringToLong(String str)
/*     */   {
/* 114 */     if (str == null) {
/* 115 */       throw new NullPointerException("string cannot be null");
/*     */     }
/* 117 */     if (str.length() != STR_SIZE) {
/* 118 */       throw new NumberFormatException("string is the wrong size");
/*     */     }
/*     */ 
/* 121 */     if (str.equals("-0000000000000")) {
/* 122 */       return -9223372036854775808L;
/*     */     }
/*     */ 
/* 125 */     char prefix = str.charAt(0);
/* 126 */     long l = Long.parseLong(str.substring(1), 36);
/*     */ 
/* 128 */     if (prefix != '0')
/*     */     {
/* 130 */       if (prefix == '-')
/* 131 */         l = l - 9223372036854775807L - 1L;
/*     */       else {
/* 133 */         throw new NumberFormatException("string does not begin with the correct prefix");
/*     */       }
/*     */     }
/*     */ 
/* 137 */     return l;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.document.NumberTools
 * JD-Core Version:    0.6.2
 */