/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.apache.lucene.util.UnicodeUtil.UTF8Result;
/*     */ 
/*     */ final class FreqProxTermsWriter extends TermsHashConsumer
/*     */ {
/*     */   private byte[] payloadBuffer;
/*     */   final UnicodeUtil.UTF8Result termsUTF8;
/*     */ 
/*     */   FreqProxTermsWriter()
/*     */   {
/* 274 */     this.termsUTF8 = new UnicodeUtil.UTF8Result();
/*     */   }
/*     */ 
/*     */   public TermsHashConsumerPerThread addThread(TermsHashPerThread perThread)
/*     */   {
/*  33 */     return new FreqProxTermsWriterPerThread(perThread);
/*     */   }
/*     */ 
/*     */   void createPostings(RawPostingList[] postings, int start, int count)
/*     */   {
/*  38 */     int end = start + count;
/*  39 */     for (int i = start; i < end; i++)
/*  40 */       postings[i] = new PostingList();
/*     */   }
/*     */ 
/*     */   private static int compareText(char[] text1, int pos1, char[] text2, int pos2) {
/*     */     while (true) {
/*  45 */       char c1 = text1[(pos1++)];
/*  46 */       char c2 = text2[(pos2++)];
/*  47 */       if (c1 != c2) {
/*  48 */         if (65535 == c2)
/*  49 */           return 1;
/*  50 */         if (65535 == c1) {
/*  51 */           return -1;
/*     */         }
/*  53 */         return c1 - c2;
/*  54 */       }if (65535 == c1)
/*  55 */         return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   void closeDocStore(SegmentWriteState state)
/*     */   {
/*     */   }
/*     */ 
/*     */   void abort()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void flush(Map<TermsHashConsumerPerThread, Collection<TermsHashConsumerPerField>> threadsAndFields, SegmentWriteState state)
/*     */     throws IOException
/*     */   {
/*  75 */     List allFields = new ArrayList();
/*     */ 
/*  77 */     for (Map.Entry entry : threadsAndFields.entrySet())
/*     */     {
/*  79 */       Collection fields = (Collection)entry.getValue();
/*     */ 
/*  82 */       for (TermsHashConsumerPerField i : fields) {
/*  83 */         FreqProxTermsWriterPerField perField = (FreqProxTermsWriterPerField)i;
/*  84 */         if (perField.termsHashPerField.numPostings > 0) {
/*  85 */           allFields.add(perField);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  90 */     Collections.sort(allFields);
/*  91 */     int numAllFields = allFields.size();
/*     */ 
/*  94 */     FormatPostingsFieldsConsumer consumer = new FormatPostingsFieldsWriter(state, this.fieldInfos);
/*     */ 
/* 107 */     int start = 0;
/* 108 */     while (start < numAllFields) {
/* 109 */       FieldInfo fieldInfo = ((FreqProxTermsWriterPerField)allFields.get(start)).fieldInfo;
/* 110 */       String fieldName = fieldInfo.name;
/*     */ 
/* 112 */       int end = start + 1;
/* 113 */       while ((end < numAllFields) && (((FreqProxTermsWriterPerField)allFields.get(end)).fieldInfo.name.equals(fieldName))) {
/* 114 */         end++;
/*     */       }
/* 116 */       FreqProxTermsWriterPerField[] fields = new FreqProxTermsWriterPerField[end - start];
/* 117 */       for (int i = start; i < end; i++) {
/* 118 */         fields[(i - start)] = ((FreqProxTermsWriterPerField)allFields.get(i));
/*     */ 
/* 122 */         fieldInfo.storePayloads |= fields[(i - start)].hasPayloads;
/*     */       }
/*     */ 
/* 127 */       appendPostings(fields, consumer);
/*     */ 
/* 129 */       for (int i = 0; i < fields.length; i++) {
/* 130 */         TermsHashPerField perField = fields[i].termsHashPerField;
/* 131 */         int numPostings = perField.numPostings;
/* 132 */         perField.reset();
/* 133 */         perField.shrinkHash(numPostings);
/* 134 */         fields[i].reset();
/*     */       }
/*     */ 
/* 137 */       start = end;
/*     */     }
/*     */ 
/* 140 */     for (Map.Entry entry : threadsAndFields.entrySet()) {
/* 141 */       FreqProxTermsWriterPerThread perThread = (FreqProxTermsWriterPerThread)entry.getKey();
/* 142 */       perThread.termsHashPerThread.reset(true);
/*     */     }
/*     */ 
/* 145 */     consumer.finish();
/*     */   }
/*     */ 
/*     */   void appendPostings(FreqProxTermsWriterPerField[] fields, FormatPostingsFieldsConsumer consumer)
/*     */     throws CorruptIndexException, IOException
/*     */   {
/* 157 */     int numFields = fields.length;
/*     */ 
/* 159 */     FreqProxFieldMergeState[] mergeStates = new FreqProxFieldMergeState[numFields];
/*     */ 
/* 161 */     for (int i = 0; i < numFields; i++) {
/* 162 */       FreqProxFieldMergeState fms = mergeStates[i] =  = new FreqProxFieldMergeState(fields[i]);
/*     */ 
/* 164 */       assert (fms.field.fieldInfo == fields[0].fieldInfo);
/*     */ 
/* 167 */       boolean result = fms.nextTerm();
/* 168 */       assert (result);
/*     */     }
/*     */ 
/* 171 */     FormatPostingsTermsConsumer termsConsumer = consumer.addField(fields[0].fieldInfo);
/*     */ 
/* 173 */     FreqProxFieldMergeState[] termStates = new FreqProxFieldMergeState[numFields];
/*     */ 
/* 175 */     boolean currentFieldOmitTermFreqAndPositions = fields[0].fieldInfo.omitTermFreqAndPositions;
/*     */ 
/* 177 */     while (numFields > 0)
/*     */     {
/* 180 */       termStates[0] = mergeStates[0];
/* 181 */       int numToMerge = 1;
/*     */ 
/* 183 */       for (int i = 1; i < numFields; i++) {
/* 184 */         char[] text = mergeStates[i].text;
/* 185 */         int textOffset = mergeStates[i].textOffset;
/* 186 */         int cmp = compareText(text, textOffset, termStates[0].text, termStates[0].textOffset);
/*     */ 
/* 188 */         if (cmp < 0) {
/* 189 */           termStates[0] = mergeStates[i];
/* 190 */           numToMerge = 1;
/* 191 */         } else if (cmp == 0) {
/* 192 */           termStates[(numToMerge++)] = mergeStates[i];
/*     */         }
/*     */       }
/* 195 */       FormatPostingsDocsConsumer docConsumer = termsConsumer.addTerm(termStates[0].text, termStates[0].textOffset);
/*     */ 
/* 200 */       while (numToMerge > 0)
/*     */       {
/* 202 */         FreqProxFieldMergeState minState = termStates[0];
/* 203 */         for (int i = 1; i < numToMerge; i++) {
/* 204 */           if (termStates[i].docID < minState.docID)
/* 205 */             minState = termStates[i];
/*     */         }
/* 207 */         int termDocFreq = minState.termFreq;
/*     */ 
/* 209 */         FormatPostingsPositionsConsumer posConsumer = docConsumer.addDoc(minState.docID, termDocFreq);
/*     */ 
/* 211 */         ByteSliceReader prox = minState.prox;
/*     */ 
/* 216 */         if (!currentFieldOmitTermFreqAndPositions)
/*     */         {
/* 219 */           int position = 0;
/* 220 */           for (int j = 0; j < termDocFreq; j++) {
/* 221 */             int code = prox.readVInt();
/* 222 */             position += (code >> 1);
/*     */             int payloadLength;
/* 225 */             if ((code & 0x1) != 0)
/*     */             {
/* 227 */               int payloadLength = prox.readVInt();
/*     */ 
/* 229 */               if ((this.payloadBuffer == null) || (this.payloadBuffer.length < payloadLength)) {
/* 230 */                 this.payloadBuffer = new byte[payloadLength];
/*     */               }
/* 232 */               prox.readBytes(this.payloadBuffer, 0, payloadLength);
/*     */             }
/*     */             else {
/* 235 */               payloadLength = 0;
/*     */             }
/* 237 */             posConsumer.addPosition(position, this.payloadBuffer, 0, payloadLength);
/*     */           }
/*     */ 
/* 240 */           posConsumer.finish();
/*     */         }
/*     */ 
/* 243 */         if (!minState.nextDoc())
/*     */         {
/* 246 */           int upto = 0;
/* 247 */           for (int i = 0; i < numToMerge; i++)
/* 248 */             if (termStates[i] != minState)
/* 249 */               termStates[(upto++)] = termStates[i];
/* 250 */           numToMerge--;
/* 251 */           assert (upto == numToMerge);
/*     */ 
/* 255 */           if (!minState.nextTerm())
/*     */           {
/* 258 */             upto = 0;
/* 259 */             for (int i = 0; i < numFields; i++)
/* 260 */               if (mergeStates[i] != minState)
/* 261 */                 mergeStates[(upto++)] = mergeStates[i];
/* 262 */             numFields--;
/* 263 */             assert (upto == numFields);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 268 */       docConsumer.finish();
/*     */     }
/*     */ 
/* 271 */     termsConsumer.finish();
/*     */   }
/*     */ 
/*     */   int bytesPerPosting()
/*     */   {
/* 285 */     return 36;
/*     */   }
/*     */ 
/*     */   static final class PostingList extends RawPostingList
/*     */   {
/*     */     int docFreq;
/*     */     int lastDocID;
/*     */     int lastDocCode;
/*     */     int lastPosition;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FreqProxTermsWriter
 * JD-Core Version:    0.6.2
 */