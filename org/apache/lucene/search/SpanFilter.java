package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;

public abstract class SpanFilter extends Filter
{
  public abstract SpanFilterResult bitSpans(IndexReader paramIndexReader)
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.search.SpanFilter
 * JD-Core Version:    0.6.2
 */