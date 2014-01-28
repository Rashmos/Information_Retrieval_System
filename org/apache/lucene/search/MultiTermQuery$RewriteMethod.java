package org.apache.lucene.search;

import java.io.IOException;
import java.io.Serializable;
import org.apache.lucene.index.IndexReader;

public abstract class MultiTermQuery$RewriteMethod
  implements Serializable
{
  public abstract Query rewrite(IndexReader paramIndexReader, MultiTermQuery paramMultiTermQuery)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.MultiTermQuery.RewriteMethod
 * JD-Core Version:    0.6.2
 */