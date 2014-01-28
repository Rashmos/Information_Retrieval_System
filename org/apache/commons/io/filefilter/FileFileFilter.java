/*    */ package org.apache.commons.io.filefilter;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class FileFileFilter extends AbstractFileFilter
/*    */   implements Serializable
/*    */ {
/* 43 */   public static final IOFileFilter FILE = new FileFileFilter();
/*    */ 
/*    */   public boolean accept(File file)
/*    */   {
/* 59 */     return file.isFile();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.filefilter.FileFileFilter
 * JD-Core Version:    0.6.2
 */