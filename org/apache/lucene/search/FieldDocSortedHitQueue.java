/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.text.Collator;
/*     */ import java.util.Locale;
/*     */ import org.apache.lucene.util.PriorityQueue;
/*     */ 
/*     */ class FieldDocSortedHitQueue extends PriorityQueue<FieldDoc>
/*     */ {
/*  35 */   volatile SortField[] fields = null;
/*     */ 
/*  39 */   volatile Collator[] collators = null;
/*     */ 
/*     */   FieldDocSortedHitQueue(int size)
/*     */   {
/*  48 */     initialize(size);
/*     */   }
/*     */ 
/*     */   void setFields(SortField[] fields)
/*     */   {
/*  61 */     this.fields = fields;
/*  62 */     this.collators = hasCollators(fields);
/*     */   }
/*     */ 
/*     */   SortField[] getFields()
/*     */   {
/*  68 */     return this.fields;
/*     */   }
/*     */ 
/*     */   private Collator[] hasCollators(SortField[] fields)
/*     */   {
/*  78 */     if (fields == null) return null;
/*  79 */     Collator[] ret = new Collator[fields.length];
/*  80 */     for (int i = 0; i < fields.length; i++) {
/*  81 */       Locale locale = fields[i].getLocale();
/*  82 */       if (locale != null)
/*  83 */         ret[i] = Collator.getInstance(locale);
/*     */     }
/*  85 */     return ret;
/*     */   }
/*     */ 
/*     */   protected final boolean lessThan(FieldDoc docA, FieldDoc docB)
/*     */   {
/*  97 */     int n = this.fields.length;
/*  98 */     int c = 0;
/*  99 */     for (int i = 0; (i < n) && (c == 0); i++) {
/* 100 */       int type = this.fields[i].getType();
/* 101 */       if (type == 3) {
/* 102 */         String s1 = (String)docA.fields[i];
/* 103 */         String s2 = (String)docB.fields[i];
/*     */ 
/* 107 */         if (s1 == null)
/* 108 */           c = s2 == null ? 0 : -1;
/* 109 */         else if (s2 == null)
/* 110 */           c = 1;
/* 111 */         else if (this.fields[i].getLocale() == null)
/* 112 */           c = s1.compareTo(s2);
/*     */         else
/* 114 */           c = this.collators[i].compare(s1, s2);
/*     */       }
/*     */       else {
/* 117 */         c = docA.fields[i].compareTo(docB.fields[i]);
/* 118 */         if (type == 0) {
/* 119 */           c = -c;
/*     */         }
/*     */       }
/*     */ 
/* 123 */       if (this.fields[i].getReverse()) {
/* 124 */         c = -c;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 129 */     if (c == 0) {
/* 130 */       return docA.doc > docB.doc;
/*     */     }
/* 132 */     return c > 0;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FieldDocSortedHitQueue
 * JD-Core Version:    0.6.2
 */