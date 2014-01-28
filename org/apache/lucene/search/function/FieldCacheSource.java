/*    */ package org.apache.lucene.search.function;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.index.IndexReader;
/*    */ import org.apache.lucene.search.FieldCache;
/*    */ 
/*    */ public abstract class FieldCacheSource extends ValueSource
/*    */ {
/*    */   private String field;
/*    */ 
/*    */   public FieldCacheSource(String field)
/*    */   {
/* 56 */     this.field = field;
/*    */   }
/*    */ 
/*    */   public final DocValues getValues(IndexReader reader)
/*    */     throws IOException
/*    */   {
/* 62 */     return getCachedFieldValues(FieldCache.DEFAULT, this.field, reader);
/*    */   }
/*    */ 
/*    */   public String description()
/*    */   {
/* 68 */     return this.field;
/*    */   }
/*    */ 
/*    */   public abstract DocValues getCachedFieldValues(FieldCache paramFieldCache, String paramString, IndexReader paramIndexReader)
/*    */     throws IOException;
/*    */ 
/*    */   public final boolean equals(Object o)
/*    */   {
/* 82 */     if (!(o instanceof FieldCacheSource)) {
/* 83 */       return false;
/*    */     }
/* 85 */     FieldCacheSource other = (FieldCacheSource)o;
/* 86 */     return (this.field.equals(other.field)) && (cachedFieldSourceEquals(other));
/*    */   }
/*    */ 
/*    */   public final int hashCode()
/*    */   {
/* 94 */     return this.field.hashCode() + cachedFieldSourceHashCode();
/*    */   }
/*    */ 
/*    */   public abstract boolean cachedFieldSourceEquals(FieldCacheSource paramFieldCacheSource);
/*    */ 
/*    */   public abstract int cachedFieldSourceHashCode();
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.function.FieldCacheSource
 * JD-Core Version:    0.6.2
 */