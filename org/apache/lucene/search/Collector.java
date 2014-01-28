package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;

public abstract class Collector
{
  public abstract void setScorer(Scorer paramScorer)
    throws IOException;

  public abstract void collect(int paramInt)
    throws IOException;

  public abstract void setNextReader(IndexReader paramIndexReader, int paramInt)
    throws IOException;

  public abstract boolean acceptsDocsOutOfOrder();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Collector
 * JD-Core Version:    0.6.2
 */