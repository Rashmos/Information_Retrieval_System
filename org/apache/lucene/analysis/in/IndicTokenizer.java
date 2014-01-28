/*    */ package org.apache.lucene.analysis.in;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import org.apache.lucene.analysis.CharTokenizer;
/*    */ import org.apache.lucene.util.AttributeSource;
/*    */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*    */ import org.apache.lucene.util.Version;
/*    */ 
/*    */ public final class IndicTokenizer extends CharTokenizer
/*    */ {
/*    */   public IndicTokenizer(Version matchVersion, AttributeSource.AttributeFactory factory, Reader input)
/*    */   {
/* 32 */     super(matchVersion, factory, input);
/*    */   }
/*    */ 
/*    */   public IndicTokenizer(Version matchVersion, AttributeSource source, Reader input) {
/* 36 */     super(matchVersion, source, input);
/*    */   }
/*    */ 
/*    */   public IndicTokenizer(Version matchVersion, Reader input) {
/* 40 */     super(matchVersion, input);
/*    */   }
/*    */ 
/*    */   protected boolean isTokenChar(int c)
/*    */   {
/* 45 */     return (Character.isLetter(c)) || (Character.getType(c) == 6) || (Character.getType(c) == 16) || (Character.getType(c) == 8);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.in.IndicTokenizer
 * JD-Core Version:    0.6.2
 */