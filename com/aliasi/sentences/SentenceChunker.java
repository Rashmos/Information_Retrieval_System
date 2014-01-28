/*     */ package com.aliasi.sentences;
/*     */ 
/*     */ import com.aliasi.chunk.Chunk;
/*     */ import com.aliasi.chunk.ChunkFactory;
/*     */ import com.aliasi.chunk.Chunker;
/*     */ import com.aliasi.chunk.Chunking;
/*     */ import com.aliasi.chunk.ChunkingImpl;
/*     */ import com.aliasi.tokenizer.Tokenization;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SentenceChunker
/*     */   implements Chunker, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 2296001471469838378L;
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final SentenceModel mSentenceModel;
/*     */   public static final String SENTENCE_CHUNK_TYPE = "S";
/*     */ 
/*     */   public SentenceChunker(TokenizerFactory tf, SentenceModel sm)
/*     */   {
/*  84 */     this.mTokenizerFactory = tf;
/*  85 */     this.mSentenceModel = sm;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/*  94 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public SentenceModel sentenceModel()
/*     */   {
/* 103 */     return this.mSentenceModel;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/* 123 */     char[] cs = Strings.toCharArray(cSeq);
/* 124 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/* 138 */     Tokenization toks = new Tokenization(cs, start, end - start, this.mTokenizerFactory);
/*     */ 
/* 140 */     String[] tokens = toks.tokens();
/* 141 */     String[] whitespaces = toks.whitespaces();
/*     */ 
/* 143 */     ChunkingImpl chunking = new ChunkingImpl(cs, start, end);
/*     */ 
/* 145 */     if (tokens.length == 0) return chunking;
/*     */ 
/* 147 */     int[] sentenceBoundaries = this.mSentenceModel.boundaryIndices(tokens, whitespaces);
/*     */ 
/* 149 */     if (sentenceBoundaries.length < 1) return chunking;
/*     */ 
/* 151 */     int startToken = 0;
/* 152 */     for (int i = 0; i < sentenceBoundaries.length; i++) {
/* 153 */       int endToken = sentenceBoundaries[i];
/* 154 */       Chunk chunk = ChunkFactory.createChunk(toks.tokenStart(startToken), toks.tokenEnd(endToken), "S");
/*     */ 
/* 158 */       chunking.add(chunk);
/* 159 */       startToken = endToken + 1;
/*     */     }
/* 161 */     return chunking;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 165 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -8566130480755448404L;
/*     */     final SentenceChunker mChunker;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 181 */       this(null);
/*     */     }
/*     */     public Serializer(SentenceChunker chunker) {
/* 184 */       this.mChunker = chunker;
/*     */     }
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 187 */       out.writeObject(this.mChunker.mTokenizerFactory);
/* 188 */       out.writeObject(this.mChunker.mSentenceModel);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 194 */       TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/*     */ 
/* 197 */       SentenceModel sentenceModel = (SentenceModel)in.readObject();
/*     */ 
/* 199 */       return new SentenceChunker(tokenizerFactory, sentenceModel);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.sentences.SentenceChunker
 * JD-Core Version:    0.6.2
 */