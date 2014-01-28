/*     */ package com.aliasi.lm;
/*     */ 
/*     */ import com.aliasi.corpus.ObjectHandler;
/*     */ import com.aliasi.io.BitInput;
/*     */ import com.aliasi.io.BitOutput;
/*     */ import com.aliasi.stats.Model;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Arrays;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public class NGramProcessLM
/*     */   implements Model<CharSequence>, LanguageModel.Process, LanguageModel.Conditional, LanguageModel.Dynamic, ObjectHandler<CharSequence>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -2865886217715962249L;
/*     */   private final TrieCharSeqCounter mTrieCharSeqCounter;
/*     */   private final int mMaxNGram;
/*     */   private double mLambdaFactor;
/*     */   private int mNumChars;
/*     */   private double mUniformEstimate;
/*     */   private double mLog2UniformEstimate;
/*     */ 
/*     */   public NGramProcessLM(int maxNGram)
/*     */   {
/* 259 */     this(maxNGram, 65535);
/*     */   }
/*     */ 
/*     */   public NGramProcessLM(int maxNGram, int numChars)
/*     */   {
/* 277 */     this(maxNGram, numChars, maxNGram);
/*     */   }
/*     */ 
/*     */   public NGramProcessLM(int maxNGram, int numChars, double lambdaFactor)
/*     */   {
/* 298 */     this(numChars, lambdaFactor, new TrieCharSeqCounter(maxNGram));
/*     */   }
/*     */ 
/*     */   public NGramProcessLM(int numChars, double lambdaFactor, TrieCharSeqCounter counter)
/*     */   {
/* 322 */     this.mMaxNGram = counter.mMaxLength;
/* 323 */     setLambdaFactor(lambdaFactor);
/* 324 */     setNumChars(numChars);
/* 325 */     this.mTrieCharSeqCounter = counter;
/*     */   }
/*     */ 
/*     */   public void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 360 */     BitOutput bitOut = new BitOutput(out);
/* 361 */     writeTo(bitOut);
/* 362 */     bitOut.flush();
/*     */   }
/*     */ 
/*     */   void writeTo(BitOutput bitOut) throws IOException {
/* 366 */     bitOut.writeDelta(this.mMaxNGram);
/* 367 */     bitOut.writeDelta(this.mNumChars);
/* 368 */     bitOut.writeDelta((int)(this.mLambdaFactor * 1000000.0D));
/* 369 */     BitTrieWriter trieWriter = new BitTrieWriter(bitOut);
/* 370 */     TrieCharSeqCounter.writeCounter(this.mTrieCharSeqCounter, trieWriter, this.mMaxNGram);
/*     */   }
/*     */ 
/*     */   public static NGramProcessLM readFrom(InputStream in)
/*     */     throws IOException
/*     */   {
/* 385 */     BitInput bitIn = new BitInput(in);
/* 386 */     return readFrom(bitIn);
/*     */   }
/*     */ 
/*     */   static NGramProcessLM readFrom(BitInput bitIn) throws IOException {
/* 390 */     int maxNGram = (int)bitIn.readDelta();
/* 391 */     int numChars = (int)bitIn.readDelta();
/* 392 */     double lambdaFactor = bitIn.readDelta() / 1000000.0D;
/* 393 */     BitTrieReader trieReader = new BitTrieReader(bitIn);
/* 394 */     TrieCharSeqCounter counter = TrieCharSeqCounter.readCounter(trieReader, maxNGram);
/*     */ 
/* 396 */     return new NGramProcessLM(numChars, lambdaFactor, counter);
/*     */   }
/*     */ 
/*     */   public double log2Prob(CharSequence cSeq) {
/* 400 */     return log2Estimate(cSeq);
/*     */   }
/*     */ 
/*     */   public double prob(CharSequence cSeq) {
/* 404 */     return java.lang.Math.pow(2.0D, log2Estimate(cSeq));
/*     */   }
/*     */ 
/*     */   public final double log2Estimate(CharSequence cSeq) {
/* 408 */     char[] cs = Strings.toCharArray(cSeq);
/* 409 */     return log2Estimate(cs, 0, cs.length);
/*     */   }
/*     */ 
/*     */   public final double log2Estimate(char[] cs, int start, int end) {
/* 413 */     Strings.checkArgsStartEnd(cs, start, end);
/* 414 */     double sum = 0.0D;
/* 415 */     for (int i = start + 1; i <= end; i++)
/* 416 */       sum += log2ConditionalEstimate(cs, start, i);
/* 417 */     return sum;
/*     */   }
/*     */ 
/*     */   public void train(CharSequence cSeq) {
/* 421 */     train(cSeq, 1);
/*     */   }
/*     */ 
/*     */   public void train(CharSequence cSeq, int incr) {
/* 425 */     char[] cs = Strings.toCharArray(cSeq);
/* 426 */     train(cs, 0, cs.length, incr);
/*     */   }
/*     */ 
/*     */   public void train(char[] cs, int start, int end) {
/* 430 */     train(cs, start, end, 1);
/*     */   }
/*     */   public void train(char[] cs, int start, int end, int incr) {
/* 433 */     Strings.checkArgsStartEnd(cs, start, end);
/* 434 */     this.mTrieCharSeqCounter.incrementSubstrings(cs, start, end, incr);
/*     */   }
/*     */ 
/*     */   public void handle(CharSequence cSeq)
/*     */   {
/* 445 */     train(cSeq);
/*     */   }
/*     */ 
/*     */   public void trainConditional(char[] cs, int start, int end, int condEnd)
/*     */   {
/* 497 */     Strings.checkArgsStartEnd(cs, start, end);
/* 498 */     Strings.checkArgsStartEnd(cs, start, condEnd);
/* 499 */     if (condEnd > end) {
/* 500 */       String msg = "Conditional end must be < end. Found condEnd=" + condEnd + " end=" + end;
/*     */ 
/* 504 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 506 */     if (condEnd == end) return;
/* 507 */     this.mTrieCharSeqCounter.incrementSubstrings(cs, start, end);
/* 508 */     this.mTrieCharSeqCounter.decrementSubstrings(cs, start, condEnd);
/*     */   }
/*     */ 
/*     */   public char[] observedCharacters()
/*     */   {
/* 513 */     return this.mTrieCharSeqCounter.observedCharacters();
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 537 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(CharSequence cSeq) {
/* 541 */     return log2ConditionalEstimate(cSeq, this.mMaxNGram, this.mLambdaFactor);
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(char[] cs, int start, int end) {
/* 545 */     return log2ConditionalEstimate(cs, start, end, this.mMaxNGram, this.mLambdaFactor);
/*     */   }
/*     */ 
/*     */   public TrieCharSeqCounter substringCounter()
/*     */   {
/* 556 */     return this.mTrieCharSeqCounter;
/*     */   }
/*     */ 
/*     */   public int maxNGram()
/*     */   {
/* 565 */     return this.mMaxNGram;
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(CharSequence cSeq, int maxNGram, double lambdaFactor)
/*     */   {
/* 588 */     char[] cs = Strings.toCharArray(cSeq);
/* 589 */     return log2ConditionalEstimate(cs, 0, cs.length, maxNGram, lambdaFactor);
/*     */   }
/*     */ 
/*     */   public double log2ConditionalEstimate(char[] cs, int start, int end, int maxNGram, double lambdaFactor)
/*     */   {
/* 609 */     if (end <= start) {
/* 610 */       String msg = "Conditional estimates require at least one character.";
/* 611 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 613 */     Strings.checkArgsStartEnd(cs, start, end);
/* 614 */     checkMaxNGram(maxNGram);
/* 615 */     checkLambdaFactor(lambdaFactor);
/* 616 */     int maxUsableNGram = java.lang.Math.min(maxNGram, this.mMaxNGram);
/* 617 */     if (start == end) return 0.0D;
/* 618 */     double currentEstimate = this.mUniformEstimate;
/* 619 */     int contextEnd = end - 1;
/* 620 */     int longestContextStart = java.lang.Math.max(start, end - maxUsableNGram);
/* 621 */     for (int currentContextStart = contextEnd; 
/* 622 */       currentContextStart >= longestContextStart; 
/* 623 */       currentContextStart--) {
/* 624 */       long contextCount = this.mTrieCharSeqCounter.extensionCount(cs, currentContextStart, contextEnd);
/*     */ 
/* 626 */       if (contextCount == 0L) break;
/* 627 */       long outcomeCount = this.mTrieCharSeqCounter.count(cs, currentContextStart, end);
/* 628 */       double lambda = lambda(cs, currentContextStart, contextEnd, lambdaFactor);
/* 629 */       currentEstimate = lambda * (outcomeCount / contextCount) + (1.0D - lambda) * currentEstimate;
/*     */     }
/*     */ 
/* 633 */     return com.aliasi.util.Math.log2(currentEstimate);
/*     */   }
/*     */ 
/*     */   double lambda(char[] cs, int start, int end)
/*     */   {
/* 650 */     return lambda(cs, start, end, getLambdaFactor());
/*     */   }
/*     */ 
/*     */   double lambda(char[] cs, int start, int end, double lambdaFactor)
/*     */   {
/* 666 */     checkLambdaFactor(lambdaFactor);
/* 667 */     Strings.checkArgsStartEnd(cs, start, end);
/* 668 */     double count = this.mTrieCharSeqCounter.extensionCount(cs, start, end);
/* 669 */     if (count <= 0.0D) return 0.0D;
/* 670 */     double numOutcomes = this.mTrieCharSeqCounter.numCharactersFollowing(cs, start, end);
/* 671 */     return lambda(count, numOutcomes, lambdaFactor);
/*     */   }
/*     */ 
/*     */   public double getLambdaFactor()
/*     */   {
/* 684 */     return this.mLambdaFactor;
/*     */   }
/*     */ 
/*     */   public final void setLambdaFactor(double lambdaFactor)
/*     */   {
/* 698 */     checkLambdaFactor(lambdaFactor);
/* 699 */     this.mLambdaFactor = lambdaFactor;
/*     */   }
/*     */ 
/*     */   public final void setNumChars(int numChars)
/*     */   {
/* 715 */     checkNumChars(numChars);
/* 716 */     this.mNumChars = numChars;
/* 717 */     this.mUniformEstimate = (1.0D / this.mNumChars);
/* 718 */     this.mLog2UniformEstimate = com.aliasi.util.Math.log2(this.mUniformEstimate);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 729 */     StringBuilder sb = new StringBuilder();
/* 730 */     toStringBuilder(sb);
/* 731 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   void toStringBuilder(StringBuilder sb) {
/* 735 */     sb.append("Max NGram=" + this.mMaxNGram + " ");
/* 736 */     sb.append("Num characters=" + this.mNumChars + "\n");
/* 737 */     sb.append("Trie of counts=\n");
/* 738 */     this.mTrieCharSeqCounter.toStringBuilder(sb);
/*     */   }
/*     */ 
/*     */   void decrementUnigram(char c)
/*     */   {
/* 743 */     decrementUnigram(c, 1);
/*     */   }
/*     */ 
/*     */   void decrementUnigram(char c, int count) {
/* 747 */     this.mTrieCharSeqCounter.decrementUnigram(c, count);
/*     */   }
/*     */ 
/*     */   private double lambda(double count, double numOutcomes, double lambdaFactor)
/*     */   {
/* 752 */     return count / (count + lambdaFactor * numOutcomes);
/*     */   }
/*     */ 
/*     */   private double lambda(Node node)
/*     */   {
/* 757 */     double count = node.contextCount(Strings.EMPTY_CHAR_ARRAY, 0, 0);
/* 758 */     double numOutcomes = node.numOutcomes(Strings.EMPTY_CHAR_ARRAY, 0, 0);
/* 759 */     return lambda(count, numOutcomes, this.mLambdaFactor);
/*     */   }
/*     */ 
/*     */   private int lastInternalNodeIndex() {
/* 763 */     int last = 1;
/* 764 */     LinkedList queue = new LinkedList();
/* 765 */     queue.add(this.mTrieCharSeqCounter.mRootNode);
/* 766 */     for (int i = 1; !queue.isEmpty(); i++) {
/* 767 */       Node node = (Node)queue.removeFirst();
/* 768 */       if (node.numOutcomes(Strings.EMPTY_CHAR_ARRAY, 0, 0) > 0)
/*     */       {
/* 770 */         last = i;
/* 771 */       }node.addDaughters(queue);
/*     */     }
/* 773 */     return last - 1;
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 778 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static void checkLambdaFactor(double lambdaFactor)
/*     */   {
/* 875 */     if ((lambdaFactor < 0.0D) || (Double.isInfinite(lambdaFactor)) || (Double.isNaN(lambdaFactor)))
/*     */     {
/* 878 */       String msg = "Lambda factor must be ordinary non-negative double. Found lambdaFactor=" + lambdaFactor;
/*     */ 
/* 880 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void checkMaxNGram(int maxNGram) {
/* 885 */     if (maxNGram < 1) {
/* 886 */       String msg = "Maximum n-gram must be greater than zero. Found max n-gram=" + maxNGram;
/*     */ 
/* 888 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkNumChars(int numChars) {
/* 893 */     if ((numChars < 0) || (numChars > 65535)) {
/* 894 */       String msg = "Number of characters must be > 0 and  must be less than Character.MAX_VALUE Found numChars=" + numChars;
/*     */ 
/* 897 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = -3623859317152451545L;
/*     */     final NGramProcessLM mLM;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 806 */       this(null);
/*     */     }
/*     */     public Externalizer(NGramProcessLM lm) {
/* 809 */       this.mLM = lm;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in) throws IOException {
/* 813 */       return new CompiledNGramProcessLM(in);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput dataOut) throws IOException {
/* 817 */       dataOut.writeInt(this.mLM.mMaxNGram);
/*     */ 
/* 819 */       dataOut.writeFloat((float)this.mLM.mLog2UniformEstimate);
/*     */ 
/* 821 */       long numNodes = this.mLM.mTrieCharSeqCounter.uniqueSequenceCount();
/* 822 */       if (numNodes > 2147483647L) {
/* 823 */         String msg = "Maximum number of compiled nodes is Integer.MAX_VALUE = 2147483647 Found number of nodes=" + numNodes;
/*     */ 
/* 826 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 828 */       dataOut.writeInt((int)numNodes);
/*     */ 
/* 830 */       int lastInternalNodeIndex = this.mLM.lastInternalNodeIndex();
/* 831 */       dataOut.writeInt(lastInternalNodeIndex);
/*     */ 
/* 834 */       dataOut.writeChar(65535);
/* 835 */       dataOut.writeFloat((float)this.mLM.mLog2UniformEstimate);
/* 836 */       double oneMinusLambda = 1.0D - this.mLM.lambda(this.mLM.mTrieCharSeqCounter.mRootNode);
/*     */ 
/* 838 */       float log2OneMinusLambda = Double.isNaN(oneMinusLambda) ? 0.0F : (float)com.aliasi.util.Math.log2(oneMinusLambda);
/*     */ 
/* 840 */       dataOut.writeFloat(log2OneMinusLambda);
/* 841 */       dataOut.writeInt(1);
/* 842 */       char[] cs = this.mLM.mTrieCharSeqCounter.observedCharacters();
/*     */ 
/* 844 */       LinkedList queue = new LinkedList();
/* 845 */       for (int i = 0; i < cs.length; i++)
/* 846 */         queue.add(new char[] { cs[i] });
/* 847 */       for (int index = 1; !queue.isEmpty(); index++) {
/* 848 */         char[] nGram = (char[])queue.removeFirst();
/* 849 */         char c = nGram[(nGram.length - 1)];
/* 850 */         dataOut.writeChar(c);
/*     */ 
/* 852 */         float logConditionalEstimate = (float)this.mLM.log2ConditionalEstimate(nGram, 0, nGram.length);
/*     */ 
/* 854 */         dataOut.writeFloat(logConditionalEstimate);
/*     */ 
/* 856 */         if (index <= lastInternalNodeIndex) {
/* 857 */           double oneMinusLambda2 = 1.0D - this.mLM.lambda(nGram, 0, nGram.length);
/*     */ 
/* 859 */           float log2OneMinusLambda2 = (float)com.aliasi.util.Math.log2(oneMinusLambda2);
/*     */ 
/* 861 */           dataOut.writeFloat(log2OneMinusLambda2);
/* 862 */           int firstChildIndex = index + queue.size() + 1;
/* 863 */           dataOut.writeInt(firstChildIndex);
/*     */         }
/* 865 */         char[] cs2 = this.mLM.mTrieCharSeqCounter.charactersFollowing(nGram, 0, nGram.length);
/*     */ 
/* 868 */         for (int i = 0; i < cs2.length; i++)
/* 869 */           queue.add(Arrays.concatenate(nGram, cs2[i]));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Serializer
/*     */     implements Externalizable
/*     */   {
/*     */     static final long serialVersionUID = -7101238964823109652L;
/*     */     NGramProcessLM mLM;
/*     */ 
/*     */     public Serializer()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Serializer(NGramProcessLM lm)
/*     */     {
/* 789 */       this.mLM = lm;
/*     */     }
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 792 */       this.mLM.writeTo((OutputStream)out);
/*     */     }
/*     */     public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 795 */       this.mLM = NGramProcessLM.readFrom((InputStream)in);
/*     */     }
/*     */     public Object readResolve() {
/* 798 */       return this.mLM;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.NGramProcessLM
 * JD-Core Version:    0.6.2
 */