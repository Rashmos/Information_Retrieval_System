/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ class IndoEuropeanTokenizer extends Tokenizer
/*     */ {
/*     */   private final char[] mChars;
/*     */   private final int mLastPosition;
/*     */   private final int mStartPosition;
/*     */   private int mPosition;
/*     */   private int mTokenStart;
/*     */   private int mLastTokenIndex;
/*  34 */   private int mLastTokenStartPosition = -1;
/*  35 */   private int mLastTokenEndPosition = -1;
/*     */ 
/*     */   public IndoEuropeanTokenizer(char[] ch, int offset, int length)
/*     */   {
/*  49 */     if ((offset < 0) || (offset + length > ch.length)) {
/*  50 */       String msg = "Illegal slice. cs.length=" + ch.length + " offset=" + offset + " length=" + length;
/*     */ 
/*  54 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  56 */     this.mChars = ch;
/*  57 */     this.mPosition = offset;
/*  58 */     this.mLastPosition = (offset + length);
/*  59 */     this.mTokenStart = -1;
/*  60 */     this.mLastTokenIndex = -1;
/*  61 */     this.mStartPosition = offset;
/*     */   }
/*     */ 
/*     */   public IndoEuropeanTokenizer(String chars)
/*     */   {
/*  70 */     this(chars.toCharArray(), 0, chars.length());
/*     */   }
/*     */ 
/*     */   public IndoEuropeanTokenizer(StringBuilder chars)
/*     */   {
/*  81 */     this(chars.toString());
/*     */   }
/*     */ 
/*     */   public int lastTokenStartPosition()
/*     */   {
/*  86 */     return this.mLastTokenStartPosition;
/*     */   }
/*     */ 
/*     */   public int lastTokenEndPosition()
/*     */   {
/*  91 */     return this.mLastTokenEndPosition;
/*     */   }
/*     */ 
/*     */   public String nextWhitespace()
/*     */   {
/* 103 */     StringBuilder sb = new StringBuilder();
/*     */ 
/* 105 */     while ((hasMoreCharacters()) && (Character.isWhitespace(currentChar()))) {
/* 106 */       sb.append(currentChar());
/* 107 */       this.mPosition += 1;
/*     */     }
/* 109 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isLetter(char c)
/*     */   {
/* 123 */     return (Character.isLetter(c)) || (devanagari(c));
/*     */   }
/*     */ 
/*     */   private static boolean devanagari(char unicode)
/*     */   {
/* 135 */     return (unicode >= 'ऀ') && (unicode <= 'ॿ');
/*     */   }
/*     */ 
/*     */   public String nextToken()
/*     */   {
/* 148 */     skipWhitespace();
/* 149 */     if (!hasMoreCharacters()) return null;
/* 150 */     this.mTokenStart = this.mPosition;
/* 151 */     this.mLastTokenIndex += 1;
/* 152 */     char startChar = this.mChars[(this.mPosition++)];
/*     */ 
/* 154 */     if (startChar == '.') {
/* 155 */       while (currentCharEquals('.')) this.mPosition += 1;
/* 156 */       return currentToken();
/*     */     }
/* 158 */     if (startChar == '-') {
/* 159 */       while (currentCharEquals('-')) this.mPosition += 1;
/* 160 */       return currentToken();
/*     */     }
/* 162 */     if (startChar == '=') {
/* 163 */       while (currentCharEquals('=')) this.mPosition += 1;
/* 164 */       return currentToken();
/*     */     }
/* 166 */     if (startChar == '\'') {
/* 167 */       if (currentCharEquals('\'')) this.mPosition += 1;
/* 168 */       return currentToken();
/*     */     }
/* 170 */     if (startChar == '`') {
/* 171 */       if (currentCharEquals('`')) this.mPosition += 1;
/* 172 */       return currentToken();
/*     */     }
/* 174 */     if (isLetter(startChar)) return alphaNumToken();
/* 175 */     if (Character.isDigit(startChar)) return numToken();
/* 176 */     return currentToken();
/*     */   }
/*     */ 
/*     */   private boolean hasMoreCharacters()
/*     */   {
/* 187 */     return this.mPosition < this.mLastPosition;
/*     */   }
/*     */ 
/*     */   private char currentChar()
/*     */   {
/* 198 */     return this.mChars[this.mPosition];
/*     */   }
/*     */ 
/*     */   private boolean currentCharEquals(char c)
/*     */   {
/* 210 */     return (hasMoreCharacters()) && (currentChar() == c);
/*     */   }
/*     */ 
/*     */   private void skipWhitespace()
/*     */   {
/* 220 */     while ((hasMoreCharacters()) && (Character.isWhitespace(currentChar())))
/* 221 */       this.mPosition += 1;
/*     */   }
/*     */ 
/*     */   private String currentToken()
/*     */   {
/* 230 */     int length = this.mPosition - this.mTokenStart;
/* 231 */     this.mLastTokenStartPosition = (this.mTokenStart - this.mStartPosition);
/* 232 */     this.mLastTokenEndPosition = (this.mLastTokenStartPosition + length);
/* 233 */     return new String(this.mChars, this.mTokenStart, length);
/*     */   }
/*     */ 
/*     */   private String alphaNumToken()
/*     */   {
/* 244 */     while ((hasMoreCharacters()) && ((isLetter(currentChar())) || (Character.isDigit(currentChar()))))
/* 245 */       this.mPosition += 1;
/* 246 */     return currentToken();
/*     */   }
/*     */ 
/*     */   private String numToken()
/*     */   {
/* 258 */     while (hasMoreCharacters()) {
/* 259 */       if (isLetter(currentChar())) {
/* 260 */         this.mPosition += 1;
/* 261 */         return alphaNumToken();
/*     */       }
/* 263 */       if (Character.isDigit(currentChar())) {
/* 264 */         this.mPosition += 1;
/*     */       }
/*     */       else {
/* 267 */         if ((currentChar() == '.') || (currentChar() == ',')) {
/* 268 */           return numPunctToken();
/*     */         }
/* 270 */         return currentToken();
/*     */       }
/*     */     }
/* 272 */     return currentToken();
/*     */   }
/*     */ 
/*     */   private String numPunctToken()
/*     */   {
/* 283 */     while (hasMoreCharacters()) {
/* 284 */       if (Character.isDigit(currentChar())) {
/* 285 */         this.mPosition += 1;
/* 286 */       } else if ((currentChar() == '.') || (currentChar() == ','))
/*     */       {
/* 288 */         this.mPosition += 1;
/* 289 */         if ((!hasMoreCharacters()) || (!Character.isDigit(currentChar()))) {
/* 290 */           this.mPosition -= 1;
/* 291 */           return currentToken();
/*     */         }
/*     */       } else {
/* 294 */         return currentToken();
/*     */       }
/*     */     }
/* 297 */     return currentToken();
/*     */   }
/*     */ 
/*     */   public static String[] tokenize(String phrase)
/*     */   {
/* 307 */     return new IndoEuropeanTokenizer(phrase).tokenize();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.IndoEuropeanTokenizer
 * JD-Core Version:    0.6.2
 */