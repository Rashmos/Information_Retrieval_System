/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.hmm.AbstractHmmEstimator;
/*     */ import com.aliasi.hmm.HiddenMarkovModel;
/*     */ import com.aliasi.hmm.HmmDecoder;
/*     */ import com.aliasi.symbol.SymbolTable;
/*     */ import com.aliasi.tag.Tagging;
/*     */ import com.aliasi.tokenizer.Tokenizer;
/*     */ import com.aliasi.tokenizer.TokenizerFactory;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class CharLmHmmChunker extends HmmChunker
/*     */   implements ObjectHandler<Chunking>, Compilable
/*     */ {
/* 225 */   private final boolean mValidateTokenizer = false;
/*     */   private final AbstractHmmEstimator mHmmEstimator;
/*     */   private final TokenizerFactory mTokenizerFactory;
/* 229 */   private final Set<String> mTagSet = new HashSet();
/*     */   private final boolean mSmoothTags;
/* 611 */   static final Chunk[] EMPTY_CHUNK_ARRAY = new Chunk[0];
/*     */ 
/*     */   public CharLmHmmChunker(TokenizerFactory tokenizerFactory, AbstractHmmEstimator hmmEstimator)
/*     */   {
/* 245 */     this(tokenizerFactory, hmmEstimator, false);
/*     */   }
/*     */ 
/*     */   public CharLmHmmChunker(TokenizerFactory tokenizerFactory, AbstractHmmEstimator hmmEstimator, boolean smoothTags)
/*     */   {
/* 269 */     super(tokenizerFactory, new HmmDecoder(hmmEstimator));
/* 270 */     this.mHmmEstimator = hmmEstimator;
/* 271 */     this.mTokenizerFactory = tokenizerFactory;
/* 272 */     this.mSmoothTags = smoothTags;
/* 273 */     smoothBoundaries();
/*     */   }
/*     */ 
/*     */   public AbstractHmmEstimator getHmmEstimator()
/*     */   {
/* 285 */     return this.mHmmEstimator;
/*     */   }
/*     */ 
/*     */   public TokenizerFactory getTokenizerFactory()
/*     */   {
/* 295 */     return this.mTokenizerFactory;
/*     */   }
/*     */ 
/*     */   public void trainDictionary(CharSequence cSeq, String type)
/*     */   {
/* 315 */     char[] cs = Strings.toCharArray(cSeq);
/* 316 */     Tokenizer tokenizer = getTokenizerFactory().tokenizer(cs, 0, cs.length);
/* 317 */     String[] tokens = tokenizer.tokenize();
/* 318 */     if (tokens.length < 1) {
/* 319 */       String msg = "Did not find any tokens in entry.Char sequence=" + cSeq;
/*     */ 
/* 321 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 323 */     AbstractHmmEstimator estimator = getHmmEstimator();
/* 324 */     SymbolTable table = estimator.stateSymbolTable();
/* 325 */     smoothBaseTag(type, table, estimator);
/* 326 */     if (tokens.length == 1) {
/* 327 */       estimator.trainEmit("W_" + type, tokens[0]);
/* 328 */       return;
/*     */     }
/* 330 */     String initialTag = "B_" + type;
/* 331 */     estimator.trainEmit(initialTag, tokens[0]);
/* 332 */     String prevTag = initialTag;
/* 333 */     for (int i = 1; i + 1 < tokens.length; i++) {
/* 334 */       String tag = "M_" + type;
/* 335 */       estimator.trainEmit(tag, tokens[i]);
/* 336 */       estimator.trainTransit(prevTag, tag);
/* 337 */       prevTag = tag;
/*     */     }
/* 339 */     String finalTag = "E_" + type;
/* 340 */     estimator.trainEmit(finalTag, tokens[(tokens.length - 1)]);
/* 341 */     estimator.trainTransit(prevTag, finalTag);
/*     */   }
/*     */ 
/*     */   public void handle(Chunking chunking)
/*     */   {
/* 355 */     CharSequence cSeq = chunking.charSequence();
/* 356 */     char[] cs = Strings.toCharArray(cSeq);
/*     */ 
/* 358 */     Set chunkSet = chunking.chunkSet();
/* 359 */     Chunk[] chunks = (Chunk[])chunkSet.toArray(EMPTY_CHUNK_ARRAY);
/* 360 */     Arrays.sort(chunks, Chunk.TEXT_ORDER_COMPARATOR);
/*     */ 
/* 362 */     List tokenList = new ArrayList();
/* 363 */     List whiteList = new ArrayList();
/* 364 */     List tagList = new ArrayList();
/* 365 */     int pos = 0;
/* 366 */     for (Chunk nextChunk : chunks) {
/* 367 */       String type = nextChunk.type();
/* 368 */       int start = nextChunk.start();
/* 369 */       int end = nextChunk.end();
/* 370 */       outTag(cs, pos, start, tokenList, whiteList, tagList, this.mTokenizerFactory);
/* 371 */       chunkTag(cs, start, end, type, tokenList, whiteList, tagList, this.mTokenizerFactory);
/* 372 */       pos = end;
/*     */     }
/* 374 */     outTag(cs, pos, cSeq.length(), tokenList, whiteList, tagList, this.mTokenizerFactory);
/* 375 */     String[] toks = (String[])tokenList.toArray(Strings.EMPTY_STRING_ARRAY);
/* 376 */     String[] whites = (String[])whiteList.toArray(Strings.EMPTY_STRING_ARRAY);
/* 377 */     String[] tags = (String[])tagList.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */ 
/* 386 */     handle(toks, whites, tags);
/*     */   }
/*     */ 
/*     */   void handle(String[] tokens, String[] whitespaces, String[] tags) {
/* 390 */     Tagging tagging = new Tagging(Arrays.asList(tokens), Arrays.asList(trainNormalize(tags)));
/*     */ 
/* 393 */     getHmmEstimator().handle(tagging);
/* 394 */     smoothTags(tags);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 409 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 421 */     StringBuilder sb = new StringBuilder();
/* 422 */     Set expandedTagSet = new TreeSet();
/* 423 */     expandedTagSet.add("MM_O");
/* 424 */     expandedTagSet.add("WW_O_BOS");
/* 425 */     expandedTagSet.add("BB_O_BOS");
/* 426 */     expandedTagSet.add("EE_O_BOS");
/* 427 */     for (Object tag0 : this.mTagSet) {
/* 428 */       String x = tag0.toString();
/* 429 */       expandedTagSet.add("B_" + x);
/* 430 */       expandedTagSet.add("M_" + x);
/* 431 */       expandedTagSet.add("E_" + x);
/* 432 */       expandedTagSet.add("W_" + x);
/* 433 */       expandedTagSet.add("BB_O_" + x);
/* 434 */       expandedTagSet.add("EE_O_" + x);
/* 435 */       expandedTagSet.add("WW_O_" + x);
/*     */     }
/*     */ 
/* 438 */     for (Object tag0Obj : expandedTagSet) {
/* 439 */       tag0 = tag0Obj.toString();
/* 440 */       sb.append("\n");
/* 441 */       sb.append("start(" + tag0 + ")=" + this.mHmmEstimator.startLog2Prob(tag0));
/* 442 */       sb.append("\n");
/* 443 */       sb.append("  end(" + tag0 + ")=" + this.mHmmEstimator.endLog2Prob(tag0));
/* 444 */       sb.append("\n");
/* 445 */       for (Object tag1Obj : expandedTagSet) {
/* 446 */         String tag1 = tag1Obj.toString();
/* 447 */         sb.append("trans(" + tag0 + "," + tag1 + ")=" + this.mHmmEstimator.transitLog2Prob(tag0, tag1));
/*     */ 
/* 449 */         sb.append("\n");
/*     */       }
/*     */     }
/*     */     String tag0;
/* 452 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   void smoothBoundaries()
/*     */   {
/* 459 */     AbstractHmmEstimator hmmEstimator = getHmmEstimator();
/* 460 */     SymbolTable table = hmmEstimator.stateSymbolTable();
/* 461 */     String bbO = "BB_O_BOS";
/* 462 */     String mmO = "MM_O";
/* 463 */     String eeO = "EE_O_BOS";
/* 464 */     String wwO = "WW_O_BOS";
/*     */ 
/* 466 */     table.getOrAddSymbol(bbO);
/* 467 */     table.getOrAddSymbol(mmO);
/* 468 */     table.getOrAddSymbol(eeO);
/* 469 */     table.getOrAddSymbol(wwO);
/*     */ 
/* 471 */     hmmEstimator.trainStart(bbO);
/* 472 */     hmmEstimator.trainStart(wwO);
/*     */ 
/* 474 */     hmmEstimator.trainEnd(eeO);
/* 475 */     hmmEstimator.trainEnd(wwO);
/*     */ 
/* 477 */     hmmEstimator.trainTransit(bbO, mmO);
/* 478 */     hmmEstimator.trainTransit(bbO, eeO);
/* 479 */     hmmEstimator.trainTransit(mmO, mmO);
/* 480 */     hmmEstimator.trainTransit(mmO, eeO);
/*     */   }
/*     */ 
/*     */   void smoothTags(String[] tags) {
/* 484 */     if (!this.mSmoothTags) return;
/* 485 */     AbstractHmmEstimator hmmEstimator = getHmmEstimator();
/* 486 */     SymbolTable table = hmmEstimator.stateSymbolTable();
/* 487 */     for (int i = 0; i < tags.length; i++)
/* 488 */       smoothTag(tags[i], table, hmmEstimator);
/*     */   }
/*     */ 
/*     */   void smoothTag(String tag, SymbolTable table, AbstractHmmEstimator hmmEstimator)
/*     */   {
/* 494 */     smoothBaseTag(HmmChunker.baseTag(tag), table, hmmEstimator);
/*     */   }
/*     */ 
/*     */   void smoothBaseTag(String baseTag, SymbolTable table, AbstractHmmEstimator hmmEstimator)
/*     */   {
/* 501 */     if (!this.mTagSet.add(baseTag)) return;
/* 502 */     if ("O".equals(baseTag)) return;
/*     */ 
/* 504 */     String b_x = "B_" + baseTag;
/* 505 */     String m_x = "M_" + baseTag;
/* 506 */     String e_x = "E_" + baseTag;
/* 507 */     String w_x = "W_" + baseTag;
/*     */ 
/* 509 */     String bb_o_x = "BB_O_" + baseTag;
/*     */ 
/* 511 */     String ee_o_x = "EE_O_" + baseTag;
/* 512 */     String ww_o_x = "WW_O_" + baseTag;
/*     */ 
/* 514 */     table.getOrAddSymbol(b_x);
/* 515 */     table.getOrAddSymbol(m_x);
/* 516 */     table.getOrAddSymbol(e_x);
/* 517 */     table.getOrAddSymbol(w_x);
/*     */ 
/* 519 */     table.getOrAddSymbol(bb_o_x);
/*     */ 
/* 521 */     table.getOrAddSymbol(ee_o_x);
/* 522 */     table.getOrAddSymbol(ww_o_x);
/*     */ 
/* 524 */     hmmEstimator.trainStart(b_x);
/* 525 */     hmmEstimator.trainTransit(b_x, m_x);
/* 526 */     hmmEstimator.trainTransit(b_x, e_x);
/*     */ 
/* 528 */     hmmEstimator.trainTransit(m_x, m_x);
/* 529 */     hmmEstimator.trainTransit(m_x, e_x);
/*     */ 
/* 531 */     hmmEstimator.trainEnd(e_x);
/* 532 */     hmmEstimator.trainTransit(e_x, bb_o_x);
/*     */ 
/* 534 */     hmmEstimator.trainStart(w_x);
/* 535 */     hmmEstimator.trainEnd(w_x);
/* 536 */     hmmEstimator.trainTransit(w_x, bb_o_x);
/*     */ 
/* 538 */     hmmEstimator.trainTransit(bb_o_x, "MM_O");
/*     */ 
/* 540 */     hmmEstimator.trainTransit("MM_O", ee_o_x);
/*     */ 
/* 542 */     hmmEstimator.trainTransit(ee_o_x, b_x);
/* 543 */     hmmEstimator.trainTransit(ee_o_x, w_x);
/*     */ 
/* 545 */     hmmEstimator.trainStart(ww_o_x);
/* 546 */     hmmEstimator.trainTransit(ww_o_x, b_x);
/* 547 */     hmmEstimator.trainTransit(ww_o_x, w_x);
/*     */ 
/* 549 */     hmmEstimator.trainTransit(e_x, "WW_O_BOS");
/* 550 */     hmmEstimator.trainTransit(w_x, "WW_O_BOS");
/*     */ 
/* 552 */     hmmEstimator.trainTransit(bb_o_x, "EE_O_BOS");
/* 553 */     hmmEstimator.trainTransit("BB_O_BOS", ee_o_x);
/*     */ 
/* 555 */     for (String type : this.mTagSet)
/* 556 */       if ((!"O".equals(type)) && 
/* 557 */         (!"BOS".equals(type))) {
/* 558 */         String bb_o_y = "BB_O_" + type;
/* 559 */         String ww_o_y = "WW_O_" + type;
/* 560 */         String ee_o_y = "EE_O_" + type;
/* 561 */         String b_y = "B_" + type;
/* 562 */         String w_y = "W_" + type;
/* 563 */         String e_y = "E_" + type;
/* 564 */         hmmEstimator.trainTransit(e_x, ww_o_y);
/* 565 */         hmmEstimator.trainTransit(e_x, b_y);
/* 566 */         hmmEstimator.trainTransit(e_x, w_y);
/* 567 */         hmmEstimator.trainTransit(w_x, ww_o_y);
/* 568 */         hmmEstimator.trainTransit(w_x, b_y);
/* 569 */         hmmEstimator.trainTransit(w_x, w_y);
/* 570 */         hmmEstimator.trainTransit(e_y, b_x);
/* 571 */         hmmEstimator.trainTransit(e_y, w_x);
/* 572 */         hmmEstimator.trainTransit(e_y, ww_o_x);
/* 573 */         hmmEstimator.trainTransit(w_y, b_x);
/* 574 */         hmmEstimator.trainTransit(w_y, w_x);
/* 575 */         hmmEstimator.trainTransit(w_y, ww_o_x);
/* 576 */         hmmEstimator.trainTransit(bb_o_x, ee_o_y);
/* 577 */         hmmEstimator.trainTransit(bb_o_y, ee_o_x);
/*     */       }
/*     */   }
/*     */ 
/*     */   static void outTag(char[] cs, int start, int end, List<String> tokenList, List<String> whiteList, List<String> tagList, TokenizerFactory factory)
/*     */   {
/* 616 */     Tokenizer tokenizer = factory.tokenizer(cs, start, end - start);
/* 617 */     whiteList.add(tokenizer.nextWhitespace());
/*     */     String nextToken;
/* 619 */     while ((nextToken = tokenizer.nextToken()) != null) {
/* 620 */       tokenList.add(nextToken);
/* 621 */       tagList.add(ChunkTagHandlerAdapter2.OUT_TAG);
/* 622 */       whiteList.add(tokenizer.nextWhitespace());
/*     */     }
/*     */   }
/*     */ 
/*     */   static void chunkTag(char[] cs, int start, int end, String type, List<String> tokenList, List<String> whiteList, List<String> tagList, TokenizerFactory factory)
/*     */   {
/* 630 */     Tokenizer tokenizer = factory.tokenizer(cs, start, end - start);
/* 631 */     String firstToken = tokenizer.nextToken();
/* 632 */     tokenList.add(firstToken);
/* 633 */     tagList.add(ChunkTagHandlerAdapter2.BEGIN_TAG_PREFIX + type);
/*     */     while (true) {
/* 635 */       String nextWhitespace = tokenizer.nextWhitespace();
/* 636 */       String nextToken = tokenizer.nextToken();
/* 637 */       if (nextToken == null) break;
/* 638 */       tokenList.add(nextToken);
/* 639 */       whiteList.add(nextWhitespace);
/* 640 */       tagList.add(ChunkTagHandlerAdapter2.IN_TAG_PREFIX + type);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean consistentTokens(String[] toks, String[] whitespaces, TokenizerFactory tokenizerFactory)
/*     */   {
/* 647 */     if (toks.length + 1 != whitespaces.length) return false;
/* 648 */     char[] cs = getChars(toks, whitespaces);
/* 649 */     Tokenizer tokenizer = tokenizerFactory.tokenizer(cs, 0, cs.length);
/* 650 */     String nextWhitespace = tokenizer.nextWhitespace();
/* 651 */     if (!whitespaces[0].equals(nextWhitespace)) {
/* 652 */       return false;
/*     */     }
/* 654 */     for (int i = 0; i < toks.length; i++) {
/* 655 */       String token = tokenizer.nextToken();
/* 656 */       if (token == null) {
/* 657 */         return false;
/*     */       }
/* 659 */       if (!toks[i].equals(token)) {
/* 660 */         return false;
/*     */       }
/* 662 */       nextWhitespace = tokenizer.nextWhitespace();
/* 663 */       if (!whitespaces[(i + 1)].equals(nextWhitespace)) {
/* 664 */         return false;
/*     */       }
/*     */     }
/* 667 */     return true;
/*     */   }
/*     */ 
/*     */   List<String> tokenization(String[] toks, String[] whitespaces) {
/* 671 */     List tokList = new ArrayList();
/* 672 */     List whiteList = new ArrayList();
/* 673 */     char[] cs = getChars(toks, whitespaces);
/* 674 */     Tokenizer tokenizer = this.mTokenizerFactory.tokenizer(cs, 0, cs.length);
/* 675 */     tokenizer.tokenize(tokList, whiteList);
/* 676 */     return tokList;
/*     */   }
/*     */ 
/*     */   static char[] getChars(String[] toks, String[] whitespaces) {
/* 680 */     StringBuilder sb = new StringBuilder();
/* 681 */     for (int i = 0; i < toks.length; i++) {
/* 682 */       sb.append(whitespaces[i]);
/* 683 */       sb.append(toks[i]);
/*     */     }
/* 685 */     sb.append(whitespaces[(whitespaces.length - 1)]);
/* 686 */     return Strings.toCharArray(sb);
/*     */   }
/*     */ 
/*     */   static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = 4630707998932521821L;
/*     */     final CharLmHmmChunker mChunker;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 586 */       this(null);
/*     */     }
/*     */     public Externalizer(CharLmHmmChunker chunker) {
/* 589 */       this.mChunker = chunker;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 595 */       TokenizerFactory tokenizerFactory = (TokenizerFactory)in.readObject();
/*     */ 
/* 597 */       HiddenMarkovModel hmm = (HiddenMarkovModel)in.readObject();
/*     */ 
/* 599 */       HmmDecoder decoder = new HmmDecoder(hmm);
/* 600 */       return new HmmChunker(tokenizerFactory, decoder);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 604 */       AbstractExternalizable.compileOrSerialize(this.mChunker.getTokenizerFactory(), objOut);
/* 605 */       AbstractExternalizable.compileOrSerialize(this.mChunker.getHmmEstimator(), objOut);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.CharLmHmmChunker
 * JD-Core Version:    0.6.2
 */