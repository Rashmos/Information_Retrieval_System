package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;

public final class TermQuery extends Query
{
  private Term term;
  private float idf = 0.0F;
  private float weight = 0.0F;

  public TermQuery(Term paramTerm)
  {
    this.term = paramTerm;
  }

  final float sumOfSquaredWeights(Searcher paramSearcher)
    throws IOException
  {
    this.idf = Similarity.idf(this.term, paramSearcher);
    this.weight = (this.idf * this.boost);
    return this.weight * this.weight;
  }

  final void normalize(float paramFloat)
  {
    this.weight *= paramFloat;
    this.weight *= this.idf;
  }

  Scorer scorer(IndexReader paramIndexReader)
    throws IOException
  {
    TermDocs localTermDocs = paramIndexReader.termDocs(this.term);
    if (localTermDocs == null)
      return null;
    return new TermScorer(localTermDocs, paramIndexReader.norms(this.term.field()), this.weight);
  }

  public String toString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (!this.term.field().equals(paramString))
    {
      localStringBuffer.append(this.term.field());
      localStringBuffer.append(":");
    }
    localStringBuffer.append(this.term.text());
    if (this.boost != 1.0F)
    {
      localStringBuffer.append("^");
      localStringBuffer.append(Float.toString(this.boost));
    }
    return localStringBuffer.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.TermQuery
 * JD-Core Version:    0.6.2
 */