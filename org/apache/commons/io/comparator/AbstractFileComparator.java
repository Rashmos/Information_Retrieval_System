/*    */ package org.apache.commons.io.comparator;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ 
/*    */ abstract class AbstractFileComparator
/*    */   implements Comparator<File>
/*    */ {
/*    */   public File[] sort(File[] files)
/*    */   {
/* 44 */     if (files != null) {
/* 45 */       Arrays.sort(files, this);
/*    */     }
/* 47 */     return files;
/*    */   }
/*    */ 
/*    */   public List<File> sort(List<File> files)
/*    */   {
/* 61 */     if (files != null) {
/* 62 */       Collections.sort(files, this);
/*    */     }
/* 64 */     return files;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 74 */     return getClass().getSimpleName();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.comparator.AbstractFileComparator
 * JD-Core Version:    0.6.2
 */