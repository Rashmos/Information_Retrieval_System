/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class CloseShieldOutputStream extends ProxyOutputStream
/*    */ {
/*    */   public CloseShieldOutputStream(OutputStream out)
/*    */   {
/* 40 */     super(out);
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 50 */     this.out = new ClosedOutputStream();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.output.CloseShieldOutputStream
 * JD-Core Version:    0.6.2
 */