/*    */ package org.apache.lucene.analysis.sinks;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.apache.lucene.analysis.SinkTokenizer;
/*    */ import org.apache.lucene.analysis.Token;
/*    */ 
/*    */ public class TokenTypeSinkTokenizer extends SinkTokenizer
/*    */ {
/*    */   private String typeToMatch;
/*    */ 
/*    */   public TokenTypeSinkTokenizer(String typeToMatch)
/*    */   {
/* 35 */     this.typeToMatch = typeToMatch;
/*    */   }
/*    */ 
/*    */   public TokenTypeSinkTokenizer(int initCap, String typeToMatch) {
/* 39 */     super(initCap);
/* 40 */     this.typeToMatch = typeToMatch;
/*    */   }
/*    */ 
/*    */   public TokenTypeSinkTokenizer(List input, String typeToMatch) {
/* 44 */     super(input);
/* 45 */     this.typeToMatch = typeToMatch;
/*    */   }
/*    */ 
/*    */   public void add(Token t)
/*    */   {
/* 50 */     if ((t != null) && (this.typeToMatch.equals(t.type())))
/* 51 */       super.add(t);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.sinks.TokenTypeSinkTokenizer
 * JD-Core Version:    0.6.2
 */