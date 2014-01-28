/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.tag.StringTagging;
/*     */ import com.aliasi.tag.Tagging;
/*     */ 
/*     */ public class TagChunkCodecAdapters
/*     */ {
/*     */   public static ObjectHandler<Chunking> stringTaggingToChunking(TagChunkCodec codec, ObjectHandler<StringTagging> handler)
/*     */   {
/*  50 */     return new StringTaggingHandlerAdapter(codec, handler);
/*     */   }
/*     */ 
/*     */   public static ObjectHandler<Chunking> taggingToChunking(TagChunkCodec codec, ObjectHandler<Tagging<String>> handler)
/*     */   {
/*  66 */     return new TaggingHandlerAdapter(codec, handler);
/*     */   }
/*     */ 
/*     */   public static ObjectHandler<StringTagging> chunkingToStringTagging(TagChunkCodec codec, ObjectHandler<Chunking> handler)
/*     */   {
/*  83 */     return new ChunkingHandlerAdapter(codec, handler);
/*     */   }
/*     */ 
/*     */   public static ObjectHandler<Tagging<String>> chunkingToTagging(TagChunkCodec codec, ObjectHandler<Chunking> handler)
/*     */   {
/* 101 */     return new ChunkingHandlerAdapterPad(codec, handler);
/*     */   }
/*     */ 
/*     */   static StringTagging pad(Tagging<String> tagging)
/*     */   {
/* 106 */     StringBuilder sb = new StringBuilder();
/* 107 */     int[] tokenStarts = new int[tagging.size()];
/* 108 */     int[] tokenEnds = new int[tagging.size()];
/* 109 */     for (int n = 0; n < tagging.size(); n++) {
/* 110 */       if (n > 0) sb.append(' ');
/* 111 */       tokenStarts[n] = sb.length();
/* 112 */       sb.append((String)tagging.token(n));
/* 113 */       tokenEnds[n] = sb.length();
/*     */     }
/* 115 */     return new StringTagging(tagging.tokens(), tagging.tags(), sb, tokenStarts, tokenEnds);
/*     */   }
/*     */ 
/*     */   static class ChunkingHandlerAdapterPad
/*     */     implements ObjectHandler<Tagging<String>>
/*     */   {
/*     */     private final TagChunkCodec mCodec;
/*     */     private final ObjectHandler<Chunking> mHandler;
/*     */ 
/*     */     public ChunkingHandlerAdapterPad(TagChunkCodec codec, ObjectHandler<Chunking> handler)
/*     */     {
/* 169 */       this.mCodec = codec;
/* 170 */       this.mHandler = handler;
/*     */     }
/*     */     public void handle(Tagging<String> tagging) {
/* 173 */       StringTagging stringTagging = TagChunkCodecAdapters.pad(tagging);
/* 174 */       Chunking chunking = this.mCodec.toChunking(stringTagging);
/* 175 */       this.mHandler.handle(chunking);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ChunkingHandlerAdapter
/*     */     implements ObjectHandler<StringTagging>
/*     */   {
/*     */     private final TagChunkCodec mCodec;
/*     */     private final ObjectHandler<Chunking> mHandler;
/*     */ 
/*     */     public ChunkingHandlerAdapter(TagChunkCodec codec, ObjectHandler<Chunking> handler)
/*     */     {
/* 155 */       this.mCodec = codec;
/* 156 */       this.mHandler = handler;
/*     */     }
/*     */     public void handle(StringTagging tagging) {
/* 159 */       Chunking chunking = this.mCodec.toChunking(tagging);
/* 160 */       this.mHandler.handle(chunking);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TaggingHandlerAdapter
/*     */     implements ObjectHandler<Chunking>
/*     */   {
/*     */     private final TagChunkCodec mCodec;
/*     */     private final ObjectHandler<Tagging<String>> mHandler;
/*     */ 
/*     */     public TaggingHandlerAdapter(TagChunkCodec codec, ObjectHandler<Tagging<String>> handler)
/*     */     {
/* 141 */       this.mCodec = codec;
/* 142 */       this.mHandler = handler;
/*     */     }
/*     */     public void handle(Chunking chunking) {
/* 145 */       Tagging tagging = this.mCodec.toTagging(chunking);
/* 146 */       this.mHandler.handle(tagging);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StringTaggingHandlerAdapter
/*     */     implements ObjectHandler<Chunking>
/*     */   {
/*     */     private final TagChunkCodec mCodec;
/*     */     private final ObjectHandler<StringTagging> mHandler;
/*     */ 
/*     */     public StringTaggingHandlerAdapter(TagChunkCodec codec, ObjectHandler<StringTagging> handler)
/*     */     {
/* 127 */       this.mCodec = codec;
/* 128 */       this.mHandler = handler;
/*     */     }
/*     */     public void handle(Chunking chunking) {
/* 131 */       StringTagging tagging = this.mCodec.toStringTagging(chunking);
/* 132 */       this.mHandler.handle(tagging);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.TagChunkCodecAdapters
 * JD-Core Version:    0.6.2
 */