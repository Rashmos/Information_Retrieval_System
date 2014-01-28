package com.aliasi.cluster;

import java.util.Set;

public abstract interface Clusterer<E>
{
  public abstract Set<Set<E>> cluster(Set<? extends E> paramSet);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.Clusterer
 * JD-Core Version:    0.6.2
 */