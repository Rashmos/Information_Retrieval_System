/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ public class SimpleFSDirectory extends FSDirectory
/*     */ {
/*     */   public SimpleFSDirectory(File path, LockFactory lockFactory)
/*     */     throws IOException
/*     */   {
/*  40 */     super(path, lockFactory);
/*     */   }
/*     */ 
/*     */   public SimpleFSDirectory(File path)
/*     */     throws IOException
/*     */   {
/*  49 */     super(path, null);
/*     */   }
/*     */ 
/*     */   public IndexOutput createOutput(String name)
/*     */     throws IOException
/*     */   {
/*  55 */     initOutput(name);
/*  56 */     return new SimpleFSIndexOutput(new File(this.directory, name));
/*     */   }
/*     */ 
/*     */   public IndexInput openInput(String name, int bufferSize)
/*     */     throws IOException
/*     */   {
/*  62 */     ensureOpen();
/*  63 */     return new SimpleFSIndexInput(new File(this.directory, name), bufferSize, getReadChunkSize());
/*     */   }
/*     */ 
/*     */   protected static class SimpleFSIndexOutput extends BufferedIndexOutput
/*     */   {
/* 173 */     RandomAccessFile file = null;
/*     */     private volatile boolean isOpen;
/*     */ 
/*     */     public SimpleFSIndexOutput(File path)
/*     */       throws IOException
/*     */     {
/* 180 */       this.file = new RandomAccessFile(path, "rw");
/* 181 */       this.isOpen = true;
/*     */     }
/*     */ 
/*     */     public void flushBuffer(byte[] b, int offset, int size)
/*     */       throws IOException
/*     */     {
/* 187 */       this.file.write(b, offset, size);
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 192 */       if (this.isOpen) {
/* 193 */         boolean success = false;
/*     */         try {
/* 195 */           super.close();
/* 196 */           success = true;
/*     */         } finally {
/* 198 */           this.isOpen = false;
/* 199 */           if (!success)
/*     */             try {
/* 201 */               this.file.close();
/*     */             }
/*     */             catch (Throwable t) {
/*     */             }
/*     */           else
/* 206 */             this.file.close();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void seek(long pos)
/*     */       throws IOException
/*     */     {
/* 214 */       super.seek(pos);
/* 215 */       this.file.seek(pos);
/*     */     }
/*     */ 
/*     */     public long length() throws IOException {
/* 219 */       return this.file.length();
/*     */     }
/*     */ 
/*     */     public void setLength(long length) throws IOException {
/* 223 */       this.file.setLength(length);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static class SimpleFSIndexInput extends BufferedIndexInput
/*     */   {
/*     */     protected final Descriptor file;
/*     */     boolean isClone;
/*     */     protected final int chunkSize;
/*     */ 
/*     */     public SimpleFSIndexInput(File path, int bufferSize, int chunkSize)
/*     */       throws IOException
/*     */     {
/*  96 */       super();
/*  97 */       this.file = new Descriptor(path, "r");
/*  98 */       this.chunkSize = chunkSize;
/*     */     }
/*     */ 
/*     */     protected void readInternal(byte[] b, int offset, int len)
/*     */       throws IOException
/*     */     {
/* 105 */       synchronized (this.file) {
/* 106 */         long position = getFilePointer();
/* 107 */         if (position != this.file.position) {
/* 108 */           this.file.seek(position);
/* 109 */           this.file.position = position;
/*     */         }
/* 111 */         int total = 0;
/*     */         try
/*     */         {
/*     */           do
/*     */           {
/*     */             int readLength;
/*     */             int readLength;
/* 116 */             if (total + this.chunkSize > len) {
/* 117 */               readLength = len - total;
/*     */             }
/*     */             else {
/* 120 */               readLength = this.chunkSize;
/*     */             }
/* 122 */             int i = this.file.read(b, offset + total, readLength);
/* 123 */             if (i == -1) {
/* 124 */               throw new IOException("read past EOF");
/*     */             }
/* 126 */             this.file.position += i;
/* 127 */             total += i;
/* 128 */           }while (total < len);
/*     */         }
/*     */         catch (OutOfMemoryError e)
/*     */         {
/* 132 */           OutOfMemoryError outOfMemoryError = new OutOfMemoryError("OutOfMemoryError likely caused by the Sun VM Bug described in https://issues.apache.org/jira/browse/LUCENE-1566; try calling FSDirectory.setReadChunkSize with a a value smaller than the current chunks size (" + this.chunkSize + ")");
/*     */ 
/* 136 */           outOfMemoryError.initCause(e);
/* 137 */           throw outOfMemoryError;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void close()
/*     */       throws IOException
/*     */     {
/* 145 */       if (!this.isClone) this.file.close();
/*     */     }
/*     */ 
/*     */     protected void seekInternal(long position)
/*     */     {
/*     */     }
/*     */ 
/*     */     public long length()
/*     */     {
/* 154 */       return this.file.length;
/*     */     }
/*     */ 
/*     */     public Object clone()
/*     */     {
/* 159 */       SimpleFSIndexInput clone = (SimpleFSIndexInput)super.clone();
/* 160 */       clone.isClone = true;
/* 161 */       return clone;
/*     */     }
/*     */ 
/*     */     boolean isFDValid()
/*     */       throws IOException
/*     */     {
/* 168 */       return this.file.getFD().valid();
/*     */     }
/*     */ 
/*     */     protected static class Descriptor extends RandomAccessFile
/*     */     {
/*     */       protected volatile boolean isOpen;
/*     */       long position;
/*     */       final long length;
/*     */ 
/*     */       public Descriptor(File file, String mode)
/*     */         throws IOException
/*     */       {
/*  76 */         super(mode);
/*  77 */         this.isOpen = true;
/*  78 */         this.length = length();
/*     */       }
/*     */ 
/*     */       public void close() throws IOException
/*     */       {
/*  83 */         if (this.isOpen) {
/*  84 */           this.isOpen = false;
/*  85 */           super.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.SimpleFSDirectory
 * JD-Core Version:    0.6.2
 */