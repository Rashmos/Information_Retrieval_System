/*     */ package com.aliasi.stats;
/*     */ 
/*     */ public abstract class AbstractDiscreteDistribution
/*     */   implements DiscreteDistribution
/*     */ {
/*     */   public abstract double probability(long paramLong);
/*     */ 
/*     */   public double cumulativeProbabilityLess(long upperBound)
/*     */   {
/*  70 */     return cumulativeProbability(-9223372036854775808L, upperBound);
/*     */   }
/*     */ 
/*     */   public double cumulativeProbabilityGreater(long lowerBound)
/*     */   {
/*  84 */     return cumulativeProbability(lowerBound, 9223372036854775807L);
/*     */   }
/*     */ 
/*     */   public double cumulativeProbability(long lowerBound, long upperBound)
/*     */   {
/*  99 */     double sum = 0.0D;
/* 100 */     long start = java.lang.Math.max(lowerBound, minOutcome());
/* 101 */     long last = java.lang.Math.min(upperBound, maxOutcome());
/* 102 */     for (long i = start; i <= last; i += 1L)
/* 103 */       sum += probability(i);
/* 104 */     return sum;
/*     */   }
/*     */ 
/*     */   public double log2Probability(long outcome)
/*     */   {
/* 115 */     return com.aliasi.util.Math.log2(probability(outcome));
/*     */   }
/*     */ 
/*     */   public long minOutcome()
/*     */   {
/* 127 */     return -9223372036854775808L;
/*     */   }
/*     */ 
/*     */   public long maxOutcome()
/*     */   {
/* 139 */     return 9223372036854775807L;
/*     */   }
/*     */ 
/*     */   public double mean()
/*     */   {
/* 150 */     double mean = 0.0D;
/* 151 */     long maxOutcome = maxOutcome();
/* 152 */     for (long i = minOutcome(); i <= maxOutcome; i += 1L)
/* 153 */       mean += i * probability(i);
/* 154 */     return mean;
/*     */   }
/*     */ 
/*     */   public double variance()
/*     */   {
/* 167 */     double mean = mean();
/* 168 */     double variance = 0.0D;
/* 169 */     long maxOutcome = maxOutcome();
/* 170 */     for (long i = minOutcome(); i <= maxOutcome; i += 1L) {
/* 171 */       double diff = mean - i;
/* 172 */       variance += probability(i) * diff * diff;
/*     */     }
/* 174 */     return variance;
/*     */   }
/*     */ 
/*     */   public double entropy()
/*     */   {
/* 193 */     double sum = 0.0D;
/* 194 */     long maxOutcome = maxOutcome();
/* 195 */     for (long i = minOutcome(); i <= maxOutcome; i += 1L) {
/* 196 */       sum += probability(i) * log2Probability(i);
/*     */     }
/* 198 */     return -sum;
/*     */   }
/*     */ 
/*     */   void checkOutcome(long outcome)
/*     */   {
/* 203 */     if (outcomeOutOfRange(outcome)) {
/* 204 */       String msg = "Outcome must be in range.  Minimum = 0. Maximum=" + maxOutcome() + " Found outcome=" + outcome;
/*     */ 
/* 207 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean outcomeOutOfRange(long outcome) {
/* 212 */     return (outcome < 0L) || (outcome > maxOutcome());
/*     */   }
/*     */ 
/*     */   void validateProbability(double p)
/*     */   {
/* 217 */     if ((p >= 0.0D) && (p <= 1.0D)) return;
/* 218 */     String msg = "Probabilities must be between 0 and 1 inclusive. Found probability=" + p;
/*     */ 
/* 220 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.AbstractDiscreteDistribution
 * JD-Core Version:    0.6.2
 */