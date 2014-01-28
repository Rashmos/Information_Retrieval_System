package org.apache.lucene.index;

import java.io.IOException;

abstract class DocConsumerPerThread
{
  abstract DocumentsWriter.DocWriter processDocument()
    throws IOException;

  abstract void abort();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocConsumerPerThread
 * JD-Core Version:    0.6.2
 */