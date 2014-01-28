/*     */ package org.apache.lucene.analysis.cjk;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.Tokenizer;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.AttributeSource.AttributeFactory;
/*     */ 
/*     */ public final class CJKTokenizer extends Tokenizer
/*     */ {
/*     */   static final int WORD_TYPE = 0;
/*     */   static final int SINGLE_TOKEN_TYPE = 1;
/*     */   static final int DOUBLE_TOKEN_TYPE = 2;
/*  60 */   static final String[] TOKEN_TYPE_NAMES = { "word", "single", "double" };
/*     */   private static final int MAX_WORD_LEN = 255;
/*     */   private static final int IO_BUFFER_SIZE = 256;
/*  71 */   private int offset = 0;
/*     */ 
/*  74 */   private int bufferIndex = 0;
/*     */ 
/*  77 */   private int dataLen = 0;
/*     */ 
/*  83 */   private final char[] buffer = new char['ÿ'];
/*     */ 
/*  89 */   private final char[] ioBuffer = new char[256];
/*     */ 
/*  92 */   private int tokenType = 0;
/*     */ 
/*  99 */   private boolean preIsTokened = false;
/*     */ 
/* 101 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/* 102 */   private final OffsetAttribute offsetAtt = (OffsetAttribute)addAttribute(OffsetAttribute.class);
/* 103 */   private final TypeAttribute typeAtt = (TypeAttribute)addAttribute(TypeAttribute.class);
/*     */ 
/*     */   public CJKTokenizer(Reader in)
/*     */   {
/* 113 */     super(in);
/*     */   }
/*     */ 
/*     */   public CJKTokenizer(AttributeSource source, Reader in) {
/* 117 */     super(source, in);
/*     */   }
/*     */ 
/*     */   public CJKTokenizer(AttributeSource.AttributeFactory factory, Reader in) {
/* 121 */     super(factory, in);
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */     throws IOException
/*     */   {
/* 139 */     clearAttributes();
/*     */     while (true)
/*     */     {
/* 144 */       int length = 0;
/*     */ 
/* 147 */       int start = this.offset;
/*     */       while (true)
/*     */       {
/* 156 */         this.offset += 1;
/*     */ 
/* 158 */         if (this.bufferIndex >= this.dataLen) {
/* 159 */           this.dataLen = this.input.read(this.ioBuffer);
/* 160 */           this.bufferIndex = 0;
/*     */         }
/*     */ 
/* 163 */         if (this.dataLen == -1) {
/* 164 */           if (length > 0) {
/* 165 */             if (this.preIsTokened == true) {
/* 166 */               length = 0;
/* 167 */               this.preIsTokened = false; break;
/*     */             }
/*     */ 
/* 170 */             this.offset -= 1;
/*     */ 
/* 173 */             break;
/*     */           }
/* 175 */           this.offset -= 1;
/* 176 */           return false;
/*     */         }
/*     */ 
/* 180 */         char c = this.ioBuffer[(this.bufferIndex++)];
/*     */ 
/* 183 */         Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
/*     */ 
/* 187 */         if ((ub == Character.UnicodeBlock.BASIC_LATIN) || (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS))
/*     */         {
/* 190 */           if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
/* 191 */             int i = c;
/* 192 */             if ((i >= 65281) && (i <= 65374))
/*     */             {
/* 194 */               i -= 65248;
/* 195 */               c = (char)i;
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 200 */           if ((Character.isLetterOrDigit(c)) || (c == '_') || (c == '+') || (c == '#'))
/*     */           {
/* 203 */             if (length == 0)
/*     */             {
/* 207 */               start = this.offset - 1;
/* 208 */             } else if (this.tokenType == 2)
/*     */             {
/* 212 */               this.offset -= 1;
/* 213 */               this.bufferIndex -= 1;
/*     */ 
/* 215 */               if (this.preIsTokened != true)
/*     */                 break;
/* 217 */               length = 0;
/* 218 */               this.preIsTokened = false;
/* 219 */               break;
/*     */             }
/*     */ 
/* 226 */             this.buffer[(length++)] = Character.toLowerCase(c);
/* 227 */             this.tokenType = 1;
/*     */ 
/* 230 */             if (length == 255)
/* 231 */               break;
/*     */           }
/* 233 */           else if (length > 0) {
/* 234 */             if (this.preIsTokened != true) break;
/* 235 */             length = 0;
/* 236 */             this.preIsTokened = false;
/*     */           }
/*     */ 
/*     */         }
/* 243 */         else if (Character.isLetter(c)) {
/* 244 */           if (length == 0) {
/* 245 */             start = this.offset - 1;
/* 246 */             this.buffer[(length++)] = c;
/* 247 */             this.tokenType = 2;
/*     */           } else {
/* 249 */             if (this.tokenType == 1) {
/* 250 */               this.offset -= 1;
/* 251 */               this.bufferIndex -= 1;
/*     */ 
/* 254 */               break;
/*     */             }
/* 256 */             this.buffer[(length++)] = c;
/* 257 */             this.tokenType = 2;
/*     */ 
/* 259 */             if (length == 2) {
/* 260 */               this.offset -= 1;
/* 261 */               this.bufferIndex -= 1;
/* 262 */               this.preIsTokened = true;
/*     */ 
/* 264 */               break;
/*     */             }
/*     */           }
/*     */         }
/* 268 */         else if (length > 0) {
/* 269 */           if (this.preIsTokened != true)
/*     */             break;
/* 271 */           length = 0;
/* 272 */           this.preIsTokened = false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 280 */       if (length > 0) {
/* 281 */         this.termAtt.copyBuffer(this.buffer, 0, length);
/* 282 */         this.offsetAtt.setOffset(correctOffset(start), correctOffset(start + length));
/* 283 */         this.typeAtt.setType(TOKEN_TYPE_NAMES[this.tokenType]);
/* 284 */         return true;
/* 285 */       }if (this.dataLen == -1) {
/* 286 */         this.offset -= 1;
/* 287 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void end()
/*     */   {
/* 298 */     int finalOffset = correctOffset(this.offset);
/* 299 */     this.offsetAtt.setOffset(finalOffset, finalOffset);
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException
/*     */   {
/* 304 */     super.reset();
/* 305 */     this.offset = (this.bufferIndex = this.dataLen = 0);
/* 306 */     this.preIsTokened = false;
/* 307 */     this.tokenType = 0;
/*     */   }
/*     */ 
/*     */   public void reset(Reader reader) throws IOException
/*     */   {
/* 312 */     super.reset(reader);
/* 313 */     reset();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.cjk.CJKTokenizer
 * JD-Core Version:    0.6.2
 */