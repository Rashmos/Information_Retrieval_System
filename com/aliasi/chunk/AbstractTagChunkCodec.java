/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.tag.StringTagging;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ abstract class AbstractTagChunkCodec
/*     */   implements TagChunkCodec
/*     */ {
/*     */   final TokenizerFactory mTokenizerFactory;
/*     */   final boolean mEnforceConsistency;
/*     */ 
/*     */   public AbstractTagChunkCodec()
/*     */   {
/*  58 */     this(null, false);
/*     */   }
/*     */ 
/*     */   public AbstractTagChunkCodec(TokenizerFactory tokenizerFactory, boolean enforceConsistency)
/*     */   {
/*  63 */     this.mTokenizerFactory = tokenizerFactory;
/*  64 */     this.mEnforceConsistency = enforceConsistency;
/*     */   }
/*     */ 
/*     */   public boolean enforceConsistency()
/*     */   {
/*  78 */     return this.mEnforceConsistency;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/*  88 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public boolean isEncodable(Chunking chunking)
/*     */   {
/* 107 */     return isEncodable(chunking, null);
/*     */   }
/*     */ 
/*     */   public boolean isDecodable(StringTagging tagging)
/*     */   {
/* 121 */     return isDecodable(tagging, null);
/*     */   }
/*     */ 
/*     */   boolean isEncodable(Chunking chunking, StringBuilder sb) {
/* 125 */     if (this.mTokenizerFactory == null) {
/* 126 */       String msg = "Tokenizer factory must be non-null to support encodability test.";
/* 127 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 129 */     Set chunkSet = chunking.chunkSet();
/* 130 */     if (chunkSet.size() == 0) return true;
/* 131 */     Chunk[] chunks = (Chunk[])chunkSet.toArray(new Chunk[chunkSet.size()]);
/* 132 */     Arrays.sort(chunks, Chunk.TEXT_ORDER_COMPARATOR);
/* 133 */     int lastEnd = chunks[0].end();
/* 134 */     for (int i = 1; i < chunks.length; i++) {
/* 135 */       if (chunks[i].start() < lastEnd) {
/* 136 */         if (sb != null) {
/* 137 */           sb.append("Chunks must not overlap. chunk=" + chunks[(i - 1)] + " chunk=" + chunks[i]);
/*     */         }
/*     */ 
/* 141 */         return false;
/*     */       }
/* 143 */       lastEnd = chunks[i].end();
/*     */     }
/* 145 */     char[] cs = Strings.toCharArray(chunking.charSequence());
/* 146 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 147 */     int chunkPos = 0;
/* 148 */     boolean chunkStarted = false;
/*     */     String token;
/* 150 */     while ((chunkPos < chunks.length) && ((token = tokenizer.nextToken()) != null)) {
/* 151 */       int tokenStart = tokenizer.lastTokenStartPosition();
/* 152 */       if (tokenStart == chunks[chunkPos].start())
/* 153 */         chunkStarted = true;
/* 154 */       int tokenEnd = tokenizer.lastTokenEndPosition();
/* 155 */       if (tokenEnd == chunks[chunkPos].end()) {
/* 156 */         if (!chunkStarted) {
/* 157 */           if (sb != null) {
/* 158 */             sb.append("Chunks must start on token boundaries. Illegal chunk=" + chunks[chunkPos]);
/*     */           }
/* 160 */           return false;
/*     */         }
/* 162 */         chunkPos++;
/* 163 */         chunkStarted = false;
/*     */       }
/*     */     }
/* 166 */     if (chunkPos < chunks.length) {
/* 167 */       if (sb != null) {
/* 168 */         sb.append("Chunk beyond last token. chunk=" + chunks[chunkPos]);
/*     */       }
/* 170 */       return false;
/*     */     }
/* 172 */     return true;
/*     */   }
/*     */ 
/*     */   boolean isDecodable(StringTagging tagging, StringBuilder sb) {
/* 176 */     if (this.mTokenizerFactory == null) {
/* 177 */       String msg = "Tokenizer factory must be non-null to support decodability test.";
/* 178 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 180 */     if (!legalTags((String[])tagging.tags().toArray(Strings.EMPTY_STRING_ARRAY))) {
/* 181 */       sb.append("Illegal tags=" + tagging.tags());
/* 182 */       return false;
/*     */     }
/* 184 */     char[] cs = Strings.toCharArray(tagging.characters());
/* 185 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 186 */     for (int n = 0; n < tagging.size(); n++) {
/* 187 */       String nextToken = tokenizer.nextToken();
/* 188 */       if (nextToken == null) {
/* 189 */         if (sb != null)
/* 190 */           sb.append("More tags than tokens.");
/* 191 */         return false;
/*     */       }
/* 193 */       if (tagging.tokenStart(n) != tokenizer.lastTokenStartPosition()) {
/* 194 */         if (sb != null) {
/* 195 */           sb.append("Tokens must start/end in tagging to match tokenizer. Found token " + n + " from tokenizer=" + nextToken + " tokenizer.lastTokenStartPosition()=" + tokenizer.lastTokenStartPosition() + " tagging.tokenStart(" + n + ")=" + tagging.tokenStart(n));
/*     */         }
/*     */ 
/* 202 */         return false;
/*     */       }
/* 204 */       if (tagging.tokenEnd(n) != tokenizer.lastTokenEndPosition()) {
/* 205 */         if (sb != null) {
/* 206 */           sb.append("Tokens must start/end in tagging to match tokenizer. Found token " + n + " from tokenizer=" + nextToken + " tokenizer.lastTokenEndPosition()=" + tokenizer.lastTokenEndPosition() + " tagging.tokenEnd(" + n + ")=" + tagging.tokenEnd(n));
/*     */         }
/*     */ 
/* 213 */         return false;
/*     */       }
/*     */     }
/* 216 */     String excessToken = tokenizer.nextToken();
/* 217 */     if ((excessToken != null) && 
/* 218 */       (sb != null)) {
/* 219 */       sb.append("Extra token from tokenizer beyond tagging. token=" + excessToken + " startPosition=" + tokenizer.lastTokenStartPosition() + " endPosition=" + tokenizer.lastTokenEndPosition());
/*     */     }
/*     */ 
/* 224 */     return true;
/*     */   }
/*     */ 
/*     */   void enforceConsistency(StringTagging tagging) {
/* 228 */     if (!this.mEnforceConsistency) return;
/* 229 */     StringBuilder sb = new StringBuilder();
/* 230 */     if (isDecodable(tagging, sb)) return;
/* 231 */     throw new IllegalArgumentException(sb.toString());
/*     */   }
/*     */ 
/*     */   void enforceConsistency(Chunking chunking) {
/* 235 */     if (!this.mEnforceConsistency) return;
/* 236 */     StringBuilder sb = new StringBuilder();
/* 237 */     if (isEncodable(chunking, sb)) return;
/* 238 */     throw new IllegalArgumentException(sb.toString());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.AbstractTagChunkCodec
 * JD-Core Version:    0.6.2
 */