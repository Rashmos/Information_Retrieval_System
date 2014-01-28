/*      */ package com.aliasi.cluster;
/*      */ 
/*      */ import com.aliasi.corpus.ObjectHandler;
/*      */ import com.aliasi.stats.Statistics;
/*      */ import com.aliasi.symbol.SymbolTable;
/*      */ import com.aliasi.tokenizer.Tokenizer;
/*      */ import com.aliasi.tokenizer.TokenizerFactory;
/*      */ import com.aliasi.util.AbstractExternalizable;
/*      */ import com.aliasi.util.FeatureExtractor;
/*      */ import com.aliasi.util.Iterators.Buffered;
/*      */ import com.aliasi.util.ObjectToCounterMap;
/*      */ import com.aliasi.util.ObjectToDoubleMap;
/*      */ import com.aliasi.util.Strings;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class LatentDirichletAllocation
/*      */   implements Serializable
/*      */ {
/*      */   static final long serialVersionUID = 6313662446710242382L;
/*      */   private final double mDocTopicPrior;
/*      */   private final double[][] mTopicWordProbs;
/*      */ 
/*      */   public LatentDirichletAllocation(double docTopicPrior, double[][] topicWordProbs)
/*      */   {
/*  531 */     if ((docTopicPrior <= 0.0D) || (Double.isNaN(docTopicPrior)) || (Double.isInfinite(docTopicPrior)))
/*      */     {
/*  534 */       String msg = "Document-topic prior must be finite and positive. Found docTopicPrior=" + docTopicPrior;
/*      */ 
/*  536 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  538 */     int numTopics = topicWordProbs.length;
/*  539 */     if (numTopics < 1) {
/*  540 */       String msg = "Require non-empty topic-word probabilities.";
/*  541 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  544 */     int numWords = topicWordProbs[0].length;
/*  545 */     for (int topic = 1; topic < numTopics; topic++) {
/*  546 */       if (topicWordProbs[topic].length != numWords) {
/*  547 */         String msg = "All topics must have the same number of words. topicWordProbs[0].length=" + topicWordProbs[0].length + " topicWordProbs[" + topic + "]=" + topicWordProbs[topic].length;
/*      */ 
/*  552 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*      */ 
/*  556 */     for (int topic = 0; topic < numTopics; topic++) {
/*  557 */       for (int word = 0; word < numWords; word++) {
/*  558 */         if ((topicWordProbs[topic][word] < 0.0D) || (topicWordProbs[topic][word] > 1.0D))
/*      */         {
/*  560 */           String msg = "All probabilities must be between 0.0 and 1.0 Found topicWordProbs[" + topic + "][" + word + "]=" + topicWordProbs[topic][word];
/*      */ 
/*  563 */           throw new IllegalArgumentException(msg);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  568 */     this.mDocTopicPrior = docTopicPrior;
/*  569 */     this.mTopicWordProbs = topicWordProbs;
/*      */   }
/*      */ 
/*      */   public int numTopics()
/*      */   {
/*  578 */     return this.mTopicWordProbs.length;
/*      */   }
/*      */ 
/*      */   public int numWords()
/*      */   {
/*  588 */     return this.mTopicWordProbs[0].length;
/*      */   }
/*      */ 
/*      */   public double documentTopicPrior()
/*      */   {
/*  600 */     return this.mDocTopicPrior;
/*      */   }
/*      */ 
/*      */   public double wordProbability(int topic, int word)
/*      */   {
/*  615 */     return this.mTopicWordProbs[topic][word];
/*      */   }
/*      */ 
/*      */   public double[] wordProbabilities(int topic)
/*      */   {
/*  630 */     double[] xs = new double[this.mTopicWordProbs[topic].length];
/*  631 */     for (int i = 0; i < xs.length; i++)
/*  632 */       xs[i] = this.mTopicWordProbs[topic][i];
/*  633 */     return xs;
/*      */   }
/*      */ 
/*      */   public short[][] sampleTopics(int[] tokens, int numSamples, int burnin, int sampleLag, Random random)
/*      */   {
/*  671 */     if (burnin < 0) {
/*  672 */       String msg = "Burnin period must be non-negative. Found burnin=" + burnin;
/*      */ 
/*  674 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  677 */     if (numSamples < 1) {
/*  678 */       String msg = "Number of samples must be at least 1. Found numSamples=" + numSamples;
/*      */ 
/*  680 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  683 */     if (sampleLag < 1) {
/*  684 */       String msg = "Sample lag must be at least 1. Found sampleLag=" + sampleLag;
/*      */ 
/*  686 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  689 */     double docTopicPrior = documentTopicPrior();
/*  690 */     int numTokens = tokens.length;
/*      */ 
/*  692 */     int numTopics = numTopics();
/*      */ 
/*  694 */     int[] topicCount = new int[numTopics];
/*      */ 
/*  696 */     short[][] samples = new short[numSamples][numTokens];
/*  697 */     int sample = 0;
/*  698 */     short[] currentSample = samples[0];
/*  699 */     for (int token = 0; token < numTokens; token++) {
/*  700 */       int randomTopic = random.nextInt(numTopics);
/*  701 */       topicCount[randomTopic] += 1;
/*  702 */       currentSample[token] = ((short)randomTopic);
/*      */     }
/*      */ 
/*  705 */     double[] topicDistro = new double[numTopics];
/*      */ 
/*  707 */     int numEpochs = burnin + sampleLag * (numSamples - 1);
/*  708 */     for (int epoch = 0; epoch < numEpochs; epoch++) {
/*  709 */       for (int token = 0; token < numTokens; token++) {
/*  710 */         int word = tokens[token];
/*  711 */         int currentTopic = currentSample[token];
/*  712 */         topicCount[currentTopic] -= 1;
/*  713 */         if (topicCount[currentTopic] < 0) {
/*  714 */           throw new IllegalArgumentException("bomb");
/*      */         }
/*  716 */         for (int topic = 0; topic < numTopics; topic++) {
/*  717 */           topicDistro[topic] = ((topicCount[topic] + docTopicPrior) * wordProbability(topic, word) + (topic == 0 ? 0.0D : topicDistro[(topic - 1)]));
/*      */         }
/*      */ 
/*  722 */         int sampledTopic = Statistics.sample(topicDistro, random);
/*  723 */         topicCount[sampledTopic] += 1;
/*  724 */         currentSample[token] = ((short)sampledTopic);
/*      */       }
/*  726 */       if ((epoch >= burnin) && ((epoch - burnin) % sampleLag == 0)) {
/*  727 */         short[] pastSample = currentSample;
/*  728 */         sample++;
/*  729 */         currentSample = samples[sample];
/*  730 */         for (int token = 0; token < numTokens; token++)
/*  731 */           currentSample[token] = pastSample[token];
/*      */       }
/*      */     }
/*  734 */     return samples;
/*      */   }
/*      */ 
/*      */   double[] mapTopicEstimate(int[] tokens, int numSamples, int burnin, int sampleLag, Random random)
/*      */   {
/*  765 */     return bayesTopicEstimate(tokens, numSamples, burnin, sampleLag, random);
/*      */   }
/*      */ 
/*      */   public double[] bayesTopicEstimate(int[] tokens, int numSamples, int burnin, int sampleLag, Random random)
/*      */   {
/*  800 */     short[][] sampleTopics = sampleTopics(tokens, numSamples, burnin, sampleLag, random);
/*  801 */     int numTopics = numTopics();
/*  802 */     int[] counts = new int[numTopics];
/*  803 */     for (short[] topics : sampleTopics) {
/*  804 */       for (int tok = 0; tok < topics.length; tok++)
/*  805 */         counts[topics[tok]] += 1;
/*      */     }
/*  807 */     double totalCount = 0.0D;
/*  808 */     for (int topic = 0; topic < numTopics; topic++)
/*  809 */       totalCount += counts[topic];
/*  810 */     double[] result = new double[numTopics];
/*  811 */     for (int topic = 0; topic < numTopics; topic++)
/*  812 */       result[topic] = (counts[topic] / totalCount);
/*  813 */     return result;
/*      */   }
/*      */ 
/*      */   Object writeReplace()
/*      */   {
/*  819 */     return new Serializer(this);
/*      */   }
/*      */ 
/*      */   public static GibbsSample gibbsSampler(int[][] docWords, short numTopics, double docTopicPrior, double topicWordPrior, int burninEpochs, int sampleLag, int numSamples, Random random, ObjectHandler<GibbsSample> handler)
/*      */   {
/*  899 */     validateInputs(docWords, numTopics, docTopicPrior, topicWordPrior, burninEpochs, sampleLag, numSamples);
/*      */ 
/*  901 */     int numDocs = docWords.length;
/*  902 */     int numWords = max(docWords) + 1;
/*      */ 
/*  904 */     int numTokens = 0;
/*  905 */     for (int doc = 0; doc < numDocs; doc++) {
/*  906 */       numTokens += docWords[doc].length;
/*      */     }
/*      */ 
/*  912 */     short[][] currentSample = new short[numDocs][];
/*  913 */     for (int doc = 0; doc < numDocs; doc++) {
/*  914 */       currentSample[doc] = new short[docWords[doc].length];
/*      */     }
/*  916 */     int[][] docTopicCount = new int[numDocs][numTopics];
/*  917 */     int[][] wordTopicCount = new int[numWords][numTopics];
/*  918 */     int[] topicTotalCount = new int[numTopics];
/*      */ 
/*  920 */     for (int doc = 0; doc < numDocs; doc++) {
/*  921 */       for (int tok = 0; tok < docWords[doc].length; tok++) {
/*  922 */         int word = docWords[doc][tok];
/*  923 */         int topic = random.nextInt(numTopics);
/*  924 */         currentSample[doc][tok] = ((short)topic);
/*  925 */         docTopicCount[doc][topic] += 1;
/*  926 */         wordTopicCount[word][topic] += 1;
/*  927 */         topicTotalCount[topic] += 1;
/*      */       }
/*      */     }
/*      */ 
/*  931 */     double numWordsTimesTopicWordPrior = numWords * topicWordPrior;
/*  932 */     double[] topicDistro = new double[numTopics];
/*  933 */     int numEpochs = burninEpochs + sampleLag * (numSamples - 1);
/*  934 */     for (int epoch = 0; epoch <= numEpochs; epoch++) {
/*  935 */       double corpusLog2Prob = 0.0D;
/*  936 */       int numChangedTopics = 0;
/*  937 */       for (int doc = 0; doc < numDocs; doc++) {
/*  938 */         int[] docWordsDoc = docWords[doc];
/*  939 */         short[] currentSampleDoc = currentSample[doc];
/*  940 */         int[] docTopicCountDoc = docTopicCount[doc];
/*  941 */         for (int tok = 0; tok < docWordsDoc.length; tok++) {
/*  942 */           int word = docWordsDoc[tok];
/*  943 */           int[] wordTopicCountWord = wordTopicCount[word];
/*  944 */           int currentTopic = currentSampleDoc[tok];
/*  945 */           if (currentTopic == 0) {
/*  946 */             topicDistro[0] = ((docTopicCountDoc[0] - 1.0D + docTopicPrior) * (wordTopicCountWord[0] - 1.0D + topicWordPrior) / (topicTotalCount[0] - 1.0D + numWordsTimesTopicWordPrior));
/*      */           }
/*      */           else
/*      */           {
/*  951 */             topicDistro[0] = ((docTopicCountDoc[0] + docTopicPrior) * (wordTopicCountWord[0] + topicWordPrior) / (topicTotalCount[0] + numWordsTimesTopicWordPrior));
/*      */ 
/*  955 */             for (int topic = 1; topic < currentTopic; topic++) {
/*  956 */               topicDistro[topic] = ((docTopicCountDoc[topic] + docTopicPrior) * (wordTopicCountWord[topic] + topicWordPrior) / (topicTotalCount[topic] + numWordsTimesTopicWordPrior) + topicDistro[(topic - 1)]);
/*      */             }
/*      */ 
/*  962 */             topicDistro[currentTopic] = ((docTopicCountDoc[currentTopic] - 1.0D + docTopicPrior) * (wordTopicCountWord[currentTopic] - 1.0D + topicWordPrior) / (topicTotalCount[currentTopic] - 1.0D + numWordsTimesTopicWordPrior) + topicDistro[(currentTopic - 1)]);
/*      */           }
/*      */ 
/*  968 */           for (int topic = currentTopic + 1; topic < numTopics; topic++) {
/*  969 */             topicDistro[topic] = ((docTopicCountDoc[topic] + docTopicPrior) * (wordTopicCountWord[topic] + topicWordPrior) / (topicTotalCount[topic] + numWordsTimesTopicWordPrior) + topicDistro[(topic - 1)]);
/*      */           }
/*      */ 
/*  975 */           int sampledTopic = Statistics.sample(topicDistro, random);
/*      */ 
/*  978 */           if (sampledTopic != currentTopic) {
/*  979 */             currentSampleDoc[tok] = ((short)sampledTopic);
/*  980 */             docTopicCountDoc[currentTopic] -= 1;
/*  981 */             wordTopicCountWord[currentTopic] -= 1;
/*  982 */             topicTotalCount[currentTopic] -= 1;
/*  983 */             docTopicCountDoc[sampledTopic] += 1;
/*  984 */             wordTopicCountWord[sampledTopic] += 1;
/*  985 */             topicTotalCount[sampledTopic] += 1;
/*      */           }
/*      */ 
/*  988 */           if (sampledTopic != currentTopic)
/*  989 */             numChangedTopics++;
/*  990 */           double topicProbGivenDoc = docTopicCountDoc[sampledTopic] / docWordsDoc.length;
/*      */ 
/*  992 */           double wordProbGivenTopic = wordTopicCountWord[sampledTopic] / topicTotalCount[sampledTopic];
/*      */ 
/*  994 */           double tokenLog2Prob = com.aliasi.util.Math.log2(topicProbGivenDoc * wordProbGivenTopic);
/*  995 */           corpusLog2Prob += tokenLog2Prob;
/*      */         }
/*      */       }
/*      */ 
/*  999 */       if ((epoch >= burninEpochs) && ((epoch - burninEpochs) % sampleLag == 0))
/*      */       {
/* 1001 */         GibbsSample sample = new GibbsSample(epoch, currentSample, docWords, docTopicPrior, topicWordPrior, docTopicCount, wordTopicCount, topicTotalCount, numChangedTopics, numWords, numTokens);
/*      */ 
/* 1013 */         if (handler != null)
/* 1014 */           handler.handle(sample);
/* 1015 */         if (epoch == numEpochs)
/* 1016 */           return sample;
/*      */       }
/*      */     }
/* 1019 */     throw new IllegalStateException("unreachable in practice because of return if epoch==numEpochs");
/*      */   }
/*      */ 
/*      */   public static Iterator<GibbsSample> gibbsSample(int[][] docWords, short numTopics, double docTopicPrior, double topicWordPrior, Random random)
/*      */   {
/* 1042 */     validateInputs(docWords, numTopics, docTopicPrior, topicWordPrior);
/*      */ 
/* 1044 */     return new SampleIterator(docWords, numTopics, docTopicPrior, topicWordPrior, random);
/*      */   }
/*      */ 
/*      */   public static int[][] tokenizeDocuments(CharSequence[] texts, TokenizerFactory tokenizerFactory, SymbolTable symbolTable, int minCount)
/*      */   {
/* 1214 */     ObjectToCounterMap tokenCounter = new ObjectToCounterMap();
/* 1215 */     for (CharSequence text : texts) {
/* 1216 */       char[] cs = Strings.toCharArray(text);
/* 1217 */       Tokenizer tokenizer = tokenizerFactory.tokenizer(cs, 0, cs.length);
/* 1218 */       for (String token : tokenizer)
/* 1219 */         tokenCounter.increment(token);
/*      */     }
/* 1221 */     tokenCounter.prune(minCount);
/* 1222 */     Set tokenSet = tokenCounter.keySet();
/* 1223 */     for (String token : tokenSet) {
/* 1224 */       symbolTable.getOrAddSymbol(token);
/*      */     }
/* 1226 */     int[][] docTokenId = new int[texts.length][];
/* 1227 */     for (int i = 0; i < docTokenId.length; i++) {
/* 1228 */       docTokenId[i] = tokenizeDocument(texts[i], tokenizerFactory, symbolTable);
/*      */     }
/* 1230 */     return docTokenId;
/*      */   }
/*      */ 
/*      */   public static int[] tokenizeDocument(CharSequence text, TokenizerFactory tokenizerFactory, SymbolTable symbolTable)
/*      */   {
/* 1250 */     char[] cs = Strings.toCharArray(text);
/* 1251 */     Tokenizer tokenizer = tokenizerFactory.tokenizer(cs, 0, cs.length);
/* 1252 */     List idList = new ArrayList();
/* 1253 */     for (String token : tokenizer) {
/* 1254 */       int id = symbolTable.symbolToID(token);
/* 1255 */       if (id >= 0)
/* 1256 */         idList.add(Integer.valueOf(id));
/*      */     }
/* 1258 */     int[] tokenIds = new int[idList.size()];
/* 1259 */     for (int i = 0; i < tokenIds.length; i++) {
/* 1260 */       tokenIds[i] = ((Integer)idList.get(i)).intValue();
/*      */     }
/* 1262 */     return tokenIds;
/*      */   }
/*      */ 
/*      */   static int max(int[][] xs)
/*      */   {
/* 1268 */     int max = 0;
/* 1269 */     for (int i = 0; i < xs.length; i++) {
/* 1270 */       int[] xsI = xs[i];
/* 1271 */       for (int j = 0; j < xsI.length; j++) {
/* 1272 */         if (xsI[j] > max)
/* 1273 */           max = xsI[j];
/*      */       }
/*      */     }
/* 1276 */     return max;
/*      */   }
/*      */ 
/*      */   static double relativeDifference(double x, double y)
/*      */   {
/* 1281 */     return java.lang.Math.abs(x - y) / (java.lang.Math.abs(x) + java.lang.Math.abs(y));
/*      */   }
/*      */ 
/*      */   static void validateInputs(int[][] docWords, short numTopics, double docTopicPrior, double topicWordPrior, int burninEpochs, int sampleLag, int numSamples)
/*      */   {
/* 1293 */     validateInputs(docWords, numTopics, docTopicPrior, topicWordPrior);
/*      */ 
/* 1295 */     if (burninEpochs < 0) {
/* 1296 */       String msg = "Number of burnin epochs must be non-negative. Found burninEpochs=" + burninEpochs;
/*      */ 
/* 1298 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/* 1301 */     if (sampleLag < 1) {
/* 1302 */       String msg = "Sample lag must be positive. Found sampleLag=" + sampleLag;
/*      */ 
/* 1304 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/* 1307 */     if (numSamples < 1) {
/* 1308 */       String msg = "Number of samples must be positive. Found numSamples=" + numSamples;
/*      */ 
/* 1310 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   static void validateInputs(int[][] docWords, short numTopics, double docTopicPrior, double topicWordPrior)
/*      */   {
/* 1320 */     for (int doc = 0; doc < docWords.length; doc++) {
/* 1321 */       for (int tok = 0; tok < docWords[doc].length; tok++) {
/* 1322 */         if (docWords[doc][tok] < 0) {
/* 1323 */           String msg = "All tokens must have IDs greater than 0. Found docWords[" + doc + "][" + tok + "]=" + docWords[doc][tok];
/*      */ 
/* 1326 */           throw new IllegalArgumentException(msg);
/*      */         }
/*      */       }
/*      */     }
/* 1330 */     if (numTopics < 1) {
/* 1331 */       String msg = "Num topics must be positive. Found numTopics=" + numTopics;
/*      */ 
/* 1333 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/* 1336 */     if ((Double.isInfinite(docTopicPrior)) || (Double.isNaN(docTopicPrior)) || (docTopicPrior < 0.0D)) {
/* 1337 */       String msg = "Document-topic prior must be finite and positive. Found docTopicPrior=" + docTopicPrior;
/*      */ 
/* 1339 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/* 1342 */     if ((Double.isInfinite(topicWordPrior)) || (Double.isNaN(topicWordPrior)) || (topicWordPrior < 0.0D)) {
/* 1343 */       String msg = "Topic-word prior must be finite and positive. Found topicWordPrior=" + topicWordPrior;
/*      */ 
/* 1345 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   FeatureExtractor<CharSequence> expectedTopicFeatureExtractor(TokenizerFactory tokenizerFactory, SymbolTable symbolTable, String featurePrefix)
/*      */   {
/* 1799 */     return new ExpectedTopicFeatureExtractor(this, tokenizerFactory, symbolTable, featurePrefix);
/*      */   }
/*      */ 
/*      */   FeatureExtractor<CharSequence> bayesTopicFeatureExtractor(TokenizerFactory tokenizerFactory, SymbolTable symbolTable, String featurePrefix, int burnIn, int sampleLag, int numSamples)
/*      */   {
/* 1840 */     return new BayesTopicFeatureExtractor(this, tokenizerFactory, symbolTable, featurePrefix, burnIn, sampleLag, numSamples);
/*      */   }
/*      */ 
/*      */   static String[] genFeatures(String prefix, int numTopics)
/*      */   {
/* 1850 */     String[] features = new String[numTopics];
/* 1851 */     for (int k = 0; k < numTopics; k++)
/* 1852 */       features[k] = (prefix + k);
/* 1853 */     return features;
/*      */   }
/*      */ 
/*      */   static class Serializer extends AbstractExternalizable
/*      */   {
/*      */     static final long serialVersionUID = 4725870665020270825L;
/*      */     final LatentDirichletAllocation mLda;
/*      */ 
/*      */     public Serializer()
/*      */     {
/* 2094 */       this(null);
/*      */     }
/*      */     public Serializer(LatentDirichletAllocation lda) {
/* 2097 */       this.mLda = lda;
/*      */     }
/*      */     public Object read(ObjectInput in) throws IOException {
/* 2100 */       double docTopicPrior = in.readDouble();
/* 2101 */       int numTopics = in.readInt();
/* 2102 */       double[][] topicWordProbs = new double[numTopics][];
/* 2103 */       for (int i = 0; i < topicWordProbs.length; i++)
/* 2104 */         topicWordProbs[i] = readDoubles(in);
/* 2105 */       return new LatentDirichletAllocation(docTopicPrior, topicWordProbs);
/*      */     }
/*      */     public void writeExternal(ObjectOutput out) throws IOException {
/* 2108 */       out.writeDouble(this.mLda.mDocTopicPrior);
/* 2109 */       out.writeInt(this.mLda.mTopicWordProbs.length);
/* 2110 */       for (double[] topicWordProbs : this.mLda.mTopicWordProbs)
/* 2111 */         writeDoubles(topicWordProbs, out);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ExpectedTopicFeatureExtractor
/*      */     implements FeatureExtractor<CharSequence>, Serializable
/*      */   {
/*      */     static final long serialVersionUID = -7996546432550775177L;
/*      */     private final double[][] mWordTopicProbs;
/*      */     private final double mDocTopicPrior;
/*      */     private final TokenizerFactory mTokenizerFactory;
/*      */     private final SymbolTable mSymbolTable;
/*      */     private final String[] mFeatures;
/*      */ 
/*      */     public ExpectedTopicFeatureExtractor(LatentDirichletAllocation lda, TokenizerFactory tokenizerFactory, SymbolTable symbolTable, String featurePrefix)
/*      */     {
/* 1996 */       double[][] wordTopicProbs = new double[lda.numWords()][lda.numTopics()];
/* 1997 */       for (int word = 0; word < lda.numWords(); word++)
/* 1998 */         for (int topic = 0; topic < lda.numTopics(); topic++)
/* 1999 */           wordTopicProbs[word][topic] = lda.wordProbability(topic, word);
/* 2000 */       for (double[] topicProbs : wordTopicProbs) {
/* 2001 */         double sum = com.aliasi.util.Math.sum(topicProbs);
/* 2002 */         for (int k = 0; k < topicProbs.length; k++)
/* 2003 */           topicProbs[k] /= sum;
/*      */       }
/* 2005 */       this.mWordTopicProbs = wordTopicProbs;
/* 2006 */       this.mDocTopicPrior = lda.documentTopicPrior();
/* 2007 */       this.mTokenizerFactory = tokenizerFactory;
/* 2008 */       this.mSymbolTable = symbolTable;
/*      */ 
/* 2010 */       this.mFeatures = LatentDirichletAllocation.genFeatures(featurePrefix, lda.numTopics());
/*      */     }
/*      */ 
/*      */     ExpectedTopicFeatureExtractor(double docTopicPrior, double[][] wordTopicProbs, TokenizerFactory tokenizerFactory, SymbolTable symbolTable, String[] features)
/*      */     {
/* 2018 */       this.mWordTopicProbs = wordTopicProbs;
/* 2019 */       this.mDocTopicPrior = docTopicPrior;
/* 2020 */       this.mTokenizerFactory = tokenizerFactory;
/* 2021 */       this.mSymbolTable = symbolTable;
/* 2022 */       this.mFeatures = features;
/*      */     }
/*      */ 
/*      */     public Map<String, Double> features(CharSequence cSeq) {
/* 2026 */       int numTopics = this.mWordTopicProbs[0].length;
/* 2027 */       char[] cs = Strings.toCharArray(cSeq);
/* 2028 */       Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 2029 */       double[] vals = new double[numTopics];
/* 2030 */       Arrays.fill(vals, this.mDocTopicPrior);
/* 2031 */       for (String token : tokenizer) {
/* 2032 */         int symbol = this.mSymbolTable.symbolToID(token);
/* 2033 */         if ((symbol >= 0) && (symbol < this.mWordTopicProbs.length))
/*      */         {
/* 2035 */           for (int k = 0; k < numTopics; k++)
/* 2036 */             vals[k] += this.mWordTopicProbs[symbol][k];
/*      */         }
/*      */       }
/* 2039 */       ObjectToDoubleMap featMap = new ObjectToDoubleMap(numTopics * 3 / 2);
/*      */ 
/* 2041 */       double sum = com.aliasi.util.Math.sum(vals);
/* 2042 */       for (int k = 0; k < numTopics; k++) {
/* 2043 */         if (vals[k] > 0.0D)
/* 2044 */           featMap.set(this.mFeatures[k], vals[k] / sum);
/*      */       }
/* 2046 */       return featMap;
/*      */     }
/*      */ 
/*      */     Object writeReplace() {
/* 2050 */       return new Serializer(this);
/*      */     }
/*      */     static class Serializer extends AbstractExternalizable {
/*      */       static final long serialVersionUID = -1472744781627035426L;
/*      */       final LatentDirichletAllocation.ExpectedTopicFeatureExtractor mFeatures;
/*      */ 
/* 2057 */       public Serializer() { this(null); }
/*      */ 
/*      */       public Serializer(LatentDirichletAllocation.ExpectedTopicFeatureExtractor features) {
/* 2060 */         this.mFeatures = features;
/*      */       }
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/* 2063 */         out.writeDouble(this.mFeatures.mDocTopicPrior);
/* 2064 */         out.writeInt(this.mFeatures.mWordTopicProbs.length);
/* 2065 */         for (int w = 0; w < this.mFeatures.mWordTopicProbs.length; w++)
/* 2066 */           writeDoubles(this.mFeatures.mWordTopicProbs[w], out);
/* 2067 */         out.writeObject(this.mFeatures.mTokenizerFactory);
/* 2068 */         out.writeObject(this.mFeatures.mSymbolTable);
/* 2069 */         writeUTFs(this.mFeatures.mFeatures, out);
/*      */       }
/*      */       public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 2072 */         double docTopicPrior = in.readDouble();
/* 2073 */         int numWords = in.readInt();
/* 2074 */         double[][] wordTopicProbs = new double[numWords][];
/* 2075 */         for (int w = 0; w < numWords; w++) {
/* 2076 */           wordTopicProbs[w] = readDoubles(in);
/*      */         }
/* 2078 */         TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/*      */ 
/* 2081 */         SymbolTable symbolTable = (SymbolTable)in.readObject();
/*      */ 
/* 2083 */         String[] features = readUTFs(in);
/* 2084 */         return new LatentDirichletAllocation.ExpectedTopicFeatureExtractor(docTopicPrior, wordTopicProbs, tokenizerFactory, symbolTable, features);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class BayesTopicFeatureExtractor
/*      */     implements FeatureExtractor<CharSequence>, Serializable
/*      */   {
/*      */     static final long serialVersionUID = 8883227852502200365L;
/*      */     private final LatentDirichletAllocation mLda;
/*      */     private final TokenizerFactory mTokenizerFactory;
/*      */     private final SymbolTable mSymbolTable;
/*      */     private final String[] mFeatures;
/*      */     private final int mBurnin;
/*      */     private final int mSampleLag;
/*      */     private final int mNumSamples;
/*      */ 
/*      */     public BayesTopicFeatureExtractor(LatentDirichletAllocation lda, TokenizerFactory tokenizerFactory, SymbolTable symbolTable, String featurePrefix, int burnin, int sampleLag, int numSamples)
/*      */     {
/* 1880 */       this(lda, tokenizerFactory, symbolTable, LatentDirichletAllocation.genFeatures(featurePrefix, lda.numTopics()), burnin, sampleLag, numSamples);
/*      */     }
/*      */ 
/*      */     BayesTopicFeatureExtractor(LatentDirichletAllocation lda, TokenizerFactory tokenizerFactory, SymbolTable symbolTable, String[] features, int burnin, int sampleLag, int numSamples)
/*      */     {
/* 1896 */       this.mLda = lda;
/* 1897 */       this.mTokenizerFactory = tokenizerFactory;
/* 1898 */       this.mSymbolTable = symbolTable;
/* 1899 */       this.mFeatures = features;
/* 1900 */       this.mBurnin = burnin;
/* 1901 */       this.mSampleLag = sampleLag;
/* 1902 */       this.mNumSamples = numSamples;
/*      */     }
/*      */ 
/*      */     public Map<String, Double> features(CharSequence cSeq) {
/* 1906 */       int numTopics = this.mLda.numTopics();
/* 1907 */       char[] cs = Strings.toCharArray(cSeq);
/* 1908 */       Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 1909 */       List tokenIdList = new ArrayList();
/* 1910 */       for (String token : tokenizer) {
/* 1911 */         int symbol = this.mSymbolTable.symbolToID(token);
/* 1912 */         if ((symbol >= 0) && (symbol < this.mLda.numWords()))
/*      */         {
/* 1914 */           tokenIdList.add(Integer.valueOf(symbol));
/*      */         }
/*      */       }
/* 1916 */       int[] tokens = new int[tokenIdList.size()];
/* 1917 */       for (int i = 0; i < tokenIdList.size(); i++) {
/* 1918 */         tokens[i] = ((Integer)tokenIdList.get(i)).intValue();
/*      */       }
/* 1920 */       double[] vals = this.mLda.mapTopicEstimate(tokens, this.mNumSamples, this.mBurnin, this.mSampleLag, new Random());
/*      */ 
/* 1926 */       ObjectToDoubleMap features = new ObjectToDoubleMap(numTopics * 3 / 2);
/*      */ 
/* 1928 */       for (int k = 0; k < numTopics; k++) {
/* 1929 */         if (vals[k] > 0.0D)
/* 1930 */           features.set(this.mFeatures[k], vals[k]);
/*      */       }
/* 1932 */       return features;
/*      */     }
/*      */ 
/*      */     Object writeReplace() {
/* 1936 */       return new Serializer(this);
/*      */     }
/*      */     static class Serializer extends AbstractExternalizable {
/*      */       static final long serialVersionUID = 6719636683732661958L;
/*      */       final LatentDirichletAllocation.BayesTopicFeatureExtractor mFeatureExtractor;
/*      */ 
/* 1943 */       public Serializer() { this(null); }
/*      */ 
/*      */       Serializer(LatentDirichletAllocation.BayesTopicFeatureExtractor featureExtractor) {
/* 1946 */         this.mFeatureExtractor = featureExtractor;
/*      */       }
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/* 1949 */         out.writeObject(this.mFeatureExtractor.mLda);
/* 1950 */         out.writeObject(this.mFeatureExtractor.mTokenizerFactory);
/* 1951 */         out.writeObject(this.mFeatureExtractor.mSymbolTable);
/* 1952 */         writeUTFs(this.mFeatureExtractor.mFeatures, out);
/* 1953 */         out.writeInt(this.mFeatureExtractor.mBurnin);
/* 1954 */         out.writeInt(this.mFeatureExtractor.mSampleLag);
/* 1955 */         out.writeInt(this.mFeatureExtractor.mNumSamples);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 1959 */         LatentDirichletAllocation lda = (LatentDirichletAllocation)in.readObject();
/*      */ 
/* 1963 */         TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/*      */ 
/* 1967 */         SymbolTable symbolTable = (SymbolTable)in.readObject();
/*      */ 
/* 1970 */         String[] features = readUTFs(in);
/* 1971 */         int burnIn = in.readInt();
/* 1972 */         int sampleLag = in.readInt();
/* 1973 */         int numSamples = in.readInt();
/* 1974 */         return new LatentDirichletAllocation.BayesTopicFeatureExtractor(lda, tokenizerFactory, symbolTable, features, burnIn, sampleLag, numSamples);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class GibbsSample
/*      */   {
/*      */     private final int mEpoch;
/*      */     private final short[][] mTopicSample;
/*      */     private final int[][] mDocWords;
/*      */     private final double mDocTopicPrior;
/*      */     private final double mTopicWordPrior;
/*      */     private final int[][] mDocTopicCount;
/*      */     private final int[][] mWordTopicCount;
/*      */     private final int[] mTopicCount;
/*      */     private final int mNumChangedTopics;
/*      */     private final int mNumWords;
/*      */     private final int mNumTokens;
/*      */ 
/*      */     GibbsSample(int epoch, short[][] topicSample, int[][] docWords, double docTopicPrior, double topicWordPrior, int[][] docTopicCount, int[][] wordTopicCount, int[] topicCount, int numChangedTopics, int numWords, int numTokens)
/*      */     {
/* 1419 */       this.mEpoch = epoch;
/* 1420 */       this.mTopicSample = topicSample;
/* 1421 */       this.mDocWords = docWords;
/* 1422 */       this.mDocTopicPrior = docTopicPrior;
/* 1423 */       this.mTopicWordPrior = topicWordPrior;
/* 1424 */       this.mDocTopicCount = docTopicCount;
/* 1425 */       this.mWordTopicCount = wordTopicCount;
/* 1426 */       this.mTopicCount = topicCount;
/* 1427 */       this.mNumChangedTopics = numChangedTopics;
/* 1428 */       this.mNumWords = numWords;
/* 1429 */       this.mNumTokens = numTokens;
/*      */     }
/*      */ 
/*      */     public int epoch()
/*      */     {
/* 1438 */       return this.mEpoch;
/*      */     }
/*      */ 
/*      */     public int numDocuments()
/*      */     {
/* 1448 */       return this.mDocWords.length;
/*      */     }
/*      */ 
/*      */     public int numWords()
/*      */     {
/* 1458 */       return this.mNumWords;
/*      */     }
/*      */ 
/*      */     public int numTokens()
/*      */     {
/* 1467 */       return this.mNumTokens;
/*      */     }
/*      */ 
/*      */     public int numTopics()
/*      */     {
/* 1476 */       return this.mTopicCount.length;
/*      */     }
/*      */ 
/*      */     public short topicSample(int doc, int token)
/*      */     {
/* 1494 */       return this.mTopicSample[doc][token];
/*      */     }
/*      */ 
/*      */     public int word(int doc, int token)
/*      */     {
/* 1512 */       return this.mDocWords[doc][token];
/*      */     }
/*      */ 
/*      */     public double documentTopicPrior()
/*      */     {
/* 1523 */       return this.mDocTopicPrior;
/*      */     }
/*      */ 
/*      */     public double topicWordPrior()
/*      */     {
/* 1532 */       return this.mTopicWordPrior;
/*      */     }
/*      */ 
/*      */     public int documentTopicCount(int doc, int topic)
/*      */     {
/* 1549 */       return this.mDocTopicCount[doc][topic];
/*      */     }
/*      */ 
/*      */     public int documentLength(int doc)
/*      */     {
/* 1562 */       return this.mDocWords[doc].length;
/*      */     }
/*      */ 
/*      */     public int topicWordCount(int topic, int word)
/*      */     {
/* 1579 */       return this.mWordTopicCount[word][topic];
/*      */     }
/*      */ 
/*      */     public int topicCount(int topic)
/*      */     {
/* 1593 */       return this.mTopicCount[topic];
/*      */     }
/*      */ 
/*      */     public int numChangedTopics()
/*      */     {
/* 1607 */       return this.mNumChangedTopics;
/*      */     }
/*      */ 
/*      */     public double topicWordProb(int topic, int word)
/*      */     {
/* 1628 */       return (topicWordCount(topic, word) + topicWordPrior()) / (topicCount(topic) + numWords() * topicWordPrior());
/*      */     }
/*      */ 
/*      */     public int wordCount(int word)
/*      */     {
/* 1643 */       int count = 0;
/* 1644 */       for (int topic = 0; topic < numTopics(); topic++)
/* 1645 */         count += topicWordCount(topic, word);
/* 1646 */       return count;
/*      */     }
/*      */ 
/*      */     public double documentTopicProb(int doc, int topic)
/*      */     {
/* 1668 */       return (documentTopicCount(doc, topic) + documentTopicPrior()) / (documentLength(doc) + numTopics() * documentTopicPrior());
/*      */     }
/*      */ 
/*      */     public double corpusLog2Probability()
/*      */     {
/* 1695 */       double corpusLog2Prob = 0.0D;
/* 1696 */       int numDocs = numDocuments();
/* 1697 */       int numTopics = numTopics();
/* 1698 */       for (int doc = 0; doc < numDocs; doc++) {
/* 1699 */         int docLength = documentLength(doc);
/* 1700 */         for (int token = 0; token < docLength; token++) {
/* 1701 */           int word = word(doc, token);
/* 1702 */           double wordProb = 0.0D;
/* 1703 */           for (int topic = 0; topic < numTopics; topic++) {
/* 1704 */             double wordTopicProbGivenDoc = topicWordProb(topic, word) * documentTopicProb(doc, topic);
/* 1705 */             wordProb += wordTopicProbGivenDoc;
/*      */           }
/* 1707 */           corpusLog2Prob += com.aliasi.util.Math.log2(wordProb);
/*      */         }
/*      */       }
/* 1710 */       return corpusLog2Prob;
/*      */     }
/*      */ 
/*      */     public LatentDirichletAllocation lda()
/*      */     {
/* 1723 */       int numTopics = numTopics();
/* 1724 */       int numWords = numWords();
/* 1725 */       double topicWordPrior = topicWordPrior();
/* 1726 */       double[][] topicWordProbs = new double[numTopics][numWords];
/* 1727 */       for (int topic = 0; topic < numTopics; topic++) {
/* 1728 */         double topicCount = topicCount(topic);
/* 1729 */         double denominator = topicCount + numWords * topicWordPrior;
/* 1730 */         for (int word = 0; word < numWords; word++) {
/* 1731 */           topicWordProbs[topic][word] = ((topicWordCount(topic, word) + topicWordPrior) / denominator);
/*      */         }
/*      */       }
/* 1734 */       return new LatentDirichletAllocation(this.mDocTopicPrior, topicWordProbs);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SampleIterator extends Iterators.Buffered<LatentDirichletAllocation.GibbsSample>
/*      */   {
/*      */     private final int[][] mDocWords;
/*      */     private final short mNumTopics;
/*      */     private final double mDocTopicPrior;
/*      */     private final double mTopicWordPrior;
/*      */     private final Random mRandom;
/*      */     private final int mNumDocs;
/*      */     private final int mNumWords;
/*      */     private final int mNumTokens;
/*      */     private final short[][] mCurrentSample;
/*      */     private final int[][] mDocTopicCount;
/*      */     private final int[][] mWordTopicCount;
/*      */     private final int[] mTopicTotalCount;
/*      */     private int mNumChangedTopics;
/* 1065 */     private int mEpoch = 0;
/*      */ 
/*      */     SampleIterator(int[][] docWords, short numTopics, double docTopicPrior, double topicWordPrior, Random random)
/*      */     {
/* 1072 */       this.mDocWords = docWords;
/* 1073 */       this.mNumTopics = numTopics;
/* 1074 */       this.mDocTopicPrior = docTopicPrior;
/* 1075 */       this.mTopicWordPrior = topicWordPrior;
/* 1076 */       this.mRandom = random;
/*      */ 
/* 1078 */       this.mNumDocs = this.mDocWords.length;
/* 1079 */       this.mNumWords = (LatentDirichletAllocation.max(this.mDocWords) + 1);
/*      */ 
/* 1081 */       int numTokens = 0;
/* 1082 */       for (int doc = 0; doc < this.mNumDocs; doc++)
/* 1083 */         numTokens += this.mDocWords[doc].length;
/* 1084 */       this.mNumTokens = numTokens;
/*      */ 
/* 1086 */       this.mNumChangedTopics = numTokens;
/*      */ 
/* 1088 */       this.mCurrentSample = new short[this.mNumDocs][];
/* 1089 */       for (int doc = 0; doc < this.mNumDocs; doc++) {
/* 1090 */         this.mCurrentSample[doc] = new short[this.mDocWords[doc].length];
/*      */       }
/* 1092 */       this.mDocTopicCount = new int[this.mNumDocs][numTopics];
/* 1093 */       this.mWordTopicCount = new int[this.mNumWords][numTopics];
/* 1094 */       this.mTopicTotalCount = new int[numTopics];
/*      */ 
/* 1097 */       for (int doc = 0; doc < this.mNumDocs; doc++)
/* 1098 */         for (int tok = 0; tok < docWords[doc].length; tok++) {
/* 1099 */           int word = docWords[doc][tok];
/* 1100 */           int topic = this.mRandom.nextInt(numTopics);
/* 1101 */           this.mCurrentSample[doc][tok] = ((short)topic);
/* 1102 */           this.mDocTopicCount[doc][topic] += 1;
/* 1103 */           this.mWordTopicCount[word][topic] += 1;
/* 1104 */           this.mTopicTotalCount[topic] += 1;
/*      */         }
/*      */     }
/*      */ 
/*      */     protected LatentDirichletAllocation.GibbsSample bufferNext()
/*      */     {
/* 1113 */       LatentDirichletAllocation.GibbsSample sample = new LatentDirichletAllocation.GibbsSample(this.mEpoch, this.mCurrentSample, this.mDocWords, this.mDocTopicPrior, this.mTopicWordPrior, this.mDocTopicCount, this.mWordTopicCount, this.mTopicTotalCount, this.mNumChangedTopics, this.mNumWords, this.mNumTokens);
/*      */ 
/* 1125 */       this.mEpoch += 1;
/* 1126 */       double numWordsTimesTopicWordPrior = this.mNumWords * this.mTopicWordPrior;
/* 1127 */       double[] topicDistro = new double[this.mNumTopics];
/* 1128 */       int numChangedTopics = 0;
/* 1129 */       for (int doc = 0; doc < this.mNumDocs; doc++) {
/* 1130 */         int[] docWordsDoc = this.mDocWords[doc];
/* 1131 */         short[] currentSampleDoc = this.mCurrentSample[doc];
/* 1132 */         int[] docTopicCountDoc = this.mDocTopicCount[doc];
/* 1133 */         for (int tok = 0; tok < docWordsDoc.length; tok++) {
/* 1134 */           int word = docWordsDoc[tok];
/* 1135 */           int[] wordTopicCountWord = this.mWordTopicCount[word];
/* 1136 */           int currentTopic = currentSampleDoc[tok];
/* 1137 */           if (currentTopic == 0) {
/* 1138 */             topicDistro[0] = ((docTopicCountDoc[0] - 1.0D + this.mDocTopicPrior) * (wordTopicCountWord[0] - 1.0D + this.mTopicWordPrior) / (this.mTopicTotalCount[0] - 1.0D + numWordsTimesTopicWordPrior));
/*      */           }
/*      */           else
/*      */           {
/* 1143 */             topicDistro[0] = ((docTopicCountDoc[0] + this.mDocTopicPrior) * (wordTopicCountWord[0] + this.mTopicWordPrior) / (this.mTopicTotalCount[0] + numWordsTimesTopicWordPrior));
/*      */ 
/* 1147 */             for (int topic = 1; topic < currentTopic; topic++) {
/* 1148 */               topicDistro[topic] = ((docTopicCountDoc[topic] + this.mDocTopicPrior) * (wordTopicCountWord[topic] + this.mTopicWordPrior) / (this.mTopicTotalCount[topic] + numWordsTimesTopicWordPrior) + topicDistro[(topic - 1)]);
/*      */             }
/*      */ 
/* 1154 */             topicDistro[currentTopic] = ((docTopicCountDoc[currentTopic] - 1.0D + this.mDocTopicPrior) * (wordTopicCountWord[currentTopic] - 1.0D + this.mTopicWordPrior) / (this.mTopicTotalCount[currentTopic] - 1.0D + numWordsTimesTopicWordPrior) + topicDistro[(currentTopic - 1)]);
/*      */           }
/*      */ 
/* 1160 */           for (int topic = currentTopic + 1; topic < this.mNumTopics; topic++) {
/* 1161 */             topicDistro[topic] = ((docTopicCountDoc[topic] + this.mDocTopicPrior) * (wordTopicCountWord[topic] + this.mTopicWordPrior) / (this.mTopicTotalCount[topic] + numWordsTimesTopicWordPrior) + topicDistro[(topic - 1)]);
/*      */           }
/*      */ 
/* 1167 */           int sampledTopic = Statistics.sample(topicDistro, this.mRandom);
/* 1168 */           if (sampledTopic != currentTopic) {
/* 1169 */             currentSampleDoc[tok] = ((short)sampledTopic);
/* 1170 */             docTopicCountDoc[currentTopic] -= 1;
/* 1171 */             wordTopicCountWord[currentTopic] -= 1;
/* 1172 */             this.mTopicTotalCount[currentTopic] -= 1;
/* 1173 */             docTopicCountDoc[sampledTopic] += 1;
/* 1174 */             wordTopicCountWord[sampledTopic] += 1;
/* 1175 */             this.mTopicTotalCount[sampledTopic] += 1;
/* 1176 */             numChangedTopics++;
/*      */           }
/*      */         }
/*      */       }
/* 1180 */       this.mNumChangedTopics = numChangedTopics;
/* 1181 */       return sample;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.LatentDirichletAllocation
 * JD-Core Version:    0.6.2
 */