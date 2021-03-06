/*    */ package org.apache.lucene.search.spans;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.index.IndexReader;
/*    */ import org.apache.lucene.search.Query;
/*    */ import org.apache.lucene.search.Searcher;
/*    */ import org.apache.lucene.search.Weight;
/*    */ 
/*    */ public abstract class SpanQuery extends Query
/*    */ {
/*    */   public abstract Spans getSpans(IndexReader paramIndexReader)
/*    */     throws IOException;
/*    */ 
/*    */   public abstract String getField();
/*    */ 
/*    */   public Weight createWeight(Searcher searcher)
/*    */     throws IOException
/*    */   {
/* 38 */     return new SpanWeight(this, searcher);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.spans.SpanQuery
 * JD-Core Version:    0.6.2
 */