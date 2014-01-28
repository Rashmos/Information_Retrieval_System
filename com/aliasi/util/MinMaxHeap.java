/*     */ package com.aliasi.util;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class MinMaxHeap<E extends Scored> extends AbstractCollection<E>
/*     */ {
/*     */   private final E[] mHeap;
/*     */   private final int mHeapLength;
/*     */   private final boolean[] mLevelTypes;
/*  44 */   private int mNextFreeIndex = 1;
/*     */   static final boolean MIN_LEVEL = true;
/*     */   static final boolean MAX_LEVEL = false;
/*     */ 
/*     */   public MinMaxHeap(int maxSize)
/*     */   {
/*  55 */     if (maxSize < 1) {
/*  56 */       String msg = "Heaps must be at least one element. Found maxSize=" + maxSize;
/*     */ 
/*  58 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/*  62 */     Scored[] tempHeap = (Scored[])new Scored[maxSize + 1];
/*  63 */     this.mHeap = tempHeap;
/*  64 */     this.mHeapLength = this.mHeap.length;
/*  65 */     this.mLevelTypes = new boolean[maxSize + 1];
/*  66 */     fillLevelTypes(this.mLevelTypes);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  76 */     return this.mNextFreeIndex - 1;
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/*  88 */     ArrayList list = new ArrayList();
/*  89 */     for (int i = 0; i < this.mNextFreeIndex; i++)
/*  90 */       list.add(this.mHeap[i]);
/*  91 */     Collections.sort(list, ScoredObject.reverseComparator());
/*  92 */     return list.iterator();
/*     */   }
/*     */ 
/*     */   public boolean add(E s)
/*     */   {
/* 104 */     if (this.mNextFreeIndex < this.mHeapLength) {
/* 105 */       this.mHeap[(this.mNextFreeIndex++)] = s;
/* 106 */       bubbleUp(this.mNextFreeIndex - 1);
/* 107 */       return true;
/* 108 */     }if (s.score() <= peekMin().score()) {
/* 109 */       return false;
/*     */     }
/* 111 */     popMin();
/* 112 */     this.mHeap[(this.mNextFreeIndex++)] = s;
/* 113 */     bubbleUp(this.mNextFreeIndex - 1);
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   public E peekMax()
/*     */   {
/* 125 */     return this.mHeap[2].score() > this.mHeap[3].score() ? this.mHeap[2] : this.mNextFreeIndex == 3 ? this.mHeap[2] : this.mNextFreeIndex == 2 ? this.mHeap[1] : this.mNextFreeIndex == 1 ? null : this.mHeap[3];
/*     */   }
/*     */ 
/*     */   public E peekMin()
/*     */   {
/* 145 */     return this.mNextFreeIndex == 1 ? null : this.mHeap[1];
/*     */   }
/*     */ 
/*     */   public E popMax()
/*     */   {
/* 157 */     if (this.mNextFreeIndex == 1) return null;
/*     */ 
/* 160 */     if (this.mNextFreeIndex == 2) {
/* 161 */       this.mNextFreeIndex -= 1;
/* 162 */       return this.mHeap[1];
/*     */     }
/*     */ 
/* 166 */     if (this.mNextFreeIndex == 3) {
/* 167 */       this.mNextFreeIndex -= 1;
/* 168 */       return this.mHeap[2];
/*     */     }
/*     */ 
/* 172 */     if (this.mHeap[2].score() > this.mHeap[3].score()) {
/* 173 */       Scored max = this.mHeap[2];
/* 174 */       this.mHeap[2] = this.mHeap[(--this.mNextFreeIndex)];
/* 175 */       trickleDownMax(2);
/* 176 */       return max;
/*     */     }
/* 178 */     Scored max = this.mHeap[3];
/* 179 */     this.mHeap[3] = this.mHeap[(--this.mNextFreeIndex)];
/* 180 */     trickleDownMax(3);
/* 181 */     return max;
/*     */   }
/*     */ 
/*     */   public E popMin()
/*     */   {
/* 192 */     if (this.mNextFreeIndex == 1) return null;
/*     */ 
/* 194 */     if (this.mNextFreeIndex == 2) {
/* 195 */       this.mNextFreeIndex = 1;
/* 196 */       return this.mHeap[1];
/*     */     }
/*     */ 
/* 199 */     Scored min = this.mHeap[1];
/* 200 */     this.mHeap[1] = this.mHeap[(--this.mNextFreeIndex)];
/* 201 */     trickleDownMin(1);
/* 202 */     return min;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 212 */     if (this.mNextFreeIndex == 1) return "EMPTY HEAP";
/* 213 */     StringBuilder sb = new StringBuilder();
/* 214 */     for (int i = 1; i < this.mNextFreeIndex; i++) {
/* 215 */       if (i > 1) sb.append("\n");
/* 216 */       sb.append(i + "=" + this.mHeap[i]);
/*     */     }
/* 218 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   void bubbleUp(int nodeIndex) {
/* 222 */     if (!hasParent(nodeIndex)) return;
/* 223 */     int parentIndex = parentIndex(nodeIndex);
/* 224 */     if (onMinLevel(nodeIndex)) {
/* 225 */       if (this.mHeap[nodeIndex].score() > this.mHeap[parentIndex].score()) {
/* 226 */         swap(nodeIndex, parentIndex);
/* 227 */         bubbleUpMax(parentIndex);
/*     */       } else {
/* 229 */         bubbleUpMin(nodeIndex);
/*     */       }
/*     */     }
/* 232 */     else if (this.mHeap[nodeIndex].score() < this.mHeap[parentIndex].score()) {
/* 233 */       swap(nodeIndex, parentIndex);
/* 234 */       bubbleUpMin(parentIndex);
/*     */     } else {
/* 236 */       bubbleUpMax(nodeIndex);
/*     */     }
/*     */   }
/*     */ 
/*     */   void bubbleUpMin(int nodeIndex)
/*     */   {
/*     */     while (true) {
/* 243 */       if (!hasParent(nodeIndex)) return;
/* 244 */       int parentIndex = parentIndex(nodeIndex);
/* 245 */       if (!hasParent(parentIndex)) return;
/* 246 */       int grandparentIndex = parentIndex(parentIndex);
/* 247 */       if (this.mHeap[nodeIndex].score() >= this.mHeap[grandparentIndex].score())
/* 248 */         return;
/* 249 */       swap(nodeIndex, grandparentIndex);
/* 250 */       nodeIndex = grandparentIndex;
/*     */     }
/*     */   }
/*     */ 
/*     */   void bubbleUpMax(int nodeIndex) {
/*     */     while (true) {
/* 256 */       if (!hasParent(nodeIndex)) return;
/* 257 */       int parentIndex = parentIndex(nodeIndex);
/* 258 */       if (!hasParent(parentIndex)) return;
/* 259 */       int grandparentIndex = parentIndex(parentIndex);
/* 260 */       if (this.mHeap[nodeIndex].score() <= this.mHeap[grandparentIndex].score())
/* 261 */         return;
/* 262 */       swap(nodeIndex, grandparentIndex);
/* 263 */       nodeIndex = grandparentIndex;
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean onMinLevel(int nodeIndex)
/*     */   {
/* 269 */     return this.mLevelTypes[nodeIndex];
/*     */   }
/*     */ 
/*     */   void trickleDown(int nodeIndex) {
/* 273 */     if (noChildren(nodeIndex)) return;
/* 274 */     if (onMinLevel(nodeIndex))
/* 275 */       trickleDownMin(nodeIndex);
/*     */     else
/* 277 */       trickleDownMax(nodeIndex);
/*     */   }
/*     */ 
/*     */   void trickleDownMin(int nodeIndex) {
/* 281 */     while (leftDaughterIndex(nodeIndex) < this.mNextFreeIndex) {
/* 282 */       int minDescIndex = minDtrOrGrandDtrIndex(nodeIndex);
/* 283 */       if (isDaughter(nodeIndex, minDescIndex)) {
/* 284 */         if (this.mHeap[minDescIndex].score() < this.mHeap[nodeIndex].score())
/* 285 */           swap(minDescIndex, nodeIndex);
/* 286 */         return;
/*     */       }
/* 288 */       if (this.mHeap[minDescIndex].score() >= this.mHeap[nodeIndex].score())
/* 289 */         return;
/* 290 */       swap(minDescIndex, nodeIndex);
/* 291 */       int parentIndex = parentIndex(minDescIndex);
/* 292 */       if (this.mHeap[minDescIndex].score() > this.mHeap[parentIndex].score())
/* 293 */         swap(minDescIndex, parentIndex);
/* 294 */       nodeIndex = minDescIndex;
/*     */     }
/*     */   }
/*     */ 
/*     */   void trickleDownMax(int nodeIndex)
/*     */   {
/* 300 */     while (leftDaughterIndex(nodeIndex) < this.mNextFreeIndex) {
/* 301 */       int maxDescIndex = maxDtrOrGrandDtrIndex(nodeIndex);
/* 302 */       if (isDaughter(nodeIndex, maxDescIndex)) {
/* 303 */         if (this.mHeap[maxDescIndex].score() > this.mHeap[nodeIndex].score())
/* 304 */           swap(maxDescIndex, nodeIndex);
/* 305 */         return;
/*     */       }
/* 307 */       if (this.mHeap[maxDescIndex].score() <= this.mHeap[nodeIndex].score())
/* 308 */         return;
/* 309 */       swap(maxDescIndex, nodeIndex);
/* 310 */       int parentIndex = parentIndex(maxDescIndex);
/* 311 */       if (this.mHeap[maxDescIndex].score() < this.mHeap[parentIndex].score())
/* 312 */         swap(maxDescIndex, parentIndex);
/* 313 */       nodeIndex = maxDescIndex;
/*     */     }
/*     */   }
/*     */ 
/*     */   int minDtrOrGrandDtrIndex(int nodeIndex)
/*     */   {
/* 322 */     int leftDtrIndex = leftDaughterIndex(nodeIndex);
/* 323 */     int minIndex = leftDtrIndex;
/* 324 */     double minScore = this.mHeap[leftDtrIndex].score();
/*     */ 
/* 326 */     int rightDtrIndex = rightDaughterIndex(nodeIndex);
/* 327 */     if (rightDtrIndex >= this.mNextFreeIndex) return minIndex;
/* 328 */     double rightDtrScore = this.mHeap[rightDtrIndex].score();
/* 329 */     if (rightDtrScore < minScore) {
/* 330 */       minIndex = rightDtrIndex;
/* 331 */       minScore = rightDtrScore;
/*     */     }
/*     */ 
/* 334 */     int grandDtr1Index = leftDaughterIndex(leftDtrIndex);
/* 335 */     if (grandDtr1Index >= this.mNextFreeIndex) return minIndex;
/* 336 */     double grandDtr1Score = this.mHeap[grandDtr1Index].score();
/* 337 */     if (grandDtr1Score < minScore) {
/* 338 */       minIndex = grandDtr1Index;
/* 339 */       minScore = grandDtr1Score;
/*     */     }
/*     */ 
/* 342 */     int grandDtr2Index = rightDaughterIndex(leftDtrIndex);
/* 343 */     if (grandDtr2Index >= this.mNextFreeIndex) return minIndex;
/* 344 */     double grandDtr2Score = this.mHeap[grandDtr2Index].score();
/* 345 */     if (grandDtr2Score < minScore) {
/* 346 */       minIndex = grandDtr2Index;
/* 347 */       minScore = grandDtr2Score;
/*     */     }
/*     */ 
/* 350 */     int grandDtr3Index = leftDaughterIndex(rightDtrIndex);
/* 351 */     if (grandDtr3Index >= this.mNextFreeIndex) return minIndex;
/* 352 */     double grandDtr3Score = this.mHeap[grandDtr3Index].score();
/* 353 */     if (grandDtr3Score < minScore) {
/* 354 */       minIndex = grandDtr3Index;
/* 355 */       minScore = grandDtr3Score;
/*     */     }
/*     */ 
/* 358 */     int grandDtr4Index = rightDaughterIndex(rightDtrIndex);
/* 359 */     if (grandDtr4Index >= this.mNextFreeIndex) return minIndex;
/* 360 */     double grandDtr4Score = this.mHeap[grandDtr4Index].score();
/*     */ 
/* 362 */     return grandDtr4Score < minScore ? grandDtr4Index : minIndex;
/*     */   }
/*     */ 
/*     */   int maxDtrOrGrandDtrIndex(int nodeIndex)
/*     */   {
/* 371 */     int leftDtrIndex = leftDaughterIndex(nodeIndex);
/* 372 */     int maxIndex = leftDtrIndex;
/* 373 */     double maxScore = this.mHeap[leftDtrIndex].score();
/*     */ 
/* 375 */     int rightDtrIndex = rightDaughterIndex(nodeIndex);
/* 376 */     if (rightDtrIndex >= this.mNextFreeIndex) return maxIndex;
/* 377 */     double rightDtrScore = this.mHeap[rightDtrIndex].score();
/* 378 */     if (rightDtrScore > maxScore) {
/* 379 */       maxIndex = rightDtrIndex;
/* 380 */       maxScore = rightDtrScore;
/*     */     }
/*     */ 
/* 383 */     int grandDtr1Index = leftDaughterIndex(leftDtrIndex);
/* 384 */     if (grandDtr1Index >= this.mNextFreeIndex) return maxIndex;
/* 385 */     double grandDtr1Score = this.mHeap[grandDtr1Index].score();
/* 386 */     if (grandDtr1Score > maxScore) {
/* 387 */       maxIndex = grandDtr1Index;
/* 388 */       maxScore = grandDtr1Score;
/*     */     }
/*     */ 
/* 391 */     int grandDtr2Index = rightDaughterIndex(leftDtrIndex);
/* 392 */     if (grandDtr2Index >= this.mNextFreeIndex) return maxIndex;
/* 393 */     double grandDtr2Score = this.mHeap[grandDtr2Index].score();
/* 394 */     if (grandDtr2Score > maxScore) {
/* 395 */       maxIndex = grandDtr2Index;
/* 396 */       maxScore = grandDtr2Score;
/*     */     }
/*     */ 
/* 399 */     int grandDtr3Index = leftDaughterIndex(rightDtrIndex);
/* 400 */     if (grandDtr3Index >= this.mNextFreeIndex) return maxIndex;
/* 401 */     double grandDtr3Score = this.mHeap[grandDtr3Index].score();
/* 402 */     if (grandDtr3Score > maxScore) {
/* 403 */       maxIndex = grandDtr3Index;
/* 404 */       maxScore = grandDtr3Score;
/*     */     }
/*     */ 
/* 407 */     int grandDtr4Index = rightDaughterIndex(rightDtrIndex);
/* 408 */     if (grandDtr4Index >= this.mNextFreeIndex) return maxIndex;
/* 409 */     double grandDtr4Score = this.mHeap[grandDtr4Index].score();
/*     */ 
/* 411 */     return grandDtr4Score > maxScore ? grandDtr4Index : maxIndex;
/*     */   }
/*     */ 
/*     */   boolean hasParent(int nodeIndex)
/*     */   {
/* 417 */     return nodeIndex > 1;
/*     */   }
/*     */ 
/*     */   boolean noChildren(int nodeIndex)
/*     */   {
/* 422 */     return leftDaughterIndex(nodeIndex) >= this.mHeapLength;
/*     */   }
/*     */ 
/*     */   boolean isDaughter(int nodeIndexParent, int nodeIndexDescendant) {
/* 426 */     return nodeIndexDescendant <= rightDaughterIndex(nodeIndexParent);
/*     */   }
/*     */ 
/*     */   void swap(int index1, int index2) {
/* 430 */     Scored temp = this.mHeap[index1];
/* 431 */     this.mHeap[index1] = this.mHeap[index2];
/* 432 */     this.mHeap[index2] = temp;
/*     */   }
/*     */ 
/*     */   static int parentIndex(int nodeIndex)
/*     */   {
/* 437 */     return nodeIndex / 2;
/*     */   }
/*     */ 
/*     */   static int leftDaughterIndex(int nodeIndex) {
/* 441 */     return 2 * nodeIndex;
/*     */   }
/*     */ 
/*     */   static int rightDaughterIndex(int nodeIndex) {
/* 445 */     return 2 * nodeIndex + 1;
/*     */   }
/*     */ 
/*     */   static void fillLevelTypes(boolean[] levelTypes) {
/* 449 */     boolean type = false;
/* 450 */     int index = 1;
/* 451 */     for (int numEltsOfType = 1; ; numEltsOfType *= 2) {
/* 452 */       type = !type;
/* 453 */       for (int j = 0; j < numEltsOfType; j++) {
/* 454 */         if (index >= levelTypes.length) return;
/* 455 */         levelTypes[(index++)] = type;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.MinMaxHeap
 * JD-Core Version:    0.6.2
 */