package org.apache.lucene.queryParser;

public abstract interface QueryParserConstants
{
  public static final int EOF = 0;
  public static final int _NUM_CHAR = 1;
  public static final int _ESCAPED_CHAR = 2;
  public static final int _TERM_START_CHAR = 3;
  public static final int _TERM_CHAR = 4;
  public static final int _WHITESPACE = 5;
  public static final int AND = 7;
  public static final int OR = 8;
  public static final int NOT = 9;
  public static final int PLUS = 10;
  public static final int MINUS = 11;
  public static final int LPAREN = 12;
  public static final int RPAREN = 13;
  public static final int COLON = 14;
  public static final int CARAT = 15;
  public static final int QUOTED = 16;
  public static final int TERM = 17;
  public static final int FUZZY = 18;
  public static final int SLOP = 19;
  public static final int PREFIXTERM = 20;
  public static final int WILDTERM = 21;
  public static final int RANGEIN = 22;
  public static final int RANGEEX = 23;
  public static final int NUMBER = 24;
  public static final int Boost = 0;
  public static final int DEFAULT = 1;
  public static final String[] tokenImage = { "<EOF>", "<_NUM_CHAR>", "<_ESCAPED_CHAR>", "<_TERM_START_CHAR>", "<_TERM_CHAR>", "<_WHITESPACE>", "<token of kind 6>", "<AND>", "<OR>", "<NOT>", "\"+\"", "\"-\"", "\"(\"", "\")\"", "\":\"", "\"^\"", "<QUOTED>", "<TERM>", "\"~\"", "<SLOP>", "<PREFIXTERM>", "<WILDTERM>", "<RANGEIN>", "<RANGEEX>", "<NUMBER>" };
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.queryParser.QueryParserConstants
 * JD-Core Version:    0.6.2
 */