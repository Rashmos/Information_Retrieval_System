/*     */ package org.apache.lucene.analysis.fr;
/*     */ 
/*     */ @Deprecated
/*     */ public class FrenchStemmer
/*     */ {
/*  37 */   private StringBuilder sb = new StringBuilder();
/*     */ 
/*  42 */   private StringBuilder tb = new StringBuilder();
/*     */   private String R0;
/*     */   private String RV;
/*     */   private String R1;
/*     */   private String R2;
/*     */   private boolean suite;
/*     */   private boolean modified;
/*     */ 
/*     */   protected String stem(String term)
/*     */   {
/*  90 */     if (!isStemmable(term)) {
/*  91 */       return term;
/*     */     }
/*     */ 
/*  95 */     term = term.toLowerCase();
/*     */ 
/*  98 */     this.sb.delete(0, this.sb.length());
/*  99 */     this.sb.insert(0, term);
/*     */ 
/* 102 */     this.modified = false;
/* 103 */     this.suite = false;
/*     */ 
/* 105 */     this.sb = treatVowels(this.sb);
/*     */ 
/* 107 */     setStrings();
/*     */ 
/* 109 */     step1();
/*     */ 
/* 111 */     if ((!this.modified) || (this.suite))
/*     */     {
/* 113 */       if (this.RV != null)
/*     */       {
/* 115 */         this.suite = step2a();
/* 116 */         if (!this.suite) {
/* 117 */           step2b();
/*     */         }
/*     */       }
/*     */     }
/* 121 */     if ((this.modified) || (this.suite))
/* 122 */       step3();
/*     */     else {
/* 124 */       step4();
/*     */     }
/* 126 */     step5();
/*     */ 
/* 128 */     step6();
/*     */ 
/* 130 */     return this.sb.toString();
/*     */   }
/*     */ 
/*     */   private void setStrings()
/*     */   {
/* 139 */     this.R0 = this.sb.toString();
/* 140 */     this.RV = retrieveRV(this.sb);
/* 141 */     this.R1 = retrieveR(this.sb);
/* 142 */     if (this.R1 != null)
/*     */     {
/* 144 */       this.tb.delete(0, this.tb.length());
/* 145 */       this.tb.insert(0, this.R1);
/* 146 */       this.R2 = retrieveR(this.tb);
/*     */     }
/*     */     else {
/* 149 */       this.R2 = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void step1()
/*     */   {
/* 157 */     String[] suffix = { "ances", "iqUes", "ismes", "ables", "istes", "ance", "iqUe", "isme", "able", "iste" };
/* 158 */     deleteFrom(this.R2, suffix);
/*     */ 
/* 160 */     replaceFrom(this.R2, new String[] { "logies", "logie" }, "log");
/* 161 */     replaceFrom(this.R2, new String[] { "usions", "utions", "usion", "ution" }, "u");
/* 162 */     replaceFrom(this.R2, new String[] { "ences", "ence" }, "ent");
/*     */ 
/* 164 */     String[] search = { "atrices", "ateurs", "ations", "atrice", "ateur", "ation" };
/* 165 */     deleteButSuffixFromElseReplace(this.R2, search, "ic", true, this.R0, "iqU");
/*     */ 
/* 167 */     deleteButSuffixFromElseReplace(this.R2, new String[] { "ements", "ement" }, "eus", false, this.R0, "eux");
/* 168 */     deleteButSuffixFrom(this.R2, new String[] { "ements", "ement" }, "ativ", false);
/* 169 */     deleteButSuffixFrom(this.R2, new String[] { "ements", "ement" }, "iv", false);
/* 170 */     deleteButSuffixFrom(this.R2, new String[] { "ements", "ement" }, "abl", false);
/* 171 */     deleteButSuffixFrom(this.R2, new String[] { "ements", "ement" }, "iqU", false);
/*     */ 
/* 173 */     deleteFromIfTestVowelBeforeIn(this.R1, new String[] { "issements", "issement" }, false, this.R0);
/* 174 */     deleteFrom(this.RV, new String[] { "ements", "ement" });
/*     */ 
/* 176 */     deleteButSuffixFromElseReplace(this.R2, new String[] { "ités", "ité" }, "abil", false, this.R0, "abl");
/* 177 */     deleteButSuffixFromElseReplace(this.R2, new String[] { "ités", "ité" }, "ic", false, this.R0, "iqU");
/* 178 */     deleteButSuffixFrom(this.R2, new String[] { "ités", "ité" }, "iv", true);
/*     */ 
/* 180 */     String[] autre = { "ifs", "ives", "if", "ive" };
/* 181 */     deleteButSuffixFromElseReplace(this.R2, autre, "icat", false, this.R0, "iqU");
/* 182 */     deleteButSuffixFromElseReplace(this.R2, autre, "at", true, this.R2, "iqU");
/*     */ 
/* 184 */     replaceFrom(this.R0, new String[] { "eaux" }, "eau");
/*     */ 
/* 186 */     replaceFrom(this.R1, new String[] { "aux" }, "al");
/*     */ 
/* 188 */     deleteButSuffixFromElseReplace(this.R2, new String[] { "euses", "euse" }, "", true, this.R1, "eux");
/*     */ 
/* 190 */     deleteFrom(this.R2, new String[] { "eux" });
/*     */ 
/* 193 */     boolean temp = false;
/* 194 */     temp = replaceFrom(this.RV, new String[] { "amment" }, "ant");
/* 195 */     if (temp == true)
/* 196 */       this.suite = true;
/* 197 */     temp = replaceFrom(this.RV, new String[] { "emment" }, "ent");
/* 198 */     if (temp == true)
/* 199 */       this.suite = true;
/* 200 */     temp = deleteFromIfTestVowelBeforeIn(this.RV, new String[] { "ments", "ment" }, true, this.RV);
/* 201 */     if (temp == true)
/* 202 */       this.suite = true;
/*     */   }
/*     */ 
/*     */   private boolean step2a()
/*     */   {
/* 215 */     String[] search = { "îmes", "îtes", "iraIent", "irait", "irais", "irai", "iras", "ira", "irent", "iriez", "irez", "irions", "irons", "iront", "issaIent", "issais", "issantes", "issante", "issants", "issant", "issait", "issais", "issions", "issons", "issiez", "issez", "issent", "isses", "isse", "ir", "is", "ît", "it", "ies", "ie", "i" };
/*     */ 
/* 220 */     return deleteFromIfTestVowelBeforeIn(this.RV, search, false, this.RV);
/*     */   }
/*     */ 
/*     */   private void step2b()
/*     */   {
/* 229 */     String[] suffix = { "eraIent", "erais", "erait", "erai", "eras", "erions", "eriez", "erons", "eront", "erez", "èrent", "era", "ées", "iez", "ée", "és", "er", "ez", "é" };
/*     */ 
/* 232 */     deleteFrom(this.RV, suffix);
/*     */ 
/* 234 */     String[] search = { "assions", "assiez", "assent", "asses", "asse", "aIent", "antes", "aIent", "Aient", "ante", "âmes", "âtes", "ants", "ant", "ait", "aît", "ais", "Ait", "Aît", "Ais", "ât", "as", "ai", "Ai", "a" };
/*     */ 
/* 237 */     deleteButSuffixFrom(this.RV, search, "e", true);
/*     */ 
/* 239 */     deleteFrom(this.R2, new String[] { "ions" });
/*     */   }
/*     */ 
/*     */   private void step3()
/*     */   {
/* 247 */     if (this.sb.length() > 0)
/*     */     {
/* 249 */       char ch = this.sb.charAt(this.sb.length() - 1);
/* 250 */       if (ch == 'Y')
/*     */       {
/* 252 */         this.sb.setCharAt(this.sb.length() - 1, 'i');
/* 253 */         setStrings();
/*     */       }
/* 255 */       else if (ch == 'ç')
/*     */       {
/* 257 */         this.sb.setCharAt(this.sb.length() - 1, 'c');
/* 258 */         setStrings();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void step4()
/*     */   {
/* 268 */     if (this.sb.length() > 1)
/*     */     {
/* 270 */       char ch = this.sb.charAt(this.sb.length() - 1);
/* 271 */       if (ch == 's')
/*     */       {
/* 273 */         char b = this.sb.charAt(this.sb.length() - 2);
/* 274 */         if ((b != 'a') && (b != 'i') && (b != 'o') && (b != 'u') && (b != 'è') && (b != 's'))
/*     */         {
/* 276 */           this.sb.delete(this.sb.length() - 1, this.sb.length());
/* 277 */           setStrings();
/*     */         }
/*     */       }
/*     */     }
/* 281 */     boolean found = deleteFromIfPrecededIn(this.R2, new String[] { "ion" }, this.RV, "s");
/* 282 */     if (!found) {
/* 283 */       found = deleteFromIfPrecededIn(this.R2, new String[] { "ion" }, this.RV, "t");
/*     */     }
/* 285 */     replaceFrom(this.RV, new String[] { "Ière", "ière", "Ier", "ier" }, "i");
/* 286 */     deleteFrom(this.RV, new String[] { "e" });
/* 287 */     deleteFromIfPrecededIn(this.RV, new String[] { "ë" }, this.R0, "gu");
/*     */   }
/*     */ 
/*     */   private void step5()
/*     */   {
/* 295 */     if (this.R0 != null)
/*     */     {
/* 297 */       if ((this.R0.endsWith("enn")) || (this.R0.endsWith("onn")) || (this.R0.endsWith("ett")) || (this.R0.endsWith("ell")) || (this.R0.endsWith("eill")))
/*     */       {
/* 299 */         this.sb.delete(this.sb.length() - 1, this.sb.length());
/* 300 */         setStrings();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void step6()
/*     */   {
/* 310 */     if ((this.R0 != null) && (this.R0.length() > 0))
/*     */     {
/* 312 */       boolean seenVowel = false;
/* 313 */       boolean seenConson = false;
/* 314 */       int pos = -1;
/* 315 */       for (int i = this.R0.length() - 1; i > -1; i--)
/*     */       {
/* 317 */         char ch = this.R0.charAt(i);
/* 318 */         if (isVowel(ch))
/*     */         {
/* 320 */           if (!seenVowel)
/*     */           {
/* 322 */             if ((ch == 'é') || (ch == 'è'))
/*     */             {
/* 324 */               pos = i;
/* 325 */               break;
/*     */             }
/*     */           }
/* 328 */           seenVowel = true;
/*     */         }
/*     */         else
/*     */         {
/* 332 */           if (seenVowel) {
/*     */             break;
/*     */           }
/* 335 */           seenConson = true;
/*     */         }
/*     */       }
/* 338 */       if ((pos > -1) && (seenConson) && (!seenVowel))
/* 339 */         this.sb.setCharAt(pos, 'e');
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean deleteFromIfPrecededIn(String source, String[] search, String from, String prefix)
/*     */   {
/* 353 */     boolean found = false;
/* 354 */     if (source != null)
/*     */     {
/* 356 */       for (int i = 0; i < search.length; i++) {
/* 357 */         if (source.endsWith(search[i]))
/*     */         {
/* 359 */           if ((from != null) && (from.endsWith(prefix + search[i])))
/*     */           {
/* 361 */             this.sb.delete(this.sb.length() - search[i].length(), this.sb.length());
/* 362 */             found = true;
/* 363 */             setStrings();
/* 364 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 369 */     return found;
/*     */   }
/*     */ 
/*     */   private boolean deleteFromIfTestVowelBeforeIn(String source, String[] search, boolean vowel, String from)
/*     */   {
/* 382 */     boolean found = false;
/* 383 */     if ((source != null) && (from != null))
/*     */     {
/* 385 */       for (int i = 0; i < search.length; i++) {
/* 386 */         if (source.endsWith(search[i]))
/*     */         {
/* 388 */           if (search[i].length() + 1 <= from.length())
/*     */           {
/* 390 */             boolean test = isVowel(this.sb.charAt(this.sb.length() - (search[i].length() + 1)));
/* 391 */             if (test == vowel)
/*     */             {
/* 393 */               this.sb.delete(this.sb.length() - search[i].length(), this.sb.length());
/* 394 */               this.modified = true;
/* 395 */               found = true;
/* 396 */               setStrings();
/* 397 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 403 */     return found;
/*     */   }
/*     */ 
/*     */   private void deleteButSuffixFrom(String source, String[] search, String prefix, boolean without)
/*     */   {
/* 415 */     if (source != null)
/*     */     {
/* 417 */       for (int i = 0; i < search.length; i++) {
/* 418 */         if (source.endsWith(prefix + search[i]))
/*     */         {
/* 420 */           this.sb.delete(this.sb.length() - (prefix.length() + search[i].length()), this.sb.length());
/* 421 */           this.modified = true;
/* 422 */           setStrings();
/* 423 */           break;
/*     */         }
/* 425 */         if ((without) && (source.endsWith(search[i])))
/*     */         {
/* 427 */           this.sb.delete(this.sb.length() - search[i].length(), this.sb.length());
/* 428 */           this.modified = true;
/* 429 */           setStrings();
/* 430 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void deleteButSuffixFromElseReplace(String source, String[] search, String prefix, boolean without, String from, String replace)
/*     */   {
/* 447 */     if (source != null)
/*     */     {
/* 449 */       for (int i = 0; i < search.length; i++) {
/* 450 */         if (source.endsWith(prefix + search[i]))
/*     */         {
/* 452 */           this.sb.delete(this.sb.length() - (prefix.length() + search[i].length()), this.sb.length());
/* 453 */           this.modified = true;
/* 454 */           setStrings();
/* 455 */           break;
/*     */         }
/* 457 */         if ((from != null) && (from.endsWith(prefix + search[i])))
/*     */         {
/* 459 */           this.sb.replace(this.sb.length() - (prefix.length() + search[i].length()), this.sb.length(), replace);
/* 460 */           this.modified = true;
/* 461 */           setStrings();
/* 462 */           break;
/*     */         }
/* 464 */         if ((without) && (source.endsWith(search[i])))
/*     */         {
/* 466 */           this.sb.delete(this.sb.length() - search[i].length(), this.sb.length());
/* 467 */           this.modified = true;
/* 468 */           setStrings();
/* 469 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean replaceFrom(String source, String[] search, String replace)
/*     */   {
/* 483 */     boolean found = false;
/* 484 */     if (source != null)
/*     */     {
/* 486 */       for (int i = 0; i < search.length; i++) {
/* 487 */         if (source.endsWith(search[i]))
/*     */         {
/* 489 */           this.sb.replace(this.sb.length() - search[i].length(), this.sb.length(), replace);
/* 490 */           this.modified = true;
/* 491 */           found = true;
/* 492 */           setStrings();
/* 493 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 497 */     return found;
/*     */   }
/*     */ 
/*     */   private void deleteFrom(String source, String[] suffix)
/*     */   {
/* 507 */     if (source != null)
/*     */     {
/* 509 */       for (int i = 0; i < suffix.length; i++)
/* 510 */         if (source.endsWith(suffix[i]))
/*     */         {
/* 512 */           this.sb.delete(this.sb.length() - suffix[i].length(), this.sb.length());
/* 513 */           this.modified = true;
/* 514 */           setStrings();
/* 515 */           break;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isVowel(char ch)
/*     */   {
/* 528 */     switch (ch)
/*     */     {
/*     */     case 'a':
/*     */     case 'e':
/*     */     case 'i':
/*     */     case 'o':
/*     */     case 'u':
/*     */     case 'y':
/*     */     case 'à':
/*     */     case 'â':
/*     */     case 'è':
/*     */     case 'é':
/*     */     case 'ê':
/*     */     case 'ë':
/*     */     case 'î':
/*     */     case 'ï':
/*     */     case 'ô':
/*     */     case 'ù':
/*     */     case 'û':
/*     */     case 'ü':
/* 548 */       return true;
/*     */     }
/* 550 */     return false;
/*     */   }
/*     */ 
/*     */   private String retrieveR(StringBuilder buffer)
/*     */   {
/* 562 */     int len = buffer.length();
/* 563 */     int pos = -1;
/* 564 */     for (int c = 0; c < len; c++) {
/* 565 */       if (isVowel(buffer.charAt(c)))
/*     */       {
/* 567 */         pos = c;
/* 568 */         break;
/*     */       }
/*     */     }
/* 571 */     if (pos > -1)
/*     */     {
/* 573 */       int consonne = -1;
/* 574 */       for (int c = pos; c < len; c++) {
/* 575 */         if (!isVowel(buffer.charAt(c)))
/*     */         {
/* 577 */           consonne = c;
/* 578 */           break;
/*     */         }
/*     */       }
/* 581 */       if ((consonne > -1) && (consonne + 1 < len)) {
/* 582 */         return buffer.substring(consonne + 1, len);
/*     */       }
/* 584 */       return null;
/*     */     }
/*     */ 
/* 587 */     return null;
/*     */   }
/*     */ 
/*     */   private String retrieveRV(StringBuilder buffer)
/*     */   {
/* 599 */     int len = buffer.length();
/* 600 */     if (buffer.length() > 3)
/*     */     {
/* 602 */       if ((isVowel(buffer.charAt(0))) && (isVowel(buffer.charAt(1)))) {
/* 603 */         return buffer.substring(3, len);
/*     */       }
/*     */ 
/* 607 */       int pos = 0;
/* 608 */       for (int c = 1; c < len; c++) {
/* 609 */         if (isVowel(buffer.charAt(c)))
/*     */         {
/* 611 */           pos = c;
/* 612 */           break;
/*     */         }
/*     */       }
/* 615 */       if (pos + 1 < len) {
/* 616 */         return buffer.substring(pos + 1, len);
/*     */       }
/* 618 */       return null;
/*     */     }
/*     */ 
/* 622 */     return null;
/*     */   }
/*     */ 
/*     */   private StringBuilder treatVowels(StringBuilder buffer)
/*     */   {
/* 636 */     for (int c = 0; c < buffer.length(); c++) {
/* 637 */       char ch = buffer.charAt(c);
/*     */ 
/* 639 */       if (c == 0)
/*     */       {
/* 641 */         if (buffer.length() > 1)
/*     */         {
/* 643 */           if ((ch == 'y') && (isVowel(buffer.charAt(c + 1))))
/* 644 */             buffer.setCharAt(c, 'Y');
/*     */         }
/*     */       }
/* 647 */       else if (c == buffer.length() - 1)
/*     */       {
/* 649 */         if ((ch == 'u') && (buffer.charAt(c - 1) == 'q'))
/* 650 */           buffer.setCharAt(c, 'U');
/* 651 */         if ((ch == 'y') && (isVowel(buffer.charAt(c - 1))))
/* 652 */           buffer.setCharAt(c, 'Y');
/*     */       }
/*     */       else
/*     */       {
/* 656 */         if (ch == 'u')
/*     */         {
/* 658 */           if (buffer.charAt(c - 1) == 'q')
/* 659 */             buffer.setCharAt(c, 'U');
/* 660 */           else if ((isVowel(buffer.charAt(c - 1))) && (isVowel(buffer.charAt(c + 1))))
/* 661 */             buffer.setCharAt(c, 'U');
/*     */         }
/* 663 */         if (ch == 'i')
/*     */         {
/* 665 */           if ((isVowel(buffer.charAt(c - 1))) && (isVowel(buffer.charAt(c + 1))))
/* 666 */             buffer.setCharAt(c, 'I');
/*     */         }
/* 668 */         if (ch == 'y')
/*     */         {
/* 670 */           if ((isVowel(buffer.charAt(c - 1))) || (isVowel(buffer.charAt(c + 1)))) {
/* 671 */             buffer.setCharAt(c, 'Y');
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 676 */     return buffer;
/*     */   }
/*     */ 
/*     */   private boolean isStemmable(String term)
/*     */   {
/* 685 */     boolean upper = false;
/* 686 */     int first = -1;
/* 687 */     for (int c = 0; c < term.length(); c++)
/*     */     {
/* 689 */       if (!Character.isLetter(term.charAt(c))) {
/* 690 */         return false;
/*     */       }
/*     */ 
/* 693 */       if (Character.isUpperCase(term.charAt(c))) {
/* 694 */         if (upper) {
/* 695 */           return false;
/*     */         }
/*     */ 
/* 700 */         first = c;
/* 701 */         upper = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 707 */     if (first > 0) {
/* 708 */       return false;
/*     */     }
/* 710 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.fr.FrenchStemmer
 * JD-Core Version:    0.6.2
 */