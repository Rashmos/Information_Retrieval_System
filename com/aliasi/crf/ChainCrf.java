/*      */ package com.aliasi.crf;
/*      */ 
/*      */ import com.aliasi.corpus.Corpus;
/*      */ import com.aliasi.corpus.ObjectHandler;
/*      */ import com.aliasi.features.Features;
/*      */ import com.aliasi.io.Reporter;
/*      */ import com.aliasi.io.Reporters;
/*      */ import com.aliasi.matrix.DenseVector;
/*      */ import com.aliasi.matrix.Matrices;
/*      */ import com.aliasi.matrix.Vector;
/*      */ import com.aliasi.stats.AnnealingSchedule;
/*      */ import com.aliasi.stats.RegressionPrior;
/*      */ import com.aliasi.symbol.MapSymbolTable;
/*      */ import com.aliasi.symbol.SymbolTable;
/*      */ import com.aliasi.tag.MarginalTagger;
/*      */ import com.aliasi.tag.NBestTagger;
/*      */ import com.aliasi.tag.ScoredTagging;
/*      */ import com.aliasi.tag.TagLattice;
/*      */ import com.aliasi.tag.Tagger;
/*      */ import com.aliasi.tag.Tagging;
/*      */ import com.aliasi.util.AbstractExternalizable;
/*      */ import com.aliasi.util.BoundedPriorityQueue;
/*      */ import com.aliasi.util.Iterators;
/*      */ import com.aliasi.util.Iterators.Buffered;
/*      */ import com.aliasi.util.ObjectToCounterMap;
/*      */ import com.aliasi.util.Scored;
/*      */ import com.aliasi.util.ScoredObject;
/*      */ import com.aliasi.util.Strings;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Formatter;
/*      */ import java.util.IllegalFormatException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class ChainCrf<E>
/*      */   implements Tagger<E>, NBestTagger<E>, MarginalTagger<E>, Serializable
/*      */ {
/*      */   static final long serialVersionUID = -4868542587460878290L;
/*      */   private final List<String> mTagList;
/*      */   private final boolean[] mLegalTagStarts;
/*      */   private final boolean[] mLegalTagEnds;
/*      */   private final boolean[][] mLegalTagTransitions;
/*      */   private final Vector[] mCoefficients;
/*      */   private final SymbolTable mFeatureSymbolTable;
/*      */   private final ChainCrfFeatureExtractor<E> mFeatureExtractor;
/*      */   private final boolean mAddInterceptFeature;
/*      */   private final int mNumDimensions;
/*      */   static final String INTERCEPT_FEATURE_NAME = "*&^INTERCEPT%$^&**";
/*  947 */   static final double[][] EMPTY_DOUBLE_2D_ARRAY = new double[0][];
/*  948 */   static final double[][][] EMPTY_DOUBLE_3D_ARRAY = new double[0][][];
/*      */ 
/*      */   public ChainCrf(String[] tags, Vector[] coefficients, SymbolTable featureSymbolTable, ChainCrfFeatureExtractor<E> featureExtractor, boolean addInterceptFeature)
/*      */   {
/*  325 */     this(tags, trueArray(tags.length), trueArray(tags.length), trueArray(tags.length, tags.length), coefficients, featureSymbolTable, featureExtractor, addInterceptFeature);
/*      */   }
/*      */ 
/*      */   public ChainCrf(String[] tags, boolean[] legalTagStarts, boolean[] legalTagEnds, boolean[][] legalTagTransitions, Vector[] coefficients, SymbolTable featureSymbolTable, ChainCrfFeatureExtractor<E> featureExtractor, boolean addInterceptFeature)
/*      */   {
/*      */     String msg;
/*  367 */     if (tags.length < 1) {
/*  368 */       msg = "Require at least one tag.";
/*      */     }
/*  370 */     if (tags.length != coefficients.length) {
/*  371 */       String msg = "Require tags and coefficients to be same length. Found tags.length=" + tags.length + " coefficients.length=" + coefficients.length;
/*      */ 
/*  374 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  376 */     if (tags.length != legalTagStarts.length) {
/*  377 */       String msg = "Tags and starts must be same length. Found tags.length=" + tags.length + " legalTagStarts.length=" + legalTagStarts.length;
/*      */ 
/*  380 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  382 */     if (tags.length != legalTagEnds.length) {
/*  383 */       String msg = "Tags and starts must be same length. Found tags.length=" + tags.length + " legalTagStarts.length=" + legalTagStarts.length;
/*      */ 
/*  386 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  389 */     if (tags.length != legalTagTransitions.length) {
/*  390 */       String msg = "Tags and transitions must be same length. Found tags.length=" + tags.length + " legalTagTransitions.length=" + legalTagTransitions.length;
/*      */ 
/*  393 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  395 */     for (int i = 0; i < legalTagTransitions.length; i++) {
/*  396 */       if (tags.length != legalTagTransitions[i].length) {
/*  397 */         String msg = "Tags and transition rows must be same length. Found tags.length=" + tags.length + " legalTagTransitions[" + i + "].length=" + legalTagTransitions[i].length;
/*      */ 
/*  401 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  406 */     for (int k = 1; k < coefficients.length; k++) {
/*  407 */       if (coefficients[0].numDimensions() != coefficients[k].numDimensions()) {
/*  408 */         String msg = "All coefficients must be same length. Found coefficents[0].numDimensions()=" + coefficients[0].numDimensions() + " coefficients[" + k + "].numDimensions()=" + coefficients[k].numDimensions();
/*      */ 
/*  413 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*  416 */     this.mTagList = Arrays.asList(tags);
/*  417 */     this.mLegalTagStarts = legalTagStarts;
/*  418 */     this.mLegalTagEnds = legalTagEnds;
/*  419 */     this.mLegalTagTransitions = legalTagTransitions;
/*  420 */     this.mCoefficients = coefficients;
/*  421 */     this.mNumDimensions = coefficients[0].numDimensions();
/*  422 */     this.mFeatureSymbolTable = featureSymbolTable;
/*  423 */     this.mFeatureExtractor = featureExtractor;
/*  424 */     this.mAddInterceptFeature = addInterceptFeature;
/*      */   }
/*      */ 
/*      */   public List<String> tags()
/*      */   {
/*  439 */     return Collections.unmodifiableList(this.mTagList);
/*      */   }
/*      */ 
/*      */   public String tag(int k)
/*      */   {
/*  453 */     return (String)this.mTagList.get(k);
/*      */   }
/*      */ 
/*      */   public Vector[] coefficients()
/*      */   {
/*  466 */     Vector[] result = new Vector[this.mCoefficients.length];
/*  467 */     for (int k = 0; k < result.length; k++)
/*  468 */       result[k] = Matrices.unmodifiableVector(this.mCoefficients[k]);
/*  469 */     return result;
/*      */   }
/*      */ 
/*      */   public SymbolTable featureSymbolTable()
/*      */   {
/*  479 */     return MapSymbolTable.unmodifiableView(this.mFeatureSymbolTable);
/*      */   }
/*      */ 
/*      */   public ChainCrfFeatureExtractor<E> featureExtractor()
/*      */   {
/*  489 */     return this.mFeatureExtractor;
/*      */   }
/*      */ 
/*      */   public boolean addInterceptFeature()
/*      */   {
/*  499 */     return this.mAddInterceptFeature;
/*      */   }
/*      */ 
/*      */   public Tagging<E> tag(List<E> tokens)
/*      */   {
/*  506 */     int numTokens = tokens.size();
/*  507 */     if (numTokens == 0)
/*  508 */       return new Tagging(tokens, Collections.emptyList());
/*  509 */     int numTags = this.mTagList.size();
/*  510 */     int numDimensions = this.mFeatureSymbolTable.numSymbols();
/*  511 */     double[][] bestScores = new double[numTokens][numTags];
/*  512 */     int[][] backPointers = new int[numTokens - 1][numTags];
/*      */ 
/*  514 */     ChainCrfFeatures features = this.mFeatureExtractor.extract(tokens, this.mTagList);
/*  515 */     Vector nodeVector0 = nodeFeatures(0, features);
/*  516 */     for (int k = 0; k < numTags; k++) {
/*  517 */       bestScores[0][k] = (this.mLegalTagStarts[k] != 0 ? nodeVector0.dotProduct(this.mCoefficients[k]) : (-1.0D / 0.0D));
/*      */     }
/*      */ 
/*  522 */     Vector[] edgeVectors = new Vector[numTags];
/*  523 */     for (int n = 1; n < numTokens; n++) {
/*  524 */       Vector nodeVector = nodeFeatures(n, features);
/*  525 */       for (int kMinus1 = 0; kMinus1 < numTags; kMinus1++)
/*  526 */         edgeVectors[kMinus1] = edgeFeatures(n, kMinus1, features);
/*  527 */       for (int k = 0; k < numTags; k++)
/*  528 */         if ((n == numTokens - 1) && (this.mLegalTagEnds[k] == 0)) {
/*  529 */           bestScores[n][k] = (-1.0D / 0.0D);
/*  530 */           backPointers[(n - 1)][k] = -1;
/*      */         }
/*      */         else {
/*  533 */           double bestScore = (-1.0D / 0.0D);
/*  534 */           int backPtr = -1;
/*  535 */           double nodeScore = nodeVector.dotProduct(this.mCoefficients[k]);
/*  536 */           for (int kMinus1 = 0; kMinus1 < numTags; kMinus1++)
/*  537 */             if (this.mLegalTagTransitions[kMinus1][k] != 0)
/*      */             {
/*  539 */               double score = nodeScore + edgeVectors[kMinus1].dotProduct(this.mCoefficients[k]) + bestScores[(n - 1)][kMinus1];
/*      */ 
/*  542 */               if (score > bestScore) {
/*  543 */                 bestScore = score;
/*  544 */                 backPtr = kMinus1;
/*      */               }
/*      */             }
/*  547 */           bestScores[n][k] = bestScore;
/*  548 */           backPointers[(n - 1)][k] = backPtr;
/*      */         }
/*      */     }
/*  551 */     double bestScore = (-1.0D / 0.0D);
/*  552 */     int bestFinalTag = -1;
/*  553 */     for (int k = 0; k < numTags; k++) {
/*  554 */       if (bestScores[(numTokens - 1)][k] > bestScore) {
/*  555 */         bestScore = bestScores[(numTokens - 1)][k];
/*  556 */         bestFinalTag = k;
/*      */       }
/*      */     }
/*  559 */     List tags = new ArrayList(numTokens);
/*  560 */     int bestPreviousTag = bestFinalTag;
/*  561 */     tags.add(this.mTagList.get(bestFinalTag));
/*  562 */     int n = numTokens - 1;
/*      */     while (true) { n--; if (n < 0) break;
/*  563 */       bestPreviousTag = backPointers[n][bestPreviousTag];
/*  564 */       tags.add(this.mTagList.get(bestPreviousTag));
/*      */     }
/*  566 */     Collections.reverse(tags);
/*  567 */     return new Tagging(tokens, tags);
/*      */   }
/*      */ 
/*      */   public Iterator<ScoredTagging<E>> tagNBest(List<E> tokens, int maxResults)
/*      */   {
/*  572 */     if (tokens.size() == 0) {
/*  573 */       ScoredTagging scoredTagging = new ScoredTagging(tokens, Collections.emptyList(), 0.0D);
/*      */ 
/*  577 */       return Iterators.singleton(scoredTagging);
/*      */     }
/*  579 */     return new NBestIterator(tokens, false, maxResults);
/*      */   }
/*      */ 
/*      */   public Iterator<ScoredTagging<E>> tagNBestConditional(List<E> tokens, int maxResults) {
/*  583 */     if (tokens.size() == 0) {
/*  584 */       ScoredTagging scoredTagging = new ScoredTagging(tokens, Collections.emptyList(), 0.0D);
/*      */ 
/*  588 */       return Iterators.singleton(scoredTagging);
/*      */     }
/*  590 */     return new NBestIterator(tokens, true, maxResults);
/*      */   }
/*      */ 
/*      */   public TagLattice<E> tagMarginal(List<E> tokens)
/*      */   {
/*  597 */     if (tokens.size() == 0) {
/*  598 */       return new ForwardBackwardTagLattice(tokens, this.mTagList, EMPTY_DOUBLE_2D_ARRAY, EMPTY_DOUBLE_2D_ARRAY, EMPTY_DOUBLE_3D_ARRAY, 0.0D);
/*      */     }
/*      */ 
/*  605 */     FeatureVectors features = features(tokens);
/*  606 */     TagLattice lattice = forwardBackward(tokens, features);
/*      */ 
/*  608 */     return lattice;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  622 */     StringBuilder sb = new StringBuilder();
/*  623 */     sb.append("Feature Extractor=" + featureExtractor());
/*  624 */     sb.append("\n");
/*  625 */     sb.append("Add intercept=" + addInterceptFeature());
/*  626 */     sb.append("\n");
/*  627 */     List tags = tags();
/*  628 */     sb.append("Tags=" + tags);
/*  629 */     sb.append("\n");
/*  630 */     Vector[] coeffs = coefficients();
/*  631 */     SymbolTable symTab = featureSymbolTable();
/*  632 */     sb.append("Coefficients=\n");
/*  633 */     for (int i = 0; i < coeffs.length; i++) {
/*  634 */       sb.append((String)tags.get(i));
/*  635 */       sb.append("  ");
/*  636 */       int[] nzDims = coeffs[i].nonZeroDimensions();
/*  637 */       for (int k = 0; k < nzDims.length; k++) {
/*  638 */         if (k > 0) sb.append(", ");
/*  639 */         int d = nzDims[k];
/*  640 */         sb.append(symTab.idToSymbol(d));
/*  641 */         sb.append("=");
/*  642 */         sb.append(coeffs[i].value(d));
/*      */       }
/*  644 */       sb.append("\n");
/*      */     }
/*  646 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   Object writeReplace()
/*      */   {
/*  651 */     return new Serializer(this);
/*      */   }
/*      */ 
/*      */   private Vector nodeFeatures(int position, ChainCrfFeatures<E> features)
/*      */   {
/*  656 */     return Features.toVector(features.nodeFeatures(position), this.mFeatureSymbolTable, this.mNumDimensions, this.mAddInterceptFeature);
/*      */   }
/*      */ 
/*      */   private Vector edgeFeatures(int position, int lastTagIndex, ChainCrfFeatures<E> features)
/*      */   {
/*  664 */     return Features.toVector(features.edgeFeatures(position, lastTagIndex), this.mFeatureSymbolTable, this.mNumDimensions, this.mAddInterceptFeature);
/*      */   }
/*      */ 
/*      */   private FeatureVectors features(List<E> tokens)
/*      */   {
/*  673 */     int numTags = this.mTagList.size();
/*  674 */     int numDimensions = this.mFeatureSymbolTable.numSymbols();
/*      */ 
/*  676 */     if (tokens.size() == 0) {
/*  677 */       return null;
/*      */     }
/*  679 */     ChainCrfFeatures features = this.mFeatureExtractor.extract(tokens, this.mTagList);
/*      */ 
/*  683 */     Vector[] nodeFeatureVectors = new Vector[tokens.size()];
/*      */ 
/*  685 */     for (int n = 0; n < tokens.size(); n++) {
/*  686 */       nodeFeatureVectors[n] = nodeFeatures(n, features);
/*      */     }
/*      */ 
/*  689 */     Vector[][] edgeFeatureVectorss = new Vector[tokens.size() - 1][this.mTagList.size()];
/*      */ 
/*  691 */     for (int n = 1; n < tokens.size(); n++) {
/*  692 */       for (int k = 0; k < numTags; k++) {
/*  693 */         edgeFeatureVectorss[(n - 1)][k] = edgeFeatures(n, k, features);
/*      */       }
/*      */     }
/*      */ 
/*  697 */     return new FeatureVectors(nodeFeatureVectors, edgeFeatureVectorss);
/*      */   }
/*      */ 
/*      */   TagLattice<E> forwardBackward(List<E> tokens, FeatureVectors features)
/*      */   {
/*  707 */     int numTokens = tokens.size();
/*  708 */     int numTags = this.mTagList.size();
/*      */ 
/*  711 */     double[] logPotentials0Begin = new double[numTags];
/*  712 */     for (int kTo = 0; kTo < numTags; kTo++) {
/*  713 */       logPotentials0Begin[kTo] = (this.mLegalTagStarts[kTo] != 0 ? features.mNodeFeatureVectors[0].dotProduct(this.mCoefficients[kTo]) : (-1.0D / 0.0D));
/*      */     }
/*      */ 
/*  719 */     double[][][] logPotentials = new double[numTokens - 1][numTags][numTags];
/*  720 */     for (double[][] logPotentials2 : logPotentials)
/*  721 */       for (double[] logPotentials3 : logPotentials2)
/*  722 */         Arrays.fill(logPotentials3, (-1.0D / 0.0D));
/*  723 */     for (int nTo = 1; nTo < numTokens; nTo++) {
/*  724 */       for (int kTo = 0; kTo < numTags; kTo++) {
/*  725 */         if ((nTo != numTokens - 1) || (this.mLegalTagEnds[kTo] != 0))
/*      */         {
/*  727 */           double nodePotentialKTo = features.mNodeFeatureVectors[nTo].dotProduct(this.mCoefficients[kTo]);
/*      */ 
/*  729 */           for (int kFrom = 0; kFrom < numTags; kFrom++) {
/*  730 */             if (this.mLegalTagTransitions[kFrom][kTo] != 0) {
/*  731 */               logPotentials[(nTo - 1)][kFrom][kTo] = (features.mEdgeFeatureVectorss[(nTo - 1)][kFrom].dotProduct(this.mCoefficients[kTo]) + nodePotentialKTo);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  738 */     double[] buf = new double[numTags];
/*  739 */     double[][] logForwards = new double[numTokens][numTags];
/*  740 */     for (int kTo = 0; kTo < numTags; kTo++)
/*  741 */       logForwards[0][kTo] = logPotentials0Begin[kTo];
/*  742 */     for (int nTo = 1; nTo < numTokens; nTo++) {
/*  743 */       for (int kTo = 0; kTo < numTags; kTo++) {
/*  744 */         for (int kFrom = 0; kFrom < numTags; kFrom++) {
/*  745 */           buf[kFrom] = (logForwards[(nTo - 1)][kFrom] + logPotentials[(nTo - 1)][kFrom][kTo]);
/*      */         }
/*      */ 
/*  748 */         logForwards[nTo][kTo] = com.aliasi.util.Math.logSumOfExponentials(buf);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  753 */     double[][] logBackwards = new double[numTokens][numTags];
/*      */ 
/*  755 */     int nFrom = numTokens - 1;
/*      */     while (true) { nFrom--; if (nFrom < 0) break;
/*  756 */       for (int kFrom = 0; kFrom < numTags; kFrom++) {
/*  757 */         for (int kTo = 0; kTo < numTags; kTo++) {
/*  758 */           buf[kTo] = (logBackwards[(nFrom + 1)][kTo] + logPotentials[nFrom][kFrom][kTo]);
/*      */         }
/*      */ 
/*  761 */         logBackwards[nFrom][kFrom] = com.aliasi.util.Math.logSumOfExponentials(buf);
/*      */       }
/*      */     }
/*      */ 
/*  765 */     double logZ = com.aliasi.util.Math.logSumOfExponentials(logForwards[(numTokens - 1)]);
/*      */ 
/*  768 */     return new ForwardBackwardTagLattice(tokens, this.mTagList, logForwards, logBackwards, logPotentials, logZ, false);
/*      */   }
/*      */ 
/*      */   static boolean[] legalStarts(int[][] tagIdss, int numTags)
/*      */   {
/*  951 */     boolean[] legalStarts = new boolean[numTags];
/*  952 */     for (int[] tagIds : tagIdss)
/*  953 */       if (tagIds.length > 0)
/*  954 */         legalStarts[tagIds[0]] = true;
/*  955 */     return legalStarts;
/*      */   }
/*      */ 
/*      */   static boolean[] legalEnds(int[][] tagIdss, int numTags) {
/*  959 */     boolean[] legalEnds = new boolean[numTags];
/*  960 */     for (int[] tagIds : tagIdss)
/*  961 */       if (tagIds.length > 0)
/*  962 */         legalEnds[tagIds[(tagIds.length - 1)]] = true;
/*  963 */     return legalEnds;
/*      */   }
/*      */ 
/*      */   static boolean[][] legalTransitions(int[][] tagIdss, int numTags) {
/*  967 */     boolean[][] legalTransitions = new boolean[numTags][numTags];
/*  968 */     for (int[] tagIds : tagIdss) {
/*  969 */       for (int i = 1; i < tagIds.length; i++)
/*  970 */         legalTransitions[tagIds[(i - 1)]][tagIds[i]] = 1;
/*      */     }
/*  972 */     return legalTransitions;
/*      */   }
/*      */ 
/*      */   static boolean[] trueArray(int m) {
/*  976 */     boolean[] result = new boolean[m];
/*  977 */     Arrays.fill(result, true);
/*  978 */     return result;
/*      */   }
/*      */ 
/*      */   static boolean[][] trueArray(int m, int n) {
/*  982 */     boolean[][] result = new boolean[m][n];
/*  983 */     for (boolean[] row : result)
/*  984 */       Arrays.fill(row, true);
/*  985 */     return result;
/*      */   }
/*      */ 
/*      */   public static <F> ChainCrf<F> estimate(Corpus<ObjectHandler<Tagging<F>>> corpus, ChainCrfFeatureExtractor<F> featureExtractor, boolean addInterceptFeature, int minFeatureCount, boolean cacheFeatureVectors, boolean allowUnseenTransitions, RegressionPrior prior, int priorBlockSize, AnnealingSchedule annealingSchedule, double minImprovement, int minEpochs, int maxEpochs, Reporter reporter)
/*      */     throws IOException
/*      */   {
/* 1152 */     if (reporter == null) {
/* 1153 */       reporter = Reporters.silent();
/*      */     }
/* 1155 */     reporter.info("ChainCrf.estimate Parameters");
/* 1156 */     reporter.info("featureExtractor=" + featureExtractor);
/* 1157 */     reporter.info("addInterceptFeature=" + addInterceptFeature);
/* 1158 */     reporter.info("minFeatureCount=" + minFeatureCount);
/* 1159 */     reporter.info("cacheFeatureVectors=" + cacheFeatureVectors);
/* 1160 */     reporter.info("allowUnseenTransitions=" + allowUnseenTransitions);
/* 1161 */     reporter.info("prior=" + prior);
/* 1162 */     reporter.info("annealingSchedule=" + annealingSchedule);
/* 1163 */     reporter.info("minImprovement=" + minImprovement);
/* 1164 */     reporter.info("minEpochs=" + minEpochs);
/* 1165 */     reporter.info("maxEpochs=" + maxEpochs);
/* 1166 */     reporter.info("priorBlockSize=" + priorBlockSize);
/*      */ 
/* 1168 */     reporter.info("Computing corpus tokens and features");
/* 1169 */     List tokenss = corpusTokens(corpus);
/* 1170 */     String[][] tagss = corpusTags(corpus);
/* 1171 */     int numTrainingInstances = tagss.length;
/* 1172 */     int longestInput = longestInput(tagss);
/*      */ 
/* 1174 */     long numTrainingTokens = 0L;
/* 1175 */     for (String[] tags : tagss) {
/* 1176 */       numTrainingTokens += tags.length;
/*      */     }
/* 1178 */     int[][] tagIdss = new int[tagss.length][];
/* 1179 */     MapSymbolTable tagSymbolTable = tagSymbolTable(tagss, tagIdss);
/*      */ 
/* 1181 */     MapSymbolTable featureSymbolTable = featureSymbolTable(tagss, tokenss, addInterceptFeature, featureExtractor, minFeatureCount);
/*      */ 
/* 1188 */     int numTags = tagSymbolTable.numSymbols();
/* 1189 */     String[] allTags = new String[numTags];
/* 1190 */     for (int n = 0; n < numTags; n++) {
/* 1191 */       allTags[n] = tagSymbolTable.idToSymbol(n);
/*      */     }
/* 1193 */     boolean[] legalTagStarts = allowUnseenTransitions ? trueArray(numTags) : legalStarts(tagIdss, numTags);
/*      */ 
/* 1197 */     boolean[] legalTagEnds = allowUnseenTransitions ? trueArray(numTags) : legalEnds(tagIdss, numTags);
/*      */ 
/* 1201 */     boolean[][] legalTagTransitions = allowUnseenTransitions ? trueArray(numTags, numTags) : legalTransitions(tagIdss, numTags);
/*      */ 
/* 1206 */     int numDimensions = featureSymbolTable.numSymbols();
/* 1207 */     DenseVector[] weightVectors = new DenseVector[numTags];
/* 1208 */     for (int i = 0; i < weightVectors.length; i++) {
/* 1209 */       weightVectors[i] = new DenseVector(numDimensions);
/*      */     }
/*      */ 
/* 1212 */     reporter.info("Corpus Statistics");
/* 1213 */     reporter.info("Num Training Instances=" + numTrainingInstances);
/* 1214 */     reporter.info("Num Training Tokens=" + numTrainingTokens);
/* 1215 */     reporter.info("Num Dimensions After Pruning=" + numDimensions);
/* 1216 */     reporter.info("Tags=" + tagSymbolTable);
/*      */ 
/* 1219 */     ChainCrf crf = new ChainCrf(allTags, legalTagStarts, legalTagEnds, legalTagTransitions, weightVectors, featureSymbolTable, featureExtractor, addInterceptFeature);
/*      */ 
/* 1229 */     FeatureVectors[] featureVectorsCache = cacheFeatureVectors ? new FeatureVectors[numTrainingInstances] : null;
/*      */ 
/* 1233 */     if (cacheFeatureVectors) {
/* 1234 */       reporter.info("Caching Feature Vectors");
/* 1235 */       for (int j = 0; j < numTrainingInstances; j++) {
/* 1236 */         featureVectorsCache[j] = crf.features((List)tokenss.get(j));
/*      */       }
/*      */     }
/* 1239 */     double lastLog2LikelihoodAndPrior = -8.988465674311579E+307D;
/* 1240 */     double rollingAverageRelativeDiff = 1.0D;
/* 1241 */     double bestLog2LikelihoodAndPrior = (-1.0D / 0.0D);
/* 1242 */     long cumFeatureExtractionMs = 0L;
/* 1243 */     long cumForwardBackwardMs = 0L;
/* 1244 */     long cumUpdateMs = 0L;
/* 1245 */     long cumLossMs = 0L;
/* 1246 */     long cumPriorUpdateMs = 0L;
/* 1247 */     for (int epoch = 0; epoch < maxEpochs; epoch++) {
/* 1248 */       int instancesSinceLastPriorUpdate = 0;
/* 1249 */       double learningRate = annealingSchedule.learningRate(epoch);
/* 1250 */       double learningRatePerTrainingInstance = learningRate / numTrainingInstances;
/* 1251 */       for (int j = 0; j < numTrainingInstances; j++) {
/* 1252 */         int[] tagIds = tagIdss[j];
/* 1253 */         List tokens = (List)tokenss.get(j);
/* 1254 */         int numTokens = tokens.size();
/* 1255 */         if (numTokens >= 1)
/*      */         {
/* 1257 */           long startMs = System.currentTimeMillis();
/* 1258 */           FeatureVectors features = cacheFeatureVectors ? featureVectorsCache[j] : crf.features(tokens);
/*      */ 
/* 1262 */           long featsMs = System.currentTimeMillis();
/* 1263 */           cumFeatureExtractionMs += featsMs - startMs;
/* 1264 */           TagLattice lattice = crf.forwardBackward(tokens, features);
/*      */ 
/* 1266 */           long fwdBkMs = System.currentTimeMillis();
/* 1267 */           cumForwardBackwardMs += fwdBkMs - featsMs;
/*      */ 
/* 1270 */           for (int nTo = 0; nTo < numTokens; nTo++) {
/* 1271 */             weightVectors[tagIds[nTo]].increment(learningRate, features.mNodeFeatureVectors[nTo]);
/*      */           }
/*      */ 
/* 1274 */           for (int nTo = 1; nTo < numTokens; nTo++) {
/* 1275 */             weightVectors[tagIds[nTo]].increment(learningRate, features.mEdgeFeatureVectorss[(nTo - 1)][tagIds[(nTo - 1)]]);
/*      */           }
/*      */ 
/* 1280 */           for (int nTo = 0; nTo < numTokens; nTo++) {
/* 1281 */             for (int kTo = 0; kTo < numTags; kTo++) {
/* 1282 */               double logP = lattice.logProbability(nTo, kTo);
/* 1283 */               if (logP >= -400.0D) {
/* 1284 */                 double p = java.lang.Math.exp(logP);
/* 1285 */                 weightVectors[kTo].increment(-p * learningRate, features.mNodeFeatureVectors[nTo]);
/*      */               }
/*      */             }
/*      */           }
/* 1289 */           for (int nTo = 1; nTo < numTokens; nTo++) {
/* 1290 */             for (int kFrom = 0; kFrom < numTags; kFrom++) {
/* 1291 */               for (int kTo = 0; kTo < numTags; kTo++) {
/* 1292 */                 double logP = lattice.logProbability(nTo, kFrom, kTo);
/* 1293 */                 if (logP >= -400.0D) {
/* 1294 */                   double p = java.lang.Math.exp(logP);
/* 1295 */                   weightVectors[kTo].increment(-p * learningRate, features.mEdgeFeatureVectorss[(nTo - 1)][kFrom]);
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/* 1300 */           long updateMs = System.currentTimeMillis();
/* 1301 */           cumUpdateMs += updateMs - fwdBkMs;
/*      */ 
/* 1303 */           instancesSinceLastPriorUpdate++; if (instancesSinceLastPriorUpdate == priorBlockSize) {
/* 1304 */             adjustWeightsWithPrior(weightVectors, prior, instancesSinceLastPriorUpdate * learningRatePerTrainingInstance);
/*      */ 
/* 1306 */             instancesSinceLastPriorUpdate = 0;
/*      */           }
/* 1308 */           long priorMs = System.currentTimeMillis();
/* 1309 */           cumPriorUpdateMs += priorMs - updateMs;
/*      */         }
/*      */       }
/* 1311 */       long finalPriorStartMs = System.currentTimeMillis();
/* 1312 */       adjustWeightsWithPrior(weightVectors, prior, instancesSinceLastPriorUpdate * learningRatePerTrainingInstance);
/*      */ 
/* 1314 */       long finalPriorEndMs = System.currentTimeMillis();
/* 1315 */       cumPriorUpdateMs += finalPriorEndMs - finalPriorStartMs;
/*      */ 
/* 1317 */       long lossStartMs = System.currentTimeMillis();
/*      */ 
/* 1319 */       double log2Likelihood = 0.0D;
/* 1320 */       for (int j = 0; j < numTrainingInstances; j++)
/*      */       {
/* 1322 */         if (((List)tokenss.get(j)).size() >= 1) {
/* 1323 */           FeatureVectors features = cacheFeatureVectors ? featureVectorsCache[j] : crf.features((List)tokenss.get(j));
/*      */ 
/* 1327 */           TagLattice lattice = crf.forwardBackward((List)tokenss.get(j), features);
/* 1328 */           log2Likelihood += lattice.logProbability(0, tagIdss[j]);
/*      */         }
/*      */       }
/* 1330 */       double log2Prior = prior == null ? 0.0D : prior.log2Prior(weightVectors);
/* 1331 */       double log2LikelihoodAndPrior = log2Likelihood + log2Prior;
/*      */ 
/* 1333 */       double relativeDiff = com.aliasi.util.Math.relativeAbsoluteDifference(lastLog2LikelihoodAndPrior, log2LikelihoodAndPrior);
/*      */ 
/* 1336 */       rollingAverageRelativeDiff = (9.0D * rollingAverageRelativeDiff + relativeDiff) / 10.0D;
/* 1337 */       lastLog2LikelihoodAndPrior = log2LikelihoodAndPrior;
/* 1338 */       if (log2LikelihoodAndPrior > bestLog2LikelihoodAndPrior) {
/* 1339 */         bestLog2LikelihoodAndPrior = log2LikelihoodAndPrior;
/*      */       }
/* 1341 */       long lossMs = System.currentTimeMillis();
/* 1342 */       cumLossMs += lossMs - lossStartMs;
/*      */ 
/* 1344 */       if (reporter.isDebugEnabled()) {
/* 1345 */         Formatter formatter = null;
/*      */         try {
/* 1347 */           formatter = new Formatter(Locale.ENGLISH);
/* 1348 */           formatter.format("epoch=%5d lr=%11.9f ll=%11.4f lp=%11.4f llp=%11.4f llp*=%11.4f", new Object[] { Integer.valueOf(epoch), Double.valueOf(learningRate), Double.valueOf(log2Likelihood), Double.valueOf(log2Prior), Double.valueOf(log2LikelihoodAndPrior), Double.valueOf(bestLog2LikelihoodAndPrior) });
/*      */ 
/* 1355 */           reporter.debug(formatter.toString());
/*      */         } catch (IllegalFormatException e) {
/* 1357 */           reporter.warn("Illegal format in Logistic Regression");
/*      */         } finally {
/* 1359 */           if (formatter != null)
/* 1360 */             formatter.close();
/*      */         }
/*      */       }
/* 1363 */       if (rollingAverageRelativeDiff < minImprovement) {
/* 1364 */         reporter.info("Converged with rollingAverageRelativeDiff=" + rollingAverageRelativeDiff);
/*      */ 
/* 1366 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1370 */     reporter.info("Feat Extraction Time=" + Strings.msToString(cumFeatureExtractionMs));
/* 1371 */     reporter.info("Forward Backward Time=" + Strings.msToString(cumForwardBackwardMs));
/* 1372 */     reporter.info("Update Time=" + Strings.msToString(cumUpdateMs));
/* 1373 */     reporter.info("Prior Update Time=" + Strings.msToString(cumPriorUpdateMs));
/* 1374 */     reporter.info("Loss Time=" + Strings.msToString(cumLossMs));
/* 1375 */     return crf;
/*      */   }
/*      */ 
/*      */   static void adjustWeightsWithPrior(DenseVector[] weightVectors, RegressionPrior prior, double learningRateDividedByNumTrainingInstances)
/*      */   {
/* 1384 */     if (prior.isUniform()) return;
/* 1385 */     for (DenseVector weightVectorsK : weightVectors)
/* 1386 */       for (int dim = 0; dim < weightVectorsK.numDimensions(); dim++) {
/* 1387 */         double weightVectorsKDim = weightVectorsK.value(dim);
/* 1388 */         double priorMode = prior.mode(dim);
/* 1389 */         if (weightVectorsKDim != priorMode)
/*      */         {
/* 1391 */           double priorGradient = prior.gradient(weightVectorsKDim, dim);
/* 1392 */           double delta = priorGradient * learningRateDividedByNumTrainingInstances;
/*      */ 
/* 1394 */           double newVal = weightVectorsKDim > priorMode ? java.lang.Math.max(priorMode, weightVectorsKDim - delta) : java.lang.Math.min(priorMode, weightVectorsKDim - delta);
/*      */ 
/* 1397 */           weightVectorsK.setValue(dim, newVal);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   static MapSymbolTable tagSymbolTable(String[][] tagss, int[][] tagIdss)
/*      */   {
/* 1405 */     MapSymbolTable tagSymbolTable = new MapSymbolTable();
/* 1406 */     for (int j = 0; j < tagss.length; j++) {
/* 1407 */       tagIdss[j] = new int[tagss[j].length];
/* 1408 */       for (int n = 0; n < tagIdss[j].length; n++) {
/* 1409 */         tagIdss[j][n] = tagSymbolTable.getOrAddSymbol(tagss[j][n]);
/*      */       }
/*      */     }
/* 1412 */     return tagSymbolTable;
/*      */   }
/*      */ 
/*      */   static <F> MapSymbolTable featureSymbolTable(String[][] tagss, List<List<F>> tokenss, boolean addInterceptFeature, ChainCrfFeatureExtractor<F> featureExtractor, int minFeatureCount)
/*      */   {
/* 1422 */     ObjectToCounterMap featureCounter = new ObjectToCounterMap();
/*      */ 
/* 1424 */     for (int j = 0; j < tagss.length; j++) {
/* 1425 */       String[] tags = tagss[j];
/* 1426 */       List tagList = Arrays.asList(tags);
/* 1427 */       List tokens = (List)tokenss.get(j);
/* 1428 */       ChainCrfFeatures features = featureExtractor.extract(tokens, tagList);
/*      */ 
/* 1430 */       for (int n = 0; n < tags.length; n++) {
/* 1431 */         for (String feature : features.nodeFeatures(n).keySet())
/* 1432 */           featureCounter.increment(feature);
/*      */       }
/* 1434 */       for (int k = 1; k < tags.length; k++)
/*      */       {
/* 1436 */         for (String feature : features.edgeFeatures(k, k - 1).keySet()) {
/* 1437 */           featureCounter.increment(feature);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1449 */     featureCounter.prune(minFeatureCount);
/* 1450 */     MapSymbolTable featureSymbolTable = new MapSymbolTable();
/*      */ 
/* 1452 */     if (addInterceptFeature)
/* 1453 */       featureSymbolTable.getOrAddSymbol("*&^INTERCEPT%$^&**");
/* 1454 */     for (String feature : featureCounter.keySet())
/* 1455 */       featureSymbolTable.getOrAddSymbol(feature);
/* 1456 */     return featureSymbolTable;
/*      */   }
/*      */ 
/*      */   static <F> List<List<F>> corpusTokens(Corpus<ObjectHandler<Tagging<F>>> corpus)
/*      */     throws IOException
/*      */   {
/* 1462 */     List corpusTokenList = new ArrayList();
/* 1463 */     corpus.visitTrain(new ObjectHandler() {
/*      */       public void handle(Tagging<F> tagging) {
/* 1465 */         this.val$corpusTokenList.add(tagging.tokens());
/*      */       }
/*      */     });
/* 1468 */     return corpusTokenList;
/*      */   }
/*      */ 
/*      */   static <F> String[][] corpusTags(Corpus<ObjectHandler<Tagging<F>>> corpus)
/*      */     throws IOException
/*      */   {
/* 1475 */     List corpusTagList = new ArrayList(1024);
/* 1476 */     corpus.visitTrain(new ObjectHandler() {
/*      */       public void handle(Tagging<F> tagging) {
/* 1478 */         this.val$corpusTagList.add(tagging.tags().toArray(Strings.EMPTY_STRING_ARRAY));
/*      */       }
/*      */     });
/* 1481 */     return (String[][])corpusTagList.toArray(Strings.EMPTY_STRING_2D_ARRAY);
/*      */   }
/*      */ 
/*      */   static DenseVector[] copy(DenseVector[] xs)
/*      */   {
/* 1487 */     DenseVector[] result = new DenseVector[xs.length];
/* 1488 */     for (int k = 0; k < xs.length; k++)
/* 1489 */       result[k] = new DenseVector(xs[k]);
/* 1490 */     return result;
/*      */   }
/*      */ 
/*      */   static int longestInput(String[][] tagss)
/*      */   {
/* 1496 */     int longest = 0;
/* 1497 */     for (String[] tags : tagss)
/* 1498 */       if (tags.length > longest)
/* 1499 */         longest = tags.length;
/* 1500 */     return longest;
/*      */   }
/*      */ 
/*      */   static class FeatureVectors
/*      */   {
/*      */     final Vector[] mNodeFeatureVectors;
/*      */     final Vector[][] mEdgeFeatureVectorss;
/*      */ 
/*      */     FeatureVectors(Vector[] nodeFeatureVectors, Vector[][] edgeFeatureVectorss)
/*      */     {
/* 1513 */       this.mNodeFeatureVectors = nodeFeatureVectors;
/* 1514 */       this.mEdgeFeatureVectorss = edgeFeatureVectorss;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NBestState
/*      */     implements Scored
/*      */   {
/*      */     final double mScore;
/*      */     final ChainCrf.ForwardPointer mForwardPointer;
/*      */     final int mN;
/*      */     final int mK;
/*      */ 
/*      */     NBestState(double score, ChainCrf.ForwardPointer forwardPointer, int n, int k)
/*      */     {
/* 1084 */       this.mScore = score;
/* 1085 */       this.mForwardPointer = forwardPointer;
/* 1086 */       this.mN = n;
/* 1087 */       this.mK = k;
/*      */     }
/*      */     public double score() {
/* 1090 */       return this.mForwardPointer != null ? this.mScore + this.mForwardPointer.mScore : this.mScore;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ForwardPointer
/*      */   {
/*      */     final int mK;
/*      */     final ForwardPointer mPointer;
/*      */     final double mScore;
/*      */ 
/*      */     ForwardPointer(int k, ForwardPointer pointer, double score)
/*      */     {
/* 1067 */       this.mK = k;
/* 1068 */       this.mPointer = pointer;
/* 1069 */       this.mScore = score;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Serializer<F> extends AbstractExternalizable
/*      */   {
/*      */     static final long serialVersionUID = -4140295941325870709L;
/*      */     final ChainCrf<F> mCrf;
/*      */ 
/*      */     public Serializer(ChainCrf<F> crf)
/*      */     {
/*  992 */       this.mCrf = crf;
/*      */     }
/*      */     public Serializer() {
/*  995 */       this(null);
/*      */     }
/*      */ 
/*      */     public void writeExternal(ObjectOutput out) throws IOException
/*      */     {
/* 1000 */       int numTags = this.mCrf.mTagList.size();
/*      */ 
/* 1002 */       out.writeInt(numTags);
/* 1003 */       for (String tag : this.mCrf.mTagList)
/* 1004 */         out.writeUTF(tag);
/* 1005 */       for (int i = 0; i < numTags; i++)
/* 1006 */         out.writeBoolean(this.mCrf.mLegalTagStarts[i]);
/* 1007 */       for (int i = 0; i < numTags; i++) {
/* 1008 */         out.writeBoolean(this.mCrf.mLegalTagEnds[i]);
/*      */       }
/* 1010 */       for (int i = 0; i < numTags; i++) {
/* 1011 */         for (int j = 0; j < numTags; j++)
/* 1012 */           out.writeBoolean(this.mCrf.mLegalTagTransitions[i][j]);
/*      */       }
/* 1014 */       for (Vector v : this.mCrf.mCoefficients) {
/* 1015 */         out.writeObject(v);
/*      */       }
/* 1017 */       out.writeObject(this.mCrf.mFeatureSymbolTable);
/* 1018 */       out.writeObject(this.mCrf.mFeatureExtractor);
/* 1019 */       out.writeBoolean(this.mCrf.mAddInterceptFeature);
/*      */     }
/*      */ 
/*      */     public Object read(ObjectInput in) throws ClassNotFoundException, IOException
/*      */     {
/* 1024 */       int numTags = in.readInt();
/* 1025 */       String[] tags = new String[numTags];
/* 1026 */       for (int i = 0; i < tags.length; i++)
/* 1027 */         tags[i] = in.readUTF();
/* 1028 */       boolean[] legalTagStarts = new boolean[numTags];
/* 1029 */       for (int i = 0; i < numTags; i++)
/* 1030 */         legalTagStarts[i] = in.readBoolean();
/* 1031 */       boolean[] legalTagEnds = new boolean[numTags];
/* 1032 */       for (int i = 0; i < numTags; i++)
/* 1033 */         legalTagEnds[i] = in.readBoolean();
/* 1034 */       boolean[][] legalTagTransitions = new boolean[numTags][numTags];
/* 1035 */       for (int i = 0; i < numTags; i++) {
/* 1036 */         for (int j = 0; j < numTags; j++) {
/* 1037 */           legalTagTransitions[i][j] = in.readBoolean();
/*      */         }
/*      */       }
/* 1040 */       Vector[] coefficients = new Vector[numTags];
/* 1041 */       for (int i = 0; i < tags.length; i++) {
/* 1042 */         coefficients[i] = ((Vector)in.readObject());
/*      */       }
/* 1044 */       SymbolTable featureSymbolTable = (SymbolTable)in.readObject();
/*      */ 
/* 1047 */       ChainCrfFeatureExtractor featureExtractor = (ChainCrfFeatureExtractor)in.readObject();
/*      */ 
/* 1049 */       boolean addInterceptFeature = in.readBoolean();
/* 1050 */       return new ChainCrf(tags, legalTagStarts, legalTagEnds, legalTagTransitions, coefficients, featureSymbolTable, featureExtractor, addInterceptFeature);
/*      */     }
/*      */   }
/*      */ 
/*      */   class NBestIterator extends Iterators.Buffered<ScoredTagging<E>>
/*      */   {
/*      */     final List<E> mTokens;
/*      */     final double mLogZ;
/*      */     final double[][][] mTransitionScores;
/*      */     final double[][] mViterbiScores;
/*      */     final int[][] mBackPointers;
/*      */     final BoundedPriorityQueue<ChainCrf.NBestState> mPriorityQueue;
/*      */ 
/*      */     NBestIterator(boolean tokens, int normToConditional)
/*      */     {
/*  790 */       this.mPriorityQueue = new BoundedPriorityQueue(ScoredObject.comparator(), maxResults);
/*      */ 
/*  793 */       this.mTokens = tokens;
/*  794 */       int numTokens = tokens.size();
/*  795 */       int numTags = ChainCrf.this.mTagList.size();
/*  796 */       this.mTransitionScores = new double[numTokens - 1][numTags][numTags];
/*  797 */       for (double[][] xss : this.mTransitionScores)
/*  798 */         for (double[] xs : xss)
/*  799 */           Arrays.fill(xs, (-1.0D / 0.0D));
/*  800 */       this.mViterbiScores = new double[numTokens][numTags];
/*  801 */       for (double[] xs : this.mViterbiScores) {
/*  802 */         Arrays.fill(xs, (-1.0D / 0.0D));
/*      */       }
/*  804 */       this.mBackPointers = new int[numTokens - 1][numTags];
/*  805 */       for (int[] ptrs : this.mBackPointers)
/*  806 */         Arrays.fill(ptrs, -1);
/*  807 */       Vector[] edgeVectors = new Vector[numTags];
/*  808 */       ChainCrfFeatures features = ChainCrf.this.mFeatureExtractor.extract(tokens, ChainCrf.this.mTagList);
/*  809 */       for (int n = 1; n < numTokens; n++) {
/*  810 */         Vector nodeVector = ChainCrf.this.nodeFeatures(n, features);
/*  811 */         for (int kMinus1 = 0; kMinus1 < numTags; kMinus1++)
/*  812 */           if ((n != 1) || (ChainCrf.this.mLegalTagStarts[kMinus1] != 0))
/*      */           {
/*  814 */             edgeVectors[kMinus1] = ChainCrf.this.edgeFeatures(n, kMinus1, features);
/*      */           }
/*  816 */         for (int k = 0; k < numTags; k++) {
/*  817 */           if ((n != numTokens - 1) || (ChainCrf.this.mLegalTagEnds[k] != 0))
/*      */           {
/*  819 */             double nodeScore = nodeVector.dotProduct(ChainCrf.this.mCoefficients[k]);
/*  820 */             for (int kMinus1 = 0; kMinus1 < numTags; kMinus1++)
/*  821 */               if (ChainCrf.this.mLegalTagTransitions[kMinus1][k] != 0)
/*      */               {
/*  823 */                 if ((n != 1) || (ChainCrf.this.mLegalTagStarts[kMinus1] != 0))
/*      */                 {
/*  825 */                   this.mTransitionScores[(n - 1)][kMinus1][k] = (nodeScore + edgeVectors[kMinus1].dotProduct(ChainCrf.this.mCoefficients[k]));
/*      */                 }
/*      */               }
/*      */           }
/*      */         }
/*      */       }
/*  831 */       Vector nodeVector0 = ChainCrf.this.nodeFeatures(0, features);
/*  832 */       for (int k = 0; k < numTags; k++) {
/*  833 */         if (ChainCrf.this.mLegalTagStarts[k] != 0)
/*      */         {
/*  835 */           this.mViterbiScores[0][k] = nodeVector0.dotProduct(ChainCrf.this.mCoefficients[k]);
/*      */         }
/*      */       }
/*  838 */       for (int n = 1; n < numTokens; n++) {
/*  839 */         for (int k = 0; k < numTags; k++)
/*  840 */           if ((n != numTokens - 1) || (ChainCrf.this.mLegalTagEnds[k] != 0))
/*      */           {
/*  842 */             double bestScore = (-1.0D / 0.0D);
/*  843 */             int backPtr = -1;
/*  844 */             for (int kMinus1 = 0; kMinus1 < numTags; kMinus1++)
/*  845 */               if (ChainCrf.this.mLegalTagTransitions[kMinus1][k] != 0)
/*      */               {
/*  847 */                 double score = this.mViterbiScores[(n - 1)][kMinus1] + this.mTransitionScores[(n - 1)][kMinus1][k];
/*      */ 
/*  849 */                 if (score > bestScore) {
/*  850 */                   bestScore = score;
/*  851 */                   backPtr = kMinus1;
/*      */                 }
/*      */               }
/*  854 */             this.mViterbiScores[n][k] = bestScore;
/*  855 */             this.mBackPointers[(n - 1)][k] = backPtr;
/*      */           }
/*      */       }
/*  858 */       this.mLogZ = (normToConditional ? logZ() : 0.0D);
/*  859 */       for (int k = 0; k < numTags; k++)
/*  860 */         offer(this.mViterbiScores[(numTokens - 1)][k], null, numTokens - 1, k);
/*      */     }
/*      */ 
/*      */     double logZ() {
/*  864 */       double[] forwards = (double[])this.mViterbiScores[0].clone();
/*  865 */       int numTags = forwards.length;
/*  866 */       double[] previousForwards = new double[numTags];
/*  867 */       double[] exps = new double[numTags];
/*  868 */       for (int n = 0; n < this.mTransitionScores.length; n++) {
/*  869 */         double[] temp = previousForwards;
/*  870 */         previousForwards = forwards;
/*  871 */         forwards = temp;
/*  872 */         for (int k = 0; k < numTags; k++) {
/*  873 */           for (int kMinus1 = 0; kMinus1 < numTags; kMinus1++) {
/*  874 */             previousForwards[kMinus1] += this.mTransitionScores[n][kMinus1][k];
/*      */           }
/*  876 */           forwards[k] = com.aliasi.util.Math.logSumOfExponentials(exps);
/*      */         }
/*      */       }
/*  879 */       double logZ = com.aliasi.util.Math.logSumOfExponentials(forwards);
/*  880 */       return logZ;
/*      */     }
/*      */     void offer(double score, ChainCrf.ForwardPointer pointer, int n, int k) {
/*  883 */       if (score == (-1.0D / 0.0D))
/*  884 */         return;
/*  885 */       if ((pointer != null) && (pointer.mScore == (-1.0D / 0.0D)))
/*  886 */         return;
/*  887 */       ChainCrf.NBestState state = new ChainCrf.NBestState(score, pointer, n, k);
/*  888 */       this.mPriorityQueue.offer(state);
/*      */     }
/*      */     public ScoredTagging<E> bufferNext() {
/*  891 */       ChainCrf.NBestState resultState = (ChainCrf.NBestState)this.mPriorityQueue.poll();
/*  892 */       if (resultState == null) return null;
/*      */ 
/*  896 */       int n = resultState.mN - 1;
/*  897 */       int k = resultState.mK;
/*  898 */       ChainCrf.ForwardPointer fwdPointer = resultState.mForwardPointer;
/*  899 */       while (n >= 0) {
/*  900 */         addAlternatives(n, k, fwdPointer);
/*  901 */         int kMinus1 = this.mBackPointers[n][k];
/*  902 */         double fwdScore = this.mTransitionScores[n][kMinus1][k];
/*  903 */         if (fwdPointer != null)
/*  904 */           fwdScore += fwdPointer.mScore;
/*  905 */         fwdPointer = new ChainCrf.ForwardPointer(k, fwdPointer, fwdScore);
/*  906 */         k = kMinus1;
/*  907 */         n--;
/*      */       }
/*  909 */       ScoredTagging scoredTagging = toScoredTagging(resultState);
/*  910 */       return scoredTagging;
/*      */     }
/*      */     void addAlternatives(int n, int k, ChainCrf.ForwardPointer fwdPointer) {
/*  913 */       int numTags = ChainCrf.this.mTagList.size();
/*  914 */       for (int kMinus1 = 0; kMinus1 < numTags; kMinus1++)
/*  915 */         if (kMinus1 != this.mBackPointers[n][k]) {
/*  916 */           double score = this.mViterbiScores[n][kMinus1];
/*  917 */           double fwdScore = this.mTransitionScores[n][kMinus1][k];
/*  918 */           if (fwdPointer != null)
/*  919 */             fwdScore += fwdPointer.mScore;
/*  920 */           ChainCrf.ForwardPointer pointer = new ChainCrf.ForwardPointer(k, fwdPointer, fwdScore);
/*      */ 
/*  922 */           offer(score, pointer, n, kMinus1);
/*      */         }
/*      */     }
/*      */ 
/*      */     public ScoredTagging<E> toScoredTagging(ChainCrf.NBestState state) {
/*  927 */       List tags = new ArrayList(this.mTokens.size());
/*  928 */       int k = state.mK;
/*  929 */       tags.add(ChainCrf.this.mTagList.get(k));
/*  930 */       for (int n = state.mN; n > 0; n--) {
/*  931 */         k = this.mBackPointers[(n - 1)][k];
/*  932 */         tags.add(ChainCrf.this.mTagList.get(k));
/*      */       }
/*  934 */       Collections.reverse(tags);
/*  935 */       for (ChainCrf.ForwardPointer pointer = state.mForwardPointer; 
/*  936 */         pointer != null; 
/*  937 */         pointer = pointer.mPointer) {
/*  938 */         tags.add(ChainCrf.this.mTagList.get(pointer.mK));
/*      */       }
/*  940 */       return new ScoredTagging(this.mTokens, tags, state.score() - this.mLogZ);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.crf.ChainCrf
 * JD-Core Version:    0.6.2
 */