/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.tokenizer.TokenCategorizer;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TrainTokenShapeChunker
/*     */   implements ObjectHandler<Chunking>, Compilable
/*     */ {
/* 144 */   private final boolean mValidateTokenizer = false;
/*     */   private final int mKnownMinTokenCount;
/*     */   private final int mMinTokenCount;
/*     */   private final int mMinTagCount;
/*     */   private final TokenCategorizer mTokenCategorizer;
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final TrainableEstimator mTrainableEstimator;
/* 153 */   private final List<String> mTokenList = new ArrayList();
/* 154 */   private final List<String> mTagList = new ArrayList();
/*     */ 
/* 350 */   static final Chunk[] EMPTY_CHUNK_ARRAY = new Chunk[0];
/*     */ 
/*     */   public TrainTokenShapeChunker(TokenCategorizer categorizer, TokenizerFactory factory)
/*     */   {
/* 170 */     this(categorizer, factory, 8, 1, 1);
/*     */   }
/*     */ 
/*     */   public TrainTokenShapeChunker(TokenCategorizer categorizer, TokenizerFactory factory, int knownMinTokenCount, int minTokenCount, int minTagCount)
/*     */   {
/* 194 */     this.mTokenCategorizer = categorizer;
/* 195 */     this.mTokenizerFactory = factory;
/* 196 */     this.mKnownMinTokenCount = knownMinTokenCount;
/* 197 */     this.mMinTokenCount = minTokenCount;
/* 198 */     this.mMinTagCount = minTagCount;
/* 199 */     this.mTrainableEstimator = new TrainableEstimator(categorizer);
/*     */   }
/*     */ 
/*     */   void handle(String[] tokens, String[] whitespaces, String[] tags)
/*     */   {
/* 215 */     if (tokens.length != tags.length) {
/* 216 */       String msg = "Tokens and tags must be same length. Found tokens.length=" + tokens.length + " tags.length=" + tags.length;
/*     */ 
/* 219 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 222 */     for (int i = 0; i < tokens.length; i++) {
/* 223 */       if ((tokens[i] == null) || (tags[i] == null)) {
/* 224 */         String msg = "Tags and tokens must not be null. Found tokens[" + i + "]=" + tokens[i] + " tags[" + i + "]=" + tags[i];
/*     */ 
/* 227 */         throw new NullPointerException(msg);
/*     */       }
/* 229 */       this.mTokenList.add(tokens[i]);
/* 230 */       this.mTagList.add(tags[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void handle(Chunking chunking)
/*     */   {
/* 242 */     CharSequence cSeq = chunking.charSequence();
/* 243 */     char[] cs = Strings.toCharArray(cSeq);
/*     */ 
/* 245 */     Set chunkSet = chunking.chunkSet();
/* 246 */     Chunk[] chunks = (Chunk[])chunkSet.toArray(EMPTY_CHUNK_ARRAY);
/* 247 */     Arrays.sort(chunks, Chunk.TEXT_ORDER_COMPARATOR);
/*     */ 
/* 249 */     List tokenList = new ArrayList();
/* 250 */     List whiteList = new ArrayList();
/* 251 */     List tagList = new ArrayList();
/* 252 */     int pos = 0;
/* 253 */     for (Chunk nextChunk : chunks) {
/* 254 */       String type = nextChunk.type();
/* 255 */       int start = nextChunk.start();
/* 256 */       int end = nextChunk.end();
/* 257 */       outTag(cs, pos, start, tokenList, whiteList, tagList, this.mTokenizerFactory);
/* 258 */       chunkTag(cs, start, end, type, tokenList, whiteList, tagList, this.mTokenizerFactory);
/* 259 */       pos = end;
/*     */     }
/* 261 */     outTag(cs, pos, cSeq.length(), tokenList, whiteList, tagList, this.mTokenizerFactory);
/* 262 */     String[] toks = (String[])tokenList.toArray(Strings.EMPTY_STRING_ARRAY);
/* 263 */     String[] whites = (String[])whiteList.toArray(Strings.EMPTY_STRING_ARRAY);
/* 264 */     String[] tags = (String[])tagList.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 273 */     handle(toks, whites, tags);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 284 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   void replaceUnknownsWithCategories(String[] tokens)
/*     */   {
/* 342 */     ObjectToCounterMap counter = new ObjectToCounterMap();
/* 343 */     for (int i = 0; i < tokens.length; i++)
/* 344 */       counter.increment(tokens[i]);
/* 345 */     for (int i = 0; i < tokens.length; i++)
/* 346 */       if (counter.getCount(tokens[i]) < this.mKnownMinTokenCount)
/* 347 */         tokens[i] = this.mTokenCategorizer.categorize(tokens[i]);
/*     */   }
/*     */ 
/*     */   static void outTag(char[] cs, int start, int end, List<String> tokenList, List<String> whiteList, List<String> tagList, TokenizerFactory factory)
/*     */   {
/* 355 */     Tokenizer tokenizer = factory.tokenizer(cs, start, end - start);
/* 356 */     whiteList.add(tokenizer.nextWhitespace());
/*     */     String nextToken;
/* 358 */     while ((nextToken = tokenizer.nextToken()) != null) {
/* 359 */       tokenList.add(nextToken);
/* 360 */       tagList.add(ChunkTagHandlerAdapter2.OUT_TAG);
/* 361 */       whiteList.add(tokenizer.nextWhitespace());
/*     */     }
/*     */   }
/*     */ 
/*     */   static void chunkTag(char[] cs, int start, int end, String type, List<String> tokenList, List<String> whiteList, List<String> tagList, TokenizerFactory factory)
/*     */   {
/* 369 */     Tokenizer tokenizer = factory.tokenizer(cs, start, end - start);
/* 370 */     String firstToken = tokenizer.nextToken();
/* 371 */     tokenList.add(firstToken);
/* 372 */     tagList.add(ChunkTagHandlerAdapter2.BEGIN_TAG_PREFIX + type);
/*     */     while (true) {
/* 374 */       String nextWhitespace = tokenizer.nextWhitespace();
/* 375 */       String nextToken = tokenizer.nextToken();
/* 376 */       if (nextToken == null) break;
/* 377 */       tokenList.add(nextToken);
/* 378 */       whiteList.add(nextWhitespace);
/* 379 */       tagList.add(ChunkTagHandlerAdapter2.IN_TAG_PREFIX + type);
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean consistentTokens(String[] toks, String[] whitespaces, TokenizerFactory tokenizerFactory)
/*     */   {
/* 386 */     if (toks.length + 1 != whitespaces.length) return false;
/* 387 */     char[] cs = getChars(toks, whitespaces);
/* 388 */     Tokenizer tokenizer = tokenizerFactory.tokenizer(cs, 0, cs.length);
/* 389 */     String nextWhitespace = tokenizer.nextWhitespace();
/* 390 */     if (!whitespaces[0].equals(nextWhitespace)) {
/* 391 */       return false;
/*     */     }
/* 393 */     for (int i = 0; i < toks.length; i++) {
/* 394 */       String token = tokenizer.nextToken();
/* 395 */       if (token == null) {
/* 396 */         return false;
/*     */       }
/* 398 */       if (!toks[i].equals(token)) {
/* 399 */         return false;
/*     */       }
/* 401 */       nextWhitespace = tokenizer.nextWhitespace();
/* 402 */       if (!whitespaces[(i + 1)].equals(nextWhitespace)) {
/* 403 */         return false;
/*     */       }
/*     */     }
/* 406 */     return true;
/*     */   }
/*     */ 
/*     */   List<String> tokenization(String[] toks, String[] whitespaces) {
/* 410 */     List tokList = new ArrayList();
/* 411 */     List whiteList = new ArrayList();
/* 412 */     char[] cs = getChars(toks, whitespaces);
/* 413 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 414 */     tokenizer.tokenize(tokList, whiteList);
/* 415 */     return tokList;
/*     */   }
/*     */ 
/*     */   static char[] getChars(String[] toks, String[] whitespaces) {
/* 419 */     StringBuilder sb = new StringBuilder();
/* 420 */     for (int i = 0; i < toks.length; i++) {
/* 421 */       sb.append(whitespaces[i]);
/* 422 */       sb.append(toks[i]);
/*     */     }
/* 424 */     sb.append(whitespaces[(whitespaces.length - 1)]);
/* 425 */     return Strings.toCharArray(sb);
/*     */   }
/*     */ 
/*     */   static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = 142720610674437597L;
/*     */     final TrainTokenShapeChunker mChunker;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 291 */       this(null);
/*     */     }
/*     */     public Externalizer(TrainTokenShapeChunker chunker) {
/* 294 */       this.mChunker = chunker;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 300 */       TokenizerFactory factory = (TokenizerFactory)in.readObject();
/* 301 */       TokenCategorizer categorizer = (TokenCategorizer)in.readObject();
/*     */ 
/* 306 */       CompiledEstimator estimator = (CompiledEstimator)in.readObject();
/*     */ 
/* 308 */       TokenShapeDecoder decoder = new TokenShapeDecoder(estimator, categorizer, 1000.0D);
/*     */ 
/* 311 */       return new TokenShapeChunker(factory, decoder);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 315 */       int len = this.mChunker.mTagList.size();
/* 316 */       String[] tokens = (String[])this.mChunker.mTokenList.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 318 */       String[] tags = (String[])this.mChunker.mTagList.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 322 */       this.mChunker.mTrainableEstimator.handle(tokens, tags);
/*     */ 
/* 325 */       this.mChunker.replaceUnknownsWithCategories(tokens);
/* 326 */       this.mChunker.mTrainableEstimator.handle(tokens, tags);
/* 327 */       this.mChunker.mTrainableEstimator.prune(this.mChunker.mMinTagCount, this.mChunker.mMinTokenCount);
/*     */ 
/* 330 */       this.mChunker.mTrainableEstimator.smoothTags(1);
/*     */ 
/* 333 */       AbstractExternalizable.compileOrSerialize(this.mChunker.mTokenizerFactory, objOut);
/* 334 */       AbstractExternalizable.compileOrSerialize(this.mChunker.mTokenCategorizer, objOut);
/* 335 */       this.mChunker.mTrainableEstimator.compileTo(objOut);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.TrainTokenShapeChunker
 * JD-Core Version:    0.6.2
 */