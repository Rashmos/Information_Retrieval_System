/*     */ package com.aliasi.dca;
/*     */ 
/*     */ import com.aliasi.io.Reporter;
/*     */ import com.aliasi.io.Reporters;
/*     */ import com.aliasi.matrix.DenseVector;
/*     */ import com.aliasi.matrix.Matrices;
/*     */ import com.aliasi.matrix.Vector;
/*     */ import com.aliasi.stats.AnnealingSchedule;
/*     */ import com.aliasi.stats.RegressionPrior;
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.Formatter;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class DiscreteChooser
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 9199242060691577692L;
/*     */   private final Vector mCoefficients;
/*     */ 
/*     */   public DiscreteChooser(Vector coefficients)
/*     */   {
/* 166 */     this.mCoefficients = coefficients;
/*     */   }
/*     */ 
/*     */   public int choose(Vector[] choices)
/*     */   {
/* 180 */     verifyNonEmpty(choices);
/* 181 */     if (choices.length == 1)
/* 182 */       return 0;
/* 183 */     int maxIndex = 0;
/* 184 */     double maxScore = linearBasis(choices[0]);
/* 185 */     for (int i = 1; i < choices.length; i++) {
/* 186 */       double score = linearBasis(choices[i]);
/* 187 */       if (score > maxScore) {
/* 188 */         maxScore = score;
/* 189 */         maxIndex = i;
/*     */       }
/*     */     }
/* 192 */     return maxIndex;
/*     */   }
/*     */ 
/*     */   public double[] choiceProbs(Vector[] choices)
/*     */   {
/* 207 */     verifyNonEmpty(choices);
/* 208 */     double[] scores = choiceLogProbs(choices);
/*     */ 
/* 210 */     for (int i = 0; i < scores.length; i++) {
/* 211 */       scores[i] = java.lang.Math.exp(scores[i]);
/*     */     }
/* 213 */     return scores;
/*     */   }
/*     */ 
/*     */   public double[] choiceLogProbs(Vector[] choices)
/*     */   {
/* 228 */     verifyNonEmpty(choices);
/* 229 */     double[] scores = new double[choices.length];
/* 230 */     for (int i = 0; i < choices.length; i++) {
/* 231 */       scores[i] = this.mCoefficients.dotProduct(choices[i]);
/*     */     }
/* 233 */     double Z = com.aliasi.util.Math.logSumOfExponentials(scores);
/* 234 */     for (int i = 0; i < choices.length; i++) {
/* 235 */       scores[i] -= Z;
/*     */     }
/* 237 */     return scores;
/*     */   }
/*     */ 
/*     */   public Vector coefficients()
/*     */   {
/* 247 */     return Matrices.unmodifiableVector(this.mCoefficients);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 257 */     StringBuilder sb = new StringBuilder();
/* 258 */     sb.append("DiscreteChoose(");
/* 259 */     int[] nzDims = this.mCoefficients.nonZeroDimensions();
/* 260 */     for (int i = 0; i < nzDims.length; i++) {
/* 261 */       int d = nzDims[i];
/* 262 */       if (i > 0)
/* 263 */         sb.append(",");
/* 264 */       sb.append(Integer.toString(d));
/* 265 */       sb.append('=');
/* 266 */       sb.append(Double.toString(this.mCoefficients.value(d)));
/*     */     }
/* 268 */     sb.append(")");
/* 269 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   double linearBasis(Vector v) {
/* 273 */     return v.dotProduct(this.mCoefficients);
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 277 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public static DiscreteChooser estimate(Vector[][] alternativess, int[] choices, RegressionPrior prior, int priorBlockSize, AnnealingSchedule annealingSchedule, double minImprovement, int minEpochs, int maxEpochs, Reporter reporter)
/*     */   {
/* 314 */     if (reporter == null) {
/* 315 */       reporter = Reporters.silent();
/*     */     }
/* 317 */     int numTrainingInstances = alternativess.length;
/*     */ 
/* 319 */     reporter.info("estimate()");
/* 320 */     reporter.info("# training cases=" + numTrainingInstances);
/* 321 */     reporter.info("regression prior=" + prior);
/* 322 */     reporter.info("annealing schedule=" + annealingSchedule);
/* 323 */     reporter.info("min improvement=" + minImprovement);
/* 324 */     reporter.info("min epochs=" + minEpochs);
/* 325 */     reporter.info("max epochs=" + maxEpochs);
/*     */ 
/* 327 */     if (alternativess.length == 0) {
/* 328 */       String msg = "Require at least 1 training instance.   Found alternativess.length=0";
/*     */ 
/* 330 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 332 */     if (alternativess.length != choices.length) {
/* 333 */       String msg = "Alternatives and choices must be the same length. Found alternativess.length=" + alternativess.length + " choices.length=" + choices.length;
/*     */ 
/* 336 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 338 */     for (int i = 0; i < alternativess.length; i++) {
/* 339 */       if (alternativess[i].length < 1) {
/* 340 */         String msg = "Require at least one alternative. Found alternativess[" + i + "].length=0";
/*     */ 
/* 342 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 345 */     for (int i = 0; i < alternativess.length; i++) {
/* 346 */       if (choices[i] < 0) {
/* 347 */         String msg = "Choices must be non-negative. Found choices[" + i + "]=" + choices[i];
/*     */ 
/* 349 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 351 */       if (choices[i] > alternativess[i].length) {
/* 352 */         String msg = "Choices must be less than alts length. Found choices[" + i + "]=" + choices[i] + " alternativess[" + i + "].length=" + alternativess.length + ".";
/*     */ 
/* 356 */         throw new IllegalArgumentException(msg);
/*     */       }
/*     */     }
/* 359 */     int numDimensions = alternativess[0][0].numDimensions();
/* 360 */     for (int i = 0; i < alternativess.length; i++) {
/* 361 */       for (int j = 0; j < alternativess[i].length; j++) {
/* 362 */         if (numDimensions != alternativess[i][j].numDimensions()) {
/* 363 */           String msg = "All alternatives must be same length. alternativess[0][0].length=" + numDimensions + " alternativess[" + i + "][" + j + "]=" + alternativess[i][j] + ".";
/*     */ 
/* 367 */           throw new IllegalArgumentException(msg);
/*     */         }
/*     */       }
/*     */     }
/* 371 */     Vector coefficientVector = new DenseVector(numDimensions);
/*     */ 
/* 373 */     DiscreteChooser chooser = new DiscreteChooser(coefficientVector);
/*     */ 
/* 376 */     double lastLlp = (0.0D / 0.0D);
/* 377 */     double rollingAverageRelativeDiff = 1.0D;
/* 378 */     double bestLlp = (-1.0D / 0.0D);
/* 379 */     for (int epoch = 0; epoch < maxEpochs; epoch++) {
/* 380 */       double learningRate = annealingSchedule.learningRate(epoch);
/* 381 */       for (int j = 0; j < numTrainingInstances; j++) {
/* 382 */         Vector[] alternatives = alternativess[j];
/* 383 */         int choice = choices[j];
/* 384 */         double[] probs = chooser.choiceProbs(alternatives);
/* 385 */         for (int k = 0; k < alternatives.length; k++) {
/* 386 */           double condProbMinusTruth = choice == k ? probs[k] - 1.0D : probs[k];
/*     */ 
/* 390 */           if (condProbMinusTruth != 0.0D)
/*     */           {
/* 392 */             coefficientVector.increment(-learningRate * condProbMinusTruth, alternatives[k]);
/*     */           }
/*     */         }
/* 395 */         if (j % priorBlockSize == 0) {
/* 396 */           updatePrior(prior, coefficientVector, learningRate * priorBlockSize / numTrainingInstances);
/*     */         }
/*     */       }
/* 399 */       updatePrior(prior, coefficientVector, learningRate * (numTrainingInstances % priorBlockSize) / numTrainingInstances);
/*     */ 
/* 401 */       double ll = logLikelihood(chooser, alternativess, choices);
/*     */ 
/* 403 */       double lp = com.aliasi.util.Math.logBase2ToNaturalLog(prior.log2Prior(coefficientVector));
/*     */ 
/* 405 */       double llp = ll + lp;
/* 406 */       if (llp > bestLlp) {
/* 407 */         bestLlp = llp;
/*     */       }
/* 409 */       if (epoch > 0) {
/* 410 */         double relativeDiff = com.aliasi.util.Math.relativeAbsoluteDifference(lastLlp, llp);
/*     */ 
/* 412 */         rollingAverageRelativeDiff = (9.0D * rollingAverageRelativeDiff + relativeDiff) / 10.0D;
/*     */       }
/* 414 */       lastLlp = llp;
/* 415 */       if (reporter.isDebugEnabled()) {
/* 416 */         Formatter formatter = null;
/*     */         try {
/* 418 */           formatter = new Formatter(Locale.ENGLISH);
/* 419 */           formatter.format("epoch=%5d lr=%11.9f ll=%11.4f lp=%11.4f llp=%11.4f llp*=%11.4f", new Object[] { Integer.valueOf(epoch), Double.valueOf(learningRate), Double.valueOf(ll), Double.valueOf(lp), Double.valueOf(llp), Double.valueOf(bestLlp) });
/*     */ 
/* 425 */           reporter.debug(formatter.toString());
/*     */         } catch (IllegalFormatException e) {
/* 427 */           reporter.warn("Illegal format in discrete chooser");
/*     */         } finally {
/* 429 */           if (formatter != null) {
/* 430 */             formatter.close();
/*     */           }
/*     */         }
/*     */       }
/* 434 */       if (rollingAverageRelativeDiff < minImprovement) {
/* 435 */         reporter.info("Converged with rollingAverageRelativeDiff=" + rollingAverageRelativeDiff);
/*     */ 
/* 437 */         break;
/*     */       }
/*     */     }
/* 440 */     return chooser;
/*     */   }
/*     */ 
/*     */   static void updatePrior(RegressionPrior prior, Vector coefficientVector, double learningRate)
/*     */   {
/* 446 */     if (prior.isUniform())
/* 447 */       return;
/* 448 */     int numDimensions = coefficientVector.numDimensions();
/* 449 */     for (int d = 0; d < numDimensions; d++) {
/* 450 */       double priorMode = prior.mode(d);
/* 451 */       double oldVal = coefficientVector.value(d);
/* 452 */       if (oldVal != priorMode)
/*     */       {
/* 454 */         double priorGradient = prior.gradient(oldVal, d);
/* 455 */         double delta = learningRate * priorGradient;
/* 456 */         if (oldVal != 0.0D) {
/* 457 */           double newVal = oldVal > 0.0D ? java.lang.Math.max(0.0D, oldVal - delta) : java.lang.Math.min(0.0D, oldVal - delta);
/*     */ 
/* 460 */           coefficientVector.setValue(d, newVal);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static double logLikelihood(DiscreteChooser chooser, Vector[][] alternativess, int[] choices) {
/* 467 */     double ll = 0.0D;
/* 468 */     for (int i = 0; i < alternativess.length; i++)
/* 469 */       ll += logLikelihood(chooser, alternativess[i], choices[i]);
/* 470 */     return ll;
/*     */   }
/*     */ 
/*     */   static double logLikelihood(DiscreteChooser chooser, Vector[] alternatives, int choice)
/*     */   {
/* 476 */     double[] logProbs = chooser.choiceLogProbs(alternatives);
/* 477 */     return logProbs[choice];
/*     */   }
/*     */ 
/*     */   static void verifyNonEmpty(Vector[] choices) {
/* 481 */     if (choices.length > 0) return;
/* 482 */     String msg = "Require at least one choice. Found choices.length=0.";
/*     */ 
/* 484 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */   static class Externalizer extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -8567713287299117186L;
/*     */     private final DiscreteChooser mChooser;
/*     */ 
/*     */     public Externalizer() {
/* 492 */       this(null);
/*     */     }
/*     */     public Externalizer(DiscreteChooser chooser) {
/* 495 */       this.mChooser = chooser;
/*     */     }
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 498 */       out.writeObject(this.mChooser.mCoefficients);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 504 */       Vector v = (Vector)in.readObject();
/* 505 */       return new DiscreteChooser(v);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.dca.DiscreteChooser
 * JD-Core Version:    0.6.2
 */