package com.aliasi.coref;

import java.util.Set;

public abstract interface MentionChain
{
  public abstract Set<Mention> mentions();

  public abstract int maxSentenceOffset();

  public abstract String entityType();

  public abstract void add(Mention paramMention, int paramInt);

  public abstract int identifier();

  public abstract boolean killed(Mention paramMention);

  public abstract int matchScore(Mention paramMention);

  public abstract String gender();

  public abstract Set<String> honorifics();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.MentionChain
 * JD-Core Version:    0.6.2
 */