/*    */ package com.aliasi.features;
/*    */ 
/*    */ import com.aliasi.util.FeatureExtractor;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public abstract class ModifiedFeatureExtractor<E> extends FeatureExtractorFilter<E>
/*    */   implements FeatureExtractor<E>
/*    */ {
/*    */   public ModifiedFeatureExtractor(FeatureExtractor<? super E> extractor)
/*    */   {
/* 44 */     super(extractor);
/*    */   }
/*    */ 
/*    */   public Map<String, ? extends Number> features(E in)
/*    */   {
/* 58 */     Map featureMap = baseExtractor().features(in);
/* 59 */     Map result = new HashMap();
/* 60 */     for (Map.Entry entry : featureMap.entrySet()) {
/* 61 */       String feature = (String)entry.getKey();
/* 62 */       Number originalValue = (Number)entry.getValue();
/* 63 */       Number value = filter(feature, originalValue);
/* 64 */       if (value != null)
/* 65 */         result.put(feature, value);
/*    */     }
/* 67 */     return result;
/*    */   }
/*    */ 
/*    */   public Number filter(String feature, Number value)
/*    */   {
/* 81 */     return value;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.ModifiedFeatureExtractor
 * JD-Core Version:    0.6.2
 */