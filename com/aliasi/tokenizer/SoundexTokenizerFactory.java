/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ import com.aliasi.util.Strings;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SoundexTokenizerFactory extends ModifyTokenTokenizerFactory
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -7062805184862100578L;
/* 272 */   static char NON_CHAR_CODE = 'ÿ';
/* 273 */   static final char[] INITIAL_CODES = new char[256];
/* 274 */   static final char[] CODES = new char[256];
/*     */   static final boolean[] VOWELS;
/*     */ 
/*     */   public SoundexTokenizerFactory(TokenizerFactory factory)
/*     */   {
/* 175 */     super(factory);
/*     */   }
/*     */ 
/*     */   public String modifyToken(String token)
/*     */   {
/* 188 */     return soundexEncoding(token);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 192 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   public static String soundexEncoding(String token)
/*     */   {
/* 202 */     int pos = 0;
/* 203 */     while (pos < token.length()) {
/* 204 */       char c = token.charAt(pos);
/* 205 */       if ((c < 'Ā') && (INITIAL_CODES[c] != NON_CHAR_CODE))
/*     */         break;
/* 207 */       pos++;
/*     */     }
/* 209 */     if (pos == token.length()) {
/* 210 */       return "0000";
/*     */     }
/* 212 */     int csPos = 1;
/* 213 */     char[] cs = new char[4];
/* 214 */     cs[0] = INITIAL_CODES[token.charAt(pos)];
/* 215 */     char lastCode = CODES[token.charAt(pos)];
/* 216 */     pos++;
/*     */ 
/* 218 */     while ((csPos < 4) && (pos < token.length())) {
/* 219 */       char c = token.charAt(pos);
/* 220 */       pos++;
/* 221 */       if (c <= 'ÿ') {
/* 222 */         char code = CODES[c];
/* 223 */         if (code == NON_CHAR_CODE) {
/* 224 */           if (VOWELS[c] != 0) {
/* 225 */             lastCode = '7';
/*     */           }
/*     */         }
/* 228 */         else if (code != lastCode) {
/* 229 */           cs[csPos] = code;
/* 230 */           lastCode = code;
/* 231 */           csPos++;
/*     */         }
/*     */       }
/*     */     }
/* 234 */     while (csPos < 4) {
/* 235 */       cs[csPos] = '0';
/* 236 */       csPos++;
/*     */     }
/* 238 */     return new String(cs);
/*     */   }
/*     */ 
/*     */   static char soundexCode(char upperCaseLetter) {
/* 242 */     switch (upperCaseLetter) { case 'B':
/* 243 */       return '1';
/*     */     case 'F':
/* 244 */       return '1';
/*     */     case 'P':
/* 245 */       return '1';
/*     */     case 'V':
/* 246 */       return '1';
/*     */     case 'C':
/* 248 */       return '2';
/*     */     case 'G':
/* 249 */       return '2';
/*     */     case 'J':
/* 250 */       return '2';
/*     */     case 'K':
/* 251 */       return '2';
/*     */     case 'Q':
/* 252 */       return '2';
/*     */     case 'S':
/* 253 */       return '2';
/*     */     case 'X':
/* 254 */       return '2';
/*     */     case 'Z':
/* 255 */       return '2';
/*     */     case 'D':
/* 257 */       return '3';
/*     */     case 'T':
/* 258 */       return '3';
/*     */     case 'L':
/* 260 */       return '4';
/*     */     case 'M':
/* 262 */       return '5';
/*     */     case 'N':
/* 263 */       return '5';
/*     */     case 'R':
/* 265 */       return '6';
/*     */     case 'E':
/*     */     case 'H':
/*     */     case 'I':
/*     */     case 'O':
/*     */     case 'U':
/*     */     case 'W':
/* 268 */     case 'Y': } return NON_CHAR_CODE;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 276 */     for (int i = 0; i < 256; i++) {
/* 277 */       char c = (char)i;
/* 278 */       if (!Character.isLetter(c)) {
/* 279 */         INITIAL_CODES[i] = NON_CHAR_CODE;
/* 280 */         CODES[i] = NON_CHAR_CODE;
/*     */       } else {
/* 282 */         INITIAL_CODES[i] = Character.toUpperCase(Strings.deAccentLatin1(c));
/*     */ 
/* 285 */         CODES[i] = soundexCode(INITIAL_CODES[i]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 290 */     VOWELS = new boolean[256];
/*     */ 
/* 292 */     for (int i = 0; i < 256; i++) {
/* 293 */       char initCode = INITIAL_CODES[i];
/* 294 */       VOWELS[i] = ((initCode == 'A') || (initCode == 'E') || (initCode == 'I') || (initCode == 'O') || (initCode == 'U') ? 1 : false);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Serializer extends ModifiedTokenizerFactory.AbstractSerializer<SoundexTokenizerFactory>
/*     */   {
/*     */     static final long serialVersionUID = 2496844521092643488L;
/*     */ 
/*     */     public Serializer(SoundexTokenizerFactory factory)
/*     */     {
/* 306 */       super();
/*     */     }
/*     */     public Serializer() {
/* 309 */       this(null);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in, TokenizerFactory baseFactory) {
/* 313 */       return new SoundexTokenizerFactory(baseFactory);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.SoundexTokenizerFactory
 * JD-Core Version:    0.6.2
 */