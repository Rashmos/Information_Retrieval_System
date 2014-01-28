/*    */ package org.apache.lucene.analysis.sinks;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.SinkTokenizer;
/*    */ import org.apache.lucene.analysis.Token;
/*    */ 
/*    */ public class TokenRangeSinkTokenizer extends SinkTokenizer
/*    */ {
/*    */   private int lower;
/*    */   private int upper;
/*    */   private int count;
/*    */ 
/*    */   public TokenRangeSinkTokenizer(int lower, int upper)
/*    */   {
/* 35 */     this.lower = lower;
/* 36 */     this.upper = upper;
/*    */   }
/*    */ 
/*    */   public TokenRangeSinkTokenizer(int initCap, int lower, int upper) {
/* 40 */     super(initCap);
/* 41 */     this.lower = lower;
/* 42 */     this.upper = upper;
/*    */   }
/*    */ 
/*    */   public void add(Token t) {
/* 46 */     if ((this.count >= this.lower) && (this.count < this.upper)) {
/* 47 */       super.add(t);
/*    */     }
/* 49 */     this.count += 1;
/*    */   }
/*    */ 
/*    */   public void reset() throws IOException {
/* 53 */     this.count = 0;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.sinks.TokenRangeSinkTokenizer
 * JD-Core Version:    0.6.2
 */