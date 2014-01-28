/*    */ package com.aliasi.lm;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ScaleTrieReader extends DeletingTrieReader
/*    */ {
/*    */   private final double mScale;
/*    */ 
/*    */   public ScaleTrieReader(TrieReader reader, double scale)
/*    */     throws IOException
/*    */   {
/* 49 */     super(reader);
/* 50 */     if ((scale <= 0.0D) || (Double.isNaN(scale)) || (Double.isInfinite(scale))) {
/* 51 */       String msg = "Scale must be positive and non-infinite. Found scale=" + scale;
/*    */ 
/* 53 */       throw new IllegalArgumentException(msg);
/*    */     }
/* 55 */     this.mScale = scale;
/*    */   }
/*    */ 
/*    */   boolean bufferCount()
/*    */     throws IOException
/*    */   {
/* 61 */     this.mNextCount = Math.round(this.mScale * nextCount());
/* 62 */     return this.mNextCount > 0L;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.lm.ScaleTrieReader
 * JD-Core Version:    0.6.2
 */