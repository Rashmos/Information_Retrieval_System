package org.apache.lucene.index;

import java.io.IOException;
import org.apache.lucene.document.Fieldable;

abstract class InvertedDocConsumerPerField
{
  abstract boolean start(Fieldable[] paramArrayOfFieldable, int paramInt)
    throws IOException;

  abstract void start(Fieldable paramFieldable);

  abstract void add()
    throws IOException;

  abstract void finish()
    throws IOException;

  abstract void abort();
}

/* Location:           /Users/Rashmi/Downloads/set_rp2614/rashmi.jar
 * Qualified Name:     org.apache.lucene.index.InvertedDocConsumerPerField
 * JD-Core Version:    0.6.2
 */