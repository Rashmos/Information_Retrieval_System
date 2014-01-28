/*      */ package com.aliasi.cluster;
/*      */ 
/*      */ import com.aliasi.io.LogLevel;
/*      */ import com.aliasi.io.Reporter;
/*      */ import com.aliasi.io.Reporters;
/*      */ import com.aliasi.stats.Statistics;
/*      */ import com.aliasi.symbol.MapSymbolTable;
/*      */ import com.aliasi.util.FeatureExtractor;
/*      */ import com.aliasi.util.ObjectToDoubleMap;
/*      */ import com.aliasi.util.SmallSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class KMeansClusterer<E>
/*      */   implements Clusterer<E>
/*      */ {
/*      */   final FeatureExtractor<E> mFeatureExtractor;
/*      */   final int mMaxNumClusters;
/*      */   final int mMaxEpochs;
/*      */   final boolean mKMeansPlusPlus;
/*      */   final double mMinRelativeImprovement;
/*      */ 
/*      */   KMeansClusterer(FeatureExtractor<E> featureExtractor, int numClusters, int maxEpochs)
/*      */   {
/*  309 */     this(featureExtractor, numClusters, maxEpochs, false, 0.0D);
/*      */   }
/*      */ 
/*      */   public KMeansClusterer(FeatureExtractor<E> featureExtractor, int numClusters, int maxEpochs, boolean kMeansPlusPlus, double minImprovement)
/*      */   {
/*  342 */     if (numClusters < 1) {
/*  343 */       String msg = "Number of clusters must be positive. Found numClusters=" + numClusters;
/*      */ 
/*  345 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  347 */     if (maxEpochs < 0) {
/*  348 */       String msg = "Number of epochs must be non-negative. Found maxEpochs=" + maxEpochs;
/*      */ 
/*  350 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  352 */     if ((minImprovement < 0.0D) || (Double.isNaN(minImprovement))) {
/*  353 */       String msg = "Mimium improvement must be non-negative. Found minImprovement=" + minImprovement;
/*      */ 
/*  355 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  358 */     this.mFeatureExtractor = featureExtractor;
/*  359 */     this.mMaxNumClusters = numClusters;
/*  360 */     this.mMaxEpochs = maxEpochs;
/*  361 */     this.mKMeansPlusPlus = kMeansPlusPlus;
/*  362 */     this.mMinRelativeImprovement = minImprovement;
/*      */   }
/*      */ 
/*      */   public FeatureExtractor<E> featureExtractor()
/*      */   {
/*  372 */     return this.mFeatureExtractor;
/*      */   }
/*      */ 
/*      */   public int numClusters()
/*      */   {
/*  386 */     return this.mMaxNumClusters;
/*      */   }
/*      */ 
/*      */   public int maxEpochs()
/*      */   {
/*  395 */     return this.mMaxEpochs;
/*      */   }
/*      */ 
/*      */   public Set<Set<E>> cluster(Set<? extends E> elementSet)
/*      */   {
/*  417 */     return cluster(elementSet, new Random(), null);
/*      */   }
/*      */ 
/*      */   public Set<Set<E>> cluster(Set<? extends E> elementSet, Random random, Reporter reporter)
/*      */   {
/*  451 */     if (reporter == null) {
/*  452 */       reporter = Reporters.silent();
/*      */     }
/*  454 */     int numElements = elementSet.size();
/*  455 */     int numClusters = this.mMaxNumClusters;
/*  456 */     reporter.report(LogLevel.INFO, "#Elements=" + numElements);
/*  457 */     reporter.report(LogLevel.INFO, "#Clusters=" + numClusters);
/*      */ 
/*  459 */     if (numElements <= numClusters) {
/*  460 */       reporter.report(LogLevel.INFO, "Returning trivial clustering due to #elements < #clusters");
/*      */ 
/*  462 */       return trivialClustering(elementSet);
/*      */     }
/*      */ 
/*  466 */     Object[] elements = (Object[])elementSet.toArray(new Object[0]);
/*      */ 
/*  468 */     reporter.report(LogLevel.DEBUG, "Converting inputs to sparse vectors");
/*  469 */     int[][] featuress = new int[numElements][];
/*  470 */     double[][] valss = new double[numElements][];
/*  471 */     double[] eltSqLengths = new double[numElements];
/*  472 */     MapSymbolTable symTab = toVectors(elements, featuress, valss, eltSqLengths);
/*      */ 
/*  474 */     int numDims = symTab.numSymbols();
/*  475 */     reporter.report(LogLevel.INFO, "#Dimensions=" + numDims);
/*      */ 
/*  477 */     double[][] centroidss = new double[numClusters][numDims];
/*  478 */     int[] closestCenters = new int[numElements];
/*  479 */     double[] sqDistToCenters = new double[numElements];
/*      */ 
/*  482 */     reporter.report(LogLevel.INFO, "K-Means++ Initialization");
/*  483 */     kmeansPlusPlusInit(featuress, valss, eltSqLengths, closestCenters, centroidss, random);
/*      */ 
/*  496 */     return kMeansEpochs(elements, eltSqLengths, centroidss, featuress, valss, sqDistToCenters, closestCenters, this.mMaxEpochs, reporter);
/*      */   }
/*      */ 
/*      */   public double minRelativeImprovement()
/*      */   {
/*  510 */     return this.mMinRelativeImprovement;
/*      */   }
/*      */ 
/*      */   public Set<Set<E>> recluster(Set<Set<E>> initialClustering, Set<E> unclusteredElements, Reporter reporter)
/*      */   {
/*  533 */     return recluster(initialClustering, unclusteredElements, this.mMaxEpochs, reporter);
/*      */   }
/*      */ 
/*      */   Set<Set<E>> recluster(Set<Set<E>> clustering, int maxEpochs)
/*      */   {
/*  559 */     return recluster(clustering, SmallSet.create(), maxEpochs, null);
/*      */   }
/*      */ 
/*      */   private Set<Set<E>> recluster(Set<Set<E>> clustering, Set<E> unclusteredElements, int maxEpochs, Reporter reporter)
/*      */   {
/*  568 */     if (reporter == null) {
/*  569 */       reporter = Reporters.silent();
/*      */     }
/*  571 */     reporter.report(LogLevel.INFO, "Reclustering");
/*      */ 
/*  573 */     int numClusters = clustering.size();
/*  574 */     reporter.report(LogLevel.INFO, "# Clusters=" + numClusters);
/*      */ 
/*  576 */     Set elementSet = new HashSet();
/*  577 */     for (Set cluster : clustering)
/*  578 */       for (i$ = cluster.iterator(); i$.hasNext(); ) { Object e = i$.next();
/*  579 */         if (!elementSet.add(e)) {
/*  580 */           String msg = "An element must not be in two clusters. Found an element in two clusters. Element=" + e;
/*      */ 
/*  583 */           throw new IllegalArgumentException(msg);
/*      */         }
/*      */       }
/*  588 */     Iterator i$;
/*  587 */     int numClusteredElements = elementSet.size();
/*  588 */     for (Iterator i$ = unclusteredElements.iterator(); i$.hasNext(); ) { Object e = i$.next();
/*  589 */       if (!elementSet.add(e)) {
/*  590 */         String msg = "An element may not be in a cluster and unclustered. Found unclustered element in a cluster. Element=" + e;
/*      */ 
/*  593 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*  596 */     int numElements = elementSet.size();
/*  597 */     reporter.report(LogLevel.INFO, "# Clustered Elements=" + numClusteredElements);
/*      */ 
/*  599 */     reporter.report(LogLevel.INFO, "# Unclustered Elements=" + unclusteredElements.size());
/*      */ 
/*  601 */     reporter.report(LogLevel.INFO, "# Elements Total=" + numElements);
/*      */ 
/*  605 */     Object[] elements = (Object[])new Object[numElements];
/*  606 */     int i = 0;
/*  607 */     for (Set cluster : clustering)
/*  608 */       for (i$ = cluster.iterator(); i$.hasNext(); ) { Object e = i$.next();
/*  609 */         elements[(i++)] = e;
/*      */       }
/*  610 */     Iterator i$;
/*  610 */     for (Iterator i$ = unclusteredElements.iterator(); i$.hasNext(); ) { Object e = i$.next();
/*  611 */       elements[(i++)] = e;
/*      */     }
/*  613 */     reporter.report(LogLevel.DEBUG, "Converting to vectors");
/*      */ 
/*  615 */     int[][] featuress = new int[numElements][];
/*  616 */     double[][] valss = new double[numElements][];
/*  617 */     double[] eltSqLengths = new double[numElements];
/*  618 */     MapSymbolTable symTab = toVectors(elements, featuress, valss, eltSqLengths);
/*      */ 
/*  620 */     int numDims = symTab.numSymbols();
/*  621 */     reporter.report(LogLevel.INFO, "#Dimensions=" + numDims);
/*      */ 
/*  623 */     double[][] centroidss = new double[numClusters][numDims];
/*  624 */     int[] closestCenters = new int[numElements];
/*  625 */     i = 0;
/*  626 */     int k = 0;
/*  627 */     for (Set cluster : clustering) {
/*  628 */       double[] centroidK = centroidss[k];
/*  629 */       for (Iterator i$ = cluster.iterator(); i$.hasNext(); ) { Object e = i$.next();
/*  630 */         closestCenters[i] = k;
/*  631 */         increment(centroidK, featuress[i], valss[i]);
/*  632 */         i++;
/*      */       }
/*  634 */       k++;
/*      */     }
/*  636 */     double[] sqDistToCenters = new double[numElements];
/*  637 */     Arrays.fill(sqDistToCenters, (1.0D / 0.0D));
/*      */ 
/*  639 */     for (k = 0; k < numClusters; k++) {
/*  640 */       double[] centroidK = centroidss[k];
/*  641 */       double centroidSqLength = selfProduct(centroidss[k]);
/*  642 */       for (i = 0; i < numElements; i++) {
/*  643 */         double sqDistToCenter = centroidSqLength + eltSqLengths[i] - 2.0D * product(centroidK, featuress[i], valss[i]);
/*      */ 
/*  647 */         if (sqDistToCenter < sqDistToCenters[i]) {
/*  648 */           sqDistToCenters[i] = sqDistToCenter;
/*  649 */           closestCenters[i] = k;
/*      */         }
/*      */       }
/*      */     }
/*  653 */     for (double[] centroid : centroidss)
/*  654 */       Arrays.fill(centroid, 0.0D);
/*  655 */     setCentroids(centroidss, featuress, valss, closestCenters);
/*      */ 
/*  657 */     return kMeansEpochs(elements, eltSqLengths, centroidss, featuress, valss, sqDistToCenters, closestCenters, maxEpochs, reporter);
/*      */   }
/*      */ 
/*      */   private Set<Set<E>> kMeansEpochs(E[] elements, double[] eltSqLengths, double[][] centroidss, int[][] featuress, double[][] valss, double[] sqDistToCenters, int[] closestCenters, int maxEpochs, Reporter reporter)
/*      */   {
/*  675 */     int numClusters = centroidss.length;
/*  676 */     int numDims = centroidss[0].length;
/*  677 */     int numElements = elements.length;
/*      */ 
/*  679 */     double[] centroidSqLengths = centroidSqLengths(centroidss);
/*  680 */     boolean[] lastCentroidChanges = createBooleanArray(numClusters, true);
/*  681 */     int[] changedClusters = new int[numClusters];
/*  682 */     int[] counts = new int[numClusters];
/*      */ 
/*  684 */     double lastError = (1.0D / 0.0D);
/*  685 */     for (int epoch = 0; epoch < maxEpochs; epoch++) {
/*  686 */       reporter.report(LogLevel.DEBUG, "Epoch=" + epoch);
/*  687 */       boolean atLeastOneClusterChanged = false;
/*  688 */       int numChangedClusters = setChangedClusters(changedClusters, lastCentroidChanges);
/*      */ 
/*  690 */       reporter.report(LogLevel.DEBUG, "    #changed clusters=" + numChangedClusters);
/*  691 */       boolean[] centroidChanges = createBooleanArray(numClusters, false);
/*      */ 
/*  695 */       for (int i = 0; i < numElements; i++) {
/*  696 */         int[] featuresI = featuress[i];
/*  697 */         double[] valsI = valss[i];
/*  698 */         double eltSqLengthI = eltSqLengths[i];
/*  699 */         double closestSqDistToCenter = lastCentroidChanges[closestCenters[i]] != 0 ? (1.0D / 0.0D) : sqDistToCenters[i];
/*      */ 
/*  703 */         int bestCenter = -1;
/*  704 */         for (int kk = 0; kk < numChangedClusters; kk++)
/*      */         {
/*  706 */           int k = changedClusters[kk];
/*  707 */           double sqDistToCenter = centroidSqLengths[k] + eltSqLengthI - 2.0D * product(centroidss[k], featuresI, valsI);
/*      */ 
/*  711 */           if (sqDistToCenter < closestSqDistToCenter) {
/*  712 */             closestSqDistToCenter = sqDistToCenter;
/*  713 */             bestCenter = k;
/*      */           }
/*      */         }
/*      */ 
/*  717 */         if (bestCenter != -1)
/*      */         {
/*  719 */           if (closestSqDistToCenter > sqDistToCenters[i]) {
/*  720 */             for (int kk = numChangedClusters; kk < numClusters; kk++)
/*      */             {
/*  722 */               int k = changedClusters[kk];
/*  723 */               double sqDistToCenter = centroidSqLengths[k] + eltSqLengthI - 2.0D * product(centroidss[k], featuresI, valsI);
/*      */ 
/*  727 */               if (sqDistToCenter < closestSqDistToCenter) {
/*  728 */                 closestSqDistToCenter = sqDistToCenter;
/*  729 */                 bestCenter = k;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*  734 */           sqDistToCenters[i] = closestSqDistToCenter;
/*  735 */           if (bestCenter != closestCenters[i]) {
/*  736 */             atLeastOneClusterChanged = true;
/*  737 */             centroidChanges[bestCenter] = true;
/*  738 */             centroidChanges[closestCenters[i]] = true;
/*  739 */             closestCenters[i] = bestCenter;
/*      */           }
/*      */         }
/*      */       }
/*  741 */       double error = sum(sqDistToCenters) / numElements;
/*  742 */       reporter.report(LogLevel.DEBUG, "    avg dist to center=" + error);
/*      */ 
/*  744 */       if (!atLeastOneClusterChanged) {
/*  745 */         reporter.report(LogLevel.INFO, "Converged by no elements changing cluster.");
/*  746 */         break;
/*      */       }
/*  748 */       double relImprovement = relativeImprovement(lastError, error);
/*  749 */       if (relImprovement < this.mMinRelativeImprovement) {
/*  750 */         reporter.report(LogLevel.INFO, "Converged by relative improvement < threshold");
/*      */ 
/*  752 */         break;
/*      */       }
/*      */ 
/*  755 */       Arrays.fill(counts, 0);
/*  756 */       int numChangedElts = 0;
/*  757 */       for (int k = 0; k < numClusters; k++)
/*  758 */         if (centroidChanges[k] != 0)
/*  759 */           Arrays.fill(centroidss[k], 0.0D);
/*  760 */       for (int i = 0; i < numElements; i++) {
/*  761 */         int closestCenterI = closestCenters[i];
/*  762 */         if (centroidChanges[closestCenterI] != 0) {
/*  763 */           increment(centroidss[closestCenterI], featuress[i], valss[i]);
/*      */ 
/*  765 */           counts[closestCenterI] += 1;
/*  766 */           numChangedElts++;
/*      */         }
/*      */       }
/*  769 */       reporter.report(LogLevel.DEBUG, "    #changed elts=" + numChangedElts);
/*      */ 
/*  771 */       for (int k = 0; k < numClusters; k++) {
/*  772 */         if (counts[k] > 0) {
/*  773 */           double[] centroidK = centroidss[k];
/*  774 */           double countD = counts[k];
/*  775 */           double sqLength = 0.0D;
/*  776 */           for (int d = 0; d < numDims; d++) {
/*  777 */             centroidK[d] /= countD;
/*  778 */             sqLength += centroidK[d] * centroidK[d];
/*      */           }
/*  780 */           centroidSqLengths[k] = sqLength;
/*      */         }
/*      */       }
/*      */ 
/*  784 */       lastCentroidChanges = centroidChanges;
/*  785 */       if (epoch == maxEpochs - 1) {
/*  786 */         reporter.report(LogLevel.INFO, "Reached max epochs. Breaking without convergence.");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  792 */     reporter.report(LogLevel.DEBUG, "Constructing Result");
/*      */ 
/*  794 */     List scoreMapList = new ArrayList(numClusters);
/*  795 */     double[] totalScores = new double[numClusters];
/*  796 */     for (int k = 0; k < numClusters; k++)
/*  797 */       scoreMapList.add(new ObjectToDoubleMap());
/*  798 */     for (int i = 0; i < numElements; i++) {
/*  799 */       ((ObjectToDoubleMap)scoreMapList.get(closestCenters[i])).set(elements[i], sqDistToCenters[i] == 0.0D ? -4.940656458412465E-324D : -sqDistToCenters[i]);
/*      */ 
/*  803 */       totalScores[closestCenters[i]] -= sqDistToCenters[i];
/*      */     }
/*      */ 
/*  806 */     ObjectToDoubleMap clusterScores = new ObjectToDoubleMap();
/*      */ 
/*  808 */     for (int k = 0; k < numClusters; k++) {
/*  809 */       ObjectToDoubleMap clusterDistances = (ObjectToDoubleMap)scoreMapList.get(k);
/*  810 */       if (!clusterDistances.isEmpty())
/*      */       {
/*  812 */         Set cluster = new LinkedHashSet(clusterDistances.keysOrderedByValueList());
/*      */ 
/*  814 */         clusterScores.set(cluster, totalScores[k] == 0.0D ? -4.940656458412465E-324D : totalScores[k] / cluster.size());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  819 */     Set result = new LinkedHashSet(clusterScores.keysOrderedByValueList());
/*      */ 
/*  821 */     return result;
/*      */   }
/*      */ 
/*      */   static double relativeImprovement(double x, double y) {
/*  825 */     return Math.abs(2.0D * (x - y) / (Math.abs(x) + Math.abs(y)));
/*      */   }
/*      */ 
/*      */   static int setChangedClusters(int[] clusterIndexes, boolean[] changed) {
/*  829 */     int numChanged = 0;
/*  830 */     int numNotChanged = clusterIndexes.length - 1;
/*      */ 
/*  832 */     for (int i = 0; i < changed.length; i++)
/*  833 */       clusterIndexes[(changed[i] != 0 ? numChanged++ : numNotChanged--)] = i;
/*  834 */     return numChanged;
/*      */   }
/*      */ 
/*      */   static boolean[] createBooleanArray(int length, boolean fillValue) {
/*  838 */     boolean[] result = new boolean[length];
/*  839 */     if (fillValue)
/*  840 */       Arrays.fill(result, true);
/*  841 */     return result;
/*      */   }
/*      */ 
/*      */   private MapSymbolTable toVectors(E[] elements, int[][] featuress, double[][] valss, double[] eltSqLengths)
/*      */   {
/*  847 */     MapSymbolTable symTab = new MapSymbolTable();
/*  848 */     for (int i = 0; i < elements.length; i++) {
/*  849 */       Object e = elements[i];
/*  850 */       Map featureMap = this.mFeatureExtractor.features(e);
/*      */ 
/*  852 */       featuress[i] = new int[featureMap.size()];
/*  853 */       valss[i] = new double[featureMap.size()];
/*  854 */       int j = 0;
/*      */ 
/*  856 */       for (Map.Entry entry : featureMap.entrySet()) {
/*  857 */         featuress[i][j] = symTab.getOrAddSymbol((String)entry.getKey());
/*  858 */         valss[i][j] = ((Number)entry.getValue()).doubleValue();
/*  859 */         j++;
/*      */       }
/*  861 */       eltSqLengths[i] = selfProduct(valss[i]);
/*      */     }
/*  863 */     return symTab;
/*      */   }
/*      */ 
/*      */   private Set<Set<E>> trivialClustering(Set<? extends E> elementSet) {
/*  867 */     Set clustering = new HashSet(3 * elementSet.size() / 2);
/*      */ 
/*  869 */     for (Iterator i$ = elementSet.iterator(); i$.hasNext(); ) { Object elt = i$.next();
/*  870 */       Set cluster = SmallSet.create(elt);
/*  871 */       clustering.add(cluster);
/*      */     }
/*  873 */     return clustering;
/*      */   }
/*      */ 
/*      */   private void randomInit(int[][] featuress, double[][] valss, int[] closestCenters, double[][] centroidss, Random random)
/*      */   {
/*  882 */     int numClusters = centroidss.length;
/*  883 */     int numElements = featuress.length;
/*  884 */     int[] permutation = Statistics.permutation(numElements, random);
/*  885 */     int[] count = new int[numClusters];
/*  886 */     for (int i = 0; i < numElements; i++)
/*  887 */       closestCenters[i] = (i % numClusters);
/*  888 */     setCentroids(centroidss, featuress, valss, closestCenters);
/*      */   }
/*      */ 
/*      */   private void kmeansPlusPlusInit(int[][] featuress, double[][] valss, double[] eltSqLengths, int[] closestCenters, double[][] centroidss, Random random)
/*      */   {
/*  898 */     int numClusters = centroidss.length;
/*  899 */     int numElements = featuress.length;
/*  900 */     double[] sqDistToCenters = new double[numElements];
/*  901 */     Arrays.fill(sqDistToCenters, (1.0D / 0.0D));
/*  902 */     for (int k = 0; k < numClusters; k++) {
/*  903 */       double[] centroidK = centroidss[k];
/*  904 */       int centroidIndex = k == 0 ? random.nextInt(numElements) : sampleNextCenter(sqDistToCenters, random);
/*      */ 
/*  908 */       setCentroid(centroidK, featuress[centroidIndex], valss[centroidIndex]);
/*      */ 
/*  910 */       double centroidSqLength = selfProduct(valss[centroidIndex]);
/*  911 */       for (int i = 0; i < numElements; i++) {
/*  912 */         double sqDistToCenter = centroidSqLength + eltSqLengths[i] - 2.0D * product(centroidK, featuress[i], valss[i]);
/*      */ 
/*  916 */         if (sqDistToCenter < sqDistToCenters[i]) {
/*  917 */           sqDistToCenters[i] = sqDistToCenter;
/*  918 */           closestCenters[i] = k;
/*      */         }
/*      */       }
/*      */     }
/*  922 */     for (double[] centroid : centroidss)
/*  923 */       Arrays.fill(centroid, 0.0D);
/*  924 */     setCentroids(centroidss, featuress, valss, closestCenters);
/*      */   }
/*      */ 
/*      */   private void setCentroids(double[][] centroidss, int[][] featuress, double[][] valss, int[] closestCenters)
/*      */   {
/*  931 */     int numClusters = centroidss.length;
/*  932 */     int numElements = featuress.length;
/*  933 */     int[] count = new int[numClusters];
/*  934 */     for (int i = 0; i < numElements; i++) {
/*  935 */       increment(centroidss[closestCenters[i]], featuress[i], valss[i]);
/*      */ 
/*  937 */       count[closestCenters[i]] += 1;
/*      */     }
/*  939 */     for (int k = 0; k < numClusters; k++) {
/*  940 */       double countK = count[k];
/*  941 */       double[] centroid = centroidss[k];
/*  942 */       for (int d = 0; d < centroid.length; d++)
/*  943 */         centroid[d] /= countK;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int sampleNextCenter(double[] probRatios, Random random)
/*      */   {
/*  950 */     double samplePoint = random.nextDouble() * sum(probRatios);
/*  951 */     double total = 0.0D;
/*  952 */     for (int i = 0; i < probRatios.length; i++) {
/*  953 */       total += probRatios[i];
/*  954 */       if (total >= samplePoint)
/*  955 */         return i;
/*      */     }
/*  957 */     return probRatios.length - 1;
/*      */   }
/*      */ 
/*      */   private static double[] centroidSqLengths(double[][] centroidss) {
/*  961 */     double[] result = new double[centroidss.length];
/*  962 */     for (int i = 0; i < result.length; i++)
/*  963 */       result[i] = selfProduct(centroidss[i]);
/*  964 */     return result;
/*      */   }
/*      */ 
/*      */   private static double selfProduct(double[] xs) {
/*  968 */     double sum = 0.0D;
/*  969 */     for (int i = 0; i < xs.length; i++)
/*  970 */       sum += xs[i] * xs[i];
/*  971 */     return sum;
/*      */   }
/*      */ 
/*      */   private static double sum(double[] xs) {
/*  975 */     double sum = 0.0D;
/*  976 */     for (int i = 0; i < xs.length; i++)
/*  977 */       sum += xs[i];
/*  978 */     return sum;
/*      */   }
/*      */ 
/*      */   private static double product(double[] centroid, int[] features, double[] values)
/*      */   {
/*  985 */     double sum = 0.0D;
/*  986 */     for (int i = 0; i < features.length; i++)
/*  987 */       sum += values[i] * centroid[features[i]];
/*  988 */     return sum;
/*      */   }
/*      */ 
/*      */   private static void setCentroid(double[] centroid, int[] indexes, double[] values)
/*      */   {
/*  994 */     for (int i = 0; i < indexes.length; i++)
/*  995 */       centroid[indexes[i]] = values[i];
/*      */   }
/*      */ 
/*      */   private static void increment(double[] centroid, int[] indexes, double[] values)
/*      */   {
/* 1001 */     for (int i = 0; i < indexes.length; i++)
/* 1002 */       centroid[indexes[i]] += values[i];
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.cluster.KMeansClusterer
 * JD-Core Version:    0.6.2
 */