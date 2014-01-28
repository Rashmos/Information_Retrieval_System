/*      */ package org.apache.lucene.index;
/*      */ 
/*      */ public final class IndexWriter$MaxFieldLength
/*      */ {
/*      */   private int limit;
/*      */   private String name;
/* 4898 */   public static final MaxFieldLength UNLIMITED = new MaxFieldLength("UNLIMITED", 2147483647);
/*      */ 
/* 4905 */   public static final MaxFieldLength LIMITED = new MaxFieldLength("LIMITED", 10000);
/*      */ 
/*      */   private IndexWriter$MaxFieldLength(String name, int limit)
/*      */   {
/* 4874 */     this.name = name;
/* 4875 */     this.limit = limit;
/*      */   }
/*      */ 
/*      */   public IndexWriter$MaxFieldLength(int limit)
/*      */   {
/* 4884 */     this("User-specified", limit);
/*      */   }
/*      */ 
/*      */   public int getLimit() {
/* 4888 */     return this.limit;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 4894 */     return this.name + ":" + this.limit;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexWriter.MaxFieldLength
 * JD-Core Version:    0.6.2
 */