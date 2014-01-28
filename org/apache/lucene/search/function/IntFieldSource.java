/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.search.FieldCache.IntParser;
/*     */ 
/*     */ public class IntFieldSource extends FieldCacheSource
/*     */ {
/*     */   private FieldCache.IntParser parser;
/*     */ 
/*     */   public IntFieldSource(String field)
/*     */   {
/*  57 */     this(field, null);
/*     */   }
/*     */ 
/*     */   public IntFieldSource(String field, FieldCache.IntParser parser)
/*     */   {
/*  64 */     super(field);
/*  65 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  71 */     return "int(" + super.description() + ')';
/*     */   }
/*     */ 
/*     */   public DocValues getCachedFieldValues(FieldCache cache, String field, IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  77 */     final int[] arr = cache.getInts(reader, field, this.parser);
/*  78 */     return new DocValues()
/*     */     {
/*     */       public float floatVal(int doc)
/*     */       {
/*  82 */         return arr[doc];
/*     */       }
/*     */ 
/*     */       public int intVal(int doc)
/*     */       {
/*  87 */         return arr[doc];
/*     */       }
/*     */ 
/*     */       public String toString(int doc)
/*     */       {
/*  92 */         return IntFieldSource.this.description() + '=' + intVal(doc);
/*     */       }
/*     */ 
/*     */       Object getInnerArray()
/*     */       {
/*  97 */         return arr;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean cachedFieldSourceEquals(FieldCacheSource o)
/*     */   {
/* 105 */     if (o.getClass() != IntFieldSource.class) {
/* 106 */       return false;
/*     */     }
/* 108 */     IntFieldSource other = (IntFieldSource)o;
/* 109 */     return other.parser == null;
/*     */   }
/*     */ 
/*     */   public int cachedFieldSourceHashCode()
/*     */   {
/* 117 */     return this.parser == null ? Integer.class.hashCode() : this.parser.getClass().hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.IntFieldSource
 * JD-Core Version:    0.6.2
 */