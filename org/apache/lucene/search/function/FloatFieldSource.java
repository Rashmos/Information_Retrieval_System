/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.search.FieldCache.FloatParser;
/*     */ 
/*     */ public class FloatFieldSource extends FieldCacheSource
/*     */ {
/*     */   private FieldCache.FloatParser parser;
/*     */ 
/*     */   public FloatFieldSource(String field)
/*     */   {
/*  57 */     this(field, null);
/*     */   }
/*     */ 
/*     */   public FloatFieldSource(String field, FieldCache.FloatParser parser)
/*     */   {
/*  64 */     super(field);
/*  65 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  71 */     return "float(" + super.description() + ')';
/*     */   }
/*     */ 
/*     */   public DocValues getCachedFieldValues(FieldCache cache, String field, IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  77 */     final float[] arr = cache.getFloats(reader, field, this.parser);
/*  78 */     return new DocValues()
/*     */     {
/*     */       public float floatVal(int doc)
/*     */       {
/*  82 */         return arr[doc];
/*     */       }
/*     */ 
/*     */       public String toString(int doc)
/*     */       {
/*  87 */         return FloatFieldSource.this.description() + '=' + arr[doc];
/*     */       }
/*     */ 
/*     */       Object getInnerArray()
/*     */       {
/*  92 */         return arr;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean cachedFieldSourceEquals(FieldCacheSource o)
/*     */   {
/* 100 */     if (o.getClass() != FloatFieldSource.class) {
/* 101 */       return false;
/*     */     }
/* 103 */     FloatFieldSource other = (FloatFieldSource)o;
/* 104 */     return other.parser == null;
/*     */   }
/*     */ 
/*     */   public int cachedFieldSourceHashCode()
/*     */   {
/* 112 */     return this.parser == null ? Float.class.hashCode() : this.parser.getClass().hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.FloatFieldSource
 * JD-Core Version:    0.6.2
 */