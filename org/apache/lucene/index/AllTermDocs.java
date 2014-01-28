/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import org.apache.lucene.util.BitVector;
/*    */ 
/*    */ class AllTermDocs extends AbstractAllTermDocs
/*    */ {
/*    */   protected BitVector deletedDocs;
/*    */ 
/*    */   protected AllTermDocs(SegmentReader parent)
/*    */   {
/* 27 */     super(parent.maxDoc());
/* 28 */     synchronized (parent) {
/* 29 */       this.deletedDocs = parent.deletedDocs;
/*    */     }
/*    */   }
/*    */ 
/*    */   public boolean isDeleted(int doc) {
/* 34 */     return (this.deletedDocs != null) && (this.deletedDocs.get(doc));
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.AllTermDocs
 * JD-Core Version:    0.6.2
 */