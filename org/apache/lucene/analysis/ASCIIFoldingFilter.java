/*     */ package org.apache.lucene.analysis;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ 
/*     */ public final class ASCIIFoldingFilter extends TokenFilter
/*     */ {
/*  66 */   private char[] output = new char[512];
/*     */   private int outputPos;
/*     */   private TermAttribute termAtt;
/*     */ 
/*     */   public ASCIIFoldingFilter(TokenStream input)
/*     */   {
/*  62 */     super(input);
/*  63 */     this.termAtt = ((TermAttribute)addAttribute(TermAttribute.class));
/*     */   }
/*     */ 
/*     */   public boolean incrementToken()
/*     */     throws IOException
/*     */   {
/*  72 */     if (this.input.incrementToken()) {
/*  73 */       char[] buffer = this.termAtt.termBuffer();
/*  74 */       int length = this.termAtt.termLength();
/*     */ 
/*  78 */       for (int i = 0; i < length; i++) {
/*  79 */         char c = buffer[i];
/*  80 */         if (c >= '')
/*     */         {
/*  82 */           foldToASCII(buffer, length);
/*  83 */           this.termAtt.setTermBuffer(this.output, 0, this.outputPos);
/*  84 */           break;
/*     */         }
/*     */       }
/*  87 */       return true;
/*     */     }
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   public void foldToASCII(char[] input, int length)
/*     */   {
/* 102 */     int maxSizeNeeded = 4 * length;
/* 103 */     if (this.output.length < maxSizeNeeded) {
/* 104 */       this.output = new char[ArrayUtil.getNextSize(maxSizeNeeded)];
/*     */     }
/*     */ 
/* 107 */     this.outputPos = 0;
/*     */ 
/* 109 */     for (int pos = 0; pos < length; pos++) {
/* 110 */       char c = input[pos];
/*     */ 
/* 113 */       if (c < '')
/* 114 */         this.output[(this.outputPos++)] = c;
/*     */       else
/* 116 */         switch (c) {
/*     */         case 'À':
/*     */         case 'Á':
/*     */         case 'Â':
/*     */         case 'Ã':
/*     */         case 'Ä':
/*     */         case 'Å':
/*     */         case 'Ā':
/*     */         case 'Ă':
/*     */         case 'Ą':
/*     */         case 'Ə':
/*     */         case 'Ǎ':
/*     */         case 'Ǟ':
/*     */         case 'Ǡ':
/*     */         case 'Ǻ':
/*     */         case 'Ȁ':
/*     */         case 'Ȃ':
/*     */         case 'Ȧ':
/*     */         case 'Ⱥ':
/*     */         case 'ᴀ':
/*     */         case 'Ḁ':
/*     */         case 'Ạ':
/*     */         case 'Ả':
/*     */         case 'Ấ':
/*     */         case 'Ầ':
/*     */         case 'Ẩ':
/*     */         case 'Ẫ':
/*     */         case 'Ậ':
/*     */         case 'Ắ':
/*     */         case 'Ằ':
/*     */         case 'Ẳ':
/*     */         case 'Ẵ':
/*     */         case 'Ặ':
/*     */         case 'Ⓐ':
/*     */         case 'Ａ':
/* 151 */           this.output[(this.outputPos++)] = 'A';
/* 152 */           break;
/*     */         case 'à':
/*     */         case 'á':
/*     */         case 'â':
/*     */         case 'ã':
/*     */         case 'ä':
/*     */         case 'å':
/*     */         case 'ā':
/*     */         case 'ă':
/*     */         case 'ą':
/*     */         case 'ǎ':
/*     */         case 'ǟ':
/*     */         case 'ǡ':
/*     */         case 'ǻ':
/*     */         case 'ȁ':
/*     */         case 'ȃ':
/*     */         case 'ȧ':
/*     */         case 'ɐ':
/*     */         case 'ə':
/*     */         case 'ɚ':
/*     */         case 'ᶏ':
/*     */         case 'ᶕ':
/*     */         case 'ḁ':
/*     */         case 'ẚ':
/*     */         case 'ạ':
/*     */         case 'ả':
/*     */         case 'ấ':
/*     */         case 'ầ':
/*     */         case 'ẩ':
/*     */         case 'ẫ':
/*     */         case 'ậ':
/*     */         case 'ắ':
/*     */         case 'ằ':
/*     */         case 'ẳ':
/*     */         case 'ẵ':
/*     */         case 'ặ':
/*     */         case 'ₐ':
/*     */         case 'ₔ':
/*     */         case 'ⓐ':
/*     */         case 'ⱥ':
/*     */         case 'Ɐ':
/*     */         case 'ａ':
/* 194 */           this.output[(this.outputPos++)] = 'a';
/* 195 */           break;
/*     */         case 'Ꜳ':
/* 197 */           this.output[(this.outputPos++)] = 'A';
/* 198 */           this.output[(this.outputPos++)] = 'A';
/* 199 */           break;
/*     */         case 'Æ':
/*     */         case 'Ǣ':
/*     */         case 'Ǽ':
/*     */         case 'ᴁ':
/* 204 */           this.output[(this.outputPos++)] = 'A';
/* 205 */           this.output[(this.outputPos++)] = 'E';
/* 206 */           break;
/*     */         case 'Ꜵ':
/* 208 */           this.output[(this.outputPos++)] = 'A';
/* 209 */           this.output[(this.outputPos++)] = 'O';
/* 210 */           break;
/*     */         case 'Ꜷ':
/* 212 */           this.output[(this.outputPos++)] = 'A';
/* 213 */           this.output[(this.outputPos++)] = 'U';
/* 214 */           break;
/*     */         case 'Ꜹ':
/*     */         case 'Ꜻ':
/* 217 */           this.output[(this.outputPos++)] = 'A';
/* 218 */           this.output[(this.outputPos++)] = 'V';
/* 219 */           break;
/*     */         case 'Ꜽ':
/* 221 */           this.output[(this.outputPos++)] = 'A';
/* 222 */           this.output[(this.outputPos++)] = 'Y';
/* 223 */           break;
/*     */         case '⒜':
/* 225 */           this.output[(this.outputPos++)] = '(';
/* 226 */           this.output[(this.outputPos++)] = 'a';
/* 227 */           this.output[(this.outputPos++)] = ')';
/* 228 */           break;
/*     */         case 'ꜳ':
/* 230 */           this.output[(this.outputPos++)] = 'a';
/* 231 */           this.output[(this.outputPos++)] = 'a';
/* 232 */           break;
/*     */         case 'æ':
/*     */         case 'ǣ':
/*     */         case 'ǽ':
/*     */         case 'ᴂ':
/* 237 */           this.output[(this.outputPos++)] = 'a';
/* 238 */           this.output[(this.outputPos++)] = 'e';
/* 239 */           break;
/*     */         case 'ꜵ':
/* 241 */           this.output[(this.outputPos++)] = 'a';
/* 242 */           this.output[(this.outputPos++)] = 'o';
/* 243 */           break;
/*     */         case 'ꜷ':
/* 245 */           this.output[(this.outputPos++)] = 'a';
/* 246 */           this.output[(this.outputPos++)] = 'u';
/* 247 */           break;
/*     */         case 'ꜹ':
/*     */         case 'ꜻ':
/* 250 */           this.output[(this.outputPos++)] = 'a';
/* 251 */           this.output[(this.outputPos++)] = 'v';
/* 252 */           break;
/*     */         case 'ꜽ':
/* 254 */           this.output[(this.outputPos++)] = 'a';
/* 255 */           this.output[(this.outputPos++)] = 'y';
/* 256 */           break;
/*     */         case 'Ɓ':
/*     */         case 'Ƃ':
/*     */         case 'Ƀ':
/*     */         case 'ʙ':
/*     */         case 'ᴃ':
/*     */         case 'Ḃ':
/*     */         case 'Ḅ':
/*     */         case 'Ḇ':
/*     */         case 'Ⓑ':
/*     */         case 'Ｂ':
/* 267 */           this.output[(this.outputPos++)] = 'B';
/* 268 */           break;
/*     */         case 'ƀ':
/*     */         case 'ƃ':
/*     */         case 'ɓ':
/*     */         case 'ᵬ':
/*     */         case 'ᶀ':
/*     */         case 'ḃ':
/*     */         case 'ḅ':
/*     */         case 'ḇ':
/*     */         case 'ⓑ':
/*     */         case 'ｂ':
/* 279 */           this.output[(this.outputPos++)] = 'b';
/* 280 */           break;
/*     */         case '⒝':
/* 282 */           this.output[(this.outputPos++)] = '(';
/* 283 */           this.output[(this.outputPos++)] = 'b';
/* 284 */           this.output[(this.outputPos++)] = ')';
/* 285 */           break;
/*     */         case 'Ç':
/*     */         case 'Ć':
/*     */         case 'Ĉ':
/*     */         case 'Ċ':
/*     */         case 'Č':
/*     */         case 'Ƈ':
/*     */         case 'Ȼ':
/*     */         case 'ʗ':
/*     */         case 'ᴄ':
/*     */         case 'Ḉ':
/*     */         case 'Ⓒ':
/*     */         case 'Ｃ':
/* 298 */           this.output[(this.outputPos++)] = 'C';
/* 299 */           break;
/*     */         case 'ç':
/*     */         case 'ć':
/*     */         case 'ĉ':
/*     */         case 'ċ':
/*     */         case 'č':
/*     */         case 'ƈ':
/*     */         case 'ȼ':
/*     */         case 'ɕ':
/*     */         case 'ḉ':
/*     */         case 'ↄ':
/*     */         case 'ⓒ':
/*     */         case 'Ꜿ':
/*     */         case 'ꜿ':
/*     */         case 'ｃ':
/* 314 */           this.output[(this.outputPos++)] = 'c';
/* 315 */           break;
/*     */         case '⒞':
/* 317 */           this.output[(this.outputPos++)] = '(';
/* 318 */           this.output[(this.outputPos++)] = 'c';
/* 319 */           this.output[(this.outputPos++)] = ')';
/* 320 */           break;
/*     */         case 'Ð':
/*     */         case 'Ď':
/*     */         case 'Đ':
/*     */         case 'Ɖ':
/*     */         case 'Ɗ':
/*     */         case 'Ƌ':
/*     */         case 'ᴅ':
/*     */         case 'ᴆ':
/*     */         case 'Ḋ':
/*     */         case 'Ḍ':
/*     */         case 'Ḏ':
/*     */         case 'Ḑ':
/*     */         case 'Ḓ':
/*     */         case 'Ⓓ':
/*     */         case 'Ꝺ':
/*     */         case 'Ｄ':
/* 337 */           this.output[(this.outputPos++)] = 'D';
/* 338 */           break;
/*     */         case 'ð':
/*     */         case 'ď':
/*     */         case 'đ':
/*     */         case 'ƌ':
/*     */         case 'ȡ':
/*     */         case 'ɖ':
/*     */         case 'ɗ':
/*     */         case 'ᵭ':
/*     */         case 'ᶁ':
/*     */         case 'ᶑ':
/*     */         case 'ḋ':
/*     */         case 'ḍ':
/*     */         case 'ḏ':
/*     */         case 'ḑ':
/*     */         case 'ḓ':
/*     */         case 'ⓓ':
/*     */         case 'ꝺ':
/*     */         case 'ｄ':
/* 357 */           this.output[(this.outputPos++)] = 'd';
/* 358 */           break;
/*     */         case 'Ǆ':
/*     */         case 'Ǳ':
/* 361 */           this.output[(this.outputPos++)] = 'D';
/* 362 */           this.output[(this.outputPos++)] = 'Z';
/* 363 */           break;
/*     */         case 'ǅ':
/*     */         case 'ǲ':
/* 366 */           this.output[(this.outputPos++)] = 'D';
/* 367 */           this.output[(this.outputPos++)] = 'z';
/* 368 */           break;
/*     */         case '⒟':
/* 370 */           this.output[(this.outputPos++)] = '(';
/* 371 */           this.output[(this.outputPos++)] = 'd';
/* 372 */           this.output[(this.outputPos++)] = ')';
/* 373 */           break;
/*     */         case 'ȸ':
/* 375 */           this.output[(this.outputPos++)] = 'd';
/* 376 */           this.output[(this.outputPos++)] = 'b';
/* 377 */           break;
/*     */         case 'ǆ':
/*     */         case 'ǳ':
/*     */         case 'ʣ':
/*     */         case 'ʥ':
/* 382 */           this.output[(this.outputPos++)] = 'd';
/* 383 */           this.output[(this.outputPos++)] = 'z';
/* 384 */           break;
/*     */         case 'È':
/*     */         case 'É':
/*     */         case 'Ê':
/*     */         case 'Ë':
/*     */         case 'Ē':
/*     */         case 'Ĕ':
/*     */         case 'Ė':
/*     */         case 'Ę':
/*     */         case 'Ě':
/*     */         case 'Ǝ':
/*     */         case 'Ɛ':
/*     */         case 'Ȅ':
/*     */         case 'Ȇ':
/*     */         case 'Ȩ':
/*     */         case 'Ɇ':
/*     */         case 'ᴇ':
/*     */         case 'Ḕ':
/*     */         case 'Ḗ':
/*     */         case 'Ḙ':
/*     */         case 'Ḛ':
/*     */         case 'Ḝ':
/*     */         case 'Ẹ':
/*     */         case 'Ẻ':
/*     */         case 'Ẽ':
/*     */         case 'Ế':
/*     */         case 'Ề':
/*     */         case 'Ể':
/*     */         case 'Ễ':
/*     */         case 'Ệ':
/*     */         case 'Ⓔ':
/*     */         case 'ⱻ':
/*     */         case 'Ｅ':
/* 417 */           this.output[(this.outputPos++)] = 'E';
/* 418 */           break;
/*     */         case 'è':
/*     */         case 'é':
/*     */         case 'ê':
/*     */         case 'ë':
/*     */         case 'ē':
/*     */         case 'ĕ':
/*     */         case 'ė':
/*     */         case 'ę':
/*     */         case 'ě':
/*     */         case 'ǝ':
/*     */         case 'ȅ':
/*     */         case 'ȇ':
/*     */         case 'ȩ':
/*     */         case 'ɇ':
/*     */         case 'ɘ':
/*     */         case 'ɛ':
/*     */         case 'ɜ':
/*     */         case 'ɝ':
/*     */         case 'ɞ':
/*     */         case 'ʚ':
/*     */         case 'ᴈ':
/*     */         case 'ᶒ':
/*     */         case 'ᶓ':
/*     */         case 'ᶔ':
/*     */         case 'ḕ':
/*     */         case 'ḗ':
/*     */         case 'ḙ':
/*     */         case 'ḛ':
/*     */         case 'ḝ':
/*     */         case 'ẹ':
/*     */         case 'ẻ':
/*     */         case 'ẽ':
/*     */         case 'ế':
/*     */         case 'ề':
/*     */         case 'ể':
/*     */         case 'ễ':
/*     */         case 'ệ':
/*     */         case 'ₑ':
/*     */         case 'ⓔ':
/*     */         case 'ⱸ':
/*     */         case 'ｅ':
/* 460 */           this.output[(this.outputPos++)] = 'e';
/* 461 */           break;
/*     */         case '⒠':
/* 463 */           this.output[(this.outputPos++)] = '(';
/* 464 */           this.output[(this.outputPos++)] = 'e';
/* 465 */           this.output[(this.outputPos++)] = ')';
/* 466 */           break;
/*     */         case 'Ƒ':
/*     */         case 'Ḟ':
/*     */         case 'Ⓕ':
/*     */         case 'ꜰ':
/*     */         case 'Ꝼ':
/*     */         case 'ꟻ':
/*     */         case 'Ｆ':
/* 474 */           this.output[(this.outputPos++)] = 'F';
/* 475 */           break;
/*     */         case 'ƒ':
/*     */         case 'ᵮ':
/*     */         case 'ᶂ':
/*     */         case 'ḟ':
/*     */         case 'ẛ':
/*     */         case 'ⓕ':
/*     */         case 'ꝼ':
/*     */         case 'ｆ':
/* 484 */           this.output[(this.outputPos++)] = 'f';
/* 485 */           break;
/*     */         case '⒡':
/* 487 */           this.output[(this.outputPos++)] = '(';
/* 488 */           this.output[(this.outputPos++)] = 'f';
/* 489 */           this.output[(this.outputPos++)] = ')';
/* 490 */           break;
/*     */         case 'ﬀ':
/* 492 */           this.output[(this.outputPos++)] = 'f';
/* 493 */           this.output[(this.outputPos++)] = 'f';
/* 494 */           break;
/*     */         case 'ﬃ':
/* 496 */           this.output[(this.outputPos++)] = 'f';
/* 497 */           this.output[(this.outputPos++)] = 'f';
/* 498 */           this.output[(this.outputPos++)] = 'i';
/* 499 */           break;
/*     */         case 'ﬄ':
/* 501 */           this.output[(this.outputPos++)] = 'f';
/* 502 */           this.output[(this.outputPos++)] = 'f';
/* 503 */           this.output[(this.outputPos++)] = 'l';
/* 504 */           break;
/*     */         case 'ﬁ':
/* 506 */           this.output[(this.outputPos++)] = 'f';
/* 507 */           this.output[(this.outputPos++)] = 'i';
/* 508 */           break;
/*     */         case 'ﬂ':
/* 510 */           this.output[(this.outputPos++)] = 'f';
/* 511 */           this.output[(this.outputPos++)] = 'l';
/* 512 */           break;
/*     */         case 'Ĝ':
/*     */         case 'Ğ':
/*     */         case 'Ġ':
/*     */         case 'Ģ':
/*     */         case 'Ɠ':
/*     */         case 'Ǥ':
/*     */         case 'ǥ':
/*     */         case 'Ǧ':
/*     */         case 'ǧ':
/*     */         case 'Ǵ':
/*     */         case 'ɢ':
/*     */         case 'ʛ':
/*     */         case 'Ḡ':
/*     */         case 'Ⓖ':
/*     */         case 'Ᵹ':
/*     */         case 'Ꝿ':
/*     */         case 'Ｇ':
/* 530 */           this.output[(this.outputPos++)] = 'G';
/* 531 */           break;
/*     */         case 'ĝ':
/*     */         case 'ğ':
/*     */         case 'ġ':
/*     */         case 'ģ':
/*     */         case 'ǵ':
/*     */         case 'ɠ':
/*     */         case 'ɡ':
/*     */         case 'ᵷ':
/*     */         case 'ᵹ':
/*     */         case 'ᶃ':
/*     */         case 'ḡ':
/*     */         case 'ⓖ':
/*     */         case 'ꝿ':
/*     */         case 'ｇ':
/* 546 */           this.output[(this.outputPos++)] = 'g';
/* 547 */           break;
/*     */         case '⒢':
/* 549 */           this.output[(this.outputPos++)] = '(';
/* 550 */           this.output[(this.outputPos++)] = 'g';
/* 551 */           this.output[(this.outputPos++)] = ')';
/* 552 */           break;
/*     */         case 'Ĥ':
/*     */         case 'Ħ':
/*     */         case 'Ȟ':
/*     */         case 'ʜ':
/*     */         case 'Ḣ':
/*     */         case 'Ḥ':
/*     */         case 'Ḧ':
/*     */         case 'Ḩ':
/*     */         case 'Ḫ':
/*     */         case 'Ⓗ':
/*     */         case 'Ⱨ':
/*     */         case 'Ⱶ':
/*     */         case 'Ｈ':
/* 566 */           this.output[(this.outputPos++)] = 'H';
/* 567 */           break;
/*     */         case 'ĥ':
/*     */         case 'ħ':
/*     */         case 'ȟ':
/*     */         case 'ɥ':
/*     */         case 'ɦ':
/*     */         case 'ʮ':
/*     */         case 'ʯ':
/*     */         case 'ḣ':
/*     */         case 'ḥ':
/*     */         case 'ḧ':
/*     */         case 'ḩ':
/*     */         case 'ḫ':
/*     */         case 'ẖ':
/*     */         case 'ⓗ':
/*     */         case 'ⱨ':
/*     */         case 'ⱶ':
/*     */         case 'ｈ':
/* 585 */           this.output[(this.outputPos++)] = 'h';
/* 586 */           break;
/*     */         case 'Ƕ':
/* 588 */           this.output[(this.outputPos++)] = 'H';
/* 589 */           this.output[(this.outputPos++)] = 'V';
/* 590 */           break;
/*     */         case '⒣':
/* 592 */           this.output[(this.outputPos++)] = '(';
/* 593 */           this.output[(this.outputPos++)] = 'h';
/* 594 */           this.output[(this.outputPos++)] = ')';
/* 595 */           break;
/*     */         case 'ƕ':
/* 597 */           this.output[(this.outputPos++)] = 'h';
/* 598 */           this.output[(this.outputPos++)] = 'v';
/* 599 */           break;
/*     */         case 'Ì':
/*     */         case 'Í':
/*     */         case 'Î':
/*     */         case 'Ï':
/*     */         case 'Ĩ':
/*     */         case 'Ī':
/*     */         case 'Ĭ':
/*     */         case 'Į':
/*     */         case 'İ':
/*     */         case 'Ɩ':
/*     */         case 'Ɨ':
/*     */         case 'Ǐ':
/*     */         case 'Ȉ':
/*     */         case 'Ȋ':
/*     */         case 'ɪ':
/*     */         case 'ᵻ':
/*     */         case 'Ḭ':
/*     */         case 'Ḯ':
/*     */         case 'Ỉ':
/*     */         case 'Ị':
/*     */         case 'Ⓘ':
/*     */         case 'ꟾ':
/*     */         case 'Ｉ':
/* 623 */           this.output[(this.outputPos++)] = 'I';
/* 624 */           break;
/*     */         case 'ì':
/*     */         case 'í':
/*     */         case 'î':
/*     */         case 'ï':
/*     */         case 'ĩ':
/*     */         case 'ī':
/*     */         case 'ĭ':
/*     */         case 'į':
/*     */         case 'ı':
/*     */         case 'ǐ':
/*     */         case 'ȉ':
/*     */         case 'ȋ':
/*     */         case 'ɨ':
/*     */         case 'ᴉ':
/*     */         case 'ᵢ':
/*     */         case 'ᵼ':
/*     */         case 'ᶖ':
/*     */         case 'ḭ':
/*     */         case 'ḯ':
/*     */         case 'ỉ':
/*     */         case 'ị':
/*     */         case 'ⁱ':
/*     */         case 'ⓘ':
/*     */         case 'ｉ':
/* 649 */           this.output[(this.outputPos++)] = 'i';
/* 650 */           break;
/*     */         case 'Ĳ':
/* 652 */           this.output[(this.outputPos++)] = 'I';
/* 653 */           this.output[(this.outputPos++)] = 'J';
/* 654 */           break;
/*     */         case '⒤':
/* 656 */           this.output[(this.outputPos++)] = '(';
/* 657 */           this.output[(this.outputPos++)] = 'i';
/* 658 */           this.output[(this.outputPos++)] = ')';
/* 659 */           break;
/*     */         case 'ĳ':
/* 661 */           this.output[(this.outputPos++)] = 'i';
/* 662 */           this.output[(this.outputPos++)] = 'j';
/* 663 */           break;
/*     */         case 'Ĵ':
/*     */         case 'Ɉ':
/*     */         case 'ᴊ':
/*     */         case 'Ⓙ':
/*     */         case 'Ｊ':
/* 669 */           this.output[(this.outputPos++)] = 'J';
/* 670 */           break;
/*     */         case 'ĵ':
/*     */         case 'ǰ':
/*     */         case 'ȷ':
/*     */         case 'ɉ':
/*     */         case 'ɟ':
/*     */         case 'ʄ':
/*     */         case 'ʝ':
/*     */         case 'ⓙ':
/*     */         case 'ⱼ':
/*     */         case 'ｊ':
/* 681 */           this.output[(this.outputPos++)] = 'j';
/* 682 */           break;
/*     */         case '⒥':
/* 684 */           this.output[(this.outputPos++)] = '(';
/* 685 */           this.output[(this.outputPos++)] = 'j';
/* 686 */           this.output[(this.outputPos++)] = ')';
/* 687 */           break;
/*     */         case 'Ķ':
/*     */         case 'Ƙ':
/*     */         case 'Ǩ':
/*     */         case 'ᴋ':
/*     */         case 'Ḱ':
/*     */         case 'Ḳ':
/*     */         case 'Ḵ':
/*     */         case 'Ⓚ':
/*     */         case 'Ⱪ':
/*     */         case 'Ꝁ':
/*     */         case 'Ꝃ':
/*     */         case 'Ꝅ':
/*     */         case 'Ｋ':
/* 701 */           this.output[(this.outputPos++)] = 'K';
/* 702 */           break;
/*     */         case 'ķ':
/*     */         case 'ƙ':
/*     */         case 'ǩ':
/*     */         case 'ʞ':
/*     */         case 'ᶄ':
/*     */         case 'ḱ':
/*     */         case 'ḳ':
/*     */         case 'ḵ':
/*     */         case 'ⓚ':
/*     */         case 'ⱪ':
/*     */         case 'ꝁ':
/*     */         case 'ꝃ':
/*     */         case 'ꝅ':
/*     */         case 'ｋ':
/* 717 */           this.output[(this.outputPos++)] = 'k';
/* 718 */           break;
/*     */         case '⒦':
/* 720 */           this.output[(this.outputPos++)] = '(';
/* 721 */           this.output[(this.outputPos++)] = 'k';
/* 722 */           this.output[(this.outputPos++)] = ')';
/* 723 */           break;
/*     */         case 'Ĺ':
/*     */         case 'Ļ':
/*     */         case 'Ľ':
/*     */         case 'Ŀ':
/*     */         case 'Ł':
/*     */         case 'Ƚ':
/*     */         case 'ʟ':
/*     */         case 'ᴌ':
/*     */         case 'Ḷ':
/*     */         case 'Ḹ':
/*     */         case 'Ḻ':
/*     */         case 'Ḽ':
/*     */         case 'Ⓛ':
/*     */         case 'Ⱡ':
/*     */         case 'Ɫ':
/*     */         case 'Ꝇ':
/*     */         case 'Ꝉ':
/*     */         case 'Ꞁ':
/*     */         case 'Ｌ':
/* 743 */           this.output[(this.outputPos++)] = 'L';
/* 744 */           break;
/*     */         case 'ĺ':
/*     */         case 'ļ':
/*     */         case 'ľ':
/*     */         case 'ŀ':
/*     */         case 'ł':
/*     */         case 'ƚ':
/*     */         case 'ȴ':
/*     */         case 'ɫ':
/*     */         case 'ɬ':
/*     */         case 'ɭ':
/*     */         case 'ᶅ':
/*     */         case 'ḷ':
/*     */         case 'ḹ':
/*     */         case 'ḻ':
/*     */         case 'ḽ':
/*     */         case 'ⓛ':
/*     */         case 'ⱡ':
/*     */         case 'ꝇ':
/*     */         case 'ꝉ':
/*     */         case 'ꞁ':
/*     */         case 'ｌ':
/* 766 */           this.output[(this.outputPos++)] = 'l';
/* 767 */           break;
/*     */         case 'Ǉ':
/* 769 */           this.output[(this.outputPos++)] = 'L';
/* 770 */           this.output[(this.outputPos++)] = 'J';
/* 771 */           break;
/*     */         case 'Ỻ':
/* 773 */           this.output[(this.outputPos++)] = 'L';
/* 774 */           this.output[(this.outputPos++)] = 'L';
/* 775 */           break;
/*     */         case 'ǈ':
/* 777 */           this.output[(this.outputPos++)] = 'L';
/* 778 */           this.output[(this.outputPos++)] = 'j';
/* 779 */           break;
/*     */         case '⒧':
/* 781 */           this.output[(this.outputPos++)] = '(';
/* 782 */           this.output[(this.outputPos++)] = 'l';
/* 783 */           this.output[(this.outputPos++)] = ')';
/* 784 */           break;
/*     */         case 'ǉ':
/* 786 */           this.output[(this.outputPos++)] = 'l';
/* 787 */           this.output[(this.outputPos++)] = 'j';
/* 788 */           break;
/*     */         case 'ỻ':
/* 790 */           this.output[(this.outputPos++)] = 'l';
/* 791 */           this.output[(this.outputPos++)] = 'l';
/* 792 */           break;
/*     */         case 'ʪ':
/* 794 */           this.output[(this.outputPos++)] = 'l';
/* 795 */           this.output[(this.outputPos++)] = 's';
/* 796 */           break;
/*     */         case 'ʫ':
/* 798 */           this.output[(this.outputPos++)] = 'l';
/* 799 */           this.output[(this.outputPos++)] = 'z';
/* 800 */           break;
/*     */         case 'Ɯ':
/*     */         case 'ᴍ':
/*     */         case 'Ḿ':
/*     */         case 'Ṁ':
/*     */         case 'Ṃ':
/*     */         case 'Ⓜ':
/*     */         case 'Ɱ':
/*     */         case 'ꟽ':
/*     */         case 'ꟿ':
/*     */         case 'Ｍ':
/* 811 */           this.output[(this.outputPos++)] = 'M';
/* 812 */           break;
/*     */         case 'ɯ':
/*     */         case 'ɰ':
/*     */         case 'ɱ':
/*     */         case 'ᵯ':
/*     */         case 'ᶆ':
/*     */         case 'ḿ':
/*     */         case 'ṁ':
/*     */         case 'ṃ':
/*     */         case 'ⓜ':
/*     */         case 'ｍ':
/* 823 */           this.output[(this.outputPos++)] = 'm';
/* 824 */           break;
/*     */         case '⒨':
/* 826 */           this.output[(this.outputPos++)] = '(';
/* 827 */           this.output[(this.outputPos++)] = 'm';
/* 828 */           this.output[(this.outputPos++)] = ')';
/* 829 */           break;
/*     */         case 'Ñ':
/*     */         case 'Ń':
/*     */         case 'Ņ':
/*     */         case 'Ň':
/*     */         case 'Ŋ':
/*     */         case 'Ɲ':
/*     */         case 'Ǹ':
/*     */         case 'Ƞ':
/*     */         case 'ɴ':
/*     */         case 'ᴎ':
/*     */         case 'Ṅ':
/*     */         case 'Ṇ':
/*     */         case 'Ṉ':
/*     */         case 'Ṋ':
/*     */         case 'Ⓝ':
/*     */         case 'Ｎ':
/* 846 */           this.output[(this.outputPos++)] = 'N';
/* 847 */           break;
/*     */         case 'ñ':
/*     */         case 'ń':
/*     */         case 'ņ':
/*     */         case 'ň':
/*     */         case 'ŉ':
/*     */         case 'ŋ':
/*     */         case 'ƞ':
/*     */         case 'ǹ':
/*     */         case 'ȵ':
/*     */         case 'ɲ':
/*     */         case 'ɳ':
/*     */         case 'ᵰ':
/*     */         case 'ᶇ':
/*     */         case 'ṅ':
/*     */         case 'ṇ':
/*     */         case 'ṉ':
/*     */         case 'ṋ':
/*     */         case 'ⁿ':
/*     */         case 'ⓝ':
/*     */         case 'ｎ':
/* 868 */           this.output[(this.outputPos++)] = 'n';
/* 869 */           break;
/*     */         case 'Ǌ':
/* 871 */           this.output[(this.outputPos++)] = 'N';
/* 872 */           this.output[(this.outputPos++)] = 'J';
/* 873 */           break;
/*     */         case 'ǋ':
/* 875 */           this.output[(this.outputPos++)] = 'N';
/* 876 */           this.output[(this.outputPos++)] = 'j';
/* 877 */           break;
/*     */         case '⒩':
/* 879 */           this.output[(this.outputPos++)] = '(';
/* 880 */           this.output[(this.outputPos++)] = 'n';
/* 881 */           this.output[(this.outputPos++)] = ')';
/* 882 */           break;
/*     */         case 'ǌ':
/* 884 */           this.output[(this.outputPos++)] = 'n';
/* 885 */           this.output[(this.outputPos++)] = 'j';
/* 886 */           break;
/*     */         case 'Ò':
/*     */         case 'Ó':
/*     */         case 'Ô':
/*     */         case 'Õ':
/*     */         case 'Ö':
/*     */         case 'Ø':
/*     */         case 'Ō':
/*     */         case 'Ŏ':
/*     */         case 'Ő':
/*     */         case 'Ɔ':
/*     */         case 'Ɵ':
/*     */         case 'Ơ':
/*     */         case 'Ǒ':
/*     */         case 'Ǫ':
/*     */         case 'Ǭ':
/*     */         case 'Ǿ':
/*     */         case 'Ȍ':
/*     */         case 'Ȏ':
/*     */         case 'Ȫ':
/*     */         case 'Ȭ':
/*     */         case 'Ȯ':
/*     */         case 'Ȱ':
/*     */         case 'ᴏ':
/*     */         case 'ᴐ':
/*     */         case 'Ṍ':
/*     */         case 'Ṏ':
/*     */         case 'Ṑ':
/*     */         case 'Ṓ':
/*     */         case 'Ọ':
/*     */         case 'Ỏ':
/*     */         case 'Ố':
/*     */         case 'Ồ':
/*     */         case 'Ổ':
/*     */         case 'Ỗ':
/*     */         case 'Ộ':
/*     */         case 'Ớ':
/*     */         case 'Ờ':
/*     */         case 'Ở':
/*     */         case 'Ỡ':
/*     */         case 'Ợ':
/*     */         case 'Ⓞ':
/*     */         case 'Ꝋ':
/*     */         case 'Ꝍ':
/*     */         case 'Ｏ':
/* 931 */           this.output[(this.outputPos++)] = 'O';
/* 932 */           break;
/*     */         case 'ò':
/*     */         case 'ó':
/*     */         case 'ô':
/*     */         case 'õ':
/*     */         case 'ö':
/*     */         case 'ø':
/*     */         case 'ō':
/*     */         case 'ŏ':
/*     */         case 'ő':
/*     */         case 'ơ':
/*     */         case 'ǒ':
/*     */         case 'ǫ':
/*     */         case 'ǭ':
/*     */         case 'ǿ':
/*     */         case 'ȍ':
/*     */         case 'ȏ':
/*     */         case 'ȫ':
/*     */         case 'ȭ':
/*     */         case 'ȯ':
/*     */         case 'ȱ':
/*     */         case 'ɔ':
/*     */         case 'ɵ':
/*     */         case 'ᴖ':
/*     */         case 'ᴗ':
/*     */         case 'ᶗ':
/*     */         case 'ṍ':
/*     */         case 'ṏ':
/*     */         case 'ṑ':
/*     */         case 'ṓ':
/*     */         case 'ọ':
/*     */         case 'ỏ':
/*     */         case 'ố':
/*     */         case 'ồ':
/*     */         case 'ổ':
/*     */         case 'ỗ':
/*     */         case 'ộ':
/*     */         case 'ớ':
/*     */         case 'ờ':
/*     */         case 'ở':
/*     */         case 'ỡ':
/*     */         case 'ợ':
/*     */         case 'ₒ':
/*     */         case 'ⓞ':
/*     */         case 'ⱺ':
/*     */         case 'ꝋ':
/*     */         case 'ꝍ':
/*     */         case 'ｏ':
/* 980 */           this.output[(this.outputPos++)] = 'o';
/* 981 */           break;
/*     */         case 'Œ':
/*     */         case 'ɶ':
/* 984 */           this.output[(this.outputPos++)] = 'O';
/* 985 */           this.output[(this.outputPos++)] = 'E';
/* 986 */           break;
/*     */         case 'Ꝏ':
/* 988 */           this.output[(this.outputPos++)] = 'O';
/* 989 */           this.output[(this.outputPos++)] = 'O';
/* 990 */           break;
/*     */         case 'Ȣ':
/*     */         case 'ᴕ':
/* 993 */           this.output[(this.outputPos++)] = 'O';
/* 994 */           this.output[(this.outputPos++)] = 'U';
/* 995 */           break;
/*     */         case '⒪':
/* 997 */           this.output[(this.outputPos++)] = '(';
/* 998 */           this.output[(this.outputPos++)] = 'o';
/* 999 */           this.output[(this.outputPos++)] = ')';
/* 1000 */           break;
/*     */         case 'œ':
/*     */         case 'ᴔ':
/* 1003 */           this.output[(this.outputPos++)] = 'o';
/* 1004 */           this.output[(this.outputPos++)] = 'e';
/* 1005 */           break;
/*     */         case 'ꝏ':
/* 1007 */           this.output[(this.outputPos++)] = 'o';
/* 1008 */           this.output[(this.outputPos++)] = 'o';
/* 1009 */           break;
/*     */         case 'ȣ':
/* 1011 */           this.output[(this.outputPos++)] = 'o';
/* 1012 */           this.output[(this.outputPos++)] = 'u';
/* 1013 */           break;
/*     */         case 'Ƥ':
/*     */         case 'ᴘ':
/*     */         case 'Ṕ':
/*     */         case 'Ṗ':
/*     */         case 'Ⓟ':
/*     */         case 'Ᵽ':
/*     */         case 'Ꝑ':
/*     */         case 'Ꝓ':
/*     */         case 'Ꝕ':
/*     */         case 'Ｐ':
/* 1024 */           this.output[(this.outputPos++)] = 'P';
/* 1025 */           break;
/*     */         case 'ƥ':
/*     */         case 'ᵱ':
/*     */         case 'ᵽ':
/*     */         case 'ᶈ':
/*     */         case 'ṕ':
/*     */         case 'ṗ':
/*     */         case 'ⓟ':
/*     */         case 'ꝑ':
/*     */         case 'ꝓ':
/*     */         case 'ꝕ':
/*     */         case 'ꟼ':
/*     */         case 'ｐ':
/* 1038 */           this.output[(this.outputPos++)] = 'p';
/* 1039 */           break;
/*     */         case '⒫':
/* 1041 */           this.output[(this.outputPos++)] = '(';
/* 1042 */           this.output[(this.outputPos++)] = 'p';
/* 1043 */           this.output[(this.outputPos++)] = ')';
/* 1044 */           break;
/*     */         case 'Ɋ':
/*     */         case 'Ⓠ':
/*     */         case 'Ꝗ':
/*     */         case 'Ꝙ':
/*     */         case 'Ｑ':
/* 1050 */           this.output[(this.outputPos++)] = 'Q';
/* 1051 */           break;
/*     */         case 'ĸ':
/*     */         case 'ɋ':
/*     */         case 'ʠ':
/*     */         case 'ⓠ':
/*     */         case 'ꝗ':
/*     */         case 'ꝙ':
/*     */         case 'ｑ':
/* 1059 */           this.output[(this.outputPos++)] = 'q';
/* 1060 */           break;
/*     */         case '⒬':
/* 1062 */           this.output[(this.outputPos++)] = '(';
/* 1063 */           this.output[(this.outputPos++)] = 'q';
/* 1064 */           this.output[(this.outputPos++)] = ')';
/* 1065 */           break;
/*     */         case 'ȹ':
/* 1067 */           this.output[(this.outputPos++)] = 'q';
/* 1068 */           this.output[(this.outputPos++)] = 'p';
/* 1069 */           break;
/*     */         case 'Ŕ':
/*     */         case 'Ŗ':
/*     */         case 'Ř':
/*     */         case 'Ȑ':
/*     */         case 'Ȓ':
/*     */         case 'Ɍ':
/*     */         case 'ʀ':
/*     */         case 'ʁ':
/*     */         case 'ᴙ':
/*     */         case 'ᴚ':
/*     */         case 'Ṙ':
/*     */         case 'Ṛ':
/*     */         case 'Ṝ':
/*     */         case 'Ṟ':
/*     */         case 'Ⓡ':
/*     */         case 'Ɽ':
/*     */         case 'Ꝛ':
/*     */         case 'Ꞃ':
/*     */         case 'Ｒ':
/* 1089 */           this.output[(this.outputPos++)] = 'R';
/* 1090 */           break;
/*     */         case 'ŕ':
/*     */         case 'ŗ':
/*     */         case 'ř':
/*     */         case 'ȑ':
/*     */         case 'ȓ':
/*     */         case 'ɍ':
/*     */         case 'ɼ':
/*     */         case 'ɽ':
/*     */         case 'ɾ':
/*     */         case 'ɿ':
/*     */         case 'ᵣ':
/*     */         case 'ᵲ':
/*     */         case 'ᵳ':
/*     */         case 'ᶉ':
/*     */         case 'ṙ':
/*     */         case 'ṛ':
/*     */         case 'ṝ':
/*     */         case 'ṟ':
/*     */         case 'ⓡ':
/*     */         case 'ꝛ':
/*     */         case 'ꞃ':
/*     */         case 'ｒ':
/* 1113 */           this.output[(this.outputPos++)] = 'r';
/* 1114 */           break;
/*     */         case '⒭':
/* 1116 */           this.output[(this.outputPos++)] = '(';
/* 1117 */           this.output[(this.outputPos++)] = 'r';
/* 1118 */           this.output[(this.outputPos++)] = ')';
/* 1119 */           break;
/*     */         case 'Ś':
/*     */         case 'Ŝ':
/*     */         case 'Ş':
/*     */         case 'Š':
/*     */         case 'Ș':
/*     */         case 'Ṡ':
/*     */         case 'Ṣ':
/*     */         case 'Ṥ':
/*     */         case 'Ṧ':
/*     */         case 'Ṩ':
/*     */         case 'Ⓢ':
/*     */         case 'ꜱ':
/*     */         case 'ꞅ':
/*     */         case 'Ｓ':
/* 1134 */           this.output[(this.outputPos++)] = 'S';
/* 1135 */           break;
/*     */         case 'ś':
/*     */         case 'ŝ':
/*     */         case 'ş':
/*     */         case 'š':
/*     */         case 'ſ':
/*     */         case 'ș':
/*     */         case 'ȿ':
/*     */         case 'ʂ':
/*     */         case 'ᵴ':
/*     */         case 'ᶊ':
/*     */         case 'ṡ':
/*     */         case 'ṣ':
/*     */         case 'ṥ':
/*     */         case 'ṧ':
/*     */         case 'ṩ':
/*     */         case 'ẜ':
/*     */         case 'ẝ':
/*     */         case 'ⓢ':
/*     */         case 'Ꞅ':
/*     */         case 'ｓ':
/* 1156 */           this.output[(this.outputPos++)] = 's';
/* 1157 */           break;
/*     */         case 'ẞ':
/* 1159 */           this.output[(this.outputPos++)] = 'S';
/* 1160 */           this.output[(this.outputPos++)] = 'S';
/* 1161 */           break;
/*     */         case '⒮':
/* 1163 */           this.output[(this.outputPos++)] = '(';
/* 1164 */           this.output[(this.outputPos++)] = 's';
/* 1165 */           this.output[(this.outputPos++)] = ')';
/* 1166 */           break;
/*     */         case 'ß':
/* 1168 */           this.output[(this.outputPos++)] = 's';
/* 1169 */           this.output[(this.outputPos++)] = 's';
/* 1170 */           break;
/*     */         case 'ﬆ':
/* 1172 */           this.output[(this.outputPos++)] = 's';
/* 1173 */           this.output[(this.outputPos++)] = 't';
/* 1174 */           break;
/*     */         case 'Ţ':
/*     */         case 'Ť':
/*     */         case 'Ŧ':
/*     */         case 'Ƭ':
/*     */         case 'Ʈ':
/*     */         case 'Ț':
/*     */         case 'Ⱦ':
/*     */         case 'ᴛ':
/*     */         case 'Ṫ':
/*     */         case 'Ṭ':
/*     */         case 'Ṯ':
/*     */         case 'Ṱ':
/*     */         case 'Ⓣ':
/*     */         case 'Ꞇ':
/*     */         case 'Ｔ':
/* 1190 */           this.output[(this.outputPos++)] = 'T';
/* 1191 */           break;
/*     */         case 'ţ':
/*     */         case 'ť':
/*     */         case 'ŧ':
/*     */         case 'ƫ':
/*     */         case 'ƭ':
/*     */         case 'ț':
/*     */         case 'ȶ':
/*     */         case 'ʇ':
/*     */         case 'ʈ':
/*     */         case 'ᵵ':
/*     */         case 'ṫ':
/*     */         case 'ṭ':
/*     */         case 'ṯ':
/*     */         case 'ṱ':
/*     */         case 'ẗ':
/*     */         case 'ⓣ':
/*     */         case 'ⱦ':
/*     */         case 'ｔ':
/* 1210 */           this.output[(this.outputPos++)] = 't';
/* 1211 */           break;
/*     */         case 'Þ':
/*     */         case 'Ꝧ':
/* 1214 */           this.output[(this.outputPos++)] = 'T';
/* 1215 */           this.output[(this.outputPos++)] = 'H';
/* 1216 */           break;
/*     */         case 'Ꜩ':
/* 1218 */           this.output[(this.outputPos++)] = 'T';
/* 1219 */           this.output[(this.outputPos++)] = 'Z';
/* 1220 */           break;
/*     */         case '⒯':
/* 1222 */           this.output[(this.outputPos++)] = '(';
/* 1223 */           this.output[(this.outputPos++)] = 't';
/* 1224 */           this.output[(this.outputPos++)] = ')';
/* 1225 */           break;
/*     */         case 'ʨ':
/* 1227 */           this.output[(this.outputPos++)] = 't';
/* 1228 */           this.output[(this.outputPos++)] = 'c';
/* 1229 */           break;
/*     */         case 'þ':
/*     */         case 'ᵺ':
/*     */         case 'ꝧ':
/* 1233 */           this.output[(this.outputPos++)] = 't';
/* 1234 */           this.output[(this.outputPos++)] = 'h';
/* 1235 */           break;
/*     */         case 'ʦ':
/* 1237 */           this.output[(this.outputPos++)] = 't';
/* 1238 */           this.output[(this.outputPos++)] = 's';
/* 1239 */           break;
/*     */         case 'ꜩ':
/* 1241 */           this.output[(this.outputPos++)] = 't';
/* 1242 */           this.output[(this.outputPos++)] = 'z';
/* 1243 */           break;
/*     */         case 'Ù':
/*     */         case 'Ú':
/*     */         case 'Û':
/*     */         case 'Ü':
/*     */         case 'Ũ':
/*     */         case 'Ū':
/*     */         case 'Ŭ':
/*     */         case 'Ů':
/*     */         case 'Ű':
/*     */         case 'Ų':
/*     */         case 'Ư':
/*     */         case 'Ǔ':
/*     */         case 'Ǖ':
/*     */         case 'Ǘ':
/*     */         case 'Ǚ':
/*     */         case 'Ǜ':
/*     */         case 'Ȕ':
/*     */         case 'Ȗ':
/*     */         case 'Ʉ':
/*     */         case 'ᴜ':
/*     */         case 'ᵾ':
/*     */         case 'Ṳ':
/*     */         case 'Ṵ':
/*     */         case 'Ṷ':
/*     */         case 'Ṹ':
/*     */         case 'Ṻ':
/*     */         case 'Ụ':
/*     */         case 'Ủ':
/*     */         case 'Ứ':
/*     */         case 'Ừ':
/*     */         case 'Ử':
/*     */         case 'Ữ':
/*     */         case 'Ự':
/*     */         case 'Ⓤ':
/*     */         case 'Ｕ':
/* 1279 */           this.output[(this.outputPos++)] = 'U';
/* 1280 */           break;
/*     */         case 'ù':
/*     */         case 'ú':
/*     */         case 'û':
/*     */         case 'ü':
/*     */         case 'ũ':
/*     */         case 'ū':
/*     */         case 'ŭ':
/*     */         case 'ů':
/*     */         case 'ű':
/*     */         case 'ų':
/*     */         case 'ư':
/*     */         case 'ǔ':
/*     */         case 'ǖ':
/*     */         case 'ǘ':
/*     */         case 'ǚ':
/*     */         case 'ǜ':
/*     */         case 'ȕ':
/*     */         case 'ȗ':
/*     */         case 'ʉ':
/*     */         case 'ᵤ':
/*     */         case 'ᶙ':
/*     */         case 'ṳ':
/*     */         case 'ṵ':
/*     */         case 'ṷ':
/*     */         case 'ṹ':
/*     */         case 'ṻ':
/*     */         case 'ụ':
/*     */         case 'ủ':
/*     */         case 'ứ':
/*     */         case 'ừ':
/*     */         case 'ử':
/*     */         case 'ữ':
/*     */         case 'ự':
/*     */         case 'ⓤ':
/*     */         case 'ｕ':
/* 1316 */           this.output[(this.outputPos++)] = 'u';
/* 1317 */           break;
/*     */         case '⒰':
/* 1319 */           this.output[(this.outputPos++)] = '(';
/* 1320 */           this.output[(this.outputPos++)] = 'u';
/* 1321 */           this.output[(this.outputPos++)] = ')';
/* 1322 */           break;
/*     */         case 'ᵫ':
/* 1324 */           this.output[(this.outputPos++)] = 'u';
/* 1325 */           this.output[(this.outputPos++)] = 'e';
/* 1326 */           break;
/*     */         case 'Ʋ':
/*     */         case 'Ʌ':
/*     */         case 'ᴠ':
/*     */         case 'Ṽ':
/*     */         case 'Ṿ':
/*     */         case 'Ỽ':
/*     */         case 'Ⓥ':
/*     */         case 'Ꝟ':
/*     */         case 'Ꝩ':
/*     */         case 'Ｖ':
/* 1337 */           this.output[(this.outputPos++)] = 'V';
/* 1338 */           break;
/*     */         case 'ʋ':
/*     */         case 'ʌ':
/*     */         case 'ᵥ':
/*     */         case 'ᶌ':
/*     */         case 'ṽ':
/*     */         case 'ṿ':
/*     */         case 'ⓥ':
/*     */         case 'ⱱ':
/*     */         case 'ⱴ':
/*     */         case 'ꝟ':
/*     */         case 'ｖ':
/* 1350 */           this.output[(this.outputPos++)] = 'v';
/* 1351 */           break;
/*     */         case 'Ꝡ':
/* 1353 */           this.output[(this.outputPos++)] = 'V';
/* 1354 */           this.output[(this.outputPos++)] = 'Y';
/* 1355 */           break;
/*     */         case '⒱':
/* 1357 */           this.output[(this.outputPos++)] = '(';
/* 1358 */           this.output[(this.outputPos++)] = 'v';
/* 1359 */           this.output[(this.outputPos++)] = ')';
/* 1360 */           break;
/*     */         case 'ꝡ':
/* 1362 */           this.output[(this.outputPos++)] = 'v';
/* 1363 */           this.output[(this.outputPos++)] = 'y';
/* 1364 */           break;
/*     */         case 'Ŵ':
/*     */         case 'Ƿ':
/*     */         case 'ᴡ':
/*     */         case 'Ẁ':
/*     */         case 'Ẃ':
/*     */         case 'Ẅ':
/*     */         case 'Ẇ':
/*     */         case 'Ẉ':
/*     */         case 'Ⓦ':
/*     */         case 'Ⱳ':
/*     */         case 'Ｗ':
/* 1376 */           this.output[(this.outputPos++)] = 'W';
/* 1377 */           break;
/*     */         case 'ŵ':
/*     */         case 'ƿ':
/*     */         case 'ʍ':
/*     */         case 'ẁ':
/*     */         case 'ẃ':
/*     */         case 'ẅ':
/*     */         case 'ẇ':
/*     */         case 'ẉ':
/*     */         case 'ẘ':
/*     */         case 'ⓦ':
/*     */         case 'ⱳ':
/*     */         case 'ｗ':
/* 1390 */           this.output[(this.outputPos++)] = 'w';
/* 1391 */           break;
/*     */         case '⒲':
/* 1393 */           this.output[(this.outputPos++)] = '(';
/* 1394 */           this.output[(this.outputPos++)] = 'w';
/* 1395 */           this.output[(this.outputPos++)] = ')';
/* 1396 */           break;
/*     */         case 'Ẋ':
/*     */         case 'Ẍ':
/*     */         case 'Ⓧ':
/*     */         case 'Ｘ':
/* 1401 */           this.output[(this.outputPos++)] = 'X';
/* 1402 */           break;
/*     */         case 'ᶍ':
/*     */         case 'ẋ':
/*     */         case 'ẍ':
/*     */         case 'ₓ':
/*     */         case 'ⓧ':
/*     */         case 'ｘ':
/* 1409 */           this.output[(this.outputPos++)] = 'x';
/* 1410 */           break;
/*     */         case '⒳':
/* 1412 */           this.output[(this.outputPos++)] = '(';
/* 1413 */           this.output[(this.outputPos++)] = 'x';
/* 1414 */           this.output[(this.outputPos++)] = ')';
/* 1415 */           break;
/*     */         case 'Ý':
/*     */         case 'Ŷ':
/*     */         case 'Ÿ':
/*     */         case 'Ƴ':
/*     */         case 'Ȳ':
/*     */         case 'Ɏ':
/*     */         case 'ʏ':
/*     */         case 'Ẏ':
/*     */         case 'Ỳ':
/*     */         case 'Ỵ':
/*     */         case 'Ỷ':
/*     */         case 'Ỹ':
/*     */         case 'Ỿ':
/*     */         case 'Ⓨ':
/*     */         case 'Ｙ':
/* 1431 */           this.output[(this.outputPos++)] = 'Y';
/* 1432 */           break;
/*     */         case 'ý':
/*     */         case 'ÿ':
/*     */         case 'ŷ':
/*     */         case 'ƴ':
/*     */         case 'ȳ':
/*     */         case 'ɏ':
/*     */         case 'ʎ':
/*     */         case 'ẏ':
/*     */         case 'ẙ':
/*     */         case 'ỳ':
/*     */         case 'ỵ':
/*     */         case 'ỷ':
/*     */         case 'ỹ':
/*     */         case 'ỿ':
/*     */         case 'ⓨ':
/*     */         case 'ｙ':
/* 1449 */           this.output[(this.outputPos++)] = 'y';
/* 1450 */           break;
/*     */         case '⒴':
/* 1452 */           this.output[(this.outputPos++)] = '(';
/* 1453 */           this.output[(this.outputPos++)] = 'y';
/* 1454 */           this.output[(this.outputPos++)] = ')';
/* 1455 */           break;
/*     */         case 'Ź':
/*     */         case 'Ż':
/*     */         case 'Ž':
/*     */         case 'Ƶ':
/*     */         case 'Ȝ':
/*     */         case 'Ȥ':
/*     */         case 'ᴢ':
/*     */         case 'Ẑ':
/*     */         case 'Ẓ':
/*     */         case 'Ẕ':
/*     */         case 'Ⓩ':
/*     */         case 'Ⱬ':
/*     */         case 'Ꝣ':
/*     */         case 'Ｚ':
/* 1470 */           this.output[(this.outputPos++)] = 'Z';
/* 1471 */           break;
/*     */         case 'ź':
/*     */         case 'ż':
/*     */         case 'ž':
/*     */         case 'ƶ':
/*     */         case 'ȝ':
/*     */         case 'ȥ':
/*     */         case 'ɀ':
/*     */         case 'ʐ':
/*     */         case 'ʑ':
/*     */         case 'ᵶ':
/*     */         case 'ᶎ':
/*     */         case 'ẑ':
/*     */         case 'ẓ':
/*     */         case 'ẕ':
/*     */         case 'ⓩ':
/*     */         case 'ⱬ':
/*     */         case 'ꝣ':
/*     */         case 'ｚ':
/* 1490 */           this.output[(this.outputPos++)] = 'z';
/* 1491 */           break;
/*     */         case '⒵':
/* 1493 */           this.output[(this.outputPos++)] = '(';
/* 1494 */           this.output[(this.outputPos++)] = 'z';
/* 1495 */           this.output[(this.outputPos++)] = ')';
/* 1496 */           break;
/*     */         case '⁰':
/*     */         case '₀':
/*     */         case '⓪':
/*     */         case '⓿':
/*     */         case '０':
/* 1502 */           this.output[(this.outputPos++)] = '0';
/* 1503 */           break;
/*     */         case '¹':
/*     */         case '₁':
/*     */         case '①':
/*     */         case '⓵':
/*     */         case '❶':
/*     */         case '➀':
/*     */         case '➊':
/*     */         case '１':
/* 1512 */           this.output[(this.outputPos++)] = '1';
/* 1513 */           break;
/*     */         case '⒈':
/* 1515 */           this.output[(this.outputPos++)] = '1';
/* 1516 */           this.output[(this.outputPos++)] = '.';
/* 1517 */           break;
/*     */         case '⑴':
/* 1519 */           this.output[(this.outputPos++)] = '(';
/* 1520 */           this.output[(this.outputPos++)] = '1';
/* 1521 */           this.output[(this.outputPos++)] = ')';
/* 1522 */           break;
/*     */         case '²':
/*     */         case '₂':
/*     */         case '②':
/*     */         case '⓶':
/*     */         case '❷':
/*     */         case '➁':
/*     */         case '➋':
/*     */         case '２':
/* 1531 */           this.output[(this.outputPos++)] = '2';
/* 1532 */           break;
/*     */         case '⒉':
/* 1534 */           this.output[(this.outputPos++)] = '2';
/* 1535 */           this.output[(this.outputPos++)] = '.';
/* 1536 */           break;
/*     */         case '⑵':
/* 1538 */           this.output[(this.outputPos++)] = '(';
/* 1539 */           this.output[(this.outputPos++)] = '2';
/* 1540 */           this.output[(this.outputPos++)] = ')';
/* 1541 */           break;
/*     */         case '³':
/*     */         case '₃':
/*     */         case '③':
/*     */         case '⓷':
/*     */         case '❸':
/*     */         case '➂':
/*     */         case '➌':
/*     */         case '３':
/* 1550 */           this.output[(this.outputPos++)] = '3';
/* 1551 */           break;
/*     */         case '⒊':
/* 1553 */           this.output[(this.outputPos++)] = '3';
/* 1554 */           this.output[(this.outputPos++)] = '.';
/* 1555 */           break;
/*     */         case '⑶':
/* 1557 */           this.output[(this.outputPos++)] = '(';
/* 1558 */           this.output[(this.outputPos++)] = '3';
/* 1559 */           this.output[(this.outputPos++)] = ')';
/* 1560 */           break;
/*     */         case '⁴':
/*     */         case '₄':
/*     */         case '④':
/*     */         case '⓸':
/*     */         case '❹':
/*     */         case '➃':
/*     */         case '➍':
/*     */         case '４':
/* 1569 */           this.output[(this.outputPos++)] = '4';
/* 1570 */           break;
/*     */         case '⒋':
/* 1572 */           this.output[(this.outputPos++)] = '4';
/* 1573 */           this.output[(this.outputPos++)] = '.';
/* 1574 */           break;
/*     */         case '⑷':
/* 1576 */           this.output[(this.outputPos++)] = '(';
/* 1577 */           this.output[(this.outputPos++)] = '4';
/* 1578 */           this.output[(this.outputPos++)] = ')';
/* 1579 */           break;
/*     */         case '⁵':
/*     */         case '₅':
/*     */         case '⑤':
/*     */         case '⓹':
/*     */         case '❺':
/*     */         case '➄':
/*     */         case '➎':
/*     */         case '５':
/* 1588 */           this.output[(this.outputPos++)] = '5';
/* 1589 */           break;
/*     */         case '⒌':
/* 1591 */           this.output[(this.outputPos++)] = '5';
/* 1592 */           this.output[(this.outputPos++)] = '.';
/* 1593 */           break;
/*     */         case '⑸':
/* 1595 */           this.output[(this.outputPos++)] = '(';
/* 1596 */           this.output[(this.outputPos++)] = '5';
/* 1597 */           this.output[(this.outputPos++)] = ')';
/* 1598 */           break;
/*     */         case '⁶':
/*     */         case '₆':
/*     */         case '⑥':
/*     */         case '⓺':
/*     */         case '❻':
/*     */         case '➅':
/*     */         case '➏':
/*     */         case '６':
/* 1607 */           this.output[(this.outputPos++)] = '6';
/* 1608 */           break;
/*     */         case '⒍':
/* 1610 */           this.output[(this.outputPos++)] = '6';
/* 1611 */           this.output[(this.outputPos++)] = '.';
/* 1612 */           break;
/*     */         case '⑹':
/* 1614 */           this.output[(this.outputPos++)] = '(';
/* 1615 */           this.output[(this.outputPos++)] = '6';
/* 1616 */           this.output[(this.outputPos++)] = ')';
/* 1617 */           break;
/*     */         case '⁷':
/*     */         case '₇':
/*     */         case '⑦':
/*     */         case '⓻':
/*     */         case '❼':
/*     */         case '➆':
/*     */         case '➐':
/*     */         case '７':
/* 1626 */           this.output[(this.outputPos++)] = '7';
/* 1627 */           break;
/*     */         case '⒎':
/* 1629 */           this.output[(this.outputPos++)] = '7';
/* 1630 */           this.output[(this.outputPos++)] = '.';
/* 1631 */           break;
/*     */         case '⑺':
/* 1633 */           this.output[(this.outputPos++)] = '(';
/* 1634 */           this.output[(this.outputPos++)] = '7';
/* 1635 */           this.output[(this.outputPos++)] = ')';
/* 1636 */           break;
/*     */         case '⁸':
/*     */         case '₈':
/*     */         case '⑧':
/*     */         case '⓼':
/*     */         case '❽':
/*     */         case '➇':
/*     */         case '➑':
/*     */         case '８':
/* 1645 */           this.output[(this.outputPos++)] = '8';
/* 1646 */           break;
/*     */         case '⒏':
/* 1648 */           this.output[(this.outputPos++)] = '8';
/* 1649 */           this.output[(this.outputPos++)] = '.';
/* 1650 */           break;
/*     */         case '⑻':
/* 1652 */           this.output[(this.outputPos++)] = '(';
/* 1653 */           this.output[(this.outputPos++)] = '8';
/* 1654 */           this.output[(this.outputPos++)] = ')';
/* 1655 */           break;
/*     */         case '⁹':
/*     */         case '₉':
/*     */         case '⑨':
/*     */         case '⓽':
/*     */         case '❾':
/*     */         case '➈':
/*     */         case '➒':
/*     */         case '９':
/* 1664 */           this.output[(this.outputPos++)] = '9';
/* 1665 */           break;
/*     */         case '⒐':
/* 1667 */           this.output[(this.outputPos++)] = '9';
/* 1668 */           this.output[(this.outputPos++)] = '.';
/* 1669 */           break;
/*     */         case '⑼':
/* 1671 */           this.output[(this.outputPos++)] = '(';
/* 1672 */           this.output[(this.outputPos++)] = '9';
/* 1673 */           this.output[(this.outputPos++)] = ')';
/* 1674 */           break;
/*     */         case '⑩':
/*     */         case '⓾':
/*     */         case '❿':
/*     */         case '➉':
/*     */         case '➓':
/* 1680 */           this.output[(this.outputPos++)] = '1';
/* 1681 */           this.output[(this.outputPos++)] = '0';
/* 1682 */           break;
/*     */         case '⒑':
/* 1684 */           this.output[(this.outputPos++)] = '1';
/* 1685 */           this.output[(this.outputPos++)] = '0';
/* 1686 */           this.output[(this.outputPos++)] = '.';
/* 1687 */           break;
/*     */         case '⑽':
/* 1689 */           this.output[(this.outputPos++)] = '(';
/* 1690 */           this.output[(this.outputPos++)] = '1';
/* 1691 */           this.output[(this.outputPos++)] = '0';
/* 1692 */           this.output[(this.outputPos++)] = ')';
/* 1693 */           break;
/*     */         case '⑪':
/*     */         case '⓫':
/* 1696 */           this.output[(this.outputPos++)] = '1';
/* 1697 */           this.output[(this.outputPos++)] = '1';
/* 1698 */           break;
/*     */         case '⒒':
/* 1700 */           this.output[(this.outputPos++)] = '1';
/* 1701 */           this.output[(this.outputPos++)] = '1';
/* 1702 */           this.output[(this.outputPos++)] = '.';
/* 1703 */           break;
/*     */         case '⑾':
/* 1705 */           this.output[(this.outputPos++)] = '(';
/* 1706 */           this.output[(this.outputPos++)] = '1';
/* 1707 */           this.output[(this.outputPos++)] = '1';
/* 1708 */           this.output[(this.outputPos++)] = ')';
/* 1709 */           break;
/*     */         case '⑫':
/*     */         case '⓬':
/* 1712 */           this.output[(this.outputPos++)] = '1';
/* 1713 */           this.output[(this.outputPos++)] = '2';
/* 1714 */           break;
/*     */         case '⒓':
/* 1716 */           this.output[(this.outputPos++)] = '1';
/* 1717 */           this.output[(this.outputPos++)] = '2';
/* 1718 */           this.output[(this.outputPos++)] = '.';
/* 1719 */           break;
/*     */         case '⑿':
/* 1721 */           this.output[(this.outputPos++)] = '(';
/* 1722 */           this.output[(this.outputPos++)] = '1';
/* 1723 */           this.output[(this.outputPos++)] = '2';
/* 1724 */           this.output[(this.outputPos++)] = ')';
/* 1725 */           break;
/*     */         case '⑬':
/*     */         case '⓭':
/* 1728 */           this.output[(this.outputPos++)] = '1';
/* 1729 */           this.output[(this.outputPos++)] = '3';
/* 1730 */           break;
/*     */         case '⒔':
/* 1732 */           this.output[(this.outputPos++)] = '1';
/* 1733 */           this.output[(this.outputPos++)] = '3';
/* 1734 */           this.output[(this.outputPos++)] = '.';
/* 1735 */           break;
/*     */         case '⒀':
/* 1737 */           this.output[(this.outputPos++)] = '(';
/* 1738 */           this.output[(this.outputPos++)] = '1';
/* 1739 */           this.output[(this.outputPos++)] = '3';
/* 1740 */           this.output[(this.outputPos++)] = ')';
/* 1741 */           break;
/*     */         case '⑭':
/*     */         case '⓮':
/* 1744 */           this.output[(this.outputPos++)] = '1';
/* 1745 */           this.output[(this.outputPos++)] = '4';
/* 1746 */           break;
/*     */         case '⒕':
/* 1748 */           this.output[(this.outputPos++)] = '1';
/* 1749 */           this.output[(this.outputPos++)] = '4';
/* 1750 */           this.output[(this.outputPos++)] = '.';
/* 1751 */           break;
/*     */         case '⒁':
/* 1753 */           this.output[(this.outputPos++)] = '(';
/* 1754 */           this.output[(this.outputPos++)] = '1';
/* 1755 */           this.output[(this.outputPos++)] = '4';
/* 1756 */           this.output[(this.outputPos++)] = ')';
/* 1757 */           break;
/*     */         case '⑮':
/*     */         case '⓯':
/* 1760 */           this.output[(this.outputPos++)] = '1';
/* 1761 */           this.output[(this.outputPos++)] = '5';
/* 1762 */           break;
/*     */         case '⒖':
/* 1764 */           this.output[(this.outputPos++)] = '1';
/* 1765 */           this.output[(this.outputPos++)] = '5';
/* 1766 */           this.output[(this.outputPos++)] = '.';
/* 1767 */           break;
/*     */         case '⒂':
/* 1769 */           this.output[(this.outputPos++)] = '(';
/* 1770 */           this.output[(this.outputPos++)] = '1';
/* 1771 */           this.output[(this.outputPos++)] = '5';
/* 1772 */           this.output[(this.outputPos++)] = ')';
/* 1773 */           break;
/*     */         case '⑯':
/*     */         case '⓰':
/* 1776 */           this.output[(this.outputPos++)] = '1';
/* 1777 */           this.output[(this.outputPos++)] = '6';
/* 1778 */           break;
/*     */         case '⒗':
/* 1780 */           this.output[(this.outputPos++)] = '1';
/* 1781 */           this.output[(this.outputPos++)] = '6';
/* 1782 */           this.output[(this.outputPos++)] = '.';
/* 1783 */           break;
/*     */         case '⒃':
/* 1785 */           this.output[(this.outputPos++)] = '(';
/* 1786 */           this.output[(this.outputPos++)] = '1';
/* 1787 */           this.output[(this.outputPos++)] = '6';
/* 1788 */           this.output[(this.outputPos++)] = ')';
/* 1789 */           break;
/*     */         case '⑰':
/*     */         case '⓱':
/* 1792 */           this.output[(this.outputPos++)] = '1';
/* 1793 */           this.output[(this.outputPos++)] = '7';
/* 1794 */           break;
/*     */         case '⒘':
/* 1796 */           this.output[(this.outputPos++)] = '1';
/* 1797 */           this.output[(this.outputPos++)] = '7';
/* 1798 */           this.output[(this.outputPos++)] = '.';
/* 1799 */           break;
/*     */         case '⒄':
/* 1801 */           this.output[(this.outputPos++)] = '(';
/* 1802 */           this.output[(this.outputPos++)] = '1';
/* 1803 */           this.output[(this.outputPos++)] = '7';
/* 1804 */           this.output[(this.outputPos++)] = ')';
/* 1805 */           break;
/*     */         case '⑱':
/*     */         case '⓲':
/* 1808 */           this.output[(this.outputPos++)] = '1';
/* 1809 */           this.output[(this.outputPos++)] = '8';
/* 1810 */           break;
/*     */         case '⒙':
/* 1812 */           this.output[(this.outputPos++)] = '1';
/* 1813 */           this.output[(this.outputPos++)] = '8';
/* 1814 */           this.output[(this.outputPos++)] = '.';
/* 1815 */           break;
/*     */         case '⒅':
/* 1817 */           this.output[(this.outputPos++)] = '(';
/* 1818 */           this.output[(this.outputPos++)] = '1';
/* 1819 */           this.output[(this.outputPos++)] = '8';
/* 1820 */           this.output[(this.outputPos++)] = ')';
/* 1821 */           break;
/*     */         case '⑲':
/*     */         case '⓳':
/* 1824 */           this.output[(this.outputPos++)] = '1';
/* 1825 */           this.output[(this.outputPos++)] = '9';
/* 1826 */           break;
/*     */         case '⒚':
/* 1828 */           this.output[(this.outputPos++)] = '1';
/* 1829 */           this.output[(this.outputPos++)] = '9';
/* 1830 */           this.output[(this.outputPos++)] = '.';
/* 1831 */           break;
/*     */         case '⒆':
/* 1833 */           this.output[(this.outputPos++)] = '(';
/* 1834 */           this.output[(this.outputPos++)] = '1';
/* 1835 */           this.output[(this.outputPos++)] = '9';
/* 1836 */           this.output[(this.outputPos++)] = ')';
/* 1837 */           break;
/*     */         case '⑳':
/*     */         case '⓴':
/* 1840 */           this.output[(this.outputPos++)] = '2';
/* 1841 */           this.output[(this.outputPos++)] = '0';
/* 1842 */           break;
/*     */         case '⒛':
/* 1844 */           this.output[(this.outputPos++)] = '2';
/* 1845 */           this.output[(this.outputPos++)] = '0';
/* 1846 */           this.output[(this.outputPos++)] = '.';
/* 1847 */           break;
/*     */         case '⒇':
/* 1849 */           this.output[(this.outputPos++)] = '(';
/* 1850 */           this.output[(this.outputPos++)] = '2';
/* 1851 */           this.output[(this.outputPos++)] = '0';
/* 1852 */           this.output[(this.outputPos++)] = ')';
/* 1853 */           break;
/*     */         case '«':
/*     */         case '»':
/*     */         case '“':
/*     */         case '”':
/*     */         case '„':
/*     */         case '″':
/*     */         case '‶':
/*     */         case '❝':
/*     */         case '❞':
/*     */         case '❮':
/*     */         case '❯':
/*     */         case '＂':
/* 1866 */           this.output[(this.outputPos++)] = '"';
/* 1867 */           break;
/*     */         case '‘':
/*     */         case '’':
/*     */         case '‚':
/*     */         case '‛':
/*     */         case '′':
/*     */         case '‵':
/*     */         case '‹':
/*     */         case '›':
/*     */         case '❛':
/*     */         case '❜':
/*     */         case '＇':
/* 1879 */           this.output[(this.outputPos++)] = '\'';
/* 1880 */           break;
/*     */         case '‐':
/*     */         case '‑':
/*     */         case '‒':
/*     */         case '–':
/*     */         case '—':
/*     */         case '⁻':
/*     */         case '₋':
/*     */         case '－':
/* 1889 */           this.output[(this.outputPos++)] = '-';
/* 1890 */           break;
/*     */         case '⁅':
/*     */         case '❲':
/*     */         case '［':
/* 1894 */           this.output[(this.outputPos++)] = '[';
/* 1895 */           break;
/*     */         case '⁆':
/*     */         case '❳':
/*     */         case '］':
/* 1899 */           this.output[(this.outputPos++)] = ']';
/* 1900 */           break;
/*     */         case '⁽':
/*     */         case '₍':
/*     */         case '❨':
/*     */         case '❪':
/*     */         case '（':
/* 1906 */           this.output[(this.outputPos++)] = '(';
/* 1907 */           break;
/*     */         case '⸨':
/* 1909 */           this.output[(this.outputPos++)] = '(';
/* 1910 */           this.output[(this.outputPos++)] = '(';
/* 1911 */           break;
/*     */         case '⁾':
/*     */         case '₎':
/*     */         case '❩':
/*     */         case '❫':
/*     */         case '）':
/* 1917 */           this.output[(this.outputPos++)] = ')';
/* 1918 */           break;
/*     */         case '⸩':
/* 1920 */           this.output[(this.outputPos++)] = ')';
/* 1921 */           this.output[(this.outputPos++)] = ')';
/* 1922 */           break;
/*     */         case '❬':
/*     */         case '❰':
/*     */         case '＜':
/* 1926 */           this.output[(this.outputPos++)] = '<';
/* 1927 */           break;
/*     */         case '❭':
/*     */         case '❱':
/*     */         case '＞':
/* 1931 */           this.output[(this.outputPos++)] = '>';
/* 1932 */           break;
/*     */         case '❴':
/*     */         case '｛':
/* 1935 */           this.output[(this.outputPos++)] = '{';
/* 1936 */           break;
/*     */         case '❵':
/*     */         case '｝':
/* 1939 */           this.output[(this.outputPos++)] = '}';
/* 1940 */           break;
/*     */         case '⁺':
/*     */         case '₊':
/*     */         case '＋':
/* 1944 */           this.output[(this.outputPos++)] = '+';
/* 1945 */           break;
/*     */         case '⁼':
/*     */         case '₌':
/*     */         case '＝':
/* 1949 */           this.output[(this.outputPos++)] = '=';
/* 1950 */           break;
/*     */         case '！':
/* 1952 */           this.output[(this.outputPos++)] = '!';
/* 1953 */           break;
/*     */         case '‼':
/* 1955 */           this.output[(this.outputPos++)] = '!';
/* 1956 */           this.output[(this.outputPos++)] = '!';
/* 1957 */           break;
/*     */         case '⁉':
/* 1959 */           this.output[(this.outputPos++)] = '!';
/* 1960 */           this.output[(this.outputPos++)] = '?';
/* 1961 */           break;
/*     */         case '＃':
/* 1963 */           this.output[(this.outputPos++)] = '#';
/* 1964 */           break;
/*     */         case '＄':
/* 1966 */           this.output[(this.outputPos++)] = '$';
/* 1967 */           break;
/*     */         case '⁒':
/*     */         case '％':
/* 1970 */           this.output[(this.outputPos++)] = '%';
/* 1971 */           break;
/*     */         case '＆':
/* 1973 */           this.output[(this.outputPos++)] = '&';
/* 1974 */           break;
/*     */         case '⁎':
/*     */         case '＊':
/* 1977 */           this.output[(this.outputPos++)] = '*';
/* 1978 */           break;
/*     */         case '，':
/* 1980 */           this.output[(this.outputPos++)] = ',';
/* 1981 */           break;
/*     */         case '．':
/* 1983 */           this.output[(this.outputPos++)] = '.';
/* 1984 */           break;
/*     */         case '⁄':
/*     */         case '／':
/* 1987 */           this.output[(this.outputPos++)] = '/';
/* 1988 */           break;
/*     */         case '：':
/* 1990 */           this.output[(this.outputPos++)] = ':';
/* 1991 */           break;
/*     */         case '⁏':
/*     */         case '；':
/* 1994 */           this.output[(this.outputPos++)] = ';';
/* 1995 */           break;
/*     */         case '？':
/* 1997 */           this.output[(this.outputPos++)] = '?';
/* 1998 */           break;
/*     */         case '⁇':
/* 2000 */           this.output[(this.outputPos++)] = '?';
/* 2001 */           this.output[(this.outputPos++)] = '?';
/* 2002 */           break;
/*     */         case '⁈':
/* 2004 */           this.output[(this.outputPos++)] = '?';
/* 2005 */           this.output[(this.outputPos++)] = '!';
/* 2006 */           break;
/*     */         case '＠':
/* 2008 */           this.output[(this.outputPos++)] = '@';
/* 2009 */           break;
/*     */         case '＼':
/* 2011 */           this.output[(this.outputPos++)] = '\\';
/* 2012 */           break;
/*     */         case '‸':
/*     */         case '＾':
/* 2015 */           this.output[(this.outputPos++)] = '^';
/* 2016 */           break;
/*     */         case '＿':
/* 2018 */           this.output[(this.outputPos++)] = '_';
/* 2019 */           break;
/*     */         case '⁓':
/*     */         case '～':
/* 2022 */           this.output[(this.outputPos++)] = '~';
/* 2023 */           break;
/*     */         default:
/* 2025 */           this.output[(this.outputPos++)] = c;
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ASCIIFoldingFilter
 * JD-Core Version:    0.6.2
 */