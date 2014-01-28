/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ 
/*     */ final class TermsHash extends InvertedDocConsumer
/*     */ {
/*     */   final TermsHashConsumer consumer;
/*     */   final TermsHash nextTermsHash;
/*     */   final int bytesPerPosting;
/*     */   final int postingsFreeChunk;
/*     */   final DocumentsWriter docWriter;
/*  46 */   private RawPostingList[] postingsFreeList = new RawPostingList[1];
/*     */   private int postingsFreeCount;
/*     */   private int postingsAllocCount;
/*     */   boolean trackAllocations;
/*     */ 
/*     */   public TermsHash(DocumentsWriter docWriter, boolean trackAllocations, TermsHashConsumer consumer, TermsHash nextTermsHash)
/*     */   {
/*  52 */     this.docWriter = docWriter;
/*  53 */     this.consumer = consumer;
/*  54 */     this.nextTermsHash = nextTermsHash;
/*  55 */     this.trackAllocations = trackAllocations;
/*     */ 
/*  62 */     this.bytesPerPosting = (consumer.bytesPerPosting() + 4 * DocumentsWriter.POINTER_NUM_BYTE);
/*  63 */     this.postingsFreeChunk = (32768 / this.bytesPerPosting);
/*     */   }
/*     */ 
/*     */   InvertedDocConsumerPerThread addThread(DocInverterPerThread docInverterPerThread)
/*     */   {
/*  68 */     return new TermsHashPerThread(docInverterPerThread, this, this.nextTermsHash, null);
/*     */   }
/*     */ 
/*     */   TermsHashPerThread addThread(DocInverterPerThread docInverterPerThread, TermsHashPerThread primaryPerThread) {
/*  72 */     return new TermsHashPerThread(docInverterPerThread, this, this.nextTermsHash, primaryPerThread);
/*     */   }
/*     */ 
/*     */   void setFieldInfos(FieldInfos fieldInfos)
/*     */   {
/*  77 */     this.fieldInfos = fieldInfos;
/*  78 */     this.consumer.setFieldInfos(fieldInfos);
/*     */   }
/*     */ 
/*     */   public synchronized void abort()
/*     */   {
/*  83 */     this.consumer.abort();
/*  84 */     if (this.nextTermsHash != null)
/*  85 */       this.nextTermsHash.abort();
/*     */   }
/*     */ 
/*     */   void shrinkFreePostings(Map<InvertedDocConsumerPerThread, Collection<InvertedDocConsumerPerField>> threadsAndFields, SegmentWriteState state)
/*     */   {
/*  90 */     assert (this.postingsFreeCount == this.postingsAllocCount) : (Thread.currentThread().getName() + ": postingsFreeCount=" + this.postingsFreeCount + " postingsAllocCount=" + this.postingsAllocCount + " consumer=" + this.consumer);
/*     */ 
/*  92 */     int newSize = 1;
/*  93 */     if (1 != this.postingsFreeList.length) {
/*  94 */       if (this.postingsFreeCount > 1) {
/*  95 */         if (this.trackAllocations) {
/*  96 */           this.docWriter.bytesAllocated(-(this.postingsFreeCount - 1) * this.bytesPerPosting);
/*     */         }
/*     */ 
/*  99 */         this.postingsFreeCount = 1;
/* 100 */         this.postingsAllocCount = 1;
/*     */       }
/*     */ 
/* 103 */       RawPostingList[] newArray = new RawPostingList[1];
/* 104 */       System.arraycopy(this.postingsFreeList, 0, newArray, 0, this.postingsFreeCount);
/* 105 */       this.postingsFreeList = newArray;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void closeDocStore(SegmentWriteState state) throws IOException
/*     */   {
/* 111 */     this.consumer.closeDocStore(state);
/* 112 */     if (this.nextTermsHash != null)
/* 113 */       this.nextTermsHash.closeDocStore(state);
/*     */   }
/*     */ 
/*     */   synchronized void flush(Map<InvertedDocConsumerPerThread, Collection<InvertedDocConsumerPerField>> threadsAndFields, SegmentWriteState state) throws IOException
/*     */   {
/* 118 */     Map childThreadsAndFields = new HashMap();
/*     */     Map nextThreadsAndFields;
/*     */     Map nextThreadsAndFields;
/* 121 */     if (this.nextTermsHash != null)
/* 122 */       nextThreadsAndFields = new HashMap();
/*     */     else {
/* 124 */       nextThreadsAndFields = null;
/*     */     }
/* 126 */     for (Map.Entry entry : threadsAndFields.entrySet())
/*     */     {
/* 128 */       TermsHashPerThread perThread = (TermsHashPerThread)entry.getKey();
/*     */ 
/* 130 */       Collection fields = (Collection)entry.getValue();
/*     */ 
/* 132 */       Iterator fieldsIt = fields.iterator();
/* 133 */       Collection childFields = new HashSet();
/*     */       Collection nextChildFields;
/*     */       Collection nextChildFields;
/* 136 */       if (this.nextTermsHash != null)
/* 137 */         nextChildFields = new HashSet();
/*     */       else {
/* 139 */         nextChildFields = null;
/*     */       }
/* 141 */       while (fieldsIt.hasNext()) {
/* 142 */         TermsHashPerField perField = (TermsHashPerField)fieldsIt.next();
/* 143 */         childFields.add(perField.consumer);
/* 144 */         if (this.nextTermsHash != null) {
/* 145 */           nextChildFields.add(perField.nextPerField);
/*     */         }
/*     */       }
/* 148 */       childThreadsAndFields.put(perThread.consumer, childFields);
/* 149 */       if (this.nextTermsHash != null) {
/* 150 */         nextThreadsAndFields.put(perThread.nextPerThread, nextChildFields);
/*     */       }
/*     */     }
/* 153 */     this.consumer.flush(childThreadsAndFields, state);
/*     */ 
/* 155 */     shrinkFreePostings(threadsAndFields, state);
/*     */ 
/* 157 */     if (this.nextTermsHash != null)
/* 158 */       this.nextTermsHash.flush(nextThreadsAndFields, state);
/*     */   }
/*     */ 
/*     */   public synchronized boolean freeRAM()
/*     */   {
/* 164 */     if (!this.trackAllocations)
/* 165 */       return false;
/*     */     int numToFree;
/*     */     int numToFree;
/* 169 */     if (this.postingsFreeCount >= this.postingsFreeChunk)
/* 170 */       numToFree = this.postingsFreeChunk;
/*     */     else
/* 172 */       numToFree = this.postingsFreeCount;
/* 173 */     boolean any = numToFree > 0;
/* 174 */     if (any) {
/* 175 */       Arrays.fill(this.postingsFreeList, this.postingsFreeCount - numToFree, this.postingsFreeCount, null);
/* 176 */       this.postingsFreeCount -= numToFree;
/* 177 */       this.postingsAllocCount -= numToFree;
/* 178 */       this.docWriter.bytesAllocated(-numToFree * this.bytesPerPosting);
/* 179 */       any = true;
/*     */     }
/*     */ 
/* 182 */     if (this.nextTermsHash != null) {
/* 183 */       any |= this.nextTermsHash.freeRAM();
/*     */     }
/* 185 */     return any;
/*     */   }
/*     */ 
/*     */   public synchronized void recyclePostings(RawPostingList[] postings, int numPostings)
/*     */   {
/* 190 */     assert (postings.length >= numPostings);
/*     */ 
/* 195 */     assert (this.postingsFreeCount + numPostings <= this.postingsFreeList.length);
/* 196 */     System.arraycopy(postings, 0, this.postingsFreeList, this.postingsFreeCount, numPostings);
/* 197 */     this.postingsFreeCount += numPostings;
/*     */   }
/*     */ 
/*     */   public synchronized void getPostings(RawPostingList[] postings)
/*     */   {
/* 202 */     assert (this.docWriter.writer.testPoint("TermsHash.getPostings start"));
/*     */ 
/* 204 */     assert (this.postingsFreeCount <= this.postingsFreeList.length);
/* 205 */     assert (this.postingsFreeCount <= this.postingsAllocCount) : ("postingsFreeCount=" + this.postingsFreeCount + " postingsAllocCount=" + this.postingsAllocCount);
/*     */     int numToCopy;
/*     */     int numToCopy;
/* 208 */     if (this.postingsFreeCount < postings.length)
/* 209 */       numToCopy = this.postingsFreeCount;
/*     */     else
/* 211 */       numToCopy = postings.length;
/* 212 */     int start = this.postingsFreeCount - numToCopy;
/* 213 */     assert (start >= 0);
/* 214 */     assert (start + numToCopy <= this.postingsFreeList.length);
/* 215 */     assert (numToCopy <= postings.length);
/* 216 */     System.arraycopy(this.postingsFreeList, start, postings, 0, numToCopy);
/*     */ 
/* 220 */     if (numToCopy != postings.length) {
/* 221 */       int extra = postings.length - numToCopy;
/* 222 */       int newPostingsAllocCount = this.postingsAllocCount + extra;
/*     */ 
/* 224 */       this.consumer.createPostings(postings, numToCopy, extra);
/* 225 */       assert (this.docWriter.writer.testPoint("TermsHash.getPostings after create"));
/* 226 */       this.postingsAllocCount += extra;
/*     */ 
/* 228 */       if (this.trackAllocations) {
/* 229 */         this.docWriter.bytesAllocated(extra * this.bytesPerPosting);
/*     */       }
/* 231 */       if (newPostingsAllocCount > this.postingsFreeList.length)
/*     */       {
/* 234 */         this.postingsFreeList = new RawPostingList[ArrayUtil.getNextSize(newPostingsAllocCount)];
/*     */       }
/*     */     }
/* 237 */     this.postingsFreeCount -= numToCopy;
/*     */ 
/* 239 */     if (this.trackAllocations)
/* 240 */       this.docWriter.bytesUsed(postings.length * this.bytesPerPosting);
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermsHash
 * JD-Core Version:    0.6.2
 */