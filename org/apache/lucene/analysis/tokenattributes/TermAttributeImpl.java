/*     */ package org.apache.lucene.analysis.tokenattributes;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ import org.apache.lucene.util.AttributeImpl;
/*     */ 
/*     */ public class TermAttributeImpl extends AttributeImpl
/*     */   implements TermAttribute, Cloneable, Serializable
/*     */ {
/*  29 */   private static int MIN_BUFFER_SIZE = 10;
/*     */   private char[] termBuffer;
/*     */   private int termLength;
/*     */ 
/*     */   public String term()
/*     */   {
/*  44 */     initTermBuffer();
/*  45 */     return new String(this.termBuffer, 0, this.termLength);
/*     */   }
/*     */ 
/*     */   public void setTermBuffer(char[] buffer, int offset, int length)
/*     */   {
/*  55 */     growTermBuffer(length);
/*  56 */     System.arraycopy(buffer, offset, this.termBuffer, 0, length);
/*  57 */     this.termLength = length;
/*     */   }
/*     */ 
/*     */   public void setTermBuffer(String buffer)
/*     */   {
/*  64 */     int length = buffer.length();
/*  65 */     growTermBuffer(length);
/*  66 */     buffer.getChars(0, length, this.termBuffer, 0);
/*  67 */     this.termLength = length;
/*     */   }
/*     */ 
/*     */   public void setTermBuffer(String buffer, int offset, int length)
/*     */   {
/*  77 */     assert (offset <= buffer.length());
/*  78 */     assert (offset + length <= buffer.length());
/*  79 */     growTermBuffer(length);
/*  80 */     buffer.getChars(offset, offset + length, this.termBuffer, 0);
/*  81 */     this.termLength = length;
/*     */   }
/*     */ 
/*     */   public char[] termBuffer()
/*     */   {
/*  92 */     initTermBuffer();
/*  93 */     return this.termBuffer;
/*     */   }
/*     */ 
/*     */   public char[] resizeTermBuffer(int newSize)
/*     */   {
/* 107 */     if (this.termBuffer == null)
/*     */     {
/* 109 */       this.termBuffer = new char[ArrayUtil.getNextSize(newSize < MIN_BUFFER_SIZE ? MIN_BUFFER_SIZE : newSize)];
/*     */     }
/* 111 */     else if (this.termBuffer.length < newSize)
/*     */     {
/* 114 */       char[] newCharBuffer = new char[ArrayUtil.getNextSize(newSize)];
/* 115 */       System.arraycopy(this.termBuffer, 0, newCharBuffer, 0, this.termBuffer.length);
/* 116 */       this.termBuffer = newCharBuffer;
/*     */     }
/*     */ 
/* 119 */     return this.termBuffer;
/*     */   }
/*     */ 
/*     */   private void growTermBuffer(int newSize)
/*     */   {
/* 128 */     if (this.termBuffer == null)
/*     */     {
/* 130 */       this.termBuffer = new char[ArrayUtil.getNextSize(newSize < MIN_BUFFER_SIZE ? MIN_BUFFER_SIZE : newSize)];
/*     */     }
/* 132 */     else if (this.termBuffer.length < newSize)
/*     */     {
/* 135 */       this.termBuffer = new char[ArrayUtil.getNextSize(newSize)];
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initTermBuffer()
/*     */   {
/* 141 */     if (this.termBuffer == null) {
/* 142 */       this.termBuffer = new char[ArrayUtil.getNextSize(MIN_BUFFER_SIZE)];
/* 143 */       this.termLength = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int termLength()
/*     */   {
/* 150 */     return this.termLength;
/*     */   }
/*     */ 
/*     */   public void setTermLength(int length)
/*     */   {
/* 161 */     initTermBuffer();
/* 162 */     if (length > this.termBuffer.length)
/* 163 */       throw new IllegalArgumentException("length " + length + " exceeds the size of the termBuffer (" + this.termBuffer.length + ")");
/* 164 */     this.termLength = length;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 169 */     initTermBuffer();
/* 170 */     int code = this.termLength;
/* 171 */     code = code * 31 + ArrayUtil.hashCode(this.termBuffer, 0, this.termLength);
/* 172 */     return code;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 177 */     this.termLength = 0;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 182 */     TermAttributeImpl t = (TermAttributeImpl)super.clone();
/*     */ 
/* 184 */     if (this.termBuffer != null) {
/* 185 */       t.termBuffer = ((char[])this.termBuffer.clone());
/*     */     }
/* 187 */     return t;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 192 */     if (other == this) {
/* 193 */       return true;
/*     */     }
/*     */ 
/* 196 */     if ((other instanceof Serializable)) {
/* 197 */       initTermBuffer();
/* 198 */       TermAttributeImpl o = (TermAttributeImpl)other;
/* 199 */       o.initTermBuffer();
/*     */ 
/* 201 */       if (this.termLength != o.termLength)
/* 202 */         return false;
/* 203 */       for (int i = 0; i < this.termLength; i++) {
/* 204 */         if (this.termBuffer[i] != o.termBuffer[i]) {
/* 205 */           return false;
/*     */         }
/*     */       }
/* 208 */       return true;
/*     */     }
/*     */ 
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 216 */     initTermBuffer();
/* 217 */     return "term=" + new String(this.termBuffer, 0, this.termLength);
/*     */   }
/*     */ 
/*     */   public void copyTo(AttributeImpl target)
/*     */   {
/* 222 */     initTermBuffer();
/* 223 */     TermAttribute t = (Serializable)target;
/* 224 */     t.setTermBuffer(this.termBuffer, 0, this.termLength);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.tokenattributes.TermAttributeImpl
 * JD-Core Version:    0.6.2
 */