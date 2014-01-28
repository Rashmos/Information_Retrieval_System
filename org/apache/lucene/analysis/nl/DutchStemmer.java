/*     */ package org.apache.lucene.analysis.nl;
/*     */ 
/*     */ import java.util.Map;
/*     */ 
/*     */ @Deprecated
/*     */ public class DutchStemmer
/*     */ {
/*  37 */   private StringBuilder sb = new StringBuilder();
/*     */   private boolean _removedE;
/*     */   private Map _stemDict;
/*     */   private int _R1;
/*     */   private int _R2;
/*     */ 
/*     */   public String stem(String term)
/*     */   {
/*  52 */     term = term.toLowerCase();
/*  53 */     if (!isStemmable(term))
/*  54 */       return term;
/*  55 */     if ((this._stemDict != null) && (this._stemDict.containsKey(term))) {
/*  56 */       if ((this._stemDict.get(term) instanceof String)) {
/*  57 */         return (String)this._stemDict.get(term);
/*     */       }
/*  59 */       return null;
/*     */     }
/*     */ 
/*  62 */     this.sb.delete(0, this.sb.length());
/*  63 */     this.sb.insert(0, term);
/*     */ 
/*  65 */     substitute(this.sb);
/*  66 */     storeYandI(this.sb);
/*  67 */     this._R1 = getRIndex(this.sb, 0);
/*  68 */     this._R1 = Math.max(3, this._R1);
/*  69 */     step1(this.sb);
/*  70 */     step2(this.sb);
/*  71 */     this._R2 = getRIndex(this.sb, this._R1);
/*  72 */     step3a(this.sb);
/*  73 */     step3b(this.sb);
/*  74 */     step4(this.sb);
/*  75 */     reStoreYandI(this.sb);
/*  76 */     return this.sb.toString();
/*     */   }
/*     */ 
/*     */   private boolean enEnding(StringBuilder sb) {
/*  80 */     String[] enend = { "ene", "en" };
/*  81 */     for (int i = 0; i < enend.length; i++) {
/*  82 */       String end = enend[i];
/*  83 */       String s = sb.toString();
/*  84 */       int index = s.length() - end.length();
/*  85 */       if ((s.endsWith(end)) && (index >= this._R1) && (isValidEnEnding(sb, index - 1)))
/*     */       {
/*  89 */         sb.delete(index, index + end.length());
/*  90 */         unDouble(sb, index);
/*  91 */         return true;
/*     */       }
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   private void step1(StringBuilder sb)
/*     */   {
/*  99 */     if (this._R1 >= sb.length()) {
/* 100 */       return;
/*     */     }
/* 102 */     String s = sb.toString();
/* 103 */     int lengthR1 = sb.length() - this._R1;
/*     */ 
/* 106 */     if (s.endsWith("heden")) {
/* 107 */       sb.replace(this._R1, lengthR1 + this._R1, sb.substring(this._R1, lengthR1 + this._R1).replaceAll("heden", "heid"));
/* 108 */       return;
/*     */     }
/*     */ 
/* 111 */     if (enEnding(sb))
/*     */       return;
/*     */     int index;
/* 114 */     if ((s.endsWith("se")) && ((index = s.length() - 2) >= this._R1) && (isValidSEnding(sb, index - 1)))
/*     */     {
/* 118 */       sb.delete(index, index + 2);
/*     */       return;
/*     */     }
/*     */     int index;
/* 121 */     if ((s.endsWith("s")) && ((index = s.length() - 1) >= this._R1) && (isValidSEnding(sb, index - 1)))
/*     */     {
/* 124 */       sb.delete(index, index + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void step2(StringBuilder sb)
/*     */   {
/* 135 */     this._removedE = false;
/* 136 */     if (this._R1 >= sb.length())
/* 137 */       return;
/* 138 */     String s = sb.toString();
/* 139 */     int index = s.length() - 1;
/* 140 */     if ((index >= this._R1) && (s.endsWith("e")) && (!isVowel(sb.charAt(index - 1))))
/*     */     {
/* 143 */       sb.delete(index, index + 1);
/* 144 */       unDouble(sb);
/* 145 */       this._removedE = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void step3a(StringBuilder sb)
/*     */   {
/* 155 */     if (this._R2 >= sb.length())
/* 156 */       return;
/* 157 */     String s = sb.toString();
/* 158 */     int index = s.length() - 4;
/* 159 */     if ((s.endsWith("heid")) && (index >= this._R2) && (sb.charAt(index - 1) != 'c')) {
/* 160 */       sb.delete(index, index + 4);
/* 161 */       enEnding(sb);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void step3b(StringBuilder sb)
/*     */   {
/* 180 */     if (this._R2 >= sb.length())
/* 181 */       return;
/* 182 */     String s = sb.toString();
/* 183 */     int index = 0;
/*     */ 
/* 185 */     if (((s.endsWith("end")) || (s.endsWith("ing"))) && ((index = s.length() - 3) >= this._R2))
/*     */     {
/* 187 */       sb.delete(index, index + 3);
/* 188 */       if ((sb.charAt(index - 2) == 'i') && (sb.charAt(index - 1) == 'g'))
/*     */       {
/* 190 */         if (((sb.charAt(index - 3) != 'e' ? 1 : 0) & (index - 2 >= this._R2 ? 1 : 0)) != 0) {
/* 191 */           index -= 2;
/* 192 */           sb.delete(index, index + 2);
/*     */         }
/*     */       }
/* 195 */       else unDouble(sb, index);
/*     */ 
/* 197 */       return;
/*     */     }
/* 199 */     if ((s.endsWith("ig")) && ((index = s.length() - 2) >= this._R2))
/*     */     {
/* 202 */       if (sb.charAt(index - 1) != 'e')
/* 203 */         sb.delete(index, index + 2);
/* 204 */       return;
/*     */     }
/* 206 */     if ((s.endsWith("lijk")) && ((index = s.length() - 4) >= this._R2))
/*     */     {
/* 209 */       sb.delete(index, index + 4);
/* 210 */       step2(sb);
/* 211 */       return;
/*     */     }
/* 213 */     if ((s.endsWith("baar")) && ((index = s.length() - 4) >= this._R2))
/*     */     {
/* 216 */       sb.delete(index, index + 4);
/* 217 */       return;
/*     */     }
/* 219 */     if ((s.endsWith("bar")) && ((index = s.length() - 3) >= this._R2))
/*     */     {
/* 222 */       if (this._removedE)
/* 223 */         sb.delete(index, index + 3);
/* 224 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void step4(StringBuilder sb)
/*     */   {
/* 235 */     if (sb.length() < 4)
/* 236 */       return;
/* 237 */     String end = sb.substring(sb.length() - 4, sb.length());
/* 238 */     char c = end.charAt(0);
/* 239 */     char v1 = end.charAt(1);
/* 240 */     char v2 = end.charAt(2);
/* 241 */     char d = end.charAt(3);
/* 242 */     if ((v1 == v2) && (d != 'I') && (v1 != 'i') && (isVowel(v1)) && (!isVowel(d)) && (!isVowel(c)))
/*     */     {
/* 248 */       sb.delete(sb.length() - 2, sb.length() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isStemmable(String term)
/*     */   {
/* 258 */     for (int c = 0; c < term.length(); c++) {
/* 259 */       if (!Character.isLetter(term.charAt(c))) return false;
/*     */     }
/* 261 */     return true;
/*     */   }
/*     */ 
/*     */   private void substitute(StringBuilder buffer)
/*     */   {
/* 268 */     for (int i = 0; i < buffer.length(); i++)
/* 269 */       switch (buffer.charAt(i))
/*     */       {
/*     */       case 'á':
/*     */       case 'ä':
/* 273 */         buffer.setCharAt(i, 'a');
/* 274 */         break;
/*     */       case 'é':
/*     */       case 'ë':
/* 279 */         buffer.setCharAt(i, 'e');
/* 280 */         break;
/*     */       case 'ú':
/*     */       case 'ü':
/* 285 */         buffer.setCharAt(i, 'u');
/* 286 */         break;
/*     */       case 'i':
/*     */       case 'ï':
/* 291 */         buffer.setCharAt(i, 'i');
/* 292 */         break;
/*     */       case 'ó':
/*     */       case 'ö':
/* 297 */         buffer.setCharAt(i, 'o');
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean isValidSEnding(StringBuilder sb, int index)
/*     */   {
/* 309 */     char c = sb.charAt(index);
/* 310 */     if ((isVowel(c)) || (c == 'j'))
/* 311 */       return false;
/* 312 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean isValidEnEnding(StringBuilder sb, int index)
/*     */   {
/* 320 */     char c = sb.charAt(index);
/* 321 */     if (isVowel(c))
/* 322 */       return false;
/* 323 */     if (c < '\003') {
/* 324 */       return false;
/*     */     }
/* 326 */     if ((c == 'm') && (sb.charAt(index - 2) == 'g') && (sb.charAt(index - 1) == 'e'))
/* 327 */       return false;
/* 328 */     return true;
/*     */   }
/*     */ 
/*     */   private void unDouble(StringBuilder sb) {
/* 332 */     unDouble(sb, sb.length());
/*     */   }
/*     */ 
/*     */   private void unDouble(StringBuilder sb, int endIndex) {
/* 336 */     String s = sb.substring(0, endIndex);
/* 337 */     if ((s.endsWith("kk")) || (s.endsWith("tt")) || (s.endsWith("dd")) || (s.endsWith("nn")) || (s.endsWith("mm")) || (s.endsWith("ff")))
/* 338 */       sb.delete(endIndex - 1, endIndex);
/*     */   }
/*     */ 
/*     */   private int getRIndex(StringBuilder sb, int start)
/*     */   {
/* 343 */     if (start == 0)
/* 344 */       start = 1;
/* 345 */     for (int i = start; 
/* 346 */       i < sb.length(); i++)
/*     */     {
/* 348 */       if ((!isVowel(sb.charAt(i))) && (isVowel(sb.charAt(i - 1)))) {
/* 349 */         return i + 1;
/*     */       }
/*     */     }
/* 352 */     return i + 1;
/*     */   }
/*     */ 
/*     */   private void storeYandI(StringBuilder sb) {
/* 356 */     if (sb.charAt(0) == 'y') {
/* 357 */       sb.setCharAt(0, 'Y');
/*     */     }
/* 359 */     int last = sb.length() - 1;
/*     */ 
/* 361 */     for (int i = 1; i < last; i++) {
/* 362 */       switch (sb.charAt(i))
/*     */       {
/*     */       case 'i':
/* 365 */         if ((isVowel(sb.charAt(i - 1))) && (isVowel(sb.charAt(i + 1))))
/*     */         {
/* 368 */           sb.setCharAt(i, 'I'); } break;
/*     */       case 'y':
/* 373 */         if (isVowel(sb.charAt(i - 1))) {
/* 374 */           sb.setCharAt(i, 'Y');
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 379 */     if ((last > 0) && (sb.charAt(last) == 'y') && (isVowel(sb.charAt(last - 1))))
/* 380 */       sb.setCharAt(last, 'Y');
/*     */   }
/*     */ 
/*     */   private void reStoreYandI(StringBuilder sb) {
/* 384 */     String tmp = sb.toString();
/* 385 */     sb.delete(0, sb.length());
/* 386 */     sb.insert(0, tmp.replaceAll("I", "i").replaceAll("Y", "y"));
/*     */   }
/*     */ 
/*     */   private boolean isVowel(char c) {
/* 390 */     switch (c)
/*     */     {
/*     */     case 'a':
/*     */     case 'e':
/*     */     case 'i':
/*     */     case 'o':
/*     */     case 'u':
/*     */     case 'y':
/*     */     case 'è':
/* 399 */       return true;
/*     */     }
/*     */ 
/* 402 */     return false;
/*     */   }
/*     */ 
/*     */   void setStemDictionary(Map dict) {
/* 406 */     this._stemDict = dict;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.nl.DutchStemmer
 * JD-Core Version:    0.6.2
 */