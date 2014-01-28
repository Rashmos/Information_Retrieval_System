/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.tokenizer.TokenCategorizer;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ final class TokenShapeDecoder
/*     */ {
/*     */   private double mLog2Beam;
/*     */   private final CompiledEstimator mEstimator;
/*     */   private final TokenCategorizer mTokenCategorizer;
/*     */ 
/*     */   public TokenShapeDecoder(CompiledEstimator estimator, TokenCategorizer categorizer, double pruningThreshold)
/*     */   {
/* 105 */     this.mEstimator = estimator;
/* 106 */     this.mTokenCategorizer = categorizer;
/* 107 */     this.mLog2Beam = pruningThreshold;
/*     */   }
/*     */ 
/*     */   void setLog2Beam(double beam) {
/* 111 */     this.mLog2Beam = beam;
/*     */   }
/*     */ 
/*     */   public String[] decodeTags(String[] tokens)
/*     */   {
/* 124 */     if (tokens == null) return null;
/* 125 */     if (tokens.length == 0) return Strings.EMPTY_STRING_ARRAY;
/* 126 */     TagHistory th = decode(tokens);
/* 127 */     String[] result = new String[tokens.length];
/* 128 */     if (th == null)
/*     */     {
/* 130 */       Arrays.fill(result, "O");
/* 131 */       return result;
/*     */     }
/* 133 */     th.toTagArray(this.mEstimator, result);
/* 134 */     return result;
/*     */   }
/*     */ 
/*     */   private TagHistory decode(String[] tokens)
/*     */   {
/* 146 */     int numTags = this.mEstimator.numTags();
/* 147 */     TagHistory[] history = new TagHistory[numTags];
/* 148 */     double[] historyScore = new double[numTags];
/* 149 */     TagHistory[] nextHistory = new TagHistory[numTags];
/* 150 */     double[] nextHistoryScore = new double[numTags];
/*     */ 
/* 152 */     int startTagID = this.mEstimator.tagToID("O");
/* 153 */     int startTokenID = this.mEstimator.tokenToID(".");
/*     */ 
/* 156 */     int tokenMinus1ID = startTokenID;
/* 157 */     int tokenMinus2ID = startTokenID;
/*     */ 
/* 159 */     int outTagID = this.mEstimator.tagToID("O");
/*     */ 
/* 162 */     String token = tokens[0];
/* 163 */     int tokenID = this.mEstimator.tokenToID(token);
/*     */ 
/* 166 */     if (tokenID < 0) {
/* 167 */       String tokenCategory = this.mTokenCategorizer.categorize(token);
/* 168 */       tokenID = this.mEstimator.tokenToID(tokenCategory);
/*     */     }
/*     */ 
/* 178 */     for (int resultTagID = 0; resultTagID < numTags; resultTagID++) {
/* 179 */       if (this.mEstimator.cannotFollow(resultTagID, startTagID)) {
/* 180 */         historyScore[resultTagID] = (0.0D / 0.0D);
/*     */       }
/*     */       else {
/* 183 */         historyScore[resultTagID] = this.mEstimator.estimate(resultTagID, tokenID, startTagID, tokenMinus1ID, tokenMinus2ID);
/*     */ 
/* 195 */         history[resultTagID] = (Double.isNaN(historyScore[resultTagID]) ? (TagHistory)null : new TagHistory(resultTagID, null));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 202 */     for (int i = 1; i < tokens.length; i++)
/*     */     {
/* 204 */       token = tokens[i];
/* 205 */       tokenID = this.mEstimator.tokenToID(token);
/*     */ 
/* 208 */       if (tokenID < 0) {
/* 209 */         String tokenCategory = this.mTokenCategorizer.categorize(token);
/* 210 */         tokenID = this.mEstimator.tokenToID(tokenCategory);
/*     */       }
/*     */ 
/* 220 */       for (int resultTagID = 0; resultTagID < numTags; resultTagID++) {
/* 221 */         int bestPreviousTagID = -1;
/* 222 */         double bestScore = (0.0D / 0.0D);
/*     */ 
/* 225 */         for (int previousTagID = 0; previousTagID < numTags; previousTagID++) {
/* 226 */           if (history[previousTagID] != null)
/*     */           {
/* 228 */             if (!this.mEstimator.cannotFollow(resultTagID, previousTagID))
/*     */             {
/* 232 */               double estimate = this.mEstimator.estimate(resultTagID, tokenID, previousTagID, tokenMinus1ID, tokenMinus2ID);
/*     */ 
/* 236 */               if ((!Double.isNaN(estimate)) && ((bestPreviousTagID == -1) || (estimate + historyScore[previousTagID] > bestScore)))
/*     */               {
/* 240 */                 bestPreviousTagID = previousTagID;
/* 241 */                 bestScore = estimate + historyScore[previousTagID];
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 247 */         if (bestPreviousTagID == -1) {
/* 248 */           nextHistory[resultTagID] = null;
/*     */         } else {
/* 250 */           nextHistory[resultTagID] = new TagHistory(resultTagID, history[bestPreviousTagID]);
/*     */ 
/* 253 */           nextHistoryScore[resultTagID] = bestScore;
/*     */         }
/*     */       }
/*     */ 
/* 257 */       int[] startIds = this.mEstimator.startTagIDs();
/* 258 */       int[] interiorIds = this.mEstimator.interiorTagIDs();
/* 259 */       for (int m = 0; m < startIds.length; m++) {
/* 260 */         if ((nextHistory[startIds[m]] != null) && (nextHistory[interiorIds[m]] != null))
/*     */         {
/* 262 */           if (nextHistoryScore[startIds[m]] > nextHistoryScore[interiorIds[m]])
/*     */           {
/* 265 */             nextHistoryScore[interiorIds[m]] = (0.0D / 0.0D);
/* 266 */             nextHistory[interiorIds[m]] = null;
/*     */           } else {
/* 268 */             nextHistoryScore[startIds[m]] = (0.0D / 0.0D);
/* 269 */             nextHistory[startIds[m]] = null;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 275 */       double bestScore = (0.0D / 0.0D);
/* 276 */       TagHistory bestPreviousHistory = null;
/* 277 */       for (int resultTagID = 0; resultTagID < numTags; resultTagID++) {
/* 278 */         if ((nextHistory[resultTagID] != null) && (
/* 279 */           (Double.isNaN(bestScore)) || (nextHistoryScore[resultTagID] > bestScore)))
/*     */         {
/* 281 */           bestScore = nextHistoryScore[resultTagID];
/* 282 */           bestPreviousHistory = nextHistory[resultTagID];
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 287 */       double worstScoreToKeep = bestScore - this.mLog2Beam;
/* 288 */       for (int resultTagID = 0; resultTagID < numTags; resultTagID++)
/*     */       {
/* 290 */         if (resultTagID == outTagID) {
/* 291 */           if (nextHistory[outTagID] == null) {
/* 292 */             nextHistory[outTagID] = new TagHistory(outTagID, bestPreviousHistory);
/*     */ 
/* 294 */             if ((Double.isNaN(nextHistoryScore[outTagID])) || (Double.isInfinite(nextHistoryScore[outTagID])))
/*     */             {
/* 296 */               nextHistoryScore[outTagID] = bestScore;
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/* 301 */         else if ((nextHistory[resultTagID] != null) && 
/* 302 */           (nextHistoryScore[resultTagID] < worstScoreToKeep)) {
/* 303 */           nextHistory[resultTagID] = null;
/*     */         }
/*     */       }
/*     */ 
/* 307 */       if (allNull(nextHistory))
/*     */       {
/* 309 */         return null;
/*     */       }
/*     */ 
/* 313 */       tokenMinus2ID = tokenMinus1ID;
/* 314 */       tokenMinus1ID = tokenID;
/* 315 */       TagHistory[] tempHistory = history;
/* 316 */       double[] tempHistoryScore = historyScore;
/* 317 */       history = nextHistory;
/* 318 */       historyScore = nextHistoryScore;
/* 319 */       nextHistory = tempHistory;
/* 320 */       nextHistoryScore = tempHistoryScore;
/*     */     }
/*     */ 
/* 323 */     return extractBest(history, historyScore);
/*     */   }
/*     */ 
/*     */   private TagHistory extractBest(TagHistory[] history, double[] historyScore)
/*     */   {
/* 334 */     int bestIndex = -1;
/* 335 */     for (int i = 0; i < history.length; i++) {
/* 336 */       if (history[i] != null)
/* 337 */         if (bestIndex == -1) bestIndex = i;
/* 338 */         else if (historyScore[i] > historyScore[bestIndex]) bestIndex = i;
/*     */     }
/* 340 */     return bestIndex == -1 ? null : history[bestIndex];
/*     */   }
/*     */ 
/*     */   private static boolean allNull(Object[] xs)
/*     */   {
/* 348 */     for (int i = 0; i < xs.length; i++)
/* 349 */       if (xs[i] != null)
/* 350 */         return false;
/* 351 */     return true;
/*     */   }
/*     */ 
/*     */   private static final class TagHistory
/*     */   {
/*     */     private final int mTag;
/*     */     private final TagHistory mPreviousHistory;
/*     */ 
/*     */     public TagHistory(int tag, TagHistory previousHistory)
/*     */     {
/* 387 */       this.mTag = tag;
/* 388 */       this.mPreviousHistory = previousHistory;
/*     */     }
/*     */ 
/*     */     public void toTagArray(CompiledEstimator estimator, String[] result)
/*     */     {
/* 400 */       TagHistory history = this;
/* 401 */       int i = result.length;
/*     */ 
/* 403 */       for (; ; history = history.mPreviousHistory)
/*     */       {
/* 402 */         i--; if (i < 0) {
/*     */           break;
/*     */         }
/* 405 */         result[i] = estimator.idToTag(history.mTag);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.TokenShapeDecoder
 * JD-Core Version:    0.6.2
 */