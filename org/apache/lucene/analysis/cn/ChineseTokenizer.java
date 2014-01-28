/*     */ package org.apache.lucene.analysis.cn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ 
/*     */ @Deprecated
/*     */ public final class ChineseTokenizer extends Tokenizer
/*     */ {
/*  75 */   private int offset = 0; private int bufferIndex = 0; private int dataLen = 0;
/*     */   private static final int MAX_WORD_LEN = 255;
/*     */   private static final int IO_BUFFER_SIZE = 1024;
/*  78 */   private final char[] buffer = new char['ÿ'];
/*  79 */   private final char[] ioBuffer = new char[1024];
/*     */   private int length;
/*     */   private int start;
/*  85 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*  86 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/*     */ 
/*     */   public ChineseTokenizer(Reader in)
/*     */   {
/*  64 */     super(in);
/*     */   }
/*     */ 
/*     */   public ChineseTokenizer(AttributeSource source, Reader in) {
/*  68 */     super(source, in);
/*     */   }
/*     */ 
/*     */   public ChineseTokenizer(AttributeSource.AttributeFactory factory, Reader in) {
/*  72 */     super(factory, in);
/*     */   }
/*     */ 
/*     */   private final void push(char c)
/*     */   {
/*  90 */     if (this.length == 0) this.start = (this.offset - 1);
/*  91 */     this.buffer[(this.length++)] = Character.toLowerCase(c);
/*     */   }
/*     */ 
/*     */   private final boolean flush()
/*     */   {
/*  97 */     if (this.length > 0)
/*     */     {
/* 100 */       this.termAtt.copyBuffer(this.buffer, 0, this.length);
/* 101 */       this.offsetAtt.setOffset(correctOffset(this.start), correctOffset(this.start + this.length));
/* 102 */       return true;
/*     */     }
/*     */ 
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean incrementToken() throws IOException
/*     */   {
/* 110 */     clearAttributes();
/*     */ 
/* 112 */     this.length = 0;
/* 113 */     this.start = this.offset;
/*     */     while (true)
/*     */     {
/* 119 */       this.offset += 1;
/*     */ 
/* 121 */       if (this.bufferIndex >= this.dataLen) {
/* 122 */         this.dataLen = this.input.read(this.ioBuffer);
/* 123 */         this.bufferIndex = 0;
/*     */       }
/*     */ 
/* 126 */       if (this.dataLen == -1) {
/* 127 */         this.offset -= 1;
/* 128 */         return flush();
/*     */       }
/* 130 */       char c = this.ioBuffer[(this.bufferIndex++)];
/*     */ 
/* 133 */       switch (Character.getType(c))
/*     */       {
/*     */       case 1:
/*     */       case 2:
/*     */       case 9:
/* 138 */         push(c);
/* 139 */         if (this.length == 255) return flush();
/*     */ 
/*     */         break;
/*     */       case 5:
/* 143 */         if (this.length > 0) {
/* 144 */           this.bufferIndex -= 1;
/* 145 */           this.offset -= 1;
/* 146 */           return flush();
/*     */         }
/* 148 */         push(c);
/* 149 */         return flush();
/*     */       case 3:
/*     */       case 4:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       default:
/* 152 */         if (this.length > 0) return flush();
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void end()
/*     */   {
/* 161 */     int finalOffset = correctOffset(this.offset);
/* 162 */     this.offsetAtt.setOffset(finalOffset, finalOffset);
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 167 */     super.reset();
/* 168 */     this.offset = (this.bufferIndex = this.dataLen = 0);
/*     */   }
/*     */ 
/*     */   public void reset(Reader input) throws IOException
/*     */   {
/* 173 */     super.reset(input);
/* 174 */     reset();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cn.ChineseTokenizer
 * JD-Core Version:    0.6.2
 */