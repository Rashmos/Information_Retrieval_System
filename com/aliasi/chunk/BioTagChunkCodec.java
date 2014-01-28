/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tag.StringTagging;
/*     */ import com.aliasi.tag.TagLattice;
/*     */ import com.aliasi.tag.Tagging;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Iterators;
/*     */ import com.aliasi.util.Iterators.Buffered;
/*     */ import com.aliasi.util.Math;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BioTagChunkCodec extends AbstractTagChunkCodec
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -4597052413756614276L;
/*     */   private final String mBeginTagPrefix;
/*     */   private final String mInTagPrefix;
/*     */   private final String mOutTag;
/*     */   public static final String OUT_TAG = "O";
/*     */   public static final String BEGIN_TAG_PREFIX = "B_";
/*     */   public static final String IN_TAG_PREFIX = "I_";
/*     */   static final int PREFIX_LENGTH = 2;
/*     */ 
/*     */   public BioTagChunkCodec()
/*     */   {
/* 200 */     this(null, false);
/*     */   }
/*     */ 
/*     */   public BioTagChunkCodec(TokenizerFactory tokenizerFactory, boolean enforceConsistency)
/*     */   {
/* 216 */     this(tokenizerFactory, enforceConsistency, "B_", "I_", "O");
/*     */   }
/*     */ 
/*     */   public BioTagChunkCodec(TokenizerFactory tokenizerFactory, boolean enforceConsistency, String beginTagPrefix, String inTagPrefix, String outTag)
/*     */   {
/* 237 */     super(tokenizerFactory, enforceConsistency);
/* 238 */     this.mOutTag = outTag;
/* 239 */     this.mBeginTagPrefix = beginTagPrefix;
/* 240 */     this.mInTagPrefix = inTagPrefix;
/*     */   }
/*     */ 
/*     */   public boolean enforceConsistency()
/*     */   {
/* 255 */     return this.mEnforceConsistency;
/*     */   }
/*     */ 
/*     */   public Set<String> tagSet(Set<String> chunkTypes)
/*     */   {
/* 262 */     Set tagSet = new HashSet();
/* 263 */     tagSet.add(this.mOutTag);
/* 264 */     for (String chunkType : chunkTypes) {
/* 265 */       tagSet.add(this.mBeginTagPrefix + chunkType);
/* 266 */       tagSet.add(this.mInTagPrefix + chunkType);
/*     */     }
/* 268 */     return tagSet;
/*     */   }
/*     */ 
/*     */   public boolean legalTagSubSequence(String[] tags) {
/* 272 */     if (tags.length == 0)
/* 273 */       return true;
/* 274 */     if (tags.length == 1)
/* 275 */       return legalTagSingle(tags[0]);
/* 276 */     for (int i = 1; i < tags.length; i++)
/* 277 */       if (!legalTagPair(tags[(i - 1)], tags[i]))
/* 278 */         return false;
/* 279 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean legalTags(String[] tags) {
/* 283 */     return (legalTagSubSequence(tags)) && ((tags.length == 0) || (!tags[0].startsWith(this.mInTagPrefix)));
/*     */   }
/*     */ 
/*     */   public Chunking toChunking(StringTagging tagging)
/*     */   {
/* 288 */     enforceConsistency(tagging);
/* 289 */     ChunkingImpl chunking = new ChunkingImpl(tagging.characters());
/* 290 */     for (int n = 0; n < tagging.size(); n++) {
/* 291 */       String tag = tagging.tag(n);
/* 292 */       if (!this.mOutTag.equals(tag)) {
/* 293 */         if (!tag.startsWith(this.mBeginTagPrefix)) {
/* 294 */           if (n == 0) {
/* 295 */             String msg = "First tag must be out or begin. Found tagging.tag(0)=" + tagging.tag(0);
/*     */ 
/* 297 */             throw new IllegalArgumentException(msg);
/*     */           }
/* 299 */           String msg = "Illegal tag sequence. tagging.tag(" + (n - 1) + ")=" + tagging.tag(n - 1) + " tagging.tag(" + n + ")=" + tagging.tag(n);
/*     */ 
/* 302 */           throw new IllegalArgumentException(msg);
/*     */         }
/* 304 */         String type = tag.substring(2);
/* 305 */         int start = tagging.tokenStart(n);
/* 306 */         String inTag = this.mInTagPrefix + type;
/* 307 */         while ((n + 1 < tagging.size()) && (inTag.equals(tagging.tag(n + 1))))
/* 308 */           n++;
/* 309 */         int end = tagging.tokenEnd(n);
/* 310 */         Chunk chunk = ChunkFactory.createChunk(start, end, type);
/* 311 */         chunking.add(chunk);
/*     */       }
/*     */     }
/* 313 */     return chunking;
/*     */   }
/*     */ 
/*     */   public StringTagging toStringTagging(Chunking chunking)
/*     */   {
/* 322 */     if (this.mTokenizerFactory == null) {
/* 323 */       String msg = "Tokenizer factory must be non-null to convert chunking to tagging.";
/* 324 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 326 */     enforceConsistency(chunking);
/* 327 */     List tokenList = new ArrayList();
/* 328 */     List tagList = new ArrayList();
/* 329 */     List tokenStartList = new ArrayList();
/* 330 */     List tokenEndList = new ArrayList();
/* 331 */     toTagging(chunking, tokenList, tagList, tokenStartList, tokenEndList);
/*     */ 
/* 333 */     StringTagging tagging = new StringTagging(tokenList, tagList, chunking.charSequence(), tokenStartList, tokenEndList);
/*     */ 
/* 338 */     return tagging;
/*     */   }
/*     */ 
/*     */   public Tagging<String> toTagging(Chunking chunking)
/*     */   {
/* 347 */     if (this.mTokenizerFactory == null) {
/* 348 */       String msg = "Tokenizer factory must be non-null to convert chunking to tagging.";
/* 349 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 351 */     enforceConsistency(chunking);
/* 352 */     List tokens = new ArrayList();
/* 353 */     List tags = new ArrayList();
/* 354 */     toTagging(chunking, tokens, tags, null, null);
/* 355 */     return new Tagging(tokens, tags);
/*     */   }
/*     */ 
/*     */   public Iterator<Chunk> nBestChunks(TagLattice<String> lattice, int[] tokenStarts, int[] tokenEnds, int maxResults)
/*     */   {
/* 362 */     if (maxResults < 0) {
/* 363 */       String msg = "Require non-negative number of results.";
/* 364 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 366 */     if (tokenStarts.length != lattice.numTokens()) {
/* 367 */       String msg = "Token starts must line up with num tokens. Found tokenStarts.length=" + tokenStarts.length + " lattice.numTokens()=" + lattice.numTokens();
/*     */ 
/* 370 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 372 */     if (tokenEnds.length != lattice.numTokens()) {
/* 373 */       String msg = "Token ends must line up with num tokens. Found tokenEnds.length=" + tokenEnds.length + " lattice.numTokens()=" + lattice.numTokens();
/*     */ 
/* 376 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 378 */     for (int i = 1; i < tokenStarts.length; i++) {
/* 379 */       if (tokenStarts[(i - 1)] > tokenStarts[i]) {
/* 380 */         String msg = "Token starts must be in order. Found tokenStarts[" + (i - 1) + "]=" + tokenStarts[(i - 1)] + " tokenStarts[" + i + "]=" + tokenStarts[i];
/*     */ 
/* 383 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 385 */       if (tokenEnds[(i - 1)] > tokenEnds[i]) {
/* 386 */         String msg = "Token ends must be in order. Found tokenEnds[" + (i - 1) + "]=" + tokenEnds[(i - 1)] + " tokenEnds[" + i + "]=" + tokenEnds[i];
/*     */ 
/* 389 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 392 */     if (lattice.numTags() == 0) {
/* 393 */       return Iterators.empty();
/*     */     }
/* 395 */     for (int i = 0; i < tokenStarts.length; i++) {
/* 396 */       if (tokenStarts[i] > tokenEnds[i]) {
/* 397 */         String msg = "Token ends must not precede starts. Found tokenStarts[" + i + "]=" + tokenStarts[i] + " tokenEnds[" + i + "]=" + tokenEnds[i];
/*     */ 
/* 400 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 403 */     return new NBestIterator(lattice, tokenStarts, tokenEnds, maxResults, this.mBeginTagPrefix, this.mInTagPrefix, this.mOutTag);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 413 */     return "BioTagChunkCodec";
/*     */   }
/*     */ 
/*     */   void enforceConsistency(StringTagging tagging)
/*     */   {
/* 574 */     if (!this.mEnforceConsistency) return;
/* 575 */     StringBuilder sb = new StringBuilder();
/* 576 */     if (isDecodable(tagging, sb)) return;
/* 577 */     throw new IllegalArgumentException(sb.toString());
/*     */   }
/*     */ 
/*     */   void enforceConsistency(Chunking chunking) {
/* 581 */     if (!this.mEnforceConsistency) return;
/* 582 */     StringBuilder sb = new StringBuilder();
/* 583 */     if (isEncodable(chunking, sb)) return;
/* 584 */     throw new IllegalArgumentException(sb.toString());
/*     */   }
/*     */ 
/*     */   boolean legalTagSingle(String tag) {
/* 588 */     return (this.mOutTag.equals(tag)) || (tag.startsWith(this.mBeginTagPrefix)) || (tag.startsWith(this.mInTagPrefix));
/*     */   }
/*     */ 
/*     */   boolean legalTagPair(String tag1, String tag2)
/*     */   {
/* 595 */     if (!legalTagSingle(tag1))
/* 596 */       return false;
/* 597 */     if (!legalTagSingle(tag2))
/* 598 */       return false;
/* 599 */     if (tag2.startsWith(this.mInTagPrefix))
/* 600 */       return tag1.endsWith(tag2.substring(this.mInTagPrefix.length()));
/* 601 */     return true;
/*     */   }
/*     */ 
/*     */   void toTagging(Chunking chunking, List<String> tokenList, List<String> tagList, List<Integer> tokenStartList, List<Integer> tokenEndList)
/*     */   {
/* 609 */     char[] cs = Strings.toCharArray(chunking.charSequence());
/* 610 */     Set chunkSet = chunking.chunkSet();
/* 611 */     Chunk[] chunks = (Chunk[])chunkSet.toArray(new Chunk[chunkSet.size()]);
/* 612 */     Arrays.sort(chunks, Chunk.TEXT_ORDER_COMPARATOR);
/* 613 */     int pos = 0;
/* 614 */     for (Chunk chunk : chunks) {
/* 615 */       String type = chunk.type();
/* 616 */       int start = chunk.start();
/* 617 */       int end = chunk.end();
/* 618 */       outBioTag(cs, pos, start, tokenList, tagList, tokenStartList, tokenEndList);
/* 619 */       chunkBioTag(cs, type, start, end, tokenList, tagList, tokenStartList, tokenEndList);
/* 620 */       pos = end;
/*     */     }
/* 622 */     outBioTag(cs, pos, cs.length, tokenList, tagList, tokenStartList, tokenEndList);
/*     */   }
/*     */ 
/*     */   void outBioTag(char[] cs, int start, int end, List<String> tokenList, List<String> tagList, List<Integer> tokenStartList, List<Integer> tokenEndList)
/*     */   {
/* 628 */     int length = end - start;
/* 629 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, length);
/*     */     String token;
/* 631 */     while ((token = tokenizer.nextToken()) != null) {
/* 632 */       tokenList.add(token);
/* 633 */       addOffsets(tokenizer, start, tokenStartList, tokenEndList);
/* 634 */       tagList.add(this.mOutTag);
/*     */     }
/*     */   }
/*     */ 
/*     */   void chunkBioTag(char[] cs, String type, int start, int end, List<String> tokenList, List<String> tagList, List<Integer> tokenStartList, List<Integer> tokenEndList)
/*     */   {
/* 641 */     int length = end - start;
/* 642 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, length);
/* 643 */     String firstToken = tokenizer.nextToken();
/* 644 */     if (firstToken == null) {
/* 645 */       String msg = "Chunks must contain at least one token. Found chunk with yield=|" + new String(cs, start, length) + "|";
/*     */ 
/* 647 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 649 */     tokenList.add(firstToken);
/* 650 */     addOffsets(tokenizer, start, tokenStartList, tokenEndList);
/* 651 */     String beginTag = this.mBeginTagPrefix + type;
/* 652 */     tagList.add(beginTag);
/* 653 */     String inTag = this.mInTagPrefix + type;
/*     */     String token;
/* 655 */     while ((token = tokenizer.nextToken()) != null) {
/* 656 */       tokenList.add(token);
/* 657 */       addOffsets(tokenizer, start, tokenStartList, tokenEndList);
/* 658 */       tagList.add(inTag);
/*     */     }
/*     */   }
/*     */ 
/*     */   void addOffsets(Tokenizer tokenizer, int offset, List<Integer> tokenStartList, List<Integer> tokenEndList)
/*     */   {
/* 665 */     if (tokenStartList == null) return;
/* 666 */     int start = tokenizer.lastTokenStartPosition() + offset;
/* 667 */     int end = tokenizer.lastTokenEndPosition() + offset;
/* 668 */     tokenStartList.add(Integer.valueOf(start));
/* 669 */     tokenEndList.add(Integer.valueOf(end));
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 673 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -2473387657606045149L;
/*     */     private final BioTagChunkCodec mCodec;
/*     */ 
/*     */     public Serializer() {
/* 682 */       this(null);
/*     */     }
/*     */     public Serializer(BioTagChunkCodec codec) {
/* 685 */       this.mCodec = codec;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException
/*     */     {
/* 690 */       out.writeBoolean(this.mCodec.mEnforceConsistency);
/* 691 */       out.writeObject(Boolean.TRUE);
/* 692 */       out.writeObject(this.mCodec.mTokenizerFactory != null ? this.mCodec.mTokenizerFactory : Boolean.FALSE);
/*     */ 
/* 695 */       out.writeUTF(this.mCodec.mBeginTagPrefix);
/* 696 */       out.writeUTF(this.mCodec.mInTagPrefix);
/* 697 */       out.writeUTF(this.mCodec.mOutTag);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException, ClassNotFoundException
/*     */     {
/* 702 */       boolean enforceConsistency = in.readBoolean();
/* 703 */       Object obj = in.readObject();
/* 704 */       if (Boolean.TRUE.equals(obj))
/*     */       {
/* 706 */         Object obj2 = in.readObject();
/*     */ 
/* 708 */         TokenizerFactory tf = Boolean.FALSE.equals(obj2) ? null : (TokenizerFactory)obj2;
/*     */ 
/* 712 */         String beginTagPrefix = in.readUTF();
/* 713 */         String inTagPrefix = in.readUTF();
/* 714 */         String outTag = in.readUTF();
/* 715 */         return new BioTagChunkCodec(tf, enforceConsistency, beginTagPrefix, inTagPrefix, outTag);
/*     */       }
/*     */ 
/* 721 */       TokenizerFactory tf = Boolean.FALSE.equals(obj) ? null : (TokenizerFactory)obj;
/*     */ 
/* 725 */       return new BioTagChunkCodec(tf, enforceConsistency);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class NBestIterator extends Iterators.Buffered<Chunk>
/*     */   {
/*     */     private final String mBeginTagPrefix;
/*     */     private final String mInTagPrefix;
/*     */     private final String mOutTag;
/*     */     private final TagLattice<String> mLattice;
/*     */     private final int[] mTokenStarts;
/*     */     private final int[] mTokenEnds;
/*     */     private final BoundedPriorityQueue<Chunk> mChunkQueue;
/*     */     private final BoundedPriorityQueue<NBestState> mStateQueue;
/*     */     private final int mMaxResults;
/*     */     private final String[] mChunkTypes;
/*     */     private final int[] mBeginTagIds;
/*     */     private final int[] mInTagIds;
/*     */     private final int mOutTagId;
/* 430 */     private int mNumResults = 0;
/*     */ 
/*     */     public NBestIterator(TagLattice<String> lattice, int[] tokenStarts, int[] tokenEnds, int maxResults, String beginTagPrefix, String inTagPrefix, String outTag)
/*     */     {
/* 438 */       this.mBeginTagPrefix = beginTagPrefix;
/* 439 */       this.mInTagPrefix = inTagPrefix;
/* 440 */       this.mOutTag = outTag;
/*     */ 
/* 442 */       this.mLattice = lattice;
/* 443 */       this.mTokenStarts = tokenStarts;
/* 444 */       this.mTokenEnds = tokenEnds;
/* 445 */       this.mMaxResults = maxResults;
/* 446 */       Set chunkTypeSet = new HashSet();
/* 447 */       SymbolTable tagSymbolTable = lattice.tagSymbolTable();
/* 448 */       for (int k = 0; k < lattice.numTags(); k++)
/* 449 */         if (lattice.tag(k).startsWith(this.mInTagPrefix))
/* 450 */           chunkTypeSet.add(lattice.tag(k).substring(this.mInTagPrefix.length()));
/* 451 */       this.mChunkTypes = ((String[])chunkTypeSet.toArray(Strings.EMPTY_STRING_ARRAY));
/* 452 */       this.mBeginTagIds = new int[this.mChunkTypes.length];
/* 453 */       this.mInTagIds = new int[this.mChunkTypes.length];
/* 454 */       for (int j = 0; j < this.mChunkTypes.length; j++) {
/* 455 */         this.mBeginTagIds[j] = tagSymbolTable.symbolToID(this.mBeginTagPrefix + this.mChunkTypes[j]);
/* 456 */         this.mInTagIds[j] = tagSymbolTable.symbolToID(this.mInTagPrefix + this.mChunkTypes[j]);
/*     */       }
/* 458 */       this.mOutTagId = tagSymbolTable.symbolToID(this.mOutTag);
/*     */ 
/* 460 */       this.mStateQueue = new BoundedPriorityQueue(ScoredObject.comparator(), maxResults);
/*     */ 
/* 462 */       this.mChunkQueue = new BoundedPriorityQueue(ScoredObject.comparator(), maxResults);
/*     */ 
/* 465 */       double[] nonContBuf = new double[lattice.numTags() - 1];
/* 466 */       for (int j = 0; j < this.mChunkTypes.length; j++) {
/* 467 */         int lastN = lattice.numTokens() - 1;
/* 468 */         if (lastN >= 0) {
/* 469 */           String chunkType = this.mChunkTypes[j];
/* 470 */           int inTagId = this.mInTagIds[j];
/* 471 */           int beginTagId = this.mBeginTagIds[j];
/*     */ 
/* 473 */           this.mChunkQueue.offer(ChunkFactory.createChunk(this.mTokenStarts[lastN], this.mTokenEnds[lastN], chunkType, lattice.logProbability(lastN, beginTagId)));
/*     */ 
/* 477 */           if (lastN > 0) {
/* 478 */             this.mStateQueue.offer(new NBestState(lattice.logBackward(lastN, inTagId), lastN, lastN, j));
/*     */           }
/*     */ 
/* 481 */           for (int n = 0; n < lastN; n++) {
/* 482 */             double nonCont = nonContLogSumExp(j, n, beginTagId, lattice, nonContBuf);
/* 483 */             this.mChunkQueue.offer(ChunkFactory.createChunk(this.mTokenStarts[n], this.mTokenEnds[n], chunkType, nonCont + lattice.logForward(n, beginTagId) - lattice.logZ()));
/*     */           }
/*     */ 
/* 489 */           for (int n = 1; n < lastN; n++) {
/* 490 */             double nonCont = nonContLogSumExp(j, n, inTagId, lattice, nonContBuf);
/* 491 */             this.mStateQueue.offer(new NBestState(nonCont, n, n, j)); } 
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 496 */     double nonContLogSumExp(int j, int n, int fromTagId, TagLattice<String> lattice, double[] nonContBuf) { nonContBuf[0] = (lattice.logBackward(n + 1, this.mOutTagId) + lattice.logTransition(n, fromTagId, this.mOutTagId));
/*     */ 
/* 498 */       int bufPos = 1;
/* 499 */       for (int j2 = 0; j2 < this.mBeginTagIds.length; j2++) {
/* 500 */         nonContBuf[(bufPos++)] = (lattice.logBackward(n + 1, this.mBeginTagIds[j2]) + lattice.logTransition(n, fromTagId, this.mBeginTagIds[j2]));
/*     */ 
/* 502 */         if (j != j2) {
/* 503 */           nonContBuf[(bufPos++)] = (lattice.logBackward(n + 1, this.mInTagIds[j2]) + lattice.logTransition(n, fromTagId, this.mInTagIds[j2]));
/*     */         }
/*     */       }
/* 506 */       double result = Math.logSumOfExponentials(nonContBuf);
/*     */ 
/* 510 */       return result; }
/*     */ 
/*     */     public Chunk bufferNext()
/*     */     {
/* 514 */       if (this.mNumResults >= this.mMaxResults)
/* 515 */         return null;
/* 516 */       search();
/* 517 */       Chunk chunk = (Chunk)this.mChunkQueue.poll();
/* 518 */       if (chunk == null)
/* 519 */         return null;
/* 520 */       this.mNumResults += 1;
/* 521 */       return chunk;
/*     */     }
/*     */ 
/*     */     void search() {
/* 525 */       while ((!this.mStateQueue.isEmpty()) && ((this.mChunkQueue.isEmpty()) || (((Chunk)this.mChunkQueue.peek()).score() < ((NBestState)this.mStateQueue.peek()).score())))
/*     */       {
/* 527 */         NBestState state = (NBestState)this.mStateQueue.poll();
/* 528 */         extend(state);
/*     */       }
/*     */     }
/*     */ 
/*     */     void extend(NBestState state) {
/* 533 */       int beginTagId = this.mBeginTagIds[state.mChunkId];
/* 534 */       int inTagId = this.mInTagIds[state.mChunkId];
/* 535 */       this.mChunkQueue.offer(ChunkFactory.createChunk(this.mTokenStarts[(state.mPos - 1)], this.mTokenEnds[state.mEndPos], this.mChunkTypes[state.mChunkId], state.score() + this.mLattice.logForward(state.mPos - 1, beginTagId) + this.mLattice.logTransition(state.mPos - 1, beginTagId, inTagId) - this.mLattice.logZ()));
/*     */ 
/* 542 */       if (state.mPos > 1)
/* 543 */         this.mStateQueue.offer(new NBestState(state.score() + this.mLattice.logTransition(state.mPos - 1, inTagId, inTagId), state.mPos - 1, state.mEndPos, state.mChunkId));
/*     */     }
/*     */ 
/*     */     static class NBestState implements Scored {
/*     */       private final double mScore;
/*     */       private final int mPos;
/*     */       private final int mEndPos;
/*     */       private int mChunkId;
/*     */ 
/*     */       public NBestState(double score, int pos, int endPos, int chunkId) {
/* 556 */         this.mScore = score;
/* 557 */         this.mPos = pos;
/* 558 */         this.mEndPos = endPos;
/* 559 */         this.mChunkId = chunkId;
/*     */       }
/*     */       public double score() {
/* 562 */         return this.mScore;
/*     */       }
/*     */ 
/*     */       public String toString() {
/* 566 */         return "score=" + this.mScore + " pos=" + this.mPos + " end=" + this.mEndPos + " id=" + this.mChunkId;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.BioTagChunkCodec
 * JD-Core Version:    0.6.2
 */