/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tokenizer.TokenCategorizer;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ final class CompiledEstimator
/*     */ {
/*     */   private final EstimatorTrie mTagTrie;
/*     */   private final EstimatorTrie mTokenTrie;
/*     */   private final SymbolTable mTagSymbolTable;
/*     */   private final SymbolTable mTokenSymbolTable;
/*     */   private final boolean[][] mCannotFollow;
/*     */   private final int[] mConvertToInterior;
/*     */   private final int[] mStart;
/*     */   private final int[] mInterior;
/*     */   private final double mLogUniformVocabEstimate;
/*     */   private final TokenCategorizer mTokenCategorizer;
/*     */ 
/*     */   public CompiledEstimator(ObjectInput in)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 144 */     this.mTokenCategorizer = ((TokenCategorizer)in.readObject());
/*     */ 
/* 146 */     this.mTagSymbolTable = ((SymbolTable)in.readObject());
/* 147 */     this.mTokenSymbolTable = ((SymbolTable)in.readObject());
/*     */ 
/* 150 */     this.mTagTrie = new EstimatorTrie(in);
/* 151 */     this.mTokenTrie = new EstimatorTrie(in);
/* 152 */     this.mLogUniformVocabEstimate = in.readDouble();
/*     */ 
/* 154 */     int numSymbols = this.mTagSymbolTable.numSymbols();
/* 155 */     this.mConvertToInterior = new int[numSymbols];
/* 156 */     this.mCannotFollow = new boolean[numSymbols][numSymbols];
/* 157 */     int numTags = this.mTagSymbolTable.numSymbols();
/* 158 */     List starts = new ArrayList();
/* 159 */     List interiors = new ArrayList();
/* 160 */     for (int tagID = 0; tagID < numTags; tagID++) {
/* 161 */       String tag = idToTag(tagID);
/* 162 */       this.mConvertToInterior[tagID] = tagToInteriorID(tag);
/* 163 */       if (tagID != this.mConvertToInterior[tagID]) {
/* 164 */         interiors.add(Integer.valueOf(this.mConvertToInterior[tagID]));
/* 165 */         starts.add(Integer.valueOf(tagID));
/*     */       }
/* 167 */       for (int tagMinus1ID = 0; tagMinus1ID < numTags; tagMinus1ID++) {
/* 168 */         this.mCannotFollow[tagID][tagMinus1ID] = Tags.illegalSequence(idToTag(tagMinus1ID), tag);
/*     */       }
/*     */     }
/* 171 */     this.mStart = convertToIntArray(starts);
/* 172 */     this.mInterior = convertToIntArray(interiors);
/*     */   }
/*     */ 
/*     */   public int[] startTagIDs()
/*     */   {
/* 183 */     return this.mStart;
/*     */   }
/*     */ 
/*     */   public int[] interiorTagIDs()
/*     */   {
/* 194 */     return this.mInterior;
/*     */   }
/*     */ 
/*     */   public int numTags()
/*     */   {
/* 205 */     return this.mTagSymbolTable.numSymbols();
/*     */   }
/*     */ 
/*     */   public int tagToID(String tag)
/*     */   {
/* 214 */     return this.mTagSymbolTable.symbolToID(tag);
/*     */   }
/*     */ 
/*     */   public String idToTag(int id)
/*     */   {
/* 225 */     return this.mTagSymbolTable.idToSymbol(id);
/*     */   }
/*     */ 
/*     */   public int tokenToID(String token)
/*     */   {
/* 236 */     return this.mTokenSymbolTable.symbolToID(token);
/*     */   }
/*     */ 
/*     */   public int tokenOrCategoryToID(String token)
/*     */   {
/* 248 */     int id = tokenToID(token);
/* 249 */     if (id < 0) {
/* 250 */       id = tokenToID(this.mTokenCategorizer.categorize(token));
/* 251 */       if (id < 0) {
/* 252 */         System.err.println("No id for token category: " + token);
/*     */       }
/*     */     }
/* 255 */     return id;
/*     */   }
/*     */ 
/*     */   public String idToToken(int id)
/*     */   {
/* 267 */     return this.mTokenSymbolTable.idToSymbol(id);
/*     */   }
/*     */ 
/*     */   public boolean cannotFollow(int tagID, int tagMinus1ID)
/*     */   {
/* 282 */     return this.mCannotFollow[tagID][tagMinus1ID];
/*     */   }
/*     */ 
/*     */   private int idToInteriorID(int tagID)
/*     */   {
/* 294 */     return this.mConvertToInterior[tagID];
/*     */   }
/*     */ 
/*     */   public double estimate(int tagID, int tokenID, int tagMinus1ID, int tokenMinus1ID, int tokenMinus2ID)
/*     */   {
/* 316 */     if (cannotFollow(tagID, tagMinus1ID)) return (0.0D / 0.0D);
/* 317 */     int tagMinus1IDInterior = idToInteriorID(tagMinus1ID);
/* 318 */     return estimateTag(tagID, tagMinus1IDInterior, tokenMinus1ID, tokenMinus2ID) + estimateToken(tokenID, tagID, tagMinus1IDInterior, tokenMinus1ID);
/*     */   }
/*     */ 
/*     */   private double estimateTag(int tagID, int tagMinus1ID, int tokenMinus1ID, int tokenMinus2ID)
/*     */   {
/* 343 */     int nodeTag1Index = this.mTagTrie.lookupChild(tagMinus1ID, 0);
/* 344 */     if (nodeTag1Index == -1)
/*     */     {
/* 346 */       return (0.0D / 0.0D);
/*     */     }
/* 348 */     int nodeTag1W1Index = this.mTagTrie.lookupChild(tokenMinus1ID, nodeTag1Index);
/*     */ 
/* 350 */     if (nodeTag1W1Index == -1) {
/* 351 */       return this.mTagTrie.estimateFromNode(tagID, nodeTag1Index);
/*     */     }
/* 353 */     int nodeTag1W1W2Index = this.mTagTrie.lookupChild(tokenMinus2ID, nodeTag1W1Index);
/*     */ 
/* 355 */     if (nodeTag1W1W2Index == -1) {
/* 356 */       return this.mTagTrie.estimateFromNode(tagID, nodeTag1W1Index);
/*     */     }
/* 358 */     return this.mTagTrie.estimateFromNode(tagID, nodeTag1W1W2Index);
/*     */   }
/*     */ 
/*     */   private double estimateToken(int tokenID, int tagID, int tagMinus1ID, int tokenMinus1ID)
/*     */   {
/* 378 */     int nodeTagIndex = this.mTokenTrie.lookupChild(tagID, 0);
/* 379 */     if (nodeTagIndex == -1)
/* 380 */       return (0.0D / 0.0D);
/* 381 */     int nodeTagTag1Index = this.mTokenTrie.lookupChild(tagMinus1ID, nodeTagIndex);
/* 382 */     if (nodeTagTag1Index == -1) {
/* 383 */       return this.mTokenTrie.estimateFromNodeUniform(tokenID, nodeTagIndex, this.mLogUniformVocabEstimate);
/*     */     }
/*     */ 
/* 388 */     int nodeTagTag1W1Index = this.mTokenTrie.lookupChild(tokenMinus1ID, nodeTagTag1Index);
/*     */ 
/* 390 */     if (nodeTagTag1W1Index != -1) {
/* 391 */       return this.mTokenTrie.estimateFromNodeUniform(tokenID, nodeTagTag1W1Index, this.mLogUniformVocabEstimate);
/*     */     }
/*     */ 
/* 396 */     return this.mTokenTrie.estimateFromNodeUniform(tokenID, nodeTagTag1Index, this.mLogUniformVocabEstimate);
/*     */   }
/*     */ 
/*     */   private int tagToInteriorID(String tag)
/*     */   {
/* 409 */     return tagToID(Tags.toInnerTag(tag));
/*     */   }
/*     */ 
/*     */   private static int[] convertToIntArray(List<Integer> xs)
/*     */   {
/* 421 */     int[] result = new int[xs.size()];
/* 422 */     for (int i = 0; i < result.length; i++)
/* 423 */       result[i] = ((Integer)xs.get(i)).intValue();
/* 424 */     return result;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.CompiledEstimator
 * JD-Core Version:    0.6.2
 */