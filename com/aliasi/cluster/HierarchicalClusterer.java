package com.aliasi.cluster;

import java.util.Set;

public abstract interface HierarchicalClusterer<E> extends Clusterer<E>
{
  public abstract Dendrogram<E> hierarchicalCluster(Set<? extends E> paramSet);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.HierarchicalClusterer
 * JD-Core Version:    0.6.2
 */