/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import org.apache.lucene.search.Explanation;
/*     */ 
/*     */ public abstract class DocValues
/*     */ {
/* 117 */   private float minVal = (0.0F / 0.0F);
/* 118 */   private float maxVal = (0.0F / 0.0F);
/* 119 */   private float avgVal = (0.0F / 0.0F);
/* 120 */   private boolean computed = false;
/*     */ 
/*     */   public abstract float floatVal(int paramInt);
/*     */ 
/*     */   public int intVal(int doc)
/*     */   {
/*  58 */     return (int)floatVal(doc);
/*     */   }
/*     */ 
/*     */   public long longVal(int doc)
/*     */   {
/*  67 */     return ()floatVal(doc);
/*     */   }
/*     */ 
/*     */   public double doubleVal(int doc)
/*     */   {
/*  76 */     return floatVal(doc);
/*     */   }
/*     */ 
/*     */   public String strVal(int doc)
/*     */   {
/*  85 */     return Float.toString(floatVal(doc));
/*     */   }
/*     */ 
/*     */   public abstract String toString(int paramInt);
/*     */ 
/*     */   public Explanation explain(int doc)
/*     */   {
/*  97 */     return new Explanation(floatVal(doc), toString(doc));
/*     */   }
/*     */ 
/*     */   Object getInnerArray()
/*     */   {
/* 113 */     throw new UnsupportedOperationException("this optional method is for test purposes only");
/*     */   }
/*     */ 
/*     */   private void compute()
/*     */   {
/* 123 */     if (this.computed) {
/* 124 */       return;
/*     */     }
/* 126 */     float sum = 0.0F;
/* 127 */     int n = 0;
/*     */     while (true) {
/*     */       float val;
/*     */       try {
/* 131 */         val = floatVal(n);
/*     */       } catch (ArrayIndexOutOfBoundsException e) {
/* 133 */         break;
/*     */       }
/* 135 */       sum += val;
/* 136 */       this.minVal = (Float.isNaN(this.minVal) ? val : Math.min(this.minVal, val));
/* 137 */       this.maxVal = (Float.isNaN(this.maxVal) ? val : Math.max(this.maxVal, val));
/* 138 */       n++;
/*     */     }
/*     */ 
/* 141 */     this.avgVal = (n == 0 ? (0.0F / 0.0F) : sum / n);
/* 142 */     this.computed = true;
/*     */   }
/*     */ 
/*     */   public float getMinValue()
/*     */   {
/* 156 */     compute();
/* 157 */     return this.minVal;
/*     */   }
/*     */ 
/*     */   public float getMaxValue()
/*     */   {
/* 171 */     compute();
/* 172 */     return this.maxVal;
/*     */   }
/*     */ 
/*     */   public float getAverageValue()
/*     */   {
/* 186 */     compute();
/* 187 */     return this.avgVal;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.DocValues
 * JD-Core Version:    0.6.2
 */