/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Tokenization
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3806073293589459401L;
/*     */   private final String mText;
/*     */   private final List<String> mTokens;
/*     */   private final List<String> mWhitespaces;
/*     */   private final int[] mTokenStarts;
/*     */   private final int[] mTokenEnds;
/*     */ 
/*     */   public Tokenization(char[] cs, int start, int length, TokenizerFactory factory)
/*     */   {
/*  85 */     this(new String(cs, start, length), factory.tokenizer(cs, start, length));
/*     */   }
/*     */ 
/*     */   public Tokenization(String text, TokenizerFactory factory)
/*     */   {
/*  97 */     this(text, factory.tokenizer(text.toCharArray(), 0, text.length()));
/*     */   }
/*     */ 
/*     */   public Tokenization(String text, List<String> tokens, List<String> whitespaces, int[] tokenStarts, int[] tokenEnds)
/*     */   {
/* 120 */     this(text, new ArrayList(tokens), new ArrayList(whitespaces), (int[])tokenStarts.clone(), (int[])tokenEnds.clone(), false);
/*     */ 
/* 126 */     if (tokens.size() != whitespaces.size() - 1) {
/* 127 */       String msg = "Require one more whitespace than token. Found tokens.size()=" + tokens.size() + " whitespaces.size()=" + whitespaces.size();
/*     */ 
/* 130 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 132 */     if (tokenStarts.length != tokens.size()) {
/* 133 */       String msg = "Require token starts to be same length as tokens Found tokenStarts.length=" + tokenStarts.length + " tokenEnds.length=" + tokenEnds.length;
/*     */ 
/* 136 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 138 */     if (tokenEnds.length != tokens.size()) {
/* 139 */       String msg = "Require token starts to be same length as tokens Found tokenEnds.length=" + tokenEnds.length + " tokenEnds.length=" + tokenEnds.length;
/*     */ 
/* 142 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 144 */     for (int i = 0; i < tokenStarts.length; i++) {
/* 145 */       if (tokenStarts[i] < 0) {
/* 146 */         String msg = "Token starts must be non-negative. Found tokenStarts[" + i + "]=" + tokenStarts[i];
/*     */ 
/* 148 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 150 */       if (tokenEnds[i] > text.length()) {
/* 151 */         String msg = "Token ends must be less than or equal to text length. Found tokenEnds[" + i + "]=" + tokenEnds[i] + " text.length()=" + text.length();
/*     */ 
/* 154 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 156 */       if (tokenStarts[i] > tokenEnds[i]) {
/* 157 */         String msg = "Token starts must be less than or equal to ends. Found tokenStarts[" + i + "]=" + tokenStarts[i] + " tokenEnds[" + i + "]=" + tokenEnds[i];
/*     */ 
/* 160 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   Tokenization(String text, List<String> tokens, List<String> whitespaces, int[] tokenStarts, int[] tokenEnds, boolean ignore)
/*     */   {
/* 171 */     this.mText = text;
/* 172 */     this.mTokens = tokens;
/* 173 */     this.mWhitespaces = whitespaces;
/* 174 */     this.mTokenStarts = tokenStarts;
/* 175 */     this.mTokenEnds = tokenEnds;
/*     */   }
/*     */ 
/*     */   Tokenization(String text, Tokenizer tokenizer) {
/* 179 */     this.mText = text;
/* 180 */     List tokens = new ArrayList();
/* 181 */     List whitespaces = new ArrayList();
/* 182 */     List starts = new ArrayList();
/* 183 */     List ends = new ArrayList();
/*     */ 
/* 185 */     whitespaces.add(tokenizer.nextWhitespace());
/*     */     String token;
/* 186 */     while ((token = tokenizer.nextToken()) != null) {
/* 187 */       tokens.add(token);
/* 188 */       whitespaces.add(tokenizer.nextWhitespace());
/* 189 */       starts.add(Integer.valueOf(tokenizer.lastTokenStartPosition()));
/* 190 */       ends.add(Integer.valueOf(tokenizer.lastTokenEndPosition()));
/*     */     }
/* 192 */     this.mTokens = tokens;
/* 193 */     this.mWhitespaces = whitespaces;
/* 194 */     this.mTokenStarts = new int[starts.size()];
/* 195 */     this.mTokenEnds = new int[starts.size()];
/* 196 */     for (int i = 0; i < starts.size(); i++) {
/* 197 */       this.mTokenStarts[i] = ((Integer)starts.get(i)).intValue();
/* 198 */       this.mTokenEnds[i] = ((Integer)ends.get(i)).intValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String text()
/*     */   {
/* 208 */     return this.mText;
/*     */   }
/*     */ 
/*     */   public int numTokens()
/*     */   {
/* 217 */     return this.mTokens.size();
/*     */   }
/*     */ 
/*     */   public String token(int n)
/*     */   {
/* 229 */     return (String)this.mTokens.get(n);
/*     */   }
/*     */ 
/*     */   public String whitespace(int n)
/*     */   {
/* 243 */     return (String)this.mWhitespaces.get(n);
/*     */   }
/*     */ 
/*     */   public int tokenStart(int n)
/*     */   {
/* 257 */     return this.mTokenStarts[n];
/*     */   }
/*     */ 
/*     */   public int tokenEnd(int n)
/*     */   {
/* 271 */     return this.mTokenEnds[n];
/*     */   }
/*     */ 
/*     */   public String[] tokens()
/*     */   {
/* 285 */     return (String[])this.mTokens.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */   }
/*     */ 
/*     */   public String[] whitespaces()
/*     */   {
/* 299 */     return (String[])this.mWhitespaces.toArray(Strings.EMPTY_STRING_ARRAY);
/*     */   }
/*     */ 
/*     */   public List<String> tokenList()
/*     */   {
/* 309 */     return Collections.unmodifiableList(this.mTokens);
/*     */   }
/*     */ 
/*     */   public List<String> whitespaceList()
/*     */   {
/* 319 */     return Collections.unmodifiableList(this.mWhitespaces);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object that)
/*     */   {
/* 329 */     if (this == that)
/* 330 */       return true;
/* 331 */     if (!(that instanceof Tokenization))
/* 332 */       return false;
/* 333 */     Tokenization thatT = (Tokenization)that;
/* 334 */     if (!text().equals(thatT.text()))
/* 335 */       return false;
/* 336 */     if (numTokens() != thatT.numTokens())
/* 337 */       return false;
/* 338 */     for (int n = 0; n < numTokens(); n++) {
/* 339 */       if (!token(n).equals(thatT.token(n)))
/* 340 */         return false;
/* 341 */       if (!whitespace(n).equals(thatT.whitespace(n)))
/* 342 */         return false;
/* 343 */       if (tokenStart(n) != thatT.tokenStart(n))
/* 344 */         return false;
/* 345 */       if (tokenEnd(n) != thatT.tokenEnd(n))
/* 346 */         return false;
/*     */     }
/* 348 */     if (!whitespace(numTokens()).equals(thatT.whitespace(numTokens())))
/* 349 */       return false;
/* 350 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 362 */     return 31 * this.mText.hashCode() + this.mTokens.size();
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 366 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 5248361056143805108L;
/*     */     Tokenization mToks;
/*     */ 
/* 373 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(Tokenization toks) {
/* 376 */       this.mToks = toks;
/*     */     }
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 379 */       out.writeUTF(this.mToks.mText);
/* 380 */       out.writeInt(this.mToks.mTokens.size());
/* 381 */       for (String token : this.mToks.mTokens)
/* 382 */         out.writeUTF(token);
/* 383 */       for (String whitespace : this.mToks.mWhitespaces)
/* 384 */         out.writeUTF(whitespace);
/* 385 */       writeInts(this.mToks.mTokenStarts, out);
/* 386 */       writeInts(this.mToks.mTokenEnds, out);
/*     */     }
/*     */     public Object read(ObjectInput in) throws IOException {
/* 389 */       String text = in.readUTF();
/* 390 */       int len = in.readInt();
/* 391 */       List tokens = new ArrayList(len);
/* 392 */       for (int i = 0; i < len; i++)
/* 393 */         tokens.add(in.readUTF());
/* 394 */       List whitespaces = new ArrayList(len + 1);
/* 395 */       for (int i = 0; i <= len; i++)
/* 396 */         whitespaces.add(in.readUTF());
/* 397 */       int[] tokenStarts = readInts(in);
/* 398 */       int[] tokenEnds = readInts(in);
/* 399 */       boolean ignoreMe = true;
/* 400 */       return new Tokenization(text, tokens, whitespaces, tokenStarts, tokenEnds, ignoreMe);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.Tokenization
 * JD-Core Version:    0.6.2
 */