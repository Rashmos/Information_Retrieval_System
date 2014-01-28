package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public final class FuzzyQuery extends MultiTermQuery
{
  private Term fuzzyTerm;

  public FuzzyQuery(Term paramTerm)
  {
    super(paramTerm);
    this.fuzzyTerm = paramTerm;
  }

  final void prepare(IndexReader paramIndexReader)
  {
    try
    {
      setEnum(new FuzzyTermEnum(paramIndexReader, this.fuzzyTerm));
    }
    catch (IOException localIOException)
    {
    }
  }

  public String toString(String paramString)
  {
    return super.toString(paramString) + '~';
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FuzzyQuery
 * JD-Core Version:    0.6.2
 */