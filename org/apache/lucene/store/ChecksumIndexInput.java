/*    */ package org.apache.lucene.store;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.zip.CRC32;
/*    */ import java.util.zip.Checksum;
/*    */ 
/*    */ public class ChecksumIndexInput extends IndexInput
/*    */ {
/*    */   IndexInput main;
/*    */   Checksum digest;
/*    */ 
/*    */   public ChecksumIndexInput(IndexInput main)
/*    */   {
/* 31 */     this.main = main;
/* 32 */     this.digest = new CRC32();
/*    */   }
/*    */ 
/*    */   public byte readByte() throws IOException
/*    */   {
/* 37 */     byte b = this.main.readByte();
/* 38 */     this.digest.update(b);
/* 39 */     return b;
/*    */   }
/*    */ 
/*    */   public void readBytes(byte[] b, int offset, int len)
/*    */     throws IOException
/*    */   {
/* 45 */     this.main.readBytes(b, offset, len);
/* 46 */     this.digest.update(b, offset, len);
/*    */   }
/*    */ 
/*    */   public long getChecksum()
/*    */   {
/* 51 */     return this.digest.getValue();
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/* 56 */     this.main.close();
/*    */   }
/*    */ 
/*    */   public long getFilePointer()
/*    */   {
/* 61 */     return this.main.getFilePointer();
/*    */   }
/*    */ 
/*    */   public void seek(long pos)
/*    */   {
/* 66 */     throw new RuntimeException("not allowed");
/*    */   }
/*    */ 
/*    */   public long length()
/*    */   {
/* 71 */     return this.main.length();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.ChecksumIndexInput
 * JD-Core Version:    0.6.2
 */