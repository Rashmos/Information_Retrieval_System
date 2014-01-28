/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.util.OpenBitSetDISI;
/*     */ 
/*     */ public class CachingWrapperFilter extends Filter
/*     */ {
/*     */   Filter filter;
/*     */   protected final FilterCache<DocIdSet> cache;
/*     */   int hitCount;
/*     */   int missCount;
/*     */ 
/*     */   public CachingWrapperFilter(Filter filter)
/*     */   {
/* 133 */     this(filter, DeletesMode.IGNORE);
/*     */   }
/*     */ 
/*     */   public CachingWrapperFilter(Filter filter, DeletesMode deletesMode)
/*     */   {
/* 145 */     this.filter = filter;
/* 146 */     this.cache = new FilterCache(deletesMode)
/*     */     {
/*     */       public DocIdSet mergeDeletes(final IndexReader r, DocIdSet docIdSet) {
/* 149 */         return new FilteredDocIdSet(docIdSet)
/*     */         {
/*     */           protected boolean match(int docID) {
/* 152 */             return !r.isDeleted(docID);
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected DocIdSet docIdSetToCache(DocIdSet docIdSet, IndexReader reader)
/*     */     throws IOException
/*     */   {
/* 166 */     if (docIdSet == null)
/*     */     {
/* 168 */       return DocIdSet.EMPTY_DOCIDSET;
/* 169 */     }if (docIdSet.isCacheable()) {
/* 170 */       return docIdSet;
/*     */     }
/* 172 */     DocIdSetIterator it = docIdSet.iterator();
/*     */ 
/* 176 */     return it == null ? DocIdSet.EMPTY_DOCIDSET : new OpenBitSetDISI(it, reader.maxDoc());
/*     */   }
/*     */ 
/*     */   public DocIdSet getDocIdSet(IndexReader reader)
/*     */     throws IOException
/*     */   {
/* 186 */     Object coreKey = reader.getFieldCacheKey();
/* 187 */     Object delCoreKey = reader.hasDeletions() ? reader.getDeletesCacheKey() : coreKey;
/*     */ 
/* 189 */     DocIdSet docIdSet = (DocIdSet)this.cache.get(reader, coreKey, delCoreKey);
/* 190 */     if (docIdSet != null) {
/* 191 */       this.hitCount += 1;
/* 192 */       return docIdSet;
/*     */     }
/*     */ 
/* 195 */     this.missCount += 1;
/*     */ 
/* 198 */     docIdSet = docIdSetToCache(this.filter.getDocIdSet(reader), reader);
/*     */ 
/* 200 */     if (docIdSet != null) {
/* 201 */       this.cache.put(coreKey, delCoreKey, docIdSet);
/*     */     }
/*     */ 
/* 204 */     return docIdSet;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 209 */     return "CachingWrapperFilter(" + this.filter + ")";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 214 */     if (!(o instanceof CachingWrapperFilter)) return false;
/* 215 */     return this.filter.equals(((CachingWrapperFilter)o).filter);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 220 */     return this.filter.hashCode() ^ 0x1117BF25;
/*     */   }
/*     */ 
/*     */   static abstract class FilterCache<T>
/*     */     implements Serializable
/*     */   {
/*     */     transient Map<Object, T> cache;
/*     */     private final CachingWrapperFilter.DeletesMode deletesMode;
/*     */ 
/*     */     public FilterCache(CachingWrapperFilter.DeletesMode deletesMode)
/*     */     {
/*  72 */       this.deletesMode = deletesMode;
/*     */     }
/*     */ 
/*     */     public synchronized T get(IndexReader reader, Object coreKey, Object delCoreKey)
/*     */       throws IOException
/*     */     {
/*  78 */       if (this.cache == null)
/*  79 */         this.cache = new WeakHashMap();
/*     */       Object value;
/*     */       Object value;
/*  82 */       if (this.deletesMode == CachingWrapperFilter.DeletesMode.IGNORE)
/*     */       {
/*  84 */         value = this.cache.get(coreKey);
/*     */       }
/*     */       else
/*     */       {
/*     */         Object value;
/*  85 */         if (this.deletesMode == CachingWrapperFilter.DeletesMode.RECACHE)
/*     */         {
/*  87 */           value = this.cache.get(delCoreKey);
/*     */         }
/*     */         else {
/*  90 */           assert (this.deletesMode == CachingWrapperFilter.DeletesMode.DYNAMIC);
/*     */ 
/*  93 */           value = this.cache.get(delCoreKey);
/*     */ 
/*  95 */           if (value == null)
/*     */           {
/*  98 */             value = this.cache.get(coreKey);
/*  99 */             if ((value != null) && (reader.hasDeletions())) {
/* 100 */               value = mergeDeletes(reader, value);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 105 */       return value;
/*     */     }
/*     */ 
/*     */     protected abstract T mergeDeletes(IndexReader paramIndexReader, T paramT);
/*     */ 
/*     */     public synchronized void put(Object coreKey, Object delCoreKey, T value) {
/* 111 */       if (this.deletesMode == CachingWrapperFilter.DeletesMode.IGNORE) {
/* 112 */         this.cache.put(coreKey, value);
/* 113 */       } else if (this.deletesMode == CachingWrapperFilter.DeletesMode.RECACHE) {
/* 114 */         this.cache.put(delCoreKey, value);
/*     */       } else {
/* 116 */         this.cache.put(coreKey, value);
/* 117 */         this.cache.put(delCoreKey, value);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum DeletesMode
/*     */   {
/*  56 */     IGNORE, RECACHE, DYNAMIC;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.CachingWrapperFilter
 * JD-Core Version:    0.6.2
 */