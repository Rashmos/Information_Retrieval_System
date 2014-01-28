/*     */ package com.aliasi.classify;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public class JointClassifierEvaluator<E> extends ConditionalClassifierEvaluator<E>
/*     */ {
/*     */   public JointClassifierEvaluator(JointClassifier<E> classifier, String[] categories, boolean storeInputs)
/*     */   {
/*  53 */     super(classifier, categories, storeInputs);
/*     */   }
/*     */ 
/*     */   public void setClassifier(JointClassifier<E> classifier)
/*     */   {
/*  66 */     setClassifier(classifier, JointClassifierEvaluator.class);
/*     */   }
/*     */ 
/*     */   public JointClassifier<E> classifier()
/*     */   {
/*  77 */     JointClassifier result = (JointClassifier)super.classifier();
/*     */ 
/*  79 */     return result;
/*     */   }
/*     */ 
/*     */   public double averageLog2JointProbability(String refCategory, String responseCategory)
/*     */   {
/* 106 */     validateCategory(refCategory);
/* 107 */     validateCategory(responseCategory);
/* 108 */     double sum = 0.0D;
/* 109 */     int count = 0;
/* 110 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 111 */       if (((String)this.mReferenceCategories.get(i)).equals(refCategory)) {
/* 112 */         JointClassification c = (JointClassification)this.mClassifications.get(i);
/*     */ 
/* 114 */         for (int rank = 0; rank < c.size(); rank++) {
/* 115 */           if (c.category(rank).equals(responseCategory)) {
/* 116 */             sum += c.jointLog2Probability(rank);
/* 117 */             count++;
/* 118 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 123 */     return sum / count;
/*     */   }
/*     */ 
/*     */   public double averageLog2JointProbabilityReference()
/*     */   {
/* 142 */     double sum = 0.0D;
/* 143 */     for (int i = 0; i < this.mReferenceCategories.size(); i++) {
/* 144 */       String refCategory = ((String)this.mReferenceCategories.get(i)).toString();
/* 145 */       JointClassification c = (JointClassification)this.mClassifications.get(i);
/*     */ 
/* 147 */       for (int rank = 0; rank < c.size(); rank++) {
/* 148 */         if (c.category(rank).equals(refCategory)) {
/* 149 */           sum += c.jointLog2Probability(rank);
/* 150 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 154 */     return sum / this.mReferenceCategories.size();
/*     */   }
/*     */ 
/*     */   public double corpusLog2JointProbability()
/*     */   {
/* 180 */     double total = 0.0D;
/* 181 */     for (int i = 0; i < this.mClassifications.size(); i++) {
/* 182 */       JointClassification c = (JointClassification)this.mClassifications.get(i);
/*     */ 
/* 184 */       double maxJointLog2P = (-1.0D / 0.0D);
/* 185 */       for (int rank = 0; rank < c.size(); rank++) {
/* 186 */         double jointLog2P = c.jointLog2Probability(rank);
/* 187 */         if (jointLog2P > maxJointLog2P)
/* 188 */           maxJointLog2P = jointLog2P;
/*     */       }
/* 190 */       double sum = 0.0D;
/* 191 */       for (int rank = 0; rank < c.size(); rank++)
/* 192 */         sum += java.lang.Math.pow(2.0D, c.jointLog2Probability(rank) - maxJointLog2P);
/* 193 */       total += maxJointLog2P + com.aliasi.util.Math.log2(sum);
/*     */     }
/* 195 */     return total;
/*     */   }
/*     */ 
/*     */   void baseToString(StringBuilder sb)
/*     */   {
/* 200 */     super.baseToString(sb);
/* 201 */     sb.append("Average Log2 Joint Probability Reference=" + averageLog2JointProbabilityReference() + "\n");
/*     */   }
/*     */ 
/*     */   void oneVsAllToString(StringBuilder sb, String category, int i)
/*     */   {
/* 207 */     super.oneVsAllToString(sb, category, i);
/* 208 */     sb.append("Average Joint Probability Histogram=\n");
/* 209 */     appendCategoryLine(sb);
/* 210 */     for (int j = 0; j < numCategories(); j++) {
/* 211 */       if (j > 0) sb.append(',');
/* 212 */       sb.append(averageLog2JointProbability(category, categories()[j]));
/*     */     }
/*     */ 
/* 215 */     sb.append("\n");
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.classify.JointClassifierEvaluator
 * JD-Core Version:    0.6.2
 */