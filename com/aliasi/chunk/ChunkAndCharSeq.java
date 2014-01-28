/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.Strings;
/*     */ 
/*     */ public class ChunkAndCharSeq
/*     */   implements Scored
/*     */ {
/*     */   private final Chunk mChunk;
/*     */   private final CharSequence mSeq;
/*     */   private final int mHashCode;
/*     */ 
/*     */   public ChunkAndCharSeq(Chunk chunk, CharSequence cSeq)
/*     */   {
/*  56 */     if (chunk.end() > cSeq.length()) {
/*  57 */       String msg = "Character sequence not long enough for chunk. Chunk end=" + chunk.end() + " Character sequence length=" + cSeq.length();
/*     */ 
/*  60 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  62 */     this.mChunk = chunk;
/*  63 */     this.mSeq = cSeq;
/*  64 */     this.mHashCode = (chunk.hashCode() + 31 * Strings.hashCode(cSeq));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  75 */     return this.mHashCode;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/*  89 */     if (!(that instanceof ChunkAndCharSeq)) return false;
/*  90 */     ChunkAndCharSeq thatChunk = (ChunkAndCharSeq)that;
/*  91 */     if (thatChunk.hashCode() != hashCode()) return false;
/*  92 */     return (this.mChunk.equals(thatChunk.mChunk)) && (this.mSeq.equals(thatChunk.mSeq));
/*     */   }
/*     */ 
/*     */   public String span()
/*     */   {
/* 106 */     return this.mSeq.subSequence(this.mChunk.start(), this.mChunk.end()).toString();
/*     */   }
/*     */ 
/*     */   public CharSequence spanStartContext(int contextLength)
/*     */   {
/* 126 */     if (contextLength < 1) {
/* 127 */       String msg = "Context length must be greater than 0.";
/* 128 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 130 */     int start = this.mChunk.start() - contextLength;
/* 131 */     if (start < 0) start = 0;
/* 132 */     int end = this.mChunk.start() + contextLength;
/* 133 */     if (end > this.mSeq.length()) end = this.mSeq.length();
/* 134 */     int len = end - start;
/* 135 */     if (len < contextLength * 2) {
/* 136 */       StringBuilder padded = new StringBuilder();
/* 137 */       for (int i = contextLength * 2; i > len; i--) {
/* 138 */         padded.append(" ");
/*     */       }
/* 140 */       padded.append(this.mSeq.subSequence(start, end).toString());
/* 141 */       return padded.subSequence(0, padded.length());
/*     */     }
/* 143 */     return this.mSeq.subSequence(start, end).toString();
/*     */   }
/*     */ 
/*     */   public CharSequence spanEndContext(int contextLength)
/*     */   {
/* 162 */     if (contextLength < 1) {
/* 163 */       String msg = "Context length must be greater than 0.";
/* 164 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 166 */     int start = this.mChunk.end() - contextLength;
/* 167 */     if (start < 0) start = 0;
/* 168 */     int end = this.mChunk.end() + contextLength;
/* 169 */     if (end > this.mSeq.length()) end = this.mSeq.length();
/* 170 */     int len = end - start;
/* 171 */     if (len < contextLength * 2) {
/* 172 */       StringBuilder padded = new StringBuilder();
/* 173 */       padded.append(this.mSeq.subSequence(start, end).toString());
/* 174 */       for (int i = contextLength * 2; i > len; i--) {
/* 175 */         padded.append(" ");
/*     */       }
/* 177 */       return padded.subSequence(0, padded.length());
/*     */     }
/* 179 */     return this.mSeq.subSequence(start, end).toString();
/*     */   }
/*     */ 
/*     */   public String charSequence()
/*     */   {
/* 188 */     return this.mSeq.toString();
/*     */   }
/*     */ 
/*     */   public Chunk chunk()
/*     */   {
/* 196 */     return this.mChunk;
/*     */   }
/*     */ 
/*     */   public double score()
/*     */   {
/* 205 */     return chunk().score();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 217 */     return chunk().start() + "-" + chunk().end() + "/" + span() + ":" + chunk().type();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.ChunkAndCharSeq
 * JD-Core Version:    0.6.2
 */