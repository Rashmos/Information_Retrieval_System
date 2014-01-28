/*     */ package com.aliasi.cluster;
/*     */ 
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SingleLinkClusterer<E> extends AbstractHierarchicalClusterer<E>
/*     */ {
/*     */   public SingleLinkClusterer(double maxDistance, Distance<? super E> distance)
/*     */   {
/* 169 */     super(maxDistance, distance);
/*     */   }
/*     */ 
/*     */   public SingleLinkClusterer(Distance<? super E> distance)
/*     */   {
/* 180 */     this((1.0D / 0.0D), distance);
/*     */   }
/*     */ 
/*     */   public Dendrogram<E> hierarchicalCluster(Set<? extends E> elementSet)
/*     */   {
/* 198 */     if (elementSet.size() == 0) {
/* 199 */       String msg = "Require non-empty set to form dendrogram. Found elementSet.size()=" + elementSet.size();
/*     */ 
/* 201 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 203 */     if (elementSet.size() == 1)
/* 204 */       return new LeafDendrogram(elementSet.iterator().next());
/* 205 */     Object[] elements = toElements(elementSet);
/*     */ 
/* 209 */     LeafDendrogram[] leafs = (LeafDendrogram[])new LeafDendrogram[elements.length];
/*     */ 
/* 211 */     for (int i = 0; i < leafs.length; i++)
/*     */     {
/* 214 */       Object elt = elements[i];
/* 215 */       leafs[i] = new LeafDendrogram(elt);
/*     */     }
/* 217 */     Set clusters = new HashSet(elements.length);
/*     */ 
/* 219 */     for (Dendrogram dendrogram : leafs) {
/* 220 */       clusters.add(dendrogram);
/*     */     }
/*     */ 
/* 223 */     ArrayList pairScoreList = new ArrayList();
/* 224 */     int len = elements.length;
/* 225 */     double maxDistance = getMaxDistance();
/* 226 */     for (int i = 0; i < len; i++)
/*     */     {
/* 229 */       Object eI = elements[i];
/* 230 */       Dendrogram dendroI = leafs[i];
/* 231 */       for (int j = i + 1; j < len; j++)
/*     */       {
/* 234 */         Object eJ = elements[j];
/* 235 */         double distanceIJ = distance().distance(eI, eJ);
/* 236 */         if (distanceIJ <= maxDistance) {
/* 237 */           Dendrogram dendroJ = leafs[j];
/* 238 */           pairScoreList.add(new AbstractHierarchicalClusterer.PairScore(dendroI, dendroJ, distanceIJ));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 243 */     AbstractHierarchicalClusterer.PairScore[] pairScores = (AbstractHierarchicalClusterer.PairScore[])new AbstractHierarchicalClusterer.PairScore[pairScoreList.size()];
/*     */ 
/* 245 */     pairScoreList.toArray(pairScores);
/* 246 */     Arrays.sort(pairScores, ScoredObject.comparator());
/*     */ 
/* 248 */     for (int i = 0; (i < pairScores.length) && (clusters.size() > 1); i++) {
/* 249 */       AbstractHierarchicalClusterer.PairScore ps = pairScores[i];
/* 250 */       if (ps.score() > getMaxDistance()) break;
/* 251 */       Dendrogram d1 = ps.mDendrogram1.dereference();
/* 252 */       Dendrogram d2 = ps.mDendrogram2.dereference();
/* 253 */       if (!d1.equals(d2))
/*     */       {
/* 256 */         clusters.remove(d1);
/* 257 */         clusters.remove(d2);
/* 258 */         LinkDendrogram dLink = new LinkDendrogram(d1, d2, pairScores[i].mScore);
/*     */ 
/* 260 */         clusters.add(dLink);
/*     */       }
/*     */     }
/* 263 */     Iterator it = clusters.iterator();
/* 264 */     Dendrogram dendro = (Dendrogram)it.next();
/* 265 */     while (it.hasNext()) {
/* 266 */       dendro = new LinkDendrogram(dendro, (Dendrogram)it.next(), (1.0D / 0.0D));
/*     */     }
/* 268 */     return dendro;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.SingleLinkClusterer
 * JD-Core Version:    0.6.2
 */