/*    */ package org.apache.lucene.index;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.apache.lucene.search.Similarity;
/*    */ import org.apache.lucene.store.Directory;
/*    */ import org.apache.lucene.store.IndexOutput;
/*    */ 
/*    */ final class NormsWriter extends InvertedDocEndConsumer
/*    */ {
/* 40 */   private static final byte defaultNorm = Similarity.encodeNorm(1.0F);
/*    */   private FieldInfos fieldInfos;
/*    */ 
/*    */   public InvertedDocEndConsumerPerThread addThread(DocInverterPerThread docInverterPerThread)
/*    */   {
/* 44 */     return new NormsWriterPerThread(docInverterPerThread, this);
/*    */   }
/*    */ 
/*    */   public void abort()
/*    */   {
/*    */   }
/*    */ 
/*    */   void files(Collection<String> files) {
/*    */   }
/*    */ 
/*    */   void setFieldInfos(FieldInfos fieldInfos) {
/* 55 */     this.fieldInfos = fieldInfos;
/*    */   }
/*    */ 
/*    */   public void flush(Map<InvertedDocEndConsumerPerThread, Collection<InvertedDocEndConsumerPerField>> threadsAndFields, SegmentWriteState state)
/*    */     throws IOException
/*    */   {
/* 63 */     Map byField = new HashMap();
/*    */ 
/* 69 */     for (Map.Entry entry : threadsAndFields.entrySet()) {
/* 70 */       Collection fields = (Collection)entry.getValue();
/* 71 */       Iterator fieldsIt = fields.iterator();
/*    */ 
/* 73 */       while (fieldsIt.hasNext()) {
/* 74 */         NormsWriterPerField perField = (NormsWriterPerField)fieldsIt.next();
/*    */ 
/* 76 */         if (perField.upto > 0)
/*    */         {
/* 78 */           List l = (List)byField.get(perField.fieldInfo);
/* 79 */           if (l == null) {
/* 80 */             l = new ArrayList();
/* 81 */             byField.put(perField.fieldInfo, l);
/*    */           }
/* 83 */           l.add(perField);
/*    */         }
/*    */         else
/*    */         {
/* 87 */           fieldsIt.remove();
/*    */         }
/*    */       }
/*    */     }
/* 91 */     String normsFileName = state.segmentName + "." + "nrm";
/* 92 */     state.flushedFiles.add(normsFileName);
/* 93 */     IndexOutput normsOut = state.directory.createOutput(normsFileName);
/*    */     try
/*    */     {
/* 96 */       normsOut.writeBytes(SegmentMerger.NORMS_HEADER, 0, SegmentMerger.NORMS_HEADER.length);
/*    */ 
/* 98 */       int numField = this.fieldInfos.size();
/*    */ 
/* 100 */       int normCount = 0;
/*    */ 
/* 102 */       for (int fieldNumber = 0; fieldNumber < numField; fieldNumber++)
/*    */       {
/* 104 */         FieldInfo fieldInfo = this.fieldInfos.fieldInfo(fieldNumber);
/*    */ 
/* 106 */         List toMerge = (List)byField.get(fieldInfo);
/* 107 */         int upto = 0;
/* 108 */         if (toMerge != null)
/*    */         {
/* 110 */           int numFields = toMerge.size();
/*    */ 
/* 112 */           normCount++;
/*    */ 
/* 114 */           NormsWriterPerField[] fields = new NormsWriterPerField[numFields];
/* 115 */           int[] uptos = new int[numFields];
/*    */ 
/* 117 */           for (int j = 0; j < numFields; j++) {
/* 118 */             fields[j] = ((NormsWriterPerField)toMerge.get(j));
/*    */           }
/* 120 */           int numLeft = numFields;
/*    */ 
/* 122 */           while (numLeft > 0)
/*    */           {
/* 124 */             assert (uptos[0] < fields[0].docIDs.length) : (" uptos[0]=" + uptos[0] + " len=" + fields[0].docIDs.length);
/*    */ 
/* 126 */             int minLoc = 0;
/* 127 */             int minDocID = fields[0].docIDs[uptos[0]];
/*    */ 
/* 129 */             for (int j = 1; j < numLeft; j++) {
/* 130 */               int docID = fields[j].docIDs[uptos[j]];
/* 131 */               if (docID < minDocID) {
/* 132 */                 minDocID = docID;
/* 133 */                 minLoc = j;
/*    */               }
/*    */             }
/*    */ 
/* 137 */             assert (minDocID < state.numDocs);
/*    */ 
/* 140 */             for (; upto < minDocID; upto++) {
/* 141 */               normsOut.writeByte(defaultNorm);
/*    */             }
/* 143 */             normsOut.writeByte(fields[minLoc].norms[uptos[minLoc]]);
/* 144 */             uptos[minLoc] += 1;
/* 145 */             upto++;
/*    */ 
/* 147 */             if (uptos[minLoc] == fields[minLoc].upto) {
/* 148 */               fields[minLoc].reset();
/* 149 */               if (minLoc != numLeft - 1) {
/* 150 */                 fields[minLoc] = fields[(numLeft - 1)];
/* 151 */                 uptos[minLoc] = uptos[(numLeft - 1)];
/*    */               }
/* 153 */               numLeft--;
/*    */             }
/*    */ 
/*    */           }
/*    */ 
/* 158 */           for (; upto < state.numDocs; upto++)
/* 159 */             normsOut.writeByte(defaultNorm);
/* 160 */         } else if ((fieldInfo.isIndexed) && (!fieldInfo.omitNorms)) {
/* 161 */           normCount++;
/*    */ 
/* 163 */           for (; upto < state.numDocs; upto++) {
/* 164 */             normsOut.writeByte(defaultNorm);
/*    */           }
/*    */         }
/* 167 */         assert (4 + normCount * state.numDocs == normsOut.getFilePointer()) : (".nrm file size mismatch: expected=" + (4 + normCount * state.numDocs) + " actual=" + normsOut.getFilePointer());
/*    */       }
/*    */     }
/*    */     finally {
/* 171 */       normsOut.close();
/*    */     }
/*    */   }
/*    */ 
/*    */   void closeDocStore(SegmentWriteState state)
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.NormsWriter
 * JD-Core Version:    0.6.2
 */