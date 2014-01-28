/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TokenShapeChunker
/*     */   implements Chunker
/*     */ {
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final TokenShapeDecoder mDecoder;
/*     */ 
/*     */   TokenShapeChunker(TokenizerFactory tf, TokenShapeDecoder decoder)
/*     */   {
/*  62 */     this.mTokenizerFactory = tf;
/*  63 */     this.mDecoder = decoder;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/*  79 */     char[] cs = Strings.toCharArray(cSeq);
/*  80 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/*  97 */     List tokenList = new ArrayList();
/*  98 */     List whiteList = new ArrayList();
/*     */ 
/* 100 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/*     */ 
/* 102 */     tokenizer.tokenize(tokenList, whiteList);
/*     */ 
/* 104 */     ChunkingImpl chunking = new ChunkingImpl(cs, start, end);
/*     */ 
/* 106 */     if (tokenList.size() == 0) return chunking;
/*     */ 
/* 108 */     String[] tokens = (String[])tokenList.toArray(Strings.EMPTY_STRING_ARRAY);
/* 109 */     String[] whites = (String[])whiteList.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 111 */     int[] tokenStarts = new int[tokens.length];
/* 112 */     int[] tokenEnds = new int[tokens.length];
/*     */ 
/* 114 */     int pos = whites[0].length();
/* 115 */     for (int i = 0; i < tokens.length; i++) {
/* 116 */       tokenStarts[i] = pos;
/* 117 */       pos += tokens[i].length();
/* 118 */       tokenEnds[i] = pos;
/* 119 */       pos += whites[(i + 1)].length();
/*     */     }
/*     */ 
/* 122 */     String[] tags = this.mDecoder.decodeTags(tokens);
/* 123 */     if (tags.length < 1) return chunking;
/*     */ 
/* 125 */     int neStartIdx = -1;
/* 126 */     int neEndIdx = -1;
/* 127 */     String neTag = "O";
/*     */ 
/* 129 */     for (int i = 0; i < tags.length; i++) {
/* 130 */       if (!tags[i].equals(neTag)) {
/* 131 */         if (!Tags.isOutTag(neTag)) {
/* 132 */           Chunk chunk = ChunkFactory.createChunk(neStartIdx, neEndIdx, Tags.baseTag(neTag));
/*     */ 
/* 136 */           chunking.add(chunk);
/*     */         }
/* 138 */         neTag = Tags.toInnerTag(tags[i]);
/* 139 */         neStartIdx = tokenStarts[i];
/*     */       }
/* 141 */       neEndIdx = tokenEnds[i];
/*     */     }
/*     */ 
/* 144 */     if (!Tags.isOutTag(neTag)) {
/* 145 */       Chunk chunk = ChunkFactory.createChunk(neStartIdx, neEndIdx, Tags.baseTag(neTag));
/*     */ 
/* 148 */       chunking.add(chunk);
/*     */     }
/* 150 */     return chunking;
/*     */   }
/*     */ 
/*     */   public void setLog2Beam(double beamWidth)
/*     */   {
/* 164 */     if ((beamWidth <= 0.0D) || (Double.isNaN(beamWidth))) {
/* 165 */       String msg = "Require beam width to be positive. Found beamWidth=" + beamWidth;
/*     */ 
/* 167 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 169 */     this.mDecoder.setLog2Beam(beamWidth);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.TokenShapeChunker
 * JD-Core Version:    0.6.2
 */