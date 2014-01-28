/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class BoundedFeatureExtractor<E> extends ModifiedFeatureExtractor<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -5628628360712035433L;
/*     */   private final double mMinValue;
/*     */   private final double mMaxValue;
/*     */   private final Number mMinValueNumber;
/*     */   private final Number mMaxValueNumber;
/*     */ 
/*     */   public BoundedFeatureExtractor(FeatureExtractor<? super E> extractor, double minValue, double maxValue)
/*     */   {
/*  70 */     super(extractor);
/*  71 */     if (minValue > maxValue) {
/*  72 */       String msg = "Require minValue <= maxValue. Found  minValue=" + minValue + " maxValue=" + maxValue;
/*     */ 
/*  75 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  77 */     this.mMinValue = minValue;
/*  78 */     this.mMaxValue = maxValue;
/*  79 */     this.mMinValueNumber = Double.valueOf(minValue);
/*  80 */     this.mMaxValueNumber = Double.valueOf(maxValue);
/*     */   }
/*     */ 
/*     */   public Number filter(String feature, Number value)
/*     */   {
/*  92 */     double v = value.doubleValue();
/*  93 */     if (v < this.mMinValue)
/*  94 */       return this.mMinValueNumber;
/*  95 */     if (v > this.mMaxValue)
/*  96 */       return this.mMaxValueNumber;
/*  97 */     return value;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 101 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 6365515337527915147L;
/*     */     private final BoundedFeatureExtractor<F> mBFExtractor;
/*     */ 
/* 108 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(BoundedFeatureExtractor<F> extractor) {
/* 111 */       this.mBFExtractor = extractor;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 115 */       out.writeDouble(this.mBFExtractor.mMinValue);
/* 116 */       out.writeDouble(this.mBFExtractor.mMaxValue);
/* 117 */       out.writeObject(this.mBFExtractor.baseExtractor());
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 123 */       double minValue = in.readDouble();
/* 124 */       double maxValue = in.readDouble();
/*     */ 
/* 127 */       FeatureExtractor extractor = (FeatureExtractor)in.readObject();
/*     */ 
/* 129 */       return new BoundedFeatureExtractor(extractor, minValue, maxValue);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.BoundedFeatureExtractor
 * JD-Core Version:    0.6.2
 */