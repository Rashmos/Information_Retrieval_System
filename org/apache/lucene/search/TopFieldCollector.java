/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.util.PriorityQueue;
/*     */ 
/*     */ public abstract class TopFieldCollector extends TopDocsCollector<FieldValueHitQueue.Entry>
/*     */ {
/* 843 */   private static final ScoreDoc[] EMPTY_SCOREDOCS = new ScoreDoc[0];
/*     */   private final boolean fillFields;
/* 851 */   float maxScore = (0.0F / 0.0F);
/*     */   final int numHits;
/* 854 */   FieldValueHitQueue.Entry bottom = null;
/*     */   boolean queueFull;
/*     */   int docBase;
/*     */ 
/*     */   private TopFieldCollector(PriorityQueue<FieldValueHitQueue.Entry> pq, int numHits, boolean fillFields)
/*     */   {
/* 864 */     super(pq);
/* 865 */     this.numHits = numHits;
/* 866 */     this.fillFields = fillFields;
/*     */   }
/*     */ 
/*     */   public static TopFieldCollector create(Sort sort, int numHits, boolean fillFields, boolean trackDocScores, boolean trackMaxScore, boolean docsScoredInOrder)
/*     */     throws IOException
/*     */   {
/* 909 */     if (sort.fields.length == 0) {
/* 910 */       throw new IllegalArgumentException("Sort must contain at least one field");
/*     */     }
/*     */ 
/* 913 */     FieldValueHitQueue queue = FieldValueHitQueue.create(sort.fields, numHits);
/* 914 */     if (queue.getComparators().length == 1) {
/* 915 */       if (docsScoredInOrder) {
/* 916 */         if (trackMaxScore)
/* 917 */           return new OneComparatorScoringMaxScoreCollector(queue, numHits, fillFields);
/* 918 */         if (trackDocScores) {
/* 919 */           return new OneComparatorScoringNoMaxScoreCollector(queue, numHits, fillFields);
/*     */         }
/* 921 */         return new OneComparatorNonScoringCollector(queue, numHits, fillFields);
/*     */       }
/*     */ 
/* 924 */       if (trackMaxScore)
/* 925 */         return new OutOfOrderOneComparatorScoringMaxScoreCollector(queue, numHits, fillFields);
/* 926 */       if (trackDocScores) {
/* 927 */         return new OutOfOrderOneComparatorScoringNoMaxScoreCollector(queue, numHits, fillFields);
/*     */       }
/* 929 */       return new OutOfOrderOneComparatorNonScoringCollector(queue, numHits, fillFields);
/*     */     }
/*     */ 
/* 935 */     if (docsScoredInOrder) {
/* 936 */       if (trackMaxScore)
/* 937 */         return new MultiComparatorScoringMaxScoreCollector(queue, numHits, fillFields);
/* 938 */       if (trackDocScores) {
/* 939 */         return new MultiComparatorScoringNoMaxScoreCollector(queue, numHits, fillFields);
/*     */       }
/* 941 */       return new MultiComparatorNonScoringCollector(queue, numHits, fillFields);
/*     */     }
/*     */ 
/* 944 */     if (trackMaxScore)
/* 945 */       return new OutOfOrderMultiComparatorScoringMaxScoreCollector(queue, numHits, fillFields);
/* 946 */     if (trackDocScores) {
/* 947 */       return new OutOfOrderMultiComparatorScoringNoMaxScoreCollector(queue, numHits, fillFields);
/*     */     }
/* 949 */     return new OutOfOrderMultiComparatorNonScoringCollector(queue, numHits, fillFields);
/*     */   }
/*     */ 
/*     */   final void add(int slot, int doc, float score)
/*     */   {
/* 955 */     this.bottom = ((FieldValueHitQueue.Entry)this.pq.add(new FieldValueHitQueue.Entry(slot, this.docBase + doc, score)));
/* 956 */     this.queueFull = (this.totalHits == this.numHits);
/*     */   }
/*     */ 
/*     */   protected void populateResults(ScoreDoc[] results, int howMany)
/*     */   {
/* 966 */     if (this.fillFields)
/*     */     {
/* 968 */       FieldValueHitQueue queue = (FieldValueHitQueue)this.pq;
/* 969 */       for (int i = howMany - 1; i >= 0; i--)
/* 970 */         results[i] = queue.fillFields((FieldValueHitQueue.Entry)queue.pop());
/*     */     }
/*     */     else {
/* 973 */       for (int i = howMany - 1; i >= 0; i--) {
/* 974 */         FieldValueHitQueue.Entry entry = (FieldValueHitQueue.Entry)this.pq.pop();
/* 975 */         results[i] = new FieldDoc(entry.doc, entry.score);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected TopDocs newTopDocs(ScoreDoc[] results, int start)
/*     */   {
/* 982 */     if (results == null) {
/* 983 */       results = EMPTY_SCOREDOCS;
/*     */ 
/* 985 */       this.maxScore = (0.0F / 0.0F);
/*     */     }
/*     */ 
/* 989 */     return new TopFieldDocs(this.totalHits, results, ((FieldValueHitQueue)this.pq).getFields(), this.maxScore);
/*     */   }
/*     */ 
/*     */   public boolean acceptsDocsOutOfOrder()
/*     */   {
/* 994 */     return false;
/*     */   }
/*     */ 
/*     */   private static final class OutOfOrderMultiComparatorScoringNoMaxScoreCollector extends TopFieldCollector.MultiComparatorScoringNoMaxScoreCollector
/*     */   {
/*     */     public OutOfOrderMultiComparatorScoringNoMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 773 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 778 */       this.totalHits += 1;
/* 779 */       if (this.queueFull)
/*     */       {
/* 781 */         for (int i = 0; ; i++) {
/* 782 */           int c = this.reverseMul[i] * this.comparators[i].compareBottom(doc);
/* 783 */           if (c < 0)
/*     */           {
/* 785 */             return;
/* 786 */           }if (c > 0) {
/*     */             break;
/*     */           }
/* 789 */           if (i == this.comparators.length - 1)
/*     */           {
/* 791 */             if (doc + this.docBase <= this.bottom.doc)
/*     */               break;
/* 793 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 800 */         for (int i = 0; i < this.comparators.length; i++) {
/* 801 */           this.comparators[i].copy(this.bottom.slot, doc);
/*     */         }
/*     */ 
/* 805 */         float score = this.scorer.score();
/* 806 */         updateBottom(doc, score);
/*     */ 
/* 808 */         for (int i = 0; i < this.comparators.length; i++)
/* 809 */           this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */       else
/*     */       {
/* 813 */         int slot = this.totalHits - 1;
/*     */ 
/* 815 */         for (int i = 0; i < this.comparators.length; i++) {
/* 816 */           this.comparators[i].copy(slot, doc);
/*     */         }
/*     */ 
/* 820 */         float score = this.scorer.score();
/* 821 */         add(slot, doc, score);
/* 822 */         if (this.queueFull)
/* 823 */           for (int i = 0; i < this.comparators.length; i++)
/* 824 */             this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer)
/*     */       throws IOException
/*     */     {
/* 832 */       this.scorer = scorer;
/* 833 */       super.setScorer(scorer);
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/* 838 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MultiComparatorScoringNoMaxScoreCollector extends TopFieldCollector.MultiComparatorNonScoringCollector
/*     */   {
/*     */     Scorer scorer;
/*     */ 
/*     */     public MultiComparatorScoringNoMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 694 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     final void updateBottom(int doc, float score) {
/* 698 */       this.bottom.doc = (this.docBase + doc);
/* 699 */       this.bottom.score = score;
/* 700 */       this.bottom = ((FieldValueHitQueue.Entry)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 705 */       this.totalHits += 1;
/* 706 */       if (this.queueFull)
/*     */       {
/* 708 */         for (int i = 0; ; i++) {
/* 709 */           int c = this.reverseMul[i] * this.comparators[i].compareBottom(doc);
/* 710 */           if (c < 0)
/*     */           {
/* 712 */             return;
/* 713 */           }if (c > 0) {
/*     */             break;
/*     */           }
/* 716 */           if (i == this.comparators.length - 1)
/*     */           {
/* 720 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 725 */         for (int i = 0; i < this.comparators.length; i++) {
/* 726 */           this.comparators[i].copy(this.bottom.slot, doc);
/*     */         }
/*     */ 
/* 730 */         float score = this.scorer.score();
/* 731 */         updateBottom(doc, score);
/*     */ 
/* 733 */         for (int i = 0; i < this.comparators.length; i++)
/* 734 */           this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */       else
/*     */       {
/* 738 */         int slot = this.totalHits - 1;
/*     */ 
/* 740 */         for (int i = 0; i < this.comparators.length; i++) {
/* 741 */           this.comparators[i].copy(slot, doc);
/*     */         }
/*     */ 
/* 745 */         float score = this.scorer.score();
/* 746 */         add(slot, doc, score);
/* 747 */         if (this.queueFull)
/* 748 */           for (int i = 0; i < this.comparators.length; i++)
/* 749 */             this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer)
/*     */       throws IOException
/*     */     {
/* 757 */       this.scorer = scorer;
/* 758 */       super.setScorer(scorer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class OutOfOrderMultiComparatorScoringMaxScoreCollector extends TopFieldCollector.MultiComparatorScoringMaxScoreCollector
/*     */   {
/*     */     public OutOfOrderMultiComparatorScoringMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 621 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 626 */       float score = this.scorer.score();
/* 627 */       if (score > this.maxScore) {
/* 628 */         this.maxScore = score;
/*     */       }
/* 630 */       this.totalHits += 1;
/* 631 */       if (this.queueFull)
/*     */       {
/* 633 */         for (int i = 0; ; i++) {
/* 634 */           int c = this.reverseMul[i] * this.comparators[i].compareBottom(doc);
/* 635 */           if (c < 0)
/*     */           {
/* 637 */             return;
/* 638 */           }if (c > 0) {
/*     */             break;
/*     */           }
/* 641 */           if (i == this.comparators.length - 1)
/*     */           {
/* 643 */             if (doc + this.docBase <= this.bottom.doc)
/*     */               break;
/* 645 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 652 */         for (int i = 0; i < this.comparators.length; i++) {
/* 653 */           this.comparators[i].copy(this.bottom.slot, doc);
/*     */         }
/*     */ 
/* 656 */         updateBottom(doc, score);
/*     */ 
/* 658 */         for (int i = 0; i < this.comparators.length; i++)
/* 659 */           this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */       else
/*     */       {
/* 663 */         int slot = this.totalHits - 1;
/*     */ 
/* 665 */         for (int i = 0; i < this.comparators.length; i++) {
/* 666 */           this.comparators[i].copy(slot, doc);
/*     */         }
/* 668 */         add(slot, doc, score);
/* 669 */         if (this.queueFull)
/* 670 */           for (int i = 0; i < this.comparators.length; i++)
/* 671 */             this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/* 679 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MultiComparatorScoringMaxScoreCollector extends TopFieldCollector.MultiComparatorNonScoringCollector
/*     */   {
/*     */     Scorer scorer;
/*     */ 
/*     */     public MultiComparatorScoringMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 542 */       super(numHits, fillFields);
/*     */ 
/* 544 */       this.maxScore = (1.0F / -1.0F);
/*     */     }
/*     */ 
/*     */     final void updateBottom(int doc, float score) {
/* 548 */       this.bottom.doc = (this.docBase + doc);
/* 549 */       this.bottom.score = score;
/* 550 */       this.bottom = ((FieldValueHitQueue.Entry)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 555 */       float score = this.scorer.score();
/* 556 */       if (score > this.maxScore) {
/* 557 */         this.maxScore = score;
/*     */       }
/* 559 */       this.totalHits += 1;
/* 560 */       if (this.queueFull)
/*     */       {
/* 562 */         for (int i = 0; ; i++) {
/* 563 */           int c = this.reverseMul[i] * this.comparators[i].compareBottom(doc);
/* 564 */           if (c < 0)
/*     */           {
/* 566 */             return;
/* 567 */           }if (c > 0) {
/*     */             break;
/*     */           }
/* 570 */           if (i == this.comparators.length - 1)
/*     */           {
/* 574 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 579 */         for (int i = 0; i < this.comparators.length; i++) {
/* 580 */           this.comparators[i].copy(this.bottom.slot, doc);
/*     */         }
/*     */ 
/* 583 */         updateBottom(doc, score);
/*     */ 
/* 585 */         for (int i = 0; i < this.comparators.length; i++)
/* 586 */           this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */       else
/*     */       {
/* 590 */         int slot = this.totalHits - 1;
/*     */ 
/* 592 */         for (int i = 0; i < this.comparators.length; i++) {
/* 593 */           this.comparators[i].copy(slot, doc);
/*     */         }
/* 595 */         add(slot, doc, score);
/* 596 */         if (this.queueFull)
/* 597 */           for (int i = 0; i < this.comparators.length; i++)
/* 598 */             this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer)
/*     */       throws IOException
/*     */     {
/* 606 */       this.scorer = scorer;
/* 607 */       super.setScorer(scorer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OutOfOrderMultiComparatorNonScoringCollector extends TopFieldCollector.MultiComparatorNonScoringCollector
/*     */   {
/*     */     public OutOfOrderMultiComparatorNonScoringCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 473 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 478 */       this.totalHits += 1;
/* 479 */       if (this.queueFull)
/*     */       {
/* 481 */         for (int i = 0; ; i++) {
/* 482 */           int c = this.reverseMul[i] * this.comparators[i].compareBottom(doc);
/* 483 */           if (c < 0)
/*     */           {
/* 485 */             return;
/* 486 */           }if (c > 0) {
/*     */             break;
/*     */           }
/* 489 */           if (i == this.comparators.length - 1)
/*     */           {
/* 491 */             if (doc + this.docBase <= this.bottom.doc)
/*     */               break;
/* 493 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 500 */         for (int i = 0; i < this.comparators.length; i++) {
/* 501 */           this.comparators[i].copy(this.bottom.slot, doc);
/*     */         }
/*     */ 
/* 504 */         updateBottom(doc);
/*     */ 
/* 506 */         for (int i = 0; i < this.comparators.length; i++)
/* 507 */           this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */       else
/*     */       {
/* 511 */         int slot = this.totalHits - 1;
/*     */ 
/* 513 */         for (int i = 0; i < this.comparators.length; i++) {
/* 514 */           this.comparators[i].copy(slot, doc);
/*     */         }
/* 516 */         add(slot, doc, (0.0F / 0.0F));
/* 517 */         if (this.queueFull)
/* 518 */           for (int i = 0; i < this.comparators.length; i++)
/* 519 */             this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/* 527 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MultiComparatorNonScoringCollector extends TopFieldCollector
/*     */   {
/*     */     final FieldComparator[] comparators;
/*     */     final int[] reverseMul;
/*     */ 
/*     */     public MultiComparatorNonScoringCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 388 */       super(numHits, fillFields, null);
/* 389 */       this.comparators = queue.getComparators();
/* 390 */       this.reverseMul = queue.getReverseMul();
/*     */     }
/*     */ 
/*     */     final void updateBottom(int doc)
/*     */     {
/* 395 */       this.bottom.doc = (this.docBase + doc);
/* 396 */       this.bottom = ((FieldValueHitQueue.Entry)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 401 */       this.totalHits += 1;
/* 402 */       if (this.queueFull)
/*     */       {
/* 404 */         for (int i = 0; ; i++) {
/* 405 */           int c = this.reverseMul[i] * this.comparators[i].compareBottom(doc);
/* 406 */           if (c < 0)
/*     */           {
/* 408 */             return;
/* 409 */           }if (c > 0) {
/*     */             break;
/*     */           }
/* 412 */           if (i == this.comparators.length - 1)
/*     */           {
/* 416 */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 421 */         for (int i = 0; i < this.comparators.length; i++) {
/* 422 */           this.comparators[i].copy(this.bottom.slot, doc);
/*     */         }
/*     */ 
/* 425 */         updateBottom(doc);
/*     */ 
/* 427 */         for (int i = 0; i < this.comparators.length; i++)
/* 428 */           this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */       else
/*     */       {
/* 432 */         int slot = this.totalHits - 1;
/*     */ 
/* 434 */         for (int i = 0; i < this.comparators.length; i++) {
/* 435 */           this.comparators[i].copy(slot, doc);
/*     */         }
/* 437 */         add(slot, doc, (0.0F / 0.0F));
/* 438 */         if (this.queueFull)
/* 439 */           for (int i = 0; i < this.comparators.length; i++)
/* 440 */             this.comparators[i].setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase)
/*     */       throws IOException
/*     */     {
/* 448 */       this.docBase = docBase;
/* 449 */       for (int i = 0; i < this.comparators.length; i++)
/* 450 */         this.comparators[i].setNextReader(reader, docBase);
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer)
/*     */       throws IOException
/*     */     {
/* 457 */       for (int i = 0; i < this.comparators.length; i++)
/* 458 */         this.comparators[i].setScorer(scorer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OutOfOrderOneComparatorScoringMaxScoreCollector extends TopFieldCollector.OneComparatorScoringMaxScoreCollector
/*     */   {
/*     */     public OutOfOrderOneComparatorScoringMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 337 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 342 */       float score = this.scorer.score();
/* 343 */       if (score > this.maxScore) {
/* 344 */         this.maxScore = score;
/*     */       }
/* 346 */       this.totalHits += 1;
/* 347 */       if (this.queueFull)
/*     */       {
/* 349 */         int cmp = this.reverseMul * this.comparator.compareBottom(doc);
/* 350 */         if ((cmp < 0) || ((cmp == 0) && (doc + this.docBase > this.bottom.doc))) {
/* 351 */           return;
/*     */         }
/*     */ 
/* 355 */         this.comparator.copy(this.bottom.slot, doc);
/* 356 */         updateBottom(doc, score);
/* 357 */         this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */       else {
/* 360 */         int slot = this.totalHits - 1;
/*     */ 
/* 362 */         this.comparator.copy(slot, doc);
/* 363 */         add(slot, doc, score);
/* 364 */         if (this.queueFull)
/* 365 */           this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/* 372 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OneComparatorScoringMaxScoreCollector extends TopFieldCollector.OneComparatorNonScoringCollector
/*     */   {
/*     */     Scorer scorer;
/*     */ 
/*     */     public OneComparatorScoringMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 277 */       super(numHits, fillFields);
/*     */ 
/* 279 */       this.maxScore = (1.0F / -1.0F);
/*     */     }
/*     */ 
/*     */     final void updateBottom(int doc, float score) {
/* 283 */       this.bottom.doc = (this.docBase + doc);
/* 284 */       this.bottom.score = score;
/* 285 */       this.bottom = ((FieldValueHitQueue.Entry)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 290 */       float score = this.scorer.score();
/* 291 */       if (score > this.maxScore) {
/* 292 */         this.maxScore = score;
/*     */       }
/* 294 */       this.totalHits += 1;
/* 295 */       if (this.queueFull) {
/* 296 */         if (this.reverseMul * this.comparator.compareBottom(doc) <= 0)
/*     */         {
/* 300 */           return;
/*     */         }
/*     */ 
/* 304 */         this.comparator.copy(this.bottom.slot, doc);
/* 305 */         updateBottom(doc, score);
/* 306 */         this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */       else {
/* 309 */         int slot = this.totalHits - 1;
/*     */ 
/* 311 */         this.comparator.copy(slot, doc);
/* 312 */         add(slot, doc, score);
/* 313 */         if (this.queueFull)
/* 314 */           this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer)
/*     */       throws IOException
/*     */     {
/* 322 */       this.scorer = scorer;
/* 323 */       super.setScorer(scorer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OutOfOrderOneComparatorScoringNoMaxScoreCollector extends TopFieldCollector.OneComparatorScoringNoMaxScoreCollector
/*     */   {
/*     */     public OutOfOrderOneComparatorScoringNoMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 224 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 229 */       this.totalHits += 1;
/* 230 */       if (this.queueFull)
/*     */       {
/* 232 */         int cmp = this.reverseMul * this.comparator.compareBottom(doc);
/* 233 */         if ((cmp < 0) || ((cmp == 0) && (doc + this.docBase > this.bottom.doc))) {
/* 234 */           return;
/*     */         }
/*     */ 
/* 238 */         float score = this.scorer.score();
/*     */ 
/* 241 */         this.comparator.copy(this.bottom.slot, doc);
/* 242 */         updateBottom(doc, score);
/* 243 */         this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */       else {
/* 246 */         float score = this.scorer.score();
/*     */ 
/* 249 */         int slot = this.totalHits - 1;
/*     */ 
/* 251 */         this.comparator.copy(slot, doc);
/* 252 */         add(slot, doc, score);
/* 253 */         if (this.queueFull)
/* 254 */           this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/* 261 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OneComparatorScoringNoMaxScoreCollector extends TopFieldCollector.OneComparatorNonScoringCollector
/*     */   {
/*     */     Scorer scorer;
/*     */ 
/*     */     public OneComparatorScoringNoMaxScoreCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 163 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     final void updateBottom(int doc, float score) {
/* 167 */       this.bottom.doc = (this.docBase + doc);
/* 168 */       this.bottom.score = score;
/* 169 */       this.bottom = ((FieldValueHitQueue.Entry)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 174 */       this.totalHits += 1;
/* 175 */       if (this.queueFull) {
/* 176 */         if (this.reverseMul * this.comparator.compareBottom(doc) <= 0)
/*     */         {
/* 180 */           return;
/*     */         }
/*     */ 
/* 184 */         float score = this.scorer.score();
/*     */ 
/* 187 */         this.comparator.copy(this.bottom.slot, doc);
/* 188 */         updateBottom(doc, score);
/* 189 */         this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */       else {
/* 192 */         float score = this.scorer.score();
/*     */ 
/* 195 */         int slot = this.totalHits - 1;
/*     */ 
/* 197 */         this.comparator.copy(slot, doc);
/* 198 */         add(slot, doc, score);
/* 199 */         if (this.queueFull)
/* 200 */           this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer)
/*     */       throws IOException
/*     */     {
/* 207 */       this.scorer = scorer;
/* 208 */       this.comparator.setScorer(scorer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OutOfOrderOneComparatorNonScoringCollector extends TopFieldCollector.OneComparatorNonScoringCollector
/*     */   {
/*     */     public OutOfOrderOneComparatorNonScoringCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/* 116 */       super(numHits, fillFields);
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/* 121 */       this.totalHits += 1;
/* 122 */       if (this.queueFull)
/*     */       {
/* 124 */         int cmp = this.reverseMul * this.comparator.compareBottom(doc);
/* 125 */         if ((cmp < 0) || ((cmp == 0) && (doc + this.docBase > this.bottom.doc))) {
/* 126 */           return;
/*     */         }
/*     */ 
/* 130 */         this.comparator.copy(this.bottom.slot, doc);
/* 131 */         updateBottom(doc);
/* 132 */         this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */       else {
/* 135 */         int slot = this.totalHits - 1;
/*     */ 
/* 137 */         this.comparator.copy(slot, doc);
/* 138 */         add(slot, doc, (0.0F / 0.0F));
/* 139 */         if (this.queueFull)
/* 140 */           this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean acceptsDocsOutOfOrder()
/*     */     {
/* 147 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OneComparatorNonScoringCollector extends TopFieldCollector
/*     */   {
/*     */     final FieldComparator comparator;
/*     */     final int reverseMul;
/*     */ 
/*     */     public OneComparatorNonScoringCollector(FieldValueHitQueue queue, int numHits, boolean fillFields)
/*     */       throws IOException
/*     */     {
/*  55 */       super(numHits, fillFields, null);
/*  56 */       this.comparator = queue.getComparators()[0];
/*  57 */       this.reverseMul = queue.getReverseMul()[0];
/*     */     }
/*     */ 
/*     */     final void updateBottom(int doc)
/*     */     {
/*  62 */       this.bottom.doc = (this.docBase + doc);
/*  63 */       this.bottom = ((FieldValueHitQueue.Entry)this.pq.updateTop());
/*     */     }
/*     */ 
/*     */     public void collect(int doc) throws IOException
/*     */     {
/*  68 */       this.totalHits += 1;
/*  69 */       if (this.queueFull) {
/*  70 */         if (this.reverseMul * this.comparator.compareBottom(doc) <= 0)
/*     */         {
/*  74 */           return;
/*     */         }
/*     */ 
/*  78 */         this.comparator.copy(this.bottom.slot, doc);
/*  79 */         updateBottom(doc);
/*  80 */         this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */       else {
/*  83 */         int slot = this.totalHits - 1;
/*     */ 
/*  85 */         this.comparator.copy(slot, doc);
/*  86 */         add(slot, doc, (0.0F / 0.0F));
/*  87 */         if (this.queueFull)
/*  88 */           this.comparator.setBottom(this.bottom.slot);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase)
/*     */       throws IOException
/*     */     {
/*  95 */       this.docBase = docBase;
/*  96 */       this.comparator.setNextReader(reader, docBase);
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer) throws IOException
/*     */     {
/* 101 */       this.comparator.setScorer(scorer);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TopFieldCollector
 * JD-Core Version:    0.6.2
 */