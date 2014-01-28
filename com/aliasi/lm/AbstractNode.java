/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import java.util.List;
/*     */ 
/*     */ abstract class AbstractNode
/*     */   implements Node
/*     */ {
/*     */   public abstract void topNGramsDtrs(NBestCounter paramNBestCounter, char[] paramArrayOfChar, int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void countNodeTypes(ObjectToCounterMap<String> paramObjectToCounterMap);
/*     */ 
/*     */   public abstract long dtrUniqueNGramCount(int paramInt);
/*     */ 
/*     */   public abstract long dtrTotalNGramCount(int paramInt);
/*     */ 
/*     */   public abstract void addDtrCounts(List<Long> paramList, int paramInt);
/*     */ 
/*     */   public abstract void addDtrNGramCounts(long[][] paramArrayOfLong, int paramInt);
/*     */ 
/*     */   public void addNGramCounts(long[][] uniqueTotalCounts, int depth)
/*     */   {
/*  71 */     uniqueTotalCounts[depth][0] += 1L;
/*  72 */     uniqueTotalCounts[depth][1] += count();
/*  73 */     addDtrNGramCounts(uniqueTotalCounts, depth + 1);
/*     */   }
/*     */ 
/*     */   public void topNGrams(NBestCounter counter, char[] csAccum, int level, int dtrLevel) {
/*  77 */     if (dtrLevel == 0)
/*  78 */       counter.put(csAccum, level, count());
/*     */     else
/*  80 */       topNGramsDtrs(counter, csAccum, level, dtrLevel); 
/*     */   }
/*     */ 
/*  83 */   public void addCounts(List<Long> counts, int dtrLevel) { if (dtrLevel == 0) {
/*  84 */       counts.add(Long.valueOf(count()));
/*  85 */       return;
/*     */     }
/*  87 */     addDtrCounts(counts, dtrLevel - 1); }
/*     */ 
/*     */   public long uniqueNGramCount(int dtrLevel) {
/*  90 */     if (dtrLevel == 0) return 1L;
/*  91 */     return dtrUniqueNGramCount(dtrLevel - 1);
/*     */   }
/*     */   public long totalNGramCount(int dtrLevel) {
/*  94 */     if (dtrLevel == 0) return count();
/*  95 */     return dtrTotalNGramCount(dtrLevel - 1);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  99 */     StringBuilder sb = new StringBuilder();
/* 100 */     toString(sb, 0);
/* 101 */     return sb.toString();
/*     */   }
/*     */   static void indent(StringBuilder sb, int depth) {
/* 104 */     sb.append('\n');
/* 105 */     for (int i = 0; i < depth; i++)
/* 106 */       sb.append("  ");
/*     */   }
/*     */ 
/*     */   protected static void toString(StringBuilder sb, char c, Node daughter, int depth) {
/* 110 */     indent(sb, depth);
/* 111 */     sb.append(c);
/* 112 */     daughter.toString(sb, depth + 1);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.AbstractNode
 * JD-Core Version:    0.6.2
 */