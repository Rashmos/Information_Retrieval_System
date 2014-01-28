/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ public class TopFieldDocs extends TopDocs
/*    */ {
/*    */   public SortField[] fields;
/*    */ 
/*    */   public TopFieldDocs(int totalHits, ScoreDoc[] scoreDocs, SortField[] fields, float maxScore)
/*    */   {
/* 37 */     super(totalHits, scoreDocs, maxScore);
/* 38 */     this.fields = fields;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TopFieldDocs
 * JD-Core Version:    0.6.2
 */