/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import org.apache.lucene.search.DocIdSet;
/*     */ import org.apache.lucene.search.DocIdSetIterator;
/*     */ 
/*     */ public class OpenBitSet extends DocIdSet
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   protected long[] bits;
/*     */   protected int wlen;
/*     */ 
/*     */   public OpenBitSet(long numBits)
/*     */   {
/*  89 */     this.bits = new long[bits2words(numBits)];
/*  90 */     this.wlen = this.bits.length;
/*     */   }
/*     */ 
/*     */   public OpenBitSet() {
/*  94 */     this(64L);
/*     */   }
/*     */ 
/*     */   public OpenBitSet(long[] bits, int numWords)
/*     */   {
/* 111 */     this.bits = bits;
/* 112 */     this.wlen = numWords;
/*     */   }
/*     */ 
/*     */   public DocIdSetIterator iterator()
/*     */   {
/* 117 */     return new OpenBitSetIterator(this.bits, this.wlen);
/*     */   }
/*     */ 
/*     */   public boolean isCacheable()
/*     */   {
/* 123 */     return true;
/*     */   }
/*     */ 
/*     */   public long capacity() {
/* 127 */     return this.bits.length << 6;
/*     */   }
/*     */ 
/*     */   public long size()
/*     */   {
/* 134 */     return capacity();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 138 */     return cardinality() == 0L;
/*     */   }
/*     */   public long[] getBits() {
/* 141 */     return this.bits;
/*     */   }
/*     */   public void setBits(long[] bits) {
/* 144 */     this.bits = bits;
/*     */   }
/*     */   public int getNumWords() {
/* 147 */     return this.wlen;
/*     */   }
/*     */   public void setNumWords(int nWords) {
/* 150 */     this.wlen = nWords;
/*     */   }
/*     */ 
/*     */   public boolean get(int index)
/*     */   {
/* 156 */     int i = index >> 6;
/*     */ 
/* 159 */     if (i >= this.bits.length) return false;
/*     */ 
/* 161 */     int bit = index & 0x3F;
/* 162 */     long bitmask = 1L << bit;
/* 163 */     return (this.bits[i] & bitmask) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean fastGet(int index)
/*     */   {
/* 171 */     int i = index >> 6;
/*     */ 
/* 174 */     int bit = index & 0x3F;
/* 175 */     long bitmask = 1L << bit;
/* 176 */     return (this.bits[i] & bitmask) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean get(long index)
/*     */   {
/* 184 */     int i = (int)(index >> 6);
/* 185 */     if (i >= this.bits.length) return false;
/* 186 */     int bit = (int)index & 0x3F;
/* 187 */     long bitmask = 1L << bit;
/* 188 */     return (this.bits[i] & bitmask) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean fastGet(long index)
/*     */   {
/* 195 */     int i = (int)(index >> 6);
/* 196 */     int bit = (int)index & 0x3F;
/* 197 */     long bitmask = 1L << bit;
/* 198 */     return (this.bits[i] & bitmask) != 0L;
/*     */   }
/*     */ 
/*     */   public int getBit(int index)
/*     */   {
/* 218 */     int i = index >> 6;
/* 219 */     int bit = index & 0x3F;
/* 220 */     return (int)(this.bits[i] >>> bit) & 0x1;
/*     */   }
/*     */ 
/*     */   public void set(long index)
/*     */   {
/* 235 */     int wordNum = expandingWordNum(index);
/* 236 */     int bit = (int)index & 0x3F;
/* 237 */     long bitmask = 1L << bit;
/* 238 */     this.bits[wordNum] |= bitmask;
/*     */   }
/*     */ 
/*     */   public void fastSet(int index)
/*     */   {
/* 246 */     int wordNum = index >> 6;
/* 247 */     int bit = index & 0x3F;
/* 248 */     long bitmask = 1L << bit;
/* 249 */     this.bits[wordNum] |= bitmask;
/*     */   }
/*     */ 
/*     */   public void fastSet(long index)
/*     */   {
/* 256 */     int wordNum = (int)(index >> 6);
/* 257 */     int bit = (int)index & 0x3F;
/* 258 */     long bitmask = 1L << bit;
/* 259 */     this.bits[wordNum] |= bitmask;
/*     */   }
/*     */ 
/*     */   public void set(long startIndex, long endIndex)
/*     */   {
/* 268 */     if (endIndex <= startIndex) return;
/*     */ 
/* 270 */     int startWord = (int)(startIndex >> 6);
/*     */ 
/* 274 */     int endWord = expandingWordNum(endIndex - 1L);
/*     */ 
/* 276 */     long startmask = -1L << (int)startIndex;
/* 277 */     long endmask = -1L >>> (int)-endIndex;
/*     */ 
/* 279 */     if (startWord == endWord) {
/* 280 */       this.bits[startWord] |= startmask & endmask;
/* 281 */       return;
/*     */     }
/*     */ 
/* 284 */     this.bits[startWord] |= startmask;
/* 285 */     Arrays.fill(this.bits, startWord + 1, endWord, -1L);
/* 286 */     this.bits[endWord] |= endmask;
/*     */   }
/*     */ 
/*     */   protected int expandingWordNum(long index)
/*     */   {
/* 292 */     int wordNum = (int)(index >> 6);
/* 293 */     if (wordNum >= this.wlen) {
/* 294 */       ensureCapacity(index + 1L);
/* 295 */       this.wlen = (wordNum + 1);
/*     */     }
/* 297 */     return wordNum;
/*     */   }
/*     */ 
/*     */   public void fastClear(int index)
/*     */   {
/* 305 */     int wordNum = index >> 6;
/* 306 */     int bit = index & 0x3F;
/* 307 */     long bitmask = 1L << bit;
/* 308 */     this.bits[wordNum] &= (bitmask ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void fastClear(long index)
/*     */   {
/* 322 */     int wordNum = (int)(index >> 6);
/* 323 */     int bit = (int)index & 0x3F;
/* 324 */     long bitmask = 1L << bit;
/* 325 */     this.bits[wordNum] &= (bitmask ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void clear(long index)
/*     */   {
/* 330 */     int wordNum = (int)(index >> 6);
/* 331 */     if (wordNum >= this.wlen) return;
/* 332 */     int bit = (int)index & 0x3F;
/* 333 */     long bitmask = 1L << bit;
/* 334 */     this.bits[wordNum] &= (bitmask ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void clear(int startIndex, int endIndex)
/*     */   {
/* 343 */     if (endIndex <= startIndex) return;
/*     */ 
/* 345 */     int startWord = startIndex >> 6;
/* 346 */     if (startWord >= this.wlen) return;
/*     */ 
/* 350 */     int endWord = endIndex - 1 >> 6;
/*     */ 
/* 352 */     long startmask = -1L << startIndex;
/* 353 */     long endmask = -1L >>> -endIndex;
/*     */ 
/* 356 */     startmask ^= -1L;
/* 357 */     endmask ^= -1L;
/*     */ 
/* 359 */     if (startWord == endWord) {
/* 360 */       this.bits[startWord] &= (startmask | endmask);
/* 361 */       return;
/*     */     }
/*     */ 
/* 364 */     this.bits[startWord] &= startmask;
/*     */ 
/* 366 */     int middle = Math.min(this.wlen, endWord);
/* 367 */     Arrays.fill(this.bits, startWord + 1, middle, 0L);
/* 368 */     if (endWord < this.wlen)
/* 369 */       this.bits[endWord] &= endmask;
/*     */   }
/*     */ 
/*     */   public void clear(long startIndex, long endIndex)
/*     */   {
/* 380 */     if (endIndex <= startIndex) return;
/*     */ 
/* 382 */     int startWord = (int)(startIndex >> 6);
/* 383 */     if (startWord >= this.wlen) return;
/*     */ 
/* 387 */     int endWord = (int)(endIndex - 1L >> 6);
/*     */ 
/* 389 */     long startmask = -1L << (int)startIndex;
/* 390 */     long endmask = -1L >>> (int)-endIndex;
/*     */ 
/* 393 */     startmask ^= -1L;
/* 394 */     endmask ^= -1L;
/*     */ 
/* 396 */     if (startWord == endWord) {
/* 397 */       this.bits[startWord] &= (startmask | endmask);
/* 398 */       return;
/*     */     }
/*     */ 
/* 401 */     this.bits[startWord] &= startmask;
/*     */ 
/* 403 */     int middle = Math.min(this.wlen, endWord);
/* 404 */     Arrays.fill(this.bits, startWord + 1, middle, 0L);
/* 405 */     if (endWord < this.wlen)
/* 406 */       this.bits[endWord] &= endmask;
/*     */   }
/*     */ 
/*     */   public boolean getAndSet(int index)
/*     */   {
/* 416 */     int wordNum = index >> 6;
/* 417 */     int bit = index & 0x3F;
/* 418 */     long bitmask = 1L << bit;
/* 419 */     boolean val = (this.bits[wordNum] & bitmask) != 0L;
/* 420 */     this.bits[wordNum] |= bitmask;
/* 421 */     return val;
/*     */   }
/*     */ 
/*     */   public boolean getAndSet(long index)
/*     */   {
/* 428 */     int wordNum = (int)(index >> 6);
/* 429 */     int bit = (int)index & 0x3F;
/* 430 */     long bitmask = 1L << bit;
/* 431 */     boolean val = (this.bits[wordNum] & bitmask) != 0L;
/* 432 */     this.bits[wordNum] |= bitmask;
/* 433 */     return val;
/*     */   }
/*     */ 
/*     */   public void fastFlip(int index)
/*     */   {
/* 440 */     int wordNum = index >> 6;
/* 441 */     int bit = index & 0x3F;
/* 442 */     long bitmask = 1L << bit;
/* 443 */     this.bits[wordNum] ^= bitmask;
/*     */   }
/*     */ 
/*     */   public void fastFlip(long index)
/*     */   {
/* 450 */     int wordNum = (int)(index >> 6);
/* 451 */     int bit = (int)index & 0x3F;
/* 452 */     long bitmask = 1L << bit;
/* 453 */     this.bits[wordNum] ^= bitmask;
/*     */   }
/*     */ 
/*     */   public void flip(long index)
/*     */   {
/* 458 */     int wordNum = expandingWordNum(index);
/* 459 */     int bit = (int)index & 0x3F;
/* 460 */     long bitmask = 1L << bit;
/* 461 */     this.bits[wordNum] ^= bitmask;
/*     */   }
/*     */ 
/*     */   public boolean flipAndGet(int index)
/*     */   {
/* 468 */     int wordNum = index >> 6;
/* 469 */     int bit = index & 0x3F;
/* 470 */     long bitmask = 1L << bit;
/* 471 */     this.bits[wordNum] ^= bitmask;
/* 472 */     return (this.bits[wordNum] & bitmask) != 0L;
/*     */   }
/*     */ 
/*     */   public boolean flipAndGet(long index)
/*     */   {
/* 479 */     int wordNum = (int)(index >> 6);
/* 480 */     int bit = (int)index & 0x3F;
/* 481 */     long bitmask = 1L << bit;
/* 482 */     this.bits[wordNum] ^= bitmask;
/* 483 */     return (this.bits[wordNum] & bitmask) != 0L;
/*     */   }
/*     */ 
/*     */   public void flip(long startIndex, long endIndex)
/*     */   {
/* 492 */     if (endIndex <= startIndex) return;
/* 493 */     int startWord = (int)(startIndex >> 6);
/*     */ 
/* 497 */     int endWord = expandingWordNum(endIndex - 1L);
/*     */ 
/* 506 */     long startmask = -1L << (int)startIndex;
/* 507 */     long endmask = -1L >>> (int)-endIndex;
/*     */ 
/* 509 */     if (startWord == endWord) {
/* 510 */       this.bits[startWord] ^= startmask & endmask;
/* 511 */       return;
/*     */     }
/*     */ 
/* 514 */     this.bits[startWord] ^= startmask;
/*     */ 
/* 516 */     for (int i = startWord + 1; i < endWord; i++) {
/* 517 */       this.bits[i] ^= -1L;
/*     */     }
/*     */ 
/* 520 */     this.bits[endWord] ^= endmask;
/*     */   }
/*     */ 
/*     */   public long cardinality()
/*     */   {
/* 549 */     return BitUtil.pop_array(this.bits, 0, this.wlen);
/*     */   }
/*     */ 
/*     */   public static long intersectionCount(OpenBitSet a, OpenBitSet b)
/*     */   {
/* 556 */     return BitUtil.pop_intersect(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
/*     */   }
/*     */ 
/*     */   public static long unionCount(OpenBitSet a, OpenBitSet b)
/*     */   {
/* 563 */     long tot = BitUtil.pop_union(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
/* 564 */     if (a.wlen < b.wlen)
/* 565 */       tot += BitUtil.pop_array(b.bits, a.wlen, b.wlen - a.wlen);
/* 566 */     else if (a.wlen > b.wlen) {
/* 567 */       tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen - b.wlen);
/*     */     }
/* 569 */     return tot;
/*     */   }
/*     */ 
/*     */   public static long andNotCount(OpenBitSet a, OpenBitSet b)
/*     */   {
/* 577 */     long tot = BitUtil.pop_andnot(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
/* 578 */     if (a.wlen > b.wlen) {
/* 579 */       tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen - b.wlen);
/*     */     }
/* 581 */     return tot;
/*     */   }
/*     */ 
/*     */   public static long xorCount(OpenBitSet a, OpenBitSet b)
/*     */   {
/* 588 */     long tot = BitUtil.pop_xor(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
/* 589 */     if (a.wlen < b.wlen)
/* 590 */       tot += BitUtil.pop_array(b.bits, a.wlen, b.wlen - a.wlen);
/* 591 */     else if (a.wlen > b.wlen) {
/* 592 */       tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen - b.wlen);
/*     */     }
/* 594 */     return tot;
/*     */   }
/*     */ 
/*     */   public int nextSetBit(int index)
/*     */   {
/* 602 */     int i = index >> 6;
/* 603 */     if (i >= this.wlen) return -1;
/* 604 */     int subIndex = index & 0x3F;
/* 605 */     long word = this.bits[i] >> subIndex;
/*     */ 
/* 607 */     if (word != 0L)
/* 608 */       return (i << 6) + subIndex + BitUtil.ntz(word);
/*     */     do
/*     */     {
/* 611 */       i++; if (i >= this.wlen) break;
/* 612 */       word = this.bits[i];
/* 613 */     }while (word == 0L); return (i << 6) + BitUtil.ntz(word);
/*     */ 
/* 616 */     return -1;
/*     */   }
/*     */ 
/*     */   public long nextSetBit(long index)
/*     */   {
/* 623 */     int i = (int)(index >>> 6);
/* 624 */     if (i >= this.wlen) return -1L;
/* 625 */     int subIndex = (int)index & 0x3F;
/* 626 */     long word = this.bits[i] >>> subIndex;
/*     */ 
/* 628 */     if (word != 0L)
/* 629 */       return (i << 6) + (subIndex + BitUtil.ntz(word));
/*     */     do
/*     */     {
/* 632 */       i++; if (i >= this.wlen) break;
/* 633 */       word = this.bits[i];
/* 634 */     }while (word == 0L); return (i << 6) + BitUtil.ntz(word);
/*     */ 
/* 637 */     return -1L;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 646 */       OpenBitSet obs = (OpenBitSet)super.clone();
/* 647 */       obs.bits = ((long[])obs.bits.clone());
/* 648 */       return obs;
/*     */     } catch (CloneNotSupportedException e) {
/* 650 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void intersect(OpenBitSet other)
/*     */   {
/* 656 */     int newLen = Math.min(this.wlen, other.wlen);
/* 657 */     long[] thisArr = this.bits;
/* 658 */     long[] otherArr = other.bits;
/*     */ 
/* 660 */     int pos = newLen;
/*     */     while (true) { pos--; if (pos < 0) break;
/* 662 */       thisArr[pos] &= otherArr[pos];
/*     */     }
/* 664 */     if (this.wlen > newLen)
/*     */     {
/* 666 */       Arrays.fill(this.bits, newLen, this.wlen, 0L);
/*     */     }
/* 668 */     this.wlen = newLen;
/*     */   }
/*     */ 
/*     */   public void union(OpenBitSet other)
/*     */   {
/* 673 */     int newLen = Math.max(this.wlen, other.wlen);
/* 674 */     ensureCapacityWords(newLen);
/*     */ 
/* 676 */     long[] thisArr = this.bits;
/* 677 */     long[] otherArr = other.bits;
/* 678 */     int pos = Math.min(this.wlen, other.wlen);
/*     */     while (true) { pos--; if (pos < 0) break;
/* 680 */       thisArr[pos] |= otherArr[pos];
/*     */     }
/* 682 */     if (this.wlen < newLen) {
/* 683 */       System.arraycopy(otherArr, this.wlen, thisArr, this.wlen, newLen - this.wlen);
/*     */     }
/* 685 */     this.wlen = newLen;
/*     */   }
/*     */ 
/*     */   public void remove(OpenBitSet other)
/*     */   {
/* 691 */     int idx = Math.min(this.wlen, other.wlen);
/* 692 */     long[] thisArr = this.bits;
/* 693 */     long[] otherArr = other.bits;
/*     */     while (true) { idx--; if (idx < 0) break;
/* 695 */       thisArr[idx] &= (otherArr[idx] ^ 0xFFFFFFFF);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void xor(OpenBitSet other)
/*     */   {
/* 701 */     int newLen = Math.max(this.wlen, other.wlen);
/* 702 */     ensureCapacityWords(newLen);
/*     */ 
/* 704 */     long[] thisArr = this.bits;
/* 705 */     long[] otherArr = other.bits;
/* 706 */     int pos = Math.min(this.wlen, other.wlen);
/*     */     while (true) { pos--; if (pos < 0) break;
/* 708 */       thisArr[pos] ^= otherArr[pos];
/*     */     }
/* 710 */     if (this.wlen < newLen) {
/* 711 */       System.arraycopy(otherArr, this.wlen, thisArr, this.wlen, newLen - this.wlen);
/*     */     }
/* 713 */     this.wlen = newLen;
/*     */   }
/*     */ 
/*     */   public void and(OpenBitSet other)
/*     */   {
/* 721 */     intersect(other);
/*     */   }
/*     */ 
/*     */   public void or(OpenBitSet other)
/*     */   {
/* 726 */     union(other);
/*     */   }
/*     */ 
/*     */   public void andNot(OpenBitSet other)
/*     */   {
/* 731 */     remove(other);
/*     */   }
/*     */ 
/*     */   public boolean intersects(OpenBitSet other)
/*     */   {
/* 736 */     int pos = Math.min(this.wlen, other.wlen);
/* 737 */     long[] thisArr = this.bits;
/* 738 */     long[] otherArr = other.bits;
/*     */     do { pos--; if (pos < 0) break; }
/* 740 */     while ((thisArr[pos] & otherArr[pos]) == 0L); return true;
/*     */ 
/* 742 */     return false;
/*     */   }
/*     */ 
/*     */   public void ensureCapacityWords(int numWords)
/*     */   {
/* 751 */     if (this.bits.length < numWords)
/* 752 */       this.bits = ArrayUtil.grow(this.bits, numWords);
/*     */   }
/*     */ 
/*     */   public void ensureCapacity(long numBits)
/*     */   {
/* 760 */     ensureCapacityWords(bits2words(numBits));
/*     */   }
/*     */ 
/*     */   public void trimTrailingZeros()
/*     */   {
/* 767 */     int idx = this.wlen - 1;
/* 768 */     while ((idx >= 0) && (this.bits[idx] == 0L)) idx--;
/* 769 */     this.wlen = (idx + 1);
/*     */   }
/*     */ 
/*     */   public static int bits2words(long numBits)
/*     */   {
/* 774 */     return (int)((numBits - 1L >>> 6) + 1L);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 781 */     if (this == o) return true;
/* 782 */     if (!(o instanceof OpenBitSet)) return false;
/*     */ 
/* 784 */     OpenBitSet b = (OpenBitSet)o;
/*     */     OpenBitSet a;
/* 786 */     if (b.wlen > this.wlen) {
/* 787 */       OpenBitSet a = b; b = this;
/*     */     } else {
/* 789 */       a = this;
/*     */     }
/*     */ 
/* 793 */     for (int i = a.wlen - 1; i >= b.wlen; i--) {
/* 794 */       if (a.bits[i] != 0L) return false;
/*     */     }
/*     */ 
/* 797 */     for (int i = b.wlen - 1; i >= 0; i--) {
/* 798 */       if (a.bits[i] != b.bits[i]) return false;
/*     */     }
/*     */ 
/* 801 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 807 */     long h = -1737092556L;
/* 808 */     int i = this.bits.length;
/*     */     while (true) { i--; if (i < 0) break;
/* 809 */       h ^= this.bits[i];
/* 810 */       h = h << 1 | h >>> 63;
/*     */     }
/* 812 */     return (int)(h >> 32 ^ h);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.OpenBitSet
 * JD-Core Version:    0.6.2
 */