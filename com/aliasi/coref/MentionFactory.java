package com.aliasi.coref;

public abstract interface MentionFactory
{
  public abstract Mention create(String paramString1, String paramString2);

  public abstract MentionChain promote(Mention paramMention, int paramInt);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.MentionFactory
 * JD-Core Version:    0.6.2
 */