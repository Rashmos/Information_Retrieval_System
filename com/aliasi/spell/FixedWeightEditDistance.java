/*     */ package com.aliasi.spell;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import com.aliasi.util.Compilable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class FixedWeightEditDistance extends WeightedEditDistance
/*     */   implements Compilable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 6520949001598595981L;
/*     */   private final double mMatchWeight;
/*     */   private final double mDeleteWeight;
/*     */   private final double mInsertWeight;
/*     */   private final double mSubstituteWeight;
/*     */   private final double mTransposeWeight;
/*     */ 
/*     */   public FixedWeightEditDistance(double matchWeight, double deleteWeight, double insertWeight, double substituteWeight, double transposeWeight)
/*     */   {
/*  74 */     this.mMatchWeight = matchWeight;
/*  75 */     this.mDeleteWeight = deleteWeight;
/*  76 */     this.mInsertWeight = insertWeight;
/*  77 */     this.mSubstituteWeight = substituteWeight;
/*  78 */     this.mTransposeWeight = transposeWeight;
/*     */   }
/*     */ 
/*     */   public FixedWeightEditDistance()
/*     */   {
/*  89 */     this(0.0D, (-1.0D / 0.0D), (-1.0D / 0.0D), (-1.0D / 0.0D), (-1.0D / 0.0D));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 103 */     StringBuilder sb = new StringBuilder();
/* 104 */     sb.append("Edit Distance Class=" + getClass());
/* 105 */     sb.append("FixedWeightEditDistance costs:");
/* 106 */     sb.append("  match weight=" + this.mMatchWeight);
/* 107 */     sb.append("  insert weight=" + this.mInsertWeight);
/* 108 */     sb.append("  delete weight=" + this.mDeleteWeight);
/* 109 */     sb.append("  substitute weight=" + this.mSubstituteWeight);
/* 110 */     sb.append("  transpose weight=" + this.mTransposeWeight);
/* 111 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private Object writeReplace() {
/* 115 */     return new Externalizer(this);
/*     */   }
/*     */ 
/*     */   public void compileTo(ObjectOutput objOut)
/*     */     throws IOException
/*     */   {
/* 157 */     objOut.writeObject(new Externalizer(this));
/*     */   }
/*     */ 
/*     */   public double matchWeight(char cMatched)
/*     */   {
/* 168 */     return this.mMatchWeight;
/*     */   }
/*     */ 
/*     */   public double deleteWeight(char cDeleted)
/*     */   {
/* 179 */     return this.mDeleteWeight;
/*     */   }
/*     */ 
/*     */   public double insertWeight(char cInserted)
/*     */   {
/* 190 */     return this.mInsertWeight;
/*     */   }
/*     */ 
/*     */   public double substituteWeight(char cDeleted, char cInserted)
/*     */   {
/* 204 */     return this.mSubstituteWeight;
/*     */   }
/*     */ 
/*     */   public double transposeWeight(char cFirst, char cSecond)
/*     */   {
/* 217 */     return this.mTransposeWeight;
/*     */   }
/*     */ 
/*     */   private static class Externalizer extends AbstractExternalizable
/*     */   {
/*     */     private static final long serialVersionUID = 636473803792927790L;
/*     */     private final FixedWeightEditDistance mFWED;
/*     */ 
/*     */     public Externalizer()
/*     */     {
/* 122 */       this.mFWED = null;
/*     */     }
/*     */     public Externalizer(FixedWeightEditDistance fwed) {
/* 125 */       this.mFWED = fwed;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 129 */       objOut.writeDouble(this.mFWED.mMatchWeight);
/* 130 */       objOut.writeDouble(this.mFWED.mDeleteWeight);
/* 131 */       objOut.writeDouble(this.mFWED.mInsertWeight);
/* 132 */       objOut.writeDouble(this.mFWED.mSubstituteWeight);
/* 133 */       objOut.writeDouble(this.mFWED.mTransposeWeight);
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput objIn) throws IOException {
/* 137 */       return new FixedWeightEditDistance(objIn.readDouble(), objIn.readDouble(), objIn.readDouble(), objIn.readDouble(), objIn.readDouble());
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.spell.FixedWeightEditDistance
 * JD-Core Version:    0.6.2
 */