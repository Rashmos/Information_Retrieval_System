/*    */ package org.apache.lucene.analysis.sinks;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TeeSinkTokenFilter.SinkFilter;
/*    */ import org.apache.lucene.util.AttributeSource;
/*    */ 
/*    */ public class TokenRangeSinkFilter extends TeeSinkTokenFilter.SinkFilter
/*    */ {
/*    */   private int lower;
/*    */   private int upper;
/*    */   private int count;
/*    */ 
/*    */   public TokenRangeSinkFilter(int lower, int upper)
/*    */   {
/* 35 */     this.lower = lower;
/* 36 */     this.upper = upper;
/*    */   }
/*    */ 
/*    */   public boolean accept(AttributeSource source)
/*    */   {
/*    */     try
/*    */     {
/*    */       boolean bool;
/* 43 */       if ((this.count >= this.lower) && (this.count < this.upper)) {
/* 44 */         return true;
/*    */       }
/* 46 */       return false;
/*    */     } finally {
/* 48 */       this.count += 1;
/*    */     }
/*    */   }
/*    */ 
/*    */   public void reset() throws IOException
/*    */   {
/* 54 */     this.count = 0;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.sinks.TokenRangeSinkFilter
 * JD-Core Version:    0.6.2
 */