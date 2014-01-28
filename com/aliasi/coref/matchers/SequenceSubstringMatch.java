/*     */ package com.aliasi.coref.matchers;
/*     */ 
/*     */ import com.aliasi.coref.BooleanMatcherAdapter;
/*     */ import com.aliasi.coref.Mention;
/*     */ import com.aliasi.coref.MentionChain;
/*     */ 
/*     */ public final class SequenceSubstringMatch extends BooleanMatcherAdapter
/*     */ {
/*     */   public SequenceSubstringMatch(int score)
/*     */   {
/*  48 */     super(score);
/*     */   }
/*     */ 
/*     */   public boolean matchBoolean(Mention mention, MentionChain chain)
/*     */   {
/*  63 */     if (mention.isPronominal()) return false;
/*  64 */     String[] mentionTokens = mention.normalTokens();
/*  65 */     for (Mention chainMention : chain.mentions()) {
/*  66 */       String[] chainMentionTokens = chainMention.normalTokens();
/*  67 */       if (withinEditDistance(mentionTokens, chainMentionTokens))
/*  68 */         return true;
/*     */     }
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   private static int threshold(String[] tokens1, String[] tokens2)
/*     */   {
/*  85 */     int max = Math.max(tokens1.length, tokens2.length);
/*  86 */     switch (max) { case 1:
/*  87 */       return 0;
/*     */     case 2:
/*  88 */       return 1;
/*     */     case 3:
/*  89 */       return 1;
/*     */     case 4:
/*  90 */       return 1; }
/*  91 */     return (max + 1) / 3;
/*     */   }
/*     */ 
/*     */   public boolean withinEditDistance(String[] tokens1, String[] tokens2)
/*     */   {
/* 105 */     return withinEditDistance(tokens1, tokens2, threshold(tokens1, tokens2));
/*     */   }
/*     */ 
/*     */   public boolean withinEditDistance(String[] tokens1, String[] tokens2, int maximumDistance)
/*     */   {
/* 133 */     int[][] distances = new int[tokens2.length + 1][tokens1.length + 1];
/*     */ 
/* 135 */     distances[0][0] = 0;
/* 136 */     for (int i = 1; i <= tokens1.length; i++) {
/* 137 */       distances[0][i] = (distances[0][(i - 1)] + deleteCost(tokens1[(i - 1)]));
/*     */     }
/* 139 */     for (int j = 1; j <= tokens2.length; j++) {
/* 140 */       distances[j][0] = (distances[(j - 1)][0] + deleteCost(tokens2[(j - 1)]));
/*     */ 
/* 142 */       boolean keep = distances[j][0] <= maximumDistance;
/* 143 */       for (int i = 1; i <= tokens1.length; i++) {
/* 144 */         distances[j][i] = Math.min(distances[(j - 1)][(i - 1)] + substituteCost(tokens1[(i - 1)], tokens2[(j - 1)]), Math.min(distances[(j - 1)][i] + deleteCost(tokens2[(j - 1)]), distances[j][(i - 1)] + deleteCost(tokens1[(i - 1)])));
/*     */ 
/* 151 */         if ((!keep) && (distances[j][i] <= maximumDistance))
/* 152 */           keep = true;
/*     */       }
/* 154 */       if (!keep) return false;
/*     */     }
/* 156 */     return distances[tokens2.length][tokens1.length] <= maximumDistance;
/*     */   }
/*     */ 
/*     */   protected int deleteCost(String token)
/*     */   {
/* 167 */     return 1;
/*     */   }
/*     */ 
/*     */   protected int insertCost(String token)
/*     */   {
/* 176 */     return 1;
/*     */   }
/*     */ 
/*     */   protected int substituteCost(String originalToken, String newToken)
/*     */   {
/* 187 */     return originalToken.equalsIgnoreCase(newToken) ? 0 : 2;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.matchers.SequenceSubstringMatch
 * JD-Core Version:    0.6.2
 */