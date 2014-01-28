/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileChannel.MapMode;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.apache.lucene.util.Constants;
/*     */ 
/*     */ public class MMapDirectory extends FSDirectory
/*     */ {
/*  94 */   private boolean useUnmapHack = false;
/*  95 */   private int maxBBuf = Constants.JRE_IS_64BIT ? 2147483647 : 268435456;
/*     */ 
/* 111 */   public static final boolean UNMAP_SUPPORTED = v;
/*     */ 
/*     */   public MMapDirectory(File path, LockFactory lockFactory)
/*     */     throws IOException
/*     */   {
/*  82 */     super(path, lockFactory);
/*     */   }
/*     */ 
/*     */   public MMapDirectory(File path)
/*     */     throws IOException
/*     */   {
/*  91 */     super(path, null);
/*     */   }
/*     */ 
/*     */   public void setUseUnmap(boolean useUnmapHack)
/*     */   {
/* 127 */     if ((useUnmapHack) && (!UNMAP_SUPPORTED))
/* 128 */       throw new IllegalArgumentException("Unmap hack not supported on this platform!");
/* 129 */     this.useUnmapHack = useUnmapHack;
/*     */   }
/*     */ 
/*     */   public boolean getUseUnmap()
/*     */   {
/* 137 */     return this.useUnmapHack;
/*     */   }
/*     */ 
/*     */   final void cleanMapping(final ByteBuffer buffer)
/*     */     throws IOException
/*     */   {
/* 146 */     if (this.useUnmapHack)
/*     */       try {
/* 148 */         AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Object run() throws Exception {
/* 150 */             Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
/*     */ 
/* 152 */             getCleanerMethod.setAccessible(true);
/* 153 */             Object cleaner = getCleanerMethod.invoke(buffer, new Object[0]);
/* 154 */             if (cleaner != null) {
/* 155 */               cleaner.getClass().getMethod("clean", new Class[0]).invoke(cleaner, new Object[0]);
/*     */             }
/*     */ 
/* 158 */             return null;
/*     */           } } );
/*     */       }
/*     */       catch (PrivilegedActionException e) {
/* 162 */         IOException ioe = new IOException("unable to unmap the mapped buffer");
/* 163 */         ioe.initCause(e.getCause());
/* 164 */         throw ioe;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void setMaxChunkSize(int maxBBuf)
/*     */   {
/* 181 */     if (maxBBuf <= 0)
/* 182 */       throw new IllegalArgumentException("Maximum chunk size for mmap must be >0");
/* 183 */     this.maxBBuf = maxBBuf;
/*     */   }
/*     */ 
/*     */   public int getMaxChunkSize()
/*     */   {
/* 191 */     return this.maxBBuf;
/*     */   }
/*     */ 
/*     */   public IndexInput openInput(String name, int bufferSize)
/*     */     throws IOException
/*     */   {
/* 403 */     ensureOpen();
/* 404 */     File f = new File(getFile(), name);
/* 405 */     RandomAccessFile raf = new RandomAccessFile(f, "r");
/*     */     try {
/* 407 */       return raf.length() <= this.maxBBuf ? new MMapIndexInput(raf, null) : new MultiMMapIndexInput(raf, this.maxBBuf);
/*     */     }
/*     */     finally
/*     */     {
/* 411 */       raf.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public IndexOutput createOutput(String name)
/*     */     throws IOException
/*     */   {
/* 418 */     initOutput(name);
/* 419 */     return new SimpleFSDirectory.SimpleFSIndexOutput(new File(this.directory, name));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     boolean v;
/*     */     try
/*     */     {
/* 104 */       Class.forName("sun.misc.Cleaner");
/* 105 */       Class.forName("java.nio.DirectByteBuffer").getMethod("cleaner", new Class[0]);
/*     */ 
/* 107 */       v = true;
/*     */     } catch (Exception e) {
/* 109 */       v = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MultiMMapIndexInput extends IndexInput
/*     */   {
/*     */     private ByteBuffer[] buffers;
/*     */     private int[] bufSizes;
/*     */     private final long length;
/*     */     private int curBufIndex;
/*     */     private final int maxBufSize;
/*     */     private ByteBuffer curBuf;
/*     */     private int curAvail;
/* 274 */     private boolean isClone = false;
/*     */ 
/*     */     public MultiMMapIndexInput(RandomAccessFile raf, int maxBufSize) throws IOException
/*     */     {
/* 278 */       this.length = raf.length();
/* 279 */       this.maxBufSize = maxBufSize;
/*     */ 
/* 281 */       if (maxBufSize <= 0) {
/* 282 */         throw new IllegalArgumentException("Non positive maxBufSize: " + maxBufSize);
/*     */       }
/*     */ 
/* 285 */       if (this.length / maxBufSize > 2147483647L) {
/* 286 */         throw new IllegalArgumentException("RandomAccessFile too big for maximum buffer size: " + raf.toString());
/*     */       }
/*     */ 
/* 290 */       int nrBuffers = (int)(this.length / maxBufSize);
/* 291 */       if (nrBuffers * maxBufSize < this.length) nrBuffers++;
/*     */ 
/* 293 */       this.buffers = new ByteBuffer[nrBuffers];
/* 294 */       this.bufSizes = new int[nrBuffers];
/*     */ 
/* 296 */       long bufferStart = 0L;
/* 297 */       FileChannel rafc = raf.getChannel();
/* 298 */       for (int bufNr = 0; bufNr < nrBuffers; bufNr++) {
/* 299 */         int bufSize = this.length > bufferStart + maxBufSize ? maxBufSize : (int)(this.length - bufferStart);
/*     */ 
/* 302 */         this.buffers[bufNr] = rafc.map(FileChannel.MapMode.READ_ONLY, bufferStart, bufSize);
/* 303 */         this.bufSizes[bufNr] = bufSize;
/* 304 */         bufferStart += bufSize;
/*     */       }
/* 306 */       seek(0L);
/*     */     }
/*     */ 
/*     */     public byte readByte()
/*     */       throws IOException
/*     */     {
/* 313 */       if (this.curAvail == 0) {
/* 314 */         this.curBufIndex += 1;
/* 315 */         if (this.curBufIndex >= this.buffers.length)
/* 316 */           throw new IOException("read past EOF");
/* 317 */         this.curBuf = this.buffers[this.curBufIndex];
/* 318 */         this.curBuf.position(0);
/* 319 */         this.curAvail = this.bufSizes[this.curBufIndex];
/*     */       }
/* 321 */       this.curAvail -= 1;
/* 322 */       return this.curBuf.get();
/*     */     }
/*     */ 
/*     */     public void readBytes(byte[] b, int offset, int len) throws IOException
/*     */     {
/* 327 */       while (len > this.curAvail) {
/* 328 */         this.curBuf.get(b, offset, this.curAvail);
/* 329 */         len -= this.curAvail;
/* 330 */         offset += this.curAvail;
/* 331 */         this.curBufIndex += 1;
/* 332 */         if (this.curBufIndex >= this.buffers.length)
/* 333 */           throw new IOException("read past EOF");
/* 334 */         this.curBuf = this.buffers[this.curBufIndex];
/* 335 */         this.curBuf.position(0);
/* 336 */         this.curAvail = this.bufSizes[this.curBufIndex];
/*     */       }
/* 338 */       this.curBuf.get(b, offset, len);
/* 339 */       this.curAvail -= len;
/*     */     }
/*     */ 
/*     */     public long getFilePointer()
/*     */     {
/* 344 */       return this.curBufIndex * this.maxBufSize + this.curBuf.position();
/*     */     }
/*     */ 
/*     */     public void seek(long pos) throws IOException
/*     */     {
/* 349 */       this.curBufIndex = ((int)(pos / this.maxBufSize));
/* 350 */       this.curBuf = this.buffers[this.curBufIndex];
/* 351 */       int bufOffset = (int)(pos - this.curBufIndex * this.maxBufSize);
/* 352 */       this.curBuf.position(bufOffset);
/* 353 */       this.curAvail = (this.bufSizes[this.curBufIndex] - bufOffset);
/*     */     }
/*     */ 
/*     */     public long length()
/*     */     {
/* 358 */       return this.length;
/*     */     }
/*     */ 
/*     */     public Object clone()
/*     */     {
/* 363 */       MultiMMapIndexInput clone = (MultiMMapIndexInput)super.clone();
/* 364 */       clone.isClone = true;
/* 365 */       clone.buffers = new ByteBuffer[this.buffers.length];
/*     */ 
/* 369 */       for (int bufNr = 0; bufNr < this.buffers.length; bufNr++)
/* 370 */         clone.buffers[bufNr] = this.buffers[bufNr].duplicate();
/*     */       try
/*     */       {
/* 373 */         clone.seek(getFilePointer());
/*     */       } catch (IOException ioe) {
/* 375 */         RuntimeException newException = new RuntimeException(ioe);
/* 376 */         newException.initCause(ioe);
/* 377 */         throw newException;
/*     */       }
/* 379 */       return clone;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 384 */       if ((this.isClone) || (this.buffers == null)) return; try
/*     */       {
/* 386 */         for (int bufNr = 0; bufNr < this.buffers.length; bufNr++)
/*     */           try
/*     */           {
/* 389 */             MMapDirectory.this.cleanMapping(this.buffers[bufNr]);
/*     */           } finally {
/* 391 */             this.buffers[bufNr] = null;
/*     */           }
/*     */       }
/*     */       finally {
/* 395 */         this.buffers = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MMapIndexInput extends IndexInput
/*     */   {
/*     */     private ByteBuffer buffer;
/*     */     private final long length;
/* 198 */     private boolean isClone = false;
/*     */ 
/*     */     private MMapIndexInput(RandomAccessFile raf) throws IOException {
/* 201 */       this.length = raf.length();
/* 202 */       this.buffer = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, this.length);
/*     */     }
/*     */ 
/*     */     public byte readByte() throws IOException
/*     */     {
/*     */       try {
/* 208 */         return this.buffer.get(); } catch (BufferUnderflowException e) {
/*     */       }
/* 210 */       throw new IOException("read past EOF");
/*     */     }
/*     */ 
/*     */     public void readBytes(byte[] b, int offset, int len) throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 217 */         this.buffer.get(b, offset, len);
/*     */       } catch (BufferUnderflowException e) {
/* 219 */         throw new IOException("read past EOF");
/*     */       }
/*     */     }
/*     */ 
/*     */     public long getFilePointer()
/*     */     {
/* 225 */       return this.buffer.position();
/*     */     }
/*     */ 
/*     */     public void seek(long pos) throws IOException
/*     */     {
/* 230 */       this.buffer.position((int)pos);
/*     */     }
/*     */ 
/*     */     public long length()
/*     */     {
/* 235 */       return this.length;
/*     */     }
/*     */ 
/*     */     public Object clone()
/*     */     {
/* 240 */       MMapIndexInput clone = (MMapIndexInput)super.clone();
/* 241 */       clone.isClone = true;
/* 242 */       clone.buffer = this.buffer.duplicate();
/* 243 */       return clone;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException
/*     */     {
/* 248 */       if ((this.isClone) || (this.buffer == null)) return;
/*     */       try
/*     */       {
/* 251 */         MMapDirectory.this.cleanMapping(this.buffer);
/*     */       } finally {
/* 253 */         this.buffer = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.MMapDirectory
 * JD-Core Version:    0.6.2
 */