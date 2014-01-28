/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
/*    */ import java.util.HashSet;
/*    */ 
/*    */ public class IndexFileNameFilter
/*    */   implements FilenameFilter
/*    */ {
/* 29 */   private static IndexFileNameFilter singleton = new IndexFileNameFilter();
/*    */   private HashSet<String> extensions;
/*    */   private HashSet<String> extensionsInCFS;
/*    */ 
/*    */   private IndexFileNameFilter()
/*    */   {
/* 35 */     this.extensions = new HashSet();
/* 36 */     for (int i = 0; i < IndexFileNames.INDEX_EXTENSIONS.length; i++) {
/* 37 */       this.extensions.add(IndexFileNames.INDEX_EXTENSIONS[i]);
/*    */     }
/* 39 */     this.extensionsInCFS = new HashSet();
/* 40 */     for (int i = 0; i < IndexFileNames.INDEX_EXTENSIONS_IN_COMPOUND_FILE.length; i++)
/* 41 */       this.extensionsInCFS.add(IndexFileNames.INDEX_EXTENSIONS_IN_COMPOUND_FILE[i]);
/*    */   }
/*    */ 
/*    */   public boolean accept(File dir, String name)
/*    */   {
/* 49 */     int i = name.lastIndexOf('.');
/* 50 */     if (i != -1) {
/* 51 */       String extension = name.substring(1 + i);
/* 52 */       if (this.extensions.contains(extension))
/* 53 */         return true;
/* 54 */       if ((extension.startsWith("f")) && (extension.matches("f\\d+")))
/*    */       {
/* 56 */         return true;
/* 57 */       }if ((extension.startsWith("s")) && (extension.matches("s\\d+")))
/*    */       {
/* 59 */         return true;
/*    */       }
/*    */     } else {
/* 62 */       if (name.equals("deletable")) return true;
/* 63 */       if (name.startsWith("segments")) return true;
/*    */     }
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean isCFSFile(String name)
/*    */   {
/* 75 */     int i = name.lastIndexOf('.');
/* 76 */     if (i != -1) {
/* 77 */       String extension = name.substring(1 + i);
/* 78 */       if (this.extensionsInCFS.contains(extension)) {
/* 79 */         return true;
/*    */       }
/* 81 */       if ((extension.startsWith("f")) && (extension.matches("f\\d+")))
/*    */       {
/* 83 */         return true;
/*    */       }
/*    */     }
/* 86 */     return false;
/*    */   }
/*    */ 
/*    */   public static IndexFileNameFilter getFilter() {
/* 90 */     return singleton;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.IndexFileNameFilter
 * JD-Core Version:    0.6.2
 */