/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class TrueFileFilter
/*    */   implements IOFileFilter, Serializable
/*    */ {
/* 35 */   public static final IOFileFilter TRUE = new TrueFileFilter();
/*    */ 
/* 42 */   public static final IOFileFilter INSTANCE = TRUE;
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 57 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean accept(File dir, String name)
/*    */   {
/* 68 */     return true;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.filefilter.TrueFileFilter
 * JD-Core Version:    0.6.2
 */