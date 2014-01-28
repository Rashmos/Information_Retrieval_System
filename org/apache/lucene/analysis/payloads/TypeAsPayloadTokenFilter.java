/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*    */ import org.apache.lucene.index.Payload;
/*    */ 
/*    */ public class TypeAsPayloadTokenFilter extends TokenFilter
/*    */ {
/* 36 */   private final PayloadAttribute payloadAtt = (PayloadAttribute)addAttribute(PayloadAttribute.class);
/* 37 */   private final TypeAttribute typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
/*    */ 
/*    */   public TypeAsPayloadTokenFilter(TokenStream input) {
/* 40 */     super(input);
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken()
/*    */     throws IOException
/*    */   {
/* 46 */     if (this.input.incrementToken()) {
/* 47 */       String type = this.typeAtt.type();
/* 48 */       if ((type != null) && (!type.equals(""))) {
/* 49 */         this.payloadAtt.setPayload(new Payload(type.getBytes("UTF-8")));
/*    */       }
/* 51 */       return true;
/*    */     }
/* 53 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.TypeAsPayloadTokenFilter
 * JD-Core Version:    0.6.2
 */