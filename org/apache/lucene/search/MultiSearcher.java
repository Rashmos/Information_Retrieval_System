package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.PriorityQueue;

public final class MultiSearcher extends Searcher
{
  private Searcher[] searchers;
  private int[] starts;
  private int maxDoc = 0;

  public MultiSearcher(Searcher[] paramArrayOfSearcher)
    throws IOException
  {
    this.searchers = paramArrayOfSearcher;
    this.starts = new int[paramArrayOfSearcher.length + 1];
    for (int i = 0; i < paramArrayOfSearcher.length; i++)
    {
      this.starts[i] = this.maxDoc;
      this.maxDoc += paramArrayOfSearcher[i].maxDoc();
    }
    this.starts[paramArrayOfSearcher.length] = this.maxDoc;
  }

  public final void close()
    throws IOException
  {
    for (int i = 0; i < this.searchers.length; i++)
      this.searchers[i].close();
  }

  final int docFreq(Term paramTerm)
    throws IOException
  {
    int i = 0;
    for (int j = 0; j < this.searchers.length; j++)
      i += this.searchers[j].docFreq(paramTerm);
    return i;
  }

  public final Document doc(int paramInt)
    throws IOException
  {
    int i = searcherIndex(paramInt);
    return this.searchers[i].doc(paramInt - this.starts[i]);
  }

  public final int searcherIndex(int paramInt)
  {
    int i = 0;
    int j = this.searchers.length - 1;
    while (j >= i)
    {
      int k = i + j >> 1;
      int m = this.starts[k];
      if (paramInt < m)
        j = k - 1;
      else if (paramInt > m)
        i = k + 1;
      else
        return k;
    }
    return j;
  }

  final int maxDoc()
    throws IOException
  {
    return this.maxDoc;
  }

  final TopDocs search(Query paramQuery, Filter paramFilter, int paramInt)
    throws IOException
  {
    HitQueue localHitQueue = new HitQueue(paramInt);
    float f = 0.0F;
    int i = 0;
    for (int j = 0; j < this.searchers.length; j++)
    {
      localObject = this.searchers[j].search(paramQuery, paramFilter, paramInt);
      i += ((TopDocs)localObject).totalHits;
      ScoreDoc[] arrayOfScoreDoc = ((TopDocs)localObject).scoreDocs;
      for (int m = 0; m < arrayOfScoreDoc.length; m++)
      {
        ScoreDoc localScoreDoc = arrayOfScoreDoc[m];
        if (localScoreDoc.score < f)
          break;
        localScoreDoc.doc += this.starts[j];
        localHitQueue.put(localScoreDoc);
        if (localHitQueue.size() > paramInt)
        {
          localHitQueue.pop();
          f = ((ScoreDoc)localHitQueue.top()).score;
        }
      }
    }
    Object localObject = new ScoreDoc[localHitQueue.size()];
    for (int k = localHitQueue.size() - 1; k >= 0; k--)
      localObject[k] = ((ScoreDoc)localHitQueue.pop());
    return new TopDocs(i, (ScoreDoc[])localObject);
  }

  public final void search(Query paramQuery, Filter paramFilter, HitCollector paramHitCollector)
    throws IOException
  {
    for (int i = 0; i < this.searchers.length; i++)
    {
      int j = this.starts[i];
      this.searchers[i].search(paramQuery, paramFilter, new HitCollector()
      {
        private final HitCollector val$results;
        private final int val$start;

        public void collect(int paramAnonymousInt, float paramAnonymousFloat)
        {
          this.val$results.collect(paramAnonymousInt + this.val$start, paramAnonymousFloat);
        }
      });
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiSearcher
 * JD-Core Version:    0.6.2
 */