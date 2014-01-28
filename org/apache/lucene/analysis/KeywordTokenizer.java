/*    */ package org.apache.lucene.analysis;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*    */ import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/*    */ import org.apache.lucene.util.AttributeSource;
/*    */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*    */ 
/*    */ public final class KeywordTokenizer extends Tokenizer
/*    */ {
/*    */   private static final int DEFAULT_BUFFER_SIZE = 256;
/*    */   private boolean done;
/*    */   private int finalOffset;
/*    */   private TermAttribute termAtt;
/*    */   private OffsetAttribute offsetAtt;
/*    */ 
/*    */   public KeywordTokenizer(Reader input)
/*    */   {
/* 40 */     this(input, 256);
/*    */   }
/*    */ 
/*    */   public KeywordTokenizer(Reader input, int bufferSize) {
/* 44 */     super(input);
/* 45 */     init(bufferSize);
/*    */   }
/*    */ 
/*    */   public KeywordTokenizer(AttributeSource source, Reader input, int bufferSize) {
/* 49 */     super(source, input);
/* 50 */     init(bufferSize);
/*    */   }
/*    */ 
/*    */   public KeywordTokenizer(AttributeSource.AttributeFactory factory, Reader input, int bufferSize) {
/* 54 */     super(factory, input);
/* 55 */     init(bufferSize);
/*    */   }
/*    */ 
/*    */   private void init(int bufferSize) {
/* 59 */     this.done = false;
/* 60 */     this.termAtt = ((TermAttribute)addAttribute(TermAttribute.class));
/* 61 */     this.offsetAtt = ((OffsetAttribute)addAttribute(OffsetAttribute.class));
/* 62 */     this.termAtt.resizeTermBuffer(bufferSize);
/*    */   }
/*    */ 
/*    */   public final boolean incrementToken() throws IOException
/*    */   {
/* 67 */     if (!this.done) {
/* 68 */       clearAttributes();
/* 69 */       this.done = true;
/* 70 */       int upto = 0;
/* 71 */       char[] buffer = this.termAtt.termBuffer();
/*    */       while (true) {
/* 73 */         int length = this.input.read(buffer, upto, buffer.length - upto);
/* 74 */         if (length == -1) break;
/* 75 */         upto += length;
/* 76 */         if (upto == buffer.length)
/* 77 */           buffer = this.termAtt.resizeTermBuffer(1 + buffer.length);
/*    */       }
/* 79 */       this.termAtt.setTermLength(upto);
/* 80 */       this.finalOffset = correctOffset(upto);
/* 81 */       this.offsetAtt.setOffset(correctOffset(0), this.finalOffset);
/* 82 */       return true;
/*    */     }
/* 84 */     return false;
/*    */   }
/*    */ 
/*    */   public final void end()
/*    */   {
/* 90 */     this.offsetAtt.setOffset(this.finalOffset, this.finalOffset);
/*    */   }
/*    */ 
/*    */   public void reset(Reader input) throws IOException
/*    */   {
/* 95 */     super.reset(input);
/* 96 */     this.done = false;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.KeywordTokenizer
 * JD-Core Version:    0.6.2
 */