/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public abstract class RescoringChunker<B extends NBestChunker>
/*     */   implements NBestChunker, ConfidenceChunker
/*     */ {
/*     */   final B mChunker;
/*     */   int mNumChunkingsRescored;
/*     */ 
/*     */   public RescoringChunker(B chunker, int numChunkingsRescored)
/*     */   {
/*  90 */     this.mChunker = chunker;
/*  91 */     this.mNumChunkingsRescored = numChunkingsRescored;
/*     */   }
/*     */ 
/*     */   public abstract double rescore(Chunking paramChunking);
/*     */ 
/*     */   public B baseChunker()
/*     */   {
/* 124 */     return this.mChunker;
/*     */   }
/*     */ 
/*     */   public int numChunkingsRescored()
/*     */   {
/* 134 */     return this.mNumChunkingsRescored;
/*     */   }
/*     */ 
/*     */   public void setNumChunkingsRescored(int numChunkingsRescored)
/*     */   {
/* 146 */     this.mNumChunkingsRescored = numChunkingsRescored;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/* 158 */     char[] cs = Strings.toCharArray(cSeq);
/* 159 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/* 173 */     return firstBest(this.mChunker.nBest(cs, start, end, this.mNumChunkingsRescored));
/*     */   }
/*     */ 
/*     */   public Iterator<ScoredObject<Chunking>> nBest(char[] cs, int start, int end, int maxNBest)
/*     */   {
/* 188 */     return nBest(this.mChunker.nBest(cs, start, end, this.mNumChunkingsRescored), maxNBest);
/*     */   }
/*     */ 
/*     */   public Iterator<Chunk> nBestChunks(char[] cs, int start, int end, int maxNBest)
/*     */   {
/* 207 */     double totalScore = 0.0D;
/* 208 */     Map chunkToScore = new HashMap();
/*     */ 
/* 210 */     Iterator it = nBest(cs, start, end, this.mNumChunkingsRescored);
/*     */     double score;
/* 211 */     while (it.hasNext()) {
/* 212 */       ScoredObject so = (ScoredObject)it.next();
/* 213 */       score = Math.pow(2.0D, so.score());
/* 214 */       totalScore += score;
/* 215 */       Chunking chunking = (Chunking)so.getObject();
/* 216 */       for (Chunk chunk : chunking.chunkSet()) {
/* 217 */         Chunk unscoredChunk = ChunkFactory.createChunk(chunk.start(), chunk.end(), chunk.type());
/*     */ 
/* 220 */         Double currentScoreD = (Double)chunkToScore.get(chunk);
/* 221 */         double currentScore = currentScoreD == null ? 0.0D : currentScoreD.doubleValue();
/*     */ 
/* 224 */         double nextScore = currentScore + score;
/* 225 */         chunkToScore.put(unscoredChunk, Double.valueOf(nextScore));
/*     */       }
/*     */     }
/* 228 */     BoundedPriorityQueue bpq = new BoundedPriorityQueue(ScoredObject.comparator(), maxNBest);
/*     */ 
/* 231 */     for (Map.Entry entry : chunkToScore.entrySet()) {
/* 232 */       Chunk chunk = (Chunk)entry.getKey();
/* 233 */       double conditionalEstimate = ((Double)entry.getValue()).doubleValue() / totalScore;
/*     */ 
/* 235 */       Chunk scored = ChunkFactory.createChunk(chunk.start(), chunk.end(), chunk.type(), conditionalEstimate);
/*     */ 
/* 239 */       bpq.offer(scored);
/*     */     }
/* 241 */     return bpq.iterator();
/*     */   }
/*     */ 
/*     */   private Chunking firstBest(Iterator<ScoredObject<Chunking>> nBestChunkingIt) {
/* 245 */     Chunking bestChunking = null;
/* 246 */     double bestScore = (-1.0D / 0.0D);
/* 247 */     while (nBestChunkingIt.hasNext()) {
/* 248 */       ScoredObject scoredChunking = (ScoredObject)nBestChunkingIt.next();
/* 249 */       Chunking chunking = (Chunking)scoredChunking.getObject();
/* 250 */       double score = rescore(chunking);
/* 251 */       if (score > bestScore) {
/* 252 */         bestScore = score;
/* 253 */         bestChunking = chunking;
/*     */       }
/*     */     }
/* 256 */     return bestChunking;
/*     */   }
/*     */ 
/*     */   private Iterator<ScoredObject<Chunking>> nBest(Iterator<ScoredObject<Chunking>> nBestChunkingIt, int maxNBest)
/*     */   {
/* 263 */     BoundedPriorityQueue queue = new BoundedPriorityQueue(ScoredObject.comparator(), maxNBest);
/*     */ 
/* 266 */     while (nBestChunkingIt.hasNext()) {
/* 267 */       ScoredObject scoredChunking = (ScoredObject)nBestChunkingIt.next();
/*     */ 
/* 269 */       Chunking chunking = (Chunking)scoredChunking.getObject();
/* 270 */       double score = rescore(chunking);
/* 271 */       queue.offer(new ScoredObject(chunking, score));
/*     */     }
/* 273 */     return queue.iterator();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.RescoringChunker
 * JD-Core Version:    0.6.2
 */