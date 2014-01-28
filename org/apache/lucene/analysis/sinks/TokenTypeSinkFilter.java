/*    */ package org.apache.lucene.analysis.sinks;
/*    */ 
/*    */ import org.apache.lucene.analysis.TeeSinkTokenFilter.SinkFilter;
/*    */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*    */ import org.apache.lucene.util.AttributeSource;
/*    */ 
/*    */ public class TokenTypeSinkFilter extends TeeSinkTokenFilter.SinkFilter
/*    */ {
/*    */   private String typeToMatch;
/*    */   private TypeAttribute typeAtt;
/*    */ 
/*    */   public TokenTypeSinkFilter(String typeToMatch)
/*    */   {
/* 29 */     this.typeToMatch = typeToMatch;
/*    */   }
/*    */ 
/*    */   public boolean accept(AttributeSource source)
/*    */   {
/* 34 */     if (this.typeAtt == null) {
/* 35 */       this.typeAtt = ((TypeAttribute)source.addAttribute(TypeAttribute.class));
/*    */     }
/*    */ 
/* 39 */     return this.typeToMatch.equals(this.typeAtt.type());
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.sinks.TokenTypeSinkFilter
 * JD-Core Version:    0.6.2
 */