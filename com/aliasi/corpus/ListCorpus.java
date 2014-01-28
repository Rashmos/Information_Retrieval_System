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
/*     */ public class ListCorpus<E> extends Corpus<ObjectHandler<E>>
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 2705587926190761352L;
/*     */   private final List<E> mTrainCases;
/*     */   private final List<E> mTestCases;
/*     */ 
/*     */   public ListCorpus()
/*     */   {
/*  69 */     this.mTrainCases = new ArrayList();
/*  70 */     this.mTestCases = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void addTest(E e)
/*     */   {
/*  79 */     this.mTestCases.add(e);
/*     */   }
/*     */ 
/*     */   public void addTrain(E e)
/*     */   {
/*  88 */     this.mTrainCases.add(e);
/*     */   }
/*     */ 
/*     */   public void permuteCorpus(Random random)
/*     */   {
/*  99 */     Collections.shuffle(this.mTrainCases, random);
/* 100 */     Collections.shuffle(this.mTestCases, random);
/*     */   }
/*     */ 
/*     */   public void visitTrain(ObjectHandler<E> handler) {
/* 104 */     for (Iterator i$ = this.mTrainCases.iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 105 */       handler.handle(e); }
/*     */   }
/*     */ 
/*     */   public void visitTest(ObjectHandler<E> handler) {
/* 109 */     for (Iterator i$ = this.mTestCases.iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 110 */       handler.handle(e); }
/*     */   }
/*     */ 
/*     */   Object writeReplace() {
/* 114 */     return new Serializer(this);
/*     */   }
/*     */   static class Serializer<F> extends AbstractExternalizable {
/*     */     static final long serialVersionUID = -5552459221218525839L;
/*     */     private final ListCorpus<F> mCorpus;
/*     */ 
/* 121 */     public Serializer() { this(null); }
/*     */ 
/*     */     public Serializer(ListCorpus<F> corpus) {
/* 124 */       this.mCorpus = corpus;
/*     */     }
/*     */ 
/*     */     public void writeExternal(ObjectOutput out) throws IOException {
/* 128 */       out.writeInt(this.mCorpus.mTrainCases.size());
/* 129 */       for (Iterator i$ = this.mCorpus.mTrainCases.iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 130 */         out.writeObject(e); }
/* 131 */       out.writeInt(this.mCorpus.mTestCases.size());
/* 132 */       for (Iterator i$ = this.mCorpus.mTestCases.iterator(); i$.hasNext(); ) { Object e = i$.next();
/* 133 */         out.writeObject(e); }
/*     */     }
/*     */ 
/*     */     public Object read(ObjectInput in)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 139 */       ListCorpus corpus = new ListCorpus();
/* 140 */       int numTrain = in.readInt();
/* 141 */       for (int i = 0; i < numTrain; i++)
/*     */       {
/* 143 */         Object e = in.readObject();
/* 144 */         corpus.addTrain(e);
/*     */       }
/* 146 */       int numTest = in.readInt();
/* 147 */       for (int i = 0; i < numTest; i++)
/*     */       {
/* 149 */         Object e = in.readObject();
/* 150 */         corpus.addTest(e);
/*     */       }
/* 152 */       return corpus;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     com.aliasi.corpus.ListCorpus
 * JD-Core Version:    0.6.2
 */