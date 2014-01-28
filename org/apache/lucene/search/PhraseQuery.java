package org.apache.lucene.search;

import java.io.IOException;
import java.util.Vector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermPositions;

public final class PhraseQuery extends Query
{
  private String field;
  private Vector terms = new Vector();
  private float idf = 0.0F;
  private float weight = 0.0F;
  private int slop = 0;

  public final void setSlop(int paramInt)
  {
    this.slop = paramInt;
  }

  public final int getSlop()
  {
    return this.slop;
  }

  public final void add(Term paramTerm)
  {
    if (this.terms.size() == 0)
      this.field = paramTerm.field();
    else if (paramTerm.field() != this.field)
      throw new IllegalArgumentException("All phrase terms must be in the same field: " + paramTerm);
    this.terms.addElement(paramTerm);
  }

  final float sumOfSquaredWeights(Searcher paramSearcher)
    throws IOException
  {
    for (int i = 0; i < this.terms.size(); i++)
      this.idf += Similarity.idf((Term)this.terms.elementAt(i), paramSearcher);
    this.weight = (this.idf * this.boost);
    return this.weight * this.weight;
  }

  final void normalize(float paramFloat)
  {
    this.weight *= paramFloat;
    this.weight *= this.idf;
  }

  final Scorer scorer(IndexReader paramIndexReader)
    throws IOException
  {
    if (this.terms.size() == 0)
      return null;
    if (this.terms.size() == 1)
    {
      localObject = (Term)this.terms.elementAt(0);
      TermDocs localTermDocs = paramIndexReader.termDocs((Term)localObject);
      if (localTermDocs == null)
        return null;
      return new TermScorer(localTermDocs, paramIndexReader.norms(((Term)localObject).field()), this.weight);
    }
    Object localObject = new TermPositions[this.terms.size()];
    for (int i = 0; i < this.terms.size(); i++)
    {
      TermPositions localTermPositions = paramIndexReader.termPositions((Term)this.terms.elementAt(i));
      if (localTermPositions == null)
        return null;
      localObject[i] = localTermPositions;
    }
    if (this.slop == 0)
      return new ExactPhraseScorer((TermPositions[])localObject, paramIndexReader.norms(this.field), this.weight);
    return new SloppyPhraseScorer((TermPositions[])localObject, this.slop, paramIndexReader.norms(this.field), this.weight);
  }

  public final String toString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (!this.field.equals(paramString))
    {
      localStringBuffer.append(this.field);
      localStringBuffer.append(":");
    }
    localStringBuffer.append("\"");
    for (int i = 0; i < this.terms.size(); i++)
    {
      localStringBuffer.append(((Term)this.terms.elementAt(i)).text());
      if (i != this.terms.size() - 1)
        localStringBuffer.append(" ");
    }
    localStringBuffer.append("\"");
    if (this.slop != 0)
    {
      localStringBuffer.append("~");
      localStringBuffer.append(this.slop);
    }
    if (this.boost != 1.0F)
    {
      localStringBuffer.append("^");
      localStringBuffer.append(Float.toString(this.boost));
    }
    return localStringBuffer.toString();
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.PhraseQuery
 * JD-Core Version:    0.6.2
 */