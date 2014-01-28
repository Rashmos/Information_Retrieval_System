/*     */ package org.apache.lucene.analysis.standard;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.Token;
/*     */ import org.apache.lucene.analysis.tokenattributes.TermAttribute;
/*     */ 
/*     */ class StandardTokenizerImpl
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "";
/* 121 */   private static final char[] ZZ_CMAP = zzUnpackCMap("");
/*     */ 
/* 126 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   private static final String ZZ_ACTION_PACKED_0 = "";
/* 157 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "";
/* 189 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   private static final String ZZ_TRANS_PACKED_0 = "";
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/* 260 */   private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/* 269 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/* 301 */   private int zzLexicalState = 0;
/*     */ 
/* 305 */   private char[] zzBuffer = new char[16384];
/*     */   private int zzMarkedPos;
/*     */   private int zzPushbackPos;
/*     */   private int zzCurrentPos;
/*     */   private int zzStartRead;
/*     */   private int zzEndRead;
/*     */   private int yyline;
/*     */   private int yychar;
/*     */   private int yycolumn;
/* 338 */   private boolean zzAtBOL = true;
/*     */   private boolean zzAtEOF;
/*     */   public static final int ALPHANUM = 0;
/*     */   public static final int APOSTROPHE = 1;
/*     */   public static final int ACRONYM = 2;
/*     */   public static final int COMPANY = 3;
/*     */   public static final int EMAIL = 4;
/*     */   public static final int HOST = 5;
/*     */   public static final int NUM = 6;
/*     */   public static final int CJ = 7;
/*     */ 
/*     */   /** @deprecated */
/*     */   public static final int ACRONYM_DEP = 8;
/* 359 */   public static final String[] TOKEN_TYPES = StandardTokenizer.TOKEN_TYPES;
/*     */ 
/*     */   private static int[] zzUnpackAction()
/*     */   {
/* 135 */     int[] result = new int[51];
/* 136 */     int offset = 0;
/* 137 */     offset = zzUnpackAction("", offset, result);
/* 138 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 142 */     int i = 0;
/* 143 */     int j = offset;
/* 144 */     int l = packed.length();
/*     */     int count;
/* 148 */     for (; i < l; 
/* 148 */       count > 0)
/*     */     {
/* 146 */       count = packed.charAt(i++);
/* 147 */       int value = packed.charAt(i++);
/* 148 */       result[(j++)] = value; count--;
/*     */     }
/* 150 */     return j;
/*     */   }
/*     */ 
/*     */   private static int[] zzUnpackRowMap()
/*     */   {
/* 169 */     int[] result = new int[51];
/* 170 */     int offset = 0;
/* 171 */     offset = zzUnpackRowMap("", offset, result);
/* 172 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 176 */     int i = 0;
/* 177 */     int j = offset;
/* 178 */     int l = packed.length();
/* 179 */     while (i < l) {
/* 180 */       int high = packed.charAt(i++) << '\020';
/* 181 */       result[(j++)] = (high | packed.charAt(i++));
/*     */     }
/* 183 */     return j;
/*     */   }
/*     */ 
/*     */   private static int[] zzUnpackTrans()
/*     */   {
/* 234 */     int[] result = new int[658];
/* 235 */     int offset = 0;
/* 236 */     offset = zzUnpackTrans("", offset, result);
/* 237 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 241 */     int i = 0;
/* 242 */     int j = offset;
/* 243 */     int l = packed.length();
/*     */     int count;
/* 248 */     for (; i < l; 
/* 248 */       count > 0)
/*     */     {
/* 245 */       count = packed.charAt(i++);
/* 246 */       int value = packed.charAt(i++);
/* 247 */       value--;
/* 248 */       result[(j++)] = value; count--;
/*     */     }
/* 250 */     return j;
/*     */   }
/*     */ 
/*     */   private static int[] zzUnpackAttribute()
/*     */   {
/* 276 */     int[] result = new int[51];
/* 277 */     int offset = 0;
/* 278 */     offset = zzUnpackAttribute("", offset, result);
/* 279 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 283 */     int i = 0;
/* 284 */     int j = offset;
/* 285 */     int l = packed.length();
/*     */     int count;
/* 289 */     for (; i < l; 
/* 289 */       count > 0)
/*     */     {
/* 287 */       count = packed.charAt(i++);
/* 288 */       int value = packed.charAt(i++);
/* 289 */       result[(j++)] = value; count--;
/*     */     }
/* 291 */     return j;
/*     */   }
/*     */ 
/*     */   public final int yychar()
/*     */   {
/* 363 */     return this.yychar;
/*     */   }
/*     */ 
/*     */   final void getText(Token t)
/*     */   {
/* 370 */     t.setTermBuffer(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/*     */   }
/*     */ 
/*     */   final void getText(TermAttribute t)
/*     */   {
/* 377 */     t.setTermBuffer(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/*     */   }
/*     */ 
/*     */   StandardTokenizerImpl(Reader in)
/*     */   {
/* 388 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */   StandardTokenizerImpl(InputStream in)
/*     */   {
/* 398 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */   private static char[] zzUnpackCMap(String packed)
/*     */   {
/* 408 */     char[] map = new char[65536];
/* 409 */     int i = 0;
/* 410 */     int j = 0;
/*     */     int count;
/* 414 */     for (; i < 1154; 
/* 414 */       count > 0)
/*     */     {
/* 412 */       count = packed.charAt(i++);
/* 413 */       char value = packed.charAt(i++);
/* 414 */       map[(j++)] = value; count--;
/*     */     }
/* 416 */     return map;
/*     */   }
/*     */ 
/*     */   private boolean zzRefill()
/*     */     throws IOException
/*     */   {
/* 430 */     if (this.zzStartRead > 0) {
/* 431 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */ 
/* 436 */       this.zzEndRead -= this.zzStartRead;
/* 437 */       this.zzCurrentPos -= this.zzStartRead;
/* 438 */       this.zzMarkedPos -= this.zzStartRead;
/* 439 */       this.zzPushbackPos -= this.zzStartRead;
/* 440 */       this.zzStartRead = 0;
/*     */     }
/*     */ 
/* 444 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/*     */     {
/* 446 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 447 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 448 */       this.zzBuffer = newBuffer;
/*     */     }
/*     */ 
/* 452 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */ 
/* 455 */     if (numRead < 0) {
/* 456 */       return true;
/*     */     }
/*     */ 
/* 459 */     this.zzEndRead += numRead;
/* 460 */     return false;
/*     */   }
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 469 */     this.zzAtEOF = true;
/* 470 */     this.zzEndRead = this.zzStartRead;
/*     */ 
/* 472 */     if (this.zzReader != null)
/* 473 */       this.zzReader.close();
/*     */   }
/*     */ 
/*     */   public final void yyreset(Reader reader)
/*     */   {
/* 488 */     this.zzReader = reader;
/* 489 */     this.zzAtBOL = true;
/* 490 */     this.zzAtEOF = false;
/* 491 */     this.zzEndRead = (this.zzStartRead = 0);
/* 492 */     this.zzCurrentPos = (this.zzMarkedPos = this.zzPushbackPos = 0);
/* 493 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 494 */     this.zzLexicalState = 0;
/*     */   }
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 502 */     return this.zzLexicalState;
/*     */   }
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 512 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 520 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/*     */   }
/*     */ 
/*     */   public final char yycharat(int pos)
/*     */   {
/* 536 */     return this.zzBuffer[(this.zzStartRead + pos)];
/*     */   }
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 544 */     return this.zzMarkedPos - this.zzStartRead;
/*     */   }
/*     */ 
/*     */   private void zzScanError(int errorCode)
/*     */   {
/*     */     String message;
/*     */     try
/*     */     {
/* 565 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 568 */       message = ZZ_ERROR_MSG[0];
/*     */     }
/*     */ 
/* 571 */     throw new Error(message);
/*     */   }
/*     */ 
/*     */   public void yypushback(int number)
/*     */   {
/* 584 */     if (number > yylength()) {
/* 585 */       zzScanError(2);
/*     */     }
/* 587 */     this.zzMarkedPos -= number;
/*     */   }
/*     */ 
/*     */   public int getNextToken()
/*     */     throws IOException
/*     */   {
/* 605 */     int zzEndReadL = this.zzEndRead;
/* 606 */     char[] zzBufferL = this.zzBuffer;
/* 607 */     char[] zzCMapL = ZZ_CMAP;
/*     */ 
/* 609 */     int[] zzTransL = ZZ_TRANS;
/* 610 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 611 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     while (true)
/*     */     {
/* 614 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */ 
/* 616 */       this.yychar += zzMarkedPosL - this.zzStartRead;
/*     */ 
/* 618 */       int zzAction = -1;
/*     */ 
/* 620 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */ 
/* 622 */       this.zzState = this.zzLexicalState;
/*     */       int zzInput;
/*     */       while (true)
/*     */       {
/*     */         int zzInput;
/* 628 */         if (zzCurrentPosL < zzEndReadL) {
/* 629 */           zzInput = zzBufferL[(zzCurrentPosL++)]; } else {
/* 630 */           if (this.zzAtEOF) {
/* 631 */             int zzInput = -1;
/* 632 */             break;
/*     */           }
/*     */ 
/* 636 */           this.zzCurrentPos = zzCurrentPosL;
/* 637 */           this.zzMarkedPos = zzMarkedPosL;
/* 638 */           boolean eof = zzRefill();
/*     */ 
/* 640 */           zzCurrentPosL = this.zzCurrentPos;
/* 641 */           zzMarkedPosL = this.zzMarkedPos;
/* 642 */           zzBufferL = this.zzBuffer;
/* 643 */           zzEndReadL = this.zzEndRead;
/* 644 */           if (eof) {
/* 645 */             int zzInput = -1;
/* 646 */             break;
/*     */           }
/*     */ 
/* 649 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/*     */         }
/*     */ 
/* 652 */         int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 653 */         if (zzNext == -1) break;
/* 654 */         this.zzState = zzNext;
/*     */ 
/* 656 */         int zzAttributes = zzAttrL[this.zzState];
/* 657 */         if ((zzAttributes & 0x1) == 1) {
/* 658 */           zzAction = this.zzState;
/* 659 */           zzMarkedPosL = zzCurrentPosL;
/* 660 */           if ((zzAttributes & 0x8) == 8)
/*     */           {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 667 */       this.zzMarkedPos = zzMarkedPosL;
/*     */ 
/* 669 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
/*     */       case 4:
/* 671 */         return 5;
/*     */       case 11:
/* 673 */         break;
/*     */       case 9:
/* 675 */         return 2;
/*     */       case 12:
/* 677 */         break;
/*     */       case 8:
/* 679 */         return 8;
/*     */       case 13:
/* 681 */         break;
/*     */       case 1:
/*     */       case 14:
/* 685 */         break;
/*     */       case 5:
/* 687 */         return 6;
/*     */       case 15:
/* 689 */         break;
/*     */       case 3:
/* 691 */         return 7;
/*     */       case 16:
/* 693 */         break;
/*     */       case 2:
/* 695 */         return 0;
/*     */       case 17:
/* 697 */         break;
/*     */       case 7:
/* 699 */         return 3;
/*     */       case 18:
/* 701 */         break;
/*     */       case 6:
/* 703 */         return 1;
/*     */       case 19:
/* 705 */         break;
/*     */       case 10:
/* 707 */         return 4;
/*     */       case 20:
/* 709 */         break;
/*     */       default:
/* 711 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos)) {
/* 712 */           this.zzAtEOF = true;
/* 713 */           return -1;
/*     */         }
/*     */ 
/* 716 */         zzScanError(1);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.standard.StandardTokenizerImpl
 * JD-Core Version:    0.6.2
 */