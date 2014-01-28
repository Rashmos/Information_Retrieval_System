/*    */ package org.apache.lucene.store;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.zip.CRC32;
/*    */ import java.util.zip.Checksum;
/*    */ 
/*    */ public class ChecksumIndexOutput extends IndexOutput
/*    */ {
/*    */   IndexOutput main;
/*    */   Checksum digest;
/*    */ 
/*    */   public ChecksumIndexOutput(IndexOutput main)
/*    */   {
/* 31 */     this.main = main;
/* 32 */     this.digest = new CRC32();
/*    */   }
/*    */ 
/*    */   public void writeByte(byte b) throws IOException
/*    */   {
/* 37 */     this.digest.update(b);
/* 38 */     this.main.writeByte(b);
/*    */   }
/*    */ 
/*    */   public void writeBytes(byte[] b, int offset, int length) throws IOException
/*    */   {
/* 43 */     this.digest.update(b, offset, length);
/* 44 */     this.main.writeBytes(b, offset, length);
/*    */   }
/*    */ 
/*    */   public long getChecksum() {
/* 48 */     return this.digest.getValue();
/*    */   }
/*    */ 
/*    */   public void flush() throws IOException
/*    */   {
/* 53 */     this.main.flush();
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/* 58 */     this.main.close();
/*    */   }
/*    */ 
/*    */   public long getFilePointer()
/*    */   {
/* 63 */     return this.main.getFilePointer();
/*    */   }
/*    */ 
/*    */   public void seek(long pos)
/*    */   {
/* 68 */     throw new RuntimeException("not allowed");
/*    */   }
/*    */ 
/*    */   public void prepareCommit()
/*    */     throws IOException
/*    */   {
/* 78 */     long checksum = getChecksum();
/*    */ 
/* 84 */     long pos = this.main.getFilePointer();
/* 85 */     this.main.writeLong(checksum - 1L);
/* 86 */     this.main.flush();
/* 87 */     this.main.seek(pos);
/*    */   }
/*    */ 
/*    */   public void finishCommit() throws IOException
/*    */   {
/* 92 */     this.main.writeLong(getChecksum());
/*    */   }
/*    */ 
/*    */   public long length() throws IOException
/*    */   {
/* 97 */     return this.main.length();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.store.ChecksumIndexOutput
 * JD-Core Version:    0.6.2
 */