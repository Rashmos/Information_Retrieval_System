package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;

public class MultiTermQuery extends Query
{
  private Term term;
  private FilteredTermEnum jdField_enum;
  private IndexReader reader;
  private BooleanQuery query;
  private static boolean LUCENE_STYLE_TOSTRING = false;

  public MultiTermQuery(Term paramTerm)
  {
    this.term = paramTerm;
    this.query = this.query;
  }

  protected void setEnum(FilteredTermEnum paramFilteredTermEnum)
  {
    this.jdField_enum = paramFilteredTermEnum;
  }

  final float sumOfSquaredWeights(Searcher paramSearcher)
    throws IOException
  {
    return getQuery().sumOfSquaredWeights(paramSearcher);
  }

  final void normalize(float paramFloat)
  {
    try
    {
      getQuery().normalize(paramFloat);
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException(localIOException.toString());
    }
  }

  final Scorer scorer(IndexReader paramIndexReader)
    throws IOException
  {
    return getQuery().scorer(paramIndexReader);
  }

  private final BooleanQuery getQuery()
    throws IOException
  {
    if (this.query == null)
    {
      BooleanQuery localBooleanQuery = new BooleanQuery();
      try
      {
        do
        {
          Term localTerm = this.jdField_enum.term();
          if (localTerm != null)
          {
            TermQuery localTermQuery = new TermQuery(localTerm);
            localTermQuery.setBoost(this.boost * this.jdField_enum.difference());
            localBooleanQuery.add(localTermQuery, false, false);
          }
        }
        while (this.jdField_enum.next());
      }
      finally
      {
        this.jdField_enum.close();
      }
      this.query = localBooleanQuery;
    }
    return this.query;
  }

  public String toString(String paramString)
  {
    if (!LUCENE_STYLE_TOSTRING)
    {
      localObject = null;
      try
      {
        localObject = getQuery();
      }
      catch (Exception localException)
      {
      }
      if (localObject != null)
        return "(" + ((Query)localObject).toString(paramString) + ")";
    }
    Object localObject = new StringBuffer();
    if (!this.term.field().equals(paramString))
    {
      ((StringBuffer)localObject).append(this.term.field());
      ((StringBuffer)localObject).append(":");
    }
    ((StringBuffer)localObject).append(this.term.text());
    if (this.boost != 1.0F)
    {
      ((StringBuffer)localObject).append("^");
      ((StringBuffer)localObject).append(Float.toString(this.boost));
    }
    return ((StringBuffer)localObject).toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiTermQuery
 * JD-Core Version:    0.6.2
 */