/*      */ package com.aliasi.classify;
/*      */ 
/*      */ import com.aliasi.stats.Statistics;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class ConfusionMatrix
/*      */ {
/*      */   private final String[] mCategories;
/*      */   private final int[][] mMatrix;
/*  302 */   private final Map<String, Integer> mCategoryToIndex = new HashMap();
/*      */ 
/*      */   public ConfusionMatrix(String[] categories)
/*      */   {
/*  312 */     this.mCategories = categories;
/*  313 */     int len = categories.length;
/*  314 */     this.mMatrix = new int[len][len];
/*  315 */     for (int i = 0; i < len; i++)
/*  316 */       for (int j = 0; j < len; j++)
/*  317 */         this.mMatrix[i][j] = 0;
/*  318 */     for (int i = 0; i < len; i++)
/*  319 */       this.mCategoryToIndex.put(categories[i], Integer.valueOf(i));
/*      */   }
/*      */ 
/*      */   public ConfusionMatrix(String[] categories, int[][] matrix)
/*      */   {
/*  350 */     this.mCategories = categories;
/*  351 */     this.mMatrix = matrix;
/*  352 */     if (categories.length != matrix.length) {
/*  353 */       String msg = "Categories and matrix must be of same length. Found categories length=" + categories.length + " and matrix length=" + matrix.length;
/*      */ 
/*  356 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  358 */     for (int j = 0; j < matrix.length; j++) {
/*  359 */       if (categories.length != matrix[j].length) {
/*  360 */         String msg = "Categories and all matrix rows must be of same length. Found categories length=" + categories.length + " Found row " + j + " length=" + matrix[j].length;
/*      */ 
/*  363 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*  366 */     int len = matrix.length;
/*  367 */     for (int i = 0; i < len; i++)
/*  368 */       for (int j = 0; j < len; j++)
/*  369 */         if (matrix[i][j] < 0) {
/*  370 */           String msg = "Matrix entries must be non-negative. matrix[" + i + "][" + j + "]=" + matrix[i][j];
/*      */ 
/*  372 */           throw new IllegalArgumentException(msg);
/*      */         }
/*      */   }
/*      */ 
/*      */   public String[] categories()
/*      */   {
/*  398 */     return this.mCategories;
/*      */   }
/*      */ 
/*      */   public int numCategories()
/*      */   {
/*  412 */     return categories().length;
/*      */   }
/*      */ 
/*      */   public int getIndex(String category)
/*      */   {
/*  427 */     Integer index = (Integer)this.mCategoryToIndex.get(category);
/*  428 */     if (index == null) return -1;
/*  429 */     return index.intValue();
/*      */   }
/*      */ 
/*      */   public int[][] matrix()
/*      */   {
/*  438 */     return this.mMatrix;
/*      */   }
/*      */ 
/*      */   public void increment(int referenceCategoryIndex, int responseCategoryIndex)
/*      */   {
/*  452 */     checkIndex("reference", referenceCategoryIndex);
/*  453 */     checkIndex("response", responseCategoryIndex);
/*  454 */     this.mMatrix[referenceCategoryIndex][responseCategoryIndex] += 1;
/*      */   }
/*      */ 
/*      */   public void incrementByN(int referenceCategoryIndex, int responseCategoryIndex, int num)
/*      */   {
/*  469 */     checkIndex("reference", referenceCategoryIndex);
/*  470 */     checkIndex("response", responseCategoryIndex);
/*  471 */     this.mMatrix[referenceCategoryIndex][responseCategoryIndex] += num;
/*      */   }
/*      */ 
/*      */   public void increment(String referenceCategory, String responseCategory)
/*      */   {
/*  486 */     increment(getIndex(referenceCategory), getIndex(responseCategory));
/*      */   }
/*      */ 
/*      */   public int count(int referenceCategoryIndex, int responseCategoryIndex)
/*      */   {
/*  500 */     checkIndex("reference", referenceCategoryIndex);
/*  501 */     checkIndex("response", responseCategoryIndex);
/*  502 */     return this.mMatrix[referenceCategoryIndex][responseCategoryIndex];
/*      */   }
/*      */ 
/*      */   public int totalCount()
/*      */   {
/*  519 */     int total = 0;
/*  520 */     int len = numCategories();
/*  521 */     for (int i = 0; i < len; i++)
/*  522 */       for (int j = 0; j < len; j++)
/*  523 */         total += this.mMatrix[i][j];
/*  524 */     return total;
/*      */   }
/*      */ 
/*      */   public int totalCorrect()
/*      */   {
/*  544 */     int total = 0;
/*  545 */     int len = numCategories();
/*  546 */     for (int i = 0; i < len; i++)
/*  547 */       total += this.mMatrix[i][i];
/*  548 */     return total;
/*      */   }
/*      */ 
/*      */   public double totalAccuracy()
/*      */   {
/*  565 */     return totalCorrect() / totalCount();
/*      */   }
/*      */ 
/*      */   public double confidence95()
/*      */   {
/*  579 */     return confidence(1.96D);
/*      */   }
/*      */ 
/*      */   public double confidence99()
/*      */   {
/*  593 */     return confidence(2.58D);
/*      */   }
/*      */ 
/*      */   public double confidence(double z)
/*      */   {
/*  661 */     double p = totalAccuracy();
/*  662 */     double n = totalCount();
/*  663 */     return z * java.lang.Math.sqrt(p * (1.0D - p) / n);
/*      */   }
/*      */ 
/*      */   public double referenceEntropy()
/*      */   {
/*  686 */     double sum = 0.0D;
/*  687 */     for (int i = 0; i < numCategories(); i++) {
/*  688 */       double prob = oneVsAll(i).referenceLikelihood();
/*  689 */       sum += prob * com.aliasi.util.Math.log2(prob);
/*      */     }
/*  691 */     return -sum;
/*      */   }
/*      */ 
/*      */   public double responseEntropy()
/*      */   {
/*  712 */     double sum = 0.0D;
/*  713 */     for (int i = 0; i < numCategories(); i++) {
/*  714 */       double prob = oneVsAll(i).responseLikelihood();
/*  715 */       sum += prob * com.aliasi.util.Math.log2(prob);
/*      */     }
/*  717 */     return -sum;
/*      */   }
/*      */ 
/*      */   public double crossEntropy()
/*      */   {
/*  749 */     double sum = 0.0D;
/*  750 */     for (int i = 0; i < numCategories(); i++) {
/*  751 */       PrecisionRecallEvaluation eval = oneVsAll(i);
/*  752 */       double referenceProb = eval.referenceLikelihood();
/*  753 */       double responseProb = eval.responseLikelihood();
/*  754 */       sum += referenceProb * com.aliasi.util.Math.log2(responseProb);
/*      */     }
/*  756 */     return -sum;
/*      */   }
/*      */ 
/*      */   public double jointEntropy()
/*      */   {
/*  785 */     double totalCount = totalCount();
/*  786 */     double entropySum = 0.0D;
/*  787 */     for (int i = 0; i < numCategories(); i++) {
/*  788 */       for (int j = 0; j < numCategories(); j++) {
/*  789 */         double prob = count(i, j) / totalCount;
/*  790 */         if (prob > 0.0D)
/*  791 */           entropySum += prob * com.aliasi.util.Math.log2(prob);
/*      */       }
/*      */     }
/*  794 */     return -entropySum;
/*      */   }
/*      */ 
/*      */   public double conditionalEntropy(int refCategoryIndex)
/*      */   {
/*  821 */     double entropySum = 0.0D;
/*  822 */     long refCount = oneVsAll(refCategoryIndex).positiveReference();
/*  823 */     for (int j = 0; j < numCategories(); j++) {
/*  824 */       double conditionalProb = count(refCategoryIndex, j) / refCount;
/*      */ 
/*  826 */       if (conditionalProb > 0.0D) {
/*  827 */         entropySum += conditionalProb * com.aliasi.util.Math.log2(conditionalProb);
/*      */       }
/*      */     }
/*  830 */     return -entropySum;
/*      */   }
/*      */ 
/*      */   public double conditionalEntropy()
/*      */   {
/*  856 */     double entropySum = 0.0D;
/*  857 */     for (int i = 0; i < numCategories(); i++) {
/*  858 */       double refProbI = oneVsAll(i).referenceLikelihood();
/*  859 */       entropySum += refProbI * conditionalEntropy(i);
/*      */     }
/*  861 */     return entropySum;
/*      */   }
/*      */ 
/*      */   public double kappa()
/*      */   {
/*  884 */     return kappa(randomAccuracy());
/*      */   }
/*      */ 
/*      */   public double kappaUnbiased()
/*      */   {
/*  910 */     return kappa(randomAccuracyUnbiased());
/*      */   }
/*      */ 
/*      */   public double kappaNoPrevalence()
/*      */   {
/*  936 */     return 2.0D * totalAccuracy() - 1.0D;
/*      */   }
/*      */ 
/*      */   private double kappa(double PE) {
/*  940 */     double PA = totalAccuracy();
/*  941 */     return (PA - PE) / (1.0D - PE);
/*      */   }
/*      */ 
/*      */   public double randomAccuracy()
/*      */   {
/*  963 */     double randomAccuracy = 0.0D;
/*  964 */     for (int i = 0; i < numCategories(); i++) {
/*  965 */       PrecisionRecallEvaluation eval = oneVsAll(i);
/*  966 */       randomAccuracy += eval.referenceLikelihood() * eval.responseLikelihood();
/*      */     }
/*      */ 
/*  969 */     return randomAccuracy;
/*      */   }
/*      */ 
/*      */   public double randomAccuracyUnbiased()
/*      */   {
/*  990 */     double randomAccuracy = 0.0D;
/*  991 */     for (int i = 0; i < numCategories(); i++) {
/*  992 */       PrecisionRecallEvaluation eval = oneVsAll(i);
/*  993 */       double avgLikelihood = (eval.referenceLikelihood() + eval.responseLikelihood()) / 2.0D;
/*      */ 
/*  996 */       randomAccuracy += avgLikelihood * avgLikelihood;
/*      */     }
/*  998 */     return randomAccuracy;
/*      */   }
/*      */ 
/*      */   public int chiSquaredDegreesOfFreedom()
/*      */   {
/* 1018 */     int sqrt = numCategories() - 1;
/* 1019 */     return sqrt * sqrt;
/*      */   }
/*      */ 
/*      */   public double chiSquared()
/*      */   {
/* 1034 */     int numCategories = numCategories();
/* 1035 */     double[][] contingencyMatrix = new double[numCategories][numCategories];
/*      */ 
/* 1037 */     for (int i = 0; i < numCategories; i++)
/* 1038 */       for (int j = 0; j < numCategories; j++)
/* 1039 */         contingencyMatrix[i][j] = count(i, j);
/* 1040 */     return Statistics.chiSquaredIndependence(contingencyMatrix);
/*      */   }
/*      */ 
/*      */   public double phiSquared()
/*      */   {
/* 1061 */     return chiSquared() / totalCount();
/*      */   }
/*      */ 
/*      */   public double cramersV()
/*      */   {
/* 1076 */     double LMinusOne = numCategories() - 1;
/* 1077 */     return java.lang.Math.sqrt(phiSquared() / LMinusOne);
/*      */   }
/*      */ 
/*      */   public PrecisionRecallEvaluation oneVsAll(int categoryIndex)
/*      */   {
/* 1090 */     PrecisionRecallEvaluation eval = new PrecisionRecallEvaluation();
/* 1091 */     for (int i = 0; i < numCategories(); i++)
/* 1092 */       for (int j = 0; j < numCategories(); j++)
/* 1093 */         eval.addCase(i == categoryIndex, j == categoryIndex, this.mMatrix[i][j]);
/* 1094 */     return eval;
/*      */   }
/*      */ 
/*      */   public PrecisionRecallEvaluation microAverage()
/*      */   {
/* 1106 */     long tp = 0L;
/* 1107 */     long fp = 0L;
/* 1108 */     long fn = 0L;
/* 1109 */     long tn = 0L;
/* 1110 */     for (int i = 0; i < numCategories(); i++) {
/* 1111 */       PrecisionRecallEvaluation eval = oneVsAll(i);
/* 1112 */       tp += eval.truePositive();
/* 1113 */       fp += eval.falsePositive();
/* 1114 */       tn += eval.trueNegative();
/* 1115 */       fn += eval.falseNegative();
/*      */     }
/* 1117 */     return new PrecisionRecallEvaluation(tp, fn, fp, tn);
/*      */   }
/*      */ 
/*      */   public double macroAvgPrecision()
/*      */   {
/* 1137 */     double sum = 0.0D;
/* 1138 */     for (int i = 0; i < numCategories(); i++)
/* 1139 */       sum += oneVsAll(i).precision();
/* 1140 */     return sum / numCategories();
/*      */   }
/*      */ 
/*      */   public double macroAvgRecall()
/*      */   {
/* 1160 */     double sum = 0.0D;
/* 1161 */     for (int i = 0; i < numCategories(); i++)
/* 1162 */       sum += oneVsAll(i).recall();
/* 1163 */     return sum / numCategories();
/*      */   }
/*      */ 
/*      */   public double macroAvgFMeasure()
/*      */   {
/* 1187 */     double sum = 0.0D;
/* 1188 */     for (int i = 0; i < numCategories(); i++)
/* 1189 */       sum += oneVsAll(i).fMeasure();
/* 1190 */     return sum / numCategories();
/*      */   }
/*      */ 
/*      */   public double lambdaA()
/*      */   {
/* 1231 */     double maxReferenceCount = 0.0D;
/* 1232 */     for (int j = 0; j < numCategories(); j++) {
/* 1233 */       double referenceCount = oneVsAll(j).positiveReference();
/* 1234 */       if (referenceCount > maxReferenceCount)
/* 1235 */         maxReferenceCount = referenceCount;
/*      */     }
/* 1237 */     double maxCountSum = 0.0D;
/* 1238 */     for (int j = 0; j < numCategories(); j++) {
/* 1239 */       int maxCount = 0;
/* 1240 */       for (int i = 0; i < numCategories(); i++) {
/* 1241 */         int count = count(i, j);
/* 1242 */         if (count > maxCount)
/* 1243 */           maxCount = count;
/*      */       }
/* 1245 */       maxCountSum += maxCount;
/*      */     }
/* 1247 */     double totalCount = totalCount();
/* 1248 */     return (maxCountSum - maxReferenceCount) / (totalCount - maxReferenceCount);
/*      */   }
/*      */ 
/*      */   public double lambdaB()
/*      */   {
/* 1294 */     double maxResponseCount = 0.0D;
/* 1295 */     for (int i = 0; i < numCategories(); i++) {
/* 1296 */       double responseCount = oneVsAll(i).positiveResponse();
/* 1297 */       if (responseCount > maxResponseCount)
/* 1298 */         maxResponseCount = responseCount;
/*      */     }
/* 1300 */     double maxCountSum = 0.0D;
/* 1301 */     for (int i = 0; i < numCategories(); i++) {
/* 1302 */       int maxCount = 0;
/* 1303 */       for (int j = 0; j < numCategories(); j++) {
/* 1304 */         int count = count(i, j);
/* 1305 */         if (count > maxCount)
/* 1306 */           maxCount = count;
/*      */       }
/* 1308 */       maxCountSum += maxCount;
/*      */     }
/* 1310 */     double totalCount = totalCount();
/* 1311 */     return (maxCountSum - maxResponseCount) / (totalCount - maxResponseCount);
/*      */   }
/*      */ 
/*      */   public double mutualInformation()
/*      */   {
/* 1358 */     double totalCount = totalCount();
/* 1359 */     double sum = 0.0D;
/* 1360 */     for (int i = 0; i < numCategories(); i++) {
/* 1361 */       double pI = oneVsAll(i).referenceLikelihood();
/* 1362 */       if (pI > 0.0D)
/* 1363 */         for (int j = 0; j < numCategories(); j++) {
/* 1364 */           double pJ = oneVsAll(j).responseLikelihood();
/* 1365 */           if (pJ > 0.0D) {
/* 1366 */             double pIJ = count(i, j) / totalCount;
/* 1367 */             if (pIJ > 0.0D)
/* 1368 */               sum += pIJ * com.aliasi.util.Math.log2(pIJ / (pI * pJ)); 
/*      */           }
/*      */         }
/*      */     }
/* 1371 */     return sum;
/*      */   }
/*      */ 
/*      */   public double klDivergence()
/*      */   {
/* 1401 */     double sum = 0.0D;
/* 1402 */     for (int k = 0; k < numCategories(); k++) {
/* 1403 */       PrecisionRecallEvaluation eval = oneVsAll(k);
/* 1404 */       double refProb = eval.referenceLikelihood();
/* 1405 */       double responseProb = eval.responseLikelihood();
/* 1406 */       sum += refProb * com.aliasi.util.Math.log2(refProb / responseProb);
/*      */     }
/*      */ 
/* 1409 */     return sum;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1419 */     StringBuilder sb = new StringBuilder();
/* 1420 */     sb.append("GLOBAL CONFUSION MATRIX STATISTICS\n");
/* 1421 */     toStringGlobal(sb);
/* 1422 */     for (int i = 0; i < numCategories(); i++) {
/* 1423 */       sb.append("CATEGORY " + i + "=" + categories()[i] + " VS. ALL\n");
/* 1424 */       sb.append("  Conditional Entropy=" + conditionalEntropy(i));
/* 1425 */       sb.append('\n');
/* 1426 */       sb.append(oneVsAll(i).toString());
/* 1427 */       sb.append('\n');
/*      */     }
/* 1429 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   void toStringGlobal(StringBuilder sb) {
/* 1433 */     String[] categories = categories();
/* 1434 */     sb.append("Categories=" + Arrays.asList(categories));
/* 1435 */     sb.append('\n');
/* 1436 */     sb.append("Total Count=" + totalCount());
/* 1437 */     sb.append('\n');
/* 1438 */     sb.append("Total Correct=" + totalCorrect());
/* 1439 */     sb.append('\n');
/* 1440 */     sb.append("Total Accuracy=" + totalAccuracy());
/* 1441 */     sb.append('\n');
/* 1442 */     sb.append("95% Confidence Interval=" + totalAccuracy() + " +/- " + confidence95());
/*      */ 
/* 1444 */     sb.append('\n');
/* 1445 */     sb.append("Confusion Matrix\n");
/* 1446 */     sb.append("reference \\ response\n");
/* 1447 */     sb.append(matrixToCSV());
/* 1448 */     sb.append('\n');
/* 1449 */     sb.append("Macro-averaged Precision=" + macroAvgPrecision());
/* 1450 */     sb.append('\n');
/* 1451 */     sb.append("Macro-averaged Recall=" + macroAvgRecall());
/* 1452 */     sb.append('\n');
/* 1453 */     sb.append("Macro-averaged F=" + macroAvgFMeasure());
/* 1454 */     sb.append('\n');
/* 1455 */     sb.append("Micro-averaged Results\n");
/* 1456 */     sb.append("         the following symmetries are expected:\n");
/* 1457 */     sb.append("           TP=TN, FN=FP\n");
/* 1458 */     sb.append("           PosRef=PosResp=NegRef=NegResp\n");
/* 1459 */     sb.append("           Acc=Prec=Rec=F\n");
/* 1460 */     sb.append(microAverage().toString());
/* 1461 */     sb.append('\n');
/* 1462 */     sb.append("Random Accuracy=" + randomAccuracy());
/* 1463 */     sb.append('\n');
/* 1464 */     sb.append("Random Accuracy Unbiased=" + randomAccuracyUnbiased());
/* 1465 */     sb.append('\n');
/* 1466 */     sb.append("kappa=" + kappa());
/* 1467 */     sb.append('\n');
/* 1468 */     sb.append("kappa Unbiased=" + kappaUnbiased());
/* 1469 */     sb.append('\n');
/* 1470 */     sb.append("kappa No Prevalence =" + kappaNoPrevalence());
/* 1471 */     sb.append('\n');
/* 1472 */     sb.append("Reference Entropy=" + referenceEntropy());
/* 1473 */     sb.append('\n');
/* 1474 */     sb.append("Response Entropy=" + responseEntropy());
/* 1475 */     sb.append('\n');
/* 1476 */     sb.append("Cross Entropy=" + crossEntropy());
/* 1477 */     sb.append('\n');
/* 1478 */     sb.append("Joint Entropy=" + jointEntropy());
/* 1479 */     sb.append('\n');
/* 1480 */     sb.append("Conditional Entropy=" + conditionalEntropy());
/* 1481 */     sb.append('\n');
/* 1482 */     sb.append("Mutual Information=" + mutualInformation());
/* 1483 */     sb.append('\n');
/* 1484 */     sb.append("Kullback-Liebler Divergence=" + klDivergence());
/* 1485 */     sb.append('\n');
/* 1486 */     sb.append("chi Squared=" + chiSquared());
/* 1487 */     sb.append('\n');
/* 1488 */     sb.append("chi-Squared Degrees of Freedom=" + chiSquaredDegreesOfFreedom());
/*      */ 
/* 1490 */     sb.append('\n');
/* 1491 */     sb.append("phi Squared=" + phiSquared());
/* 1492 */     sb.append('\n');
/* 1493 */     sb.append("Cramer's V=" + cramersV());
/* 1494 */     sb.append('\n');
/* 1495 */     sb.append("lambda A=" + lambdaA());
/* 1496 */     sb.append('\n');
/* 1497 */     sb.append("lambda B=" + lambdaB());
/* 1498 */     sb.append('\n');
/*      */   }
/*      */ 
/*      */   String matrixToCSV()
/*      */   {
/* 1505 */     StringBuilder sb = new StringBuilder();
/*      */ 
/* 1509 */     sb.append("  ");
/* 1510 */     for (int i = 0; i < numCategories(); i++) {
/* 1511 */       sb.append(',');
/* 1512 */       sb.append(categories()[i]);
/*      */     }
/*      */ 
/* 1515 */     for (int i = 0; i < numCategories(); i++) {
/* 1516 */       sb.append("\n  ");
/* 1517 */       sb.append(categories()[i]);
/* 1518 */       for (int j = 0; j < numCategories(); j++) {
/* 1519 */         sb.append(',');
/* 1520 */         sb.append(count(i, j));
/*      */       }
/*      */     }
/* 1523 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   String matrixToHTML()
/*      */   {
/* 1530 */     StringBuilder sb = new StringBuilder();
/* 1531 */     sb.append("<html>\n");
/* 1532 */     sb.append("<table border='1' cellpadding='5'>");
/* 1533 */     sb.append('\n');
/* 1534 */     sb.append("<tr>\n  <td colspan='2' rowspan='2'>&nbsp;</td>");
/* 1535 */     sb.append("\n  <td colspan='" + numCategories() + "' align='center' bgcolor='darkgray'><b>Response</b></td></tr>");
/* 1536 */     sb.append("<tr>");
/* 1537 */     for (int i = 0; i < numCategories(); i++) {
/* 1538 */       sb.append("\n  <td align='right' bgcolor='lightgray'><i>" + categories()[i] + "</i></td>");
/*      */     }
/* 1540 */     sb.append("</tr>\n");
/* 1541 */     for (int i = 0; i < numCategories(); i++) {
/* 1542 */       sb.append("<tr>");
/* 1543 */       if (i == 0) sb.append("\n  <td rowspan='" + numCategories() + "' bgcolor='darkgray'><b>Ref-<br>erence</b></td>");
/* 1544 */       sb.append("\n  <td align='right' bgcolor='lightgray'><i>" + categories()[i] + "</i></td>");
/* 1545 */       for (int j = 0; j < numCategories(); j++) {
/* 1546 */         if (i == j)
/* 1547 */           sb.append("\n  <td align='right' bgcolor='lightgreen'>");
/* 1548 */         else if (count(i, j) == 0)
/* 1549 */           sb.append("\n  <td align='right' bgcolor='yellow'>");
/*      */         else {
/* 1551 */           sb.append("\n  <td align='right' bgcolor='red'>");
/*      */         }
/* 1553 */         sb.append(count(i, j));
/* 1554 */         sb.append("</td>");
/*      */       }
/* 1556 */       sb.append("</tr>\n");
/*      */     }
/* 1558 */     sb.append("</table>\n");
/* 1559 */     sb.append("</html>\n");
/* 1560 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   private void checkIndex(String argMsg, int index) {
/* 1564 */     if (index < 0) {
/* 1565 */       String msg = "Index for " + argMsg + " must be > 0." + " Found index=" + index;
/*      */ 
/* 1567 */       throw new IllegalArgumentException(msg);
/*      */     }
/* 1569 */     if (index >= numCategories()) {
/* 1570 */       String msg = "Index for " + argMsg + " must be < numCategories()=" + numCategories();
/*      */ 
/* 1572 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.ConfusionMatrix
 * JD-Core Version:    0.6.2
 */