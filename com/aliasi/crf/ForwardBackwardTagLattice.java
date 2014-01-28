/*     */ package com.aliasi.crf;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.symbol.SymbolTableCompiler;
/*     */ import com.aliasi.tag.TagLattice;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ForwardBackwardTagLattice<E> extends TagLattice<E>
/*     */ {
/*     */   private final List<E> mTokens;
/*     */   private final List<String> mTags;
/*     */   private final double[][] mLogForwards;
/*     */   private final double[][] mLogBackwards;
/*     */   private final double[][][] mLogTransitions;
/*     */   private final double mLogZ;
/*     */ 
/*     */   public ForwardBackwardTagLattice(List<E> tokens, List<String> tags, double[][] logForwards, double[][] logBackwards, double[][][] logTransitions, double logZ)
/*     */   {
/* 114 */     this(new ArrayList(tokens), new ArrayList(tags), logForwards, logBackwards, logTransitions, logZ, true);
/*     */ 
/* 119 */     int N = tokens.size();
/* 120 */     int K = tags.size();
/* 121 */     if (logForwards.length != N) {
/* 122 */       String msg = "Log forwards must be length of input. tokens.size()=" + N + " logForwards.length=" + logForwards.length;
/*     */ 
/* 125 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 127 */     if (logBackwards.length != N) {
/* 128 */       String msg = "Log backwards must be length of input. tokens.size()=" + N + " logBackwards.length=" + logBackwards.length;
/*     */ 
/* 131 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 133 */     if ((N > 0) && (logTransitions.length != N - 1)) {
/* 134 */       String msg = "Log transitions length must be one shorter than input, or empty. Found tokens.size()=" + N + " logTransitions.length=" + logTransitions.length;
/*     */ 
/* 137 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 139 */     for (int n = 0; n < N; n++) {
/* 140 */       if (logForwards[n].length != K) {
/* 141 */         String msg = "Each log forward must be length of tags. Found tags.size()=" + K + " logForwards[" + n + "]=" + logForwards[n];
/*     */ 
/* 144 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 146 */       if (logBackwards[n].length != K) {
/* 147 */         String msg = "Each log backward must be length of tags. Found tags.size()=" + K + " logBackwards[" + n + "]=" + logBackwards[n];
/*     */ 
/* 150 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 153 */     for (int n = 1; n < N; n++) {
/* 154 */       if (logTransitions[(n - 1)].length != K) {
/* 155 */         String msg = "Each transition source must be length of tags. Found tags.size()=" + tags.size() + " logTransitions[" + (n - 1) + "].length=" + logTransitions[(n - 1)].length;
/*     */ 
/* 158 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 160 */       for (int k = 0; k < K; k++)
/* 161 */         if (logTransitions[(n - 1)][k].length != K) {
/* 162 */           String msg = "Each transition target must be length of tags. Found tags.size()=" + tags.size() + " logTransitions[" + (n - 1) + "][" + k + "].length=" + logTransitions[(n - 1)][k].length;
/*     */ 
/* 165 */           throw new IllegalArgumentException(msg);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   ForwardBackwardTagLattice(List<E> tokens, List<String> tags, double[][] logForwards, double[][] logBackwards, double[][][] logTransitions, double logZ, boolean ignore)
/*     */   {
/* 179 */     this.mTokens = tokens;
/* 180 */     this.mTags = tags;
/* 181 */     this.mLogForwards = logForwards;
/* 182 */     this.mLogBackwards = logBackwards;
/* 183 */     this.mLogTransitions = logTransitions;
/* 184 */     this.mLogZ = logZ;
/*     */   }
/*     */ 
/*     */   public List<E> tokenList()
/*     */   {
/* 189 */     return Collections.unmodifiableList(this.mTokens);
/*     */   }
/*     */ 
/*     */   public List<String> tagList() {
/* 193 */     return Collections.unmodifiableList(this.mTags);
/*     */   }
/*     */ 
/*     */   public String tag(int id) {
/* 197 */     return (String)this.mTags.get(id);
/*     */   }
/*     */ 
/*     */   public int numTags() {
/* 201 */     return this.mTags.size();
/*     */   }
/*     */ 
/*     */   public E token(int n) {
/* 205 */     return this.mTokens.get(n);
/*     */   }
/*     */ 
/*     */   public int numTokens() {
/* 209 */     return this.mTokens.size();
/*     */   }
/*     */ 
/*     */   public SymbolTable tagSymbolTable() {
/* 213 */     return SymbolTableCompiler.asSymbolTable((String[])this.mTags.toArray(Strings.EMPTY_STRING_ARRAY));
/*     */   }
/*     */ 
/*     */   public double logProbability(int token, int tag)
/*     */   {
/* 228 */     return this.mLogForwards[token][tag] + this.mLogBackwards[token][tag] - this.mLogZ;
/*     */   }
/*     */ 
/*     */   public double logProbability(int tokenTo, int tagFrom, int tagTo)
/*     */   {
/* 235 */     double logProb = this.mLogForwards[(tokenTo - 1)][tagFrom] + this.mLogBackwards[tokenTo][tagTo] + this.mLogTransitions[(tokenTo - 1)][tagFrom][tagTo] - this.mLogZ;
/*     */ 
/* 239 */     return logProb;
/*     */   }
/*     */ 
/*     */   public double logProbability(int tokenFrom, int[] tags)
/*     */   {
/* 256 */     int startTag = tags[0];
/* 257 */     int endTag = tags[(tags.length - 1)];
/* 258 */     int tokenTo = tokenFrom + tags.length - 1;
/* 259 */     double logProb = this.mLogForwards[tokenFrom][startTag] + this.mLogBackwards[tokenTo][endTag] - this.mLogZ;
/*     */ 
/* 262 */     for (int n = 1; n < tags.length; n++)
/* 263 */       logProb += this.mLogTransitions[(tokenFrom + n - 1)][tags[(n - 1)]][tags[n]];
/* 264 */     return logProb;
/*     */   }
/*     */ 
/*     */   public double logForward(int token, int tag) {
/* 268 */     return this.mLogForwards[token][tag];
/*     */   }
/*     */ 
/*     */   public double logBackward(int token, int tag) {
/* 272 */     return this.mLogBackwards[token][tag];
/*     */   }
/*     */ 
/*     */   public double logTransition(int tokenFrom, int tagFrom, int tagTo) {
/* 276 */     return this.mLogTransitions[tokenFrom][tagFrom][tagTo];
/*     */   }
/*     */ 
/*     */   public double logZ()
/*     */   {
/* 289 */     return this.mLogZ;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 301 */     StringBuilder sb = new StringBuilder();
/* 302 */     for (int i = 0; i < this.mTokens.size(); i++)
/* 303 */       sb.append("token[" + i + "]=" + this.mTokens.get(i) + "\n");
/* 304 */     sb.append("\n");
/* 305 */     for (int k = 0; k < this.mTags.size(); k++)
/* 306 */       sb.append("tag[" + k + "]=" + (String)this.mTags.get(k) + "\n");
/* 307 */     sb.append("\nlogZ=" + logZ() + "\n");
/* 308 */     sb.append("\nlogFwd[token][tag]\n");
/* 309 */     for (int i = 0; i < this.mTokens.size(); i++)
/* 310 */       for (int k = 0; k < this.mTags.size(); k++)
/* 311 */         sb.append("logFwd[" + i + "][" + k + "]=" + logForward(i, k) + "\n");
/* 312 */     sb.append("\nlogBk[token][tag]\n");
/* 313 */     for (int i = 0; i < this.mTokens.size(); i++)
/* 314 */       for (int k = 0; k < this.mTags.size(); k++)
/* 315 */         sb.append("logBk[" + i + "][" + k + "]=" + logBackward(i, k) + "\n");
/* 316 */     sb.append("\nlogTrans[tokenFrom][tagFrom][tagTo]\n");
/* 317 */     for (int i = 1; i < this.mTokens.size(); i++) {
/* 318 */       for (int kFrom = 0; kFrom < this.mTags.size(); kFrom++)
/* 319 */         for (int kTo = 0; kTo < this.mTags.size(); kTo++)
/* 320 */           sb.append("logTrans[" + (i - 1) + "][" + kFrom + "][" + kTo + "]=" + logTransition(i - 1, kFrom, kTo) + "\n");
/*     */     }
/* 322 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static void verifyNonPos(String var, double x)
/*     */   {
/* 327 */     if ((Double.isNaN(x)) || (x > 0.0D)) {
/* 328 */       String msg = var + " must be a non-positive number." + " Found " + var + "=" + x;
/*     */ 
/* 330 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void verifyNonPos(String var, double[] xs)
/*     */   {
/* 337 */     for (int i = 0; i < xs.length; i++)
/* 338 */       if ((Double.isNaN(xs[i])) || (xs[i] > 0.0D)) {
/* 339 */         String msg = var + " must be a non-positive number." + " Found " + var + "[" + i + "]=" + xs[i];
/*     */ 
/* 341 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */   }
/*     */ 
/*     */   static void verifyNonPos(String var, double[][] xs)
/*     */   {
/* 347 */     for (int i = 0; i < xs.length; i++)
/* 348 */       for (int j = 0; j < xs[i].length; j++)
/* 349 */         if ((Double.isNaN(xs[i][j])) || (xs[i][j] > 0.0D)) {
/* 350 */           String msg = var + " must be a non-positive number." + " Found " + var + "[" + i + "][" + j + "]=" + xs[i][j];
/*     */ 
/* 352 */           throw new IllegalArgumentException(msg);
/*     */         }
/*     */   }
/*     */ 
/*     */   static void verifyNonPos(String var, double[][][] xs)
/*     */   {
/* 359 */     for (int i = 0; i < xs.length; i++)
/* 360 */       for (int j = 0; j < xs[i].length; j++)
/* 361 */         for (int k = 0; k < xs[i][j].length; k++)
/* 362 */           if ((Double.isNaN(xs[i][j][k])) || (xs[i][j][k] > 0.0D)) {
/* 363 */             String msg = var + " must be finite and non-positive." + " Found " + var + "[" + i + "][" + j + "][" + k + "]=" + xs[i][j][k];
/*     */ 
/* 365 */             throw new IllegalArgumentException(msg);
/*     */           }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.crf.ForwardBackwardTagLattice
 * JD-Core Version:    0.6.2
 */