/*     */ package com.aliasi.hmm;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tag.MarginalTagger;
/*     */ import com.aliasi.tag.NBestTagger;
/*     */ import com.aliasi.tag.ScoredTagging;
/*     */ import com.aliasi.tag.TagLattice;
/*     */ import com.aliasi.tag.Tagger;
/*     */ import com.aliasi.tag.Tagging;
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Iterators;
/*     */ import com.aliasi.util.Iterators.Buffered;
/*     */ import com.aliasi.util.Iterators.Modifier;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class HmmDecoder
/*     */   implements Tagger<String>, NBestTagger<String>, MarginalTagger<String>
/*     */ {
/*     */   private final HiddenMarkovModel mHmm;
/*     */   private Map<String, double[]> mEmissionCache;
/*     */   private Map<String, double[]> mEmissionLog2Cache;
/*     */   private double mLog2EmissionBeam;
/*     */   private double mLog2Beam;
/*     */ 
/*     */   public HmmDecoder(HiddenMarkovModel hmm)
/*     */   {
/* 157 */     this(hmm, null, null);
/*     */   }
/*     */ 
/*     */   public HmmDecoder(HiddenMarkovModel hmm, Map<String, double[]> emissionCache, Map<String, double[]> emissionLog2Cache)
/*     */   {
/* 174 */     this(hmm, emissionCache, emissionLog2Cache, (1.0D / 0.0D), (1.0D / 0.0D));
/*     */   }
/*     */ 
/*     */   public HmmDecoder(HiddenMarkovModel hmm, Map<String, double[]> emissionCache, Map<String, double[]> emissionLog2Cache, double log2Beam, double log2EmissionBeam)
/*     */   {
/* 198 */     this.mHmm = hmm;
/* 199 */     this.mEmissionCache = emissionCache;
/* 200 */     this.mEmissionLog2Cache = emissionLog2Cache;
/* 201 */     setLog2Beam(log2Beam);
/* 202 */     setLog2EmissionBeam(log2EmissionBeam);
/*     */   }
/*     */ 
/*     */   public HiddenMarkovModel getHmm()
/*     */   {
/* 213 */     return this.mHmm;
/*     */   }
/*     */ 
/*     */   public Map<String, double[]> emissionCache()
/*     */   {
/* 224 */     return this.mEmissionCache;
/*     */   }
/*     */ 
/*     */   public Map<String, double[]> emissionLog2Cache()
/*     */   {
/* 235 */     return this.mEmissionLog2Cache;
/*     */   }
/*     */ 
/*     */   public void setEmissionCache(Map<String, double[]> cache)
/*     */   {
/* 250 */     this.mEmissionCache = cache;
/*     */   }
/*     */ 
/*     */   public void setLog2EmissionBeam(double log2EmissionBeam)
/*     */   {
/* 265 */     if ((log2EmissionBeam <= 0.0D) || (Double.isNaN(log2EmissionBeam)))
/*     */     {
/* 267 */       String msg = "Beam width must be a positive number. Found log2EmissionBeam=" + log2EmissionBeam;
/*     */ 
/* 269 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 271 */     this.mLog2EmissionBeam = log2EmissionBeam;
/*     */   }
/*     */ 
/*     */   public void setLog2Beam(double log2Beam)
/*     */   {
/* 284 */     if ((log2Beam <= 0.0D) || (Double.isNaN(log2Beam)))
/*     */     {
/* 286 */       String msg = "Beam width must be a positive number. Found log2EmissionBeam=" + log2Beam;
/*     */ 
/* 288 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 290 */     this.mLog2Beam = log2Beam;
/*     */   }
/*     */ 
/*     */   public void setEmissionLog2Cache(Map<String, double[]> cache)
/*     */   {
/* 305 */     this.mEmissionLog2Cache = cache;
/*     */   }
/*     */ 
/*     */   double[] cachedEmitProbs(String emission) {
/* 309 */     double[] emitProbs = (double[])this.mEmissionCache.get(emission);
/* 310 */     if (emitProbs != null) {
/* 311 */       return emitProbs;
/*     */     }
/* 313 */     emitProbs = computeEmitProbs(emission);
/* 314 */     this.mEmissionCache.put(emission, emitProbs);
/* 315 */     return emitProbs;
/*     */   }
/*     */ 
/*     */   double[] computeEmitProbs(String emission) {
/* 319 */     int numTags = this.mHmm.stateSymbolTable().numSymbols();
/* 320 */     double[] emitProbs = new double[numTags];
/* 321 */     for (int i = 0; i < numTags; i++)
/* 322 */       emitProbs[i] = this.mHmm.emitProb(i, emission);
/* 323 */     return emitProbs;
/*     */   }
/*     */ 
/*     */   double[] emitProbs(String emission) {
/* 327 */     return this.mEmissionCache == null ? computeEmitProbs(emission) : cachedEmitProbs(emission);
/*     */   }
/*     */ 
/*     */   double[] cachedEmitLog2Probs(String emission)
/*     */   {
/* 333 */     double[] emitLog2Probs = (double[])this.mEmissionLog2Cache.get(emission);
/* 334 */     if (emitLog2Probs != null) {
/* 335 */       return emitLog2Probs;
/*     */     }
/* 337 */     emitLog2Probs = computeEmitLog2Probs(emission);
/* 338 */     this.mEmissionLog2Cache.put(emission, emitLog2Probs);
/* 339 */     return emitLog2Probs;
/*     */   }
/*     */ 
/*     */   double[] computeEmitLog2Probs(String emission) {
/* 343 */     int numTags = this.mHmm.stateSymbolTable().numSymbols();
/* 344 */     double[] emitLog2Probs = new double[numTags];
/* 345 */     for (int i = 0; i < numTags; i++)
/* 346 */       emitLog2Probs[i] = this.mHmm.emitLog2Prob(i, emission);
/* 347 */     additiveBeamPrune(emitLog2Probs, this.mLog2EmissionBeam);
/* 348 */     return emitLog2Probs;
/*     */   }
/*     */ 
/*     */   static void additiveBeamPrune(double[] emitLog2Probs, double beam) {
/* 352 */     if (beam == (1.0D / 0.0D)) return;
/* 353 */     double best = emitLog2Probs[0];
/* 354 */     for (int i = 1; i < emitLog2Probs.length; i++)
/* 355 */       if (emitLog2Probs[i] > best)
/* 356 */         best = emitLog2Probs[i];
/* 357 */     for (int i = 1; i < emitLog2Probs.length; i++)
/* 358 */       if (emitLog2Probs[i] + beam < best)
/* 359 */         emitLog2Probs[i] = (-1.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   double[] emitLog2Probs(String emission) {
/* 363 */     return this.mEmissionLog2Cache == null ? computeEmitLog2Probs(emission) : cachedEmitLog2Probs(emission);
/*     */   }
/*     */ 
/*     */   TagWordLattice lattice(String[] emissions)
/*     */   {
/* 478 */     int numTokens = emissions.length;
/* 479 */     int numTags = this.mHmm.stateSymbolTable().numSymbols();
/* 480 */     if (numTokens == 0) {
/* 481 */       return new TagWordLattice(emissions, this.mHmm.stateSymbolTable(), new double[numTags], new double[numTags], new double[0][numTags][numTags]);
/*     */     }
/*     */ 
/* 486 */     double[] starts = new double[numTags];
/* 487 */     double[] emitProbs = emitProbs(emissions[0]);
/* 488 */     for (int tagId = 0; tagId < numTags; tagId++) {
/* 489 */       starts[tagId] = (this.mHmm.startProb(tagId) * emitProbs[tagId]);
/*     */     }
/*     */ 
/* 492 */     double[][][] transitions = new double[numTokens][][];
/* 493 */     for (int i = 1; i < numTokens; i++) {
/* 494 */       double[][] transitionsI = new double[numTags][];
/* 495 */       transitions[i] = transitionsI;
/* 496 */       double[] emitProbs2 = emitProbs(emissions[i]);
/* 497 */       for (int prevTagId = 0; prevTagId < numTags; prevTagId++) {
/* 498 */         double[] transitionsIPrevTag = new double[numTags];
/* 499 */         transitions[i][prevTagId] = transitionsIPrevTag;
/* 500 */         for (int tagId = 0; tagId < numTags; tagId++) {
/* 501 */           double transitEstimate = this.mHmm.transitProb(prevTagId, tagId);
/* 502 */           transitionsIPrevTag[tagId] = (transitEstimate * emitProbs2[tagId]);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 508 */     double[] ends = new double[numTags];
/* 509 */     for (int tagId = 0; tagId < numTags; tagId++) {
/* 510 */       ends[tagId] = this.mHmm.endProb(tagId);
/*     */     }
/* 512 */     return new TagWordLattice(emissions, this.mHmm.stateSymbolTable(), starts, ends, transitions);
/*     */   }
/*     */ 
/*     */   String[] firstBest(String[] emissions)
/*     */   {
/* 587 */     if (emissions.length == 0)
/* 588 */       return Strings.EMPTY_STRING_ARRAY;
/* 589 */     return new Viterbi(emissions).bestStates();
/*     */   }
/*     */ 
/*     */   Iterator<ScoredObject<String[]>> nBest(String[] emissions)
/*     */   {
/* 611 */     if (emissions.length == 0) {
/* 612 */       ScoredObject result = new ScoredObject(Strings.EMPTY_STRING_ARRAY, 0.0D);
/*     */ 
/* 614 */       return Iterators.singleton(result);
/*     */     }
/* 616 */     Viterbi viterbiLattice = new Viterbi(emissions);
/* 617 */     return new NBestIterator(viterbiLattice, 2147483647);
/*     */   }
/*     */ 
/*     */   Iterator<ScoredObject<String[]>> nBest(String[] emissions, int maxN)
/*     */   {
/* 639 */     if (emissions.length == 0) {
/* 640 */       ScoredObject result = new ScoredObject(Strings.EMPTY_STRING_ARRAY, 0.0D);
/*     */ 
/* 642 */       return Iterators.singleton(result);
/*     */     }
/* 644 */     Viterbi viterbiLattice = new Viterbi(emissions);
/* 645 */     return new NBestIterator(viterbiLattice, maxN);
/*     */   }
/*     */ 
/*     */   Iterator<ScoredObject<String[]>> nBestConditional(String[] emissions)
/*     */   {
/* 678 */     Iterator nBestIterator = nBest(emissions);
/* 679 */     double jointLog2Prob = lattice(emissions).log2Total();
/* 680 */     return new JointIterator(nBestIterator, jointLog2Prob);
/*     */   }
/*     */ 
/*     */   public Tagging<String> tag(List<String> tokens)
/*     */   {
/* 685 */     String[] tokenArray = (String[])tokens.toArray(Strings.EMPTY_STRING_ARRAY);
/* 686 */     String[] tags = firstBest(tokenArray);
/* 687 */     return new Tagging(Arrays.asList(tokenArray), Arrays.asList(tags));
/*     */   }
/*     */ 
/*     */   public Iterator<ScoredTagging<String>> tagNBest(List<String> tokens, int maxResults)
/*     */   {
/* 692 */     String[] tokenArray = (String[])tokens.toArray(Strings.EMPTY_STRING_ARRAY);
/* 693 */     Iterator it = nBest(tokenArray, maxResults);
/* 694 */     return new TaggingIteratorAdapter(tokens, it, maxResults);
/*     */   }
/*     */ 
/*     */   public Iterator<ScoredTagging<String>> tagNBestConditional(List<String> tokens, int maxResults) {
/* 698 */     String[] tokenArray = (String[])tokens.toArray(Strings.EMPTY_STRING_ARRAY);
/* 699 */     Iterator it = nBestConditional(tokenArray);
/* 700 */     return new TaggingIteratorAdapter(tokens, it, maxResults);
/*     */   }
/*     */ 
/*     */   public TagLattice<String> tagMarginal(List<String> tokens) {
/* 704 */     String[] tokenArray = (String[])tokens.toArray(Strings.EMPTY_STRING_ARRAY);
/* 705 */     return lattice(tokenArray);
/*     */   }
/*     */ 
/*     */   void unprunedSources(double[] sources, int[] survivors, double beam)
/*     */   {
/* 737 */     double best = sources[0];
/* 738 */     for (int i = 0; i < sources.length; i++)
/* 739 */       if (sources[i] > best)
/* 740 */         best = sources[i];
/* 741 */     int next = 0;
/* 742 */     for (int i = 0; i < sources.length; i++)
/* 743 */       if (sources[i] + beam >= best)
/* 744 */         survivors[(next++)] = i;
/* 745 */     survivors[next] = -1;
/*     */   }
/*     */ 
/*     */   private static final class JointIterator extends Iterators.Modifier<ScoredObject<String[]>>
/*     */   {
/*     */     final double mLog2TotalProb;
/*     */ 
/*     */     JointIterator(Iterator<ScoredObject<String[]>> nBestIterator, double log2TotalProb)
/*     */     {
/* 926 */       super();
/* 927 */       this.mLog2TotalProb = log2TotalProb;
/*     */     }
/*     */ 
/*     */     public ScoredObject<String[]> modify(ScoredObject<String[]> jointObj) {
/* 931 */       String[] tags = (String[])jointObj.getObject();
/* 932 */       double log2JointProb = jointObj.score();
/* 933 */       double log2CondProb = log2JointProb - this.mLog2TotalProb;
/* 934 */       return new ScoredObject(tags, log2CondProb);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class State
/*     */     implements Scored
/*     */   {
/*     */     private final double mScore;
/*     */     private final double mContScore;
/*     */     private final int mTagId;
/*     */     private final State mPreviousState;
/*     */     private final int mEmissionIndex;
/*     */ 
/*     */     State(int emissionIndex, double score, double contScore, int tagId, State previousState)
/*     */     {
/* 896 */       this.mEmissionIndex = emissionIndex;
/* 897 */       this.mScore = score;
/* 898 */       this.mContScore = contScore;
/* 899 */       this.mTagId = tagId;
/* 900 */       this.mPreviousState = previousState;
/*     */     }
/*     */     public int emissionIndex() {
/* 903 */       return this.mEmissionIndex;
/*     */     }
/*     */     public double score() {
/* 906 */       return this.mScore + this.mContScore;
/*     */     }
/*     */     ScoredObject<String[]> result(int numTags) {
/* 909 */       return new ScoredObject(tags(numTags), score());
/*     */     }
/*     */     String[] tags(int numTags) {
/* 912 */       SymbolTable symTable = HmmDecoder.this.mHmm.stateSymbolTable();
/* 913 */       String[] tags = new String[numTags];
/* 914 */       State state = this;
/* 915 */       for (int i = 0; i < numTags; i++) {
/* 916 */         tags[i] = symTable.idToSymbol(state.mTagId);
/* 917 */         state = state.mPreviousState;
/*     */       }
/* 919 */       return tags;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class NBestIterator extends Iterators.Buffered<ScoredObject<String[]>>
/*     */   {
/*     */     private final HmmDecoder.Viterbi mViterbi;
/*     */     private final BoundedPriorityQueue<HmmDecoder.State> mPQ;
/*     */ 
/*     */     NBestIterator(HmmDecoder.Viterbi vit, int maxSize)
/*     */     {
/* 834 */       this.mViterbi = vit;
/* 835 */       this.mPQ = new BoundedPriorityQueue(ScoredObject.comparator(), maxSize);
/*     */ 
/* 837 */       String[] emissions = HmmDecoder.Viterbi.access$200(vit);
/* 838 */       int numStates = HmmDecoder.this.mHmm.stateSymbolTable().numSymbols();
/* 839 */       int numEmits = emissions.length;
/* 840 */       int lastEmitIndex = numEmits - 1;
/* 841 */       for (int tagId = 0; tagId < numStates; tagId++) {
/* 842 */         double contScore = HmmDecoder.Viterbi.access$300(vit)[lastEmitIndex][tagId];
/* 843 */         if (contScore > (-1.0D / 0.0D)) {
/* 844 */           double score = 0.0D;
/* 845 */           this.mPQ.offer(new HmmDecoder.State(HmmDecoder.this, lastEmitIndex, score, contScore, tagId, null));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public ScoredObject<String[]> bufferNext()
/*     */     {
/* 852 */       int numTags = HmmDecoder.this.mHmm.stateSymbolTable().numSymbols();
/* 853 */       int numEmissions = HmmDecoder.Viterbi.access$200(this.mViterbi).length;
/* 854 */       int lastEmitIndex = numEmissions - 1;
/* 855 */       while (!this.mPQ.isEmpty()) {
/* 856 */         HmmDecoder.State st = (HmmDecoder.State)this.mPQ.poll();
/* 857 */         int emitIndex = st.emissionIndex();
/* 858 */         if (emitIndex == 0) {
/* 859 */           this.mPQ.setMaxSize(this.mPQ.maxSize() - 1);
/* 860 */           return st.result(numEmissions);
/*     */         }
/* 862 */         String emission = HmmDecoder.Viterbi.access$200(this.mViterbi)[emitIndex];
/* 863 */         int emitTagId = st.mTagId;
/* 864 */         double score = st.mScore;
/* 865 */         if (emitIndex == lastEmitIndex)
/* 866 */           score += HmmDecoder.this.mHmm.endLog2Prob(emitTagId);
/* 867 */         int emitIndexMinus1 = emitIndex - 1;
/*     */ 
/* 869 */         double emitLog2Prob = HmmDecoder.this.mHmm.emitLog2Prob(emitTagId, emission);
/* 870 */         for (int tagId = 0; tagId < numTags; tagId++) {
/* 871 */           double nextScore = score + HmmDecoder.this.mHmm.transitLog2Prob(tagId, emitTagId) + emitLog2Prob;
/*     */ 
/* 874 */           double contScore = HmmDecoder.Viterbi.access$300(this.mViterbi)[emitIndexMinus1][tagId];
/*     */ 
/* 876 */           if ((nextScore > (-1.0D / 0.0D)) && (contScore > (-1.0D / 0.0D)))
/*     */           {
/* 878 */             this.mPQ.offer(new HmmDecoder.State(HmmDecoder.this, emitIndexMinus1, nextScore, contScore, tagId, st));
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 884 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Viterbi
/*     */   {
/*     */     private final String[] mEmissions;
/*     */     private final double[][] mLattice;
/*     */     private final int[][] mBackPts;
/*     */ 
/*     */     Viterbi(String[] emissions)
/*     */     {
/* 753 */       this.mEmissions = emissions;
/* 754 */       HiddenMarkovModel hmm = HmmDecoder.this.mHmm;
/* 755 */       int numStates = hmm.stateSymbolTable().numSymbols();
/* 756 */       int numEmits = emissions.length;
/* 757 */       double[][] lattice = new double[numEmits][numStates];
/* 758 */       this.mLattice = lattice;
/* 759 */       int[][] backPts = new int[numEmits][numStates];
/* 760 */       this.mBackPts = backPts;
/* 761 */       if (emissions.length == 0) {
/* 762 */         return;
/*     */       }
/* 764 */       double[] emitLog2Probs = HmmDecoder.this.emitLog2Probs(emissions[0]);
/*     */ 
/* 766 */       for (int stateId = 0; stateId < numStates; stateId++) {
/* 767 */         lattice[0][stateId] = (emitLog2Probs[stateId] + hmm.startLog2Prob(stateId));
/*     */       }
/*     */ 
/* 772 */       int[] unprunedSources = new int[numStates + 1];
/* 773 */       for (int i = 1; i < numEmits; i++) {
/* 774 */         double[] lastSlice = lattice[(i - 1)];
/* 775 */         HmmDecoder.this.unprunedSources(lastSlice, unprunedSources, HmmDecoder.this.mLog2Beam);
/* 776 */         double[] emitLog2Probs2 = HmmDecoder.this.emitLog2Probs(emissions[i]);
/* 777 */         for (int targetId = 0; targetId < numStates; targetId++) {
/* 778 */           if ((-1.0D / 0.0D) != emitLog2Probs2[targetId]) {
/* 779 */             double best = (-1.0D / 0.0D);
/* 780 */             int bk = 0;
/* 781 */             for (int next = 0; unprunedSources[next] != -1; next++) {
/* 782 */               int sourceId = unprunedSources[next];
/* 783 */               double est = lastSlice[sourceId] + hmm.transitLog2Prob(sourceId, targetId);
/*     */ 
/* 785 */               if (est > best) {
/* 786 */                 best = est;
/* 787 */                 bk = sourceId;
/*     */               }
/*     */             }
/* 790 */             lattice[i][targetId] = (best + emitLog2Probs2[targetId]);
/*     */ 
/* 792 */             backPts[i][targetId] = bk;
/*     */           } else {
/* 794 */             lattice[i][targetId] = (-1.0D / 0.0D);
/* 795 */             backPts[i][targetId] = 0;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 800 */       double[] lastColumn = lattice[(numEmits - 1)];
/* 801 */       for (int i = 0; i < numStates; i++)
/* 802 */         lastColumn[i] += hmm.endLog2Prob(i);
/*     */     }
/*     */ 
/*     */     String[] bestStates() {
/* 806 */       HiddenMarkovModel hmm = HmmDecoder.this.mHmm;
/* 807 */       int numStates = hmm.stateSymbolTable().numSymbols();
/* 808 */       int numEmits = this.mEmissions.length;
/* 809 */       if (numEmits == 0) return Strings.EMPTY_STRING_ARRAY;
/* 810 */       int[][] backPts = this.mBackPts;
/* 811 */       double[][] lattice = this.mLattice;
/*     */ 
/* 813 */       int[] bestStateIds = new int[numEmits];
/* 814 */       int bestStateId = 0;
/* 815 */       double[] lastCol = lattice[(numEmits - 1)];
/* 816 */       for (int i = 1; i < numStates; i++)
/* 817 */         if (lastCol[i] > lastCol[bestStateId])
/* 818 */           bestStateId = i;
/* 819 */       bestStateIds[(numEmits - 1)] = bestStateId;
/* 820 */       int i = numEmits;
/*     */       while (true) { i--; if (i <= 0) break;
/* 821 */         bestStateIds[(i - 1)] = backPts[i][bestStateIds[i]]; }
/* 822 */       String[] bestStates = new String[numEmits];
/* 823 */       SymbolTable st = hmm.stateSymbolTable();
/* 824 */       for (int i = 0; i < bestStates.length; i++)
/* 825 */         bestStates[i] = st.idToSymbol(bestStateIds[i]);
/* 826 */       return bestStates;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TaggingIteratorAdapter
/*     */     implements Iterator<ScoredTagging<String>>
/*     */   {
/*     */     private final Iterator<ScoredObject<String[]>> mIt;
/*     */     private final List<String> mTokens;
/*     */     private final int mMaxResults;
/* 712 */     private int mResults = 0;
/*     */ 
/*     */     TaggingIteratorAdapter(List<String> tokens, Iterator<ScoredObject<String[]>> it, int maxResults)
/*     */     {
/* 716 */       this.mTokens = tokens;
/* 717 */       this.mIt = it;
/* 718 */       this.mMaxResults = maxResults;
/*     */     }
/*     */     public ScoredTagging<String> next() {
/* 721 */       ScoredObject so = (ScoredObject)this.mIt.next();
/* 722 */       double score = so.score();
/* 723 */       String[] tags = (String[])so.getObject();
/* 724 */       List tagList = Arrays.asList(tags);
/* 725 */       this.mResults += 1;
/* 726 */       return new ScoredTagging(this.mTokens, tagList, score);
/*     */     }
/*     */     public boolean hasNext() {
/* 729 */       return (this.mResults < this.mMaxResults) && (this.mIt.hasNext());
/*     */     }
/*     */     public void remove() {
/* 732 */       this.mIt.remove();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.hmm.HmmDecoder
 * JD-Core Version:    0.6.2
 */