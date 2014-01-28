/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ final class TermsHashPerThread extends InvertedDocConsumerPerThread
/*     */ {
/*     */   final TermsHash termsHash;
/*     */   final TermsHashConsumerPerThread consumer;
/*     */   final TermsHashPerThread nextPerThread;
/*     */   final CharBlockPool charPool;
/*     */   final IntBlockPool intPool;
/*     */   final ByteBlockPool bytePool;
/*     */   final boolean primary;
/*     */   final DocumentsWriter.DocState docState;
/*  34 */   final RawPostingList[] freePostings = new RawPostingList[256];
/*     */   int freePostingsCount;
/*     */ 
/*     */   public TermsHashPerThread(DocInverterPerThread docInverterPerThread, TermsHash termsHash, TermsHash nextTermsHash, TermsHashPerThread primaryPerThread)
/*     */   {
/*  38 */     this.docState = docInverterPerThread.docState;
/*     */ 
/*  40 */     this.termsHash = termsHash;
/*  41 */     this.consumer = termsHash.consumer.addThread(this);
/*     */ 
/*  43 */     if (nextTermsHash != null)
/*     */     {
/*  45 */       this.charPool = new CharBlockPool(termsHash.docWriter);
/*  46 */       this.primary = true;
/*     */     } else {
/*  48 */       this.charPool = primaryPerThread.charPool;
/*  49 */       this.primary = false;
/*     */     }
/*     */ 
/*  52 */     this.intPool = new IntBlockPool(termsHash.docWriter, termsHash.trackAllocations);
/*  53 */     this.bytePool = new ByteBlockPool(termsHash.docWriter.byteBlockAllocator, termsHash.trackAllocations);
/*     */ 
/*  55 */     if (nextTermsHash != null)
/*  56 */       this.nextPerThread = nextTermsHash.addThread(docInverterPerThread, this);
/*     */     else
/*  58 */       this.nextPerThread = null;
/*     */   }
/*     */ 
/*     */   InvertedDocConsumerPerField addField(DocInverterPerField docInverterPerField, FieldInfo fieldInfo)
/*     */   {
/*  63 */     return new TermsHashPerField(docInverterPerField, this, this.nextPerThread, fieldInfo);
/*     */   }
/*     */ 
/*     */   public synchronized void abort()
/*     */   {
/*  68 */     reset(true);
/*  69 */     this.consumer.abort();
/*  70 */     if (this.nextPerThread != null)
/*  71 */       this.nextPerThread.abort();
/*     */   }
/*     */ 
/*     */   void morePostings() throws IOException
/*     */   {
/*  76 */     assert (this.freePostingsCount == 0);
/*  77 */     this.termsHash.getPostings(this.freePostings);
/*  78 */     this.freePostingsCount = this.freePostings.length;
/*  79 */     assert (noNullPostings(this.freePostings, this.freePostingsCount, "consumer=" + this.consumer));
/*     */   }
/*     */ 
/*     */   private static boolean noNullPostings(RawPostingList[] postings, int count, String details) {
/*  83 */     for (int i = 0; i < count; i++)
/*  84 */       assert (postings[i] != null) : ("postings[" + i + "] of " + count + " is null: " + details);
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   public void startDocument() throws IOException
/*     */   {
/*  90 */     this.consumer.startDocument();
/*  91 */     if (this.nextPerThread != null)
/*  92 */       this.nextPerThread.consumer.startDocument();
/*     */   }
/*     */ 
/*     */   public DocumentsWriter.DocWriter finishDocument() throws IOException
/*     */   {
/*  97 */     DocumentsWriter.DocWriter doc = this.consumer.finishDocument();
/*     */     DocumentsWriter.DocWriter doc2;
/*     */     DocumentsWriter.DocWriter doc2;
/* 100 */     if (this.nextPerThread != null)
/* 101 */       doc2 = this.nextPerThread.consumer.finishDocument();
/*     */     else
/* 103 */       doc2 = null;
/* 104 */     if (doc == null) {
/* 105 */       return doc2;
/*     */     }
/* 107 */     doc.setNext(doc2);
/* 108 */     return doc;
/*     */   }
/*     */ 
/*     */   void reset(boolean recyclePostings)
/*     */   {
/* 114 */     this.intPool.reset();
/* 115 */     this.bytePool.reset();
/*     */ 
/* 117 */     if (this.primary) {
/* 118 */       this.charPool.reset();
/*     */     }
/* 120 */     if (recyclePostings) {
/* 121 */       this.termsHash.recyclePostings(this.freePostings, this.freePostingsCount);
/* 122 */       this.freePostingsCount = 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermsHashPerThread
 * JD-Core Version:    0.6.2
 */