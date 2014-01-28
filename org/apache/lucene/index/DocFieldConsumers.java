/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ 
/*     */ final class DocFieldConsumers extends DocFieldConsumer
/*     */ {
/*     */   final DocFieldConsumer one;
/*     */   final DocFieldConsumer two;
/* 108 */   PerDoc[] docFreeList = new PerDoc[1];
/*     */   int freeCount;
/*     */   int allocCount;
/*     */ 
/*     */   public DocFieldConsumers(DocFieldConsumer one, DocFieldConsumer two)
/*     */   {
/*  37 */     this.one = one;
/*  38 */     this.two = two;
/*     */   }
/*     */ 
/*     */   void setFieldInfos(FieldInfos fieldInfos)
/*     */   {
/*  43 */     super.setFieldInfos(fieldInfos);
/*  44 */     this.one.setFieldInfos(fieldInfos);
/*  45 */     this.two.setFieldInfos(fieldInfos);
/*     */   }
/*     */ 
/*     */   public void flush(Map<DocFieldConsumerPerThread, Collection<DocFieldConsumerPerField>> threadsAndFields, SegmentWriteState state)
/*     */     throws IOException
/*     */   {
/*  51 */     Map oneThreadsAndFields = new HashMap();
/*  52 */     Map twoThreadsAndFields = new HashMap();
/*     */ 
/*  54 */     for (Map.Entry entry : threadsAndFields.entrySet())
/*     */     {
/*  56 */       DocFieldConsumersPerThread perThread = (DocFieldConsumersPerThread)entry.getKey();
/*     */ 
/*  58 */       Collection fields = (Collection)entry.getValue();
/*     */ 
/*  60 */       Iterator fieldsIt = fields.iterator();
/*  61 */       Collection oneFields = new HashSet();
/*  62 */       Collection twoFields = new HashSet();
/*  63 */       while (fieldsIt.hasNext()) {
/*  64 */         DocFieldConsumersPerField perField = (DocFieldConsumersPerField)fieldsIt.next();
/*  65 */         oneFields.add(perField.one);
/*  66 */         twoFields.add(perField.two);
/*     */       }
/*     */ 
/*  69 */       oneThreadsAndFields.put(perThread.one, oneFields);
/*  70 */       twoThreadsAndFields.put(perThread.two, twoFields);
/*     */     }
/*     */ 
/*  74 */     this.one.flush(oneThreadsAndFields, state);
/*  75 */     this.two.flush(twoThreadsAndFields, state);
/*     */   }
/*     */ 
/*     */   public void closeDocStore(SegmentWriteState state) throws IOException
/*     */   {
/*     */     try {
/*  81 */       this.one.closeDocStore(state);
/*     */     } finally {
/*  83 */       this.two.closeDocStore(state);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void abort()
/*     */   {
/*     */     try {
/*  90 */       this.one.abort();
/*     */     } finally {
/*  92 */       this.two.abort();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean freeRAM()
/*     */   {
/*  98 */     boolean any = this.one.freeRAM();
/*  99 */     any |= this.two.freeRAM();
/* 100 */     return any;
/*     */   }
/*     */ 
/*     */   public DocFieldConsumerPerThread addThread(DocFieldProcessorPerThread docFieldProcessorPerThread) throws IOException
/*     */   {
/* 105 */     return new DocFieldConsumersPerThread(docFieldProcessorPerThread, this, this.one.addThread(docFieldProcessorPerThread), this.two.addThread(docFieldProcessorPerThread));
/*     */   }
/*     */ 
/*     */   synchronized PerDoc getPerDoc()
/*     */   {
/* 113 */     if (this.freeCount == 0) {
/* 114 */       this.allocCount += 1;
/* 115 */       if (this.allocCount > this.docFreeList.length)
/*     */       {
/* 119 */         assert (this.allocCount == 1 + this.docFreeList.length);
/* 120 */         this.docFreeList = new PerDoc[ArrayUtil.getNextSize(this.allocCount)];
/*     */       }
/* 122 */       return new PerDoc();
/*     */     }
/* 124 */     return this.docFreeList[(--this.freeCount)];
/*     */   }
/*     */ 
/*     */   synchronized void freePerDoc(PerDoc perDoc) {
/* 128 */     assert (this.freeCount < this.docFreeList.length);
/* 129 */     this.docFreeList[(this.freeCount++)] = perDoc;
/*     */   }
/*     */   class PerDoc extends DocumentsWriter.DocWriter {
/*     */     DocumentsWriter.DocWriter one;
/*     */     DocumentsWriter.DocWriter two;
/*     */ 
/*     */     PerDoc() {
/*     */     }
/*     */     public long sizeInBytes() {
/* 139 */       return this.one.sizeInBytes() + this.two.sizeInBytes();
/*     */     }
/*     */ 
/*     */     public void finish() throws IOException
/*     */     {
/*     */       try {
/*     */         try {
/* 146 */           this.one.finish();
/*     */         } finally {
/* 148 */           this.two.finish();
/*     */         }
/*     */       } finally {
/* 151 */         DocFieldConsumers.this.freePerDoc(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void abort()
/*     */     {
/*     */       try {
/*     */         try {
/* 159 */           this.one.abort();
/*     */         } finally {
/* 161 */           this.two.abort();
/*     */         }
/*     */       } finally {
/* 164 */         DocFieldConsumers.this.freePerDoc(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldConsumers
 * JD-Core Version:    0.6.2
 */