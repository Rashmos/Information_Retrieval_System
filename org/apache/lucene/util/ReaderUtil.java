/*     */ package org.apache.lucene.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ 
/*     */ public class ReaderUtil
/*     */ {
/*     */   public static void gatherSubReaders(List<IndexReader> allSubReaders, IndexReader reader)
/*     */   {
/*  38 */     IndexReader[] subReaders = reader.getSequentialSubReaders();
/*  39 */     if (subReaders == null)
/*     */     {
/*  41 */       allSubReaders.add(reader);
/*     */     }
/*  43 */     else for (int i = 0; i < subReaders.length; i++)
/*  44 */         gatherSubReaders(allSubReaders, subReaders[i]);
/*     */   }
/*     */ 
/*     */   public static IndexReader subReader(int doc, IndexReader reader)
/*     */   {
/*  57 */     List subReadersList = new ArrayList();
/*  58 */     gatherSubReaders(subReadersList, reader);
/*  59 */     IndexReader[] subReaders = (IndexReader[])subReadersList.toArray(new IndexReader[subReadersList.size()]);
/*     */ 
/*  61 */     int[] docStarts = new int[subReaders.length];
/*  62 */     int maxDoc = 0;
/*  63 */     for (int i = 0; i < subReaders.length; i++) {
/*  64 */       docStarts[i] = maxDoc;
/*  65 */       maxDoc += subReaders[i].maxDoc();
/*     */     }
/*  67 */     return subReaders[subIndex(doc, docStarts)];
/*     */   }
/*     */ 
/*     */   public static IndexReader subReader(IndexReader reader, int subIndex)
/*     */   {
/*  78 */     List subReadersList = new ArrayList();
/*  79 */     gatherSubReaders(subReadersList, reader);
/*  80 */     IndexReader[] subReaders = (IndexReader[])subReadersList.toArray(new IndexReader[subReadersList.size()]);
/*     */ 
/*  82 */     return subReaders[subIndex];
/*     */   }
/*     */ 
/*     */   public static int subIndex(int n, int[] docStarts)
/*     */   {
/*  92 */     int size = docStarts.length;
/*  93 */     int lo = 0;
/*  94 */     int hi = size - 1;
/*  95 */     while (hi >= lo) {
/*  96 */       int mid = lo + hi >>> 1;
/*  97 */       int midValue = docStarts[mid];
/*  98 */       if (n < midValue) {
/*  99 */         hi = mid - 1;
/* 100 */       } else if (n > midValue) {
/* 101 */         lo = mid + 1;
/*     */       } else {
/* 103 */         while ((mid + 1 < size) && (docStarts[(mid + 1)] == midValue)) {
/* 104 */           mid++;
/*     */         }
/* 106 */         return mid;
/*     */       }
/*     */     }
/* 109 */     return hi;
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.util.ReaderUtil
 * JD-Core Version:    0.6.2
 */