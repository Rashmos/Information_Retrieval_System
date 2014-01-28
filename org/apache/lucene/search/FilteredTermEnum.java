package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

public abstract class FilteredTermEnum extends TermEnum
{
  private Term currentTerm = null;
  private TermEnum actualEnum = null;

  public FilteredTermEnum(IndexReader paramIndexReader, Term paramTerm)
    throws IOException
  {
  }

  protected abstract boolean termCompare(Term paramTerm);

  protected abstract float difference();

  protected abstract boolean endEnum();

  protected void setEnum(TermEnum paramTermEnum)
    throws IOException
  {
    this.actualEnum = paramTermEnum;
    Term localTerm = paramTermEnum.term();
    if (termCompare(localTerm))
      this.currentTerm = localTerm;
    else
      next();
  }

  public int docFreq()
  {
    if (this.actualEnum == null)
      return -1;
    return this.actualEnum.docFreq();
  }

  public boolean next()
    throws IOException
  {
    if (this.actualEnum == null)
      return false;
    this.currentTerm = null;
    while (this.currentTerm == null)
    {
      if (endEnum())
        return false;
      if (this.actualEnum.next())
      {
        Term localTerm = this.actualEnum.term();
        if (termCompare(localTerm))
        {
          this.currentTerm = localTerm;
          return true;
        }
      }
      else
      {
        return false;
      }
    }
    this.currentTerm = null;
    return false;
  }

  public Term term()
  {
    return this.currentTerm;
  }

  public void close()
    throws IOException
  {
    this.actualEnum.close();
    this.currentTerm = null;
    this.actualEnum = null;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.FilteredTermEnum
 * JD-Core Version:    0.6.2
 */