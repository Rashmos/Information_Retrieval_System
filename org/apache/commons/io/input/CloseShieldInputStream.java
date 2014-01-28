/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class CloseShieldInputStream extends ProxyInputStream
/*    */ {
/*    */   public CloseShieldInputStream(InputStream in)
/*    */   {
/* 40 */     super(in);
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/* 50 */     this.in = new ClosedInputStream();
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.input.CloseShieldInputStream
 * JD-Core Version:    0.6.2
 */