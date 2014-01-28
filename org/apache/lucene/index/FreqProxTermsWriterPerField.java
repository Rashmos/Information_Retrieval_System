/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.tokenattributes.PayloadAttribute;
/*     */ import org.apache.lucene.document.Fieldable;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ 
/*     */ final class FreqProxTermsWriterPerField extends TermsHashConsumerPerField
/*     */   implements Comparable<FreqProxTermsWriterPerField>
/*     */ {
/*     */   final FreqProxTermsWriterPerThread perThread;
/*     */   final TermsHashPerField termsHashPerField;
/*     */   final FieldInfo fieldInfo;
/*     */   final DocumentsWriter.DocState docState;
/*     */   final FieldInvertState fieldState;
/*     */   boolean omitTermFreqAndPositions;
/*     */   PayloadAttribute payloadAttribute;
/*     */   boolean hasPayloads;
/*     */ 
/*     */   public FreqProxTermsWriterPerField(TermsHashPerField termsHashPerField, FreqProxTermsWriterPerThread perThread, FieldInfo fieldInfo)
/*     */   {
/*  38 */     this.termsHashPerField = termsHashPerField;
/*  39 */     this.perThread = perThread;
/*  40 */     this.fieldInfo = fieldInfo;
/*  41 */     this.docState = termsHashPerField.docState;
/*  42 */     this.fieldState = termsHashPerField.fieldState;
/*  43 */     this.omitTermFreqAndPositions = fieldInfo.omitTermFreqAndPositions;
/*     */   }
/*     */ 
/*     */   int getStreamCount()
/*     */   {
/*  48 */     if (this.fieldInfo.omitTermFreqAndPositions) {
/*  49 */       return 1;
/*     */     }
/*  51 */     return 2;
/*     */   }
/*     */ 
/*     */   void finish()
/*     */   {
/*     */   }
/*     */ 
/*     */   void skippingLongTerm() throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public int compareTo(FreqProxTermsWriterPerField other) {
/*  63 */     return this.fieldInfo.name.compareTo(other.fieldInfo.name);
/*     */   }
/*     */ 
/*     */   void reset()
/*     */   {
/*  69 */     this.omitTermFreqAndPositions = this.fieldInfo.omitTermFreqAndPositions;
/*  70 */     this.payloadAttribute = null;
/*     */   }
/*     */ 
/*     */   boolean start(Fieldable[] fields, int count)
/*     */   {
/*  75 */     for (int i = 0; i < count; i++)
/*  76 */       if (fields[i].isIndexed())
/*  77 */         return true;
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */   void start(Fieldable f)
/*     */   {
/*  83 */     if (this.fieldState.attributeSource.hasAttribute(PayloadAttribute.class))
/*  84 */       this.payloadAttribute = ((PayloadAttribute)this.fieldState.attributeSource.getAttribute(PayloadAttribute.class));
/*     */     else
/*  86 */       this.payloadAttribute = null;
/*     */   }
/*     */ 
/*     */   final void writeProx(FreqProxTermsWriter.PostingList p, int proxCode)
/*     */   {
/*     */     Payload payload;
/*     */     Payload payload;
/*  92 */     if (this.payloadAttribute == null)
/*  93 */       payload = null;
/*     */     else {
/*  95 */       payload = this.payloadAttribute.getPayload();
/*     */     }
/*     */ 
/*  98 */     if ((payload != null) && (payload.length > 0)) {
/*  99 */       this.termsHashPerField.writeVInt(1, proxCode << 1 | 0x1);
/* 100 */       this.termsHashPerField.writeVInt(1, payload.length);
/* 101 */       this.termsHashPerField.writeBytes(1, payload.data, payload.offset, payload.length);
/* 102 */       this.hasPayloads = true;
/*     */     } else {
/* 104 */       this.termsHashPerField.writeVInt(1, proxCode << 1);
/* 105 */     }p.lastPosition = this.fieldState.position;
/*     */   }
/*     */ 
/*     */   final void newTerm(RawPostingList p0)
/*     */   {
/* 112 */     assert (this.docState.testPoint("FreqProxTermsWriterPerField.newTerm start"));
/* 113 */     FreqProxTermsWriter.PostingList p = (FreqProxTermsWriter.PostingList)p0;
/* 114 */     p.lastDocID = this.docState.docID;
/* 115 */     if (this.omitTermFreqAndPositions) {
/* 116 */       p.lastDocCode = this.docState.docID;
/*     */     } else {
/* 118 */       p.lastDocCode = (this.docState.docID << 1);
/* 119 */       p.docFreq = 1;
/* 120 */       writeProx(p, this.fieldState.position);
/*     */     }
/*     */   }
/*     */ 
/*     */   final void addTerm(RawPostingList p0)
/*     */   {
/* 127 */     assert (this.docState.testPoint("FreqProxTermsWriterPerField.addTerm start"));
/*     */ 
/* 129 */     FreqProxTermsWriter.PostingList p = (FreqProxTermsWriter.PostingList)p0;
/*     */ 
/* 131 */     assert ((this.omitTermFreqAndPositions) || (p.docFreq > 0));
/*     */ 
/* 133 */     if (this.omitTermFreqAndPositions) {
/* 134 */       if (this.docState.docID != p.lastDocID) {
/* 135 */         assert (this.docState.docID > p.lastDocID);
/* 136 */         this.termsHashPerField.writeVInt(0, p.lastDocCode);
/* 137 */         p.lastDocCode = (this.docState.docID - p.lastDocID);
/* 138 */         p.lastDocID = this.docState.docID;
/*     */       }
/*     */     }
/* 141 */     else if (this.docState.docID != p.lastDocID) {
/* 142 */       assert (this.docState.docID > p.lastDocID);
/*     */ 
/* 148 */       if (1 == p.docFreq) {
/* 149 */         this.termsHashPerField.writeVInt(0, p.lastDocCode | 0x1);
/*     */       } else {
/* 151 */         this.termsHashPerField.writeVInt(0, p.lastDocCode);
/* 152 */         this.termsHashPerField.writeVInt(0, p.docFreq);
/*     */       }
/* 154 */       p.docFreq = 1;
/* 155 */       p.lastDocCode = (this.docState.docID - p.lastDocID << 1);
/* 156 */       p.lastDocID = this.docState.docID;
/* 157 */       writeProx(p, this.fieldState.position);
/*     */     } else {
/* 159 */       p.docFreq += 1;
/* 160 */       writeProx(p, this.fieldState.position - p.lastPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void abort()
/*     */   {
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FreqProxTermsWriterPerField
 * JD-Core Version:    0.6.2
 */