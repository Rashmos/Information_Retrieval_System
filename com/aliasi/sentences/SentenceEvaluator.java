/*    */ package com.aliasi.sentences;
/*    */ 
/*    */ import com.aliasi.chunk.Chunking;
/*    */ import com.aliasi.corpus.ObjectHandler;
/*    */ 
/*    */ public class SentenceEvaluator
/*    */   implements ObjectHandler<Chunking>
/*    */ {
/*    */   private final SentenceChunker mSentenceChunker;
/*    */   private final SentenceEvaluation mSentenceEvaluation;
/*    */ 
/*    */   public SentenceEvaluator(SentenceChunker sentenceChunker)
/*    */   {
/* 55 */     this.mSentenceEvaluation = new SentenceEvaluation();
/* 56 */     this.mSentenceChunker = sentenceChunker;
/*    */   }
/*    */ 
/*    */   public void handle(Chunking refChunking)
/*    */   {
/* 68 */     CharSequence cSeq = refChunking.charSequence();
/* 69 */     Chunking responseChunking = this.mSentenceChunker.chunk(cSeq);
/* 70 */     this.mSentenceEvaluation.addCase(refChunking, responseChunking);
/*    */   }
/*    */ 
/*    */   public SentenceEvaluation evaluation()
/*    */   {
/* 81 */     return this.mSentenceEvaluation;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.sentences.SentenceEvaluator
 * JD-Core Version:    0.6.2
 */