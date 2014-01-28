/*     */ package com.aliasi.tag;
/*     */ 
/*     */ import com.aliasi.classify.ConditionalClassification;
/*     */ import com.aliasi.classify.ConditionalClassifierEvaluator;
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class MarginalTaggerEvaluator<E>
/*     */   implements ObjectHandler<Tagging<E>>
/*     */ {
/*     */   private MarginalTagger<E> mTagger;
/*     */   private final Set<String> mTagSet;
/*     */   private final boolean mStoreTokens;
/*     */   private final ConditionalClassifierEvaluator<E> mEval;
/*     */   private Tagging<E> mLastReferenceTagging;
/*     */ 
/*     */   public MarginalTaggerEvaluator(MarginalTagger<E> tagger, Set<String> tagSet, boolean storeTokens)
/*     */   {
/*  90 */     this.mTagger = tagger;
/*  91 */     this.mTagSet = new HashSet(tagSet);
/*  92 */     this.mStoreTokens = storeTokens;
/*  93 */     String[] tags = (String[])tagSet.toArray(Strings.EMPTY_STRING_ARRAY);
/*  94 */     this.mEval = new ConditionalClassifierEvaluator(null, tags, storeTokens);
/*     */   }
/*     */ 
/*     */   public MarginalTagger<E> tagger()
/*     */   {
/* 104 */     return this.mTagger;
/*     */   }
/*     */ 
/*     */   public void setTagger(MarginalTagger<E> tagger)
/*     */   {
/* 114 */     this.mTagger = tagger;
/*     */   }
/*     */ 
/*     */   public boolean storeTokens()
/*     */   {
/* 124 */     return this.mStoreTokens;
/*     */   }
/*     */ 
/*     */   public Set<String> tagSet()
/*     */   {
/* 134 */     return Collections.unmodifiableSet(this.mTagSet);
/*     */   }
/*     */ 
/*     */   public void addCase(Tagging<E> referenceTagging, TagLattice<E> responseLattice)
/*     */   {
/* 149 */     this.mLastReferenceTagging = referenceTagging;
/* 150 */     if (referenceTagging.size() != responseLattice.numTokens()) {
/* 151 */       String msg = "Reference and response must have the same number of tokens. Found referenceTagging.size()=" + referenceTagging.size() + " responseLattice.numTokens()=" + responseLattice.numTokens();
/*     */ 
/* 154 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 156 */     for (int i = 0; i < referenceTagging.size(); i++) {
/* 157 */       if (!referenceTagging.token(i).equals(responseLattice.token(i))) {
/* 158 */         String msg = "Reference and response token lists must be the same. referenceTagging.token(" + i + ")=|" + referenceTagging.token(i) + "|" + " responseLattice.token(" + i + ")=|" + responseLattice.token(i) + "|";
/*     */ 
/* 161 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 164 */     for (String tag : referenceTagging.tags()) {
/* 165 */       if (!this.mTagSet.contains(tag)) {
/* 166 */         String msg = "Unknown tag in reference tagging. Unknown tag=" + tag + " reference tagging=" + referenceTagging;
/*     */ 
/* 169 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 172 */     List responseTags = responseLattice.tagList();
/* 173 */     for (String tag : responseTags) {
/* 174 */       if (!this.mTagSet.contains(tag)) {
/* 175 */         String msg = "Unknown tag in output lattice. Tag=" + tag + " referenceTagging=" + referenceTagging;
/*     */ 
/* 178 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 181 */     List tokens = referenceTagging.tokens();
/* 182 */     for (int n = 0; n < tokens.size(); n++) {
/* 183 */       Object token = referenceTagging.token(n);
/* 184 */       List tags = responseLattice.tagList();
/* 185 */       String referenceTag = referenceTagging.tag(n);
/* 186 */       ObjectToDoubleMap tagToScore = new ObjectToDoubleMap();
/* 187 */       for (int i = 0; i < tags.size(); i++) {
/* 188 */         tagToScore.set(responseTags.get(i), Math.exp(responseLattice.logProbability(n, i)));
/*     */       }
/*     */ 
/* 191 */       List responseTagsList = tagToScore.keysOrderedByValueList();
/* 192 */       String[] responseTagArray = (String[])responseTags.toArray(Strings.EMPTY_STRING_ARRAY);
/* 193 */       double[] responseProbs = new double[responseTags.size()];
/* 194 */       for (int i = 0; i < responseTagArray.length; i++)
/* 195 */         responseProbs[i] = tagToScore.getValue(responseTagArray[i]);
/* 196 */       ConditionalClassification responseClassification = ConditionalClassification.createProbs(responseTagArray, responseProbs);
/*     */ 
/* 198 */       this.mEval.addClassification(referenceTag, responseClassification, token);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handle(Tagging<E> referenceTagging)
/*     */   {
/* 210 */     List tokens = referenceTagging.tokens();
/* 211 */     TagLattice responseLattice = this.mTagger.tagMarginal(tokens);
/* 212 */     addCase(referenceTagging, responseLattice);
/*     */   }
/*     */ 
/*     */   public String lastCaseToString(int maxTagsPerToken) {
/* 216 */     if (this.mLastReferenceTagging == null)
/* 217 */       return "No cases seen yet.";
/* 218 */     List tokenList = this.mLastReferenceTagging.tokens();
/* 219 */     TagLattice lattice = this.mTagger.tagMarginal(tokenList);
/* 220 */     StringBuilder sb = new StringBuilder();
/* 221 */     sb.append("Index Token  RefTag  (Prob:ResponseTag)*\n");
/* 222 */     for (int tokenIndex = 0; tokenIndex < tokenList.size(); tokenIndex++) {
/* 223 */       ConditionalClassification tagScores = lattice.tokenClassification(tokenIndex);
/* 224 */       sb.append(TaggerEvaluator.pad(Integer.toString(tokenIndex), 4));
/* 225 */       sb.append(TaggerEvaluator.pad(tokenList.get(tokenIndex), 15));
/* 226 */       String refTag = this.mLastReferenceTagging.tag(tokenIndex);
/* 227 */       sb.append(TaggerEvaluator.pad(refTag, 6));
/* 228 */       sb.append("  ");
/* 229 */       for (int i = 0; i < maxTagsPerToken; i++) {
/* 230 */         double conditionalProb = tagScores.score(i);
/* 231 */         String tag = tagScores.category(i);
/* 232 */         sb.append(" " + NBestTaggerEvaluator.format(conditionalProb) + ":" + TaggerEvaluator.pad(tag, 4));
/*     */ 
/* 234 */         sb.append(tag.equals(refTag) ? "* " : "  ");
/*     */       }
/* 236 */       sb.append("\n");
/*     */     }
/* 238 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public ConditionalClassifierEvaluator<E> perTokenEval()
/*     */   {
/* 247 */     return this.mEval;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.MarginalTaggerEvaluator
 * JD-Core Version:    0.6.2
 */