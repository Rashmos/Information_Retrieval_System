/*     */ package com.aliasi.cluster;
/*     */ 
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.SmallSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class LeafDendrogram<E> extends Dendrogram<E>
/*     */ {
/*     */   private final E mObject;
/*     */ 
/*     */   public LeafDendrogram(E object)
/*     */   {
/*  45 */     this.mObject = object;
/*     */   }
/*     */ 
/*     */   public double score()
/*     */   {
/*  55 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public E object()
/*     */   {
/*  64 */     return this.mObject;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  75 */     return 1;
/*     */   }
/*     */ 
/*     */   public Set<E> memberSet()
/*     */   {
/*  85 */     return SmallSet.create(this.mObject);
/*     */   }
/*     */ 
/*     */   void split(Collection<Set<E>> resultSet, BoundedPriorityQueue<Dendrogram<E>> queue)
/*     */   {
/*  91 */     resultSet.add(memberSet());
/*     */   }
/*     */ 
/*     */   int copheneticCorrelation(int i, double[] xs, double[] ys, Distance<? super E> distance)
/*     */   {
/*  97 */     return i;
/*     */   }
/*     */ 
/*     */   void addMembers(Set<E> set)
/*     */   {
/* 103 */     set.add(this.mObject);
/*     */   }
/*     */ 
/*     */   void toString(StringBuilder sb, int depth)
/*     */   {
/* 108 */     sb.append(this.mObject);
/*     */   }
/*     */ 
/*     */   void prettyPrint(StringBuilder sb, int depth)
/*     */   {
/* 113 */     indent(sb, depth);
/* 114 */     sb.append(this.mObject);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.LeafDendrogram
 * JD-Core Version:    0.6.2
 */