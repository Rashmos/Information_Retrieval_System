package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public final class WildcardQuery extends MultiTermQuery
{
  private Term wildcardTerm;

  public WildcardQuery(Term paramTerm)
  {
    super(paramTerm);
    this.wildcardTerm = paramTerm;
  }

  final void prepare(IndexReader paramIndexReader)
  {
    try
    {
      setEnum(new WildcardTermEnum(paramIndexReader, this.wildcardTerm));
    }
    catch (IOException localIOException)
    {
    }
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.WildcardQuery
 * JD-Core Version:    0.6.2
 */