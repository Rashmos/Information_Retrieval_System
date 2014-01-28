package org.apache.lucene.search;

public final class BooleanClause
{
  public Query query;
  public boolean required = false;
  public boolean prohibited = false;

  public BooleanClause(Query paramQuery, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.query = paramQuery;
    this.required = paramBoolean1;
    this.prohibited = paramBoolean2;
  }
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.BooleanClause
 * JD-Core Version:    0.6.2
 */