/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ public final class UnicodeUtil
/*     */ {
/*     */   public static final int UNI_SUR_HIGH_START = 55296;
/*     */   public static final int UNI_SUR_HIGH_END = 56319;
/*     */   public static final int UNI_SUR_LOW_START = 56320;
/*     */   public static final int UNI_SUR_LOW_END = 57343;
/*     */   public static final int UNI_REPLACEMENT_CHAR = 65533;
/*     */   private static final long UNI_MAX_BMP = 65535L;
/*     */   private static final int HALF_BASE = 65536;
/*     */   private static final long HALF_SHIFT = 10L;
/*     */   private static final long HALF_MASK = 1023L;
/*     */ 
/*     */   public static void UTF16toUTF8(char[] source, int offset, UTF8Result result)
/*     */   {
/* 114 */     int upto = 0;
/* 115 */     int i = offset;
/* 116 */     byte[] out = result.result;
/*     */     while (true)
/*     */     {
/* 120 */       int code = source[(i++)];
/*     */ 
/* 122 */       if (upto + 4 > out.length) {
/* 123 */         byte[] newOut = new byte[2 * out.length];
/* 124 */         assert (newOut.length >= upto + 4);
/* 125 */         System.arraycopy(out, 0, newOut, 0, upto);
/* 126 */         result.result = (out = newOut);
/*     */       }
/* 128 */       if (code < 128) {
/* 129 */         out[(upto++)] = ((byte)code);
/* 130 */       } else if (code < 2048) {
/* 131 */         out[(upto++)] = ((byte)(0xC0 | code >> 6));
/* 132 */         out[(upto++)] = ((byte)(0x80 | code & 0x3F));
/* 133 */       } else if ((code < 55296) || (code > 57343)) {
/* 134 */         if (code == 65535) {
/*     */           break;
/*     */         }
/* 137 */         out[(upto++)] = ((byte)(0xE0 | code >> 12));
/* 138 */         out[(upto++)] = ((byte)(0x80 | code >> 6 & 0x3F));
/* 139 */         out[(upto++)] = ((byte)(0x80 | code & 0x3F));
/*     */       }
/* 143 */       else if ((code < 56320) && (source[i] != 65535)) {
/* 144 */         int utf32 = source[i];
/*     */ 
/* 146 */         if ((utf32 >= 56320) && (utf32 <= 57343)) {
/* 147 */           utf32 = (code - 55232 << 10) + (utf32 & 0x3FF);
/* 148 */           i++;
/* 149 */           out[(upto++)] = ((byte)(0xF0 | utf32 >> 18));
/* 150 */           out[(upto++)] = ((byte)(0x80 | utf32 >> 12 & 0x3F));
/* 151 */           out[(upto++)] = ((byte)(0x80 | utf32 >> 6 & 0x3F));
/* 152 */           out[(upto++)] = ((byte)(0x80 | utf32 & 0x3F));
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 158 */         out[(upto++)] = -17;
/* 159 */         out[(upto++)] = -65;
/* 160 */         out[(upto++)] = -67;
/*     */       }
/*     */     }
/*     */ 
/* 164 */     result.length = upto;
/*     */   }
/*     */ 
/*     */   public static void UTF16toUTF8(char[] source, int offset, int length, UTF8Result result)
/*     */   {
/* 172 */     int upto = 0;
/* 173 */     int i = offset;
/* 174 */     int end = offset + length;
/* 175 */     byte[] out = result.result;
/*     */ 
/* 177 */     while (i < end)
/*     */     {
/* 179 */       int code = source[(i++)];
/*     */ 
/* 181 */       if (upto + 4 > out.length) {
/* 182 */         byte[] newOut = new byte[2 * out.length];
/* 183 */         assert (newOut.length >= upto + 4);
/* 184 */         System.arraycopy(out, 0, newOut, 0, upto);
/* 185 */         result.result = (out = newOut);
/*     */       }
/* 187 */       if (code < 128) {
/* 188 */         out[(upto++)] = ((byte)code);
/* 189 */       } else if (code < 2048) {
/* 190 */         out[(upto++)] = ((byte)(0xC0 | code >> 6));
/* 191 */         out[(upto++)] = ((byte)(0x80 | code & 0x3F));
/* 192 */       } else if ((code < 55296) || (code > 57343)) {
/* 193 */         out[(upto++)] = ((byte)(0xE0 | code >> 12));
/* 194 */         out[(upto++)] = ((byte)(0x80 | code >> 6 & 0x3F));
/* 195 */         out[(upto++)] = ((byte)(0x80 | code & 0x3F));
/*     */       }
/* 199 */       else if ((code < 56320) && (i < end) && (source[i] != 65535)) {
/* 200 */         int utf32 = source[i];
/*     */ 
/* 202 */         if ((utf32 >= 56320) && (utf32 <= 57343)) {
/* 203 */           utf32 = (code - 55232 << 10) + (utf32 & 0x3FF);
/* 204 */           i++;
/* 205 */           out[(upto++)] = ((byte)(0xF0 | utf32 >> 18));
/* 206 */           out[(upto++)] = ((byte)(0x80 | utf32 >> 12 & 0x3F));
/* 207 */           out[(upto++)] = ((byte)(0x80 | utf32 >> 6 & 0x3F));
/* 208 */           out[(upto++)] = ((byte)(0x80 | utf32 & 0x3F));
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 214 */         out[(upto++)] = -17;
/* 215 */         out[(upto++)] = -65;
/* 216 */         out[(upto++)] = -67;
/*     */       }
/*     */     }
/*     */ 
/* 220 */     result.length = upto;
/*     */   }
/*     */ 
/*     */   public static void UTF16toUTF8(String s, int offset, int length, UTF8Result result)
/*     */   {
/* 227 */     int end = offset + length;
/*     */ 
/* 229 */     byte[] out = result.result;
/*     */ 
/* 231 */     int upto = 0;
/* 232 */     for (int i = offset; i < end; i++) {
/* 233 */       int code = s.charAt(i);
/*     */ 
/* 235 */       if (upto + 4 > out.length) {
/* 236 */         byte[] newOut = new byte[2 * out.length];
/* 237 */         assert (newOut.length >= upto + 4);
/* 238 */         System.arraycopy(out, 0, newOut, 0, upto);
/* 239 */         result.result = (out = newOut);
/*     */       }
/* 241 */       if (code < 128) {
/* 242 */         out[(upto++)] = ((byte)code);
/* 243 */       } else if (code < 2048) {
/* 244 */         out[(upto++)] = ((byte)(0xC0 | code >> 6));
/* 245 */         out[(upto++)] = ((byte)(0x80 | code & 0x3F));
/* 246 */       } else if ((code < 55296) || (code > 57343)) {
/* 247 */         out[(upto++)] = ((byte)(0xE0 | code >> 12));
/* 248 */         out[(upto++)] = ((byte)(0x80 | code >> 6 & 0x3F));
/* 249 */         out[(upto++)] = ((byte)(0x80 | code & 0x3F));
/*     */       }
/*     */       else
/*     */       {
/* 253 */         if ((code < 56320) && (i < end - 1)) {
/* 254 */           int utf32 = s.charAt(i + 1);
/*     */ 
/* 256 */           if ((utf32 >= 56320) && (utf32 <= 57343)) {
/* 257 */             utf32 = (code - 55232 << 10) + (utf32 & 0x3FF);
/* 258 */             i++;
/* 259 */             out[(upto++)] = ((byte)(0xF0 | utf32 >> 18));
/* 260 */             out[(upto++)] = ((byte)(0x80 | utf32 >> 12 & 0x3F));
/* 261 */             out[(upto++)] = ((byte)(0x80 | utf32 >> 6 & 0x3F));
/* 262 */             out[(upto++)] = ((byte)(0x80 | utf32 & 0x3F));
/* 263 */             continue;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 268 */         out[(upto++)] = -17;
/* 269 */         out[(upto++)] = -65;
/* 270 */         out[(upto++)] = -67;
/*     */       }
/*     */     }
/*     */ 
/* 274 */     result.length = upto;
/*     */   }
/*     */ 
/*     */   public static void UTF8toUTF16(byte[] utf8, int offset, int length, UTF16Result result)
/*     */   {
/* 283 */     int end = offset + length;
/* 284 */     char[] out = result.result;
/* 285 */     if (result.offsets.length <= end) {
/* 286 */       int[] newOffsets = new int[2 * end];
/* 287 */       System.arraycopy(result.offsets, 0, newOffsets, 0, result.offsets.length);
/* 288 */       result.offsets = newOffsets;
/*     */     }
/* 290 */     int[] offsets = result.offsets;
/*     */ 
/* 294 */     int upto = offset;
/* 295 */     while (offsets[upto] == -1) {
/* 296 */       upto--;
/*     */     }
/* 298 */     int outUpto = offsets[upto];
/*     */ 
/* 301 */     if (outUpto + length >= out.length) {
/* 302 */       char[] newOut = new char[2 * (outUpto + length)];
/* 303 */       System.arraycopy(out, 0, newOut, 0, outUpto);
/* 304 */       result.result = (out = newOut);
/*     */     }
/*     */ 
/* 307 */     while (upto < end)
/*     */     {
/* 309 */       int b = utf8[upto] & 0xFF;
/*     */ 
/* 312 */       offsets[(upto++)] = outUpto;
/*     */       int ch;
/*     */       int ch;
/* 314 */       if (b < 192) {
/* 315 */         assert (b < 128);
/* 316 */         ch = b;
/* 317 */       } else if (b < 224) {
/* 318 */         int ch = ((b & 0x1F) << 6) + (utf8[upto] & 0x3F);
/* 319 */         offsets[(upto++)] = -1;
/* 320 */       } else if (b < 240) {
/* 321 */         int ch = ((b & 0xF) << 12) + ((utf8[upto] & 0x3F) << 6) + (utf8[(upto + 1)] & 0x3F);
/* 322 */         offsets[(upto++)] = -1;
/* 323 */         offsets[(upto++)] = -1;
/*     */       } else {
/* 325 */         assert (b < 248);
/* 326 */         ch = ((b & 0x7) << 18) + ((utf8[upto] & 0x3F) << 12) + ((utf8[(upto + 1)] & 0x3F) << 6) + (utf8[(upto + 2)] & 0x3F);
/* 327 */         offsets[(upto++)] = -1;
/* 328 */         offsets[(upto++)] = -1;
/* 329 */         offsets[(upto++)] = -1;
/*     */       }
/*     */ 
/* 332 */       if (ch <= 65535L)
/*     */       {
/* 334 */         out[(outUpto++)] = ((char)ch);
/*     */       }
/*     */       else {
/* 337 */         int chHalf = ch - 65536;
/* 338 */         out[(outUpto++)] = ((char)((chHalf >> 10) + 55296));
/* 339 */         out[(outUpto++)] = ((char)(int)((chHalf & 0x3FF) + 56320L));
/*     */       }
/*     */     }
/*     */ 
/* 343 */     offsets[upto] = outUpto;
/* 344 */     result.length = outUpto;
/*     */   }
/*     */ 
/*     */   public static final class UTF16Result
/*     */   {
/*  90 */     public char[] result = new char[10];
/*  91 */     public int[] offsets = new int[10];
/*     */     public int length;
/*     */ 
/*     */     public void setLength(int newLength)
/*     */     {
/*  95 */       if (this.result.length < newLength) {
/*  96 */         char[] newArray = new char[(int)(1.5D * newLength)];
/*  97 */         System.arraycopy(this.result, 0, newArray, 0, this.length);
/*  98 */         this.result = newArray;
/*     */       }
/* 100 */       this.length = newLength;
/*     */     }
/*     */ 
/*     */     public void copyText(UTF16Result other) {
/* 104 */       setLength(other.length);
/* 105 */       System.arraycopy(other.result, 0, this.result, 0, this.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class UTF8Result
/*     */   {
/*  76 */     public byte[] result = new byte[10];
/*     */     public int length;
/*     */ 
/*     */     public void setLength(int newLength)
/*     */     {
/*  80 */       if (this.result.length < newLength) {
/*  81 */         byte[] newArray = new byte[(int)(1.5D * newLength)];
/*  82 */         System.arraycopy(this.result, 0, newArray, 0, this.length);
/*  83 */         this.result = newArray;
/*     */       }
/*  85 */       this.length = newLength;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.UnicodeUtil
 * JD-Core Version:    0.6.2
 */