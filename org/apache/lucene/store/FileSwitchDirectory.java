/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class FileSwitchDirectory extends Directory
/*     */ {
/*     */   private final Directory secondaryDir;
/*     */   private final Directory primaryDir;
/*     */   private final Set<String> primaryExtensions;
/*     */   private boolean doClose;
/*     */ 
/*     */   public FileSwitchDirectory(Set<String> primaryExtensions, Directory primaryDir, Directory secondaryDir, boolean doClose)
/*     */   {
/*  45 */     this.primaryExtensions = primaryExtensions;
/*  46 */     this.primaryDir = primaryDir;
/*  47 */     this.secondaryDir = secondaryDir;
/*  48 */     this.doClose = doClose;
/*  49 */     this.lockFactory = primaryDir.getLockFactory();
/*     */   }
/*     */ 
/*     */   public Directory getPrimaryDir()
/*     */   {
/*  54 */     return this.primaryDir;
/*     */   }
/*     */ 
/*     */   public Directory getSecondaryDir()
/*     */   {
/*  59 */     return this.secondaryDir;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/*  64 */     if (this.doClose) {
/*     */       try {
/*  66 */         this.secondaryDir.close();
/*     */       } finally {
/*  68 */         this.primaryDir.close();
/*     */       }
/*  70 */       this.doClose = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] listAll() throws IOException
/*     */   {
/*  76 */     String[] primaryFiles = this.primaryDir.listAll();
/*  77 */     String[] secondaryFiles = this.secondaryDir.listAll();
/*  78 */     String[] files = new String[primaryFiles.length + secondaryFiles.length];
/*  79 */     System.arraycopy(primaryFiles, 0, files, 0, primaryFiles.length);
/*  80 */     System.arraycopy(secondaryFiles, 0, files, primaryFiles.length, secondaryFiles.length);
/*  81 */     return files;
/*     */   }
/*     */ 
/*     */   public static String getExtension(String name)
/*     */   {
/*  86 */     int i = name.lastIndexOf('.');
/*  87 */     if (i == -1) {
/*  88 */       return "";
/*     */     }
/*  90 */     return name.substring(i + 1, name.length());
/*     */   }
/*     */ 
/*     */   private Directory getDirectory(String name) {
/*  94 */     String ext = getExtension(name);
/*  95 */     if (this.primaryExtensions.contains(ext)) {
/*  96 */       return this.primaryDir;
/*     */     }
/*  98 */     return this.secondaryDir;
/*     */   }
/*     */ 
/*     */   public boolean fileExists(String name)
/*     */     throws IOException
/*     */   {
/* 104 */     return getDirectory(name).fileExists(name);
/*     */   }
/*     */ 
/*     */   public long fileModified(String name) throws IOException
/*     */   {
/* 109 */     return getDirectory(name).fileModified(name);
/*     */   }
/*     */ 
/*     */   public void touchFile(String name) throws IOException
/*     */   {
/* 114 */     getDirectory(name).touchFile(name);
/*     */   }
/*     */ 
/*     */   public void deleteFile(String name) throws IOException
/*     */   {
/* 119 */     getDirectory(name).deleteFile(name);
/*     */   }
/*     */ 
/*     */   public long fileLength(String name) throws IOException
/*     */   {
/* 124 */     return getDirectory(name).fileLength(name);
/*     */   }
/*     */ 
/*     */   public IndexOutput createOutput(String name) throws IOException
/*     */   {
/* 129 */     return getDirectory(name).createOutput(name);
/*     */   }
/*     */ 
/*     */   public void sync(String name) throws IOException
/*     */   {
/* 134 */     getDirectory(name).sync(name);
/*     */   }
/*     */ 
/*     */   public IndexInput openInput(String name) throws IOException
/*     */   {
/* 139 */     return getDirectory(name).openInput(name);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.FileSwitchDirectory
 * JD-Core Version:    0.6.2
 */