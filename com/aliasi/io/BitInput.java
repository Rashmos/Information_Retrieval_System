/*     */ package com.aliasi.io;
/*     */ 
/*     */ import com.aliasi.util.Math;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public class BitInput
/*     */ {
/*     */   private final InputStream mIn;
/*     */   private int mNextByte;
/*     */   private int mNextBitIndex;
/*  26 */   private boolean mEndOfStream = false;
/*     */   static final byte ZERO_BYTE = 0;
/* 496 */   static int ALL_ONES_INT = -1;
/*     */ 
/*     */   public BitInput(InputStream in)
/*     */     throws IOException
/*     */   {
/*  39 */     this.mIn = in;
/*  40 */     readAhead();
/*     */   }
/*     */ 
/*     */   public long available()
/*     */     throws IOException
/*     */   {
/*  54 */     return this.mEndOfStream ? 0L : this.mNextBitIndex + 1L + 8L * this.mIn.available();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  70 */     this.mEndOfStream = true;
/*  71 */     this.mIn.close();
/*     */   }
/*     */ 
/*     */   public boolean endOfStream()
/*     */   {
/*  82 */     return this.mEndOfStream;
/*     */   }
/*     */ 
/*     */   public long skip(long numBits)
/*     */     throws IOException
/*     */   {
/* 101 */     if (numBits < 0L) {
/* 102 */       String msg = "Require positive number of bits to skip. Found numBits=" + numBits;
/*     */ 
/* 104 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 106 */     if (this.mNextBitIndex >= numBits) {
/* 107 */       this.mNextBitIndex = ((int)(this.mNextBitIndex - numBits));
/* 108 */       return numBits;
/*     */     }
/*     */ 
/* 111 */     long numBitsSkipped = this.mNextBitIndex + 1;
/* 112 */     long numBitsLeft = numBits - numBitsSkipped;
/*     */ 
/* 114 */     long bytesToSkip = numBitsLeft / 8L;
/* 115 */     long bytesSkipped = this.mIn.skip(bytesToSkip);
/*     */ 
/* 117 */     numBitsSkipped += 8L * bytesSkipped;
/* 118 */     if (bytesSkipped < bytesToSkip) {
/* 119 */       this.mEndOfStream = true;
/* 120 */       return numBitsSkipped;
/*     */     }
/*     */ 
/* 123 */     readAhead();
/* 124 */     if (this.mEndOfStream)
/* 125 */       return numBitsSkipped;
/* 126 */     this.mNextBitIndex = (7 - (int)numBitsLeft % 8);
/* 127 */     return numBits;
/*     */   }
/*     */ 
/*     */   public boolean readBit()
/*     */     throws IOException
/*     */   {
/* 143 */     switch (this.mNextBitIndex--) {
/*     */     case 0:
/* 145 */       boolean result = (this.mNextByte & 0x1) != 0;
/* 146 */       readAhead();
/* 147 */       return result;
/*     */     case 1:
/* 149 */       return (this.mNextByte & 0x2) != 0;
/*     */     case 2:
/* 151 */       return (this.mNextByte & 0x4) != 0;
/*     */     case 3:
/* 153 */       return (this.mNextByte & 0x8) != 0;
/*     */     case 4:
/* 155 */       return (this.mNextByte & 0x10) != 0;
/*     */     case 5:
/* 157 */       return (this.mNextByte & 0x20) != 0;
/*     */     case 6:
/* 159 */       return (this.mNextByte & 0x40) != 0;
/*     */     case 7:
/* 161 */       return (this.mNextByte & 0x80) != 0;
/*     */     }
/* 163 */     String msg = "Index out of bounds. mNextBitIndex=" + this.mNextBitIndex;
/*     */ 
/* 165 */     throw new IOException(msg);
/*     */   }
/*     */ 
/*     */   public int readUnary()
/*     */     throws IOException
/*     */   {
/* 182 */     for (int result = 1; 
/* 185 */       (!endOfStream()) && (this.mNextBitIndex != 7); result++) {
/* 186 */       if (readBit()) {
/* 187 */         return result;
/*     */       }
/*     */     }
/* 190 */     while ((!endOfStream()) && (this.mNextByte == 0)) {
/* 191 */       result += 8;
/* 192 */       this.mNextByte = this.mIn.read();
/* 193 */       if (this.mNextByte == -1) {
/* 194 */         String msg = "Final sequence of 0 bits with no 1";
/* 195 */         throw new IOException(msg);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 200 */     while (!readBit())
/* 201 */       result++;
/* 202 */     return result;
/*     */   }
/*     */ 
/*     */   public void skipUnary()
/*     */     throws IOException
/*     */   {
/* 215 */     while ((!endOfStream()) && (this.mNextBitIndex != 7)) {
/* 216 */       if (readBit()) {
/* 217 */         return;
/*     */       }
/*     */     }
/* 220 */     while ((!endOfStream()) && (this.mNextByte == 0)) {
/* 221 */       this.mNextByte = this.mIn.read();
/* 222 */       if (this.mNextByte == -1) {
/* 223 */         String msg = "Final sequence of 0 bits with no 1";
/* 224 */         throw new IOException(msg);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 229 */     while (!readBit());
/*     */   }
/*     */ 
/*     */   public long readGamma()
/*     */     throws IOException
/*     */   {
/* 248 */     int numBits = readUnary();
/* 249 */     if (numBits > 63) {
/* 250 */       String msg = "Gamma code binary part must be <= 63 bits. Found numBits=" + numBits;
/*     */ 
/* 252 */       throw new IOException(msg);
/*     */     }
/* 254 */     return readRest(numBits - 1, 1L);
/*     */   }
/*     */ 
/*     */   public void skipGamma()
/*     */     throws IOException
/*     */   {
/* 265 */     int numBits = readUnary();
/* 266 */     checkGamma(numBits);
/* 267 */     skip(numBits - 1);
/*     */   }
/*     */ 
/*     */   public long readDelta()
/*     */     throws IOException
/*     */   {
/* 285 */     long numBits = readGamma();
/* 286 */     checkDelta(numBits);
/* 287 */     if (numBits > 63L) {
/* 288 */       String msg = "Delta code must use <= 63 bits for fixed portion. Found number of remaining bits=" + numBits;
/*     */ 
/* 290 */       throw new IOException(msg);
/*     */     }
/* 292 */     return readRest((int)numBits - 1, 1L);
/*     */   }
/*     */ 
/*     */   public void skipDelta()
/*     */     throws IOException
/*     */   {
/* 303 */     long numBits = readGamma();
/* 304 */     checkDelta(numBits);
/* 305 */     skip(numBits - 1L);
/*     */   }
/*     */ 
/*     */   public long readBinary(int numBits)
/*     */     throws IOException
/*     */   {
/* 332 */     if (numBits > 63) {
/* 333 */       String msg = "Cannot read more than 63 bits into positive long. Found numBits=" + numBits;
/*     */ 
/* 335 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 337 */     if (numBits < 1) {
/* 338 */       String msg = "Number of bits to read must be > 0. Found numBits=" + numBits;
/*     */ 
/* 340 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 342 */     long result = readBit() ? 1L : 0L;
/* 343 */     return readRest(numBits - 1, result);
/*     */   }
/*     */ 
/*     */   public long readRice(int numFixedBits)
/*     */     throws IOException
/*     */   {
/* 360 */     if (numFixedBits < 1) {
/* 361 */       String msg = "Rice coding requires a number of fixed bits > 0. Found numFixedBits=" + numFixedBits;
/*     */ 
/* 363 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 365 */     if (numFixedBits > 63) {
/* 366 */       String msg = "Rice coding requires a number of fixed bits < 64.Found numFixedBits=" + numFixedBits;
/*     */ 
/* 368 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 370 */     long prefixBits = readUnary();
/* 371 */     long remainder = readBinary(numFixedBits);
/* 372 */     long q = prefixBits - 1L;
/* 373 */     long div = q << numFixedBits;
/* 374 */     return div + (remainder + 1L);
/*     */   }
/*     */ 
/*     */   public void skipRice(int numFixedBits)
/*     */     throws IOException
/*     */   {
/* 385 */     skipUnary();
/* 386 */     skip(numFixedBits);
/*     */   }
/*     */ 
/*     */   public long readFibonacci()
/*     */     throws IOException
/*     */   {
/* 403 */     long[] fibs = Math.FIBONACCI_SEQUENCE;
/* 404 */     long sum = 0L;
/* 405 */     for (int i = 0; (i < fibs.length) && (!endOfStream()); i++) {
/* 406 */       if (readBit()) {
/* 407 */         sum += fibs[(i++)];
/* 408 */         if ((!endOfStream()) && (readBit())) return sum;
/*     */       }
/*     */     }
/* 411 */     String msg = "Ran off end of input or beyond maximum length  without finding two consecutive 1s";
/*     */ 
/* 413 */     throw new IOException(msg);
/*     */   }
/*     */ 
/*     */   public void skipFibonacci()
/*     */     throws IOException
/*     */   {
/* 424 */     while (!endOfStream())
/* 425 */       if ((readBit()) && (!endOfStream()) && (readBit()))
/* 426 */         return;
/* 427 */     String msg = "Ran off end of input without finding two consecutive 1s";
/* 428 */     throw new IOException(msg);
/*     */   }
/*     */ 
/*     */   long readRest(int numBits, long result)
/*     */     throws IOException
/*     */   {
/* 441 */     if (numBits == 0) return result;
/*     */ 
/* 443 */     notEndOfStream();
/*     */ 
/* 446 */     if (this.mNextBitIndex >= numBits) {
/* 447 */       this.mNextBitIndex -= numBits;
/* 448 */       return result << numBits | sliceBits2(this.mNextByte, this.mNextBitIndex + 1, numBits + 1);
/*     */     }
/*     */ 
/* 454 */     numBits -= this.mNextBitIndex + 1;
/* 455 */     result = result << this.mNextBitIndex + 1 | sliceBits2(this.mNextByte, 0, this.mNextBitIndex + 1);
/*     */ 
/* 458 */     for (; numBits >= 8; numBits -= 8) {
/* 459 */       int nextByte = this.mIn.read();
/* 460 */       if (nextByte == -1) {
/* 461 */         this.mEndOfStream = true;
/* 462 */         String msg = "Premature end of stream reading binary - mid.";
/* 463 */         throw new IOException(msg);
/*     */       }
/* 465 */       result = result << 8 | nextByte;
/*     */     }
/* 467 */     readAhead();
/*     */ 
/* 469 */     if (numBits == 0) return result;
/* 470 */     notEndOfStream();
/* 471 */     this.mNextBitIndex = (7 - numBits);
/* 472 */     return result << numBits | sliceBits2(this.mNextByte, this.mNextBitIndex + 1, numBits);
/*     */   }
/*     */ 
/*     */   private void readAhead() throws IOException
/*     */   {
/* 477 */     if (this.mEndOfStream) return;
/* 478 */     this.mNextByte = this.mIn.read();
/* 479 */     if (this.mNextByte == -1) {
/* 480 */       this.mEndOfStream = true;
/* 481 */       return;
/*     */     }
/* 483 */     this.mNextBitIndex = 7;
/*     */   }
/*     */ 
/*     */   private void notEndOfStream() throws IOException
/*     */   {
/* 488 */     if (endOfStream()) {
/* 489 */       String msg = "End of stream reached prematurely.";
/* 490 */       throw new IOException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static long leastSignificantBits2(int n, int numBits)
/*     */   {
/* 499 */     return ALL_ONES_INT >>> 32 - numBits & n;
/*     */   }
/*     */ 
/*     */   static long sliceBits2(int n, int leastSignificantBit, int numBits) {
/* 503 */     return leastSignificantBits2(n >>> leastSignificantBit, numBits);
/*     */   }
/*     */ 
/*     */   static void checkGamma(int numBits) throws IOException
/*     */   {
/* 508 */     if (numBits <= 63) return;
/* 509 */     String msg = "Gamma code binary part must be <= 63 bits. Found numBits=" + numBits;
/*     */ 
/* 511 */     throw new IOException(msg);
/*     */   }
/*     */ 
/*     */   static void checkDelta(long numBits) throws IOException {
/* 515 */     if (numBits <= 63L) return;
/* 516 */     String msg = "Delta code binary part must be <= 63 bits. Number of bits specified=" + numBits;
/*     */ 
/* 518 */     throw new IOException(msg);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.io.BitInput
 * JD-Core Version:    0.6.2
 */