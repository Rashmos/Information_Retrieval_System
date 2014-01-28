/*      */ package com.aliasi.stats;
/*      */ 
/*      */ import java.util.Arrays;
/*      */ import java.util.Random;
/*      */ 
/*      */ public class Statistics
/*      */ {
/*      */   public static double klDivergenceDirichlet(double[] xs, double[] ys)
/*      */   {
/*   91 */     verifyDivergenceDirichletArgs(xs, ys);
/*      */ 
/*   93 */     double sumXs = sum(xs);
/*   94 */     double sumYs = sum(ys);
/*   95 */     double divergence = logGamma(sumXs) - logGamma(sumYs);
/*      */ 
/*   98 */     double digammaSumXs = com.aliasi.util.Math.digamma(sumXs);
/*   99 */     for (int i = 0; i < xs.length; i++) {
/*  100 */       divergence += logGamma(ys[i]) - logGamma(xs[i]) + (xs[i] - ys[i]) * (com.aliasi.util.Math.digamma(xs[i]) - digammaSumXs);
/*      */     }
/*      */ 
/*  105 */     return divergence;
/*      */   }
/*      */ 
/*      */   static void verifyDivergenceDirichletArgs(double[] xs, double[] ys) {
/*  109 */     if (xs.length != ys.length) {
/*  110 */       String msg = "Parameter arrays must be the same length. Found xs.length=" + xs.length + " ys.length=" + ys.length;
/*      */ 
/*  113 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  115 */     for (int i = 0; i < xs.length; i++) {
/*  116 */       if ((xs[i] <= 0.0D) || (Double.isInfinite(xs[i])) || (Double.isNaN(xs[i]))) {
/*  117 */         String msg = "All parameters must be positive and finite. Found xs[" + i + "]=" + xs[i];
/*      */ 
/*  119 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*  122 */     for (int i = 0; i < ys.length; i++)
/*  123 */       if ((ys[i] <= 0.0D) || (Double.isInfinite(ys[i])) || (Double.isNaN(ys[i]))) {
/*  124 */         String msg = "All parameters must be positive and finite. Found ys[" + i + "]=" + ys[i];
/*      */ 
/*  126 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */   }
/*      */ 
/*      */   public static double symmetrizedKlDivergenceDirichlet(double[] xs, double[] ys)
/*      */   {
/*  153 */     return (klDivergenceDirichlet(xs, ys) + klDivergenceDirichlet(ys, xs)) / 2.0D;
/*      */   }
/*      */ 
/*      */   static double logGamma(double x) {
/*  157 */     return com.aliasi.util.Math.log2Gamma(x) / com.aliasi.util.Math.log2(2.718281828459045D);
/*      */   }
/*      */ 
/*      */   static double sum(double[] xs)
/*      */   {
/*  162 */     double sum = 0.0D;
/*  163 */     for (int i = 0; i < xs.length; i++)
/*  164 */       sum += xs[i];
/*  165 */     return sum;
/*      */   }
/*      */ 
/*      */   public static double klDivergence(double[] p, double[] q)
/*      */   {
/*  208 */     verifyDivergenceArgs(p, q);
/*  209 */     double divergence = 0.0D;
/*  210 */     int len = p.length;
/*  211 */     for (int i = 0; i < len; i++) {
/*  212 */       if ((p[i] > 0.0D) && (p[i] != q[i]))
/*  213 */         divergence += p[i] * com.aliasi.util.Math.log2(p[i] / q[i]);
/*      */     }
/*  215 */     return divergence;
/*      */   }
/*      */ 
/*      */   static void verifyDivergenceArgs(double[] p, double[] q) {
/*  219 */     if (p.length != q.length) {
/*  220 */       String msg = "Input distributions must have same length. Found p.length=" + p.length + " q.length=" + q.length;
/*      */ 
/*  223 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  225 */     int len = p.length;
/*  226 */     for (int i = 0; i < len; i++) {
/*  227 */       if ((p[i] < 0.0D) || (p[i] > 1.0D) || (Double.isNaN(p[i])) || (Double.isInfinite(p[i])))
/*      */       {
/*  231 */         String msg = "p[i] must be between 0.0 and 1.0 inclusive. found p[" + i + "]=" + p[i];
/*      */ 
/*  233 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  235 */       if ((q[i] < 0.0D) || (q[i] > 1.0D) || (Double.isNaN(q[i])) || (Double.isInfinite(q[i])))
/*      */       {
/*  239 */         String msg = "q[i] must be between 0.0 and 1.0 inclusive. found q[" + i + "] =" + q[i];
/*      */ 
/*  241 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static double symmetrizedKlDivergence(double[] p, double[] q)
/*      */   {
/*  264 */     verifyDivergenceArgs(p, q);
/*  265 */     return (klDivergence(p, q) + klDivergence(q, p)) / 2.0D;
/*      */   }
/*      */ 
/*      */   public static double jsDivergence(double[] p, double[] q)
/*      */   {
/*  295 */     verifyDivergenceArgs(p, q);
/*  296 */     double[] m = new double[p.length];
/*  297 */     for (int i = 0; i < p.length; i++)
/*  298 */       m[i] = ((p[i] + q[i]) / 2.0D);
/*  299 */     return (klDivergence(p, m) + klDivergence(q, m)) / 2.0D;
/*      */   }
/*      */ 
/*      */   public static int[] permutation(int length)
/*      */   {
/*  311 */     return permutation(length, new Random());
/*      */   }
/*      */ 
/*      */   public static int[] permutation(int length, Random random)
/*      */   {
/*  324 */     int[] xs = new int[length];
/*  325 */     for (int i = 0; i < xs.length; i++)
/*  326 */       xs[i] = i;
/*  327 */     int i = xs.length;
/*      */     while (true) { i--; if (i <= 0) break;
/*  328 */       int pos = random.nextInt(i);
/*  329 */       int temp = xs[pos];
/*  330 */       xs[pos] = xs[i];
/*  331 */       xs[i] = temp;
/*      */     }
/*  333 */     return xs;
/*      */   }
/*      */ 
/*      */   public static double chiSquaredIndependence(double both, double oneOnly, double twoOnly, double neither)
/*      */   {
/*  427 */     assertNonNegative("both", both);
/*  428 */     assertNonNegative("oneOnly", oneOnly);
/*  429 */     assertNonNegative("twoOnly", twoOnly);
/*  430 */     assertNonNegative("neither", neither);
/*  431 */     double n = both + oneOnly + twoOnly + neither;
/*  432 */     double p1 = (both + oneOnly) / n;
/*  433 */     double p2 = (both + twoOnly) / n;
/*  434 */     double eBoth = n * p1 * p2;
/*  435 */     double eOneOnly = n * p1 * (1.0D - p2);
/*  436 */     double eTwoOnly = n * (1.0D - p1) * p2;
/*  437 */     double eNeither = n * (1.0D - p1) * (1.0D - p2);
/*  438 */     return csTerm(both, eBoth) + csTerm(oneOnly, eOneOnly) + csTerm(twoOnly, eTwoOnly) + csTerm(neither, eNeither);
/*      */   }
/*      */ 
/*      */   public static double[] linearRegression(double[] xs, double[] ys)
/*      */   {
/*  492 */     if (xs.length != ys.length) {
/*  493 */       String msg = "Require parallel arrays of x and y values. Found xs.length=" + xs.length + " ys.length=" + ys.length;
/*      */ 
/*  496 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  498 */     if (xs.length < 2) {
/*  499 */       String msg = "Require arrays of length >= 2. Found xs.length=" + xs.length;
/*      */ 
/*  501 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  503 */     double n = xs.length;
/*  504 */     double xSum = 0.0D;
/*  505 */     double ySum = 0.0D;
/*  506 */     double xySum = 0.0D;
/*  507 */     double xxSum = 0.0D;
/*  508 */     for (int i = 0; i < xs.length; i++) {
/*  509 */       double x = xs[i];
/*  510 */       double y = ys[i];
/*  511 */       xSum += x;
/*  512 */       ySum += y;
/*  513 */       xxSum += x * x;
/*  514 */       xySum += x * y;
/*      */     }
/*  516 */     double denominator = n * xxSum - xSum * xSum;
/*  517 */     if (denominator == 0.0D) {
/*  518 */       String msg = "Ill formed input. Denominator for beta1 is zero. Most likely cause is fewer than 2 distinct inputs.";
/*      */ 
/*  520 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  522 */     double beta1 = (n * xySum - xSum * ySum) / denominator;
/*      */ 
/*  524 */     double beta0 = (ySum - beta1 * xSum) / n;
/*  525 */     return new double[] { beta0, beta1 };
/*      */   }
/*      */ 
/*      */   public static double[] logisticRegression(double[] xs, double[] ys, double maxValue)
/*      */   {
/*  562 */     if ((maxValue <= 0.0D) || (Double.isInfinite(maxValue)) || (Double.isNaN(maxValue))) {
/*  563 */       String msg = "Require finite max value > 0. Found maxValue=" + maxValue;
/*      */ 
/*  565 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  567 */     double[] logisticYs = new double[ys.length];
/*  568 */     for (int i = 0; i < ys.length; i++)
/*  569 */       logisticYs[i] = java.lang.Math.log((maxValue - ys[i]) / ys[i]);
/*  570 */     return linearRegression(xs, logisticYs);
/*      */   }
/*      */ 
/*      */   public static double chiSquaredIndependence(double[][] contingencyMatrix)
/*      */   {
/*  636 */     int numRows = contingencyMatrix.length;
/*  637 */     if (numRows < 2) {
/*  638 */       String msg = "Require at least two rows. Found numRows=" + numRows;
/*      */ 
/*  640 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  642 */     int numCols = contingencyMatrix[0].length;
/*  643 */     if (numCols < 2) {
/*  644 */       String msg = "Require at least two cols. Found numCols=" + numCols;
/*      */ 
/*  646 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  648 */     double[] rowSums = new double[numRows];
/*  649 */     Arrays.fill(rowSums, 0.0D);
/*  650 */     double[] colSums = new double[numCols];
/*  651 */     Arrays.fill(colSums, 0.0D);
/*  652 */     double totalCount = 0.0D;
/*  653 */     for (int i = 0; i < numRows; i++) {
/*  654 */       if (contingencyMatrix[i].length != numCols) {
/*  655 */         String msg = "Matrix must be rectangular.Row 0 length=" + numCols + "Row " + i + " length=" + contingencyMatrix[i].length;
/*      */ 
/*  658 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  660 */       for (int j = 0; j < numCols; j++) {
/*  661 */         double val = contingencyMatrix[i][j];
/*  662 */         if ((Double.isInfinite(val)) || (val < 0.0D) || (Double.isNaN(val)))
/*      */         {
/*  664 */           String msg = "Values must be finite non-negative. Found matrix[" + i + "][" + j + "]=" + val;
/*      */ 
/*  667 */           throw new IllegalArgumentException(msg);
/*      */         }
/*  669 */         rowSums[i] += val;
/*  670 */         colSums[j] += val;
/*  671 */         totalCount += val;
/*      */       }
/*      */     }
/*  674 */     double result = 0.0D;
/*  675 */     for (int i = 0; i < numRows; i++) {
/*  676 */       for (int j = 0; j < numCols; j++)
/*  677 */         result += csTerm(contingencyMatrix[i][j], rowSums[i] * colSums[j] / totalCount);
/*      */     }
/*  679 */     return result;
/*      */   }
/*      */ 
/*      */   public static double[] normalize(double[] probabilityRatios)
/*      */   {
/*  707 */     for (int i = 0; i < probabilityRatios.length; i++) {
/*  708 */       if ((probabilityRatios[i] < 0.0D) || (Double.isInfinite(probabilityRatios[i])) || (Double.isNaN(probabilityRatios[i])))
/*      */       {
/*  711 */         String msg = "Probabilities must be finite non-negative. Found probabilityRatios[" + i + "]=" + probabilityRatios[i];
/*      */ 
/*  714 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*  717 */     double sum = com.aliasi.util.Math.sum(probabilityRatios);
/*  718 */     if (sum <= 0.0D) {
/*  719 */       String msg = "Ratios must sum to number greater than zero. Found sum=" + sum;
/*      */ 
/*  721 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  723 */     double[] result = new double[probabilityRatios.length];
/*  724 */     for (int i = 0; i < probabilityRatios.length; i++)
/*  725 */       probabilityRatios[i] /= sum;
/*  726 */     return result;
/*      */   }
/*      */ 
/*      */   public static double kappa(double observedProb, double expectedProb)
/*      */   {
/*  750 */     return (observedProb - expectedProb) / (1.0D - expectedProb);
/*      */   }
/*      */ 
/*      */   public static double mean(double[] xs)
/*      */   {
/*  769 */     return com.aliasi.util.Math.sum(xs) / xs.length;
/*      */   }
/*      */ 
/*      */   public static double variance(double[] xs)
/*      */   {
/*  790 */     return variance(xs, mean(xs));
/*      */   }
/*      */ 
/*      */   public static double standardDeviation(double[] xs)
/*      */   {
/*  809 */     return java.lang.Math.sqrt(variance(xs));
/*      */   }
/*      */ 
/*      */   public static double correlation(double[] xs, double[] ys)
/*      */   {
/*  871 */     if (xs.length != ys.length) {
/*  872 */       String msg = "xs and ys must be the same length. Found xs.length=" + xs.length + " ys.length=" + ys.length;
/*      */ 
/*  875 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  877 */     double meanXs = mean(xs);
/*  878 */     double meanYs = mean(ys);
/*  879 */     double ssXX = sumSquareDiffs(xs, meanXs);
/*  880 */     double ssYY = sumSquareDiffs(ys, meanYs);
/*  881 */     double ssXY = sumSquareDiffs(xs, ys, meanXs, meanYs);
/*      */ 
/*  883 */     return java.lang.Math.sqrt(ssXY * ssXY / (ssXX * ssYY));
/*      */   }
/*      */ 
/*      */   public static int sample(double[] cumulativeProbRatios, Random random)
/*      */   {
/*  932 */     int low = 0;
/*  933 */     int high = cumulativeProbRatios.length - 1;
/*  934 */     double x = random.nextDouble() * cumulativeProbRatios[high];
/*  935 */     while (low < high) {
/*  936 */       int mid = (high + low) / 2;
/*  937 */       if (x > cumulativeProbRatios[mid]) {
/*  938 */         low = mid + 1; } else {
/*  939 */         if (high == mid) {
/*  940 */           return x > cumulativeProbRatios[low] ? mid : low;
/*      */         }
/*  942 */         high = mid;
/*      */       }
/*      */     }
/*  945 */     return low;
/*      */   }
/*      */ 
/*      */   public static double dirichletLog2Prob(double alpha, double[] xs)
/*      */   {
/*  989 */     verifyAlpha(alpha);
/*  990 */     verifyDistro(xs);
/*  991 */     int k = xs.length;
/*      */ 
/*  993 */     double result = com.aliasi.util.Math.log2Gamma(k * alpha) - k * com.aliasi.util.Math.log2Gamma(alpha);
/*      */ 
/*  995 */     double alphaMinus1 = alpha - 1.0D;
/*  996 */     for (int i = 0; i < k; i++)
/*  997 */       result += alphaMinus1 * com.aliasi.util.Math.log2(xs[i]);
/*  998 */     return result;
/*      */   }
/*      */ 
/*      */   public static double dirichletLog2Prob(double[] alphas, double[] xs)
/*      */   {
/* 1052 */     if (alphas.length != xs.length) {
/* 1053 */       String msg = "Dirichlet prior alphas and distribution xs must be the same length. Found alphas.length=" + alphas.length + " xs.length=" + xs.length;
/*      */ 
/* 1056 */       throw new IllegalArgumentException(msg);
/*      */     }
/* 1058 */     for (int i = 0; i < alphas.length; i++)
/* 1059 */       verifyAlpha(alphas[i]);
/* 1060 */     verifyDistro(xs);
/* 1061 */     int k = xs.length;
/* 1062 */     double result = 0.0D;
/* 1063 */     double alphaSum = 0.0D;
/* 1064 */     for (int i = 0; i < alphas.length; i++) {
/* 1065 */       alphaSum += alphas[i];
/* 1066 */       result -= com.aliasi.util.Math.log2Gamma(alphas[i]);
/*      */     }
/* 1068 */     result += com.aliasi.util.Math.log2Gamma(alphaSum);
/* 1069 */     for (int i = 0; i < k; i++)
/* 1070 */       result += (alphas[i] - 1.0D) * com.aliasi.util.Math.log2(xs[i]);
/* 1071 */     return result;
/*      */   }
/*      */ 
/*      */   static void verifyAlpha(double alpha)
/*      */   {
/* 1076 */     if ((Double.isNaN(alpha)) || (Double.isInfinite(alpha)) || (alpha <= 0.0D)) {
/* 1077 */       String msg = "Concentration parameter must be positive and finite. Found alpha=" + alpha;
/*      */ 
/* 1079 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   static void verifyDistro(double[] xs) {
/* 1084 */     for (int i = 0; i < xs.length; i++)
/* 1085 */       if ((xs[i] < 0.0D) || (xs[i] > 1.0D) || (Double.isNaN(xs[i])) || (Double.isInfinite(xs[i]))) {
/* 1086 */         String msg = "All xs must be betwee 0.0 and 1.0 inclusive. Found xs[" + i + "]=" + xs[i];
/*      */ 
/* 1088 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */   }
/*      */ 
/*      */   static double sumSquareDiffs(double[] xs, double mean)
/*      */   {
/* 1097 */     double sum = 0.0D;
/* 1098 */     for (int i = 0; i < xs.length; i++) {
/* 1099 */       double diff = xs[i] - mean;
/* 1100 */       sum += diff * diff;
/*      */     }
/* 1102 */     return sum;
/*      */   }
/*      */ 
/*      */   static double sumSquareDiffs(double[] xs, double[] ys, double meanXs, double meanYs) {
/* 1106 */     double sum = 0.0D;
/* 1107 */     for (int i = 0; i < xs.length; i++)
/* 1108 */       sum += (xs[i] - meanXs) * (ys[i] - meanYs);
/* 1109 */     return sum;
/*      */   }
/*      */ 
/*      */   static double variance(double[] xs, double mean) {
/* 1113 */     return sumSquareDiffs(xs, mean) / xs.length;
/*      */   }
/*      */ 
/*      */   static void assertNonNegative(String variableName, double value)
/*      */   {
/* 1120 */     if ((Double.isInfinite(value)) || (Double.isNaN(value)) || (value < 0.0D)) {
/* 1121 */       String msg = "Require finite non-negative value. Found " + variableName + " =" + value;
/*      */ 
/* 1123 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static double csTerm(double found, double expected) {
/* 1128 */     double diff = found - expected;
/* 1129 */     return diff * diff / expected;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.Statistics
 * JD-Core Version:    0.6.2
 */