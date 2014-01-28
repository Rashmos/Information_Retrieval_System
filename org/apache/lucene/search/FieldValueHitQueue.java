/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.util.PriorityQueue;
/*     */ 
/*     */ public abstract class FieldValueHitQueue extends PriorityQueue<Entry>
/*     */ {
/*     */   protected final SortField[] fields;
/*     */   protected final FieldComparator[] comparators;
/*     */   protected final int[] reverseMul;
/*     */ 
/*     */   private FieldValueHitQueue(SortField[] fields)
/*     */   {
/* 152 */     this.fields = fields;
/* 153 */     int numComparators = fields.length;
/* 154 */     this.comparators = new FieldComparator[numComparators];
/* 155 */     this.reverseMul = new int[numComparators];
/*     */   }
/*     */ 
/*     */   public static FieldValueHitQueue create(SortField[] fields, int size)
/*     */     throws IOException
/*     */   {
/* 173 */     if (fields.length == 0) {
/* 174 */       throw new IllegalArgumentException("Sort must contain at least one field");
/*     */     }
/*     */ 
/* 177 */     if (fields.length == 1) {
/* 178 */       return new OneComparatorFieldValueHitQueue(fields, size);
/*     */     }
/* 180 */     return new MultiComparatorsFieldValueHitQueue(fields, size);
/*     */   }
/*     */ 
/*     */   FieldComparator[] getComparators() {
/* 184 */     return this.comparators;
/*     */   }
/* 186 */   int[] getReverseMul() { return this.reverseMul; }
/*     */ 
/*     */ 
/*     */   protected abstract boolean lessThan(Entry paramEntry1, Entry paramEntry2);
/*     */ 
/*     */   FieldDoc fillFields(Entry entry)
/*     */   {
/* 208 */     int n = this.comparators.length;
/* 209 */     Comparable[] fields = new Comparable[n];
/* 210 */     for (int i = 0; i < n; i++) {
/* 211 */       fields[i] = this.comparators[i].value(entry.slot);
/*     */     }
/*     */ 
/* 214 */     return new FieldDoc(entry.doc, entry.score, fields);
/*     */   }
/*     */ 
/*     */   SortField[] getFields()
/*     */   {
/* 219 */     return this.fields;
/*     */   }
/*     */ 
/*     */   private static final class MultiComparatorsFieldValueHitQueue extends FieldValueHitQueue
/*     */   {
/*     */     public MultiComparatorsFieldValueHitQueue(SortField[] fields, int size)
/*     */       throws IOException
/*     */     {
/* 110 */       super(null);
/*     */ 
/* 112 */       int numComparators = this.comparators.length;
/* 113 */       for (int i = 0; i < numComparators; i++) {
/* 114 */         SortField field = fields[i];
/*     */ 
/* 116 */         this.reverseMul[i] = (field.reverse ? -1 : 1);
/* 117 */         this.comparators[i] = field.getComparator(size, i);
/*     */       }
/*     */ 
/* 120 */       initialize(size);
/*     */     }
/*     */ 
/*     */     protected boolean lessThan(FieldValueHitQueue.Entry hitA, FieldValueHitQueue.Entry hitB)
/*     */     {
/* 126 */       assert (hitA != hitB);
/* 127 */       assert (hitA.slot != hitB.slot);
/*     */ 
/* 129 */       int numComparators = this.comparators.length;
/* 130 */       for (int i = 0; i < numComparators; i++) {
/* 131 */         int c = this.reverseMul[i] * this.comparators[i].compare(hitA.slot, hitB.slot);
/* 132 */         if (c != 0)
/*     */         {
/* 134 */           return c > 0;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 139 */       return hitA.doc > hitB.doc;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class OneComparatorFieldValueHitQueue extends FieldValueHitQueue
/*     */   {
/*     */     private final FieldComparator comparator;
/*     */     private final int oneReverseMul;
/*     */ 
/*     */     public OneComparatorFieldValueHitQueue(SortField[] fields, int size)
/*     */       throws IOException
/*     */     {
/*  64 */       super(null);
/*  65 */       if (fields.length == 0) {
/*  66 */         throw new IllegalArgumentException("Sort must contain at least one field");
/*     */       }
/*     */ 
/*  69 */       SortField field = fields[0];
/*  70 */       this.comparator = field.getComparator(size, 0);
/*  71 */       this.oneReverseMul = (field.reverse ? -1 : 1);
/*     */ 
/*  73 */       this.comparators[0] = this.comparator;
/*  74 */       this.reverseMul[0] = this.oneReverseMul;
/*     */ 
/*  76 */       initialize(size);
/*     */     }
/*     */ 
/*     */     protected boolean lessThan(FieldValueHitQueue.Entry hitA, FieldValueHitQueue.Entry hitB)
/*     */     {
/*  88 */       assert (hitA != hitB);
/*  89 */       assert (hitA.slot != hitB.slot);
/*     */ 
/*  91 */       int c = this.oneReverseMul * this.comparator.compare(hitA.slot, hitB.slot);
/*  92 */       if (c != 0) {
/*  93 */         return c > 0;
/*     */       }
/*     */ 
/*  97 */       return hitA.doc > hitB.doc;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Entry extends ScoreDoc
/*     */   {
/*     */     int slot;
/*     */ 
/*     */     Entry(int slot, int doc, float score)
/*     */     {
/*  43 */       super(score);
/*  44 */       this.slot = slot;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/*  49 */       return "slot:" + this.slot + " " + super.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FieldValueHitQueue
 * JD-Core Version:    0.6.2
 */