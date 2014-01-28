package org.apache.lucene.index;

import java.io.IOException;

abstract class FormatPostingsDocsConsumer
{
  abstract FormatPostingsPositionsConsumer addDoc(int paramInt1, int paramInt2)
    throws IOException;

  abstract void finish()
    throws IOException;
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.FormatPostingsDocsConsumer
 * JD-Core Version:    0.6.2
 */