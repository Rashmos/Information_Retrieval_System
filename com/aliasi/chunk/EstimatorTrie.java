/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ 
/*     */ class EstimatorTrie
/*     */ {
/*     */   private final int _numNodes;
/*     */   private final int[] _nodeSymbol;
/*     */   private final int[] _nodeFirstOutcome;
/*     */   private final int[] _nodeFirstChild;
/*     */   private final float[] _nodeLogOneMinusLambda;
/*     */   private final int[] _nodeBackoff;
/*     */   private final int _numOutcomes;
/*     */   private final int[] _outcomeSymbol;
/*     */   private final float[] _outcomeLogEstimate;
/*     */ 
/*     */   public EstimatorTrie(ObjectInput in)
/*     */     throws IOException
/*     */   {
/* 227 */     this._numNodes = in.readInt();
/* 228 */     this._nodeSymbol = new int[this._numNodes];
/* 229 */     this._nodeFirstOutcome = new int[this._numNodes + 1];
/* 230 */     this._nodeFirstChild = new int[this._numNodes + 1];
/* 231 */     this._nodeLogOneMinusLambda = new float[this._numNodes];
/* 232 */     this._nodeBackoff = new int[this._numNodes];
/* 233 */     for (int i = 0; i < this._numNodes; i++) {
/* 234 */       this._nodeSymbol[i] = in.readInt();
/* 235 */       this._nodeFirstOutcome[i] = in.readInt();
/* 236 */       this._nodeFirstChild[i] = in.readInt();
/* 237 */       this._nodeLogOneMinusLambda[i] = in.readFloat();
/* 238 */       this._nodeBackoff[i] = in.readInt();
/*     */     }
/* 240 */     this._nodeFirstChild[this._numNodes] = this._numNodes;
/* 241 */     this._numOutcomes = in.readInt();
/* 242 */     this._nodeFirstOutcome[this._numNodes] = this._numOutcomes;
/* 243 */     this._outcomeSymbol = new int[this._numOutcomes];
/* 244 */     this._outcomeLogEstimate = new float[this._numOutcomes];
/* 245 */     for (int i = 0; i < this._numOutcomes; i++) {
/* 246 */       this._outcomeSymbol[i] = in.readInt();
/* 247 */       this._outcomeLogEstimate[i] = in.readFloat();
/*     */     }
/*     */   }
/*     */ 
/*     */   public double estimateFromNode(int symbolID, int nodeIndex)
/*     */   {
/* 263 */     if (symbolID < 0) return (0.0D / 0.0D);
/* 264 */     double backoffAccumulator = 0.0D;
/* 265 */     for (int currentNodeIndex = nodeIndex; 
/* 266 */       currentNodeIndex >= 0; 
/* 267 */       currentNodeIndex = this._nodeBackoff[currentNodeIndex]) {
/* 268 */       int low = this._nodeFirstOutcome[currentNodeIndex];
/* 269 */       int high = this._nodeFirstOutcome[(currentNodeIndex + 1)] - 1;
/* 270 */       while (low <= high) {
/* 271 */         int mid = (high + low) / 2;
/* 272 */         if (this._outcomeSymbol[mid] == symbolID)
/* 273 */           return backoffAccumulator + this._outcomeLogEstimate[mid];
/* 274 */         if (this._outcomeSymbol[mid] < symbolID)
/* 275 */           low = low == mid ? mid + 1 : mid;
/* 276 */         else high = high == mid ? mid - 1 : mid;
/*     */       }
/* 278 */       backoffAccumulator += this._nodeLogOneMinusLambda[currentNodeIndex];
/*     */     }
/* 280 */     return (0.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public double estimateFromNodeUniform(int symbolID, int nodeIndex, double uniformEstimate)
/*     */   {
/* 298 */     if (symbolID < 0) return (0.0D / 0.0D);
/* 299 */     double backoffAccumulator = 0.0D;
/* 300 */     for (int currentNodeIndex = nodeIndex; 
/* 301 */       currentNodeIndex >= 0; 
/* 302 */       currentNodeIndex = this._nodeBackoff[currentNodeIndex]) {
/* 303 */       int low = this._nodeFirstOutcome[currentNodeIndex];
/* 304 */       int high = this._nodeFirstOutcome[(currentNodeIndex + 1)] - 1;
/* 305 */       while (low <= high) {
/* 306 */         int mid = (high + low) / 2;
/* 307 */         if (this._outcomeSymbol[mid] == symbolID)
/* 308 */           return backoffAccumulator + this._outcomeLogEstimate[mid];
/* 309 */         if (this._outcomeSymbol[mid] < symbolID)
/* 310 */           low = low == mid ? mid + 1 : mid;
/* 311 */         else high = high == mid ? mid - 1 : mid;
/*     */       }
/* 313 */       backoffAccumulator += this._nodeLogOneMinusLambda[currentNodeIndex];
/*     */     }
/* 315 */     return backoffAccumulator + uniformEstimate;
/*     */   }
/*     */ 
/*     */   public int lookupChild(int symbolID, int parentNodeIndex)
/*     */   {
/* 330 */     int low = this._nodeFirstChild[parentNodeIndex];
/* 331 */     int high = this._nodeFirstChild[(parentNodeIndex + 1)] - 1;
/* 332 */     if (symbolID < 0) return -1;
/* 333 */     while (low <= high) {
/* 334 */       int mid = (high + low) / 2;
/* 335 */       if (this._nodeSymbol[mid] == symbolID) return mid;
/* 336 */       if (this._nodeSymbol[mid] < symbolID)
/* 337 */         low = low == mid ? mid + 1 : mid;
/* 338 */       else high = high == mid ? mid - 1 : mid;
/*     */     }
/* 340 */     return -1;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.EstimatorTrie
 * JD-Core Version:    0.6.2
 */