/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.search.FieldCache.ShortParser;
/*     */ 
/*     */ public class ShortFieldSource extends FieldCacheSource
/*     */ {
/*     */   private FieldCache.ShortParser parser;
/*     */ 
/*     */   public ShortFieldSource(String field)
/*     */   {
/*  57 */     this(field, null);
/*     */   }
/*     */ 
/*     */   public ShortFieldSource(String field, FieldCache.ShortParser parser)
/*     */   {
/*  64 */     super(field);
/*  65 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  71 */     return "short(" + super.description() + ')';
/*     */   }
/*     */ 
/*     */   public DocValues getCachedFieldValues(FieldCache cache, String field, IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  77 */     final short[] arr = cache.getShorts(reader, field, this.parser);
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
/*  92 */         return ShortFieldSource.this.description() + '=' + intVal(doc);
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
/* 105 */     if (o.getClass() != ShortFieldSource.class) {
/* 106 */       return false;
/*     */     }
/* 108 */     ShortFieldSource other = (ShortFieldSource)o;
/* 109 */     return other.parser == null;
/*     */   }
/*     */ 
/*     */   public int cachedFieldSourceHashCode()
/*     */   {
/* 117 */     return this.parser == null ? Short.class.hashCode() : this.parser.getClass().hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.ShortFieldSource
 * JD-Core Version:    0.6.2
 */