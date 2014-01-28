/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.Collator;
/*     */ import java.util.Locale;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ 
/*     */ public abstract class FieldComparator
/*     */ {
/*     */   public abstract int compare(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void setBottom(int paramInt);
/*     */ 
/*     */   public abstract int compareBottom(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void copy(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void setNextReader(IndexReader paramIndexReader, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public void setScorer(Scorer scorer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract Comparable value(int paramInt);
/*     */ 
/*     */   protected static final int binarySearch(String[] a, String key)
/*     */   {
/* 914 */     return binarySearch(a, key, 0, a.length - 1);
/*     */   }
/*     */ 
/*     */   protected static final int binarySearch(String[] a, String key, int low, int high)
/*     */   {
/* 919 */     while (low <= high) {
/* 920 */       int mid = low + high >>> 1;
/* 921 */       String midVal = a[mid];
/*     */       int cmp;
/*     */       int cmp;
/* 923 */       if (midVal != null)
/* 924 */         cmp = midVal.compareTo(key);
/*     */       else {
/* 926 */         cmp = -1;
/*     */       }
/*     */ 
/* 929 */       if (cmp < 0)
/* 930 */         low = mid + 1;
/* 931 */       else if (cmp > 0)
/* 932 */         high = mid - 1;
/*     */       else
/* 934 */         return mid;
/*     */     }
/* 936 */     return -(low + 1);
/*     */   }
/*     */ 
/*     */   public static final class StringValComparator extends FieldComparator
/*     */   {
/*     */     private String[] values;
/*     */     private String[] currentReaderValues;
/*     */     private final String field;
/*     */     private String bottom;
/*     */ 
/*     */     StringValComparator(int numHits, String field)
/*     */     {
/* 858 */       this.values = new String[numHits];
/* 859 */       this.field = field;
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 864 */       String val1 = this.values[slot1];
/* 865 */       String val2 = this.values[slot2];
/* 866 */       if (val1 == null) {
/* 867 */         if (val2 == null) {
/* 868 */           return 0;
/*     */         }
/* 870 */         return -1;
/* 871 */       }if (val2 == null) {
/* 872 */         return 1;
/*     */       }
/*     */ 
/* 875 */       return val1.compareTo(val2);
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 880 */       String val2 = this.currentReaderValues[doc];
/* 881 */       if (this.bottom == null) {
/* 882 */         if (val2 == null) {
/* 883 */           return 0;
/*     */         }
/* 885 */         return -1;
/* 886 */       }if (val2 == null) {
/* 887 */         return 1;
/*     */       }
/* 889 */       return this.bottom.compareTo(val2);
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 894 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 899 */       this.currentReaderValues = FieldCache.DEFAULT.getStrings(reader, this.field);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 904 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 909 */       return this.values[slot];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class StringOrdValComparator extends FieldComparator
/*     */   {
/*     */     private final int[] ords;
/*     */     private final String[] values;
/*     */     private final int[] readerGen;
/* 700 */     private int currentReaderGen = -1;
/*     */     private String[] lookup;
/*     */     private int[] order;
/*     */     private final String field;
/* 705 */     private int bottomSlot = -1;
/*     */     private int bottomOrd;
/*     */     private String bottomValue;
/*     */     private final boolean reversed;
/*     */     private final int sortPos;
/*     */ 
/*     */     public StringOrdValComparator(int numHits, String field, int sortPos, boolean reversed)
/*     */     {
/* 712 */       this.ords = new int[numHits];
/* 713 */       this.values = new String[numHits];
/* 714 */       this.readerGen = new int[numHits];
/* 715 */       this.sortPos = sortPos;
/* 716 */       this.reversed = reversed;
/* 717 */       this.field = field;
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 722 */       if (this.readerGen[slot1] == this.readerGen[slot2]) {
/* 723 */         int cmp = this.ords[slot1] - this.ords[slot2];
/* 724 */         if (cmp != 0) {
/* 725 */           return cmp;
/*     */         }
/*     */       }
/*     */ 
/* 729 */       String val1 = this.values[slot1];
/* 730 */       String val2 = this.values[slot2];
/* 731 */       if (val1 == null) {
/* 732 */         if (val2 == null) {
/* 733 */           return 0;
/*     */         }
/* 735 */         return -1;
/* 736 */       }if (val2 == null) {
/* 737 */         return 1;
/*     */       }
/* 739 */       return val1.compareTo(val2);
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 744 */       assert (this.bottomSlot != -1);
/* 745 */       int order = this.order[doc];
/* 746 */       int cmp = this.bottomOrd - order;
/* 747 */       if (cmp != 0) {
/* 748 */         return cmp;
/*     */       }
/*     */ 
/* 751 */       String val2 = this.lookup[order];
/* 752 */       if (this.bottomValue == null) {
/* 753 */         if (val2 == null) {
/* 754 */           return 0;
/*     */         }
/*     */ 
/* 757 */         return -1;
/* 758 */       }if (val2 == null)
/*     */       {
/* 760 */         return 1;
/*     */       }
/* 762 */       return this.bottomValue.compareTo(val2);
/*     */     }
/*     */ 
/*     */     private void convert(int slot) {
/* 766 */       this.readerGen[slot] = this.currentReaderGen;
/* 767 */       int index = 0;
/* 768 */       String value = this.values[slot];
/* 769 */       if (value == null) {
/* 770 */         this.ords[slot] = 0;
/* 771 */         return;
/*     */       }
/*     */ 
/* 774 */       if ((this.sortPos == 0) && (this.bottomSlot != -1) && (this.bottomSlot != slot))
/*     */       {
/* 777 */         assert (this.bottomOrd < this.lookup.length);
/* 778 */         if (this.reversed)
/* 779 */           index = binarySearch(this.lookup, value, this.bottomOrd, this.lookup.length - 1);
/*     */         else
/* 781 */           index = binarySearch(this.lookup, value, 0, this.bottomOrd);
/*     */       }
/*     */       else
/*     */       {
/* 785 */         index = binarySearch(this.lookup, value);
/*     */       }
/*     */ 
/* 788 */       if (index < 0) {
/* 789 */         index = -index - 2;
/*     */       }
/* 791 */       this.ords[slot] = index;
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 796 */       int ord = this.order[doc];
/* 797 */       this.ords[slot] = ord;
/* 798 */       assert (ord >= 0);
/* 799 */       this.values[slot] = this.lookup[ord];
/* 800 */       this.readerGen[slot] = this.currentReaderGen;
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 805 */       FieldCache.StringIndex currentReaderValues = FieldCache.DEFAULT.getStringIndex(reader, this.field);
/* 806 */       this.currentReaderGen += 1;
/* 807 */       this.order = currentReaderValues.order;
/* 808 */       this.lookup = currentReaderValues.lookup;
/* 809 */       assert (this.lookup.length > 0);
/* 810 */       if (this.bottomSlot != -1) {
/* 811 */         convert(this.bottomSlot);
/* 812 */         this.bottomOrd = this.ords[this.bottomSlot];
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 818 */       this.bottomSlot = bottom;
/* 819 */       if (this.readerGen[bottom] != this.currentReaderGen) {
/* 820 */         convert(this.bottomSlot);
/*     */       }
/* 822 */       this.bottomOrd = this.ords[bottom];
/* 823 */       assert (this.bottomOrd >= 0);
/* 824 */       assert (this.bottomOrd < this.lookup.length);
/* 825 */       this.bottomValue = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 830 */       return this.values[slot];
/*     */     }
/*     */ 
/*     */     public String[] getValues() {
/* 834 */       return this.values;
/*     */     }
/*     */ 
/*     */     public int getBottomSlot() {
/* 838 */       return this.bottomSlot;
/*     */     }
/*     */ 
/*     */     public String getField() {
/* 842 */       return this.field;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class StringComparatorLocale extends FieldComparator
/*     */   {
/*     */     private final String[] values;
/*     */     private String[] currentReaderValues;
/*     */     private final String field;
/*     */     final Collator collator;
/*     */     private String bottom;
/*     */ 
/*     */     StringComparatorLocale(int numHits, String field, Locale locale)
/*     */     {
/* 630 */       this.values = new String[numHits];
/* 631 */       this.field = field;
/* 632 */       this.collator = Collator.getInstance(locale);
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 637 */       String val1 = this.values[slot1];
/* 638 */       String val2 = this.values[slot2];
/* 639 */       if (val1 == null) {
/* 640 */         if (val2 == null) {
/* 641 */           return 0;
/*     */         }
/* 643 */         return -1;
/* 644 */       }if (val2 == null) {
/* 645 */         return 1;
/*     */       }
/* 647 */       return this.collator.compare(val1, val2);
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 652 */       String val2 = this.currentReaderValues[doc];
/* 653 */       if (this.bottom == null) {
/* 654 */         if (val2 == null) {
/* 655 */           return 0;
/*     */         }
/* 657 */         return -1;
/* 658 */       }if (val2 == null) {
/* 659 */         return 1;
/*     */       }
/* 661 */       return this.collator.compare(this.bottom, val2);
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 666 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 671 */       this.currentReaderValues = FieldCache.DEFAULT.getStrings(reader, this.field);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 676 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 681 */       return this.values[slot];
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class ShortComparator extends FieldComparator
/*     */   {
/*     */     private final short[] values;
/*     */     private short[] currentReaderValues;
/*     */     private final String field;
/*     */     private FieldCache.ShortParser parser;
/*     */     private short bottom;
/*     */ 
/*     */     ShortComparator(int numHits, String field, FieldCache.Parser parser)
/*     */     {
/* 583 */       this.values = new short[numHits];
/* 584 */       this.field = field;
/* 585 */       this.parser = ((FieldCache.ShortParser)parser);
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 590 */       return this.values[slot1] - this.values[slot2];
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 595 */       return this.bottom - this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 600 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 605 */       this.currentReaderValues = FieldCache.DEFAULT.getShorts(reader, this.field, this.parser);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 610 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 615 */       return Short.valueOf(this.values[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class RelevanceComparator extends FieldComparator
/*     */   {
/*     */     private final float[] scores;
/*     */     private float bottom;
/*     */     private Scorer scorer;
/*     */ 
/*     */     RelevanceComparator(int numHits)
/*     */     {
/* 530 */       this.scores = new float[numHits];
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 535 */       float score1 = this.scores[slot1];
/* 536 */       float score2 = this.scores[slot2];
/* 537 */       return score1 < score2 ? 1 : score1 > score2 ? -1 : 0;
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc) throws IOException
/*     */     {
/* 542 */       float score = this.scorer.score();
/* 543 */       return this.bottom < score ? 1 : this.bottom > score ? -1 : 0;
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc) throws IOException
/*     */     {
/* 548 */       this.scores[slot] = this.scorer.score();
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 557 */       this.bottom = this.scores[bottom];
/*     */     }
/*     */ 
/*     */     public void setScorer(Scorer scorer)
/*     */     {
/* 564 */       this.scorer = new ScoreCachingWrappingScorer(scorer);
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 569 */       return Float.valueOf(this.scores[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class LongComparator extends FieldComparator
/*     */   {
/*     */     private final long[] values;
/*     */     private long[] currentReaderValues;
/*     */     private final String field;
/*     */     private FieldCache.LongParser parser;
/*     */     private long bottom;
/*     */ 
/*     */     LongComparator(int numHits, String field, FieldCache.Parser parser)
/*     */     {
/* 463 */       this.values = new long[numHits];
/* 464 */       this.field = field;
/* 465 */       this.parser = ((FieldCache.LongParser)parser);
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 472 */       long v1 = this.values[slot1];
/* 473 */       long v2 = this.values[slot2];
/* 474 */       if (v1 > v2)
/* 475 */         return 1;
/* 476 */       if (v1 < v2) {
/* 477 */         return -1;
/*     */       }
/* 479 */       return 0;
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 487 */       long v2 = this.currentReaderValues[doc];
/* 488 */       if (this.bottom > v2)
/* 489 */         return 1;
/* 490 */       if (this.bottom < v2) {
/* 491 */         return -1;
/*     */       }
/* 493 */       return 0;
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 499 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 504 */       this.currentReaderValues = FieldCache.DEFAULT.getLongs(reader, this.field, this.parser);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 509 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 514 */       return Long.valueOf(this.values[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class IntComparator extends FieldComparator
/*     */   {
/*     */     private final int[] values;
/*     */     private int[] currentReaderValues;
/*     */     private final String field;
/*     */     private FieldCache.IntParser parser;
/*     */     private int bottom;
/*     */ 
/*     */     IntComparator(int numHits, String field, FieldCache.Parser parser)
/*     */     {
/* 394 */       this.values = new int[numHits];
/* 395 */       this.field = field;
/* 396 */       this.parser = ((FieldCache.IntParser)parser);
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 405 */       int v1 = this.values[slot1];
/* 406 */       int v2 = this.values[slot2];
/* 407 */       if (v1 > v2)
/* 408 */         return 1;
/* 409 */       if (v1 < v2) {
/* 410 */         return -1;
/*     */       }
/* 412 */       return 0;
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 422 */       int v2 = this.currentReaderValues[doc];
/* 423 */       if (this.bottom > v2)
/* 424 */         return 1;
/* 425 */       if (this.bottom < v2) {
/* 426 */         return -1;
/*     */       }
/* 428 */       return 0;
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 434 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 439 */       this.currentReaderValues = FieldCache.DEFAULT.getInts(reader, this.field, this.parser);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 444 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 449 */       return Integer.valueOf(this.values[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class FloatComparator extends FieldComparator
/*     */   {
/*     */     private final float[] values;
/*     */     private float[] currentReaderValues;
/*     */     private final String field;
/*     */     private FieldCache.FloatParser parser;
/*     */     private float bottom;
/*     */ 
/*     */     FloatComparator(int numHits, String field, FieldCache.Parser parser)
/*     */     {
/* 329 */       this.values = new float[numHits];
/* 330 */       this.field = field;
/* 331 */       this.parser = ((FieldCache.FloatParser)parser);
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 338 */       float v1 = this.values[slot1];
/* 339 */       float v2 = this.values[slot2];
/* 340 */       if (v1 > v2)
/* 341 */         return 1;
/* 342 */       if (v1 < v2) {
/* 343 */         return -1;
/*     */       }
/* 345 */       return 0;
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 353 */       float v2 = this.currentReaderValues[doc];
/* 354 */       if (this.bottom > v2)
/* 355 */         return 1;
/* 356 */       if (this.bottom < v2) {
/* 357 */         return -1;
/*     */       }
/* 359 */       return 0;
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 365 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 370 */       this.currentReaderValues = FieldCache.DEFAULT.getFloats(reader, this.field, this.parser);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 375 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 380 */       return Float.valueOf(this.values[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class DoubleComparator extends FieldComparator
/*     */   {
/*     */     private final double[] values;
/*     */     private double[] currentReaderValues;
/*     */     private final String field;
/*     */     private FieldCache.DoubleParser parser;
/*     */     private double bottom;
/*     */ 
/*     */     DoubleComparator(int numHits, String field, FieldCache.Parser parser)
/*     */     {
/* 268 */       this.values = new double[numHits];
/* 269 */       this.field = field;
/* 270 */       this.parser = ((FieldCache.DoubleParser)parser);
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 275 */       double v1 = this.values[slot1];
/* 276 */       double v2 = this.values[slot2];
/* 277 */       if (v1 > v2)
/* 278 */         return 1;
/* 279 */       if (v1 < v2) {
/* 280 */         return -1;
/*     */       }
/* 282 */       return 0;
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 288 */       double v2 = this.currentReaderValues[doc];
/* 289 */       if (this.bottom > v2)
/* 290 */         return 1;
/* 291 */       if (this.bottom < v2) {
/* 292 */         return -1;
/*     */       }
/* 294 */       return 0;
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 300 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 305 */       this.currentReaderValues = FieldCache.DEFAULT.getDoubles(reader, this.field, this.parser);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 310 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 315 */       return Double.valueOf(this.values[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class DocComparator extends FieldComparator
/*     */   {
/*     */     private final int[] docIDs;
/*     */     private int docBase;
/*     */     private int bottom;
/*     */ 
/*     */     DocComparator(int numHits)
/*     */     {
/* 219 */       this.docIDs = new int[numHits];
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 225 */       return this.docIDs[slot1] - this.docIDs[slot2];
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 231 */       return this.bottom - (this.docBase + doc);
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 236 */       this.docIDs[slot] = (this.docBase + doc);
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase)
/*     */     {
/* 244 */       this.docBase = docBase;
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 249 */       this.bottom = this.docIDs[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 254 */       return Integer.valueOf(this.docIDs[slot]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class ByteComparator extends FieldComparator
/*     */   {
/*     */     private final byte[] values;
/*     */     private byte[] currentReaderValues;
/*     */     private final String field;
/*     */     private FieldCache.ByteParser parser;
/*     */     private byte bottom;
/*     */ 
/*     */     ByteComparator(int numHits, String field, FieldCache.Parser parser)
/*     */     {
/* 176 */       this.values = new byte[numHits];
/* 177 */       this.field = field;
/* 178 */       this.parser = ((FieldCache.ByteParser)parser);
/*     */     }
/*     */ 
/*     */     public int compare(int slot1, int slot2)
/*     */     {
/* 183 */       return this.values[slot1] - this.values[slot2];
/*     */     }
/*     */ 
/*     */     public int compareBottom(int doc)
/*     */     {
/* 188 */       return this.bottom - this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void copy(int slot, int doc)
/*     */     {
/* 193 */       this.values[slot] = this.currentReaderValues[doc];
/*     */     }
/*     */ 
/*     */     public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */     {
/* 198 */       this.currentReaderValues = FieldCache.DEFAULT.getBytes(reader, this.field, this.parser);
/*     */     }
/*     */ 
/*     */     public void setBottom(int bottom)
/*     */     {
/* 203 */       this.bottom = this.values[bottom];
/*     */     }
/*     */ 
/*     */     public Comparable value(int slot)
/*     */     {
/* 208 */       return Byte.valueOf(this.values[slot]);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FieldComparator
 * JD-Core Version:    0.6.2
 */