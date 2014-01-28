/*    */ package org.apache.commons.io.comparator;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class LastModifiedFileComparator extends AbstractFileComparator
/*    */   implements Serializable
/*    */ {
/* 51 */   public static final Comparator<File> LASTMODIFIED_COMPARATOR = new LastModifiedFileComparator();
/*    */ 
/* 54 */   public static final Comparator<File> LASTMODIFIED_REVERSE = new ReverseComparator(LASTMODIFIED_COMPARATOR);
/*    */ 
/*    */   public int compare(File file1, File file2)
/*    */   {
/* 68 */     long result = file1.lastModified() - file2.lastModified();
/* 69 */     if (result < 0L)
/* 70 */       return -1;
/* 71 */     if (result > 0L) {
/* 72 */       return 1;
/*    */     }
/* 74 */     return 0;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.comparator.LastModifiedFileComparator
 * JD-Core Version:    0.6.2
 */