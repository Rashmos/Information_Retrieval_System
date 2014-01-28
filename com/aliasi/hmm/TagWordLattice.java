/*     */ package com.aliasi.hmm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tag.TagLattice;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ class TagWordLattice extends TagLattice<String>
/*     */ {
/*     */   final double[][][] mTransitions;
/*     */   final double[][] mForwards;
/*     */   final double[] mForwardExps;
/*     */   final double[][] mBacks;
/*     */   final double[] mBackExps;
/*     */   final double[] mStarts;
/*     */   final double[] mEnds;
/*     */   final String[] mTokens;
/*     */   final SymbolTable mTagSymbolTable;
/*  59 */   double mTotal = (0.0D / 0.0D);
/*  60 */   double mLog2Total = (0.0D / 0.0D);
/*     */ 
/*     */   public TagWordLattice(String[] tokens, SymbolTable tagSymbolTable, double[] startProbs, double[] endProbs, double[][][] transitProbs)
/*     */   {
/* 104 */     for (int i = 0; i < startProbs.length; i++) {
/* 105 */       if ((startProbs[i] < 0.0D) || (startProbs[i] > 1.0D)) {
/* 106 */         String msg = "startProbs[" + i + "]=" + startProbs[i];
/* 107 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 110 */     for (int i = 0; i < endProbs.length; i++) {
/* 111 */       if ((endProbs[i] < 0.0D) || (endProbs[i] > 1.0D)) {
/* 112 */         String msg = "endProbs[" + i + "]=" + endProbs[i];
/* 113 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 116 */     for (int i = 1; i < transitProbs.length; i++) {
/* 117 */       for (int j = 0; j < transitProbs[i].length; j++) {
/* 118 */         for (int k = 0; k < transitProbs[i][j].length; k++) {
/* 119 */           if ((transitProbs[i][j][k] < 0.0D) || (transitProbs[i][j][k] > 1.0D)) {
/* 120 */             String msg = "transitProbs[" + i + "][" + j + "][" + k + "]=" + transitProbs[i][j][k];
/*     */ 
/* 122 */             throw new IllegalArgumentException(msg);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 128 */     int numTags = tagSymbolTable.numSymbols();
/* 129 */     int numTokens = tokens.length;
/* 130 */     this.mStarts = startProbs;
/* 131 */     this.mEnds = endProbs;
/* 132 */     this.mTransitions = transitProbs;
/* 133 */     this.mTokens = tokens;
/* 134 */     this.mTagSymbolTable = tagSymbolTable;
/* 135 */     this.mForwards = new double[numTokens][numTags];
/* 136 */     this.mForwardExps = new double[numTokens];
/*     */ 
/* 138 */     this.mBacks = new double[numTokens][numTags];
/* 139 */     this.mBackExps = new double[numTokens];
/*     */ 
/* 141 */     computeAll();
/*     */   }
/*     */ 
/*     */   public String[] tokens()
/*     */   {
/* 150 */     return this.mTokens;
/*     */   }
/*     */ 
/*     */   public SymbolTable tagSymbolTable()
/*     */   {
/* 159 */     return this.mTagSymbolTable;
/*     */   }
/*     */ 
/*     */   public List<ScoredObject<String>> log2ConditionalTagList(int tokenIndex)
/*     */   {
/* 172 */     double log2Total = log2Total();
/* 173 */     SymbolTable st = this.mTagSymbolTable;
/* 174 */     int numTags = st.numSymbols();
/* 175 */     List scoredTagList = new ArrayList();
/*     */ 
/* 177 */     for (int tagId = 0; tagId < numTags; tagId++) {
/* 178 */       String tag = st.idToSymbol(tagId);
/* 179 */       double log2P = log2ForwardBackward(tokenIndex, tagId);
/* 180 */       double condLog2P = log2P - log2Total;
/* 181 */       if (condLog2P > 0.0D)
/* 182 */         condLog2P = 0.0D;
/* 183 */       else if ((Double.isNaN(condLog2P)) || (Double.isInfinite(condLog2P)))
/* 184 */         condLog2P = com.aliasi.util.Math.log2(4.9E-324D);
/* 185 */       scoredTagList.add(new ScoredObject(tag, condLog2P));
/*     */     }
/* 187 */     Collections.sort(scoredTagList, ScoredObject.reverseComparator());
/* 188 */     return scoredTagList;
/*     */   }
/*     */ 
/*     */   public ScoredObject<String>[] log2ConditionalTags(int tokenIndex)
/*     */   {
/* 201 */     double log2Total = log2Total();
/* 202 */     SymbolTable st = this.mTagSymbolTable;
/* 203 */     int numTags = st.numSymbols();
/*     */ 
/* 205 */     ScoredObject[] scoredTags = (ScoredObject[])new ScoredObject[numTags];
/*     */ 
/* 207 */     for (int tagId = 0; tagId < numTags; tagId++) {
/* 208 */       String tag = st.idToSymbol(tagId);
/* 209 */       double log2P = log2ForwardBackward(tokenIndex, tagId);
/* 210 */       double condLog2P = log2P - log2Total;
/* 211 */       if (condLog2P > 0.0D)
/* 212 */         condLog2P = 0.0D;
/* 213 */       else if ((Double.isNaN(condLog2P)) || (Double.isInfinite(condLog2P)))
/* 214 */         condLog2P = com.aliasi.util.Math.log2(4.9E-324D);
/* 215 */       scoredTags[tagId] = new ScoredObject(tag, condLog2P);
/*     */     }
/* 217 */     Arrays.sort(scoredTags, ScoredObject.reverseComparator());
/* 218 */     return scoredTags;
/*     */   }
/*     */ 
/*     */   public String[] bestForwardBackward()
/*     */   {
/* 232 */     String[] bestTags = new String[this.mTokens.length];
/* 233 */     int numTags = this.mTagSymbolTable.numSymbols();
/* 234 */     for (int i = 0; i < bestTags.length; i++) {
/* 235 */       int bestTagId = 0;
/* 236 */       double bestFB = forwardBackward(i, 0);
/* 237 */       for (int tagId = 1; tagId < numTags; tagId++) {
/* 238 */         double fb = forwardBackward(i, tagId);
/* 239 */         if (fb > bestFB) {
/* 240 */           bestFB = fb;
/* 241 */           bestTagId = tagId;
/*     */         }
/*     */       }
/* 244 */       bestTags[i] = this.mTagSymbolTable.idToSymbol(bestTagId);
/*     */     }
/* 246 */     return bestTags;
/*     */   }
/*     */ 
/*     */   public double start(int tagId)
/*     */   {
/* 258 */     return this.mStarts[tagId];
/*     */   }
/*     */ 
/*     */   public double log2Start(int tagId)
/*     */   {
/* 272 */     return com.aliasi.util.Math.log2(start(tagId));
/*     */   }
/*     */ 
/*     */   public double end(int tagId)
/*     */   {
/* 287 */     return this.mEnds[tagId];
/*     */   }
/*     */ 
/*     */   public double log2End(int tagId)
/*     */   {
/* 300 */     return com.aliasi.util.Math.log2(end(tagId));
/*     */   }
/*     */ 
/*     */   public double transition(int tokenIndex, int sourceTagId, int targetTagId)
/*     */   {
/* 323 */     if (tokenIndex == 0) {
/* 324 */       String msg = "Token index must be > 0.";
/* 325 */       throw new IndexOutOfBoundsException(msg);
/*     */     }
/* 327 */     return this.mTransitions[tokenIndex][sourceTagId][targetTagId];
/*     */   }
/*     */ 
/*     */   public double log2Transitions(int tokenIndex, int sourceTagId, int targetTagId)
/*     */   {
/* 345 */     return com.aliasi.util.Math.log2(transition(tokenIndex, sourceTagId, targetTagId));
/*     */   }
/*     */ 
/*     */   public double forward(int tokenIndex, int tagId)
/*     */   {
/* 362 */     return this.mForwards[tokenIndex][tagId] * java.lang.Math.pow(2.0D, this.mForwardExps[tokenIndex]);
/*     */   }
/*     */ 
/*     */   public double log2Forward(int tokenIndex, int tagId)
/*     */   {
/* 379 */     return com.aliasi.util.Math.log2(this.mForwards[tokenIndex][tagId]) + this.mForwardExps[tokenIndex];
/*     */   }
/*     */ 
/*     */   public double backward(int tokenIndex, int tagId)
/*     */   {
/* 397 */     return this.mBacks[tokenIndex][tagId] * java.lang.Math.pow(2.0D, this.mBackExps[tokenIndex]);
/*     */   }
/*     */ 
/*     */   public double log2Backward(int tokenIndex, int tagId)
/*     */   {
/* 414 */     return com.aliasi.util.Math.log2(this.mBacks[tokenIndex][tagId]) + this.mBackExps[tokenIndex];
/*     */   }
/*     */ 
/*     */   public double forwardBackward(int tokenIndex, int tagId)
/*     */   {
/* 434 */     return forward(tokenIndex, tagId) * backward(tokenIndex, tagId);
/*     */   }
/*     */ 
/*     */   public double log2ForwardBackward(int tokenIndex, int tagId)
/*     */   {
/* 453 */     return log2Forward(tokenIndex, tagId) + log2Backward(tokenIndex, tagId);
/*     */   }
/*     */ 
/*     */   public double total()
/*     */   {
/* 481 */     return this.mTotal;
/*     */   }
/*     */ 
/*     */   public double log2Total()
/*     */   {
/* 491 */     return this.mLog2Total;
/*     */   }
/*     */ 
/*     */   public double logForward(int token, int tag)
/*     */   {
/* 496 */     return com.aliasi.util.Math.logBase2ToNaturalLog(log2Forward(token, tag));
/*     */   }
/*     */ 
/*     */   public double logBackward(int token, int tag) {
/* 500 */     return com.aliasi.util.Math.logBase2ToNaturalLog(log2Backward(token, tag));
/*     */   }
/*     */ 
/*     */   public double logZ()
/*     */   {
/* 505 */     return com.aliasi.util.Math.logBase2ToNaturalLog(log2Total());
/*     */   }
/*     */ 
/*     */   public double logTransition(int tokenFrom, int tagFrom, int tagTo) {
/* 509 */     return com.aliasi.util.Math.logBase2ToNaturalLog(log2Transitions(tokenFrom + 1, tagFrom, tagTo));
/*     */   }
/*     */ 
/*     */   public double logProbability(int tokenIndex, int tagId) {
/* 513 */     return com.aliasi.util.Math.logBase2ToNaturalLog(log2ForwardBackward(tokenIndex, tagId));
/*     */   }
/*     */ 
/*     */   public double logProbability(int tokenTo, int tagFrom, int tagTo) {
/* 517 */     return logProbability(tokenTo - 1, new int[] { tagFrom, tagTo });
/*     */   }
/*     */ 
/*     */   public double logProbability(int tokenFrom, int[] tags) {
/* 521 */     int startTag = tags[0];
/* 522 */     int endTag = tags[(tags.length - 1)];
/* 523 */     int tokenTo = tokenFrom + tags.length - 1;
/* 524 */     double logProb = logForward(tokenFrom, startTag) + logBackward(tokenTo, endTag) - logZ();
/*     */ 
/* 527 */     for (int n = 1; n < tags.length; n++)
/* 528 */       logProb += logTransition(tokenFrom + n - 1, tags[(n - 1)], tags[n]);
/* 529 */     return logProb;
/*     */   }
/*     */ 
/*     */   public int numTokens() {
/* 533 */     return this.mTokens.length;
/*     */   }
/*     */ 
/*     */   public List<String> tokenList() {
/* 537 */     return Arrays.asList(this.mTokens);
/*     */   }
/*     */ 
/*     */   public String token(int n) {
/* 541 */     return this.mTokens[n];
/*     */   }
/*     */ 
/*     */   public int numTags() {
/* 545 */     return this.mTagSymbolTable.numSymbols();
/*     */   }
/*     */ 
/*     */   public String tag(int n) {
/* 549 */     return this.mTagSymbolTable.idToSymbol(n);
/*     */   }
/*     */ 
/*     */   public List<String> tagList() {
/* 553 */     List result = new ArrayList(numTags());
/* 554 */     for (int i = 0; i < numTags(); i++)
/* 555 */       result.add(tag(i));
/* 556 */     return result;
/*     */   }
/*     */ 
/*     */   final void computeAll() {
/* 560 */     computeForward();
/* 561 */     computeBackward();
/* 562 */     computeTotal();
/*     */   }
/*     */ 
/*     */   private void computeTotal() {
/* 566 */     if (this.mForwards.length == 0) {
/* 567 */       this.mTotal = 1.0D;
/* 568 */       this.mLog2Total = 0.0D;
/* 569 */       return;
/*     */     }
/* 571 */     double total = 0.0D;
/* 572 */     int numSymbols = tagSymbolTable().numSymbols();
/* 573 */     for (int tagId = 0; tagId < numSymbols; tagId++)
/* 574 */       total += this.mForwards[0][tagId] * this.mBacks[0][tagId];
/* 575 */     double exp = this.mForwardExps[0] + this.mBackExps[0];
/* 576 */     this.mLog2Total = (com.aliasi.util.Math.log2(total) + exp);
/*     */ 
/* 578 */     this.mTotal = (total * java.lang.Math.pow(2.0D, exp));
/*     */   }
/*     */ 
/*     */   private void computeForward() {
/* 582 */     if (this.mForwards.length == 0) return;
/* 583 */     int numSymbols = tagSymbolTable().numSymbols();
/* 584 */     double[] forwards = this.mForwards[0];
/* 585 */     for (int tagId = 0; tagId < numSymbols; tagId++) {
/* 586 */       if (this.mStarts[tagId] < 0.0D) {
/* 587 */         this.mStarts[tagId] = 0.0D;
/*     */       }
/* 589 */       forwards[tagId] = this.mStarts[tagId];
/*     */     }
/* 591 */     this.mForwardExps[0] = log2ScaleExp(forwards);
/* 592 */     int numToks = this.mTokens.length;
/* 593 */     for (int tokenId = 1; tokenId < numToks; tokenId++) {
/* 594 */       forwards = this.mForwards[(tokenId - 1)];
/* 595 */       double[][] transits = this.mTransitions[tokenId];
/* 596 */       for (int tagId = 0; tagId < numSymbols; tagId++) {
/* 597 */         double f = 0.0D;
/* 598 */         for (int prevTagId = 0; prevTagId < numSymbols; prevTagId++) {
/* 599 */           f += forwards[prevTagId] * transits[prevTagId][tagId];
/*     */         }
/*     */ 
/* 602 */         this.mForwards[tokenId][tagId] = f;
/*     */       }
/* 604 */       this.mForwardExps[tokenId] = (log2ScaleExp(this.mForwards[tokenId]) + this.mForwardExps[(tokenId - 1)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void computeBackward()
/*     */   {
/* 610 */     if (this.mBacks.length == 0) return;
/* 611 */     int numSymbols = tagSymbolTable().numSymbols();
/* 612 */     int lastTok = this.mTokens.length - 1;
/* 613 */     double[] backs = this.mBacks[lastTok];
/* 614 */     for (int tagId = 0; tagId < numSymbols; tagId++)
/* 615 */       backs[tagId] = this.mEnds[tagId];
/* 616 */     this.mBackExps[lastTok] = log2ScaleExp(backs);
/* 617 */     int tokenId = lastTok;
/*     */     while (true) { tokenId--; if (tokenId < 0) break;
/* 618 */       backs = this.mBacks[(tokenId + 1)];
/* 619 */       double[][] transits = this.mTransitions[(tokenId + 1)];
/* 620 */       for (int tagId = 0; tagId < numSymbols; tagId++) {
/* 621 */         double b = 0.0D;
/* 622 */         for (int nextTagId = 0; nextTagId < numSymbols; nextTagId++) {
/* 623 */           b += backs[nextTagId] * transits[tagId][nextTagId];
/*     */         }
/*     */ 
/* 626 */         this.mBacks[tokenId][tagId] = b;
/*     */       }
/* 628 */       this.mBackExps[tokenId] = (log2ScaleExp(this.mBacks[tokenId]) + this.mBackExps[(tokenId + 1)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   static double log2ScaleExp(double[] xs)
/*     */   {
/* 637 */     if (xs.length == 0) return 0.0D;
/* 638 */     double max = xs[0];
/* 639 */     for (int i = 1; i < xs.length; i++)
/* 640 */       if (max < xs[i]) max = xs[i];
/* 641 */     if ((max < 0.0D) || (max > 1.0D)) {
/* 642 */       String msg = "Max must be >= 0 and <= 1. Found max=" + max;
/*     */ 
/* 644 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 646 */     if (max == 0.0D) return 0.0D;
/*     */ 
/* 648 */     double exp = 0.0D;
/* 649 */     double mult = 1.0D;
/* 650 */     while (max < 0.5D) {
/* 651 */       exp -= 1.0D;
/* 652 */       mult *= 2.0D;
/* 653 */       max *= 2.0D;
/*     */     }
/* 655 */     for (int j = 0; j < xs.length; j++)
/* 656 */       xs[j] *= mult;
/* 657 */     if (exp > 0.0D) {
/* 658 */       String msg = "Exponent must be <= 0. Found exp=" + exp;
/*     */ 
/* 660 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 662 */     return exp;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.hmm.TagWordLattice
 * JD-Core Version:    0.6.2
 */