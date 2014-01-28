/*     */ package com.aliasi.corpus;
/*     */ 
/*     */ import com.aliasi.util.AbstractExternalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ 
/*     */ public class XValidatingObjectCorpus<E> extends Corpus<ObjectHandler<E>>
/*     */   implements ObjectHandler<E>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -4855182679645668642L;
/*     */   private final List<E> mItemList;
/*     */   private int mNumFolds;
/*     */   private int mFold;
/*     */ 
/*     */   public XValidatingObjectCorpus(int numFolds)
/*     */   {
/* 161 */     this(new ArrayList(), numFolds, 0);
/*     */   }
/*     */ 
/*     */   XValidatingObjectCorpus(List<E> itemList, int numFolds, int fold)
/*     */   {
/* 167 */     this.mItemList = itemList;
/* 168 */     setNumFolds(numFolds);
/* 169 */     this.mFold = fold;
/*     */   }
/*     */ 
/*     */   public XValidatingObjectCorpus<E> itemView()
/*     */   {
/* 190 */     return new XValidatingObjectCorpus(Collections.unmodifiableList(this.mItemList), this.mNumFolds, this.mFold);
/*     */   }
/*     */ 
/*     */   public int numFolds()
/*     */   {
/* 202 */     return this.mNumFolds;
/*     */   }
/*     */ 
/*     */   public void setNumFolds(int numFolds)
/*     */   {
/* 216 */     if (numFolds < 0) {
/* 217 */       String msg = "Number of folds must be non-negative. Found numFolds=" + numFolds;
/*     */ 
/* 219 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 221 */     this.mNumFolds = numFolds;
/*     */   }
/*     */ 
/*     */   public int fold()
/*     */   {
/* 230 */     return this.mFold;
/*     */   }
/*     */ 
/*     */   public void permuteCorpus(Random random)
/*     */   {
/* 239 */     Collections.shuffle(this.mItemList, random);
/*     */   }
/*     */ 
/*     */   public void setFold(int fold)
/*     */   {
/* 252 */     if (this.mNumFolds == 0) {
/* 253 */       String msg = "Cannot set folds when numFolds() is 0.";
/* 254 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 256 */     if ((fold < 0) || (fold >= this.mNumFolds)) {
/* 257 */       String msg = "Fold must be non-negative and less than numFolds. Found numFolds=" + this.mNumFolds + " fold=" + fold;
/*     */ 
/* 260 */       throw new IllegalArgumentException(msg);
/*     */     }
/* 262 */     this.mFold = fold;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 271 */     return this.mItemList.size();
/*     */   }
/*     */ 
/*     */   public void handle(E e)
/*     */   {
/* 281 */     this.mItemList.add(e);
/*     */   }
/*     */ 
/*     */   public void visitTrain(ObjectHandler<E> handler)
/*     */   {
/* 295 */     handle(handler, 0, startTestFold());
/* 296 */     handle(handler, endTestFold(), size());
/*     */   }
/*     */ 
/*     */   public void visitTest(ObjectHandler<E> handler)
/*     */   {
/* 309 */     handle(handler, startTestFold(), endTestFold());
/*     */   }
/*     */ 
/*     */   public void visitCorpus(ObjectHandler<E> handler)
/*     */   {
/* 314 */     for (Iterator i$ = this.mItemList.iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 315 */       handler.handle(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visitCorpus(ObjectHandler<E> trainHandler, ObjectHandler<E> testHandler)
/*     */   {
/* 321 */     visitTrain(trainHandler);
/* 322 */     visitTest(testHandler);
/*     */   }
/*     */ 
/*     */   public void visitTest(ObjectHandler<E> handler, int fold)
/*     */   {
/* 336 */     handle(handler, startTestFold(fold), endTestFold(fold));
/*     */   }
/*     */ 
/*     */   public void visitTrain(ObjectHandler<E> handler, int fold)
/*     */   {
/* 349 */     handle(handler, 0, startTestFold(fold));
/* 350 */     handle(handler, endTestFold(fold), size());
/*     */   }
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 355 */     return new Serializer(this);
/*     */   }
/*     */ 
/*     */   private void handle(ObjectHandler<E> handler, int start, int end) {
/* 359 */     for (int i = start; i < end; i++)
/* 360 */       handler.handle(this.mItemList.get(i));
/*     */   }
/*     */ 
/*     */   private int startTestFold() {
/* 364 */     return startTestFold(this.mFold);
/*     */   }
/*     */ 
/*     */   private int startTestFold(int fold) {
/* 368 */     if (this.mNumFolds == 0) return 0;
/* 369 */     return (int)(size() * (fold / this.mNumFolds));
/*     */   }
/*     */ 
/*     */   private int endTestFold() {
/* 373 */     return endTestFold(this.mFold);
/*     */   }
/*     */ 
/*     */   private int endTestFold(int fold) {
/* 377 */     if (this.mNumFolds == 0) return 0;
/* 378 */     if (fold == this.mNumFolds - 1)
/* 379 */       return size();
/* 380 */     return (int)(size() * ((fold + 1.0D) / this.mNumFolds));
/*     */   }
/*     */   private static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = 5857544240166060800L;
/*     */     final XValidatingObjectCorpus<F> mCorpus;
/*     */ 
/*     */     public Serializer() {
/* 388 */       this(null);
/*     */     }
/*     */     public Serializer(XValidatingObjectCorpus<F> corpus) {
/* 391 */       this.mCorpus = corpus;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 395 */       out.writeInt(this.mCorpus.numFolds());
/* 396 */       out.writeInt(this.mCorpus.fold());
/* 397 */       out.writeInt(this.mCorpus.size());
/* 398 */       for (Iterator i$ = this.mCorpus.mItemList.iterator(); i$.hasNext(); ) { Object f = i$.next();
/* 399 */         out.writeObject(f); }
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 405 */       int numFolds = in.readInt();
/* 406 */       int fold = in.readInt();
/* 407 */       int size = in.readInt();
/*     */ 
/* 409 */       XValidatingObjectCorpus corpus = new XValidatingObjectCorpus(numFolds);
/* 410 */       corpus.setFold(fold);
/*     */ 
/* 412 */       for (int i = 0; i < size; i++) {
/* 413 */         Object o = in.readObject();
/* 414 */         corpus.handle(o);
/*     */       }
/* 416 */       return corpus;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.XValidatingObjectCorpus
 * JD-Core Version:    0.6.2
 */