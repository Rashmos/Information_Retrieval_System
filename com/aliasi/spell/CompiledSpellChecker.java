/*      */ package com.aliasi.spell;
/*      */ 
/*      */ import com.aliasi.lm.CompiledNGramProcessLM;
/*      */ import com.aliasi.tokenizer.Tokenizer;
/*      */ import com.aliasi.tokenizer.TokenizerFactory;
/*      */ import com.aliasi.util.AbstractExternalizable;
/*      */ import com.aliasi.util.BoundedPriorityQueue;
/*      */ import com.aliasi.util.Compilable;
/*      */ import com.aliasi.util.Iterators;
/*      */ import com.aliasi.util.Scored;
/*      */ import com.aliasi.util.ScoredObject;
/*      */ import com.aliasi.util.SmallSet;
/*      */ import com.aliasi.util.Strings;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class CompiledSpellChecker
/*      */   implements SpellChecker
/*      */ {
/*      */   CompiledNGramProcessLM mLM;
/*      */   WeightedEditDistance mEditDistance;
/*      */   Set<String> mTokenSet;
/*  244 */   Set<String> mDoNotEditTokens = SmallSet.create();
/*      */   int mNBestSize;
/*  247 */   boolean mAllowInsert = true;
/*  248 */   boolean mAllowDelete = true;
/*  249 */   boolean mAllowMatch = true;
/*  250 */   boolean mAllowSubstitute = true;
/*  251 */   boolean mAllowTranspose = true;
/*  252 */   int mMinimumTokenLengthToCorrect = 0;
/*  253 */   int mNumConsecutiveInsertionsAllowed = 1;
/*      */   double mKnownTokenEditCost;
/*      */   double mFirstCharEditCost;
/*      */   double mSecondCharEditCost;
/*      */   TokenizerFactory mTokenizerFactory;
/*      */   TokenTrieNode mTokenPrefixTrie;
/* 1250 */   public static WeightedEditDistance CASE_RESTORING = new CaseRestoring();
/*      */ 
/* 1271 */   public static WeightedEditDistance TOKENIZING = new Tokenizing();
/*      */   static final int DEFAULT_N_BEST_SIZE = 64;
/*      */   static final double DEFAULT_KNOWN_TOKEN_EDIT_COST = -2.0D;
/*      */   static final double DEFAULT_FIRST_CHAR_EDIT_COST = -1.5D;
/*      */   static final double DEFAULT_SECOND_CHAR_EDIT_COST = -1.0D;
/*      */ 
/*      */   public CompiledSpellChecker(CompiledNGramProcessLM lm, WeightedEditDistance editDistance, TokenizerFactory factory, Set<String> tokenSet, int nBestSize, double knownTokenEditCost, double firstCharEditCost, double secondCharEditCost)
/*      */   {
/*  302 */     this.mLM = lm;
/*  303 */     this.mEditDistance = editDistance;
/*  304 */     this.mTokenizerFactory = factory;
/*  305 */     this.mNBestSize = nBestSize;
/*  306 */     setTokenSet(tokenSet);
/*  307 */     this.mKnownTokenEditCost = knownTokenEditCost;
/*  308 */     this.mFirstCharEditCost = firstCharEditCost;
/*  309 */     this.mSecondCharEditCost = secondCharEditCost;
/*      */   }
/*      */ 
/*      */   public CompiledSpellChecker(CompiledNGramProcessLM lm, WeightedEditDistance editDistance, TokenizerFactory factory, Set<String> tokenSet, int nBestSize)
/*      */   {
/*  335 */     this(lm, editDistance, factory, tokenSet, nBestSize, -2.0D, -1.5D, -1.0D);
/*      */   }
/*      */ 
/*      */   public CompiledSpellChecker(CompiledNGramProcessLM lm, WeightedEditDistance editDistance, Set<String> tokenSet, int nBestSize)
/*      */   {
/*  360 */     this(lm, editDistance, null, tokenSet, nBestSize);
/*      */   }
/*      */ 
/*      */   public CompiledSpellChecker(CompiledNGramProcessLM lm, WeightedEditDistance editDistance, Set<String> tokenSet)
/*      */   {
/*  380 */     this(lm, editDistance, tokenSet, 64);
/*      */   }
/*      */ 
/*      */   public CompiledNGramProcessLM languageModel()
/*      */   {
/*  394 */     return this.mLM;
/*      */   }
/*      */ 
/*      */   public WeightedEditDistance editDistance()
/*      */   {
/*  404 */     return this.mEditDistance;
/*      */   }
/*      */ 
/*      */   public TokenizerFactory tokenizerFactory()
/*      */   {
/*  413 */     return this.mTokenizerFactory;
/*      */   }
/*      */ 
/*      */   public Set<String> tokenSet()
/*      */   {
/*  424 */     return Collections.unmodifiableSet(this.mTokenSet);
/*      */   }
/*      */ 
/*      */   public Set<String> doNotEditTokens()
/*      */   {
/*  435 */     return Collections.unmodifiableSet(this.mDoNotEditTokens);
/*      */   }
/*      */ 
/*      */   public void setDoNotEditTokens(Set<String> tokens)
/*      */   {
/*  446 */     this.mDoNotEditTokens = tokens;
/*      */   }
/*      */ 
/*      */   public int nBestSize()
/*      */   {
/*  458 */     return this.mNBestSize;
/*      */   }
/*      */ 
/*      */   public double knownTokenEditCost()
/*      */   {
/*  469 */     return this.mKnownTokenEditCost;
/*      */   }
/*      */ 
/*      */   public double firstCharEditCost()
/*      */   {
/*  484 */     return this.mFirstCharEditCost;
/*      */   }
/*      */ 
/*      */   public double secondCharEditCost()
/*      */   {
/*  495 */     return this.mSecondCharEditCost;
/*      */   }
/*      */ 
/*      */   public void setKnownTokenEditCost(double cost)
/*      */   {
/*  504 */     this.mKnownTokenEditCost = cost;
/*      */   }
/*      */ 
/*      */   public void setFirstCharEditCost(double cost)
/*      */   {
/*  513 */     this.mFirstCharEditCost = cost;
/*      */   }
/*      */ 
/*      */   public void setSecondCharEditCost(double cost)
/*      */   {
/*  522 */     this.mSecondCharEditCost = cost;
/*      */   }
/*      */ 
/*      */   public int numConsecutiveInsertionsAllowed()
/*      */   {
/*  530 */     return this.mNumConsecutiveInsertionsAllowed;
/*      */   }
/*      */ 
/*      */   public boolean allowInsert()
/*      */   {
/*  541 */     return this.mAllowInsert;
/*      */   }
/*      */ 
/*      */   public boolean allowDelete()
/*      */   {
/*  552 */     return this.mAllowDelete;
/*      */   }
/*      */ 
/*      */   public boolean allowMatch()
/*      */   {
/*  563 */     return this.mAllowMatch;
/*      */   }
/*      */ 
/*      */   public boolean allowSubstitute()
/*      */   {
/*  574 */     return this.mAllowSubstitute;
/*      */   }
/*      */ 
/*      */   public boolean allowTranspose()
/*      */   {
/*  585 */     return this.mAllowTranspose;
/*      */   }
/*      */ 
/*      */   public void setEditDistance(WeightedEditDistance editDistance)
/*      */   {
/*  596 */     this.mEditDistance = editDistance;
/*      */   }
/*      */ 
/*      */   public void setMinimumTokenLengthToCorrect(int tokenCharLength)
/*      */   {
/*  609 */     if (tokenCharLength < 0) {
/*  610 */       String msg = "Minimum token length to correct must be >= 0. Found tokenCharLength=" + tokenCharLength;
/*      */ 
/*  612 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  614 */     this.mMinimumTokenLengthToCorrect = tokenCharLength;
/*      */   }
/*      */ 
/*      */   public int minimumTokenLengthToCorrect()
/*      */   {
/*  625 */     return this.mMinimumTokenLengthToCorrect;
/*      */   }
/*      */ 
/*      */   public void setLanguageModel(CompiledNGramProcessLM lm)
/*      */   {
/*  637 */     this.mLM = lm;
/*      */   }
/*      */ 
/*      */   public void setTokenizerFactory(TokenizerFactory factory)
/*      */   {
/*  648 */     this.mTokenizerFactory = factory;
/*      */   }
/*      */ 
/*      */   public final void setTokenSet(Set<String> tokenSet)
/*      */   {
/*  665 */     if (tokenSet == null) return;
/*  666 */     int maxLen = 0;
/*  667 */     for (String token : tokenSet)
/*  668 */       maxLen = Math.max(maxLen, token.length());
/*  669 */     this.mTokenSet = tokenSet;
/*  670 */     this.mTokenPrefixTrie = (tokenSet == null ? null : prefixTrie(tokenSet));
/*      */   }
/*      */ 
/*      */   public void setNBest(int size)
/*      */   {
/*  683 */     if (size < 1) {
/*  684 */       String msg = "N-best size must be greather than 0. Found size=" + size;
/*      */ 
/*  686 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  688 */     this.mNBestSize = size;
/*      */   }
/*      */ 
/*      */   public void setAllowInsert(boolean allowInsert)
/*      */   {
/*  702 */     this.mAllowInsert = allowInsert;
/*  703 */     if (!allowInsert) setNumConsecutiveInsertionsAllowed(0);
/*      */   }
/*      */ 
/*      */   public void setAllowDelete(boolean allowDelete)
/*      */   {
/*  714 */     this.mAllowDelete = allowDelete;
/*      */   }
/*      */ 
/*      */   public void setAllowMatch(boolean allowMatch)
/*      */   {
/*  725 */     this.mAllowMatch = allowMatch;
/*      */   }
/*      */ 
/*      */   public void setAllowSubstitute(boolean allowSubstitute)
/*      */   {
/*  736 */     this.mAllowSubstitute = allowSubstitute;
/*      */   }
/*      */ 
/*      */   public void setAllowTranspose(boolean allowTranspose)
/*      */   {
/*  747 */     this.mAllowTranspose = allowTranspose;
/*      */   }
/*      */ 
/*      */   public void setNumConsecutiveInsertionsAllowed(int numAllowed)
/*      */   {
/*  762 */     if (numAllowed < 0) {
/*  763 */       String msg = "Num insertions allowed must be >= 0. Found numAllowed=" + numAllowed;
/*      */ 
/*  765 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  767 */     if (numAllowed > 0) setAllowInsert(true);
/*  768 */     this.mNumConsecutiveInsertionsAllowed = numAllowed;
/*      */   }
/*      */ 
/*      */   public String didYouMean(String receivedMsg)
/*      */   {
/*  783 */     String msg = normalizeQuery(receivedMsg);
/*  784 */     if (msg.length() == 0) return msg;
/*  785 */     DpSpellQueue queue = new DpSpellQueue(null);
/*  786 */     DpSpellQueue finalQueue = new DpSpellQueue(null);
/*  787 */     computeBestPaths(msg, queue, finalQueue);
/*  788 */     if (finalQueue.isEmpty())
/*  789 */       return msg;
/*  790 */     State bestState = (State)finalQueue.poll();
/*      */ 
/*  792 */     return bestState.output().trim();
/*      */   }
/*      */ 
/*      */   void computeBestPaths(String msg, StateQueue queue, StateQueue finalQueue)
/*      */   {
/*  797 */     double[] editPenalties = editPenalties(msg);
/*  798 */     State initialState = new State(0.0D, false, this.mTokenPrefixTrie, null, this.mLM.nextContext(0, ' '));
/*      */ 
/*  801 */     addToQueue(queue, initialState, editPenalties[0]);
/*  802 */     DpSpellQueue nextQ = new DpSpellQueue(null);
/*  803 */     DpSpellQueue nextQ2 = new DpSpellQueue(null);
/*  804 */     for (int i = 0; i < msg.length(); i++) {
/*  805 */       char c = msg.charAt(i);
/*  806 */       char nextC = i + 1 < msg.length() ? msg.charAt(i + 1) : '\000';
/*  807 */       for (State state : queue) {
/*  808 */         if (i + 1 < msg.length())
/*  809 */           extend2(c, nextC, state, nextQ, nextQ2, editPenalties[i]);
/*      */         else
/*  811 */           extend1(c, state, nextQ, editPenalties[i]);
/*      */       }
/*  813 */       queue = nextQ;
/*  814 */       nextQ = nextQ2;
/*  815 */       nextQ2 = new DpSpellQueue(null);
/*      */     }
/*  817 */     extendToFinalSpace(queue, finalQueue);
/*      */   }
/*      */ 
/*      */   public Iterator<ScoredObject<String>> didYouMeanNBest(String receivedMsg)
/*      */   {
/*  878 */     String msg = normalizeQuery(receivedMsg);
/*  879 */     if (msg.length() == 0)
/*  880 */       return Iterators.singleton(new ScoredObject("", 0.0D));
/*  881 */     StateQueue queue = new NBestSpellQueue(null);
/*  882 */     StateQueue finalQueue = new NBestSpellQueue(null);
/*  883 */     computeBestPaths(msg, queue, finalQueue);
/*  884 */     BoundedPriorityQueue resultQueue = new BoundedPriorityQueue(ScoredObject.comparator(), this.mNBestSize);
/*      */ 
/*  887 */     for (State state : finalQueue) {
/*  888 */       resultQueue.offer(new ScoredObject(state.output().trim(), state.score()));
/*      */     }
/*      */ 
/*  891 */     return resultQueue.iterator();
/*      */   }
/*      */ 
/*      */   private boolean isShortToken(String token) {
/*  895 */     return token.length() <= this.mMinimumTokenLengthToCorrect;
/*      */   }
/*      */ 
/*      */   private double[] editPenalties(String msg) {
/*  899 */     double[] penalties = new double[msg.length()];
/*  900 */     java.util.Arrays.fill(penalties, 0.0D);
/*      */ 
/*  902 */     if (this.mTokenSet == null) return penalties;
/*      */ 
/*  904 */     int charPosition = 0;
/*  905 */     for (int i = 0; i < penalties.length; i++) {
/*  906 */       char c = msg.charAt(i);
/*  907 */       if ((this.mTokenSet != null) && ((i == 0) || (msg.charAt(i - 1) == ' ')))
/*      */       {
/*  909 */         int endIndex = msg.indexOf(' ', i);
/*  910 */         if (endIndex == -1)
/*  911 */           endIndex = msg.length();
/*  912 */         String token = msg.substring(i, endIndex);
/*  913 */         if ((this.mDoNotEditTokens.contains(token)) || (isShortToken(token)))
/*      */         {
/*  916 */           if (i > 0) {
/*  917 */             penalties[(i - 1)] = (-1.0D / 0.0D);
/*      */           }
/*      */ 
/*  920 */           for (int j = i; j < endIndex; j++) {
/*  921 */             penalties[j] = (-1.0D / 0.0D);
/*      */           }
/*      */ 
/*  924 */           if (endIndex < penalties.length)
/*  925 */             penalties[endIndex] = (-1.0D / 0.0D);
/*      */         }
/*  927 */         else if (this.mTokenSet.contains(token)) {
/*  928 */           if (i > 0) {
/*  929 */             penalties[(i - 1)] += this.mKnownTokenEditCost;
/*      */           }
/*      */ 
/*  932 */           for (int j = i; j < endIndex; j++) {
/*  933 */             penalties[j] += this.mKnownTokenEditCost;
/*      */           }
/*      */ 
/*  936 */           if (endIndex < penalties.length) {
/*  937 */             penalties[endIndex] += this.mKnownTokenEditCost;
/*      */           }
/*      */         }
/*      */       }
/*  941 */       if (c == ' ') {
/*  942 */         charPosition = 0;
/*      */ 
/*  944 */         penalties[i] += this.mFirstCharEditCost;
/*  945 */       } else if (charPosition == 0) {
/*  946 */         penalties[i] += this.mFirstCharEditCost;
/*  947 */         charPosition++;
/*  948 */       } else if (charPosition == 1) {
/*  949 */         penalties[i] += this.mSecondCharEditCost;
/*  950 */         charPosition++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  956 */     return penalties;
/*      */   }
/*      */ 
/*      */   public String parametersToString()
/*      */   {
/*  967 */     StringBuilder sb = new StringBuilder();
/*      */ 
/*  969 */     sb.append("SEARCH");
/*  970 */     sb.append("\n  N-best size=" + this.mNBestSize);
/*      */ 
/*  972 */     sb.append("\n\nTOKEN SENSITIVITY");
/*  973 */     sb.append("\n  Token sensitive=" + (this.mTokenSet != null));
/*  974 */     if (this.mTokenSet != null) {
/*  975 */       sb.append("\n  # Known Tokens=" + this.mTokenSet.size());
/*      */     }
/*      */ 
/*  978 */     sb.append("\n\nEDITS ALLOWED");
/*  979 */     sb.append("\n  Allow insert=" + this.mAllowInsert);
/*  980 */     sb.append("\n  Allow delete=" + this.mAllowDelete);
/*  981 */     sb.append("\n  Allow match=" + this.mAllowMatch);
/*  982 */     sb.append("\n  Allow substitute=" + this.mAllowSubstitute);
/*  983 */     sb.append("\n  Allow transpose=" + this.mAllowTranspose);
/*  984 */     sb.append("\n  Num consecutive insertions allowed=" + this.mNumConsecutiveInsertionsAllowed);
/*      */ 
/*  986 */     sb.append("\n  Minimum Length Token Edit=" + this.mMinimumTokenLengthToCorrect);
/*      */ 
/*  988 */     sb.append("\n  # of do-not-Edit Tokens=" + this.mDoNotEditTokens.size());
/*      */ 
/*  991 */     sb.append("\n\nEDIT COSTS");
/*  992 */     sb.append("\n  Edit Distance=" + this.mEditDistance);
/*  993 */     sb.append("\n  Known Token Edit Cost=" + this.mKnownTokenEditCost);
/*  994 */     sb.append("\n  First Char Edit Cost=" + this.mFirstCharEditCost);
/*  995 */     sb.append("\n  Second Char Edit Cost=" + this.mSecondCharEditCost);
/*      */ 
/*  997 */     sb.append("\n\nEDIT DISTANCE\n");
/*  998 */     sb.append(this.mEditDistance);
/*      */ 
/* 1000 */     sb.append("\n\nTOKENIZER FACTORY\n");
/* 1001 */     sb.append(this.mTokenizerFactory);
/*      */ 
/* 1003 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   String normalizeQuery(String query) {
/* 1007 */     StringBuilder sb = new StringBuilder();
/* 1008 */     if (this.mTokenizerFactory == null) {
/* 1009 */       Strings.normalizeWhitespace(query, sb);
/*      */     } else {
/* 1011 */       char[] cs = query.toCharArray();
/* 1012 */       Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/*      */       String nextToken;
/* 1014 */       for (int i = 0; (nextToken = tokenizer.nextToken()) != null; i++) {
/* 1015 */         if (i > 0) sb.append(' ');
/* 1016 */         sb.append(nextToken);
/*      */       }
/*      */     }
/* 1019 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   char[] observedCharacters()
/*      */   {
/* 1024 */     return this.mLM.observedCharacters();
/*      */   }
/*      */ 
/*      */   void extendToFinalSpace(StateQueue queue, StateQueue finalQueue)
/*      */   {
/* 1029 */     for (State state : queue)
/* 1030 */       if ((!state.mTokenEdited) || (state.tokenComplete()))
/*      */       {
/* 1034 */         double nextScore = state.mScore + this.mLM.log2Estimate(state.mContextIndex, ' ');
/*      */ 
/* 1036 */         if (nextScore != (-1.0D / 0.0D))
/*      */         {
/* 1039 */           State nextState = new State(nextScore, false, null, state, -1);
/*      */ 
/* 1043 */           finalQueue.addState(nextState);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   void extend2(char c1, char c2, State state, DpSpellQueue nextQ, DpSpellQueue nextQ2, double positionalEditPenalty)
/*      */   {
/* 1050 */     extend1(c1, state, nextQ, positionalEditPenalty);
/* 1051 */     if (positionalEditPenalty == (-1.0D / 0.0D)) return;
/* 1052 */     if (allowTranspose())
/* 1053 */       transpose(c1, c2, state, nextQ2, positionalEditPenalty);
/*      */   }
/*      */ 
/*      */   void extend1(char c, State state, DpSpellQueue nextQ, double positionalEditPenalty)
/*      */   {
/* 1058 */     if (allowMatch())
/* 1059 */       match(c, state, nextQ, positionalEditPenalty);
/* 1060 */     if (positionalEditPenalty == (-1.0D / 0.0D)) return;
/* 1061 */     if (allowSubstitute())
/* 1062 */       substitute(c, state, nextQ, positionalEditPenalty);
/* 1063 */     if (allowDelete())
/* 1064 */       delete(c, state, nextQ, positionalEditPenalty);
/*      */   }
/*      */ 
/*      */   void addToQueue(StateQueue queue, State state, double positionalEditPenalty)
/*      */   {
/* 1070 */     addToQueue(queue, state, 0, positionalEditPenalty);
/*      */   }
/*      */ 
/*      */   void addToQueue(StateQueue queue, State state, int numInserts, double positionalEditPenalty)
/*      */   {
/* 1075 */     if (!queue.addState(state)) return;
/* 1076 */     if (numInserts >= this.mNumConsecutiveInsertionsAllowed) return;
/* 1077 */     if (positionalEditPenalty == (-1.0D / 0.0D)) return;
/* 1078 */     insert(state, queue, numInserts, positionalEditPenalty);
/*      */   }
/*      */ 
/*      */   TokenTrieNode daughter(TokenTrieNode node, char c)
/*      */   {
/* 1083 */     return node == null ? null : node.daughter(c);
/*      */   }
/*      */ 
/*      */   void match(char c, State state, DpSpellQueue nextQ, double positionalEditPenalty)
/*      */   {
/* 1088 */     if (state.mTokenEdited) {
/* 1089 */       if (c == ' ')
/*      */       {
/* 1090 */         if (state.tokenComplete());
/*      */       }
/* 1093 */       else if (!state.continuedBy(c)) {
/* 1094 */         return;
/*      */       }
/*      */     }
/* 1097 */     double score = state.mScore + this.mLM.log2Estimate(state.mContextIndex, c) + this.mEditDistance.matchWeight(c);
/*      */ 
/* 1100 */     if (score == (-1.0D / 0.0D)) return;
/* 1101 */     TokenTrieNode tokenTrieNode = c == ' ' ? this.mTokenPrefixTrie : daughter(state.mTokenTrieNode, c);
/*      */ 
/* 1103 */     addToQueue(nextQ, new State1(score, (c != ' ') && (state.mTokenEdited), tokenTrieNode, state, c, this.mLM.nextContext(state.mContextIndex, c)), positionalEditPenalty);
/*      */   }
/*      */ 
/*      */   void delete(char c, State state, DpSpellQueue nextQ, double positionalEditPenalty)
/*      */   {
/* 1114 */     double deleteWeight = this.mEditDistance.deleteWeight(c);
/* 1115 */     if (deleteWeight == (-1.0D / 0.0D)) return;
/* 1116 */     double score = state.mScore + deleteWeight + positionalEditPenalty;
/* 1117 */     addToQueue(nextQ, new State(score, true, state.mTokenTrieNode, state, state.mContextIndex), positionalEditPenalty);
/*      */   }
/*      */ 
/*      */   void insert(State state, StateQueue nextQ, int numInserts, double positionalEditPenalty)
/*      */   {
/* 1127 */     if (state.tokenComplete()) {
/* 1128 */       double score = state.mScore + this.mLM.log2Estimate(state.mContextIndex, ' ') + this.mEditDistance.insertWeight(' ') + positionalEditPenalty;
/*      */ 
/* 1132 */       if (score != (-1.0D / 0.0D)) {
/* 1133 */         addToQueue(nextQ, new State1(score, true, this.mTokenPrefixTrie, state, ' ', this.mLM.nextContext(state.mContextIndex, ' ')), numInserts + 1, positionalEditPenalty);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1141 */     char[] followers = state.getContinuations();
/* 1142 */     if (followers == null) return;
/* 1143 */     for (int i = 0; i < followers.length; i++) {
/* 1144 */       char c = followers[i];
/* 1145 */       double insertWeight = this.mEditDistance.insertWeight(c);
/* 1146 */       if (insertWeight != (-1.0D / 0.0D)) {
/* 1147 */         double score = state.mScore + this.mLM.log2Estimate(state.mContextIndex, c) + insertWeight + positionalEditPenalty;
/*      */ 
/* 1151 */         if (score != (-1.0D / 0.0D))
/* 1152 */           addToQueue(nextQ, new State1(score, true, state.followingNode(i), state, c, this.mLM.nextContext(state.mContextIndex, c)), numInserts + 1, positionalEditPenalty);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void substitute(char c, State state, StateQueue nextQ, double positionalEditPenalty)
/*      */   {
/* 1163 */     if ((state.tokenComplete()) && (c != ' ')) {
/* 1164 */       double score = state.mScore + this.mLM.log2Estimate(state.mContextIndex, ' ') + this.mEditDistance.substituteWeight(c, ' ') + positionalEditPenalty;
/*      */ 
/* 1168 */       if (score != (-1.0D / 0.0D)) {
/* 1169 */         addToQueue(nextQ, new State1(score, true, this.mTokenPrefixTrie, state, ' ', this.mLM.nextContext(state.mContextIndex, ' ')), positionalEditPenalty);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1177 */     char[] followers = state.getContinuations();
/* 1178 */     if (followers == null) return;
/* 1179 */     for (int i = 0; i < followers.length; i++) {
/* 1180 */       char c2 = followers[i];
/* 1181 */       if (c != c2) {
/* 1182 */         double substWeight = this.mEditDistance.substituteWeight(c, c2);
/*      */ 
/* 1184 */         if (substWeight != (-1.0D / 0.0D)) {
/* 1185 */           double score = state.mScore + this.mLM.log2Estimate(state.mContextIndex, c2) + substWeight + positionalEditPenalty;
/*      */ 
/* 1189 */           if (score != (-1.0D / 0.0D))
/* 1190 */             addToQueue(nextQ, new State1(score, true, state.followingNode(i), state, c2, this.mLM.nextContext(state.mContextIndex, c2)), positionalEditPenalty);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void transpose(char c1, char c2, State state, StateQueue nextQ, double positionalEditPenalty)
/*      */   {
/* 1202 */     double transposeWeight = this.mEditDistance.transposeWeight(c1, c2);
/* 1203 */     if (transposeWeight == (-1.0D / 0.0D)) return;
/* 1204 */     if ((c2 == ' ') && (!state.tokenComplete())) return;
/* 1205 */     TokenTrieNode midNode = c2 == ' ' ? this.mTokenPrefixTrie : daughter(state.mTokenTrieNode, c2);
/*      */ 
/* 1209 */     if ((c1 == ' ') && (midNode != null) && (!midNode.mIsToken)) return;
/* 1210 */     int nextContextIndex = this.mLM.nextContext(state.mContextIndex, c2);
/* 1211 */     int nextContextIndex2 = this.mLM.nextContext(nextContextIndex, c1);
/* 1212 */     double score = state.mScore + this.mLM.log2Estimate(state.mContextIndex, c2) + this.mLM.log2Estimate(nextContextIndex, c1) + this.mEditDistance.transposeWeight(c1, c2) + positionalEditPenalty;
/*      */ 
/* 1217 */     if (score == (-1.0D / 0.0D)) return;
/* 1218 */     TokenTrieNode nextNode = c1 == ' ' ? this.mTokenPrefixTrie : daughter(midNode, c1);
/*      */ 
/* 1222 */     addToQueue(nextQ, new State2(score, true, nextNode, state, c2, c1, nextContextIndex2), positionalEditPenalty);
/*      */   }
/*      */ 
/*      */   private static Map<String, char[]> prefixToContinuations(Set<String> tokens)
/*      */   {
/* 1280 */     Map prefixToContinuations = new HashMap();
/*      */ 
/* 1282 */     for (String token : tokens) {
/* 1283 */       for (int i = 0; i < token.length(); i++) {
/* 1284 */         String prefix = token.substring(0, i);
/* 1285 */         char nextChar = token.charAt(i);
/* 1286 */         char[] currentCs = (char[])prefixToContinuations.get(prefix);
/* 1287 */         if (currentCs == null) {
/* 1288 */           prefixToContinuations.put(prefix, new char[] { nextChar });
/*      */         } else {
/* 1290 */           char[] nextCs = com.aliasi.util.Arrays.add(nextChar, currentCs);
/* 1291 */           if (nextCs.length > currentCs.length)
/* 1292 */             prefixToContinuations.put(prefix, nextCs);
/*      */         }
/*      */       }
/*      */     }
/* 1296 */     return prefixToContinuations;
/*      */   }
/*      */ 
/*      */   private TokenTrieNode prefixTrie(Set<String> tokens) {
/* 1300 */     Map prefixMap = prefixToContinuations(tokens);
/* 1301 */     return completeTrieNode("", tokens, prefixMap);
/*      */   }
/*      */ 
/*      */   private static TokenTrieNode completeTrieNode(String prefix, Set<String> tokens, Map<String, char[]> prefixMap)
/*      */   {
/* 1310 */     char[] contChars = (char[])prefixMap.get(prefix);
/* 1311 */     if (contChars == null)
/* 1312 */       contChars = Strings.EMPTY_CHAR_ARRAY;
/*      */     else {
/* 1314 */       java.util.Arrays.sort(contChars);
/*      */     }
/* 1316 */     TokenTrieNode[] contNodes = new TokenTrieNode[contChars.length];
/* 1317 */     for (int i = 0; i < contNodes.length; i++) {
/* 1318 */       contNodes[i] = completeTrieNode(prefix + contChars[i], tokens, prefixMap);
/*      */     }
/* 1320 */     return new TokenTrieNode(tokens.contains(prefix), contChars, contNodes);
/*      */   }
/*      */ 
/*      */   private static final class Tokenizing extends FixedWeightEditDistance
/*      */     implements Compilable
/*      */   {
/*      */     static final long serialVersionUID = 514970533483080541L;
/*      */ 
/*      */     public Tokenizing()
/*      */     {
/* 1539 */       super((-1.0D / 0.0D), (-1.0D / 0.0D), (-1.0D / 0.0D), (-1.0D / 0.0D));
/*      */     }
/*      */ 
/*      */     public double insertWeight(char cInserted)
/*      */     {
/* 1547 */       return cInserted == ' ' ? 0.0D : (-1.0D / 0.0D);
/*      */     }
/*      */ 
/*      */     public void compileTo(ObjectOutput objOut) throws IOException {
/* 1551 */       objOut.writeObject(new Externalizable());
/*      */     }
/*      */ 
/*      */     private static class Externalizable extends AbstractExternalizable
/*      */     {
/*      */       private static final long serialVersionUID = -3015819851142009998L;
/*      */ 
/*      */       public void writeExternal(ObjectOutput objOut)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput objIn)
/*      */       {
/* 1564 */         return CompiledSpellChecker.TOKENIZING;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class CaseRestoring extends FixedWeightEditDistance
/*      */     implements Compilable
/*      */   {
/*      */     static final long serialVersionUID = -4504141535738468405L;
/*      */ 
/*      */     public CaseRestoring()
/*      */     {
/* 1499 */       super((-1.0D / 0.0D), (-1.0D / 0.0D), (-1.0D / 0.0D), (-1.0D / 0.0D));
/*      */     }
/*      */ 
/*      */     public double substituteWeight(char cDeleted, char cInserted)
/*      */     {
/* 1507 */       return Character.toLowerCase(cDeleted) == Character.toLowerCase(cInserted) ? 0.0D : (-1.0D / 0.0D);
/*      */     }
/*      */ 
/*      */     public void compileTo(ObjectOutput objOut)
/*      */       throws IOException
/*      */     {
/* 1514 */       objOut.writeObject(new Externalizable());
/*      */     }
/*      */ 
/*      */     private static class Externalizable extends AbstractExternalizable
/*      */     {
/*      */       private static final long serialVersionUID = 2825384056772387737L;
/*      */ 
/*      */       public void writeExternal(ObjectOutput objOut)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput objIn)
/*      */       {
/* 1527 */         return CompiledSpellChecker.CASE_RESTORING;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private abstract class StateQueue extends BoundedPriorityQueue<CompiledSpellChecker.State>
/*      */   {
/*      */     StateQueue()
/*      */     {
/* 1486 */       super(CompiledSpellChecker.this.mNBestSize);
/*      */     }
/*      */ 
/*      */     abstract boolean addState(CompiledSpellChecker.State paramState);
/*      */   }
/*      */ 
/*      */   private final class NBestSpellQueue extends CompiledSpellChecker.StateQueue
/*      */   {
/*      */     private NBestSpellQueue()
/*      */     {
/* 1476 */       super();
/*      */     }
/*      */ 
/*      */     public boolean addState(CompiledSpellChecker.State state) {
/* 1480 */       return offer(state);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class DpSpellQueue extends CompiledSpellChecker.StateQueue
/*      */   {
/* 1458 */     private final Map<Integer, CompiledSpellChecker.State> mStateToBest = new HashMap();
/*      */ 
/*      */     private DpSpellQueue()
/*      */     {
/* 1457 */       super();
/*      */     }
/*      */ 
/*      */     public boolean addState(CompiledSpellChecker.State state)
/*      */     {
/* 1462 */       Integer dp = Integer.valueOf(state.mContextIndex);
/* 1463 */       CompiledSpellChecker.State bestState = (CompiledSpellChecker.State)this.mStateToBest.get(dp);
/* 1464 */       if (bestState == null) {
/* 1465 */         this.mStateToBest.put(dp, state);
/* 1466 */         return offer(state);
/*      */       }
/* 1468 */       if (bestState.mScore >= state.mScore)
/* 1469 */         return false;
/* 1470 */       remove(bestState);
/* 1471 */       this.mStateToBest.put(dp, state);
/* 1472 */       return offer(state);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class State
/*      */     implements Scored
/*      */   {
/*      */     final CompiledSpellChecker.TokenTrieNode mTokenTrieNode;
/*      */     final double mScore;
/*      */     final boolean mTokenEdited;
/*      */     final State mPreviousState;
/*      */     final int mContextIndex;
/*      */ 
/*      */     State(double score, boolean tokenEdited, CompiledSpellChecker.TokenTrieNode tokenTrieNode, State previousState, int contextIndex)
/*      */     {
/* 1405 */       this.mScore = score;
/* 1406 */       this.mTokenEdited = tokenEdited;
/* 1407 */       this.mTokenTrieNode = tokenTrieNode;
/* 1408 */       this.mPreviousState = previousState;
/* 1409 */       this.mContextIndex = contextIndex;
/*      */     }
/*      */     public double score() {
/* 1412 */       return this.mScore;
/*      */     }
/*      */     CompiledSpellChecker.TokenTrieNode followingNode(int i) {
/* 1415 */       return this.mTokenTrieNode == null ? null : this.mTokenTrieNode.mFollowingNodes[i];
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1421 */       return output() + "/" + this.mTokenEdited + "/" + this.mScore;
/*      */     }
/*      */     boolean tokenComplete() {
/* 1424 */       boolean result = (CompiledSpellChecker.this.mTokenSet == null) || ((this.mTokenTrieNode != null) && (this.mTokenTrieNode.mIsToken));
/*      */ 
/* 1426 */       return result;
/*      */     }
/*      */     boolean continuedBy(char c) {
/* 1429 */       if (this.mTokenTrieNode == null) return true;
/* 1430 */       char[] continuations = getContinuations();
/* 1431 */       return (continuations != null) && (java.util.Arrays.binarySearch(continuations, c) >= 0);
/*      */     }
/*      */ 
/*      */     char[] getContinuations() {
/* 1435 */       return this.mTokenTrieNode == null ? CompiledSpellChecker.this.observedCharacters() : this.mTokenTrieNode.mFollowingChars;
/*      */     }
/*      */ 
/*      */     void outputLocal(StringBuilder ignoreMeSb)
/*      */     {
/*      */     }
/*      */ 
/*      */     String output() {
/* 1443 */       StringBuilder sb = new StringBuilder();
/* 1444 */       for (State s = this; s != null; s = s.mPreviousState) {
/* 1445 */         s.outputLocal(sb);
/*      */       }
/* 1447 */       int len = sb.length();
/* 1448 */       char[] cs = new char[len];
/* 1449 */       for (int i = 0; i < len; i++)
/* 1450 */         cs[i] = sb.charAt(len - i - 1);
/* 1451 */       return new String(cs);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class State2 extends CompiledSpellChecker.State
/*      */   {
/*      */     final char mChar1;
/*      */     final char mChar2;
/*      */ 
/*      */     State2(double score, boolean tokenEdited, CompiledSpellChecker.TokenTrieNode tokenTrieNode, CompiledSpellChecker.State previousState, char char1, char char2, int contextIndex)
/*      */     {
/* 1383 */       super(score, tokenEdited, tokenTrieNode, previousState, contextIndex);
/*      */ 
/* 1385 */       this.mChar1 = char1;
/* 1386 */       this.mChar2 = char2;
/*      */     }
/*      */ 
/*      */     void outputLocal(StringBuilder sb) {
/* 1390 */       sb.append(this.mChar2);
/* 1391 */       sb.append(this.mChar1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class State1 extends CompiledSpellChecker.State
/*      */   {
/*      */     final char mChar1;
/*      */ 
/*      */     State1(double score, boolean tokenEdited, CompiledSpellChecker.TokenTrieNode tokenTrieNode, CompiledSpellChecker.State previousState, char char1, int contextIndex)
/*      */     {
/* 1365 */       super(score, tokenEdited, tokenTrieNode, previousState, contextIndex);
/*      */ 
/* 1367 */       this.mChar1 = char1;
/*      */     }
/*      */ 
/*      */     void outputLocal(StringBuilder sb) {
/* 1371 */       sb.append(this.mChar1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class TokenTrieNode
/*      */   {
/*      */     final boolean mIsToken;
/*      */     final char[] mFollowingChars;
/*      */     final TokenTrieNode[] mFollowingNodes;
/*      */ 
/*      */     TokenTrieNode(boolean isToken, char[] followingChars, TokenTrieNode[] followingNodes)
/*      */     {
/* 1330 */       this.mIsToken = isToken;
/* 1331 */       this.mFollowingChars = followingChars;
/* 1332 */       this.mFollowingNodes = followingNodes;
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1336 */       StringBuilder sb = new StringBuilder();
/* 1337 */       toString("", sb, 0);
/* 1338 */       return sb.toString();
/*      */     }
/*      */     void toString(String prefix, StringBuilder sb, int indent) {
/* 1341 */       if (this.mIsToken) sb.append(" [token=" + prefix + "]");
/* 1342 */       sb.append('\n');
/* 1343 */       for (int i = 0; i < this.mFollowingChars.length; i++) {
/* 1344 */         for (int k = 0; k < indent; k++) sb.append("  ");
/* 1345 */         sb.append(this.mFollowingChars[i]);
/* 1346 */         this.mFollowingNodes[i].toString(prefix + this.mFollowingChars[i], sb, indent + 1);
/*      */       }
/*      */     }
/*      */ 
/*      */     TokenTrieNode daughter(char c) {
/* 1351 */       int index = java.util.Arrays.binarySearch(this.mFollowingChars, c);
/* 1352 */       return index < 0 ? null : this.mFollowingNodes[index];
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.CompiledSpellChecker
 * JD-Core Version:    0.6.2
 */