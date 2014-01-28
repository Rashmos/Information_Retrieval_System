/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.util.Pair;
/*     */ 
/*     */ public class JointClassification extends ConditionalClassification
/*     */ {
/*     */   private final double[] mLogJointProbs;
/*     */ 
/*     */   public JointClassification(String[] categories, double[] log2JointProbs)
/*     */   {
/*  91 */     this(categories, log2JointProbs, log2JointProbs);
/*     */   }
/*     */ 
/*     */   JointClassification(String[] categories, double[] condProbs, double[] log2JointProbs, double tolerance)
/*     */   {
/*  98 */     super(categories, condProbs, tolerance);
/*     */ 
/* 101 */     this.mLogJointProbs = log2JointProbs;
/*     */   }
/*     */ 
/*     */   public JointClassification(String[] categories, double[] scores, double[] log2JointProbs)
/*     */   {
/* 128 */     super(categories, scores, logJointToConditional(log2JointProbs), (1.0D / 0.0D));
/*     */ 
/* 132 */     this.mLogJointProbs = log2JointProbs;
/*     */   }
/*     */ 
/*     */   public double jointLog2Probability(int rank)
/*     */   {
/* 145 */     return rank >= this.mLogJointProbs.length ? (-1.0D / 0.0D) : this.mLogJointProbs[rank];
/*     */   }
/*     */ 
/*     */   public double score(int rank)
/*     */   {
/* 168 */     return super.score(rank);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 179 */     StringBuilder sb = new StringBuilder();
/* 180 */     sb.append("Rank  Category  Score  P(Category|Input)   log2 P(Category,Input)\n");
/* 181 */     for (int i = 0; i < size(); i++) {
/* 182 */       sb.append(i + "=" + category(i) + " " + score(i) + " " + conditionalProbability(i) + " " + jointLog2Probability(i) + '\n');
/*     */     }
/*     */ 
/* 185 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static JointClassification create(String[] categories, double[] logProbabilities)
/*     */   {
/* 208 */     verifyLengths(categories, logProbabilities);
/* 209 */     verifyLogProbs(logProbabilities);
/* 210 */     Pair catsProbs = sort(categories, logProbabilities);
/* 211 */     return new JointClassification((String[])catsProbs.a(), (double[])catsProbs.b());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.JointClassification
 * JD-Core Version:    0.6.2
 */