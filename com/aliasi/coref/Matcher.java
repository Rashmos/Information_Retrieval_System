package com.aliasi.coref;

public abstract interface Matcher
{
  public static final int NO_MATCH_SCORE = -1;
  public static final int MAX_SEMANTIC_SCORE = 4;
  public static final int MAX_DISTANCE_SCORE = 2;
  public static final int MAX_SCORE = 6;

  public abstract int match(Mention paramMention, MentionChain paramMentionChain);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.Matcher
 * JD-Core Version:    0.6.2
 */