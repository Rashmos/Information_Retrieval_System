/*    */ package org.apache.lucene.util;
/*    */ 
/*    */ import java.io.ObjectStreamException;
/*    */ import java.io.Serializable;
/*    */ import java.io.StreamCorruptedException;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ /** @deprecated */
/*    */ public abstract class Parameter
/*    */   implements Serializable
/*    */ {
/* 33 */   static Map<String, Parameter> allParameters = new HashMap();
/*    */   private String name;
/*    */ 
/*    */   private Parameter()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected Parameter(String name)
/*    */   {
/* 43 */     this.name = name;
/* 44 */     String key = makeKey(name);
/*    */ 
/* 46 */     if (allParameters.containsKey(key)) {
/* 47 */       throw new IllegalArgumentException("Parameter name " + key + " already used!");
/*    */     }
/* 49 */     allParameters.put(key, this);
/*    */   }
/*    */ 
/*    */   private String makeKey(String name) {
/* 53 */     return getClass() + " " + name;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 58 */     return this.name;
/*    */   }
/*    */ 
/*    */   protected Object readResolve()
/*    */     throws ObjectStreamException
/*    */   {
/* 69 */     Object par = allParameters.get(makeKey(this.name));
/*    */ 
/* 71 */     if (par == null) {
/* 72 */       throw new StreamCorruptedException("Unknown parameter value: " + this.name);
/*    */     }
/* 74 */     return par;
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.Parameter
 * JD-Core Version:    0.6.2
 */