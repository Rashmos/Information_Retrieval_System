/*     */ package com.aliasi.cluster;
/*     */ 
/*     */ import com.aliasi.classify.PrecisionRecallEvaluation;
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.Tuple;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ClusterScore<E>
/*     */ {
/*     */   private final PrecisionRecallEvaluation mPrEval;
/*     */   private final Set<? extends Set<? extends E>> mReferencePartition;
/*     */   private final Set<? extends Set<? extends E>> mResponsePartition;
/*     */ 
/*     */   public ClusterScore(Set<? extends Set<? extends E>> referencePartition, Set<? extends Set<? extends E>> responsePartition)
/*     */   {
/* 196 */     assertPartitionSameSets(referencePartition, responsePartition);
/* 197 */     this.mReferencePartition = referencePartition;
/* 198 */     this.mResponsePartition = responsePartition;
/* 199 */     this.mPrEval = calculateConfusionMatrix();
/*     */   }
/*     */ 
/*     */   public PrecisionRecallEvaluation equivalenceEvaluation()
/*     */   {
/* 209 */     return this.mPrEval;
/*     */   }
/*     */ 
/*     */   public double mucPrecision()
/*     */   {
/* 220 */     return mucRecall(this.mResponsePartition, this.mReferencePartition);
/*     */   }
/*     */ 
/*     */   public double mucRecall()
/*     */   {
/* 230 */     return mucRecall(this.mReferencePartition, this.mResponsePartition);
/*     */   }
/*     */ 
/*     */   public double mucF()
/*     */   {
/* 241 */     return f(mucPrecision(), mucRecall());
/*     */   }
/*     */ 
/*     */   public double b3ClusterPrecision()
/*     */   {
/* 253 */     return b3ClusterRecall(this.mResponsePartition, this.mReferencePartition);
/*     */   }
/*     */ 
/*     */   public double b3ClusterRecall()
/*     */   {
/* 264 */     return b3ClusterRecall(this.mReferencePartition, this.mResponsePartition);
/*     */   }
/*     */ 
/*     */   public double b3ClusterF()
/*     */   {
/* 275 */     return f(b3ClusterPrecision(), b3ClusterRecall());
/*     */   }
/*     */ 
/*     */   public double b3ElementPrecision()
/*     */   {
/* 287 */     return b3ElementRecall(this.mResponsePartition, this.mReferencePartition);
/*     */   }
/*     */ 
/*     */   public double b3ElementRecall()
/*     */   {
/* 298 */     return b3ElementRecall(this.mReferencePartition, this.mResponsePartition);
/*     */   }
/*     */ 
/*     */   public double b3ElementF()
/*     */   {
/* 309 */     return f(b3ElementPrecision(), b3ElementRecall());
/*     */   }
/*     */ 
/*     */   public Set<Tuple<E>> truePositives()
/*     */   {
/* 323 */     Set referenceEquivalences = toEquivalences(this.mReferencePartition);
/* 324 */     Set responseEquivalences = toEquivalences(this.mResponsePartition);
/* 325 */     referenceEquivalences.retainAll(responseEquivalences);
/* 326 */     return referenceEquivalences;
/*     */   }
/*     */ 
/*     */   public Set<Tuple<E>> falsePositives()
/*     */   {
/* 339 */     Set referenceEquivalences = toEquivalences(this.mReferencePartition);
/* 340 */     Set responseEquivalences = toEquivalences(this.mResponsePartition);
/* 341 */     responseEquivalences.removeAll(referenceEquivalences);
/* 342 */     return responseEquivalences;
/*     */   }
/*     */ 
/*     */   public Set<Tuple<E>> falseNegatives()
/*     */   {
/* 355 */     Set referenceEquivalences = toEquivalences(this.mReferencePartition);
/* 356 */     Set responseEquivalences = toEquivalences(this.mResponsePartition);
/* 357 */     referenceEquivalences.removeAll(responseEquivalences);
/* 358 */     return referenceEquivalences;
/*     */   }
/*     */ 
/*     */   private PrecisionRecallEvaluation calculateConfusionMatrix()
/*     */   {
/* 363 */     Set referenceEquivalences = toEquivalences(this.mReferencePartition);
/* 364 */     Set responseEquivalences = toEquivalences(this.mResponsePartition);
/* 365 */     long tp = 0L;
/* 366 */     long fn = 0L;
/* 367 */     for (Tuple tuple : referenceEquivalences) {
/* 368 */       if (responseEquivalences.remove(tuple))
/* 369 */         tp += 1L;
/*     */       else
/* 371 */         fn += 1L;
/*     */     }
/* 373 */     long numElements = elementsOf(this.mReferencePartition).size();
/* 374 */     long totalCount = numElements * numElements;
/* 375 */     long fp = responseEquivalences.size();
/* 376 */     long tn = totalCount - tp - fn - fp;
/* 377 */     return new PrecisionRecallEvaluation(tp, fn, fp, tn);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 391 */     StringBuilder sb = new StringBuilder();
/*     */ 
/* 393 */     sb.append("CLUSTER SCORE");
/* 394 */     sb.append("\nEquivalence Evaluation\n");
/* 395 */     sb.append(this.mPrEval.toString());
/*     */ 
/* 397 */     sb.append("\nMUC Evaluation");
/* 398 */     sb.append("\n  MUC Precision = " + mucPrecision());
/* 399 */     sb.append("\n  MUC Recall = " + mucRecall());
/* 400 */     sb.append("\n  MUC F(1) = " + mucF());
/*     */ 
/* 402 */     sb.append("\nB-Cubed Evaluation");
/* 403 */     sb.append("\n  B3 Cluster Averaged Precision = " + b3ClusterPrecision());
/*     */ 
/* 405 */     sb.append("\n  B3 Cluster Averaged Recall = " + b3ClusterRecall());
/* 406 */     sb.append("\n  B3 Cluster Averaged F(1) = " + b3ClusterF());
/* 407 */     sb.append("\n  B3 Element Averaged Precision = " + b3ElementPrecision());
/*     */ 
/* 409 */     sb.append("\n  B3 Element Averaged Recall = " + b3ElementRecall());
/* 410 */     sb.append("\n  B3 Element Averaged F(1) = " + b3ElementF());
/*     */ 
/* 412 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public static <E> double withinClusterScatter(Set<? extends Set<? extends E>> clustering, Distance<? super E> distance)
/*     */   {
/* 441 */     double scatter = 0.0D;
/* 442 */     for (Set s : clustering)
/* 443 */       scatter += scatter(s, distance);
/* 444 */     return scatter;
/*     */   }
/*     */ 
/*     */   public static <E> double scatter(Set<? extends E> cluster, Distance<? super E> distance)
/*     */   {
/* 473 */     Object[] elements = (Object[])cluster.toArray();
/* 474 */     double scatter = 0.0D;
/* 475 */     for (int i = 0; i < elements.length; i++)
/* 476 */       for (int j = i + 1; j < elements.length; j++)
/* 477 */         scatter += distance.distance(elements[i], elements[j]);
/* 478 */     return scatter;
/*     */   }
/*     */ 
/*     */   Set<Tuple<E>> toEquivalences(Set<? extends Set<? extends E>> partition)
/*     */   {
/* 484 */     Set equivalences = new HashSet();
/* 485 */     for (Set equivalenceClass : partition)
/*     */     {
/* 488 */       Object[] xs = (Object[])new Object[equivalenceClass.size()];
/* 489 */       equivalenceClass.toArray(xs);
/* 490 */       for (int i = 0; i < xs.length; i++)
/* 491 */         for (int j = 0; j < xs.length; j++)
/* 492 */           equivalences.add(Tuple.create(xs[i], xs[j]));
/*     */     }
/* 494 */     return equivalences;
/*     */   }
/*     */ 
/*     */   private static <F> double b3ElementRecall(Set<? extends Set<? extends F>> referencePartition, Set<? extends Set<? extends F>> responsePartition)
/*     */   {
/* 500 */     double score = 0.0D;
/* 501 */     Set elementsOfReference = elementsOf(referencePartition);
/* 502 */     for (Iterator i$ = referencePartition.iterator(); i$.hasNext(); ) { referenceEqClass = (Set)i$.next();
/* 503 */       for (i$ = referenceEqClass.iterator(); i$.hasNext(); ) { Object referenceEqClassElt = i$.next();
/* 504 */         score += uniformElementWeight(elementsOfReference) * b3Recall(referenceEqClassElt, referenceEqClass, responsePartition);
/*     */       }
/*     */     }
/*     */     Set referenceEqClass;
/*     */     Iterator i$;
/* 507 */     return score;
/*     */   }
/*     */ 
/*     */   private static <F> double uniformElementWeight(Set<? extends F> elements) {
/* 511 */     return 1.0D / elements.size();
/*     */   }
/*     */ 
/*     */   private static <F> double uniformClusterWeight(Set<? extends F> eqClass, Set<? extends Set<? extends F>> partition)
/*     */   {
/* 516 */     return 1.0D / (eqClass.size() * partition.size());
/*     */   }
/*     */ 
/*     */   private static <F> double b3ClusterRecall(Set<? extends Set<? extends F>> referencePartition, Set<? extends Set<? extends F>> responsePartition)
/*     */   {
/* 521 */     double score = 0.0D;
/* 522 */     for (Iterator i$ = referencePartition.iterator(); i$.hasNext(); ) { referenceEqClass = (Set)i$.next();
/* 523 */       for (i$ = referenceEqClass.iterator(); i$.hasNext(); ) { Object referenceEqClassElt = i$.next();
/* 524 */         score += uniformClusterWeight(referenceEqClass, referencePartition) * b3Recall(referenceEqClassElt, referenceEqClass, responsePartition);
/*     */       }
/*     */     }
/*     */     Set referenceEqClass;
/*     */     Iterator i$;
/* 527 */     return score;
/*     */   }
/*     */ 
/*     */   private static <F> double b3Recall(F element, Set<? extends F> referenceEqClass, Set<? extends Set<? extends F>> responsePartition)
/*     */   {
/* 533 */     Set responseClass = getEquivalenceClass(element, responsePartition);
/* 534 */     return recallSets(referenceEqClass, responseClass);
/*     */   }
/*     */ 
/*     */   private static <F> double recallSets(Set<? extends F> referenceSet, Set<? extends F> responseSet) {
/* 538 */     if (referenceSet.size() == 0) return 1.0D;
/* 539 */     return intersectionSize(referenceSet, responseSet) / referenceSet.size();
/*     */   }
/*     */ 
/*     */   private static <F> long intersectionSize(Set<? extends F> set1, Set<? extends F> set2)
/*     */   {
/* 544 */     long count = 0L;
/* 545 */     for (Iterator i$ = set1.iterator(); i$.hasNext(); ) { Object f = i$.next();
/* 546 */       if (set2.contains(f))
/* 547 */         count += 1L; }
/* 548 */     return count;
/*     */   }
/*     */ 
/*     */   private static <F> void assertPartitionSameSets(Set<? extends Set<? extends F>> set1, Set<? extends Set<? extends F>> set2)
/*     */   {
/* 553 */     assertValidPartition(set1);
/* 554 */     assertValidPartition(set2);
/* 555 */     if (!elementsOf(set1).equals(elementsOf(set2))) {
/* 556 */       String msg = "Partitions must be of same sets.";
/* 557 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static <F> void assertValidPartition(Set<? extends Set<? extends F>> partition) {
/* 562 */     Set eltsSoFar = new HashSet();
/* 563 */     for (Set eqClass : partition)
/* 564 */       for (i$ = eqClass.iterator(); i$.hasNext(); ) { Object member = i$.next();
/* 565 */         if (!eltsSoFar.add(member)) {
/* 566 */           String msg = "Partitions must not contain overlapping members. Found overlapping element=" + member;
/*     */ 
/* 568 */           throw new IllegalArgumentException(msg);
/*     */         }
/*     */       }
/*     */     Iterator i$;
/*     */   }
/*     */ 
/*     */   private static <F> Set<? extends F> getEquivalenceClass(F element, Set<? extends Set<? extends F>> partition)
/*     */   {
/* 577 */     for (Set equivalenceClass : partition)
/* 578 */       if (equivalenceClass.contains(element))
/* 579 */         return equivalenceClass;
/* 580 */     throw new IllegalArgumentException("Element must be in an equivalence class in partition.");
/*     */   }
/*     */ 
/*     */   private static <F> Set<F> elementsOf(Set<? extends Set<? extends F>> partition) {
/* 584 */     Set elementSet = new HashSet();
/* 585 */     for (Set eqClass : partition)
/* 586 */       elementSet.addAll(eqClass);
/* 587 */     return elementSet;
/*     */   }
/*     */ 
/*     */   private static double f(double precision, double recall)
/*     */   {
/* 592 */     return 2.0D * precision * recall / (precision + recall);
/*     */   }
/*     */ 
/*     */   private static <F> double mucRecall(Set<? extends Set<? extends F>> referencePartition, Set<? extends Set<? extends F>> responsePartition)
/*     */   {
/* 598 */     long numerator = 0L;
/* 599 */     long denominator = 0L;
/* 600 */     for (Set referenceEqClass : referencePartition) {
/* 601 */       long numPartitions = 0L;
/* 602 */       for (Set responseEqClass : responsePartition) {
/* 603 */         if (!Collections.disjoint(referenceEqClass, responseEqClass))
/* 604 */           numPartitions += 1L;
/*     */       }
/* 606 */       numerator += referenceEqClass.size() - numPartitions;
/* 607 */       denominator += referenceEqClass.size() - 1;
/*     */     }
/* 609 */     if (denominator == 0L) return 1.0D;
/* 610 */     return numerator / denominator;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.ClusterScore
 * JD-Core Version:    0.6.2
 */