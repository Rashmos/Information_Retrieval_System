package org.apache.lucene.search;

import java.io.IOException;
import java.util.BitSet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.PriorityQueue;

public final class IndexSearcher extends Searcher
{
  IndexReader reader;

  public IndexSearcher(String paramString)
    throws IOException
  {
    this(IndexReader.open(paramString));
  }

  public IndexSearcher(Directory paramDirectory)
    throws IOException
  {
    this(IndexReader.open(paramDirectory));
  }

  public IndexSearcher(IndexReader paramIndexReader)
  {
    this.reader = paramIndexReader;
  }

  public final void close()
    throws IOException
  {
    this.reader.close();
  }

  final int docFreq(Term paramTerm)
    throws IOException
  {
    return this.reader.docFreq(paramTerm);
  }

  public final Document doc(int paramInt)
    throws IOException
  {
    return this.reader.document(paramInt);
  }

  final int maxDoc()
    throws IOException
  {
    return this.reader.maxDoc();
  }

  final TopDocs search(Query paramQuery, Filter paramFilter, int paramInt)
    throws IOException
  {
    Scorer localScorer = Query.scorer(paramQuery, this, this.reader);
    if (localScorer == null)
      return new TopDocs(0, new ScoreDoc[0]);
    BitSet localBitSet = paramFilter != null ? paramFilter.bits(this.reader) : null;
    HitQueue localHitQueue = new HitQueue(paramInt);
    int[] arrayOfInt = new int[1];
    localScorer.score(new HitCollector()
    {
      private float minScore;
      private final BitSet val$bits;
      private final int[] val$totalHits;
      private final HitQueue val$hq;
      private final int val$nDocs;

      public final void collect(int paramAnonymousInt, float paramAnonymousFloat)
      {
        if ((paramAnonymousFloat > 0.0F) && ((this.val$bits == null) || (this.val$bits.get(paramAnonymousInt))))
        {
          this.val$totalHits[0] += 1;
          if (paramAnonymousFloat >= this.minScore)
          {
            this.val$hq.put(new ScoreDoc(paramAnonymousInt, paramAnonymousFloat));
            if (this.val$hq.size() > this.val$nDocs)
            {
              this.val$hq.pop();
              this.minScore = ((ScoreDoc)this.val$hq.top()).score;
            }
          }
        }
      }
    }
    , this.reader.maxDoc());
    ScoreDoc[] arrayOfScoreDoc = new ScoreDoc[localHitQueue.size()];
    for (int i = localHitQueue.size() - 1; i >= 0; i--)
      arrayOfScoreDoc[i] = ((ScoreDoc)localHitQueue.pop());
    return new TopDocs(arrayOfInt[0], arrayOfScoreDoc);
  }

  public final void search(Query paramQuery, Filter paramFilter, HitCollector paramHitCollector)
    throws IOException
  {
    Object localObject1 = paramHitCollector;
    if (paramFilter != null)
    {
      localObject2 = paramFilter.bits(this.reader);
      localObject1 = new HitCollector()
      {
        private final BitSet val$bits;
        private final HitCollector val$results;

        public final void collect(int paramAnonymousInt, float paramAnonymousFloat)
        {
          if (this.val$bits.get(paramAnonymousInt))
            this.val$results.collect(paramAnonymousInt, paramAnonymousFloat);
        }
      };
    }
    Object localObject2 = Query.scorer(paramQuery, this, this.reader);
    if (localObject2 == null)
      return;
    ((Scorer)localObject2).score((HitCollector)localObject1, this.reader.maxDoc());
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.IndexSearcher
 * JD-Core Version:    0.6.2
 */