/*      */ package com.aliasi.classify;
/*      */ 
/*      */ import com.aliasi.corpus.Corpus;
/*      */ import com.aliasi.corpus.ObjectHandler;
/*      */ import com.aliasi.io.Reporter;
/*      */ import com.aliasi.io.Reporters;
/*      */ import com.aliasi.stats.Statistics;
/*      */ import com.aliasi.tokenizer.Tokenizer;
/*      */ import com.aliasi.tokenizer.TokenizerFactory;
/*      */ import com.aliasi.util.AbstractExternalizable;
/*      */ import com.aliasi.util.Compilable;
/*      */ import com.aliasi.util.Counter;
/*      */ import com.aliasi.util.Exceptions;
/*      */ import com.aliasi.util.Factory;
/*      */ import com.aliasi.util.Iterators.Buffered;
/*      */ import com.aliasi.util.ObjectToCounterMap;
/*      */ import com.aliasi.util.Strings;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Formatter;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class TradNaiveBayesClassifier
/*      */   implements JointClassifier<CharSequence>, ObjectHandler<Classified<CharSequence>>, Serializable, Compilable
/*      */ {
/*      */   static final long serialVersionUID = -300327951207213311L;
/*      */   private final Set<String> mCategorySet;
/*      */   private final String[] mCategories;
/*      */   private final TokenizerFactory mTokenizerFactory;
/*      */   private final double mCategoryPrior;
/*      */   private final double mTokenInCategoryPrior;
/*      */   private Map<String, double[]> mTokenToCountsMap;
/*      */   private double[] mTotalCountsPerCategory;
/*      */   private double[] mCaseCounts;
/*      */   private double mTotalCaseCount;
/*      */   private double mLengthNorm;
/*      */ 
/*      */   public String toString()
/*      */   {
/*  374 */     StringBuilder sb = new StringBuilder();
/*  375 */     sb.append("categories=" + Arrays.asList(this.mCategories) + "\n");
/*  376 */     sb.append("category Prior=" + this.mCategoryPrior + "\n");
/*  377 */     sb.append("token in category prior=" + this.mTokenInCategoryPrior + "\n");
/*  378 */     sb.append("total case count=" + this.mTotalCaseCount + "\n");
/*  379 */     for (int i = 0; i < this.mCategories.length; i++) {
/*  380 */       sb.append("category count(" + this.mCategories[i] + ")=" + this.mCaseCounts[i] + "\n");
/*      */     }
/*  382 */     for (String token : this.mTokenToCountsMap.keySet()) {
/*  383 */       sb.append("token=" + token + "\n");
/*  384 */       double[] counts = (double[])this.mTokenToCountsMap.get(token);
/*  385 */       for (int i = 0; i < this.mCategories.length; i++) {
/*  386 */         sb.append("  tokenCount(" + this.mCategories[i] + "," + token + ")=" + counts[i] + "\n");
/*      */       }
/*      */     }
/*  389 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   private TradNaiveBayesClassifier(String[] categories, TokenizerFactory tokenizerFactory, double categoryPrior, double tokenInCategoryPrior, Map<String, double[]> tokenToCountsMap, double[] totalCountsPerCategory, double[] caseCounts, double totalCaseCount, double lengthNorm)
/*      */   {
/*  402 */     this.mCategories = categories;
/*  403 */     this.mCategorySet = new HashSet(Arrays.asList(categories));
/*  404 */     this.mTokenizerFactory = tokenizerFactory;
/*  405 */     this.mCategoryPrior = categoryPrior;
/*  406 */     this.mTokenInCategoryPrior = tokenInCategoryPrior;
/*  407 */     this.mTokenToCountsMap = tokenToCountsMap;
/*  408 */     this.mTotalCountsPerCategory = totalCountsPerCategory;
/*  409 */     this.mCaseCounts = caseCounts;
/*  410 */     this.mTotalCaseCount = totalCaseCount;
/*  411 */     this.mLengthNorm = lengthNorm;
/*      */   }
/*      */ 
/*      */   public TradNaiveBayesClassifier(Set<String> categorySet, TokenizerFactory tokenizerFactory)
/*      */   {
/*  431 */     this(categorySet, tokenizerFactory, 0.5D, 0.5D, (0.0D / 0.0D));
/*      */   }
/*      */ 
/*      */   public TradNaiveBayesClassifier(Set<String> categorySet, TokenizerFactory tokenizerFactory, double categoryPrior, double tokenInCategoryPrior, double lengthNorm)
/*      */   {
/*  457 */     if (categorySet.size() < 2) {
/*  458 */       String msg = "Require at least two categorySet. Found categorySet.size()=" + categorySet.size();
/*      */ 
/*  460 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  462 */     Exceptions.finiteNonNegative("categoryPrior", categoryPrior);
/*  463 */     Exceptions.finiteNonNegative("tokenInCategoryPrior", tokenInCategoryPrior);
/*      */ 
/*  468 */     setLengthNorm(lengthNorm);
/*      */ 
/*  470 */     this.mTotalCaseCount = 0.0D;
/*      */ 
/*  472 */     this.mCategorySet = new HashSet(categorySet);
/*      */ 
/*  474 */     this.mCategories = ((String[])this.mCategorySet.toArray(Strings.EMPTY_STRING_ARRAY));
/*  475 */     Arrays.sort(this.mCategories);
/*      */ 
/*  477 */     this.mTokenizerFactory = tokenizerFactory;
/*  478 */     this.mCategoryPrior = categoryPrior;
/*  479 */     this.mTokenInCategoryPrior = tokenInCategoryPrior;
/*      */ 
/*  481 */     this.mTokenToCountsMap = new HashMap();
/*  482 */     this.mTotalCountsPerCategory = new double[this.mCategories.length];
/*  483 */     this.mCaseCounts = new double[this.mCategories.length];
/*      */   }
/*      */ 
/*      */   public Set<String> categorySet()
/*      */   {
/*  492 */     return Collections.unmodifiableSet(this.mCategorySet);
/*      */   }
/*      */ 
/*      */   public void setLengthNorm(double lengthNorm)
/*      */   {
/*  505 */     if ((lengthNorm <= 0.0D) || (Double.isInfinite(lengthNorm))) {
/*  506 */       String msg = "Length norm must be finite and positive, or Double.NaN. Found lengthNorm=" + lengthNorm;
/*      */ 
/*  508 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  510 */     this.mLengthNorm = lengthNorm;
/*      */   }
/*      */ 
/*      */   public JointClassification classify(CharSequence in)
/*      */   {
/*  520 */     double[] logps = new double[this.mCategories.length];
/*  521 */     char[] cs = Strings.toCharArray(in);
/*  522 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*  523 */     int tokenCount = 0;
/*  524 */     for (String token : tokenizer) {
/*  525 */       double[] tokenCounts = (double[])this.mTokenToCountsMap.get(token);
/*  526 */       tokenCount++;
/*  527 */       if (tokenCounts != null)
/*      */       {
/*  529 */         for (int i = 0; i < this.mCategories.length; i++)
/*  530 */           logps[i] += com.aliasi.util.Math.log2(probTokenByIndexArray(i, tokenCounts)); 
/*      */       }
/*      */     }
/*  532 */     if ((!Double.isNaN(this.mLengthNorm)) && (tokenCount > 0)) {
/*  533 */       for (int i = 0; i < logps.length; i++)
/*  534 */         logps[i] *= this.mLengthNorm / tokenCount;
/*      */     }
/*  536 */     for (int i = 0; i < logps.length; i++) {
/*  537 */       logps[i] += com.aliasi.util.Math.log2(probCatByIndex(i));
/*      */     }
/*  539 */     return JointClassification.create(this.mCategories, logps);
/*      */   }
/*      */ 
/*      */   public double lengthNorm()
/*      */   {
/*  550 */     return this.mLengthNorm;
/*      */   }
/*      */ 
/*      */   public boolean isKnownToken(String token)
/*      */   {
/*  562 */     return this.mTokenToCountsMap.containsKey(token);
/*      */   }
/*      */ 
/*      */   public Set<String> knownTokenSet()
/*      */   {
/*  573 */     return Collections.unmodifiableSet(this.mTokenToCountsMap.keySet());
/*      */   }
/*      */ 
/*      */   public double probToken(String token, String cat)
/*      */   {
/*  585 */     int catIndex = getIndex(cat);
/*  586 */     double[] tokenCounts = (double[])this.mTokenToCountsMap.get(token);
/*  587 */     if (tokenCounts == null) {
/*  588 */       String msg = "Requires known token. Found token=" + token;
/*      */ 
/*  590 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  592 */     return probTokenByIndexArray(catIndex, tokenCounts);
/*      */   }
/*      */ 
/*      */   public void compileTo(ObjectOutput out)
/*      */     throws IOException
/*      */   {
/*  604 */     out.writeObject(new Compiler(this));
/*      */   }
/*      */ 
/*      */   public double probCat(String cat)
/*      */   {
/*  618 */     int catIndex = getIndex(cat);
/*  619 */     return probCatByIndex(catIndex);
/*      */   }
/*      */ 
/*      */   public void handle(Classified<CharSequence> classifiedObject)
/*      */   {
/*  632 */     handle((CharSequence)classifiedObject.getObject(), classifiedObject.getClassification());
/*      */   }
/*      */ 
/*      */   void handle(CharSequence cSeq, Classification classification)
/*      */   {
/*  647 */     train(cSeq, classification, 1.0D);
/*      */   }
/*      */ 
/*      */   public void trainConditional(CharSequence cSeq, ConditionalClassification classification, double countMultiplier, double minCount)
/*      */   {
/*  672 */     if ((countMultiplier < 0.0D) || (Double.isNaN(countMultiplier)) || (Double.isInfinite(countMultiplier)))
/*      */     {
/*  675 */       String msg = "Count multipliers must be finite and non-negative. Found countMultiplier=" + countMultiplier;
/*      */ 
/*  677 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  679 */     if ((minCount < 0.0D) || (Double.isNaN(minCount)) || (Double.isInfinite(minCount))) {
/*  680 */       String msg = "Minimum count must be finite non-negative. Found minCount=" + minCount;
/*      */ 
/*  682 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  684 */     int numCats = 0;
/*      */ 
/*  686 */     while ((numCats < classification.size()) && (classification.conditionalProbability(numCats) * countMultiplier >= minCount)) {
/*  687 */       numCats++;
/*      */     }
/*  689 */     ObjectToCounterMap tokenCountMap = tokenCountMap(cSeq);
/*  690 */     double lengthMultiplier = lengthMultiplier(tokenCountMap);
/*      */ 
/*  693 */     double[] lengthNormCatMultipliers = new double[numCats];
/*  694 */     int[] catIndexes = new int[numCats];
/*  695 */     for (int j = 0; j < numCats; j++) {
/*  696 */       catIndexes[j] = getIndex(classification.category(j));
/*  697 */       double count = countMultiplier * classification.conditionalProbability(j);
/*  698 */       this.mTotalCaseCount += count;
/*  699 */       this.mCaseCounts[catIndexes[j]] += count;
/*  700 */       lengthNormCatMultipliers[j] = (lengthMultiplier * count);
/*      */     }
/*  702 */     for (Map.Entry entry : tokenCountMap.entrySet()) {
/*  703 */       String token = (String)entry.getKey();
/*  704 */       double tokenCount = ((Counter)entry.getValue()).doubleValue();
/*  705 */       double[] tokenCounts = (double[])this.mTokenToCountsMap.get(token);
/*  706 */       if (tokenCounts == null) {
/*  707 */         tokenCounts = new double[this.mCategories.length];
/*  708 */         this.mTokenToCountsMap.put(token, tokenCounts);
/*      */       }
/*  710 */       for (int j = 0; j < numCats; j++) {
/*  711 */         double addend = tokenCount * lengthNormCatMultipliers[j];
/*  712 */         tokenCounts[catIndexes[j]] += addend;
/*  713 */         this.mTotalCountsPerCategory[catIndexes[j]] += addend;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void train(CharSequence cSeq, Classification classification, double count)
/*      */   {
/*  740 */     if (count == 0.0D) return;
/*  741 */     String cat = classification.bestCategory();
/*  742 */     int catIndex = getIndex(cat);
/*      */ 
/*  745 */     if (this.mCaseCounts[catIndex] < -count) {
/*  746 */       String msg = "Decrement caused negative token count.Revert to previous state. cSeq=" + cSeq + " classification=" + cat + " count=" + count;
/*      */ 
/*  751 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  754 */     this.mCaseCounts[catIndex] += count;
/*  755 */     this.mTotalCaseCount += count;
/*      */ 
/*  757 */     ObjectToCounterMap tokenCountMap = tokenCountMap(cSeq);
/*  758 */     double lengthMultiplier = lengthMultiplier(tokenCountMap);
/*      */ 
/*  760 */     double lengthNormCount = lengthMultiplier * count;
/*      */ 
/*  762 */     char[] cs = Strings.toCharArray(cSeq);
/*  763 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*  764 */     int pos = 0;
/*  765 */     for (String token : tokenizer) {
/*  766 */       double[] tokenCounts = (double[])this.mTokenToCountsMap.get(token);
/*      */ 
/*  769 */       if ((lengthNormCount < 0.0D) && ((tokenCounts == null) || (tokenCounts[catIndex] < -lengthNormCount)))
/*      */       {
/*  773 */         this.mCaseCounts[catIndex] -= count;
/*  774 */         this.mTotalCaseCount -= count;
/*      */ 
/*  776 */         Tokenizer tokenizer2 = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*  777 */         int fixPos = 0;
/*  778 */         for (String token2 : tokenizer2) {
/*  779 */           if (fixPos >= pos) break;
/*  780 */           fixPos++;
/*  781 */           double[] tokenCounts2 = (double[])this.mTokenToCountsMap.get(token2);
/*  782 */           tokenCounts2[catIndex] -= lengthNormCount;
/*  783 */           this.mTotalCountsPerCategory[catIndex] -= lengthNormCount;
/*      */         }
/*  785 */         String msg = "Decrement caused negative token count.Revert to previous state. cSeq=" + cSeq + " classification=" + cat + " count=" + count;
/*      */ 
/*  790 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */ 
/*  793 */       pos++;
/*  794 */       if (tokenCounts == null) {
/*  795 */         tokenCounts = new double[this.mCategories.length];
/*  796 */         this.mTokenToCountsMap.put(token, tokenCounts);
/*      */       }
/*  798 */       tokenCounts[catIndex] += lengthNormCount;
/*  799 */       this.mTotalCountsPerCategory[catIndex] += lengthNormCount;
/*      */     }
/*      */   }
/*      */ 
/*      */   public double log2CaseProb(CharSequence input)
/*      */   {
/*  823 */     JointClassification c = classify(input);
/*  824 */     double maxJointLog2P = (-1.0D / 0.0D);
/*  825 */     for (int rank = 0; rank < c.size(); rank++) {
/*  826 */       double jointLog2P = c.jointLog2Probability(rank);
/*  827 */       if (jointLog2P > maxJointLog2P)
/*  828 */         maxJointLog2P = jointLog2P;
/*      */     }
/*  830 */     double sum = 0.0D;
/*  831 */     for (int rank = 0; rank < c.size(); rank++)
/*  832 */       sum += java.lang.Math.pow(2.0D, c.jointLog2Probability(rank) - maxJointLog2P);
/*  833 */     return maxJointLog2P + com.aliasi.util.Math.log2(sum);
/*      */   }
/*      */ 
/*      */   public double log2ModelProb()
/*      */   {
/*  853 */     double[] catProbs = new double[this.mCategories.length];
/*  854 */     for (int i = 0; i < this.mCategories.length; i++) {
/*  855 */       catProbs[i] = probCatByIndex(i);
/*      */     }
/*  857 */     double sum = Statistics.dirichletLog2Prob(this.mCategoryPrior, catProbs);
/*      */ 
/*  859 */     double[] wordProbs = new double[this.mTokenToCountsMap.size()];
/*  860 */     for (int catIndex = 0; catIndex < this.mCategories.length; catIndex++) {
/*  861 */       int j = 0;
/*  862 */       for (double[] counts : this.mTokenToCountsMap.values()) {
/*  863 */         double totalCountForCat = this.mTotalCountsPerCategory[catIndex];
/*  864 */         wordProbs[(j++)] = ((counts[catIndex] + this.mTokenInCategoryPrior) / (totalCountForCat + this.mCaseCounts.length * this.mTokenInCategoryPrior));
/*      */       }
/*  866 */       sum += Statistics.dirichletLog2Prob(this.mTokenInCategoryPrior, wordProbs);
/*      */     }
/*  868 */     return sum;
/*      */   }
/*      */ 
/*      */   private Object writeReplace() throws ObjectStreamException {
/*  872 */     return new Serializer(this);
/*      */   }
/*      */ 
/*      */   private double probTokenByIndexArray(int catIndex, double[] tokenCounts) {
/*  876 */     double tokenCatCount = tokenCounts[catIndex];
/*  877 */     double totalCatCount = this.mTotalCountsPerCategory[catIndex];
/*  878 */     return (tokenCatCount + this.mTokenInCategoryPrior) / (totalCatCount + this.mTokenToCountsMap.size() * this.mTokenInCategoryPrior);
/*      */   }
/*      */ 
/*      */   private double probCatByIndex(int catIndex)
/*      */   {
/*  883 */     double caseCountCat = this.mCaseCounts[catIndex];
/*  884 */     return (caseCountCat + this.mCategoryPrior) / (this.mTotalCaseCount + this.mCategories.length * this.mCategoryPrior);
/*      */   }
/*      */ 
/*      */   private ObjectToCounterMap<String> tokenCountMap(CharSequence cSeq)
/*      */   {
/*  889 */     ObjectToCounterMap tokenCountMap = new ObjectToCounterMap();
/*  890 */     char[] cs = Strings.toCharArray(cSeq);
/*  891 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*  892 */     for (String token : tokenizer)
/*  893 */       tokenCountMap.increment(token);
/*  894 */     return tokenCountMap;
/*      */   }
/*      */ 
/*      */   private double lengthMultiplier(ObjectToCounterMap<String> tokenCountMap)
/*      */   {
/*  900 */     if (Double.isNaN(this.mLengthNorm)) return 1.0D;
/*  901 */     int length = 0;
/*  902 */     for (Counter counter : tokenCountMap.values())
/*  903 */       length += counter.intValue();
/*  904 */     return length != 0.0D ? this.mLengthNorm / length : 1.0D;
/*      */   }
/*      */ 
/*      */   private int getIndex(String cat)
/*      */   {
/*  910 */     int catIndex = Arrays.binarySearch(this.mCategories, cat);
/*  911 */     if (catIndex < 0) {
/*  912 */       String msg = "Unknown category.  Require category in category set. Found category=" + cat + " category set=" + this.mCategorySet;
/*      */ 
/*  915 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  917 */     return catIndex;
/*      */   }
/*      */ 
/*      */   public static Iterator<TradNaiveBayesClassifier> emIterator(TradNaiveBayesClassifier initialClassifier, Factory<TradNaiveBayesClassifier> classifierFactory, Corpus<ObjectHandler<Classified<CharSequence>>> labeledData, Corpus<ObjectHandler<CharSequence>> unlabeledData, double minTokenCount)
/*      */     throws IOException
/*      */   {
/*  943 */     return new EmIterator(initialClassifier, classifierFactory, labeledData, unlabeledData, minTokenCount);
/*      */   }
/*      */ 
/*      */   public static TradNaiveBayesClassifier emTrain(TradNaiveBayesClassifier initialClassifier, Factory<TradNaiveBayesClassifier> classifierFactory, Corpus<ObjectHandler<Classified<CharSequence>>> labeledData, Corpus<ObjectHandler<CharSequence>> unlabeledData, double minTokenCount, int maxEpochs, double minImprovement, Reporter reporter)
/*      */     throws IOException
/*      */   {
/*  977 */     if (reporter == null) {
/*  978 */       reporter = Reporters.silent();
/*      */     }
/*  980 */     long startTime = System.currentTimeMillis();
/*      */ 
/*  982 */     double lastLogProb = (-1.0D / 0.0D);
/*  983 */     Iterator it = emIterator(initialClassifier, classifierFactory, labeledData, unlabeledData, minTokenCount);
/*      */ 
/*  985 */     TradNaiveBayesClassifier classifier = null;
/*  986 */     for (int epoch = 0; (it.hasNext()) && (epoch < maxEpochs); epoch++) {
/*  987 */       classifier = (TradNaiveBayesClassifier)it.next();
/*  988 */       double modelLogProb = classifier.log2ModelProb();
/*  989 */       double dataLogProb = dataProb(classifier, labeledData, unlabeledData);
/*  990 */       double logProb = modelLogProb + dataLogProb;
/*  991 */       double relativeDiff = relativeDiff(lastLogProb, logProb);
/*  992 */       if (reporter.isDebugEnabled()) {
/*  993 */         Formatter formatter = new Formatter();
/*  994 */         formatter.format("epoch=%4d   dataLogProb=%15.2f   modelLogProb=%15.2f   logProb=%15.2f   diff=%15.12f", new Object[] { Integer.valueOf(epoch), Double.valueOf(dataLogProb), Double.valueOf(modelLogProb), Double.valueOf(logProb), Double.valueOf(relativeDiff) });
/*      */ 
/*  996 */         String msg = formatter.toString();
/*  997 */         reporter.debug(msg);
/*      */       }
/*  999 */       if ((!Double.isNaN(lastLogProb)) && (relativeDiff < minImprovement)) {
/* 1000 */         reporter.info("Converged");
/* 1001 */         return classifier;
/*      */       }
/* 1003 */       lastLogProb = logProb;
/*      */     }
/*      */ 
/* 1006 */     return classifier;
/*      */   }
/*      */ 
/*      */   static double dataProb(TradNaiveBayesClassifier classifier, Corpus<ObjectHandler<Classified<CharSequence>>> labeledData, Corpus<ObjectHandler<CharSequence>> unlabeledData)
/*      */     throws IOException
/*      */   {
/* 1013 */     CaseProbAccumulator accum = new CaseProbAccumulator(classifier);
/* 1014 */     labeledData.visitTrain(accum.supHandler());
/* 1015 */     unlabeledData.visitTrain(accum);
/* 1016 */     return accum.mCaseProb;
/*      */   }
/*      */ 
/*      */   static double relativeDiff(double x, double y) {
/* 1020 */     return 2.0D * java.lang.Math.abs(x - y) / (java.lang.Math.abs(x) + java.lang.Math.abs(y));
/*      */   }
/*      */ 
/*      */   private static class CompiledTradNaiveBayesClassifier
/*      */     implements JointClassifier<CharSequence>
/*      */   {
/*      */     private final TokenizerFactory mTokenizerFactory;
/*      */     private final String[] mCategories;
/*      */     private final Map<String, double[]> mTokenToLog2ProbsInCats;
/*      */     private final double[] mLog2CatProbs;
/*      */     private final double mLengthNorm;
/*      */ 
/*      */     CompiledTradNaiveBayesClassifier(String[] categories, TokenizerFactory tokenizerFactory, Map<String, double[]> tokenToLog2ProbsInCats, double[] log2CatProbs, double lengthNorm)
/*      */     {
/* 1331 */       this.mCategories = categories;
/* 1332 */       this.mTokenizerFactory = tokenizerFactory;
/* 1333 */       this.mTokenToLog2ProbsInCats = tokenToLog2ProbsInCats;
/* 1334 */       this.mLog2CatProbs = log2CatProbs;
/* 1335 */       this.mLengthNorm = lengthNorm;
/*      */     }
/*      */ 
/*      */     public JointClassification classify(CharSequence in) {
/* 1339 */       double[] logps = new double[this.mCategories.length];
/* 1340 */       char[] cs = Strings.toCharArray(in);
/* 1341 */       Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 1342 */       int tokenCount = 0;
/* 1343 */       for (String token : tokenizer) {
/* 1344 */         double[] tokenLog2Probs = (double[])this.mTokenToLog2ProbsInCats.get(token);
/* 1345 */         tokenCount++;
/* 1346 */         if (tokenLog2Probs != null) {
/* 1347 */           for (int i = 0; i < logps.length; i++)
/* 1348 */             logps[i] += tokenLog2Probs[i];
/*      */         }
/*      */       }
/* 1351 */       if ((!Double.isNaN(this.mLengthNorm)) && (tokenCount > 0)) {
/* 1352 */         for (int i = 0; i < logps.length; i++) {
/* 1353 */           logps[i] *= this.mLengthNorm / tokenCount;
/*      */         }
/*      */       }
/* 1356 */       for (int i = 0; i < logps.length; i++) {
/* 1357 */         logps[i] += this.mLog2CatProbs[i];
/*      */       }
/* 1359 */       return JointClassification.create(this.mCategories, logps);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CompiledBinaryTradNaiveBayesClassifier
/*      */     implements JointClassifier<CharSequence>
/*      */   {
/*      */     private final TokenizerFactory mTokenizerFactory;
/*      */     private final Map<String, Double> mTokenToLog2ProbDiff;
/*      */     private final double mLog2CatProbDiff;
/*      */     private final double mLengthNorm;
/*      */     private final String[] mCats01;
/*      */     private final String[] mCats10;
/*      */ 
/*      */     CompiledBinaryTradNaiveBayesClassifier(String[] categories, TokenizerFactory tokenizerFactory, Map<String, double[]> tokenToLog2ProbsInCats, double[] log2CatProbs, double lengthNorm)
/*      */     {
/* 1275 */       this.mTokenizerFactory = tokenizerFactory;
/* 1276 */       this.mTokenToLog2ProbDiff = new HashMap();
/* 1277 */       for (Map.Entry entry : tokenToLog2ProbsInCats.entrySet()) {
/* 1278 */         String token = (String)entry.getKey();
/* 1279 */         double[] log2Probs = (double[])entry.getValue();
/* 1280 */         double log2ProbDiff = (log2Probs[0] - log2Probs[1]) / com.aliasi.util.Math.LOG2_E;
/* 1281 */         this.mTokenToLog2ProbDiff.put(token, Double.valueOf(log2ProbDiff));
/*      */       }
/* 1283 */       this.mLog2CatProbDiff = ((log2CatProbs[0] - log2CatProbs[1]) / com.aliasi.util.Math.LOG2_E);
/* 1284 */       this.mLengthNorm = lengthNorm;
/* 1285 */       this.mCats01 = new String[] { categories[0], categories[1] };
/* 1286 */       this.mCats10 = new String[] { categories[1], categories[0] };
/*      */     }
/*      */ 
/*      */     public JointClassification classify(CharSequence in) {
/* 1290 */       double logDiff = 0.0D;
/* 1291 */       char[] cs = Strings.toCharArray(in);
/* 1292 */       Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 1293 */       int tokenCount = 0;
/* 1294 */       for (String token : tokenizer) {
/* 1295 */         Double tokLogDiff = (Double)this.mTokenToLog2ProbDiff.get(token);
/* 1296 */         tokenCount++;
/* 1297 */         if (tokLogDiff != null)
/* 1298 */           logDiff += tokLogDiff.doubleValue();
/*      */       }
/* 1300 */       if ((!Double.isNaN(this.mLengthNorm)) && (tokenCount > 0))
/* 1301 */         logDiff *= this.mLengthNorm / tokenCount;
/* 1302 */       return classification(logDiff + this.mLog2CatProbDiff);
/*      */     }
/*      */ 
/*      */     JointClassification classification(double logDiff) {
/* 1306 */       double expProd = java.lang.Math.exp(logDiff);
/* 1307 */       double p0 = expProd / (1.0D + expProd);
/* 1308 */       double p1 = 1.0D - p0;
/* 1309 */       double log2P0 = com.aliasi.util.Math.log2(p0);
/* 1310 */       double log2P1 = com.aliasi.util.Math.log2(p1);
/* 1311 */       return p0 > p1 ? new JointClassification(this.mCats01, new double[] { log2P0, log2P1 }) : new JointClassification(this.mCats10, new double[] { log2P1, log2P0 });
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Compiler extends AbstractExternalizable
/*      */   {
/*      */     static final long serialVersionUID = 5689464666886334529L;
/*      */     private final TradNaiveBayesClassifier mClassifier;
/*      */ 
/*      */     public Compiler()
/*      */     {
/* 1190 */       this(null);
/*      */     }
/*      */     public Compiler(TradNaiveBayesClassifier classifier) {
/* 1193 */       this.mClassifier = classifier;
/*      */     }
/*      */ 
/*      */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 1197 */       objOut.writeInt(this.mClassifier.mCategories.length);
/* 1198 */       for (int i = 0; i < this.mClassifier.mCategories.length; i++)
/* 1199 */         objOut.writeUTF(this.mClassifier.mCategories[i]);
/* 1200 */       AbstractExternalizable.compileOrSerialize(this.mClassifier.mTokenizerFactory, objOut);
/* 1201 */       objOut.writeInt(this.mClassifier.mTokenToCountsMap.size());
/* 1202 */       for (Map.Entry entry : this.mClassifier.mTokenToCountsMap.entrySet()) {
/* 1203 */         objOut.writeUTF((String)entry.getKey());
/* 1204 */         double[] tokenCounts = (double[])entry.getValue();
/* 1205 */         for (int i = 0; i < this.mClassifier.mCategories.length; i++) {
/* 1206 */           double log2Prob = com.aliasi.util.Math.log2(this.mClassifier.probTokenByIndexArray(i, tokenCounts));
/* 1207 */           if (log2Prob > 0.0D) {
/* 1208 */             String msg = "key=" + (String)entry.getKey() + " i=" + i + " log2Prob=" + log2Prob + " prob=" + this.mClassifier.probTokenByIndexArray(i, tokenCounts) + " token counts[" + i + "]=" + tokenCounts[i] + " totalCatCount=" + this.mClassifier.mTotalCountsPerCategory[i] + " mTokenToCountsMap.size()=" + this.mClassifier.mTokenToCountsMap.size();
/*      */ 
/* 1215 */             throw new IllegalArgumentException(msg);
/*      */           }
/* 1217 */           objOut.writeDouble(log2Prob);
/*      */         }
/*      */       }
/* 1220 */       for (int i = 0; i < this.mClassifier.mCategories.length; i++)
/* 1221 */         objOut.writeDouble(com.aliasi.util.Math.log2(this.mClassifier.probCatByIndex(i)));
/* 1222 */       objOut.writeDouble(this.mClassifier.mLengthNorm);
/*      */     }
/*      */ 
/*      */     public Object read(ObjectInput in) throws ClassNotFoundException, IOException {
/* 1226 */       int numCategories = in.readInt();
/* 1227 */       String[] categories = new String[numCategories];
/* 1228 */       for (int i = 0; i < numCategories; i++)
/* 1229 */         categories[i] = in.readUTF();
/* 1230 */       TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/* 1231 */       int size = in.readInt();
/* 1232 */       Map tokenToLog2ProbsInCats = new HashMap(size * 3 / 2);
/*      */ 
/* 1234 */       for (int k = 0; k < size; k++) {
/* 1235 */         String token = in.readUTF();
/* 1236 */         double[] log2ProbsInCats = new double[numCategories];
/* 1237 */         for (int i = 0; i < numCategories; i++)
/* 1238 */           log2ProbsInCats[i] = in.readDouble();
/* 1239 */         tokenToLog2ProbsInCats.put(token, log2ProbsInCats);
/*      */       }
/* 1241 */       double[] log2CatProbs = new double[numCategories];
/* 1242 */       for (int i = 0; i < numCategories; i++) {
/* 1243 */         log2CatProbs[i] = in.readDouble();
/*      */       }
/* 1245 */       double lengthNorm = in.readDouble();
/*      */ 
/* 1247 */       return categories.length == 2 ? new TradNaiveBayesClassifier.CompiledBinaryTradNaiveBayesClassifier(categories, tokenizerFactory, tokenToLog2ProbsInCats, log2CatProbs, lengthNorm) : new TradNaiveBayesClassifier.CompiledTradNaiveBayesClassifier(categories, tokenizerFactory, tokenToLog2ProbsInCats, log2CatProbs, lengthNorm);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class Serializer extends AbstractExternalizable
/*      */   {
/*      */     static final long serialVersionUID = -4786039228920809976L;
/*      */     private final TradNaiveBayesClassifier mClassifier;
/*      */ 
/*      */     public Serializer(TradNaiveBayesClassifier classifier)
/*      */     {
/* 1118 */       this.mClassifier = classifier;
/*      */     }
/*      */     public Serializer() {
/* 1121 */       this(null);
/*      */     }
/*      */ 
/*      */     public Object read(ObjectInput in) throws ClassNotFoundException, IOException {
/* 1125 */       int numCats = in.readInt();
/* 1126 */       String[] categories = new String[numCats];
/* 1127 */       for (int i = 0; i < numCats; i++)
/* 1128 */         categories[i] = in.readUTF();
/* 1129 */       TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/* 1130 */       double catPrior = in.readDouble();
/* 1131 */       double tokenInCatPrior = in.readDouble();
/*      */ 
/* 1133 */       int tokenToCountsMapSize = in.readInt();
/* 1134 */       Map tokenToCountsMap = new HashMap(tokenToCountsMapSize * 3 / 2);
/*      */ 
/* 1136 */       for (int k = 0; k < tokenToCountsMapSize; k++) {
/* 1137 */         String key = in.readUTF();
/* 1138 */         double[] vals = new double[categories.length];
/* 1139 */         for (int i = 0; i < categories.length; i++)
/* 1140 */           vals[i] = in.readDouble();
/* 1141 */         tokenToCountsMap.put(key, vals);
/*      */       }
/* 1143 */       double[] totalCountsPerCategory = new double[categories.length];
/* 1144 */       for (int i = 0; i < categories.length; i++)
/* 1145 */         totalCountsPerCategory[i] = in.readDouble();
/* 1146 */       double[] caseCounts = new double[categories.length];
/* 1147 */       for (int i = 0; i < categories.length; i++)
/* 1148 */         caseCounts[i] = in.readDouble();
/* 1149 */       double totalCaseCount = in.readDouble();
/* 1150 */       double lengthNorm = in.readDouble();
/* 1151 */       return new TradNaiveBayesClassifier(categories, tokenizerFactory, catPrior, tokenInCatPrior, tokenToCountsMap, totalCountsPerCategory, caseCounts, totalCaseCount, lengthNorm, null);
/*      */     }
/*      */ 
/*      */     public void writeExternal(ObjectOutput objOut)
/*      */       throws IOException
/*      */     {
/* 1163 */       objOut.writeInt(this.mClassifier.mCategories.length);
/* 1164 */       for (String category : this.mClassifier.mCategories) {
/* 1165 */         objOut.writeUTF(category);
/*      */       }
/* 1167 */       objOut.writeObject(this.mClassifier.mTokenizerFactory);
/* 1168 */       objOut.writeDouble(this.mClassifier.mCategoryPrior);
/* 1169 */       objOut.writeDouble(this.mClassifier.mTokenInCategoryPrior);
/* 1170 */       objOut.writeInt(this.mClassifier.mTokenToCountsMap.size());
/* 1171 */       for (Map.Entry entry : this.mClassifier.mTokenToCountsMap.entrySet()) {
/* 1172 */         objOut.writeUTF((String)entry.getKey());
/* 1173 */         double[] vals = (double[])entry.getValue();
/* 1174 */         for (int i = 0; i < this.mClassifier.mCategories.length; i++)
/* 1175 */           objOut.writeDouble(vals[i]);
/*      */       }
/* 1177 */       for (int i = 0; i < this.mClassifier.mCategories.length; i++)
/* 1178 */         objOut.writeDouble(this.mClassifier.mTotalCountsPerCategory[i]);
/* 1179 */       for (int i = 0; i < this.mClassifier.mCategories.length; i++)
/* 1180 */         objOut.writeDouble(this.mClassifier.mCaseCounts[i]);
/* 1181 */       objOut.writeDouble(this.mClassifier.mTotalCaseCount);
/* 1182 */       objOut.writeDouble(this.mClassifier.mLengthNorm);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class EmIterator extends Iterators.Buffered<TradNaiveBayesClassifier>
/*      */   {
/*      */     private final Factory<TradNaiveBayesClassifier> mClassifierFactory;
/*      */     private final Corpus<ObjectHandler<Classified<CharSequence>>> mLabeledData;
/*      */     private final Corpus<ObjectHandler<CharSequence>> mUnlabeledData;
/*      */     private final double mMinTokenCount;
/*      */     private JointClassifier<CharSequence> mLastClassifier;
/*      */ 
/*      */     EmIterator(TradNaiveBayesClassifier initialClassifier, Factory<TradNaiveBayesClassifier> classifierFactory, Corpus<ObjectHandler<Classified<CharSequence>>> labeledData, Corpus<ObjectHandler<CharSequence>> unlabeledData, double minTokenCount)
/*      */     {
/* 1058 */       this.mClassifierFactory = classifierFactory;
/* 1059 */       this.mLabeledData = labeledData;
/* 1060 */       this.mUnlabeledData = unlabeledData;
/* 1061 */       this.mMinTokenCount = minTokenCount;
/* 1062 */       trainSup(labeledData, initialClassifier);
/* 1063 */       compile(initialClassifier);
/*      */     }
/*      */ 
/*      */     public TradNaiveBayesClassifier bufferNext() {
/* 1067 */       TradNaiveBayesClassifier classifier = (TradNaiveBayesClassifier)this.mClassifierFactory.create();
/* 1068 */       trainSup(this.mLabeledData, classifier);
/* 1069 */       trainUnsup(this.mUnlabeledData, classifier);
/* 1070 */       compile(classifier);
/* 1071 */       return classifier;
/*      */     }
/*      */ 
/*      */     void trainSup(Corpus<ObjectHandler<Classified<CharSequence>>> labeledData, TradNaiveBayesClassifier classifier) {
/*      */       try {
/* 1076 */         labeledData.visitTrain(classifier);
/*      */       } catch (IOException e) {
/* 1078 */         throw new IllegalStateException("Error during labeled training", e);
/*      */       }
/*      */     }
/*      */ 
/*      */     void trainUnsup(Corpus<ObjectHandler<CharSequence>> unlabeledData, final TradNaiveBayesClassifier classifier) {
/*      */       try {
/* 1084 */         unlabeledData.visitTrain(new ObjectHandler() {
/*      */           public void handle(CharSequence cSeq) {
/* 1086 */             ConditionalClassification c = TradNaiveBayesClassifier.EmIterator.this.mLastClassifier.classify(cSeq);
/* 1087 */             classifier.trainConditional(cSeq, c, 1.0D, TradNaiveBayesClassifier.EmIterator.this.mMinTokenCount);
/*      */           } } );
/*      */       }
/*      */       catch (IOException e) {
/* 1091 */         throw new IllegalStateException("Error during unlabeled training", e);
/*      */       }
/*      */     }
/*      */ 
/*      */     void compile(TradNaiveBayesClassifier classifier) {
/*      */       try {
/* 1097 */         JointClassifier lastClassifier = (JointClassifier)AbstractExternalizable.compile(classifier);
/*      */ 
/* 1100 */         this.mLastClassifier = lastClassifier;
/*      */       } catch (IOException e) {
/* 1102 */         this.mLastClassifier = null;
/* 1103 */         throw new IllegalStateException("Error during compilation.", e);
/*      */       } catch (ClassNotFoundException e) {
/* 1105 */         this.mLastClassifier = null;
/* 1106 */         throw new IllegalStateException("Error during compilation.", e);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CaseProbAccumulator
/*      */     implements ObjectHandler<CharSequence>
/*      */   {
/* 1028 */     double mCaseProb = 0.0D;
/*      */     final TradNaiveBayesClassifier mClassifier;
/*      */ 
/*      */     CaseProbAccumulator(TradNaiveBayesClassifier classifier)
/*      */     {
/* 1031 */       this.mClassifier = classifier;
/*      */     }
/*      */     public void handle(CharSequence cSeq) {
/* 1034 */       this.mCaseProb += this.mClassifier.log2CaseProb(cSeq);
/*      */     }
/*      */     public ObjectHandler<Classified<CharSequence>> supHandler() {
/* 1037 */       final ObjectHandler cSeqHandler = this;
/* 1038 */       return new ObjectHandler() {
/*      */         public void handle(Classified<CharSequence> classified) {
/* 1040 */           cSeqHandler.handle(classified.getObject());
/*      */         }
/*      */       };
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.TradNaiveBayesClassifier
 * JD-Core Version:    0.6.2
 */