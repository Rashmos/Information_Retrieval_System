/*      */ package org.apache.lucene.analysis.br;
/*      */ 
/*      */ public class BrazilianStemmer
/*      */ {
/*      */   private String TERM;
/*      */   private String CT;
/*      */   private String R1;
/*      */   private String R2;
/*      */   private String RV;
/*      */ 
/*      */   protected String stem(String term)
/*      */   {
/*   45 */     boolean altered = false;
/*      */ 
/*   48 */     createCT(term);
/*      */ 
/*   50 */     if (!isIndexable(this.CT)) {
/*   51 */       return null;
/*      */     }
/*   53 */     if (!isStemmable(this.CT)) {
/*   54 */       return this.CT;
/*      */     }
/*      */ 
/*   57 */     this.R1 = getR1(this.CT);
/*   58 */     this.R2 = getR1(this.R1);
/*   59 */     this.RV = getRV(this.CT);
/*   60 */     this.TERM = (term + ";" + this.CT);
/*      */ 
/*   62 */     altered = step1();
/*   63 */     if (!altered) {
/*   64 */       altered = step2();
/*      */     }
/*      */ 
/*   67 */     if (altered)
/*   68 */       step3();
/*      */     else {
/*   70 */       step4();
/*      */     }
/*      */ 
/*   73 */     step5();
/*      */ 
/*   75 */     return this.CT;
/*      */   }
/*      */ 
/*      */   private boolean isStemmable(String term)
/*      */   {
/*   84 */     for (int c = 0; c < term.length(); c++)
/*      */     {
/*   86 */       if (!Character.isLetter(term.charAt(c))) {
/*   87 */         return false;
/*      */       }
/*      */     }
/*   90 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean isIndexable(String term)
/*      */   {
/*   99 */     return (term.length() < 30) && (term.length() > 2);
/*      */   }
/*      */ 
/*      */   private boolean isVowel(char value)
/*      */   {
/*  108 */     return (value == 'a') || (value == 'e') || (value == 'i') || (value == 'o') || (value == 'u');
/*      */   }
/*      */ 
/*      */   private String getR1(String value)
/*      */   {
/*  129 */     if (value == null) {
/*  130 */       return null;
/*      */     }
/*      */ 
/*  134 */     int i = value.length() - 1;
/*  135 */     for (int j = 0; (j < i) && 
/*  136 */       (!isVowel(value.charAt(j))); j++);
/*  141 */     if (j >= i) {
/*  142 */       return null;
/*      */     }
/*      */ 
/*  146 */     while ((j < i) && 
/*  147 */       (isVowel(value.charAt(j)))) {
/*  146 */       j++;
/*      */     }
/*      */ 
/*  152 */     if (j >= i) {
/*  153 */       return null;
/*      */     }
/*      */ 
/*  156 */     return value.substring(j + 1);
/*      */   }
/*      */ 
/*      */   private String getRV(String value)
/*      */   {
/*  181 */     if (value == null) {
/*  182 */       return null;
/*      */     }
/*      */ 
/*  185 */     int i = value.length() - 1;
/*      */ 
/*  189 */     if ((i > 0) && (!isVowel(value.charAt(1))))
/*      */     {
/*  191 */       for (int j = 2; (j < i) && 
/*  192 */         (!isVowel(value.charAt(j))); j++);
/*  197 */       if (j < i) {
/*  198 */         return value.substring(j + 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  205 */     if ((i > 1) && (isVowel(value.charAt(0))) && (isVowel(value.charAt(1))))
/*      */     {
/*  209 */       for (int j = 2; (j < i) && 
/*  210 */         (isVowel(value.charAt(j))); j++);
/*  215 */       if (j < i) {
/*  216 */         return value.substring(j + 1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  222 */     if (i > 2) {
/*  223 */       return value.substring(3);
/*      */     }
/*      */ 
/*  226 */     return null;
/*      */   }
/*      */ 
/*      */   private String changeTerm(String value)
/*      */   {
/*  239 */     String r = "";
/*      */ 
/*  242 */     if (value == null) {
/*  243 */       return null;
/*      */     }
/*      */ 
/*  246 */     value = value.toLowerCase();
/*  247 */     for (int j = 0; j < value.length(); j++) {
/*  248 */       if ((value.charAt(j) == 'á') || (value.charAt(j) == 'â') || (value.charAt(j) == 'ã'))
/*      */       {
/*  251 */         r = r + "a";
/*      */       }
/*  253 */       else if ((value.charAt(j) == 'é') || (value.charAt(j) == 'ê'))
/*      */       {
/*  255 */         r = r + "e";
/*      */       }
/*  257 */       else if (value.charAt(j) == 'í') {
/*  258 */         r = r + "i";
/*      */       }
/*  260 */       else if ((value.charAt(j) == 'ó') || (value.charAt(j) == 'ô') || (value.charAt(j) == 'õ'))
/*      */       {
/*  263 */         r = r + "o";
/*      */       }
/*  265 */       else if ((value.charAt(j) == 'ú') || (value.charAt(j) == 'ü'))
/*      */       {
/*  267 */         r = r + "u";
/*      */       }
/*  269 */       else if (value.charAt(j) == 'ç') {
/*  270 */         r = r + "c";
/*      */       }
/*  272 */       else if (value.charAt(j) == 'ñ') {
/*  273 */         r = r + "n";
/*      */       }
/*      */       else {
/*  276 */         r = r + value.charAt(j);
/*      */       }
/*      */     }
/*  279 */     return r;
/*      */   }
/*      */ 
/*      */   private boolean suffix(String value, String suffix)
/*      */   {
/*  290 */     if ((value == null) || (suffix == null)) {
/*  291 */       return false;
/*      */     }
/*      */ 
/*  294 */     if (suffix.length() > value.length()) {
/*  295 */       return false;
/*      */     }
/*      */ 
/*  298 */     return value.substring(value.length() - suffix.length()).equals(suffix);
/*      */   }
/*      */ 
/*      */   private String replaceSuffix(String value, String toReplace, String changeTo)
/*      */   {
/*  310 */     if ((value == null) || (toReplace == null) || (changeTo == null))
/*      */     {
/*  313 */       return value;
/*      */     }
/*      */ 
/*  316 */     String vvalue = removeSuffix(value, toReplace);
/*      */ 
/*  318 */     if (value.equals(vvalue)) {
/*  319 */       return value;
/*      */     }
/*  321 */     return vvalue + changeTo;
/*      */   }
/*      */ 
/*      */   private String removeSuffix(String value, String toRemove)
/*      */   {
/*  332 */     if ((value == null) || (toRemove == null) || (!suffix(value, toRemove)))
/*      */     {
/*  335 */       return value;
/*      */     }
/*      */ 
/*  338 */     return value.substring(0, value.length() - toRemove.length());
/*      */   }
/*      */ 
/*      */   private boolean suffixPreceded(String value, String suffix, String preceded)
/*      */   {
/*  348 */     if ((value == null) || (suffix == null) || (preceded == null) || (!suffix(value, suffix)))
/*      */     {
/*  352 */       return false;
/*      */     }
/*      */ 
/*  355 */     return suffix(removeSuffix(value, suffix), preceded);
/*      */   }
/*      */ 
/*      */   private void createCT(String term)
/*      */   {
/*  362 */     this.CT = changeTerm(term);
/*      */ 
/*  364 */     if (this.CT.length() < 2) return;
/*      */ 
/*  367 */     if ((this.CT.charAt(0) == '"') || (this.CT.charAt(0) == '\'') || (this.CT.charAt(0) == '-') || (this.CT.charAt(0) == ',') || (this.CT.charAt(0) == ';') || (this.CT.charAt(0) == '.') || (this.CT.charAt(0) == '?') || (this.CT.charAt(0) == '!'))
/*      */     {
/*  376 */       this.CT = this.CT.substring(1);
/*      */     }
/*      */ 
/*  379 */     if (this.CT.length() < 2) return;
/*      */ 
/*  382 */     if ((this.CT.charAt(this.CT.length() - 1) == '-') || (this.CT.charAt(this.CT.length() - 1) == ',') || (this.CT.charAt(this.CT.length() - 1) == ';') || (this.CT.charAt(this.CT.length() - 1) == '.') || (this.CT.charAt(this.CT.length() - 1) == '?') || (this.CT.charAt(this.CT.length() - 1) == '!') || (this.CT.charAt(this.CT.length() - 1) == '\'') || (this.CT.charAt(this.CT.length() - 1) == '"'))
/*      */     {
/*  391 */       this.CT = this.CT.substring(0, this.CT.length() - 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean step1()
/*      */   {
/*  404 */     if (this.CT == null) return false;
/*      */ 
/*  407 */     if ((suffix(this.CT, "uciones")) && (suffix(this.R2, "uciones"))) {
/*  408 */       this.CT = replaceSuffix(this.CT, "uciones", "u"); return true;
/*      */     }
/*      */ 
/*  412 */     if (this.CT.length() >= 6) {
/*  413 */       if ((suffix(this.CT, "imentos")) && (suffix(this.R2, "imentos"))) {
/*  414 */         this.CT = removeSuffix(this.CT, "imentos"); return true;
/*      */       }
/*  416 */       if ((suffix(this.CT, "amentos")) && (suffix(this.R2, "amentos"))) {
/*  417 */         this.CT = removeSuffix(this.CT, "amentos"); return true;
/*      */       }
/*  419 */       if ((suffix(this.CT, "adores")) && (suffix(this.R2, "adores"))) {
/*  420 */         this.CT = removeSuffix(this.CT, "adores"); return true;
/*      */       }
/*  422 */       if ((suffix(this.CT, "adoras")) && (suffix(this.R2, "adoras"))) {
/*  423 */         this.CT = removeSuffix(this.CT, "adoras"); return true;
/*      */       }
/*  425 */       if ((suffix(this.CT, "logias")) && (suffix(this.R2, "logias"))) {
/*  426 */         replaceSuffix(this.CT, "logias", "log"); return true;
/*      */       }
/*  428 */       if ((suffix(this.CT, "encias")) && (suffix(this.R2, "encias"))) {
/*  429 */         this.CT = replaceSuffix(this.CT, "encias", "ente"); return true;
/*      */       }
/*  431 */       if ((suffix(this.CT, "amente")) && (suffix(this.R1, "amente"))) {
/*  432 */         this.CT = removeSuffix(this.CT, "amente"); return true;
/*      */       }
/*  434 */       if ((suffix(this.CT, "idades")) && (suffix(this.R2, "idades"))) {
/*  435 */         this.CT = removeSuffix(this.CT, "idades"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  440 */     if (this.CT.length() >= 5) {
/*  441 */       if ((suffix(this.CT, "acoes")) && (suffix(this.R2, "acoes"))) {
/*  442 */         this.CT = removeSuffix(this.CT, "acoes"); return true;
/*      */       }
/*  444 */       if ((suffix(this.CT, "imento")) && (suffix(this.R2, "imento"))) {
/*  445 */         this.CT = removeSuffix(this.CT, "imento"); return true;
/*      */       }
/*  447 */       if ((suffix(this.CT, "amento")) && (suffix(this.R2, "amento"))) {
/*  448 */         this.CT = removeSuffix(this.CT, "amento"); return true;
/*      */       }
/*  450 */       if ((suffix(this.CT, "adora")) && (suffix(this.R2, "adora"))) {
/*  451 */         this.CT = removeSuffix(this.CT, "adora"); return true;
/*      */       }
/*  453 */       if ((suffix(this.CT, "ismos")) && (suffix(this.R2, "ismos"))) {
/*  454 */         this.CT = removeSuffix(this.CT, "ismos"); return true;
/*      */       }
/*  456 */       if ((suffix(this.CT, "istas")) && (suffix(this.R2, "istas"))) {
/*  457 */         this.CT = removeSuffix(this.CT, "istas"); return true;
/*      */       }
/*  459 */       if ((suffix(this.CT, "logia")) && (suffix(this.R2, "logia"))) {
/*  460 */         this.CT = replaceSuffix(this.CT, "logia", "log"); return true;
/*      */       }
/*  462 */       if ((suffix(this.CT, "ucion")) && (suffix(this.R2, "ucion"))) {
/*  463 */         this.CT = replaceSuffix(this.CT, "ucion", "u"); return true;
/*      */       }
/*  465 */       if ((suffix(this.CT, "encia")) && (suffix(this.R2, "encia"))) {
/*  466 */         this.CT = replaceSuffix(this.CT, "encia", "ente"); return true;
/*      */       }
/*  468 */       if ((suffix(this.CT, "mente")) && (suffix(this.R2, "mente"))) {
/*  469 */         this.CT = removeSuffix(this.CT, "mente"); return true;
/*      */       }
/*  471 */       if ((suffix(this.CT, "idade")) && (suffix(this.R2, "idade"))) {
/*  472 */         this.CT = removeSuffix(this.CT, "idade"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  477 */     if (this.CT.length() >= 4) {
/*  478 */       if ((suffix(this.CT, "acao")) && (suffix(this.R2, "acao"))) {
/*  479 */         this.CT = removeSuffix(this.CT, "acao"); return true;
/*      */       }
/*  481 */       if ((suffix(this.CT, "ezas")) && (suffix(this.R2, "ezas"))) {
/*  482 */         this.CT = removeSuffix(this.CT, "ezas"); return true;
/*      */       }
/*  484 */       if ((suffix(this.CT, "icos")) && (suffix(this.R2, "icos"))) {
/*  485 */         this.CT = removeSuffix(this.CT, "icos"); return true;
/*      */       }
/*  487 */       if ((suffix(this.CT, "icas")) && (suffix(this.R2, "icas"))) {
/*  488 */         this.CT = removeSuffix(this.CT, "icas"); return true;
/*      */       }
/*  490 */       if ((suffix(this.CT, "ismo")) && (suffix(this.R2, "ismo"))) {
/*  491 */         this.CT = removeSuffix(this.CT, "ismo"); return true;
/*      */       }
/*  493 */       if ((suffix(this.CT, "avel")) && (suffix(this.R2, "avel"))) {
/*  494 */         this.CT = removeSuffix(this.CT, "avel"); return true;
/*      */       }
/*  496 */       if ((suffix(this.CT, "ivel")) && (suffix(this.R2, "ivel"))) {
/*  497 */         this.CT = removeSuffix(this.CT, "ivel"); return true;
/*      */       }
/*  499 */       if ((suffix(this.CT, "ista")) && (suffix(this.R2, "ista"))) {
/*  500 */         this.CT = removeSuffix(this.CT, "ista"); return true;
/*      */       }
/*  502 */       if ((suffix(this.CT, "osos")) && (suffix(this.R2, "osos"))) {
/*  503 */         this.CT = removeSuffix(this.CT, "osos"); return true;
/*      */       }
/*  505 */       if ((suffix(this.CT, "osas")) && (suffix(this.R2, "osas"))) {
/*  506 */         this.CT = removeSuffix(this.CT, "osas"); return true;
/*      */       }
/*  508 */       if ((suffix(this.CT, "ador")) && (suffix(this.R2, "ador"))) {
/*  509 */         this.CT = removeSuffix(this.CT, "ador"); return true;
/*      */       }
/*  511 */       if ((suffix(this.CT, "ivas")) && (suffix(this.R2, "ivas"))) {
/*  512 */         this.CT = removeSuffix(this.CT, "ivas"); return true;
/*      */       }
/*  514 */       if ((suffix(this.CT, "ivos")) && (suffix(this.R2, "ivos"))) {
/*  515 */         this.CT = removeSuffix(this.CT, "ivos"); return true;
/*      */       }
/*  517 */       if ((suffix(this.CT, "iras")) && (suffix(this.RV, "iras")) && (suffixPreceded(this.CT, "iras", "e")))
/*      */       {
/*  520 */         this.CT = replaceSuffix(this.CT, "iras", "ir"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  525 */     if (this.CT.length() >= 3) {
/*  526 */       if ((suffix(this.CT, "eza")) && (suffix(this.R2, "eza"))) {
/*  527 */         this.CT = removeSuffix(this.CT, "eza"); return true;
/*      */       }
/*  529 */       if ((suffix(this.CT, "ico")) && (suffix(this.R2, "ico"))) {
/*  530 */         this.CT = removeSuffix(this.CT, "ico"); return true;
/*      */       }
/*  532 */       if ((suffix(this.CT, "ica")) && (suffix(this.R2, "ica"))) {
/*  533 */         this.CT = removeSuffix(this.CT, "ica"); return true;
/*      */       }
/*  535 */       if ((suffix(this.CT, "oso")) && (suffix(this.R2, "oso"))) {
/*  536 */         this.CT = removeSuffix(this.CT, "oso"); return true;
/*      */       }
/*  538 */       if ((suffix(this.CT, "osa")) && (suffix(this.R2, "osa"))) {
/*  539 */         this.CT = removeSuffix(this.CT, "osa"); return true;
/*      */       }
/*  541 */       if ((suffix(this.CT, "iva")) && (suffix(this.R2, "iva"))) {
/*  542 */         this.CT = removeSuffix(this.CT, "iva"); return true;
/*      */       }
/*  544 */       if ((suffix(this.CT, "ivo")) && (suffix(this.R2, "ivo"))) {
/*  545 */         this.CT = removeSuffix(this.CT, "ivo"); return true;
/*      */       }
/*  547 */       if ((suffix(this.CT, "ira")) && (suffix(this.RV, "ira")) && (suffixPreceded(this.CT, "ira", "e")))
/*      */       {
/*  550 */         this.CT = replaceSuffix(this.CT, "ira", "ir"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  555 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean step2()
/*      */   {
/*  568 */     if (this.RV == null) return false;
/*      */ 
/*  571 */     if (this.RV.length() >= 7) {
/*  572 */       if (suffix(this.RV, "issemos")) {
/*  573 */         this.CT = removeSuffix(this.CT, "issemos"); return true;
/*      */       }
/*  575 */       if (suffix(this.RV, "essemos")) {
/*  576 */         this.CT = removeSuffix(this.CT, "essemos"); return true;
/*      */       }
/*  578 */       if (suffix(this.RV, "assemos")) {
/*  579 */         this.CT = removeSuffix(this.CT, "assemos"); return true;
/*      */       }
/*  581 */       if (suffix(this.RV, "ariamos")) {
/*  582 */         this.CT = removeSuffix(this.CT, "ariamos"); return true;
/*      */       }
/*  584 */       if (suffix(this.RV, "eriamos")) {
/*  585 */         this.CT = removeSuffix(this.CT, "eriamos"); return true;
/*      */       }
/*  587 */       if (suffix(this.RV, "iriamos")) {
/*  588 */         this.CT = removeSuffix(this.CT, "iriamos"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  593 */     if (this.RV.length() >= 6) {
/*  594 */       if (suffix(this.RV, "iremos")) {
/*  595 */         this.CT = removeSuffix(this.CT, "iremos"); return true;
/*      */       }
/*  597 */       if (suffix(this.RV, "eremos")) {
/*  598 */         this.CT = removeSuffix(this.CT, "eremos"); return true;
/*      */       }
/*  600 */       if (suffix(this.RV, "aremos")) {
/*  601 */         this.CT = removeSuffix(this.CT, "aremos"); return true;
/*      */       }
/*  603 */       if (suffix(this.RV, "avamos")) {
/*  604 */         this.CT = removeSuffix(this.CT, "avamos"); return true;
/*      */       }
/*  606 */       if (suffix(this.RV, "iramos")) {
/*  607 */         this.CT = removeSuffix(this.CT, "iramos"); return true;
/*      */       }
/*  609 */       if (suffix(this.RV, "eramos")) {
/*  610 */         this.CT = removeSuffix(this.CT, "eramos"); return true;
/*      */       }
/*  612 */       if (suffix(this.RV, "aramos")) {
/*  613 */         this.CT = removeSuffix(this.CT, "aramos"); return true;
/*      */       }
/*  615 */       if (suffix(this.RV, "asseis")) {
/*  616 */         this.CT = removeSuffix(this.CT, "asseis"); return true;
/*      */       }
/*  618 */       if (suffix(this.RV, "esseis")) {
/*  619 */         this.CT = removeSuffix(this.CT, "esseis"); return true;
/*      */       }
/*  621 */       if (suffix(this.RV, "isseis")) {
/*  622 */         this.CT = removeSuffix(this.CT, "isseis"); return true;
/*      */       }
/*  624 */       if (suffix(this.RV, "arieis")) {
/*  625 */         this.CT = removeSuffix(this.CT, "arieis"); return true;
/*      */       }
/*  627 */       if (suffix(this.RV, "erieis")) {
/*  628 */         this.CT = removeSuffix(this.CT, "erieis"); return true;
/*      */       }
/*  630 */       if (suffix(this.RV, "irieis")) {
/*  631 */         this.CT = removeSuffix(this.CT, "irieis"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  637 */     if (this.RV.length() >= 5) {
/*  638 */       if (suffix(this.RV, "irmos")) {
/*  639 */         this.CT = removeSuffix(this.CT, "irmos"); return true;
/*      */       }
/*  641 */       if (suffix(this.RV, "iamos")) {
/*  642 */         this.CT = removeSuffix(this.CT, "iamos"); return true;
/*      */       }
/*  644 */       if (suffix(this.RV, "armos")) {
/*  645 */         this.CT = removeSuffix(this.CT, "armos"); return true;
/*      */       }
/*  647 */       if (suffix(this.RV, "ermos")) {
/*  648 */         this.CT = removeSuffix(this.CT, "ermos"); return true;
/*      */       }
/*  650 */       if (suffix(this.RV, "areis")) {
/*  651 */         this.CT = removeSuffix(this.CT, "areis"); return true;
/*      */       }
/*  653 */       if (suffix(this.RV, "ereis")) {
/*  654 */         this.CT = removeSuffix(this.CT, "ereis"); return true;
/*      */       }
/*  656 */       if (suffix(this.RV, "ireis")) {
/*  657 */         this.CT = removeSuffix(this.CT, "ireis"); return true;
/*      */       }
/*  659 */       if (suffix(this.RV, "asses")) {
/*  660 */         this.CT = removeSuffix(this.CT, "asses"); return true;
/*      */       }
/*  662 */       if (suffix(this.RV, "esses")) {
/*  663 */         this.CT = removeSuffix(this.CT, "esses"); return true;
/*      */       }
/*  665 */       if (suffix(this.RV, "isses")) {
/*  666 */         this.CT = removeSuffix(this.CT, "isses"); return true;
/*      */       }
/*  668 */       if (suffix(this.RV, "astes")) {
/*  669 */         this.CT = removeSuffix(this.CT, "astes"); return true;
/*      */       }
/*  671 */       if (suffix(this.RV, "assem")) {
/*  672 */         this.CT = removeSuffix(this.CT, "assem"); return true;
/*      */       }
/*  674 */       if (suffix(this.RV, "essem")) {
/*  675 */         this.CT = removeSuffix(this.CT, "essem"); return true;
/*      */       }
/*  677 */       if (suffix(this.RV, "issem")) {
/*  678 */         this.CT = removeSuffix(this.CT, "issem"); return true;
/*      */       }
/*  680 */       if (suffix(this.RV, "ardes")) {
/*  681 */         this.CT = removeSuffix(this.CT, "ardes"); return true;
/*      */       }
/*  683 */       if (suffix(this.RV, "erdes")) {
/*  684 */         this.CT = removeSuffix(this.CT, "erdes"); return true;
/*      */       }
/*  686 */       if (suffix(this.RV, "irdes")) {
/*  687 */         this.CT = removeSuffix(this.CT, "irdes"); return true;
/*      */       }
/*  689 */       if (suffix(this.RV, "ariam")) {
/*  690 */         this.CT = removeSuffix(this.CT, "ariam"); return true;
/*      */       }
/*  692 */       if (suffix(this.RV, "eriam")) {
/*  693 */         this.CT = removeSuffix(this.CT, "eriam"); return true;
/*      */       }
/*  695 */       if (suffix(this.RV, "iriam")) {
/*  696 */         this.CT = removeSuffix(this.CT, "iriam"); return true;
/*      */       }
/*  698 */       if (suffix(this.RV, "arias")) {
/*  699 */         this.CT = removeSuffix(this.CT, "arias"); return true;
/*      */       }
/*  701 */       if (suffix(this.RV, "erias")) {
/*  702 */         this.CT = removeSuffix(this.CT, "erias"); return true;
/*      */       }
/*  704 */       if (suffix(this.RV, "irias")) {
/*  705 */         this.CT = removeSuffix(this.CT, "irias"); return true;
/*      */       }
/*  707 */       if (suffix(this.RV, "estes")) {
/*  708 */         this.CT = removeSuffix(this.CT, "estes"); return true;
/*      */       }
/*  710 */       if (suffix(this.RV, "istes")) {
/*  711 */         this.CT = removeSuffix(this.CT, "istes"); return true;
/*      */       }
/*  713 */       if (suffix(this.RV, "areis")) {
/*  714 */         this.CT = removeSuffix(this.CT, "areis"); return true;
/*      */       }
/*  716 */       if (suffix(this.RV, "aveis")) {
/*  717 */         this.CT = removeSuffix(this.CT, "aveis"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  722 */     if (this.RV.length() >= 4) {
/*  723 */       if (suffix(this.RV, "aria")) {
/*  724 */         this.CT = removeSuffix(this.CT, "aria"); return true;
/*      */       }
/*  726 */       if (suffix(this.RV, "eria")) {
/*  727 */         this.CT = removeSuffix(this.CT, "eria"); return true;
/*      */       }
/*  729 */       if (suffix(this.RV, "iria")) {
/*  730 */         this.CT = removeSuffix(this.CT, "iria"); return true;
/*      */       }
/*  732 */       if (suffix(this.RV, "asse")) {
/*  733 */         this.CT = removeSuffix(this.CT, "asse"); return true;
/*      */       }
/*  735 */       if (suffix(this.RV, "esse")) {
/*  736 */         this.CT = removeSuffix(this.CT, "esse"); return true;
/*      */       }
/*  738 */       if (suffix(this.RV, "isse")) {
/*  739 */         this.CT = removeSuffix(this.CT, "isse"); return true;
/*      */       }
/*  741 */       if (suffix(this.RV, "aste")) {
/*  742 */         this.CT = removeSuffix(this.CT, "aste"); return true;
/*      */       }
/*  744 */       if (suffix(this.RV, "este")) {
/*  745 */         this.CT = removeSuffix(this.CT, "este"); return true;
/*      */       }
/*  747 */       if (suffix(this.RV, "iste")) {
/*  748 */         this.CT = removeSuffix(this.CT, "iste"); return true;
/*      */       }
/*  750 */       if (suffix(this.RV, "arei")) {
/*  751 */         this.CT = removeSuffix(this.CT, "arei"); return true;
/*      */       }
/*  753 */       if (suffix(this.RV, "erei")) {
/*  754 */         this.CT = removeSuffix(this.CT, "erei"); return true;
/*      */       }
/*  756 */       if (suffix(this.RV, "irei")) {
/*  757 */         this.CT = removeSuffix(this.CT, "irei"); return true;
/*      */       }
/*  759 */       if (suffix(this.RV, "aram")) {
/*  760 */         this.CT = removeSuffix(this.CT, "aram"); return true;
/*      */       }
/*  762 */       if (suffix(this.RV, "eram")) {
/*  763 */         this.CT = removeSuffix(this.CT, "eram"); return true;
/*      */       }
/*  765 */       if (suffix(this.RV, "iram")) {
/*  766 */         this.CT = removeSuffix(this.CT, "iram"); return true;
/*      */       }
/*  768 */       if (suffix(this.RV, "avam")) {
/*  769 */         this.CT = removeSuffix(this.CT, "avam"); return true;
/*      */       }
/*  771 */       if (suffix(this.RV, "arem")) {
/*  772 */         this.CT = removeSuffix(this.CT, "arem"); return true;
/*      */       }
/*  774 */       if (suffix(this.RV, "erem")) {
/*  775 */         this.CT = removeSuffix(this.CT, "erem"); return true;
/*      */       }
/*  777 */       if (suffix(this.RV, "irem")) {
/*  778 */         this.CT = removeSuffix(this.CT, "irem"); return true;
/*      */       }
/*  780 */       if (suffix(this.RV, "ando")) {
/*  781 */         this.CT = removeSuffix(this.CT, "ando"); return true;
/*      */       }
/*  783 */       if (suffix(this.RV, "endo")) {
/*  784 */         this.CT = removeSuffix(this.CT, "endo"); return true;
/*      */       }
/*  786 */       if (suffix(this.RV, "indo")) {
/*  787 */         this.CT = removeSuffix(this.CT, "indo"); return true;
/*      */       }
/*  789 */       if (suffix(this.RV, "arao")) {
/*  790 */         this.CT = removeSuffix(this.CT, "arao"); return true;
/*      */       }
/*  792 */       if (suffix(this.RV, "erao")) {
/*  793 */         this.CT = removeSuffix(this.CT, "erao"); return true;
/*      */       }
/*  795 */       if (suffix(this.RV, "irao")) {
/*  796 */         this.CT = removeSuffix(this.CT, "irao"); return true;
/*      */       }
/*  798 */       if (suffix(this.RV, "adas")) {
/*  799 */         this.CT = removeSuffix(this.CT, "adas"); return true;
/*      */       }
/*  801 */       if (suffix(this.RV, "idas")) {
/*  802 */         this.CT = removeSuffix(this.CT, "idas"); return true;
/*      */       }
/*  804 */       if (suffix(this.RV, "aras")) {
/*  805 */         this.CT = removeSuffix(this.CT, "aras"); return true;
/*      */       }
/*  807 */       if (suffix(this.RV, "eras")) {
/*  808 */         this.CT = removeSuffix(this.CT, "eras"); return true;
/*      */       }
/*  810 */       if (suffix(this.RV, "iras")) {
/*  811 */         this.CT = removeSuffix(this.CT, "iras"); return true;
/*      */       }
/*  813 */       if (suffix(this.RV, "avas")) {
/*  814 */         this.CT = removeSuffix(this.CT, "avas"); return true;
/*      */       }
/*  816 */       if (suffix(this.RV, "ares")) {
/*  817 */         this.CT = removeSuffix(this.CT, "ares"); return true;
/*      */       }
/*  819 */       if (suffix(this.RV, "eres")) {
/*  820 */         this.CT = removeSuffix(this.CT, "eres"); return true;
/*      */       }
/*  822 */       if (suffix(this.RV, "ires")) {
/*  823 */         this.CT = removeSuffix(this.CT, "ires"); return true;
/*      */       }
/*  825 */       if (suffix(this.RV, "ados")) {
/*  826 */         this.CT = removeSuffix(this.CT, "ados"); return true;
/*      */       }
/*  828 */       if (suffix(this.RV, "idos")) {
/*  829 */         this.CT = removeSuffix(this.CT, "idos"); return true;
/*      */       }
/*  831 */       if (suffix(this.RV, "amos")) {
/*  832 */         this.CT = removeSuffix(this.CT, "amos"); return true;
/*      */       }
/*  834 */       if (suffix(this.RV, "emos")) {
/*  835 */         this.CT = removeSuffix(this.CT, "emos"); return true;
/*      */       }
/*  837 */       if (suffix(this.RV, "imos")) {
/*  838 */         this.CT = removeSuffix(this.CT, "imos"); return true;
/*      */       }
/*  840 */       if (suffix(this.RV, "iras")) {
/*  841 */         this.CT = removeSuffix(this.CT, "iras"); return true;
/*      */       }
/*  843 */       if (suffix(this.RV, "ieis")) {
/*  844 */         this.CT = removeSuffix(this.CT, "ieis"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  849 */     if (this.RV.length() >= 3) {
/*  850 */       if (suffix(this.RV, "ada")) {
/*  851 */         this.CT = removeSuffix(this.CT, "ada"); return true;
/*      */       }
/*  853 */       if (suffix(this.RV, "ida")) {
/*  854 */         this.CT = removeSuffix(this.CT, "ida"); return true;
/*      */       }
/*  856 */       if (suffix(this.RV, "ara")) {
/*  857 */         this.CT = removeSuffix(this.CT, "ara"); return true;
/*      */       }
/*  859 */       if (suffix(this.RV, "era")) {
/*  860 */         this.CT = removeSuffix(this.CT, "era"); return true;
/*      */       }
/*  862 */       if (suffix(this.RV, "ira")) {
/*  863 */         this.CT = removeSuffix(this.CT, "ava"); return true;
/*      */       }
/*  865 */       if (suffix(this.RV, "iam")) {
/*  866 */         this.CT = removeSuffix(this.CT, "iam"); return true;
/*      */       }
/*  868 */       if (suffix(this.RV, "ado")) {
/*  869 */         this.CT = removeSuffix(this.CT, "ado"); return true;
/*      */       }
/*  871 */       if (suffix(this.RV, "ido")) {
/*  872 */         this.CT = removeSuffix(this.CT, "ido"); return true;
/*      */       }
/*  874 */       if (suffix(this.RV, "ias")) {
/*  875 */         this.CT = removeSuffix(this.CT, "ias"); return true;
/*      */       }
/*  877 */       if (suffix(this.RV, "ais")) {
/*  878 */         this.CT = removeSuffix(this.CT, "ais"); return true;
/*      */       }
/*  880 */       if (suffix(this.RV, "eis")) {
/*  881 */         this.CT = removeSuffix(this.CT, "eis"); return true;
/*      */       }
/*  883 */       if (suffix(this.RV, "ira")) {
/*  884 */         this.CT = removeSuffix(this.CT, "ira"); return true;
/*      */       }
/*  886 */       if (suffix(this.RV, "ear")) {
/*  887 */         this.CT = removeSuffix(this.CT, "ear"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  892 */     if (this.RV.length() >= 2) {
/*  893 */       if (suffix(this.RV, "ia")) {
/*  894 */         this.CT = removeSuffix(this.CT, "ia"); return true;
/*      */       }
/*  896 */       if (suffix(this.RV, "ei")) {
/*  897 */         this.CT = removeSuffix(this.CT, "ei"); return true;
/*      */       }
/*  899 */       if (suffix(this.RV, "am")) {
/*  900 */         this.CT = removeSuffix(this.CT, "am"); return true;
/*      */       }
/*  902 */       if (suffix(this.RV, "em")) {
/*  903 */         this.CT = removeSuffix(this.CT, "em"); return true;
/*      */       }
/*  905 */       if (suffix(this.RV, "ar")) {
/*  906 */         this.CT = removeSuffix(this.CT, "ar"); return true;
/*      */       }
/*  908 */       if (suffix(this.RV, "er")) {
/*  909 */         this.CT = removeSuffix(this.CT, "er"); return true;
/*      */       }
/*  911 */       if (suffix(this.RV, "ir")) {
/*  912 */         this.CT = removeSuffix(this.CT, "ir"); return true;
/*      */       }
/*  914 */       if (suffix(this.RV, "as")) {
/*  915 */         this.CT = removeSuffix(this.CT, "as"); return true;
/*      */       }
/*  917 */       if (suffix(this.RV, "es")) {
/*  918 */         this.CT = removeSuffix(this.CT, "es"); return true;
/*      */       }
/*  920 */       if (suffix(this.RV, "is")) {
/*  921 */         this.CT = removeSuffix(this.CT, "is"); return true;
/*      */       }
/*  923 */       if (suffix(this.RV, "eu")) {
/*  924 */         this.CT = removeSuffix(this.CT, "eu"); return true;
/*      */       }
/*  926 */       if (suffix(this.RV, "iu")) {
/*  927 */         this.CT = removeSuffix(this.CT, "iu"); return true;
/*      */       }
/*  929 */       if (suffix(this.RV, "iu")) {
/*  930 */         this.CT = removeSuffix(this.CT, "iu"); return true;
/*      */       }
/*  932 */       if (suffix(this.RV, "ou")) {
/*  933 */         this.CT = removeSuffix(this.CT, "ou"); return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  938 */     return false;
/*      */   }
/*      */ 
/*      */   private void step3()
/*      */   {
/*  946 */     if (this.RV == null) return;
/*      */ 
/*  948 */     if ((suffix(this.RV, "i")) && (suffixPreceded(this.RV, "i", "c")))
/*  949 */       this.CT = removeSuffix(this.CT, "i");
/*      */   }
/*      */ 
/*      */   private void step4()
/*      */   {
/*  962 */     if (this.RV == null) return;
/*      */ 
/*  964 */     if (suffix(this.RV, "os")) {
/*  965 */       this.CT = removeSuffix(this.CT, "os"); return;
/*      */     }
/*  967 */     if (suffix(this.RV, "a")) {
/*  968 */       this.CT = removeSuffix(this.CT, "a"); return;
/*      */     }
/*  970 */     if (suffix(this.RV, "i")) {
/*  971 */       this.CT = removeSuffix(this.CT, "i"); return;
/*      */     }
/*  973 */     if (suffix(this.RV, "o")) {
/*  974 */       this.CT = removeSuffix(this.CT, "o"); return;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void step5()
/*      */   {
/*  988 */     if (this.RV == null) return;
/*      */ 
/*  990 */     if (suffix(this.RV, "e")) {
/*  991 */       if (suffixPreceded(this.RV, "e", "gu")) {
/*  992 */         this.CT = removeSuffix(this.CT, "e");
/*  993 */         this.CT = removeSuffix(this.CT, "u");
/*  994 */         return;
/*      */       }
/*      */ 
/*  997 */       if (suffixPreceded(this.RV, "e", "ci")) {
/*  998 */         this.CT = removeSuffix(this.CT, "e");
/*  999 */         this.CT = removeSuffix(this.CT, "i");
/* 1000 */         return;
/*      */       }
/*      */ 
/* 1003 */       this.CT = removeSuffix(this.CT, "e"); return;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String log()
/*      */   {
/* 1013 */     return " (TERM = " + this.TERM + ")" + " (CT = " + this.CT + ")" + " (RV = " + this.RV + ")" + " (R1 = " + this.R1 + ")" + " (R2 = " + this.R2 + ")";
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.br.BrazilianStemmer
 * JD-Core Version:    0.6.2
 */