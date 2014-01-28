/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexInput;
/*     */ import org.apache.lucene.store.IndexOutput;
/*     */ 
/*     */ final class CompoundFileWriter
/*     */ {
/*     */   private Directory directory;
/*     */   private String fileName;
/*     */   private HashSet<String> ids;
/*     */   private LinkedList<FileEntry> entries;
/*  67 */   private boolean merged = false;
/*     */   private SegmentMerger.CheckAbort checkAbort;
/*     */ 
/*     */   public CompoundFileWriter(Directory dir, String name)
/*     */   {
/*  75 */     this(dir, name, null);
/*     */   }
/*     */ 
/*     */   CompoundFileWriter(Directory dir, String name, SegmentMerger.CheckAbort checkAbort) {
/*  79 */     if (dir == null)
/*  80 */       throw new NullPointerException("directory cannot be null");
/*  81 */     if (name == null)
/*  82 */       throw new NullPointerException("name cannot be null");
/*  83 */     this.checkAbort = checkAbort;
/*  84 */     this.directory = dir;
/*  85 */     this.fileName = name;
/*  86 */     this.ids = new HashSet();
/*  87 */     this.entries = new LinkedList();
/*     */   }
/*     */ 
/*     */   public Directory getDirectory()
/*     */   {
/*  92 */     return this.directory;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  97 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   public void addFile(String file)
/*     */   {
/* 109 */     if (this.merged) {
/* 110 */       throw new IllegalStateException("Can't add extensions after merge has been called");
/*     */     }
/*     */ 
/* 113 */     if (file == null) {
/* 114 */       throw new NullPointerException("file cannot be null");
/*     */     }
/*     */ 
/* 117 */     if (!this.ids.add(file)) {
/* 118 */       throw new IllegalArgumentException("File " + file + " already added");
/*     */     }
/*     */ 
/* 121 */     FileEntry entry = new FileEntry(null);
/* 122 */     entry.file = file;
/* 123 */     this.entries.add(entry);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 134 */     if (this.merged) {
/* 135 */       throw new IllegalStateException("Merge already performed");
/*     */     }
/*     */ 
/* 138 */     if (this.entries.isEmpty()) {
/* 139 */       throw new IllegalStateException("No entries to merge have been defined");
/*     */     }
/*     */ 
/* 142 */     this.merged = true;
/*     */ 
/* 145 */     IndexOutput os = null;
/*     */     try {
/* 147 */       os = this.directory.createOutput(this.fileName);
/*     */ 
/* 150 */       os.writeVInt(this.entries.size());
/*     */ 
/* 155 */       long totalSize = 0L;
/* 156 */       for (FileEntry fe : this.entries) {
/* 157 */         fe.directoryOffset = os.getFilePointer();
/* 158 */         os.writeLong(0L);
/* 159 */         os.writeString(fe.file);
/* 160 */         totalSize += this.directory.fileLength(fe.file);
/*     */       }
/*     */ 
/* 169 */       long finalLength = totalSize + os.getFilePointer();
/* 170 */       os.setLength(finalLength);
/*     */ 
/* 174 */       byte[] buffer = new byte[16384];
/* 175 */       for (FileEntry fe : this.entries) {
/* 176 */         fe.dataOffset = os.getFilePointer();
/* 177 */         copyFile(fe, os, buffer);
/*     */       }
/*     */ 
/* 181 */       for (FileEntry fe : this.entries) {
/* 182 */         os.seek(fe.directoryOffset);
/* 183 */         os.writeLong(fe.dataOffset);
/*     */       }
/*     */ 
/* 186 */       assert (finalLength == os.length());
/*     */ 
/* 192 */       IndexOutput tmp = os;
/* 193 */       os = null;
/* 194 */       tmp.close();
/*     */     }
/*     */     finally {
/* 197 */       if (os != null) try { os.close(); }
/*     */         catch (IOException e)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void copyFile(FileEntry source, IndexOutput os, byte[] buffer)
/*     */     throws IOException
/*     */   {
/* 208 */     IndexInput is = null;
/*     */     try {
/* 210 */       long startPtr = os.getFilePointer();
/*     */ 
/* 212 */       is = this.directory.openInput(source.file);
/* 213 */       long length = is.length();
/* 214 */       long remainder = length;
/* 215 */       int chunk = buffer.length;
/*     */ 
/* 217 */       while (remainder > 0L) {
/* 218 */         int len = (int)Math.min(chunk, remainder);
/* 219 */         is.readBytes(buffer, 0, len, false);
/* 220 */         os.writeBytes(buffer, len);
/* 221 */         remainder -= len;
/* 222 */         if (this.checkAbort != null)
/*     */         {
/* 225 */           this.checkAbort.work(80.0D);
/*     */         }
/*     */       }
/*     */ 
/* 229 */       if (remainder != 0L) {
/* 230 */         throw new IOException("Non-zero remainder length after copying: " + remainder + " (id: " + source.file + ", length: " + length + ", buffer size: " + chunk + ")");
/*     */       }
/*     */ 
/* 236 */       long endPtr = os.getFilePointer();
/* 237 */       long diff = endPtr - startPtr;
/* 238 */       if (diff != length) {
/* 239 */         throw new IOException("Difference in the output file offsets " + diff + " does not match the original file length " + length);
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 244 */       if (is != null) is.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class FileEntry
/*     */   {
/*     */     String file;
/*     */     long directoryOffset;
/*     */     long dataOffset;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.CompoundFileWriter
 * JD-Core Version:    0.6.2
 */