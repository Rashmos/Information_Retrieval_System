package org.apache.lucene.search;

import java.io.IOException;
import java.io.Serializable;
import org.apache.lucene.index.IndexReader;

public abstract class Query
  implements Serializable
{
  protected float boost = 1.0F;

  abstract float sumOfSquaredWeights(Searcher paramSearcher)
    throws IOException;

  abstract void normalize(float paramFloat);

  abstract Scorer scorer(IndexReader paramIndexReader)
    throws IOException;

  void prepare(IndexReader paramIndexReader)
  {
  }

  static Scorer scorer(Query paramQuery, Searcher paramSearcher, IndexReader paramIndexReader)
    throws IOException
  {
    paramQuery.prepare(paramIndexReader);
    float f1 = paramQuery.sumOfSquaredWeights(paramSearcher);
    float f2 = 1.0F / (float)Math.sqrt(f1);
    paramQuery.normalize(f2);
    return paramQuery.scorer(paramIndexReader);
  }

  public void setBoost(float paramFloat)
  {
    this.boost = paramFloat;
  }

  public float getBoost()
  {
    return this.boost;
  }

  public abstract String toString(String paramString);
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Query
 * JD-Core Version:    0.6.2
 */