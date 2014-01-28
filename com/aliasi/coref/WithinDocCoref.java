/*     */ package com.aliasi.coref;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class WithinDocCoref
/*     */ {
/*  34 */   private final List<MentionChain> mMentionChains = new ArrayList();
/*     */   private final MentionFactory mMentionFactory;
/* 223 */   public static final Comparator<MentionChain> SENTENCE_FINAL_COMPARATOR = new Comparator()
/*     */   {
/*     */     public int compare(MentionChain chain1, MentionChain chain2)
/*     */     {
/* 227 */       if (chain1.maxSentenceOffset() < chain2.maxSentenceOffset())
/* 228 */         return 1;
/* 229 */       if (chain1.maxSentenceOffset() > chain2.maxSentenceOffset())
/* 230 */         return -1;
/* 231 */       return 0;
/*     */     }
/* 223 */   };
/*     */ 
/* 236 */   static final MentionChain[] EMPTY_MENTION_CHAIN_ARRAY = new MentionChain[0];
/*     */ 
/*     */   public WithinDocCoref(MentionFactory mentionFactory)
/*     */   {
/*  50 */     this.mMentionFactory = mentionFactory;
/*     */   }
/*     */ 
/*     */   public MentionChain[] mentionChains()
/*     */   {
/*  60 */     return (MentionChain[])this.mMentionChains.toArray(EMPTY_MENTION_CHAIN_ARRAY);
/*     */   }
/*     */ 
/*     */   public int resolveMention(Mention mention, int offset)
/*     */   {
/*  78 */     List[] hypotheses = new List[7];
/*     */ 
/*  80 */     for (int i = 0; i < hypotheses.length; i++)
/*  81 */       hypotheses[i] = new ArrayList();
/*  82 */     MentionChain[] antecedents = mentionChains();
/*  83 */     Arrays.sort(antecedents, SENTENCE_FINAL_COMPARATOR);
/*  84 */     for (int i = 0; i < antecedents.length; i++) {
/*  85 */       MentionChain nextAntecedent = antecedents[i];
/*  86 */       if (finished(offset, nextAntecedent, hypotheses)) break;
/*  87 */       addPossibleAntecedent(mention, offset, nextAntecedent, hypotheses);
/*     */     }
/*  89 */     return selectAntecedent(hypotheses, mention, offset);
/*     */   }
/*     */ 
/*     */   private boolean finished(int mentionOffset, MentionChain chain, List<MentionChain>[] hypotheses)
/*     */   {
/* 111 */     int distance = distanceScore(mentionOffset, chain);
/* 112 */     for (int i = distance; i > 0; i--)
/* 113 */       if (hypotheses[i].size() > 0) return true;
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   private void addPossibleAntecedent(Mention mention, int offset, MentionChain antecedent, List<MentionChain>[] hypotheses)
/*     */   {
/* 131 */     if (antecedent.killed(mention)) return;
/* 132 */     int matchScore = antecedent.matchScore(mention);
/* 133 */     if (matchScore == -1) return;
/* 134 */     int totalScore = matchScore + distanceScore(offset, antecedent);
/* 135 */     hypotheses[totalScore].add(antecedent);
/*     */   }
/*     */ 
/*     */   private int selectAntecedent(List<MentionChain>[] hypotheses, Mention mention, int offset)
/*     */   {
/* 160 */     for (int score = 0; score < hypotheses.length; score++) {
/* 161 */       if (hypotheses[score].size() == 1) {
/* 162 */         MentionChain antecedent = (MentionChain)hypotheses[score].get(0);
/* 163 */         antecedent.add(mention, offset);
/* 164 */         return antecedent.identifier();
/* 165 */       }if (hypotheses[score].size() > 1)
/*     */       {
/* 167 */         return promoteToNewChain(mention, offset);
/*     */       }
/*     */     }
/*     */ 
/* 171 */     return promoteToNewChain(mention, offset);
/*     */   }
/*     */ 
/*     */   private int promoteToNewChain(Mention mention, int offset)
/*     */   {
/* 186 */     if (mention.isPronominal()) return -1;
/* 187 */     MentionChain chain = this.mMentionFactory.promote(mention, offset);
/* 188 */     this.mMentionChains.add(chain);
/* 189 */     return chain.identifier();
/*     */   }
/*     */ 
/*     */   private static int distanceScore(int mentionOffset, MentionChain antecedent)
/*     */   {
/* 206 */     switch (mentionOffset - antecedent.maxSentenceOffset()) { case 0:
/* 207 */       return 0;
/*     */     case 1:
/* 208 */       return 1;
/*     */     case 2:
/* 209 */       return 1; }
/* 210 */     return 2;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.WithinDocCoref
 * JD-Core Version:    0.6.2
 */