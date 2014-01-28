/*    */ package org.apache.commons.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class IOExceptionWithCause extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public IOExceptionWithCause(String message, Throwable cause)
/*    */   {
/* 49 */     super(message);
/* 50 */     initCause(cause);
/*    */   }
/*    */ 
/*    */   public IOExceptionWithCause(Throwable cause)
/*    */   {
/* 64 */     super(cause == null ? null : cause.toString());
/* 65 */     initCause(cause);
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.IOExceptionWithCause
 * JD-Core Version:    0.6.2
 */