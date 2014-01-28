/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.search.FieldCache.StringIndex;
/*     */ 
/*     */ public class ReverseOrdFieldSource extends ValueSource
/*     */ {
/*     */   public String field;
/* 119 */   private static final int hcode = ReverseOrdFieldSource.class.hashCode();
/*     */ 
/*     */   public ReverseOrdFieldSource(String field)
/*     */   {
/*  64 */     this.field = field;
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  70 */     return "rord(" + this.field + ')';
/*     */   }
/*     */ 
/*     */   public DocValues getValues(IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  76 */     FieldCache.StringIndex sindex = FieldCache.DEFAULT.getStringIndex(reader, this.field);
/*     */ 
/*  78 */     final int[] arr = sindex.order;
/*  79 */     final int end = sindex.lookup.length;
/*     */ 
/*  81 */     return new DocValues()
/*     */     {
/*     */       public float floatVal(int doc)
/*     */       {
/*  85 */         return end - arr[doc];
/*     */       }
/*     */ 
/*     */       public int intVal(int doc)
/*     */       {
/*  90 */         return end - arr[doc];
/*     */       }
/*     */ 
/*     */       public String strVal(int doc)
/*     */       {
/*  96 */         return Integer.toString(intVal(doc));
/*     */       }
/*     */ 
/*     */       public String toString(int doc)
/*     */       {
/* 101 */         return ReverseOrdFieldSource.this.description() + '=' + strVal(doc);
/*     */       }
/*     */ 
/*     */       Object getInnerArray()
/*     */       {
/* 106 */         return arr;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 114 */     if (o.getClass() != ReverseOrdFieldSource.class) return false;
/* 115 */     ReverseOrdFieldSource other = (ReverseOrdFieldSource)o;
/* 116 */     return this.field.equals(other.field);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 124 */     return hcode + this.field.hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.ReverseOrdFieldSource
 * JD-Core Version:    0.6.2
 */