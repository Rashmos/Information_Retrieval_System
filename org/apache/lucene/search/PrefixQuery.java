package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

public final class PrefixQuery extends Query
{
  private Term prefix;
  private IndexReader reader;
  private BooleanQuery query;

  public PrefixQuery(Term paramTerm)
  {
    this.prefix = paramTerm;
    this.reader = this.reader;
  }

  final void prepare(IndexReader paramIndexReader)
  {
    this.query = null;
    this.reader = paramIndexReader;
  }

  final float sumOfSquaredWeights(Searcher paramSearcher)
    throws IOException
  {
    return getQuery().sumOfSquaredWeights(paramSearcher);
  }

  void normalize(float paramFloat)
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

  Scorer scorer(IndexReader paramIndexReader)
    throws IOException
  {
    return getQuery().scorer(paramIndexReader);
  }

  private BooleanQuery getQuery()
    throws IOException
  {
    if (this.query == null)
    {
      BooleanQuery localBooleanQuery = new BooleanQuery();
      TermEnum localTermEnum = this.reader.terms(this.prefix);
      try
      {
        String str1 = this.prefix.text();
        String str2 = this.prefix.field();
        do
        {
          Term localTerm = localTermEnum.term();
          if ((localTerm == null) || (!localTerm.text().startsWith(str1)) || (localTerm.field() != str2))
            break;
          TermQuery localTermQuery = new TermQuery(localTerm);
          localTermQuery.setBoost(this.boost);
          localBooleanQuery.add(localTermQuery, false, false);
        }
        while (localTermEnum.next());
      }
      finally
      {
        localTermEnum.close();
      }
      this.query = localBooleanQuery;
    }
    return this.query;
  }

  public String toString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (!this.prefix.field().equals(paramString))
    {
      localStringBuffer.append(this.prefix.field());
      localStringBuffer.append(":");
    }
    localStringBuffer.append(this.prefix.text());
    localStringBuffer.append('*');
    if (this.boost != 1.0F)
    {
      localStringBuffer.append("^");
      localStringBuffer.append(Float.toString(this.boost));
    }
    return localStringBuffer.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.PrefixQuery
 * JD-Core Version:    0.6.2
 */