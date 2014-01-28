/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ public class ChunkFactory
/*     */ {
/*     */   public static final String DEFAULT_CHUNK_TYPE = "CHUNK";
/*     */   public static final double DEFAULT_CHUNK_SCORE = (-1.0D / 0.0D);
/*     */ 
/*     */   public static Chunk createChunk(int start, int end)
/*     */   {
/*  65 */     validateSpan(start, end);
/*  66 */     return new StartEndChunk(start, end);
/*     */   }
/*     */ 
/*     */   public static Chunk createChunk(int start, int end, String type)
/*     */   {
/*  82 */     validateSpan(start, end);
/*  83 */     return new StartEndTypeChunk(start, end, type);
/*     */   }
/*     */ 
/*     */   public static Chunk createChunk(int start, int end, double score)
/*     */   {
/*  99 */     validateSpan(start, end);
/* 100 */     return new StartEndScoreChunk(start, end, score);
/*     */   }
/*     */ 
/*     */   public static Chunk createChunk(int start, int end, String type, double score)
/*     */   {
/* 117 */     validateSpan(start, end);
/* 118 */     return new StartEndTypeScoreChunk(start, end, type, score);
/*     */   }
/*     */ 
/*     */   private static void validateSpan(int start, int end) {
/* 122 */     if (start < 0) {
/* 123 */       String msg = "Start must be >= 0. Found start=" + start;
/*     */ 
/* 125 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 127 */     if (start > end) {
/* 128 */       String msg = "Start must be > end. Found start=" + start + " end=" + end;
/*     */ 
/* 131 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StartEndTypeScoreChunk extends ChunkFactory.AbstractChunk
/*     */   {
/*     */     private final String mType;
/*     */     private final double mScore;
/*     */ 
/*     */     StartEndTypeScoreChunk(int start, int end, String type, double score)
/*     */     {
/* 211 */       super(end);
/* 212 */       this.mType = type;
/* 213 */       this.mScore = score;
/*     */     }
/*     */ 
/*     */     public String type() {
/* 217 */       return this.mType;
/*     */     }
/*     */ 
/*     */     public double score() {
/* 221 */       return this.mScore;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StartEndScoreChunk extends ChunkFactory.AbstractChunk
/*     */   {
/*     */     private final double mScore;
/*     */ 
/*     */     StartEndScoreChunk(int start, int end, double score)
/*     */     {
/* 197 */       super(end);
/* 198 */       this.mScore = score;
/*     */     }
/*     */ 
/*     */     public double score() {
/* 202 */       return this.mScore;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StartEndTypeChunk extends ChunkFactory.AbstractChunk
/*     */   {
/*     */     private final String mType;
/*     */ 
/*     */     StartEndTypeChunk(int start, int end, String type)
/*     */     {
/* 185 */       super(end);
/* 186 */       this.mType = type;
/*     */     }
/*     */ 
/*     */     public String type() {
/* 190 */       return this.mType;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StartEndChunk extends ChunkFactory.AbstractChunk
/*     */   {
/*     */     StartEndChunk(int start, int end)
/*     */     {
/* 178 */       super(end);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class AbstractChunk
/*     */     implements Chunk
/*     */   {
/*     */     private final int mStart;
/*     */     private final int mEnd;
/*     */ 
/*     */     AbstractChunk(int start, int end)
/*     */     {
/* 139 */       this.mStart = start;
/* 140 */       this.mEnd = end;
/*     */     }
/*     */     public final int start() {
/* 143 */       return this.mStart;
/*     */     }
/*     */     public final int end() {
/* 146 */       return this.mEnd;
/*     */     }
/*     */     public String type() {
/* 149 */       return "CHUNK";
/*     */     }
/*     */     public double score() {
/* 152 */       return (-1.0D / 0.0D);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object that) {
/* 156 */       if (!(that instanceof Chunk)) return false;
/* 157 */       Chunk thatChunk = (Chunk)that;
/* 158 */       return (start() == thatChunk.start()) && (end() == thatChunk.end()) && (score() == thatChunk.score()) && (type().equals(thatChunk.type()));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 165 */       int h1 = start();
/* 166 */       int h2 = end();
/* 167 */       int h3 = type().hashCode();
/* 168 */       return h1 + 31 * (h2 + 31 * h3);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 172 */       return start() + "-" + end() + ":" + type() + "@" + score();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.ChunkFactory
 * JD-Core Version:    0.6.2
 */