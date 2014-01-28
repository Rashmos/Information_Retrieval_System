/*     */ package com.aliasi.tag;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import java.util.Formatter;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class NBestTaggerEvaluator<E>
/*     */   implements ObjectHandler<Tagging<E>>
/*     */ {
/*     */   private final int mMaxNBest;
/*     */   private final int mMaxNBestToString;
/*     */   private NBestTagger<E> mTagger;
/*  81 */   private final ObjectToCounterMap<Integer> mNBestHistogram = new ObjectToCounterMap();
/*     */ 
/*  83 */   private int mNumCases = 0;
/*  84 */   private long mNumTokens = 0L;
/*     */   private int mLastCaseRank;
/*     */   private Tagging<E> mLastCase;
/*     */ 
/*     */   public NBestTaggerEvaluator(NBestTagger<E> tagger, int maxNBest, int maxNBestToString)
/*     */   {
/* 106 */     this.mTagger = tagger;
/* 107 */     this.mMaxNBest = maxNBest;
/* 108 */     this.mMaxNBestToString = maxNBestToString;
/*     */   }
/*     */ 
/*     */   public int maxNBest()
/*     */   {
/* 118 */     return this.mMaxNBest;
/*     */   }
/*     */ 
/*     */   public void setTagger(NBestTagger<E> tagger)
/*     */   {
/* 127 */     this.mTagger = tagger;
/*     */   }
/*     */ 
/*     */   public NBestTagger<E> tagger()
/*     */   {
/* 137 */     return this.mTagger;
/*     */   }
/*     */ 
/*     */   public void handle(Tagging<E> referenceTagging)
/*     */   {
/* 148 */     this.mLastCase = referenceTagging;
/* 149 */     Iterator it = this.mTagger.tagNBest(referenceTagging.tokens(), this.mMaxNBest);
/*     */ 
/* 151 */     addCase(referenceTagging, it);
/*     */   }
/*     */ 
/*     */   public void addCase(Tagging<E> referenceTagging, Iterator<ScoredTagging<E>> responseTaggingIterator)
/*     */   {
/* 164 */     this.mNumCases += 1;
/* 165 */     this.mNumTokens += referenceTagging.size();
/* 166 */     this.mLastCase = referenceTagging;
/* 167 */     List expectedTags = referenceTagging.tags();
/* 168 */     for (int i = 0; (i < this.mMaxNBest) && (responseTaggingIterator.hasNext()); i++) {
/* 169 */       Tagging tagging = (Tagging)responseTaggingIterator.next();
/* 170 */       if (expectedTags.equals(tagging.tags())) {
/* 171 */         this.mNBestHistogram.increment(Integer.valueOf(i));
/* 172 */         this.mLastCaseRank = i;
/* 173 */         return;
/*     */       }
/*     */     }
/* 176 */     this.mLastCaseRank = -1;
/* 177 */     this.mNBestHistogram.increment(Integer.valueOf(-1));
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap<Integer> nBestHistogram()
/*     */   {
/* 187 */     return this.mNBestHistogram;
/*     */   }
/*     */ 
/*     */   public double[] recallAtN()
/*     */   {
/* 199 */     double[] result = new double[this.mMaxNBest];
/* 200 */     double maxNBest = this.mMaxNBest;
/* 201 */     int sum = 0;
/* 202 */     for (int i = 0; i < this.mMaxNBest; i++) {
/* 203 */       sum += this.mNBestHistogram.getCount(Integer.valueOf(i));
/* 204 */       result[i] = (sum / maxNBest);
/*     */     }
/* 206 */     return result;
/*     */   }
/*     */ 
/*     */   public int numCases()
/*     */   {
/* 215 */     return this.mNumCases;
/*     */   }
/*     */ 
/*     */   public long numTokens()
/*     */   {
/* 225 */     return this.mNumTokens;
/*     */   }
/*     */ 
/*     */   public String lastCaseToString(int maxNBestReport)
/*     */   {
/* 238 */     int max = Math.min(maxNBestReport, this.mMaxNBest);
/* 239 */     if (numCases() == 0)
/* 240 */       return "No cases seen yet.";
/* 241 */     StringBuilder sb = new StringBuilder();
/* 242 */     Formatter formatter = new Formatter(sb, Locale.US);
/* 243 */     sb.append("Last case n-best reference rank=" + this.mLastCaseRank + "\n");
/* 244 */     sb.append("Last case " + max + "-best:\n");
/* 245 */     sb.append("Correct,Rank,LogJointProb,Tags\n");
/* 246 */     List tokenList = this.mLastCase.tokens();
/* 247 */     Iterator nBestIt = this.mTagger.tagNBest(tokenList, max);
/* 248 */     for (int n = 0; (n < max) && (nBestIt.hasNext()); n++) {
/* 249 */       sb.append(n == this.mLastCaseRank ? " *** " : "     ");
/* 250 */       ScoredTagging scoredTagging = (ScoredTagging)nBestIt.next();
/* 251 */       double score = scoredTagging.score();
/* 252 */       sb.append(n + "   " + format(score) + "  ");
/* 253 */       for (int i = 0; i < tokenList.size(); i++)
/* 254 */         sb.append(scoredTagging.token(i) + "_" + TaggerEvaluator.pad(scoredTagging.tag(i), 5));
/* 255 */       sb.append("\n");
/*     */     }
/* 257 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static String format(double x) {
/* 261 */     return String.format("%9.3f", new Object[] { Double.valueOf(x) });
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.NBestTaggerEvaluator
 * JD-Core Version:    0.6.2
 */