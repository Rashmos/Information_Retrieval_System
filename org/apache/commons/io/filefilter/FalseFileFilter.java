/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class FalseFileFilter
/*    */   implements IOFileFilter, Serializable
/*    */ {
/* 36 */   public static final IOFileFilter FALSE = new FalseFileFilter();
/*    */ 
/* 43 */   public static final IOFileFilter INSTANCE = FALSE;
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean accept(File dir, String name)
/*    */   {
/* 69 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.filefilter.FalseFileFilter
 * JD-Core Version:    0.6.2
 */