package com.aliasi.tag;

import java.util.List;

public abstract interface Tagger<E>
{
  public abstract Tagging<E> tag(List<E> paramList);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.Tagger
 * JD-Core Version:    0.6.2
 */