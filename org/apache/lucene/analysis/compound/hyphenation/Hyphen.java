/*    */ package org.apache.lucene.analysis.compound.hyphenation;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class Hyphen
/*    */   implements Serializable
/*    */ {
/*    */   public String preBreak;
/*    */   public String noBreak;
/*    */   public String postBreak;
/*    */ 
/*    */   Hyphen(String pre, String no, String post)
/*    */   {
/* 43 */     this.preBreak = pre;
/* 44 */     this.noBreak = no;
/* 45 */     this.postBreak = post;
/*    */   }
/*    */ 
/*    */   Hyphen(String pre) {
/* 49 */     this.preBreak = pre;
/* 50 */     this.noBreak = null;
/* 51 */     this.postBreak = null;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 56 */     if ((this.noBreak == null) && (this.postBreak == null) && (this.preBreak != null) && (this.preBreak.equals("-")))
/*    */     {
/* 58 */       return "-";
/*    */     }
/* 60 */     StringBuilder res = new StringBuilder("{");
/* 61 */     res.append(this.preBreak);
/* 62 */     res.append("}{");
/* 63 */     res.append(this.postBreak);
/* 64 */     res.append("}{");
/* 65 */     res.append(this.noBreak);
/* 66 */     res.append('}');
/* 67 */     return res.toString();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.compound.hyphenation.Hyphen
 * JD-Core Version:    0.6.2
 */