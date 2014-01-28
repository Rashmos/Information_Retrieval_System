/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.SortedSet;
/*     */ 
/*     */ public class AutoCompleter
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -6846604550231066369L;
/*     */   final double mTotalCount;
/*     */   final String[] mPhrases;
/*     */   final float[] mPhraseLog2Probs;
/*     */   final char[] mLabels;
/*     */   final int[] mFirstDtr;
/*     */   final int[] mFirstOutcome;
/*     */   final int[] mOutcomes;
/*     */   int mMaxSearchQueueSize;
/*     */   final int mMaxResultsPerPrefix;
/*     */   final WeightedEditDistance mEditDistance;
/*     */   final double mMinScore;
/*     */ 
/*     */   public AutoCompleter(Map<String, ? extends Number> phraseCounts, WeightedEditDistance editDistance, int maxResultsPerPrefix, int maxSearchQueueSize, double minScore)
/*     */   {
/* 155 */     if ((Double.isInfinite(minScore)) || (Double.isNaN(minScore)) || (minScore >= 0.0D))
/*     */     {
/* 158 */       String msg = "Minimum score must be finite and negative. Found minScore=" + minScore;
/*     */ 
/* 160 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 162 */     this.mMinScore = minScore;
/* 163 */     String[] phrases = new String[phraseCounts.size()];
/* 164 */     float[] counts = new float[phraseCounts.size()];
/* 165 */     this.mPhrases = phrases;
/* 166 */     int idx = 0;
/* 167 */     for (Map.Entry entry : phraseCounts.entrySet()) {
/* 168 */       this.mPhrases[idx] = ((String)entry.getKey());
/* 169 */       counts[idx] = ((Number)entry.getValue()).floatValue();
/* 170 */       if ((Float.isNaN(counts[idx])) || (Float.isInfinite(counts[idx])) || (counts[idx] < 0.0F))
/*     */       {
/* 173 */         String msg = "Counts must be finite and non-negative. Found phrase=" + (String)entry.getKey() + " count=" + entry.getValue();
/*     */ 
/* 176 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 178 */       idx++;
/*     */     }
/* 180 */     setMaxSearchQueueSize(maxSearchQueueSize);
/* 181 */     if (maxResultsPerPrefix <= 0) {
/* 182 */       String msg = "Max results per prefix must be positive. Found maxResultsPerPrefix=" + maxResultsPerPrefix;
/*     */ 
/* 184 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */ 
/* 187 */     this.mMaxResultsPerPrefix = maxResultsPerPrefix;
/* 188 */     this.mEditDistance = editDistance;
/*     */ 
/* 190 */     float[] phraseLog2Probs = new float[counts.length];
/* 191 */     this.mPhraseLog2Probs = phraseLog2Probs;
/* 192 */     double totalCount = 0.0D;
/* 193 */     for (int i = 0; i < counts.length; i++)
/* 194 */       totalCount += counts[i];
/* 195 */     this.mTotalCount = totalCount;
/* 196 */     for (int i = 0; i < counts.length; i++) {
/* 197 */       phraseLog2Probs[i] = ((float)com.aliasi.util.Math.log2(counts[i] / totalCount));
/*     */     }
/*     */ 
/* 201 */     int maxLength = maxLength(phrases);
/*     */ 
/* 204 */     int[] numNodes = new int[maxLength];
/* 205 */     String last = "";
/* 206 */     for (String phrase : phrases) {
/* 207 */       for (int pos = prefixMatchLength(phrase, last); 
/* 208 */         pos < phrase.length(); pos++)
/* 209 */         numNodes[pos] += 1;
/* 210 */       last = phrase;
/*     */     }
/*     */ 
/* 213 */     int totalNumNodes = 1;
/* 214 */     for (int pos = 0; pos < maxLength; pos++) {
/* 215 */       totalNumNodes += numNodes[pos];
/*     */     }
/*     */ 
/* 218 */     int[] nextIndex = new int[maxLength];
/* 219 */     nextIndex[0] = 1;
/* 220 */     for (int pos = 1; pos < maxLength; pos++) {
/* 221 */       nextIndex[pos] = (nextIndex[(pos - 1)] + numNodes[(pos - 1)]);
/*     */     }
/*     */ 
/* 224 */     this.mLabels = new char[totalNumNodes];
/* 225 */     this.mFirstDtr = new int[totalNumNodes + 1];
/* 226 */     this.mFirstOutcome = new int[totalNumNodes + 1];
/* 227 */     last = "";
/* 228 */     for (String phrase : phrases) {
/* 229 */       for (int pos = prefixMatchLength(phrase, last); 
/* 230 */         pos < phrase.length(); pos++) {
/* 231 */         this.mLabels[nextIndex[pos]] = phrase.charAt(pos);
/* 232 */         this.mFirstDtr[nextIndex[pos]] = (pos + 1 < nextIndex.length ? nextIndex[(pos + 1)] : totalNumNodes);
/*     */ 
/* 236 */         nextIndex[pos] += 1;
/*     */       }
/* 238 */       last = phrase;
/*     */     }
/*     */ 
/* 242 */     int outcomesLength = 0;
/* 243 */     int prefixCount = 0;
/*     */     int i;
/* 244 */     for (int length = 0; length <= maxLength; length++) {
/* 245 */       for (i = 0; i < phrases.length; ) {
/* 246 */         while ((i < phrases.length) && (phrases[i].length() < length))
/* 247 */           i++;
/* 248 */         if (i >= phrases.length) break;
/* 249 */         String currentPrefix = phrases[i].substring(0, length);
/*     */ 
/* 251 */         for (; (i < phrases.length) && (phrases[i].startsWith(currentPrefix)); i++)
/* 252 */           prefixCount++;
/* 253 */         outcomesLength += java.lang.Math.min(prefixCount, maxResultsPerPrefix);
/* 254 */         prefixCount = 0;
/*     */       }
/*     */     }
/*     */ 
/* 258 */     this.mOutcomes = new int[outcomesLength];
/*     */ 
/* 260 */     BoundedPriorityQueue queue = new BoundedPriorityQueue(ScoredObject.comparator(), maxResultsPerPrefix);
/*     */ 
/* 263 */     int prefixIdx = 0;
/* 264 */     int id = 0;
/*     */     int i;
/* 265 */     for (int length = 0; length <= maxLength; length++) {
/* 266 */       for (i = 0; i < phrases.length; ) {
/* 267 */         while ((i < phrases.length) && (phrases[i].length() < length))
/* 268 */           i++;
/* 269 */         if (i >= phrases.length) break;
/* 270 */         String currentPrefix = phrases[i].substring(0, length);
/*     */ 
/* 272 */         for (; (i < phrases.length) && (phrases[i].startsWith(currentPrefix)); i++)
/* 273 */           queue.offer(new ScoredObject(Integer.valueOf(i), phraseLog2Probs[i]));
/* 274 */         this.mFirstOutcome[(prefixIdx++)] = id;
/* 275 */         for (ScoredObject so : queue)
/* 276 */           this.mOutcomes[(id++)] = ((Integer)so.getObject()).intValue();
/* 277 */         queue.clear();
/*     */       }
/*     */     }
/*     */ 
/* 281 */     this.mFirstDtr[(this.mFirstDtr.length - 1)] = (this.mFirstDtr.length - 1);
/* 282 */     this.mFirstOutcome[(this.mFirstOutcome.length - 1)] = this.mOutcomes.length;
/*     */   }
/*     */ 
/*     */   public int maxResultsPerPrefix()
/*     */   {
/* 323 */     return this.mMaxResultsPerPrefix;
/*     */   }
/*     */ 
/*     */   public WeightedEditDistance editDistance()
/*     */   {
/* 330 */     return this.mEditDistance;
/*     */   }
/*     */ 
/*     */   public Map<String, Float> phraseCountMap()
/*     */   {
/* 344 */     Map counter = new HashMap(this.mPhrases.length * 3 / 2);
/* 345 */     for (int i = 0; i < this.mPhrases.length; i++) {
/* 346 */       counter.put(this.mPhrases[i], Float.valueOf((float)(this.mTotalCount * java.lang.Math.pow(2.0D, this.mPhraseLog2Probs[i]))));
/*     */     }
/* 348 */     return counter;
/*     */   }
/*     */ 
/*     */   public int maxSearchQueueSize()
/*     */   {
/* 359 */     return this.mMaxSearchQueueSize;
/*     */   }
/*     */ 
/*     */   public void setMaxSearchQueueSize(int size)
/*     */   {
/* 375 */     if (size <= 0) {
/* 376 */       String msg = "Max queue size must be positive. Found maxSearchQueueSize=" + size;
/*     */ 
/* 378 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 380 */     this.mMaxSearchQueueSize = size;
/*     */   }
/*     */ 
/*     */   public SortedSet<ScoredObject<String>> complete(String in)
/*     */   {
/* 400 */     Results results = new Results(this.mMaxResultsPerPrefix);
/* 401 */     BoundedPriorityQueue queue = new BoundedPriorityQueue(ScoredObject.comparator(), this.mMaxSearchQueueSize);
/*     */ 
/* 404 */     queue.offer(new SearchState(0, 0, 0.0D, this.mPhraseLog2Probs[0]));
/* 405 */     while (!queue.isEmpty()) {
/* 406 */       SearchState state = (SearchState)queue.poll();
/* 407 */       if (results.dominate(state.mEditCost))
/* 408 */         return results;
/* 409 */       if (state.mInputPosition == in.length()) {
/* 410 */         for (int k = this.mFirstOutcome[state.mTrieNode]; k < this.mFirstOutcome[(state.mTrieNode + 1)]; k++) {
/* 411 */           double score = this.mPhraseLog2Probs[this.mOutcomes[k]] + state.mEditCost;
/*     */ 
/* 414 */           if (score >= this.mMinScore)
/*     */           {
/* 416 */             results.add(this.mPhrases[this.mOutcomes[k]], score);
/*     */           }
/*     */         }
/*     */       } else {
/* 420 */         char c = in.charAt(state.mInputPosition);
/* 421 */         for (int i = this.mFirstDtr[state.mTrieNode]; i < this.mFirstDtr[(state.mTrieNode + 1)]; i++) {
/* 422 */           char d = this.mLabels[i];
/* 423 */           double bestCompletionCost = this.mPhraseLog2Probs[this.mOutcomes[this.mFirstOutcome[i]]];
/*     */ 
/* 425 */           double editCost = c == d ? state.mEditCost : state.mEditCost + this.mEditDistance.substituteWeight(c, d);
/*     */ 
/* 429 */           double score = editCost + bestCompletionCost;
/* 430 */           if ((score >= this.mMinScore) && (!results.dominate(score))) {
/* 431 */             queue.offer(new SearchState(state.mInputPosition + 1, i, editCost, bestCompletionCost));
/*     */           }
/*     */ 
/* 437 */           editCost = state.mEditCost + this.mEditDistance.insertWeight(d);
/* 438 */           score = editCost + bestCompletionCost;
/* 439 */           if ((score >= this.mMinScore) && (!results.dominate(score))) {
/* 440 */             queue.offer(new SearchState(state.mInputPosition, i, editCost, bestCompletionCost));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 448 */         double editCost = state.mEditCost + this.mEditDistance.deleteWeight(c);
/* 449 */         double bestCompletionCost = this.mPhraseLog2Probs[this.mOutcomes[this.mFirstOutcome[state.mTrieNode]]];
/*     */ 
/* 451 */         double score = editCost + bestCompletionCost;
/* 452 */         if ((score >= this.mMinScore) && (!results.dominate(score))) {
/* 453 */           queue.offer(new SearchState(state.mInputPosition + 1, state.mTrieNode, editCost, bestCompletionCost));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 458 */     return results;
/*     */   }
/*     */ 
/*     */   boolean dominated(double score, SortedSet<ScoredObject<String>> results)
/*     */   {
/* 564 */     return (score < this.mMinScore) || ((results.size() == this.mMaxResultsPerPrefix) && (((ScoredObject)results.last()).score() >= score));
/*     */   }
/*     */ 
/*     */   private Object writeReplace()
/*     */   {
/* 570 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static int prefixMatchLength(String x, String y)
/*     */   {
/* 640 */     int len = java.lang.Math.min(x.length(), y.length());
/* 641 */     for (int i = 0; i < len; i++)
/* 642 */       if (x.charAt(i) != y.charAt(i))
/* 643 */         return i;
/* 644 */     return len;
/*     */   }
/*     */ 
/*     */   static int maxLength(String[] xs)
/*     */   {
/* 649 */     int max = -1;
/* 650 */     for (String x : xs)
/* 651 */       if (x.length() > max)
/* 652 */         max = x.length();
/* 653 */     return max;
/*     */   }
/*     */ 
/*     */   static class SearchState
/*     */     implements Scored
/*     */   {
/*     */     final int mInputPosition;
/*     */     final int mTrieNode;
/*     */     final double mEditCost;
/*     */     final double mScore;
/*     */ 
/*     */     SearchState(int inputPosition, int trieNode, double editCost, double bestCompletionCost)
/*     */     {
/* 629 */       this.mInputPosition = inputPosition;
/* 630 */       this.mTrieNode = trieNode;
/* 631 */       this.mEditCost = editCost;
/* 632 */       this.mScore = (editCost + bestCompletionCost);
/*     */     }
/*     */     public double score() {
/* 635 */       return this.mScore;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 2403836255870648306L;
/*     */     final AutoCompleter mAutoCompleter;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 577 */       this(null);
/*     */     }
/*     */     public Serializer(AutoCompleter autoCompleter) {
/* 580 */       this.mAutoCompleter = autoCompleter;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 584 */       objOut.writeObject(this.mAutoCompleter.mEditDistance);
/* 585 */       objOut.writeInt(this.mAutoCompleter.mMaxResultsPerPrefix);
/* 586 */       objOut.writeInt(this.mAutoCompleter.mMaxSearchQueueSize);
/* 587 */       objOut.writeInt(this.mAutoCompleter.mPhrases.length);
/* 588 */       for (int i = 0; i < this.mAutoCompleter.mPhrases.length; i++) {
/* 589 */         objOut.writeUTF(this.mAutoCompleter.mPhrases[i]);
/* 590 */         objOut.writeFloat((float)(this.mAutoCompleter.mTotalCount * java.lang.Math.pow(2.0D, this.mAutoCompleter.mPhraseLog2Probs[i])));
/*     */       }
/*     */ 
/* 594 */       objOut.writeDouble(this.mAutoCompleter.mMinScore);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 601 */       WeightedEditDistance editDistance = (WeightedEditDistance)objIn.readObject();
/* 602 */       int maxResultsPerPrefix = objIn.readInt();
/* 603 */       int maxSearchQueueSize = objIn.readInt();
/* 604 */       int numPhrases = objIn.readInt();
/* 605 */       Map phraseCountMap = new HashMap(numPhrases * 3 / 2);
/* 606 */       for (int i = 0; i < numPhrases; i++) {
/* 607 */         String phrase = objIn.readUTF();
/* 608 */         float count = objIn.readFloat();
/* 609 */         phraseCountMap.put(phrase, Float.valueOf(count));
/*     */       }
/* 611 */       double minScore = objIn.readDouble();
/* 612 */       return new AutoCompleter(phraseCountMap, editDistance, maxResultsPerPrefix, maxSearchQueueSize, minScore);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Results extends AbstractSet<ScoredObject<String>>
/*     */     implements SortedSet<ScoredObject<String>>
/*     */   {
/*     */     private final String[] mResults;
/*     */     private final double[] mScores;
/* 467 */     private int mSize = 0;
/*     */ 
/* 469 */     Results(int maxSize) { this.mResults = new String[maxSize];
/* 470 */       this.mScores = new double[maxSize]; }
/*     */ 
/*     */     public boolean dominate(double score) {
/* 473 */       return (full()) && (this.mScores[(this.mSize - 1)] >= score);
/*     */     }
/*     */     public void add(String s, double score) {
/* 476 */       for (int i = 0; i < this.mSize; i++) {
/* 477 */         if (score > this.mScores[i]) {
/* 478 */           tamp(i, s);
/* 479 */           this.mScores[i] = score;
/* 480 */           this.mResults[i] = s;
/* 481 */           return;
/*     */         }
/* 483 */         if (this.mResults[i].equals(s))
/* 484 */           return;
/*     */       }
/* 486 */       if (this.mSize < this.mResults.length) {
/* 487 */         this.mResults[this.mSize] = s;
/* 488 */         this.mScores[this.mSize] = score;
/* 489 */         this.mSize += 1;
/*     */       }
/*     */     }
/*     */ 
/*     */     void tamp(int i, String s) {
/* 494 */       for (int pos = i; pos < this.mSize; pos++) {
/* 495 */         if (this.mResults[pos].equals(s)) {
/*     */           while (true) { pos++; if (pos >= this.mSize) break;
/* 497 */             this.mResults[(pos - 1)] = this.mResults[pos];
/* 498 */             this.mScores[(pos - 1)] = this.mScores[pos];
/*     */           }
/* 500 */           return;
/*     */         }
/*     */       }
/*     */ 
/* 504 */       int pos = this.mSize < this.mResults.length ? this.mSize++ : this.mSize - 1;
/*     */       while (true) { pos--; if (pos < i) break;
/* 506 */         this.mResults[(pos + 1)] = this.mResults[pos];
/* 507 */         this.mScores[(pos + 1)] = this.mScores[pos]; }
/*     */     }
/*     */ 
/*     */     public boolean full() {
/* 511 */       return this.mSize == this.mResults.length;
/*     */     }
/*     */ 
/*     */     public int size() {
/* 515 */       return this.mSize;
/*     */     }
/*     */     public ScoredObject<String> first() {
/* 518 */       if (this.mSize == 0)
/* 519 */         throw new NoSuchElementException();
/* 520 */       return new ScoredObject(this.mResults[0], this.mScores[0]);
/*     */     }
/*     */     public ScoredObject<String> last() {
/* 523 */       if (this.mSize == 0)
/* 524 */         throw new NoSuchElementException();
/* 525 */       return new ScoredObject(this.mResults[(this.mSize - 1)], this.mScores[(this.mSize - 1)]);
/*     */     }
/*     */ 
/*     */     public SortedSet<ScoredObject<String>> headSet(ScoredObject<String> from) {
/* 529 */       return null;
/*     */     }
/*     */     public SortedSet<ScoredObject<String>> tailSet(ScoredObject<String> from) {
/* 532 */       return null;
/*     */     }
/*     */ 
/*     */     public SortedSet<ScoredObject<String>> subSet(ScoredObject<String> from, ScoredObject<String> to) {
/* 536 */       return null;
/*     */     }
/*     */     public Comparator<ScoredObject<String>> comparator() {
/* 539 */       return ScoredObject.reverseComparator();
/*     */     }
/*     */ 
/*     */     public Iterator<ScoredObject<String>> iterator()
/*     */     {
/* 544 */       return new ResultsIterator();
/*     */     }
/*     */ 
/*     */     class ResultsIterator implements Iterator<ScoredObject<String>> {
/* 548 */       int mPosition = 0;
/*     */ 
/*     */       ResultsIterator() {  } 
/* 550 */       public boolean hasNext() { return this.mPosition < AutoCompleter.Results.this.mSize; }
/*     */ 
/*     */       public ScoredObject<String> next() {
/* 553 */         this.mPosition += 1;
/* 554 */         return new ScoredObject(AutoCompleter.Results.this.mResults[(this.mPosition - 1)], AutoCompleter.Results.this.mScores[(this.mPosition - 1)]);
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 558 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.AutoCompleter
 * JD-Core Version:    0.6.2
 */