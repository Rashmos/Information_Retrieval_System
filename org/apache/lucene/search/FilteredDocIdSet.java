/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class FilteredDocIdSet extends DocIdSet
/*    */ {
/*    */   private final DocIdSet _innerSet;
/*    */ 
/*    */   public FilteredDocIdSet(DocIdSet innerSet)
/*    */   {
/* 49 */     this._innerSet = innerSet;
/*    */   }
/*    */ 
/*    */   public boolean isCacheable()
/*    */   {
/* 55 */     return this._innerSet.isCacheable();
/*    */   }
/*    */ 
/*    */   protected abstract boolean match(int paramInt)
/*    */     throws IOException;
/*    */ 
/*    */   public DocIdSetIterator iterator()
/*    */     throws IOException
/*    */   {
/* 72 */     return new FilteredDocIdSetIterator(this._innerSet.iterator())
/*    */     {
/*    */       protected boolean match(int docid) throws IOException {
/* 75 */         return FilteredDocIdSet.this.match(docid);
/*    */       }
/*    */     };
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FilteredDocIdSet
 * JD-Core Version:    0.6.2
 */