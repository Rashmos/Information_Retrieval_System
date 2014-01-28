package org.apache.lucene.search;

import java.io.IOException;
import java.util.BitSet;
import org.apache.lucene.index.IndexReader;

public abstract class Filter
{
  public abstract BitSet bits(IndexReader paramIndexReader)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.Filter
 * JD-Core Version:    0.6.2
 */