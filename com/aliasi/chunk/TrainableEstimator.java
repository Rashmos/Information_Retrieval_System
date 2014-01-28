/*     */ package com.aliasi.chunk;
/*     */ 
/*     */ import com.aliasi.symbol.SymbolTableCompiler;
/*     */ import com.aliasi.tokenizer.TokenCategorizer;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ final class TrainableEstimator
/*     */   implements Compilable
/*     */ {
/*     */   private Node mRootTagNode;
/*     */   private Node mRootTokenNode;
/*  51 */   private final SymbolTableCompiler mTokenSymbolTable = new SymbolTableCompiler();
/*     */ 
/*  57 */   private final SymbolTableCompiler mTagSymbolTable = new SymbolTableCompiler();
/*     */   private double mLambdaFactor;
/*     */   private double mLogUniformVocabEstimate;
/*     */   private final TokenCategorizer mTokenCategorizer;
/*     */ 
/*     */   public TrainableEstimator(double lambdaFactor, double logUniformVocabEstimate, TokenCategorizer categorizer)
/*     */   {
/*  90 */     this.mLambdaFactor = lambdaFactor;
/*  91 */     this.mLogUniformVocabEstimate = logUniformVocabEstimate;
/*  92 */     this.mTokenCategorizer = categorizer;
/*  93 */     this.mRootTagNode = new Node(null, this.mTagSymbolTable, null);
/*  94 */     this.mRootTokenNode = new Node(null, this.mTokenSymbolTable, null);
/*  95 */     this.mTagSymbolTable.addSymbol("O");
/*     */   }
/*     */ 
/*     */   public TrainableEstimator(TokenCategorizer categorizer)
/*     */   {
/* 109 */     this(4.0D, Math.log(1.0E-06D), categorizer);
/*     */   }
/*     */ 
/*     */   public void setLambdaFactor(double lambdaFactor)
/*     */   {
/* 123 */     if ((lambdaFactor < 0.0D) || (Double.isNaN(lambdaFactor)) || (Double.isInfinite(lambdaFactor)))
/*     */     {
/* 126 */       throw new IllegalArgumentException("Lambda factor must be > 0. Was=" + lambdaFactor);
/*     */     }
/*     */ 
/* 129 */     this.mLambdaFactor = lambdaFactor;
/*     */   }
/*     */ 
/*     */   public void setLogUniformVocabularyEstimate(double estimate)
/*     */   {
/* 141 */     if ((estimate >= 0.0D) || (Double.isNaN(estimate)) || (Double.isInfinite(estimate)))
/*     */     {
/* 144 */       throw new IllegalArgumentException("Log vocab estimate must be < 0. Was=" + estimate);
/*     */     }
/*     */ 
/* 147 */     this.mLogUniformVocabEstimate = estimate;
/*     */   }
/*     */ 
/*     */   public void handle(String[] tokens, String[] tags)
/*     */   {
/* 162 */     if (tokens.length < 1) return;
/* 163 */     trainOutcome(tokens[0], tags[0], "O", ".", ".");
/*     */ 
/* 166 */     if (tokens.length < 2)
/*     */     {
/* 168 */       trainOutcome(".", "O", tags[0], tokens[0], ".");
/*     */ 
/* 171 */       return;
/*     */     }
/*     */ 
/* 175 */     trainOutcome(tokens[1], tags[1], tags[0], tokens[0], ".");
/*     */ 
/* 180 */     for (int i = 2; i < tokens.length; i++) {
/* 181 */       trainOutcome(tokens[i], tags[i], tags[(i - 1)], tokens[(i - 1)], tokens[(i - 2)]);
/*     */     }
/*     */ 
/* 186 */     trainOutcome(".", "O", tags[(tags.length - 1)], tokens[(tokens.length - 1)], tokens[(tokens.length - 2)]);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 202 */     out.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public void trainOutcome(String token, String tag, String tagMinus1, String tokenMinus1, String tokenMinus2)
/*     */   {
/* 249 */     this.mTagSymbolTable.addSymbol(tag);
/* 250 */     this.mTokenSymbolTable.addSymbol(token);
/* 251 */     String tagMinus1Interior = tagMinus1 == null ? null : Tags.toInnerTag(tagMinus1);
/*     */ 
/* 255 */     trainTokenModel(token, tag, tagMinus1Interior, tokenMinus1);
/* 256 */     trainTagModel(tag, tagMinus1Interior, tokenMinus1, tokenMinus2);
/*     */   }
/*     */ 
/*     */   private void generateSymbols()
/*     */   {
/* 264 */     this.mRootTagNode.generateSymbols();
/*     */ 
/* 266 */     this.mRootTokenNode.generateSymbols();
/*     */ 
/* 269 */     String[] tokenCategories = this.mTokenCategorizer.categories();
/* 270 */     for (int i = 0; i < tokenCategories.length; i++)
/* 271 */       this.mTokenSymbolTable.addSymbol(tokenCategories[i]);
/*     */   }
/*     */ 
/*     */   public void trainTokenModel(String token, String tag, String tagMinus1, String tokenMinus1)
/*     */   {
/* 293 */     if ((tag == null) || (token == null)) return;
/* 294 */     Node nodeTag = this.mRootTokenNode.getOrCreateChild(tag, null, this.mTagSymbolTable);
/*     */ 
/* 296 */     nodeTag.incrementOutcome(token, this.mTokenSymbolTable);
/*     */ 
/* 298 */     if (tagMinus1 == null) return;
/* 299 */     Node nodeTagTag1 = nodeTag.getOrCreateChild(tagMinus1, nodeTag, this.mTagSymbolTable);
/*     */ 
/* 301 */     nodeTagTag1.incrementOutcome(token, this.mTokenSymbolTable);
/*     */ 
/* 303 */     if (tokenMinus1 == null) return;
/* 304 */     Node nodeTagTag1W1 = nodeTagTag1.getOrCreateChild(tokenMinus1, nodeTagTag1, this.mTokenSymbolTable);
/*     */ 
/* 307 */     nodeTagTag1W1.incrementOutcome(token, this.mTokenSymbolTable);
/*     */   }
/*     */ 
/*     */   public void trainTagModel(String tag, String tagMinus1, String tokenMinus1, String tokenMinus2)
/*     */   {
/* 330 */     if ((tag == null) || (tagMinus1 == null)) return;
/* 331 */     Node nodeTag1 = this.mRootTagNode.getOrCreateChild(tagMinus1, null, this.mTagSymbolTable);
/*     */ 
/* 333 */     nodeTag1.incrementOutcome(tag, this.mTagSymbolTable);
/*     */ 
/* 335 */     if (tokenMinus1 == null) return;
/* 336 */     Node nodeTag1W1 = nodeTag1.getOrCreateChild(tokenMinus1, nodeTag1, this.mTokenSymbolTable);
/*     */ 
/* 339 */     nodeTag1W1.incrementOutcome(tag, this.mTagSymbolTable);
/*     */ 
/* 341 */     if (tokenMinus2 == null) return;
/* 342 */     Node nodeTag1W1W2 = nodeTag1W1.getOrCreateChild(tokenMinus2, nodeTag1W1, this.mTokenSymbolTable);
/*     */ 
/* 345 */     nodeTag1W1W2.incrementOutcome(tag, this.mTagSymbolTable);
/*     */   }
/*     */ 
/*     */   public void trainTokenOutcome(String token, String tag)
/*     */   {
/* 357 */     trainTokenModel(token, tag, null, null);
/*     */   }
/*     */ 
/*     */   public int numTagNodes()
/*     */   {
/* 366 */     return this.mRootTagNode.numNodes();
/*     */   }
/*     */ 
/*     */   public int numTagOutcomes()
/*     */   {
/* 375 */     return this.mRootTagNode.numCounters();
/*     */   }
/*     */ 
/*     */   public int numTokenNodes()
/*     */   {
/* 384 */     return this.mRootTokenNode.numNodes();
/*     */   }
/*     */ 
/*     */   public int numTokenOutcomes()
/*     */   {
/* 393 */     return this.mRootTokenNode.numCounters();
/*     */   }
/*     */ 
/*     */   public void prune(int thresholdTag, int thresholdToken)
/*     */   {
/* 407 */     this.mRootTagNode.prune(thresholdTag);
/* 408 */     this.mRootTokenNode.prune(thresholdToken);
/*     */   }
/*     */ 
/*     */   public void smoothTags(int countToAdd)
/*     */   {
/* 432 */     String[] tags = this.mTagSymbolTable.symbols();
/* 433 */     for (int i = 0; i < tags.length; i++) {
/* 434 */       String tag1 = tags[i];
/* 435 */       for (int j = 0; j < tags.length; j++) {
/* 436 */         String tag2 = tags[j];
/* 437 */         if (!Tags.illegalSequence(tag1, tag2))
/* 438 */           for (int k = 0; k < countToAdd; k++)
/* 439 */             trainTagModel(tag2, tag1, null, null);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeEstimator(Node rootNode, ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 455 */     rootNode.compileEstimates(this.mLambdaFactor);
/* 456 */     indexNodes(rootNode);
/* 457 */     out.writeInt(rootNode.numNodes());
/* 458 */     writeNodes(rootNode, out);
/* 459 */     out.writeInt(rootNode.numCounters());
/* 460 */     writeOutcomes(rootNode, out);
/*     */   }
/*     */ 
/*     */   private static void indexNodes(Node rootNode)
/*     */   {
/* 469 */     LinkedList nodeQueue = new LinkedList();
/* 470 */     nodeQueue.addLast(rootNode);
/* 471 */     int index = 0;
/*     */     Node node;
/* 472 */     while (nodeQueue.size() > 0) {
/* 473 */       node = (Node)nodeQueue.removeFirst();
/* 474 */       node.setIndex(index++);
/* 475 */       for (String childString : node.children())
/* 476 */         nodeQueue.addLast(node.getChild(childString));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void writeNodes(Node rootNode, ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 491 */     LinkedList nodeQueue = new LinkedList();
/* 492 */     nodeQueue.addLast(new Object[] { rootNode, null });
/* 493 */     int outcomesIndex = 0;
/* 494 */     int index = 0;
/* 495 */     while (nodeQueue.size() > 0)
/*     */     {
/* 505 */       Object[] pair = (Object[])nodeQueue.removeFirst();
/* 506 */       Node node = (Node)pair[0];
/* 507 */       out.writeInt(node.getSymbolID());
/* 508 */       out.writeInt(outcomesIndex);
/* 509 */       outcomesIndex += node.outcomes().size();
/* 510 */       TreeSet children = new TreeSet(node.children());
/* 511 */       if (children.size() == 0) {
/* 512 */         out.writeInt(index);
/*     */       } else {
/* 514 */         Iterator childIterator = children.iterator();
/* 515 */         Node firstChild = node.getChild((String)childIterator.next());
/*     */ 
/* 517 */         out.writeInt(firstChild.index());
/* 518 */         index = firstChild.index() + node.children().size();
/* 519 */         childIterator = children.iterator();
/* 520 */         while (childIterator.hasNext()) {
/* 521 */           String childName = (String)childIterator.next();
/* 522 */           Node childNode = node.getChild(childName);
/* 523 */           nodeQueue.addLast(new Object[] { childNode, childName });
/*     */         }
/*     */       }
/* 526 */       out.writeFloat(node.oneMinusLambda());
/* 527 */       out.writeInt(node.backoffNode() == null ? -1 : node.backoffNode().index());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void writeOutcomes(Node rootNode, ObjectOutput out)
/*     */     throws IOException
/*     */   {
/* 543 */     LinkedList nodeQueue = new LinkedList();
/* 544 */     nodeQueue.addLast(rootNode);
/*     */     Node node;
/* 545 */     while (nodeQueue.size() > 0) {
/* 546 */       node = (Node)nodeQueue.removeFirst();
/* 547 */       for (String outcome : node.outcomes()) {
/* 548 */         OutcomeCounter outcomeCounter = node.getOutcome(outcome);
/* 549 */         out.writeInt(outcomeCounter.getSymbolID());
/* 550 */         out.writeFloat(outcomeCounter.estimate());
/*     */       }
/* 552 */       for (String child : node.children())
/* 553 */         nodeQueue.addLast(node.getChild(child));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = 4179100933315980535L;
/*     */     final TrainableEstimator mEstimator;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 209 */       this(null);
/*     */     }
/*     */     public Externalizer(TrainableEstimator estimator) {
/* 212 */       this.mEstimator = estimator;
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 218 */       return new CompiledEstimator(in);
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 222 */       AbstractExternalizable.compileOrSerialize(this.mEstimator.mTokenCategorizer, objOut);
/* 223 */       this.mEstimator.generateSymbols();
/* 224 */       this.mEstimator.mTagSymbolTable.compileTo(objOut);
/* 225 */       this.mEstimator.mTokenSymbolTable.compileTo(objOut);
/* 226 */       this.mEstimator.writeEstimator(this.mEstimator.mRootTagNode, objOut);
/* 227 */       this.mEstimator.writeEstimator(this.mEstimator.mRootTokenNode, objOut);
/* 228 */       objOut.writeDouble(this.mEstimator.mLogUniformVocabEstimate);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.chunk.TrainableEstimator
 * JD-Core Version:    0.6.2
 */