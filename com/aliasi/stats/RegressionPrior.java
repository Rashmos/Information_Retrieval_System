/*      */ package com.aliasi.stats;
/*      */ 
/*      */ import com.aliasi.matrix.Vector;
/*      */ import com.aliasi.util.AbstractExternalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.Serializable;
/*      */ 
/*      */ public abstract class RegressionPrior
/*      */   implements Serializable
/*      */ {
/*      */   static final long serialVersionUID = 2955531646832969891L;
/*  493 */   static final double SQRT_2 = java.lang.Math.sqrt(2.0D);
/*      */ 
/*  495 */   private static final RegressionPrior NONINFORMATIVE_PRIOR = new NoninformativeRegressionPrior();
/*      */ 
/*  860 */   static final double sqrt2 = java.lang.Math.sqrt(2.0D);
/*  861 */   static final double log2Sqrt2Over2 = com.aliasi.util.Math.log2(sqrt2 / 2.0D);
/*  862 */   static final double log2Sqrt2Pi = com.aliasi.util.Math.log2(java.lang.Math.sqrt(6.283185307179586D));
/*      */ 
/*  864 */   static final double log21OverPi = -com.aliasi.util.Math.log2(3.141592653589793D);
/*      */ 
/*      */   public boolean isUniform()
/*      */   {
/*  409 */     return false;
/*      */   }
/*      */ 
/*      */   public double mode(int dimension)
/*      */   {
/*  421 */     return 0.0D;
/*      */   }
/*      */ 
/*      */   public abstract double gradient(double paramDouble, int paramInt);
/*      */ 
/*      */   public abstract double log2Prior(double paramDouble, int paramInt);
/*      */ 
/*      */   public double log2Prior(Vector beta)
/*      */   {
/*  462 */     int numDimensions = beta.numDimensions();
/*  463 */     verifyNumberOfDimensions(numDimensions);
/*  464 */     double log2Prior = 0.0D;
/*  465 */     for (int i = 0; i < numDimensions; i++)
/*  466 */       log2Prior += log2Prior(beta.value(i), i);
/*  467 */     return log2Prior;
/*      */   }
/*      */ 
/*      */   public double log2Prior(Vector[] betas)
/*      */   {
/*  481 */     double log2Prior = 0.0D;
/*  482 */     for (Vector beta : betas)
/*  483 */       log2Prior += log2Prior(beta);
/*  484 */     return log2Prior;
/*      */   }
/*      */ 
/*      */   void verifyNumberOfDimensions(int ignoreMeNumDimensions)
/*      */   {
/*      */   }
/*      */ 
/*      */   public static RegressionPrior noninformative()
/*      */   {
/*  506 */     return NONINFORMATIVE_PRIOR;
/*      */   }
/*      */ 
/*      */   public static RegressionPrior gaussian(double priorVariance, boolean noninformativeIntercept)
/*      */   {
/*  532 */     verifyPriorVariance(priorVariance);
/*  533 */     return new VariableGaussianRegressionPrior(priorVariance, noninformativeIntercept);
/*      */   }
/*      */ 
/*      */   public static RegressionPrior gaussian(double[] priorVariances)
/*      */   {
/*  552 */     verifyPriorVariances(priorVariances);
/*  553 */     return new GaussianRegressionPrior(priorVariances);
/*      */   }
/*      */ 
/*      */   public static RegressionPrior laplace(double priorVariance, boolean noninformativeIntercept)
/*      */   {
/*  580 */     verifyPriorVariance(priorVariance);
/*  581 */     return new VariableLaplaceRegressionPrior(priorVariance, noninformativeIntercept);
/*      */   }
/*      */ 
/*      */   public static RegressionPrior laplace(double[] priorVariances)
/*      */   {
/*  598 */     verifyPriorVariances(priorVariances);
/*  599 */     return new LaplaceRegressionPrior(priorVariances);
/*      */   }
/*      */ 
/*      */   public static RegressionPrior cauchy(double priorSquaredScale, boolean noninformativeIntercept)
/*      */   {
/*  620 */     verifyPriorVariance(priorSquaredScale);
/*  621 */     return new VariableCauchyRegressionPrior(priorSquaredScale, noninformativeIntercept);
/*      */   }
/*      */ 
/*      */   public static RegressionPrior cauchy(double[] priorSquaredScales)
/*      */   {
/*  636 */     verifyPriorVariances(priorSquaredScales);
/*  637 */     return new CauchyRegressionPrior(priorSquaredScales);
/*      */   }
/*      */ 
/*      */   public static RegressionPrior logInterpolated(double alpha, RegressionPrior prior1, RegressionPrior prior2)
/*      */   {
/*  657 */     if ((Double.isNaN(alpha)) || (alpha < 0.0D) || (alpha > 1.0D)) {
/*  658 */       String msg = "Weight of first prior must be between 0 and 1 inclusive. Found alpha=" + alpha;
/*      */ 
/*  660 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */ 
/*  663 */     return new LogInterpolatedRegressionPrior(alpha, prior1, prior2);
/*      */   }
/*      */ 
/*      */   public static RegressionPrior elasticNet(double laplaceWeight, double scale, boolean noninformativeIntercept)
/*      */   {
/*  695 */     if ((Double.isInfinite(scale)) || (scale <= 0.0D)) {
/*  696 */       String msg = "Scale parameter must be finite and positive. Found scale=" + scale;
/*      */ 
/*  698 */       throw new IllegalArgumentException(msg);
/*      */     }
/*  700 */     return logInterpolated(laplaceWeight, laplace(1.0D / java.lang.Math.sqrt(scale), noninformativeIntercept), gaussian(SQRT_2 / scale, noninformativeIntercept));
/*      */   }
/*      */ 
/*      */   public static RegressionPrior shiftMeans(double[] shifts, RegressionPrior prior)
/*      */   {
/*  718 */     return new ShiftMeans(shifts, prior);
/*      */   }
/*      */ 
/*      */   static void verifyPriorVariance(double priorVariance)
/*      */   {
/*  724 */     if ((priorVariance < 0.0D) || (Double.isNaN(priorVariance)) || (priorVariance == (-1.0D / 0.0D)))
/*      */     {
/*  728 */       String msg = "Prior variance must be a non-negative number. Found priorVariance=" + priorVariance;
/*      */ 
/*  730 */       throw new IllegalArgumentException(msg);
/*      */     }
/*      */   }
/*      */ 
/*      */   static void verifyPriorVariances(double[] priorVariances) {
/*  735 */     for (int i = 0; i < priorVariances.length; i++)
/*      */     {
/*  737 */       if ((priorVariances[i] < 0.0D) || (Double.isNaN(priorVariances[i])) || (priorVariances[i] == (-1.0D / 0.0D)))
/*      */       {
/*  741 */         String msg = "Prior variances must be non-negative numbers. Found priorVariances[" + i + "]=" + priorVariances[i];
/*      */ 
/*  743 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ShiftMeans extends RegressionPrior
/*      */   {
/*      */     static final long serialVersionUID = 5159543505446681732L;
/*      */     private final double[] mMeans;
/*      */     private final RegressionPrior mPrior;
/*      */ 
/*      */     ShiftMeans(double[] means, RegressionPrior prior)
/*      */     {
/* 1244 */       super();
/* 1245 */       this.mPrior = prior;
/* 1246 */       this.mMeans = means;
/*      */     }
/*      */ 
/*      */     public double mode(int i) {
/* 1250 */       return this.mMeans[i] + this.mPrior.mode(i);
/*      */     }
/*      */ 
/*      */     public boolean isUniform() {
/* 1254 */       return this.mPrior.isUniform();
/*      */     }
/*      */ 
/*      */     public double log2Prior(double betaI, int i) {
/* 1258 */       return this.mPrior.log2Prior(betaI - this.mMeans[i], i);
/*      */     }
/*      */ 
/*      */     public double gradient(double betaI, int i) {
/* 1262 */       return this.mPrior.gradient(betaI - this.mMeans[i], i);
/*      */     }
/*      */ 
/*      */     public String toString() {
/* 1266 */       return "ShiftMeans(means=...,prior=" + this.mPrior + ")";
/*      */     }
/*      */     static class Serializer extends AbstractExternalizable { static final long serialVersionUID = -777157399350907424L;
/*      */       final RegressionPrior.ShiftMeans mPrior;
/*      */ 
/* 1272 */       public Serializer() { this(null); }
/*      */ 
/*      */       public Serializer(RegressionPrior.ShiftMeans prior) {
/* 1275 */         this.mPrior = prior;
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/* 1279 */         writeDoubles(this.mPrior.mMeans, out);
/* 1280 */         out.writeObject(this.mPrior.mPrior);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in)
/*      */         throws IOException, ClassNotFoundException
/*      */       {
/* 1286 */         double[] means = readDoubles(in);
/*      */ 
/* 1288 */         RegressionPrior prior = (RegressionPrior)in.readObject();
/* 1289 */         return new RegressionPrior.ShiftMeans(means, prior);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LogInterpolatedRegressionPrior extends RegressionPrior
/*      */   {
/*      */     static final long serialVersionUID = 1052451778773339516L;
/*      */     private final double mAlpha;
/*      */     private final RegressionPrior mPrior1;
/*      */     private final RegressionPrior mPrior2;
/*      */ 
/*      */     LogInterpolatedRegressionPrior(double alpha, RegressionPrior prior1, RegressionPrior prior2)
/*      */     {
/* 1184 */       super();
/* 1185 */       this.mAlpha = alpha;
/* 1186 */       this.mPrior1 = prior1;
/* 1187 */       this.mPrior2 = prior2;
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension) {
/* 1191 */       return this.mAlpha * this.mPrior1.gradient(beta, dimension) + (1.0D - this.mAlpha) * this.mPrior2.gradient(beta, dimension);
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension)
/*      */     {
/* 1196 */       return this.mAlpha * this.mPrior1.log2Prior(beta, dimension) + (1.0D - this.mAlpha) * this.mPrior2.log2Prior(beta, dimension);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1201 */       return "LogInterpolatedRegressionPrior(alpha=" + this.mAlpha + ", prior1=" + this.mPrior1 + ", prior2=" + this.mPrior2 + ")";
/*      */     }
/*      */ 
/*      */     Object writeReplace()
/*      */     {
/* 1207 */       return new Serializer(this);
/*      */     }
/*      */     static class Serializer extends AbstractExternalizable { static final long serialVersionUID = 1071183663202516816L;
/*      */       final RegressionPrior.LogInterpolatedRegressionPrior mPrior;
/*      */ 
/* 1213 */       public Serializer() { this(null); }
/*      */ 
/*      */       public Serializer(RegressionPrior.LogInterpolatedRegressionPrior prior) {
/* 1216 */         this.mPrior = prior;
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/* 1220 */         out.writeDouble(this.mPrior.mAlpha);
/* 1221 */         out.writeObject(this.mPrior.mPrior1);
/* 1222 */         out.writeObject(this.mPrior.mPrior2);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in)
/*      */         throws IOException, ClassNotFoundException
/*      */       {
/* 1228 */         double alpha = in.readDouble();
/*      */ 
/* 1230 */         RegressionPrior prior1 = (RegressionPrior)in.readObject();
/*      */ 
/* 1232 */         RegressionPrior prior2 = (RegressionPrior)in.readObject();
/* 1233 */         return new RegressionPrior.LogInterpolatedRegressionPrior(alpha, prior1, prior2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class VariableCauchyRegressionPrior extends RegressionPrior.VariableRegressionPrior
/*      */   {
/*      */     static final long serialVersionUID = 3368658136325392652L;
/*      */ 
/*      */     VariableCauchyRegressionPrior(double priorVariance, boolean noninformativeIntercept)
/*      */     {
/* 1127 */       super(noninformativeIntercept);
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension) {
/* 1131 */       return (dimension == 0) && (this.mNoninformativeIntercept) ? 0.0D : 2.0D * beta / (beta * beta + this.mPriorVariance);
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension)
/*      */     {
/* 1137 */       if ((dimension == 0) && (this.mNoninformativeIntercept))
/* 1138 */         return 0.0D;
/* 1139 */       return log21OverPi + 0.5D * com.aliasi.util.Math.log2(this.mPriorVariance) - com.aliasi.util.Math.log2(beta * beta + this.mPriorVariance);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1145 */       return toString("CauchyRegressionPrior", "Scale");
/*      */     }
/*      */     public Object writeReplace() {
/* 1148 */       return new Serializer(this);
/*      */     }
/*      */     private static class Serializer extends AbstractExternalizable { static final long serialVersionUID = -7209096281888148303L;
/*      */       final RegressionPrior.VariableCauchyRegressionPrior mPrior;
/*      */ 
/* 1154 */       public Serializer(RegressionPrior.VariableCauchyRegressionPrior prior) { this.mPrior = prior; }
/*      */ 
/*      */       public Serializer() {
/* 1157 */         this(null);
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/* 1161 */         out.writeDouble(this.mPrior.mPriorVariance);
/* 1162 */         out.writeBoolean(this.mPrior.mNoninformativeIntercept);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in)
/*      */         throws IOException, ClassNotFoundException
/*      */       {
/* 1168 */         double priorScale = in.readDouble();
/* 1169 */         boolean noninformativeIntercept = in.readBoolean();
/* 1170 */         return new RegressionPrior.VariableCauchyRegressionPrior(priorScale, noninformativeIntercept);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class VariableLaplaceRegressionPrior extends RegressionPrior.VariableRegressionPrior
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = -4286001162222250623L;
/*      */     final double mPositiveGradient;
/*      */     final double mNegativeGradient;
/*      */     final double mPriorIntercept;
/*      */     final double mPriorCoefficient;
/*      */ 
/*      */     VariableLaplaceRegressionPrior(double priorVariance, boolean noninformativeIntercept)
/*      */     {
/* 1063 */       super(noninformativeIntercept);
/* 1064 */       this.mPositiveGradient = java.lang.Math.sqrt(2.0D / priorVariance);
/* 1065 */       this.mNegativeGradient = (-this.mPositiveGradient);
/* 1066 */       this.mPriorIntercept = (log2Sqrt2Over2 - 0.5D * com.aliasi.util.Math.log2(priorVariance));
/*      */ 
/* 1069 */       this.mPriorCoefficient = (-sqrt2 / java.lang.Math.sqrt(priorVariance));
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension) {
/* 1073 */       return beta > 0.0D ? this.mPositiveGradient : ((dimension == 0) && (this.mNoninformativeIntercept)) || (beta == 0.0D) ? 0.0D : this.mNegativeGradient;
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension)
/*      */     {
/* 1081 */       if ((dimension == 0) && (this.mNoninformativeIntercept))
/* 1082 */         return 0.0D;
/* 1083 */       return this.mPriorIntercept + this.mPriorCoefficient * java.lang.Math.abs(beta);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1091 */       return toString("LaplaceRegressionPrior", "Variance");
/*      */     }
/*      */     private Object writeReplace() {
/* 1094 */       return new Serializer(this);
/*      */     }
/*      */     private static class Serializer extends AbstractExternalizable { static final long serialVersionUID = 2321796089407881776L;
/*      */       final RegressionPrior.VariableLaplaceRegressionPrior mPrior;
/*      */ 
/* 1100 */       public Serializer(RegressionPrior.VariableLaplaceRegressionPrior prior) { this.mPrior = prior; }
/*      */ 
/*      */       public Serializer() {
/* 1103 */         this(null);
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/* 1107 */         out.writeDouble(this.mPrior.mPriorVariance);
/* 1108 */         out.writeBoolean(this.mPrior.mNoninformativeIntercept);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in)
/*      */         throws IOException, ClassNotFoundException
/*      */       {
/* 1114 */         double priorVariance = in.readDouble();
/* 1115 */         boolean noninformativeIntercept = in.readBoolean();
/* 1116 */         return new RegressionPrior.VariableLaplaceRegressionPrior(priorVariance, noninformativeIntercept);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class VariableGaussianRegressionPrior extends RegressionPrior.VariableRegressionPrior
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = -7527207309328127863L;
/*      */ 
/*      */     VariableGaussianRegressionPrior(double priorVariance, boolean noninformativeIntercept)
/*      */     {
/* 1002 */       super(noninformativeIntercept);
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension) {
/* 1006 */       return (dimension == 0) && (this.mNoninformativeIntercept) ? 0.0D : beta / this.mPriorVariance;
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension)
/*      */     {
/* 1012 */       if ((dimension == 0) && (this.mNoninformativeIntercept))
/* 1013 */         return 0.0D;
/* 1014 */       return -log2Sqrt2Pi - 0.5D * com.aliasi.util.Math.log2(this.mPriorVariance) - beta * beta / (2.0D * this.mPriorVariance);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1021 */       return toString("GaussianRegressionPrior", "Variance");
/*      */     }
/*      */     private Object writeReplace() {
/* 1024 */       return new Serializer(this);
/*      */     }
/*      */     private static class Serializer extends AbstractExternalizable { static final long serialVersionUID = 5979483825025936160L;
/*      */       final RegressionPrior.VariableGaussianRegressionPrior mPrior;
/*      */ 
/* 1030 */       public Serializer(RegressionPrior.VariableGaussianRegressionPrior prior) { this.mPrior = prior; }
/*      */ 
/*      */       public Serializer() {
/* 1033 */         this(null);
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/* 1037 */         out.writeDouble(this.mPrior.mPriorVariance);
/* 1038 */         out.writeBoolean(this.mPrior.mNoninformativeIntercept);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in)
/*      */         throws IOException, ClassNotFoundException
/*      */       {
/* 1044 */         double priorVariance = in.readDouble();
/* 1045 */         boolean noninformativeIntercept = in.readBoolean();
/* 1046 */         return new RegressionPrior.VariableGaussianRegressionPrior(priorVariance, noninformativeIntercept);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class VariableRegressionPrior extends RegressionPrior
/*      */   {
/*      */     static final long serialVersionUID = -7527207309328127863L;
/*      */     final double mPriorVariance;
/*      */     final boolean mNoninformativeIntercept;
/*      */ 
/*      */     VariableRegressionPrior(double priorVariance, boolean noninformativeIntercept)
/*      */     {
/*  986 */       super();
/*  987 */       this.mPriorVariance = priorVariance;
/*  988 */       this.mNoninformativeIntercept = noninformativeIntercept;
/*      */     }
/*      */     public String toString(String priorName, String paramName) {
/*  991 */       return priorName + "(" + paramName + "=" + this.mPriorVariance + ", noninformativeIntercept=" + this.mNoninformativeIntercept + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CauchyRegressionPrior extends RegressionPrior.ArrayRegressionPrior
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = 2351846943518745614L;
/*      */ 
/*      */     CauchyRegressionPrior(double[] priorSquaredScales)
/*      */     {
/*  930 */       super();
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension) {
/*  934 */       return 2.0D * beta / (beta * beta + this.mValues[dimension]);
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension) {
/*  938 */       return log21OverPi + 0.5D * com.aliasi.util.Math.log2(this.mValues[dimension]) - com.aliasi.util.Math.log2(beta * beta + this.mValues[dimension] * this.mValues[dimension]);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  945 */       return toString("CauchyRegressionPrior", "Scale");
/*      */     }
/*      */     private Object writeReplace() {
/*  948 */       return new Serializer(this);
/*      */     }
/*      */     private static class Serializer extends AbstractExternalizable { static final long serialVersionUID = 5202676106810759907L;
/*      */       final RegressionPrior.CauchyRegressionPrior mPrior;
/*      */ 
/*  954 */       public Serializer(RegressionPrior.CauchyRegressionPrior prior) { this.mPrior = prior; }
/*      */ 
/*      */       public Serializer() {
/*  957 */         this(null);
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/*  961 */         out.writeInt(this.mPrior.mValues.length);
/*  962 */         for (int i = 0; i < this.mPrior.mValues.length; i++)
/*  963 */           out.writeDouble(this.mPrior.mValues[i]);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in)
/*      */         throws IOException, ClassNotFoundException
/*      */       {
/*  969 */         int numDimensions = in.readInt();
/*  970 */         double[] priorScales = new double[numDimensions];
/*  971 */         for (int i = 0; i < numDimensions; i++)
/*  972 */           priorScales[i] = in.readDouble();
/*  973 */         return new RegressionPrior.CauchyRegressionPrior(priorScales);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LaplaceRegressionPrior extends RegressionPrior.ArrayRegressionPrior
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = 9120480132502062861L;
/*      */ 
/*      */     LaplaceRegressionPrior(double[] priorVariances)
/*      */     {
/*  873 */       super();
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension) {
/*  877 */       if (beta == 0.0D) return 0.0D;
/*  878 */       if (beta > 0.0D)
/*  879 */         return java.lang.Math.sqrt(2.0D / this.mValues[dimension]);
/*  880 */       return -java.lang.Math.sqrt(2.0D / this.mValues[dimension]);
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension) {
/*  884 */       return log2Sqrt2Over2 - 0.5D * com.aliasi.util.Math.log2(this.mValues[dimension]) - sqrt2 * java.lang.Math.abs(beta) / java.lang.Math.sqrt(this.mValues[dimension]);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  890 */       return toString("LaplaceRegressionPrior", "Variance");
/*      */     }
/*      */     private Object writeReplace() {
/*  893 */       return new Serializer(this);
/*      */     }
/*      */     private static class Serializer extends AbstractExternalizable { static final long serialVersionUID = 7844951573062416091L;
/*      */       final RegressionPrior.LaplaceRegressionPrior mPrior;
/*      */ 
/*  899 */       public Serializer(RegressionPrior.LaplaceRegressionPrior prior) { this.mPrior = prior; }
/*      */ 
/*      */       public Serializer() {
/*  902 */         this(null);
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/*  906 */         out.writeInt(this.mPrior.mValues.length);
/*  907 */         for (int i = 0; i < this.mPrior.mValues.length; i++)
/*  908 */           out.writeDouble(this.mPrior.mValues[i]);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in)
/*      */         throws IOException, ClassNotFoundException
/*      */       {
/*  914 */         int numDimensions = in.readInt();
/*  915 */         double[] priorVariances = new double[numDimensions];
/*  916 */         for (int i = 0; i < numDimensions; i++)
/*  917 */           priorVariances[i] = in.readDouble();
/*  918 */         return new RegressionPrior.LaplaceRegressionPrior(priorVariances);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class GaussianRegressionPrior extends RegressionPrior.ArrayRegressionPrior
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = 8257747607648390037L;
/*      */ 
/*      */     GaussianRegressionPrior(double[] priorVariances)
/*      */     {
/*  813 */       super();
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension) {
/*  817 */       return beta / this.mValues[dimension];
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension) {
/*  821 */       return -log2Sqrt2Pi - 0.5D * com.aliasi.util.Math.log2(this.mValues[dimension]) - beta * beta / (2.0D * this.mValues[dimension]);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  827 */       return toString("GaussianRegressionPrior", "Variance");
/*      */     }
/*      */     private Object writeReplace() {
/*  830 */       return new Serializer(this);
/*      */     }
/*      */     private static class Serializer extends AbstractExternalizable {
/*      */       static final long serialVersionUID = -1129377549371296060L;
/*      */       final RegressionPrior.GaussianRegressionPrior mPrior;
/*      */ 
/*      */       public Serializer(RegressionPrior.GaussianRegressionPrior prior) {
/*  838 */         this.mPrior = prior;
/*      */       }
/*      */       public Serializer() {
/*  841 */         this(null);
/*      */       }
/*      */ 
/*      */       public void writeExternal(ObjectOutput out) throws IOException {
/*  845 */         out.writeInt(this.mPrior.mValues.length);
/*  846 */         for (int i = 0; i < this.mPrior.mValues.length; i++)
/*  847 */           out.writeDouble(this.mPrior.mValues[i]);
/*      */       }
/*      */ 
/*      */       public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
/*  851 */         int numDimensions = in.readInt();
/*  852 */         double[] priorVariances = new double[numDimensions];
/*  853 */         for (int i = 0; i < numDimensions; i++)
/*  854 */           priorVariances[i] = in.readDouble();
/*  855 */         return new RegressionPrior.GaussianRegressionPrior(priorVariances);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static abstract class ArrayRegressionPrior extends RegressionPrior
/*      */   {
/*      */     static final long serialVersionUID = -1887383164794837169L;
/*      */     final double[] mValues;
/*      */ 
/*      */     ArrayRegressionPrior(double[] values)
/*      */     {
/*  784 */       super();
/*  785 */       this.mValues = values;
/*      */     }
/*      */ 
/*      */     void verifyNumberOfDimensions(int numDimensions) {
/*  789 */       if (this.mValues.length != numDimensions) {
/*  790 */         String msg = "Prior and instances must match in number of dimensions. Found prior numDimensions=" + this.mValues.length + " instance numDimensions=" + numDimensions;
/*      */ 
/*  793 */         throw new IllegalArgumentException(msg);
/*      */       }
/*      */     }
/*      */ 
/*  797 */     public String toString(String priorName, String paramName) { StringBuilder sb = new StringBuilder();
/*  798 */       sb.append(priorName + "\n");
/*  799 */       sb.append("     dimensionality=" + this.mValues.length);
/*  800 */       for (int i = 0; i < this.mValues.length; i++)
/*  801 */         sb.append("     " + paramName + "[" + i + "]=" + this.mValues[i] + "\n");
/*  802 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class NoninformativeRegressionPrior extends RegressionPrior
/*      */     implements Serializable
/*      */   {
/*      */     static final long serialVersionUID = -582012445093979284L;
/*      */ 
/*      */     NoninformativeRegressionPrior()
/*      */     {
/*  748 */       super();
/*      */     }
/*      */ 
/*      */     public double gradient(double beta, int dimension)
/*      */     {
/*  756 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     public double log2Prior(double beta, int dimension) {
/*  760 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     public double log2Prior(Vector beta) {
/*  764 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     public double log2Prior(Vector[] betas) {
/*  768 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  772 */       return "NoninformativeRegressionPrior";
/*      */     }
/*      */ 
/*      */     public boolean isUniform() {
/*  776 */       return true;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.RegressionPrior
 * JD-Core Version:    0.6.2
 */