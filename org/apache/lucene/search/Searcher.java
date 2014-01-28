package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;

public abstract class Searcher
{
  public final Hits search(Query paramQuery)
    throws IOException
  {
    return search(paramQuery, (Filter)null);
  }

  public Hits search(Query paramQuery, Filter paramFilter)
    throws IOException
  {
    return new Hits(this, paramQuery, paramFilter);
  }

  public void search(Query paramQuery, HitCollector paramHitCollector)
    throws IOException
  {
    search(paramQuery, (Filter)null, paramHitCollector);
  }

  public abstract void search(Query paramQuery, Filter paramFilter, HitCollector paramHitCollector)
    throws IOException;

  public abstract void close()
    throws IOException;

  abstract int docFreq(Term paramTerm)
    throws IOException;

  abstract int maxDoc()
    throws IOException;

  abstract TopDocs search(Query paramQuery, Filter paramFilter, int paramInt)
    throws IOException;

  public abstract Document doc(int paramInt)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Searcher
 * JD-Core Version:    0.6.2
 */