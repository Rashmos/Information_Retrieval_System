/*     */ package com.aliasi.crf;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class ChainCrfFeatures<E>
/*     */ {
/*     */   private final List<E> mTokens;
/*     */   private final List<String> mTags;
/*     */ 
/*     */   public ChainCrfFeatures(List<E> tokens, List<String> tags)
/*     */   {
/*  63 */     this.mTokens = tokens;
/*  64 */     this.mTags = tags;
/*     */   }
/*     */ 
/*     */   public int numTokens()
/*     */   {
/*  73 */     return this.mTokens.size();
/*     */   }
/*     */ 
/*     */   public E token(int n)
/*     */   {
/*  86 */     return this.mTokens.get(n);
/*     */   }
/*     */ 
/*     */   public int numTags()
/*     */   {
/*  96 */     return this.mTags.size();
/*     */   }
/*     */ 
/*     */   public String tag(int k)
/*     */   {
/* 107 */     return (String)this.mTags.get(k);
/*     */   }
/*     */ 
/*     */   public abstract Map<String, ? extends Number> nodeFeatures(int paramInt);
/*     */ 
/*     */   public abstract Map<String, ? extends Number> edgeFeatures(int paramInt1, int paramInt2);
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.crf.ChainCrfFeatures
 * JD-Core Version:    0.6.2
 */