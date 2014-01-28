/*      */ package com.aliasi.lm;
/*      */ 
/*      */ import com.aliasi.corpus.ObjectHandler;
/*      */ import com.aliasi.stats.BinomialDistribution;
/*      */ import com.aliasi.stats.Statistics;
/*      */ import com.aliasi.symbol.MapSymbolTable;
/*      */ import com.aliasi.symbol.SymbolTable;
/*      */ import com.aliasi.tokenizer.Tokenizer;
/*      */ import com.aliasi.tokenizer.TokenizerFactory;
/*      */ import com.aliasi.util.AbstractExternalizable;
/*      */ import com.aliasi.util.Arrays;
/*      */ import com.aliasi.util.BoundedPriorityQueue;
/*      */ import com.aliasi.util.Exceptions;
/*      */ import com.aliasi.util.ScoredObject;
/*      */ import com.aliasi.util.Strings;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.SortedSet;
/*      */ 
/*      */ public class TokenizedLM
/*      */   implements LanguageModel.Dynamic, LanguageModel.Sequence, LanguageModel.Tokenized, ObjectHandler<CharSequence>
/*      */ {
/*      */   private final TokenizerFactory mTokenizerFactory;
/*      */   private final MapSymbolTable mSymbolTable;
/*      */   private final TrieIntSeqCounter mCounter;
/*      */   private final LanguageModel.Sequence mUnknownTokenModel;
/*      */   private final LanguageModel.Sequence mWhitespaceModel;
/*      */   private final double mLambdaFactor;
/*      */   private final LanguageModel.Dynamic mDynamicUnknownTokenModel;
/*      */   private final LanguageModel.Dynamic mDynamicWhitespaceModel;
/*      */   private final int mNGramOrder;
/*      */   public static final int UNKNOWN_TOKEN = -1;
/*      */   public static final int BOUNDARY_TOKEN = -2;
/* 1193 */   static final ScoredObject[] EMPTY_SCORED_OBJECT_ARRAY = new ScoredObject[0];
/*      */ 
/* 1196 */   static final ScoredObject<String[]>[] EMPTY_SCORED_OBJECT_STRING_ARRAY_ARRAY = emptyScoredObjectArray();
/*      */ 
/*      */   public TokenizedLM(TokenizerFactory factory, int nGramOrder)
/*      */   {
/*  168 */     this(factory, nGramOrder, new UniformBoundaryLM(), new UniformBoundaryLM(), nGramOrder);
/*      */   }
/*      */ 
/*      */   public TokenizedLM(TokenizerFactory tokenizerFactory, int nGramOrder, LanguageModel.Sequence unknownTokenModel, LanguageModel.Sequence whitespaceModel, double lambdaFactor)
/*      */   {
/*  201 */     this(tokenizerFactory, nGramOrder, unknownTokenModel, whitespaceModel, lambdaFactor, true);
/*      */   }
/*      */ 
/*      */   public TokenizedLM(TokenizerFactory tokenizerFactory, int nGramOrder, LanguageModel.Sequence unknownTokenModel, LanguageModel.Sequence whitespaceModel, double lambdaFactor, boolean initialIncrementBoundary)
/*      */   {
/*  239 */     NGramProcessLM.checkMaxNGram(nGramOrder);
/*  240 */     NGramProcessLM.checkLambdaFactor(lambdaFactor);
/*  241 */     this.mSymbolTable = new MapSymbolTable();
/*  242 */     this.mNGramOrder = nGramOrder;
/*  243 */     this.mTokenizerFactory = tokenizerFactory;
/*  244 */     this.mUnknownTokenModel = unknownTokenModel;
/*  245 */     this.mWhitespaceModel = whitespaceModel;
/*  246 */     this.mDynamicUnknownTokenModel = ((this.mUnknownTokenModel instanceof LanguageModel.Dynamic) ? (LanguageModel.Dynamic)this.mUnknownTokenModel : null);
/*      */ 
/*  250 */     this.mDynamicWhitespaceModel = ((this.mWhitespaceModel instanceof LanguageModel.Dynamic) ? (LanguageModel.Dynamic)this.mWhitespaceModel : null);
/*      */ 
/*  254 */     this.mCounter = new TrieIntSeqCounter(nGramOrder);
/*  255 */     this.mLambdaFactor = lambdaFactor;
/*      */ 
/*  258 */     if (initialIncrementBoundary)
/*  259 */       this.mCounter.incrementSubsequences(new int[] { -2 }, 0, 1);
/*      */   }
/*      */ 
/*      */   public double lambdaFactor()
/*      */   {
/*  270 */     return this.mLambdaFactor;
/*      */   }
/*      */ 
/*      */   public TrieIntSeqCounter sequenceCounter()
/*      */   {
/*  283 */     return this.mCounter;
/*      */   }
/*      */ 
/*      */   public SymbolTable symbolTable()
/*      */   {
/*  294 */     return this.mSymbolTable;
/*      */   }
/*      */ 
/*      */   public int nGramOrder()
/*      */   {
/*  305 */     return this.mNGramOrder;
/*      */   }
/*      */ 
/*      */   public TokenizerFactory tokenizerFactory()
/*      */   {
/*  316 */     return this.mTokenizerFactory;
/*      */   }
/*      */ 
/*      */   public LanguageModel.Sequence unknownTokenLM()
/*      */   {
/*  327 */     return this.mUnknownTokenModel;
/*      */   }
/*      */ 
/*      */   public LanguageModel.Sequence whitespaceLM()
/*      */   {
/*  338 */     return this.mWhitespaceModel;
/*      */   }
/*      */ 
/*      */   public void compileTo(ObjectOutput objOut)
/*      */     throws IOException
/*      */   {
/*  352 */     objOut.writeObject(new Externalizer(this));
/*      */   }
/*      */ 
/*      */   public void handleNGrams(int nGramLength, int minCount, ObjectHandler<String[]> handler)
/*      */   {
/*  367 */     StringArrayAdapter adapter = new StringArrayAdapter(handler);
/*  368 */     this.mCounter.handleNGrams(nGramLength, minCount, adapter);
/*      */   }
/*      */ 
/*      */   double lambda(int[] tokIds)
/*      */   {
/*  376 */     double numExtensionsD = this.mCounter.numExtensions(tokIds, 0, tokIds.length);
/*  377 */     double extCountD = this.mCounter.extensionCount(tokIds, 0, tokIds.length);
/*  378 */     return extCountD / (extCountD + this.mLambdaFactor * numExtensionsD);
/*      */   }
/*      */ 
/*      */   public void train(CharSequence cSeq)
/*      */   {
/*  389 */     char[] cs = Strings.toCharArray(cSeq);
/*  390 */     train(cs, 0, cs.length);
/*      */   }
/*      */ 
/*      */   public void train(CharSequence cSeq, int count)
/*      */   {
/*  404 */     if (count < 0) {
/*  405 */       String msg = "Counts must be non-negative. Found count=" + count;
/*      */ 
/*  407 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  409 */     if (count == 0) return;
/*  410 */     char[] cs = Strings.toCharArray(cSeq);
/*  411 */     train(cs, 0, cs.length, count);
/*      */   }
/*      */ 
/*      */   public void train(char[] cs, int start, int end)
/*      */   {
/*  425 */     Strings.checkArgsStartEnd(cs, start, end);
/*  426 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/*  427 */     List tokenList = new ArrayList();
/*      */     while (true) {
/*  429 */       if (this.mDynamicWhitespaceModel != null) {
/*  430 */         String whitespace = tokenizer.nextWhitespace();
/*  431 */         this.mDynamicWhitespaceModel.train(whitespace);
/*      */       }
/*  433 */       String token = tokenizer.nextToken();
/*  434 */       if (token == null) break;
/*  435 */       tokenList.add(token);
/*      */     }
/*  437 */     int[] tokIds = new int[tokenList.size() + 2];
/*  438 */     tokIds[0] = -2;
/*  439 */     tokIds[(tokIds.length - 1)] = -2;
/*  440 */     Iterator it = tokenList.iterator();
/*  441 */     for (int i = 1; it.hasNext(); i++) {
/*  442 */       String token = (String)it.next();
/*      */ 
/*  444 */       if ((this.mDynamicUnknownTokenModel != null) && (this.mSymbolTable.symbolToID(token) < 0))
/*      */       {
/*  446 */         this.mDynamicUnknownTokenModel.train(token);
/*      */       }
/*  448 */       tokIds[i] = this.mSymbolTable.getOrAddSymbol(token);
/*      */     }
/*  450 */     this.mCounter.incrementSubsequences(tokIds, 0, tokIds.length);
/*  451 */     this.mCounter.decrementUnigram(-2);
/*      */   }
/*      */ 
/*      */   public void handle(CharSequence cs)
/*      */   {
/*  465 */     train(cs, 1);
/*      */   }
/*      */ 
/*      */   public void train(char[] cs, int start, int end, int count)
/*      */   {
/*  481 */     Strings.checkArgsStartEnd(cs, start, end);
/*  482 */     if (count < 0) {
/*  483 */       String msg = "Counts must be non-negative. Found count=" + count;
/*      */ 
/*  485 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  487 */     if (count == 0) return;
/*  488 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/*  489 */     List tokenList = new ArrayList();
/*      */     while (true) {
/*  491 */       if (this.mDynamicWhitespaceModel != null) {
/*  492 */         String whitespace = tokenizer.nextWhitespace();
/*  493 */         this.mDynamicWhitespaceModel.train(whitespace, count);
/*      */       }
/*  495 */       String token = tokenizer.nextToken();
/*  496 */       if (token == null) break;
/*  497 */       tokenList.add(token);
/*      */     }
/*  499 */     int[] tokIds = new int[tokenList.size() + 2];
/*  500 */     tokIds[0] = -2;
/*  501 */     tokIds[(tokIds.length - 1)] = -2;
/*  502 */     Iterator it = tokenList.iterator();
/*  503 */     for (int i = 1; it.hasNext(); i++) {
/*  504 */       String token = (String)it.next();
/*      */ 
/*  506 */       if ((this.mDynamicUnknownTokenModel != null) && (this.mSymbolTable.symbolToID(token) < 0))
/*      */       {
/*  508 */         this.mDynamicUnknownTokenModel.train(token, count);
/*      */       }
/*  510 */       tokIds[i] = this.mSymbolTable.getOrAddSymbol(token);
/*      */     }
/*  512 */     this.mCounter.incrementSubsequences(tokIds, 0, tokIds.length, count);
/*  513 */     this.mCounter.decrementUnigram(-2, count);
/*      */   }
/*      */ 
/*      */   void trainSequence(char[] cs, int start, int end, int count)
/*      */   {
/*  530 */     Strings.checkArgsStartEnd(cs, start, end);
/*  531 */     if (count < 0) {
/*  532 */       String msg = "Count must be non-negative.  Found count=" + count;
/*  533 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  535 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/*  536 */     String[] tokens = tokenizer.tokenize();
/*  537 */     int len = java.lang.Math.min(tokens.length, nGramOrder());
/*  538 */     int offset = tokens.length - len;
/*  539 */     int[] tokIds = new int[len];
/*  540 */     for (int i = 0; i < len; i++)
/*  541 */       tokIds[i] = this.mSymbolTable.getOrAddSymbol(tokens[(i + offset)]);
/*  542 */     this.mCounter.incrementSequence(tokIds, 0, len, count);
/*      */   }
/*      */ 
/*      */   public void trainSequence(CharSequence cSeq, int count)
/*      */   {
/*  583 */     char[] cs = Strings.toCharArray(cSeq);
/*  584 */     trainSequence(cs, 0, cs.length, count);
/*      */   }
/*      */ 
/*      */   public double log2Estimate(CharSequence cSeq) {
/*  588 */     char[] cs = Strings.toCharArray(cSeq);
/*  589 */     return log2Estimate(cs, 0, cs.length);
/*      */   }
/*      */ 
/*      */   public double log2Estimate(char[] cs, int start, int end) {
/*  593 */     Strings.checkArgsStartEnd(cs, start, end);
/*  594 */     double logEstimate = 0.0D;
/*      */ 
/*  597 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/*  598 */     List tokenList = new ArrayList();
/*      */     while (true) {
/*  600 */       String whitespace = tokenizer.nextWhitespace();
/*  601 */       logEstimate += this.mWhitespaceModel.log2Estimate(whitespace);
/*  602 */       String token = tokenizer.nextToken();
/*  603 */       if (token == null) break;
/*  604 */       tokenList.add(token);
/*      */     }
/*      */ 
/*  608 */     int[] tokIds = new int[tokenList.size() + 2];
/*  609 */     tokIds[0] = -2;
/*  610 */     tokIds[(tokIds.length - 1)] = -2;
/*  611 */     Iterator it = tokenList.iterator();
/*  612 */     for (int i = 1; it.hasNext(); i++) {
/*  613 */       String token = (String)it.next();
/*  614 */       tokIds[i] = this.mSymbolTable.symbolToID(token);
/*  615 */       if (tokIds[i] < 0) {
/*  616 */         logEstimate += this.mUnknownTokenModel.log2Estimate(token);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  621 */     for (int i = 2; i <= tokIds.length; i++) {
/*  622 */       logEstimate += conditionalLog2TokenEstimate(tokIds, 0, i);
/*      */     }
/*  624 */     return logEstimate;
/*      */   }
/*      */ 
/*      */   String[] nGramToTokens(int[] nGram)
/*      */   {
/*  717 */     String[] toks = new String[nGram.length];
/*  718 */     for (int i = 0; i < nGram.length; i++) {
/*  719 */       toks[i] = (i == 0 ? "*BEGIN*" : nGram[i] >= 0 ? this.mSymbolTable.idToSymbol(nGram[i]) : "*END*");
/*      */     }
/*      */ 
/*  723 */     return toks;
/*      */   }
/*      */ 
/*      */   public double tokenProbability(String[] tokens, int start, int end) {
/*  727 */     return java.lang.Math.pow(2.0D, tokenLog2Probability(tokens, start, end));
/*      */   }
/*      */ 
/*      */   public double tokenLog2Probability(String[] tokens, int start, int end)
/*      */   {
/*  733 */     double log2Estimate = 0.0D;
/*  734 */     int[] tokIds = new int[tokens.length];
/*  735 */     for (int i = start; i < end; i++) {
/*  736 */       tokIds[i] = this.mSymbolTable.symbolToID(tokens[i]);
/*  737 */       double conditionalLog2TokenEstimate = conditionalLog2TokenEstimate(tokIds, 0, i + 1);
/*      */ 
/*  739 */       if (Double.isInfinite(conditionalLog2TokenEstimate)) {
/*  740 */         double extCountD = this.mCounter.extensionCount(new int[0], 0, 0);
/*  741 */         double numTokensD = this.mSymbolTable.numSymbols();
/*  742 */         log2Estimate += com.aliasi.util.Math.log2(extCountD / (extCountD + numTokensD));
/*      */ 
/*  745 */         log2Estimate += this.mUnknownTokenModel.log2Estimate(tokens[i]);
/*      */       } else {
/*  747 */         log2Estimate += conditionalLog2TokenEstimate;
/*      */       }
/*  749 */       if (Double.isInfinite(log2Estimate)) {
/*  750 */         System.out.println("tokens[" + i + "]=" + tokens[i] + "\n     id=" + tokIds[i]);
/*      */       }
/*      */     }
/*      */ 
/*  754 */     return log2Estimate;
/*      */   }
/*      */ 
/*      */   public double processLog2Probability(String[] tokens)
/*      */   {
/*  766 */     return tokenLog2Probability(tokens, 0, tokens.length);
/*      */   }
/*      */ 
/*      */   public SortedSet<ScoredObject<String[]>> collocationSet(int nGram, int minCount, int maxReturned)
/*      */   {
/*  789 */     CollocationCollector collector = new CollocationCollector(maxReturned);
/*  790 */     this.mCounter.handleNGrams(nGram, minCount, collector);
/*  791 */     return collector.nGramSet();
/*      */   }
/*      */ 
/*      */   public SortedSet<ScoredObject<String[]>> newTermSet(int nGram, int minCount, int maxReturned, LanguageModel.Tokenized backgroundLM)
/*      */   {
/*  833 */     return sigTermSet(nGram, minCount, maxReturned, backgroundLM, false);
/*      */   }
/*      */ 
/*      */   public SortedSet<ScoredObject<String[]>> oldTermSet(int nGram, int minCount, int maxReturned, LanguageModel.Tokenized backgroundLM)
/*      */   {
/*  861 */     return sigTermSet(nGram, minCount, maxReturned, backgroundLM, true);
/*      */   }
/*      */ 
/*      */   private ScoredObject<String[]>[] sigTerms(int nGram, int minCount, int maxReturned, LanguageModel.Tokenized backgroundLM, boolean reverse)
/*      */   {
/*  868 */     SigTermCollector collector = new SigTermCollector(maxReturned, backgroundLM, reverse);
/*      */ 
/*  870 */     this.mCounter.handleNGrams(nGram, minCount, collector);
/*  871 */     return collector.nGrams();
/*      */   }
/*      */ 
/*      */   private SortedSet<ScoredObject<String[]>> sigTermSet(int nGram, int minCount, int maxReturned, LanguageModel.Tokenized backgroundLM, boolean reverse)
/*      */   {
/*  878 */     SigTermCollector collector = new SigTermCollector(maxReturned, backgroundLM, reverse);
/*      */ 
/*  880 */     this.mCounter.handleNGrams(nGram, minCount, collector);
/*  881 */     return collector.nGramSet();
/*      */   }
/*      */ 
/*      */   public SortedSet<ScoredObject<String[]>> frequentTermSet(int nGram, int maxReturned)
/*      */   {
/*  900 */     return freqTermSet(nGram, maxReturned, false);
/*      */   }
/*      */ 
/*      */   private ScoredObject<String[]>[] freqTerms(int nGram, int maxReturned, boolean reverse)
/*      */   {
/*  905 */     FreqTermCollector collector = new FreqTermCollector(maxReturned, reverse);
/*      */ 
/*  907 */     this.mCounter.handleNGrams(nGram, 1, collector);
/*  908 */     return collector.nGrams();
/*      */   }
/*      */ 
/*      */   private SortedSet<ScoredObject<String[]>> freqTermSet(int nGram, int maxReturned, boolean reverse)
/*      */   {
/*  913 */     FreqTermCollector collector = new FreqTermCollector(maxReturned, reverse);
/*      */ 
/*  915 */     this.mCounter.handleNGrams(nGram, 1, collector);
/*  916 */     return collector.nGramSet();
/*      */   }
/*      */ 
/*      */   public SortedSet<ScoredObject<String[]>> infrequentTermSet(int nGram, int maxReturned)
/*      */   {
/*  934 */     return freqTermSet(nGram, maxReturned, true);
/*      */   }
/*      */ 
/*      */   public double chiSquaredIndependence(int[] nGram)
/*      */   {
/*  992 */     if (nGram.length < 2) {
/*  993 */       String msg = "Require n-gram >= 2 for chi square independence. Found nGram length=" + nGram.length;
/*      */ 
/*  995 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  997 */     if (nGram.length == 2) {
/*  998 */       return chiSquaredSplit(nGram, 1);
/*      */     }
/* 1000 */     double bestScore = (-1.0D / 0.0D);
/* 1001 */     for (int mid = 1; mid + 1 < nGram.length; mid++) {
/* 1002 */       bestScore = java.lang.Math.max(bestScore, chiSquaredSplit(nGram, mid));
/*      */     }
/* 1004 */     return bestScore;
/*      */   }
/*      */ 
/*      */   public double z(int[] nGram, int nGramSampleCount, int totalSampleCount)
/*      */   {
/* 1029 */     double totalCount = this.mCounter.count(nGram, 0, 0);
/* 1030 */     double nGramCount = this.mCounter.count(nGram, 0, nGram.length);
/* 1031 */     double successProbability = nGramCount / totalCount;
/* 1032 */     return BinomialDistribution.z(successProbability, nGramSampleCount, totalSampleCount);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1045 */     return this.mCounter.mRootNode.toString(this.mSymbolTable);
/*      */   }
/*      */ 
/*      */   private double conditionalLog2TokenEstimate(int[] tokIds, int start, int end)
/*      */   {
/* 1050 */     if (end < 1) return 0.0D;
/* 1051 */     int maxLength = this.mCounter.maxLength();
/* 1052 */     int contextEnd = end - 1;
/*      */ 
/* 1054 */     double estimate = tokIds[(end - 1)] == -1 ? 1.0D : 0.0D;
/* 1055 */     for (int contextStart = end - 1; 
/* 1056 */       (contextStart >= start) && (end - contextStart <= maxLength); 
/* 1058 */       contextStart--) {
/* 1059 */       int numExtensions = this.mCounter.numExtensions(tokIds, contextStart, contextEnd);
/*      */ 
/* 1061 */       if (numExtensions == 0) break;
/* 1062 */       double extCountD = this.mCounter.extensionCount(tokIds, contextStart, contextEnd);
/*      */ 
/* 1064 */       double lambda = extCountD / (extCountD + this.mLambdaFactor * numExtensions);
/*      */ 
/* 1067 */       estimate *= (1.0D - lambda);
/* 1068 */       if (tokIds[(end - 1)] != -1) {
/* 1069 */         int count = this.mCounter.count(tokIds, contextStart, end);
/* 1070 */         if (count > 0)
/* 1071 */           estimate += lambda * count / extCountD; 
/*      */       }
/*      */     }
/* 1073 */     return com.aliasi.util.Math.log2(estimate);
/*      */   }
/*      */ 
/*      */   private double chiSquaredSplit(int[] pair, int mid)
/*      */   {
/* 1081 */     long count12 = this.mCounter.count(pair, 0, pair.length);
/* 1082 */     long count1_ = this.mCounter.count(pair, 0, mid);
/* 1083 */     long count_2 = this.mCounter.count(pair, mid, pair.length);
/* 1084 */     long n = this.mCounter.extensionCount(pair, 0, 0);
/* 1085 */     long countxy = n - count1_ - count_2 + count12;
/* 1086 */     long countx2 = count_2 - count12;
/* 1087 */     long count1y = count1_ - count12;
/* 1088 */     return Statistics.chiSquaredIndependence(count12, count1y, countx2, countxy);
/*      */   }
/*      */ 
/*      */   private int lastInternalNodeIndex() {
/* 1092 */     int last = 1;
/* 1093 */     LinkedList queue = new LinkedList();
/* 1094 */     queue.add(this.mCounter.mRootNode);
/* 1095 */     for (int i = 1; !queue.isEmpty(); i++) {
/* 1096 */       IntNode node = (IntNode)queue.removeFirst();
/* 1097 */       if (node.numExtensions() > 0)
/* 1098 */         last = i;
/* 1099 */       node.addDaughters(queue);
/*      */     }
/* 1101 */     return last - 1;
/*      */   }
/*      */ 
/*      */   private static int[] concatenate(int[] is, int i)
/*      */   {
/* 1116 */     int[] result = new int[is.length + 1];
/* 1117 */     System.arraycopy(is, 0, result, 0, is.length);
/* 1118 */     result[is.length] = i;
/* 1119 */     return result;
/*      */   }
/*      */ 
/*      */   static ScoredObject<String[]>[] emptyScoredObjectArray()
/*      */   {
/* 1201 */     ScoredObject[] result = (ScoredObject[])EMPTY_SCORED_OBJECT_ARRAY;
/*      */ 
/* 1203 */     return result;
/*      */   }
/*      */ 
/*      */   static class Externalizer extends AbstractExternalizable
/*      */   {
/*      */     private static final long serialVersionUID = 6135272620545804504L;
/*      */     final TokenizedLM mLM;
/*      */ 
/*      */     public Externalizer()
/*      */     {
/* 1126 */       this(null);
/*      */     }
/*      */     public Externalizer(TokenizedLM lm) {
/* 1129 */       this.mLM = lm;
/*      */     }
/*      */ 
/*      */     public Object read(ObjectInput in) throws IOException {
/*      */       try {
/* 1134 */         return new CompiledTokenizedLM(in);
/*      */       } catch (ClassNotFoundException e) {
/* 1136 */         throw Exceptions.toIO("TokenizedLM.Externalizer.read()", e);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 1141 */       if ((this.mLM.mTokenizerFactory instanceof Serializable)) {
/* 1142 */         objOut.writeUTF("");
/* 1143 */         objOut.writeObject(this.mLM.mTokenizerFactory);
/*      */       } else {
/* 1145 */         objOut.writeUTF(this.mLM.mTokenizerFactory.getClass().getName());
/*      */       }
/* 1147 */       objOut.writeObject(this.mLM.mSymbolTable);
/* 1148 */       ((LanguageModel.Dynamic)this.mLM.mUnknownTokenModel).compileTo(objOut);
/* 1149 */       ((LanguageModel.Dynamic)this.mLM.mWhitespaceModel).compileTo(objOut);
/* 1150 */       objOut.writeInt(this.mLM.mNGramOrder);
/*      */ 
/* 1152 */       int numNodes = this.mLM.mCounter.mRootNode.trieSize();
/* 1153 */       objOut.writeInt(numNodes);
/*      */ 
/* 1155 */       int lastInternalNodeIndex = this.mLM.lastInternalNodeIndex();
/* 1156 */       objOut.writeInt(lastInternalNodeIndex);
/*      */ 
/* 1159 */       objOut.writeInt(-2147483648);
/* 1160 */       objOut.writeFloat((0.0F / 0.0F));
/* 1161 */       objOut.writeFloat((float)com.aliasi.util.Math.log2(1.0D - this.mLM.lambda(Arrays.EMPTY_INT_ARRAY)));
/*      */ 
/* 1165 */       objOut.writeInt(1);
/*      */ 
/* 1167 */       LinkedList queue = new LinkedList();
/* 1168 */       int[] outcomes = this.mLM.mCounter.mRootNode.integersFollowing(Arrays.EMPTY_INT_ARRAY, 0, 0);
/*      */ 
/* 1171 */       for (int i = 0; i < outcomes.length; i++)
/* 1172 */         queue.add(new int[] { outcomes[i] });
/* 1173 */       for (int i = 1; !queue.isEmpty(); i++) {
/* 1174 */         int[] is = (int[])queue.removeFirst();
/* 1175 */         objOut.writeInt(is[(is.length - 1)]);
/* 1176 */         objOut.writeFloat((float)this.mLM.conditionalLog2TokenEstimate(is, 0, is.length));
/*      */ 
/* 1178 */         if (i <= lastInternalNodeIndex) {
/* 1179 */           objOut.writeFloat((float)com.aliasi.util.Math.log2(1.0D - this.mLM.lambda(is)));
/*      */ 
/* 1181 */           objOut.writeInt(i + queue.size() + 1);
/*      */         }
/* 1183 */         int[] followers = this.mLM.mCounter.mRootNode.integersFollowing(is, 0, is.length);
/*      */ 
/* 1185 */         for (int j = 0; j < followers.length; j++)
/* 1186 */           queue.add(TokenizedLM.concatenate(is, followers[j]));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class SigTermCollector extends TokenizedLM.Collector
/*      */   {
/*      */     final LanguageModel.Tokenized mBGModel;
/*      */ 
/*      */     SigTermCollector(int maxReturned, LanguageModel.Tokenized bgModel, boolean reverse)
/*      */     {
/*  699 */       super(maxReturned, reverse);
/*  700 */       this.mBGModel = bgModel;
/*      */     }
/*      */ 
/*      */     double scoreNGram(int[] nGram) {
/*  704 */       String[] tokens = TokenizedLM.this.nGramToTokens(nGram);
/*  705 */       int totalSampleCount = TokenizedLM.this.mCounter.count(nGram, 0, 0);
/*  706 */       int sampleCount = TokenizedLM.this.mCounter.count(nGram, 0, nGram.length);
/*  707 */       double bgProb = this.mBGModel.tokenProbability(tokens, 0, tokens.length);
/*      */ 
/*  709 */       double score = BinomialDistribution.z(bgProb, sampleCount, totalSampleCount);
/*      */ 
/*  712 */       return score;
/*      */     }
/*      */   }
/*      */ 
/*      */   class CollocationCollector extends TokenizedLM.Collector
/*      */   {
/*      */     CollocationCollector(int maxReturned)
/*      */     {
/*  687 */       super(maxReturned, false);
/*      */     }
/*      */ 
/*      */     double scoreNGram(int[] nGram) {
/*  691 */       return TokenizedLM.this.chiSquaredIndependence(nGram);
/*      */     }
/*      */   }
/*      */ 
/*      */   class FreqTermCollector extends TokenizedLM.Collector
/*      */   {
/*      */     FreqTermCollector(int maxReturned, boolean reverse)
/*      */     {
/*  677 */       super(maxReturned, reverse);
/*      */     }
/*      */ 
/*      */     double scoreNGram(int[] nGram) {
/*  681 */       return TokenizedLM.this.mCounter.count(nGram, 0, nGram.length);
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class Collector
/*      */     implements ObjectHandler<int[]>
/*      */   {
/*      */     final BoundedPriorityQueue<ScoredObject<String[]>> mBPQ;
/*      */ 
/*      */     Collector(int maxReturned, boolean reverse)
/*      */     {
/*  649 */       Comparator comparator = null;
/*  650 */       if (reverse)
/*  651 */         comparator = ScoredObject.reverseComparator();
/*      */       else
/*  653 */         comparator = ScoredObject.comparator();
/*  654 */       this.mBPQ = new BoundedPriorityQueue(comparator, maxReturned);
/*      */     }
/*      */ 
/*      */     SortedSet<ScoredObject<String[]>> nGramSet() {
/*  658 */       return this.mBPQ;
/*      */     }
/*      */ 
/*      */     ScoredObject<String[]>[] nGrams() {
/*  662 */       return (ScoredObject[])this.mBPQ.toArray(TokenizedLM.EMPTY_SCORED_OBJECT_STRING_ARRAY_ARRAY);
/*      */     }
/*      */     public void handle(int[] nGram) {
/*  665 */       for (int i = 0; i < nGram.length; i++)
/*  666 */         if (nGram[i] < 0) return;
/*  667 */       this.mBPQ.offer(new ScoredObject(TokenizedLM.this.nGramToTokens(nGram), scoreNGram(nGram)));
/*      */     }
/*      */ 
/*      */     abstract double scoreNGram(int[] paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   class StringArrayAdapter
/*      */     implements ObjectHandler<int[]>
/*      */   {
/*      */     ObjectHandler<String[]> mHandler;
/*      */ 
/*      */     public StringArrayAdapter()
/*      */     {
/*  630 */       this.mHandler = handler;
/*      */     }
/*      */     public void handle(int[] nGram) {
/*  633 */       this.mHandler.handle(simpleNGramToTokens(nGram));
/*      */     }
/*      */     String[] simpleNGramToTokens(int[] nGram) {
/*  636 */       String[] tokens = new String[nGram.length];
/*  637 */       for (int i = 0; i < tokens.length; i++) {
/*  638 */         tokens[i] = (nGram[i] >= 0 ? TokenizedLM.this.mSymbolTable.idToSymbol(nGram[i]) : null);
/*      */       }
/*      */ 
/*  642 */       return tokens;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TokenizedLM
 * JD-Core Version:    0.6.2
 */