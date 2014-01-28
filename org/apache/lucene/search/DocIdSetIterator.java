/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class DocIdSetIterator
/*    */ {
/* 31 */   private int doc = -1;
/*    */   public static final int NO_MORE_DOCS = 2147483647;
/*    */ 
/*    */   public abstract int docID();
/*    */ 
/*    */   public abstract int nextDoc()
/*    */     throws IOException;
/*    */ 
/*    */   public abstract int advance(int paramInt)
/*    */     throws IOException;
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.DocIdSetIterator
 * JD-Core Version:    0.6.2
 */