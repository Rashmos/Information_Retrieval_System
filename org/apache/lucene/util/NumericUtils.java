/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ public final class NumericUtils
/*     */ {
/*     */   public static final int PRECISION_STEP_DEFAULT = 4;
/*     */   public static final char SHIFT_START_LONG = ' ';
/*     */   public static final int BUF_SIZE_LONG = 11;
/*     */   public static final char SHIFT_START_INT = '`';
/*     */   public static final int BUF_SIZE_INT = 6;
/*     */ 
/*     */   public static int longToPrefixCoded(long val, int shift, char[] buffer)
/*     */   {
/* 109 */     if ((shift > 63) || (shift < 0))
/* 110 */       throw new IllegalArgumentException("Illegal shift value, must be 0..63");
/* 111 */     int nChars = (63 - shift) / 7 + 1; int len = nChars + 1;
/* 112 */     buffer[0] = ((char)(32 + shift));
/* 113 */     long sortableBits = val ^ 0x0;
/* 114 */     sortableBits >>>= shift;
/* 115 */     while (nChars >= 1)
/*     */     {
/* 119 */       buffer[(nChars--)] = ((char)(int)(sortableBits & 0x7F));
/* 120 */       sortableBits >>>= 7;
/*     */     }
/* 122 */     return len;
/*     */   }
/*     */ 
/*     */   public static String longToPrefixCoded(long val, int shift)
/*     */   {
/* 132 */     char[] buffer = new char[11];
/* 133 */     int len = longToPrefixCoded(val, shift, buffer);
/* 134 */     return new String(buffer, 0, len);
/*     */   }
/*     */ 
/*     */   public static String longToPrefixCoded(long val)
/*     */   {
/* 144 */     return longToPrefixCoded(val, 0);
/*     */   }
/*     */ 
/*     */   public static int intToPrefixCoded(int val, int shift, char[] buffer)
/*     */   {
/* 157 */     if ((shift > 31) || (shift < 0))
/* 158 */       throw new IllegalArgumentException("Illegal shift value, must be 0..31");
/* 159 */     int nChars = (31 - shift) / 7 + 1; int len = nChars + 1;
/* 160 */     buffer[0] = ((char)(96 + shift));
/* 161 */     int sortableBits = val ^ 0x80000000;
/* 162 */     sortableBits >>>= shift;
/* 163 */     while (nChars >= 1)
/*     */     {
/* 167 */       buffer[(nChars--)] = ((char)(sortableBits & 0x7F));
/* 168 */       sortableBits >>>= 7;
/*     */     }
/* 170 */     return len;
/*     */   }
/*     */ 
/*     */   public static String intToPrefixCoded(int val, int shift)
/*     */   {
/* 180 */     char[] buffer = new char[6];
/* 181 */     int len = intToPrefixCoded(val, shift, buffer);
/* 182 */     return new String(buffer, 0, len);
/*     */   }
/*     */ 
/*     */   public static String intToPrefixCoded(int val)
/*     */   {
/* 192 */     return intToPrefixCoded(val, 0);
/*     */   }
/*     */ 
/*     */   public static long prefixCodedToLong(String prefixCoded)
/*     */   {
/* 204 */     int shift = prefixCoded.charAt(0) - ' ';
/* 205 */     if ((shift > 63) || (shift < 0))
/* 206 */       throw new NumberFormatException("Invalid shift value in prefixCoded string (is encoded value really a LONG?)");
/* 207 */     long sortableBits = 0L;
/* 208 */     int i = 1; for (int len = prefixCoded.length(); i < len; i++) {
/* 209 */       sortableBits <<= 7;
/* 210 */       char ch = prefixCoded.charAt(i);
/* 211 */       if (ch > '') {
/* 212 */         throw new NumberFormatException("Invalid prefixCoded numerical value representation (char " + Integer.toHexString(ch) + " at position " + i + " is invalid)");
/*     */       }
/*     */ 
/* 217 */       sortableBits |= ch;
/*     */     }
/* 219 */     return sortableBits << shift ^ 0x0;
/*     */   }
/*     */ 
/*     */   public static int prefixCodedToInt(String prefixCoded)
/*     */   {
/* 231 */     int shift = prefixCoded.charAt(0) - '`';
/* 232 */     if ((shift > 31) || (shift < 0))
/* 233 */       throw new NumberFormatException("Invalid shift value in prefixCoded string (is encoded value really an INT?)");
/* 234 */     int sortableBits = 0;
/* 235 */     int i = 1; for (int len = prefixCoded.length(); i < len; i++) {
/* 236 */       sortableBits <<= 7;
/* 237 */       char ch = prefixCoded.charAt(i);
/* 238 */       if (ch > '') {
/* 239 */         throw new NumberFormatException("Invalid prefixCoded numerical value representation (char " + Integer.toHexString(ch) + " at position " + i + " is invalid)");
/*     */       }
/*     */ 
/* 244 */       sortableBits |= ch;
/*     */     }
/* 246 */     return sortableBits << shift ^ 0x80000000;
/*     */   }
/*     */ 
/*     */   public static long doubleToSortableLong(double val)
/*     */   {
/* 257 */     long f = Double.doubleToRawLongBits(val);
/* 258 */     if (f < 0L) f ^= 9223372036854775807L;
/* 259 */     return f;
/*     */   }
/*     */ 
/*     */   public static String doubleToPrefixCoded(double val)
/*     */   {
/* 267 */     return longToPrefixCoded(doubleToSortableLong(val));
/*     */   }
/*     */ 
/*     */   public static double sortableLongToDouble(long val)
/*     */   {
/* 275 */     if (val < 0L) val ^= 9223372036854775807L;
/* 276 */     return Double.longBitsToDouble(val);
/*     */   }
/*     */ 
/*     */   public static double prefixCodedToDouble(String val)
/*     */   {
/* 284 */     return sortableLongToDouble(prefixCodedToLong(val));
/*     */   }
/*     */ 
/*     */   public static int floatToSortableInt(float val)
/*     */   {
/* 295 */     int f = Float.floatToRawIntBits(val);
/* 296 */     if (f < 0) f ^= 2147483647;
/* 297 */     return f;
/*     */   }
/*     */ 
/*     */   public static String floatToPrefixCoded(float val)
/*     */   {
/* 305 */     return intToPrefixCoded(floatToSortableInt(val));
/*     */   }
/*     */ 
/*     */   public static float sortableIntToFloat(int val)
/*     */   {
/* 313 */     if (val < 0) val ^= 2147483647;
/* 314 */     return Float.intBitsToFloat(val);
/*     */   }
/*     */ 
/*     */   public static float prefixCodedToFloat(String val)
/*     */   {
/* 322 */     return sortableIntToFloat(prefixCodedToInt(val));
/*     */   }
/*     */ 
/*     */   public static void splitLongRange(LongRangeBuilder builder, int precisionStep, long minBound, long maxBound)
/*     */   {
/* 336 */     splitRange(builder, 64, precisionStep, minBound, maxBound);
/*     */   }
/*     */ 
/*     */   public static void splitIntRange(IntRangeBuilder builder, int precisionStep, int minBound, int maxBound)
/*     */   {
/* 350 */     splitRange(builder, 32, precisionStep, minBound, maxBound);
/*     */   }
/*     */ 
/*     */   private static void splitRange(Object builder, int valSize, int precisionStep, long minBound, long maxBound)
/*     */   {
/* 358 */     if (precisionStep < 1)
/* 359 */       throw new IllegalArgumentException("precisionStep must be >=1");
/* 360 */     if (minBound > maxBound) return;
/* 361 */     for (int shift = 0; ; shift += precisionStep)
/*     */     {
/* 363 */       long diff = 1L << shift + precisionStep;
/* 364 */       long mask = (1L << precisionStep) - 1L << shift;
/*     */ 
/* 366 */       boolean hasLower = (minBound & mask) != 0L;
/* 367 */       boolean hasUpper = (maxBound & mask) != mask;
/*     */ 
/* 369 */       long nextMinBound = (hasLower ? minBound + diff : minBound) & (mask ^ 0xFFFFFFFF);
/* 370 */       long nextMaxBound = (hasUpper ? maxBound - diff : maxBound) & (mask ^ 0xFFFFFFFF);
/*     */ 
/* 372 */       if ((shift + precisionStep >= valSize) || (nextMinBound > nextMaxBound))
/*     */       {
/* 374 */         addRange(builder, valSize, minBound, maxBound, shift);
/*     */ 
/* 376 */         break;
/*     */       }
/*     */ 
/* 379 */       if (hasLower)
/* 380 */         addRange(builder, valSize, minBound, minBound | mask, shift);
/* 381 */       if (hasUpper) {
/* 382 */         addRange(builder, valSize, maxBound & (mask ^ 0xFFFFFFFF), maxBound, shift);
/*     */       }
/*     */ 
/* 385 */       minBound = nextMinBound;
/* 386 */       maxBound = nextMaxBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void addRange(Object builder, int valSize, long minBound, long maxBound, int shift)
/*     */   {
/* 400 */     maxBound |= (1L << shift) - 1L;
/*     */ 
/* 402 */     switch (valSize) {
/*     */     case 64:
/* 404 */       ((LongRangeBuilder)builder).addRange(minBound, maxBound, shift);
/* 405 */       break;
/*     */     case 32:
/* 407 */       ((IntRangeBuilder)builder).addRange((int)minBound, (int)maxBound, shift);
/* 408 */       break;
/*     */     default:
/* 411 */       throw new IllegalArgumentException("valSize must be 32 or 64.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class IntRangeBuilder
/*     */   {
/*     */     public void addRange(String minPrefixCoded, String maxPrefixCoded)
/*     */     {
/* 454 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public void addRange(int min, int max, int shift)
/*     */     {
/* 462 */       addRange(NumericUtils.intToPrefixCoded(min, shift), NumericUtils.intToPrefixCoded(max, shift));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class LongRangeBuilder
/*     */   {
/*     */     public void addRange(String minPrefixCoded, String maxPrefixCoded)
/*     */     {
/* 428 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public void addRange(long min, long max, int shift)
/*     */     {
/* 436 */       addRange(NumericUtils.longToPrefixCoded(min, shift), NumericUtils.longToPrefixCoded(max, shift));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.NumericUtils
 * JD-Core Version:    0.6.2
 */