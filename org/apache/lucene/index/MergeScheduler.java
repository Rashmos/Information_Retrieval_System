package org.apache.lucene.index;

import java.io.IOException;

public abstract class MergeScheduler
{
  abstract void merge(IndexWriter paramIndexWriter)
    throws CorruptIndexException, IOException;

  abstract void close()
    throws CorruptIndexException, IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.MergeScheduler
 * JD-Core Version:    0.6.2
 */