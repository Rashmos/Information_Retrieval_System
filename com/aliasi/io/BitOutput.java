/*     */ package com.aliasi.io;
/*     */ 
/*     */ import com.aliasi.util.Math;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class BitOutput
/*     */ {
/*     */   private int mNextByte;
/*     */   private int mNextBitIndex;
/*     */   private final OutputStream mOut;
/*     */   private static final long ALL_ONES_LONG = -1L;
/* 676 */   private static final boolean[] FIB_BUF = new boolean[Math.FIBONACCI_SEQUENCE.length + 1];
/*     */   private static final byte ZERO_BYTE = 0;
/*     */ 
/*     */   public BitOutput(OutputStream out)
/*     */   {
/*  40 */     this.mOut = out;
/*  41 */     reset();
/*     */   }
/*     */ 
/*     */   public void writeUnary(int n)
/*     */     throws IOException
/*     */   {
/*  73 */     validatePositive(n);
/*     */ 
/*  76 */     int numZeros = n - 1;
/*  77 */     if (numZeros <= this.mNextBitIndex) {
/*  78 */       this.mNextByte <<= numZeros;
/*  79 */       this.mNextBitIndex -= numZeros;
/*  80 */       writeTrue();
/*  81 */       return;
/*     */     }
/*     */ 
/*  86 */     this.mOut.write(this.mNextByte << this.mNextBitIndex);
/*  87 */     numZeros -= this.mNextBitIndex + 1;
/*  88 */     reset();
/*     */ 
/*  91 */     for (; numZeros >= 8; numZeros -= 8) {
/*  92 */       this.mOut.write(0);
/*     */     }
/*     */ 
/*  96 */     this.mNextBitIndex -= numZeros;
/*  97 */     writeTrue();
/*     */   }
/*     */ 
/*     */   public void writeBinary(long n, int numBits)
/*     */     throws IOException
/*     */   {
/* 161 */     validateNonNegative(n);
/* 162 */     validateNumBits(numBits);
/* 163 */     int k = mostSignificantPowerOfTwo(n);
/* 164 */     if (k >= numBits) {
/* 165 */       String msg = "Number will not fit into number of bits. n=" + n + " numBits=" + numBits;
/*     */ 
/* 168 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 170 */     writeLowOrderBits(numBits, n);
/*     */   }
/*     */ 
/*     */   public void writeRice(long n, int numFixedBits)
/*     */     throws IOException
/*     */   {
/* 298 */     validatePositive(n);
/* 299 */     validateNumBits(numFixedBits);
/* 300 */     long q = n - 1L >> numFixedBits;
/* 301 */     long prefixBits = q + 1L;
/* 302 */     if (prefixBits >= 2147483647L) {
/* 303 */       String msg = "Prefix too long to code. n=" + n + " numFixedBits=" + numFixedBits + " number of prefix bits=(n>>numFixBits)=" + prefixBits;
/*     */ 
/* 307 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 309 */     writeUnary((int)prefixBits);
/* 310 */     long remainder = n - (q << numFixedBits) - 1L;
/* 311 */     writeLowOrderBits(numFixedBits, remainder);
/*     */   }
/*     */ 
/*     */   public void writeFibonacci(long n)
/*     */     throws IOException
/*     */   {
/* 391 */     validatePositive(n);
/* 392 */     long[] fibs = Math.FIBONACCI_SEQUENCE;
/* 393 */     boolean[] buf = FIB_BUF;
/* 394 */     int mostSigPlace = mostSigFibonacci(fibs, n);
/* 395 */     for (int place = mostSigPlace; place >= 0; place--) {
/* 396 */       if (n >= fibs[place]) {
/* 397 */         n -= fibs[place];
/* 398 */         buf[place] = true;
/*     */       } else {
/* 400 */         buf[place] = false;
/*     */       }
/*     */     }
/* 403 */     for (int i = 0; i <= mostSigPlace; i++)
/* 404 */       writeBit(buf[i]);
/* 405 */     writeTrue();
/*     */   }
/*     */ 
/*     */   public void writeGamma(long n)
/*     */     throws IOException
/*     */   {
/* 469 */     validatePositive(n);
/* 470 */     if (n == 1L) {
/* 471 */       writeTrue();
/* 472 */       return;
/*     */     }
/* 474 */     int k = mostSignificantPowerOfTwo(n);
/* 475 */     writeUnary(k + 1);
/* 476 */     writeLowOrderBits(k, n);
/*     */   }
/*     */ 
/*     */   public void writeDelta(long n)
/*     */     throws IOException
/*     */   {
/* 541 */     validatePositive(n);
/* 542 */     int numBits = mostSignificantPowerOfTwo(n);
/* 543 */     if (numBits > 63) {
/* 544 */       throw new IOException("numBits too large=" + numBits);
/*     */     }
/* 546 */     writeGamma(numBits + 1);
/* 547 */     if (numBits > 0)
/* 548 */       writeLowOrderBits(numBits, n);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 564 */     flush();
/* 565 */     this.mOut.close();
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 578 */     if (this.mNextBitIndex < 7) {
/* 579 */       this.mOut.write(this.mNextByte << this.mNextBitIndex);
/* 580 */       reset();
/*     */     }
/* 582 */     this.mOut.flush();
/*     */   }
/*     */ 
/*     */   public void writeBit(boolean bit)
/*     */     throws IOException
/*     */   {
/* 595 */     if (bit) writeTrue(); else
/* 596 */       writeFalse();
/*     */   }
/*     */ 
/*     */   public void writeTrue()
/*     */     throws IOException
/*     */   {
/* 606 */     if (this.mNextBitIndex == 0) {
/* 607 */       this.mOut.write(this.mNextByte | 0x1);
/* 608 */       reset();
/*     */     } else {
/* 610 */       this.mNextByte = ((this.mNextByte | 0x1) << 1);
/* 611 */       this.mNextBitIndex -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeFalse()
/*     */     throws IOException
/*     */   {
/* 622 */     if (this.mNextBitIndex == 0) {
/* 623 */       this.mOut.write(this.mNextByte);
/* 624 */       reset();
/*     */     } else {
/* 626 */       this.mNextByte <<= 1;
/* 627 */       this.mNextBitIndex -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeLowOrderBits(int numBits, long n)
/*     */     throws IOException
/*     */   {
/* 639 */     if (this.mNextBitIndex >= numBits) {
/* 640 */       this.mNextByte = ((this.mNextByte << numBits - 1 | (int)leastSignificantBits2(n, numBits)) << 1);
/*     */ 
/* 644 */       this.mNextBitIndex -= numBits;
/* 645 */       return;
/*     */     }
/*     */ 
/* 649 */     numBits -= this.mNextBitIndex + 1;
/* 650 */     this.mOut.write(this.mNextByte << this.mNextBitIndex | (int)sliceBits2(n, numBits, this.mNextBitIndex + 1));
/*     */ 
/* 654 */     while (numBits >= 8) {
/* 655 */       numBits -= 8;
/* 656 */       this.mOut.write((int)sliceBits2(n, numBits, 8));
/*     */     }
/*     */ 
/* 660 */     if (numBits == 0) {
/* 661 */       reset();
/* 662 */       return;
/*     */     }
/* 664 */     this.mNextByte = ((int)leastSignificantBits2(n, numBits) << 1);
/* 665 */     this.mNextBitIndex = (7 - numBits);
/*     */   }
/*     */ 
/*     */   private void reset() {
/* 669 */     this.mNextByte = 0;
/* 670 */     this.mNextBitIndex = 7;
/*     */   }
/*     */ 
/*     */   public static long leastSignificantBits(long n, int numBits)
/*     */   {
/* 696 */     if ((numBits < 1) || (numBits > 64)) {
/* 697 */       String msg = "Number of bits must be between 1 and 64 inclusive. Found numBits=" + numBits;
/*     */ 
/* 699 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 701 */     return leastSignificantBits2(n, numBits);
/*     */   }
/*     */ 
/*     */   public static long sliceBits(long n, int leastSignificantBit, int numBits)
/*     */   {
/* 726 */     if ((leastSignificantBit < 0) || (leastSignificantBit > 63)) {
/* 727 */       String msg = "Least significant bit must be between 0 and 63. Found leastSignificantBit=" + leastSignificantBit;
/*     */ 
/* 729 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 731 */     if ((numBits < 1) || (numBits > 64)) {
/* 732 */       String msg = "Number of bits must be between 1 and 64 inclusive. Found numBits=" + numBits;
/*     */ 
/* 734 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 736 */     return sliceBits2(n, leastSignificantBit, numBits);
/*     */   }
/*     */ 
/*     */   static long leastSignificantBits2(long n, int numBits) {
/* 740 */     return -1L >>> 64 - numBits & n;
/*     */   }
/*     */ 
/*     */   static long sliceBits2(long n, int leastSignificantBit, int numBits) {
/* 744 */     return leastSignificantBits2(n >>> leastSignificantBit, numBits);
/*     */   }
/*     */ 
/*     */   public static int mostSignificantPowerOfTwo(long n)
/*     */   {
/* 771 */     int sum = n >> 32 != 0L ? 32 : 0;
/* 772 */     if (n >> (sum | 0x10) != 0L) sum |= 16;
/* 773 */     if (n >> (sum | 0x8) != 0L) sum |= 8;
/* 774 */     if (n >> (sum | 0x4) != 0L) sum |= 4;
/* 775 */     if (n >> (sum | 0x2) != 0L) sum |= 2;
/* 776 */     return n >> (sum | 0x1) != 0L ? sum | 0x1 : sum;
/*     */   }
/*     */ 
/*     */   static int mostSigFibonacci(long[] fibs, long n) {
/* 780 */     int low = 0;
/* 781 */     int high = fibs.length - 1;
/* 782 */     while (low <= high) {
/* 783 */       int mid = (low + high) / 2;
/* 784 */       if (fibs[mid] < n)
/* 785 */         low = low == mid ? mid + 1 : mid;
/* 786 */       else if (fibs[mid] > n)
/* 787 */         high = high == mid ? mid - 1 : mid;
/* 788 */       else return mid;
/*     */     }
/* 790 */     return low - 1;
/*     */   }
/*     */ 
/*     */   static void validateNumBits(int numBits) {
/* 794 */     if (numBits > 0) return;
/* 795 */     String msg = "Number of bits must be positive. Found numBits=" + numBits;
/*     */ 
/* 797 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   static void validatePositive(long n) {
/* 801 */     if (n > 0L) return;
/* 802 */     String msg = "Require number greater than zero. Found n=" + n;
/*     */ 
/* 804 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   static void validateNonNegative(long n) {
/* 808 */     if (n >= 0L) return;
/* 809 */     String msg = "Require non-negative number. Found n=" + n;
/*     */ 
/* 811 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.io.BitOutput
 * JD-Core Version:    0.6.2
 */