/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.matrix.SparseFloatVector;
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class Features
/*     */ {
/*     */   public static Vector toVectorAddSymbols(Map<String, ? extends Number> featureVector, SymbolTable table, int numDimensions, boolean addIntercept)
/*     */   {
/*  64 */     int size = featureVector.size() * 3 / 2;
/*  65 */     Map vectorMap = new HashMap(size);
/*     */ 
/*  68 */     for (Map.Entry entry : featureVector.entrySet()) {
/*  69 */       String feature = (String)entry.getKey();
/*  70 */       Number val = (Number)entry.getValue();
/*  71 */       int id = table.getOrAddSymbol(feature);
/*  72 */       vectorMap.put(Integer.valueOf(id), val);
/*     */     }
/*  74 */     if (addIntercept)
/*  75 */       vectorMap.put(Integer.valueOf(0), Double.valueOf(1.0D));
/*  76 */     return new SparseFloatVector(vectorMap, numDimensions);
/*     */   }
/*     */ 
/*     */   public static Vector toVector(Map<String, ? extends Number> featureVector, SymbolTable table, int numDimensions, boolean addIntercept)
/*     */   {
/* 102 */     int size = featureVector.size() * 3 / 2;
/* 103 */     Map vectorMap = new HashMap(size);
/*     */ 
/* 106 */     for (Map.Entry entry : featureVector.entrySet()) {
/* 107 */       String feature = (String)entry.getKey();
/* 108 */       int id = table.symbolToID(feature);
/* 109 */       if (id >= 0)
/*     */       {
/* 111 */         Number val = (Number)entry.getValue();
/* 112 */         vectorMap.put(Integer.valueOf(id), val);
/*     */       }
/*     */     }
/* 114 */     if (addIntercept)
/* 115 */       vectorMap.put(Integer.valueOf(0), Double.valueOf(1.0D));
/* 116 */     return new SparseFloatVector(vectorMap, numDimensions);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.Features
 * JD-Core Version:    0.6.2
 */