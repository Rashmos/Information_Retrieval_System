/*      */ package com.aliasi.util;
/*      */ 
/*      */ public class Strings
/*      */ {
/*   48 */   public static String UTF8 = "UTF-8";
/*      */ 
/*   53 */   public static String Latin1 = "ISO-8859-1";
/*      */ 
/*   58 */   public static String ASCII = "ASCII";
/*      */ 
/*  979 */   public static char NBSP_CHAR = ' ';
/*      */ 
/*  984 */   public static char NEWLINE_CHAR = '\n';
/*      */ 
/*  989 */   public static char DEFAULT_SEPARATOR_CHAR = ' ';
/*      */ 
/*  996 */   public static String DEFAULT_SEPARATOR_STRING = String.valueOf(DEFAULT_SEPARATOR_CHAR);
/*      */   public static final String SINGLE_SPACE_STRING = " ";
/*      */   public static final String EMPTY_STRING = "";
/* 1012 */   public static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*      */ 
/* 1017 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */ 
/* 1023 */   public static final String[][] EMPTY_STRING_2D_ARRAY = new String[0][];
/*      */ 
/*      */   public static String reverse(CharSequence cs)
/*      */   {
/*   65 */     StringBuilder sb = new StringBuilder(cs.length());
/*   66 */     int i = cs.length();
/*      */     while (true) { i--; if (i < 0) break;
/*   67 */       sb.append(cs.charAt(i)); }
/*   68 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static boolean containsChar(String s, char c)
/*      */   {
/*   81 */     return s.indexOf(c) >= 0;
/*      */   }
/*      */ 
/*      */   public static boolean allWhitespace(StringBuilder sb)
/*      */   {
/*   93 */     return allWhitespace(sb.toString());
/*      */   }
/*      */ 
/*      */   public static boolean allWhitespace(String s)
/*      */   {
/*  106 */     return allWhitespace(s.toCharArray(), 0, s.length());
/*      */   }
/*      */ 
/*      */   public static boolean allWhitespace(char[] ch, int start, int length)
/*      */   {
/*  121 */     for (int i = start; i < start + length; i++)
/*  122 */       if (!isWhitespace(ch[i])) return false;
/*  123 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isWhitespace(char c)
/*      */   {
/*  138 */     return (Character.isWhitespace(c)) || (c == NBSP_CHAR);
/*      */   }
/*      */ 
/*      */   public static void normalizeWhitespace(CharSequence cs, StringBuilder sb)
/*      */   {
/*  159 */     int i = 0;
/*  160 */     int length = cs.length();
/*  161 */     while ((length > 0) && (isWhitespace(cs.charAt(length - 1))))
/*  162 */       length--;
/*  163 */     while ((i < length) && (isWhitespace(cs.charAt(i))))
/*  164 */       i++;
/*  165 */     boolean inWhiteSpace = false;
/*  166 */     for (; i < length; i++) {
/*  167 */       char nextChar = cs.charAt(i);
/*  168 */       if (isWhitespace(nextChar)) {
/*  169 */         if (!inWhiteSpace) {
/*  170 */           sb.append(' ');
/*  171 */           inWhiteSpace = true;
/*      */         }
/*      */       } else {
/*  174 */         inWhiteSpace = false;
/*  175 */         sb.append(nextChar);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String normalizeWhitespace(CharSequence cs)
/*      */   {
/*  190 */     StringBuilder sb = new StringBuilder();
/*  191 */     normalizeWhitespace(cs, sb);
/*  192 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static boolean allDigits(String s)
/*      */   {
/*  204 */     return allDigits(s.toCharArray(), 0, s.length());
/*      */   }
/*      */ 
/*      */   public static boolean allDigits(char[] cs, int start, int length)
/*      */   {
/*  218 */     for (int i = 0; i < length; i++)
/*  219 */       if (!Character.isDigit(cs[(i + start)])) return false;
/*  220 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean isPunctuation(char c)
/*      */   {
/*  235 */     return (c == ',') || (c == '.') || (c == '!') || (c == '?') || (c == ':') || (c == ';');
/*      */   }
/*      */ 
/*      */   public static String power(String s, int count)
/*      */   {
/*  255 */     StringBuilder sb = new StringBuilder();
/*  256 */     for (int i = 0; i < count; i++)
/*  257 */       sb.append(s);
/*  258 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static String concatenate(Object[] xs)
/*      */   {
/*  272 */     return concatenate(xs, DEFAULT_SEPARATOR_STRING);
/*      */   }
/*      */ 
/*      */   public static String concatenate(Object[] xs, String spacer)
/*      */   {
/*  287 */     return concatenate(xs, 0, spacer);
/*      */   }
/*      */ 
/*      */   public static String concatenate(Object[] xs, int start, String spacer)
/*      */   {
/*  306 */     return concatenate(xs, start, xs.length, spacer);
/*      */   }
/*      */ 
/*      */   public static String concatenate(Object[] xs, int start, int end)
/*      */   {
/*  324 */     return concatenate(xs, start, end, DEFAULT_SEPARATOR_STRING);
/*      */   }
/*      */ 
/*      */   public static String concatenate(Object[] xs, int start, int end, String spacer)
/*      */   {
/*  345 */     StringBuilder sb = new StringBuilder();
/*  346 */     for (int i = start; i < end; i++) {
/*  347 */       if (i > start) sb.append(spacer);
/*  348 */       sb.append(xs[i]);
/*      */     }
/*  350 */     sb.setLength(sb.length());
/*  351 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static void indent(StringBuilder sb, int length)
/*      */   {
/*  364 */     sb.append(NEWLINE_CHAR);
/*  365 */     padding(sb, length);
/*      */   }
/*      */ 
/*      */   public static String padding(int length)
/*      */   {
/*  378 */     StringBuilder sb = new StringBuilder();
/*  379 */     padding(sb, length);
/*  380 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   public static void padding(StringBuilder sb, int length)
/*      */   {
/*  392 */     for (int i = 0; i < length; i++) sb.append(DEFAULT_SEPARATOR_CHAR);
/*      */   }
/*      */ 
/*      */   public static String functionArgs(String functionName, Object[] args)
/*      */   {
/*  406 */     return functionName + functionArgsList(args);
/*      */   }
/*      */ 
/*      */   public static String functionArgsList(Object[] args)
/*      */   {
/*  419 */     return "(" + concatenate(args, ",") + ")";
/*      */   }
/*      */ 
/*      */   public static boolean allLowerCase(char[] chars)
/*      */   {
/*  431 */     for (int i = 0; i < chars.length; i++)
/*  432 */       if (!Character.isLowerCase(chars[i]))
/*  433 */         return false;
/*  434 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean allLowerCase(CharSequence token)
/*      */   {
/*  446 */     int len = token.length();
/*  447 */     for (int i = 0; i < len; i++) {
/*  448 */       if (!Character.isLowerCase(token.charAt(i)))
/*  449 */         return false;
/*      */     }
/*  451 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean allUpperCase(char[] chars)
/*      */   {
/*  463 */     for (int i = 0; i < chars.length; i++)
/*  464 */       if (!Character.isUpperCase(chars[i]))
/*  465 */         return false;
/*  466 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean allLetters(char[] chars)
/*      */   {
/*  478 */     for (int i = 0; i < chars.length; i++)
/*  479 */       if (!Character.isLetter(chars[i]))
/*  480 */         return false;
/*  481 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean allPunctuation(char[] chars)
/*      */   {
/*  494 */     for (int i = 0; i < chars.length; i++)
/*  495 */       if (!isPunctuation(chars[i]))
/*  496 */         return false;
/*  497 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean allPunctuation(String token)
/*      */   {
/*  510 */     int i = token.length();
/*      */     do { i--; if (i < 0) break; }
/*  511 */     while (isPunctuation(token.charAt(i)));
/*  512 */     return false;
/*  513 */     return true;
/*      */   }
/*      */ 
/*      */   public static String[] split(String s, char c)
/*      */   {
/*  562 */     char[] cs = s.toCharArray();
/*  563 */     int tokCount = 1;
/*  564 */     for (int i = 0; i < cs.length; i++)
/*  565 */       if (cs[i] == c) tokCount++;
/*  566 */     String[] result = new String[tokCount];
/*  567 */     int tokIndex = 0;
/*  568 */     int start = 0;
/*  569 */     for (int end = 0; end < cs.length; end++) {
/*  570 */       if (cs[end] == c) {
/*  571 */         result[tokIndex] = new String(cs, start, end - start);
/*  572 */         tokIndex++;
/*  573 */         start = end + 1;
/*      */       }
/*      */     }
/*  576 */     result[tokIndex] = new String(cs, start, cs.length - start);
/*  577 */     return result;
/*      */   }
/*      */ 
/*      */   public static boolean allSymbols(char[] cs)
/*      */   {
/*  590 */     for (int i = 0; i < cs.length; i++)
/*  591 */       if ((Character.isLetter(cs[i])) || (Character.isDigit(cs[i])))
/*  592 */         return false;
/*  593 */     return true;
/*      */   }
/*      */ 
/*      */   public static boolean containsDigits(char[] chars)
/*      */   {
/*  605 */     for (int i = 0; i < chars.length; i++)
/*  606 */       if (Character.isDigit(chars[i]))
/*  607 */         return true;
/*  608 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean containsLetter(char[] chars)
/*      */   {
/*  620 */     for (int i = 0; i < chars.length; i++)
/*  621 */       if (Character.isLetter(chars[i]))
/*  622 */         return true;
/*  623 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean capitalized(char[] chars)
/*      */   {
/*  637 */     if (chars.length == 0) return false;
/*  638 */     if (!Character.isUpperCase(chars[0])) return false;
/*  639 */     for (int i = 1; i < chars.length; i++)
/*  640 */       if (!Character.isLowerCase(chars[i]))
/*  641 */         return false;
/*  642 */     return true;
/*      */   }
/*      */ 
/*      */   public static String titleCase(String word)
/*      */   {
/*  654 */     if (word.length() < 1) return word;
/*  655 */     if (!Character.isLetter(word.charAt(0))) return word;
/*  656 */     return Character.toUpperCase(word.charAt(0)) + word.substring(1);
/*      */   }
/*      */ 
/*      */   public static String bytesToHex(byte[] bytes)
/*      */   {
/*  672 */     StringBuilder sb = new StringBuilder();
/*  673 */     for (int i = 0; i < bytes.length; i++)
/*  674 */       sb.append(byteToHex(bytes[i]));
/*  675 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   static String byteToHex(byte b) {
/*  679 */     String result = Integer.toHexString(Math.byteAsUnsigned(b));
/*  680 */     switch (result.length()) { case 0:
/*  681 */       return "00";
/*      */     case 1:
/*  682 */       return "0" + result;
/*      */     case 2:
/*  683 */       return result;
/*      */     }
/*      */ 
/*  686 */     String msg = "byteToHex(" + b + ")=" + result;
/*  687 */     throw new IllegalArgumentException(msg);
/*      */   }
/*      */ 
/*      */   public static void checkArgsStartEnd(char[] cs, int start, int end)
/*      */   {
/*  702 */     if (end < start) {
/*  703 */       String msg = "End must be >= start. Found start=" + start + " end=" + end;
/*      */ 
/*  706 */       throw new IndexOutOfBoundsException(msg);
/*      */     }
/*  708 */     if ((start >= 0) && (end <= cs.length)) return;
/*  709 */     if ((start < 0) || (start >= cs.length)) {
/*  710 */       String msg = "Start must be greater than 0 and less than length of array. Found start=" + start + " Array length=" + cs.length;
/*      */ 
/*  713 */       throw new IndexOutOfBoundsException(msg);
/*      */     }
/*      */ 
/*  716 */     if ((end < 0) || (end > cs.length)) {
/*  717 */       String msg = "End must be between 0 and  the length of the array. Found end=" + end + " Array length=" + cs.length;
/*      */ 
/*  720 */       throw new IndexOutOfBoundsException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static char[] toCharArray(CharSequence cSeq)
/*      */   {
/*  733 */     char[] cs = new char[cSeq.length()];
/*  734 */     for (int i = 0; i < cs.length; i++)
/*  735 */       cs[i] = cSeq.charAt(i);
/*  736 */     return cs;
/*      */   }
/*      */ 
/*      */   public static String nsToString(long ns)
/*      */   {
/*  749 */     return msToString(ns / 1000000L);
/*      */   }
/*      */ 
/*      */   public static String msToString(long ms)
/*      */   {
/*  776 */     long totalSecs = ms / 1000L;
/*  777 */     long hours = totalSecs / 3600L;
/*  778 */     long mins = totalSecs / 60L % 60L;
/*  779 */     long secs = totalSecs % 60L;
/*  780 */     String minsString = "" + mins;
/*      */ 
/*  785 */     String secsString = "" + secs;
/*      */ 
/*  790 */     if (hours > 0L)
/*  791 */       return hours + ":" + minsString + ":" + secsString;
/*  792 */     if (mins > 0L)
/*  793 */       return mins + ":" + secsString;
/*  794 */     return ":" + secsString;
/*      */   }
/*      */ 
/*      */   public static boolean equalCharSequence(CharSequence cs1, CharSequence cs2)
/*      */   {
/*  813 */     if (cs1 == cs2) return true;
/*  814 */     int len = cs1.length();
/*  815 */     if (len != cs2.length()) return false;
/*  816 */     for (int i = 0; i < len; i++)
/*  817 */       if (cs1.charAt(i) != cs2.charAt(i)) return false;
/*  818 */     return true;
/*      */   }
/*      */ 
/*      */   public static int hashCode(CharSequence cSeq)
/*      */   {
/*  849 */     if ((cSeq instanceof String)) return cSeq.hashCode();
/*  850 */     int h = 0;
/*  851 */     for (int i = 0; i < cSeq.length(); i++)
/*  852 */       h = 31 * h + cSeq.charAt(i);
/*  853 */     return h;
/*      */   }
/*      */ 
/*      */   public static char deAccentLatin1(char c)
/*      */   {
/*  873 */     switch (c) { case 'À':
/*  874 */       return 'A';
/*      */     case 'Á':
/*  875 */       return 'A';
/*      */     case 'Â':
/*  876 */       return 'A';
/*      */     case 'Ã':
/*  877 */       return 'A';
/*      */     case 'Ä':
/*  878 */       return 'A';
/*      */     case 'Å':
/*  879 */       return 'A';
/*      */     case 'Æ':
/*  880 */       return 'A';
/*      */     case 'Ç':
/*  881 */       return 'C';
/*      */     case 'È':
/*  882 */       return 'E';
/*      */     case 'É':
/*  883 */       return 'E';
/*      */     case 'Ê':
/*  884 */       return 'E';
/*      */     case 'Ë':
/*  885 */       return 'E';
/*      */     case 'Ì':
/*  886 */       return 'I';
/*      */     case 'Í':
/*  887 */       return 'I';
/*      */     case 'Î':
/*  888 */       return 'I';
/*      */     case 'Ï':
/*  889 */       return 'I';
/*      */     case 'Ð':
/*  891 */       return 'D';
/*      */     case 'Ñ':
/*  892 */       return 'N';
/*      */     case 'Ò':
/*  893 */       return 'O';
/*      */     case 'Ó':
/*  894 */       return 'O';
/*      */     case 'Ô':
/*  895 */       return 'O';
/*      */     case 'Õ':
/*  896 */       return 'O';
/*      */     case 'Ö':
/*  897 */       return 'O';
/*      */     case 'Ø':
/*  898 */       return 'O';
/*      */     case 'Ù':
/*  899 */       return 'U';
/*      */     case 'Ú':
/*  900 */       return 'U';
/*      */     case 'Û':
/*  901 */       return 'U';
/*      */     case 'Ü':
/*  902 */       return 'U';
/*      */     case 'Ý':
/*  903 */       return 'Y';
/*      */     case 'Þ':
/*  904 */       return 'P';
/*      */     case 'ß':
/*  905 */       return 's';
/*      */     case 'à':
/*  907 */       return 'a';
/*      */     case 'á':
/*  908 */       return 'a';
/*      */     case 'â':
/*  909 */       return 'a';
/*      */     case 'ã':
/*  910 */       return 'a';
/*      */     case 'ä':
/*  911 */       return 'a';
/*      */     case 'å':
/*  912 */       return 'a';
/*      */     case 'æ':
/*  913 */       return 'a';
/*      */     case 'ç':
/*  914 */       return 'c';
/*      */     case 'è':
/*  915 */       return 'e';
/*      */     case 'é':
/*  916 */       return 'e';
/*      */     case 'ê':
/*  917 */       return 'e';
/*      */     case 'ë':
/*  918 */       return 'e';
/*      */     case 'ì':
/*  919 */       return 'i';
/*      */     case 'í':
/*  920 */       return 'i';
/*      */     case 'î':
/*  921 */       return 'i';
/*      */     case 'ï':
/*  922 */       return 'i';
/*      */     case 'ð':
/*  924 */       return 'd';
/*      */     case 'ñ':
/*  925 */       return 'n';
/*      */     case 'ò':
/*  926 */       return 'o';
/*      */     case 'ó':
/*  927 */       return 'o';
/*      */     case 'ô':
/*  928 */       return 'o';
/*      */     case 'õ':
/*  929 */       return 'o';
/*      */     case 'ö':
/*  930 */       return 'o';
/*      */     case 'ø':
/*  931 */       return 'o';
/*      */     case 'ù':
/*  932 */       return 'u';
/*      */     case 'ú':
/*  933 */       return 'u';
/*      */     case 'û':
/*  934 */       return 'u';
/*      */     case 'ü':
/*  935 */       return 'u';
/*      */     case 'ý':
/*  936 */       return 'y';
/*      */     case 'þ':
/*  937 */       return 'p';
/*      */     case 'ÿ':
/*  938 */       return 'y';
/*      */     case '×':
/*  940 */     case '÷': } return c;
/*      */   }
/*      */ 
/*      */   public static String deAccentLatin1(CharSequence cSeq)
/*      */   {
/*  953 */     char[] cs = new char[cSeq.length()];
/*  954 */     for (int i = 0; i < cs.length; i++)
/*  955 */       cs[i] = deAccentLatin1(cSeq.charAt(i));
/*  956 */     return new String(cs);
/*      */   }
/*      */ 
/*      */   public static int sharedPrefixLength(String a, String b)
/*      */   {
/*  969 */     int end = java.lang.Math.min(a.length(), b.length());
/*  970 */     for (int i = 0; i < end; i++)
/*  971 */       if (a.charAt(i) != b.charAt(i))
/*  972 */         return i;
/*  973 */     return end;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.Strings
 * JD-Core Version:    0.6.2
 */