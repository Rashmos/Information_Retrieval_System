/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ public final class IndexReader$FieldOption
/*     */ {
/*     */   private String option;
/*  93 */   public static final FieldOption ALL = new FieldOption("ALL");
/*     */ 
/*  95 */   public static final FieldOption INDEXED = new FieldOption("INDEXED");
/*     */ 
/*  97 */   public static final FieldOption STORES_PAYLOADS = new FieldOption("STORES_PAYLOADS");
/*     */ 
/*  99 */   public static final FieldOption OMIT_TERM_FREQ_AND_POSITIONS = new FieldOption("OMIT_TERM_FREQ_AND_POSITIONS");
/*     */ 
/* 101 */   public static final FieldOption UNINDEXED = new FieldOption("UNINDEXED");
/*     */ 
/* 103 */   public static final FieldOption INDEXED_WITH_TERMVECTOR = new FieldOption("INDEXED_WITH_TERMVECTOR");
/*     */ 
/* 105 */   public static final FieldOption INDEXED_NO_TERMVECTOR = new FieldOption("INDEXED_NO_TERMVECTOR");
/*     */ 
/* 107 */   public static final FieldOption TERMVECTOR = new FieldOption("TERMVECTOR");
/*     */ 
/* 109 */   public static final FieldOption TERMVECTOR_WITH_POSITION = new FieldOption("TERMVECTOR_WITH_POSITION");
/*     */ 
/* 111 */   public static final FieldOption TERMVECTOR_WITH_OFFSET = new FieldOption("TERMVECTOR_WITH_OFFSET");
/*     */ 
/* 113 */   public static final FieldOption TERMVECTOR_WITH_POSITION_OFFSET = new FieldOption("TERMVECTOR_WITH_POSITION_OFFSET");
/*     */ 
/*     */   private IndexReader$FieldOption()
/*     */   {
/*     */   }
/*     */ 
/*     */   private IndexReader$FieldOption(String option)
/*     */   {
/*  86 */     this.option = option;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  90 */     return this.option;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexReader.FieldOption
 * JD-Core Version:    0.6.2
 */