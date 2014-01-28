/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ public final class ArrayUtil
/*     */ {
/*     */   public static int parseInt(char[] chars)
/*     */     throws NumberFormatException
/*     */   {
/*  41 */     return parseInt(chars, 0, chars.length, 10);
/*     */   }
/*     */ 
/*     */   public static int parseInt(char[] chars, int offset, int len)
/*     */     throws NumberFormatException
/*     */   {
/*  53 */     return parseInt(chars, offset, len, 10);
/*     */   }
/*     */ 
/*     */   public static int parseInt(char[] chars, int offset, int len, int radix)
/*     */     throws NumberFormatException
/*     */   {
/*  69 */     if ((chars == null) || (radix < 2) || (radix > 36))
/*     */     {
/*  71 */       throw new NumberFormatException();
/*     */     }
/*  73 */     int i = 0;
/*  74 */     if (len == 0) {
/*  75 */       throw new NumberFormatException("chars length is 0");
/*     */     }
/*  77 */     boolean negative = chars[(offset + i)] == '-';
/*  78 */     if (negative) { i++; if (i == len)
/*  79 */         throw new NumberFormatException("can't convert to an int");
/*     */     }
/*  81 */     if (negative == true) {
/*  82 */       offset++;
/*  83 */       len--;
/*     */     }
/*  85 */     return parse(chars, offset, len, radix, negative);
/*     */   }
/*     */ 
/*     */   private static int parse(char[] chars, int offset, int len, int radix, boolean negative)
/*     */     throws NumberFormatException
/*     */   {
/*  91 */     int max = -2147483648 / radix;
/*  92 */     int result = 0;
/*  93 */     for (int i = 0; i < len; i++) {
/*  94 */       int digit = Character.digit(chars[(i + offset)], radix);
/*  95 */       if (digit == -1) {
/*  96 */         throw new NumberFormatException("Unable to parse");
/*     */       }
/*  98 */       if (max > result) {
/*  99 */         throw new NumberFormatException("Unable to parse");
/*     */       }
/* 101 */       int next = result * radix - digit;
/* 102 */       if (next > result) {
/* 103 */         throw new NumberFormatException("Unable to parse");
/*     */       }
/* 105 */       result = next;
/*     */     }
/*     */ 
/* 110 */     if (!negative) {
/* 111 */       result = -result;
/* 112 */       if (result < 0) {
/* 113 */         throw new NumberFormatException("Unable to parse");
/*     */       }
/*     */     }
/* 116 */     return result;
/*     */   }
/*     */ 
/*     */   public static int getNextSize(int targetSize)
/*     */   {
/* 134 */     return (targetSize >> 3) + (targetSize < 9 ? 3 : 6) + targetSize;
/*     */   }
/*     */ 
/*     */   public static int getShrinkSize(int currentSize, int targetSize) {
/* 138 */     int newSize = getNextSize(targetSize);
/*     */ 
/* 142 */     if (newSize < currentSize / 2) {
/* 143 */       return newSize;
/*     */     }
/* 145 */     return currentSize;
/*     */   }
/*     */ 
/*     */   public static int[] grow(int[] array, int minSize) {
/* 149 */     if (array.length < minSize) {
/* 150 */       int[] newArray = new int[getNextSize(minSize)];
/* 151 */       System.arraycopy(array, 0, newArray, 0, array.length);
/* 152 */       return newArray;
/*     */     }
/* 154 */     return array;
/*     */   }
/*     */ 
/*     */   public static int[] grow(int[] array) {
/* 158 */     return grow(array, 1 + array.length);
/*     */   }
/*     */ 
/*     */   public static int[] shrink(int[] array, int targetSize) {
/* 162 */     int newSize = getShrinkSize(array.length, targetSize);
/* 163 */     if (newSize != array.length) {
/* 164 */       int[] newArray = new int[newSize];
/* 165 */       System.arraycopy(array, 0, newArray, 0, newSize);
/* 166 */       return newArray;
/*     */     }
/* 168 */     return array;
/*     */   }
/*     */ 
/*     */   public static long[] grow(long[] array, int minSize) {
/* 172 */     if (array.length < minSize) {
/* 173 */       long[] newArray = new long[getNextSize(minSize)];
/* 174 */       System.arraycopy(array, 0, newArray, 0, array.length);
/* 175 */       return newArray;
/*     */     }
/* 177 */     return array;
/*     */   }
/*     */ 
/*     */   public static long[] grow(long[] array) {
/* 181 */     return grow(array, 1 + array.length);
/*     */   }
/*     */ 
/*     */   public static long[] shrink(long[] array, int targetSize) {
/* 185 */     int newSize = getShrinkSize(array.length, targetSize);
/* 186 */     if (newSize != array.length) {
/* 187 */       long[] newArray = new long[newSize];
/* 188 */       System.arraycopy(array, 0, newArray, 0, newSize);
/* 189 */       return newArray;
/*     */     }
/* 191 */     return array;
/*     */   }
/*     */ 
/*     */   public static byte[] grow(byte[] array, int minSize) {
/* 195 */     if (array.length < minSize) {
/* 196 */       byte[] newArray = new byte[getNextSize(minSize)];
/* 197 */       System.arraycopy(array, 0, newArray, 0, array.length);
/* 198 */       return newArray;
/*     */     }
/* 200 */     return array;
/*     */   }
/*     */ 
/*     */   public static byte[] grow(byte[] array) {
/* 204 */     return grow(array, 1 + array.length);
/*     */   }
/*     */ 
/*     */   public static byte[] shrink(byte[] array, int targetSize) {
/* 208 */     int newSize = getShrinkSize(array.length, targetSize);
/* 209 */     if (newSize != array.length) {
/* 210 */       byte[] newArray = new byte[newSize];
/* 211 */       System.arraycopy(array, 0, newArray, 0, newSize);
/* 212 */       return newArray;
/*     */     }
/* 214 */     return array;
/*     */   }
/*     */ 
/*     */   public static int hashCode(char[] array, int start, int end)
/*     */   {
/* 222 */     int code = 0;
/* 223 */     for (int i = end - 1; i >= start; i--)
/* 224 */       code = code * 31 + array[i];
/* 225 */     return code;
/*     */   }
/*     */ 
/*     */   public static int hashCode(byte[] array, int start, int end)
/*     */   {
/* 233 */     int code = 0;
/* 234 */     for (int i = end - 1; i >= start; i--)
/* 235 */       code = code * 31 + array[i];
/* 236 */     return code;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.ArrayUtil
 * JD-Core Version:    0.6.2
 */