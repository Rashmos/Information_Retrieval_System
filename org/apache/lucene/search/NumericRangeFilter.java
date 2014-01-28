/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ public final class NumericRangeFilter<T extends Number> extends MultiTermQueryWrapperFilter<NumericRangeQuery<T>>
/*     */ {
/*     */   private NumericRangeFilter(NumericRangeQuery<T> query)
/*     */   {
/*  51 */     super(query);
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Long> newLongRange(String field, int precisionStep, Long min, Long max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/*  64 */     return new NumericRangeFilter(NumericRangeQuery.newLongRange(field, precisionStep, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Long> newLongRange(String field, Long min, Long max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/*  79 */     return new NumericRangeFilter(NumericRangeQuery.newLongRange(field, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Integer> newIntRange(String field, int precisionStep, Integer min, Integer max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/*  94 */     return new NumericRangeFilter(NumericRangeQuery.newIntRange(field, precisionStep, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Integer> newIntRange(String field, Integer min, Integer max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/* 109 */     return new NumericRangeFilter(NumericRangeQuery.newIntRange(field, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Double> newDoubleRange(String field, int precisionStep, Double min, Double max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/* 124 */     return new NumericRangeFilter(NumericRangeQuery.newDoubleRange(field, precisionStep, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Double> newDoubleRange(String field, Double min, Double max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/* 139 */     return new NumericRangeFilter(NumericRangeQuery.newDoubleRange(field, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Float> newFloatRange(String field, int precisionStep, Float min, Float max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/* 154 */     return new NumericRangeFilter(NumericRangeQuery.newFloatRange(field, precisionStep, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public static NumericRangeFilter<Float> newFloatRange(String field, Float min, Float max, boolean minInclusive, boolean maxInclusive)
/*     */   {
/* 169 */     return new NumericRangeFilter(NumericRangeQuery.newFloatRange(field, min, max, minInclusive, maxInclusive));
/*     */   }
/*     */ 
/*     */   public String getField()
/*     */   {
/* 175 */     return ((NumericRangeQuery)this.query).getField();
/*     */   }
/*     */   public boolean includesMin() {
/* 178 */     return ((NumericRangeQuery)this.query).includesMin();
/*     */   }
/*     */   public boolean includesMax() {
/* 181 */     return ((NumericRangeQuery)this.query).includesMax();
/*     */   }
/*     */   public T getMin() {
/* 184 */     return ((NumericRangeQuery)this.query).getMin();
/*     */   }
/*     */   public T getMax() {
/* 187 */     return ((NumericRangeQuery)this.query).getMax();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.NumericRangeFilter
 * JD-Core Version:    0.6.2
 */