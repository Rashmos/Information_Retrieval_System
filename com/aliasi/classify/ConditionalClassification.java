/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.stats.Statistics;
/*     */ import com.aliasi.util.Pair;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ConditionalClassification extends ScoredClassification
/*     */ {
/*     */   private final double[] mConditionalProbs;
/*     */   private static final double TOLERANCE = 0.01D;
/*     */ 
/*     */   public ConditionalClassification(String[] categories, double[] conditionalProbs)
/*     */   {
/*  76 */     this(categories, conditionalProbs, conditionalProbs, 0.01D);
/*     */   }
/*     */ 
/*     */   public ConditionalClassification(String[] categories, double[] scores, double[] conditionalProbs)
/*     */   {
/*  99 */     this(categories, scores, conditionalProbs, 0.01D);
/*     */   }
/*     */ 
/*     */   public ConditionalClassification(String[] categories, double[] conditionalProbs, double tolerance)
/*     */   {
/* 124 */     this(categories, conditionalProbs, conditionalProbs, tolerance);
/*     */   }
/*     */ 
/*     */   public ConditionalClassification(String[] categories, double[] scores, double[] conditionalProbs, double tolerance)
/*     */   {
/* 153 */     super(categories, scores);
/* 154 */     this.mConditionalProbs = conditionalProbs;
/* 155 */     if ((tolerance < 0.0D) || (Double.isNaN(tolerance))) {
/* 156 */       String msg = "Tolerance must be a positive number. Found tolerance=" + tolerance;
/*     */ 
/* 158 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 160 */     for (int i = 0; i < conditionalProbs.length; i++) {
/* 161 */       if ((conditionalProbs[i] < 0.0D) || (conditionalProbs[i] > 1.0D)) {
/* 162 */         String msg = "Conditional probabilities must be  between 0.0 and 1.0. Found conditionalProbs[" + i + "]=" + conditionalProbs[i];
/*     */ 
/* 166 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 169 */     double sum = com.aliasi.util.Math.sum(conditionalProbs);
/* 170 */     if ((sum < 1.0D - tolerance) || (sum > 1.0D + tolerance)) {
/* 171 */       String msg = "Conditional probabilities must sum to 1.0. Acceptable tolerance=" + tolerance + " Found sum=" + sum;
/*     */ 
/* 174 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public double conditionalProbability(int rank)
/*     */   {
/* 190 */     if ((rank < 0) || (rank > this.mConditionalProbs.length - 1)) {
/* 191 */       String msg = "Require rank in range 0.." + (this.mConditionalProbs.length - 1) + " Found rank=" + rank;
/*     */ 
/* 194 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 196 */     return this.mConditionalProbs[rank];
/*     */   }
/*     */ 
/*     */   public double conditionalProbability(String category)
/*     */   {
/* 207 */     for (int rank = 0; rank < size(); rank++) {
/* 208 */       if (category(rank).equals(category)) {
/* 209 */         return conditionalProbability(rank);
/*     */       }
/*     */     }
/* 212 */     String msg = category + " is not a valid category for this classification.  Valid categories are:";
/* 213 */     for (int rank = 0; rank < size(); rank++) {
/* 214 */       msg = msg + " " + category(rank) + ",";
/*     */     }
/* 216 */     msg = msg.substring(0, msg.length() - 1);
/* 217 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 228 */     StringBuilder sb = new StringBuilder();
/* 229 */     sb.append("Rank  Category  Score  P(Category|Input)\n");
/* 230 */     for (int i = 0; i < size(); i++) {
/* 231 */       sb.append(i + "=" + category(i) + " " + score(i) + " " + conditionalProbability(i) + '\n');
/*     */     }
/* 233 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static ConditionalClassification createLogProbs(String[] categories, double[] logProbabilities)
/*     */   {
/* 258 */     verifyLengths(categories, logProbabilities);
/* 259 */     verifyLogProbs(logProbabilities);
/* 260 */     double[] linearProbs = logJointToConditional(logProbabilities);
/* 261 */     Pair catsProbs = sort(categories, linearProbs);
/* 262 */     return new ConditionalClassification((String[])catsProbs.a(), (double[])catsProbs.b());
/*     */   }
/*     */ 
/*     */   public static ConditionalClassification createProbs(String[] categories, double[] probabilityRatios)
/*     */   {
/* 288 */     for (int i = 0; i < probabilityRatios.length; i++) {
/* 289 */       if ((probabilityRatios[i] < 0.0D) || (Double.isInfinite(probabilityRatios[i])) || (Double.isNaN(probabilityRatios[i]))) {
/* 290 */         String msg = "Probability ratios must be non-negative and finite. Found probabilityRatios[" + i + "]=" + probabilityRatios[i];
/*     */ 
/* 292 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 295 */     if (com.aliasi.util.Math.sum(probabilityRatios) == 0.0D) {
/* 296 */       double[] conditionalProbs = new double[probabilityRatios.length];
/* 297 */       Arrays.fill(conditionalProbs, 1.0D / probabilityRatios.length);
/* 298 */       return new ConditionalClassification(categories, conditionalProbs);
/*     */     }
/*     */ 
/* 301 */     double[] logProbs = new double[probabilityRatios.length];
/* 302 */     for (int i = 0; i < probabilityRatios.length; i++)
/* 303 */       logProbs[i] = com.aliasi.util.Math.log2(probabilityRatios[i]);
/* 304 */     return createLogProbs(categories, logProbs);
/*     */   }
/*     */ 
/*     */   static void verifyLogProbs(double[] logProbabilities) {
/* 308 */     for (double x : logProbabilities)
/* 309 */       if ((Double.isNaN(x)) || (x > 0.0D)) {
/* 310 */         String msg = "Log probs must be non-positive numbers. Found x=" + x;
/*     */ 
/* 312 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */   }
/*     */ 
/*     */   static void verifyLengths(String[] categories, double[] logProbabilities)
/*     */   {
/* 319 */     if (categories.length != logProbabilities.length) {
/* 320 */       String msg = "Arrays must be same length. Found categories.length=" + categories.length + " logProbabilities.length=" + logProbabilities.length;
/*     */ 
/* 323 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Pair<String[], double[]> sort(String[] categories, double[] vals) {
/* 328 */     verifyLengths(categories, vals);
/*     */ 
/* 331 */     ScoredObject[] scoredObjects = (ScoredObject[])new ScoredObject[categories.length];
/*     */ 
/* 334 */     for (int i = 0; i < categories.length; i++) {
/* 335 */       scoredObjects[i] = new ScoredObject(categories[i], vals[i]);
/*     */     }
/* 337 */     String[] categoriesSorted = new String[scoredObjects.length];
/*     */ 
/* 339 */     double[] valsSorted = new double[categories.length];
/*     */ 
/* 341 */     Arrays.sort(scoredObjects, ScoredObject.reverseComparator());
/* 342 */     for (int i = 0; i < scoredObjects.length; i++) {
/* 343 */       categoriesSorted[i] = ((String)scoredObjects[i].getObject());
/* 344 */       valsSorted[i] = scoredObjects[i].score();
/*     */     }
/*     */ 
/* 347 */     return new Pair(categoriesSorted, valsSorted);
/*     */   }
/*     */ 
/*     */   static double[] logJointToConditional(double[] logJointProbs) {
/* 351 */     for (int i = 0; i < logJointProbs.length; i++) {
/* 352 */       if ((logJointProbs[i] > 0.0D) && (logJointProbs[i] < 1.0E-10D))
/* 353 */         logJointProbs[i] = 0.0D;
/* 354 */       if ((logJointProbs[i] > 0.0D) || (Double.isNaN(logJointProbs[i]))) {
/* 355 */         StringBuilder sb = new StringBuilder();
/* 356 */         sb.append("Joint probs must be zero or negative. Found log2JointProbs[" + i + "]=" + logJointProbs[i]);
/*     */ 
/* 358 */         for (int k = 0; k < logJointProbs.length; k++)
/* 359 */           sb.append("\nlogJointProbs[" + k + "]=" + logJointProbs[k]);
/* 360 */         throw new IllegalArgumentException(sb.toString());
/*     */       }
/*     */     }
/* 363 */     double max = com.aliasi.util.Math.maximum(logJointProbs);
/* 364 */     double[] probRatios = new double[logJointProbs.length];
/* 365 */     for (int i = 0; i < logJointProbs.length; i++) {
/* 366 */       probRatios[i] = java.lang.Math.pow(2.0D, logJointProbs[i] - max);
/* 367 */       if (probRatios[i] == (1.0D / 0.0D))
/* 368 */         probRatios[i] = 3.402823466385289E+38D;
/* 369 */       else if ((probRatios[i] == (-1.0D / 0.0D)) || (Double.isNaN(probRatios[i])))
/* 370 */         probRatios[i] = 0.0D;
/*     */     }
/* 372 */     return Statistics.normalize(probRatios);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ConditionalClassification
 * JD-Core Version:    0.6.2
 */