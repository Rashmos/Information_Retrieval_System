/*     */ package com.aliasi.sentences;
/*     */ 
/*     */ import com.aliasi.chunk.Chunk;
/*     */ import com.aliasi.chunk.ChunkAndCharSeq;
/*     */ import com.aliasi.chunk.Chunking;
/*     */ import com.aliasi.chunk.ChunkingEvaluation;
/*     */ import com.aliasi.classify.PrecisionRecallEvaluation;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SentenceEvaluation
/*     */ {
/*     */   private final ChunkingEvaluation mChunkingEvaluation;
/*     */   private final PrecisionRecallEvaluation mEndBoundaryEvaluation;
/*  51 */   private final Set<ChunkAndCharSeq> mTPBoundaries = new HashSet();
/*  52 */   private final Set<ChunkAndCharSeq> mFPBoundaries = new HashSet();
/*  53 */   private final Set<ChunkAndCharSeq> mFNBoundaries = new HashSet();
/*  54 */   private final Chunking[] mLastCase = new Chunking[2];
/*     */ 
/*     */   public SentenceEvaluation()
/*     */   {
/*  59 */     this.mChunkingEvaluation = new ChunkingEvaluation();
/*  60 */     this.mEndBoundaryEvaluation = new PrecisionRecallEvaluation();
/*     */   }
/*     */ 
/*     */   public void addCase(Chunking referenceChunking, Chunking responseChunking)
/*     */   {
/*  77 */     if (!Strings.equalCharSequence(referenceChunking.charSequence(), responseChunking.charSequence()))
/*     */     {
/*  79 */       String msg = "Underlying char sequences must have same characters. Found referenceChunking.charSequence()=" + referenceChunking.charSequence() + " responseChunking.charSequence()=" + responseChunking.charSequence();
/*     */ 
/*  84 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  86 */     verifySentenceTypes("reference", referenceChunking);
/*  87 */     verifySentenceTypes("response", responseChunking);
/*     */ 
/*  89 */     this.mChunkingEvaluation.addCase(referenceChunking, responseChunking);
/*     */ 
/*  91 */     this.mLastCase[0] = referenceChunking;
/*  92 */     this.mLastCase[1] = responseChunking;
/*     */ 
/*  95 */     Map endChunkMap = new HashMap();
/*  96 */     CharSequence cSeq = referenceChunking.charSequence();
/*  97 */     for (Chunk refChunk : referenceChunking.chunkSet()) {
/*  98 */       Integer end = Integer.valueOf(refChunk.end());
/*  99 */       endChunkMap.put(end, refChunk);
/*     */     }
/* 101 */     for (Chunk respChunk : responseChunking.chunkSet()) {
/* 102 */       Integer end = Integer.valueOf(respChunk.end());
/* 103 */       boolean inRef = endChunkMap.containsKey(end);
/* 104 */       ChunkAndCharSeq ccs = new ChunkAndCharSeq(respChunk, cSeq);
/* 105 */       if (inRef) {
/* 106 */         this.mTPBoundaries.add(ccs);
/* 107 */         this.mEndBoundaryEvaluation.addCase(true, true);
/* 108 */         endChunkMap.remove(end);
/*     */       } else {
/* 110 */         this.mFPBoundaries.add(ccs);
/* 111 */         this.mEndBoundaryEvaluation.addCase(false, true);
/*     */       }
/*     */     }
/*     */ 
/* 115 */     for (Chunk refChunk : endChunkMap.values()) {
/* 116 */       this.mFNBoundaries.add(new ChunkAndCharSeq(refChunk, cSeq));
/* 117 */       this.mEndBoundaryEvaluation.addCase(true, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ChunkingEvaluation chunkingEvaluation()
/*     */   {
/* 129 */     return this.mChunkingEvaluation;
/*     */   }
/*     */ 
/*     */   public PrecisionRecallEvaluation endBoundaryEvaluation()
/*     */   {
/* 140 */     return this.mEndBoundaryEvaluation;
/*     */   }
/*     */ 
/*     */   public Set<ChunkAndCharSeq> truePositiveEndBoundaries()
/*     */   {
/* 150 */     return Collections.unmodifiableSet(this.mTPBoundaries);
/*     */   }
/*     */ 
/*     */   public Set<ChunkAndCharSeq> falsePositiveEndBoundaries()
/*     */   {
/* 160 */     return Collections.unmodifiableSet(this.mFPBoundaries);
/*     */   }
/*     */ 
/*     */   public Set<ChunkAndCharSeq> falseNegativeEndBoundaries()
/*     */   {
/* 170 */     return Collections.unmodifiableSet(this.mFNBoundaries);
/*     */   }
/*     */ 
/*     */   static void verifySentenceTypes(String input, Chunking chunking) {
/* 174 */     for (Chunk chunk : chunking.chunkSet())
/* 175 */       if (!chunk.type().equals("S")) {
/* 176 */         String msg = "Chunk must have sentence type. Found type=" + chunk.type() + " in input type=" + input;
/*     */ 
/* 179 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */   }
/*     */ 
/*     */   public String lastCaseToString(int lineLength)
/*     */   {
/* 197 */     if (lineLength < 1) {
/* 198 */       String msg = "Line length must be greater than 0.";
/* 199 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 201 */     if ((this.mLastCase[0] == null) || (this.mLastCase[1] == null)) {
/* 202 */       String msg = "No cases have been evaluated.";
/* 203 */       throw new IllegalStateException(msg);
/*     */     }
/* 205 */     return sentenceCaseToString(this.mLastCase[0], this.mLastCase[1], lineLength);
/*     */   }
/*     */ 
/*     */   public static String sentenceCaseToString(Chunking referenceChunking, Chunking responseChunking, int lineLength)
/*     */   {
/* 228 */     if (lineLength < 1) {
/* 229 */       String msg = "Line length must be greater than 0.";
/* 230 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 232 */     if (!Strings.equalCharSequence(referenceChunking.charSequence(), responseChunking.charSequence()))
/*     */     {
/* 234 */       String msg = "Underlying char sequences must have same characters. Found referenceChunking.charSequence()=" + referenceChunking.charSequence() + " responseChunking.charSequence()=" + responseChunking.charSequence();
/*     */ 
/* 239 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 241 */     verifySentenceTypes("reference", referenceChunking);
/* 242 */     verifySentenceTypes("response", responseChunking);
/*     */ 
/* 244 */     CharSequence cSeq = referenceChunking.charSequence();
/* 245 */     int[] refEnds = new int[referenceChunking.chunkSet().size()];
/* 246 */     int iRef = 0;
/* 247 */     for (Chunk refChunk : referenceChunking.chunkSet())
/* 248 */       refEnds[(iRef++)] = (refChunk.end() - 1);
/* 249 */     int[] respEnds = new int[responseChunking.chunkSet().size()];
/* 250 */     int iResp = 0;
/* 251 */     for (Chunk respChunk : responseChunking.chunkSet())
/* 252 */       respEnds[(iResp++)] = (respChunk.end() - 1);
/* 253 */     StringBuilder sbOut = new StringBuilder();
/* 254 */     StringBuilder refLine = new StringBuilder();
/* 255 */     StringBuilder textLine = new StringBuilder();
/* 256 */     StringBuilder respLine = new StringBuilder();
/* 257 */     refLine.append("ref:  ");
/* 258 */     textLine.append("text: ");
/* 259 */     respLine.append("resp: ");
/* 260 */     int cLen = cSeq.length();
/* 261 */     int refIndex = 0;
/* 262 */     int respIndex = 0;
/* 263 */     for (int i = 0; i < cLen; i++) {
/* 264 */       textLine.append(cSeq.charAt(i));
/* 265 */       if ((refIndex < refEnds.length) && (respIndex < respEnds.length) && (refEnds[refIndex] == i) && (respEnds[respIndex] == i))
/*     */       {
/* 267 */         refLine.append("+");
/* 268 */         respLine.append("+");
/* 269 */         refIndex++;
/* 270 */         respIndex++;
/*     */       }
/* 272 */       else if ((refIndex < refEnds.length) && (refEnds[refIndex] == i)) {
/* 273 */         refLine.append("X");
/* 274 */         respLine.append("-");
/* 275 */         refIndex++;
/*     */       }
/* 277 */       else if ((respIndex < respEnds.length) && (respEnds[respIndex] == i)) {
/* 278 */         refLine.append("-");
/* 279 */         respLine.append("X");
/* 280 */         respIndex++;
/*     */       }
/*     */       else {
/* 283 */         refLine.append("-");
/* 284 */         respLine.append("-");
/*     */       }
/* 286 */       if ((i > 0) && (i % lineLength == 0)) {
/* 287 */         sbOut.append(refLine + "\n");
/* 288 */         sbOut.append(textLine + "\n");
/* 289 */         sbOut.append(respLine + "\n");
/* 290 */         sbOut.append("\n");
/* 291 */         refLine.setLength(0);
/* 292 */         textLine.setLength(0);
/* 293 */         respLine.setLength(0);
/* 294 */         refLine.append("ref:  ");
/* 295 */         textLine.append("text: ");
/* 296 */         respLine.append("resp: ");
/*     */       }
/*     */     }
/* 299 */     sbOut.append(refLine + "\n");
/* 300 */     sbOut.append(textLine + "\n");
/* 301 */     sbOut.append(respLine + "\n");
/* 302 */     sbOut.append("\n\n");
/* 303 */     return sbOut.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.sentences.SentenceEvaluation
 * JD-Core Version:    0.6.2
 */