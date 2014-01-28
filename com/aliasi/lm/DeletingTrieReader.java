/*    */ package com.aliasi.lm;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ abstract class DeletingTrieReader
/*    */   implements TrieReader
/*    */ {
/*    */   private final TrieReader mReader;
/*    */   long mNextCount;
/*    */ 
/*    */   DeletingTrieReader(TrieReader reader)
/*    */     throws IOException
/*    */   {
/* 11 */     this.mReader = reader;
/* 12 */     bufferCount();
/*    */   }
/*    */ 
/*    */   public long readCount() {
/* 16 */     return this.mNextCount;
/*    */   }
/*    */ 
/*    */   public long readSymbol()
/*    */     throws IOException
/*    */   {
/*    */     long sym;
/* 21 */     while ((sym = this.mReader.readSymbol()) != -1L) {
/* 22 */       if (bufferCount()) {
/* 23 */         return sym;
/*    */       }
/* 25 */       flushDaughters();
/*    */     }
/* 27 */     return -1L;
/*    */   }
/*    */ 
/*    */   long nextCount() throws IOException {
/* 31 */     return this.mReader.readCount();
/*    */   }
/*    */ 
/*    */   abstract boolean bufferCount() throws IOException;
/*    */ 
/*    */   void flushDaughters() throws IOException
/*    */   {
/* 38 */     while (this.mReader.readSymbol() != -1L) {
/* 39 */       this.mReader.readCount();
/* 40 */       flushDaughters();
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.DeletingTrieReader
 * JD-Core Version:    0.6.2
 */