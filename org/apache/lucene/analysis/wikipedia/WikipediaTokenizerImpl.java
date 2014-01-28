/*     */ package org.apache.lucene.analysis.wikipedia;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
/*     */ 
/*     */ class WikipediaTokenizerImpl
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int THREE_SINGLE_QUOTES_STATE = 10;
/*     */   public static final int EXTERNAL_LINK_STATE = 6;
/*     */   public static final int DOUBLE_EQUALS_STATE = 14;
/*     */   public static final int INTERNAL_LINK_STATE = 4;
/*     */   public static final int DOUBLE_BRACE_STATE = 16;
/*     */   public static final int CATEGORY_STATE = 2;
/*     */   public static final int YYINITIAL = 0;
/*     */   public static final int STRING = 18;
/*     */   public static final int FIVE_SINGLE_QUOTES_STATE = 12;
/*     */   public static final int TWO_SINGLE_QUOTES_STATE = 8;
/*  57 */   private static final int[] ZZ_LEXSTATE = { 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9 };
/*     */   private static final String ZZ_CMAP_PACKED = "";
/*  82 */   private static final char[] ZZ_CMAP = zzUnpackCMap("");
/*     */ 
/*  87 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   private static final String ZZ_ACTION_PACKED_0 = "";
/* 126 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "";
/* 174 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   private static final String ZZ_TRANS_PACKED_0 = "";
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/* 349 */   private static final String[] ZZ_ERROR_MSG = { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/* 358 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/* 396 */   private int zzLexicalState = 0;
/*     */ 
/* 400 */   private char[] zzBuffer = new char[16384];
/*     */   private int zzMarkedPos;
/*     */   private int zzCurrentPos;
/*     */   private int zzStartRead;
/*     */   private int zzEndRead;
/*     */   private int yyline;
/*     */   private int yychar;
/*     */   private int yycolumn;
/* 430 */   private boolean zzAtBOL = true;
/*     */   private boolean zzAtEOF;
/*     */   private boolean zzEOFDone;
/*     */   public static final int ALPHANUM = 0;
/*     */   public static final int APOSTROPHE = 1;
/*     */   public static final int ACRONYM = 2;
/*     */   public static final int COMPANY = 3;
/*     */   public static final int EMAIL = 4;
/*     */   public static final int HOST = 5;
/*     */   public static final int NUM = 6;
/*     */   public static final int CJ = 7;
/*     */   public static final int INTERNAL_LINK = 8;
/*     */   public static final int EXTERNAL_LINK = 9;
/*     */   public static final int CITATION = 10;
/*     */   public static final int CATEGORY = 11;
/*     */   public static final int BOLD = 12;
/*     */   public static final int ITALICS = 13;
/*     */   public static final int BOLD_ITALICS = 14;
/*     */   public static final int HEADING = 15;
/*     */   public static final int SUB_HEADING = 16;
/*     */   public static final int EXTERNAL_LINK_URL = 17;
/*     */   private int currentTokType;
/* 461 */   private int numBalanced = 0;
/* 462 */   private int positionInc = 1;
/* 463 */   private int numLinkToks = 0;
/*     */ 
/* 467 */   private int numWikiTokensSeen = 0;
/*     */ 
/* 469 */   public static final String[] TOKEN_TYPES = WikipediaTokenizer.TOKEN_TYPES;
/*     */ 
/*     */   private static int[] zzUnpackAction()
/*     */   {
/* 104 */     int[] result = new int['¸'];
/* 105 */     int offset = 0;
/* 106 */     offset = zzUnpackAction("", offset, result);
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 111 */     int i = 0;
/* 112 */     int j = offset;
/* 113 */     int l = packed.length();
/*     */     int count;
/* 117 */     for (; i < l; 
/* 117 */       count > 0)
/*     */     {
/* 115 */       count = packed.charAt(i++);
/* 116 */       int value = packed.charAt(i++);
/* 117 */       result[(j++)] = value; count--;
/*     */     }
/* 119 */     return j;
/*     */   }
/*     */ 
/*     */   private static int[] zzUnpackRowMap()
/*     */   {
/* 154 */     int[] result = new int['¸'];
/* 155 */     int offset = 0;
/* 156 */     offset = zzUnpackRowMap("", offset, result);
/* 157 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 161 */     int i = 0;
/* 162 */     int j = offset;
/* 163 */     int l = packed.length();
/* 164 */     while (i < l) {
/* 165 */       int high = packed.charAt(i++) << '\020';
/* 166 */       result[(j++)] = (high | packed.charAt(i++));
/*     */     }
/* 168 */     return j;
/*     */   }
/*     */ 
/*     */   private static int[] zzUnpackTrans()
/*     */   {
/* 323 */     int[] result = new int[7040];
/* 324 */     int offset = 0;
/* 325 */     offset = zzUnpackTrans("", offset, result);
/* 326 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 330 */     int i = 0;
/* 331 */     int j = offset;
/* 332 */     int l = packed.length();
/*     */     int count;
/* 337 */     for (; i < l; 
/* 337 */       count > 0)
/*     */     {
/* 334 */       count = packed.charAt(i++);
/* 335 */       int value = packed.charAt(i++);
/* 336 */       value--;
/* 337 */       result[(j++)] = value; count--;
/*     */     }
/* 339 */     return j;
/*     */   }
/*     */ 
/*     */   private static int[] zzUnpackAttribute()
/*     */   {
/* 371 */     int[] result = new int['¸'];
/* 372 */     int offset = 0;
/* 373 */     offset = zzUnpackAttribute("", offset, result);
/* 374 */     return result;
/*     */   }
/*     */ 
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 378 */     int i = 0;
/* 379 */     int j = offset;
/* 380 */     int l = packed.length();
/*     */     int count;
/* 384 */     for (; i < l; 
/* 384 */       count > 0)
/*     */     {
/* 382 */       count = packed.charAt(i++);
/* 383 */       int value = packed.charAt(i++);
/* 384 */       result[(j++)] = value; count--;
/*     */     }
/* 386 */     return j;
/*     */   }
/*     */ 
/*     */   public final int getNumWikiTokensSeen()
/*     */   {
/* 476 */     return this.numWikiTokensSeen;
/*     */   }
/*     */ 
/*     */   public final int yychar()
/*     */   {
/* 481 */     return this.yychar;
/*     */   }
/*     */ 
/*     */   public final int getPositionIncrement() {
/* 485 */     return this.positionInc;
/*     */   }
/*     */ 
/*     */   final void getText(CharTermAttribute t)
/*     */   {
/* 492 */     t.copyBuffer(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/*     */   }
/*     */ 
/*     */   final int setText(StringBuilder buffer) {
/* 496 */     int length = this.zzMarkedPos - this.zzStartRead;
/* 497 */     buffer.append(this.zzBuffer, this.zzStartRead, length);
/* 498 */     return length;
/*     */   }
/*     */ 
/*     */   WikipediaTokenizerImpl(Reader in)
/*     */   {
/* 511 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */   WikipediaTokenizerImpl(InputStream in)
/*     */   {
/* 521 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */   private static char[] zzUnpackCMap(String packed)
/*     */   {
/* 531 */     char[] map = new char[65536];
/* 532 */     int i = 0;
/* 533 */     int j = 0;
/*     */     int count;
/* 537 */     for (; i < 230; 
/* 537 */       count > 0)
/*     */     {
/* 535 */       count = packed.charAt(i++);
/* 536 */       char value = packed.charAt(i++);
/* 537 */       map[(j++)] = value; count--;
/*     */     }
/* 539 */     return map;
/*     */   }
/*     */ 
/*     */   private boolean zzRefill()
/*     */     throws IOException
/*     */   {
/* 553 */     if (this.zzStartRead > 0) {
/* 554 */       System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
/*     */ 
/* 559 */       this.zzEndRead -= this.zzStartRead;
/* 560 */       this.zzCurrentPos -= this.zzStartRead;
/* 561 */       this.zzMarkedPos -= this.zzStartRead;
/* 562 */       this.zzStartRead = 0;
/*     */     }
/*     */ 
/* 566 */     if (this.zzCurrentPos >= this.zzBuffer.length)
/*     */     {
/* 568 */       char[] newBuffer = new char[this.zzCurrentPos * 2];
/* 569 */       System.arraycopy(this.zzBuffer, 0, newBuffer, 0, this.zzBuffer.length);
/* 570 */       this.zzBuffer = newBuffer;
/*     */     }
/*     */ 
/* 574 */     int numRead = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
/*     */ 
/* 577 */     if (numRead > 0) {
/* 578 */       this.zzEndRead += numRead;
/* 579 */       return false;
/*     */     }
/*     */ 
/* 582 */     if (numRead == 0) {
/* 583 */       int c = this.zzReader.read();
/* 584 */       if (c == -1) {
/* 585 */         return true;
/*     */       }
/* 587 */       this.zzBuffer[(this.zzEndRead++)] = ((char)c);
/* 588 */       return false;
/*     */     }
/*     */ 
/* 593 */     return true;
/*     */   }
/*     */ 
/*     */   public final void yyclose()
/*     */     throws IOException
/*     */   {
/* 601 */     this.zzAtEOF = true;
/* 602 */     this.zzEndRead = this.zzStartRead;
/*     */ 
/* 604 */     if (this.zzReader != null)
/* 605 */       this.zzReader.close();
/*     */   }
/*     */ 
/*     */   public final void yyreset(Reader reader)
/*     */   {
/* 622 */     this.zzReader = reader;
/* 623 */     this.zzAtBOL = true;
/* 624 */     this.zzAtEOF = false;
/* 625 */     this.zzEOFDone = false;
/* 626 */     this.zzEndRead = (this.zzStartRead = 0);
/* 627 */     this.zzCurrentPos = (this.zzMarkedPos = 0);
/* 628 */     this.yyline = (this.yychar = this.yycolumn = 0);
/* 629 */     this.zzLexicalState = 0;
/* 630 */     if (this.zzBuffer.length > 16384)
/* 631 */       this.zzBuffer = new char[16384];
/*     */   }
/*     */ 
/*     */   public final int yystate()
/*     */   {
/* 639 */     return this.zzLexicalState;
/*     */   }
/*     */ 
/*     */   public final void yybegin(int newState)
/*     */   {
/* 649 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */   public final String yytext()
/*     */   {
/* 657 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
/*     */   }
/*     */ 
/*     */   public final char yycharat(int pos)
/*     */   {
/* 673 */     return this.zzBuffer[(this.zzStartRead + pos)];
/*     */   }
/*     */ 
/*     */   public final int yylength()
/*     */   {
/* 681 */     return this.zzMarkedPos - this.zzStartRead;
/*     */   }
/*     */ 
/*     */   private void zzScanError(int errorCode)
/*     */   {
/*     */     String message;
/*     */     try
/*     */     {
/* 702 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 705 */       message = ZZ_ERROR_MSG[0];
/*     */     }
/*     */ 
/* 708 */     throw new Error(message);
/*     */   }
/*     */ 
/*     */   public void yypushback(int number)
/*     */   {
/* 721 */     if (number > yylength()) {
/* 722 */       zzScanError(2);
/*     */     }
/* 724 */     this.zzMarkedPos -= number;
/*     */   }
/*     */ 
/*     */   public int getNextToken()
/*     */     throws IOException
/*     */   {
/* 742 */     int zzEndReadL = this.zzEndRead;
/* 743 */     char[] zzBufferL = this.zzBuffer;
/* 744 */     char[] zzCMapL = ZZ_CMAP;
/*     */ 
/* 746 */     int[] zzTransL = ZZ_TRANS;
/* 747 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 748 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     while (true)
/*     */     {
/* 751 */       int zzMarkedPosL = this.zzMarkedPos;
/*     */ 
/* 753 */       this.yychar += zzMarkedPosL - this.zzStartRead;
/*     */ 
/* 755 */       int zzAction = -1;
/*     */ 
/* 757 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */ 
/* 759 */       this.zzState = ZZ_LEXSTATE[this.zzLexicalState];
/*     */ 
/* 762 */       int zzAttributes = zzAttrL[this.zzState];
/* 763 */       if ((zzAttributes & 0x1) == 1)
/* 764 */         zzAction = this.zzState;
/*     */       int zzInput;
/*     */       while (true)
/*     */       {
/*     */         int zzInput;
/* 771 */         if (zzCurrentPosL < zzEndReadL) {
/* 772 */           zzInput = zzBufferL[(zzCurrentPosL++)]; } else {
/* 773 */           if (this.zzAtEOF) {
/* 774 */             int zzInput = -1;
/* 775 */             break;
/*     */           }
/*     */ 
/* 779 */           this.zzCurrentPos = zzCurrentPosL;
/* 780 */           this.zzMarkedPos = zzMarkedPosL;
/* 781 */           boolean eof = zzRefill();
/*     */ 
/* 783 */           zzCurrentPosL = this.zzCurrentPos;
/* 784 */           zzMarkedPosL = this.zzMarkedPos;
/* 785 */           zzBufferL = this.zzBuffer;
/* 786 */           zzEndReadL = this.zzEndRead;
/* 787 */           if (eof) {
/* 788 */             int zzInput = -1;
/* 789 */             break;
/*     */           }
/*     */ 
/* 792 */           zzInput = zzBufferL[(zzCurrentPosL++)];
/*     */         }
/*     */ 
/* 795 */         int zzNext = zzTransL[(zzRowMapL[this.zzState] + zzCMapL[zzInput])];
/* 796 */         if (zzNext == -1) break;
/* 797 */         this.zzState = zzNext;
/*     */ 
/* 799 */         zzAttributes = zzAttrL[this.zzState];
/* 800 */         if ((zzAttributes & 0x1) == 1) {
/* 801 */           zzAction = this.zzState;
/* 802 */           zzMarkedPosL = zzCurrentPosL;
/* 803 */           if ((zzAttributes & 0x8) == 8)
/*     */           {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 810 */       this.zzMarkedPos = zzMarkedPosL;
/*     */ 
/* 812 */       switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
/*     */       case 44:
/* 814 */         this.numWikiTokensSeen = 0; this.positionInc = 1; this.currentTokType = 11; yybegin(2); break;
/*     */       case 47:
/* 816 */         break;
/*     */       case 37:
/* 818 */         this.currentTokType = 14; yybegin(12); break;
/*     */       case 48:
/* 820 */         break;
/*     */       case 16:
/* 822 */         this.currentTokType = 15; yybegin(14); this.numWikiTokensSeen += 1; return this.currentTokType;
/*     */       case 49:
/* 824 */         break;
/*     */       case 20:
/* 826 */         this.numBalanced = 0; this.numWikiTokensSeen = 0; this.currentTokType = 9; yybegin(6); break;
/*     */       case 50:
/* 828 */         break;
/*     */       case 40:
/* 830 */         this.positionInc = 1; return 2;
/*     */       case 51:
/* 832 */         break;
/*     */       case 5:
/* 834 */         this.positionInc = 1; break;
/*     */       case 52:
/* 836 */         break;
/*     */       case 36:
/* 838 */         this.positionInc = 1; return 3;
/*     */       case 53:
/* 840 */         break;
/*     */       case 10:
/* 842 */         this.numLinkToks = 0; this.positionInc = 0; yybegin(0); break;
/*     */       case 54:
/* 844 */         break;
/*     */       case 15:
/* 846 */         this.currentTokType = 16; this.numWikiTokensSeen = 0; yybegin(18); break;
/*     */       case 55:
/* 848 */         break;
/*     */       case 22:
/* 850 */         this.numWikiTokensSeen = 0; this.positionInc = 1; if (this.numBalanced == 0) { this.numBalanced += 1; yybegin(8); } else { this.numBalanced = 0; } break;
/*     */       case 56:
/* 852 */         break;
/*     */       case 35:
/* 854 */         this.positionInc = 1; return 6;
/*     */       case 57:
/* 856 */         break;
/*     */       case 33:
/* 858 */         this.positionInc = 1; return 1;
/*     */       case 58:
/* 860 */         break;
/*     */       case 21:
/* 862 */         yybegin(18); return this.currentTokType;
/*     */       case 59:
/* 864 */         break;
/*     */       case 18:
/* 866 */         break;
/*     */       case 60:
/* 868 */         break;
/*     */       case 2:
/* 870 */         this.positionInc = 1; return 0;
/*     */       case 61:
/* 872 */         break;
/*     */       case 1:
/* 874 */         this.numWikiTokensSeen = 0; this.positionInc = 1; break;
/*     */       case 62:
/* 876 */         break;
/*     */       case 17:
/* 878 */         yybegin(16); this.numWikiTokensSeen = 0; return this.currentTokType;
/*     */       case 63:
/* 880 */         break;
/*     */       case 39:
/* 882 */         this.numBalanced = 0; this.currentTokType = 0; yybegin(0); break;
/*     */       case 64:
/* 884 */         break;
/*     */       case 29:
/* 886 */         this.currentTokType = 8; this.numWikiTokensSeen = 0; yybegin(4); break;
/*     */       case 65:
/* 888 */         break;
/*     */       case 46:
/* 890 */         this.numBalanced = 0; this.numWikiTokensSeen = 0; this.currentTokType = 11; yybegin(2); break;
/*     */       case 66:
/* 892 */         break;
/*     */       case 27:
/* 894 */         this.numLinkToks = 0; yybegin(0); break;
/*     */       case 67:
/* 896 */         break;
/*     */       case 4:
/* 898 */         this.numWikiTokensSeen = 0; this.positionInc = 1; this.currentTokType = 17; yybegin(6); break;
/*     */       case 68:
/* 900 */         break;
/*     */       case 38:
/* 902 */         this.numBalanced = 0; this.currentTokType = 0; yybegin(0); break;
/*     */       case 69:
/* 904 */         break;
/*     */       case 13:
/* 906 */         this.currentTokType = 9; this.numWikiTokensSeen = 0; yybegin(6); break;
/*     */       case 70:
/* 908 */         break;
/*     */       case 3:
/* 910 */         this.positionInc = 1; return 7;
/*     */       case 71:
/* 912 */         break;
/*     */       case 45:
/* 914 */         this.currentTokType = 11; this.numWikiTokensSeen = 0; yybegin(2); break;
/*     */       case 72:
/* 916 */         break;
/*     */       case 6:
/* 918 */         yybegin(2); this.numWikiTokensSeen += 1; return this.currentTokType;
/*     */       case 73:
/* 920 */         break;
/*     */       case 11:
/* 922 */         this.currentTokType = 12; yybegin(10); break;
/*     */       case 74:
/* 924 */         break;
/*     */       case 25:
/* 926 */         this.numWikiTokensSeen = 0; this.positionInc = 1; this.currentTokType = 10; yybegin(16); break;
/*     */       case 75:
/* 928 */         break;
/*     */       case 8:
/* 930 */         break;
/*     */       case 76:
/* 932 */         break;
/*     */       case 19:
/* 934 */         yybegin(18); this.numWikiTokensSeen += 1; return this.currentTokType;
/*     */       case 77:
/* 936 */         break;
/*     */       case 43:
/* 938 */         this.positionInc = 1; this.numWikiTokensSeen += 1; yybegin(6); return this.currentTokType;
/*     */       case 78:
/* 940 */         break;
/*     */       case 42:
/* 942 */         this.numBalanced = 0; this.currentTokType = 0; yybegin(0); break;
/*     */       case 79:
/* 944 */         break;
/*     */       case 30:
/* 946 */         yybegin(0); break;
/*     */       case 80:
/* 948 */         break;
/*     */       case 14:
/* 950 */         yybegin(18); this.numWikiTokensSeen += 1; return this.currentTokType;
/*     */       case 81:
/* 952 */         break;
/*     */       case 9:
/* 954 */         if (this.numLinkToks == 0) this.positionInc = 0; else this.positionInc = 1; this.numWikiTokensSeen += 1; this.currentTokType = 9; yybegin(6); this.numLinkToks += 1; return this.currentTokType;
/*     */       case 82:
/* 956 */         break;
/*     */       case 7:
/* 958 */         yybegin(4); this.numWikiTokensSeen += 1; return this.currentTokType;
/*     */       case 83:
/* 960 */         break;
/*     */       case 41:
/* 962 */         this.positionInc = 1; return 4;
/*     */       case 84:
/* 964 */         break;
/*     */       case 28:
/* 966 */         this.currentTokType = 8; this.numWikiTokensSeen = 0; yybegin(4); break;
/*     */       case 85:
/* 968 */         break;
/*     */       case 23:
/* 970 */         this.numWikiTokensSeen = 0; this.positionInc = 1; yybegin(14); break;
/*     */       case 86:
/* 972 */         break;
/*     */       case 34:
/* 974 */         this.positionInc = 1; return 5;
/*     */       case 87:
/* 976 */         break;
/*     */       case 32:
/* 978 */         this.numBalanced = 0; this.numWikiTokensSeen = 0; this.currentTokType = 8; yybegin(4); break;
/*     */       case 88:
/* 980 */         break;
/*     */       case 12:
/* 982 */         this.currentTokType = 13; this.numWikiTokensSeen += 1; yybegin(18); return this.currentTokType;
/*     */       case 89:
/* 984 */         break;
/*     */       case 24:
/* 986 */         this.numWikiTokensSeen = 0; this.positionInc = 1; this.currentTokType = 8; yybegin(4); break;
/*     */       case 90:
/* 988 */         break;
/*     */       case 31:
/* 990 */         this.numBalanced = 0; this.currentTokType = 0; yybegin(0); break;
/*     */       case 91:
/* 992 */         break;
/*     */       case 26:
/* 994 */         yybegin(0); break;
/*     */       case 92:
/* 996 */         break;
/*     */       default:
/* 998 */         if ((zzInput == -1) && (this.zzStartRead == this.zzCurrentPos)) {
/* 999 */           this.zzAtEOF = true;
/* 1000 */           return -1;
/*     */         }
/*     */ 
/* 1003 */         zzScanError(1);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.wikipedia.WikipediaTokenizerImpl
 * JD-Core Version:    0.6.2
 */