/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.search.FieldCache.StringIndex;
/*     */ 
/*     */ public class OrdFieldSource extends ValueSource
/*     */ {
/*     */   protected String field;
/* 109 */   private static final int hcode = OrdFieldSource.class.hashCode();
/*     */ 
/*     */   public OrdFieldSource(String field)
/*     */   {
/*  63 */     this.field = field;
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  69 */     return "ord(" + this.field + ')';
/*     */   }
/*     */ 
/*     */   public DocValues getValues(IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  75 */     final int[] arr = FieldCache.DEFAULT.getStringIndex(reader, this.field).order;
/*  76 */     return new DocValues()
/*     */     {
/*     */       public float floatVal(int doc)
/*     */       {
/*  80 */         return arr[doc];
/*     */       }
/*     */ 
/*     */       public String strVal(int doc)
/*     */       {
/*  86 */         return Integer.toString(arr[doc]);
/*     */       }
/*     */ 
/*     */       public String toString(int doc)
/*     */       {
/*  91 */         return OrdFieldSource.this.description() + '=' + intVal(doc);
/*     */       }
/*     */ 
/*     */       Object getInnerArray()
/*     */       {
/*  96 */         return arr;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 104 */     if (o.getClass() != OrdFieldSource.class) return false;
/* 105 */     OrdFieldSource other = (OrdFieldSource)o;
/* 106 */     return this.field.equals(other.field);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 114 */     return hcode + this.field.hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.OrdFieldSource
 * JD-Core Version:    0.6.2
 */