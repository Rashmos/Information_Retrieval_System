package com.aliasi.tag;

import java.util.Iterator;
import java.util.List;

public abstract interface NBestTagger<E> extends Tagger<E>
{
  public abstract Iterator<ScoredTagging<E>> tagNBest(List<E> paramList, int paramInt);

  public abstract Iterator<ScoredTagging<E>> tagNBestConditional(List<E> paramList, int paramInt);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.tag.NBestTagger
 * JD-Core Version:    0.6.2
 */