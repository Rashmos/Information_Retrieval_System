/*     */ package com.aliasi.dict;
/*     */ 
/*     */ import com.aliasi.chunk.Chunk;
/*     */ import com.aliasi.chunk.ChunkFactory;
/*     */ import com.aliasi.chunk.Chunker;
/*     */ import com.aliasi.chunk.Chunking;
/*     */ import com.aliasi.chunk.ChunkingImpl;
/*     */ import com.aliasi.spell.WeightedEditDistance;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ApproxDictionaryChunker
/*     */   implements Chunker, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 5364907367744655793L;
/*     */   private final TrieDictionary<String> mDictionary;
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final WeightedEditDistance mEditDistance;
/*     */   private double mDistanceThreshold;
/* 448 */   public static final WeightedEditDistance TT_DISTANCE = new TTDistance();
/*     */ 
/*     */   public ApproxDictionaryChunker(TrieDictionary<String> dictionary, TokenizerFactory tokenizerFactory, WeightedEditDistance editDistance, double distanceThreshold)
/*     */   {
/* 149 */     this.mDictionary = dictionary;
/* 150 */     this.mTokenizerFactory = tokenizerFactory;
/* 151 */     this.mEditDistance = editDistance;
/* 152 */     this.mDistanceThreshold = distanceThreshold;
/*     */   }
/*     */ 
/*     */   public TrieDictionary<String> dictionary()
/*     */   {
/* 164 */     return this.mDictionary;
/*     */   }
/*     */ 
/*     */   public WeightedEditDistance editDistance()
/*     */   {
/* 175 */     return this.mEditDistance;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory tokenizerFactory()
/*     */   {
/* 187 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public double distanceThreshold()
/*     */   {
/* 198 */     return this.mDistanceThreshold;
/*     */   }
/*     */ 
/*     */   public void setMaxDistance(double distanceThreshold)
/*     */   {
/* 207 */     this.mDistanceThreshold = distanceThreshold;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/* 218 */     char[] cs = Strings.toCharArray(cSeq);
/* 219 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/* 234 */     int length = end - start;
/*     */ 
/* 237 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, length);
/* 238 */     boolean[] startTokens = new boolean[length];
/* 239 */     boolean[] endTokens = new boolean[length + 1];
/* 240 */     Arrays.fill(startTokens, false);
/* 241 */     Arrays.fill(endTokens, false);
/*     */     String token;
/* 243 */     while ((token = tokenizer.nextToken()) != null) {
/* 244 */       int lastStart = tokenizer.lastTokenStartPosition();
/* 245 */       startTokens[lastStart] = true;
/* 246 */       endTokens[(lastStart + token.length())] = true;
/*     */     }
/*     */ 
/* 249 */     Map dpToChunk = new HashMap();
/* 250 */     Map queue = new HashMap();
/* 251 */     for (int i = 0; i < length; i++) {
/* 252 */       int startPlusI = start + i;
/* 253 */       char c = cs[startPlusI];
/* 254 */       if (startTokens[i] != 0) {
/* 255 */         add(queue, this.mDictionary.mRootNode, startPlusI, 0.0D, false, dpToChunk, cs, startPlusI);
/*     */       }
/*     */ 
/* 259 */       Map nextQueue = new HashMap();
/* 260 */       double deleteCost = -this.mEditDistance.deleteWeight(c);
/* 261 */       for (SearchState state : queue.values())
/*     */       {
/* 264 */         add(nextQueue, state.mNode, state.mStartIndex, state.mScore + deleteCost, endTokens[(i + 1)], dpToChunk, cs, startPlusI);
/*     */ 
/* 269 */         char[] dtrChars = state.mNode.mDtrChars;
/* 270 */         Node[] dtrNodes = state.mNode.mDtrNodes;
/* 271 */         for (int j = 0; j < dtrChars.length; j++) {
/* 272 */           add(nextQueue, dtrNodes[j], state.mStartIndex, state.mScore - (dtrChars[j] == c ? this.mEditDistance.matchWeight(dtrChars[j]) : this.mEditDistance.substituteWeight(dtrChars[j], c)), endTokens[(i + 1)], dpToChunk, cs, startPlusI);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 281 */       queue = nextQueue;
/*     */     }
/* 283 */     ChunkingImpl result = new ChunkingImpl(cs, start, end);
/* 284 */     for (Chunk chunk : dpToChunk.values())
/* 285 */       result.add(chunk);
/* 286 */     return result;
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 290 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   void add(Map<SearchState, SearchState> nextQueue, Node<String> node, int startIndex, double chunkScore, boolean isTokenEnd, Map<Dp, Chunk> chunking, char[] cs, int end)
/*     */   {
/* 298 */     if (chunkScore > this.mDistanceThreshold) {
/* 299 */       return;
/*     */     }
/* 301 */     SearchState state2 = new SearchState(node, startIndex, chunkScore);
/*     */ 
/* 304 */     SearchState exState = (SearchState)nextQueue.get(state2);
/*     */ 
/* 306 */     if ((exState != null) && (exState.mScore < chunkScore)) {
/* 307 */       return;
/*     */     }
/* 309 */     nextQueue.put(state2, state2);
/*     */ 
/* 312 */     if (isTokenEnd) {
/* 313 */       for (int i = 0; i < node.mEntries.length; i++) {
/* 314 */         Chunk newChunk = ChunkFactory.createChunk(startIndex, end + 1, ((String)node.mEntries[i].category()).toString(), chunkScore);
/*     */ 
/* 319 */         Dp dpNewChunk = new Dp(newChunk);
/* 320 */         Chunk oldChunk = (Chunk)chunking.get(dpNewChunk);
/* 321 */         if ((oldChunk == null) || (oldChunk.score() > chunkScore))
/*     */         {
/* 323 */           chunking.remove(dpNewChunk);
/* 324 */           chunking.put(dpNewChunk, newChunk);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 329 */     for (int i = 0; i < node.mDtrChars.length; i++)
/* 330 */       add(nextQueue, node.mDtrNodes[i], startIndex, chunkScore - this.mEditDistance.insertWeight(node.mDtrChars[i]), isTokenEnd, chunking, cs, end);
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 3935654738558540166L;
/*     */     private final ApproxDictionaryChunker mChunker;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 488 */       this(null);
/*     */     }
/*     */     public Serializer(ApproxDictionaryChunker chunker) {
/* 491 */       this.mChunker = chunker;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException
/*     */     {
/* 496 */       TrieDictionary dictionary = (TrieDictionary)in.readObject();
/*     */ 
/* 499 */       TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/*     */ 
/* 502 */       WeightedEditDistance editDistance = (WeightedEditDistance)in.readObject();
/*     */ 
/* 504 */       double distanceThreshold = in.readDouble();
/* 505 */       return new ApproxDictionaryChunker(dictionary, tokenizerFactory, editDistance, distanceThreshold);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out)
/*     */       throws IOException
/*     */     {
/* 513 */       out.writeObject(this.mChunker.mDictionary);
/* 514 */       out.writeObject(this.mChunker.mTokenizerFactory);
/* 515 */       out.writeObject(this.mChunker.mEditDistance);
/* 516 */       out.writeDouble(this.mChunker.mDistanceThreshold);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class TTDistance extends WeightedEditDistance
/*     */   {
/*     */     public double deleteWeight(char cDeleted)
/*     */     {
/* 453 */       return (cDeleted == ' ') || (cDeleted == '-') ? -10.0D : -100.0D;
/*     */     }
/*     */ 
/*     */     public double insertWeight(char cInserted)
/*     */     {
/* 459 */       return deleteWeight(cInserted);
/*     */     }
/*     */ 
/*     */     public double matchWeight(char cMatched) {
/* 463 */       return 0.0D;
/*     */     }
/*     */ 
/*     */     public double substituteWeight(char cDeleted, char cInserted) {
/* 467 */       if ((cDeleted == ' ') && (cInserted == '-'))
/* 468 */         return -10.0D;
/* 469 */       if ((cDeleted == '-') && (cInserted == ' '))
/* 470 */         return -10.0D;
/* 471 */       if ((Character.isDigit(cDeleted)) && (Character.isDigit(cInserted)))
/* 472 */         return -10.0D;
/* 473 */       if (Character.toLowerCase(cDeleted) == Character.toLowerCase(cInserted))
/*     */       {
/* 475 */         return -10.0D;
/* 476 */       }return -50.0D;
/*     */     }
/*     */ 
/*     */     public double transposeWeight(char c1, char c2) {
/* 480 */       return (-1.0D / 0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SearchState
/*     */     implements Scored
/*     */   {
/*     */     private final double mScore;
/*     */     private final Node<String> mNode;
/*     */     private final int mStartIndex;
/*     */ 
/*     */     SearchState(Node<String> node, int startIndex)
/*     */     {
/* 365 */       this(node, startIndex, 0.0D);
/*     */     }
/*     */     SearchState(Node<String> node, int startIndex, double score) {
/* 368 */       this.mNode = node;
/* 369 */       this.mStartIndex = startIndex;
/* 370 */       this.mScore = score;
/*     */     }
/*     */     public double score() {
/* 373 */       return this.mScore;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object that) {
/* 377 */       SearchState thatState = (SearchState)that;
/* 378 */       return (this.mStartIndex == thatState.mStartIndex) && (this.mNode == thatState.mNode);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 383 */       return this.mStartIndex;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 387 */       return "SearchState(" + this.mNode + ", " + this.mStartIndex + ", " + this.mScore + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Dp
/*     */   {
/*     */     final int mStart;
/*     */     final int mEnd;
/*     */     final String mType;
/*     */     int mHashCode;
/*     */ 
/*     */     Dp(Chunk chunk)
/*     */     {
/* 342 */       this.mStart = chunk.start();
/* 343 */       this.mEnd = chunk.end();
/* 344 */       this.mType = chunk.type();
/* 345 */       this.mHashCode = (this.mStart + 31 * (this.mEnd + 31 * this.mType.hashCode()));
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 349 */       return this.mHashCode;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object that) {
/* 353 */       Dp thatDp = (Dp)that;
/* 354 */       return (this.mStart == thatDp.mStart) && (this.mEnd == thatDp.mEnd) && (this.mType.equals(thatDp.mType));
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dict.ApproxDictionaryChunker
 * JD-Core Version:    0.6.2
 */