/*    */ package org.apache.lucene.search;
/*    */ 
/*    */ public class FieldDoc extends ScoreDoc
/*    */ {
/*    */   public Comparable[] fields;
/*    */ 
/*    */   public FieldDoc(int doc, float score)
/*    */   {
/* 52 */     super(doc, score);
/*    */   }
/*    */ 
/*    */   public FieldDoc(int doc, float score, Comparable[] fields)
/*    */   {
/* 57 */     super(doc, score);
/* 58 */     this.fields = fields;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 66 */     StringBuilder sb = new StringBuilder(super.toString());
/* 67 */     sb.append("[");
/* 68 */     for (int i = 0; i < this.fields.length; i++) {
/* 69 */       sb.append(this.fields[i]).append(", ");
/*    */     }
/* 71 */     sb.setLength(sb.length() - 2);
/* 72 */     sb.append("]");
/* 73 */     return sb.toString();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FieldDoc
 * JD-Core Version:    0.6.2
 */