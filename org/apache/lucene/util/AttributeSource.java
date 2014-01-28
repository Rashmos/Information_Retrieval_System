/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ public class AttributeSource
/*     */ {
/*     */   private final Map<Class<? extends Attribute>, AttributeImpl> attributes;
/*     */   private final Map<Class<? extends AttributeImpl>, AttributeImpl> attributeImpls;
/*     */   private AttributeFactory factory;
/* 180 */   private static final WeakHashMap<Class<? extends AttributeImpl>, LinkedList<WeakReference<Class<? extends Attribute>>>> knownImplClasses = new WeakHashMap();
/*     */ 
/* 304 */   private State currentState = null;
/*     */ 
/*     */   public AttributeSource()
/*     */   {
/* 107 */     this(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY);
/*     */   }
/*     */ 
/*     */   public AttributeSource(AttributeSource input)
/*     */   {
/* 114 */     if (input == null) {
/* 115 */       throw new IllegalArgumentException("input AttributeSource must not be null");
/*     */     }
/* 117 */     this.attributes = input.attributes;
/* 118 */     this.attributeImpls = input.attributeImpls;
/* 119 */     this.factory = input.factory;
/*     */   }
/*     */ 
/*     */   public AttributeSource(AttributeFactory factory)
/*     */   {
/* 126 */     this.attributes = new LinkedHashMap();
/* 127 */     this.attributeImpls = new LinkedHashMap();
/* 128 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */   public AttributeFactory getAttributeFactory()
/*     */   {
/* 135 */     return this.factory;
/*     */   }
/*     */ 
/*     */   public Iterator<Class<? extends Attribute>> getAttributeClassesIterator()
/*     */   {
/* 142 */     return Collections.unmodifiableSet(this.attributes.keySet()).iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<AttributeImpl> getAttributeImplsIterator()
/*     */   {
/* 150 */     if (hasAttributes()) {
/* 151 */       if (this.currentState == null) {
/* 152 */         computeCurrentState();
/*     */       }
/* 154 */       final State initState = this.currentState;
/* 155 */       return new Iterator() {
/* 156 */         private AttributeSource.State state = initState;
/*     */ 
/*     */         public void remove() {
/* 159 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */         public AttributeImpl next() {
/* 163 */           if (this.state == null)
/* 164 */             throw new NoSuchElementException();
/* 165 */           AttributeImpl att = this.state.attribute;
/* 166 */           this.state = this.state.next;
/* 167 */           return att;
/*     */         }
/*     */ 
/*     */         public boolean hasNext() {
/* 171 */           return this.state != null;
/*     */         }
/*     */       };
/*     */     }
/* 175 */     return Collections.emptySet().iterator();
/*     */   }
/*     */ 
/*     */   public void addAttributeImpl(AttributeImpl att)
/*     */   {
/* 192 */     Class clazz = att.getClass();
/* 193 */     if (this.attributeImpls.containsKey(clazz))
/*     */       return;
/*     */     LinkedList foundInterfaces;
/* 195 */     synchronized (knownImplClasses) {
/* 196 */       foundInterfaces = (LinkedList)knownImplClasses.get(clazz);
/* 197 */       if (foundInterfaces == null)
/*     */       {
/* 200 */         knownImplClasses.put(clazz, foundInterfaces = new LinkedList());
/*     */ 
/* 203 */         Class actClazz = clazz;
/*     */         do {
/* 205 */           for (Class curInterface : actClazz.getInterfaces()) {
/* 206 */             if ((curInterface != Attribute.class) && (Attribute.class.isAssignableFrom(curInterface))) {
/* 207 */               foundInterfaces.add(new WeakReference(curInterface.asSubclass(Attribute.class)));
/*     */             }
/*     */           }
/* 210 */           actClazz = actClazz.getSuperclass();
/* 211 */         }while (actClazz != null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 216 */     for (WeakReference curInterfaceRef : foundInterfaces) {
/* 217 */       Class curInterface = (Class)curInterfaceRef.get();
/*     */ 
/* 219 */       assert (curInterface != null) : "We have a strong reference on the class holding the interfaces, so they should never get evicted";
/*     */ 
/* 221 */       if (!this.attributes.containsKey(curInterface))
/*     */       {
/* 223 */         this.currentState = null;
/* 224 */         this.attributes.put(curInterface, att);
/* 225 */         this.attributeImpls.put(clazz, att);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public <A extends Attribute> A addAttribute(Class<A> attClass)
/*     */   {
/* 237 */     AttributeImpl attImpl = (AttributeImpl)this.attributes.get(attClass);
/* 238 */     if (attImpl == null) {
/* 239 */       if ((!attClass.isInterface()) || (!Attribute.class.isAssignableFrom(attClass))) {
/* 240 */         throw new IllegalArgumentException("addAttribute() only accepts an interface that extends Attribute, but " + attClass.getName() + " does not fulfil this contract.");
/*     */       }
/*     */ 
/* 245 */       addAttributeImpl(attImpl = this.factory.createAttributeInstance(attClass));
/*     */     }
/* 247 */     return (Attribute)attClass.cast(attImpl);
/*     */   }
/*     */ 
/*     */   public boolean hasAttributes()
/*     */   {
/* 252 */     return !this.attributes.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean hasAttribute(Class<? extends Attribute> attClass)
/*     */   {
/* 260 */     return this.attributes.containsKey(attClass);
/*     */   }
/*     */ 
/*     */   public <A extends Attribute> A getAttribute(Class<A> attClass)
/*     */   {
/* 275 */     AttributeImpl attImpl = (AttributeImpl)this.attributes.get(attClass);
/* 276 */     if (attImpl == null) {
/* 277 */       throw new IllegalArgumentException("This AttributeSource does not have the attribute '" + attClass.getName() + "'.");
/*     */     }
/* 279 */     return (Attribute)attClass.cast(attImpl);
/*     */   }
/*     */ 
/*     */   private void computeCurrentState()
/*     */   {
/* 307 */     this.currentState = new State();
/* 308 */     State c = this.currentState;
/* 309 */     Iterator it = this.attributeImpls.values().iterator();
/* 310 */     c.attribute = ((AttributeImpl)it.next());
/* 311 */     while (it.hasNext()) {
/* 312 */       c.next = new State();
/* 313 */       c = c.next;
/* 314 */       c.attribute = ((AttributeImpl)it.next());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clearAttributes()
/*     */   {
/* 323 */     if (hasAttributes()) {
/* 324 */       if (this.currentState == null) {
/* 325 */         computeCurrentState();
/*     */       }
/* 327 */       for (State state = this.currentState; state != null; state = state.next)
/* 328 */         state.attribute.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public State captureState()
/*     */   {
/* 338 */     if (!hasAttributes()) {
/* 339 */       return null;
/*     */     }
/*     */ 
/* 342 */     if (this.currentState == null) {
/* 343 */       computeCurrentState();
/*     */     }
/* 345 */     return (State)this.currentState.clone();
/*     */   }
/*     */ 
/*     */   public void restoreState(State state)
/*     */   {
/* 364 */     if (state == null)
/*     */       return;
/*     */     do {
/* 367 */       AttributeImpl targetImpl = (AttributeImpl)this.attributeImpls.get(state.attribute.getClass());
/* 368 */       if (targetImpl == null)
/* 369 */         throw new IllegalArgumentException("State contains an AttributeImpl that is not in this AttributeSource");
/* 370 */       state.attribute.copyTo(targetImpl);
/* 371 */       state = state.next;
/* 372 */     }while (state != null);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 377 */     int code = 0;
/* 378 */     if (hasAttributes()) {
/* 379 */       if (this.currentState == null) {
/* 380 */         computeCurrentState();
/*     */       }
/* 382 */       for (State state = this.currentState; state != null; state = state.next) {
/* 383 */         code = code * 31 + state.attribute.hashCode();
/*     */       }
/*     */     }
/*     */ 
/* 387 */     return code;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 392 */     if (obj == this) {
/* 393 */       return true;
/*     */     }
/*     */ 
/* 396 */     if ((obj instanceof AttributeSource)) {
/* 397 */       AttributeSource other = (AttributeSource)obj;
/*     */ 
/* 399 */       if (hasAttributes()) {
/* 400 */         if (!other.hasAttributes()) {
/* 401 */           return false;
/*     */         }
/*     */ 
/* 404 */         if (this.attributeImpls.size() != other.attributeImpls.size()) {
/* 405 */           return false;
/*     */         }
/*     */ 
/* 409 */         if (this.currentState == null) {
/* 410 */           computeCurrentState();
/*     */         }
/* 412 */         State thisState = this.currentState;
/* 413 */         if (other.currentState == null) {
/* 414 */           other.computeCurrentState();
/*     */         }
/* 416 */         State otherState = other.currentState;
/* 417 */         while ((thisState != null) && (otherState != null)) {
/* 418 */           if ((otherState.attribute.getClass() != thisState.attribute.getClass()) || (!otherState.attribute.equals(thisState.attribute))) {
/* 419 */             return false;
/*     */           }
/* 421 */           thisState = thisState.next;
/* 422 */           otherState = otherState.next;
/*     */         }
/* 424 */         return true;
/*     */       }
/* 426 */       return !other.hasAttributes();
/*     */     }
/*     */ 
/* 429 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 434 */     StringBuilder sb = new StringBuilder().append('(');
/* 435 */     if (hasAttributes()) {
/* 436 */       if (this.currentState == null) {
/* 437 */         computeCurrentState();
/*     */       }
/* 439 */       for (State state = this.currentState; state != null; state = state.next) {
/* 440 */         if (state != this.currentState) sb.append(',');
/* 441 */         sb.append(state.attribute.toString());
/*     */       }
/*     */     }
/* 444 */     return ')';
/*     */   }
/*     */ 
/*     */   public AttributeSource cloneAttributes()
/*     */   {
/* 453 */     AttributeSource clone = new AttributeSource(this.factory);
/*     */ 
/* 456 */     if (hasAttributes()) {
/* 457 */       if (this.currentState == null) {
/* 458 */         computeCurrentState();
/*     */       }
/* 460 */       for (State state = this.currentState; state != null; state = state.next) {
/* 461 */         clone.attributeImpls.put(state.attribute.getClass(), (AttributeImpl)state.attribute.clone());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 466 */     for (Map.Entry entry : this.attributes.entrySet()) {
/* 467 */       clone.attributes.put(entry.getKey(), clone.attributeImpls.get(((AttributeImpl)entry.getValue()).getClass()));
/*     */     }
/*     */ 
/* 470 */     return clone;
/*     */   }
/*     */ 
/*     */   public static final class State
/*     */     implements Cloneable
/*     */   {
/*     */     private AttributeImpl attribute;
/*     */     private State next;
/*     */ 
/*     */     public Object clone()
/*     */     {
/* 293 */       State clone = new State();
/* 294 */       clone.attribute = ((AttributeImpl)this.attribute.clone());
/*     */ 
/* 296 */       if (this.next != null) {
/* 297 */         clone.next = ((State)this.next.clone());
/*     */       }
/*     */ 
/* 300 */       return clone;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class AttributeFactory
/*     */   {
/*  55 */     public static final AttributeFactory DEFAULT_ATTRIBUTE_FACTORY = new DefaultAttributeFactory(null);
/*     */ 
/*     */     public abstract AttributeImpl createAttributeInstance(Class<? extends Attribute> paramClass);
/*     */ 
/*  58 */     private static final class DefaultAttributeFactory extends AttributeSource.AttributeFactory { private static final WeakHashMap<Class<? extends Attribute>, WeakReference<Class<? extends AttributeImpl>>> attClassImplMap = new WeakHashMap();
/*     */ 
/*     */       public AttributeImpl createAttributeInstance(Class<? extends Attribute> attClass)
/*     */       {
/*     */         try
/*     */         {
/*  66 */           return (AttributeImpl)getClassForInterface(attClass).newInstance();
/*     */         } catch (InstantiationException e) {
/*  68 */           throw new IllegalArgumentException("Could not instantiate implementing class for " + attClass.getName()); } catch (IllegalAccessException e) {
/*     */         }
/*  70 */         throw new IllegalArgumentException("Could not instantiate implementing class for " + attClass.getName());
/*     */       }
/*     */ 
/*     */       private static Class<? extends AttributeImpl> getClassForInterface(Class<? extends Attribute> attClass)
/*     */       {
/*  75 */         synchronized (attClassImplMap) {
/*  76 */           WeakReference ref = (WeakReference)attClassImplMap.get(attClass);
/*  77 */           Class clazz = ref == null ? null : (Class)ref.get();
/*  78 */           if (clazz == null) {
/*     */             try {
/*  80 */               attClassImplMap.put(attClass, new WeakReference(clazz = Class.forName(attClass.getName() + "Impl", true, attClass.getClassLoader()).asSubclass(AttributeImpl.class)));
/*     */             }
/*     */             catch (ClassNotFoundException e)
/*     */             {
/*  87 */               throw new IllegalArgumentException("Could not find implementing class for " + attClass.getName());
/*     */             }
/*     */           }
/*  90 */           return clazz;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.AttributeSource
 * JD-Core Version:    0.6.2
 */