/*    */ package org.apache.lucene.analysis;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class BaseCharFilter extends CharFilter
/*    */ {
/*    */   private List<OffCorrectMap> pcmList;
/*    */ 
/*    */   public BaseCharFilter(CharStream in)
/*    */   {
/* 39 */     super(in);
/*    */   }
/*    */ 
/*    */   protected int correct(int currentOff)
/*    */   {
/* 49 */     if ((this.pcmList == null) || (this.pcmList.isEmpty())) {
/* 50 */       return currentOff;
/*    */     }
/* 52 */     for (int i = this.pcmList.size() - 1; i >= 0; i--) {
/* 53 */       if (currentOff >= ((OffCorrectMap)this.pcmList.get(i)).off) {
/* 54 */         return currentOff + ((OffCorrectMap)this.pcmList.get(i)).cumulativeDiff;
/*    */       }
/*    */     }
/* 57 */     return currentOff;
/*    */   }
/*    */ 
/*    */   protected int getLastCumulativeDiff() {
/* 61 */     return (this.pcmList == null) || (this.pcmList.isEmpty()) ? 0 : ((OffCorrectMap)this.pcmList.get(this.pcmList.size() - 1)).cumulativeDiff;
/*    */   }
/*    */ 
/*    */   protected void addOffCorrectMap(int off, int cumulativeDiff)
/*    */   {
/* 66 */     if (this.pcmList == null) {
/* 67 */       this.pcmList = new ArrayList();
/*    */     }
/* 69 */     this.pcmList.add(new OffCorrectMap(off, cumulativeDiff));
/*    */   }
/*    */ 
/*    */   static class OffCorrectMap {
/*    */     int off;
/*    */     int cumulativeDiff;
/*    */ 
/*    */     OffCorrectMap(int off, int cumulativeDiff) {
/* 78 */       this.off = off;
/* 79 */       this.cumulativeDiff = cumulativeDiff;
/*    */     }
/*    */ 
/*    */     public String toString()
/*    */     {
/* 84 */       StringBuilder sb = new StringBuilder();
/* 85 */       sb.append('(');
/* 86 */       sb.append(this.off);
/* 87 */       sb.append(',');
/* 88 */       sb.append(this.cumulativeDiff);
/* 89 */       sb.append(')');
/* 90 */       return sb.toString();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.BaseCharFilter
 * JD-Core Version:    0.6.2
 */