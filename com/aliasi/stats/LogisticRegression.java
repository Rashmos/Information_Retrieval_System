/*     */ package com.aliasi.stats;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.io.Reporter;
/*     */ import com.aliasi.io.Reporters;
/*     */ import com.aliasi.matrix.DenseVector;
/*     */ import com.aliasi.matrix.Matrices;
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Formatter;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class LogisticRegression
/*     */   implements Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -8585743596322227589L;
/*     */   private final Vector[] mWeightVectors;
/*     */ 
/*     */   public LogisticRegression(Vector[] weightVectors)
/*     */   {
/* 383 */     if (weightVectors.length < 1) {
/* 384 */       String msg = "Require at least one weight vector.";
/* 385 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 387 */     int numDimensions = weightVectors[0].numDimensions();
/* 388 */     for (int k = 1; k < weightVectors.length; k++) {
/* 389 */       if (numDimensions != weightVectors[k].numDimensions()) {
/* 390 */         String msg = "All weight vectors must be same dimensionality. Found weightVectors[0].numDimensions()=" + numDimensions + " weightVectors[" + k + "]=" + weightVectors[k].numDimensions();
/*     */ 
/* 393 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 396 */     this.mWeightVectors = weightVectors;
/*     */   }
/*     */ 
/*     */   public LogisticRegression(Vector weightVector)
/*     */   {
/* 411 */     this.mWeightVectors = new Vector[] { weightVector };
/*     */   }
/*     */ 
/*     */   public int numInputDimensions()
/*     */   {
/* 421 */     return this.mWeightVectors[0].numDimensions();
/*     */   }
/*     */ 
/*     */   public int numOutcomes()
/*     */   {
/* 431 */     return this.mWeightVectors.length + 1;
/*     */   }
/*     */ 
/*     */   public Vector[] weightVectors()
/*     */   {
/* 444 */     Vector[] immutables = new Vector[this.mWeightVectors.length];
/* 445 */     for (int i = 0; i < immutables.length; i++)
/* 446 */       immutables[i] = Matrices.unmodifiableVector(this.mWeightVectors[i]);
/* 447 */     return immutables;
/*     */   }
/*     */ 
/*     */   public double[] classify(Vector x)
/*     */   {
/* 470 */     double[] ysHat = new double[numOutcomes()];
/* 471 */     classify(x, ysHat);
/* 472 */     return ysHat;
/*     */   }
/*     */ 
/*     */   public void classify(Vector x, double[] ysHat)
/*     */   {
/* 493 */     if (numInputDimensions() != x.numDimensions()) {
/* 494 */       String msg = "Vector and classifer must be of same dimensionality. Regression model this.numInputDimensions()=" + numInputDimensions() + " Vector x.numDimensions()=" + x.numDimensions();
/*     */ 
/* 497 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 499 */     int numOutcomesMinus1 = ysHat.length - 1;
/* 500 */     ysHat[numOutcomesMinus1] = 0.0D;
/* 501 */     double max = 0.0D;
/* 502 */     for (int k = 0; k < numOutcomesMinus1; k++) {
/* 503 */       ysHat[k] = x.dotProduct(this.mWeightVectors[k]);
/* 504 */       if (ysHat[k] > max)
/* 505 */         max = ysHat[k];
/*     */     }
/* 507 */     double z = 0.0D;
/* 508 */     for (int k = 0; k < ysHat.length; k++) {
/* 509 */       ysHat[k] = java.lang.Math.exp(ysHat[k] - max);
/* 510 */       z += ysHat[k];
/*     */     }
/* 512 */     for (int k = 0; k < ysHat.length; k++)
/* 513 */       ysHat[k] /= z;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 528 */     out.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 532 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public static LogisticRegression estimate(Vector[] xs, int[] cs, RegressionPrior prior, AnnealingSchedule annealingSchedule, Reporter reporter, double minImprovement, int minEpochs, int maxEpochs)
/*     */   {
/* 582 */     LogisticRegression hotStart = null;
/* 583 */     ObjectHandler handler = null;
/* 584 */     int rollingAverageSize = 10;
/* 585 */     int priorBlockSize = java.lang.Math.max(1, cs.length / 50);
/* 586 */     return estimate(xs, cs, prior, priorBlockSize, hotStart, annealingSchedule, minImprovement, rollingAverageSize, minEpochs, maxEpochs, handler, reporter);
/*     */   }
/*     */ 
/*     */   public static LogisticRegression estimate(Vector[] xs, int[] cs, RegressionPrior prior, int priorBlockSize, LogisticRegression hotStart, AnnealingSchedule annealingSchedule, double minImprovement, int rollingAverageSize, int minEpochs, int maxEpochs, ObjectHandler<LogisticRegression> handler, Reporter reporter)
/*     */   {
/* 650 */     if (reporter == null) {
/* 651 */       reporter = Reporters.silent();
/*     */     }
/* 653 */     reporter.info("Logistic Regression Estimation");
/*     */ 
/* 655 */     boolean monitoringConvergence = !Double.isNaN(minImprovement);
/* 656 */     reporter.info("Monitoring convergence=" + monitoringConvergence);
/* 657 */     if (minImprovement < 0.0D) {
/* 658 */       String msg = "Min improvement should be Double.NaN to turn off convergence or >= 0.0 otherwise. Found minImprovement=" + minImprovement;
/*     */ 
/* 660 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 663 */     if (xs.length < 1) {
/* 664 */       String msg = "Require at least one training instance.";
/* 665 */       reporter.fatal(msg);
/* 666 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 669 */     if (xs.length != cs.length) {
/* 670 */       String msg = "Require same number of training instances as outcomes. Found xs.length=" + xs.length + " cs.length=" + cs.length;
/*     */ 
/* 673 */       reporter.fatal(msg);
/* 674 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 678 */     int numTrainingInstances = xs.length;
/* 679 */     int numOutcomesMinus1 = com.aliasi.util.Math.max(cs);
/* 680 */     int numOutcomes = numOutcomesMinus1 + 1;
/* 681 */     int numDimensions = xs[0].numDimensions();
/*     */ 
/* 683 */     prior.verifyNumberOfDimensions(numDimensions);
/*     */ 
/* 685 */     for (int i = 1; i < xs.length; i++) {
/* 686 */       if (xs[i].numDimensions() != numDimensions) {
/* 687 */         String msg = "Number of dimensions must match for all input vectors. Found xs[0].numDimensions()=" + numDimensions + " xs[" + i + "].numDimensions()=" + xs[i].numDimensions();
/*     */ 
/* 690 */         reporter.fatal(msg);
/* 691 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 696 */     DenseVector[] weightVectors = new DenseVector[numOutcomesMinus1];
/* 697 */     if (hotStart == null) {
/* 698 */       for (int k = 0; k < numOutcomesMinus1; k++)
/* 699 */         weightVectors[k] = new DenseVector(numDimensions);
/*     */     } else {
/* 701 */       Vector[] hotStartWeightVectors = hotStart.weightVectors();
/* 702 */       for (int k = 0; k < weightVectors.length; k++)
/* 703 */         weightVectors[k] = new DenseVector(hotStartWeightVectors[k]);
/*     */     }
/* 705 */     LogisticRegression regression = new LogisticRegression(weightVectors);
/*     */ 
/* 707 */     boolean hasPrior = (prior != null) && (!prior.isUniform());
/*     */ 
/* 709 */     reporter.info("Number of dimensions=" + numDimensions);
/* 710 */     reporter.info("Number of Outcomes=" + numOutcomes);
/* 711 */     reporter.info("Number of Parameters=" + (numOutcomes - 1) * numDimensions);
/* 712 */     reporter.info("Number of Training Instances=" + cs.length);
/* 713 */     reporter.info("Prior=" + prior);
/* 714 */     reporter.info("Annealing Schedule=" + annealingSchedule);
/* 715 */     reporter.info("Minimum Epochs=" + minEpochs);
/* 716 */     reporter.info("Maximum Epochs=" + maxEpochs);
/* 717 */     reporter.info("Minimum Improvement Per Period=" + minImprovement);
/* 718 */     reporter.info("Has Informative Prior=" + hasPrior);
/*     */ 
/* 720 */     double lastLog2LikelihoodAndPrior = -8.988465674311579E+307D;
/*     */ 
/* 722 */     double[] rollingAbsDiffs = new double[rollingAverageSize];
/* 723 */     Arrays.fill(rollingAbsDiffs, (1.0D / 0.0D));
/* 724 */     int rollingAveragePosition = 0;
/* 725 */     double bestLog2LikelihoodAndPrior = (-1.0D / 0.0D);
/* 726 */     for (int epoch = 0; epoch < maxEpochs; epoch++)
/*     */     {
/* 728 */       DenseVector[] weightVectorCopies = copy(weightVectors);
/*     */ 
/* 730 */       double learningRate = annealingSchedule.learningRate(epoch);
/* 731 */       double[] conditionalProbs = new double[numOutcomes];
/* 732 */       for (int j = 0; j < numTrainingInstances; j++) {
/* 733 */         if ((j % (numTrainingInstances / 10) == 0) && (reporter.isDebugEnabled())) {
/* 734 */           reporter.debug("          epoch " + epoch + " is " + 100 * j / numTrainingInstances + "% complete");
/*     */         }
/* 736 */         Vector xsJ = xs[j];
/* 737 */         int csJ = cs[j];
/* 738 */         if ((hasPrior) && (j > 0) && (j % priorBlockSize == 0)) {
/* 739 */           adjustWeightsWithPrior(weightVectors, prior, learningRate * priorBlockSize / numTrainingInstances);
/*     */         }
/* 741 */         regression.classify(xsJ, conditionalProbs);
/* 742 */         for (int k = 0; k < numOutcomesMinus1; k++) {
/* 743 */           adjustWeightsWithConditionalProbs(weightVectors[k], conditionalProbs[k], learningRate, xsJ, k, csJ);
/*     */         }
/*     */       }
/*     */ 
/* 747 */       reporter.debug("catching up regularizations at end of epoch");
/*     */ 
/* 749 */       int blockRemainder = numTrainingInstances % priorBlockSize;
/*     */ 
/* 751 */       if (blockRemainder == 0)
/* 752 */         blockRemainder = priorBlockSize;
/* 753 */       if (hasPrior) {
/* 754 */         adjustWeightsWithPrior(weightVectors, prior, learningRate * blockRemainder / numTrainingInstances);
/*     */       }
/*     */ 
/* 758 */       if (handler != null) {
/* 759 */         reporter.debug("handling regression for epoch");
/* 760 */         handler.handle(regression);
/*     */       }
/*     */ 
/* 764 */       if (!monitoringConvergence) {
/* 765 */         reporter.info("Unmonitored Epoch=" + epoch);
/*     */       }
/*     */       else
/*     */       {
/* 769 */         reporter.debug("computing log likelihood");
/*     */ 
/* 771 */         double log2Likelihood = log2Likelihood(xs, cs, regression);
/* 772 */         double log2Prior = prior.log2Prior(weightVectors);
/* 773 */         double log2LikelihoodAndPrior = log2Likelihood + prior.log2Prior(weightVectors);
/* 774 */         if (log2LikelihoodAndPrior > bestLog2LikelihoodAndPrior) {
/* 775 */           bestLog2LikelihoodAndPrior = log2LikelihoodAndPrior;
/*     */         }
/* 777 */         if (reporter.isInfoEnabled()) {
/* 778 */           Formatter formatter = null;
/*     */           try {
/* 780 */             formatter = new Formatter(Locale.ENGLISH);
/* 781 */             formatter.format("epoch=%5d lr=%11.9f ll=%11.4f lp=%11.4f llp=%11.4f llp*=%11.4f", new Object[] { Integer.valueOf(epoch), Double.valueOf(learningRate), Double.valueOf(log2Likelihood), Double.valueOf(log2Prior), Double.valueOf(log2LikelihoodAndPrior), Double.valueOf(bestLog2LikelihoodAndPrior) });
/*     */ 
/* 787 */             reporter.info(formatter.toString());
/*     */           } catch (IllegalFormatException e) {
/* 789 */             reporter.warn("Illegal format in Logistic Regression");
/*     */           } finally {
/* 791 */             if (formatter != null) {
/* 792 */               formatter.close();
/*     */             }
/*     */           }
/*     */         }
/* 796 */         boolean acceptUpdate = annealingSchedule.receivedError(epoch, learningRate, -log2LikelihoodAndPrior);
/* 797 */         if (!acceptUpdate) {
/* 798 */           reporter.info("Annealing rejected update at learningRate=" + learningRate + " error=" + -log2LikelihoodAndPrior);
/* 799 */           weightVectors = weightVectorCopies;
/* 800 */           regression = new LogisticRegression(weightVectors);
/*     */         } else {
/* 802 */           double relativeAbsDiff = com.aliasi.util.Math.relativeAbsoluteDifference(lastLog2LikelihoodAndPrior, log2LikelihoodAndPrior);
/*     */ 
/* 807 */           rollingAbsDiffs[rollingAveragePosition] = relativeAbsDiff;
/* 808 */           rollingAveragePosition++; if (rollingAveragePosition == rollingAbsDiffs.length) {
/* 809 */             rollingAveragePosition = 0;
/*     */           }
/* 811 */           double rollingAvgAbsDiff = Statistics.mean(rollingAbsDiffs);
/*     */ 
/* 813 */           reporter.debug("relativeAbsDiff=" + relativeAbsDiff + " rollingAvg=" + rollingAvgAbsDiff);
/*     */ 
/* 816 */           lastLog2LikelihoodAndPrior = log2LikelihoodAndPrior;
/*     */ 
/* 818 */           if (rollingAvgAbsDiff < minImprovement) {
/* 819 */             reporter.info("Converged with Rolling Average Absolute Difference=" + rollingAvgAbsDiff);
/*     */ 
/* 821 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 825 */     return regression;
/*     */   }
/*     */ 
/*     */   public static double log2Likelihood(Vector[] inputs, int[] cats, LogisticRegression regression)
/*     */   {
/* 841 */     if (inputs.length != cats.length) {
/* 842 */       String msg = "Inputs and categories must be same length. Found inputs.length=" + inputs.length + " cats.length=" + cats.length;
/*     */ 
/* 845 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 847 */     int numTrainingInstances = inputs.length;
/* 848 */     double log2Likelihood = 0.0D;
/* 849 */     double[] conditionalProbs = new double[regression.numOutcomes()];
/* 850 */     for (int j = 0; j < numTrainingInstances; j++) {
/* 851 */       regression.classify(inputs[j], conditionalProbs);
/* 852 */       log2Likelihood += com.aliasi.util.Math.log2(conditionalProbs[cats[j]]);
/*     */     }
/* 854 */     return log2Likelihood;
/*     */   }
/*     */ 
/*     */   private static void adjustWeightsWithPrior(DenseVector[] weightVectors, RegressionPrior prior, double learningRate)
/*     */   {
/* 862 */     for (int k = 0; k < weightVectors.length; k++) {
/* 863 */       DenseVector weightVectorsK = weightVectors[k];
/* 864 */       int numDimensions = weightVectorsK.numDimensions();
/* 865 */       for (int i = 0; i < numDimensions; i++) {
/* 866 */         double weight_k_i = weightVectorsK.value(i);
/* 867 */         double priorMode = prior.mode(i);
/* 868 */         if (weight_k_i != priorMode)
/*     */         {
/* 870 */           double priorGradient = prior.gradient(weight_k_i, i);
/* 871 */           double delta = priorGradient * learningRate;
/* 872 */           if (delta != 0.0D)
/*     */           {
/* 874 */             double adjWeight_k_i = weight_k_i - delta;
/*     */ 
/* 876 */             double mode = prior.mode(i);
/* 877 */             if (weight_k_i > mode) {
/* 878 */               if (adjWeight_k_i < mode)
/* 879 */                 adjWeight_k_i = mode;
/* 880 */             } else if (adjWeight_k_i > mode) {
/* 881 */               adjWeight_k_i = mode;
/*     */             }
/* 883 */             weightVectorsK.setValue(i, adjWeight_k_i);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 890 */   private static void adjustWeightsWithConditionalProbs(DenseVector weightVectorsK, double conditionalProb, double learningRate, Vector xsJ, int k, int csJ) { double conditionalProbMinusTruth = k == csJ ? conditionalProb - 1.0D : conditionalProb;
/*     */ 
/* 894 */     if (conditionalProbMinusTruth == 0.0D) return;
/* 895 */     weightVectorsK.increment(-learningRate * conditionalProbMinusTruth, xsJ);
/*     */   }
/*     */ 
/*     */   private static DenseVector[] copy(DenseVector[] xs)
/*     */   {
/* 901 */     DenseVector[] result = new DenseVector[xs.length];
/* 902 */     for (int k = 0; k < xs.length; k++)
/* 903 */       result[k] = new DenseVector(xs[k]);
/* 904 */     return result;
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -2256261505231943102L;
/*     */     final LogisticRegression mRegression;
/*     */ 
/*     */     public Externalizer() {
/* 912 */       this(null);
/*     */     }
/*     */     public Externalizer(LogisticRegression regression) {
/* 915 */       this.mRegression = regression;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 919 */       int numOutcomes = this.mRegression.mWeightVectors.length + 1;
/* 920 */       out.writeInt(numOutcomes);
/* 921 */       int numDimensions = this.mRegression.mWeightVectors[0].numDimensions();
/* 922 */       out.writeInt(numDimensions);
/* 923 */       for (int c = 0; c < numOutcomes - 1; c++) {
/* 924 */         Vector vC = this.mRegression.mWeightVectors[c];
/* 925 */         for (int i = 0; i < numDimensions; i++)
/* 926 */           out.writeDouble(vC.value(i));
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 931 */       int numOutcomes = in.readInt();
/* 932 */       int numDimensions = in.readInt();
/* 933 */       Vector[] weightVectors = new Vector[numOutcomes - 1];
/* 934 */       for (int c = 0; c < weightVectors.length; c++) {
/* 935 */         Vector weightVectorsC = new DenseVector(numDimensions);
/* 936 */         weightVectors[c] = weightVectorsC;
/* 937 */         for (int i = 0; i < numDimensions; i++)
/* 938 */           weightVectorsC.setValue(i, in.readDouble());
/*     */       }
/* 940 */       return new LogisticRegression(weightVectors);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.LogisticRegression
 * JD-Core Version:    0.6.2
 */