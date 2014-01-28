/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class CanWriteFileFilter extends AbstractFileFilter
/*    */   implements Serializable
/*    */ {
/* 58 */   public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();
/*    */ 
/* 61 */   public static final IOFileFilter CANNOT_WRITE = new NotFileFilter(CAN_WRITE);
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 78 */     return file.canWrite();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.filefilter.CanWriteFileFilter
 * JD-Core Version:    0.6.2
 */