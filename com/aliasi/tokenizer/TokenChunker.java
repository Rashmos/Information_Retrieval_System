/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.chunk.Chunk;
/*     */ import com.aliasi.chunk.ChunkFactory;
/*     */ import com.aliasi.chunk.Chunker;
/*     */ import com.aliasi.chunk.Chunking;
/*     */ import com.aliasi.chunk.ChunkingImpl;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TokenChunker
/*     */   implements Chunker, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -6559339721653291504L;
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */ 
/*     */   public TokenChunker(TokenizerFactory tokenizerFactory)
/*     */   {
/*  70 */     this.mTokenizerFactory = tokenizerFactory;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/*  79 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/*  91 */     char[] cs = Strings.toCharArray(cSeq);
/*  92 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/* 106 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/* 107 */     ChunkingImpl chunking = new ChunkingImpl(cs, start, end);
/*     */     String token;
/* 109 */     while ((token = tokenizer.nextToken()) != null) {
/* 110 */       int chunkStart = tokenizer.lastTokenStartPosition();
/* 111 */       int chunkEnd = tokenizer.lastTokenEndPosition();
/* 112 */       Chunk chunk = ChunkFactory.createChunk(chunkStart, chunkEnd, token);
/* 113 */       chunking.add(chunk);
/*     */     }
/* 115 */     return chunking;
/*     */   }
/*     */   static class Serializer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 5541846439684999440L;
/*     */     private final TokenChunker mChunker;
/*     */ 
/* 122 */     public Serializer(TokenChunker chunker) { this.mChunker = chunker; }
/*     */ 
/*     */     public Serializer() {
/* 125 */       this(null);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 129 */       out.writeObject(this.mChunker.tokenizerFactory());
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 135 */       TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/*     */ 
/* 137 */       return new TokenChunker(tokenizerFactory);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.TokenChunker
 * JD-Core Version:    0.6.2
 */