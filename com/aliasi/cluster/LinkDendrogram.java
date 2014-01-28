/*     */ package com.aliasi.cluster;
/*     */ 
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Distance;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class LinkDendrogram<E> extends Dendrogram<E>
/*     */ {
/*     */   private final double mCost;
/*     */   private final Dendrogram<E> mDendrogram1;
/*     */   private final Dendrogram<E> mDendrogram2;
/*     */ 
/*     */   public LinkDendrogram(Dendrogram<E> dendrogram1, Dendrogram<E> dendrogram2, double cost)
/*     */   {
/*  58 */     if ((cost < 0.0D) || (Double.isNaN(cost))) {
/*  59 */       String msg = "Cost must be >= 0.0 Found cost=" + cost;
/*     */ 
/*  61 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  63 */     dendrogram1.setParent(this);
/*  64 */     dendrogram2.setParent(this);
/*  65 */     this.mDendrogram1 = dendrogram1;
/*  66 */     this.mDendrogram2 = dendrogram2;
/*  67 */     this.mCost = cost;
/*     */   }
/*     */ 
/*     */   public double score()
/*     */   {
/*  80 */     return this.mCost;
/*     */   }
/*     */ 
/*     */   public Set<E> memberSet()
/*     */   {
/*  85 */     HashSet members = new HashSet();
/*  86 */     addMembers(members);
/*  87 */     return members;
/*     */   }
/*     */ 
/*     */   void addMembers(Set<E> set)
/*     */   {
/*  92 */     this.mDendrogram1.addMembers(set);
/*  93 */     this.mDendrogram2.addMembers(set);
/*     */   }
/*     */ 
/*     */   void split(Collection<Set<E>> resultSet, BoundedPriorityQueue<Dendrogram<E>> queue)
/*     */   {
/*  99 */     queue.offer(this.mDendrogram1);
/* 100 */     queue.offer(this.mDendrogram2);
/*     */   }
/*     */ 
/*     */   public Dendrogram<E> dendrogram1()
/*     */   {
/* 112 */     return this.mDendrogram1;
/*     */   }
/*     */ 
/*     */   public Dendrogram<E> dendrogram2()
/*     */   {
/* 124 */     return this.mDendrogram2;
/*     */   }
/*     */ 
/*     */   void subpartitionDistance(LinkedList<Dendrogram<E>> stack)
/*     */   {
/* 129 */     stack.addFirst(dendrogram1());
/* 130 */     stack.addFirst(dendrogram2());
/*     */   }
/*     */ 
/*     */   int copheneticCorrelation(int i, double[] xs, double[] ys, Distance<? super E> distance)
/*     */   {
/* 136 */     for (Iterator i$ = this.mDendrogram1.memberSet().iterator(); i$.hasNext(); ) { e1 = i$.next();
/* 137 */       for (i$ = this.mDendrogram2.memberSet().iterator(); i$.hasNext(); ) { Object e2 = i$.next();
/* 138 */         xs[i] = score();
/* 139 */         ys[i] = distance.distance(e1, e2);
/* 140 */         i++;
/*     */       }
/*     */     }
/*     */     Object e1;
/*     */     Iterator i$;
/* 143 */     return i;
/*     */   }
/*     */ 
/*     */   void toString(StringBuilder sb, int depth)
/*     */   {
/* 148 */     sb.append('{');
/* 149 */     this.mDendrogram1.toString(sb, depth + 1);
/* 150 */     sb.append('+');
/* 151 */     this.mDendrogram2.toString(sb, depth + 1);
/* 152 */     sb.append("}:");
/* 153 */     sb.append(this.mCost);
/*     */   }
/*     */ 
/*     */   void prettyPrint(StringBuilder sb, int depth)
/*     */   {
/* 158 */     indent(sb, depth);
/* 159 */     sb.append(score());
/* 160 */     this.mDendrogram1.prettyPrint(sb, depth + 1);
/* 161 */     this.mDendrogram2.prettyPrint(sb, depth + 1);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.LinkDendrogram
 * JD-Core Version:    0.6.2
 */