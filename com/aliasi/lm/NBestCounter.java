/*      */ package com.aliasi.lm;
/*      */ 
/*      */ import com.aliasi.util.BoundedPriorityQueue;
/*      */ import com.aliasi.util.ObjectToCounterMap;
/*      */ import java.util.Comparator;
/*      */ 
/*      */ class NBestCounter extends BoundedPriorityQueue<NBEntry>
/*      */ {
/*      */   static final long serialVersionUID = -1604467508550079460L;
/*      */   private final boolean mReversed;
/* 1569 */   static Comparator<NBEntry> COMPARATOR = new Comparator()
/*      */   {
/*      */     public int compare(NBestCounter.NBEntry entry1, NBestCounter.NBEntry entry2)
/*      */     {
/* 1573 */       return entry1.compareTo(entry2);
/*      */     }
/* 1569 */   };
/*      */ 
/*      */   public NBestCounter(int maxEntries)
/*      */   {
/* 1528 */     this(maxEntries, false);
/*      */   }
/*      */   public NBestCounter(int maxEntries, boolean reversed) {
/* 1531 */     super(COMPARATOR, maxEntries);
/* 1532 */     this.mReversed = reversed;
/*      */   }
/*      */   public ObjectToCounterMap<String> toObjectToCounter() {
/* 1535 */     ObjectToCounterMap otc = new ObjectToCounterMap();
/* 1536 */     for (NBEntry entry : this) {
/* 1537 */       if (entry.mCount > 2147483647L) {
/* 1538 */         String msg = "Entry too large.";
/* 1539 */         throw new IllegalArgumentException(msg);
/*      */       }
/* 1541 */       otc.set(entry.mString, (int)entry.mCount);
/*      */     }
/* 1543 */     return otc;
/*      */   }
/*      */   public void put(char[] cs, int length, long count) {
/* 1546 */     offer(new NBEntry(cs, length, count));
/*      */   }
/*      */   class NBEntry implements Comparable<NBEntry> {
/*      */     final String mString;
/*      */     final long mCount;
/*      */ 
/* 1553 */     public NBEntry(char[] cs, int length, long count) { this.mString = new String(cs, 0, length);
/* 1554 */       this.mCount = count; }
/*      */ 
/*      */     public int compareTo(NBEntry thatEntry) {
/* 1557 */       if (thatEntry.mCount == this.mCount)
/* 1558 */         return thatEntry.mString.compareTo(this.mString);
/* 1559 */       long diff = thatEntry.mCount - this.mCount;
/* 1560 */       int comp = diff > 0L ? 1 : diff < 0L ? -1 : 0;
/*      */ 
/* 1566 */       return NBestCounter.this.mReversed ? -comp : comp;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.NBestCounter
 * JD-Core Version:    0.6.2
 */