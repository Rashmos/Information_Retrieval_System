/*     */ package org.apache.lucene.analysis.util;
/*     */ 
/*     */ public class OpenStringBuilder
/*     */   implements Appendable, CharSequence
/*     */ {
/*     */   protected char[] buf;
/*     */   protected int len;
/*     */ 
/*     */   public OpenStringBuilder()
/*     */   {
/*  28 */     this(32);
/*     */   }
/*     */ 
/*     */   public OpenStringBuilder(int size) {
/*  32 */     this.buf = new char[size];
/*     */   }
/*     */ 
/*     */   public OpenStringBuilder(char[] arr, int len) {
/*  36 */     set(arr, len);
/*     */   }
/*     */   public void setLength(int len) {
/*  39 */     this.len = len;
/*     */   }
/*     */   public void set(char[] arr, int end) {
/*  42 */     this.buf = arr;
/*  43 */     this.len = end;
/*     */   }
/*     */   public char[] getArray() {
/*  46 */     return this.buf; } 
/*  47 */   public int size() { return this.len; } 
/*  48 */   public int length() { return this.len; } 
/*  49 */   public int capacity() { return this.buf.length; }
/*     */ 
/*     */   public Appendable append(CharSequence csq) {
/*  52 */     return append(csq, 0, csq.length());
/*     */   }
/*     */ 
/*     */   public Appendable append(CharSequence csq, int start, int end) {
/*  56 */     reserve(end - start);
/*  57 */     for (int i = start; i < end; i++) {
/*  58 */       unsafeWrite(csq.charAt(i));
/*     */     }
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */   public Appendable append(char c) {
/*  64 */     write(c);
/*  65 */     return this;
/*     */   }
/*     */ 
/*     */   public char charAt(int index) {
/*  69 */     return this.buf[index];
/*     */   }
/*     */ 
/*     */   public void setCharAt(int index, char ch) {
/*  73 */     this.buf[index] = ch;
/*     */   }
/*     */ 
/*     */   public CharSequence subSequence(int start, int end) {
/*  77 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void unsafeWrite(char b) {
/*  81 */     this.buf[(this.len++)] = b;
/*     */   }
/*     */   public void unsafeWrite(int b) {
/*  84 */     unsafeWrite((char)b);
/*     */   }
/*     */   public void unsafeWrite(char[] b, int off, int len) {
/*  87 */     System.arraycopy(b, off, this.buf, this.len, len);
/*  88 */     this.len += len;
/*     */   }
/*     */ 
/*     */   protected void resize(int len) {
/*  92 */     char[] newbuf = new char[Math.max(this.buf.length << 1, len)];
/*  93 */     System.arraycopy(this.buf, 0, newbuf, 0, size());
/*  94 */     this.buf = newbuf;
/*     */   }
/*     */ 
/*     */   public void reserve(int num) {
/*  98 */     if (this.len + num > this.buf.length) resize(this.len + num); 
/*     */   }
/*     */ 
/*     */   public void write(char b)
/*     */   {
/* 102 */     if (this.len >= this.buf.length) {
/* 103 */       resize(this.len + 1);
/*     */     }
/* 105 */     unsafeWrite(b);
/*     */   }
/*     */   public void write(int b) {
/* 108 */     write((char)b);
/*     */   }
/*     */   public final void write(char[] b) {
/* 111 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   public void write(char[] b, int off, int len) {
/* 115 */     reserve(len);
/* 116 */     unsafeWrite(b, off, len);
/*     */   }
/*     */ 
/*     */   public final void write(OpenStringBuilder arr) {
/* 120 */     write(arr.buf, 0, this.len);
/*     */   }
/*     */ 
/*     */   public void write(String s) {
/* 124 */     reserve(s.length());
/* 125 */     s.getChars(0, s.length(), this.buf, this.len);
/* 126 */     this.len += s.length();
/*     */   }
/*     */ 
/*     */   public void flush() {
/*     */   }
/*     */ 
/*     */   public final void reset() {
/* 133 */     this.len = 0;
/*     */   }
/*     */ 
/*     */   public char[] toCharArray() {
/* 137 */     char[] newbuf = new char[size()];
/* 138 */     System.arraycopy(this.buf, 0, newbuf, 0, size());
/* 139 */     return newbuf;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 143 */     return new String(this.buf, 0, size());
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.util.OpenStringBuilder
 * JD-Core Version:    0.6.2
 */