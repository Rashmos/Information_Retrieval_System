/*     */ package com.aliasi.tokenizer;
/*     */ 
/*     */ class PorterStemmer
/*     */ {
/*     */   private char[] b;
/*     */   private int i;
/*     */   private int i_end;
/*     */   private int j;
/*     */   private int k;
/*     */   private static final int INC = 50;
/*     */ 
/*     */   private PorterStemmer()
/*     */   {
/*  72 */     this.b = new char[50];
/*  73 */     this.i = 0;
/*  74 */     this.i_end = 0;
/*     */   }
/*     */ 
/*     */   private void add(char ch)
/*     */   {
/*  82 */     if (this.i == this.b.length) {
/*  83 */       char[] new_b = new char[this.i + 50];
/*  84 */       for (int c = 0; c < this.i; c++) new_b[c] = this.b[c];
/*  85 */       this.b = new_b;
/*     */     }
/*  87 */     this.b[(this.i++)] = ch;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  97 */     return new String(this.b, 0, this.i_end);
/*     */   }
/*     */ 
/*     */   private final boolean cons(int i)
/*     */   {
/* 129 */     switch (this.b[i]) {
/*     */     case 'a':
/* 131 */       return false;
/*     */     case 'e':
/* 132 */       return false;
/*     */     case 'i':
/* 133 */       return false;
/*     */     case 'o':
/* 134 */       return false;
/*     */     case 'u':
/* 135 */       return false;
/*     */     case 'y':
/* 136 */       return i == 0;
/* 137 */     }return true;
/*     */   }
/*     */ 
/*     */   private final int m()
/*     */   {
/* 152 */     int n = 0;
/* 153 */     int i = 0;
/*     */     while (true) {
/* 155 */       if (i > this.j) return n;
/* 156 */       if (!cons(i)) break; i++;
/*     */     }
/* 158 */     i++;
/*     */     while (true)
/*     */     {
/* 161 */       if (i > this.j) return n;
/* 162 */       if (!cons(i)) {
/* 163 */         i++;
/*     */       } else {
/* 165 */         i++;
/* 166 */         n++;
/*     */         while (true) {
/* 168 */           if (i > this.j) return n;
/* 169 */           if (!cons(i)) break;
/* 170 */           i++;
/*     */         }
/* 172 */         i++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final boolean vowelinstem() {
/* 178 */     for (int i = 0; i <= this.j; i++) if (!cons(i)) return true;
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */   private final boolean doublec(int j)
/*     */   {
/* 184 */     if (j < 1) return false;
/* 185 */     if (this.b[j] != this.b[(j - 1)]) return false;
/* 186 */     return cons(j);
/*     */   }
/*     */ 
/*     */   private final boolean cvc(int i)
/*     */   {
/* 198 */     if ((i < 2) || (!cons(i)) || (cons(i - 1)) || (!cons(i - 2))) return false;
/* 199 */     int ch = this.b[i];
/* 200 */     if ((ch == 119) || (ch == 120) || (ch == 121)) return false;
/*     */ 
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */   private final boolean ends(String s) {
/* 206 */     int l = s.length();
/* 207 */     int o = this.k - l + 1;
/* 208 */     if (o < 0) return false;
/* 209 */     for (int i = 0; i < l; i++) if (this.b[(o + i)] != s.charAt(i)) return false;
/* 210 */     this.j = (this.k - l);
/* 211 */     return true;
/*     */   }
/*     */ 
/*     */   private final void setto(String s)
/*     */   {
/* 217 */     int l = s.length();
/* 218 */     int o = this.j + 1;
/* 219 */     for (int i = 0; i < l; i++) this.b[(o + i)] = s.charAt(i);
/* 220 */     this.k = (this.j + l);
/*     */   }
/*     */ 
/*     */   private final void r(String s) {
/* 224 */     if (m() > 0) setto(s);
/*     */   }
/*     */ 
/*     */   private final void step1()
/*     */   {
/* 248 */     if (this.b[this.k] == 's') {
/* 249 */       if (ends("sses")) this.k -= 2;
/* 250 */       else if (ends("ies")) setto("i");
/* 251 */       else if (this.b[(this.k - 1)] != 's') this.k -= 1;
/*     */     }
/* 253 */     if (ends("eed")) { if (m() > 0) this.k -= 1; 
/*     */     }
/* 254 */     else if (((ends("ed")) || (ends("ing"))) && (vowelinstem())) {
/* 255 */       this.k = this.j;
/* 256 */       if (ends("at")) { setto("ate");
/* 257 */       } else if (ends("bl")) { setto("ble");
/* 258 */       } else if (ends("iz")) { setto("ize");
/* 259 */       } else if (doublec(this.k)) {
/* 260 */         this.k -= 1;
/* 261 */         int ch = this.b[this.k];
/* 262 */         if ((ch == 108) || (ch == 115) || (ch == 122)) this.k += 1;
/*     */ 
/*     */       }
/* 265 */       else if ((m() == 1) && (cvc(this.k))) { setto("e"); }
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void step2()
/*     */   {
/* 271 */     if ((ends("y")) && (vowelinstem())) this.b[this.k] = 'i';
/*     */   }
/*     */ 
/*     */   private final void step3()
/*     */   {
/* 276 */     if (this.k == 0) return; switch (this.b[(this.k - 1)]) {
/*     */     case 'a':
/* 278 */       if (ends("ational")) r("ate");
/* 279 */       else if (ends("tional")) r("tion"); break;
/*     */     case 'c':
/* 281 */       if (ends("enci")) r("ence");
/* 282 */       else if (ends("anci")) r("ance"); break;
/*     */     case 'e':
/* 284 */       if (ends("izer")) r("ize"); break;
/*     */     case 'l':
/* 286 */       if (ends("bli")) r("ble");
/* 287 */       else if (ends("alli")) r("al");
/* 288 */       else if (ends("entli")) r("ent");
/* 289 */       else if (ends("eli")) r("e");
/* 290 */       else if (ends("ousli")) r("ous"); break;
/*     */     case 'o':
/* 292 */       if (ends("ization")) r("ize");
/* 293 */       else if (ends("ation")) r("ate");
/* 294 */       else if (ends("ator")) r("ate"); break;
/*     */     case 's':
/* 296 */       if (ends("alism")) r("al");
/* 297 */       else if (ends("iveness")) r("ive");
/* 298 */       else if (ends("fulness")) r("ful");
/* 299 */       else if (ends("ousness")) r("ous"); break;
/*     */     case 't':
/* 301 */       if (ends("aliti")) r("al");
/* 302 */       else if (ends("iviti")) r("ive");
/* 303 */       else if (ends("biliti")) r("ble"); break;
/*     */     case 'g':
/* 305 */       if (ends("logi")) r("log"); break;
/*     */     case 'b':
/*     */     case 'd':
/*     */     case 'f':
/*     */     case 'h':
/*     */     case 'i':
/*     */     case 'j':
/*     */     case 'k':
/*     */     case 'm':
/*     */     case 'n':
/*     */     case 'p':
/*     */     case 'q':
/*     */     case 'r': }  } 
/* 313 */   private final void step4() { switch (this.b[this.k]) {
/*     */     case 'e':
/* 315 */       if (ends("icate")) r("ic");
/* 316 */       else if (ends("ative")) r("");
/* 317 */       else if (ends("alize")) r("al"); break;
/*     */     case 'i':
/* 319 */       if (ends("iciti")) r("ic"); break;
/*     */     case 'l':
/* 321 */       if (ends("ical")) r("ic");
/* 322 */       else if (ends("ful")) r(""); break;
/*     */     case 's':
/* 324 */       if (ends("ness")) r(""); break;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void step5()
/*     */   {
/* 332 */     if (this.k == 0) return; switch (this.b[(this.k - 1)]) { case 'a':
/* 333 */       if (!ends("al")) return; break;
/*     */     case 'c':
/* 334 */       if ((!ends("ance")) && 
/* 335 */         (!ends("ence"))) return; break;
/*     */     case 'e':
/* 336 */       if (!ends("er")) return; break;
/*     */     case 'i':
/* 337 */       if (!ends("ic")) return; break;
/*     */     case 'l':
/* 338 */       if ((!ends("able")) && 
/* 339 */         (!ends("ible"))) return; break;
/*     */     case 'n':
/* 340 */       if ((!ends("ant")) && 
/* 341 */         (!ends("ement")) && 
/* 342 */         (!ends("ment")))
/*     */       {
/* 344 */         if (!ends("ent")) return;  } break;
/*     */     case 'o':
/* 345 */       if ((!ends("ion")) || (this.j < 0) || ((this.b[this.j] != 's') && (this.b[this.j] != 't')))
/*     */       {
/* 347 */         if (!ends("ou")) return;  } break;
/*     */     case 's':
/* 349 */       if (!ends("ism")) return; break;
/*     */     case 't':
/* 350 */       if ((!ends("ate")) && 
/* 351 */         (!ends("iti"))) return; break;
/*     */     case 'u':
/* 352 */       if (!ends("ous")) return; break;
/*     */     case 'v':
/* 353 */       if (!ends("ive")) return; break;
/*     */     case 'z':
/* 354 */       if (!ends("ize")) return; break;
/*     */     case 'b':
/*     */     case 'd':
/*     */     case 'f':
/*     */     case 'g':
/*     */     case 'h':
/*     */     case 'j':
/*     */     case 'k':
/*     */     case 'm':
/*     */     case 'p':
/*     */     case 'q':
/*     */     case 'r':
/*     */     case 'w':
/*     */     case 'x':
/*     */     case 'y':
/*     */     default:
/* 355 */       return;
/*     */     }
/* 357 */     if (m() > 1) this.k = this.j;
/*     */   }
/*     */ 
/*     */   private final void step6()
/*     */   {
/* 362 */     this.j = this.k;
/* 363 */     if (this.b[this.k] == 'e') {
/* 364 */       int a = m();
/* 365 */       if ((a > 1) || ((a == 1) && (!cvc(this.k - 1)))) this.k -= 1;
/*     */     }
/* 367 */     if ((this.b[this.k] == 'l') && (doublec(this.k)) && (m() > 1)) this.k -= 1;
/*     */   }
/*     */ 
/*     */   private void stem()
/*     */   {
/* 376 */     this.k = (this.i - 1);
/* 377 */     if (this.k > 1) { step1(); step2(); step3(); step4(); step5(); step6(); }
/* 378 */     this.i_end = (this.k + 1); this.i = 0;
/*     */   }
/*     */ 
/*     */   public static String stem(String in) {
/* 382 */     PorterStemmer stemmer = new PorterStemmer();
/* 383 */     if (in == null)
/* 384 */       throw new IllegalArgumentException("Input must not be null: " + in);
/* 385 */     char[] cs = in.toCharArray();
/* 386 */     if ((cs.length < 1) || (!Character.isLetter(cs[0]))) return in;
/* 387 */     for (int i = 0; i < cs.length; i++)
/* 388 */       stemmer.add(cs[i]);
/* 389 */     stemmer.stem();
/* 390 */     return stemmer.toString();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tokenizer.PorterStemmer
 * JD-Core Version:    0.6.2
 */