/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.Proximity;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class JaroWinklerDistance
/*     */   implements Distance<CharSequence>, Proximity<CharSequence>
/*     */ {
/*     */   private final double mWeightThreshold;
/*     */   private final int mNumChars;
/* 524 */   public static final JaroWinklerDistance JARO_DISTANCE = new JaroWinklerDistance();
/*     */ 
/* 536 */   public static final JaroWinklerDistance JARO_WINKLER_DISTANCE = new JaroWinklerDistance(0.7D, 4);
/*     */ 
/*     */   public JaroWinklerDistance()
/*     */   {
/* 408 */     this((1.0D / 0.0D), 0);
/*     */   }
/*     */ 
/*     */   public JaroWinklerDistance(double weightThreshold, int numChars)
/*     */   {
/* 419 */     this.mNumChars = numChars;
/* 420 */     this.mWeightThreshold = weightThreshold;
/*     */   }
/*     */ 
/*     */   public double distance(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 440 */     return 1.0D - proximity(cSeq1, cSeq2);
/*     */   }
/*     */ 
/*     */   public double proximity(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/* 459 */     int len1 = cSeq1.length();
/* 460 */     int len2 = cSeq2.length();
/* 461 */     if (len1 == 0) {
/* 462 */       return len2 == 0 ? 1.0D : 0.0D;
/*     */     }
/* 464 */     int searchRange = Math.max(0, Math.max(len1, len2) / 2 - 1);
/*     */ 
/* 466 */     boolean[] matched1 = new boolean[len1];
/* 467 */     Arrays.fill(matched1, false);
/* 468 */     boolean[] matched2 = new boolean[len2];
/* 469 */     Arrays.fill(matched2, false);
/*     */ 
/* 471 */     int numCommon = 0;
/* 472 */     for (int i = 0; i < len1; i++) {
/* 473 */       int start = Math.max(0, i - searchRange);
/* 474 */       int end = Math.min(i + searchRange + 1, len2);
/* 475 */       for (int j = start; j < end; j++)
/* 476 */         if ((matched2[j] == 0) && 
/* 477 */           (cSeq1.charAt(i) == cSeq2.charAt(j)))
/*     */         {
/* 479 */           matched1[i] = true;
/* 480 */           matched2[j] = true;
/* 481 */           numCommon++;
/* 482 */           break;
/*     */         }
/*     */     }
/* 485 */     if (numCommon == 0) return 0.0D;
/*     */ 
/* 487 */     int numHalfTransposed = 0;
/* 488 */     int j = 0;
/* 489 */     for (int i = 0; i < len1; i++) {
/* 490 */       if (matched1[i] != 0) {
/* 491 */         while (matched2[j] == 0) j++;
/* 492 */         if (cSeq1.charAt(i) != cSeq2.charAt(j))
/* 493 */           numHalfTransposed++;
/* 494 */         j++;
/*     */       }
/*     */     }
/* 497 */     int numTransposed = numHalfTransposed / 2;
/*     */ 
/* 501 */     double numCommonD = numCommon;
/* 502 */     double weight = (numCommonD / len1 + numCommonD / len2 + (numCommon - numTransposed) / numCommonD) / 3.0D;
/*     */ 
/* 506 */     if (weight <= this.mWeightThreshold) return weight;
/* 507 */     int max = Math.min(this.mNumChars, Math.min(cSeq1.length(), cSeq2.length()));
/* 508 */     int pos = 0;
/* 509 */     while ((pos < max) && (cSeq1.charAt(pos) == cSeq2.charAt(pos)))
/* 510 */       pos++;
/* 511 */     if (pos == 0) return weight;
/* 512 */     return weight + 0.1D * pos * (1.0D - weight);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.JaroWinklerDistance
 * JD-Core Version:    0.6.2
 */