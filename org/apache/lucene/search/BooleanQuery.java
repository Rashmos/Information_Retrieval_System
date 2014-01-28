package org.apache.lucene.search;

import java.io.IOException;
import java.util.Vector;
import org.apache.lucene.index.IndexReader;

public final class BooleanQuery extends Query
{
  private Vector clauses = new Vector();

  public final void add(Query paramQuery, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.clauses.addElement(new BooleanClause(paramQuery, paramBoolean1, paramBoolean2));
  }

  public final void add(BooleanClause paramBooleanClause)
  {
    this.clauses.addElement(paramBooleanClause);
  }

  void prepare(IndexReader paramIndexReader)
  {
    for (int i = 0; i < this.clauses.size(); i++)
    {
      BooleanClause localBooleanClause = (BooleanClause)this.clauses.elementAt(i);
      localBooleanClause.query.prepare(paramIndexReader);
    }
  }

  final float sumOfSquaredWeights(Searcher paramSearcher)
    throws IOException
  {
    float f = 0.0F;
    for (int i = 0; i < this.clauses.size(); i++)
    {
      BooleanClause localBooleanClause = (BooleanClause)this.clauses.elementAt(i);
      if (!localBooleanClause.prohibited)
        f += localBooleanClause.query.sumOfSquaredWeights(paramSearcher);
    }
    return f;
  }

  final void normalize(float paramFloat)
  {
    for (int i = 0; i < this.clauses.size(); i++)
    {
      BooleanClause localBooleanClause = (BooleanClause)this.clauses.elementAt(i);
      if (!localBooleanClause.prohibited)
        localBooleanClause.query.normalize(paramFloat);
    }
  }

  final Scorer scorer(IndexReader paramIndexReader)
    throws IOException
  {
    if (this.clauses.size() == 1)
    {
      localObject = (BooleanClause)this.clauses.elementAt(0);
      if (!((BooleanClause)localObject).prohibited)
        return ((BooleanClause)localObject).query.scorer(paramIndexReader);
    }
    Object localObject = new BooleanScorer();
    for (int i = 0; i < this.clauses.size(); i++)
    {
      BooleanClause localBooleanClause = (BooleanClause)this.clauses.elementAt(i);
      Scorer localScorer = localBooleanClause.query.scorer(paramIndexReader);
      if (localScorer != null)
        ((BooleanScorer)localObject).add(localScorer, localBooleanClause.required, localBooleanClause.prohibited);
      else if (localBooleanClause.required)
        return null;
    }
    return localObject;
  }

  public String toString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < this.clauses.size(); i++)
    {
      BooleanClause localBooleanClause = (BooleanClause)this.clauses.elementAt(i);
      if (localBooleanClause.prohibited)
        localStringBuffer.append("-");
      else if (localBooleanClause.required)
        localStringBuffer.append("+");
      Query localQuery = localBooleanClause.query;
      if ((localQuery instanceof BooleanQuery))
      {
        BooleanQuery localBooleanQuery = (BooleanQuery)localQuery;
        localStringBuffer.append("(");
        localStringBuffer.append(localBooleanClause.query.toString(paramString));
        localStringBuffer.append(")");
      }
      else
      {
        localStringBuffer.append(localBooleanClause.query.toString(paramString));
      }
      if (i != this.clauses.size() - 1)
        localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanQuery
 * JD-Core Version:    0.6.2
 */