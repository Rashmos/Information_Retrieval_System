package com.aliasi.crf;

import java.util.List;

public abstract interface ChainCrfFeatureExtractor<E>
{
  public abstract ChainCrfFeatures<E> extract(List<E> paramList, List<String> paramList1);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.crf.ChainCrfFeatureExtractor
 * JD-Core Version:    0.6.2
 */