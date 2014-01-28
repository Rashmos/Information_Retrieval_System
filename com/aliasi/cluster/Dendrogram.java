/*     */ package com.aliasi.cluster;
/*     */ 
/*     */ import com.aliasi.stats.Statistics;
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.Scored;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class Dendrogram<E>
/*     */   implements Scored
/*     */ {
/*     */   private LinkDendrogram<E> mParent;
/*     */   private Dendrogram<E> mReferenceLink;
/*     */ 
/*     */   Dendrogram()
/*     */   {
/*  89 */     this.mReferenceLink = this;
/*     */   }
/*     */ 
/*     */   public LinkDendrogram<E> parent()
/*     */   {
/* 101 */     return this.mParent;
/*     */   }
/*     */ 
/*     */   public Dendrogram<E> dereference()
/*     */   {
/* 113 */     Dendrogram ancestor = this.mReferenceLink.parent();
/* 114 */     if (ancestor == null) return this.mReferenceLink;
/* 115 */     LinkDendrogram nextAncestor = null;
/* 116 */     while ((nextAncestor = ancestor.parent()) != null)
/* 117 */       ancestor = nextAncestor;
/* 118 */     this.mReferenceLink = ancestor;
/* 119 */     return this.mReferenceLink;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 130 */     return memberSet().size();
/*     */   }
/*     */ 
/*     */   public boolean contains(E elt)
/*     */   {
/* 142 */     return memberSet().contains(elt);
/*     */   }
/*     */ 
/*     */   public Set<Set<E>> partitionK(int numClusters)
/*     */   {
/* 158 */     if (numClusters < 1) {
/* 159 */       String msg = "Require at least one cluster.  Found numClusters=" + numClusters;
/*     */ 
/* 161 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 163 */     if (size() < numClusters) {
/* 164 */       String msg = "This dendrogram contains only " + size() + " elements. " + " Require at least numClusters=" + numClusters;
/*     */ 
/* 167 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 169 */     BoundedPriorityQueue queue = new BoundedPriorityQueue(ScoredObject.comparator(), numClusters + 1);
/*     */ 
/* 172 */     queue.offer(this);
/* 173 */     HashSet resultSet = new HashSet(numClusters);
/*     */ 
/* 175 */     while (queue.size() + resultSet.size() < numClusters) {
/* 176 */       Dendrogram toSplit = (Dendrogram)queue.poll();
/* 177 */       toSplit.split(resultSet, queue);
/*     */     }
/* 179 */     for (Dendrogram d : queue)
/* 180 */       resultSet.add(d.memberSet());
/* 181 */     return resultSet;
/*     */   }
/*     */ 
/*     */   public Set<Set<E>> partitionDistance(double maxDistance)
/*     */   {
/* 205 */     HashSet clustering = new HashSet();
/* 206 */     LinkedList stack = new LinkedList();
/* 207 */     stack.addFirst(this);
/* 208 */     while (!stack.isEmpty()) {
/* 209 */       Dendrogram curDendro = (Dendrogram)stack.removeFirst();
/* 210 */       if (curDendro.score() <= maxDistance)
/* 211 */         clustering.add(curDendro.memberSet());
/*     */       else
/* 213 */         curDendro.subpartitionDistance(stack);
/*     */     }
/* 215 */     return clustering;
/*     */   }
/*     */ 
/*     */   void subpartitionDistance(LinkedList<Dendrogram<E>> stack)
/*     */   {
/*     */   }
/*     */ 
/*     */   abstract void split(Collection<Set<E>> paramCollection, BoundedPriorityQueue<Dendrogram<E>> paramBoundedPriorityQueue);
/*     */ 
/*     */   public String prettyPrint()
/*     */   {
/* 234 */     StringBuilder sb = new StringBuilder();
/* 235 */     prettyPrint(sb, 0);
/* 236 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 246 */     StringBuilder sb = new StringBuilder();
/* 247 */     toString(sb, 1);
/* 248 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public abstract Set<E> memberSet();
/*     */ 
/*     */   public abstract double score();
/*     */ 
/*     */   abstract void prettyPrint(StringBuilder paramStringBuilder, int paramInt);
/*     */ 
/*     */   abstract void addMembers(Set<E> paramSet);
/*     */ 
/*     */   abstract void toString(StringBuilder paramStringBuilder, int paramInt);
/*     */ 
/*     */   public double withinClusterScatter(int numClusters, Distance<? super E> distance)
/*     */   {
/* 287 */     if ((numClusters < 1) || (numClusters > size())) {
/* 288 */       String msg = "Require number of clusters between 1 and size. Found numClusters=" + numClusters + " size()=" + size();
/*     */ 
/* 291 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 293 */     Set clustering = partitionK(numClusters);
/* 294 */     return ClusterScore.withinClusterScatter(clustering, distance);
/*     */   }
/*     */ 
/*     */   public double copheneticCorrelation(Distance<? super E> distance)
/*     */   {
/* 347 */     int size = size() * (size() - 1) / 2;
/* 348 */     double[] xs = new double[size];
/* 349 */     double[] ys = new double[size];
/* 350 */     copheneticCorrelation(0, xs, ys, distance);
/* 351 */     return Statistics.correlation(xs, ys);
/*     */   }
/*     */ 
/*     */   public static <E> boolean structurallyEquivalent(Dendrogram<E> dendrogram1, Dendrogram<E> dendrogram2)
/*     */   {
/* 374 */     if ((dendrogram1 instanceof LeafDendrogram))
/*     */     {
/* 376 */       if (!(dendrogram2 instanceof LeafDendrogram)) {
/* 377 */         return false;
/*     */       }
/* 379 */       LeafDendrogram leafDendrogram1 = (LeafDendrogram)dendrogram1;
/* 380 */       LeafDendrogram leafDendrogram2 = (LeafDendrogram)dendrogram2;
/* 381 */       return (leafDendrogram1.object().equals(leafDendrogram2.object())) && (leafDendrogram1.score() == leafDendrogram2.score());
/*     */     }
/*     */ 
/* 384 */     if (!(dendrogram2 instanceof LinkDendrogram)) {
/* 385 */       return false;
/*     */     }
/*     */ 
/* 388 */     LinkDendrogram linkDendrogram1 = (LinkDendrogram)dendrogram1;
/* 389 */     LinkDendrogram linkDendrogram2 = (LinkDendrogram)dendrogram2;
/* 390 */     if (linkDendrogram1.score() != linkDendrogram2.score()) {
/* 391 */       return false;
/*     */     }
/* 393 */     return ((structurallyEquivalent(linkDendrogram1.dendrogram1(), linkDendrogram2.dendrogram1())) && (structurallyEquivalent(linkDendrogram1.dendrogram2(), linkDendrogram2.dendrogram2()))) || ((structurallyEquivalent(linkDendrogram1.dendrogram1(), linkDendrogram2.dendrogram2())) && (structurallyEquivalent(linkDendrogram1.dendrogram2(), linkDendrogram2.dendrogram1())));
/*     */   }
/*     */ 
/*     */   abstract int copheneticCorrelation(int paramInt, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, Distance<? super E> paramDistance);
/*     */ 
/*     */   void indent(StringBuilder sb, int indent)
/*     */   {
/* 409 */     sb.append('\n');
/* 410 */     for (int i = 0; i < indent; i++)
/* 411 */       sb.append("    ");
/*     */   }
/*     */ 
/*     */   void setParent(LinkDendrogram<E> parent) {
/* 415 */     this.mParent = parent;
/* 416 */     this.mReferenceLink = parent;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.Dendrogram
 * JD-Core Version:    0.6.2
 */