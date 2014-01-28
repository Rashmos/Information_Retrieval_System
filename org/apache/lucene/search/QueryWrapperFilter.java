/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.index.IndexReader;
/*    */ 
/*    */ public class QueryWrapperFilter extends Filter
/*    */ {
/*    */   private Query query;
/*    */ 
/*    */   public QueryWrapperFilter(Query query)
/*    */   {
/* 44 */     this.query = query;
/*    */   }
/*    */ 
/*    */   public DocIdSet getDocIdSet(final IndexReader reader) throws IOException
/*    */   {
/* 49 */     final Weight weight = this.query.weight(new IndexSearcher(reader));
/* 50 */     return new DocIdSet()
/*    */     {
/*    */       public DocIdSetIterator iterator() throws IOException {
/* 53 */         return weight.scorer(reader, true, false);
/*    */       }
/*    */       public boolean isCacheable() {
/* 56 */         return false;
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 62 */     return "QueryWrapperFilter(" + this.query + ")";
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 67 */     if (!(o instanceof QueryWrapperFilter))
/* 68 */       return false;
/* 69 */     return this.query.equals(((QueryWrapperFilter)o).query);
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 74 */     return this.query.hashCode() ^ 0x923F64B9;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.QueryWrapperFilter
 * JD-Core Version:    0.6.2
 */