/*     */ package org.apache.lucene.search;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CompletionService;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorCompletionService;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.lucene.index.IndexReader;
/*     */ import org.apache.lucene.index.Term;
/*     */ import org.apache.lucene.util.NamedThreadFactory;
/*     */ import org.apache.lucene.util.ThreadInterruptedException;
/*     */ 
/*     */ public class ParallelMultiSearcher extends MultiSearcher
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   private final Searchable[] searchables;
/*     */   private final int[] starts;
/*     */ 
/*     */   public ParallelMultiSearcher(Searchable[] searchables)
/*     */     throws IOException
/*     */   {
/*  51 */     super(searchables);
/*  52 */     this.searchables = searchables;
/*  53 */     this.starts = getStarts();
/*  54 */     this.executor = Executors.newCachedThreadPool(new NamedThreadFactory(getClass().getSimpleName()));
/*     */   }
/*     */ 
/*     */   public int docFreq(final Term term)
/*     */     throws IOException
/*     */   {
/*  63 */     ExecutionHelper runner = new ExecutionHelper(this.executor);
/*  64 */     for (int i = 0; i < this.searchables.length; i++) {
/*  65 */       final Searchable searchable = this.searchables[i];
/*  66 */       runner.submit(new Callable() {
/*     */         public Integer call() throws IOException {
/*  68 */           return Integer.valueOf(searchable.docFreq(term));
/*     */         }
/*     */       });
/*     */     }
/*  72 */     int docFreq = 0;
/*  73 */     for (Integer num : runner) {
/*  74 */       docFreq += num.intValue();
/*     */     }
/*  76 */     return docFreq;
/*     */   }
/*     */ 
/*     */   public TopDocs search(Weight weight, Filter filter, int nDocs)
/*     */     throws IOException
/*     */   {
/*  86 */     HitQueue hq = new HitQueue(nDocs, false);
/*  87 */     Lock lock = new ReentrantLock();
/*  88 */     ExecutionHelper runner = new ExecutionHelper(this.executor);
/*     */ 
/*  90 */     for (int i = 0; i < this.searchables.length; i++) {
/*  91 */       runner.submit(new MultiSearcher.MultiSearcherCallableNoSort(lock, this.searchables[i], weight, filter, nDocs, hq, i, this.starts));
/*     */     }
/*     */ 
/*  95 */     int totalHits = 0;
/*  96 */     float maxScore = (1.0F / -1.0F);
/*  97 */     for (TopDocs topDocs : runner) {
/*  98 */       totalHits += topDocs.totalHits;
/*  99 */       maxScore = Math.max(maxScore, topDocs.getMaxScore());
/*     */     }
/*     */ 
/* 102 */     ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];
/* 103 */     for (int i = hq.size() - 1; i >= 0; i--) {
/* 104 */       scoreDocs[i] = ((ScoreDoc)hq.pop());
/*     */     }
/* 106 */     return new TopDocs(totalHits, scoreDocs, maxScore);
/*     */   }
/*     */ 
/*     */   public TopFieldDocs search(Weight weight, Filter filter, int nDocs, Sort sort)
/*     */     throws IOException
/*     */   {
/* 116 */     if (sort == null) throw new NullPointerException();
/*     */ 
/* 118 */     FieldDocSortedHitQueue hq = new FieldDocSortedHitQueue(nDocs);
/* 119 */     Lock lock = new ReentrantLock();
/* 120 */     ExecutionHelper runner = new ExecutionHelper(this.executor);
/* 121 */     for (int i = 0; i < this.searchables.length; i++) {
/* 122 */       runner.submit(new MultiSearcher.MultiSearcherCallableWithSort(lock, this.searchables[i], weight, filter, nDocs, hq, sort, i, this.starts));
/*     */     }
/*     */ 
/* 125 */     int totalHits = 0;
/* 126 */     float maxScore = (1.0F / -1.0F);
/* 127 */     for (TopFieldDocs topFieldDocs : runner) {
/* 128 */       totalHits += topFieldDocs.totalHits;
/* 129 */       maxScore = Math.max(maxScore, topFieldDocs.getMaxScore());
/*     */     }
/* 131 */     ScoreDoc[] scoreDocs = new ScoreDoc[hq.size()];
/* 132 */     for (int i = hq.size() - 1; i >= 0; i--) {
/* 133 */       scoreDocs[i] = ((ScoreDoc)hq.pop());
/*     */     }
/* 135 */     return new TopFieldDocs(totalHits, scoreDocs, hq.getFields(), maxScore);
/*     */   }
/*     */ 
/*     */   public void search(Weight weight, Filter filter, final Collector collector)
/*     */     throws IOException
/*     */   {
/* 157 */     for (int i = 0; i < this.searchables.length; i++)
/*     */     {
/* 159 */       final int start = this.starts[i];
/*     */ 
/* 161 */       Collector hc = new Collector()
/*     */       {
/*     */         public void setScorer(Scorer scorer) throws IOException {
/* 164 */           collector.setScorer(scorer);
/*     */         }
/*     */ 
/*     */         public void collect(int doc) throws IOException
/*     */         {
/* 169 */           collector.collect(doc);
/*     */         }
/*     */ 
/*     */         public void setNextReader(IndexReader reader, int docBase) throws IOException
/*     */         {
/* 174 */           collector.setNextReader(reader, start + docBase);
/*     */         }
/*     */ 
/*     */         public boolean acceptsDocsOutOfOrder()
/*     */         {
/* 179 */           return collector.acceptsDocsOutOfOrder();
/*     */         }
/*     */       };
/* 183 */       this.searchables[i].search(weight, filter, hc);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/* 189 */     this.executor.shutdown();
/* 190 */     super.close();
/*     */   }
/*     */ 
/*     */   private static final class ExecutionHelper<T>
/*     */     implements Iterator<T>, Iterable<T>
/*     */   {
/*     */     private final CompletionService<T> service;
/*     */     private int numTasks;
/*     */ 
/*     */     ExecutionHelper(Executor executor)
/*     */     {
/* 205 */       this.service = new ExecutorCompletionService(executor);
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 209 */       return this.numTasks > 0;
/*     */     }
/*     */ 
/*     */     public void submit(Callable<T> task) {
/* 213 */       this.service.submit(task);
/* 214 */       this.numTasks += 1;
/*     */     }
/*     */ 
/*     */     public T next() {
/* 218 */       if (!hasNext())
/* 219 */         throw new NoSuchElementException();
/*     */       try {
/* 221 */         return this.service.take().get();
/*     */       } catch (InterruptedException e) {
/* 223 */         throw new ThreadInterruptedException(e);
/*     */       } catch (ExecutionException e) {
/* 225 */         throw new RuntimeException(e);
/*     */       } finally {
/* 227 */         this.numTasks -= 1;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 232 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     public Iterator<T> iterator()
/*     */     {
/* 237 */       return this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.ParallelMultiSearcher
 * JD-Core Version:    0.6.2
 */