/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.stats.Statistics;
/*     */ 
/*     */ public class PrecisionRecallEvaluation
/*     */ {
/*     */   private long mTP;
/*     */   private long mFP;
/*     */   private long mTN;
/*     */   private long mFN;
/*     */ 
/*     */   public PrecisionRecallEvaluation()
/*     */   {
/* 459 */     this(0L, 0L, 0L, 0L);
/*     */   }
/*     */ 
/*     */   public PrecisionRecallEvaluation(long tp, long fn, long fp, long tn)
/*     */   {
/* 474 */     validateCount("tp", tp);
/* 475 */     validateCount("fp", fp);
/* 476 */     validateCount("tn", tn);
/* 477 */     validateCount("fn", fn);
/* 478 */     this.mTP = tp;
/* 479 */     this.mFP = fp;
/* 480 */     this.mTN = tn;
/* 481 */     this.mFN = fn;
/*     */   }
/*     */ 
/*     */   public void addCase(boolean reference, boolean response)
/*     */   {
/* 492 */     if ((reference) && (response)) this.mTP += 1L;
/* 493 */     else if ((reference) && (!response)) this.mFN += 1L;
/* 494 */     else if ((!reference) && (response)) this.mFP += 1L; else
/* 495 */       this.mTN += 1L;
/*     */   }
/*     */ 
/*     */   void addCase(boolean reference, boolean response, int count) {
/* 499 */     if ((reference) && (response)) this.mTP += count;
/* 500 */     else if ((reference) && (!response)) this.mFN += count;
/* 501 */     else if ((!reference) && (response)) this.mFP += count; else
/* 502 */       this.mTN += count;
/*     */   }
/*     */ 
/*     */   public long truePositive()
/*     */   {
/* 512 */     return this.mTP;
/*     */   }
/*     */ 
/*     */   public long falsePositive()
/*     */   {
/* 522 */     return this.mFP;
/*     */   }
/*     */ 
/*     */   public long trueNegative()
/*     */   {
/* 532 */     return this.mTN;
/*     */   }
/*     */ 
/*     */   public long falseNegative()
/*     */   {
/* 542 */     return this.mFN;
/*     */   }
/*     */ 
/*     */   public long positiveReference()
/*     */   {
/* 552 */     return truePositive() + falseNegative();
/*     */   }
/*     */ 
/*     */   public long negativeReference()
/*     */   {
/* 562 */     return trueNegative() + falsePositive();
/*     */   }
/*     */ 
/*     */   public double referenceLikelihood()
/*     */   {
/* 572 */     return div(positiveReference(), total());
/*     */   }
/*     */ 
/*     */   public long positiveResponse()
/*     */   {
/* 582 */     return truePositive() + falsePositive();
/*     */   }
/*     */ 
/*     */   public long negativeResponse()
/*     */   {
/* 592 */     return trueNegative() + falseNegative();
/*     */   }
/*     */ 
/*     */   public double responseLikelihood()
/*     */   {
/* 602 */     return div(positiveResponse(), total());
/*     */   }
/*     */ 
/*     */   public long correctResponse()
/*     */   {
/* 613 */     return truePositive() + trueNegative();
/*     */   }
/*     */ 
/*     */   public long incorrectResponse()
/*     */   {
/* 624 */     return falsePositive() + falseNegative();
/*     */   }
/*     */ 
/*     */   public long total()
/*     */   {
/* 633 */     return this.mTP + this.mFP + this.mTN + this.mFN;
/*     */   }
/*     */ 
/*     */   public double accuracy()
/*     */   {
/* 644 */     return div(correctResponse(), total());
/*     */   }
/*     */ 
/*     */   public double recall()
/*     */   {
/* 656 */     return div(truePositive(), positiveReference());
/*     */   }
/*     */ 
/*     */   public double precision()
/*     */   {
/* 668 */     return div(truePositive(), positiveResponse());
/*     */   }
/*     */ 
/*     */   public double rejectionRecall()
/*     */   {
/* 679 */     return div(trueNegative(), negativeReference());
/*     */   }
/*     */ 
/*     */   public double rejectionPrecision()
/*     */   {
/* 690 */     return div(trueNegative(), negativeResponse());
/*     */   }
/*     */ 
/*     */   public double fMeasure()
/*     */   {
/* 702 */     return fMeasure(1.0D);
/*     */   }
/*     */ 
/*     */   public double fMeasure(double beta)
/*     */   {
/* 713 */     return fMeasure(beta, recall(), precision());
/*     */   }
/*     */ 
/*     */   public double jaccardCoefficient()
/*     */   {
/* 722 */     return div(truePositive(), truePositive() + falseNegative() + falsePositive());
/*     */   }
/*     */ 
/*     */   public double chiSquared()
/*     */   {
/* 732 */     double tp = truePositive();
/* 733 */     double tn = trueNegative();
/* 734 */     double fp = falsePositive();
/* 735 */     double fn = falseNegative();
/* 736 */     double tot = total();
/* 737 */     double diff = tp * tn - fp * fn;
/* 738 */     return tot * diff * diff / ((tp + fn) * (fp + tn) * (tp + fp) * (fn + tn));
/*     */   }
/*     */ 
/*     */   public double phiSquared()
/*     */   {
/* 748 */     return chiSquared() / total();
/*     */   }
/*     */ 
/*     */   public double yulesQ()
/*     */   {
/* 757 */     double tp = truePositive();
/* 758 */     double tn = trueNegative();
/* 759 */     double fp = falsePositive();
/* 760 */     double fn = falseNegative();
/* 761 */     return (tp * tn - fp * fn) / (tp * tn + fp * fn);
/*     */   }
/*     */ 
/*     */   public double yulesY()
/*     */   {
/* 770 */     double tp = truePositive();
/* 771 */     double tn = trueNegative();
/* 772 */     double fp = falsePositive();
/* 773 */     double fn = falseNegative();
/* 774 */     return (Math.sqrt(tp * tn) - Math.sqrt(fp * fn)) / (Math.sqrt(tp * tn) + Math.sqrt(fp * fn));
/*     */   }
/*     */ 
/*     */   public double fowlkesMallows()
/*     */   {
/* 784 */     double tp = truePositive();
/* 785 */     return tp / Math.sqrt(precision() * recall());
/*     */   }
/*     */ 
/*     */   public double accuracyDeviation()
/*     */   {
/* 798 */     double p = accuracy();
/* 799 */     double total = total();
/* 800 */     double variance = p * (1.0D - p) / total;
/* 801 */     return Math.sqrt(variance);
/*     */   }
/*     */ 
/*     */   public double randomAccuracy()
/*     */   {
/* 812 */     double ref = referenceLikelihood();
/* 813 */     double resp = responseLikelihood();
/* 814 */     return ref * resp + (1.0D - ref) * (1.0D - resp);
/*     */   }
/*     */ 
/*     */   public double randomAccuracyUnbiased()
/*     */   {
/* 825 */     double avg = (referenceLikelihood() + responseLikelihood()) / 2.0D;
/* 826 */     return avg * avg + (1.0D - avg) * (1.0D - avg);
/*     */   }
/*     */ 
/*     */   public double kappa()
/*     */   {
/* 835 */     return Statistics.kappa(accuracy(), randomAccuracy());
/*     */   }
/*     */ 
/*     */   public double kappaUnbiased()
/*     */   {
/* 844 */     return Statistics.kappa(accuracy(), randomAccuracyUnbiased());
/*     */   }
/*     */ 
/*     */   public double kappaNoPrevalence()
/*     */   {
/* 855 */     return 2.0D * accuracy() - 1.0D;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 865 */     StringBuilder sb = new StringBuilder(2048);
/* 866 */     sb.append("  Total=" + total() + '\n');
/* 867 */     sb.append("  True Positive=" + truePositive() + '\n');
/* 868 */     sb.append("  False Negative=" + falseNegative() + '\n');
/* 869 */     sb.append("  False Positive=" + falsePositive() + '\n');
/* 870 */     sb.append("  True Negative=" + trueNegative() + '\n');
/* 871 */     sb.append("  Positive Reference=" + positiveReference() + '\n');
/* 872 */     sb.append("  Positive Response=" + positiveResponse() + '\n');
/* 873 */     sb.append("  Negative Reference=" + negativeReference() + '\n');
/* 874 */     sb.append("  Negative Response=" + negativeResponse() + '\n');
/* 875 */     sb.append("  Accuracy=" + accuracy() + '\n');
/* 876 */     sb.append("  Recall=" + recall() + '\n');
/* 877 */     sb.append("  Precision=" + precision() + '\n');
/* 878 */     sb.append("  Rejection Recall=" + rejectionRecall() + '\n');
/* 879 */     sb.append("  Rejection Precision=" + rejectionPrecision() + '\n');
/* 880 */     sb.append("  F(1)=" + fMeasure(1.0D) + '\n');
/* 881 */     sb.append("  Fowlkes-Mallows=" + fowlkesMallows() + '\n');
/* 882 */     sb.append("  Jaccard Coefficient=" + jaccardCoefficient() + '\n');
/* 883 */     sb.append("  Yule's Q=" + yulesQ() + '\n');
/* 884 */     sb.append("  Yule's Y=" + yulesY() + '\n');
/* 885 */     sb.append("  Reference Likelihood=" + referenceLikelihood() + '\n');
/* 886 */     sb.append("  Response Likelihood=" + responseLikelihood() + '\n');
/* 887 */     sb.append("  Random Accuracy=" + randomAccuracy() + '\n');
/* 888 */     sb.append("  Random Accuracy Unbiased=" + randomAccuracyUnbiased() + '\n');
/*     */ 
/* 890 */     sb.append("  kappa=" + kappa() + '\n');
/* 891 */     sb.append("  kappa Unbiased=" + kappaUnbiased() + '\n');
/* 892 */     sb.append("  kappa No Prevalence=" + kappaNoPrevalence() + '\n');
/* 893 */     sb.append("  chi Squared=" + chiSquared() + '\n');
/* 894 */     sb.append("  phi Squared=" + phiSquared() + '\n');
/* 895 */     sb.append("  Accuracy Deviation=" + accuracyDeviation());
/* 896 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static double fMeasure(double beta, double recall, double precision)
/*     */   {
/* 912 */     double betaSq = beta * beta;
/* 913 */     return (1.0D + betaSq) * recall * precision / (recall + betaSq * precision);
/*     */   }
/*     */ 
/*     */   private static void validateCount(String countName, long val)
/*     */   {
/* 918 */     if (val < 0L) {
/* 919 */       String msg = "Count must be non-negative. Found " + countName + "=" + val;
/*     */ 
/* 921 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static double div(double x, double y) {
/* 926 */     return x / y;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.PrecisionRecallEvaluation
 * JD-Core Version:    0.6.2
 */