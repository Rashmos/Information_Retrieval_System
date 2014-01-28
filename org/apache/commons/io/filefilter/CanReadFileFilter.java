/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class CanReadFileFilter extends AbstractFileFilter
/*    */   implements Serializable
/*    */ {
/* 66 */   public static final IOFileFilter CAN_READ = new CanReadFileFilter();
/*    */ 
/* 69 */   public static final IOFileFilter CANNOT_READ = new NotFileFilter(CAN_READ);
/*    */ 
/* 72 */   public static final IOFileFilter READ_ONLY = new AndFileFilter(CAN_READ, CanWriteFileFilter.CANNOT_WRITE);
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 90 */     return file.canRead();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.filefilter.CanReadFileFilter
 * JD-Core Version:    0.6.2
 */