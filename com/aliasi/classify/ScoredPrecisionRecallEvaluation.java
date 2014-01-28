/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ScoredPrecisionRecallEvaluation
/*     */ {
/* 255 */   private final List<Case> mCases = new ArrayList();
/* 256 */   private int mNegativeRef = 0;
/* 257 */   private int mPositiveRef = 0;
/*     */ 
/* 735 */   static final double[][] EMPTY_DOUBLE_2D_ARRAY = new double[0][];
/*     */ 
/*     */   public void addCase(boolean correct, double score)
/*     */   {
/* 279 */     this.mCases.add(new Case(correct, score));
/* 280 */     if (correct) this.mPositiveRef += 1; else
/* 281 */       this.mNegativeRef += 1;
/*     */   }
/*     */ 
/*     */   public void addMisses(int count)
/*     */   {
/* 295 */     this.mPositiveRef += count;
/*     */   }
/*     */ 
/*     */   public int numCases()
/*     */   {
/* 305 */     return this.mCases.size();
/*     */   }
/*     */ 
/*     */   public double[][] prCurve(boolean interpolate)
/*     */   {
/* 352 */     PrecisionRecallEvaluation eval = new PrecisionRecallEvaluation();
/*     */ 
/* 354 */     List prList = new ArrayList();
/*     */ 
/* 356 */     for (Case cse : sortedCases()) {
/* 357 */       boolean correct = cse.mCorrect;
/* 358 */       eval.addCase(correct, true);
/* 359 */       if (correct) {
/* 360 */         double r = div(eval.truePositive(), this.mPositiveRef);
/* 361 */         double p = eval.precision();
/* 362 */         prList.add(new double[] { r, p });
/*     */       }
/*     */     }
/* 365 */     return interpolate(prList, interpolate);
/*     */   }
/*     */ 
/*     */   public double[][] prScoreCurve(boolean interpolate)
/*     */   {
/* 382 */     PrecisionRecallEvaluation eval = new PrecisionRecallEvaluation();
/*     */ 
/* 384 */     List prList = new ArrayList();
/*     */ 
/* 386 */     for (Case cse : sortedCases()) {
/* 387 */       boolean correct = cse.mCorrect;
/* 388 */       eval.addCase(correct, true);
/* 389 */       if (correct) {
/* 390 */         double r = div(eval.truePositive(), this.mPositiveRef);
/* 391 */         double p = eval.precision();
/* 392 */         double s = cse.score();
/* 393 */         prList.add(new double[] { r, p, s });
/*     */       }
/*     */     }
/* 396 */     return interpolate(prList, interpolate);
/*     */   }
/*     */ 
/*     */   public double[][] rocCurve(boolean interpolate)
/*     */   {
/* 443 */     PrecisionRecallEvaluation eval = new PrecisionRecallEvaluation();
/* 444 */     List prList = new ArrayList();
/* 445 */     for (Case cse : sortedCases()) {
/* 446 */       boolean correct = cse.mCorrect;
/* 447 */       eval.addCase(correct, true);
/* 448 */       if (correct) {
/* 449 */         double r = div(eval.truePositive(), this.mPositiveRef);
/* 450 */         double rr = 1.0D - div(eval.falsePositive(), this.mNegativeRef);
/*     */ 
/* 452 */         prList.add(new double[] { r, rr });
/*     */       }
/*     */     }
/* 455 */     return interpolate(prList, interpolate);
/*     */   }
/*     */ 
/*     */   public double maximumFMeasure()
/*     */   {
/* 477 */     return maximumFMeasure(1.0D);
/*     */   }
/*     */ 
/*     */   public double maximumFMeasure(double beta)
/*     */   {
/* 500 */     double maxF = 0.0D;
/* 501 */     double[][] pr = prCurve(false);
/* 502 */     for (int i = 0; i < pr.length; i++) {
/* 503 */       double f = PrecisionRecallEvaluation.fMeasure(beta, pr[i][0], pr[i][1]);
/* 504 */       maxF = Math.max(maxF, f);
/*     */     }
/* 506 */     return maxF;
/*     */   }
/*     */ 
/*     */   public double prBreakevenPoint()
/*     */   {
/* 540 */     double[][] prCurve = prCurve(true);
/* 541 */     for (int i = 0; i < prCurve.length; i++)
/* 542 */       if (prCurve[i][0] > prCurve[i][1])
/* 543 */         return prCurve[i][1];
/* 544 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double averagePrecision()
/*     */   {
/* 568 */     double[][] prCurve = prCurve(false);
/* 569 */     double sum = 0.0D;
/* 570 */     for (int i = 0; i < prCurve.length; i++)
/* 571 */       sum += prCurve[i][1];
/* 572 */     return sum / prCurve.length;
/*     */   }
/*     */ 
/*     */   public double precisionAt(int rank)
/*     */   {
/* 585 */     if (this.mCases.size() < rank) return (0.0D / 0.0D);
/* 586 */     int correctCount = 0;
/* 587 */     Iterator it = sortedCases().iterator();
/* 588 */     for (int i = 0; i < rank; i++)
/* 589 */       if (((Case)it.next()).mCorrect)
/* 590 */         correctCount++;
/* 591 */     return correctCount / rank;
/*     */   }
/*     */ 
/*     */   public double reciprocalRank()
/*     */   {
/* 608 */     Iterator it = sortedCases().iterator();
/* 609 */     for (int i = 0; it.hasNext(); i++) {
/* 610 */       Case cse = (Case)it.next();
/* 611 */       boolean correct = cse.mCorrect;
/* 612 */       if (correct)
/* 613 */         return 1.0D / (i + 1);
/*     */     }
/* 615 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double areaUnderPrCurve(boolean interpolate)
/*     */   {
/* 640 */     return areaUnder(prCurve(interpolate));
/*     */   }
/*     */ 
/*     */   public double areaUnderRocCurve(boolean interpolate)
/*     */   {
/* 657 */     return areaUnder(rocCurve(interpolate));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 665 */     StringBuilder sb = new StringBuilder();
/* 666 */     sb.append("  Area Under PR Curve (interpolated)=" + areaUnderPrCurve(true));
/*     */ 
/* 668 */     sb.append("\n  Area Under PR Curve (uninterpolated)=" + areaUnderPrCurve(false));
/*     */ 
/* 670 */     sb.append("\n  Area Under ROC Curve (interpolated)=" + areaUnderRocCurve(true));
/*     */ 
/* 672 */     sb.append("\n  Area Under ROC Curve (uninterpolated)=" + areaUnderRocCurve(false));
/*     */ 
/* 674 */     sb.append("\n  Average Precision=" + averagePrecision());
/* 675 */     sb.append("\n  Maximum F(1) Measure=" + maximumFMeasure());
/* 676 */     sb.append("\n  BEP (Precision-Recall break even point)=" + prBreakevenPoint());
/*     */ 
/* 678 */     sb.append("\n  Reciprocal Rank=" + reciprocalRank());
/* 679 */     int[] ranks = { 5, 10, 25, 100, 500 };
/* 680 */     for (int i = 0; (i < ranks.length) && (this.mCases.size() < ranks[i]); i++) {
/* 681 */       sb.append("\n  Precision at " + ranks[i] + "=" + precisionAt(ranks[i]));
/*     */     }
/* 683 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static void printPrecisionRecallCurve(double[][] prCurve, PrintWriter pw)
/*     */   {
/* 698 */     pw.printf("%8s %8s %8s\n", new Object[] { "PRECI.", "RECALL", "F" });
/* 699 */     for (double[] pr : prCurve) {
/* 700 */       pw.printf("%8.6f %8.6f %8.6f\n", new Object[] { Double.valueOf(pr[1]), Double.valueOf(pr[0]), Double.valueOf(PrecisionRecallEvaluation.fMeasure(1.0D, pr[0], pr[1])) });
/*     */     }
/*     */ 
/* 703 */     pw.flush();
/*     */   }
/*     */ 
/*     */   private List<Case> sortedCases() {
/* 707 */     Collections.sort(this.mCases, ScoredObject.reverseComparator());
/* 708 */     return this.mCases;
/*     */   }
/*     */ 
/*     */   static double div(double x, double y) {
/* 712 */     return x / y;
/*     */   }
/*     */ 
/*     */   private static double[][] interpolate(List<double[]> prList, boolean interpolate)
/*     */   {
/* 717 */     if (!interpolate) {
/* 718 */       double[][] rps = new double[prList.size()][];
/* 719 */       prList.toArray(rps);
/* 720 */       return rps;
/*     */     }
/* 722 */     Collections.reverse(prList);
/* 723 */     LinkedList resultList = new LinkedList();
/* 724 */     double maxP = (-1.0D / 0.0D);
/* 725 */     for (double[] rp : prList) {
/* 726 */       double p = rp[1];
/* 727 */       if (maxP < p) {
/* 728 */         maxP = p;
/* 729 */         resultList.addFirst(rp);
/*     */       }
/*     */     }
/* 732 */     return (double[][])resultList.toArray(EMPTY_DOUBLE_2D_ARRAY);
/*     */   }
/*     */ 
/*     */   private static double areaUnder(double[][] zeroOneStepFunction)
/*     */   {
/* 738 */     double area = 0.0D;
/* 739 */     double lastX = 0.0D;
/* 740 */     for (int i = 0; i < zeroOneStepFunction.length; i++) {
/* 741 */       double x = zeroOneStepFunction[i][0];
/* 742 */       double height = zeroOneStepFunction[i][1];
/* 743 */       double width = x - lastX;
/* 744 */       area += width * height;
/* 745 */       lastX = x;
/*     */     }
/* 747 */     return area;
/*     */   }
/*     */   static class Case implements Scored {
/*     */     private final boolean mCorrect;
/*     */     private final double mScore;
/*     */ 
/* 754 */     Case(boolean correct, double score) { this.mCorrect = correct;
/* 755 */       this.mScore = score; }
/*     */ 
/*     */     public double score() {
/* 758 */       return this.mScore;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 762 */       return this.mCorrect + " : " + this.mScore;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ScoredPrecisionRecallEvaluation
 * JD-Core Version:    0.6.2
 */