package org.apache.lucene.analysis.standard;

public abstract interface StandardTokenizerConstants
{
  public static final int EOF = 0;
  public static final int ALPHANUM = 1;
  public static final int APOSTROPHE = 2;
  public static final int ACRONYM = 3;
  public static final int COMPANY = 4;
  public static final int EMAIL = 5;
  public static final int HOST = 6;
  public static final int NUM = 7;
  public static final int P = 8;
  public static final int HAS_DIGIT = 9;
  public static final int ALPHA = 10;
  public static final int LETTER = 11;
  public static final int DIGIT = 12;
  public static final int NOISE = 13;
  public static final int DEFAULT = 0;
  public static final String[] tokenImage = { "<EOF>", "<ALPHANUM>", "<APOSTROPHE>", "<ACRONYM>", "<COMPANY>", "<EMAIL>", "<HOST>", "<NUM>", "<P>", "<HAS_DIGIT>", "<ALPHA>", "<LETTER>", "<DIGIT>", "<NOISE>" };
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.analysis.standard.StandardTokenizerConstants
 * JD-Core Version:    0.6.2
 */