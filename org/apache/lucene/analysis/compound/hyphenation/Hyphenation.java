/*    */ package org.apache.lucene.analysis.compound.hyphenation;
/*    */ 
/*    */ public class Hyphenation
/*    */ {
/*    */   private int[] hyphenPoints;
/*    */ 
/*    */   Hyphenation(int[] points)
/*    */   {
/* 33 */     this.hyphenPoints = points;
/*    */   }
/*    */ 
/*    */   public int length()
/*    */   {
/* 40 */     return this.hyphenPoints.length;
/*    */   }
/*    */ 
/*    */   public int[] getHyphenationPoints()
/*    */   {
/* 47 */     return this.hyphenPoints;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.Hyphenation
 * JD-Core Version:    0.6.2
 */