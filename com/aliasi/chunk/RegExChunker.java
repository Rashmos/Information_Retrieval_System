/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class RegExChunker
/*     */   implements Chunker, Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -8997320544817071938L;
/*     */   private final Pattern mPattern;
/*     */   private final String mChunkType;
/*     */   private final double mChunkScore;
/*     */ 
/*     */   public RegExChunker(String regex, String chunkType, double chunkScore)
/*     */   {
/*  87 */     this(Pattern.compile(regex), chunkType, chunkScore);
/*     */   }
/*     */ 
/*     */   public RegExChunker(Pattern pattern, String chunkType, double chunkScore)
/*     */   {
/*  99 */     this.mPattern = pattern;
/* 100 */     this.mChunkType = chunkType;
/* 101 */     this.mChunkScore = chunkScore;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/* 113 */     ChunkingImpl result = new ChunkingImpl(cSeq);
/* 114 */     Matcher matcher = this.mPattern.matcher(cSeq);
/* 115 */     while (matcher.find()) {
/* 116 */       int start = matcher.start();
/* 117 */       int end = matcher.end();
/* 118 */       Chunk chunk = ChunkFactory.createChunk(start, end, this.mChunkType, this.mChunkScore);
/*     */ 
/* 120 */       result.add(chunk);
/*     */     }
/* 122 */     return result;
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 136 */     out.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   private Object writeReplace() {
/* 140 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/* 152 */     return chunk(new String(cs, start, end - start));
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -3419191413174871277L;
/*     */     private final RegExChunker mChunker;
/*     */ 
/* 159 */     public Externalizer() { this(null); }
/*     */ 
/*     */     public Externalizer(RegExChunker chunker) {
/* 162 */       this.mChunker = chunker;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 166 */       out.writeUTF(this.mChunker.mPattern.pattern());
/* 167 */       out.writeUTF(this.mChunker.mChunkType);
/* 168 */       out.writeDouble(this.mChunker.mChunkScore);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/* 172 */       String pattern = in.readUTF();
/* 173 */       String chunkType = in.readUTF();
/* 174 */       double score = in.readDouble();
/* 175 */       return new RegExChunker(pattern, chunkType, score);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.RegExChunker
 * JD-Core Version:    0.6.2
 */