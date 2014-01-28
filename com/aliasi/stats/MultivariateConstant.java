/*     */ package com.aliasi.stats;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MultivariateConstant extends MultivariateDistribution
/*     */ {
/*     */   final String[] mLabels;
/*     */   final double[] mEstimates;
/*     */   final Map<String, Long> mLabelToIndex;
/*     */ 
/*     */   public MultivariateConstant(int numOutcomes)
/*     */   {
/*  50 */     this(allOnes(numOutcomes));
/*     */   }
/*     */ 
/*     */   public MultivariateConstant(long[] counts)
/*     */   {
/*  64 */     this(toDouble(counts));
/*     */   }
/*     */ 
/*     */   public MultivariateConstant(double[] probabilityRatios)
/*     */   {
/*  78 */     this(probabilityRatios, null, null);
/*     */   }
/*     */ 
/*     */   public MultivariateConstant(String[] labels)
/*     */   {
/*  92 */     this(allOnes(labels.length), labels);
/*     */   }
/*     */ 
/*     */   public MultivariateConstant(long[] counts, String[] labels)
/*     */   {
/* 110 */     this(toDouble(counts), labels);
/*     */   }
/*     */ 
/*     */   public MultivariateConstant(double[] probabilityRatios, String[] labels)
/*     */   {
/* 125 */     this(probabilityRatios, (String[])labels.clone(), new HashMap());
/* 126 */     validateEqualLengths(probabilityRatios, labels);
/* 127 */     for (int i = 0; i < labels.length; i++)
/* 128 */       if (this.mLabelToIndex.put(labels[i], Long.valueOf(i)) != null) {
/* 129 */         String msg = "Duplicate labels=" + labels[i];
/* 130 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */   }
/*     */ 
/*     */   private MultivariateConstant(double[] probabilityRatios, String[] labels, Map<String, Long> labelToIndex)
/*     */   {
/* 138 */     if (probabilityRatios.length < 1) {
/* 139 */       String msg = "Require at least one count or probability ratio.";
/* 140 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 142 */     this.mLabels = labels;
/* 143 */     this.mLabelToIndex = labelToIndex;
/* 144 */     validateRatios(probabilityRatios);
/* 145 */     double z = 0.0D;
/* 146 */     for (int i = 0; i < probabilityRatios.length; i++)
/* 147 */       z += probabilityRatios[i];
/* 148 */     this.mEstimates = new double[probabilityRatios.length];
/* 149 */     for (int i = 0; i < probabilityRatios.length; i++)
/* 150 */       this.mEstimates[i] = (probabilityRatios[i] / z);
/*     */   }
/*     */ 
/*     */   public int numDimensions()
/*     */   {
/* 155 */     return this.mEstimates.length;
/*     */   }
/*     */ 
/*     */   public long outcome(String outcomeLabel)
/*     */   {
/* 160 */     if (this.mLabels == null) return super.outcome(outcomeLabel);
/* 161 */     Long result = (Long)this.mLabelToIndex.get(outcomeLabel);
/* 162 */     if (result == null) {
/* 163 */       String msg = "Unknown outcome label=" + outcomeLabel;
/* 164 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 166 */     return result.longValue();
/*     */   }
/*     */ 
/*     */   public String label(long outcome)
/*     */   {
/* 171 */     if (this.mLabels == null) return super.label(outcome);
/* 172 */     checkOutcome(outcome);
/* 173 */     return this.mLabels[((int)outcome)];
/*     */   }
/*     */ 
/*     */   public double probability(long outcome)
/*     */   {
/* 178 */     return outcomeOutOfRange(outcome) ? 0.0D : this.mEstimates[((int)outcome)];
/*     */   }
/*     */ 
/*     */   private void validateEqualLengths(double[] probabilityRatios, String[] labels)
/*     */   {
/* 184 */     if (probabilityRatios.length != labels.length) {
/* 185 */       String msg = "Require same number of ratios and labels. Found #ratios=" + probabilityRatios.length + " # labels=" + labels.length;
/*     */ 
/* 188 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/* 192 */   private void validateRatios(double[] probabilityRatios) { for (int i = 0; i < probabilityRatios.length; i++)
/* 193 */       if (probabilityRatios[i] < 0.0D) {
/* 194 */         String msg = "All probability ratios must be >= 0. Found probabilityRatios[" + i + "]=" + probabilityRatios[i];
/*     */ 
/* 197 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */   }
/*     */ 
/*     */   static double[] toDouble(long[] counts)
/*     */   {
/* 203 */     double[] result = new double[counts.length];
/* 204 */     for (int i = 0; i < counts.length; i++) {
/* 205 */       if (counts[i] < 0L) {
/* 206 */         String msg = "All counts must be positive. Found counts[" + i + "]=" + counts[i];
/*     */ 
/* 208 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 210 */       result[i] = counts[i];
/*     */     }
/* 212 */     return result;
/*     */   }
/*     */ 
/*     */   static double[] allOnes(int length) {
/* 216 */     if (length < 0) {
/* 217 */       String msg = "Number of outcomes must be positive.Found number of outcomes=" + length;
/*     */ 
/* 219 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 221 */     double[] result = new double[length];
/* 222 */     Arrays.fill(result, 1.0D);
/* 223 */     return result;
/*     */   }
/*     */ 
/*     */   static String[] numberedLabels(int length) {
/* 227 */     String[] labels = new String[length];
/* 228 */     for (int i = 0; i < length; i++)
/* 229 */       labels[i] = Integer.toString(i);
/* 230 */     return labels;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.MultivariateConstant
 * JD-Core Version:    0.6.2
 */