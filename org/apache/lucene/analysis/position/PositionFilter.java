/*    */ package org.apache.lucene.analysis.position;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
/*    */ 
/*    */ public final class PositionFilter extends TokenFilter
/*    */ {
/* 33 */   private int positionIncrement = 0;
/*    */ 
/* 36 */   private boolean firstTokenPositioned = false;
/*    */ 
/* 38 */   private PositionIncrementAttribute posIncrAtt = (PositionIncrementAttribute)addAttribute(PositionIncrementAttribute.class);
/*    */ 
/*    */   public PositionFilter(TokenStream input)
/*    */   {
/* 47 */     super(input);
/*    */   }
/*    */ 
/*    */   public PositionFilter(TokenStream input, int positionIncrement)
/*    */   {
/* 59 */     this(input);
/* 60 */     this.positionIncrement = positionIncrement;
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken() throws IOException
/*    */   {
/* 65 */     if (this.input.incrementToken()) {
/* 66 */       if (this.firstTokenPositioned)
/* 67 */         this.posIncrAtt.setPositionIncrement(this.positionIncrement);
/*    */       else {
/* 69 */         this.firstTokenPositioned = true;
/*    */       }
/* 71 */       return true;
/*    */     }
/* 73 */     return false;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */     throws IOException
/*    */   {
/* 79 */     super.reset();
/* 80 */     this.firstTokenPositioned = false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.position.PositionFilter
 * JD-Core Version:    0.6.2
 */