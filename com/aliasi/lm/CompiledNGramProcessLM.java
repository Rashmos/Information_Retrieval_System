/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.stats.Model;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class CompiledNGramProcessLM
/*     */   implements LanguageModel.Process, LanguageModel.Conditional, Model<CharSequence>
/*     */ {
/*     */   private final int mMaxNGram;
/*     */   private final float mLogUniformEstimate;
/*     */   private final char[] mChars;
/*     */   private final float[] mLogProbs;
/*     */   private final float[] mLogOneMinusLambdas;
/*     */   private final int[] mFirstChild;
/*     */   private final int[] mSuffix;
/*     */   private final int mLastContextIndex;
/*     */   public static final int ROOT_NODE_INDEX = 0;
/*     */   private static final int CACHE_NOT_COMPUTED_VALUE = -1;
/*     */ 
/*     */   CompiledNGramProcessLM(ObjectInput dataIn)
/*     */     throws IOException
/*     */   {
/* 260 */     this.mMaxNGram = dataIn.readInt();
/* 261 */     this.mLogUniformEstimate = dataIn.readFloat();
/* 262 */     int numTotalNodes = dataIn.readInt();
/* 263 */     int lastInternalNodeIndex = dataIn.readInt();
/* 264 */     this.mLastContextIndex = lastInternalNodeIndex;
/* 265 */     this.mChars = new char[numTotalNodes];
/* 266 */     this.mLogProbs = new float[numTotalNodes];
/* 267 */     this.mSuffix = new int[numTotalNodes];
/* 268 */     Arrays.fill(this.mSuffix, -1);
/* 269 */     this.mLogOneMinusLambdas = new float[lastInternalNodeIndex + 1];
/* 270 */     this.mFirstChild = new int[lastInternalNodeIndex + 2];
/* 271 */     this.mFirstChild[(lastInternalNodeIndex + 1)] = numTotalNodes;
/* 272 */     for (int i = 0; i <= lastInternalNodeIndex; i++) {
/* 273 */       this.mChars[i] = dataIn.readChar();
/* 274 */       this.mLogProbs[i] = dataIn.readFloat();
/* 275 */       this.mLogOneMinusLambdas[i] = dataIn.readFloat();
/* 276 */       this.mFirstChild[i] = dataIn.readInt();
/*     */     }
/* 278 */     for (int i = lastInternalNodeIndex + 1; i < numTotalNodes; i++) {
/* 279 */       this.mChars[i] = dataIn.readChar();
/* 280 */       this.mLogProbs[i] = dataIn.readFloat();
/*     */     }
/* 282 */     compileSuffixes("", 0);
/*     */   }
/*     */ 
/*     */   public char[] observedCharacters()
/*     */   {
/* 296 */     if (this.mFirstChild.length < 2) return new char[0];
/* 297 */     char[] result = new char[this.mFirstChild[1] - 1];
/* 298 */     for (int i = 0; i < result.length; i++)
/* 299 */       result[i] = this.mChars[(i + 1)];
/* 300 */     return result;
/*     */   }
/*     */ 
/*     */   public int maxNGram()
/*     */   {
/* 312 */     return this.mMaxNGram;
/*     */   }
/*     */ 
/*     */   public int numNodes()
/*     */   {
/* 324 */     return this.mChars.length;
/*     */   }
/*     */ 
/*     */   public int longestContextIndex(String context)
/*     */   {
/* 337 */     char[] cs = context.toCharArray();
/* 338 */     int length = cs.length;
/* 339 */     for (int i = 0; i < length; i++) {
/* 340 */       int k = getIndex(cs, i, length);
/* 341 */       if (k >= 0) {
/* 342 */         while (k >= this.mLogOneMinusLambdas.length)
/* 343 */           k = this.mSuffix[k];
/* 344 */         return k;
/*     */       }
/*     */     }
/* 347 */     return 0;
/*     */   }
/*     */ 
/*     */   int numInternalNodes() {
/* 351 */     return this.mFirstChild.length;
/*     */   }
/*     */ 
/*     */   private void compileSuffixes(String context, int index) {
/* 355 */     this.mSuffix[index] = suffixIndex(context);
/* 356 */     if (index >= this.mFirstChild.length) return;
/* 357 */     int firstChildIndex = this.mFirstChild[index];
/* 358 */     int lastChildIndex = index + 1 < this.mFirstChild.length ? this.mFirstChild[(index + 1)] : this.mChars.length;
/*     */ 
/* 362 */     for (int i = firstChildIndex; i < lastChildIndex; i++)
/* 363 */       compileSuffixes(context + this.mChars[i], i);
/*     */   }
/*     */ 
/*     */   private int suffixIndex(String context) {
/* 367 */     int suffixLength = context.length() - 1;
/* 368 */     if (suffixLength < 0) return -1;
/* 369 */     char[] cs = new char[suffixLength];
/* 370 */     for (int i = 0; i < suffixLength; i++)
/* 371 */       cs[i] = context.charAt(i + 1);
/* 372 */     return getIndex(cs, 0, suffixLength);
/*     */   }
/*     */ 
/*     */   public double log2Prob(CharSequence cSeq)
/*     */   {
/* 384 */     return log2Estimate(cSeq);
/*     */   }
/*     */ 
/*     */   public double prob(CharSequence cSeq)
/*     */   {
/* 396 */     return Math.pow(2.0D, log2Estimate(cSeq));
/*     */   }
/*     */ 
/*     */   public final double log2Estimate(CharSequence cSeq)
/*     */   {
/* 401 */     char[] cs = Strings.toCharArray(cSeq);
/* 402 */     return log2Estimate(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public final double log2Estimate(int contextIndex, char nextChar)
/*     */   {
/* 423 */     double sum = 0.0D;
/*     */     int outcomeIndex;
/* 425 */     for (int currentContextIndex = contextIndex; 
/* 426 */       (outcomeIndex = getIndex(currentContextIndex, nextChar)) < 0; 
/* 427 */       currentContextIndex = this.mSuffix[currentContextIndex]) {
/* 428 */       if (currentContextIndex < this.mLogOneMinusLambdas.length)
/* 429 */         sum += this.mLogOneMinusLambdas[currentContextIndex];
/* 430 */       if (currentContextIndex == 0) {
/* 431 */         return sum + this.mLogUniformEstimate;
/*     */       }
/*     */     }
/* 434 */     return sum + this.mLogProbs[outcomeIndex];
/*     */   }
/*     */ 
/*     */   public int nextContext(int contextIndex, char nextChar)
/*     */   {
/* 454 */     if ((contextIndex < 0) || (contextIndex > this.mLastContextIndex))
/*     */     {
/* 456 */       String msg = "Context must be greater than zero. Context must be less than last index=" + this.mLastContextIndex + " Context=" + contextIndex;
/*     */ 
/* 459 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 461 */     int currentContextIndex = contextIndex;
/*     */ 
/* 463 */     for (; ; currentContextIndex = this.mSuffix[currentContextIndex]) {
/* 464 */       int outcomeIndex = getIndex(currentContextIndex, nextChar);
/* 465 */       if ((outcomeIndex < this.mLogOneMinusLambdas.length) && (outcomeIndex >= 0))
/* 466 */         return outcomeIndex;
/* 467 */       if (currentContextIndex == 0)
/* 468 */         return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final double log2Estimate(char[] cs, int start, int end) {
/* 473 */     int len = this.mLogOneMinusLambdas.length;
/* 474 */     Strings.checkArgsStartEnd(cs, start, end);
/* 475 */     double sum = 0.0D;
/* 476 */     int contextIndex = 0;
/*     */ 
/* 478 */     label135: for (int i = start; i < end; i++) {
/* 479 */       char nextChar = cs[i];
/*     */       int outcomeIndex;
/* 481 */       while ((outcomeIndex = getIndex(contextIndex, nextChar)) < 0) {
/* 482 */         if (contextIndex < len)
/* 483 */           sum += this.mLogOneMinusLambdas[contextIndex];
/* 484 */         if (contextIndex == 0) {
/* 485 */           sum += this.mLogUniformEstimate;
/* 486 */           contextIndex = 0;
/* 487 */           break label135;
/*     */         }
/* 489 */         contextIndex = this.mSuffix[contextIndex];
/*     */       }
/* 491 */       sum += this.mLogProbs[outcomeIndex];
/* 492 */       contextIndex = outcomeIndex < len ? outcomeIndex : this.mSuffix[outcomeIndex];
/*     */     }
/*     */ 
/* 497 */     return sum;
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(CharSequence cSeq)
/*     */   {
/* 502 */     char[] cs = cSeq.toString().toCharArray();
/* 503 */     return log2ConditionalEstimate(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(char[] cs, int start, int end) {
/* 507 */     Strings.checkArgsStartEnd(cs, start, end);
/* 508 */     double total = 0.0D;
/* 509 */     int contextEnd = end - 1;
/* 510 */     char c = cs[contextEnd];
/* 511 */     int maxContextLength = Math.min(contextEnd - start, this.mMaxNGram - 1);
/* 512 */     for (int contextLength = maxContextLength; 
/* 513 */       contextLength >= 0; 
/* 514 */       contextLength--)
/*     */     {
/* 516 */       int contextStart = contextEnd - contextLength;
/* 517 */       int contextIndex = getIndex(cs, contextStart, contextEnd);
/* 518 */       if (contextIndex != -1) {
/* 519 */         while (contextIndex > this.mLastContextIndex) {
/* 520 */           contextIndex = this.mSuffix[contextIndex];
/*     */         }
/* 522 */         int outcomeIndex = getIndex(contextIndex, c);
/* 523 */         if (outcomeIndex != -1)
/* 524 */           return total + this.mLogProbs[outcomeIndex];
/* 525 */         total += this.mLogOneMinusLambdas[contextIndex];
/*     */       }
/*     */     }
/* 527 */     return total + this.mLogUniformEstimate;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 540 */     StringBuilder sb = new StringBuilder();
/* 541 */     sb.append("Max NGram=" + this.mMaxNGram);
/* 542 */     sb.append('\n');
/* 543 */     sb.append("Log2 Uniform Estimate=" + this.mLogUniformEstimate);
/* 544 */     sb.append('\n');
/* 545 */     sb.append("i c suff prob 1-lambda firstChild");
/* 546 */     sb.append('\n');
/* 547 */     for (int i = 0; i < this.mChars.length; i++) {
/* 548 */       sb.append(i);
/* 549 */       sb.append(" ");
/* 550 */       sb.append(this.mChars[i]);
/* 551 */       sb.append(" ");
/* 552 */       sb.append(this.mSuffix[i]);
/* 553 */       sb.append(" ");
/* 554 */       sb.append(this.mLogProbs[i]);
/* 555 */       if (i < this.mLogOneMinusLambdas.length) {
/* 556 */         sb.append(" ");
/* 557 */         sb.append(this.mFirstChild[i]);
/* 558 */         sb.append(" ");
/* 559 */         sb.append(this.mLogOneMinusLambdas[i]);
/*     */       }
/* 561 */       sb.append("\n");
/*     */     }
/* 563 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private int getIndex(int fromIndex, char c) {
/* 567 */     if (fromIndex + 1 >= this.mFirstChild.length) return -1;
/* 568 */     int low = this.mFirstChild[fromIndex];
/* 569 */     int high = this.mFirstChild[(fromIndex + 1)] - 1;
/* 570 */     while (low <= high) {
/* 571 */       int mid = (high + low) / 2;
/* 572 */       if (this.mChars[mid] == c) return mid;
/* 573 */       if (this.mChars[mid] < c)
/* 574 */         low = low == mid ? mid + 1 : mid;
/*     */       else
/* 576 */         high = high == mid ? mid - 1 : mid;
/*     */     }
/* 578 */     return -1;
/*     */   }
/*     */ 
/*     */   private int getIndex(char[] cs, int start, int end) {
/* 582 */     int index = 0;
/* 583 */     for (int currentStart = start; 
/* 584 */       currentStart < end; 
/* 585 */       currentStart++) {
/* 586 */       index = getIndex(index, cs[currentStart]);
/* 587 */       if (index == -1) return -1;
/*     */     }
/* 589 */     return index;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.CompiledNGramProcessLM
 * JD-Core Version:    0.6.2
 */