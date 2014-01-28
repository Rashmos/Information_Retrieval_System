/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import org.apache.lucene.store.BufferedIndexInput;
/*     */ import org.apache.lucene.store.Directory;
/*     */ import org.apache.lucene.store.IndexInput;
/*     */ import org.apache.lucene.store.IndexOutput;
/*     */ import org.apache.lucene.store.Lock;
/*     */ 
/*     */ class CompoundFileReader extends Directory
/*     */ {
/*     */   private int readBufferSize;
/*     */   private Directory directory;
/*     */   private String fileName;
/*     */   private IndexInput stream;
/*  50 */   private HashMap<String, FileEntry> entries = new HashMap();
/*     */ 
/*     */   public CompoundFileReader(Directory dir, String name) throws IOException
/*     */   {
/*  54 */     this(dir, name, 1024);
/*     */   }
/*     */ 
/*     */   public CompoundFileReader(Directory dir, String name, int readBufferSize)
/*     */     throws IOException
/*     */   {
/*  60 */     this.directory = dir;
/*  61 */     this.fileName = name;
/*  62 */     this.readBufferSize = readBufferSize;
/*     */ 
/*  64 */     boolean success = false;
/*     */     try
/*     */     {
/*  67 */       this.stream = dir.openInput(name, readBufferSize);
/*     */ 
/*  70 */       int count = this.stream.readVInt();
/*  71 */       FileEntry entry = null;
/*  72 */       for (int i = 0; i < count; i++) {
/*  73 */         long offset = this.stream.readLong();
/*  74 */         String id = this.stream.readString();
/*     */ 
/*  76 */         if (entry != null)
/*     */         {
/*  78 */           entry.length = (offset - entry.offset);
/*     */         }
/*     */ 
/*  81 */         entry = new FileEntry(null);
/*  82 */         entry.offset = offset;
/*  83 */         this.entries.put(id, entry);
/*     */       }
/*     */ 
/*  87 */       if (entry != null) {
/*  88 */         entry.length = (this.stream.length() - entry.offset);
/*     */       }
/*     */ 
/*  91 */       success = true;
/*     */     }
/*     */     finally {
/*  94 */       if ((!success) && (this.stream != null))
/*     */         try {
/*  96 */           this.stream.close();
/*     */         } catch (IOException e) {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Directory getDirectory() {
/* 103 */     return this.directory;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 107 */     return this.fileName;
/*     */   }
/*     */ 
/*     */   public synchronized void close() throws IOException
/*     */   {
/* 112 */     if (this.stream == null) {
/* 113 */       throw new IOException("Already closed");
/*     */     }
/* 115 */     this.entries.clear();
/* 116 */     this.stream.close();
/* 117 */     this.stream = null;
/*     */   }
/*     */ 
/*     */   public synchronized IndexInput openInput(String id)
/*     */     throws IOException
/*     */   {
/* 125 */     return openInput(id, this.readBufferSize);
/*     */   }
/*     */ 
/*     */   public synchronized IndexInput openInput(String id, int readBufferSize)
/*     */     throws IOException
/*     */   {
/* 132 */     if (this.stream == null) {
/* 133 */       throw new IOException("Stream closed");
/*     */     }
/* 135 */     FileEntry entry = (FileEntry)this.entries.get(id);
/* 136 */     if (entry == null) {
/* 137 */       throw new IOException("No sub-file with id " + id + " found");
/*     */     }
/* 139 */     return new CSIndexInput(this.stream, entry.offset, entry.length, readBufferSize);
/*     */   }
/*     */ 
/*     */   public String[] listAll()
/*     */   {
/* 145 */     String[] res = new String[this.entries.size()];
/* 146 */     return (String[])this.entries.keySet().toArray(res);
/*     */   }
/*     */ 
/*     */   public boolean fileExists(String name)
/*     */   {
/* 152 */     return this.entries.containsKey(name);
/*     */   }
/*     */ 
/*     */   public long fileModified(String name)
/*     */     throws IOException
/*     */   {
/* 158 */     return this.directory.fileModified(this.fileName);
/*     */   }
/*     */ 
/*     */   public void touchFile(String name)
/*     */     throws IOException
/*     */   {
/* 164 */     this.directory.touchFile(this.fileName);
/*     */   }
/*     */ 
/*     */   public void deleteFile(String name)
/*     */   {
/* 172 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void renameFile(String from, String to)
/*     */   {
/* 179 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public long fileLength(String name)
/*     */     throws IOException
/*     */   {
/* 188 */     FileEntry e = (FileEntry)this.entries.get(name);
/* 189 */     if (e == null)
/* 190 */       throw new IOException("File " + name + " does not exist");
/* 191 */     return e.length;
/*     */   }
/*     */ 
/*     */   public IndexOutput createOutput(String name)
/*     */   {
/* 199 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Lock makeLock(String name)
/*     */   {
/* 207 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   static final class CSIndexInput extends BufferedIndexInput
/*     */   {
/*     */     IndexInput base;
/*     */     long fileOffset;
/*     */     long length;
/*     */ 
/*     */     CSIndexInput(IndexInput base, long fileOffset, long length)
/*     */     {
/* 223 */       this(base, fileOffset, length, 1024);
/*     */     }
/*     */ 
/*     */     CSIndexInput(IndexInput base, long fileOffset, long length, int readBufferSize)
/*     */     {
/* 228 */       super();
/* 229 */       this.base = ((IndexInput)base.clone());
/* 230 */       this.fileOffset = fileOffset;
/* 231 */       this.length = length;
/*     */     }
/*     */ 
/*     */     public Object clone()
/*     */     {
/* 236 */       CSIndexInput clone = (CSIndexInput)super.clone();
/* 237 */       clone.base = ((IndexInput)this.base.clone());
/* 238 */       clone.fileOffset = this.fileOffset;
/* 239 */       clone.length = this.length;
/* 240 */       return clone;
/*     */     }
/*     */ 
/*     */     protected void readInternal(byte[] b, int offset, int len)
/*     */       throws IOException
/*     */     {
/* 253 */       long start = getFilePointer();
/* 254 */       if (start + len > this.length)
/* 255 */         throw new IOException("read past EOF");
/* 256 */       this.base.seek(this.fileOffset + start);
/* 257 */       this.base.readBytes(b, offset, len, false);
/*     */     }
/*     */ 
/*     */     protected void seekInternal(long pos)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 270 */       this.base.close();
/*     */     }
/*     */ 
/*     */     public long length()
/*     */     {
/* 275 */       return this.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class FileEntry
/*     */   {
/*     */     long offset;
/*     */     long length;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.CompoundFileReader
 * JD-Core Version:    0.6.2
 */