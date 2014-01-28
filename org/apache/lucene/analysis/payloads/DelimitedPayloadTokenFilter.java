/*    */ package org.apache.lucene.analysis.payloads;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.lucene.analysis.TokenFilter;
/*    */ import org.apache.lucene.analysis.TokenStream;
/*    */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
/*    */ 
/*    */ public final class DelimitedPayloadTokenFilter extends TokenFilter
/*    */ {
/*    */   public static final char DEFAULT_DELIMITER = '|';
/*    */   private final char delimiter;
/* 42 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 43 */   private final PayloadAttribute payAtt = (PayloadAttribute)addAttribute(PayloadAttribute.class);
/*    */   private final PayloadEncoder encoder;
/*    */ 
/*    */   public DelimitedPayloadTokenFilter(TokenStream input, char delimiter, PayloadEncoder encoder)
/*    */   {
/* 48 */     super(input);
/* 49 */     this.delimiter = delimiter;
/* 50 */     this.encoder = encoder;
/*    */   }
/*    */ 
/*    */   public boolean incrementToken() throws IOException
/*    */   {
/* 55 */     if (this.input.incrementToken()) {
/* 56 */       char[] buffer = this.termAtt.buffer();
/* 57 */       int length = this.termAtt.length();
/* 58 */       for (int i = 0; i < length; i++) {
/* 59 */         if (buffer[i] == this.delimiter) {
/* 60 */           this.payAtt.setPayload(this.encoder.encode(buffer, i + 1, length - (i + 1)));
/* 61 */           this.termAtt.setLength(i);
/* 62 */           return true;
/*    */         }
/*    */       }
/*    */ 
/* 66 */       this.payAtt.setPayload(null);
/* 67 */       return true;
/* 68 */     }return false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.payloads.DelimitedPayloadTokenFilter
 * JD-Core Version:    0.6.2
 */