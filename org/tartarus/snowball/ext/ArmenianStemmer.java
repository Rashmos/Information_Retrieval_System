/*     */ package org.tartarus.snowball.ext;
/*     */ 
/*     */ import org.tartarus.snowball.Among;
/*     */ import org.tartarus.snowball.SnowballProgram;
/*     */ 
/*     */ public class ArmenianStemmer extends SnowballProgram
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  17 */   private static final ArmenianStemmer methodObject = new ArmenianStemmer();
/*     */ 
/*  19 */   private static final Among[] a_0 = { new Among("րորդ", -1, 1, "", methodObject), new Among("երորդ", 0, 1, "", methodObject), new Among("ալի", -1, 1, "", methodObject), new Among("ակի", -1, 1, "", methodObject), new Among("որակ", -1, 1, "", methodObject), new Among("եղ", -1, 1, "", methodObject), new Among("ական", -1, 1, "", methodObject), new Among("արան", -1, 1, "", methodObject), new Among("են", -1, 1, "", methodObject), new Among("եկեն", 8, 1, "", methodObject), new Among("երեն", 8, 1, "", methodObject), new Among("որէն", -1, 1, "", methodObject), new Among("ին", -1, 1, "", methodObject), new Among("գին", 12, 1, "", methodObject), new Among("ովին", 12, 1, "", methodObject), new Among("լայն", -1, 1, "", methodObject), new Among("վուն", -1, 1, "", methodObject), new Among("պես", -1, 1, "", methodObject), new Among("իվ", -1, 1, "", methodObject), new Among("ատ", -1, 1, "", methodObject), new Among("ավետ", -1, 1, "", methodObject), new Among("կոտ", -1, 1, "", methodObject), new Among("բար", -1, 1, "", methodObject) };
/*     */ 
/*  45 */   private static final Among[] a_1 = { new Among("ա", -1, 1, "", methodObject), new Among("ացա", 0, 1, "", methodObject), new Among("եցա", 0, 1, "", methodObject), new Among("վե", -1, 1, "", methodObject), new Among("ացրի", -1, 1, "", methodObject), new Among("ացի", -1, 1, "", methodObject), new Among("եցի", -1, 1, "", methodObject), new Among("վեցի", 6, 1, "", methodObject), new Among("ալ", -1, 1, "", methodObject), new Among("ըալ", 8, 1, "", methodObject), new Among("անալ", 8, 1, "", methodObject), new Among("ենալ", 8, 1, "", methodObject), new Among("ացնալ", 8, 1, "", methodObject), new Among("ել", -1, 1, "", methodObject), new Among("ըել", 13, 1, "", methodObject), new Among("նել", 13, 1, "", methodObject), new Among("ցնել", 15, 1, "", methodObject), new Among("եցնել", 16, 1, "", methodObject), new Among("չել", 13, 1, "", methodObject), new Among("վել", 13, 1, "", methodObject), new Among("ացվել", 19, 1, "", methodObject), new Among("եցվել", 19, 1, "", methodObject), new Among("տել", 13, 1, "", methodObject), new Among("ատել", 22, 1, "", methodObject), new Among("ոտել", 22, 1, "", methodObject), new Among("կոտել", 24, 1, "", methodObject), new Among("ված", -1, 1, "", methodObject), new Among("ում", -1, 1, "", methodObject), new Among("վում", 27, 1, "", methodObject), new Among("ան", -1, 1, "", methodObject), new Among("ցան", 29, 1, "", methodObject), new Among("ացան", 30, 1, "", methodObject), new Among("ացրին", -1, 1, "", methodObject), new Among("ացին", -1, 1, "", methodObject), new Among("եցին", -1, 1, "", methodObject), new Among("վեցին", 34, 1, "", methodObject), new Among("ալիս", -1, 1, "", methodObject), new Among("ելիս", -1, 1, "", methodObject), new Among("ավ", -1, 1, "", methodObject), new Among("ացավ", 38, 1, "", methodObject), new Among("եցավ", 38, 1, "", methodObject), new Among("ալով", -1, 1, "", methodObject), new Among("ելով", -1, 1, "", methodObject), new Among("ար", -1, 1, "", methodObject), new Among("ացար", 43, 1, "", methodObject), new Among("եցար", 43, 1, "", methodObject), new Among("ացրիր", -1, 1, "", methodObject), new Among("ացիր", -1, 1, "", methodObject), new Among("եցիր", -1, 1, "", methodObject), new Among("վեցիր", 48, 1, "", methodObject), new Among("աց", -1, 1, "", methodObject), new Among("եց", -1, 1, "", methodObject), new Among("ացրեց", 51, 1, "", methodObject), new Among("ալուց", -1, 1, "", methodObject), new Among("ելուց", -1, 1, "", methodObject), new Among("ալու", -1, 1, "", methodObject), new Among("ելու", -1, 1, "", methodObject), new Among("աք", -1, 1, "", methodObject), new Among("ցաք", 57, 1, "", methodObject), new Among("ացաք", 58, 1, "", methodObject), new Among("ացրիք", -1, 1, "", methodObject), new Among("ացիք", -1, 1, "", methodObject), new Among("եցիք", -1, 1, "", methodObject), new Among("վեցիք", 62, 1, "", methodObject), new Among("անք", -1, 1, "", methodObject), new Among("ցանք", 64, 1, "", methodObject), new Among("ացանք", 65, 1, "", methodObject), new Among("ացրինք", -1, 1, "", methodObject), new Among("ացինք", -1, 1, "", methodObject), new Among("եցինք", -1, 1, "", methodObject), new Among("վեցինք", 69, 1, "", methodObject) };
/*     */ 
/* 119 */   private static final Among[] a_2 = { new Among("որդ", -1, 1, "", methodObject), new Among("ույթ", -1, 1, "", methodObject), new Among("ուհի", -1, 1, "", methodObject), new Among("ցի", -1, 1, "", methodObject), new Among("իլ", -1, 1, "", methodObject), new Among("ակ", -1, 1, "", methodObject), new Among("յակ", 5, 1, "", methodObject), new Among("անակ", 5, 1, "", methodObject), new Among("իկ", -1, 1, "", methodObject), new Among("ուկ", -1, 1, "", methodObject), new Among("ան", -1, 1, "", methodObject), new Among("պան", 10, 1, "", methodObject), new Among("ստան", 10, 1, "", methodObject), new Among("արան", 10, 1, "", methodObject), new Among("եղէն", -1, 1, "", methodObject), new Among("յուն", -1, 1, "", methodObject), new Among("ություն", 15, 1, "", methodObject), new Among("ածո", -1, 1, "", methodObject), new Among("իչ", -1, 1, "", methodObject), new Among("ուս", -1, 1, "", methodObject), new Among("ուստ", -1, 1, "", methodObject), new Among("գար", -1, 1, "", methodObject), new Among("վոր", -1, 1, "", methodObject), new Among("ավոր", 22, 1, "", methodObject), new Among("ոց", -1, 1, "", methodObject), new Among("անօց", -1, 1, "", methodObject), new Among("ու", -1, 1, "", methodObject), new Among("ք", -1, 1, "", methodObject), new Among("չեք", 27, 1, "", methodObject), new Among("իք", 27, 1, "", methodObject), new Among("ալիք", 29, 1, "", methodObject), new Among("անիք", 29, 1, "", methodObject), new Among("վածք", 27, 1, "", methodObject), new Among("ույք", 27, 1, "", methodObject), new Among("ենք", 27, 1, "", methodObject), new Among("ոնք", 27, 1, "", methodObject), new Among("ունք", 27, 1, "", methodObject), new Among("մունք", 36, 1, "", methodObject), new Among("իչք", 27, 1, "", methodObject), new Among("արք", 27, 1, "", methodObject) };
/*     */ 
/* 162 */   private static final Among[] a_3 = { new Among("սա", -1, 1, "", methodObject), new Among("վա", -1, 1, "", methodObject), new Among("ամբ", -1, 1, "", methodObject), new Among("դ", -1, 1, "", methodObject), new Among("անդ", 3, 1, "", methodObject), new Among("ությանդ", 4, 1, "", methodObject), new Among("վանդ", 4, 1, "", methodObject), new Among("ոջդ", 3, 1, "", methodObject), new Among("երդ", 3, 1, "", methodObject), new Among("ներդ", 8, 1, "", methodObject), new Among("ուդ", 3, 1, "", methodObject), new Among("ը", -1, 1, "", methodObject), new Among("անը", 11, 1, "", methodObject), new Among("ությանը", 12, 1, "", methodObject), new Among("վանը", 12, 1, "", methodObject), new Among("ոջը", 11, 1, "", methodObject), new Among("երը", 11, 1, "", methodObject), new Among("ները", 16, 1, "", methodObject), new Among("ի", -1, 1, "", methodObject), new Among("վի", 18, 1, "", methodObject), new Among("երի", 18, 1, "", methodObject), new Among("ների", 20, 1, "", methodObject), new Among("անում", -1, 1, "", methodObject), new Among("երում", -1, 1, "", methodObject), new Among("ներում", 23, 1, "", methodObject), new Among("ն", -1, 1, "", methodObject), new Among("ան", 25, 1, "", methodObject), new Among("ության", 26, 1, "", methodObject), new Among("վան", 26, 1, "", methodObject), new Among("ին", 25, 1, "", methodObject), new Among("երին", 29, 1, "", methodObject), new Among("ներին", 30, 1, "", methodObject), new Among("ությանն", 25, 1, "", methodObject), new Among("երն", 25, 1, "", methodObject), new Among("ներն", 33, 1, "", methodObject), new Among("ուն", 25, 1, "", methodObject), new Among("ոջ", -1, 1, "", methodObject), new Among("ությանս", -1, 1, "", methodObject), new Among("վանս", -1, 1, "", methodObject), new Among("ոջս", -1, 1, "", methodObject), new Among("ով", -1, 1, "", methodObject), new Among("անով", 40, 1, "", methodObject), new Among("վով", 40, 1, "", methodObject), new Among("երով", 40, 1, "", methodObject), new Among("ներով", 43, 1, "", methodObject), new Among("եր", -1, 1, "", methodObject), new Among("ներ", 45, 1, "", methodObject), new Among("ց", -1, 1, "", methodObject), new Among("ից", 47, 1, "", methodObject), new Among("վանից", 48, 1, "", methodObject), new Among("ոջից", 48, 1, "", methodObject), new Among("վից", 48, 1, "", methodObject), new Among("երից", 48, 1, "", methodObject), new Among("ներից", 52, 1, "", methodObject), new Among("ցից", 48, 1, "", methodObject), new Among("ոց", 47, 1, "", methodObject), new Among("ուց", 47, 1, "", methodObject) };
/*     */ 
/* 222 */   private static final char[] g_v = { 'Ñ', '\004', '', '\000', '\022' };
/*     */   private int I_p2;
/*     */   private int I_pV;
/*     */ 
/*     */   private void copy_from(ArmenianStemmer other)
/*     */   {
/* 228 */     this.I_p2 = other.I_p2;
/* 229 */     this.I_pV = other.I_pV;
/* 230 */     super.copy_from(other);
/*     */   }
/*     */ 
/*     */   private boolean r_mark_regions()
/*     */   {
/* 236 */     this.I_pV = this.limit;
/* 237 */     this.I_p2 = this.limit;
/*     */ 
/* 239 */     int v_1 = this.cursor;
/*     */ 
/* 246 */     while (!in_grouping(g_v, 1377, 1413))
/*     */     {
/* 252 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label209;
/*     */       }
/* 256 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 259 */     this.I_pV = this.cursor;
/*     */ 
/* 264 */     while (!out_grouping(g_v, 1377, 1413))
/*     */     {
/* 270 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label209;
/*     */       }
/* 274 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 280 */     while (!in_grouping(g_v, 1377, 1413))
/*     */     {
/* 286 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label209;
/*     */       }
/* 290 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 296 */     while (!out_grouping(g_v, 1377, 1413))
/*     */     {
/* 302 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label209;
/*     */       }
/* 306 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 309 */     this.I_p2 = this.cursor;
/*     */ 
/* 311 */     label209: this.cursor = v_1;
/* 312 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_R2() {
/* 316 */     if (this.I_p2 > this.cursor)
/*     */     {
/* 318 */       return false;
/*     */     }
/* 320 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_adjective()
/*     */   {
/* 327 */     this.ket = this.cursor;
/*     */ 
/* 329 */     int among_var = find_among_b(a_0, 23);
/* 330 */     if (among_var == 0)
/*     */     {
/* 332 */       return false;
/*     */     }
/*     */ 
/* 335 */     this.bra = this.cursor;
/* 336 */     switch (among_var) {
/*     */     case 0:
/* 338 */       return false;
/*     */     case 1:
/* 342 */       slice_del();
/*     */     }
/*     */ 
/* 345 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_verb()
/*     */   {
/* 352 */     this.ket = this.cursor;
/*     */ 
/* 354 */     int among_var = find_among_b(a_1, 71);
/* 355 */     if (among_var == 0)
/*     */     {
/* 357 */       return false;
/*     */     }
/*     */ 
/* 360 */     this.bra = this.cursor;
/* 361 */     switch (among_var) {
/*     */     case 0:
/* 363 */       return false;
/*     */     case 1:
/* 367 */       slice_del();
/*     */     }
/*     */ 
/* 370 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_noun()
/*     */   {
/* 377 */     this.ket = this.cursor;
/*     */ 
/* 379 */     int among_var = find_among_b(a_2, 40);
/* 380 */     if (among_var == 0)
/*     */     {
/* 382 */       return false;
/*     */     }
/*     */ 
/* 385 */     this.bra = this.cursor;
/* 386 */     switch (among_var) {
/*     */     case 0:
/* 388 */       return false;
/*     */     case 1:
/* 392 */       slice_del();
/*     */     }
/*     */ 
/* 395 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_ending()
/*     */   {
/* 402 */     this.ket = this.cursor;
/*     */ 
/* 404 */     int among_var = find_among_b(a_3, 57);
/* 405 */     if (among_var == 0)
/*     */     {
/* 407 */       return false;
/*     */     }
/*     */ 
/* 410 */     this.bra = this.cursor;
/*     */ 
/* 412 */     if (!r_R2())
/*     */     {
/* 414 */       return false;
/*     */     }
/* 416 */     switch (among_var) {
/*     */     case 0:
/* 418 */       return false;
/*     */     case 1:
/* 422 */       slice_del();
/*     */     }
/*     */ 
/* 425 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean stem()
/*     */   {
/* 438 */     int v_1 = this.cursor;
/*     */ 
/* 441 */     if (!r_mark_regions());
/* 446 */     this.cursor = v_1;
/*     */ 
/* 448 */     this.limit_backward = this.cursor; this.cursor = this.limit;
/*     */ 
/* 450 */     int v_2 = this.limit - this.cursor;
/*     */ 
/* 452 */     if (this.cursor < this.I_pV)
/*     */     {
/* 454 */       return false;
/*     */     }
/* 456 */     this.cursor = this.I_pV;
/* 457 */     int v_3 = this.limit_backward;
/* 458 */     this.limit_backward = this.cursor;
/* 459 */     this.cursor = (this.limit - v_2);
/*     */ 
/* 462 */     int v_4 = this.limit - this.cursor;
/*     */ 
/* 465 */     if (!r_ending());
/* 470 */     this.cursor = (this.limit - v_4);
/*     */ 
/* 472 */     int v_5 = this.limit - this.cursor;
/*     */ 
/* 475 */     if (!r_verb());
/* 480 */     this.cursor = (this.limit - v_5);
/*     */ 
/* 482 */     int v_6 = this.limit - this.cursor;
/*     */ 
/* 485 */     if (!r_adjective());
/* 490 */     this.cursor = (this.limit - v_6);
/*     */ 
/* 492 */     int v_7 = this.limit - this.cursor;
/*     */ 
/* 495 */     if (!r_noun());
/* 500 */     this.cursor = (this.limit - v_7);
/* 501 */     this.limit_backward = v_3;
/* 502 */     this.cursor = this.limit_backward; return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o) {
/* 506 */     return o instanceof ArmenianStemmer;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 510 */     return ArmenianStemmer.class.getName().hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.tartarus.snowball.ext.ArmenianStemmer
 * JD-Core Version:    0.6.2
 */