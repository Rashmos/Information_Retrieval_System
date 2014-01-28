/*      */ package com.aliasi.matrix;
/*      */ 
/*      */ import com.aliasi.io.Reporter;
/*      */ import com.aliasi.io.Reporters;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ 
/*      */ public class SvdMatrix extends AbstractMatrix
/*      */ {
/*      */   private final double[][] mRowVectors;
/*      */   private final double[][] mColumnVectors;
/*      */   private final int mOrder;
/* 1170 */   static final double[][] EMPTY_DOUBLE_2D_ARRAY = new double[0][];
/*      */ 
/*      */   public SvdMatrix(double[][] rowVectors, double[][] columnVectors, int order)
/*      */   {
/*  538 */     verifyDimensions("row", order, rowVectors);
/*  539 */     verifyDimensions("column", order, columnVectors);
/*  540 */     this.mRowVectors = rowVectors;
/*  541 */     this.mColumnVectors = columnVectors;
/*  542 */     this.mOrder = order;
/*      */   }
/*      */ 
/*      */   public SvdMatrix(double[][] rowSingularVectors, double[][] columnSingularVectors, double[] singularValues)
/*      */   {
/*  561 */     this.mOrder = singularValues.length;
/*  562 */     verifyDimensions("row", this.mOrder, rowSingularVectors);
/*  563 */     verifyDimensions("column", this.mOrder, columnSingularVectors);
/*      */ 
/*  565 */     this.mRowVectors = new double[rowSingularVectors.length][this.mOrder];
/*  566 */     this.mColumnVectors = new double[columnSingularVectors.length][this.mOrder];
/*  567 */     double[] sqrtSingularValues = new double[singularValues.length];
/*  568 */     for (int i = 0; i < sqrtSingularValues.length; i++)
/*  569 */       sqrtSingularValues[i] = Math.sqrt(singularValues[i]);
/*  570 */     scale(this.mRowVectors, rowSingularVectors, sqrtSingularValues);
/*  571 */     scale(this.mColumnVectors, columnSingularVectors, sqrtSingularValues);
/*      */   }
/*      */ 
/*      */   public int numRows()
/*      */   {
/*  581 */     return this.mRowVectors.length;
/*      */   }
/*      */ 
/*      */   public int numColumns()
/*      */   {
/*  591 */     return this.mColumnVectors.length;
/*      */   }
/*      */ 
/*      */   public int order()
/*      */   {
/*  602 */     return this.mRowVectors[0].length;
/*      */   }
/*      */ 
/*      */   public double value(int row, int column)
/*      */   {
/*  615 */     double[] rowVec = this.mRowVectors[row];
/*  616 */     double[] colVec = this.mColumnVectors[column];
/*  617 */     double result = 0.0D;
/*  618 */     for (int i = 0; i < rowVec.length; i++)
/*  619 */       result += rowVec[i] * colVec[i];
/*  620 */     return result;
/*      */   }
/*      */ 
/*      */   public double[] singularValues()
/*      */   {
/*  629 */     double[] singularValues = new double[this.mOrder];
/*  630 */     for (int i = 0; i < singularValues.length; i++)
/*  631 */       singularValues[i] = singularValue(i);
/*  632 */     return singularValues;
/*      */   }
/*      */ 
/*      */   public double singularValue(int order)
/*      */   {
/*  642 */     if (order >= this.mOrder) {
/*  643 */       String msg = "Maximum order=" + (this.mOrder - 1) + " found order=" + order;
/*      */ 
/*  645 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  647 */     return columnLength(this.mRowVectors, order) * columnLength(this.mColumnVectors, order);
/*      */   }
/*      */ 
/*      */   public double[][] leftSingularVectors()
/*      */   {
/*  662 */     return normalizeColumns(this.mRowVectors);
/*      */   }
/*      */ 
/*      */   public double[][] rightSingularVectors()
/*      */   {
/*  677 */     return normalizeColumns(this.mColumnVectors);
/*      */   }
/*      */ 
/*      */   public static SvdMatrix svd(double[][] values, int maxOrder, double featureInit, double initialLearningRate, double annealingRate, double regularization, Reporter reporter, double minImprovement, int minEpochs, int maxEpochs)
/*      */   {
/*  734 */     if (reporter == null) {
/*  735 */       reporter = Reporters.silent();
/*      */     }
/*  737 */     int m = values.length;
/*  738 */     int n = values[0].length;
/*  739 */     reporter.info("Calculating SVD");
/*  740 */     reporter.info("#Rows=" + m + " #Cols=" + n);
/*      */ 
/*  743 */     for (int i = 1; i < m; i++) {
/*  744 */       if (values[i].length != n) {
/*  745 */         String msg = "All rows must be of same length. Found row[0].length=" + n + " row[" + i + "]=" + values[i].length;
/*      */ 
/*  748 */         reporter.fatal(msg);
/*  749 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  754 */     int[] sharedRow = new int[n];
/*  755 */     for (int j = 0; j < n; j++)
/*  756 */       sharedRow[j] = j;
/*  757 */     int[][] columnIds = new int[m][];
/*  758 */     for (int j = 0; j < m; j++) {
/*  759 */       columnIds[j] = sharedRow;
/*      */     }
/*  761 */     return partialSvd(columnIds, values, maxOrder, featureInit, initialLearningRate, annealingRate, regularization, reporter, minImprovement, minEpochs, maxEpochs);
/*      */   }
/*      */ 
/*      */   public static SvdMatrix partialSvd(int[][] columnIds, double[][] values, int maxOrder, double featureInit, double initialLearningRate, double annealingRate, double regularization, Reporter reporter, double minImprovement, int minEpochs, int maxEpochs)
/*      */   {
/*  856 */     return partialSvd(columnIds, values, maxOrder, featureInit, initialLearningRate, annealingRate, regularization, new Random(), reporter, minImprovement, minEpochs, maxEpochs);
/*      */   }
/*      */ 
/*      */   static SvdMatrix partialSvd(int[][] columnIds, double[][] values, int maxOrder, double featureInit, double initialLearningRate, double annealingRate, double regularization, Random random, Reporter reporter, double minImprovement, int minEpochs, int maxEpochs)
/*      */   {
/*  909 */     if (reporter == null)
/*  910 */       reporter = Reporters.silent();
/*  911 */     reporter.info("Start");
/*  912 */     if (maxOrder < 1) {
/*  913 */       String msg = "Max order must be >= 1. Found maxOrder=" + maxOrder;
/*      */ 
/*  915 */       reporter.fatal(msg);
/*  916 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  918 */     if ((minImprovement < 0.0D) || (notFinite(minImprovement))) {
/*  919 */       String msg = "Min improvement must be finite and non-negative. Found minImprovement=" + minImprovement;
/*      */ 
/*  921 */       reporter.fatal(msg);
/*  922 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  924 */     if ((minEpochs <= 0) || (maxEpochs < minEpochs)) {
/*  925 */       String msg = "Min epochs must be non-negative and less than or equal to max epochs. found minEpochs=" + minEpochs + " maxEpochs=" + maxEpochs;
/*      */ 
/*  928 */       reporter.fatal(msg);
/*  929 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  931 */     if ((notFinite(featureInit)) || (featureInit == 0.0D)) {
/*  932 */       String msg = "Feature inits must be finite and non-zero. Found featureInit=" + featureInit;
/*      */ 
/*  934 */       reporter.fatal(msg);
/*  935 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  937 */     if ((notFinite(initialLearningRate)) || (initialLearningRate < 0.0D)) {
/*  938 */       String msg = "Initial learning rate must be finite and non-negative. Found initialLearningRate=" + initialLearningRate;
/*      */ 
/*  940 */       reporter.fatal(msg);
/*  941 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  943 */     if ((notFinite(regularization)) || (regularization < 0.0D)) {
/*  944 */       String msg = "Regularization must be finite and non-negative. Found regularization=" + regularization;
/*      */ 
/*  946 */       reporter.fatal(msg);
/*  947 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  949 */     for (int row = 0; row < columnIds.length; row++) {
/*  950 */       if (columnIds == null) {
/*  951 */         String msg = "ColumnIds must not be null.";
/*  952 */         reporter.fatal(msg);
/*  953 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  955 */       if (values == null) {
/*  956 */         String msg = "Values must not be null";
/*  957 */         reporter.fatal(msg);
/*  958 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  960 */       if (columnIds[row] == null) {
/*  961 */         String msg = "All column Ids must be non-null. Found null in row=" + row;
/*      */ 
/*  963 */         reporter.fatal(msg);
/*  964 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  966 */       if (values[row] == null) {
/*  967 */         String msg = "All values must be non-null. Found null row=" + row;
/*      */ 
/*  969 */         reporter.fatal(msg);
/*  970 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  972 */       if (columnIds[row].length != values[row].length) {
/*  973 */         String msg = "column Ids and values must be same length. For row=" + row + " Found columnIds[row].length=" + columnIds[row].length + " Found values[row].length=" + values[row].length;
/*      */ 
/*  977 */         reporter.fatal(msg);
/*  978 */         throw new IllegalArgumentException(msg);
/*      */       }
/*  980 */       for (int i = 0; i < columnIds[row].length; i++) {
/*  981 */         if (columnIds[row][i] < 0) {
/*  982 */           String msg = "Column ids must be non-negative. Found columnIds[" + row + "][" + i + "]=" + columnIds[row][i];
/*      */ 
/*  984 */           reporter.fatal(msg);
/*  985 */           throw new IllegalArgumentException(msg);
/*      */         }
/*  987 */         if ((i > 0) && (columnIds[row][(i - 1)] >= columnIds[row][i])) {
/*  988 */           String msg = "All column Ids must be same length. At row=" + row + " Mismatch at rows " + i + " and " + (i - 1);
/*      */ 
/*  991 */           reporter.fatal(msg);
/*  992 */           throw new IllegalArgumentException(msg);
/*      */         }
/*      */       }
/*      */     }
/*  996 */     if ((annealingRate < 0.0D) || (notFinite(annealingRate))) {
/*  997 */       String msg = "Annealing rate must be finite and non-negative. Found rate=" + annealingRate;
/*      */ 
/*  999 */       reporter.fatal(msg);
/* 1000 */       throw new IllegalArgumentException("14");
/*      */     }
/*      */ 
/* 1003 */     int numRows = columnIds.length;
/*      */ 
/* 1005 */     int numEntries = 0;
/* 1006 */     for (double[] xs : values) {
/* 1007 */       numEntries += xs.length;
/*      */     }
/* 1009 */     int maxColumnIndex = 0;
/* 1010 */     for (int[] xs : columnIds)
/* 1011 */       for (int i = 0; i < xs.length; i++)
/* 1012 */         if (xs[i] > maxColumnIndex)
/* 1013 */           maxColumnIndex = xs[i];
/* 1014 */     int numColumns = maxColumnIndex + 1;
/*      */ 
/* 1016 */     maxOrder = Math.min(maxOrder, Math.min(numRows, numColumns));
/*      */ 
/* 1018 */     double[][] cache = new double[values.length][];
/* 1019 */     for (int row = 0; row < numRows; row++) {
/* 1020 */       cache[row] = new double[values[row].length];
/* 1021 */       Arrays.fill(cache[row], 0.0D);
/*      */     }
/*      */ 
/* 1024 */     List rowVectorList = new ArrayList(maxOrder);
/* 1025 */     List columnVectorList = new ArrayList(maxOrder);
/* 1026 */     for (int order = 0; order < maxOrder; order++) {
/* 1027 */       reporter.info("  Factor=" + order);
/* 1028 */       double[] rowVector = initArray(numRows, featureInit, random);
/* 1029 */       double[] columnVector = initArray(numColumns, featureInit, random);
/* 1030 */       double rmseLast = (1.0D / 0.0D);
/* 1031 */       for (int epoch = 0; epoch < maxEpochs; epoch++) {
/* 1032 */         double learningRateForEpoch = initialLearningRate / (1.0D + epoch / annealingRate);
/* 1033 */         double sumOfSquareErrors = 0.0D;
/* 1034 */         for (int row = 0; row < numRows; row++) {
/* 1035 */           int[] columnIdsForRow = columnIds[row];
/* 1036 */           double[] valuesForRow = values[row];
/* 1037 */           double[] cacheForRow = cache[row];
/* 1038 */           for (int i = 0; i < columnIdsForRow.length; i++) {
/* 1039 */             int column = columnIdsForRow[i];
/* 1040 */             double prediction = predict(row, column, rowVector, columnVector, cacheForRow[i]);
/*      */ 
/* 1043 */             double error = valuesForRow[i] - prediction;
/*      */ 
/* 1045 */             sumOfSquareErrors += error * error;
/*      */ 
/* 1047 */             double rowCurrent = rowVector[row];
/* 1048 */             double columnCurrent = columnVector[column];
/*      */ 
/* 1050 */             rowVector[row] += learningRateForEpoch * (error * columnCurrent - regularization * rowCurrent);
/*      */ 
/* 1053 */             columnVector[column] += learningRateForEpoch * (error * rowCurrent - regularization * columnCurrent);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1058 */         double rmse = Math.sqrt(sumOfSquareErrors / numEntries);
/* 1059 */         reporter.info("    epoch=" + epoch + " rmse=" + rmse);
/* 1060 */         if ((epoch >= minEpochs) && (relativeDifference(rmse, rmseLast) < minImprovement)) {
/* 1061 */           reporter.info("Converged in epoch=" + epoch + " rmse=" + rmse + " relDiff=" + relativeDifference(rmse, rmseLast));
/*      */ 
/* 1064 */           break;
/*      */         }
/* 1066 */         rmseLast = rmse;
/*      */       }
/* 1068 */       reporter.info("Order=" + order + " RMSE=" + rmseLast);
/* 1069 */       rowVectorList.add(rowVector);
/* 1070 */       columnVectorList.add(columnVector);
/*      */ 
/* 1072 */       for (int row = 0; row < cache.length; row++) {
/* 1073 */         double[] cacheRow = cache[row];
/* 1074 */         for (int i = 0; i < cacheRow.length; i++) {
/* 1075 */           cacheRow[i] = predict(row, columnIds[row][i], rowVector, columnVector, cacheRow[i]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1082 */     double[][] rowVectors = (double[][])rowVectorList.toArray(EMPTY_DOUBLE_2D_ARRAY);
/* 1083 */     double[][] columnVectors = (double[][])columnVectorList.toArray(EMPTY_DOUBLE_2D_ARRAY);
/*      */ 
/* 1085 */     return new SvdMatrix(transpose(rowVectors), transpose(columnVectors), maxOrder);
/*      */   }
/*      */ 
/*      */   static double relativeDifference(double x, double y)
/*      */   {
/* 1093 */     return Math.abs(x - y) / (Math.abs(x) + Math.abs(y));
/*      */   }
/*      */ 
/*      */   static double[][] transpose(double[][] xs) {
/* 1097 */     double[][] ys = new double[xs[0].length][xs.length];
/* 1098 */     for (int i = 0; i < xs.length; i++)
/* 1099 */       for (int j = 0; j < xs[i].length; j++)
/* 1100 */         ys[j][i] = xs[i][j];
/* 1101 */     return ys;
/*      */   }
/*      */ 
/*      */   static double predict(int row, int column, double[] rowVector, double[] columnVector, double cache)
/*      */   {
/* 1107 */     return cache + rowVector[row] * columnVector[column];
/*      */   }
/*      */ 
/*      */   static double[] initArray(int size, double val, Random random) {
/* 1111 */     double[] xs = new double[size];
/*      */ 
/* 1114 */     for (int i = 0; i < xs.length; i++) {
/* 1115 */       xs[i] = (random.nextGaussian() * val);
/*      */     }
/* 1117 */     return xs;
/*      */   }
/*      */ 
/*      */   static boolean notFinite(double x) {
/* 1121 */     return (Double.isNaN(x)) || (Double.isInfinite(x));
/*      */   }
/*      */ 
/*      */   static double columnLength(double[][] xs, int col) {
/* 1125 */     double sumOfSquares = 0.0D;
/* 1126 */     for (int i = 0; i < xs.length; i++)
/* 1127 */       sumOfSquares += xs[i][col] * xs[i][col];
/* 1128 */     return Math.sqrt(sumOfSquares);
/*      */   }
/*      */ 
/*      */   static void scale(double[][] vecs, double[][] singularVecs, double[] singularVals)
/*      */   {
/* 1134 */     for (int i = 0; i < vecs.length; i++)
/* 1135 */       for (int k = 0; k < vecs[i].length; k++)
/* 1136 */         singularVecs[i][k] *= singularVals[k];
/*      */   }
/*      */ 
/*      */   static void verifyDimensions(String prefix, int order, double[][] vectors) {
/* 1140 */     for (int i = 0; i < vectors.length; i++)
/* 1141 */       if (vectors[i].length != order) {
/* 1142 */         String msg = "All vectors must have length equal to order. order=" + order + " " + prefix + "Vectors[" + i + "].length=" + vectors[i].length;
/*      */ 
/* 1146 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */   }
/*      */ 
/*      */   static double[][] normalizeColumns(double[][] xs)
/*      */   {
/* 1153 */     int numDims = xs.length;
/* 1154 */     int order = xs[0].length;
/* 1155 */     double[][] result = new double[numDims][order];
/* 1156 */     for (int j = 0; j < order; j++) {
/* 1157 */       double sumOfSquares = 0.0D;
/* 1158 */       for (int i = 0; i < numDims; i++) {
/* 1159 */         double valIJ = xs[i][j];
/* 1160 */         result[i][j] = valIJ;
/* 1161 */         sumOfSquares += valIJ * valIJ;
/*      */       }
/* 1163 */       double length = Math.sqrt(sumOfSquares);
/* 1164 */       for (int i = 0; i < numDims; i++)
/* 1165 */         result[i][j] /= length;
/*      */     }
/* 1167 */     return result;
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.SvdMatrix
 * JD-Core Version:    0.6.2
 */