/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class BufferedIndexInput extends IndexInput
/*     */ {
/*     */   public static final int BUFFER_SIZE = 1024;
/*  28 */   private int bufferSize = 1024;
/*     */   protected byte[] buffer;
/*  32 */   private long bufferStart = 0L;
/*  33 */   private int bufferLength = 0;
/*  34 */   private int bufferPosition = 0;
/*     */ 
/*     */   public byte readByte() throws IOException
/*     */   {
/*  38 */     if (this.bufferPosition >= this.bufferLength)
/*  39 */       refill();
/*  40 */     return this.buffer[(this.bufferPosition++)];
/*     */   }
/*     */ 
/*     */   public BufferedIndexInput() {
/*     */   }
/*     */ 
/*     */   public BufferedIndexInput(int bufferSize) {
/*  47 */     checkBufferSize(bufferSize);
/*  48 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   public void setBufferSize(int newSize)
/*     */   {
/*  53 */     if ((!$assertionsDisabled) && (this.buffer != null) && (this.bufferSize != this.buffer.length)) throw new AssertionError("buffer=" + this.buffer + " bufferSize=" + this.bufferSize + " buffer.length=" + (this.buffer != null ? this.buffer.length : 0));
/*  54 */     if (newSize != this.bufferSize) {
/*  55 */       checkBufferSize(newSize);
/*  56 */       this.bufferSize = newSize;
/*  57 */       if (this.buffer != null)
/*     */       {
/*  61 */         byte[] newBuffer = new byte[newSize];
/*  62 */         int leftInBuffer = this.bufferLength - this.bufferPosition;
/*     */         int numToCopy;
/*     */         int numToCopy;
/*  64 */         if (leftInBuffer > newSize)
/*  65 */           numToCopy = newSize;
/*     */         else
/*  67 */           numToCopy = leftInBuffer;
/*  68 */         System.arraycopy(this.buffer, this.bufferPosition, newBuffer, 0, numToCopy);
/*  69 */         this.bufferStart += this.bufferPosition;
/*  70 */         this.bufferPosition = 0;
/*  71 */         this.bufferLength = numToCopy;
/*  72 */         newBuffer(newBuffer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void newBuffer(byte[] newBuffer)
/*     */   {
/*  79 */     this.buffer = newBuffer;
/*     */   }
/*     */ 
/*     */   public int getBufferSize()
/*     */   {
/*  84 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */   private void checkBufferSize(int bufferSize) {
/*  88 */     if (bufferSize <= 0)
/*  89 */       throw new IllegalArgumentException("bufferSize must be greater than 0 (got " + bufferSize + ")");
/*     */   }
/*     */ 
/*     */   public void readBytes(byte[] b, int offset, int len) throws IOException
/*     */   {
/*  94 */     readBytes(b, offset, len, true);
/*     */   }
/*     */ 
/*     */   public void readBytes(byte[] b, int offset, int len, boolean useBuffer)
/*     */     throws IOException
/*     */   {
/* 100 */     if (len <= this.bufferLength - this.bufferPosition)
/*     */     {
/* 102 */       if (len > 0)
/* 103 */         System.arraycopy(this.buffer, this.bufferPosition, b, offset, len);
/* 104 */       this.bufferPosition += len;
/*     */     }
/*     */     else {
/* 107 */       int available = this.bufferLength - this.bufferPosition;
/* 108 */       if (available > 0) {
/* 109 */         System.arraycopy(this.buffer, this.bufferPosition, b, offset, available);
/* 110 */         offset += available;
/* 111 */         len -= available;
/* 112 */         this.bufferPosition += available;
/*     */       }
/*     */ 
/* 115 */       if ((useBuffer) && (len < this.bufferSize))
/*     */       {
/* 119 */         refill();
/* 120 */         if (this.bufferLength < len)
/*     */         {
/* 122 */           System.arraycopy(this.buffer, 0, b, offset, this.bufferLength);
/* 123 */           throw new IOException("read past EOF");
/*     */         }
/* 125 */         System.arraycopy(this.buffer, 0, b, offset, len);
/* 126 */         this.bufferPosition = len;
/*     */       }
/*     */       else
/*     */       {
/* 136 */         long after = this.bufferStart + this.bufferPosition + len;
/* 137 */         if (after > length())
/* 138 */           throw new IOException("read past EOF");
/* 139 */         readInternal(b, offset, len);
/* 140 */         this.bufferStart = after;
/* 141 */         this.bufferPosition = 0;
/* 142 */         this.bufferLength = 0;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void refill() throws IOException {
/* 148 */     long start = this.bufferStart + this.bufferPosition;
/* 149 */     long end = start + this.bufferSize;
/* 150 */     if (end > length())
/* 151 */       end = length();
/* 152 */     int newLength = (int)(end - start);
/* 153 */     if (newLength <= 0) {
/* 154 */       throw new IOException("read past EOF");
/*     */     }
/* 156 */     if (this.buffer == null) {
/* 157 */       newBuffer(new byte[this.bufferSize]);
/* 158 */       seekInternal(this.bufferStart);
/*     */     }
/* 160 */     readInternal(this.buffer, 0, newLength);
/* 161 */     this.bufferLength = newLength;
/* 162 */     this.bufferStart = start;
/* 163 */     this.bufferPosition = 0;
/*     */   }
/*     */ 
/*     */   protected abstract void readInternal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public long getFilePointer()
/*     */   {
/* 176 */     return this.bufferStart + this.bufferPosition;
/*     */   }
/*     */ 
/*     */   public void seek(long pos) throws IOException {
/* 180 */     if ((pos >= this.bufferStart) && (pos < this.bufferStart + this.bufferLength)) {
/* 181 */       this.bufferPosition = ((int)(pos - this.bufferStart));
/*     */     } else {
/* 183 */       this.bufferStart = pos;
/* 184 */       this.bufferPosition = 0;
/* 185 */       this.bufferLength = 0;
/* 186 */       seekInternal(pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void seekInternal(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 198 */     BufferedIndexInput clone = (BufferedIndexInput)super.clone();
/*     */ 
/* 200 */     clone.buffer = null;
/* 201 */     clone.bufferLength = 0;
/* 202 */     clone.bufferPosition = 0;
/* 203 */     clone.bufferStart = getFilePointer();
/*     */ 
/* 205 */     return clone;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.BufferedIndexInput
 * JD-Core Version:    0.6.2
 */