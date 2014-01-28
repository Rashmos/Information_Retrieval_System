/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.classify.ScoredPrecisionRecallEvaluation;
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Formatter;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ChunkerEvaluator
/*     */   implements ObjectHandler<Chunking>
/*     */ {
/*     */   private Chunker mChunker;
/*  64 */   private boolean mVerbose = false;
/*     */   private final ChunkingEvaluation mChunkingEvaluation;
/*  70 */   private final ObjectToCounterMap<Integer> mCorrectRanks = new ObjectToCounterMap();
/*     */ 
/*  74 */   private final ScoredPrecisionRecallEvaluation mConfEval = new ScoredPrecisionRecallEvaluation();
/*     */ 
/*  78 */   int mMaxNBest = 64;
/*  79 */   int mMaxNBestPrint = 8;
/*  80 */   String mLastNBestCase = null;
/*     */ 
/*  83 */   int mConfMaxChunks = 128;
/*  84 */   String mLastConfidenceCase = null;
/*     */ 
/*     */   public ChunkerEvaluator(Chunker chunker)
/*     */   {
/*  93 */     this.mChunker = chunker;
/*  94 */     this.mChunkingEvaluation = new ChunkingEvaluation();
/*     */   }
/*     */ 
/*     */   public Chunker chunker()
/*     */   {
/* 103 */     return this.mChunker;
/*     */   }
/*     */ 
/*     */   public void setChunker(Chunker chunker)
/*     */   {
/* 112 */     this.mChunker = chunker;
/*     */   }
/*     */ 
/*     */   public void setVerbose(boolean isVerbose)
/*     */   {
/* 130 */     this.mVerbose = isVerbose;
/*     */   }
/*     */ 
/*     */   public String lastFirstBestCaseReport()
/*     */   {
/* 140 */     return this.mChunkingEvaluation.mLastCase;
/*     */   }
/*     */ 
/*     */   public void setMaxConfidenceChunks(int n)
/*     */   {
/* 150 */     this.mConfMaxChunks = n;
/*     */   }
/*     */ 
/*     */   public String lastConfidenceCaseReport()
/*     */   {
/* 164 */     return this.mLastConfidenceCase;
/*     */   }
/*     */ 
/*     */   public void setMaxNBest(int n)
/*     */   {
/* 174 */     this.mMaxNBest = n;
/*     */   }
/*     */ 
/*     */   public void setMaxNBestReport(int n)
/*     */   {
/* 185 */     this.mMaxNBestPrint = n;
/*     */   }
/*     */ 
/*     */   public String lastNBestCaseReport()
/*     */   {
/* 195 */     return this.mLastNBestCase;
/*     */   }
/*     */ 
/*     */   void handle(String[] tokens, String[] whitespaces, String[] tags)
/*     */   {
/* 211 */     ChunkTagHandlerAdapter2 adapter = new ChunkTagHandlerAdapter2(this);
/* 212 */     adapter.handle(tokens, whitespaces, tags);
/*     */   }
/*     */ 
/*     */   public void handle(Chunking referenceChunking)
/*     */   {
/* 229 */     CharSequence cSeq = referenceChunking.charSequence();
/*     */ 
/* 233 */     Chunking firstBestChunking = this.mChunker.chunk(cSeq);
/* 234 */     if (firstBestChunking == null)
/* 235 */       firstBestChunking = new ChunkingImpl(cSeq);
/* 236 */     this.mChunkingEvaluation.addCase(referenceChunking, firstBestChunking);
/*     */ 
/* 238 */     if ((this.mChunker instanceof NBestChunker)) {
/* 239 */       NBestChunker nBestChunker = (NBestChunker)this.mChunker;
/* 240 */       char[] cs = Strings.toCharArray(cSeq);
/* 241 */       StringBuilder sb = new StringBuilder();
/*     */ 
/* 243 */       sb.append(ChunkingEvaluation.formatHeader(13, referenceChunking));
/* 244 */       sb.append(" REF                 " + ChunkingEvaluation.formatChunks(referenceChunking));
/*     */ 
/* 247 */       double score = (-1.0D / 0.0D);
/* 248 */       int foundRank = -1;
/* 249 */       int i = 0;
/* 250 */       Iterator nBestIt = nBestChunker.nBest(cs, 0, cs.length, this.mMaxNBest);
/*     */ 
/* 252 */       Formatter formatter = new Formatter(sb, Locale.US);
/* 253 */       for (i = 0; (i < this.mMaxNBest) && (nBestIt.hasNext()); i++) {
/* 254 */         ScoredObject so = (ScoredObject)nBestIt.next();
/* 255 */         score = so.score();
/* 256 */         Chunking responseChunking = (Chunking)so.getObject();
/* 257 */         if (i < this.mMaxNBestPrint) {
/* 258 */           formatter.format("%9d", new Object[] { Integer.valueOf(i) });
/* 259 */           sb.append(" ");
/* 260 */           formatter.format("%10.3f", new Object[] { Double.valueOf(score) });
/* 261 */           sb.append(" ");
/* 262 */           sb.append(ChunkingEvaluation.formatChunks(responseChunking));
/*     */         }
/* 264 */         if (responseChunking.equals(referenceChunking)) {
/* 265 */           sb.append("  -----------\n");
/* 266 */           foundRank = i;
/*     */         }
/*     */       }
/* 269 */       if (foundRank < 0)
/* 270 */         sb.append("Correct Rank >=" + this.mMaxNBest + "\n\n");
/*     */       else
/* 272 */         sb.append("Correct Rank=" + foundRank + "\n\n");
/* 273 */       this.mCorrectRanks.increment(Integer.valueOf(foundRank));
/*     */ 
/* 275 */       this.mLastNBestCase = sb.toString();
/*     */     }
/*     */ 
/* 278 */     if ((this.mChunker instanceof ConfidenceChunker)) {
/* 279 */       ConfidenceChunker confChunker = (ConfidenceChunker)this.mChunker;
/* 280 */       char[] cs = Strings.toCharArray(cSeq);
/* 281 */       StringBuilder sb = new StringBuilder();
/* 282 */       Set refChunks = new HashSet();
/* 283 */       for (Chunk nextChunk : referenceChunking.chunkSet()) {
/* 284 */         Chunk zeroChunk = toUnscoredChunk(nextChunk);
/* 285 */         refChunks.add(zeroChunk);
/*     */       }
/* 287 */       sb.append(ChunkingEvaluation.formatHeader(5, referenceChunking));
/*     */ 
/* 289 */       Iterator nBestChunkIt = confChunker.nBestChunks(cs, 0, cs.length, this.mConfMaxChunks);
/*     */ 
/* 292 */       int count = 0;
/* 293 */       int missCount = refChunks.size();
/* 294 */       while (nBestChunkIt.hasNext()) {
/* 295 */         Chunk nextChunk = (Chunk)nBestChunkIt.next();
/* 296 */         double score = nextChunk.score();
/* 297 */         Chunk zeroedChunk = toUnscoredChunk(nextChunk);
/* 298 */         boolean correct = refChunks.contains(zeroedChunk);
/* 299 */         if (correct) missCount--;
/* 300 */         sb.append((correct ? "TRUE " : "false") + " (" + nextChunk.start() + ", " + nextChunk.end() + ")" + ": " + nextChunk.type() + "  " + nextChunk.score() + "\n");
/*     */ 
/* 305 */         this.mConfEval.addCase(correct, score);
/*     */       }
/* 307 */       this.mConfEval.addMisses(missCount);
/* 308 */       this.mLastConfidenceCase = sb.toString();
/*     */     }
/* 310 */     report();
/*     */   }
/*     */ 
/*     */   void report() {
/* 314 */     if (!this.mVerbose) return;
/* 315 */     System.out.println(this.mChunkingEvaluation.mLastCase);
/* 316 */     if ((this.mChunker instanceof NBestChunker))
/* 317 */       System.out.println(this.mLastNBestCase);
/* 318 */     if ((this.mChunker instanceof ConfidenceChunker))
/* 319 */       System.out.println(this.mLastConfidenceCase);
/*     */   }
/*     */ 
/*     */   public ScoredPrecisionRecallEvaluation confidenceEvaluation()
/*     */   {
/* 334 */     return this.mConfEval;
/*     */   }
/*     */ 
/*     */   public ChunkingEvaluation evaluation()
/*     */   {
/* 347 */     return this.mChunkingEvaluation;
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap<Integer> nBestEvaluation()
/*     */   {
/* 367 */     return this.mCorrectRanks;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 379 */     StringBuilder sb = new StringBuilder();
/* 380 */     sb.append("FIRST-BEST EVAL\n");
/* 381 */     sb.append(evaluation().toString());
/* 382 */     if ((this.mChunker instanceof NBestChunker)) {
/* 383 */       sb.append("\n\nN-BEST EVAL (rank=count)\n");
/* 384 */       sb.append(nBestEvaluation().toString());
/*     */     }
/* 386 */     if ((this.mChunker instanceof ConfidenceChunker)) {
/* 387 */       sb.append("\n\nCONFIDENCE EVALUATION");
/* 388 */       sb.append(confidenceEvaluation().toString());
/*     */     }
/* 390 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static Chunk toUnscoredChunk(Chunk c) {
/* 394 */     return ChunkFactory.createChunk(c.start(), c.end(), c.type());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.ChunkerEvaluator
 * JD-Core Version:    0.6.2
 */