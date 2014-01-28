/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ 
/*     */ public abstract class AttributeImpl
/*     */   implements Cloneable, Serializable, Attribute
/*     */ {
/*     */   public abstract void clear();
/*     */ 
/*     */   public String toString()
/*     */   {
/*  53 */     StringBuilder buffer = new StringBuilder();
/*  54 */     Class clazz = getClass();
/*  55 */     Field[] fields = clazz.getDeclaredFields();
/*     */     try {
/*  57 */       for (int i = 0; i < fields.length; i++) {
/*  58 */         Field f = fields[i];
/*  59 */         if (!Modifier.isStatic(f.getModifiers())) {
/*  60 */           f.setAccessible(true);
/*  61 */           Object value = f.get(this);
/*  62 */           if (buffer.length() > 0) {
/*  63 */             buffer.append(',');
/*     */           }
/*  65 */           if (value == null)
/*  66 */             buffer.append(f.getName() + "=null");
/*     */           else
/*  68 */             buffer.append(f.getName() + "=" + value);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IllegalAccessException e)
/*     */     {
/*  74 */       throw new RuntimeException(e);
/*     */     }
/*     */ 
/*  77 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   public abstract int hashCode();
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */ 
/*     */   public abstract void copyTo(AttributeImpl paramAttributeImpl);
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 118 */     Object clone = null;
/*     */     try {
/* 120 */       clone = super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 122 */       throw new RuntimeException(e);
/*     */     }
/* 124 */     return clone;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.AttributeImpl
 * JD-Core Version:    0.6.2
 */