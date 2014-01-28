package com.aliasi.tag;

import java.util.List;

public abstract interface MarginalTagger<E>
{
  public abstract TagLattice<E> tagMarginal(List<E> paramList);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.MarginalTagger
 * JD-Core Version:    0.6.2
 */