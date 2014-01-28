/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class CloseableThreadLocal<T>
/*     */   implements Closeable
/*     */ {
/*  55 */   private ThreadLocal<WeakReference<T>> t = new ThreadLocal();
/*     */ 
/*  57 */   private Map<Thread, T> hardRefs = new HashMap();
/*     */ 
/*     */   protected T initialValue() {
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */   public T get() {
/*  64 */     WeakReference weakRef = (WeakReference)this.t.get();
/*  65 */     if (weakRef == null) {
/*  66 */       Object iv = initialValue();
/*  67 */       if (iv != null) {
/*  68 */         set(iv);
/*  69 */         return iv;
/*     */       }
/*  71 */       return null;
/*     */     }
/*  73 */     return weakRef.get();
/*     */   }
/*     */ 
/*     */   public void set(T object)
/*     */   {
/*  79 */     this.t.set(new WeakReference(object));
/*     */     Iterator it;
/*  81 */     synchronized (this.hardRefs) {
/*  82 */       this.hardRefs.put(Thread.currentThread(), object);
/*     */ 
/*  85 */       for (it = this.hardRefs.keySet().iterator(); it.hasNext(); ) {
/*  86 */         Thread t = (Thread)it.next();
/*  87 */         if (!t.isAlive())
/*  88 */           it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  97 */     this.hardRefs = null;
/*     */ 
/* 100 */     if (this.t != null) {
/* 101 */       this.t.remove();
/*     */     }
/* 103 */     this.t = null;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.CloseableThreadLocal
 * JD-Core Version:    0.6.2
 */