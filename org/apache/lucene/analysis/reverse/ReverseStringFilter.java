/*     */ package org.apache.lucene.analysis.reverse;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.TokenFilter;
/*     */ import org.apache.lucene.analysis.TokenStream;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public final class ReverseStringFilter extends TokenFilter
/*     */ {
/*  45 */   private final CharTermAttribute termAtt = (CharTermAttribute)addAttribute(CharTermAttribute.class);
/*     */   private final char marker;
/*     */   private final Version matchVersion;
/*     */   private static final char NOMARKER = '￿';
/*     */   public static final char START_OF_HEADING_MARKER = '\001';
/*     */   public static final char INFORMATION_SEPARATOR_MARKER = '\037';
/*     */   public static final char PUA_EC00_MARKER = '';
/*     */   public static final char RTL_DIRECTION_MARKER = '‏';
/*     */ 
/*     */   @Deprecated
/*     */   public ReverseStringFilter(TokenStream in)
/*     */   {
/*  83 */     this(in, 65535);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public ReverseStringFilter(TokenStream in, char marker)
/*     */   {
/* 101 */     this(Version.LUCENE_30, in, marker);
/*     */   }
/*     */ 
/*     */   public ReverseStringFilter(Version matchVersion, TokenStream in)
/*     */   {
/* 115 */     this(matchVersion, in, 65535);
/*     */   }
/*     */ 
/*     */   public ReverseStringFilter(Version matchVersion, TokenStream in, char marker)
/*     */   {
/* 131 */     super(in);
/* 132 */     this.matchVersion = matchVersion;
/* 133 */     this.marker = marker;
/*     */   }
/*     */ 
/*     */   public boolean incrementToken() throws IOException
/*     */   {
/* 138 */     if (this.input.incrementToken()) {
/* 139 */       int len = this.termAtt.length();
/* 140 */       if (this.marker != 65535) {
/* 141 */         len++;
/* 142 */         this.termAtt.resizeBuffer(len);
/* 143 */         this.termAtt.buffer()[(len - 1)] = this.marker;
/*     */       }
/* 145 */       reverse(this.matchVersion, this.termAtt.buffer(), 0, len);
/* 146 */       this.termAtt.setLength(len);
/* 147 */       return true;
/*     */     }
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static String reverse(String input)
/*     */   {
/* 163 */     return reverse(Version.LUCENE_30, input);
/*     */   }
/*     */ 
/*     */   public static String reverse(Version matchVersion, String input)
/*     */   {
/* 174 */     char[] charInput = input.toCharArray();
/* 175 */     reverse(matchVersion, charInput, 0, charInput.length);
/* 176 */     return new String(charInput);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void reverse(char[] buffer)
/*     */   {
/* 187 */     reverse(buffer, 0, buffer.length);
/*     */   }
/*     */ 
/*     */   public static void reverse(Version matchVersion, char[] buffer)
/*     */   {
/* 196 */     reverse(matchVersion, buffer, 0, buffer.length);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void reverse(char[] buffer, int len)
/*     */   {
/* 210 */     reverse(buffer, 0, len);
/*     */   }
/*     */ 
/*     */   public static void reverse(Version matchVersion, char[] buffer, int len)
/*     */   {
/* 223 */     reverse(matchVersion, buffer, 0, len);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static void reverse(char[] buffer, int start, int len)
/*     */   {
/* 238 */     reverseUnicode3(buffer, start, len);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   private static void reverseUnicode3(char[] buffer, int start, int len)
/*     */   {
/* 246 */     if (len <= 1) return;
/* 247 */     int num = len >> 1;
/* 248 */     for (int i = start; i < start + num; i++) {
/* 249 */       char c = buffer[i];
/* 250 */       buffer[i] = buffer[(start * 2 + len - i - 1)];
/* 251 */       buffer[(start * 2 + len - i - 1)] = c;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void reverse(Version matchVersion, char[] buffer, int start, int len)
/*     */   {
/* 266 */     if (!matchVersion.onOrAfter(Version.LUCENE_31)) {
/* 267 */       reverseUnicode3(buffer, start, len);
/* 268 */       return;
/*     */     }
/*     */ 
/* 271 */     if (len < 2)
/* 272 */       return;
/* 273 */     int end = start + len - 1;
/* 274 */     char frontHigh = buffer[start];
/* 275 */     char endLow = buffer[end];
/* 276 */     boolean allowFrontSur = true; boolean allowEndSur = true;
/* 277 */     int mid = start + (len >> 1);
/* 278 */     for (int i = start; i < mid; end--) {
/* 279 */       char frontLow = buffer[(i + 1)];
/* 280 */       char endHigh = buffer[(end - 1)];
/* 281 */       boolean surAtFront = (allowFrontSur) && (Character.isSurrogatePair(frontHigh, frontLow));
/*     */ 
/* 283 */       if ((surAtFront) && (len < 3))
/*     */       {
/* 285 */         return;
/*     */       }
/* 287 */       boolean surAtEnd = (allowEndSur) && (Character.isSurrogatePair(endHigh, endLow));
/*     */ 
/* 289 */       allowFrontSur = allowEndSur = 1;
/* 290 */       if (surAtFront == surAtEnd) {
/* 291 */         if (surAtFront)
/*     */         {
/* 293 */           buffer[end] = frontLow;
/* 294 */           buffer[(--end)] = frontHigh;
/* 295 */           buffer[i] = endHigh;
/* 296 */           buffer[(++i)] = endLow;
/* 297 */           frontHigh = buffer[(i + 1)];
/* 298 */           endLow = buffer[(end - 1)];
/*     */         }
/*     */         else {
/* 301 */           buffer[end] = frontHigh;
/* 302 */           buffer[i] = endLow;
/* 303 */           frontHigh = frontLow;
/* 304 */           endLow = endHigh;
/*     */         }
/*     */       }
/* 307 */       else if (surAtFront)
/*     */       {
/* 309 */         buffer[end] = frontLow;
/* 310 */         buffer[i] = endLow;
/* 311 */         endLow = endHigh;
/* 312 */         allowFrontSur = false;
/*     */       }
/*     */       else {
/* 315 */         buffer[end] = frontHigh;
/* 316 */         buffer[i] = endHigh;
/* 317 */         frontHigh = frontLow;
/* 318 */         allowEndSur = false;
/*     */       }
/* 278 */       i++;
/*     */     }
/*     */ 
/* 322 */     if (((len & 0x1) == 1) && ((!allowFrontSur) || (!allowEndSur)))
/*     */     {
/* 324 */       buffer[end] = (allowFrontSur ? endLow : frontHigh);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.reverse.ReverseStringFilter
 * JD-Core Version:    0.6.2
 */