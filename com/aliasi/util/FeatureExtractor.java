package com.aliasi.util;

import java.util.Map;

public abstract interface FeatureExtractor<E>
{
  public abstract Map<String, ? extends Number> features(E paramE);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.util.FeatureExtractor
 * JD-Core Version:    0.6.2
 */