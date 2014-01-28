/*    */ package com.aliasi.lm;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class PruneTrieReader extends DeletingTrieReader
/*    */ {
/*    */   private final long mMinCount;
/*    */ 
/*    */   public PruneTrieReader(TrieReader reader, long minCount)
/*    */     throws IOException
/*    */   {
/* 46 */     super(reader);
/* 47 */     if (minCount < 0L) {
/* 48 */       String msg = "Minimum count must be >= 0. Found minCount=" + minCount;
/*    */ 
/* 50 */       throw new IllegalArgumentException(msg);
/*    */     }
/* 52 */     this.mMinCount = minCount;
/*    */   }
/*    */ 
/*    */   boolean bufferCount() throws IOException
/*    */   {
/* 57 */     this.mNextCount = nextCount();
/* 58 */     return this.mNextCount >= this.mMinCount;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.PruneTrieReader
 * JD-Core Version:    0.6.2
 */