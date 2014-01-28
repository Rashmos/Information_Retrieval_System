/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ public class ToStringUtils
/*    */ {
/*    */   public static String boost(float boost)
/*    */   {
/* 26 */     if (boost != 1.0F)
/* 27 */       return "^" + Float.toString(boost);
/* 28 */     return "";
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.ToStringUtils
 * JD-Core Version:    0.6.2
 */