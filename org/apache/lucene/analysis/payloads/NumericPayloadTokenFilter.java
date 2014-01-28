/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*    */ import org.apache.lucene.index.Payload;
/*    */ 
/*    */ public class NumericPayloadTokenFilter extends TokenFilter
/*    */ {
/*    */   private String typeMatch;
/*    */   private Payload thePayload;
/* 38 */   private final PayloadAttribute payloadAtt = (PayloadAttribute)addAttribute(PayloadAttribute.class);
/* 39 */   private final TypeAttribute typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
/*    */ 
/*    */   public NumericPayloadTokenFilter(TokenStream input, float payload, String typeMatch) {
/* 42 */     super(input);
/*    */ 
/* 44 */     this.thePayload = new Payload(PayloadHelper.encodeFloat(payload));
/* 45 */     this.typeMatch = typeMatch;
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken() throws IOException
/*    */   {
/* 50 */     if (this.input.incrementToken()) {
/* 51 */       if (this.typeAtt.type().equals(this.typeMatch))
/* 52 */         this.payloadAtt.setPayload(this.thePayload);
/* 53 */       return true;
/*    */     }
/* 55 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.NumericPayloadTokenFilter
 * JD-Core Version:    0.6.2
 */