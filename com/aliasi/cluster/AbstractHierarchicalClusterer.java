/*     */ package com.aliasi.cluster;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.Scored;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractHierarchicalClusterer<E>
/*     */   implements HierarchicalClusterer<E>
/*     */ {
/*     */   private double mMaxDistance;
/*     */   private final Distance<? super E> mDistance;
/*     */ 
/*     */   public AbstractHierarchicalClusterer(double maxDistance, Distance<? super E> distance)
/*     */   {
/*  81 */     setMaxDistance(maxDistance);
/*  82 */     this.mDistance = distance;
/*     */   }
/*     */ 
/*     */   public Distance<? super E> distance()
/*     */   {
/*  91 */     return this.mDistance;
/*     */   }
/*     */ 
/*     */   public abstract Dendrogram<E> hierarchicalCluster(Set<? extends E> paramSet);
/*     */ 
/*     */   public Set<Set<E>> cluster(Set<? extends E> elements)
/*     */   {
/* 118 */     if (elements.isEmpty())
/* 119 */       return new HashSet();
/* 120 */     Dendrogram dendrogram = hierarchicalCluster(elements);
/*     */ 
/* 122 */     return dendrogram.partitionDistance(this.mMaxDistance);
/*     */   }
/*     */ 
/*     */   public double getMaxDistance()
/*     */   {
/* 132 */     return this.mMaxDistance;
/*     */   }
/*     */ 
/*     */   public final void setMaxDistance(double maxDistance)
/*     */   {
/* 142 */     assertValidDistanceBound(maxDistance);
/* 143 */     this.mMaxDistance = maxDistance;
/*     */   }
/*     */ 
/*     */   static void assertValidDistanceBound(double maxDistance)
/*     */   {
/* 149 */     if ((maxDistance < 0.0D) || (Double.isNaN(maxDistance))) {
/* 150 */       String msg = "Max distance must be non-negative number. Found maxDistance=" + maxDistance;
/*     */ 
/* 152 */       throw new IllegalArgumentException(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   E[] toElements(Set<? extends E> elementSet)
/*     */   {
/* 158 */     int len = elementSet.size();
/*     */ 
/* 161 */     Object[] elements = (Object[])new Object[len];
/* 162 */     elementSet.toArray(elements);
/* 163 */     return elements;
/*     */   }
/*     */ 
/*     */   static class PairScore<E> implements Scored {
/*     */     final Dendrogram<E> mDendrogram1;
/*     */     final Dendrogram<E> mDendrogram2;
/*     */     final double mScore;
/*     */ 
/*     */     public PairScore(Dendrogram<E> dendrogram1, Dendrogram<E> dendrogram2, double score) {
/* 174 */       this.mDendrogram1 = dendrogram1;
/* 175 */       this.mDendrogram2 = dendrogram2;
/* 176 */       this.mScore = score;
/*     */     }
/*     */     public double score() {
/* 179 */       return this.mScore;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 183 */       return "ps(" + this.mDendrogram1 + "," + this.mDendrogram2 + ":" + this.mScore + ") ";
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.AbstractHierarchicalClusterer
 * JD-Core Version:    0.6.2
 */