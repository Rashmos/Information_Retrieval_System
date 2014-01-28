/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.lang.reflect.Proxy;
/*     */ 
/*     */ public class ClassLoaderObjectInputStream extends ObjectInputStream
/*     */ {
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */   public ClassLoaderObjectInputStream(ClassLoader classLoader, InputStream inputStream)
/*     */     throws IOException, StreamCorruptedException
/*     */   {
/*  51 */     super(inputStream);
/*  52 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */   protected Class<?> resolveClass(ObjectStreamClass objectStreamClass)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  68 */     Class clazz = Class.forName(objectStreamClass.getName(), false, this.classLoader);
/*     */ 
/*  70 */     if (clazz != null)
/*     */     {
/*  72 */       return clazz;
/*     */     }
/*     */ 
/*  75 */     return super.resolveClass(objectStreamClass);
/*     */   }
/*     */ 
/*     */   protected Class<?> resolveProxyClass(String[] interfaces)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*  93 */     Class[] interfaceClasses = new Class[interfaces.length];
/*  94 */     for (int i = 0; i < interfaces.length; i++)
/*  95 */       interfaceClasses[i] = Class.forName(interfaces[i], false, this.classLoader);
/*     */     try
/*     */     {
/*  98 */       return Proxy.getProxyClass(this.classLoader, interfaceClasses); } catch (IllegalArgumentException e) {
/*     */     }
/* 100 */     return super.resolveProxyClass(interfaces);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.commons.io.input.ClassLoaderObjectInputStream
 * JD-Core Version:    0.6.2
 */