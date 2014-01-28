/*    */ package com.aliasi.lm;
/*    */ 
/*    */ import com.aliasi.io.BitOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class BitTrieWriter extends BitTrie
/*    */   implements TrieWriter
/*    */ {
/*    */   private final BitOutput mBitOutput;
/*    */ 
/*    */   public BitTrieWriter(BitOutput bitOutput)
/*    */   {
/* 52 */     this.mBitOutput = bitOutput;
/*    */   }
/*    */ 
/*    */   public void writeCount(long count) throws IOException {
/* 56 */     checkCount(count);
/*    */ 
/* 59 */     this.mBitOutput.writeDelta(count);
/* 60 */     pushValue(-1L);
/*    */   }
/*    */ 
/*    */   public void writeSymbol(long symbol) throws IOException {
/* 64 */     if (symbol == -1L)
/*    */     {
/* 66 */       this.mBitOutput.writeDelta(1L);
/*    */ 
/* 69 */       popValue();
/*    */     } else {
/* 71 */       long code = symbol - popValue() + 1L;
/* 72 */       this.mBitOutput.writeDelta(code);
/*    */ 
/* 75 */       pushValue(symbol);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static void copy(TrieReader reader, TrieWriter writer)
/*    */     throws IOException
/*    */   {
/* 89 */     long count = reader.readCount();
/*    */ 
/* 91 */     writer.writeCount(count);
/*    */     long symbol;
/* 93 */     while ((symbol = reader.readSymbol()) != -1L)
/*    */     {
/* 95 */       writer.writeSymbol(symbol);
/* 96 */       copy(reader, writer);
/*    */     }
/*    */ 
/* 99 */     writer.writeSymbol(-1L);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.BitTrieWriter
 * JD-Core Version:    0.6.2
 */