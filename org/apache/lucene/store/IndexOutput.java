/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.lucene.util.UnicodeUtil;
/*     */ import org.apache.lucene.util.UnicodeUtil.UTF8Result;
/*     */ 
/*     */ public abstract class IndexOutput
/*     */   implements Closeable
/*     */ {
/* 159 */   private static int COPY_BUFFER_SIZE = 16384;
/*     */   private byte[] copyBuffer;
/*     */ 
/*     */   public abstract void writeByte(byte paramByte)
/*     */     throws IOException;
/*     */ 
/*     */   public void writeBytes(byte[] b, int length)
/*     */     throws IOException
/*     */   {
/*  43 */     writeBytes(b, 0, length);
/*     */   }
/*     */ 
/*     */   public abstract void writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public void writeInt(int i)
/*     */     throws IOException
/*     */   {
/*  58 */     writeByte((byte)(i >> 24));
/*  59 */     writeByte((byte)(i >> 16));
/*  60 */     writeByte((byte)(i >> 8));
/*  61 */     writeByte((byte)i);
/*     */   }
/*     */ 
/*     */   public void writeVInt(int i)
/*     */     throws IOException
/*     */   {
/*  70 */     while ((i & 0xFFFFFF80) != 0) {
/*  71 */       writeByte((byte)(i & 0x7F | 0x80));
/*  72 */       i >>>= 7;
/*     */     }
/*  74 */     writeByte((byte)i);
/*     */   }
/*     */ 
/*     */   public void writeLong(long i)
/*     */     throws IOException
/*     */   {
/*  81 */     writeInt((int)(i >> 32));
/*  82 */     writeInt((int)i);
/*     */   }
/*     */ 
/*     */   public void writeVLong(long i)
/*     */     throws IOException
/*     */   {
/*  91 */     while ((i & 0xFFFFFF80) != 0L) {
/*  92 */       writeByte((byte)(int)(i & 0x7F | 0x80));
/*  93 */       i >>>= 7;
/*     */     }
/*  95 */     writeByte((byte)(int)i);
/*     */   }
/*     */ 
/*     */   public void writeString(String s)
/*     */     throws IOException
/*     */   {
/* 102 */     UnicodeUtil.UTF8Result utf8Result = new UnicodeUtil.UTF8Result();
/* 103 */     UnicodeUtil.UTF16toUTF8(s, 0, s.length(), utf8Result);
/* 104 */     writeVInt(utf8Result.length);
/* 105 */     writeBytes(utf8Result.result, 0, utf8Result.length);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void writeChars(String s, int start, int length)
/*     */     throws IOException
/*     */   {
/* 118 */     int end = start + length;
/* 119 */     for (int i = start; i < end; i++) {
/* 120 */       int code = s.charAt(i);
/* 121 */       if ((code >= 1) && (code <= 127)) {
/* 122 */         writeByte((byte)code);
/* 123 */       } else if (((code >= 128) && (code <= 2047)) || (code == 0)) {
/* 124 */         writeByte((byte)(0xC0 | code >> 6));
/* 125 */         writeByte((byte)(0x80 | code & 0x3F));
/*     */       } else {
/* 127 */         writeByte((byte)(0xE0 | code >>> 12));
/* 128 */         writeByte((byte)(0x80 | code >> 6 & 0x3F));
/* 129 */         writeByte((byte)(0x80 | code & 0x3F));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void writeChars(char[] s, int start, int length)
/*     */     throws IOException
/*     */   {
/* 143 */     int end = start + length;
/* 144 */     for (int i = start; i < end; i++) {
/* 145 */       int code = s[i];
/* 146 */       if ((code >= 1) && (code <= 127)) {
/* 147 */         writeByte((byte)code);
/* 148 */       } else if (((code >= 128) && (code <= 2047)) || (code == 0)) {
/* 149 */         writeByte((byte)(0xC0 | code >> 6));
/* 150 */         writeByte((byte)(0x80 | code & 0x3F));
/*     */       } else {
/* 152 */         writeByte((byte)(0xE0 | code >>> 12));
/* 153 */         writeByte((byte)(0x80 | code >> 6 & 0x3F));
/* 154 */         writeByte((byte)(0x80 | code & 0x3F));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void copyBytes(IndexInput input, long numBytes)
/*     */     throws IOException
/*     */   {
/* 164 */     assert (numBytes >= 0L) : ("numBytes=" + numBytes);
/* 165 */     long left = numBytes;
/* 166 */     if (this.copyBuffer == null)
/* 167 */       this.copyBuffer = new byte[COPY_BUFFER_SIZE];
/* 168 */     while (left > 0L)
/*     */     {
/*     */       int toCopy;
/*     */       int toCopy;
/* 170 */       if (left > COPY_BUFFER_SIZE)
/* 171 */         toCopy = COPY_BUFFER_SIZE;
/*     */       else
/* 173 */         toCopy = (int)left;
/* 174 */       input.readBytes(this.copyBuffer, 0, toCopy);
/* 175 */       writeBytes(this.copyBuffer, 0, toCopy);
/* 176 */       left -= toCopy;
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void flush()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract long getFilePointer();
/*     */ 
/*     */   public abstract void seek(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract long length()
/*     */     throws IOException;
/*     */ 
/*     */   public void setLength(long length)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeStringStringMap(Map<String, String> map)
/*     */     throws IOException
/*     */   {
/* 213 */     if (map == null) {
/* 214 */       writeInt(0);
/*     */     } else {
/* 216 */       writeInt(map.size());
/* 217 */       for (Map.Entry entry : map.entrySet()) {
/* 218 */         writeString((String)entry.getKey());
/* 219 */         writeString((String)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.IndexOutput
 * JD-Core Version:    0.6.2
 */