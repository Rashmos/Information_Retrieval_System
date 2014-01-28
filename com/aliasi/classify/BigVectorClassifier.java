/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class BigVectorClassifier
/*     */   implements ScoredClassifier<Vector>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 5149230080619243511L;
/*     */   private final int[] mTermIndexes;
/*     */   private final int[] mDocumentIds;
/*     */   private final float[] mScores;
/*     */   private final String[] mCategories;
/*     */   private int mMaxResults;
/*     */ 
/*     */   public BigVectorClassifier(Vector[] termVectors, int maxResults)
/*     */   {
/* 121 */     this(termVectors, categoriesFor(termVectors), maxResults);
/*     */   }
/*     */ 
/*     */   public BigVectorClassifier(Vector[] termVectors, String[] categories, int maxResults)
/*     */   {
/* 136 */     this.mCategories = categories;
/*     */ 
/* 138 */     this.mTermIndexes = new int[termVectors.length];
/* 139 */     int size = termVectors.length;
/* 140 */     for (Vector termVector : termVectors)
/* 141 */       size += termVector.nonZeroDimensions().length;
/* 142 */     this.mDocumentIds = new int[size];
/* 143 */     this.mScores = new float[size];
/* 144 */     int pos = 0;
/* 145 */     for (int i = 0; i < termVectors.length; i++) {
/* 146 */       this.mTermIndexes[i] = pos;
/* 147 */       Vector termVector = termVectors[i];
/* 148 */       int[] nzDims = termVector.nonZeroDimensions();
/* 149 */       for (int k = 0; k < nzDims.length; k++) {
/* 150 */         int j = nzDims[k];
/* 151 */         this.mDocumentIds[pos] = j;
/* 152 */         this.mScores[pos] = ((float)termVector.value(j));
/* 153 */         pos++;
/*     */       }
/* 155 */       this.mDocumentIds[pos] = -1;
/* 156 */       pos++;
/*     */     }
/* 158 */     setMaxResults(maxResults);
/*     */   }
/*     */ 
/*     */   BigVectorClassifier(int[] termIndexes, int[] documentIds, float[] scores, String[] categories, int maxResults)
/*     */   {
/* 179 */     this.mTermIndexes = termIndexes;
/* 180 */     this.mDocumentIds = documentIds;
/* 181 */     this.mScores = scores;
/* 182 */     setMaxResults(maxResults);
/* 183 */     this.mCategories = categories;
/*     */   }
/*     */ 
/*     */   static String[] categoriesFor(Vector[] termVectors) {
/* 187 */     int max = 0;
/* 188 */     for (Vector termVector : termVectors) {
/* 189 */       int[] nzDims = termVector.nonZeroDimensions();
/* 190 */       for (int k = 0; k < nzDims.length; k++)
/* 191 */         max = Math.max(max, nzDims[k]);
/*     */     }
/* 193 */     String[] categories = new String[max];
/* 194 */     for (int i = 0; i < categories.length; i++)
/* 195 */       categories[i] = Integer.toString(i);
/* 196 */     return categories;
/*     */   }
/*     */ 
/*     */   public int maxResults()
/*     */   {
/* 206 */     return this.mMaxResults;
/*     */   }
/*     */ 
/*     */   public void setMaxResults(int maxResults)
/*     */   {
/* 220 */     if (maxResults < 1) {
/* 221 */       String msg = "Max results must be positive. Found maxResults=" + maxResults;
/*     */ 
/* 223 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 225 */     this.mMaxResults = maxResults;
/*     */   }
/*     */ 
/*     */   public ScoredClassification classify(Vector x)
/*     */   {
/* 241 */     int[] nzDims = x.nonZeroDimensions();
/* 242 */     int heapSize = 0;
/* 243 */     for (int k = 0; k < nzDims.length; k++)
/* 244 */       if (nzDims[k] < this.mTermIndexes.length)
/* 245 */         heapSize++;
/* 246 */     int[] current = new int[heapSize];
/* 247 */     float[] vals = new float[heapSize];
/* 248 */     int j = 0;
/* 249 */     for (int k = 0; k < heapSize; k++)
/* 250 */       if (nzDims[k] < this.mTermIndexes.length)
/*     */       {
/* 252 */         current[j] = this.mTermIndexes[nzDims[k]];
/* 253 */         vals[j] = ((float)x.value(nzDims[k]));
/* 254 */         j++;
/*     */       }
/* 256 */     int k = (heapSize + 1) / 2;
/*     */     while (true) { k--; if (k < 0) break;
/* 257 */       heapify(k, heapSize, current, vals, this.mDocumentIds);
/*     */     }
/* 259 */     BoundedPriorityQueue queue = new BoundedPriorityQueue(ScoredObject.comparator(), this.mMaxResults);
/*     */ 
/* 262 */     int[] documentIds = this.mDocumentIds;
/* 263 */     while (heapSize > 0)
/*     */     {
/* 265 */       int doc = documentIds[current[0]];
/*     */ 
/* 267 */       double score = 0.0D;
/* 268 */       while ((heapSize > 0) && (documentIds[current[0]] == doc)) {
/* 269 */         score += vals[0] * this.mScores[current[0]];
/* 270 */         current[0] += 1;
/* 271 */         if (documentIds[current[0]] == -1) {
/* 272 */           heapSize--;
/* 273 */           if (heapSize > 0) {
/* 274 */             current[0] = current[heapSize];
/* 275 */             vals[0] = vals[heapSize];
/*     */           }
/*     */         }
/* 278 */         heapify(0, heapSize, current, vals, documentIds);
/*     */       }
/* 280 */       queue.offer(new ScoredDoc(doc, score));
/*     */     }
/* 282 */     String[] categories = new String[queue.size()];
/* 283 */     double[] scores = new double[queue.size()];
/* 284 */     int pos = 0;
/* 285 */     for (ScoredDoc sd : queue) {
/* 286 */       categories[pos] = Integer.toString(sd.docId());
/* 287 */       scores[pos] = sd.score();
/* 288 */       pos++;
/*     */     }
/* 290 */     return new ScoredClassification(categories, scores);
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 295 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   static void heapify(int i, int heapSize, int[] current, float[] vals, int[] documentIds)
/*     */   {
/*     */     while (true)
/*     */     {
/* 303 */       int left = 2 * (i + 1) - 1;
/* 304 */       if (left >= heapSize)
/* 305 */         return;
/* 306 */       if (documentIds[current[i]] > documentIds[current[left]]) {
/* 307 */         swap(left, i, current);
/* 308 */         swap(left, i, vals);
/* 309 */         i = left;
/*     */       }
/*     */       else {
/* 312 */         int right = left + 1;
/* 313 */         if (right >= heapSize)
/* 314 */           return;
/* 315 */         if (documentIds[current[i]] <= documentIds[current[right]]) break;
/* 316 */         swap(right, i, current);
/* 317 */         swap(right, i, vals);
/* 318 */         i = right;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void printHeap(int heapSize, int[] current, float[] vals, int[] documentIds)
/*     */   {
/* 328 */     System.out.println("\nHeapSize=" + heapSize);
/* 329 */     for (int i = 0; i < heapSize; i++)
/* 330 */       System.out.println("i=" + i + " curent=" + current[i] + " vals=" + vals[i] + " docId=" + documentIds[current[i]]);
/*     */   }
/*     */ 
/*     */   static void swap(int i, int j, int[] xs)
/*     */   {
/* 335 */     int tempXsI = xs[i];
/* 336 */     xs[i] = xs[j];
/* 337 */     xs[j] = tempXsI;
/*     */   }
/*     */ 
/*     */   static void swap(int i, int j, float[] xs)
/*     */   {
/* 342 */     float tempXsI = xs[i];
/* 343 */     xs[i] = xs[j];
/* 344 */     xs[j] = tempXsI;
/*     */   }
/*     */ 
/*     */   static class Serializer extends AbstractExternalizable
/*     */   {
/*     */     static final long serialVersionUID = 3954262240692411543L;
/*     */     private final BigVectorClassifier mClassifier;
/*     */ 
/*     */     public Serializer()
/*     */     {
/* 372 */       this(null);
/*     */     }
/*     */     public Serializer(BigVectorClassifier classifier) {
/* 375 */       this.mClassifier = classifier;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 379 */       writeInts(this.mClassifier.mTermIndexes, objOut);
/* 380 */       writeInts(this.mClassifier.mDocumentIds, objOut);
/* 381 */       writeFloats(this.mClassifier.mScores, objOut);
/* 382 */       writeUTFs(this.mClassifier.mCategories, objOut);
/* 383 */       objOut.writeInt(this.mClassifier.mMaxResults);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws ClassNotFoundException, IOException
/*     */     {
/* 388 */       int[] termIndexes = readInts(objIn);
/* 389 */       int[] documentIds = readInts(objIn);
/* 390 */       float[] scores = readFloats(objIn);
/* 391 */       String[] categories = readUTFs(objIn);
/* 392 */       int maxResults = objIn.readInt();
/* 393 */       return new BigVectorClassifier(termIndexes, documentIds, scores, categories, maxResults);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ScoredDoc
/*     */     implements Scored
/*     */   {
/*     */     private final int mDocId;
/*     */     private final double mScore;
/*     */ 
/*     */     public ScoredDoc(int docId, double score)
/*     */     {
/* 352 */       this.mDocId = docId;
/* 353 */       this.mScore = score;
/*     */     }
/*     */     public int docId() {
/* 356 */       return this.mDocId;
/*     */     }
/*     */     public double score() {
/* 359 */       return this.mScore;
/*     */     }
/*     */     public String toString() {
/* 362 */       return this.mDocId + ":" + this.mScore;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.BigVectorClassifier
 * JD-Core Version:    0.6.2
 */