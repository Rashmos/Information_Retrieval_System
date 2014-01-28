/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ 
/*     */ public class NIOFSDirectory extends FSDirectory
/*     */ {
/*     */   public NIOFSDirectory(File path, LockFactory lockFactory)
/*     */     throws IOException
/*     */   {
/*  51 */     super(path, lockFactory);
/*     */   }
/*     */ 
/*     */   public NIOFSDirectory(File path)
/*     */     throws IOException
/*     */   {
/*  60 */     super(path, null);
/*     */   }
/*     */ 
/*     */   public IndexInput openInput(String name, int bufferSize)
/*     */     throws IOException
/*     */   {
/*  66 */     ensureOpen();
/*  67 */     return new NIOFSIndexInput(new File(getFile(), name), bufferSize, getReadChunkSize());
/*     */   }
/*     */ 
/*     */   public IndexOutput createOutput(String name)
/*     */     throws IOException
/*     */   {
/*  73 */     initOutput(name);
/*  74 */     return new SimpleFSDirectory.SimpleFSIndexOutput(new File(this.directory, name));
/*     */   }
/*     */ 
/*     */   protected static class NIOFSIndexInput extends SimpleFSDirectory.SimpleFSIndexInput {
/*     */     private ByteBuffer byteBuf;
/*     */     private byte[] otherBuffer;
/*     */     private ByteBuffer otherByteBuf;
/*     */     final FileChannel channel;
/*     */ 
/*     */     public NIOFSIndexInput(File path, int bufferSize, int chunkSize) throws IOException {
/*  87 */       super(bufferSize, chunkSize);
/*  88 */       this.channel = this.file.getChannel();
/*     */     }
/*     */ 
/*     */     protected void newBuffer(byte[] newBuffer)
/*     */     {
/*  93 */       super.newBuffer(newBuffer);
/*  94 */       this.byteBuf = ByteBuffer.wrap(newBuffer);
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/*  99 */       if ((!this.isClone) && (this.file.isOpen))
/*     */         try
/*     */         {
/* 102 */           this.channel.close();
/*     */         } finally {
/* 104 */           this.file.close();
/*     */         }
/*     */     }
/*     */ 
/*     */     protected void readInternal(byte[] b, int offset, int len)
/*     */       throws IOException
/*     */     {
/*     */       ByteBuffer bb;
/*     */       ByteBuffer bb;
/* 115 */       if ((b == this.buffer) && (0 == offset))
/*     */       {
/* 117 */         assert (this.byteBuf != null);
/* 118 */         this.byteBuf.clear();
/* 119 */         this.byteBuf.limit(len);
/* 120 */         bb = this.byteBuf;
/*     */       }
/*     */       else
/*     */       {
/*     */         ByteBuffer bb;
/* 122 */         if (offset == 0) {
/* 123 */           if (this.otherBuffer != b)
/*     */           {
/* 128 */             this.otherBuffer = b;
/* 129 */             this.otherByteBuf = ByteBuffer.wrap(b);
/*     */           } else {
/* 131 */             this.otherByteBuf.clear();
/* 132 */           }this.otherByteBuf.limit(len);
/* 133 */           bb = this.otherByteBuf;
/*     */         }
/*     */         else {
/* 136 */           bb = ByteBuffer.wrap(b, offset, len);
/*     */         }
/*     */       }
/*     */ 
/* 140 */       int readOffset = bb.position();
/* 141 */       int readLength = bb.limit() - readOffset;
/* 142 */       assert (readLength == len);
/*     */ 
/* 144 */       long pos = getFilePointer();
/*     */       try
/*     */       {
/* 147 */         while (readLength > 0)
/*     */         {
/*     */           int limit;
/*     */           int limit;
/* 149 */           if (readLength > this.chunkSize)
/*     */           {
/* 152 */             limit = readOffset + this.chunkSize;
/*     */           }
/* 154 */           else limit = readOffset + readLength;
/*     */ 
/* 156 */           bb.limit(limit);
/* 157 */           int i = this.channel.read(bb, pos);
/* 158 */           if (i == -1) {
/* 159 */             throw new IOException("read past EOF");
/*     */           }
/* 161 */           pos += i;
/* 162 */           readOffset += i;
/* 163 */           readLength -= i;
/*     */         }
/*     */       }
/*     */       catch (OutOfMemoryError e)
/*     */       {
/* 168 */         OutOfMemoryError outOfMemoryError = new OutOfMemoryError("OutOfMemoryError likely caused by the Sun VM Bug described in https://issues.apache.org/jira/browse/LUCENE-1566; try calling FSDirectory.setReadChunkSize with a a value smaller than the current chunk size (" + this.chunkSize + ")");
/*     */ 
/* 172 */         outOfMemoryError.initCause(e);
/* 173 */         throw outOfMemoryError;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.NIOFSDirectory
 * JD-Core Version:    0.6.2
 */