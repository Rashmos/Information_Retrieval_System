/*    */ package com.aliasi.stats;
/*    */ 
/*    */ import com.aliasi.util.AbstractExternalizable;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInput;
/*    */ import java.io.ObjectOutput;
/*    */ 
/*    */ public class PoissonConstant extends PoissonDistribution
/*    */ {
/*    */   private final double mMean;
/*    */ 
/*    */   public PoissonConstant(double mean)
/*    */   {
/* 32 */     if (mean <= 0.0D) {
/* 33 */       String msg = "Mean must be finite and strictly > 0. Found mean=" + mean;
/*    */ 
/* 35 */       throw new IllegalArgumentException(msg);
/*    */     }
/* 37 */     this.mMean = mean;
/*    */   }
/*    */ 
/*    */   public void compileTo(ObjectOutput objOut)
/*    */     throws IOException
/*    */   {
/* 52 */     objOut.writeObject(new Externalizer(this));
/*    */   }
/*    */ 
/*    */   public double mean()
/*    */   {
/* 63 */     return this.mMean;
/*    */   }
/*    */   private static class Externalizer extends AbstractExternalizable { private static final long serialVersionUID = -2824074866517957016L;
/*    */     final PoissonConstant mDistro;
/*    */ 
/* 69 */     public Externalizer() { this.mDistro = null; } 
/*    */     Externalizer(PoissonConstant distro) {
/* 71 */       this.mDistro = distro;
/*    */     }
/*    */ 
/*    */     public void writeExternal(ObjectOutput objOut) throws IOException {
/* 75 */       objOut.writeDouble(this.mDistro.mean());
/*    */     }
/*    */ 
/*    */     protected Object read(ObjectInput objIn) throws IOException {
/* 79 */       return new PoissonConstant(objIn.readDouble());
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.stats.PoissonConstant
 * JD-Core Version:    0.6.2
 */