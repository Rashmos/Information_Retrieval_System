package com.aliasi.coref;

import java.util.Set;

public abstract interface Mention
{
  public abstract String phrase();

  public abstract String entityType();

  public abstract Set<String> honorifics();

  public abstract String normalPhrase();

  public abstract String[] normalTokens();

  public abstract boolean isPronominal();

  public abstract String gender();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.coref.Mention
 * JD-Core Version:    0.6.2
 */