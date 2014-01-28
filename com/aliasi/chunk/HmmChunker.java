/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.hmm.HmmDecoder;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tag.ScoredTagging;
/*     */ import com.aliasi.tag.TagLattice;
/*     */ import com.aliasi.tag.Tagging;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Iterators.Buffered;
/*     */ import com.aliasi.util.Math;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class HmmChunker
/*     */   implements NBestChunker, ConfidenceChunker
/*     */ {
/*     */   private final TokenizerFactory mTokenizerFactory;
/*     */   private final HmmDecoder mDecoder;
/*     */ 
/*     */   public HmmChunker(TokenizerFactory tokenizerFactory, HmmDecoder decoder)
/*     */   {
/* 236 */     this.mTokenizerFactory = tokenizerFactory;
/* 237 */     this.mDecoder = decoder;
/*     */   }
/*     */ 
/*     */   public HmmDecoder getDecoder()
/*     */   {
/* 251 */     return this.mDecoder;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory getTokenizerFactory()
/*     */   {
/* 260 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public Chunking chunk(char[] cs, int start, int end)
/*     */   {
/* 278 */     Strings.checkArgsStartEnd(cs, start, end);
/* 279 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/* 280 */     List tokList = new ArrayList();
/* 281 */     List whiteList = new ArrayList();
/* 282 */     tokenizer.tokenize(tokList, whiteList);
/* 283 */     String[] toks = toStringArray(tokList);
/* 284 */     String[] whites = toStringArray(whiteList);
/* 285 */     String[] tags = (String[])this.mDecoder.tag(tokList).tags().toArray(Strings.EMPTY_STRING_ARRAY);
/* 286 */     decodeNormalize(tags);
/* 287 */     return ChunkTagHandlerAdapter2.toChunkingBIO(toks, whites, tags);
/*     */   }
/*     */ 
/*     */   public Chunking chunk(CharSequence cSeq)
/*     */   {
/* 302 */     char[] cs = Strings.toCharArray(cSeq);
/* 303 */     return chunk(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public Iterator<ScoredObject<Chunking>> nBest(char[] cs, int start, int end, int maxNBest)
/*     */   {
/* 327 */     Strings.checkArgsStartEnd(cs, start, end);
/* 328 */     if (maxNBest < 1) {
/* 329 */       String msg = "Maximum n-best value must be greater than zero. Found maxNBest=" + maxNBest;
/*     */ 
/* 331 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 333 */     String[][] toksWhites = getToksWhites(cs, start, end);
/* 334 */     Iterator it = this.mDecoder.tagNBest(Arrays.asList(toksWhites[0]), maxNBest);
/* 335 */     return new NBestIt(it, toksWhites);
/*     */   }
/*     */ 
/*     */   public Iterator<ScoredObject<Chunking>> nBestConditional(char[] cs, int start, int end, int maxNBest)
/*     */   {
/* 359 */     Strings.checkArgsStartEnd(cs, start, end);
/* 360 */     if (maxNBest < 1) {
/* 361 */       String msg = "Maximum n-best value must be greater than zero. Found maxNBest=" + maxNBest;
/*     */ 
/* 363 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 365 */     String[][] toksWhites = getToksWhites(cs, start, end);
/* 366 */     Iterator it = this.mDecoder.tagNBestConditional(Arrays.asList(toksWhites[0]), maxNBest);
/* 367 */     return new NBestIt(it, toksWhites);
/*     */   }
/*     */ 
/*     */   public Iterator<Chunk> nBestChunks(char[] cs, int start, int end, int maxNBest)
/*     */   {
/* 392 */     String[][] toksWhites = getToksWhites(cs, start, end);
/*     */ 
/* 394 */     TagLattice lattice = this.mDecoder.tagMarginal(Arrays.asList(toksWhites[0]));
/* 395 */     return new NBestChunkIt(lattice, toksWhites[1], maxNBest);
/*     */   }
/*     */ 
/*     */   String[][] getToksWhites(char[] cs, int start, int end) {
/* 399 */     Strings.checkArgsStartEnd(cs, start, end);
/* 400 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, start, end - start);
/* 401 */     List tokList = new ArrayList();
/* 402 */     List whiteList = new ArrayList();
/* 403 */     tokenizer.tokenize(tokList, whiteList);
/* 404 */     String[] toks = toStringArray(tokList);
/* 405 */     String[] whites = toStringArray(whiteList);
/* 406 */     return new String[][] { toks, whites };
/*     */   }
/*     */ 
/*     */   private static String[] toStringArray(Collection<String> c)
/*     */   {
/* 652 */     return (String[])c.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */   }
/*     */ 
/*     */   private static int[] toIntArray(Collection<Integer> c) {
/* 656 */     int[] result = new int[c.size()];
/* 657 */     Iterator it = c.iterator();
/* 658 */     for (int i = 0; it.hasNext(); i++) {
/* 659 */       Integer nextVal = (Integer)it.next();
/* 660 */       result[i] = nextVal.intValue();
/*     */     }
/* 662 */     return result;
/*     */   }
/*     */ 
/*     */   static String baseTag(String tag)
/*     */   {
/* 667 */     if (ChunkTagHandlerAdapter2.isOutTag(tag)) return tag;
/* 668 */     return tag.substring(2);
/*     */   }
/*     */ 
/*     */   static String[] trainNormalize(String[] tags)
/*     */   {
/* 674 */     if (tags.length == 0) return tags;
/* 675 */     String[] normalTags = new String[tags.length];
/* 676 */     for (int i = 0; i < normalTags.length; i++) {
/* 677 */       String prevTag = i - 1 >= 0 ? tags[(i - 1)] : "W_BOS";
/* 678 */       String nextTag = i + 1 < tags.length ? tags[(i + 1)] : "W_BOS";
/* 679 */       normalTags[i] = trainNormalize(prevTag, tags[i], nextTag);
/*     */     }
/* 681 */     return normalTags;
/*     */   }
/*     */ 
/*     */   private static void decodeNormalize(String[] tags)
/*     */   {
/* 686 */     for (int i = 0; i < tags.length; i++)
/* 687 */       tags[i] = decodeNormalize(tags[i]);
/*     */   }
/*     */ 
/*     */   static String trainNormalize(String prevTag, String tag, String nextTag)
/*     */   {
/* 694 */     if (ChunkTagHandlerAdapter2.isOutTag(tag)) {
/* 695 */       if (ChunkTagHandlerAdapter2.isOutTag(prevTag)) {
/* 696 */         if (ChunkTagHandlerAdapter2.isOutTag(nextTag)) {
/* 697 */           return "MM_O";
/*     */         }
/* 699 */         return "EE_O_" + baseTag(nextTag);
/*     */       }
/* 701 */       if (ChunkTagHandlerAdapter2.isOutTag(nextTag)) {
/* 702 */         return "BB_O_" + baseTag(prevTag);
/*     */       }
/* 704 */       return "WW_O_" + baseTag(nextTag);
/*     */     }
/*     */ 
/* 707 */     if (ChunkTagHandlerAdapter2.isBeginTag(tag)) {
/* 708 */       if (ChunkTagHandlerAdapter2.isInTag(nextTag)) {
/* 709 */         return "B_" + baseTag(tag);
/*     */       }
/* 711 */       return "W_" + baseTag(tag);
/*     */     }
/* 713 */     if (ChunkTagHandlerAdapter2.isInTag(tag)) {
/* 714 */       if (ChunkTagHandlerAdapter2.isInTag(nextTag)) {
/* 715 */         return "M_" + baseTag(tag);
/*     */       }
/* 717 */       return "E_" + baseTag(tag);
/*     */     }
/* 719 */     String msg = "Unknown tag triple. prevTag=" + prevTag + " tag=" + tag + " nextTag=" + nextTag;
/*     */ 
/* 723 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   private static String decodeNormalize(String tag) {
/* 727 */     if ((tag.startsWith("B_")) || (tag.startsWith("W_"))) {
/* 728 */       String baseTag = tag.substring(2);
/* 729 */       return ChunkTagHandlerAdapter2.toBeginTag(baseTag);
/*     */     }
/* 731 */     if ((tag.startsWith("M_")) || (tag.startsWith("E_"))) {
/* 732 */       String baseTag = tag.substring(2);
/* 733 */       return ChunkTagHandlerAdapter2.toInTag(baseTag);
/*     */     }
/* 735 */     return ChunkTagHandlerAdapter2.OUT_TAG;
/*     */   }
/*     */ 
/*     */   private static class NBestIt
/*     */     implements Iterator<ScoredObject<Chunking>>
/*     */   {
/*     */     final Iterator<ScoredTagging<String>> mIt;
/*     */     final String[] mWhites;
/*     */     final String[] mToks;
/*     */ 
/*     */     NBestIt(Iterator<ScoredTagging<String>> it, String[][] toksWhites)
/*     */     {
/* 629 */       this.mIt = it;
/* 630 */       this.mToks = toksWhites[0];
/* 631 */       this.mWhites = toksWhites[1];
/*     */     }
/*     */     public boolean hasNext() {
/* 634 */       return this.mIt.hasNext();
/*     */     }
/*     */     public ScoredObject<Chunking> next() {
/* 637 */       ScoredTagging so = (ScoredTagging)this.mIt.next();
/* 638 */       double score = so.score();
/* 639 */       String[] tags = (String[])so.tags().toArray(Strings.EMPTY_STRING_ARRAY);
/* 640 */       HmmChunker.decodeNormalize(tags);
/* 641 */       Chunking chunking = ChunkTagHandlerAdapter2.toChunkingBIO(this.mToks, this.mWhites, tags);
/*     */ 
/* 643 */       return new ScoredObject(chunking, score);
/*     */     }
/*     */     public void remove() {
/* 646 */       this.mIt.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ChunkItState
/*     */     implements Scored
/*     */   {
/*     */     final int mStartCharPos;
/*     */     final int mTokPos;
/*     */     final String mTag;
/*     */     final double mForward;
/*     */     final double mBack;
/*     */     final double mScore;
/*     */     final int mCurrentTagId;
/*     */     final int mMidTagId;
/*     */     final int mEndTagId;
/*     */ 
/*     */     ChunkItState(int startCharPos, int tokPos, String tag, int currentTagId, int midTagId, int endTagId, double forward, double back)
/*     */     {
/* 600 */       this.mStartCharPos = startCharPos;
/* 601 */       this.mTokPos = tokPos;
/*     */ 
/* 603 */       this.mTag = tag;
/* 604 */       this.mCurrentTagId = currentTagId;
/* 605 */       this.mMidTagId = midTagId;
/* 606 */       this.mEndTagId = endTagId;
/*     */ 
/* 608 */       this.mForward = forward;
/* 609 */       this.mBack = back;
/* 610 */       this.mScore = (forward + back);
/*     */     }
/*     */     public double score() {
/* 613 */       return this.mScore;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class NBestChunkIt extends Iterators.Buffered<Chunk>
/*     */   {
/*     */     final TagLattice<String> mLattice;
/*     */     final String[] mWhites;
/*     */     final int mMaxNBest;
/*     */     final int[] mTokenStartIndexes;
/*     */     final int[] mTokenEndIndexes;
/*     */     String[] mBeginTags;
/*     */     int[] mBeginTagIds;
/*     */     int[] mMidTagIds;
/*     */     int[] mEndTagIds;
/*     */     String[] mWholeTags;
/*     */     int[] mWholeTagIds;
/*     */     final BoundedPriorityQueue<Scored> mQueue;
/*     */     final int mNumToks;
/*     */     final double mTotal;
/* 431 */     int mCount = 0;
/*     */ 
/*     */     NBestChunkIt(TagLattice<String> lattice, String[] whites, int maxNBest) {
/* 434 */       this.mTotal = Math.naturalLogToBase2Log(lattice.logZ());
/* 435 */       this.mLattice = lattice;
/* 436 */       this.mWhites = whites;
/* 437 */       String[] toks = (String[])lattice.tokenList().toArray(Strings.EMPTY_STRING_ARRAY);
/* 438 */       this.mNumToks = toks.length;
/* 439 */       this.mTokenStartIndexes = new int[this.mNumToks];
/* 440 */       this.mTokenEndIndexes = new int[this.mNumToks];
/* 441 */       int pos = 0;
/* 442 */       for (int i = 0; i < this.mNumToks; i++) {
/* 443 */         pos += whites[i].length();
/* 444 */         this.mTokenStartIndexes[i] = pos;
/* 445 */         pos += toks[i].length();
/* 446 */         this.mTokenEndIndexes[i] = pos;
/*     */       }
/*     */ 
/* 449 */       this.mMaxNBest = maxNBest;
/* 450 */       this.mQueue = new BoundedPriorityQueue(ScoredObject.comparator(), maxNBest);
/*     */ 
/* 452 */       initializeTags();
/* 453 */       initializeQueue();
/*     */     }
/*     */ 
/*     */     void initializeTags() {
/* 457 */       SymbolTable tagTable = this.mLattice.tagSymbolTable();
/* 458 */       List beginTagList = new ArrayList();
/* 459 */       List beginTagIdList = new ArrayList();
/* 460 */       List midTagIdList = new ArrayList();
/* 461 */       List endTagIdList = new ArrayList();
/* 462 */       List wholeTagList = new ArrayList();
/* 463 */       List wholeTagIdList = new ArrayList();
/* 464 */       int numTags = tagTable.numSymbols();
/* 465 */       for (int i = 0; i < numTags; i++) {
/* 466 */         String tag = tagTable.idToSymbol(i);
/* 467 */         if (tag.startsWith("B_")) {
/* 468 */           String baseTag = tag.substring(2);
/* 469 */           beginTagList.add(baseTag);
/* 470 */           beginTagIdList.add(Integer.valueOf(i));
/*     */ 
/* 472 */           String midTag = "M_" + baseTag;
/* 473 */           int midTagId = tagTable.symbolToID(midTag);
/* 474 */           midTagIdList.add(Integer.valueOf(midTagId));
/*     */ 
/* 476 */           String endTag = "E_" + baseTag;
/* 477 */           int endTagId = tagTable.symbolToID(endTag);
/* 478 */           endTagIdList.add(Integer.valueOf(endTagId));
/*     */         }
/* 480 */         else if (tag.startsWith("W_")) {
/* 481 */           String baseTag = tag.substring(2);
/* 482 */           wholeTagList.add(baseTag);
/* 483 */           wholeTagIdList.add(Integer.valueOf(i));
/*     */         }
/*     */       }
/* 486 */       this.mBeginTags = HmmChunker.toStringArray(beginTagList);
/* 487 */       this.mBeginTagIds = HmmChunker.toIntArray(beginTagIdList);
/* 488 */       this.mMidTagIds = HmmChunker.toIntArray(midTagIdList);
/* 489 */       this.mEndTagIds = HmmChunker.toIntArray(endTagIdList);
/*     */ 
/* 491 */       this.mWholeTags = HmmChunker.toStringArray(wholeTagList);
/* 492 */       this.mWholeTagIds = HmmChunker.toIntArray(wholeTagIdList);
/*     */     }
/*     */     void initializeQueue() {
/* 495 */       int len = this.mWhites.length - 1;
/* 496 */       for (int i = 0; i < len; i++) {
/* 497 */         for (int j = 0; j < this.mBeginTagIds.length; j++)
/* 498 */           initializeBeginTag(i, j);
/* 499 */         for (int j = 0; j < this.mWholeTagIds.length; j++)
/* 500 */           initializeWholeTag(i, j); 
/*     */       }
/*     */     }
/*     */ 
/* 504 */     void initializeBeginTag(int tokPos, int j) { int startCharPos = this.mTokenStartIndexes[tokPos];
/*     */ 
/* 506 */       String tag = this.mBeginTags[j];
/* 507 */       int beginTagId = this.mBeginTagIds[j];
/* 508 */       int midTagId = this.mMidTagIds[j];
/* 509 */       int endTagId = this.mEndTagIds[j];
/*     */ 
/* 511 */       double forward = Math.naturalLogToBase2Log(this.mLattice.logForward(tokPos, beginTagId));
/* 512 */       double backward = Math.naturalLogToBase2Log(this.mLattice.logBackward(tokPos, beginTagId));
/*     */ 
/* 514 */       HmmChunker.ChunkItState state = new HmmChunker.ChunkItState(startCharPos, tokPos, tag, beginTagId, midTagId, endTagId, forward, backward);
/*     */ 
/* 518 */       this.mQueue.offer(state); }
/*     */ 
/*     */     void initializeWholeTag(int tokPos, int j) {
/* 521 */       int start = this.mTokenStartIndexes[tokPos];
/* 522 */       int end = this.mTokenEndIndexes[tokPos];
/* 523 */       String tag = this.mWholeTags[j];
/* 524 */       double log2Score = Math.naturalLogToBase2Log(this.mLattice.logProbability(tokPos, this.mWholeTagIds[j]));
/* 525 */       Chunk chunk = ChunkFactory.createChunk(start, end, tag, log2Score);
/* 526 */       this.mQueue.offer(chunk);
/*     */     }
/*     */ 
/*     */     public Chunk bufferNext() {
/* 530 */       if (this.mCount > this.mMaxNBest) return null;
/* 531 */       while (!this.mQueue.isEmpty()) {
/* 532 */         Object next = this.mQueue.poll();
/* 533 */         if ((next instanceof Chunk)) {
/* 534 */           this.mCount += 1;
/* 535 */           Chunk result = (Chunk)next;
/* 536 */           return ChunkFactory.createChunk(result.start(), result.end(), result.type(), result.score() - this.mTotal);
/*     */         }
/*     */ 
/* 541 */         HmmChunker.ChunkItState state = (HmmChunker.ChunkItState)next;
/* 542 */         addNextMidState(state);
/* 543 */         addNextEndState(state);
/*     */       }
/* 545 */       return null;
/*     */     }
/*     */     void addNextMidState(HmmChunker.ChunkItState state) {
/* 548 */       int nextTokPos = state.mTokPos + 1;
/* 549 */       if (nextTokPos + 1 >= this.mNumToks)
/* 550 */         return;
/* 551 */       int midTagId = state.mMidTagId;
/* 552 */       double transition = Math.naturalLogToBase2Log(this.mLattice.logTransition(nextTokPos - 1, state.mCurrentTagId, midTagId));
/*     */ 
/* 556 */       double forward = state.mForward + transition;
/* 557 */       double backward = Math.naturalLogToBase2Log(this.mLattice.logBackward(nextTokPos, midTagId));
/* 558 */       HmmChunker.ChunkItState nextState = new HmmChunker.ChunkItState(state.mStartCharPos, nextTokPos, state.mTag, midTagId, state.mMidTagId, state.mEndTagId, forward, backward);
/*     */ 
/* 563 */       this.mQueue.offer(nextState);
/*     */     }
/*     */     void addNextEndState(HmmChunker.ChunkItState state) {
/* 566 */       int nextTokPos = state.mTokPos + 1;
/* 567 */       if (nextTokPos >= this.mNumToks) return;
/* 568 */       int endTagId = state.mEndTagId;
/* 569 */       double transition = Math.naturalLogToBase2Log(this.mLattice.logTransition(nextTokPos - 1, state.mCurrentTagId, endTagId));
/*     */ 
/* 573 */       double forward = state.mForward + transition;
/* 574 */       double backward = Math.naturalLogToBase2Log(this.mLattice.logBackward(nextTokPos, endTagId));
/* 575 */       double log2Prob = forward + backward;
/* 576 */       Chunk chunk = ChunkFactory.createChunk(state.mStartCharPos, this.mTokenEndIndexes[nextTokPos], state.mTag, log2Prob);
/*     */ 
/* 581 */       this.mQueue.offer(chunk);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.HmmChunker
 * JD-Core Version:    0.6.2
 */