/*    */ package com.aliasi.features;
/*    */ 
/*    */ import com.aliasi.util.FeatureExtractor;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class FeatureExtractorFilter<E>
/*    */   implements FeatureExtractor<E>
/*    */ {
/*    */   final FeatureExtractor<? super E> mExtractor;
/*    */ 
/*    */   public FeatureExtractorFilter(FeatureExtractor<? super E> extractor)
/*    */   {
/* 44 */     this.mExtractor = extractor;
/*    */   }
/*    */ 
/*    */   public Map<String, ? extends Number> features(E in)
/*    */   {
/* 56 */     return this.mExtractor.features(in);
/*    */   }
/*    */ 
/*    */   public FeatureExtractor<? super E> baseExtractor()
/*    */   {
/* 65 */     return this.mExtractor;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.FeatureExtractorFilter
 * JD-Core Version:    0.6.2
 */