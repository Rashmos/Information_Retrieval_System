/*     */ package org.apache.lucene.analysis.el;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.lucene.analysis.CharArraySet;
/*     */ import org.apache.lucene.util.Version;
/*     */ 
/*     */ public class GreekStemmer
/*     */ {
/* 199 */   private static final CharArraySet exc4 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "θ", "δ", "ελ", "γαλ", "ν", "π", "ιδ", "παρ" }), false);
/*     */ 
/* 225 */   private static final CharArraySet exc6 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "αλ", "αδ", "ενδ", "αμαν", "αμμοχαλ", "ηθ", "ανηθ", "αντιδ", "φυσ", "βρωμ", "γερ", "εξωδ", "καλπ", "καλλιν", "καταδ", "μουλ", "μπαν", "μπαγιατ", "μπολ", "μποσ", "νιτ", "ξικ", "συνομηλ", "πετσ", "πιτσ", "πικαντ", "πλιατσ", "ποστελν", "πρωτοδ", "σερτ", "συναδ", "τσαμ", "υποδ", "φιλον", "φυλοδ", "χασ" }), false);
/*     */ 
/* 250 */   private static final CharArraySet exc7 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "αναπ", "αποθ", "αποκ", "αποστ", "βουβ", "ξεθ", "ουλ", "πεθ", "πικρ", "ποτ", "σιχ", "χ" }), false);
/*     */ 
/* 277 */   private static final CharArraySet exc8a = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "τρ", "τσ" }), false);
/*     */ 
/* 281 */   private static final CharArraySet exc8b = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "βετερ", "βουλκ", "βραχμ", "γ", "δραδουμ", "θ", "καλπουζ", "καστελ", "κορμορ", "λαοπλ", "μωαμεθ", "μ", "μουσουλμ", "ν", "ουλ", "π", "πελεκ", "πλ", "πολισ", "πορτολ", "σαρακατσ", "σουλτ", "τσαρλατ", "ορφ", "τσιγγ", "τσοπ", "φωτοστεφ", "χ", "ψυχοπλ", "αγ", "ορφ", "γαλ", "γερ", "δεκ", "διπλ", "αμερικαν", "ουρ", "πιθ", "πουριτ", "σ", "ζωντ", "ικ", "καστ", "κοπ", "λιχ", "λουθηρ", "μαιντ", "μελ", "σιγ", "σπ", "στεγ", "τραγ", "τσαγ", "φ", "ερ", "αδαπ", "αθιγγ", "αμηχ", "ανικ", "ανοργ", "απηγ", "απιθ", "ατσιγγ", "βασ", "βασκ", "βαθυγαλ", "βιομηχ", "βραχυκ", "διατ", "διαφ", "ενοργ", "θυσ", "καπνοβιομηχ", "καταγαλ", "κλιβ", "κοιλαρφ", "λιβ", "μεγλοβιομηχ", "μικροβιομηχ", "νταβ", "ξηροκλιβ", "ολιγοδαμ", "ολογαλ", "πενταρφ", "περηφ", "περιτρ", "πλατ", "πολυδαπ", "πολυμηχ", "στεφ", "ταβ", "τετ", "υπερηφ", "υποκοπ", "χαμηλοδαπ", "ψηλοταβ" }), false);
/*     */ 
/* 340 */   private static final CharArraySet exc9 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "αβαρ", "βεν", "εναρ", "αβρ", "αδ", "αθ", "αν", "απλ", "βαρον", "ντρ", "σκ", "κοπ", "μπορ", "νιφ", "παγ", "παρακαλ", "σερπ", "σκελ", "συρφ", "τοκ", "υ", "δ", "εμ", "θαρρ", "θ" }), false);
/*     */ 
/* 428 */   private static final CharArraySet exc12a = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "π", "απ", "συμπ", "ασυμπ", "ακαταπ", "αμεταμφ" }), false);
/*     */ 
/* 432 */   private static final CharArraySet exc12b = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "αλ", "αρ", "εκτελ", "ζ", "μ", "ξ", "παρακαλ", "αρ", "προ", "νισ" }), false);
/*     */ 
/* 452 */   private static final CharArraySet exc13 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "διαθ", "θ", "παρακαταθ", "προσθ", "συνθ" }), false);
/*     */ 
/* 486 */   private static final CharArraySet exc14 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "φαρμακ", "χαδ", "αγκ", "αναρρ", "βρομ", "εκλιπ", "λαμπιδ", "λεχ", "μ", "πατ", "ρ", "λ", "μεδ", "μεσαζ", "υποτειν", "αμ", "αιθ", "ανηκ", "δεσποζ", "ενδιαφερ", "δε", "δευτερευ", "καθαρευ", "πλε", "τσα" }), false);
/*     */ 
/* 524 */   private static final CharArraySet exc15a = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "αβαστ", "πολυφ", "αδηφ", "παμφ", "ρ", "ασπ", "αφ", "αμαλ", "αμαλλι", "ανυστ", "απερ", "ασπαρ", "αχαρ", "δερβεν", "δροσοπ", "ξεφ", "νεοπ", "νομοτ", "ολοπ", "ομοτ", "προστ", "προσωποπ", "συμπ", "συντ", "τ", "υποτ", "χαρ", "αειπ", "αιμοστ", "ανυπ", "αποτ", "αρτιπ", "διατ", "εν", "επιτ", "κροκαλοπ", "σιδηροπ", "λ", "ναυ", "ουλαμ", "ουρ", "π", "τρ", "μ" }), false);
/*     */ 
/* 533 */   private static final CharArraySet exc15b = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "ψοφ", "ναυλοχ" }), false);
/*     */ 
/* 570 */   private static final CharArraySet exc16 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "ν", "χερσον", "δωδεκαν", "ερημον", "μεγαλον", "επταν" }), false);
/*     */ 
/* 590 */   private static final CharArraySet exc17 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "ασβ", "σβ", "αχρ", "χρ", "απλ", "αειμν", "δυσχρ", "ευχρ", "κοινοχρ", "παλιμψ" }), false);
/*     */ 
/* 604 */   private static final CharArraySet exc18 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "ν", "ρ", "σπι", "στραβομουτσ", "κακομουτσ", "εξων" }), false);
/*     */ 
/* 628 */   private static final CharArraySet exc19 = new CharArraySet(Version.LUCENE_31, Arrays.asList(new String[] { "παρασουσ", "φ", "χ", "ωριοπλ", "αζ", "αλλοσουσ", "ασουσ" }), false);
/*     */ 
/*     */   public int stem(char[] s, int len)
/*     */   {
/*  36 */     if (len < 4) {
/*  37 */       return len;
/*     */     }
/*  39 */     int origLen = len;
/*     */ 
/*  41 */     len = rule0(s, len);
/*  42 */     len = rule1(s, len);
/*  43 */     len = rule2(s, len);
/*  44 */     len = rule3(s, len);
/*  45 */     len = rule4(s, len);
/*  46 */     len = rule5(s, len);
/*  47 */     len = rule6(s, len);
/*  48 */     len = rule7(s, len);
/*  49 */     len = rule8(s, len);
/*  50 */     len = rule9(s, len);
/*  51 */     len = rule10(s, len);
/*  52 */     len = rule11(s, len);
/*  53 */     len = rule12(s, len);
/*  54 */     len = rule13(s, len);
/*  55 */     len = rule14(s, len);
/*  56 */     len = rule15(s, len);
/*  57 */     len = rule16(s, len);
/*  58 */     len = rule17(s, len);
/*  59 */     len = rule18(s, len);
/*  60 */     len = rule19(s, len);
/*  61 */     len = rule20(s, len);
/*     */ 
/*  63 */     if (len == origLen) {
/*  64 */       len = rule21(s, len);
/*     */     }
/*  66 */     return rule22(s, len);
/*     */   }
/*     */ 
/*     */   private int rule0(char[] s, int len) {
/*  70 */     if ((len > 9) && ((endsWith(s, len, "καθεστωτοσ")) || (endsWith(s, len, "καθεστωτων"))))
/*     */     {
/*  72 */       return len - 4;
/*     */     }
/*  74 */     if ((len > 8) && ((endsWith(s, len, "γεγονοτοσ")) || (endsWith(s, len, "γεγονοτων"))))
/*     */     {
/*  76 */       return len - 4;
/*     */     }
/*  78 */     if ((len > 8) && (endsWith(s, len, "καθεστωτα"))) {
/*  79 */       return len - 3;
/*     */     }
/*  81 */     if ((len > 7) && ((endsWith(s, len, "τατογιου")) || (endsWith(s, len, "τατογιων"))))
/*     */     {
/*  83 */       return len - 4;
/*     */     }
/*  85 */     if ((len > 7) && (endsWith(s, len, "γεγονοτα"))) {
/*  86 */       return len - 3;
/*     */     }
/*  88 */     if ((len > 7) && (endsWith(s, len, "καθεστωσ"))) {
/*  89 */       return len - 2;
/*     */     }
/*  91 */     if (((len > 6) && (endsWith(s, len, "σκαγιου"))) || (endsWith(s, len, "σκαγιων")) || (endsWith(s, len, "ολογιου")) || (endsWith(s, len, "ολογιων")) || (endsWith(s, len, "κρεατοσ")) || (endsWith(s, len, "κρεατων")) || (endsWith(s, len, "περατοσ")) || (endsWith(s, len, "περατων")) || (endsWith(s, len, "τερατοσ")) || (endsWith(s, len, "τερατων")))
/*     */     {
/* 101 */       return len - 4;
/*     */     }
/* 103 */     if ((len > 6) && (endsWith(s, len, "τατογια"))) {
/* 104 */       return len - 3;
/*     */     }
/* 106 */     if ((len > 6) && (endsWith(s, len, "γεγονοσ"))) {
/* 107 */       return len - 2;
/*     */     }
/* 109 */     if ((len > 5) && ((endsWith(s, len, "φαγιου")) || (endsWith(s, len, "φαγιων")) || (endsWith(s, len, "σογιου")) || (endsWith(s, len, "σογιων"))))
/*     */     {
/* 113 */       return len - 4;
/*     */     }
/* 115 */     if ((len > 5) && ((endsWith(s, len, "σκαγια")) || (endsWith(s, len, "ολογια")) || (endsWith(s, len, "κρεατα")) || (endsWith(s, len, "περατα")) || (endsWith(s, len, "τερατα"))))
/*     */     {
/* 120 */       return len - 3;
/*     */     }
/* 122 */     if ((len > 4) && ((endsWith(s, len, "φαγια")) || (endsWith(s, len, "σογια")) || (endsWith(s, len, "φωτοσ")) || (endsWith(s, len, "φωτων"))))
/*     */     {
/* 126 */       return len - 3;
/*     */     }
/* 128 */     if ((len > 4) && ((endsWith(s, len, "κρεασ")) || (endsWith(s, len, "περασ")) || (endsWith(s, len, "τερασ"))))
/*     */     {
/* 131 */       return len - 2;
/*     */     }
/* 133 */     if ((len > 3) && (endsWith(s, len, "φωτα"))) {
/* 134 */       return len - 2;
/*     */     }
/* 136 */     if ((len > 2) && (endsWith(s, len, "φωσ"))) {
/* 137 */       return len - 1;
/*     */     }
/* 139 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule1(char[] s, int len) {
/* 143 */     if ((len > 4) && ((endsWith(s, len, "αδεσ")) || (endsWith(s, len, "αδων")))) {
/* 144 */       len -= 4;
/* 145 */       if ((!endsWith(s, len, "οκ")) && (!endsWith(s, len, "μαμ")) && (!endsWith(s, len, "μαν")) && (!endsWith(s, len, "μπαμπ")) && (!endsWith(s, len, "πατερ")) && (!endsWith(s, len, "γιαγι")) && (!endsWith(s, len, "νταντ")) && (!endsWith(s, len, "κυρ")) && (!endsWith(s, len, "θει")) && (!endsWith(s, len, "πεθερ")))
/*     */       {
/* 155 */         len += 2;
/*     */       }
/*     */     }
/* 157 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule2(char[] s, int len) {
/* 161 */     if ((len > 4) && ((endsWith(s, len, "εδεσ")) || (endsWith(s, len, "εδων")))) {
/* 162 */       len -= 4;
/* 163 */       if ((endsWith(s, len, "οπ")) || (endsWith(s, len, "ιπ")) || (endsWith(s, len, "εμπ")) || (endsWith(s, len, "υπ")) || (endsWith(s, len, "γηπ")) || (endsWith(s, len, "δαπ")) || (endsWith(s, len, "κρασπ")) || (endsWith(s, len, "μιλ")))
/*     */       {
/* 171 */         len += 2;
/*     */       }
/*     */     }
/* 173 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule3(char[] s, int len) {
/* 177 */     if ((len > 5) && ((endsWith(s, len, "ουδεσ")) || (endsWith(s, len, "ουδων")))) {
/* 178 */       len -= 5;
/* 179 */       if ((endsWith(s, len, "αρκ")) || (endsWith(s, len, "καλιακ")) || (endsWith(s, len, "πεταλ")) || (endsWith(s, len, "λιχ")) || (endsWith(s, len, "πλεξ")) || (endsWith(s, len, "σκ")) || (endsWith(s, len, "σ")) || (endsWith(s, len, "φλ")) || (endsWith(s, len, "φρ")) || (endsWith(s, len, "βελ")) || (endsWith(s, len, "λουλ")) || (endsWith(s, len, "χν")) || (endsWith(s, len, "σπ")) || (endsWith(s, len, "τραγ")) || (endsWith(s, len, "φε")))
/*     */       {
/* 194 */         len += 3;
/*     */       }
/*     */     }
/* 196 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule4(char[] s, int len)
/*     */   {
/* 204 */     if ((len > 3) && ((endsWith(s, len, "εωσ")) || (endsWith(s, len, "εων")))) {
/* 205 */       len -= 3;
/* 206 */       if (exc4.contains(s, 0, len))
/* 207 */         len++;
/*     */     }
/* 209 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule5(char[] s, int len) {
/* 213 */     if ((len > 2) && (endsWith(s, len, "ια"))) {
/* 214 */       len -= 2;
/* 215 */       if (endsWithVowel(s, len))
/* 216 */         len++;
/* 217 */     } else if ((len > 3) && ((endsWith(s, len, "ιου")) || (endsWith(s, len, "ιων")))) {
/* 218 */       len -= 3;
/* 219 */       if (endsWithVowel(s, len))
/* 220 */         len++;
/*     */     }
/* 222 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule6(char[] s, int len)
/*     */   {
/* 234 */     boolean removed = false;
/* 235 */     if ((len > 3) && ((endsWith(s, len, "ικα")) || (endsWith(s, len, "ικο")))) {
/* 236 */       len -= 3;
/* 237 */       removed = true;
/* 238 */     } else if ((len > 4) && ((endsWith(s, len, "ικου")) || (endsWith(s, len, "ικων")))) {
/* 239 */       len -= 4;
/* 240 */       removed = true;
/*     */     }
/*     */ 
/* 243 */     if ((removed) && (
/* 244 */       (endsWithVowel(s, len)) || (exc6.contains(s, 0, len)))) {
/* 245 */       len += 2;
/*     */     }
/* 247 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule7(char[] s, int len)
/*     */   {
/* 256 */     if ((len == 5) && (endsWith(s, len, "αγαμε"))) {
/* 257 */       return len - 1;
/*     */     }
/* 259 */     if ((len > 7) && (endsWith(s, len, "ηθηκαμε")))
/* 260 */       len -= 7;
/* 261 */     else if ((len > 6) && (endsWith(s, len, "ουσαμε")))
/* 262 */       len -= 6;
/* 263 */     else if ((len > 5) && ((endsWith(s, len, "αγαμε")) || (endsWith(s, len, "ησαμε")) || (endsWith(s, len, "ηκαμε"))))
/*     */     {
/* 266 */       len -= 5;
/*     */     }
/* 268 */     if ((len > 3) && (endsWith(s, len, "αμε"))) {
/* 269 */       len -= 3;
/* 270 */       if (exc7.contains(s, 0, len)) {
/* 271 */         len += 2;
/*     */       }
/*     */     }
/* 274 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule8(char[] s, int len)
/*     */   {
/* 298 */     boolean removed = false;
/*     */ 
/* 300 */     if ((len > 8) && (endsWith(s, len, "ιουντανε"))) {
/* 301 */       len -= 8;
/* 302 */       removed = true;
/* 303 */     } else if (((len > 7) && (endsWith(s, len, "ιοντανε"))) || (endsWith(s, len, "ουντανε")) || (endsWith(s, len, "ηθηκανε")))
/*     */     {
/* 306 */       len -= 7;
/* 307 */       removed = true;
/* 308 */     } else if (((len > 6) && (endsWith(s, len, "ιοτανε"))) || (endsWith(s, len, "οντανε")) || (endsWith(s, len, "ουσανε")))
/*     */     {
/* 311 */       len -= 6;
/* 312 */       removed = true;
/* 313 */     } else if (((len > 5) && (endsWith(s, len, "αγανε"))) || (endsWith(s, len, "ησανε")) || (endsWith(s, len, "οτανε")) || (endsWith(s, len, "ηκανε")))
/*     */     {
/* 317 */       len -= 5;
/* 318 */       removed = true;
/*     */     }
/*     */ 
/* 321 */     if ((removed) && (exc8a.contains(s, 0, len)))
/*     */     {
/* 323 */       len += 4;
/* 324 */       s[(len - 4)] = 'α';
/* 325 */       s[(len - 3)] = 'γ';
/* 326 */       s[(len - 2)] = 'α';
/* 327 */       s[(len - 1)] = 'ν';
/*     */     }
/*     */ 
/* 330 */     if ((len > 3) && (endsWith(s, len, "ανε"))) {
/* 331 */       len -= 3;
/* 332 */       if ((endsWithVowelNoY(s, len)) || (exc8b.contains(s, 0, len))) {
/* 333 */         len += 2;
/*     */       }
/*     */     }
/*     */ 
/* 337 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule9(char[] s, int len)
/*     */   {
/* 347 */     if ((len > 5) && (endsWith(s, len, "ησετε"))) {
/* 348 */       len -= 5;
/*     */     }
/* 350 */     if ((len > 3) && (endsWith(s, len, "ετε"))) {
/* 351 */       len -= 3;
/* 352 */       if ((exc9.contains(s, 0, len)) || (endsWithVowelNoY(s, len)) || (endsWith(s, len, "οδ")) || (endsWith(s, len, "αιρ")) || (endsWith(s, len, "φορ")) || (endsWith(s, len, "ταθ")) || (endsWith(s, len, "διαθ")) || (endsWith(s, len, "σχ")) || (endsWith(s, len, "ενδ")) || (endsWith(s, len, "ευρ")) || (endsWith(s, len, "τιθ")) || (endsWith(s, len, "υπερθ")) || (endsWith(s, len, "ραθ")) || (endsWith(s, len, "ενθ")) || (endsWith(s, len, "ροθ")) || (endsWith(s, len, "σθ")) || (endsWith(s, len, "πυρ")) || (endsWith(s, len, "αιν")) || (endsWith(s, len, "συνδ")) || (endsWith(s, len, "συν")) || (endsWith(s, len, "συνθ")) || (endsWith(s, len, "χωρ")) || (endsWith(s, len, "πον")) || (endsWith(s, len, "βρ")) || (endsWith(s, len, "καθ")) || (endsWith(s, len, "ευθ")) || (endsWith(s, len, "εκθ")) || (endsWith(s, len, "νετ")) || (endsWith(s, len, "ρον")) || (endsWith(s, len, "αρκ")) || (endsWith(s, len, "βαρ")) || (endsWith(s, len, "βολ")) || (endsWith(s, len, "ωφελ")))
/*     */       {
/* 385 */         len += 2;
/*     */       }
/*     */     }
/*     */ 
/* 389 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule10(char[] s, int len) {
/* 393 */     if ((len > 5) && ((endsWith(s, len, "οντασ")) || (endsWith(s, len, "ωντασ")))) {
/* 394 */       len -= 5;
/* 395 */       if ((len == 3) && (endsWith(s, len, "αρχ"))) {
/* 396 */         len += 3;
/* 397 */         s[(len - 3)] = 'ο';
/*     */       }
/* 399 */       if (endsWith(s, len, "κρε")) {
/* 400 */         len += 3;
/* 401 */         s[(len - 3)] = 'ω';
/*     */       }
/*     */     }
/*     */ 
/* 405 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule11(char[] s, int len) {
/* 409 */     if ((len > 6) && (endsWith(s, len, "ομαστε"))) {
/* 410 */       len -= 6;
/* 411 */       if ((len == 2) && (endsWith(s, len, "ον")))
/* 412 */         len += 5;
/*     */     }
/* 414 */     else if ((len > 7) && (endsWith(s, len, "ιομαστε"))) {
/* 415 */       len -= 7;
/* 416 */       if ((len == 2) && (endsWith(s, len, "ον"))) {
/* 417 */         len += 5;
/* 418 */         s[(len - 5)] = 'ο';
/* 419 */         s[(len - 4)] = 'μ';
/* 420 */         s[(len - 3)] = 'α';
/* 421 */         s[(len - 2)] = 'σ';
/* 422 */         s[(len - 1)] = 'τ';
/*     */       }
/*     */     }
/* 425 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule12(char[] s, int len)
/*     */   {
/* 437 */     if ((len > 5) && (endsWith(s, len, "ιεστε"))) {
/* 438 */       len -= 5;
/* 439 */       if (exc12a.contains(s, 0, len)) {
/* 440 */         len += 4;
/*     */       }
/*     */     }
/* 443 */     if ((len > 4) && (endsWith(s, len, "εστε"))) {
/* 444 */       len -= 4;
/* 445 */       if (exc12b.contains(s, 0, len)) {
/* 446 */         len += 3;
/*     */       }
/*     */     }
/* 449 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule13(char[] s, int len)
/*     */   {
/* 457 */     if ((len > 6) && (endsWith(s, len, "ηθηκεσ")))
/* 458 */       len -= 6;
/* 459 */     else if ((len > 5) && ((endsWith(s, len, "ηθηκα")) || (endsWith(s, len, "ηθηκε")))) {
/* 460 */       len -= 5;
/*     */     }
/*     */ 
/* 463 */     boolean removed = false;
/*     */ 
/* 465 */     if ((len > 4) && (endsWith(s, len, "ηκεσ"))) {
/* 466 */       len -= 4;
/* 467 */       removed = true;
/* 468 */     } else if ((len > 3) && ((endsWith(s, len, "ηκα")) || (endsWith(s, len, "ηκε")))) {
/* 469 */       len -= 3;
/* 470 */       removed = true;
/*     */     }
/*     */ 
/* 473 */     if ((removed) && ((exc13.contains(s, 0, len)) || (endsWith(s, len, "σκωλ")) || (endsWith(s, len, "σκουλ")) || (endsWith(s, len, "ναρθ")) || (endsWith(s, len, "σφ")) || (endsWith(s, len, "οθ")) || (endsWith(s, len, "πιθ"))))
/*     */     {
/* 480 */       len += 2;
/*     */     }
/*     */ 
/* 483 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule14(char[] s, int len)
/*     */   {
/* 494 */     boolean removed = false;
/*     */ 
/* 496 */     if ((len > 5) && (endsWith(s, len, "ουσεσ"))) {
/* 497 */       len -= 5;
/* 498 */       removed = true;
/* 499 */     } else if ((len > 4) && ((endsWith(s, len, "ουσα")) || (endsWith(s, len, "ουσε")))) {
/* 500 */       len -= 4;
/* 501 */       removed = true;
/*     */     }
/*     */ 
/* 504 */     if ((removed) && ((exc14.contains(s, 0, len)) || (endsWithVowel(s, len)) || (endsWith(s, len, "ποδαρ")) || (endsWith(s, len, "βλεπ")) || (endsWith(s, len, "πανταχ")) || (endsWith(s, len, "φρυδ")) || (endsWith(s, len, "μαντιλ")) || (endsWith(s, len, "μαλλ")) || (endsWith(s, len, "κυματ")) || (endsWith(s, len, "λαχ")) || (endsWith(s, len, "ληγ")) || (endsWith(s, len, "φαγ")) || (endsWith(s, len, "ομ")) || (endsWith(s, len, "πρωτ"))))
/*     */     {
/* 518 */       len += 3;
/*     */     }
/*     */ 
/* 521 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule15(char[] s, int len)
/*     */   {
/* 538 */     boolean removed = false;
/* 539 */     if ((len > 4) && (endsWith(s, len, "αγεσ"))) {
/* 540 */       len -= 4;
/* 541 */       removed = true;
/* 542 */     } else if ((len > 3) && ((endsWith(s, len, "αγα")) || (endsWith(s, len, "αγε")))) {
/* 543 */       len -= 3;
/* 544 */       removed = true;
/*     */     }
/*     */ 
/* 547 */     if (removed) {
/* 548 */       boolean cond1 = (exc15a.contains(s, 0, len)) || (endsWith(s, len, "οφ")) || (endsWith(s, len, "πελ")) || (endsWith(s, len, "χορτ")) || (endsWith(s, len, "λλ")) || (endsWith(s, len, "σφ")) || (endsWith(s, len, "ρπ")) || (endsWith(s, len, "φρ")) || (endsWith(s, len, "πρ")) || (endsWith(s, len, "λοχ")) || (endsWith(s, len, "σμην"));
/*     */ 
/* 560 */       boolean cond2 = (exc15b.contains(s, 0, len)) || (endsWith(s, len, "κολλ"));
/*     */ 
/* 563 */       if ((cond1) && (!cond2)) {
/* 564 */         len += 2;
/*     */       }
/*     */     }
/* 567 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule16(char[] s, int len)
/*     */   {
/* 575 */     boolean removed = false;
/* 576 */     if ((len > 4) && (endsWith(s, len, "ησου"))) {
/* 577 */       len -= 4;
/* 578 */       removed = true;
/* 579 */     } else if ((len > 3) && ((endsWith(s, len, "ησε")) || (endsWith(s, len, "ησα")))) {
/* 580 */       len -= 3;
/* 581 */       removed = true;
/*     */     }
/*     */ 
/* 584 */     if ((removed) && (exc16.contains(s, 0, len))) {
/* 585 */       len += 2;
/*     */     }
/* 587 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule17(char[] s, int len)
/*     */   {
/* 595 */     if ((len > 4) && (endsWith(s, len, "ηστε"))) {
/* 596 */       len -= 4;
/* 597 */       if (exc17.contains(s, 0, len)) {
/* 598 */         len += 3;
/*     */       }
/*     */     }
/* 601 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule18(char[] s, int len)
/*     */   {
/* 609 */     boolean removed = false;
/*     */ 
/* 611 */     if ((len > 6) && ((endsWith(s, len, "ησουνε")) || (endsWith(s, len, "ηθουνε")))) {
/* 612 */       len -= 6;
/* 613 */       removed = true;
/* 614 */     } else if ((len > 4) && (endsWith(s, len, "ουνε"))) {
/* 615 */       len -= 4;
/* 616 */       removed = true;
/*     */     }
/*     */ 
/* 619 */     if ((removed) && (exc18.contains(s, 0, len))) {
/* 620 */       len += 3;
/* 621 */       s[(len - 3)] = 'ο';
/* 622 */       s[(len - 2)] = 'υ';
/* 623 */       s[(len - 1)] = 'ν';
/*     */     }
/* 625 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule19(char[] s, int len)
/*     */   {
/* 633 */     boolean removed = false;
/*     */ 
/* 635 */     if ((len > 6) && ((endsWith(s, len, "ησουμε")) || (endsWith(s, len, "ηθουμε")))) {
/* 636 */       len -= 6;
/* 637 */       removed = true;
/* 638 */     } else if ((len > 4) && (endsWith(s, len, "ουμε"))) {
/* 639 */       len -= 4;
/* 640 */       removed = true;
/*     */     }
/*     */ 
/* 643 */     if ((removed) && (exc19.contains(s, 0, len))) {
/* 644 */       len += 3;
/* 645 */       s[(len - 3)] = 'ο';
/* 646 */       s[(len - 2)] = 'υ';
/* 647 */       s[(len - 1)] = 'μ';
/*     */     }
/* 649 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule20(char[] s, int len) {
/* 653 */     if ((len > 5) && ((endsWith(s, len, "ματων")) || (endsWith(s, len, "ματοσ"))))
/* 654 */       len -= 3;
/* 655 */     else if ((len > 4) && (endsWith(s, len, "ματα")))
/* 656 */       len -= 2;
/* 657 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule21(char[] s, int len) {
/* 661 */     if ((len > 9) && (endsWith(s, len, "ιοντουσαν"))) {
/* 662 */       return len - 9;
/*     */     }
/* 664 */     if ((len > 8) && ((endsWith(s, len, "ιομασταν")) || (endsWith(s, len, "ιοσασταν")) || (endsWith(s, len, "ιουμαστε")) || (endsWith(s, len, "οντουσαν"))))
/*     */     {
/* 668 */       return len - 8;
/*     */     }
/* 670 */     if ((len > 7) && ((endsWith(s, len, "ιεμαστε")) || (endsWith(s, len, "ιεσαστε")) || (endsWith(s, len, "ιομουνα")) || (endsWith(s, len, "ιοσαστε")) || (endsWith(s, len, "ιοσουνα")) || (endsWith(s, len, "ιουνται")) || (endsWith(s, len, "ιουνταν")) || (endsWith(s, len, "ηθηκατε")) || (endsWith(s, len, "ομασταν")) || (endsWith(s, len, "οσασταν")) || (endsWith(s, len, "ουμαστε"))))
/*     */     {
/* 681 */       return len - 7;
/*     */     }
/* 683 */     if ((len > 6) && ((endsWith(s, len, "ιομουν")) || (endsWith(s, len, "ιονταν")) || (endsWith(s, len, "ιοσουν")) || (endsWith(s, len, "ηθειτε")) || (endsWith(s, len, "ηθηκαν")) || (endsWith(s, len, "ομουνα")) || (endsWith(s, len, "οσαστε")) || (endsWith(s, len, "οσουνα")) || (endsWith(s, len, "ουνται")) || (endsWith(s, len, "ουνταν")) || (endsWith(s, len, "ουσατε"))))
/*     */     {
/* 694 */       return len - 6;
/*     */     }
/* 696 */     if ((len > 5) && ((endsWith(s, len, "αγατε")) || (endsWith(s, len, "ιεμαι")) || (endsWith(s, len, "ιεται")) || (endsWith(s, len, "ιεσαι")) || (endsWith(s, len, "ιοταν")) || (endsWith(s, len, "ιουμα")) || (endsWith(s, len, "ηθεισ")) || (endsWith(s, len, "ηθουν")) || (endsWith(s, len, "ηκατε")) || (endsWith(s, len, "ησατε")) || (endsWith(s, len, "ησουν")) || (endsWith(s, len, "ομουν")) || (endsWith(s, len, "ονται")) || (endsWith(s, len, "ονταν")) || (endsWith(s, len, "οσουν")) || (endsWith(s, len, "ουμαι")) || (endsWith(s, len, "ουσαν"))))
/*     */     {
/* 713 */       return len - 5;
/*     */     }
/* 715 */     if ((len > 4) && ((endsWith(s, len, "αγαν")) || (endsWith(s, len, "αμαι")) || (endsWith(s, len, "ασαι")) || (endsWith(s, len, "αται")) || (endsWith(s, len, "ειτε")) || (endsWith(s, len, "εσαι")) || (endsWith(s, len, "εται")) || (endsWith(s, len, "ηδεσ")) || (endsWith(s, len, "ηδων")) || (endsWith(s, len, "ηθει")) || (endsWith(s, len, "ηκαν")) || (endsWith(s, len, "ησαν")) || (endsWith(s, len, "ησει")) || (endsWith(s, len, "ησεσ")) || (endsWith(s, len, "ομαι")) || (endsWith(s, len, "οταν"))))
/*     */     {
/* 731 */       return len - 4;
/*     */     }
/* 733 */     if ((len > 3) && ((endsWith(s, len, "αει")) || (endsWith(s, len, "εισ")) || (endsWith(s, len, "ηθω")) || (endsWith(s, len, "ησω")) || (endsWith(s, len, "ουν")) || (endsWith(s, len, "ουσ"))))
/*     */     {
/* 739 */       return len - 3;
/*     */     }
/* 741 */     if ((len > 2) && ((endsWith(s, len, "αν")) || (endsWith(s, len, "ασ")) || (endsWith(s, len, "αω")) || (endsWith(s, len, "ει")) || (endsWith(s, len, "εσ")) || (endsWith(s, len, "ησ")) || (endsWith(s, len, "οι")) || (endsWith(s, len, "οσ")) || (endsWith(s, len, "ου")) || (endsWith(s, len, "υσ")) || (endsWith(s, len, "ων"))))
/*     */     {
/* 752 */       return len - 2;
/*     */     }
/* 754 */     if ((len > 1) && (endsWithVowel(s, len))) {
/* 755 */       return len - 1;
/*     */     }
/* 757 */     return len;
/*     */   }
/*     */ 
/*     */   private int rule22(char[] s, int len) {
/* 761 */     if ((endsWith(s, len, "εστερ")) || (endsWith(s, len, "εστατ")))
/*     */     {
/* 763 */       return len - 5;
/*     */     }
/* 765 */     if ((endsWith(s, len, "οτερ")) || (endsWith(s, len, "οτατ")) || (endsWith(s, len, "υτερ")) || (endsWith(s, len, "υτατ")) || (endsWith(s, len, "ωτερ")) || (endsWith(s, len, "ωτατ")))
/*     */     {
/* 771 */       return len - 4;
/*     */     }
/* 773 */     return len;
/*     */   }
/*     */ 
/*     */   private boolean endsWith(char[] s, int len, String suffix) {
/* 777 */     int suffixLen = suffix.length();
/* 778 */     if (suffixLen > len)
/* 779 */       return false;
/* 780 */     for (int i = suffixLen - 1; i >= 0; i--) {
/* 781 */       if (s[(len - (suffixLen - i))] != suffix.charAt(i))
/* 782 */         return false;
/*     */     }
/* 784 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean endsWithVowel(char[] s, int len) {
/* 788 */     if (len == 0)
/* 789 */       return false;
/* 790 */     switch (s[(len - 1)]) {
/*     */     case 'α':
/*     */     case 'ε':
/*     */     case 'η':
/*     */     case 'ι':
/*     */     case 'ο':
/*     */     case 'υ':
/*     */     case 'ω':
/* 798 */       return true;
/*     */     case 'β':
/*     */     case 'γ':
/*     */     case 'δ':
/*     */     case 'ζ':
/*     */     case 'θ':
/*     */     case 'κ':
/*     */     case 'λ':
/*     */     case 'μ':
/*     */     case 'ν':
/*     */     case 'ξ':
/*     */     case 'π':
/*     */     case 'ρ':
/*     */     case 'ς':
/*     */     case 'σ':
/*     */     case 'τ':
/*     */     case 'φ':
/*     */     case 'χ':
/* 800 */     case 'ψ': } return false;
/*     */   }
/*     */ 
/*     */   private boolean endsWithVowelNoY(char[] s, int len)
/*     */   {
/* 805 */     if (len == 0)
/* 806 */       return false;
/* 807 */     switch (s[(len - 1)]) {
/*     */     case 'α':
/*     */     case 'ε':
/*     */     case 'η':
/*     */     case 'ι':
/*     */     case 'ο':
/*     */     case 'ω':
/* 814 */       return true;
/*     */     }
/* 816 */     return false;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.el.GreekStemmer
 * JD-Core Version:    0.6.2
 */