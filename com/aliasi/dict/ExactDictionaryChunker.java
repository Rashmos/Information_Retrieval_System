/*     */ package com.aliasi.dict;
/*     */ 
/*     */ import com.aliasi.chunk.Chunk;
/*     */ import com.aliasi.chunk.ChunkFactory;
/*     */ import com.aliasi.chunk.Chunker;
/*     */ import com.aliasi.chunk.Chunking;
/*     */ import com.aliasi.chunk.ChunkingImpl;
/*     */ import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ExactDictionaryChunker
/*     */   implements Chunker
/*     */ {
/*     */   final TrieNode mTrieRootNode;
/*     */   final TokenizerFactory mTokenizerFactory;
/*     */   final boolean mCaseSensitive;
/*     */   boolean mReturnAllMatches;
/* 142 */   int mMaxPhraseLength = 0;
/*     */ 
/* 415 */   static ScoredCat[] EMPTY_SCORED_CATS = new ScoredCat[0];
/*     */ 
/* 539 */   static final Chunk[] EMPTY_CHUNK_ARRAY = new Chunk[0];
/*     */ 
/*     */   public ExactDictionaryChunker(Dictionary<String> dict, TokenizerFactory factory)
/*     */   {
/* 159 */     this(dict, factory, true, true);
/*     */   }
/*     */ 
/*     */   public ExactDictionaryChunker(Dictionary<String> dict, TokenizerFactory factory, boolean returnAllMatches, boolean caseSensitive)
/*     */   {
/* 188 */     this.mTokenizerFactory = factory;
/* 189 */     this.mReturnAllMatches = returnAllMatches;
/* 190 */     this.mCaseSensitive = caseSensitive;
/* 191 */     this.mTrieRootNode = compileTrie(dict);
/*     */   }
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/* 204 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public boolean caseSensitive()
/*     */   {
/* 215 */     return this.mCaseSensitive;
/*     */   }
/*     */ 
/*     */   public boolean returnAllMatches()
/*     */   {
/* 224 */     return this.mReturnAllMatches;
/*     */   }
/*     */ 
/*     */   public void setReturnAllMatches(boolean returnAllMatches)
/*     */   {
/* 237 */     this.mReturnAllMatches = returnAllMatches;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/* 250 */     char[] cs = Strings.toCharArray(cSeq);
/* 251 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   final Tokenizer tokenizer(char[] cs, int start, int end)
/*     */   {
/* 256 */     TokenizerFactory factory = this.mCaseSensitive ? this.mTokenizerFactory : new LowerCaseTokenizerFactory(this.mTokenizerFactory);
/*     */ 
/* 260 */     return factory.tokenizer(cs, start, end - start);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/* 275 */     ChunkingImpl chunking = new ChunkingImpl(cs, start, end);
/* 276 */     if (this.mMaxPhraseLength == 0)
/* 277 */       return chunking;
/* 278 */     CircularQueueInt queue = new CircularQueueInt(this.mMaxPhraseLength);
/* 279 */     Tokenizer tokenizer = tokenizer(cs, start, end);
/* 280 */     TrieNode node = this.mTrieRootNode;
/*     */     String token;
/* 282 */     while ((token = tokenizer.nextToken()) != null) {
/* 283 */       int tokenStartPos = tokenizer.lastTokenStartPosition();
/* 284 */       int tokenEndPos = tokenizer.lastTokenEndPosition();
/*     */ 
/* 286 */       queue.enqueue(tokenStartPos);
/*     */       while (true) {
/* 288 */         TrieNode daughterNode = node.getDaughter(token);
/* 289 */         if (daughterNode != null) {
/* 290 */           node = daughterNode;
/* 291 */           break;
/*     */         }
/* 293 */         if (node.mSuffixNode == null) {
/* 294 */           node = this.mTrieRootNode.getDaughter(token);
/* 295 */           if (node != null) break;
/* 296 */           node = this.mTrieRootNode; break;
/*     */         }
/*     */ 
/* 299 */         node = node.mSuffixNode;
/*     */       }
/* 301 */       emit(node, queue, tokenEndPos, chunking);
/* 302 */       for (TrieNode suffixNode = node.mSuffixNodeWithCategory; 
/* 303 */         suffixNode != null; 
/* 304 */         suffixNode = suffixNode.mSuffixNodeWithCategory) {
/* 305 */         emit(suffixNode, queue, tokenEndPos, chunking);
/*     */       }
/*     */     }
/* 308 */     return this.mReturnAllMatches ? chunking : restrictToLongest(chunking);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 323 */     StringBuilder sb = new StringBuilder();
/* 324 */     sb.append("ExactDictionaryChunker\n");
/* 325 */     sb.append("Tokenizer factory=" + this.mTokenizerFactory.getClass() + "\n");
/* 326 */     sb.append("(toString) mCaseSensitive=" + this.mCaseSensitive + "\n");
/* 327 */     sb.append("Return all matches=" + this.mReturnAllMatches + "\n\n");
/* 328 */     this.mTrieRootNode.toString(sb, 0);
/* 329 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   void emit(TrieNode node, CircularQueueInt queue, int end, ChunkingImpl chunking)
/*     */   {
/* 334 */     ScoredCat[] scoredCats = node.mCategories;
/* 335 */     for (int i = 0; i < scoredCats.length; i++) {
/* 336 */       int start = queue.get(node.depth());
/* 337 */       String type = scoredCats[i].mCat;
/* 338 */       double score = scoredCats[i].mScore;
/* 339 */       Chunk chunk = ChunkFactory.createChunk(start, end, type, score);
/* 340 */       chunking.add(chunk);
/*     */     }
/*     */   }
/*     */ 
/*     */   final TrieNode compileTrie(Dictionary<String> dict) {
/* 345 */     TrieNode rootNode = new TrieNode(0);
/* 346 */     for (DictionaryEntry entry : dict) {
/* 347 */       String phrase = entry.phrase();
/* 348 */       char[] cs = phrase.toCharArray();
/* 349 */       Tokenizer tokenizer = tokenizer(cs, 0, cs.length);
/* 350 */       int length = rootNode.add(tokenizer, entry);
/* 351 */       if (length > this.mMaxPhraseLength)
/* 352 */         this.mMaxPhraseLength = length;
/*     */     }
/* 354 */     computeSuffixes(rootNode, rootNode, new String[this.mMaxPhraseLength], 0);
/* 355 */     return rootNode;
/*     */   }
/*     */ 
/*     */   final void computeSuffixes(TrieNode node, TrieNode rootNode, String[] tokens, int length)
/*     */   {
/* 360 */     for (int i = 1; i < length; i++) {
/* 361 */       TrieNode suffixNode = rootNode.getDaughter(tokens, i, length);
/* 362 */       if (suffixNode != null) {
/* 363 */         node.mSuffixNode = suffixNode;
/* 364 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 368 */     for (int i = 1; i < length; i++) {
/* 369 */       TrieNode suffixNode = rootNode.getDaughter(tokens, i, length);
/* 370 */       if ((suffixNode != null) && 
/* 371 */         (suffixNode.mCategories.length != 0)) {
/* 372 */         node.mSuffixNodeWithCategory = suffixNode;
/* 373 */         break;
/*     */       }
/*     */     }
/* 375 */     if (node.mDaughterMap == null) return;
/* 376 */     for (Map.Entry entry : node.mDaughterMap.entrySet()) {
/* 377 */       tokens[length] = ((String)entry.getKey()).toString();
/* 378 */       TrieNode dtrNode = (TrieNode)entry.getValue();
/* 379 */       computeSuffixes(dtrNode, rootNode, tokens, length + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Chunking restrictToLongest(Chunking chunking) {
/* 384 */     ChunkingImpl result = new ChunkingImpl(chunking.charSequence());
/* 385 */     Set chunkSet = chunking.chunkSet();
/* 386 */     if (chunkSet.size() == 0) return chunking;
/* 387 */     Chunk[] chunks = (Chunk[])chunkSet.toArray(EMPTY_CHUNK_ARRAY);
/* 388 */     Arrays.sort(chunks, Chunk.LONGEST_MATCH_ORDER_COMPARATOR);
/* 389 */     int lastEnd = -1;
/* 390 */     for (int i = 0; i < chunks.length; i++) {
/* 391 */       if (chunks[i].start() >= lastEnd) {
/* 392 */         result.add(chunks[i]);
/* 393 */         lastEnd = chunks[i].end();
/*     */       }
/*     */     }
/* 396 */     return result;
/*     */   }
/*     */ 
/*     */   static class CircularQueueInt
/*     */   {
/*     */     final int[] mQueue;
/* 520 */     int mNextPos = 0;
/*     */ 
/* 522 */     public CircularQueueInt(int size) { this.mQueue = new int[size];
/* 523 */       Arrays.fill(this.mQueue, 0); }
/*     */ 
/*     */     public void enqueue(int val) {
/* 526 */       this.mQueue[this.mNextPos] = val;
/* 527 */       if (++this.mNextPos == this.mQueue.length)
/* 528 */         this.mNextPos = 0;
/*     */     }
/*     */ 
/*     */     public int get(int offset) {
/* 532 */       int pos = this.mNextPos - offset;
/* 533 */       if (pos < 0)
/* 534 */         pos += this.mQueue.length;
/* 535 */       return this.mQueue[pos];
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TrieNode
/*     */   {
/*     */     int mDepth;
/* 420 */     Map<String, TrieNode> mDaughterMap = null;
/*     */ 
/* 422 */     ExactDictionaryChunker.ScoredCat[] mCategories = ExactDictionaryChunker.EMPTY_SCORED_CATS;
/*     */     TrieNode mSuffixNode;
/*     */     TrieNode mSuffixNodeWithCategory;
/*     */ 
/*     */     TrieNode(int depth)
/*     */     {
/* 428 */       this.mDepth = depth;
/*     */     }
/*     */ 
/*     */     public int depth() {
/* 432 */       return this.mDepth;
/*     */     }
/*     */ 
/*     */     public void addEntry(DictionaryEntry<String> entry)
/*     */     {
/* 437 */       ExactDictionaryChunker.ScoredCat[] newCats = new ExactDictionaryChunker.ScoredCat[this.mCategories.length + 1];
/* 438 */       System.arraycopy(this.mCategories, 0, newCats, 0, this.mCategories.length);
/* 439 */       newCats[(newCats.length - 1)] = new ExactDictionaryChunker.ScoredCat(((String)entry.category()).toString(), entry.score());
/*     */ 
/* 441 */       Arrays.sort(newCats, ScoredObject.reverseComparator());
/* 442 */       this.mCategories = newCats;
/*     */     }
/*     */ 
/*     */     public TrieNode getDaughter(String[] tokens, int start, int end) {
/* 446 */       TrieNode node = this;
/* 447 */       for (int i = start; (i < end) && (node != null); i++)
/* 448 */         node = node.getDaughter(tokens[i]);
/* 449 */       return node;
/*     */     }
/*     */ 
/*     */     public TrieNode getDaughter(String token) {
/* 453 */       return this.mDaughterMap == null ? null : (TrieNode)this.mDaughterMap.get(token);
/*     */     }
/*     */ 
/*     */     public TrieNode getOrCreateDaughter(String token)
/*     */     {
/* 458 */       TrieNode existingDaughter = getDaughter(token);
/* 459 */       if (existingDaughter != null) return existingDaughter;
/* 460 */       TrieNode newDaughter = new TrieNode(depth() + 1);
/* 461 */       if (this.mDaughterMap == null)
/* 462 */         this.mDaughterMap = new HashMap(2);
/* 463 */       this.mDaughterMap.put(token, newDaughter);
/* 464 */       return newDaughter;
/*     */     }
/*     */ 
/*     */     public int add(Tokenizer tokenizer, DictionaryEntry<String> entry) {
/* 468 */       TrieNode node = this;
/*     */       String token;
/* 470 */       while ((token = tokenizer.nextToken()) != null)
/* 471 */         node = node.getOrCreateDaughter(token);
/* 472 */       node.addEntry(entry);
/* 473 */       return node.depth();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 478 */       StringBuilder sb = new StringBuilder();
/* 479 */       toString(sb, 0);
/* 480 */       return sb.toString();
/*     */     }
/*     */     String id() {
/* 483 */       return this.mDepth + ":" + Integer.toHexString(hashCode());
/*     */     }
/*     */     void toString(StringBuilder sb, int depth) {
/* 486 */       indent(sb, depth);
/* 487 */       sb.append(id());
/* 488 */       for (int i = 0; i < this.mCategories.length; i++) {
/* 489 */         indent(sb, depth);
/* 490 */         sb.append("cat " + i + "=" + this.mCategories[i]);
/*     */       }
/* 492 */       if (this.mSuffixNode != null) {
/* 493 */         indent(sb, depth);
/* 494 */         sb.append("suffixNode=");
/* 495 */         sb.append(this.mSuffixNode.id());
/*     */       }
/* 497 */       if (this.mSuffixNodeWithCategory != null) {
/* 498 */         indent(sb, depth);
/* 499 */         sb.append("suffixNodeWithCat=");
/* 500 */         sb.append(this.mSuffixNodeWithCategory.id());
/*     */       }
/* 502 */       if (this.mDaughterMap == null) return;
/* 503 */       for (String token : this.mDaughterMap.keySet()) {
/* 504 */         indent(sb, depth);
/* 505 */         sb.append(token);
/* 506 */         getDaughter(token).toString(sb, depth + 1);
/*     */       }
/*     */     }
/*     */ 
/*     */     static void indent(StringBuilder sb, int depth) {
/* 511 */       sb.append("\n");
/* 512 */       for (int i = 0; i < depth; i++)
/* 513 */         sb.append("  ");
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ScoredCat
/*     */     implements Scored
/*     */   {
/*     */     String mCat;
/*     */     double mScore;
/*     */ 
/*     */     ScoredCat(String cat, double score)
/*     */     {
/* 403 */       this.mCat = cat;
/* 404 */       this.mScore = score;
/*     */     }
/*     */     public double score() {
/* 407 */       return this.mScore;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 411 */       return this.mCat + ":" + this.mScore;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.ExactDictionaryChunker
 * JD-Core Version:    0.6.2
 */