/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
/*     */ import org.apache.lucene.document.Fieldable;
/*     */ import org.apache.lucene.store.IndexOutput;
/*     */ import org.apache.lucene.store.RAMOutputStream;
/*     */ import org.apache.lucene.util.AttributeSource;
/*     */ import org.apache.lucene.util.UnicodeUtil;
/*     */ import org.apache.lucene.util.UnicodeUtil.UTF8Result;
/*     */ 
/*     */ final class TermVectorsTermsWriterPerField extends TermsHashConsumerPerField
/*     */ {
/*     */   final TermVectorsTermsWriterPerThread perThread;
/*     */   final TermsHashPerField termsHashPerField;
/*     */   final TermVectorsTermsWriter termsWriter;
/*     */   final FieldInfo fieldInfo;
/*     */   final DocumentsWriter.DocState docState;
/*     */   final FieldInvertState fieldState;
/*     */   boolean doVectors;
/*     */   boolean doVectorPositions;
/*     */   boolean doVectorOffsets;
/*     */   int maxNumPostings;
/*  41 */   OffsetAttribute offsetAttribute = null;
/*     */ 
/*     */   public TermVectorsTermsWriterPerField(TermsHashPerField termsHashPerField, TermVectorsTermsWriterPerThread perThread, FieldInfo fieldInfo) {
/*  44 */     this.termsHashPerField = termsHashPerField;
/*  45 */     this.perThread = perThread;
/*  46 */     this.termsWriter = perThread.termsWriter;
/*  47 */     this.fieldInfo = fieldInfo;
/*  48 */     this.docState = termsHashPerField.docState;
/*  49 */     this.fieldState = termsHashPerField.fieldState;
/*     */   }
/*     */ 
/*     */   int getStreamCount()
/*     */   {
/*  54 */     return 2;
/*     */   }
/*     */ 
/*     */   boolean start(Fieldable[] fields, int count)
/*     */   {
/*  59 */     this.doVectors = false;
/*  60 */     this.doVectorPositions = false;
/*  61 */     this.doVectorOffsets = false;
/*     */ 
/*  63 */     for (int i = 0; i < count; i++) {
/*  64 */       Fieldable field = fields[i];
/*  65 */       if ((field.isIndexed()) && (field.isTermVectorStored())) {
/*  66 */         this.doVectors = true;
/*  67 */         this.doVectorPositions |= field.isStorePositionWithTermVector();
/*  68 */         this.doVectorOffsets |= field.isStoreOffsetWithTermVector();
/*     */       }
/*     */     }
/*     */ 
/*  72 */     if (this.doVectors) {
/*  73 */       if (this.perThread.doc == null) {
/*  74 */         this.perThread.doc = this.termsWriter.getPerDoc();
/*  75 */         this.perThread.doc.docID = this.docState.docID;
/*  76 */         assert (this.perThread.doc.numVectorFields == 0);
/*  77 */         assert (0L == this.perThread.doc.perDocTvf.length());
/*  78 */         if ((!$assertionsDisabled) && (0L != this.perThread.doc.perDocTvf.getFilePointer())) throw new AssertionError(); 
/*     */       }
/*  80 */       else { assert (this.perThread.doc.docID == this.docState.docID);
/*     */ 
/*  82 */         if (this.termsHashPerField.numPostings != 0)
/*     */         {
/*  86 */           this.termsHashPerField.reset();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  93 */     return this.doVectors;
/*     */   }
/*     */ 
/*     */   public void abort()
/*     */   {
/*     */   }
/*     */ 
/*     */   void finish()
/*     */     throws IOException
/*     */   {
/* 105 */     assert (this.docState.testPoint("TermVectorsTermsWriterPerField.finish start"));
/*     */ 
/* 107 */     int numPostings = this.termsHashPerField.numPostings;
/*     */ 
/* 109 */     assert (numPostings >= 0);
/*     */ 
/* 111 */     if ((!this.doVectors) || (numPostings == 0)) {
/* 112 */       return;
/*     */     }
/* 114 */     if (numPostings > this.maxNumPostings) {
/* 115 */       this.maxNumPostings = numPostings;
/*     */     }
/* 117 */     IndexOutput tvf = this.perThread.doc.perDocTvf;
/*     */ 
/* 123 */     assert (this.fieldInfo.storeTermVector);
/* 124 */     assert (this.perThread.vectorFieldsInOrder(this.fieldInfo));
/*     */ 
/* 126 */     this.perThread.doc.addField(this.termsHashPerField.fieldInfo.number);
/*     */ 
/* 128 */     RawPostingList[] postings = this.termsHashPerField.sortPostings();
/*     */ 
/* 130 */     tvf.writeVInt(numPostings);
/* 131 */     byte bits = 0;
/* 132 */     if (this.doVectorPositions)
/* 133 */       bits = (byte)(bits | 0x1);
/* 134 */     if (this.doVectorOffsets)
/* 135 */       bits = (byte)(bits | 0x2);
/* 136 */     tvf.writeByte(bits);
/*     */ 
/* 138 */     int encoderUpto = 0;
/* 139 */     int lastTermBytesCount = 0;
/*     */ 
/* 141 */     ByteSliceReader reader = this.perThread.vectorSliceReader;
/* 142 */     char[][] charBuffers = this.perThread.termsHashPerThread.charPool.buffers;
/* 143 */     for (int j = 0; j < numPostings; j++) {
/* 144 */       TermVectorsTermsWriter.PostingList posting = (TermVectorsTermsWriter.PostingList)postings[j];
/* 145 */       int freq = posting.freq;
/*     */ 
/* 147 */       char[] text2 = charBuffers[(posting.textStart >> 14)];
/* 148 */       int start2 = posting.textStart & 0x3FFF;
/*     */ 
/* 152 */       UnicodeUtil.UTF8Result utf8Result = this.perThread.utf8Results[encoderUpto];
/*     */ 
/* 155 */       UnicodeUtil.UTF16toUTF8(text2, start2, utf8Result);
/* 156 */       int termBytesCount = utf8Result.length;
/*     */ 
/* 161 */       int prefix = 0;
/* 162 */       if (j > 0) {
/* 163 */         byte[] lastTermBytes = this.perThread.utf8Results[(1 - encoderUpto)].result;
/* 164 */         byte[] termBytes = this.perThread.utf8Results[encoderUpto].result;
/* 165 */         while ((prefix < lastTermBytesCount) && (prefix < termBytesCount) && 
/* 166 */           (lastTermBytes[prefix] == termBytes[prefix]))
/*     */         {
/* 168 */           prefix++;
/*     */         }
/*     */       }
/* 171 */       encoderUpto = 1 - encoderUpto;
/* 172 */       lastTermBytesCount = termBytesCount;
/*     */ 
/* 174 */       int suffix = termBytesCount - prefix;
/* 175 */       tvf.writeVInt(prefix);
/* 176 */       tvf.writeVInt(suffix);
/* 177 */       tvf.writeBytes(utf8Result.result, prefix, suffix);
/* 178 */       tvf.writeVInt(freq);
/*     */ 
/* 180 */       if (this.doVectorPositions) {
/* 181 */         this.termsHashPerField.initReader(reader, posting, 0);
/* 182 */         reader.writeTo(tvf);
/*     */       }
/*     */ 
/* 185 */       if (this.doVectorOffsets) {
/* 186 */         this.termsHashPerField.initReader(reader, posting, 1);
/* 187 */         reader.writeTo(tvf);
/*     */       }
/*     */     }
/*     */ 
/* 191 */     this.termsHashPerField.reset();
/* 192 */     this.perThread.termsHashPerThread.reset(false);
/*     */   }
/*     */ 
/*     */   void shrinkHash() {
/* 196 */     this.termsHashPerField.shrinkHash(this.maxNumPostings);
/* 197 */     this.maxNumPostings = 0;
/*     */   }
/*     */ 
/*     */   void start(Fieldable f)
/*     */   {
/* 202 */     if (this.doVectorOffsets)
/* 203 */       this.offsetAttribute = ((OffsetAttribute)this.fieldState.attributeSource.addAttribute(OffsetAttribute.class));
/*     */     else
/* 205 */       this.offsetAttribute = null;
/*     */   }
/*     */ 
/*     */   void newTerm(RawPostingList p0)
/*     */   {
/* 212 */     assert (this.docState.testPoint("TermVectorsTermsWriterPerField.newTerm start"));
/*     */ 
/* 214 */     TermVectorsTermsWriter.PostingList p = (TermVectorsTermsWriter.PostingList)p0;
/*     */ 
/* 216 */     p.freq = 1;
/*     */ 
/* 218 */     if (this.doVectorOffsets) {
/* 219 */       int startOffset = this.fieldState.offset + this.offsetAttribute.startOffset();
/* 220 */       int endOffset = this.fieldState.offset + this.offsetAttribute.endOffset();
/*     */ 
/* 222 */       this.termsHashPerField.writeVInt(1, startOffset);
/* 223 */       this.termsHashPerField.writeVInt(1, endOffset - startOffset);
/* 224 */       p.lastOffset = endOffset;
/*     */     }
/*     */ 
/* 227 */     if (this.doVectorPositions) {
/* 228 */       this.termsHashPerField.writeVInt(0, this.fieldState.position);
/* 229 */       p.lastPosition = this.fieldState.position;
/*     */     }
/*     */   }
/*     */ 
/*     */   void addTerm(RawPostingList p0)
/*     */   {
/* 236 */     assert (this.docState.testPoint("TermVectorsTermsWriterPerField.addTerm start"));
/*     */ 
/* 238 */     TermVectorsTermsWriter.PostingList p = (TermVectorsTermsWriter.PostingList)p0;
/* 239 */     p.freq += 1;
/*     */ 
/* 241 */     if (this.doVectorOffsets) {
/* 242 */       int startOffset = this.fieldState.offset + this.offsetAttribute.startOffset();
/* 243 */       int endOffset = this.fieldState.offset + this.offsetAttribute.endOffset();
/*     */ 
/* 245 */       this.termsHashPerField.writeVInt(1, startOffset - p.lastOffset);
/* 246 */       this.termsHashPerField.writeVInt(1, endOffset - startOffset);
/* 247 */       p.lastOffset = endOffset;
/*     */     }
/*     */ 
/* 250 */     if (this.doVectorPositions) {
/* 251 */       this.termsHashPerField.writeVInt(0, this.fieldState.position - p.lastPosition);
/* 252 */       p.lastPosition = this.fieldState.position;
/*     */     }
/*     */   }
/*     */ 
/*     */   void skippingLongTerm()
/*     */   {
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.TermVectorsTermsWriterPerField
 * JD-Core Version:    0.6.2
 */