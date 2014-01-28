/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class HiddenFileFilter extends AbstractFileFilter
/*    */   implements Serializable
/*    */ {
/* 54 */   public static final IOFileFilter HIDDEN = new HiddenFileFilter();
/*    */ 
/* 57 */   public static final IOFileFilter VISIBLE = new NotFileFilter(HIDDEN);
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 74 */     return file.isHidden();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.filefilter.HiddenFileFilter
 * JD-Core Version:    0.6.2
 */