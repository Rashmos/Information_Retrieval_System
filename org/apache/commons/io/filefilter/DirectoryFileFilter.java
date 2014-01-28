/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class DirectoryFileFilter extends AbstractFileFilter
/*    */   implements Serializable
/*    */ {
/* 47 */   public static final IOFileFilter DIRECTORY = new DirectoryFileFilter();
/*    */ 
/* 54 */   public static final IOFileFilter INSTANCE = DIRECTORY;
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 70 */     return file.isDirectory();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.filefilter.DirectoryFileFilter
 * JD-Core Version:    0.6.2
 */