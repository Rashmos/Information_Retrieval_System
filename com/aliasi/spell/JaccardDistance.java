/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class JaccardDistance extends TokenizedDistance
/*     */ {
/*     */   public JaccardDistance(TokenizerFactory factory)
/*     */   {
/*  70 */     super(factory);
/*     */   }
/*     */ 
/*     */   public double distance(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/*  82 */     return 1.0D - proximity(cSeq1, cSeq2);
/*     */   }
/*     */ 
/*     */   public double proximity(CharSequence cSeq1, CharSequence cSeq2)
/*     */   {
/*  94 */     Set s1 = tokenSet(cSeq1);
/*  95 */     Set s2 = tokenSet(cSeq2);
/*  96 */     if (s1.size() < s2.size()) {
/*  97 */       Set temp = s2;
/*  98 */       s2 = s1;
/*  99 */       s1 = temp;
/*     */     }
/* 101 */     int numMatch = 0;
/* 102 */     for (String x : s1)
/* 103 */       if (s2.contains(x))
/* 104 */         numMatch++;
/* 105 */     int numTotal = s1.size() + s2.size() - numMatch;
/* 106 */     return numMatch / numTotal;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.JaccardDistance
 * JD-Core Version:    0.6.2
 */