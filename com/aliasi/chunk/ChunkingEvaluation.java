/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.classify.PrecisionRecallEvaluation;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ChunkingEvaluation
/*     */ {
/*  75 */   private final Set<Chunking[]> mCases = new HashSet();
/*     */ 
/*  77 */   private final Set<ChunkAndCharSeq> mTruePositiveSet = new HashSet();
/*     */ 
/*  79 */   private final Set<ChunkAndCharSeq> mFalsePositiveSet = new HashSet();
/*     */ 
/*  81 */   private final Set<ChunkAndCharSeq> mFalseNegativeSet = new HashSet();
/*     */ 
/*  84 */   String mLastCase = null;
/*     */ 
/*     */   public Set<Chunking[]> cases()
/*     */   {
/* 108 */     return Collections.unmodifiableSet(this.mCases);
/*     */   }
/*     */ 
/*     */   public ChunkingEvaluation perTypeEvaluation(String chunkType)
/*     */   {
/* 123 */     ChunkingEvaluation evaluation = new ChunkingEvaluation();
/* 124 */     for (Chunking[] testCase : cases()) {
/* 125 */       Chunking referenceChunking = testCase[0];
/* 126 */       Chunking responseChunking = testCase[1];
/* 127 */       Chunking referenceChunkingRestricted = restrictTo(referenceChunking, chunkType);
/*     */ 
/* 129 */       Chunking responseChunkingRestricted = restrictTo(responseChunking, chunkType);
/*     */ 
/* 131 */       evaluation.addCase(referenceChunkingRestricted, responseChunkingRestricted);
/*     */     }
/*     */ 
/* 134 */     return evaluation;
/*     */   }
/*     */ 
/*     */   static Chunking restrictTo(Chunking chunking, String type) {
/* 138 */     CharSequence cs = chunking.charSequence();
/* 139 */     ChunkingImpl chunkingOut = new ChunkingImpl(cs);
/* 140 */     for (Chunk chunk : chunking.chunkSet())
/* 141 */       if (chunk.type().equals(type))
/* 142 */         chunkingOut.add(chunk);
/* 143 */     return chunkingOut;
/*     */   }
/*     */ 
/*     */   static String formatChunks(Chunking chunking)
/*     */   {
/* 148 */     StringBuilder sb = new StringBuilder();
/* 149 */     int pos = 0;
/* 150 */     for (Chunk chunk : chunking.chunkSet()) {
/* 151 */       int start = chunk.start();
/* 152 */       int padLength = start - pos;
/* 153 */       for (int j = 0; j < padLength; j++)
/* 154 */         sb.append(" ");
/* 155 */       int end = chunk.end();
/* 156 */       int chunkLength = end - start;
/* 157 */       char marker = chunk.type().length() > 0 ? chunk.type().charAt(0) : '!';
/*     */ 
/* 160 */       if (chunkLength > 0) sb.append(marker);
/* 161 */       for (int j = 1; j < chunkLength; j++)
/* 162 */         sb.append(".");
/* 163 */       pos = end;
/*     */     }
/* 165 */     sb.append("\n");
/* 166 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static String formatHeader(int indent, Chunking chunking) {
/* 170 */     String cs = chunking.charSequence().toString();
/* 171 */     StringBuilder sb = new StringBuilder();
/* 172 */     for (int i = 0; i < indent; i++) sb.append(" ");
/* 173 */     sb.append("CHUNKS= ");
/* 174 */     for (Chunk chunk : chunking.chunkSet()) {
/* 175 */       sb.append("(" + chunk.start() + "," + chunk.end() + "):" + chunk.type() + "   ");
/*     */     }
/*     */ 
/* 180 */     if (sb.charAt(sb.length() - 1) != '\n') sb.append("\n");
/* 181 */     for (int i = 0; i < indent; i++) sb.append(" ");
/* 182 */     sb.append(cs);
/* 183 */     sb.append("\n");
/* 184 */     int length = cs.length();
/* 185 */     printMods(1, length, sb, indent);
/* 186 */     printMods(10, length, sb, indent);
/* 187 */     printMods(100, length, sb, indent);
/* 188 */     if (sb.charAt(sb.length() - 1) != '\n') sb.append("\n");
/* 189 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   static void printMods(int base, int length, StringBuilder sb, int indent) {
/* 193 */     if (length <= base) return;
/* 194 */     for (int i = 0; i < indent; i++) sb.append(" ");
/* 195 */     for (int i = 0; i < length; i++) {
/* 196 */       if ((base == 1) || ((i >= base) && (i % 10 == 0)))
/* 197 */         sb.append(Integer.toString(i / base % 10));
/*     */       else
/* 199 */         sb.append(" ");
/*     */     }
/* 201 */     sb.append("\n");
/*     */   }
/*     */ 
/*     */   public void addCase(Chunking referenceChunking, Chunking responseChunking)
/*     */   {
/* 216 */     StringBuilder sb = new StringBuilder();
/*     */ 
/* 218 */     CharSequence cSeq = referenceChunking.charSequence();
/* 219 */     if (!Strings.equalCharSequence(cSeq, responseChunking.charSequence()))
/*     */     {
/* 221 */       String msg = "Char sequences must be same. Reference char seq=" + cSeq + " Response char seq=" + responseChunking.charSequence();
/*     */ 
/* 224 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 226 */     sb.append("\n");
/* 227 */     sb.append(formatHeader(5, referenceChunking));
/* 228 */     sb.append("\n REF ");
/* 229 */     sb.append(formatChunks(referenceChunking));
/* 230 */     sb.append("RESP ");
/* 231 */     sb.append(formatChunks(responseChunking));
/* 232 */     sb.append("\n");
/* 233 */     this.mLastCase = sb.toString();
/*     */ 
/* 235 */     this.mCases.add(new Chunking[] { referenceChunking, responseChunking });
/*     */ 
/* 237 */     Set refSet = unscoredChunkSet(referenceChunking);
/* 238 */     Set respSet = unscoredChunkSet(responseChunking);
/* 239 */     for (Chunk respChunk : respSet) {
/* 240 */       boolean inRef = refSet.remove(respChunk);
/* 241 */       ChunkAndCharSeq ccs = new ChunkAndCharSeq(respChunk, cSeq);
/* 242 */       if (inRef)
/* 243 */         this.mTruePositiveSet.add(ccs);
/*     */       else {
/* 245 */         this.mFalsePositiveSet.add(ccs);
/*     */       }
/*     */     }
/* 248 */     for (Chunk refChunk : refSet)
/* 249 */       this.mFalseNegativeSet.add(new ChunkAndCharSeq(refChunk, cSeq));
/*     */   }
/*     */ 
/*     */   static Set<Chunk> unscoredChunkSet(Chunking chunking)
/*     */   {
/* 254 */     Set result = new HashSet();
/* 255 */     for (Chunk chunk : chunking.chunkSet()) {
/* 256 */       result.add(ChunkFactory.createChunk(chunk.start(), chunk.end(), chunk.type()));
/*     */     }
/*     */ 
/* 259 */     return result;
/*     */   }
/*     */ 
/*     */   public Set<ChunkAndCharSeq> truePositiveSet()
/*     */   {
/* 274 */     return Collections.unmodifiableSet(this.mTruePositiveSet);
/*     */   }
/*     */ 
/*     */   public Set<ChunkAndCharSeq> falsePositiveSet()
/*     */   {
/* 289 */     return Collections.unmodifiableSet(this.mFalsePositiveSet);
/*     */   }
/*     */ 
/*     */   public Set<ChunkAndCharSeq> falseNegativeSet()
/*     */   {
/* 304 */     return Collections.unmodifiableSet(this.mFalseNegativeSet);
/*     */   }
/*     */ 
/*     */   public PrecisionRecallEvaluation precisionRecallEvaluation()
/*     */   {
/* 315 */     int tp = truePositiveSet().size();
/* 316 */     int fn = falseNegativeSet().size();
/* 317 */     int fp = falsePositiveSet().size();
/* 318 */     return new PrecisionRecallEvaluation(tp, fn, fp, 0L);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 329 */     return precisionRecallEvaluation().toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.ChunkingEvaluation
 * JD-Core Version:    0.6.2
 */