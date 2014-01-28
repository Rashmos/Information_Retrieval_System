/*    */ package org.apache.commons.io;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class FileExistsException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public FileExistsException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FileExistsException(String message)
/*    */   {
/* 48 */     super(message);
/*    */   }
/*    */ 
/*    */   public FileExistsException(File file)
/*    */   {
/* 57 */     super("File " + file + " exists");
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.FileExistsException
 * JD-Core Version:    0.6.2
 */