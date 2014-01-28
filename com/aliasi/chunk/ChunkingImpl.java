/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.util.Iterators;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ChunkingImpl
/*     */   implements Chunking, Iterable<Chunk>
/*     */ {
/*     */   private final String mString;
/*  43 */   private final Set<Chunk> mChunkSet = new LinkedHashSet();
/*     */ 
/* 168 */   static final Chunk[] EMPTY_CHUNK_ARRAY = new Chunk[0];
/*     */ 
/*     */   public ChunkingImpl(CharSequence cSeq)
/*     */   {
/*  56 */     this.mString = cSeq.toString();
/*     */   }
/*     */ 
/*     */   public ChunkingImpl(char[] cs, int start, int end)
/*     */   {
/*  73 */     this(new String(cs, start, end - start));
/*     */   }
/*     */ 
/*     */   public void addAll(Collection<Chunk> chunks)
/*     */   {
/*  87 */     for (Chunk next : chunks)
/*  88 */       add(next);
/*     */   }
/*     */ 
/*     */   public Iterator<Chunk> iterator()
/*     */   {
/*  99 */     return Iterators.unmodifiable(chunkSet().iterator());
/*     */   }
/*     */ 
/*     */   public void add(Chunk chunk)
/*     */   {
/* 112 */     if (chunk.end() > this.mString.length()) {
/* 113 */       String msg = "End point of chunk beyond end of char sequence.Char sequence length=" + this.mString.length() + " chunk.end()=" + chunk.end();
/*     */ 
/* 116 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 118 */     this.mChunkSet.add(chunk);
/*     */   }
/*     */ 
/*     */   public CharSequence charSequence()
/*     */   {
/* 127 */     return this.mString;
/*     */   }
/*     */ 
/*     */   public Set<Chunk> chunkSet()
/*     */   {
/* 138 */     return Collections.unmodifiableSet(this.mChunkSet);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 145 */     return (that instanceof Chunking) ? equal(this, (Chunking)that) : false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 152 */     return hashCode(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 164 */     return charSequence() + " : " + chunkSet();
/*     */   }
/*     */ 
/*     */   public static boolean equal(Chunking chunking1, Chunking chunking2)
/*     */   {
/* 184 */     return (Strings.equalCharSequence(chunking1.charSequence(), chunking2.charSequence())) && (chunking1.chunkSet().equals(chunking2.chunkSet()));
/*     */   }
/*     */ 
/*     */   public static int hashCode(Chunking chunking)
/*     */   {
/* 199 */     return Strings.hashCode(chunking.charSequence()) + 31 * chunking.chunkSet().hashCode();
/*     */   }
/*     */ 
/*     */   public static boolean overlap(Chunk chunk1, Chunk chunk2)
/*     */   {
/* 222 */     return (overlapOneWay(chunk1, chunk2)) || (overlapOneWay(chunk2, chunk1));
/*     */   }
/*     */ 
/*     */   public static Chunking merge(Chunking chunking1, Chunking chunking2)
/*     */   {
/* 252 */     if (!Strings.equalCharSequence(chunking1.charSequence(), chunking2.charSequence()))
/*     */     {
/* 254 */       String msg = "Chunkings must be over same character sequence. Found chunking1.charSequence()=" + chunking1.charSequence() + " chunking2.charSequence()=" + chunking2.charSequence();
/*     */ 
/* 257 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 259 */     ChunkingImpl chunking = new ChunkingImpl(chunking1.charSequence().toString());
/* 260 */     Chunk[] chunks1 = sortedChunks(chunking1);
/* 261 */     Chunk[] chunks2 = sortedChunks(chunking2);
/* 262 */     int pos1 = 0;
/* 263 */     Chunk lastChunk = null;
/* 264 */     for (int pos2 = 0; pos2 < chunks2.length; pos2++) {
/* 265 */       for (; (isBefore(chunks1, pos1, chunks2, pos2)) || (overlap(chunks1, pos1, lastChunk)); pos1++) {
/* 266 */         if (!overlap(chunks1, pos1, lastChunk)) {
/* 267 */           lastChunk = chunks1[pos1];
/* 268 */           chunking.add(lastChunk);
/*     */         }
/*     */       }
/* 271 */       if (((pos1 >= chunks1.length) || (!overlap(chunks1[pos1], chunks2[pos2]))) && (!overlap(chunks2, pos2, lastChunk)))
/*     */       {
/* 273 */         lastChunk = chunks2[pos2];
/* 274 */         chunking.add(lastChunk);
/*     */       }
/*     */     }
/* 277 */     for (; pos1 < chunks1.length; pos1++) {
/* 278 */       if (!overlap(chunks1, pos1, lastChunk)) {
/* 279 */         lastChunk = chunks1[pos1];
/* 280 */         chunking.add(lastChunk);
/*     */       }
/*     */     }
/* 283 */     return chunking;
/*     */   }
/*     */ 
/*     */   static boolean overlap(Chunk[] chunks, int pos, Chunk lastChunk) {
/* 287 */     return (lastChunk != null) && (pos < chunks.length) && (overlap(chunks[pos], lastChunk));
/*     */   }
/*     */ 
/*     */   static boolean isBefore(Chunk[] chunks1, int pos1, Chunk[] chunks2, int pos2)
/*     */   {
/* 294 */     return (pos1 < chunks1.length) && (chunks1[pos1].end() <= chunks2[pos2].start());
/*     */   }
/*     */ 
/*     */   static boolean overlapOneWay(Chunk chunk1, Chunk chunk2)
/*     */   {
/* 299 */     return (chunk1.start() <= chunk2.start()) && (chunk2.start() < chunk1.end());
/*     */   }
/*     */ 
/*     */   static final Chunk[] sortedChunks(Chunking chunking)
/*     */   {
/* 304 */     Set chunkSet = chunking.chunkSet();
/* 305 */     Chunk[] chunks = (Chunk[])chunkSet.toArray(EMPTY_CHUNK_ARRAY);
/* 306 */     Arrays.sort(chunks, Chunk.TEXT_ORDER_COMPARATOR);
/* 307 */     return chunks;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.ChunkingImpl
 * JD-Core Version:    0.6.2
 */