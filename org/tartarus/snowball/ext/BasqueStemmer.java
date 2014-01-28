/*     */ package org.tartarus.snowball.ext;
/*     */ 
/*     */ import org.tartarus.snowball.Among;
/*     */ import org.tartarus.snowball.SnowballProgram;
/*     */ 
/*     */ public class BasqueStemmer extends SnowballProgram
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  17 */   private static final BasqueStemmer methodObject = new BasqueStemmer();
/*     */ 
/*  19 */   private static final Among[] a_0 = { new Among("idea", -1, 1, "", methodObject), new Among("bidea", 0, 1, "", methodObject), new Among("kidea", 0, 1, "", methodObject), new Among("pidea", 0, 1, "", methodObject), new Among("kundea", -1, 1, "", methodObject), new Among("galea", -1, 1, "", methodObject), new Among("tailea", -1, 1, "", methodObject), new Among("tzailea", -1, 1, "", methodObject), new Among("gunea", -1, 1, "", methodObject), new Among("kunea", -1, 1, "", methodObject), new Among("tzaga", -1, 1, "", methodObject), new Among("gaia", -1, 1, "", methodObject), new Among("aldia", -1, 1, "", methodObject), new Among("taldia", 12, 1, "", methodObject), new Among("karia", -1, 1, "", methodObject), new Among("garria", -1, 2, "", methodObject), new Among("karria", -1, 1, "", methodObject), new Among("ka", -1, 1, "", methodObject), new Among("tzaka", 17, 1, "", methodObject), new Among("la", -1, 1, "", methodObject), new Among("mena", -1, 1, "", methodObject), new Among("pena", -1, 1, "", methodObject), new Among("kina", -1, 1, "", methodObject), new Among("ezina", -1, 1, "", methodObject), new Among("tezina", 23, 1, "", methodObject), new Among("kuna", -1, 1, "", methodObject), new Among("tuna", -1, 1, "", methodObject), new Among("kizuna", -1, 1, "", methodObject), new Among("era", -1, 1, "", methodObject), new Among("bera", 28, 1, "", methodObject), new Among("arabera", 29, 4, "", methodObject), new Among("kera", 28, 1, "", methodObject), new Among("pera", 28, 1, "", methodObject), new Among("orra", -1, 1, "", methodObject), new Among("korra", 33, 1, "", methodObject), new Among("dura", -1, 1, "", methodObject), new Among("gura", -1, 1, "", methodObject), new Among("kura", -1, 1, "", methodObject), new Among("tura", -1, 1, "", methodObject), new Among("eta", -1, 1, "", methodObject), new Among("keta", 39, 1, "", methodObject), new Among("gailua", -1, 1, "", methodObject), new Among("eza", -1, 1, "", methodObject), new Among("erreza", 42, 1, "", methodObject), new Among("tza", -1, 2, "", methodObject), new Among("gaitza", 44, 1, "", methodObject), new Among("kaitza", 44, 1, "", methodObject), new Among("kuntza", 44, 1, "", methodObject), new Among("ide", -1, 1, "", methodObject), new Among("bide", 48, 1, "", methodObject), new Among("kide", 48, 1, "", methodObject), new Among("pide", 48, 1, "", methodObject), new Among("kunde", -1, 1, "", methodObject), new Among("tzake", -1, 1, "", methodObject), new Among("tzeke", -1, 1, "", methodObject), new Among("le", -1, 1, "", methodObject), new Among("gale", 55, 1, "", methodObject), new Among("taile", 55, 1, "", methodObject), new Among("tzaile", 55, 1, "", methodObject), new Among("gune", -1, 1, "", methodObject), new Among("kune", -1, 1, "", methodObject), new Among("tze", -1, 1, "", methodObject), new Among("atze", 61, 1, "", methodObject), new Among("gai", -1, 1, "", methodObject), new Among("aldi", -1, 1, "", methodObject), new Among("taldi", 64, 1, "", methodObject), new Among("ki", -1, 1, "", methodObject), new Among("ari", -1, 1, "", methodObject), new Among("kari", 67, 1, "", methodObject), new Among("lari", 67, 1, "", methodObject), new Among("tari", 67, 1, "", methodObject), new Among("etari", 70, 1, "", methodObject), new Among("garri", -1, 2, "", methodObject), new Among("karri", -1, 1, "", methodObject), new Among("arazi", -1, 1, "", methodObject), new Among("tarazi", 74, 1, "", methodObject), new Among("an", -1, 1, "", methodObject), new Among("ean", 76, 1, "", methodObject), new Among("rean", 77, 1, "", methodObject), new Among("kan", 76, 1, "", methodObject), new Among("etan", 76, 1, "", methodObject), new Among("atseden", -1, 3, "", methodObject), new Among("men", -1, 1, "", methodObject), new Among("pen", -1, 1, "", methodObject), new Among("kin", -1, 1, "", methodObject), new Among("rekin", 84, 1, "", methodObject), new Among("ezin", -1, 1, "", methodObject), new Among("tezin", 86, 1, "", methodObject), new Among("tun", -1, 1, "", methodObject), new Among("kizun", -1, 1, "", methodObject), new Among("go", -1, 1, "", methodObject), new Among("ago", 90, 1, "", methodObject), new Among("tio", -1, 1, "", methodObject), new Among("dako", -1, 1, "", methodObject), new Among("or", -1, 1, "", methodObject), new Among("kor", 94, 1, "", methodObject), new Among("tzat", -1, 1, "", methodObject), new Among("du", -1, 1, "", methodObject), new Among("gailu", -1, 1, "", methodObject), new Among("tu", -1, 1, "", methodObject), new Among("atu", 99, 1, "", methodObject), new Among("aldatu", 100, 1, "", methodObject), new Among("tatu", 100, 1, "", methodObject), new Among("baditu", 99, 5, "", methodObject), new Among("ez", -1, 1, "", methodObject), new Among("errez", 104, 1, "", methodObject), new Among("tzez", 104, 1, "", methodObject), new Among("gaitz", -1, 1, "", methodObject), new Among("kaitz", -1, 1, "", methodObject) };
/*     */ 
/* 131 */   private static final Among[] a_1 = { new Among("ada", -1, 1, "", methodObject), new Among("kada", 0, 1, "", methodObject), new Among("anda", -1, 1, "", methodObject), new Among("denda", -1, 1, "", methodObject), new Among("gabea", -1, 1, "", methodObject), new Among("kabea", -1, 1, "", methodObject), new Among("aldea", -1, 1, "", methodObject), new Among("kaldea", 6, 1, "", methodObject), new Among("taldea", 6, 1, "", methodObject), new Among("ordea", -1, 1, "", methodObject), new Among("zalea", -1, 1, "", methodObject), new Among("tzalea", 10, 1, "", methodObject), new Among("gilea", -1, 1, "", methodObject), new Among("emea", -1, 1, "", methodObject), new Among("kumea", -1, 1, "", methodObject), new Among("nea", -1, 1, "", methodObject), new Among("enea", 15, 1, "", methodObject), new Among("zionea", 15, 1, "", methodObject), new Among("unea", 15, 1, "", methodObject), new Among("gunea", 18, 1, "", methodObject), new Among("pea", -1, 1, "", methodObject), new Among("aurrea", -1, 1, "", methodObject), new Among("tea", -1, 1, "", methodObject), new Among("kotea", 22, 1, "", methodObject), new Among("artea", 22, 1, "", methodObject), new Among("ostea", 22, 1, "", methodObject), new Among("etxea", -1, 1, "", methodObject), new Among("ga", -1, 1, "", methodObject), new Among("anga", 27, 1, "", methodObject), new Among("gaia", -1, 1, "", methodObject), new Among("aldia", -1, 1, "", methodObject), new Among("taldia", 30, 1, "", methodObject), new Among("handia", -1, 1, "", methodObject), new Among("mendia", -1, 1, "", methodObject), new Among("geia", -1, 1, "", methodObject), new Among("egia", -1, 1, "", methodObject), new Among("degia", 35, 1, "", methodObject), new Among("tegia", 35, 1, "", methodObject), new Among("nahia", -1, 1, "", methodObject), new Among("ohia", -1, 1, "", methodObject), new Among("kia", -1, 1, "", methodObject), new Among("tokia", 40, 1, "", methodObject), new Among("oia", -1, 1, "", methodObject), new Among("koia", 42, 1, "", methodObject), new Among("aria", -1, 1, "", methodObject), new Among("karia", 44, 1, "", methodObject), new Among("laria", 44, 1, "", methodObject), new Among("taria", 44, 1, "", methodObject), new Among("eria", -1, 1, "", methodObject), new Among("keria", 48, 1, "", methodObject), new Among("teria", 48, 1, "", methodObject), new Among("garria", -1, 2, "", methodObject), new Among("larria", -1, 1, "", methodObject), new Among("kirria", -1, 1, "", methodObject), new Among("duria", -1, 1, "", methodObject), new Among("asia", -1, 1, "", methodObject), new Among("tia", -1, 1, "", methodObject), new Among("ezia", -1, 1, "", methodObject), new Among("bizia", -1, 1, "", methodObject), new Among("ontzia", -1, 1, "", methodObject), new Among("ka", -1, 1, "", methodObject), new Among("joka", 60, 3, "", methodObject), new Among("aurka", 60, 10, "", methodObject), new Among("ska", 60, 1, "", methodObject), new Among("xka", 60, 1, "", methodObject), new Among("zka", 60, 1, "", methodObject), new Among("gibela", -1, 1, "", methodObject), new Among("gela", -1, 1, "", methodObject), new Among("kaila", -1, 1, "", methodObject), new Among("skila", -1, 1, "", methodObject), new Among("tila", -1, 1, "", methodObject), new Among("ola", -1, 1, "", methodObject), new Among("na", -1, 1, "", methodObject), new Among("kana", 72, 1, "", methodObject), new Among("ena", 72, 1, "", methodObject), new Among("garrena", 74, 1, "", methodObject), new Among("gerrena", 74, 1, "", methodObject), new Among("urrena", 74, 1, "", methodObject), new Among("zaina", 72, 1, "", methodObject), new Among("tzaina", 78, 1, "", methodObject), new Among("kina", 72, 1, "", methodObject), new Among("mina", 72, 1, "", methodObject), new Among("garna", 72, 1, "", methodObject), new Among("una", 72, 1, "", methodObject), new Among("duna", 83, 1, "", methodObject), new Among("asuna", 83, 1, "", methodObject), new Among("tasuna", 85, 1, "", methodObject), new Among("ondoa", -1, 1, "", methodObject), new Among("kondoa", 87, 1, "", methodObject), new Among("ngoa", -1, 1, "", methodObject), new Among("zioa", -1, 1, "", methodObject), new Among("koa", -1, 1, "", methodObject), new Among("takoa", 91, 1, "", methodObject), new Among("zkoa", 91, 1, "", methodObject), new Among("noa", -1, 1, "", methodObject), new Among("zinoa", 94, 1, "", methodObject), new Among("aroa", -1, 1, "", methodObject), new Among("taroa", 96, 1, "", methodObject), new Among("zaroa", 96, 1, "", methodObject), new Among("eroa", -1, 1, "", methodObject), new Among("oroa", -1, 1, "", methodObject), new Among("osoa", -1, 1, "", methodObject), new Among("toa", -1, 1, "", methodObject), new Among("ttoa", 102, 1, "", methodObject), new Among("ztoa", 102, 1, "", methodObject), new Among("txoa", -1, 1, "", methodObject), new Among("tzoa", -1, 1, "", methodObject), new Among("ñoa", -1, 1, "", methodObject), new Among("ra", -1, 1, "", methodObject), new Among("ara", 108, 1, "", methodObject), new Among("dara", 109, 1, "", methodObject), new Among("liara", 109, 1, "", methodObject), new Among("tiara", 109, 1, "", methodObject), new Among("tara", 109, 1, "", methodObject), new Among("etara", 113, 1, "", methodObject), new Among("tzara", 109, 1, "", methodObject), new Among("bera", 108, 1, "", methodObject), new Among("kera", 108, 1, "", methodObject), new Among("pera", 108, 1, "", methodObject), new Among("ora", 108, 2, "", methodObject), new Among("tzarra", 108, 1, "", methodObject), new Among("korra", 108, 1, "", methodObject), new Among("tra", 108, 1, "", methodObject), new Among("sa", -1, 1, "", methodObject), new Among("osa", 123, 1, "", methodObject), new Among("ta", -1, 1, "", methodObject), new Among("eta", 125, 1, "", methodObject), new Among("keta", 126, 1, "", methodObject), new Among("sta", 125, 1, "", methodObject), new Among("dua", -1, 1, "", methodObject), new Among("mendua", 129, 1, "", methodObject), new Among("ordua", 129, 1, "", methodObject), new Among("lekua", -1, 1, "", methodObject), new Among("burua", -1, 1, "", methodObject), new Among("durua", -1, 1, "", methodObject), new Among("tsua", -1, 1, "", methodObject), new Among("tua", -1, 1, "", methodObject), new Among("mentua", 136, 1, "", methodObject), new Among("estua", 136, 1, "", methodObject), new Among("txua", -1, 1, "", methodObject), new Among("zua", -1, 1, "", methodObject), new Among("tzua", 140, 1, "", methodObject), new Among("za", -1, 1, "", methodObject), new Among("eza", 142, 1, "", methodObject), new Among("eroza", 142, 1, "", methodObject), new Among("tza", 142, 2, "", methodObject), new Among("koitza", 145, 1, "", methodObject), new Among("antza", 145, 1, "", methodObject), new Among("gintza", 145, 1, "", methodObject), new Among("kintza", 145, 1, "", methodObject), new Among("kuntza", 145, 1, "", methodObject), new Among("gabe", -1, 1, "", methodObject), new Among("kabe", -1, 1, "", methodObject), new Among("kide", -1, 1, "", methodObject), new Among("alde", -1, 1, "", methodObject), new Among("kalde", 154, 1, "", methodObject), new Among("talde", 154, 1, "", methodObject), new Among("orde", -1, 1, "", methodObject), new Among("ge", -1, 1, "", methodObject), new Among("zale", -1, 1, "", methodObject), new Among("tzale", 159, 1, "", methodObject), new Among("gile", -1, 1, "", methodObject), new Among("eme", -1, 1, "", methodObject), new Among("kume", -1, 1, "", methodObject), new Among("ne", -1, 1, "", methodObject), new Among("zione", 164, 1, "", methodObject), new Among("une", 164, 1, "", methodObject), new Among("gune", 166, 1, "", methodObject), new Among("pe", -1, 1, "", methodObject), new Among("aurre", -1, 1, "", methodObject), new Among("te", -1, 1, "", methodObject), new Among("kote", 170, 1, "", methodObject), new Among("arte", 170, 1, "", methodObject), new Among("oste", 170, 1, "", methodObject), new Among("etxe", -1, 1, "", methodObject), new Among("gai", -1, 1, "", methodObject), new Among("di", -1, 1, "", methodObject), new Among("aldi", 176, 1, "", methodObject), new Among("taldi", 177, 1, "", methodObject), new Among("geldi", 176, 8, "", methodObject), new Among("handi", 176, 1, "", methodObject), new Among("mendi", 176, 1, "", methodObject), new Among("gei", -1, 1, "", methodObject), new Among("egi", -1, 1, "", methodObject), new Among("degi", 183, 1, "", methodObject), new Among("tegi", 183, 1, "", methodObject), new Among("nahi", -1, 1, "", methodObject), new Among("ohi", -1, 1, "", methodObject), new Among("ki", -1, 1, "", methodObject), new Among("toki", 188, 1, "", methodObject), new Among("oi", -1, 1, "", methodObject), new Among("goi", 190, 1, "", methodObject), new Among("koi", 190, 1, "", methodObject), new Among("ari", -1, 1, "", methodObject), new Among("kari", 193, 1, "", methodObject), new Among("lari", 193, 1, "", methodObject), new Among("tari", 193, 1, "", methodObject), new Among("garri", -1, 2, "", methodObject), new Among("larri", -1, 1, "", methodObject), new Among("kirri", -1, 1, "", methodObject), new Among("duri", -1, 1, "", methodObject), new Among("asi", -1, 1, "", methodObject), new Among("ti", -1, 1, "", methodObject), new Among("ontzi", -1, 1, "", methodObject), new Among("ñi", -1, 1, "", methodObject), new Among("ak", -1, 1, "", methodObject), new Among("ek", -1, 1, "", methodObject), new Among("tarik", -1, 1, "", methodObject), new Among("gibel", -1, 1, "", methodObject), new Among("ail", -1, 1, "", methodObject), new Among("kail", 209, 1, "", methodObject), new Among("kan", -1, 1, "", methodObject), new Among("tan", -1, 1, "", methodObject), new Among("etan", 212, 1, "", methodObject), new Among("en", -1, 4, "", methodObject), new Among("ren", 214, 2, "", methodObject), new Among("garren", 215, 1, "", methodObject), new Among("gerren", 215, 1, "", methodObject), new Among("urren", 215, 1, "", methodObject), new Among("ten", 214, 4, "", methodObject), new Among("tzen", 214, 4, "", methodObject), new Among("zain", -1, 1, "", methodObject), new Among("tzain", 221, 1, "", methodObject), new Among("kin", -1, 1, "", methodObject), new Among("min", -1, 1, "", methodObject), new Among("dun", -1, 1, "", methodObject), new Among("asun", -1, 1, "", methodObject), new Among("tasun", 226, 1, "", methodObject), new Among("aizun", -1, 1, "", methodObject), new Among("ondo", -1, 1, "", methodObject), new Among("kondo", 229, 1, "", methodObject), new Among("go", -1, 1, "", methodObject), new Among("ngo", 231, 1, "", methodObject), new Among("zio", -1, 1, "", methodObject), new Among("ko", -1, 1, "", methodObject), new Among("trako", 234, 5, "", methodObject), new Among("tako", 234, 1, "", methodObject), new Among("etako", 236, 1, "", methodObject), new Among("eko", 234, 1, "", methodObject), new Among("tariko", 234, 1, "", methodObject), new Among("sko", 234, 1, "", methodObject), new Among("tuko", 234, 1, "", methodObject), new Among("minutuko", 241, 6, "", methodObject), new Among("zko", 234, 1, "", methodObject), new Among("no", -1, 1, "", methodObject), new Among("zino", 244, 1, "", methodObject), new Among("ro", -1, 1, "", methodObject), new Among("aro", 246, 1, "", methodObject), new Among("igaro", 247, 9, "", methodObject), new Among("taro", 247, 1, "", methodObject), new Among("zaro", 247, 1, "", methodObject), new Among("ero", 246, 1, "", methodObject), new Among("giro", 246, 1, "", methodObject), new Among("oro", 246, 1, "", methodObject), new Among("oso", -1, 1, "", methodObject), new Among("to", -1, 1, "", methodObject), new Among("tto", 255, 1, "", methodObject), new Among("zto", 255, 1, "", methodObject), new Among("txo", -1, 1, "", methodObject), new Among("tzo", -1, 1, "", methodObject), new Among("gintzo", 259, 1, "", methodObject), new Among("ño", -1, 1, "", methodObject), new Among("zp", -1, 1, "", methodObject), new Among("ar", -1, 1, "", methodObject), new Among("dar", 263, 1, "", methodObject), new Among("behar", 263, 1, "", methodObject), new Among("zehar", 263, 7, "", methodObject), new Among("liar", 263, 1, "", methodObject), new Among("tiar", 263, 1, "", methodObject), new Among("tar", 263, 1, "", methodObject), new Among("tzar", 263, 1, "", methodObject), new Among("or", -1, 2, "", methodObject), new Among("kor", 271, 1, "", methodObject), new Among("os", -1, 1, "", methodObject), new Among("ket", -1, 1, "", methodObject), new Among("du", -1, 1, "", methodObject), new Among("mendu", 275, 1, "", methodObject), new Among("ordu", 275, 1, "", methodObject), new Among("leku", -1, 1, "", methodObject), new Among("buru", -1, 2, "", methodObject), new Among("duru", -1, 1, "", methodObject), new Among("tsu", -1, 1, "", methodObject), new Among("tu", -1, 1, "", methodObject), new Among("tatu", 282, 4, "", methodObject), new Among("mentu", 282, 1, "", methodObject), new Among("estu", 282, 1, "", methodObject), new Among("txu", -1, 1, "", methodObject), new Among("zu", -1, 1, "", methodObject), new Among("tzu", 287, 1, "", methodObject), new Among("gintzu", 288, 1, "", methodObject), new Among("z", -1, 1, "", methodObject), new Among("ez", 290, 1, "", methodObject), new Among("eroz", 290, 1, "", methodObject), new Among("tz", 290, 1, "", methodObject), new Among("koitz", 293, 1, "", methodObject) };
/*     */ 
/* 429 */   private static final Among[] a_2 = { new Among("zlea", -1, 2, "", methodObject), new Among("keria", -1, 1, "", methodObject), new Among("la", -1, 1, "", methodObject), new Among("era", -1, 1, "", methodObject), new Among("dade", -1, 1, "", methodObject), new Among("tade", -1, 1, "", methodObject), new Among("date", -1, 1, "", methodObject), new Among("tate", -1, 1, "", methodObject), new Among("gi", -1, 1, "", methodObject), new Among("ki", -1, 1, "", methodObject), new Among("ik", -1, 1, "", methodObject), new Among("lanik", 10, 1, "", methodObject), new Among("rik", 10, 1, "", methodObject), new Among("larik", 12, 1, "", methodObject), new Among("ztik", 10, 1, "", methodObject), new Among("go", -1, 1, "", methodObject), new Among("ro", -1, 1, "", methodObject), new Among("ero", 16, 1, "", methodObject), new Among("to", -1, 1, "", methodObject) };
/*     */ 
/* 451 */   private static final char[] g_v = { '\021', 'A', '\020' };
/*     */   private int I_p2;
/*     */   private int I_p1;
/*     */   private int I_pV;
/*     */ 
/*     */   private void copy_from(BasqueStemmer other)
/*     */   {
/* 458 */     this.I_p2 = other.I_p2;
/* 459 */     this.I_p1 = other.I_p1;
/* 460 */     this.I_pV = other.I_pV;
/* 461 */     super.copy_from(other);
/*     */   }
/*     */ 
/*     */   private boolean r_mark_regions()
/*     */   {
/* 471 */     this.I_pV = this.limit;
/* 472 */     this.I_p1 = this.limit;
/* 473 */     this.I_p2 = this.limit;
/*     */ 
/* 475 */     int v_1 = this.cursor;
/*     */ 
/* 480 */     int v_2 = this.cursor;
/*     */ 
/* 483 */     if (in_grouping(g_v, 97, 117))
/*     */     {
/* 489 */       int v_3 = this.cursor;
/*     */ 
/* 492 */       if (out_grouping(g_v, 97, 117))
/*     */       {
/*     */         while (true)
/*     */         {
/* 500 */           if (in_grouping(g_v, 97, 117))
/*     */           {
/*     */             break label310;
/*     */           }
/*     */ 
/* 506 */           if (this.cursor >= this.limit)
/*     */           {
/*     */             break;
/*     */           }
/* 510 */           this.cursor += 1;
/*     */         }
/*     */       }
/*     */ 
/* 514 */       this.cursor = v_3;
/*     */ 
/* 516 */       if (in_grouping(g_v, 97, 117))
/*     */       {
/*     */         while (true)
/*     */         {
/* 524 */           if (out_grouping(g_v, 97, 117))
/*     */           {
/*     */             break label310;
/*     */           }
/*     */ 
/* 530 */           if (this.cursor >= this.limit)
/*     */           {
/*     */             break;
/*     */           }
/* 534 */           this.cursor += 1;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 539 */     this.cursor = v_2;
/*     */ 
/* 541 */     if (out_grouping(g_v, 97, 117))
/*     */     {
/* 547 */       int v_6 = this.cursor;
/*     */ 
/* 550 */       if (out_grouping(g_v, 97, 117))
/*     */       {
/*     */         while (true)
/*     */         {
/* 558 */           if (in_grouping(g_v, 97, 117))
/*     */           {
/*     */             break label310;
/*     */           }
/*     */ 
/* 564 */           if (this.cursor >= this.limit)
/*     */           {
/*     */             break;
/*     */           }
/* 568 */           this.cursor += 1;
/*     */         }
/*     */       }
/*     */ 
/* 572 */       this.cursor = v_6;
/*     */ 
/* 574 */       if (in_grouping(g_v, 97, 117))
/*     */       {
/* 579 */         if (this.cursor < this.limit)
/*     */         {
/* 583 */           this.cursor += 1;
/*     */ 
/* 587 */           label310: this.I_pV = this.cursor;
/*     */         }
/*     */       }
/*     */     }
/* 589 */     this.cursor = v_1;
/*     */ 
/* 591 */     int v_8 = this.cursor;
/*     */ 
/* 598 */     while (!in_grouping(g_v, 97, 117))
/*     */     {
/* 604 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label509;
/*     */       }
/* 608 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 614 */     while (!out_grouping(g_v, 97, 117))
/*     */     {
/* 620 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label509;
/*     */       }
/* 624 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 627 */     this.I_p1 = this.cursor;
/*     */ 
/* 632 */     while (!in_grouping(g_v, 97, 117))
/*     */     {
/* 638 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label509;
/*     */       }
/* 642 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 648 */     while (!out_grouping(g_v, 97, 117))
/*     */     {
/* 654 */       if (this.cursor >= this.limit)
/*     */       {
/*     */         break label509;
/*     */       }
/* 658 */       this.cursor += 1;
/*     */     }
/*     */ 
/* 661 */     this.I_p2 = this.cursor;
/*     */ 
/* 663 */     label509: this.cursor = v_8;
/* 664 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_RV() {
/* 668 */     if (this.I_pV > this.cursor)
/*     */     {
/* 670 */       return false;
/*     */     }
/* 672 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_R2() {
/* 676 */     if (this.I_p2 > this.cursor)
/*     */     {
/* 678 */       return false;
/*     */     }
/* 680 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_R1() {
/* 684 */     if (this.I_p1 > this.cursor)
/*     */     {
/* 686 */       return false;
/*     */     }
/* 688 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_aditzak()
/*     */   {
/* 695 */     this.ket = this.cursor;
/*     */ 
/* 697 */     int among_var = find_among_b(a_0, 109);
/* 698 */     if (among_var == 0)
/*     */     {
/* 700 */       return false;
/*     */     }
/*     */ 
/* 703 */     this.bra = this.cursor;
/* 704 */     switch (among_var) {
/*     */     case 0:
/* 706 */       return false;
/*     */     case 1:
/* 710 */       if (!r_RV())
/*     */       {
/* 712 */         return false;
/*     */       }
/*     */ 
/* 715 */       slice_del();
/* 716 */       break;
/*     */     case 2:
/* 720 */       if (!r_R2())
/*     */       {
/* 722 */         return false;
/*     */       }
/*     */ 
/* 725 */       slice_del();
/* 726 */       break;
/*     */     case 3:
/* 730 */       slice_from("atseden");
/* 731 */       break;
/*     */     case 4:
/* 735 */       slice_from("arabera");
/* 736 */       break;
/*     */     case 5:
/* 740 */       slice_from("baditu");
/*     */     }
/*     */ 
/* 743 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_izenak()
/*     */   {
/* 750 */     this.ket = this.cursor;
/*     */ 
/* 752 */     int among_var = find_among_b(a_1, 295);
/* 753 */     if (among_var == 0)
/*     */     {
/* 755 */       return false;
/*     */     }
/*     */ 
/* 758 */     this.bra = this.cursor;
/* 759 */     switch (among_var) {
/*     */     case 0:
/* 761 */       return false;
/*     */     case 1:
/* 765 */       if (!r_RV())
/*     */       {
/* 767 */         return false;
/*     */       }
/*     */ 
/* 770 */       slice_del();
/* 771 */       break;
/*     */     case 2:
/* 775 */       if (!r_R2())
/*     */       {
/* 777 */         return false;
/*     */       }
/*     */ 
/* 780 */       slice_del();
/* 781 */       break;
/*     */     case 3:
/* 785 */       slice_from("jok");
/* 786 */       break;
/*     */     case 4:
/* 790 */       if (!r_R1())
/*     */       {
/* 792 */         return false;
/*     */       }
/*     */ 
/* 795 */       slice_del();
/* 796 */       break;
/*     */     case 5:
/* 800 */       slice_from("tra");
/* 801 */       break;
/*     */     case 6:
/* 805 */       slice_from("minutu");
/* 806 */       break;
/*     */     case 7:
/* 810 */       slice_from("zehar");
/* 811 */       break;
/*     */     case 8:
/* 815 */       slice_from("geldi");
/* 816 */       break;
/*     */     case 9:
/* 820 */       slice_from("igaro");
/* 821 */       break;
/*     */     case 10:
/* 825 */       slice_from("aurka");
/*     */     }
/*     */ 
/* 828 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean r_adjetiboak()
/*     */   {
/* 835 */     this.ket = this.cursor;
/*     */ 
/* 837 */     int among_var = find_among_b(a_2, 19);
/* 838 */     if (among_var == 0)
/*     */     {
/* 840 */       return false;
/*     */     }
/*     */ 
/* 843 */     this.bra = this.cursor;
/* 844 */     switch (among_var) {
/*     */     case 0:
/* 846 */       return false;
/*     */     case 1:
/* 850 */       if (!r_RV())
/*     */       {
/* 852 */         return false;
/*     */       }
/*     */ 
/* 855 */       slice_del();
/* 856 */       break;
/*     */     case 2:
/* 860 */       slice_from("z");
/*     */     }
/*     */ 
/* 863 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean stem()
/*     */   {
/* 873 */     int v_1 = this.cursor;
/*     */ 
/* 876 */     if (!r_mark_regions());
/* 881 */     this.cursor = v_1;
/*     */ 
/* 883 */     this.limit_backward = this.cursor; this.cursor = this.limit;
/*     */     int v_2;
/*     */     do {
/* 888 */       v_2 = this.limit - this.cursor;
/*     */     }
/*     */ 
/* 891 */     while (r_aditzak());
/*     */ 
/* 897 */     this.cursor = (this.limit - v_2);
/*     */     int v_3;
/*     */     do
/*     */     {
/* 903 */       v_3 = this.limit - this.cursor;
/*     */     }
/*     */ 
/* 906 */     while (r_izenak());
/*     */ 
/* 912 */     this.cursor = (this.limit - v_3);
/*     */ 
/* 916 */     int v_4 = this.limit - this.cursor;
/*     */ 
/* 919 */     if (!r_adjetiboak());
/* 924 */     this.cursor = (this.limit - v_4);
/* 925 */     this.cursor = this.limit_backward; return true;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o) {
/* 929 */     return o instanceof BasqueStemmer;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 933 */     return BasqueStemmer.class.getName().hashCode();
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.tartarus.snowball.ext.BasqueStemmer
 * JD-Core Version:    0.6.2
 */