/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ public class BitUtil
/*     */ {
/* 688 */   public static final byte[] ntzTable = { 8, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 7, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0, 4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0 };
/*     */ 
/*     */   public static int pop(long x)
/*     */   {
/*  42 */     x -= (x >>> 1 & 0x55555555);
/*  43 */     x = (x & 0x33333333) + (x >>> 2 & 0x33333333);
/*  44 */     x = x + (x >>> 4) & 0xF0F0F0F;
/*  45 */     x += (x >>> 8);
/*  46 */     x += (x >>> 16);
/*  47 */     x += (x >>> 32);
/*  48 */     return (int)x & 0x7F;
/*     */   }
/*     */ 
/*     */   public static long pop_array(long[] A, int wordOffset, int numWords)
/*     */   {
/*  67 */     int n = wordOffset + numWords;
/*  68 */     long tot = 0L; long tot8 = 0L;
/*  69 */     long ones = 0L; long twos = 0L; long fours = 0L;
/*     */ 
/*  72 */     for (int i = wordOffset; i <= n - 8; i += 8)
/*     */     {
/*  83 */       long b = A[i]; long c = A[(i + 1)];
/*  84 */       long u = ones ^ b;
/*  85 */       long twosA = ones & b | u & c;
/*  86 */       ones = u ^ c;
/*     */ 
/*  90 */       long b = A[(i + 2)]; long c = A[(i + 3)];
/*  91 */       long u = ones ^ b;
/*  92 */       long twosB = ones & b | u & c;
/*  93 */       ones = u ^ c;
/*     */ 
/*  97 */       long u = twos ^ twosA;
/*  98 */       long foursA = twos & twosA | u & twosB;
/*  99 */       twos = u ^ twosB;
/*     */ 
/* 103 */       long b = A[(i + 4)]; long c = A[(i + 5)];
/* 104 */       long u = ones ^ b;
/* 105 */       twosA = ones & b | u & c;
/* 106 */       ones = u ^ c;
/*     */ 
/* 110 */       long b = A[(i + 6)]; long c = A[(i + 7)];
/* 111 */       long u = ones ^ b;
/* 112 */       twosB = ones & b | u & c;
/* 113 */       ones = u ^ c;
/*     */ 
/* 117 */       long u = twos ^ twosA;
/* 118 */       long foursB = twos & twosA | u & twosB;
/* 119 */       twos = u ^ twosB;
/*     */ 
/* 124 */       long u = fours ^ foursA;
/* 125 */       long eights = fours & foursA | u & foursB;
/* 126 */       fours = u ^ foursB;
/*     */ 
/* 128 */       tot8 += pop(eights);
/*     */     }
/*     */ 
/* 137 */     if (i <= n - 4)
/*     */     {
/* 140 */       long b = A[i]; long c = A[(i + 1)];
/* 141 */       long u = ones ^ b;
/* 142 */       long twosA = ones & b | u & c;
/* 143 */       ones = u ^ c;
/*     */ 
/* 146 */       long b = A[(i + 2)]; long c = A[(i + 3)];
/* 147 */       long u = ones ^ b;
/* 148 */       long twosB = ones & b | u & c;
/* 149 */       ones = u ^ c;
/*     */ 
/* 152 */       long u = twos ^ twosA;
/* 153 */       long foursA = twos & twosA | u & twosB;
/* 154 */       twos = u ^ twosB;
/*     */ 
/* 156 */       long eights = fours & foursA;
/* 157 */       fours ^= foursA;
/*     */ 
/* 159 */       tot8 += pop(eights);
/* 160 */       i += 4;
/*     */     }
/*     */ 
/* 163 */     if (i <= n - 2) {
/* 164 */       long b = A[i]; long c = A[(i + 1)];
/* 165 */       long u = ones ^ b;
/* 166 */       long twosA = ones & b | u & c;
/* 167 */       ones = u ^ c;
/*     */ 
/* 169 */       long foursA = twos & twosA;
/* 170 */       twos ^= twosA;
/*     */ 
/* 172 */       long eights = fours & foursA;
/* 173 */       fours ^= foursA;
/*     */ 
/* 175 */       tot8 += pop(eights);
/* 176 */       i += 2;
/*     */     }
/*     */ 
/* 179 */     if (i < n) {
/* 180 */       tot += pop(A[i]);
/*     */     }
/*     */ 
/* 183 */     tot += (pop(fours) << 2) + (pop(twos) << 1) + pop(ones) + (tot8 << 3);
/*     */ 
/* 188 */     return tot;
/*     */   }
/*     */ 
/*     */   public static long pop_intersect(long[] A, long[] B, int wordOffset, int numWords)
/*     */   {
/* 196 */     int n = wordOffset + numWords;
/* 197 */     long tot = 0L; long tot8 = 0L;
/* 198 */     long ones = 0L; long twos = 0L; long fours = 0L;
/*     */ 
/* 201 */     for (int i = wordOffset; i <= n - 8; i += 8)
/*     */     {
/* 206 */       long b = A[i] & B[i]; long c = A[(i + 1)] & B[(i + 1)];
/* 207 */       long u = ones ^ b;
/* 208 */       long twosA = ones & b | u & c;
/* 209 */       ones = u ^ c;
/*     */ 
/* 213 */       long b = A[(i + 2)] & B[(i + 2)]; long c = A[(i + 3)] & B[(i + 3)];
/* 214 */       long u = ones ^ b;
/* 215 */       long twosB = ones & b | u & c;
/* 216 */       ones = u ^ c;
/*     */ 
/* 220 */       long u = twos ^ twosA;
/* 221 */       long foursA = twos & twosA | u & twosB;
/* 222 */       twos = u ^ twosB;
/*     */ 
/* 226 */       long b = A[(i + 4)] & B[(i + 4)]; long c = A[(i + 5)] & B[(i + 5)];
/* 227 */       long u = ones ^ b;
/* 228 */       twosA = ones & b | u & c;
/* 229 */       ones = u ^ c;
/*     */ 
/* 233 */       long b = A[(i + 6)] & B[(i + 6)]; long c = A[(i + 7)] & B[(i + 7)];
/* 234 */       long u = ones ^ b;
/* 235 */       twosB = ones & b | u & c;
/* 236 */       ones = u ^ c;
/*     */ 
/* 240 */       long u = twos ^ twosA;
/* 241 */       long foursB = twos & twosA | u & twosB;
/* 242 */       twos = u ^ twosB;
/*     */ 
/* 247 */       long u = fours ^ foursA;
/* 248 */       long eights = fours & foursA | u & foursB;
/* 249 */       fours = u ^ foursB;
/*     */ 
/* 251 */       tot8 += pop(eights);
/*     */     }
/*     */ 
/* 255 */     if (i <= n - 4)
/*     */     {
/* 258 */       long b = A[i] & B[i]; long c = A[(i + 1)] & B[(i + 1)];
/* 259 */       long u = ones ^ b;
/* 260 */       long twosA = ones & b | u & c;
/* 261 */       ones = u ^ c;
/*     */ 
/* 264 */       long b = A[(i + 2)] & B[(i + 2)]; long c = A[(i + 3)] & B[(i + 3)];
/* 265 */       long u = ones ^ b;
/* 266 */       long twosB = ones & b | u & c;
/* 267 */       ones = u ^ c;
/*     */ 
/* 270 */       long u = twos ^ twosA;
/* 271 */       long foursA = twos & twosA | u & twosB;
/* 272 */       twos = u ^ twosB;
/*     */ 
/* 274 */       long eights = fours & foursA;
/* 275 */       fours ^= foursA;
/*     */ 
/* 277 */       tot8 += pop(eights);
/* 278 */       i += 4;
/*     */     }
/*     */ 
/* 281 */     if (i <= n - 2) {
/* 282 */       long b = A[i] & B[i]; long c = A[(i + 1)] & B[(i + 1)];
/* 283 */       long u = ones ^ b;
/* 284 */       long twosA = ones & b | u & c;
/* 285 */       ones = u ^ c;
/*     */ 
/* 287 */       long foursA = twos & twosA;
/* 288 */       twos ^= twosA;
/*     */ 
/* 290 */       long eights = fours & foursA;
/* 291 */       fours ^= foursA;
/*     */ 
/* 293 */       tot8 += pop(eights);
/* 294 */       i += 2;
/*     */     }
/*     */ 
/* 297 */     if (i < n) {
/* 298 */       tot += pop(A[i] & B[i]);
/*     */     }
/*     */ 
/* 301 */     tot += (pop(fours) << 2) + (pop(twos) << 1) + pop(ones) + (tot8 << 3);
/*     */ 
/* 306 */     return tot;
/*     */   }
/*     */ 
/*     */   public static long pop_union(long[] A, long[] B, int wordOffset, int numWords)
/*     */   {
/* 314 */     int n = wordOffset + numWords;
/* 315 */     long tot = 0L; long tot8 = 0L;
/* 316 */     long ones = 0L; long twos = 0L; long fours = 0L;
/*     */ 
/* 319 */     for (int i = wordOffset; i <= n - 8; i += 8)
/*     */     {
/* 330 */       long b = A[i] | B[i]; long c = A[(i + 1)] | B[(i + 1)];
/* 331 */       long u = ones ^ b;
/* 332 */       long twosA = ones & b | u & c;
/* 333 */       ones = u ^ c;
/*     */ 
/* 337 */       long b = A[(i + 2)] | B[(i + 2)]; long c = A[(i + 3)] | B[(i + 3)];
/* 338 */       long u = ones ^ b;
/* 339 */       long twosB = ones & b | u & c;
/* 340 */       ones = u ^ c;
/*     */ 
/* 344 */       long u = twos ^ twosA;
/* 345 */       long foursA = twos & twosA | u & twosB;
/* 346 */       twos = u ^ twosB;
/*     */ 
/* 350 */       long b = A[(i + 4)] | B[(i + 4)]; long c = A[(i + 5)] | B[(i + 5)];
/* 351 */       long u = ones ^ b;
/* 352 */       twosA = ones & b | u & c;
/* 353 */       ones = u ^ c;
/*     */ 
/* 357 */       long b = A[(i + 6)] | B[(i + 6)]; long c = A[(i + 7)] | B[(i + 7)];
/* 358 */       long u = ones ^ b;
/* 359 */       twosB = ones & b | u & c;
/* 360 */       ones = u ^ c;
/*     */ 
/* 364 */       long u = twos ^ twosA;
/* 365 */       long foursB = twos & twosA | u & twosB;
/* 366 */       twos = u ^ twosB;
/*     */ 
/* 371 */       long u = fours ^ foursA;
/* 372 */       long eights = fours & foursA | u & foursB;
/* 373 */       fours = u ^ foursB;
/*     */ 
/* 375 */       tot8 += pop(eights);
/*     */     }
/*     */ 
/* 379 */     if (i <= n - 4)
/*     */     {
/* 382 */       long b = A[i] | B[i]; long c = A[(i + 1)] | B[(i + 1)];
/* 383 */       long u = ones ^ b;
/* 384 */       long twosA = ones & b | u & c;
/* 385 */       ones = u ^ c;
/*     */ 
/* 388 */       long b = A[(i + 2)] | B[(i + 2)]; long c = A[(i + 3)] | B[(i + 3)];
/* 389 */       long u = ones ^ b;
/* 390 */       long twosB = ones & b | u & c;
/* 391 */       ones = u ^ c;
/*     */ 
/* 394 */       long u = twos ^ twosA;
/* 395 */       long foursA = twos & twosA | u & twosB;
/* 396 */       twos = u ^ twosB;
/*     */ 
/* 398 */       long eights = fours & foursA;
/* 399 */       fours ^= foursA;
/*     */ 
/* 401 */       tot8 += pop(eights);
/* 402 */       i += 4;
/*     */     }
/*     */ 
/* 405 */     if (i <= n - 2) {
/* 406 */       long b = A[i] | B[i]; long c = A[(i + 1)] | B[(i + 1)];
/* 407 */       long u = ones ^ b;
/* 408 */       long twosA = ones & b | u & c;
/* 409 */       ones = u ^ c;
/*     */ 
/* 411 */       long foursA = twos & twosA;
/* 412 */       twos ^= twosA;
/*     */ 
/* 414 */       long eights = fours & foursA;
/* 415 */       fours ^= foursA;
/*     */ 
/* 417 */       tot8 += pop(eights);
/* 418 */       i += 2;
/*     */     }
/*     */ 
/* 421 */     if (i < n) {
/* 422 */       tot += pop(A[i] | B[i]);
/*     */     }
/*     */ 
/* 425 */     tot += (pop(fours) << 2) + (pop(twos) << 1) + pop(ones) + (tot8 << 3);
/*     */ 
/* 430 */     return tot;
/*     */   }
/*     */ 
/*     */   public static long pop_andnot(long[] A, long[] B, int wordOffset, int numWords)
/*     */   {
/* 438 */     int n = wordOffset + numWords;
/* 439 */     long tot = 0L; long tot8 = 0L;
/* 440 */     long ones = 0L; long twos = 0L; long fours = 0L;
/*     */ 
/* 443 */     for (int i = wordOffset; i <= n - 8; i += 8)
/*     */     {
/* 454 */       long b = A[i] & (B[i] ^ 0xFFFFFFFF); long c = A[(i + 1)] & (B[(i + 1)] ^ 0xFFFFFFFF);
/* 455 */       long u = ones ^ b;
/* 456 */       long twosA = ones & b | u & c;
/* 457 */       ones = u ^ c;
/*     */ 
/* 461 */       long b = A[(i + 2)] & (B[(i + 2)] ^ 0xFFFFFFFF); long c = A[(i + 3)] & (B[(i + 3)] ^ 0xFFFFFFFF);
/* 462 */       long u = ones ^ b;
/* 463 */       long twosB = ones & b | u & c;
/* 464 */       ones = u ^ c;
/*     */ 
/* 468 */       long u = twos ^ twosA;
/* 469 */       long foursA = twos & twosA | u & twosB;
/* 470 */       twos = u ^ twosB;
/*     */ 
/* 474 */       long b = A[(i + 4)] & (B[(i + 4)] ^ 0xFFFFFFFF); long c = A[(i + 5)] & (B[(i + 5)] ^ 0xFFFFFFFF);
/* 475 */       long u = ones ^ b;
/* 476 */       twosA = ones & b | u & c;
/* 477 */       ones = u ^ c;
/*     */ 
/* 481 */       long b = A[(i + 6)] & (B[(i + 6)] ^ 0xFFFFFFFF); long c = A[(i + 7)] & (B[(i + 7)] ^ 0xFFFFFFFF);
/* 482 */       long u = ones ^ b;
/* 483 */       twosB = ones & b | u & c;
/* 484 */       ones = u ^ c;
/*     */ 
/* 488 */       long u = twos ^ twosA;
/* 489 */       long foursB = twos & twosA | u & twosB;
/* 490 */       twos = u ^ twosB;
/*     */ 
/* 495 */       long u = fours ^ foursA;
/* 496 */       long eights = fours & foursA | u & foursB;
/* 497 */       fours = u ^ foursB;
/*     */ 
/* 499 */       tot8 += pop(eights);
/*     */     }
/*     */ 
/* 503 */     if (i <= n - 4)
/*     */     {
/* 506 */       long b = A[i] & (B[i] ^ 0xFFFFFFFF); long c = A[(i + 1)] & (B[(i + 1)] ^ 0xFFFFFFFF);
/* 507 */       long u = ones ^ b;
/* 508 */       long twosA = ones & b | u & c;
/* 509 */       ones = u ^ c;
/*     */ 
/* 512 */       long b = A[(i + 2)] & (B[(i + 2)] ^ 0xFFFFFFFF); long c = A[(i + 3)] & (B[(i + 3)] ^ 0xFFFFFFFF);
/* 513 */       long u = ones ^ b;
/* 514 */       long twosB = ones & b | u & c;
/* 515 */       ones = u ^ c;
/*     */ 
/* 518 */       long u = twos ^ twosA;
/* 519 */       long foursA = twos & twosA | u & twosB;
/* 520 */       twos = u ^ twosB;
/*     */ 
/* 522 */       long eights = fours & foursA;
/* 523 */       fours ^= foursA;
/*     */ 
/* 525 */       tot8 += pop(eights);
/* 526 */       i += 4;
/*     */     }
/*     */ 
/* 529 */     if (i <= n - 2) {
/* 530 */       long b = A[i] & (B[i] ^ 0xFFFFFFFF); long c = A[(i + 1)] & (B[(i + 1)] ^ 0xFFFFFFFF);
/* 531 */       long u = ones ^ b;
/* 532 */       long twosA = ones & b | u & c;
/* 533 */       ones = u ^ c;
/*     */ 
/* 535 */       long foursA = twos & twosA;
/* 536 */       twos ^= twosA;
/*     */ 
/* 538 */       long eights = fours & foursA;
/* 539 */       fours ^= foursA;
/*     */ 
/* 541 */       tot8 += pop(eights);
/* 542 */       i += 2;
/*     */     }
/*     */ 
/* 545 */     if (i < n) {
/* 546 */       tot += pop(A[i] & (B[i] ^ 0xFFFFFFFF));
/*     */     }
/*     */ 
/* 549 */     tot += (pop(fours) << 2) + (pop(twos) << 1) + pop(ones) + (tot8 << 3);
/*     */ 
/* 554 */     return tot;
/*     */   }
/*     */ 
/*     */   public static long pop_xor(long[] A, long[] B, int wordOffset, int numWords) {
/* 558 */     int n = wordOffset + numWords;
/* 559 */     long tot = 0L; long tot8 = 0L;
/* 560 */     long ones = 0L; long twos = 0L; long fours = 0L;
/*     */ 
/* 563 */     for (int i = wordOffset; i <= n - 8; i += 8)
/*     */     {
/* 574 */       long b = A[i] ^ B[i]; long c = A[(i + 1)] ^ B[(i + 1)];
/* 575 */       long u = ones ^ b;
/* 576 */       long twosA = ones & b | u & c;
/* 577 */       ones = u ^ c;
/*     */ 
/* 581 */       long b = A[(i + 2)] ^ B[(i + 2)]; long c = A[(i + 3)] ^ B[(i + 3)];
/* 582 */       long u = ones ^ b;
/* 583 */       long twosB = ones & b | u & c;
/* 584 */       ones = u ^ c;
/*     */ 
/* 588 */       long u = twos ^ twosA;
/* 589 */       long foursA = twos & twosA | u & twosB;
/* 590 */       twos = u ^ twosB;
/*     */ 
/* 594 */       long b = A[(i + 4)] ^ B[(i + 4)]; long c = A[(i + 5)] ^ B[(i + 5)];
/* 595 */       long u = ones ^ b;
/* 596 */       twosA = ones & b | u & c;
/* 597 */       ones = u ^ c;
/*     */ 
/* 601 */       long b = A[(i + 6)] ^ B[(i + 6)]; long c = A[(i + 7)] ^ B[(i + 7)];
/* 602 */       long u = ones ^ b;
/* 603 */       twosB = ones & b | u & c;
/* 604 */       ones = u ^ c;
/*     */ 
/* 608 */       long u = twos ^ twosA;
/* 609 */       long foursB = twos & twosA | u & twosB;
/* 610 */       twos = u ^ twosB;
/*     */ 
/* 615 */       long u = fours ^ foursA;
/* 616 */       long eights = fours & foursA | u & foursB;
/* 617 */       fours = u ^ foursB;
/*     */ 
/* 619 */       tot8 += pop(eights);
/*     */     }
/*     */ 
/* 623 */     if (i <= n - 4)
/*     */     {
/* 626 */       long b = A[i] ^ B[i]; long c = A[(i + 1)] ^ B[(i + 1)];
/* 627 */       long u = ones ^ b;
/* 628 */       long twosA = ones & b | u & c;
/* 629 */       ones = u ^ c;
/*     */ 
/* 632 */       long b = A[(i + 2)] ^ B[(i + 2)]; long c = A[(i + 3)] ^ B[(i + 3)];
/* 633 */       long u = ones ^ b;
/* 634 */       long twosB = ones & b | u & c;
/* 635 */       ones = u ^ c;
/*     */ 
/* 638 */       long u = twos ^ twosA;
/* 639 */       long foursA = twos & twosA | u & twosB;
/* 640 */       twos = u ^ twosB;
/*     */ 
/* 642 */       long eights = fours & foursA;
/* 643 */       fours ^= foursA;
/*     */ 
/* 645 */       tot8 += pop(eights);
/* 646 */       i += 4;
/*     */     }
/*     */ 
/* 649 */     if (i <= n - 2) {
/* 650 */       long b = A[i] ^ B[i]; long c = A[(i + 1)] ^ B[(i + 1)];
/* 651 */       long u = ones ^ b;
/* 652 */       long twosA = ones & b | u & c;
/* 653 */       ones = u ^ c;
/*     */ 
/* 655 */       long foursA = twos & twosA;
/* 656 */       twos ^= twosA;
/*     */ 
/* 658 */       long eights = fours & foursA;
/* 659 */       fours ^= foursA;
/*     */ 
/* 661 */       tot8 += pop(eights);
/* 662 */       i += 2;
/*     */     }
/*     */ 
/* 665 */     if (i < n) {
/* 666 */       tot += pop(A[i] ^ B[i]);
/*     */     }
/*     */ 
/* 669 */     tot += (pop(fours) << 2) + (pop(twos) << 1) + pop(ones) + (tot8 << 3);
/*     */ 
/* 674 */     return tot;
/*     */   }
/*     */ 
/*     */   public static int ntz(long val)
/*     */   {
/* 704 */     int lower = (int)val;
/* 705 */     int lowByte = lower & 0xFF;
/* 706 */     if (lowByte != 0) return ntzTable[lowByte];
/*     */ 
/* 708 */     if (lower != 0) {
/* 709 */       lowByte = lower >>> 8 & 0xFF;
/* 710 */       if (lowByte != 0) return ntzTable[lowByte] + 8;
/* 711 */       lowByte = lower >>> 16 & 0xFF;
/* 712 */       if (lowByte != 0) return ntzTable[lowByte] + 16;
/*     */ 
/* 715 */       return ntzTable[(lower >>> 24)] + 24;
/*     */     }
/*     */ 
/* 718 */     int upper = (int)(val >> 32);
/* 719 */     lowByte = upper & 0xFF;
/* 720 */     if (lowByte != 0) return ntzTable[lowByte] + 32;
/* 721 */     lowByte = upper >>> 8 & 0xFF;
/* 722 */     if (lowByte != 0) return ntzTable[lowByte] + 40;
/* 723 */     lowByte = upper >>> 16 & 0xFF;
/* 724 */     if (lowByte != 0) return ntzTable[lowByte] + 48;
/*     */ 
/* 727 */     return ntzTable[(upper >>> 24)] + 56;
/*     */   }
/*     */ 
/*     */   public static int ntz(int val)
/*     */   {
/* 737 */     int lowByte = val & 0xFF;
/* 738 */     if (lowByte != 0) return ntzTable[lowByte];
/* 739 */     lowByte = val >>> 8 & 0xFF;
/* 740 */     if (lowByte != 0) return ntzTable[lowByte] + 8;
/* 741 */     lowByte = val >>> 16 & 0xFF;
/* 742 */     if (lowByte != 0) return ntzTable[lowByte] + 16;
/*     */ 
/* 745 */     return ntzTable[(val >>> 24)] + 24;
/*     */   }
/*     */ 
/*     */   public static int ntz2(long x)
/*     */   {
/* 753 */     int n = 0;
/* 754 */     int y = (int)x;
/* 755 */     if (y == 0) { n += 32; y = (int)(x >>> 32); }
/* 756 */     if ((y & 0xFFFF) == 0) { n += 16; y >>>= 16; }
/* 757 */     if ((y & 0xFF) == 0) { n += 8; y >>>= 8; }
/* 758 */     return ntzTable[(y & 0xFF)] + n;
/*     */   }
/*     */ 
/*     */   public static int ntz3(long x)
/*     */   {
/* 768 */     int n = 1;
/*     */ 
/* 771 */     int y = (int)x;
/* 772 */     if (y == 0) { n += 32; y = (int)(x >>> 32); }
/* 773 */     if ((y & 0xFFFF) == 0) { n += 16; y >>>= 16; }
/* 774 */     if ((y & 0xFF) == 0) { n += 8; y >>>= 8; }
/* 775 */     if ((y & 0xF) == 0) { n += 4; y >>>= 4; }
/* 776 */     if ((y & 0x3) == 0) { n += 2; y >>>= 2; }
/* 777 */     return n - (y & 0x1);
/*     */   }
/*     */ 
/*     */   public static boolean isPowerOfTwo(int v)
/*     */   {
/* 783 */     return (v & v - 1) == 0;
/*     */   }
/*     */ 
/*     */   public static boolean isPowerOfTwo(long v)
/*     */   {
/* 788 */     return (v & v - 1L) == 0L;
/*     */   }
/*     */ 
/*     */   public static int nextHighestPowerOfTwo(int v)
/*     */   {
/* 793 */     v--;
/* 794 */     v |= v >> 1;
/* 795 */     v |= v >> 2;
/* 796 */     v |= v >> 4;
/* 797 */     v |= v >> 8;
/* 798 */     v |= v >> 16;
/* 799 */     v++;
/* 800 */     return v;
/*     */   }
/*     */ 
/*     */   public static long nextHighestPowerOfTwo(long v)
/*     */   {
/* 805 */     v -= 1L;
/* 806 */     v |= v >> 1;
/* 807 */     v |= v >> 2;
/* 808 */     v |= v >> 4;
/* 809 */     v |= v >> 8;
/* 810 */     v |= v >> 16;
/* 811 */     v |= v >> 32;
/* 812 */     v += 1L;
/* 813 */     return v;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.BitUtil
 * JD-Core Version:    0.6.2
 */