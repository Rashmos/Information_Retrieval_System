/*     */ package org.apache.lucene.analysis.ru;
/*     */ 
/*     */ @Deprecated
/*     */ class RussianStemmer
/*     */ {
/*     */   private int RV;
/*     */   private int R2;
/*     */   private static final char A = 'а';
/*     */   private static final char V = 'в';
/*     */   private static final char G = 'г';
/*     */   private static final char E = 'е';
/*     */   private static final char I = 'и';
/*     */   private static final char I_ = 'й';
/*     */   private static final char L = 'л';
/*     */   private static final char M = 'м';
/*     */   private static final char N = 'н';
/*     */   private static final char O = 'о';
/*     */   private static final char S = 'с';
/*     */   private static final char T = 'т';
/*     */   private static final char U = 'у';
/*     */   private static final char X = 'х';
/*     */   private static final char SH = 'ш';
/*     */   private static final char SHCH = 'щ';
/*     */   private static final char Y = 'ы';
/*     */   private static final char SOFT = 'ь';
/*     */   private static final char AE = 'э';
/*     */   private static final char IU = 'ю';
/*     */   private static final char IA = 'я';
/*  66 */   private static char[] vowels = { 'а', 'е', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я' };
/*     */ 
/*  68 */   private static char[][] perfectiveGerundEndings1 = { { 'в' }, { 'в', 'ш', 'и' }, { 'в', 'ш', 'и', 'с', 'ь' } };
/*     */ 
/*  74 */   private static char[][] perfectiveGerund1Predessors = { { 'а' }, { 'я' } };
/*     */ 
/*  79 */   private static char[][] perfectiveGerundEndings2 = { { 'и', 'в' }, { 'ы', 'в' }, { 'и', 'в', 'ш', 'и' }, { 'ы', 'в', 'ш', 'и' }, { 'и', 'в', 'ш', 'и', 'с', 'ь' }, { 'ы', 'в', 'ш', 'и', 'с', 'ь' } };
/*     */ 
/*  87 */   private static char[][] adjectiveEndings = { { 'е', 'е' }, { 'и', 'е' }, { 'ы', 'е' }, { 'о', 'е' }, { 'е', 'й' }, { 'и', 'й' }, { 'ы', 'й' }, { 'о', 'й' }, { 'е', 'м' }, { 'и', 'м' }, { 'ы', 'м' }, { 'о', 'м' }, { 'и', 'х' }, { 'ы', 'х' }, { 'у', 'ю' }, { 'ю', 'ю' }, { 'а', 'я' }, { 'я', 'я' }, { 'о', 'ю' }, { 'е', 'ю' }, { 'и', 'м', 'и' }, { 'ы', 'м', 'и' }, { 'е', 'г', 'о' }, { 'о', 'г', 'о' }, { 'е', 'м', 'у' }, { 'о', 'м', 'у' } };
/*     */ 
/* 116 */   private static char[][] participleEndings1 = { { 'щ' }, { 'е', 'м' }, { 'н', 'н' }, { 'в', 'ш' }, { 'ю', 'щ' } };
/*     */ 
/* 124 */   private static char[][] participleEndings2 = { { 'и', 'в', 'ш' }, { 'ы', 'в', 'ш' }, { 'у', 'ю', 'щ' } };
/*     */ 
/* 130 */   private static char[][] participle1Predessors = { { 'а' }, { 'я' } };
/*     */ 
/* 135 */   private static char[][] reflexiveEndings = { { 'с', 'я' }, { 'с', 'ь' } };
/*     */ 
/* 140 */   private static char[][] verbEndings1 = { { 'й' }, { 'л' }, { 'н' }, { 'л', 'о' }, { 'н', 'о' }, { 'е', 'т' }, { 'ю', 'т' }, { 'л', 'а' }, { 'н', 'а' }, { 'л', 'и' }, { 'е', 'м' }, { 'н', 'ы' }, { 'е', 'т', 'е' }, { 'й', 'т', 'е' }, { 'т', 'ь' }, { 'е', 'ш', 'ь' }, { 'н', 'н', 'о' } };
/*     */ 
/* 160 */   private static char[][] verbEndings2 = { { 'ю' }, { 'у', 'ю' }, { 'е', 'н' }, { 'е', 'й' }, { 'я', 'т' }, { 'у', 'й' }, { 'и', 'л' }, { 'ы', 'л' }, { 'и', 'м' }, { 'ы', 'м' }, { 'и', 'т' }, { 'ы', 'т' }, { 'и', 'л', 'а' }, { 'ы', 'л', 'а' }, { 'е', 'н', 'а' }, { 'и', 'т', 'е' }, { 'и', 'л', 'и' }, { 'ы', 'л', 'и' }, { 'и', 'л', 'о' }, { 'ы', 'л', 'о' }, { 'е', 'н', 'о' }, { 'у', 'е', 'т' }, { 'у', 'ю', 'т' }, { 'е', 'н', 'ы' }, { 'и', 'т', 'ь' }, { 'ы', 'т', 'ь' }, { 'и', 'ш', 'ь' }, { 'е', 'й', 'т', 'е' }, { 'у', 'й', 'т', 'е' } };
/*     */ 
/* 192 */   private static char[][] verb1Predessors = { { 'а' }, { 'я' } };
/*     */ 
/* 197 */   private static char[][] nounEndings = { { 'а' }, { 'у' }, { 'й' }, { 'о' }, { 'у' }, { 'е' }, { 'ы' }, { 'и' }, { 'ь' }, { 'я' }, { 'е', 'в' }, { 'о', 'в' }, { 'и', 'е' }, { 'ь', 'е' }, { 'я', 'х' }, { 'и', 'ю' }, { 'е', 'и' }, { 'и', 'и' }, { 'е', 'й' }, { 'о', 'й' }, { 'е', 'м' }, { 'а', 'м' }, { 'о', 'м' }, { 'а', 'х' }, { 'ь', 'ю' }, { 'и', 'я' }, { 'ь', 'я' }, { 'и', 'й' }, { 'я', 'м' }, { 'я', 'м', 'и' }, { 'а', 'м', 'и' }, { 'и', 'е', 'й' }, { 'и', 'я', 'м' }, { 'и', 'е', 'м' }, { 'и', 'я', 'х' }, { 'и', 'я', 'м', 'и' } };
/*     */ 
/* 236 */   private static char[][] superlativeEndings = { { 'е', 'й', 'ш' }, { 'е', 'й', 'ш', 'е' } };
/*     */ 
/* 241 */   private static char[][] derivationalEndings = { { 'о', 'с', 'т' }, { 'о', 'с', 'т', 'ь' } };
/*     */ 
/*     */   private boolean adjectival(StringBuilder stemmingZone)
/*     */   {
/* 263 */     if (!findAndRemoveEnding(stemmingZone, adjectiveEndings)) {
/* 264 */       return false;
/*     */     }
/* 266 */     if (!findAndRemoveEnding(stemmingZone, participleEndings1, participle1Predessors))
/* 267 */       findAndRemoveEnding(stemmingZone, participleEndings2);
/* 268 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean derivational(StringBuilder stemmingZone)
/*     */   {
/* 278 */     int endingLength = findEnding(stemmingZone, derivationalEndings);
/* 279 */     if (endingLength == 0)
/*     */     {
/* 281 */       return false;
/*     */     }
/*     */ 
/* 285 */     if (this.R2 - this.RV <= stemmingZone.length() - endingLength)
/*     */     {
/* 287 */       stemmingZone.setLength(stemmingZone.length() - endingLength);
/* 288 */       return true;
/*     */     }
/*     */ 
/* 292 */     return false;
/*     */   }
/*     */ 
/*     */   private int findEnding(StringBuilder stemmingZone, int startIndex, char[][] theEndingClass)
/*     */   {
/* 303 */     boolean match = false;
/* 304 */     for (int i = theEndingClass.length - 1; i >= 0; i--)
/*     */     {
/* 306 */       char[] theEnding = theEndingClass[i];
/*     */ 
/* 308 */       if (startIndex < theEnding.length - 1)
/*     */       {
/* 310 */         match = false;
/*     */       }
/*     */       else {
/* 313 */         match = true;
/* 314 */         int stemmingIndex = startIndex;
/* 315 */         for (int j = theEnding.length - 1; j >= 0; j--)
/*     */         {
/* 317 */           if (stemmingZone.charAt(stemmingIndex--) != theEnding[j])
/*     */           {
/* 319 */             match = false;
/* 320 */             break;
/*     */           }
/*     */         }
/*     */ 
/* 324 */         if (match)
/*     */         {
/* 326 */           return theEndingClass[i].length;
/*     */         }
/*     */       }
/*     */     }
/* 329 */     return 0;
/*     */   }
/*     */ 
/*     */   private int findEnding(StringBuilder stemmingZone, char[][] theEndingClass)
/*     */   {
/* 334 */     return findEnding(stemmingZone, stemmingZone.length() - 1, theEndingClass);
/*     */   }
/*     */ 
/*     */   private boolean findAndRemoveEnding(StringBuilder stemmingZone, char[][] theEndingClass)
/*     */   {
/* 343 */     int endingLength = findEnding(stemmingZone, theEndingClass);
/* 344 */     if (endingLength == 0)
/*     */     {
/* 346 */       return false;
/*     */     }
/* 348 */     stemmingZone.setLength(stemmingZone.length() - endingLength);
/*     */ 
/* 350 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean findAndRemoveEnding(StringBuilder stemmingZone, char[][] theEndingClass, char[][] thePredessors)
/*     */   {
/* 362 */     int endingLength = findEnding(stemmingZone, theEndingClass);
/* 363 */     if (endingLength == 0)
/*     */     {
/* 365 */       return false;
/*     */     }
/*     */ 
/* 368 */     int predessorLength = findEnding(stemmingZone, stemmingZone.length() - endingLength - 1, thePredessors);
/*     */ 
/* 372 */     if (predessorLength == 0) {
/* 373 */       return false;
/*     */     }
/* 375 */     stemmingZone.setLength(stemmingZone.length() - endingLength);
/*     */ 
/* 377 */     return true;
/*     */   }
/*     */ 
/*     */   private void markPositions(String word)
/*     */   {
/* 389 */     this.RV = 0;
/*     */ 
/* 391 */     this.R2 = 0;
/* 392 */     int i = 0;
/*     */ 
/* 394 */     while ((word.length() > i) && (!isVowel(word.charAt(i))))
/*     */     {
/* 396 */       i++;
/*     */     }
/* 398 */     if (word.length() - 1 < ++i)
/* 399 */       return;
/* 400 */     this.RV = i;
/*     */ 
/* 402 */     while ((word.length() > i) && (isVowel(word.charAt(i))))
/*     */     {
/* 404 */       i++;
/*     */     }
/* 406 */     if (word.length() - 1 < ++i) {
/* 407 */       return;
/*     */     }
/*     */ 
/* 410 */     while ((word.length() > i) && (!isVowel(word.charAt(i))))
/*     */     {
/* 412 */       i++;
/*     */     }
/* 414 */     if (word.length() - 1 < ++i)
/* 415 */       return;
/* 416 */     while ((word.length() > i) && (isVowel(word.charAt(i))))
/*     */     {
/* 418 */       i++;
/*     */     }
/* 420 */     if (word.length() - 1 < ++i)
/* 421 */       return;
/* 422 */     this.R2 = i;
/*     */   }
/*     */ 
/*     */   private boolean isVowel(char letter)
/*     */   {
/* 433 */     for (int i = 0; i < vowels.length; i++)
/*     */     {
/* 435 */       if (letter == vowels[i])
/* 436 */         return true;
/*     */     }
/* 438 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean noun(StringBuilder stemmingZone)
/*     */   {
/* 448 */     return findAndRemoveEnding(stemmingZone, nounEndings);
/*     */   }
/*     */ 
/*     */   private boolean perfectiveGerund(StringBuilder stemmingZone)
/*     */   {
/* 458 */     return (findAndRemoveEnding(stemmingZone, perfectiveGerundEndings1, perfectiveGerund1Predessors)) || (findAndRemoveEnding(stemmingZone, perfectiveGerundEndings2));
/*     */   }
/*     */ 
/*     */   private boolean reflexive(StringBuilder stemmingZone)
/*     */   {
/* 472 */     return findAndRemoveEnding(stemmingZone, reflexiveEndings);
/*     */   }
/*     */ 
/*     */   private boolean removeI(StringBuilder stemmingZone)
/*     */   {
/* 482 */     if ((stemmingZone.length() > 0) && (stemmingZone.charAt(stemmingZone.length() - 1) == 'и'))
/*     */     {
/* 485 */       stemmingZone.setLength(stemmingZone.length() - 1);
/* 486 */       return true;
/*     */     }
/*     */ 
/* 490 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean removeSoft(StringBuilder stemmingZone)
/*     */   {
/* 501 */     if ((stemmingZone.length() > 0) && (stemmingZone.charAt(stemmingZone.length() - 1) == 'ь'))
/*     */     {
/* 504 */       stemmingZone.setLength(stemmingZone.length() - 1);
/* 505 */       return true;
/*     */     }
/*     */ 
/* 509 */     return false;
/*     */   }
/*     */ 
/*     */   public String stem(String input)
/*     */   {
/* 521 */     markPositions(input);
/* 522 */     if (this.RV == 0)
/* 523 */       return input;
/* 524 */     StringBuilder stemmingZone = new StringBuilder(input.substring(this.RV));
/*     */ 
/* 528 */     if (!perfectiveGerund(stemmingZone))
/*     */     {
/* 530 */       reflexive(stemmingZone);
/* 531 */       if ((!adjectival(stemmingZone)) && 
/* 532 */         (!verb(stemmingZone))) {
/* 533 */         noun(stemmingZone);
/*     */       }
/*     */     }
/* 536 */     removeI(stemmingZone);
/*     */ 
/* 538 */     derivational(stemmingZone);
/*     */ 
/* 540 */     superlative(stemmingZone);
/* 541 */     undoubleN(stemmingZone);
/* 542 */     removeSoft(stemmingZone);
/*     */ 
/* 544 */     return input.substring(0, this.RV) + stemmingZone.toString();
/*     */   }
/*     */ 
/*     */   private boolean superlative(StringBuilder stemmingZone)
/*     */   {
/* 554 */     return findAndRemoveEnding(stemmingZone, superlativeEndings);
/*     */   }
/*     */ 
/*     */   private boolean undoubleN(StringBuilder stemmingZone)
/*     */   {
/* 564 */     char[][] doubleN = { { 'н', 'н' } };
/*     */ 
/* 567 */     if (findEnding(stemmingZone, doubleN) != 0)
/*     */     {
/* 569 */       stemmingZone.setLength(stemmingZone.length() - 1);
/* 570 */       return true;
/*     */     }
/*     */ 
/* 574 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean verb(StringBuilder stemmingZone)
/*     */   {
/* 585 */     return (findAndRemoveEnding(stemmingZone, verbEndings1, verb1Predessors)) || (findAndRemoveEnding(stemmingZone, verbEndings2));
/*     */   }
/*     */ 
/*     */   public static String stemWord(String theWord)
/*     */   {
/* 597 */     RussianStemmer stemmer = new RussianStemmer();
/* 598 */     return stemmer.stem(theWord);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.ru.RussianStemmer
 * JD-Core Version:    0.6.2
 */