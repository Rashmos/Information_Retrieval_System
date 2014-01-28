package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.document.Fieldable;

abstract class DocFieldConsumerPerField
{
  abstract void processFields(Fieldable[] paramArrayOfFieldable, int paramInt)
    throws IOException;

  abstract void abort();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.DocFieldConsumerPerField
 * JD-Core Version:    0.6.2
 */