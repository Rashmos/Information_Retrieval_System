/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import org.apache.lucene.index.Payload;
/*    */ 
/*    */ public class IdentityEncoder extends AbstractEncoder
/*    */   implements PayloadEncoder
/*    */ {
/* 32 */   protected Charset charset = Charset.forName("UTF-8");
/*    */ 
/*    */   @Deprecated
/* 35 */   protected String charsetName = this.charset.name();
/*    */ 
/*    */   public IdentityEncoder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public IdentityEncoder(Charset charset) {
/* 42 */     this.charset = charset;
/*    */ 
/* 44 */     this.charsetName = charset.name();
/*    */   }
/*    */ 
/*    */   public Payload encode(char[] buffer, int offset, int length)
/*    */   {
/* 49 */     ByteBuffer bb = this.charset.encode(CharBuffer.wrap(buffer, offset, length));
/* 50 */     if (bb.hasArray()) {
/* 51 */       return new Payload(bb.array(), bb.arrayOffset() + bb.position(), bb.remaining());
/*    */     }
/*    */ 
/* 54 */     byte[] b = new byte[bb.remaining()];
/* 55 */     bb.get(b);
/* 56 */     return new Payload(b);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.IdentityEncoder
 * JD-Core Version:    0.6.2
 */