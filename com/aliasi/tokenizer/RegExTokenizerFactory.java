/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class RegExTokenizerFactory
/*     */   implements Serializable, TokenizerFactory
/*     */ {
/*     */   static final long serialVersionUID = -1668745791323535436L;
/*     */   private final Pattern mPattern;
/*     */ 
/*     */   public RegExTokenizerFactory(String regex)
/*     */   {
/* 105 */     this(Pattern.compile(regex));
/*     */   }
/*     */ 
/*     */   public RegExTokenizerFactory(String regex, int flags)
/*     */   {
/* 126 */     this(Pattern.compile(regex, flags));
/*     */   }
/*     */ 
/*     */   public RegExTokenizerFactory(Pattern pattern)
/*     */   {
/* 136 */     this.mPattern = pattern;
/*     */   }
/*     */ 
/*     */   public Pattern pattern()
/*     */   {
/* 146 */     return this.mPattern;
/*     */   }
/*     */ 
/*     */   public Tokenizer tokenizer(char[] cs, int start, int length) {
/* 150 */     return new RegExTokenizer(this.mPattern, cs, start, length);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 154 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 167 */     return "com.aliasi.tokenizer.RegExTokenizerFactory(pattern=" + pattern().pattern() + ", flags=" + pattern().flags() + ")";
/*     */   }
/*     */ 
/*     */   static class RegExTokenizer extends Tokenizer
/*     */   {
/*     */     final Matcher mMatcher;
/*     */     final char[] mCs;
/*     */     final int mStart;
/*     */     final int mLength;
/* 196 */     int mWhiteStart = 0;
/* 197 */     int mTokenStart = 0;
/* 198 */     int mTokenEnd = -1;
/* 199 */     boolean mHasNext = false;
/* 200 */     int mLastTokenStartPosition = -1;
/* 201 */     int mLastTokenEndPosition = -1;
/*     */ 
/*     */     RegExTokenizer(Pattern pattern, char[] cs, int start, int length) {
/* 204 */       this.mMatcher = pattern.matcher(CharBuffer.wrap(cs, start, length));
/* 205 */       this.mCs = cs;
/* 206 */       this.mStart = start;
/* 207 */       this.mLength = length;
/*     */     }
/*     */ 
/*     */     public String nextToken() {
/* 211 */       return hasNextToken() ? getNextToken() : null;
/*     */     }
/*     */     boolean hasNextToken() {
/* 214 */       if (this.mHasNext) return true;
/* 215 */       if (!this.mMatcher.find(this.mWhiteStart)) return false;
/* 216 */       this.mHasNext = true;
/* 217 */       this.mTokenStart = this.mMatcher.start(0);
/* 218 */       this.mTokenEnd = this.mMatcher.end(0);
/* 219 */       return true;
/*     */     }
/*     */     String getNextToken() {
/* 222 */       String token = new String(this.mCs, this.mStart + this.mTokenStart, this.mTokenEnd - this.mTokenStart);
/*     */ 
/* 224 */       this.mWhiteStart = this.mTokenEnd;
/* 225 */       this.mHasNext = false;
/* 226 */       this.mLastTokenStartPosition = this.mTokenStart;
/* 227 */       this.mLastTokenEndPosition = this.mTokenEnd;
/* 228 */       return token;
/*     */     }
/*     */ 
/*     */     public String nextWhitespace() {
/* 232 */       return new String(this.mCs, this.mStart + this.mWhiteStart, (hasNextToken() ? this.mTokenStart : this.mLength) - this.mWhiteStart);
/*     */     }
/*     */ 
/*     */     public int lastTokenStartPosition()
/*     */     {
/* 238 */       return this.mLastTokenStartPosition;
/*     */     }
/*     */ 
/*     */     public int lastTokenEndPosition() {
/* 242 */       return this.mLastTokenEndPosition;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = 7772106464245966975L;
/*     */     final RegExTokenizerFactory mFactory;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 176 */       this(null);
/*     */     }
/* 178 */     public Externalizer(RegExTokenizerFactory factory) { this.mFactory = factory; }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws ClassNotFoundException, IOException
/*     */     {
/* 183 */       return new RegExTokenizerFactory((Pattern)in.readObject());
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 187 */       objOut.writeObject(this.mFactory.mPattern);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.RegExTokenizerFactory
 * JD-Core Version:    0.6.2
 */