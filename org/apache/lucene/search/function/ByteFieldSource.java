/*     */ package org.apache.lucene.search.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.search.FieldCache;
/*     */ import org.apache.lucene.search.FieldCache.ByteParser;
/*     */ 
/*     */ public class ByteFieldSource extends FieldCacheSource
/*     */ {
/*     */   private FieldCache.ByteParser parser;
/*     */ 
/*     */   public ByteFieldSource(String field)
/*     */   {
/*  57 */     this(field, null);
/*     */   }
/*     */ 
/*     */   public ByteFieldSource(String field, FieldCache.ByteParser parser)
/*     */   {
/*  64 */     super(field);
/*  65 */     this.parser = parser;
/*     */   }
/*     */ 
/*     */   public String description()
/*     */   {
/*  71 */     return "byte(" + super.description() + ')';
/*     */   }
/*     */ 
/*     */   public DocValues getCachedFieldValues(FieldCache cache, String field, IndexReader reader)
/*     */     throws IOException
/*     */   {
/*  77 */     final byte[] arr = cache.getBytes(reader, field, this.parser);
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
/*  92 */         return ByteFieldSource.this.description() + '=' + intVal(doc);
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
/* 105 */     if (o.getClass() != ByteFieldSource.class) {
/* 106 */       return false;
/*     */     }
/* 108 */     ByteFieldSource other = (ByteFieldSource)o;
/* 109 */     return other.parser == null;
/*     */   }
/*     */ 
/*     */   public int cachedFieldSourceHashCode()
/*     */   {
/* 117 */     return this.parser == null ? Byte.class.hashCode() : this.parser.getClass().hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.ByteFieldSource
 * JD-Core Version:    0.6.2
 */