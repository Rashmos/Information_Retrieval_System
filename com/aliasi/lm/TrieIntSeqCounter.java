/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ 
/*     */ public class TrieIntSeqCounter
/*     */   implements IntSeqCounter
/*     */ {
/*     */   private final int mMaxLength;
/*     */   final IntNode mRootNode;
/*     */ 
/*     */   public TrieIntSeqCounter(int maxLength)
/*     */   {
/*  52 */     if (maxLength < 0) {
/*  53 */       String msg = "Max length must be >= 0. Found maxLength=" + maxLength;
/*     */ 
/*  55 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  57 */     this.mMaxLength = maxLength;
/*  58 */     this.mRootNode = new IntNode();
/*     */   }
/*     */ 
/*     */   public void prune(int minCount)
/*     */   {
/*  70 */     this.mRootNode.prune(minCount);
/*     */   }
/*     */ 
/*     */   public void rescale(double countMultiplier)
/*     */   {
/*  98 */     this.mRootNode.rescale(countMultiplier);
/*     */   }
/*     */ 
/*     */   public int maxLength()
/*     */   {
/* 109 */     return this.mMaxLength;
/*     */   }
/*     */ 
/*     */   public void incrementSubsequences(int[] is, int start, int end)
/*     */   {
/* 129 */     checkBoundaries(is, start, end);
/* 130 */     for (int i = start; i < end; i++)
/* 131 */       this.mRootNode.increment(is, i, Math.min(i + maxLength(), end));
/*     */   }
/*     */ 
/*     */   public void incrementSubsequences(int[] is, int start, int end, int count)
/*     */   {
/* 154 */     checkBoundaries(is, start, end);
/* 155 */     checkCount(count);
/* 156 */     if (count == 0) return;
/* 157 */     for (int i = start; i < end; i++)
/* 158 */       this.mRootNode.increment(is, i, Math.min(i + maxLength(), end), count);
/*     */   }
/*     */ 
/*     */   static void checkCount(int count) {
/* 162 */     if (count >= 0) return;
/* 163 */     String msg = "Counts must be non-negative. Found count=" + count;
/*     */ 
/* 165 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   public void incrementSequence(int[] is, int start, int end, int count)
/*     */   {
/* 193 */     checkBoundaries(is, start, end);
/* 194 */     checkCount(count);
/* 195 */     if (count == 0) return;
/* 196 */     this.mRootNode.incrementSequence(is, Math.max(start, end - maxLength()), end, count);
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap<int[]> nGramCounts(int nGram, int minCount)
/*     */   {
/* 221 */     if (nGram < 1) {
/* 222 */       String msg = "Ngrams must be positive. Found n-gram=" + nGram;
/*     */ 
/* 224 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 226 */     ObjectToCounterMap result = new ObjectToCounterMap();
/*     */ 
/* 228 */     int[] nGramBuffer = new int[nGram];
/* 229 */     addNGramCounts(minCount, 0, nGram, nGramBuffer, result);
/* 230 */     return result;
/*     */   }
/*     */ 
/*     */   public int trieSize()
/*     */   {
/* 241 */     return this.mRootNode.trieSize();
/*     */   }
/*     */ 
/*     */   public void handleNGrams(int nGram, int minCount, ObjectHandler<int[]> handler)
/*     */   {
/* 255 */     if (nGram < 1) {
/* 256 */       String msg = "Ngrams must be positive. Found n-gram=" + nGram;
/*     */ 
/* 258 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 260 */     int[] nGramBuffer = new int[nGram];
/* 261 */     handleNGrams(minCount, 0, nGram, nGramBuffer, handler);
/*     */   }
/*     */ 
/*     */   public int count(int[] is, int start, int end) {
/* 265 */     checkBoundaries(is, start, end);
/* 266 */     IntNode dtr = this.mRootNode.getDtr(is, start, end);
/* 267 */     return dtr == null ? 0 : dtr.count();
/*     */   }
/*     */ 
/*     */   public long extensionCount(int[] is, int start, int end) {
/* 271 */     checkBoundaries(is, start, end);
/* 272 */     IntNode dtr = this.mRootNode.getDtr(is, start, end);
/* 273 */     return dtr == null ? 0L : dtr.extensionCount();
/*     */   }
/*     */ 
/*     */   public int numExtensions(int[] is, int start, int end) {
/* 277 */     checkBoundaries(is, start, end);
/* 278 */     IntNode dtr = this.mRootNode.getDtr(is, start, end);
/* 279 */     return dtr == null ? 0 : dtr.numExtensions();
/*     */   }
/*     */ 
/*     */   public int[] observedIntegers() {
/* 283 */     return this.mRootNode.observedIntegers();
/*     */   }
/*     */ 
/*     */   public int[] integersFollowing(int[] is, int start, int end) {
/* 287 */     return this.mRootNode.integersFollowing(is, start, end);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 299 */     return this.mRootNode.toString(null);
/*     */   }
/*     */ 
/*     */   void decrementUnigram(int symbol) {
/* 303 */     this.mRootNode.decrement(symbol);
/*     */   }
/*     */ 
/*     */   void decrementUnigram(int symbol, int count) {
/* 307 */     this.mRootNode.decrement(symbol, count);
/*     */   }
/*     */ 
/*     */   void handleNGrams(int minCount, int pos, int nGram, int[] buf, ObjectHandler<int[]> handler)
/*     */   {
/* 312 */     int[] integersFollowing = integersFollowing(buf, 0, pos);
/* 313 */     if (pos == nGram) {
/* 314 */       int count = count(buf, 0, nGram);
/* 315 */       if (count < minCount) return;
/* 316 */       handler.handle(buf);
/* 317 */       return;
/*     */     }
/* 319 */     for (int i = 0; i < integersFollowing.length; i++) {
/* 320 */       buf[pos] = integersFollowing[i];
/* 321 */       handleNGrams(minCount, pos + 1, nGram, buf, handler);
/*     */     }
/*     */   }
/*     */ 
/*     */   void addNGramCounts(int minCount, int pos, int nGram, int[] buf, ObjectToCounterMap<int[]> counter)
/*     */   {
/* 327 */     int[] integersFollowing = integersFollowing(buf, 0, pos);
/* 328 */     if (pos == nGram) {
/* 329 */       int count = count(buf, 0, nGram);
/* 330 */       if (count < minCount) return;
/* 331 */       counter.set(buf.clone(), count);
/* 332 */       return;
/*     */     }
/* 334 */     for (int i = 0; i < integersFollowing.length; i++) {
/* 335 */       buf[pos] = integersFollowing[i];
/* 336 */       addNGramCounts(minCount, pos + 1, nGram, buf, counter);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void checkBoundaries(int[] is, int start, int end) {
/* 341 */     if (start < 0) {
/* 342 */       String msg = "Start must be in array range. Found start=" + start;
/*     */ 
/* 344 */       throw new IndexOutOfBoundsException(msg);
/*     */     }
/* 346 */     if (end > is.length) {
/* 347 */       String msg = "End must be in array range. Found end=" + end + " Length=" + is.length;
/*     */ 
/* 350 */       throw new IndexOutOfBoundsException(msg);
/*     */     }
/* 352 */     if (end < start) {
/* 353 */       String msg = "End must be at or after start. Found start=" + start + " Found end=" + end;
/*     */ 
/* 356 */       throw new IndexOutOfBoundsException(msg);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TrieIntSeqCounter
 * JD-Core Version:    0.6.2
 */