/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
/*    */ import org.apache.lucene.index.Payload;
/*    */ 
/*    */ public class TokenOffsetPayloadTokenFilter extends TokenFilter
/*    */ {
/* 36 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/* 37 */   private final PayloadAttribute payAtt = (PayloadAttribute)addAttribute(PayloadAttribute.class);
/*    */ 
/*    */   public TokenOffsetPayloadTokenFilter(TokenStream input) {
/* 40 */     super(input);
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken() throws IOException
/*    */   {
/* 45 */     if (this.input.incrementToken()) {
/* 46 */       byte[] data = new byte[8];
/* 47 */       PayloadHelper.encodeInt(this.offsetAtt.startOffset(), data, 0);
/* 48 */       PayloadHelper.encodeInt(this.offsetAtt.endOffset(), data, 4);
/* 49 */       Payload payload = new Payload(data);
/* 50 */       this.payAtt.setPayload(payload);
/* 51 */       return true;
/*    */     }
/* 53 */     return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.TokenOffsetPayloadTokenFilter
 * JD-Core Version:    0.6.2
 */