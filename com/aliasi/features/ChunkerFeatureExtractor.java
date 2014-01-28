/*     */ package com.aliasi.features;
/*     */ 
/*     */ import com.aliasi.chunk.Chunk;
/*     */ import com.aliasi.chunk.Chunker;
/*     */ import com.aliasi.chunk.Chunking;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.FeatureExtractor;
/*     */ import com.aliasi.util.ObjectToDoubleMap;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ChunkerFeatureExtractor
/*     */   implements FeatureExtractor<CharSequence>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 6331037082723097532L;
/*     */   private final Chunker mChunker;
/*     */   private final boolean mIncludePhrase;
/*     */ 
/*     */   public ChunkerFeatureExtractor(Chunker chunker, boolean includePhrase)
/*     */   {
/*  86 */     this.mChunker = chunker;
/*  87 */     this.mIncludePhrase = includePhrase;
/*     */   }
/*     */ 
/*     */   public Map<String, ? extends Number> features(CharSequence in) {
/*  91 */     Chunking chunking = this.mChunker.chunk(in);
/*  92 */     Set chunkSet = chunking.chunkSet();
/*  93 */     if (chunkSet.isEmpty())
/*  94 */       return Collections.emptyMap();
/*  95 */     ObjectToDoubleMap features = new ObjectToDoubleMap();
/*  96 */     CharSequence text = chunking.charSequence();
/*  97 */     for (Chunk chunk : chunkSet) {
/*  98 */       String chunkType = chunk.type();
/*  99 */       if (!this.mIncludePhrase) {
/* 100 */         features.increment(chunkType, 1.0D);
/*     */       } else {
/* 102 */         StringBuilder sb = new StringBuilder(chunkType);
/* 103 */         sb.append('_');
/* 104 */         sb.append(text, chunk.start(), chunk.end());
/* 105 */         features.increment(sb.toString(), 1.0D);
/*     */       }
/*     */     }
/* 108 */     return features;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 112 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 1943280252018121446L;
/*     */     final ChunkerFeatureExtractor mExtractor;
/*     */ 
/* 119 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(ChunkerFeatureExtractor extractor) {
/* 122 */       this.mExtractor = extractor;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 128 */       Chunker chunker = (Chunker)in.readObject();
/* 129 */       boolean includePhrase = in.readBoolean();
/* 130 */       return new ChunkerFeatureExtractor(chunker, includePhrase);
/*     */     }
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 133 */       out.writeObject(this.mExtractor.mChunker);
/* 134 */       out.writeBoolean(this.mExtractor.mIncludePhrase);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.features.ChunkerFeatureExtractor
 * JD-Core Version:    0.6.2
 */