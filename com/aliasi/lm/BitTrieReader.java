/*    */ package com.aliasi.lm;
/*    */ 
/*    */ import com.aliasi.io.BitInput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class BitTrieReader extends BitTrie
/*    */   implements TrieReader
/*    */ {
/*    */   private final BitInput mBitInput;
/*    */ 
/*    */   public BitTrieReader(BitInput bitInput)
/*    */   {
/* 44 */     this.mBitInput = bitInput;
/*    */   }
/*    */ 
/*    */   public long readCount()
/*    */     throws IOException
/*    */   {
/* 54 */     long count = this.mBitInput.readDelta();
/*    */ 
/* 56 */     pushValue(-1L);
/*    */ 
/* 58 */     return count;
/*    */   }
/*    */ 
/*    */   public long readSymbol()
/*    */     throws IOException
/*    */   {
/* 70 */     long n = this.mBitInput.readDelta();
/*    */ 
/* 72 */     if (n == 1L) {
/* 73 */       popValue();
/*    */ 
/* 75 */       return -1L;
/*    */     }
/* 77 */     long sym = n + popValue() - 1L;
/*    */ 
/* 79 */     pushValue(sym);
/* 80 */     return sym;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.BitTrieReader
 * JD-Core Version:    0.6.2
 */