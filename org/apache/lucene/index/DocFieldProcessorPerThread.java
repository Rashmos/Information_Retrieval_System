/*     */ package org.apache.lucene.index;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.document.Document;
/*     */ import org.apache.lucene.document.Fieldable;
/*     */ import org.apache.lucene.util.ArrayUtil;
/*     */ 
/*     */ final class DocFieldProcessorPerThread extends DocConsumerPerThread
/*     */ {
/*     */   float docBoost;
/*     */   int fieldGen;
/*     */   final DocFieldProcessor docFieldProcessor;
/*     */   final FieldInfos fieldInfos;
/*     */   final DocFieldConsumerPerThread consumer;
/*  46 */   DocFieldProcessorPerField[] fields = new DocFieldProcessorPerField[1];
/*     */   int fieldCount;
/*  50 */   DocFieldProcessorPerField[] fieldHash = new DocFieldProcessorPerField[2];
/*  51 */   int hashMask = 1;
/*     */   int totalFieldCount;
/*     */   final StoredFieldsWriterPerThread fieldsWriter;
/*     */   final DocumentsWriter.DocState docState;
/* 331 */   PerDoc[] docFreeList = new PerDoc[1];
/*     */   int freeCount;
/*     */   int allocCount;
/*     */ 
/*     */   public DocFieldProcessorPerThread(DocumentsWriterThreadState threadState, DocFieldProcessor docFieldProcessor)
/*     */     throws IOException
/*     */   {
/*  59 */     this.docState = threadState.docState;
/*  60 */     this.docFieldProcessor = docFieldProcessor;
/*  61 */     this.fieldInfos = docFieldProcessor.fieldInfos;
/*  62 */     this.consumer = docFieldProcessor.consumer.addThread(this);
/*  63 */     this.fieldsWriter = docFieldProcessor.fieldsWriter.addThread(this.docState);
/*     */   }
/*     */ 
/*     */   public void abort()
/*     */   {
/*  68 */     for (int i = 0; i < this.fieldHash.length; i++) {
/*  69 */       DocFieldProcessorPerField field = this.fieldHash[i];
/*  70 */       while (field != null) {
/*  71 */         DocFieldProcessorPerField next = field.next;
/*  72 */         field.abort();
/*  73 */         field = next;
/*     */       }
/*     */     }
/*  76 */     this.fieldsWriter.abort();
/*  77 */     this.consumer.abort();
/*     */   }
/*     */ 
/*     */   public Collection<DocFieldConsumerPerField> fields() {
/*  81 */     Collection fields = new HashSet();
/*  82 */     for (int i = 0; i < this.fieldHash.length; i++) {
/*  83 */       DocFieldProcessorPerField field = this.fieldHash[i];
/*  84 */       while (field != null) {
/*  85 */         fields.add(field.consumer);
/*  86 */         field = field.next;
/*     */       }
/*     */     }
/*  89 */     assert (fields.size() == this.totalFieldCount);
/*  90 */     return fields;
/*     */   }
/*     */ 
/*     */   void trimFields(SegmentWriteState state)
/*     */   {
/*  98 */     for (int i = 0; i < this.fieldHash.length; i++) {
/*  99 */       DocFieldProcessorPerField perField = this.fieldHash[i];
/* 100 */       DocFieldProcessorPerField lastPerField = null;
/*     */ 
/* 102 */       while (perField != null)
/*     */       {
/* 104 */         if (perField.lastGen == -1)
/*     */         {
/* 110 */           if (lastPerField == null)
/* 111 */             this.fieldHash[i] = perField.next;
/*     */           else {
/* 113 */             lastPerField.next = perField.next;
/*     */           }
/* 115 */           if (state.docWriter.infoStream != null) {
/* 116 */             state.docWriter.infoStream.println("  purge field=" + perField.fieldInfo.name);
/*     */           }
/* 118 */           this.totalFieldCount -= 1;
/*     */         }
/*     */         else
/*     */         {
/* 122 */           perField.lastGen = -1;
/* 123 */           lastPerField = perField;
/*     */         }
/*     */ 
/* 126 */         perField = perField.next;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void rehash() {
/* 132 */     int newHashSize = this.fieldHash.length * 2;
/* 133 */     assert (newHashSize > this.fieldHash.length);
/*     */ 
/* 135 */     DocFieldProcessorPerField[] newHashArray = new DocFieldProcessorPerField[newHashSize];
/*     */ 
/* 138 */     int newHashMask = newHashSize - 1;
/* 139 */     for (int j = 0; j < this.fieldHash.length; j++) {
/* 140 */       DocFieldProcessorPerField fp0 = this.fieldHash[j];
/* 141 */       while (fp0 != null) {
/* 142 */         int hashPos2 = fp0.fieldInfo.name.hashCode() & newHashMask;
/* 143 */         DocFieldProcessorPerField nextFP0 = fp0.next;
/* 144 */         fp0.next = newHashArray[hashPos2];
/* 145 */         newHashArray[hashPos2] = fp0;
/* 146 */         fp0 = nextFP0;
/*     */       }
/*     */     }
/*     */ 
/* 150 */     this.fieldHash = newHashArray;
/* 151 */     this.hashMask = newHashMask;
/*     */   }
/*     */ 
/*     */   public DocumentsWriter.DocWriter processDocument()
/*     */     throws IOException
/*     */   {
/* 157 */     this.consumer.startDocument();
/* 158 */     this.fieldsWriter.startDocument();
/*     */ 
/* 160 */     Document doc = this.docState.doc;
/*     */ 
/* 162 */     assert (this.docFieldProcessor.docWriter.writer.testPoint("DocumentsWriter.ThreadState.init start"));
/*     */ 
/* 164 */     this.fieldCount = 0;
/*     */ 
/* 166 */     int thisFieldGen = this.fieldGen++;
/*     */ 
/* 168 */     List docFields = doc.getFields();
/* 169 */     int numDocFields = docFields.size();
/*     */ 
/* 176 */     for (int i = 0; i < numDocFields; i++) {
/* 177 */       Fieldable field = (Fieldable)docFields.get(i);
/* 178 */       String fieldName = field.name();
/*     */ 
/* 181 */       int hashPos = fieldName.hashCode() & this.hashMask;
/* 182 */       DocFieldProcessorPerField fp = this.fieldHash[hashPos];
/* 183 */       while ((fp != null) && (!fp.fieldInfo.name.equals(fieldName))) {
/* 184 */         fp = fp.next;
/*     */       }
/* 186 */       if (fp == null)
/*     */       {
/* 193 */         FieldInfo fi = this.fieldInfos.add(fieldName, field.isIndexed(), field.isTermVectorStored(), field.isStorePositionWithTermVector(), field.isStoreOffsetWithTermVector(), field.getOmitNorms(), false, field.getOmitTermFreqAndPositions());
/*     */ 
/* 197 */         fp = new DocFieldProcessorPerField(this, fi);
/* 198 */         fp.next = this.fieldHash[hashPos];
/* 199 */         this.fieldHash[hashPos] = fp;
/* 200 */         this.totalFieldCount += 1;
/*     */ 
/* 202 */         if (this.totalFieldCount >= this.fieldHash.length / 2)
/* 203 */           rehash();
/*     */       } else {
/* 205 */         fp.fieldInfo.update(field.isIndexed(), field.isTermVectorStored(), field.isStorePositionWithTermVector(), field.isStoreOffsetWithTermVector(), field.getOmitNorms(), false, field.getOmitTermFreqAndPositions());
/*     */       }
/*     */ 
/* 209 */       if (thisFieldGen != fp.lastGen)
/*     */       {
/* 212 */         fp.fieldCount = 0;
/*     */ 
/* 214 */         if (this.fieldCount == this.fields.length) {
/* 215 */           int newSize = this.fields.length * 2;
/* 216 */           DocFieldProcessorPerField[] newArray = new DocFieldProcessorPerField[newSize];
/* 217 */           System.arraycopy(this.fields, 0, newArray, 0, this.fieldCount);
/* 218 */           this.fields = newArray;
/*     */         }
/*     */ 
/* 221 */         this.fields[(this.fieldCount++)] = fp;
/* 222 */         fp.lastGen = thisFieldGen;
/*     */       }
/*     */ 
/* 225 */       if (fp.fieldCount == fp.fields.length) {
/* 226 */         Fieldable[] newArray = new Fieldable[fp.fields.length * 2];
/* 227 */         System.arraycopy(fp.fields, 0, newArray, 0, fp.fieldCount);
/* 228 */         fp.fields = newArray;
/*     */       }
/*     */ 
/* 231 */       fp.fields[(fp.fieldCount++)] = field;
/* 232 */       if (field.isStored()) {
/* 233 */         this.fieldsWriter.addField(field, fp.fieldInfo);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 243 */     quickSort(this.fields, 0, this.fieldCount - 1);
/*     */ 
/* 245 */     for (int i = 0; i < this.fieldCount; i++) {
/* 246 */       this.fields[i].consumer.processFields(this.fields[i].fields, this.fields[i].fieldCount);
/*     */     }
/* 248 */     if ((this.docState.maxTermPrefix != null) && (this.docState.infoStream != null)) {
/* 249 */       this.docState.infoStream.println("WARNING: document contains at least one immense term (longer than the max length 16383), all of which were skipped.  Please correct the analyzer to not produce such terms.  The prefix of the first immense term is: '" + this.docState.maxTermPrefix + "...'");
/* 250 */       this.docState.maxTermPrefix = null;
/*     */     }
/*     */ 
/* 253 */     DocumentsWriter.DocWriter one = this.fieldsWriter.finishDocument();
/* 254 */     DocumentsWriter.DocWriter two = this.consumer.finishDocument();
/* 255 */     if (one == null)
/* 256 */       return two;
/* 257 */     if (two == null) {
/* 258 */       return one;
/*     */     }
/* 260 */     PerDoc both = getPerDoc();
/* 261 */     both.docID = this.docState.docID;
/* 262 */     assert (one.docID == this.docState.docID);
/* 263 */     assert (two.docID == this.docState.docID);
/* 264 */     both.one = one;
/* 265 */     both.two = two;
/* 266 */     return both;
/*     */   }
/*     */ 
/*     */   void quickSort(DocFieldProcessorPerField[] array, int lo, int hi)
/*     */   {
/* 271 */     if (lo >= hi)
/* 272 */       return;
/* 273 */     if (hi == 1 + lo) {
/* 274 */       if (array[lo].fieldInfo.name.compareTo(array[hi].fieldInfo.name) > 0) {
/* 275 */         DocFieldProcessorPerField tmp = array[lo];
/* 276 */         array[lo] = array[hi];
/* 277 */         array[hi] = tmp;
/*     */       }
/* 279 */       return;
/*     */     }
/*     */ 
/* 282 */     int mid = lo + hi >>> 1;
/*     */ 
/* 284 */     if (array[lo].fieldInfo.name.compareTo(array[mid].fieldInfo.name) > 0) {
/* 285 */       DocFieldProcessorPerField tmp = array[lo];
/* 286 */       array[lo] = array[mid];
/* 287 */       array[mid] = tmp;
/*     */     }
/*     */ 
/* 290 */     if (array[mid].fieldInfo.name.compareTo(array[hi].fieldInfo.name) > 0) {
/* 291 */       DocFieldProcessorPerField tmp = array[mid];
/* 292 */       array[mid] = array[hi];
/* 293 */       array[hi] = tmp;
/*     */ 
/* 295 */       if (array[lo].fieldInfo.name.compareTo(array[mid].fieldInfo.name) > 0) {
/* 296 */         DocFieldProcessorPerField tmp2 = array[lo];
/* 297 */         array[lo] = array[mid];
/* 298 */         array[mid] = tmp2;
/*     */       }
/*     */     }
/*     */ 
/* 302 */     int left = lo + 1;
/* 303 */     int right = hi - 1;
/*     */ 
/* 305 */     if (left >= right) {
/* 306 */       return;
/*     */     }
/* 308 */     DocFieldProcessorPerField partition = array[mid];
/*     */     while (true)
/*     */     {
/* 311 */       if (array[right].fieldInfo.name.compareTo(partition.fieldInfo.name) > 0) {
/* 312 */         right--;
/*     */       } else {
/* 314 */         while ((left < right) && (array[left].fieldInfo.name.compareTo(partition.fieldInfo.name) <= 0)) {
/* 315 */           left++;
/*     */         }
/* 317 */         if (left >= right) break;
/* 318 */         DocFieldProcessorPerField tmp = array[left];
/* 319 */         array[left] = array[right];
/* 320 */         array[right] = tmp;
/* 321 */         right--;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 327 */     quickSort(array, lo, left);
/* 328 */     quickSort(array, left + 1, hi);
/*     */   }
/*     */ 
/*     */   synchronized PerDoc getPerDoc()
/*     */   {
/* 336 */     if (this.freeCount == 0) {
/* 337 */       this.allocCount += 1;
/* 338 */       if (this.allocCount > this.docFreeList.length)
/*     */       {
/* 342 */         assert (this.allocCount == 1 + this.docFreeList.length);
/* 343 */         this.docFreeList = new PerDoc[ArrayUtil.getNextSize(this.allocCount)];
/*     */       }
/* 345 */       return new PerDoc();
/*     */     }
/* 347 */     return this.docFreeList[(--this.freeCount)];
/*     */   }
/*     */ 
/*     */   synchronized void freePerDoc(PerDoc perDoc) {
/* 351 */     assert (this.freeCount < this.docFreeList.length);
/* 352 */     this.docFreeList[(this.freeCount++)] = perDoc;
/*     */   }
/*     */   class PerDoc extends DocumentsWriter.DocWriter {
/*     */     DocumentsWriter.DocWriter one;
/*     */     DocumentsWriter.DocWriter two;
/*     */ 
/*     */     PerDoc() {
/*     */     }
/*     */     public long sizeInBytes() {
/* 362 */       return this.one.sizeInBytes() + this.two.sizeInBytes();
/*     */     }
/*     */ 
/*     */     public void finish() throws IOException
/*     */     {
/*     */       try {
/*     */         try {
/* 369 */           this.one.finish();
/*     */         } finally {
/* 371 */           this.two.finish();
/*     */         }
/*     */       } finally {
/* 374 */         DocFieldProcessorPerThread.this.freePerDoc(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void abort()
/*     */     {
/*     */       try {
/*     */         try {
/* 382 */           this.one.abort();
/*     */         } finally {
/* 384 */           this.two.abort();
/*     */         }
/*     */       } finally {
/* 387 */         DocFieldProcessorPerThread.this.freePerDoc(this);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldProcessorPerThread
 * JD-Core Version:    0.6.2
 */