/*     */ package org.apache.lucene.store;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class IndexInput
/*     */   implements Cloneable, Closeable
/*     */ {
/*     */   private boolean preUTF8Strings;
/*     */ 
/*     */   public abstract byte readByte()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */ 
/*     */   public void readBytes(byte[] b, int offset, int len, boolean useBuffer)
/*     */     throws IOException
/*     */   {
/*  62 */     readBytes(b, offset, len);
/*     */   }
/*     */ 
/*     */   public int readInt()
/*     */     throws IOException
/*     */   {
/*  69 */     return (readByte() & 0xFF) << 24 | (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 8 | readByte() & 0xFF;
/*     */   }
/*     */ 
/*     */   public int readVInt()
/*     */     throws IOException
/*     */   {
/*  79 */     byte b = readByte();
/*  80 */     int i = b & 0x7F;
/*  81 */     for (int shift = 7; (b & 0x80) != 0; shift += 7) {
/*  82 */       b = readByte();
/*  83 */       i |= (b & 0x7F) << shift;
/*     */     }
/*  85 */     return i;
/*     */   }
/*     */ 
/*     */   public long readLong()
/*     */     throws IOException
/*     */   {
/*  92 */     return readInt() << 32 | readInt() & 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   public long readVLong()
/*     */     throws IOException
/*     */   {
/*  99 */     byte b = readByte();
/* 100 */     long i = b & 0x7F;
/* 101 */     for (int shift = 7; (b & 0x80) != 0; shift += 7) {
/* 102 */       b = readByte();
/* 103 */       i |= (b & 0x7F) << shift;
/*     */     }
/* 105 */     return i;
/*     */   }
/*     */ 
/*     */   public void setModifiedUTF8StringsMode()
/*     */   {
/* 113 */     this.preUTF8Strings = true;
/*     */   }
/*     */ 
/*     */   public String readString()
/*     */     throws IOException
/*     */   {
/* 120 */     if (this.preUTF8Strings)
/* 121 */       return readModifiedUTF8String();
/* 122 */     int length = readVInt();
/* 123 */     byte[] bytes = new byte[length];
/* 124 */     readBytes(bytes, 0, length);
/* 125 */     return new String(bytes, 0, length, "UTF-8");
/*     */   }
/*     */ 
/*     */   private String readModifiedUTF8String() throws IOException {
/* 129 */     int length = readVInt();
/* 130 */     char[] chars = new char[length];
/* 131 */     readChars(chars, 0, length);
/* 132 */     return new String(chars, 0, length);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void readChars(char[] buffer, int start, int length)
/*     */     throws IOException
/*     */   {
/* 147 */     int end = start + length;
/* 148 */     for (int i = start; i < end; i++) {
/* 149 */       byte b = readByte();
/* 150 */       if ((b & 0x80) == 0)
/* 151 */         buffer[i] = ((char)(b & 0x7F));
/* 152 */       else if ((b & 0xE0) != 224) {
/* 153 */         buffer[i] = ((char)((b & 0x1F) << 6 | readByte() & 0x3F));
/*     */       }
/*     */       else
/* 156 */         buffer[i] = ((char)((b & 0xF) << 12 | (readByte() & 0x3F) << 6 | readByte() & 0x3F));
/*     */     }
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void skipChars(int length)
/*     */     throws IOException
/*     */   {
/* 174 */     for (int i = 0; i < length; i++) {
/* 175 */       byte b = readByte();
/* 176 */       if ((b & 0x80) != 0)
/*     */       {
/* 179 */         if ((b & 0xE0) != 224) {
/* 180 */           readByte();
/*     */         }
/*     */         else {
/* 183 */           readByte();
/* 184 */           readByte();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract long getFilePointer();
/*     */ 
/*     */   public abstract void seek(long paramLong)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract long length();
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 218 */     IndexInput clone = null;
/*     */     try {
/* 220 */       clone = (IndexInput)super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 223 */     return clone;
/*     */   }
/*     */ 
/*     */   public Map<String, String> readStringStringMap() throws IOException {
/* 227 */     Map map = new HashMap();
/* 228 */     int count = readInt();
/* 229 */     for (int i = 0; i < count; i++) {
/* 230 */       String key = readString();
/* 231 */       String val = readString();
/* 232 */       map.put(key, val);
/*     */     }
/*     */ 
/* 235 */     return map;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.IndexInput
 * JD-Core Version:    0.6.2
 */