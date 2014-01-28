/*     */ package com.aliasi.tag;
/*     */ 
/*     */ import com.aliasi.classify.ConditionalClassification;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class TagLattice<E>
/*     */ {
/*     */   public abstract List<E> tokenList();
/*     */ 
/*     */   public abstract List<String> tagList();
/*     */ 
/*     */   public abstract String tag(int paramInt);
/*     */ 
/*     */   public abstract int numTags();
/*     */ 
/*     */   public abstract E token(int paramInt);
/*     */ 
/*     */   public abstract int numTokens();
/*     */ 
/*     */   public abstract SymbolTable tagSymbolTable();
/*     */ 
/*     */   public abstract double logProbability(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract double logProbability(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public abstract double logProbability(int paramInt, int[] paramArrayOfInt);
/*     */ 
/*     */   public abstract double logForward(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract double logBackward(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract double logTransition(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public abstract double logZ();
/*     */ 
/*     */   public ConditionalClassification tokenClassification(int tokenIndex)
/*     */   {
/* 364 */     String[] tags = (String[])tagList().toArray(Strings.EMPTY_STRING_ARRAY);
/* 365 */     double[] logProbs = new double[tags.length];
/* 366 */     for (int tagId = 0; tagId < tags.length; tagId++) {
/* 367 */       double logProbTagId = logProbability(tokenIndex, tagId);
/* 368 */       logProbs[tagId] = (logProbTagId > 0.0D ? 0.0D : (Double.isNaN(logProbTagId)) || (Double.isInfinite(logProbTagId)) ? -500.0D : logProbTagId);
/*     */     }
/*     */ 
/* 375 */     ConditionalClassification classification = ConditionalClassification.createLogProbs(tags, logProbs);
/*     */ 
/* 377 */     return classification;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.TagLattice
 * JD-Core Version:    0.6.2
 */