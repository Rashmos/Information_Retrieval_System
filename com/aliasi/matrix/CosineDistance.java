/*    */ package com.aliasi.matrix;
/*    */ 
/*    */ import com.aliasi.util.Proximity;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class CosineDistance
/*    */   implements Proximity<Vector>, Serializable
/*    */ {
/*    */   static final long serialVersionUID = -8456511197031445244L;
/* 45 */   public static final CosineDistance DISTANCE = new CosineDistance();
/*    */ 
/*    */   public double distance(Vector v1, Vector v2)
/*    */   {
/* 71 */     return 1.0D / (1.0D + proximity(v1, v2));
/*    */   }
/*    */ 
/*    */   public double proximity(Vector v1, Vector v2)
/*    */   {
/* 85 */     return v1.cosine(v2);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.matrix.CosineDistance
 * JD-Core Version:    0.6.2
 */