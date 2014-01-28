/*    */ package com.aliasi.corpus;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class Corpus<H extends Handler>
/*    */ {
/*    */   public void visitCorpus(H handler)
/*    */     throws IOException
/*    */   {
/* 56 */     visitCorpus(handler, handler);
/*    */   }
/*    */ 
/*    */   public void visitCorpus(H trainHandler, H testHandler)
/*    */     throws IOException
/*    */   {
/* 77 */     visitTrain(trainHandler);
/* 78 */     visitTest(testHandler);
/*    */   }
/*    */ 
/*    */   public void visitTrain(H handler)
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void visitTest(H handler)
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.Corpus
 * JD-Core Version:    0.6.2
 */