/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.TermDocs;
/*     */ import org.apache.lucene.util.NumericUtils;
/*     */ 
/*     */ public abstract class FieldCacheRangeFilter<T> extends Filter
/*     */ {
/*     */   final String field;
/*     */   final FieldCache.Parser parser;
/*     */   final T lowerVal;
/*     */   final T upperVal;
/*     */   final boolean includeLower;
/*     */   final boolean includeUpper;
/*     */ 
/*     */   private FieldCacheRangeFilter(String field, FieldCache.Parser parser, T lowerVal, T upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*  64 */     this.field = field;
/*  65 */     this.parser = parser;
/*  66 */     this.lowerVal = lowerVal;
/*  67 */     this.upperVal = upperVal;
/*  68 */     this.includeLower = includeLower;
/*  69 */     this.includeUpper = includeUpper;
/*     */   }
/*     */ 
/*     */   public abstract DocIdSet getDocIdSet(IndexReader paramIndexReader)
/*     */     throws IOException;
/*     */ 
/*     */   public static FieldCacheRangeFilter<String> newStringRange(String field, String lowerVal, String upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 9	org/apache/lucene/search/FieldCacheRangeFilter$1
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aconst_null
/*     */     //   6: aload_1
/*     */     //   7: aload_2
/*     */     //   8: iload_3
/*     */     //   9: iload 4
/*     */     //   11: invokespecial 10	org/apache/lucene/search/FieldCacheRangeFilter$1:<init>	(Ljava/lang/String;Lorg/apache/lucene/search/FieldCache$Parser;Ljava/lang/String;Ljava/lang/String;ZZ)V
/*     */     //   14: areturn
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Byte> newByteRange(String field, Byte lowerVal, Byte upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/* 140 */     return newByteRange(field, null, lowerVal, upperVal, includeLower, includeUpper);
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Byte> newByteRange(String field, FieldCache.ByteParser parser, Byte lowerVal, Byte upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 12	org/apache/lucene/search/FieldCacheRangeFilter$2
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_1
/*     */     //   6: aload_2
/*     */     //   7: aload_3
/*     */     //   8: iload 4
/*     */     //   10: iload 5
/*     */     //   12: invokespecial 13	org/apache/lucene/search/FieldCacheRangeFilter$2:<init>	(Ljava/lang/String;Lorg/apache/lucene/search/FieldCache$Parser;Ljava/lang/Byte;Ljava/lang/Byte;ZZ)V
/*     */     //   15: areturn
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Short> newShortRange(String field, Short lowerVal, Short upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/* 191 */     return newShortRange(field, null, lowerVal, upperVal, includeLower, includeUpper);
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Short> newShortRange(String field, FieldCache.ShortParser parser, Short lowerVal, Short upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 15	org/apache/lucene/search/FieldCacheRangeFilter$3
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_1
/*     */     //   6: aload_2
/*     */     //   7: aload_3
/*     */     //   8: iload 4
/*     */     //   10: iload 5
/*     */     //   12: invokespecial 16	org/apache/lucene/search/FieldCacheRangeFilter$3:<init>	(Ljava/lang/String;Lorg/apache/lucene/search/FieldCache$Parser;Ljava/lang/Short;Ljava/lang/Short;ZZ)V
/*     */     //   15: areturn
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Integer> newIntRange(String field, Integer lowerVal, Integer upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/* 242 */     return newIntRange(field, null, lowerVal, upperVal, includeLower, includeUpper);
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Integer> newIntRange(String field, FieldCache.IntParser parser, Integer lowerVal, Integer upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 18	org/apache/lucene/search/FieldCacheRangeFilter$4
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_1
/*     */     //   6: aload_2
/*     */     //   7: aload_3
/*     */     //   8: iload 4
/*     */     //   10: iload 5
/*     */     //   12: invokespecial 19	org/apache/lucene/search/FieldCacheRangeFilter$4:<init>	(Ljava/lang/String;Lorg/apache/lucene/search/FieldCache$Parser;Ljava/lang/Integer;Ljava/lang/Integer;ZZ)V
/*     */     //   15: areturn
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Long> newLongRange(String field, Long lowerVal, Long upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/* 293 */     return newLongRange(field, null, lowerVal, upperVal, includeLower, includeUpper);
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Long> newLongRange(String field, FieldCache.LongParser parser, Long lowerVal, Long upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 21	org/apache/lucene/search/FieldCacheRangeFilter$5
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_1
/*     */     //   6: aload_2
/*     */     //   7: aload_3
/*     */     //   8: iload 4
/*     */     //   10: iload 5
/*     */     //   12: invokespecial 22	org/apache/lucene/search/FieldCacheRangeFilter$5:<init>	(Ljava/lang/String;Lorg/apache/lucene/search/FieldCache$Parser;Ljava/lang/Long;Ljava/lang/Long;ZZ)V
/*     */     //   15: areturn
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Float> newFloatRange(String field, Float lowerVal, Float upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/* 344 */     return newFloatRange(field, null, lowerVal, upperVal, includeLower, includeUpper);
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Float> newFloatRange(String field, FieldCache.FloatParser parser, Float lowerVal, Float upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 24	org/apache/lucene/search/FieldCacheRangeFilter$6
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_1
/*     */     //   6: aload_2
/*     */     //   7: aload_3
/*     */     //   8: iload 4
/*     */     //   10: iload 5
/*     */     //   12: invokespecial 25	org/apache/lucene/search/FieldCacheRangeFilter$6:<init>	(Ljava/lang/String;Lorg/apache/lucene/search/FieldCache$Parser;Ljava/lang/Float;Ljava/lang/Float;ZZ)V
/*     */     //   15: areturn
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Double> newDoubleRange(String field, Double lowerVal, Double upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/* 399 */     return newDoubleRange(field, null, lowerVal, upperVal, includeLower, includeUpper);
/*     */   }
/*     */ 
/*     */   public static FieldCacheRangeFilter<Double> newDoubleRange(String field, FieldCache.DoubleParser parser, Double lowerVal, Double upperVal, boolean includeLower, boolean includeUpper)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 27	org/apache/lucene/search/FieldCacheRangeFilter$7
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: aload_1
/*     */     //   6: aload_2
/*     */     //   7: aload_3
/*     */     //   8: iload 4
/*     */     //   10: iload 5
/*     */     //   12: invokespecial 28	org/apache/lucene/search/FieldCacheRangeFilter$7:<init>	(Ljava/lang/String;Lorg/apache/lucene/search/FieldCache$Parser;Ljava/lang/Double;Ljava/lang/Double;ZZ)V
/*     */     //   15: areturn
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 450 */     StringBuilder sb = new StringBuilder(this.field).append(":");
/* 451 */     return (this.includeLower ? '[' : '{') + (this.lowerVal == null ? "*" : this.lowerVal.toString()) + " TO " + (this.upperVal == null ? "*" : this.upperVal.toString()) + (this.includeUpper ? ']' : '}');
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object o)
/*     */   {
/* 461 */     if (this == o) return true;
/* 462 */     if (!(o instanceof FieldCacheRangeFilter)) return false;
/* 463 */     FieldCacheRangeFilter other = (FieldCacheRangeFilter)o;
/*     */ 
/* 465 */     if ((!this.field.equals(other.field)) || (this.includeLower != other.includeLower) || (this.includeUpper != other.includeUpper))
/*     */     {
/* 468 */       return false;
/* 469 */     }if (this.lowerVal != null ? !this.lowerVal.equals(other.lowerVal) : other.lowerVal != null) return false;
/* 470 */     if (this.upperVal != null ? !this.upperVal.equals(other.upperVal) : other.upperVal != null) return false;
/* 471 */     if (this.parser != null ? !this.parser.equals(other.parser) : other.parser != null) return false;
/* 472 */     return true;
/*     */   }
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 477 */     int h = this.field.hashCode();
/* 478 */     h ^= (this.lowerVal != null ? this.lowerVal.hashCode() : 550356204);
/* 479 */     h = h << 1 | h >>> 31;
/* 480 */     h ^= (this.upperVal != null ? this.upperVal.hashCode() : -1674416163);
/* 481 */     h ^= (this.parser != null ? this.parser.hashCode() : -1572457324);
/* 482 */     h ^= (this.includeLower ? 1549299360 : -365038026) ^ (this.includeUpper ? 1721088258 : 1948649653);
/* 483 */     return h;
/*     */   }
/*     */ 
/*     */   public String getField() {
/* 487 */     return this.field;
/*     */   }
/*     */   public boolean includesLower() {
/* 490 */     return this.includeLower;
/*     */   }
/*     */   public boolean includesUpper() {
/* 493 */     return this.includeUpper;
/*     */   }
/*     */   public T getLowerVal() {
/* 496 */     return this.lowerVal;
/*     */   }
/*     */   public T getUpperVal() {
/* 499 */     return this.upperVal;
/*     */   }
/*     */   public FieldCache.Parser getParser() {
/* 502 */     return this.parser;
/*     */   }
/*     */   static abstract class FieldCacheDocIdSet extends DocIdSet {
/*     */     private final IndexReader reader;
/*     */     private boolean mayUseTermDocs;
/*     */ 
/* 509 */     FieldCacheDocIdSet(IndexReader reader, boolean mayUseTermDocs) { this.reader = reader;
/* 510 */       this.mayUseTermDocs = mayUseTermDocs;
/*     */     }
/*     */ 
/*     */     abstract boolean matchDoc(int paramInt)
/*     */       throws ArrayIndexOutOfBoundsException;
/*     */ 
/*     */     public boolean isCacheable()
/*     */     {
/* 519 */       return (!this.mayUseTermDocs) || (!this.reader.hasDeletions());
/*     */     }
/*     */ 
/*     */     public DocIdSetIterator iterator()
/*     */       throws IOException
/*     */     {
/*     */       final TermDocs termDocs;
/* 529 */       synchronized (this.reader) {
/* 530 */         termDocs = isCacheable() ? null : this.reader.termDocs(null);
/*     */       }
/* 532 */       if (termDocs != null)
/*     */       {
/* 534 */         return new DocIdSetIterator() {
/* 535 */           private int doc = -1;
/*     */ 
/*     */           public int docID()
/*     */           {
/* 539 */             return this.doc;
/*     */           }
/*     */ 
/*     */           public int nextDoc() throws IOException
/*     */           {
/*     */             do
/* 545 */               if (!termDocs.next())
/* 546 */                 return this.doc = 2147483647;
/* 547 */             while (!FieldCacheRangeFilter.FieldCacheDocIdSet.this.matchDoc(this.doc = termDocs.doc()));
/* 548 */             return this.doc;
/*     */           }
/*     */ 
/*     */           public int advance(int target) throws IOException
/*     */           {
/* 553 */             if (!termDocs.skipTo(target))
/* 554 */               return this.doc = 2147483647;
/* 555 */             while (!FieldCacheRangeFilter.FieldCacheDocIdSet.this.matchDoc(this.doc = termDocs.doc())) {
/* 556 */               if (!termDocs.next())
/* 557 */                 return this.doc = 2147483647;
/*     */             }
/* 559 */             return this.doc;
/*     */           }
/*     */ 
/*     */         };
/*     */       }
/*     */ 
/* 565 */       return new DocIdSetIterator() {
/* 566 */         private int doc = -1;
/*     */ 
/*     */         public int docID()
/*     */         {
/* 570 */           return this.doc;
/*     */         }
/*     */ 
/*     */         public int nextDoc()
/*     */         {
/*     */           try {
/*     */             do
/* 577 */               this.doc += 1;
/* 578 */             while (!FieldCacheRangeFilter.FieldCacheDocIdSet.this.matchDoc(this.doc));
/* 579 */             return this.doc; } catch (ArrayIndexOutOfBoundsException e) {
/*     */           }
/* 581 */           return this.doc = 2147483647;
/*     */         }
/*     */ 
/*     */         public int advance(int target)
/*     */         {
/*     */           try
/*     */           {
/* 588 */             this.doc = target;
/* 589 */             while (!FieldCacheRangeFilter.FieldCacheDocIdSet.this.matchDoc(this.doc)) {
/* 590 */               this.doc += 1;
/*     */             }
/* 592 */             return this.doc; } catch (ArrayIndexOutOfBoundsException e) {
/*     */           }
/* 594 */           return this.doc = 2147483647;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FieldCacheRangeFilter
 * JD-Core Version:    0.6.2
 */