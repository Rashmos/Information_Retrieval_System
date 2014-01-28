/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTableCompiler;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ class Node
/*     */ {
/*     */   float mOneMinusLambda;
/* 131 */   private int mIndex = -1;
/*     */ 
/* 136 */   private int mTotalCount = 0;
/*     */ 
/* 141 */   private short mNumOutcomes = 0;
/*     */ 
/* 146 */   private final Map<String, Node> mChildren = new TreeMap();
/*     */ 
/* 151 */   private final Map<String, OutcomeCounter> mOutcomes = new TreeMap();
/*     */   private final Node mBackoffNode;
/*     */   private final SymbolTableCompiler mSymbolTable;
/*     */   private final String mSymbol;
/*     */ 
/*     */   public Node(String symbol, SymbolTableCompiler symbolTable, Node backoffNode)
/*     */   {
/* 185 */     this.mSymbol = symbol;
/* 186 */     if (symbolTable == null)
/* 187 */       throw new IllegalArgumentException("Null table.");
/* 188 */     this.mSymbolTable = symbolTable;
/* 189 */     if (symbol != null) symbolTable.addSymbol(symbol);
/* 190 */     this.mBackoffNode = backoffNode;
/*     */   }
/*     */ 
/*     */   public void printSymbols() {
/* 194 */     if (this.mSymbolTable == null) System.out.println("NULL Symbol TABLE");
/* 195 */     System.out.println(this.mSymbolTable.toString());
/*     */   }
/*     */ 
/*     */   public int getSymbolID()
/*     */   {
/* 208 */     if (this.mSymbol == null) return -1;
/* 209 */     return this.mSymbolTable.symbolToID(this.mSymbol);
/*     */   }
/*     */ 
/*     */   public void generateSymbols()
/*     */   {
/* 218 */     if (this.mSymbol != null) this.mSymbolTable.addSymbol(this.mSymbol);
/* 219 */     for (OutcomeCounter counter : this.mOutcomes.values())
/* 220 */       counter.addSymbolToTable();
/* 221 */     for (Node child : this.mChildren.values())
/* 222 */       child.generateSymbols();
/*     */   }
/*     */ 
/*     */   public int index()
/*     */   {
/* 234 */     return this.mIndex;
/*     */   }
/*     */ 
/*     */   public void setIndex(int index)
/*     */   {
/* 244 */     this.mIndex = index;
/*     */   }
/*     */ 
/*     */   public void prune(int threshold)
/*     */   {
/* 256 */     Iterator outcomes = outcomes().iterator();
/* 257 */     while (outcomes.hasNext()) {
/* 258 */       OutcomeCounter counter = getOutcome((String)outcomes.next());
/* 259 */       if (counter.count() < threshold) {
/* 260 */         this.mTotalCount -= counter.count();
/* 261 */         this.mNumOutcomes = ((short)(this.mNumOutcomes - 1));
/* 262 */         outcomes.remove();
/*     */       }
/*     */     }
/* 265 */     Iterator childrenIt = children().iterator();
/* 266 */     while (childrenIt.hasNext()) {
/* 267 */       Node childNode = getChild((String)childrenIt.next());
/* 268 */       childNode.prune(threshold);
/* 269 */       if (childNode.totalCount() < threshold)
/* 270 */         childrenIt.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int numNodes()
/*     */   {
/* 282 */     int count = 1;
/* 283 */     for (String childString : children())
/* 284 */       count += getChild(childString).numNodes();
/* 285 */     return count;
/*     */   }
/*     */ 
/*     */   public int numCounters()
/*     */   {
/* 296 */     int count = this.mOutcomes.keySet().size();
/* 297 */     for (String childString : children())
/* 298 */       count += getChild(childString).numCounters();
/* 299 */     return count;
/*     */   }
/*     */ 
/*     */   public boolean hasOutcome(String outcome)
/*     */   {
/* 311 */     return this.mOutcomes.containsKey(outcome);
/*     */   }
/*     */ 
/*     */   public OutcomeCounter getOutcome(String outcome)
/*     */   {
/* 323 */     return (OutcomeCounter)this.mOutcomes.get(outcome);
/*     */   }
/*     */ 
/*     */   public boolean hasChild(String child)
/*     */   {
/* 335 */     return this.mChildren.containsKey(child);
/*     */   }
/*     */ 
/*     */   public Node getChild(String child)
/*     */   {
/* 346 */     return (Node)this.mChildren.get(child);
/*     */   }
/*     */ 
/*     */   public Node getOrCreateChild(String child, Node backoffNode, SymbolTableCompiler symbolTable)
/*     */   {
/* 366 */     if (hasChild(child)) return getChild(child);
/* 367 */     Node node = new Node(child, symbolTable, backoffNode);
/* 368 */     this.mChildren.put(child, node);
/* 369 */     return node;
/*     */   }
/*     */ 
/*     */   public Set<String> outcomes()
/*     */   {
/* 378 */     return this.mOutcomes.keySet();
/*     */   }
/*     */ 
/*     */   public Set<String> children()
/*     */   {
/* 387 */     return this.mChildren.keySet();
/*     */   }
/*     */ 
/*     */   public int outcomeCount(String outcome)
/*     */   {
/* 399 */     OutcomeCounter ctr = getOutcome(outcome);
/* 400 */     return ctr == null ? 0 : ctr.count();
/*     */   }
/*     */ 
/*     */   public void incrementOutcome(String outcome, SymbolTableCompiler symbolTable)
/*     */   {
/* 414 */     this.mTotalCount += 1;
/* 415 */     if (hasOutcome(outcome)) {
/* 416 */       getOutcome(outcome).increment();
/*     */     } else {
/* 418 */       this.mNumOutcomes = ((short)(this.mNumOutcomes + 1));
/* 419 */       this.mOutcomes.put(outcome, new OutcomeCounter(outcome, symbolTable, 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public int totalCount()
/*     */   {
/* 429 */     return this.mTotalCount;
/*     */   }
/*     */ 
/*     */   public float oneMinusLambda()
/*     */   {
/* 439 */     return this.mOneMinusLambda;
/*     */   }
/*     */ 
/*     */   public void compileEstimates(double lambdaFactor)
/*     */   {
/* 451 */     this.mOneMinusLambda = ((float)Math.log(1.0D - lambda(lambdaFactor)));
/* 452 */     for (String outcome : outcomes()) {
/* 453 */       getOutcome(outcome).setEstimate((float)logEstimate(outcome, lambdaFactor));
/*     */     }
/*     */ 
/* 456 */     for (String childString : children()) {
/* 457 */       Node child = getChild(childString);
/* 458 */       child.compileEstimates(lambdaFactor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public double logEstimate(String outcome, double lambdaFactor)
/*     */   {
/* 471 */     return Math.log(estimate(outcome, lambdaFactor));
/*     */   }
/*     */ 
/*     */   public Node backoffNode()
/*     */   {
/* 482 */     return this.mBackoffNode;
/*     */   }
/*     */ 
/*     */   public double estimate(String outcome, double lambdaFactor)
/*     */   {
/* 494 */     if (this.mBackoffNode == null) return maxLikelihoodEstimate(outcome);
/* 495 */     double lambda = lambda(lambdaFactor);
/* 496 */     return lambda * maxLikelihoodEstimate(outcome) + (1.0D - lambda) * this.mBackoffNode.estimate(outcome, lambdaFactor);
/*     */   }
/*     */ 
/*     */   public double maxLikelihoodEstimate(String outcome)
/*     */   {
/* 508 */     return outcomeCount(outcome) / this.mTotalCount;
/*     */   }
/*     */ 
/*     */   public double lambda(double lambdaFactor)
/*     */   {
/* 519 */     if (this.mTotalCount == 0) return 0.0D;
/* 520 */     return this.mTotalCount / (this.mTotalCount + lambdaFactor * this.mNumOutcomes);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.Node
 * JD-Core Version:    0.6.2
 */