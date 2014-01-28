/*     */ package com.aliasi.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ 
/*     */ public class FileExtensionFilter
/*     */   implements FileFilter
/*     */ {
/*     */   private final String[] mExtensions;
/*     */   private final boolean mAcceptDirectories;
/*     */ 
/*     */   public FileExtensionFilter(String[] extensions, boolean acceptDirectories)
/*     */   {
/*  53 */     this.mAcceptDirectories = acceptDirectories;
/*  54 */     this.mExtensions = new String[extensions.length];
/*  55 */     for (int i = 0; i < extensions.length; i++)
/*  56 */       this.mExtensions[i] = extensions[i];
/*     */   }
/*     */ 
/*     */   public FileExtensionFilter(String extension, boolean acceptDirectories)
/*     */   {
/*  69 */     this(new String[] { extension }, acceptDirectories);
/*     */   }
/*     */ 
/*     */   public FileExtensionFilter(String extension)
/*     */   {
/*  81 */     this(extension, true);
/*     */   }
/*     */ 
/*     */   public FileExtensionFilter(String[] extensions)
/*     */   {
/*  91 */     this(extensions, true);
/*     */   }
/*     */ 
/*     */   protected final boolean accept(String fileName)
/*     */   {
/* 102 */     for (int i = 0; i < this.mExtensions.length; i++)
/* 103 */       if (fileName.endsWith(this.mExtensions[i])) return true;
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean accept(File pathName)
/*     */   {
/* 115 */     return ((this.mAcceptDirectories) && (pathName.isDirectory())) || (accept(pathName.getName()));
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.io.FileExtensionFilter
 * JD-Core Version:    0.6.2
 */