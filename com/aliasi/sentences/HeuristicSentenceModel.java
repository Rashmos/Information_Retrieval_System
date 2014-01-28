/*     */ package com.aliasi.sentences;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class HeuristicSentenceModel extends AbstractSentenceModel
/*     */ {
/*     */   Set<String> mPossibleStops;
/*     */   Set<String> mBadPrevious;
/*     */   Set<String> mBadFollowing;
/*     */   private final boolean mForceFinalStop;
/*     */   private final boolean mBalanceParens;
/*     */ 
/*     */   public HeuristicSentenceModel(Set<String> possibleStops, Set<String> impossiblePenultimate, Set<String> impossibleStarts)
/*     */   {
/* 152 */     this(possibleStops, impossiblePenultimate, impossibleStarts, false, false);
/*     */   }
/*     */ 
/*     */   public HeuristicSentenceModel(Set<String> possibleStops, Set<String> impossiblePenultimate, Set<String> impossibleStarts, boolean forceFinalStop, boolean balanceParens)
/*     */   {
/* 172 */     this.mPossibleStops = toLowerCase(possibleStops);
/* 173 */     this.mBadPrevious = toLowerCase(impossiblePenultimate);
/* 174 */     this.mBadFollowing = toLowerCase(impossibleStarts);
/* 175 */     this.mForceFinalStop = forceFinalStop;
/* 176 */     this.mBalanceParens = balanceParens;
/*     */   }
/*     */ 
/*     */   public boolean forceFinalStop()
/*     */   {
/* 201 */     return this.mForceFinalStop;
/*     */   }
/*     */ 
/*     */   public boolean balanceParens()
/*     */   {
/* 215 */     return this.mBalanceParens;
/*     */   }
/*     */ 
/*     */   public void boundaryIndices(String[] tokens, String[] whitespaces, int start, int length, Collection<Integer> indices)
/*     */   {
/* 236 */     if (length == 0) return;
/*     */ 
/* 238 */     if (length == 1) {
/* 239 */       if ((this.mForceFinalStop) || (this.mPossibleStops.contains(tokens[start].toLowerCase())))
/*     */       {
/* 242 */         indices.add(Integer.valueOf(start));
/*     */       }
/* 244 */       return;
/*     */     }
/*     */ 
/* 248 */     boolean inParens = false;
/* 249 */     if (tokens[start].equals("(")) inParens = true;
/* 250 */     boolean inBrackets = false;
/* 251 */     if (tokens[start].equals("[")) inBrackets = true;
/* 252 */     int end = start + length - 1;
/* 253 */     for (int i = start + 1; i < end; i++)
/*     */     {
/* 255 */       if (this.mBalanceParens) {
/* 256 */         if (tokens[i].equals("(")) {
/* 257 */           inParens = true;
/*     */         }
/* 260 */         else if (tokens[i].equals(")")) {
/* 261 */           inParens = false;
/*     */         }
/* 264 */         else if (tokens[i].equals("[")) {
/* 265 */           inBrackets = true;
/*     */         }
/* 268 */         else if (tokens[i].equals("]")) {
/* 269 */           inBrackets = false;
/*     */         }
/* 273 */         else if ((inParens) || (inBrackets));
/*     */       }
/* 277 */       else if (this.mPossibleStops.contains(tokens[i].toLowerCase()))
/*     */       {
/* 280 */         if (whitespaces[(i + 1)].length() != 0)
/*     */         {
/* 283 */           if (!this.mBadPrevious.contains(tokens[(i - 1)].toLowerCase()))
/*     */           {
/* 286 */             if (!this.mBadFollowing.contains(tokens[(i + 1)].toLowerCase()))
/*     */             {
/* 289 */               if (possibleStart(tokens, whitespaces, i + 1, end))
/*     */               {
/* 291 */                 indices.add(Integer.valueOf(i));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 295 */     if ((this.mForceFinalStop) || ((this.mPossibleStops.contains(tokens[end].toLowerCase())) && (!this.mBadPrevious.contains(tokens[(end - 1)].toLowerCase()))))
/*     */     {
/* 298 */       indices.add(Integer.valueOf(end));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean possibleStart(String[] tokens, String[] whitespaces, int start, int end)
/*     */   {
/* 323 */     String tok = tokens[start];
/* 324 */     return (tok.length() > 0) && (!Character.isLowerCase(tok.charAt(0)));
/*     */   }
/*     */ 
/*     */   static Set<String> toLowerCase(Set<String> xs)
/*     */   {
/* 329 */     Set result = new HashSet();
/* 330 */     for (String s : xs)
/* 331 */       result.add(s.toLowerCase());
/* 332 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.sentences.HeuristicSentenceModel
 * JD-Core Version:    0.6.2
 */