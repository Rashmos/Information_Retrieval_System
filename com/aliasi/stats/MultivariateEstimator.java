/*     */ package com.aliasi.stats;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MultivariateEstimator extends MultivariateDistribution
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 1171641384366463097L;
/*     */   final Map<String, Integer> mLabelToIndex;
/*     */   final List<String> mIndexToLabel;
/*     */   final List<Long> mIndexToCount;
/*  68 */   long mTotalCount = 0L;
/*  69 */   int mNextIndex = 0;
/*     */ 
/* 298 */   static final Long[] EMPTY_LONG_ARRAY = new Long[0];
/*     */ 
/*     */   public MultivariateEstimator()
/*     */   {
/*  76 */     this(new HashMap(), new ArrayList(), new ArrayList());
/*     */   }
/*     */ 
/*     */   private MultivariateEstimator(Map<String, Integer> labelToIndex, List<String> indexToLabel, List<Long> indexToCount)
/*     */   {
/*  84 */     this.mLabelToIndex = labelToIndex;
/*  85 */     this.mIndexToLabel = indexToLabel;
/*  86 */     this.mIndexToCount = indexToCount;
/*     */   }
/*     */ 
/*     */   static void checkLongAddInRange(long a, long b) {
/*  90 */     if (9223372036854775807L - b < a) {
/*  91 */       String msg = "Long addition overflow. a=" + a + " b=" + b;
/*     */ 
/*  94 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resetCount(String outcomeLabel)
/*     */   {
/* 108 */     Integer index = (Integer)this.mLabelToIndex.get(outcomeLabel);
/* 109 */     if (index == null) {
/* 110 */       String msg = "May only reset known outcomes. Found outcome=" + outcomeLabel;
/*     */ 
/* 112 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 114 */     long currentCount = ((Long)this.mIndexToCount.get(index.intValue())).longValue();
/*     */ 
/* 116 */     this.mTotalCount -= currentCount;
/* 117 */     this.mIndexToCount.set(index.intValue(), Long.valueOf(0L));
/*     */   }
/*     */ 
/*     */   public void train(String outcomeLabel, long increment)
/*     */   {
/* 132 */     if (increment < 1L) {
/* 133 */       String msg = "Increment must be positive. Found increment=" + increment;
/*     */ 
/* 135 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 137 */     this.mTotalCount += increment;
/* 138 */     Integer indexInteger = (Integer)this.mLabelToIndex.get(outcomeLabel);
/*     */ 
/* 140 */     if (indexInteger == null) {
/* 141 */       int index = this.mNextIndex++;
/* 142 */       this.mLabelToIndex.put(outcomeLabel, Integer.valueOf(index));
/* 143 */       this.mIndexToLabel.add(index, outcomeLabel);
/* 144 */       this.mIndexToCount.add(index, Long.valueOf(increment));
/* 145 */       return;
/*     */     }
/* 147 */     int index = indexInteger.intValue();
/* 148 */     long currentCount = ((Long)this.mIndexToCount.get(index)).longValue();
/* 149 */     checkLongAddInRange(currentCount, increment);
/* 150 */     this.mIndexToCount.set(index, Long.valueOf(currentCount + increment));
/*     */   }
/*     */ 
/*     */   public long outcome(String outcomeLabel)
/*     */   {
/* 161 */     Integer outcome = (Integer)this.mLabelToIndex.get(outcomeLabel);
/* 162 */     return outcome == null ? -1L : outcome.longValue();
/*     */   }
/*     */ 
/*     */   public String label(long outcome)
/*     */   {
/* 173 */     if ((outcome < 0L) || (outcome >= this.mNextIndex)) {
/* 174 */       String msg = "Outcome must be between 0 and max. Max outcome=" + maxOutcome() + " Argument outcome=" + outcome;
/*     */ 
/* 177 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 179 */     return (String)this.mIndexToLabel.get((int)outcome);
/*     */   }
/*     */ 
/*     */   public int numDimensions()
/*     */   {
/* 191 */     return this.mIndexToLabel.size();
/*     */   }
/*     */ 
/*     */   public double probability(long outcome)
/*     */   {
/* 203 */     if ((outcome < minOutcome()) || (outcome > maxOutcome()))
/* 204 */       return 0.0D;
/* 205 */     return getCount(outcome) / trainingSampleCount();
/*     */   }
/*     */ 
/*     */   public long getCount(long outcome)
/*     */   {
/* 218 */     checkOutcome(outcome);
/* 219 */     Long count = (Long)this.mIndexToCount.get((int)outcome);
/* 220 */     return count == null ? 0L : count.longValue();
/*     */   }
/*     */ 
/*     */   public long getCount(String outcomeLabel)
/*     */   {
/* 231 */     Integer index = (Integer)this.mLabelToIndex.get(outcomeLabel);
/* 232 */     if (index == null) {
/* 233 */       String msg = "May only count known outcomes by label. Found outcome=" + outcomeLabel;
/*     */ 
/* 235 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 237 */     return getCount(index.longValue());
/*     */   }
/*     */ 
/*     */   public long trainingSampleCount()
/*     */   {
/* 248 */     return this.mTotalCount;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 263 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     private static final long serialVersionUID = 2913496935213914118L;
/*     */     final MultivariateEstimator mEstimator;
/*     */ 
/* 270 */     public Externalizer() { this.mEstimator = null; } 
/*     */     public Externalizer(MultivariateEstimator estimator) {
/* 272 */       this.mEstimator = estimator;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out)
/*     */       throws IOException
/*     */     {
/* 278 */       String[] labels = (String[])this.mEstimator.mIndexToLabel.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 280 */       out.writeObject(labels);
/* 281 */       Long[] counts = (Long[])this.mEstimator.mIndexToCount.toArray(MultivariateEstimator.EMPTY_LONG_ARRAY);
/* 282 */       double totalCount = this.mEstimator.mTotalCount;
/* 283 */       double[] ratios = new double[counts.length];
/* 284 */       for (int i = 0; i < ratios.length; i++)
/* 285 */         ratios[i] = (counts[i].doubleValue() / totalCount);
/* 286 */       out.writeObject(ratios);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws ClassNotFoundException, IOException
/*     */     {
/* 291 */       String[] labels = (String[])in.readObject();
/* 292 */       double[] ratios = (double[])in.readObject();
/* 293 */       return new MultivariateConstant(ratios, labels);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.MultivariateEstimator
 * JD-Core Version:    0.6.2
 */