/*     */ package com.aliasi.cluster;
/*     */ 
/*     */ import com.aliasi.util.BoundedPriorityQueue;
/*     */ import com.aliasi.util.Distance;
/*     */ import com.aliasi.util.ObjectToSet;
/*     */ import com.aliasi.util.ScoredObject;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CompleteLinkClusterer<E> extends AbstractHierarchicalClusterer<E>
/*     */ {
/*     */   public CompleteLinkClusterer(double maxDistance, Distance<? super E> distance)
/*     */   {
/* 130 */     super(maxDistance, distance);
/*     */   }
/*     */ 
/*     */   public CompleteLinkClusterer(Distance<? super E> distance)
/*     */   {
/* 139 */     this((1.0D / 0.0D), distance);
/*     */   }
/*     */ 
/*     */   public Dendrogram<E> hierarchicalCluster(Set<? extends E> elementSet)
/*     */   {
/* 145 */     if (elementSet.size() == 0) {
/* 146 */       String msg = "Require non-empty set to form dendrogram. Found elementSet.size()=" + elementSet.size();
/*     */ 
/* 148 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 150 */     if (elementSet.size() == 1) {
/* 151 */       return new LeafDendrogram(elementSet.iterator().next());
/*     */     }
/*     */ 
/* 154 */     BoundedPriorityQueue queue = new BoundedPriorityQueue(ScoredObject.reverseComparator(), 2147483647);
/*     */ 
/* 157 */     ObjectToSet index = new ObjectToSet();
/*     */ 
/* 159 */     Object[] elements = toElements(elementSet);
/*     */ 
/* 163 */     LeafDendrogram[] leafs = (LeafDendrogram[])new LeafDendrogram[elements.length];
/*     */ 
/* 165 */     for (int i = 0; i < leafs.length; i++)
/* 166 */       leafs[i] = new LeafDendrogram(elements[i]);
/* 167 */     double maxDistance = getMaxDistance();
/* 168 */     for (int i = 0; i < elements.length; i++) {
/* 169 */       Object eI = elements[i];
/* 170 */       LeafDendrogram dI = leafs[i];
/* 171 */       for (int j = i + 1; j < elements.length; j++) {
/* 172 */         Object eJ = elements[j];
/* 173 */         double score = distance().distance(eI, eJ);
/* 174 */         if (score <= maxDistance) {
/* 175 */           LeafDendrogram dJ = leafs[j];
/* 176 */           AbstractHierarchicalClusterer.PairScore psIJ = new AbstractHierarchicalClusterer.PairScore(dI, dJ, score);
/* 177 */           queue.offer(psIJ);
/* 178 */           index.addMember(dI, psIJ);
/* 179 */           index.addMember(dJ, psIJ);
/*     */         }
/*     */       }
/*     */     }
/* 183 */     while (queue.size() > 0) {
/* 184 */       AbstractHierarchicalClusterer.PairScore next = (AbstractHierarchicalClusterer.PairScore)queue.poll();
/* 185 */       Dendrogram dendro1 = next.mDendrogram1.dereference();
/* 186 */       Dendrogram dendro2 = next.mDendrogram2.dereference();
/* 187 */       double dist12 = next.score();
/* 188 */       LinkDendrogram dendro12 = new LinkDendrogram(dendro1, dendro2, dist12);
/*     */ 
/* 192 */       HashMap distanceBuf = new HashMap();
/*     */ 
/* 194 */       Set ps3Set = (Set)index.remove(dendro1);
/* 195 */       queue.removeAll(ps3Set);
/* 196 */       for (AbstractHierarchicalClusterer.PairScore ps3 : ps3Set) {
/* 197 */         Dendrogram dendro3 = ps3.mDendrogram1 == dendro1 ? ps3.mDendrogram2 : ps3.mDendrogram1;
/*     */ 
/* 201 */         ((Set)index.get(dendro3)).remove(ps3);
/* 202 */         double dist1_3 = ps3.score();
/* 203 */         distanceBuf.put(dendro3, Double.valueOf(dist1_3));
/*     */       }
/*     */ 
/* 207 */       ps3Set = (Set)index.remove(dendro2);
/* 208 */       queue.removeAll(ps3Set);
/* 209 */       for (AbstractHierarchicalClusterer.PairScore ps3 : ps3Set) {
/* 210 */         Dendrogram dendro3 = ps3.mDendrogram1 == dendro2 ? ps3.mDendrogram2 : ps3.mDendrogram1;
/*     */ 
/* 214 */         ((Set)index.get(dendro3)).remove(ps3);
/* 215 */         Double dist1_3D = (Double)distanceBuf.get(dendro3);
/* 216 */         if (dist1_3D != null) {
/* 217 */           double dist1_3 = dist1_3D.doubleValue();
/* 218 */           double dist2_3 = ps3.score();
/* 219 */           double dist12_3 = Math.max(dist1_3, dist2_3);
/* 220 */           AbstractHierarchicalClusterer.PairScore ps = new AbstractHierarchicalClusterer.PairScore(dendro12, dendro3, dist12_3);
/* 221 */           queue.offer(ps);
/* 222 */           index.addMember(dendro12, ps);
/* 223 */           index.addMember(dendro3, ps);
/*     */         }
/*     */       }
/*     */ 
/* 227 */       if (queue.isEmpty()) return dendro12;
/*     */ 
/*     */     }
/*     */ 
/* 231 */     Iterator it = index.keySet().iterator();
/* 232 */     Dendrogram dendro = (Dendrogram)it.next();
/* 233 */     while (it.hasNext()) {
/* 234 */       dendro = new LinkDendrogram(dendro, (Dendrogram)it.next(), (1.0D / 0.0D));
/*     */     }
/* 236 */     return dendro;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.CompleteLinkClusterer
 * JD-Core Version:    0.6.2
 */