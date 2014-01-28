/*    */ package org.apache.commons.io.comparator;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ class ReverseComparator extends AbstractFileComparator
/*    */   implements Serializable
/*    */ {
/*    */   private final Comparator<File> delegate;
/*    */ 
/*    */   public ReverseComparator(Comparator<File> delegate)
/*    */   {
/* 40 */     if (delegate == null) {
/* 41 */       throw new IllegalArgumentException("Delegate comparator is missing");
/*    */     }
/* 43 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */   public int compare(File file1, File file2)
/*    */   {
/* 55 */     return this.delegate.compare(file2, file1);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 65 */     return super.toString() + "[" + this.delegate.toString() + "]";
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.comparator.ReverseComparator
 * JD-Core Version:    0.6.2
 */