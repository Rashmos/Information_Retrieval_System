package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

public final class RangeQuery extends Query
{
  private Term lowerTerm;
  private Term upperTerm;
  private boolean inclusive;
  private IndexReader reader;
  private BooleanQuery query;

  public RangeQuery(Term paramTerm1, Term paramTerm2, boolean paramBoolean)
  {
    if ((paramTerm1 == null) && (paramTerm2 == null))
      throw new IllegalArgumentException("At least one term must be non-null");
    if ((paramTerm1 != null) && (paramTerm2 != null) && (paramTerm1.field() != paramTerm2.field()))
      throw new IllegalArgumentException("Both terms must be for the same field");
    this.lowerTerm = paramTerm1;
    this.upperTerm = paramTerm2;
    this.inclusive = paramBoolean;
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
      if (this.lowerTerm == null)
        this.lowerTerm = new Term(getField(), "");
      TermEnum localTermEnum = this.reader.terms(this.lowerTerm);
      try
      {
        String str = null;
        int i = 0;
        if (!this.inclusive)
        {
          if (this.lowerTerm != null)
          {
            str = this.lowerTerm.text();
            i = 1;
          }
          if (this.upperTerm != null)
          {
            localObject1 = this.reader.terms(this.upperTerm);
            this.upperTerm = ((TermEnum)localObject1).term();
          }
        }
        Object localObject1 = getField();
        do
        {
          Term localTerm = localTermEnum.term();
          if ((localTerm == null) || (localTerm.field() != localObject1))
            break;
          if ((i == 0) || (localTerm.text().compareTo(str) > 0))
          {
            i = 0;
            if (this.upperTerm != null)
            {
              int j = this.upperTerm.compareTo(localTerm);
              if ((j < 0) || ((!this.inclusive) && (j == 0)))
                break;
            }
            TermQuery localTermQuery = new TermQuery(localTerm);
            localTermQuery.setBoost(this.boost);
            localBooleanQuery.add(localTermQuery, false, false);
          }
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

  private String getField()
  {
    return this.lowerTerm != null ? this.lowerTerm.field() : this.upperTerm.field();
  }

  public String toString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (!getField().equals(paramString))
    {
      localStringBuffer.append(getField());
      localStringBuffer.append(":");
    }
    localStringBuffer.append(this.inclusive ? "[" : "{");
    localStringBuffer.append(this.lowerTerm != null ? this.lowerTerm.text() : "null");
    localStringBuffer.append("-");
    localStringBuffer.append(this.upperTerm != null ? this.upperTerm.text() : "null");
    localStringBuffer.append(this.inclusive ? "]" : "}");
    if (this.boost != 1.0F)
    {
      localStringBuffer.append("^");
      localStringBuffer.append(Float.toString(this.boost));
    }
    return localStringBuffer.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.RangeQuery
 * JD-Core Version:    0.6.2
 */