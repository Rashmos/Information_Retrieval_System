/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.io.BitInput;
/*     */ import com.aliasi.io.BitOutput;
/*     */ import com.aliasi.util.ObjectToCounterMap;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TrieCharSeqCounter
/*     */   implements CharSeqCounter
/*     */ {
/*  63 */   Node mRootNode = NodeFactory.createNode(0L);
/*     */   final int mMaxLength;
/* 645 */   static final Node[] EMPTY_NODE_ARRAY = new Node[0];
/*     */ 
/*     */   public TrieCharSeqCounter(int maxLength)
/*     */   {
/*  76 */     if (maxLength < 0) {
/*  77 */       String msg = "Max length must be >= 0. Found length=" + maxLength;
/*     */ 
/*  79 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  81 */     this.mMaxLength = maxLength;
/*     */   }
/*     */ 
/*     */   public long count(char[] cs, int start, int end)
/*     */   {
/*  87 */     Strings.checkArgsStartEnd(cs, start, end);
/*  88 */     return this.mRootNode.count(cs, start, end);
/*     */   }
/*     */ 
/*     */   public long extensionCount(char[] cs, int start, int end) {
/*  92 */     Strings.checkArgsStartEnd(cs, start, end);
/*  93 */     return this.mRootNode.contextCount(cs, start, end);
/*     */   }
/*     */ 
/*     */   public char[] observedCharacters() {
/*  97 */     return com.aliasi.util.Arrays.copy(this.mRootNode.outcomes(new char[0], 0, 0));
/*     */   }
/*     */ 
/*     */   public char[] charactersFollowing(char[] cs, int start, int end) {
/* 101 */     Strings.checkArgsStartEnd(cs, start, end);
/* 102 */     return com.aliasi.util.Arrays.copy(this.mRootNode.outcomes(cs, start, end));
/*     */   }
/*     */ 
/*     */   public int numCharactersFollowing(char[] cs, int start, int end) {
/* 106 */     Strings.checkArgsStartEnd(cs, start, end);
/* 107 */     return this.mRootNode.numOutcomes(cs, start, end);
/*     */   }
/*     */ 
/*     */   public long totalSequenceCount()
/*     */   {
/* 118 */     long sum = 0L;
/* 119 */     long[][] uniqueTotals = uniqueTotalNGramCount();
/* 120 */     for (int i = 0; i < uniqueTotals.length; i++)
/* 121 */       sum += uniqueTotals[i][1];
/* 122 */     return sum;
/*     */   }
/*     */ 
/*     */   public long totalSequenceCount(int length)
/*     */   {
/* 133 */     return this.mRootNode.totalNGramCount(length);
/*     */   }
/*     */ 
/*     */   public long uniqueSequenceCount()
/*     */   {
/* 143 */     return this.mRootNode.size();
/*     */   }
/*     */ 
/*     */   public long uniqueSequenceCount(int nGramOrder)
/*     */   {
/* 154 */     return this.mRootNode.uniqueNGramCount(nGramOrder);
/*     */   }
/*     */ 
/*     */   public void prune(int minCount)
/*     */   {
/* 168 */     if (minCount < 1) {
/* 169 */       String msg = "Prune minimum count must be more than 1. Found minCount=" + minCount;
/*     */ 
/* 171 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 173 */     this.mRootNode = this.mRootNode.prune(minCount);
/* 174 */     if (this.mRootNode == null)
/* 175 */       this.mRootNode = NodeFactory.createNode(0L);
/*     */   }
/*     */ 
/*     */   public int[] nGramFrequencies(int nGramOrder)
/*     */   {
/* 189 */     List counts = countsList(nGramOrder);
/* 190 */     int[] result = new int[counts.size()];
/* 191 */     for (int i = 0; i < result.length; i++)
/* 192 */       result[i] = ((Long)counts.get(i)).intValue();
/* 193 */     java.util.Arrays.sort(result);
/* 194 */     for (int i = result.length / 2; i >= 0; i--) {
/* 195 */       int iOpp = result.length - i - 1;
/* 196 */       int tmp = result[i];
/* 197 */       result[i] = result[iOpp];
/* 198 */       result[iOpp] = tmp;
/*     */     }
/* 200 */     return result;
/*     */   }
/*     */ 
/*     */   public long[][] uniqueTotalNGramCount()
/*     */   {
/* 228 */     long[][] result = new long[this.mMaxLength + 1][2];
/* 229 */     this.mRootNode.addNGramCounts(result, 0);
/* 230 */     return result;
/*     */   }
/*     */ 
/*     */   public ObjectToCounterMap<String> topNGrams(int nGramOrder, int maxReturn)
/*     */   {
/* 247 */     NBestCounter counter = new NBestCounter(maxReturn, true);
/* 248 */     this.mRootNode.topNGrams(counter, new char[nGramOrder], 0, nGramOrder);
/* 249 */     return counter.toObjectToCounter();
/*     */   }
/*     */ 
/*     */   public long count(CharSequence cSeq)
/*     */   {
/* 261 */     return count(com.aliasi.util.Arrays.toArray(cSeq), 0, cSeq.length());
/*     */   }
/*     */ 
/*     */   public long extensionCount(CharSequence cSeq)
/*     */   {
/* 274 */     return this.mRootNode.contextCount(com.aliasi.util.Arrays.toArray(cSeq), 0, cSeq.length());
/*     */   }
/*     */ 
/*     */   public void incrementSubstrings(char[] cs, int start, int end)
/*     */   {
/* 289 */     incrementSubstrings(cs, start, end, 1);
/*     */   }
/*     */ 
/*     */   public void incrementSubstrings(char[] cs, int start, int end, int count)
/*     */   {
/* 306 */     Strings.checkArgsStartEnd(cs, start, end);
/*     */ 
/* 308 */     for (int i = start; i + this.mMaxLength <= end; i++) {
/* 309 */       incrementPrefixes(cs, i, i + this.mMaxLength, count);
/*     */     }
/* 311 */     for (int i = Math.max(start, end - this.mMaxLength + 1); i < end; i++)
/* 312 */       incrementPrefixes(cs, i, end, count);
/*     */   }
/*     */ 
/*     */   public void incrementSubstrings(CharSequence cSeq)
/*     */   {
/* 323 */     incrementSubstrings(cSeq, 1);
/*     */   }
/*     */ 
/*     */   public void incrementSubstrings(CharSequence cSeq, int count)
/*     */   {
/* 335 */     incrementSubstrings(com.aliasi.util.Arrays.toArray(cSeq), 0, cSeq.length(), count);
/*     */   }
/*     */ 
/*     */   public void incrementPrefixes(char[] cs, int start, int end)
/*     */   {
/* 351 */     incrementPrefixes(cs, start, end, 1);
/*     */   }
/*     */ 
/*     */   public void incrementPrefixes(char[] cs, int start, int end, int count)
/*     */   {
/* 369 */     Strings.checkArgsStartEnd(cs, start, end);
/* 370 */     this.mRootNode = this.mRootNode.increment(cs, start, end, count);
/*     */   }
/*     */ 
/*     */   public void decrementSubstrings(char[] cs, int start, int end)
/*     */   {
/* 406 */     Strings.checkArgsStartEnd(cs, start, end);
/* 407 */     for (int i = start; i < end; i++)
/* 408 */       for (int j = i; j <= end; j++)
/* 409 */         this.mRootNode = this.mRootNode.decrement(cs, i, j);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 424 */     return this.mRootNode.toString();
/*     */   }
/*     */ 
/*     */   void toStringBuilder(StringBuilder sb) {
/* 428 */     this.mRootNode.toString(sb, 0);
/*     */   }
/*     */ 
/*     */   public void decrementUnigram(char c)
/*     */   {
/* 440 */     decrementUnigram(c, 1);
/*     */   }
/*     */ 
/*     */   public void decrementUnigram(char c, int count)
/*     */   {
/* 454 */     this.mRootNode = this.mRootNode.decrement(new char[] { c }, 0, 1, count);
/*     */   }
/*     */ 
/*     */   private List<Long> countsList(int nGramOrder) {
/* 458 */     List accum = new ArrayList();
/* 459 */     this.mRootNode.addCounts(accum, nGramOrder);
/* 460 */     return accum;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 485 */     BitOutput bitOut = new BitOutput(out);
/* 486 */     bitOut.writeDelta(this.mMaxLength + 1L);
/* 487 */     TrieWriter writer = new BitTrieWriter(bitOut);
/* 488 */     writeCounter(this, writer, this.mMaxLength);
/* 489 */     bitOut.flush();
/*     */   }
/*     */ 
/*     */   public static void writeCounter(CharSeqCounter counter, TrieWriter writer, int maxNGram)
/*     */     throws IOException
/*     */   {
/* 507 */     writeCounter(new char[maxNGram], 0, counter, writer);
/*     */   }
/*     */ 
/*     */   public static TrieCharSeqCounter readFrom(InputStream in)
/*     */     throws IOException
/*     */   {
/* 527 */     BitInput bitIn = new BitInput(in);
/* 528 */     int maxNGram = (int)(bitIn.readDelta() - 1L);
/* 529 */     BitTrieReader reader = new BitTrieReader(bitIn);
/* 530 */     return readCounter(reader, maxNGram);
/*     */   }
/*     */ 
/*     */   public static TrieCharSeqCounter readCounter(TrieReader reader, int maxNGram)
/*     */     throws IOException
/*     */   {
/* 547 */     TrieCharSeqCounter counter = new TrieCharSeqCounter(maxNGram);
/* 548 */     counter.mRootNode = readNode(reader, 0, maxNGram);
/* 549 */     return counter;
/*     */   }
/*     */ 
/*     */   static void writeCounter(char[] cs, int pos, CharSeqCounter counter, TrieWriter writer)
/*     */     throws IOException
/*     */   {
/* 557 */     long count = counter.count(cs, 0, pos);
/* 558 */     writer.writeCount(count);
/* 559 */     if (pos < cs.length) {
/* 560 */       char[] csNext = counter.charactersFollowing(cs, 0, pos);
/* 561 */       for (int i = 0; i < csNext.length; i++) {
/* 562 */         writer.writeSymbol(csNext[i]);
/* 563 */         cs[pos] = csNext[i];
/* 564 */         writeCounter(cs, pos + 1, counter, writer);
/*     */       }
/*     */     }
/* 567 */     writer.writeSymbol(-1L);
/*     */   }
/*     */ 
/*     */   private static void skipNode(TrieReader reader)
/*     */     throws IOException
/*     */   {
/* 575 */     reader.readCount();
/* 576 */     while (reader.readSymbol() != -1L)
/* 577 */       skipNode(reader);
/*     */   }
/*     */ 
/*     */   private static Node readNode(TrieReader reader, int depth, int maxDepth)
/*     */     throws IOException
/*     */   {
/* 583 */     if (depth > maxDepth) {
/* 584 */       skipNode(reader);
/* 585 */       return null;
/*     */     }
/*     */ 
/* 588 */     long count = reader.readCount();
/*     */ 
/* 590 */     int depthPlus1 = depth + 1;
/*     */ 
/* 592 */     long sym1 = reader.readSymbol();
/*     */ 
/* 595 */     if (sym1 == -1L) {
/* 596 */       return NodeFactory.createNode(count);
/*     */     }
/*     */ 
/* 599 */     Node node1 = readNode(reader, depthPlus1, maxDepth);
/* 600 */     long sym2 = reader.readSymbol();
/* 601 */     if (sym2 == -1L) {
/* 602 */       return NodeFactory.createNodeFold((char)(int)sym1, node1, count);
/*     */     }
/*     */ 
/* 605 */     Node node2 = readNode(reader, depthPlus1, maxDepth);
/* 606 */     long sym3 = reader.readSymbol();
/* 607 */     if (sym3 == -1L) {
/* 608 */       return NodeFactory.createNode((char)(int)sym1, node1, (char)(int)sym2, node2, count);
/*     */     }
/*     */ 
/* 612 */     Node node3 = readNode(reader, depthPlus1, maxDepth);
/* 613 */     long sym4 = reader.readSymbol();
/* 614 */     if (sym4 == -1L) {
/* 615 */       return NodeFactory.createNode((char)(int)sym1, node1, (char)(int)sym2, node2, (char)(int)sym3, node3, count);
/*     */     }
/*     */ 
/* 619 */     Node node4 = readNode(reader, depthPlus1, maxDepth);
/*     */ 
/* 622 */     StringBuilder cBuf = new StringBuilder();
/* 623 */     cBuf.append((char)(int)sym1);
/* 624 */     cBuf.append((char)(int)sym2);
/* 625 */     cBuf.append((char)(int)sym3);
/* 626 */     cBuf.append((char)(int)sym4);
/*     */ 
/* 628 */     List nodeList = new ArrayList();
/* 629 */     nodeList.add(node1);
/* 630 */     nodeList.add(node2);
/* 631 */     nodeList.add(node3);
/* 632 */     nodeList.add(node4);
/*     */     long sym;
/* 636 */     while ((sym = reader.readSymbol()) != -1L) {
/* 637 */       cBuf.append((char)(int)sym);
/* 638 */       nodeList.add(readNode(reader, depthPlus1, maxDepth));
/*     */     }
/* 640 */     Node[] nodes = (Node[])nodeList.toArray(EMPTY_NODE_ARRAY);
/* 641 */     char[] cs = Strings.toCharArray(cBuf);
/* 642 */     return NodeFactory.createNode(cs, nodes, count);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.TrieCharSeqCounter
 * JD-Core Version:    0.6.2
 */