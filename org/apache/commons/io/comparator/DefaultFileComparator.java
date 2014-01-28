/*    */ package org.apache.commons.io.comparator;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class DefaultFileComparator extends AbstractFileComparator
/*    */   implements Serializable
/*    */ {
/* 50 */   public static final Comparator<File> DEFAULT_COMPARATOR = new DefaultFileComparator();
/*    */ 
/* 53 */   public static final Comparator<File> DEFAULT_REVERSE = new ReverseComparator(DEFAULT_COMPARATOR);
/*    */ 
/*    */   public int compare(File file1, File file2)
/*    */   {
/* 64 */     return file1.compareTo(file2);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.comparator.DefaultFileComparator
 * JD-Core Version:    0.6.2
 */